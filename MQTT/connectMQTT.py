#import RPi.GPIO as gpio
#import picamera
import cv2
import base64
import json
#import cv2
import numpy as np
import time
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import  MQTT.FirebaseUtil as fuDB
####
import os
# import matplotlib.pyplot as plt
# import matplotlib.image as mpimg
# import cv2
# #from google.colab.patches import cv2_imshow
# import tensorflow as tf
# from tensorflow.python.keras.models import load_model
# import numpy as np
# #import color2
####
#import faceRecognition.AX as procImg
#as procImg;


MQTT_SERVER = "192.168.43.171"  #Write Server IP Address
MQTT_PATH = "VisitorImage"
global id_flag
global lp_flag
########### Raspbery  Init ###################
# m11 = 17
# m12 = 27
# led = 5
# buz = 26
# button = 19
# RS = 18
# EN = 23
# D4 = 24
# D5 = 16
# D6 = 20
# D7 = 21
#
# HIGH = 1
# LOW = 0

def AreStringsEqual (s1,s2):

     return s1 == s2
     if s1 == s2 :
           return True
     else:
           return False

###############################################
############  MQTT #############
def on_message(client, userdata, message):
    global g_IsFirstHndler
    print("Image arrived to process!!!:")
    # if g_IsFirstHndler :
    #    g_IsFirstHndler = False
    #    return
    print("my status is:")
    Topic = ""
    bDataOn = False
    bImageFaceArrived = False
    Data = str(message.payload)
    Topic = str(message.topic)
    #Data = str(message.payload.decode("utf-8"))
    #Topic = str(message.topic.decode("utf-8"))
    print("message received ", Data)
    print("message topic=", Topic)
    print("message qos=", message.qos)
    print("message retain flag=", message.retain)
    #"/tmp/receivedLPImage.jpg"
    bImageFaceArrived = AreStringsEqual(Topic,'ImageFaceArrived')
    bImageLpArrived = AreStringsEqual(Topic, 'ImageLpArrived')
    #bDataOn = AreStringsEqual(Data,'ON')
    bDataOn = True
    receivedImageFileName = ""
    outImage = ""
    print("bInOpenGate",bImageFaceArrived)
    print("bDataOn",bDataOn)
    if bImageFaceArrived:
        #receivedImageFileName = "/tmp/receivedFaceImage.jpg"
        receivedImageFileName = "C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\lfw_filtered\Ariel_Sharon\\Ariel_Sharon_0006.jpg"
        receivedImageFileName = "C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\faceRecognition\\lfw_filtered\\George_W_Bush\\George_W_Bush_0003.jpg"
        outImage = "/tmp/receivedFaceImage.jpg"
    elif bImageLpArrived:
        #receivedImageFileName = "/tmp/receivedLPImage.jpg"
        receivedImageFileName = "C:\\Users\\Owner\\Desktop\\OpenCV_3_License_Plate_Recognition_Python-master\\OpenCV_3_License_Plate_Recognition_Python-master\\LicPlateImages\\1.png"
        outImage = "/tmp/receivedLPImage.jpg"
    if bImageFaceArrived or bImageLpArrived :

       print("outer!!!")
       if bDataOn :

          print("before gate handler")
          #clrImgeFile = open(receivedImageFileName, "wb")  # there is a output.jpg which is different
          #clrImgeFile.write(message.payload)
          #imgOriginal = cv2.imread(receivedImageFileName)
          #clrImgeFile.flush()
          #clrImgeFile.close()
          #clrImgeFile = open("/tmp/output.jpg", "rb")
          #clrImgeFile = cv2.imread('C:/Users/N/Desktop/Test.jpg')
          imgOriginal = cv2.imread(receivedImageFileName)
          cv2.imwrite(outImage,imgOriginal)
          #cv2.imshow('img', imgOriginal)
          cv2.waitKey(0)
          if bImageFaceArrived:
              gray = cv2.cvtColor(imgOriginal, cv2.COLOR_BGR2GRAY)
              #cv2.imshow('img', gray)
              cv2.waitKey(0)
              cv2.destroyAllWindows()
              cv2.imwrite("/tmp/receivedFaceImage_gray.jpg",gray)

          '''
          clrImgeFile = cv2.imread('/tmp/output.jpg')
          grayImgeFile = open("/tmp/output_gray.jpg", "wb")
          #img = np.array(clrImgeFile)
          #print(img.shape)
          gray = cv2.cvtColor(clrImgeFile, cv2.COLOR_BGR2GRAY)
          cv2.imshow('Gray image', gray)
          grayImgeFile.write(gray)
          grayImgeFile.close()
          #cv2.imwrite('./0.jpg', image)
          #img_np = cv2.imdecode(jpg_as_np, cv2.CV_LOAD_IMAGE_COLOR)
         # gate() ----------------------------->change
         # gate() ----------------------------->change
         '''
          print("after gate handler")
          while(True):
            try:
                print("Sleep",)
                time.sleep(0.5)
                if(os.path.isfile("/tmp/output_face_answer.txt")):
                    print("if output_face_answer")
                    answerFile = open("/tmp/output_face_answer.txt", "r")
                    faceAnswerStr = answerFile.read()
                    answerFile.close()
                    os.remove("/tmp/output_face_answer.txt")
                    print("end if output_face_answer")
                    #fuDB.SetVisitorName(faceAnswerStr)
                    #publish (faceAnswerStr) ????
                elif (os.path.isfile("/tmp/output_lp_answer.txt")):
                    print("elif output_lp_answer")
                    answerFile = open("/tmp/output_lp_answer.txt", "r")
                    lpAnswerStr = answerFile.read()
                    answerFile.close()
                    os.remove("/tmp/output_lp_answer.txt")
                    print("end elif output_lp_answer")
                    #fuDB.SetVisitorCarLisencePlate(lpAnswerStr)
                    #bId,bLp = fuDB.GetAuthorizedParams()
                    bId = True
                    bLp = True
                    if(bId):
                     client.publish("FaceFlag", "True")
                     print("publish - FaceFlag True")
                    else:
                     client.publish("FaceFlag", "False")
                     print("publish - FaceFlag False")

                    if (bLp):
                     client.publish("LPFlag", lpAnswerStr)
                     print("publish - LPFlag True")
                    else:
                     client.publish("LPFlag", "")
                     print("publish - LPFlag False")
                    print("break")
                break
            except:
                print("except ocured - 1!")
            #client.publish("FaceFlag_T",faceAnswerStr)


          #faceRecognition.ProcessImage()


