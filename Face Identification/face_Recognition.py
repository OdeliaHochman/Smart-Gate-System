# import os

import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import cv2
from tensorflow.keras import regularizers
import tensorflow as tf
import datetime, os
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
# import numpy as np
# import matplotlib.pyplot as plt
from sklearn.preprocessing import LabelBinarizer
from sklearn.datasets import fetch_lfw_people
from sklearn.model_selection import train_test_split

# path = "C:\\Networks\\work\\Visitors_lisencePlateRecognition\\root"
'''
    For the given path, get the List of all files in the directory tree 
'''
idxImage = 0
idxOfFolder = 0
dicFoldersNames = {}
images = []  # list()
labels = list()
imagesArr = []


def getListOfFiles(dirName):
    # create a list of file and sub directories
    # names in the given directory
    listOfNumbersByFolders = []

    listOfFile = os.listdir(dirName)
    os.listdir(dirName)
    allFiles = []  # list()

    # Iterate over all the entries
    for entry in listOfFile:
        # Create full path
        fullPath = os.path.join(dirName, entry)
        # If entry is a directory then get the list of files in this directory
        if os.path.isdir(fullPath):
            allFiles = allFiles + getListOfFiles(fullPath)
        else:
            allFiles.append(entry)
            folderName = os.path.basename(dirName)
            idxOfFolder = len(dicFoldersNames.keys())
            dicFoldersNames[folderName] = idxOfFolder
            if (len(labels) < 10):
                labels.append(dicFoldersNames[folderName])
                # print(x)
        #  if(x.)
        # listOfNumbersByFolders.append(idxOfFolder)

    return allFiles


def LoadImage(imageName):
    image = cv2.imread(imageName)
    # mpimg.imread(imageName)
    return image


def loadImages(listAllFiles, imagesNum):
    # images = []
    cntImages = 0
    for imageName in listAllFiles:
        image = LoadImage(imageName)
        images.append(image)
        cntImages += 1
        if (cntImages >= imagesNum):
            break
    return images


def ProcessImages(imagesList, labelList):
    # print("imagesList:", len(imagesList))
    labels = labelList

    lb = LabelBinarizer()
    labelsHotEncode = lb.fit_transform(labels)
    images = np.array(imagesList)
    print("labelHot:", type(labelsHotEncode))
    print("Images:", type(images))

    '''
      X, y = np.arange(10).reshape((5, 2)), range(5)
      print(X.shape)
  
    X
    array([[0, 1],
          [2, 3],
          [4, 5],
          [6, 7],
          [8, 9]])
      list(y)
      [0, 1, 2, 3, 4]
  
      X_train, X_test, y_train, y_test = train_test_split(X,y)
    '''

    # x_train, x_test, y_train, y_test = train_test_split(images, labels,random_state=42)
    x_train, x_test, labels_train, labels_test = train_test_split(images, labelsHotEncode, random_state=42)
    # x_train.reshape(-1,62,47,1)
    # for nameOfFolder in target_names:
    #    createFolderFromImagesByName(nameOfFolder)
    # print('****************print images********************')
    # lfw_ds.draw_Images(x_train, y_train, target_names)

    # for nameOfFolder in target_names:
    #    createFolderFromImagesByName(nameOfFolder)
    print('\n****************print images********************\n')
    # lfw_ds.draw_Images(x_train, y_train, target_names)
    # cv2.imwrite('Path/Image.jpg', image_name)

    print('************************************')
    # lfw_ds.draw_Images(x_test, y_train, target_names)
    print('x train sahpe', x_train.shape)
    #print('y train sahpe', y_train[0].shape)
    print('x test sahpe', x_test.shape)
    #print('y test sahpe', y_test[0].shape)

    # ------------------ model  -----------------------------------
    model4 = model3 = tf.keras.models.Sequential([
        # Your Code Here
        # This is the first convolution
        # tf.keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(62, 47, 3)),
        tf.keras.layers.Conv2D(16, (3, 3), activation='relu', input_shape=(62, 47, 1)),
        tf.keras.layers.MaxPooling2D(2, 2),
        tf.keras.layers.Dropout(rate=0.001),
        # The second convolution
        tf.keras.layers.Conv2D(32, (3, 3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2, 2),
        # The third convolution
        # tf.keras.layers.Conv2D(32, (3,3), activation='relu'),
        # tf.keras.layers.MaxPooling2D(2,2),

        tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2, 2),
        # tf.keras.layers.Conv2D(256, (3,3), activation='relu'),
        # tf.keras.layers.MaxPooling2D(2,2),
        tf.keras.layers.Flatten(),
        # 512 neuron hidden layer
        # tf.keras.layers.Dense(512, activation='relu'),

        tf.keras.layers.Dense(512, activation='relu',
                              # kernel_regularizer=tf.keras.regularizers.l1(0.001),
                              activity_regularizer=tf.keras.regularizers.l2(0.001)
                              ),

        # Only 1 output neuron. It will contain a value from 0-1 where 0 for 1 class ('horses') and 1 for the other ('humans')
        tf.keras.layers.Dense(8, activation='sigmoid')

    ])

    # model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
    # model3.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
    model3.compile(loss='sparse_categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

    logdir = os.path.join("logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S"))

    file_writer = tf.summary.create_file_writer(logdir)
    file_writer.set_as_default()

    tensorboard_callback = tf.keras.callbacks.TensorBoard(
        logdir)  # Add this argumant in order to send all model weights: histogram_freq=1)

    # ------------    expand_dims

    Itr = np.expand_dims(x_train, axis=-1)
    Itst = np.expand_dims(x_test, axis=-1)
    print(x_train.shape)
    print(x_test.shape)
    print(Itr.shape)
    print(Itst.shape)
  #  print(y_train.shape)
  #  print(y_test.shape)
    print(labelsHotEncode[0])
    print(labelsHotEncode[1])

    # ----------  train

    # I=np.reshape(x_train[0:,:],(62,47))

    Itr = np.expand_dims(x_train, axis=-1)
    Itst = np.expand_dims(x_test, axis=-1)

    # x_train = x_train.reshape(-1, 62, 47, 1)
    # x_train = tf.reshape(x_train, (-1, 62, 47, 1))
    # x_train = np.expand_dims(x_train, axis=-1)
    # yy_train = np.expand_dims(y_train, axis=-1)
    # model.fit(x=Itr, y=y_train, epochs=2, validation_data=(Itst, y_test), callbacks=[tensorboard_callback])
    # model.fit(x=x_train, y=yy_train, epochs=2, validation_data=(x_test, y_test), callbacks=[tensorboard_callback])
    # model.fit(x=Itr, y=y_train, epochs=2, validation_data=(Itst, y_test), callbacks=[tensorboard_callback])
    # model3.fit(x=Itr, y=labels_train, epochs=85, validation_data=(Itst, labels_test), callbacks=[tensorboard_callback])
    # model3.add(tf.keras.layers.Dense(nb_classes, activation='softmax'))
    model3.fit(x=Itr, y=labels_train, epochs=7, validation_data=(Itst, labels_test), callbacks=[tensorboard_callback])
    # model3.save_weights('Weight_FacesDetection')

    # ----------- test
    print('**************************   Start Test *************')
    prediction = model3.predict(Itst)
    # prediction = model4.predict(Itst)
    print(Itst.shape)
    print(prediction.shape)
    print(prediction[0])

    predicted1 = np.argmax(prediction, axis=1)
    print('predicted1', predicted1)
  #  print(target_names)
  #  CATEGORIES = target_names


