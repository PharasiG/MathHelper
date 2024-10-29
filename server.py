import google.generativeai as genai
from dotenv import load_dotenv
from flask import Flask, jsonify, request, send_file
import os

app = Flask(__name__)

load_dotenv()

genai.configure(api_key=os.environ["GEMINI_KEY"])
model = genai.GenerativeModel("gemini-1.5-flash")

@app.route('/gemini/<question>', methods=['GET'])
def gemini(question):
    response = model.generate_content(question)
    return jsonify({'response': response.text})
#     return response.text

if __name__ == '__main__':
    app.run()
