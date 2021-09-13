import cv2
import os
import firebasestoragedemo


# Read the video from specified path
cam = cv2.VideoCapture("video+id+".mp4")
#cam = cv2.VideoCapture("video_11.mp4")
# id = firebasestoragedemo.idFromApp
# cam = cv2.VideoCapture("video_"+id+".mp4")

try:

    # creating a folder named data
   # if not os.path.exists('11'):
   if not os.path.exists(id):
        os.makedirs('11')

        # if not os.path.exists(id):
        #     os.makedirs(id)

# if not created then raise error
except OSError:
    print('Error: Creating directory of data')

# frame
currentframe = 0

while (True):

    # reading from frame
    ret, frame = cam.read()

    if ret:
        # if video is still left continue creating images
        name = './id/frame' + str(currentframe) + '.jpg'
        #name = './11/frame' + str(currentframe) + '.jpg'
        print('Creating...' + name)

        # writing the extracted images
        cv2.imwrite(name, frame)

        # increasing counter so that it will
        # show how many frames are created
        currentframe += 1
    else:
        break

# Release all space and windows once done
cam.release()
cv2.destroyAllWindows()