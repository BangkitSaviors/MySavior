from flask import Flask, request, jsonify
from firebase_admin import credentials, auth, firestore, initialize_app
import os
import sys
import requests

app = Flask(__name__)

# Path ke service account (Firebase)
firebase_key_path = os.environ.get('FIREBASE_KEY_PATH', 'mysavior-5bd47-firebase-adminsdk-cm4cb-fa58e42afe.json')
if not os.path.exists(firebase_key_path):
    print(f"Firebase key file not found at: {firebase_key_path}", file=sys.stderr)
    sys.exit(1)

try:
    cred = credentials.Certificate(firebase_key_path)
    initialize_app(cred)
    db = firestore.client()
    print("Firebase Admin initialized successfully.")
except Exception as e:
    print(f"Error initializing Firebase: {e}", file=sys.stderr)
    sys.exit(1)

# API Key untuk NewsAPI (pastikan sudah punya key dari https://newsapi.org/)
NEWS_API_KEY = "eff32602439e4c71b754af9bd6232933"  # Ganti dengan API key Anda
NEWS_API_URL = f"https://newsapi.org/v2/top-headlines?country=us&category=health&apiKey={NEWS_API_KEY}"

@app.route('/', methods=['GET'])
def home():
    """
    Endpoint Root untuk memberikan informasi tentang API.
    """
    return jsonify({
        "message": "Welcome to the API!",
        "endpoints": {
            "/register": "Register new users with POST requests.",
            "/login": "Login users with POST requests.",
            "/articles": "Get a list of articles from NewsAPI."
        }
    })

@app.route('/register', methods=['POST'])
def register():
    """
    Endpoint untuk registrasi pengguna baru.
    """
    # Log data mentah yang diterima
    print("Raw request data:", request.data)
    print("Request headers:", request.headers)
    print("Request JSON parsed:", request.json)

    data = request.json
    if not data:
        return jsonify({"error": True, "message": "No JSON body found. Please send data as JSON."}), 400

    name = data.get('name', '').strip()
    email = data.get('email', '').strip()
    password = data.get('password', '').strip()
    
    # Log data yang sudah di-parse
    print("Parsed Data:", {"name": name, "email": email, "password": password })

    if not email or not password or not name:
        return jsonify({"error": True, "message": "Missing name, email, or password"}), 400

    try:
        # Cek apakah user sudah terdaftar
        try:
            existing_user = auth.get_user_by_email(email)
            # Jika tidak raise exception berarti user dengan email ini sudah ada
            return jsonify({"error": True, "message": "The email is already registered"}), 400
        except auth.UserNotFoundError:
            # Jika UserNotFoundError ter-raise, berarti email ini belum terdaftar
            pass

        # Buat user baru
        user = auth.create_user(display_name=name, email=email, password=password )
        # Simpan data user ke Firestore
        db.collection('users').document(user.uid).set({
            'name': name,
            'email': email,
            'uid': user.uid,
            'created_at': firestore.SERVER_TIMESTAMP
        })

        print("User created successfully with UID:", user.uid)
        return jsonify({"error": False, "message": "User Created"}), 201

    except Exception as e:
        # Log error di server
        print("Error on user registration:", str(e), file=sys.stderr)
        return jsonify({"error": True, "message": str(e)}), 500

@app.route('/login', methods=['POST'])
def login():
    """
    Endpoint untuk login pengguna.
    """
    data = request.json
    if not data:
        return jsonify({"error": True, "message": "No JSON body found. Please send data as JSON."}), 400

    email = data.get('email')

    if not email:
        return jsonify({"error": True, "message": "Missing email"}), 400

    try:
        user = auth.get_user_by_email(email)
        custom_token = auth.create_custom_token(user.uid).decode('utf-8')

        return jsonify({
            "error": False,
            "message": "Login successful",
            "loginResult": {
                "userId": user.uid,
                "name": user.display_name,
                "token": custom_token
            }
        }), 200

    except auth.UserNotFoundError:
        return jsonify({"error": True, "message": "Account not found"}), 404
    except Exception as e:
        return jsonify({"error": True, "message": str(e)}), 500

@app.route('/articles', methods=['GET'])
def get_articles():
    """
    Endpoint untuk mengambil data artikel dari NewsAPI.
    """
    try:
        response = requests.get(NEWS_API_URL)
        data = response.json()

        if data.get("status") == "ok":
            return jsonify({
                "status": "ok",
                "totalResults": data.get("totalResults"),
                "articles": data.get("articles")
            }), 200
        else:
            return jsonify({
                "status": "error",
                "message": "Failed to fetch articles from NewsAPI",
                "error": data.get("message")
            }), 500

    except Exception as e:
        return jsonify({
            "status": "error",
            "message": "An error occurred while fetching articles.",
            "error": str(e)
        }), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port)
