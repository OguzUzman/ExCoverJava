import csv
import numpy as np
import array as arr
import os
import re

maxAttributeIndex = 92
maxAttributeNum = maxAttributeIndex + 1

positiveClass = '+'
delim = ' '

cls = 'anneal'

infolder = 'kminput'
outfolder = 'kmoutput'

positives = []
negatives = []

with open(infolder + '/' + cls + '.txt', 'rb') as csvfile:
    for line in csvfile:
        splitted = re.split(" ", line)
        sz = np.shape(splitted)[0]
        transaction = np.zeros( maxAttributeNum , dtype=np.int)
        for i in range(1, sz):
            transaction[int(splitted[i])] = 1
        if splitted[0] == positiveClass:
            positives.append(transaction)
        else:
            negatives.append(transaction)

if not os.path.exists(outfolder + '/' + cls +'/'):
    os.makedirs(outfolder + '/' + cls +'/')

with open(outfolder + '/' + cls + '/binary negative output.txt', 'wr') as csvfile:
    csvfile.write(str(np.shape(negatives)[0]) + " " + str(np.shape(negatives)[1]) + "\n")
    cw = csv.writer(csvfile, delimiter=' ',
                    quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(np.shape(negatives)[0]):
        cw.writerow(negatives[i])

with open(outfolder + '/' + cls + '/binary positive output.txt', 'wr') as csvfile:
    csvfile.write(str(np.shape(positives)[0]) + " " + str(np.shape(positives)[1]) + "\n")
    cw = csv.writer(csvfile, delimiter=' ',
                    quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(np.shape(positives)[0]):
        cw.writerow(positives[i])


