from flask import Flask, request, jsonify
import face_recognition
import numpy as np
import os
from PIL import Image
import io

app = Flask(__name__)

# Cấu hình thư mục lưu trữ
ENCODINGS_DIR = "face_encodings"
if not os.path.exists(ENCODINGS_DIR):
    os.makedirs(ENCODINGS_DIR)


def get_encoding_path(email):
    """Trả về đường dẫn tệp encoding của người dùng"""
    safe_email = email.replace("@", "_at_").replace(".", "_")
    return os.path.join(ENCODINGS_DIR, f"{safe_email}.npy")


@app.route('/register-face', methods=['POST'])
def register_face():
    """Đăng ký khuôn mặt cho người dùng"""
    email = request.form.get("email")
    file = request.files.get("image")

    if not email or not file:
        return jsonify({"error": "Email and image are required"}), 400

    image = face_recognition.load_image_file(file)
    encodings = face_recognition.face_encodings(image)

    if len(encodings) == 0:
        return jsonify({"error": "No face detected"}), 400

    encoding = encodings[0]
    path = get_encoding_path(email)
    np.save(path, encoding)

    return jsonify({"message": f"Face registered for {email}"}), 200


@app.route('/verify-face', methods=['POST'])
def verify_face():
    """Xác thực khuôn mặt"""
    file = request.files.get("image")

    if not file:
        return jsonify({"error": "No image provided"}), 400

    unknown_image = face_recognition.load_image_file(file)
    unknown_encodings = face_recognition.face_encodings(unknown_image)

    if len(unknown_encodings) == 0:
        return jsonify({"error": "No face detected"}), 400

    unknown_encoding = unknown_encodings[0]

    # Lặp qua tất cả encoding đã lưu
    for filename in os.listdir(ENCODINGS_DIR):
        if filename.endswith(".npy"):
            known_encoding = np.load(os.path.join(ENCODINGS_DIR, filename))
            result = face_recognition.compare_faces([known_encoding], unknown_encoding)[0]
            if result:
                email = filename.replace("_at_", "@").replace("_", ".").replace(".npy", "")
                return jsonify({"verified": True, "email": email})

    return jsonify({"verified": False}), 401


if __name__ == '__main__':
    app.run(debug=True, port=5001)
