import numpy as np
import csv
import tensorflow as tf
import random

environmentdatalist =[]
sitdatalist = []
standatalist = []
walkdatalist = []
file = "environment480"
with open(file) as f:
    for x in f:
        environmentdatalist.append([[float(n) for n in x.strip().split(' ')],[1,0,0,0]])
# print environmentdatalist

filesit = "onesit480"
with open(filesit) as f1:
    for x in f1:
        sitdatalist.append([[float(n) for n in x.strip().split(' ')],[0,1,0,0]])

filestand = "standingin480"
with open(filestand) as f2:
    for x in f2:
        standatalist.append([[float(n) for n in x.strip().split(' ')],[0,0,1,0]])

filewalk = "walkingin480"
with open(filewalk) as f3:
    for x in f3:
        walkdatalist.append([[float(n) for n in x.strip().split(' ')],[0,0,0,1]])

w_datalist = environmentdatalist + sitdatalist+standatalist  + walkdatalist

labellist =[]
distancelist =[]
for x in range(len(w_datalist)):
    labellist.append(w_datalist[x][1])
    distancelist.append(w_datalist[x][0])

def random_validation(v_list):
    datalistrandom = []
    lablelistrandom = []
    number20percent = int(len(v_list)*0.2)
    for x in range(number20percent):
        e = random.randint(0, len(v_list) - 1)
        datalistrandom.append(v_list[e][0])
        lablelistrandom.append(v_list[e][1])
    return datalistrandom, lablelistrandom


def split_label(v_list):
    v_labellist=[]
    for x in range(len(v_list)):
        v_labellist.append(v_list[x][1])
    return v_labellist

def split_data(v_list):
    v_datalist=[]
    for x in range(len(v_list)):
        v_datalist.append(v_list[x][0])
    return v_datalist
# print w_datalist
# print len(w_datalist)

# foo = ['a', 'b', 'c', 'd', 'e']
# print (foo[random.randint(0,len(foo)-1)])

# print len(datalist)
#
# lable = []
# for y in range(len(datalist)):
#     lable.append([1,0,0,0,0])
#
# with open("labelValue","w") as lablefile:
#     lablefileWrite = csv.writer(lablefile)
#     for y in lable:
#         lablefileWrite.writerow(y)
# lablefile.close()

# indices = [0, 2, -1, 1]
# depth = 3
# on_value = 5.0
# off_value = 0.0
# axis = -1
# print tf.one_hot(indices,depth,on_value,off_value,axis,dtype=tf.float32)

# x = np.ones((1,2,3))
#
# print x
#
# print '---'
# print np.transpose(x,(1,0,2))