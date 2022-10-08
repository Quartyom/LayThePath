def encrypt(n):
    k = int("1" + str(n)) + 39
    A =  6180339887
    d = 10000000000
    
    tmp = str(k * A % d)
    return tmp[len(tmp)-6 : len(tmp)]

def decrypt(code):
    for n in range(1000000, 1999999):
        trial = str(n)[1:]
        if code == encrypt(trial):
            print(trial)
            #return

    
while True:
    c = input()
    decrypt(c)
    print()
