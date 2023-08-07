import os

i = int(input("i: "))
for filename in os.listdir("."):
    if ".py" not in filename:
        print(filename)
        os.rename(filename, str(i) + ".json")
        i+=1