imagesLst = []


def main():
    dirName ='C:\\Users\\odelia\\Desktop\\face recognition -project\\face recognition -project\\Images'
    listAllFiles = []
    # Get the list of all files in directory tree at given path
    listAllFiles = getListOfFiles(dirName)



    # Get the list of all files in directory tree at given path
    listOfFiles = list()
    for (dirpath, dirnames, filenames) in os.walk(dirName):
        listOfFiles += [os.path.join(dirpath, file) for file in filenames]

    imagesNum = 10
    images = loadImages(listOfFiles, imagesNum)
    # images_array=np.asarray(images)
    # imagesArr=images_array.reshape(62,47).T
    # for img in images:
    # imagesArr.append(img)

    for image in images:
        print(image.shape)
        # imagesArr+=image
        # I=np.reshape(x_train[i,:],(62,47))
        # I=np.reshape(img,(250,250,3))
        # I=cv2.resize(img,(62,47))
        # gray = cv2.cvtColor(I, cv2.COLOR_BGR2GRAY)
        # gray2 = cv2.bilateralFilter(gray, 13, 15, 15)
        # print("shape of image: ",gray2.shape)
        # imgplot = plt.imshow(img)
        # image = cv2.imread(img)
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        image = cv2.resize(gray, (47, 62))
        print(image.shape)
        imagesLst.append(image)
        cv2.imshow("",image)
        cv2.waitKey(0)
        cv2.destroyAllWindows()
    # print("type :", type(imagesLst))
    # imgplot = plt.imshow(gray2)

    # Print the files
    '''
    for elem in listAllFiles:
        print(elem)
    for dir1 in dirnames:
        print(dir1)
    for pth1 in dirpath:
        print(pth1)    
    '''
    for lblKey in dicFoldersNames.keys():
        print(lblKey, "=", dicFoldersNames[lblKey])
    # images_array=np.asarray(imagesLst)
    # images_array=imagesLst.reshape(62,47).T
    # imagesArr = np.array(imagesLst)
    for im in imagesLst:
        imagesArr.append(im)
        # imagesArr.reshape(x_train_[i,:],62,47)
    print("size of Array:", len(imagesArr))
    print("size of List:", len(imagesLst))
    print(type(imagesArr))
    print(type(imagesLst))

    ProcessImages(imagesArr, labels)

    # imagesArr=images_array.reshape(62,47).T
    # print("Shape:" ,imagesArr.shape())

    # print(imagesArr.shape())


if __name__ == '__main__':
    main()