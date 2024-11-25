# MySavior - Image Recognition for Food Analysis
Utilizes AI-powered image recognition to evaluate and classify food as healthy or unhealthy, promoting better dietary choices for elderly users.

#### Step 1: Clone the Repo
```bash
git clone https://github.com/BangkitSaviors/MySavior.git
cd MySavior
git checkout Machine-Learning
```

#### Step 2: Get an API Key
Get an API Key from http://app.nanonets.com/user/api_key
 >_**Note:** You need to register first to get an API key

#### Step 3: Set the API key as an Environment Variable
```bash
NANONETS_API_KEY='10707b22-aaf1-11ef-96dc-aa008b6b55e8'
```

#### Step 4: Create a New Model
```bash
python ./src/model/create_model.py
```
 >_**Note:** This generates a MODEL_ID that you need for the next step

#### Step 5: Add Model Id as Environment Variable
```bash
NANONETS_MODEL_ID='5425df82-b1f3-4fe8-8c05-7d51a227d6ca'
```
 >_**Note:** you will get YOUR_MODEL_ID from the previous step

#### Step 6: Upload the Training Data
```bash
python ./src/data/upload_training.py
```
 >_**Note:** run this code to upload the data.

#### Step 7: Train Model
Once the Images have been uploaded, begin training the Model
```bash
python ./src/models/train_model.py
```

#### Step 8: Get Model State
The model takes ~2 hours to train. You will get an email once the model is trained. In the meanwhile you check the state of the model
```bash
python ./code/model_state.py
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
w