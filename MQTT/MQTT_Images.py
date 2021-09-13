import RPi.GPIO as gpio
import picamera
import time
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

MQTT_SERVER ="192.168.42.173"   #Write Server IP Address
MQTT_PATH = "VisitorImage"
MQTT_PATH_FACE_IMAGE = "ImageFaceArrived"
MQTT_PATH_LP_IMAGE = "ImageLpArrived"

########### Raspbery  Init ###################
m11 = 17
m12 = 27
led = 5
buz = 26

button = 19

RS = 18
EN = 23
D4 = 24
D5 = 16
D6 = 20
D7 = 21

HIGH = 1
LOW = 0

gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(RS, gpio.OUT)
gpio.setup(EN, gpio.OUT)
gpio.setup(D4, gpio.OUT)
gpio.setup(D5, gpio.OUT)
gpio.setup(D6, gpio.OUT)
gpio.setup(D7, gpio.OUT)
gpio.setup(led, gpio.OUT)
gpio.setup(buz, gpio.OUT)
gpio.setup(m11, gpio.OUT)
gpio.setup(m12, gpio.OUT)
gpio.setup(button, gpio.IN)
gpio.output(led, 0)
gpio.output(buz, 0)
gpio.output(m11, 0)
gpio.output(m12, 0)
data = ""
global g_inOpenGate
global g_IsFirstHndler

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
    if g_IsFirstHndler :
       g_IsFirstHndler = False
       return

    gate()
    print("my status is:")
    Data = ""
    Topic = ""
    bDataOn = False
    bInOpenGate = False
    Data = str(message.payload.decode("utf-8"))
    Topic = str(message.topic.decode("utf-8"))
    print("message received ", Data)
    print("message topic=", Topic)
    print("message qos=", message.qos)
    print("message retain flag=", message.retain)



    bInOpenGate = AreStringsEqual(Topic,'OpenGate')
    bDataOn = AreStringsEqual(Data,'ON')
    print("bInOpenGate",bInOpenGate)
    print("bDataOn",bDataOn)
    if bInOpenGate :
       print("outer!!!")
       if bDataOn :

          print("before gate handler")
         # gate() ----------------------------->change
         # gate() ----------------------------->change
          print("after gate handler")
          time.sleep(0.5)


#broker_address = "10.100.102.24"  # 127.0.0.1
# broker_address="iot.eclipse.org"
print("creating new instance")
client = mqtt.Client("GateClient")  # create new instance
client.on_message = on_message  # attach function to callback
print("connecting to broker")
client.connect(MQTT_SERVER)  # connect to broker
print("Subscribing to topic", "OpenGate")
client.subscribe("OpenGate")
time.sleep(4)  # wait
#client.loop_start()  # start the loop
#time.sleep(40000)  # wait
#client.loop_stop() #stop the loop
########################################


########################################
    
def capture_image1():
    lcdcmd(0x01)
    lcdprint("Please Wait..");
    data = time.strftime("%d_%b_%Y\%H:%M:%S")
    camera.start_preview()
    time.sleep(5)
    print(data)
    #sImageFileName = '/home/pi/Desktop/Visitors/%s.jpg' % data
    sImageFileName_face = '/home/pi/Desktop/Visitors/face_image.jpg'
    print("----Image 1-------")
    camera.capture(sImageFileName_face)
    camera.stop_preview()
    lcdcmd(0x01)
    lcdprint("Face Image Captured")
    lcdcmd(0xc0)
    lcdprint(" Successfully ")
    time.sleep(6)
    #MQTT_PATH = "Image"
  #  storage.child("Images").child("face_image.jpg").put(sImageFileName_face)

    f=open(sImageFileName_face, "rb") #3.7kiB in same folder
    fileContent = f.read()
    byteArr = bytearray(fileContent)

    publish.single(MQTT_PATH_FACE_IMAGE, byteArr, hostname=MQTT_SERVER)
    lcdcmd(0x01)
    lcdprint("Face Image Sent")
    time.sleep(2)
    lcdcmd(0xc0)
 

    
def capture_image2():
    lcdcmd(0x01)
    lcdprint("Please Wait 2..");
    data = time.strftime("%d_%b_%Y\%H:%M:%S")
    camera.start_preview()
    time.sleep(5)
    print(data)
    #sImageFileName = '/home/pi/Desktop/Visitors/%s.jpg' % data
    sImageFileName_lp = '/home/pi/Desktop/Visitors/lp_image.jpg' 
    print("----Image 2-------")
    camera.capture(sImageFileName_lp)
    camera.stop_preview()
    lcdcmd(0x01)
    lcdprint("LP Image Captured")
    lcdcmd(0xc0)
    lcdprint(" Successfully ")
    time.sleep(6)
    #MQTT_PATH = "Image"
    #storage.child("Images").child("lp_image.jpg").put(sImageFileName_lp)
    f=open(sImageFileName_lp, "rb") #3.7kiB in same folder
    fileContent = f.read()
    byteArr = bytearray(fileContent)

    publish.single(MQTT_PATH_LP_IMAGE, byteArr, hostname=MQTT_SERVER)
    lcdcmd(0x01)
    lcdprint("Lp Image Sent")
    time.sleep(2)
    lcdcmd(0xc0)

