# -*- coding: UTF-8 -*-
import os,sys
import time
def cmp(a):
    return a.begin
class part:
    def __init__(self,id,begin,content):
        self.id=id
        self.begin=int(begin)
        self.content=content
        self.end=int(begin)+len(content)-1

str=[]
sum=""
with open ('data/ref.fa') as file:
    next(file)
    for line in file:
        sum=sum+line.replace('\n','')

file = open("data/result_sort.sam")
array_part=[]
end_array_part=[]

for line in file:
    lose = []
    lose_1 = []
    line=line.split()
    str_1 = line[9]
    if (line[5].__contains__("D")):
        lose = line[5].split("D")
        for index in range(0,len(lose)):
            lose_1.append(lose[index].split("M"))
        for index in range(0, len(lose_1)):
            if len(lose_1[index]) == 2 and index == 0 and lose_1[index][1] != '':
                str_1 = str_1[:int(lose_1[index][0])] + "*" * int(lose_1[index][1]) + str_1[int(lose_1[index][0]):]
            elif len(lose_1[index]) == 2 and index > 0 and lose_1[index][1] != '':
                str_1 = str_1[:(int(lose_1[index][0]) + int(lose_1[index - 1][1]) + int(lose_1[index - 1][0]))] + \
                        "*" * int(lose_1[index][1]) + \
                        str_1[(int(lose_1[index][0]) + int(lose_1[index - 1][1]) + int(lose_1[index - 1][0])):]
    else:
        lose = line[5].split("I")
        for index in range(0, len(lose)):
            lose_1.append(lose[index].split("M"))
        for index in range(0, len(lose_1)):
            if len(lose_1[index]) == 2 and index == 0 and lose_1[index][1] != '':
                str_1 = str_1[:int(lose_1[index][0])] + str_1[int(lose_1[index][0]) + int(lose_1[index][1]):]
            elif len(lose_1[index]) == 2 and index > 0 and lose_1[index][1] != '':
                str_1 = str_1[:int(lose_1[index][0]) + int(lose_1[index - 1][1])] + \
                        str_1[int(lose_1[index][0]) + int(lose_1[index - 1][1]) + int(lose_1[index][0]):]

    a=part(line[0],line[3],str_1)
    array_part.append(a)
file.close()
array_part.sort(key=cmp)
end_array_part=array_part

file_match = open("data/result_match","w+")
str_match = ""
str_id = ""
str_id = end_array_part[0].id

end_flag = end_array_part[0].end # end's flag
str_mes= " "*(end_array_part[0].begin-1)+end_array_part[0].content # first message
end_array_part.remove(end_array_part[0]) # delete the first message,and the length should be -1
len_of_array = len(end_array_part)  #remember the length now

file_patch = open("data/result_patch","w+")
file_id = open("data/result_id","w+")

while(len_of_array > 0 ):
    i = 0
    while(i<len_of_array):
        if end_array_part[i].begin > end_flag:
            str_mes += " "*(end_array_part[i].begin-end_flag-1)   #-1
            str_mes += end_array_part[i].content
            str_id += " "*(end_array_part[i].begin-end_flag-1)
            str_id += end_array_part[i].id+" "*(len(end_array_part[i].content)-len(end_array_part[i].id))
            end_flag = end_array_part[i].end
            end_array_part.remove(end_array_part[i])
            i = i-1
            len_of_array = len(end_array_part)
        i += 1
    for index in range(0,len(str_mes)):
        if index < len(sum) and str_mes[index] == sum[index]:
            str_match += ","
        else: str_match += str_mes[index]
    file_patch.write(str_mes+"\n")
    file_match.write(str_match+"\n")
    file_id.write(str_id+"\n")
    end_flag = 0
    str_mes= ""
    str_match = ""
    str_id = ""
    len_of_array = len(end_array_part)


