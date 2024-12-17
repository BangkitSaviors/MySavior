from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
import numpy as np

app = Flask(__name__)

# Load model lokal
model = load_model('mobilenet.h5')

@app.route('/', methods=['GET'])
def home():
    """
    Endpoint untuk root URL. Mengembalikan pesan sambutan.
    """
    return jsonify({
        "message": "Welcome to the ML API!",
        "endpoints": {
            "/predict": "Use this endpoint to make predictions with POST requests."
        }
    })

@app.route('/predict', methods=['POST'])
def predict():
    """
    Endpoint untuk prediksi.
    Menerima JSON berisi data input dan mengembalikan prediksi dari model.
    """
    try:
        # Ambil data dari body request
        data = request.get_json()
        if not data or 'input' not in data:
            return jsonify({"error": "Invalid input format. Please provide 'input' key in JSON body."}), 400
        
        # Ubah input ke format numpy array
        input_array = np.array(data['input']).reshape(1, -1)  # Sesuaikan dimensi input dengan model Anda
        predictions = model.predict(input_array)
        
        return jsonify({'prediction': predictions.tolist()})
    except Exception as e:
        return jsonify({'error': f"An error occurred: {str(e)}"}), 400

if __name__ == '__main__':
    import os
    # Gunakan port yang diberikan oleh Cloud Run
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port)
