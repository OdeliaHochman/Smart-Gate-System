import os
import time
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import cv2
#from google.colab.patches import cv2_imshow
import tensorflow as tf
from tensorflow.python.keras.models import load_model
import numpy as np
#import color2


w = 62
h = 47


def LoadImage(imageName):
    image = cv2.imread(imageName)
    #mpimg.imread(imageName)
    return image

'''
def printMat(x_train_,target_names_,labels_):
  fig, ax = plt.subplots(6, 7,True,True,True,figsize=(15, 19))
  #fig, ax = plt.subplots(3, 5)
  for i, axi in enumerate(ax.flat):
      I=np.reshape(x_train_[i,:],(62,47))
      axi.imshow(I, cmap='bone')
      #print(I.shape)
      axi.set(xticks=[], yticks=[],
      xlabel=target_names_[labels_[i]])
'''


def ProcessImage():
    model3 = tf.keras.models.Sequential([
        # Your Code Here
        # This is the first convolution
        #tf.keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(62, 47, 3)),
        #tf.keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(w, h, 1)),
        tf.keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(w, h, 1)),
        tf.keras.layers.MaxPooling2D(2, 2),
        #tf.keras.layers.Dropout(rate=0.001),
        tf.keras.layers.Dropout(rate=0.0001),
        # The second convolution
        tf.keras.layers.Conv2D(32, (3,3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2,2),
        # The third convolution
        # tf.keras.layers.Conv2D(32, (3,3), activation='relu'),
        # tf.keras.layers.MaxPooling2D(2,2),

        tf.keras.layers.Conv2D(64, (3,3), activation='relu'),
        tf.keras.layers.MaxPooling2D(2,2),
        #tf.keras.layers.Conv2D(256, (3,3), activation='relu'),
        #tf.keras.layers.MaxPooling2D(2,2),
        tf.keras.layers.Flatten(),
        # 512 neuron hidden layer
        #tf.keras.layers.Dense(512, activation='relu'),

        tf.keras.layers.Dense(512, activation='relu',
                               kernel_regularizer=tf.keras.regularizers.l1(0.001),
                               activity_regularizer=tf.keras.regularizers.l2(0.001)
                              ),


        # Only 1 output neuron. It will contain a value from 0-1 where 0 for 1 class ('horses') and 1 for the other ('humans')
        tf.keras.layers.Dense(7, activation='sigmoid')

    ])

    #model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
    model3.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    #model3.load_weights('C:\\Users\\Owner\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\Weight_VisitoresFacesRecognition.index')


    #imageName = "C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\lfw_filtered\\Ariel_Sharon_0001.jpg"
    #imageName = "faceRecognition\\lfw_filtered\\Ariel_Sharon_0001.jpg"
    #imageName =
    image1 = cv2.imread("/tmp/output.jpg")
    image = cv2.imread("/tmp/output_gray.jpg")
    print(image.shape)
    #I = np.reshape(x_train_[i, :], (62, 47))
    img = cv2.resize(image,(w, h))
    print(img.shape)
    #I=np.reshape(img[::,:],(62,47))
    I=img[:,:,0]
    print(I.shape)
    #img = np.reshape(img[0, :], (w,h) )
    #img = np.reshape(img[::-1], (1,w,h,1) )
    #img = cv2.resize(image, 47, 62)
    imagesLst = []
    #image = LoadImage(imageName)
    #imgeGray = cv2.clr2rgb(image)
    imagesLst.append(I)
    print(img.shape)

    #model3.load_weights('C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\Weight_VisitoresFacesRecognition.index')
    #model3.load_weights('C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\my_model.h5')
    with tf.device('/cpu:0'):
        new_model = load_model('C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\my_model.h5')
    #model3.load_weights("/content/drive/MyDrive/Weight_VisitoresFacesRecognition.index")
    print('SUCESS!!!!!!!!!!!')


    print('**************************   Start Test *************')


    #prediction = model3.predict(imagesLst)

    #print('predicted1',prediction)

    #(Itst)
    #prediction = model4.predict(Itst)
    '''
    print(Itst.shape)
    print(prediction.shape)
    print(prediction[0])
    
    predicted1 = np.argmax(prediction,axis = 1)
    print('predicted1',predicted1)
    persons_tgt = []
    for person in labels_names:
        persons_tgt
    print(labels_names)
    CATEGORIES = labels_names
    
    # ----------- print Mat
    #print('************** testing results*******************')
    
    printMat(x_test, labels_names,predicted1 )
    ####################################################################
    '''


    def get_features_labels(self):
        return self.faces.images, self.faces.data, self.faces.target, self.faces.target_names
    #x_test = []

    #Itst=np.expand_dims(imagesLst,axis=-1)
    Itst=np.expand_dims(imagesLst,axis=-1)

    #I=np.reshape(x_train[0:,:],(62,47))
    '''
    Itr=np.expand_dims(x_train,axis=-1) 
    Itst=np.expand_dims(x_test,axis=-1) 
    '''
    #x_train = x_train.reshape(-1, 62, 47, 1)
    #x_train = tf.reshape(x_train, (-1, 62, 47, 1))
    #x_train = np.expand_dims(x_train, axis=-1)
    #yy_train = np.expand_dims(y_train, axis=-1)
    #model.fit(x=Itr, y=y_train, epochs=2, validation_data=(Itst, y_test), callbacks=[tensorboard_callback])
    #model.fit(x=x_train, y=yy_train, epochs=2, validation_data=(x_test, y_test), callbacks=[tensorboard_callback])
    #model.fit(x=Itr, y=y_train, epochs=2, validation_data=(Itst, y_test), callbacks=[tensorboard_callback])
    #model3.fit(x=Itr, y=labels_train, epochs=85, validation_data=(Itst, labels_test), callbacks=[tensorboard_callback])
    #model3.fit(x=Itr, y=labels_train, epochs=50, validation_data=(Itst, labels_test), callbacks=[tensorboard_callback])
    #model3.save_weights('Weight_FacesDetection')

    #----------- test
    print('**************************   Start Test *************')
    print(Itst)
    prediction = new_model.predict(Itst)
    #prediction = model4.predict(Itst)
    print(Itst.shape)
    print(prediction.shape)
    print(prediction[0])

    predicted1 = np.argmax(prediction,axis = 1)
    print('predicted1',predicted1)
    target_names = ['Tony_Blair', 'Hugo_Chavez', 'Gerhard_Schroeder', 'George_W_Bush', 'Donald_Rumsfeld', 'Colin_Powell', 'Ariel_Sharon']
    print(target_names)
    CATEGORIES = target_names

    def printMat(x_train_,target_names_,labels_):
      fig, ax = plt.subplots(6, 7,True,True,True,figsize=(15, 19))
      #fig, ax = plt.subplots(3, 5)
      for i, axi in enumerate(ax.flat):
          #I=np.reshape(x_train_[i,:],(62,47))
          #axi.imshow(I, cmap='bone')
          axi.imshow(x_train_, cmap='bone')
          print(x_train_.shape)
          axi.set(xticks=[], yticks=[],
          xlabel=target_names_[labels_[i]])
    #printMat(image, target_names,predicted1 )
    #printMat(image1, target_names,predicted1 )



    #fig, ax = plt.subplots(3, 5,True,True,True,figsize=(8, 8))
           #fig, ax = plt.subplots(3, 5)
    #for i, axi in enumerate(ax.flat):
       #print()
      # I=np.reshape(x_train[i,:],(62,47))
      # axi.imshow(I, cmap='bone')
      # axi.set(xticks=[], yticks=[],
      # xlabel=target_names[labels[i]])
      # print(xlabel)
def PrintMyName():
    print("myName!!")
if __name__ == '__main__':
    print("after gate handler")
    while (True):
        time.sleep(0.5)
        if (os.path.isfile("/tmp/output.jpg")):
            ProcessImage()
            os.remove("/tmp/output.jpg")
            os.remove("/tmp/output_gray.jpg")
            time.sleep(0.5)






