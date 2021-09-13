import os
import urllib
import pyrebase


#setting up firebase
firebaseConfig={'apiKey': "AIzaSyDsk8ml48_460i6kX4aMwrkj1nHnUV5f-g",
  'authDomain': "smart-gate-5414b.firebaseapp.com",
  'databaseURL': "https://smart-gate-5414b-default-rtdb.firebaseio.com",
  'projectId': "smart-gate-5414b",
  'storageBucket': "smart-gate-5414b.appspot.com",
  'messagingSenderId': "981695082072",
  'appId': "1:981695082072:web:48de855263acbdd4d5a42d",
  'measurementId': "G-C1BJFJXNRC"}

firebase=pyrebase.initialize_app(firebaseConfig)

#define storage
storage=firebase.storage()


 #global idFromApp = we need to get id from the app after we add/update person details.


#download a file
#storage.child(cloudfilename).download("downloaded.txt")
#storage.child("Videos/video_11").download(filename="video_11.mp4")
storage.child("Videos/video_"+ idFromApp).download(filename="video_"+ idFromApp+".mp4")





#upload a file
# file=input("Enter the name of the file you want to upload to storage")
# cloudfilename=input("Enter the name for the file in storage")
# storage.child(cloudfilename).put(file)
#
# #get url of the file we just uploaded
# print(storage.child(cloudfilename).get_url(None))

#to read from the file
# path=storage.child(cloudfilename).get_url(None)
# f = urllib.request.urlopen(path).read()
# print(f)

#download a file
#storage.child(cloudfilename).download("downloaded.txt")
#storage.child("Videos/video_11").download(filename="video_11.mp4")


