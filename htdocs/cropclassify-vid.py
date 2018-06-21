#!C:\Users\khmkh\AppData\Local\Programs\Python\Python35\python.exe
import pymysql
import cgitb
import time, threading
cgitb.enable()
print("Content-Type: text/html;charset=utf-8")
print()

from keras.preprocessing.image import img_to_array
from keras.models import load_model
import cv2
import xml.etree.ElementTree as ET
import numpy as np
import argparse
import imutils


#python cropclassify.py --model meeting542018.model --xml real-parking.xml
#python cropclassify.py --model meeting542018.model --xml sunny-park.xml

# construct the argument parse and parse the arguments
#ap = argparse.ArgumentParser()
#ap.add_argument("-m", "--model", required=True,
#	help="path to trained model")
#ap.add_argument("-x", "--xml", required=True,
#	help="path to xml file")
#ap.add_argument("-s", "--save-file", required=True,
#	help="path to save cropped image")
#args = vars(ap.parse_args())

parking_dict = {}

print("PARSING XML")
tree = ET.parse('C:\\xampp\\htdocs\\video.xml')
root = tree.getroot()

print("[INFO] loading network...")
model = load_model('C:\\xampp\\htdocs\\meeting542018.model')	

print("CAPTURING IMAGE..")	
#for image in root.iter('image'):
#    print("CURRENT IMAGE" , image.attrib['file'])
#    image_name = image.attrib['file']
#    orig_img = cv2.imread(image_name)
cap = cv2.VideoCapture('C:\\xampp\\htdocs\\SAM_3303.AVI')
while(cap.isOpened()):
    ret, frame = cap.read()
    frame = cv2.resize(frame, (900, 700))
    #cv2.imshow('Original Image', frame)
    #cv2.waitKey(0)
    parking_lot_index = 1
	
    #Iterates over all bounding boxes found in each image
    for box in root.iter('box'):
        height = box.attrib['height']
        width = box.attrib['width']
        top = box.attrib['top']
        left = box.attrib['left']
		
        startX = int(left)
        endX = int(left) + int(width)
        startY = int(top)
        endY = int(top) + int(height)
		
		#Crops the image according to calculated parameters
        #print('Cropping Image..')
        cropped_img = frame[startY : endY, startX : endX]
        parking_lot_tag = 'Space' + str(parking_lot_index) + '.jpg'
        #cv2.imshow(parking_lot_tag, cropped_img)
        #cv2.waitKey(0)
        
		#Saves image to file
        #print('Saving Image')
        save_filename = r'C:\\xampp\\htdocs\\segmented-real\\' + parking_lot_tag
        #print(save_filename)
        cv2.imwrite(save_filename, cropped_img)
        parking_lot_index = parking_lot_index + 1
		
		
        #print('Classifying Image..')
        image = cv2.imread(save_filename)
        orig = image.copy()
		
        image = cv2.resize(cropped_img, (28, 28))
        image = image.astype("float") / 255.0
        image = img_to_array(image)
        image = np.expand_dims(image, axis=0)
        
        (Empty, Occupied) = model.predict(image)[0]
		
		#Work on whole frame
        #if(Occupied > Empty):
        #    cv2.rectangle(frame,(startX,startY),(endX,endY),(0,0,255),3)
        #else:
        #    cv2.rectangle(frame,(startX,startY),(endX,endY),(0,255,0),3)

        #cv2.imshow('frame',frame)
        #if (cv2.waitKey(1) & 0xFF == ord('q')):
        #    break
		
		# build the label
        label = "Occupied" if Occupied > Empty else "Empty"
        #proba = Occupied if Occupied > Empty else Empty
        #output = " {} : {} , {:.2f}%".format(parking_lot_tag, label, proba * 100)
		
		# draw the label on the image
        #output = imutils.resize(orig, width = 250)
        #cv2.putText(output, label, (10, 25),  cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 0), 2)

        # show the output image
        #cv2.imshow(parking_lot_tag, output)
        #cv2.waitKey(0)
		
        #print(output)
		
        parking_dict[parking_lot_tag.replace('.jpg','')]  = 1 if label == "Occupied" else 0
        print(parking_dict)
		
    conn = pymysql.connect(host='localhost',port=3307, user='root', password='', db='parking_test') 
    a = conn.cursor()
    
    sql = """SELECT COUNT(*) FROM parking_state"""
    a.execute(sql)
    (result,)= a.fetchone()
    print("RESULT!!")
    print(result)
    if(result == 0 ):
        flag = True
    else:
        flag = False
	
	#flag = True	
    if(flag):
        for i in parking_dict.items():   
            keys = i[0]
            values = i[1]
            id = int(keys[5:])
        #print(id)
            sql = """INSERT INTO parking_state (Space, State, ID) VALUES (%s, %s, %s)"""
        #sql = """UPDATE parking_state SET Space = %s, State = %s WHERE ID = %s """
            a.execute(sql, (keys, values, int(id)))
            flag = False
    else:
        for key in sorted(parking_dict):
            print ("%s: %s" % (key, parking_dict[key]))

        for i in parking_dict.items():   
            keys = i[0]
            values = i[1]
            id = int(keys[5:])
        #sql = """INSERT INTO parking_state (Space, State, ID) VALUES (%s, %s, %s)"""
            sql = """UPDATE parking_state SET Space = %s, State = %s WHERE ID = %s """
            a.execute(sql, (keys, values, int(id)))
            flag = False
		 
    conn.commit()
    conn.close()
	
    print(time.ctime())
    #threading.Timer(3).start()

#To sort the dictionary according to key values..
#print(sorted(parking_dict))