import os
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import cv2
from google.colab.patches import cv2_imshow
import tensorflow as tf
from sklearn.preprocessing import LabelBinarizer
import numpy as np
from sklearn.datasets import fetch_lfw_people
from sklearn.model_selection import train_test_split


def LoadImage(imageName):
    image = cv2.imread(imageName)
     #mpimg.imread(imageName)
    return image

def loadImages(listAllFiles,imagesNum):
    images = []
    cntImages = 0
    for imageName in listAllFiles:
        image = LoadImage(imageName)
        images.append(image)
        cntImages+=1
        if(cntImages>=imagesNum):
          break
    return images

def FilterDirFiles(dirName,dicFoldersNames):
    labels = []
    listOfFiles = []
    labels_names = []
    idx = 0;
    nTakePicsInFolder = 300
    #print(dicFoldersNames)
    for root, directories, files in os.walk(dirName,topdown=False):
      for dir1 in directories:
        idx = len(dicFoldersNames.keys())
        dicFoldersNames[dir1] = idx
        curDir = os.path.join(root,dir1)
        filesLst = os.listdir(curDir)
        #print(curDir)
        for f1 in filesLst[0:nTakePicsInFolder:]:
          if (f1!=[] and curDir!=[]):
            file1 = (os.path.join(curDir,f1))
            listOfFiles.append(file1)
            #print(dir1)
            lbl = dicFoldersNames[dir1]
            #print(lbl)
            labels.append(lbl)
            #print(labels)
    key_to_be_deleted = '.ipynb_checkpoints'
    result = dicFoldersNames.pop(key_to_be_deleted, None)
    for tgtname in directories:
      if(tgtname in dicFoldersNames.keys()):
        labels_names.append(tgtname)
    print('labels_names',labels_names)
    return listOfFiles,labels,labels_names

def printMat(images2Plot,target_names_,labels_):
  fig, ax = plt.subplots(6, 7,True,True,True,figsize=(15, 19))
  #fig, ax = plt.subplots(3, 5)
  for i, axi in enumerate(ax.flat):
      I=np.reshape(images2Plot[i,:],(62,47))
      axi.imshow(I, cmap='bone')
      #print(I.shape)
      axi.set(xticks=[], yticks=[],
      xlabel=target_names_[labels_[i]-1])


def GetLoadedImagesList():
  dirName = "/content/drive/MyDrive/lfw_filtered"
  dicFoldersNames = {}
  # Get the list of all files in directory tree at given path
  listOfFiles = list()
  imagesLst = []
  labels_names = []
  listOfFiles, labels, labels_names = FilterDirFiles(dirName, dicFoldersNames)
  imagesNum = len(listOfFiles)
  images = loadImages(listOfFiles, imagesNum)
  for image in images:
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    image = cv2.resize(gray, (47, 62))
    # print(image.shape)
    imagesLst.append(image)

  return imagesLst, labels, labels_names



imagesLoaded, labels, labels_names=GetLoadedImagesList()
images = np.array(imagesLoaded)[0::]
size =images.shape[0]
labels = np.array(labels)


lb = LabelBinarizer()
labelsHotEncode = lb.fit_transform(labels)
#print(labels)
# print('f',features)
print('l',labels)
print('hotEncode',labelsHotEncode)

x_train, x_test, labels_train, labels_test = train_test_split(images, labelsHotEncode,random_state=42)
labels_train = np.array(labels_train)
labels_test = np.array(labels_test)
w=62
h=47


model3 = tf.keras.models.Sequential([
  # Your Code Here
  # This is the first convolution
  # tf.keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(62, 47, 3)),
  tf.keras.layers.Conv2D(16, (3, 3), activation='relu', input_shape=(w, h, 1)),
  tf.keras.layers.MaxPooling2D(2, 2),
  # tf.keras.layers.Dropout(rate=0.001),
  tf.keras.layers.Dropout(rate=0.0001),
  # The second convolution
  tf.keras.layers.Conv2D(32, (3, 3), activation='relu'),
  tf.keras.layers.MaxPooling2D(2, 2),

  tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
  tf.keras.layers.MaxPooling2D(2, 2),
  tf.keras.layers.Flatten(),

  tf.keras.layers.Dense(512, activation='relu',
                        # kernel_regularizer=tf.keras.regularizers.l1(0.001),
                        activity_regularizer=tf.keras.regularizers.l2(0.001)
                        ),

  # Only 1 output neuron. It will contain a value from 0-1 where 0 for 1 class ('horses') and 1 for the other ('humans')
  tf.keras.layers.Dense(7, activation='sigmoid')

])

model3.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

Itr=np.expand_dims(x_train,axis=-1)
Itst=np.expand_dims(x_test,axis=-1)
model3.fit(x=Itr, y=labels_train, epochs=150, validation_data=(Itst, labels_test), callbacks=[tensorboard_callback])


print('**************************   Start Test *************')
prediction = model3.predict(Itst)
#prediction = model4.predict(Itst)

predicted1 = np.argmax(prediction,axis = 1)
print('predicted1',predicted1)
persons_tgt = []
for person in labels_names:
    persons_tgt
print(labels_names)
CATEGORIES = labels_names

# ----------- print Mat
#print('************** testing results*******************')
def printMat(x_train_,target_names_,labels_):
  fig, ax = plt.subplots(6, 7,True,True,True,figsize=(15, 19))
  #fig, ax = plt.subplots(3, 5)
  for i, axi in enumerate(ax.flat):
      I=np.reshape(x_train_[i,:],(62,47))
      axi.imshow(I, cmap='bone')
      #print(I.shape)
      axi.set(xticks=[], yticks=[],
      xlabel=target_names_[labels_[i]])
printMat(x_test, labels_names,predicted1 )
