import os
from evdev import InputDevice
from select import select

deviceFilePath = '/sys/class/input/'

def showDevice():
    os.chdir(deviceFilePath)
    for i in os.listdir(os.getcwd()):
        namePath = deviceFilePath + i + '/device/name'
        if os.path.isfile(namePath):
            print "Name: %s Device: %s" % (i, file(namePath).read())

def detectInputKey():
    dev = InputDevice('/dev/input/event2')
    while True:
        select([dev], [], [])
        for event in dev.read():
            if (event.value == 1 or event.value == 0) and event.code != 0:
                print "Key: %s Status: %s" % (event.code, "pressed" if event.value else "release")

if __name__ == '__main__':
    showDevice()
    detectInputKey()