#broker_address = "10.100.102.24"  # 127.0.0.1
# broker_address="iot.eclipse.org"
print("creating new instance")
client = mqtt.Client("FaceDetectionClient")  # create new instance
client.on_message = on_message  # attach function to callback
print("connecting to broker")
client.connect(MQTT_SERVER)  # connect to broker
print("Subscribing to topic", "ImageFaceArrived")
client.subscribe("ImageFaceArrived")
print("Subscribing to topic", "ImageLpArrived")
client.subscribe("ImageLpArrived")
time.sleep(4)  # wait
#client.loop_start()  # start the loop
#time.sleep(40000)  # wait
#client.loop_stop() #stop the loop
########################################



def begin():
    time.sleep(0.0005)


begin()
global g_IsFirstHndler
g_IsFirstHndler = True
global g_inOpenGate
g_inOpenGate = -1

time.sleep(3)

time.sleep(2)

client.loop_start()  # start the loop

bReady = True
while 1:

    d = time.strftime("%d %b %Y")
    t = time.strftime("%H:%M:%S")

    if g_inOpenGate == 1:
       print("While g_inOpenGate:"+str(g_inOpenGate))
       time.sleep(4)
    elif g_inOpenGate == 0 :
        print("While g_inOpenGate:"+str(g_inOpenGate))
        g_inOpenGate = -1
        print("While g_inOpenGate:"+str(g_inOpenGate))
        time.sleep(2)

    if bReady :
       print("....... Ready! ........")
       bReady = False
    time.sleep(0.5)
