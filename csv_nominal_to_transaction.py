import csv
import numpy as np
import array as arr
import os

columnCount = 23

positiveClass = 'e'
delim = ','

cls = 'splice_n'


with open('pythoninput/' + cls + '/' + cls + '.data', 'rb') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=delim, quotechar='|')
    lss = list(spamreader)

lines = np.chararray((1, columnCount))
with open('pythoninput/' + cls + '/' + cls + '.data', 'rb') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=delim, quotechar='|')
    for row in spamreader:
        r = np.chararray((1,columnCount))
        for i in range(columnCount):
            r[0, i] = row[i]
        lines = np.concatenate((lines, r), axis=0)

lines = np.delete(lines, 0, axis=0)

labels = lines[:, 0]
rowCount = lines.shape[0]
firstColumn = labels.reshape((rowCount, 1))
miniRep = None
uqs = np.ndarray(0)
meanings = ""
totalattri = 0
for i in range(1,columnCount):
    col = lines[:, i]
    uniques = np.unique(col)
    uqs = np.concatenate((uqs, uniques))
    newcols = np.chararray((rowCount, uniques.size))
    newcols[:] = '0'
    meanings += str(totalattri+1)
    totalattri += uniques.size
    mincol = np.ndarray((rowCount, 1))
    meanings += ":" +str(totalattri)+" "+ str(uniques) + "\n"
    for j in range(uniques.size):
        char = uniques[j]
        tr = col == char
        newcols[tr, j] = '1'
        nu = totalattri + 1 + j - uniques.size
        mincol[tr, 0] = nu

    if miniRep is None:
        miniRep = mincol
    else:
        miniRep = np.concatenate((miniRep, mincol), axis=1)

    firstColumn = np.concatenate((firstColumn, newcols), axis=1)
    a = 4

positiveBinary = firstColumn[firstColumn[:,0] == positiveClass, 1:]
negativeBinary = firstColumn[firstColumn[:,0] != positiveClass, 1:]

if not os.path.exists('pythonout/' + cls +'/'):
    os.makedirs('pythonout/' + cls +'/')


with open('pythonout/' + cls + '/binary negative output.txt', 'wr') as csvfile:
    csvfile.write(str(negativeBinary.shape[0]) + " " + str(negativeBinary.shape[1]) + "\n")
    cw = csv.writer(csvfile, delimiter=' ',
               quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(negativeBinary.shape[0]):
        cw.writerow(negativeBinary[i])

with open('pythonout/' + cls + '/binary positive output.txt', 'wr') as csvfile:
    csvfile.write(str(positiveBinary.shape[0]) + " " + str(positiveBinary.shape[1]) + "\n")
    cw = csv.writer(csvfile, delimiter=' ',
               quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(positiveBinary.shape[0]):
        cw.writerow(positiveBinary[i])

text_file = open("pythonout/"+cls+"/meanings output.txt", "w")
text_file.write(meanings + "\n" + str(totalattri)+"\n")
text_file.write(str(uqs))
text_file.close()


positiveIndices = labels == positiveClass
negativeIndices = labels != positiveClass

positiveItemsets = miniRep[positiveIndices].astype(int)
negativeItemsets = miniRep[negativeIndices].astype(int)
delim = ' '
with open('pythonout/'+cls+'/positivetransactions.data', 'wr') as csvfile:
    cw = csv.writer(csvfile, delimiter=delim,
               quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(positiveItemsets.shape[0]):
        cw.writerow(positiveItemsets[i])

with open('pythonout/'+cls+'/negativetransactions.data', 'wr') as csvfile:
    cw = csv.writer(csvfile, delimiter=delim,
               quotechar='|', quoting=csv.QUOTE_MINIMAL)
    for i in range(negativeItemsets.shape[0]):
        cw.writerow(negativeItemsets[i])