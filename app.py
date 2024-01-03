from flask import Flask, request, jsonify
import cv2
import numpy as np
import tensorflow as tf
from flask_cors import CORS
import matplotlib.pyplot as plt
from PIL import Image

app = Flask(__name__)
CORS(app)

model = tf.keras.models.load_model('handwritten_digits.model')

@app.route('/predict', methods=["POST"])
def predict():
    # Get the image data from the request
    image_data = request.files['image']

    #save the image
    image_data.save('received_image.png')

    # Read the saved image using OpenCV
    # img = cv2.imread('received_image.png')
    # img = cv2.resize(img, (28, 28))

    img = Image.open('received_image.png')

    # Remove alpha channel if it exists
    if img.mode in ('RGBA', 'LA') or (img.mode == 'P' and 'transparency' in img.info):
        img = img.convert('RGB')

    # Resize the image with anti-aliasing to minimize quality loss
    resized_image = img.resize((28, 28), Image.ANTIALIAS)

    # Create a new blank 28x28 image
    # new_image = Image.new("RGB", (28, 28), color="white")

    # Calculate the position to paste the resized image in the center
    # left = (28 - resized_image.width) // 2
    # top = (28 - resized_image.height) // 2
    # right = left + resized_image.width
    # bottom = top + resized_image.height

    # Paste the resized image onto the blank canvas
    # new_image.paste(resized_image, (left, top, right, bottom))

    # Save the resized and padded image
    resized_image.save('resized_image.png')

    # Remove alpha channel if it exists
    # if img.mode in ('RGBA', 'LA') or (img.mode == 'P' and 'transparency' in img.info):
    #     img = img.convert('RGB')

    # # Save the image with 24-bit depth (PNG format for lossless compression)
    # img.save('output_image.png', quality=95)

    
    # Process the image
    # img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # cv2.imwrite('resized_image.png', img)

    # Convert the Pillow Image to a NumPy array
    # img_array = np.array(resized_image)

    # Invert the image array
    # inverted_img_array = np.invert(img_array)

    img = cv2.imread('resized_image.png')

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    img = np.invert(np.array([img]))

    
    # Perform prediction using your model
    prediction = model.predict(img)

    pred_out = np.argmax(prediction)

    print(pred_out)
    
    # Return the prediction result
    return jsonify({'prediction': 'hi'})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