def gate():
    global g_inOpenGate
    g_inOpenGate = 1
    #lcdcmd(0x01)
    #lcdprint("    Welcome  ")
    print("Welcome g_inOpenGate"+str(g_inOpenGate))
    gpio.output(m11, 1)
    gpio.output(m12, 0)
    time.sleep(0.25)
    gpio.output(m11, 0)
    gpio.output(m12, 0)
    time.sleep(3)
    gpio.output(m11, 0)
    gpio.output(m12, 1)
    time.sleep(0.25)
    gpio.output(m11, 0)
    gpio.output(m12, 0)
    #lcdcmd(0x01);
    #lcdprint("  Thank You  ")
    #time.sleep(2)
    g_inOpenGate = 0
    print("Thankx g_inOpenGate"+str(g_inOpenGate))

def begin():
    lcdcmd(0x33)
    lcdcmd(0x32)
    lcdcmd(0x06)
    lcdcmd(0x0C)
    lcdcmd(0x28)
    lcdcmd(0x01)
    time.sleep(0.0005)


def lcdcmd(ch):
    gpio.output(RS, 0)
    gpio.output(D4, 0)
    gpio.output(D5, 0)
    gpio.output(D6, 0)
    gpio.output(D7, 0)
    if ch & 0x10 == 0x10:
        gpio.output(D4, 1)
    if ch & 0x20 == 0x20:
        gpio.output(D5, 1)
    if ch & 0x40 == 0x40:
        gpio.output(D6, 1)
    if ch & 0x80 == 0x80:
        gpio.output(D7, 1)
    gpio.output(EN, 1)
    time.sleep(0.005)
    gpio.output(EN, 0)

    # Low bits
    gpio.output(D4, 0)
    gpio.output(D5, 0)
    gpio.output(D6, 0)
    gpio.output(D7, 0)
    if ch & 0x01 == 0x01:
        gpio.output(D4, 1)
    if ch & 0x02 == 0x02:
        gpio.output(D5, 1)
    if ch & 0x04 == 0x04:
        gpio.output(D6, 1)
    if ch & 0x08 == 0x08:
        gpio.output(D7, 1)
    gpio.output(EN, 1)
    time.sleep(0.005)
    gpio.output(EN, 0)


def lcdwrite(ch):
    gpio.output(RS, 1)
    gpio.output(D4, 0)
    gpio.output(D5, 0)
    gpio.output(D6, 0)
    gpio.output(D7, 0)
    if ch & 0x10 == 0x10:
        gpio.output(D4, 1)
    if ch & 0x20 == 0x20:
        gpio.output(D5, 1)
    if ch & 0x40 == 0x40:
        gpio.output(D6, 1)
    if ch & 0x80 == 0x80:
        gpio.output(D7, 1)
    gpio.output(EN, 1)
    time.sleep(0.005)
    gpio.output(EN, 0)

    # Low bits
    gpio.output(D4, 0)
    gpio.output(D5, 0)
    gpio.output(D6, 0)
    gpio.output(D7, 0)
    if ch & 0x01 == 0x01:
        gpio.output(D4, 1)
    if ch & 0x02 == 0x02:
        gpio.output(D5, 1)
    if ch & 0x04 == 0x04:
        gpio.output(D6, 1)
    if ch & 0x08 == 0x08:
        gpio.output(D7, 1)
    gpio.output(EN, 1)
    time.sleep(0.005)
    gpio.output(EN, 0)


def lcdprint(Str):
    l = 0;
    l = len(Str)
    for i in range(l):
        lcdwrite(ord(Str[i]))


begin()
global g_IsFirstHndler
g_IsFirstHndler = True
global g_inOpenGate
g_inOpenGate = -1
lcdcmd(0x01)
lcdprint("Visitor Monitoring")
lcdcmd(0xc0)
lcdprint("      System     ")
time.sleep(3)
lcdcmd(0x01)
lcdprint("Secure Room")
lcdcmd(0xc0)
lcdprint("Smart Gate")
time.sleep(3)
lcdcmd(0x01)
camera = picamera.PiCamera()
camera.rotation = 180
camera.awb_mode = 'auto'
camera.brightness = 55
lcdcmd(0x01)
lcdprint(" Please Press ")
lcdcmd(0xc0)
lcdprint("    Button      ")
time.sleep(2)

client.loop_start()  # start the loop

bReady = True
while 1:

    d = time.strftime("%d %b %Y")
    t = time.strftime("%H:%M:%S")
    lcdcmd(0x80)
    lcdprint("Time: %s" % t)
    lcdcmd(0xc0)
    lcdprint("Date:%s" % d)
    gpio.output(led, 1)

    if g_inOpenGate == 1:
       print("While g_inOpenGate:"+str(g_inOpenGate))
       lcdcmd(0x01)
       lcdprint("    Welcome  ")
       time.sleep(4)
    elif g_inOpenGate == 0 :
        print("While g_inOpenGate:"+str(g_inOpenGate))
        lcdcmd(0x01)
        lcdprint("  Thank You  ")
        g_inOpenGate = -1
        print("While g_inOpenGate:"+str(g_inOpenGate))
        time.sleep(2)
    elif gpio.input(button) == 0 :

             print("g_inOpenGate:"+str(g_inOpenGate))
             gpio.output(buz, 1)
             gpio.output(led, 0)
             time.sleep(0.5)
             gpio.output(buz, 0)
             capture_image1()
             time.sleep(3)
             capture_image2()

             #gate()
             print("Publishing message to topic", "Visitor,ON")
             client.publish("Visitor","ON")

    if bReady :
       print("....... Ready! ........")
       bReady = False
    time.sleep(0.5)


