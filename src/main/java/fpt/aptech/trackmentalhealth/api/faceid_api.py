from flask import Flask, request, jsonify
import face_recognition
import numpy as np
import io
from PIL import Image

app = Flask(__name__)

# Ảnh mẫu (khuôn mặt đăng ký sẵn)
KNOWN_FACE_ENCODINGS = []
KNOWN_FACE_NAMES = []

# Giả sử ta có 1 ảnh khuôn mặt mẫu
def load_known_faces():
    image = face_recognition.load_image_file("known_user.jpg")
    encoding = face_recognition.face_encodings(image)[0]
    KNOWN_FACE_ENCODINGS.append(encoding)
    KNOWN_FACE_NAMES.append("user@example.com")  # mapping to email or id

@app.route('/verify-face', methods=['POST'])
def verify_face():
    if 'image' not in request.files:
        return jsonify({"error": "No image file"}), 400

    file = request.files['image']
    img = face_recognition.load_image_file(file)

    unknown_encodings = face_recognition.face_encodings(img)
    if len(unknown_encodings) == 0:
        return jsonify({"error": "No face detected"}), 400

    for unknown in unknown_encodings:
        results = face_recognition.compare_faces(KNOWN_FACE_ENCODINGS, unknown)
        if True in results:
            index = results.index(True)
            return jsonify({"verified": True, "email": KNOWN_FACE_NAMES[index]})

    return jsonify({"verified": False})

if __name__ == '__main__':
    load_known_faces()
    app.run(debug=True, port=5001)
