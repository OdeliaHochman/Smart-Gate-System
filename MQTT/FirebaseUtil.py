import pyrebase

id_flag = False
lp_flag = False
dictName2_id = {'Tony_Blair':'777777777', 'Hugo_Chavez':'666666666', 'Gerhard_Schroeder':'555555555',
                    'George_W_Bush': '444444444', 'Donald_Rumsfeld': '333333333', 'Colin_Powell': "222222222",
                    'Ariel_Sharon': "111111111"}

def InitFB():
    # setting up firebase
    firebaseConfig = {'apiKey': "AIzaSyDsk8ml48_460i6kX4aMwrkj1nHnUV5f-g",
                      'authDomain': "smart-gate-5414b.firebaseapp.com",
                      'databaseURL': "https://smart-gate-5414b-default-rtdb.firebaseio.com",
                      'projectId': "smart-gate-5414b",
                      'storageBucket': "smart-gate-5414b.appspot.com",
                      'messagingSenderId': "981695082072",
                      'appId': "1:981695082072:web:48de855263acbdd4d5a42d",
                      'measurementId': "G-C1BJFJXNRC"}

    firebase = pyrebase.initialize_app(firebaseConfig)

    db = firebase.database()

    # -----------------------------------------------------------------------------------------------------------------------
    ## this code is  supposed to get the place name from the FB but it doesn't work...we will need to get the placename from the app..
    # auth = firebase.auth()
    # user = auth.currentUser()
    # name = ""
    # if user is not None:
    #     name = user.displayName
    #     print(name)
    # ----------------------------------------------------------------------------------------------------------------------

    idnumber = ""  # "111111111"  # idnumber from face detection
    lpnumber = ""  # "MCLRNF1"  # lpnumber from lp detection
    placename = "Place1"  # we need to retrieve from FB
    #id_flag = False
    #lp_flag = False
    ref = db.child("Places").child(placename).child("Authorized People").shallow().get()
    print(ref.val())

    if idnumber in ref.val():
        id_flag = True
        ref_lp = db.child("Places").child(placename).child("Authorized People").child(idnumber).child("lpnumber").get()
        if ref_lp.val() == lpnumber:
            lp_flag = True


    #print("ID flag = {}".format(id_flag))
    #print("LP flag = {}".format(lp_flag))


def SetVisitorName(sVisitorName):
    idnumber = dictName2_id[sVisitorName]


def SetVisitorCarLisencePlate(sVisitorName):
    lpnumber = sVisitorName
    InitFB()


def GetAuthorizedParams():
    return (id_flag, lp_flag)


