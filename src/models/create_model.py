import requests 
import json
import os

BASE_URL = 'https://app.nanonets.com/api/v2/MultiLabelClassification/'
AUTH_KEY = '10707b22-aaf1-11ef-96dc-aa008b6b55e8'


categories = [
'healthy', 'junk', 'dessert', 'appetizer', 'mains', 'soups', 'carbs', 'protein', 'fats', 'meat'
]


ext = ['.jpg']


def create_new_model(categories):
    try:
        url = BASE_URL + "Model/"
        data = json.dumps({'categories' : categories})

        response = requests.request("POST", url, auth=requests.auth.HTTPBasicAuth(AUTH_KEY, ''), data=data)
        result = json.loads(response.text)
        return result["model_id"]
    except:
        raise ValueError("Error in creating model")

if __name__=="__main__":
    model_id = create_new_model(categories)
    print("NEXT RUN: export NANONETS_MODEL_ID=" + model_id)
    print("THEN RUN: python ./src/data/upload_training.py")