from redis_service.redis_operations import set_status, get_status
from flask import Flask, jsonify, request

app = Flask(__name__)

# Endpoint untuk mengatur status ke Redis
@app.route('/set_status', methods=['POST'])
def set_user_status():
    data = request.json
    user_id = data['user_id']
    status = data['status']
    set_status(user_id, status)
    return jsonify({"message": "Status saved successfully"}), 200

# Endpoint untuk mendapatkan status dari Redis
@app.route('/get_status/<user_id>', methods=['GET'])
def get_user_status(user_id):
    status = get_status(user_id)
    return jsonify({"user_id": user_id, "status": status}), 200

if __name__ == "__main__":
    app.run(port=5001)  # Jalankan di port terpisah untuk memisahkan dari API utama
