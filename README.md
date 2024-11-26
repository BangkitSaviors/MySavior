# MySavior - Image Recognition for Food Analysis
Utilizes AI-powered image recognition to evaluate and classify food as healthy or unhealthy, promoting better dietary choices for elderly users.

#### Prerequisite
  - Download the dataset [data.tar.gz](https://drive.google.com/open?id=1hKFMGIY2jNYbntK4e4aGvwOwvzptP8DH) and place it in the project root directory.
  - Use Python 3.8.10 (recommended). Python versions lower than 3.10 are supported.
  - Install the required dependencies from requirements.txt:

```bash
pip install -r requirements.
```

#### Step 1: Clone the Repo
```bash
git clone https://github.com/BangkitSaviors/MySavior.git
cd MySavior
git checkout Machine-Learning
```

#### Step 2: Obtain an API Key
Get an API Key from http://app.nanonets.com/user/api_key
 >_**Note:**  You need to register an account to receive the API key.

#### Step 3: Configure the API Key as an Environment Variable
Set the API key as an environment variable:
```bash
NANONETS_API_KEY='10707b22-aaf1-11ef-96dc-aa008b6b55e8'
```

#### Step 4: Create a New Model
Generate a new model using the following command:
```bash
python ./src/models/create_model.py
```
 >_**Note:** This generates a MODEL_ID that you need for the next step

#### Step 5: Configure the Model ID as an Environment Variable
Set the MODEL_ID as an environment variable:
```bash
NANONETS_MODEL_ID='5425df82-b1f3-4fe8-8c05-7d51a227d6ca'
```
 >_**Note:** '5425df82-b1f3-4fe8-8c05-7d51a227d6ca' with the MODEL_ID from the previous step.

#### Step 6: Upload the Training Data
Upload the training dataset to the server:
```bash
python ./src/data/upload_training.py
```
 >_**Note:** Ensure the dataset is correctly placed in the project directory before running this command.

#### Step 7: Train Model
After uploading the dataset, start training the model:
```bash
python ./src/models/train_model.py
```

#### Step 8: Check the Model State
Training may take approximately 2 hours. You will receive an email once training is complete. In the meantime, you can check the training status:
```bash
python ./models/model_state.py
```

#### Step 9: Make Prediction
Once the model is trained. You can make predictions using the model
```bash
python ./src/prediction/prediction.py PATH_TO_YOUR_IMAGE.jpg
```

**Sample Usage:**
```bash
python ./src/prediction/prediction.py ./data/nanonets/multilabel_data/ImageSets/2_my_caesar_salad_hostedLargeUrl.jpg
```

## Additional Notes
  - Ensure all dependencies are correctly installed before starting.
  - For troubleshooting, refer to the official [Nanonets Documentation.](https://nanonets.com/documentation/)