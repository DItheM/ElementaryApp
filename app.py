from flask import Flask, request, jsonify
import cv2
import numpy as np
import tensorflow as tf
from flask_cors import CORS
import matplotlib.pyplot as plt
from PIL import Image
from keras.models import model_from_json
import PIL.Image
import pickle
from ultralytics import YOLO
import torch

app = Flask(__name__)
CORS(app)

model_number = tf.keras.models.load_model('models/handwritten_digits.model')

model_sinhala = tf.keras.models.load_model('models/sin_model.h5')
# save the LB object to disk
filename = 'models/LB.sav'
# load the LB object from disk
loaded_LB = pickle.load(open(filename, 'rb'))

# Load JSON and create the model for animal detection
json_file = open('models/animal-classifier.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
model_animal = model_from_json(loaded_model_json)

# Load the model
model_animal.load_weights('models/animal-classifier.h5')

model_object = YOLO("models/best.pt") 


animal_class_labels = ['bear', 'brown bear', 'bull', 'butterfly', 'camel', 'canary', 'caterpillar', 'cattle', 'centipede',
                'cheetah', 'chicken', 'crab', 'crocodile', 'deer', 'duck', 'eagle', 'elephant',
                'fish', 'fox', 'frog', 'giraffe', 'goat', 'goldfish', 'goose', 'hamster',
                'harbor seal', 'hedgehog', 'hippopotamus', 'horse', 'jaguar', 'jellyfish', 'kangaroo', 'koala', 'ladybug',
                'leopard', 'lion', 'lizard', 'lynx', 'magpie', 'monkey', 'moths and butterflies', 'mouse', 'mule', 'ostrich', 'otter', 'owl', 'panda',
                'parrot', 'penguin', 'pig', 'polar bear', 'rabbit', 'raccoon', 'raven', 'red panda', 'rhinoceros', 'scorpian', 'sea lion', 'sea turtle',
                'seahorse', 'shark', 'sheep', 'shrimp', 'snail', 'snake', 'sparrow', 'spider', 'squid', 'squirrel', 'starfish', 'swan', 'tick', 'tiger',
                'tortoise', 'turkey', 'turtle', 'whale', 'woodpecker', 'worm', 'zebra']

# Define the classes dictionary
objects_dict = {0: "Door", 1: "Cabinet Door", 2: "Refrigerator Door", 3: "Window", 4: "Chair", 5: "Table",
                6: "Cabinet", 7: "Couch", 8: "Opened Door", 9: "Pole"}


def preprocess_image(image_path):
    img = PIL.Image.open(image_path)
    img = img.resize((299, 299))
    img = np.array(img) / 255.0
    img = np.expand_dims(img, axis=0)
    return img


def predict_animal(image_path):
    processed_image = preprocess_image(image_path)
    predictions = model_animal.predict(processed_image)
    predicted_label = animal_class_labels[np.argmax(predictions)]
    confidence = np.max(predictions)
    return predicted_label, confidence



@app.route('/predict_number', methods=["POST"])
def predictNumber():
    # Get the image data from the request
    image_data = request.files['image']

    #save the image
    image_data.save('data/received/number.png')

    img = Image.open('data/received/number.png')

    # Remove alpha channel if it exists
    if img.mode in ('RGBA', 'LA') or (img.mode == 'P' and 'transparency' in img.info):
        img = img.convert('RGB')

    # Resize the image with anti-aliasing to minimize quality loss
    resized_image = img.resize((28, 28), Image.LANCZOS)

    # Save the resized and padded image
    resized_image.save('data/resized/number.png')

    img = cv2.imread('data/resized/number.png')

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    img = np.invert(np.array([img]))

    
    # Perform prediction using your model
    prediction = model_number.predict(img)

    pred_out = np.argmax(prediction)

    print(str(pred_out))
    
    # Return the prediction result
    return str(pred_out)

@app.route('/predict_sinhala', methods=["POST"])
def predictSinhala():
    # Get the image data from the request
    image_data = request.files['image']

    # Get the selected letter from the request
    selected_letter = request.form['selectedLetter']

    #save the image
    image_data.save('data/received/sinhala.png')

    img = Image.open('data/received/sinhala.png')

    # Remove alpha channel if it exists
    if img.mode in ('RGBA', 'LA') or (img.mode == 'P' and 'transparency' in img.info):
        img = img.convert('RGB')

    # Resize the image with anti-aliasing to minimize quality loss
    resized_image = img.resize((32, 32), Image.LANCZOS)

    # Save the resized and padded image
    resized_image.save('data/resized/sinhala.png')
    
    img = cv2.imread('data/resized/sinhala.png', cv2.IMREAD_GRAYSCALE)
    
    # Convert the image to a numpy array and scale it
    img_array = np.array(img) / 255.0

    # Add a dimension to transform array into a "batch"
    # of size (1, 32, 32, 1)
    img_batch = np.expand_dims(img_array, axis=0)
    img_batch = np.expand_dims(img_batch, axis=-1)
    
    # Get predictions
    predictions = model_sinhala.predict(img_batch)

    #pred_out = np.argmax(predictions[0])

    # Get the indices of the top 40 probabilities
    top40_indices = np.argpartition(predictions[0], -40)[-40:]

    # Sort the indices by their corresponding probabilities
    top40_indices = top40_indices[np.argsort(predictions[0][top40_indices])][::-1]
    
    # Get the corresponding class names
    top40_classes = loaded_LB.classes_[top40_indices]
    
    # Print the top 40 predictions along with their probabilities
    for i in range(40):
        print(f'Class: {top40_classes[i]}, Probability: {predictions[0][top40_indices[i]]}')

    print(selected_letter)
    returnVal = str(-1)

    for i in range(40):
        if str(top40_classes[i]) == selected_letter:
            returnVal = selected_letter 
            break

    return returnVal
        

@app.route('/detect_animal', methods=["POST"])
def detectAnimal():
    # Get the image data from the request
    image_data = request.files['image']

    img_path = 'data/received/animal.jpg'

    #save the image
    image_data.save(img_path)

    prediction, confidence = predict_animal(img_path)

    confidence_float = float(confidence)  # Convert string to float
    rounded_percentage_confidence = round(confidence_float * 100, 2)  # Multiply by 100 and round to 2 decimal places
    formatted_percentage_confidence = "{:.2f}%".format(rounded_percentage_confidence)  # Format as a percentage string


    print(f'Prediction: {prediction}\nConfidence: {confidence:.2%}')

    return {
        "prediction": str(prediction),
        "confidence": formatted_percentage_confidence
    }

@app.route('/detect_object', methods=["POST"])
def detectObject():
    # Get the image data from the request
    image_data = request.files['image']

    img_path = 'data/object_received/object.jpg'

    #save the image
    image_data.save(img_path)

    with torch.no_grad():
        results=model_object.predict(source="data/object_received",conf=0.20,iou=0.70)

    tensor_array = results[0].boxes.cls
    returnData = []

    # Initialize a dictionary to store counts for each class
    class_counts = {class_id: 0 for class_id in objects_dict.keys()}

    # Iterate through the output tensor and count occurrences of each class
    for class_id in tensor_array.cpu().numpy():
        if class_id in objects_dict:
            class_counts[class_id] += 1

    # Check if any class has a count greater than zero
    has_non_zero_count = any(count > 0 for count in class_counts.values())

    # Print the counts for each class with non-zero count
    if has_non_zero_count:
        for class_id, count in class_counts.items():
            if count > 0:
                class_name = objects_dict[class_id]
                if count > 1:
                    class_name = f"{class_name}s"
                returnData.append([class_name, count])             

    return returnData


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
