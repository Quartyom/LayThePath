import os

start = int(input("start: "))
end = int(input("end: "))
shift = int(input("shift: "))

name = end
while name >= start:
    os.rename(str(name)+".json", str(name + shift) + ".json")
    name -= 1
