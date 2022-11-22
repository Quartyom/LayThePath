import random

def get_distr_arr(n_from, n_to):
    mid_range = int((n_from + n_to) * 0.5)

    incr = mid_range - n_from

    arr = []

    for i in range(n_from, mid_range + 1):
        arr += [i] * incr
        incr += 1

    if (n_to-n_from)%2 == 0:
        incr -= 2
    else:
        incr -= 1
    for i in range(mid_range + 1, n_to + 1):
        arr += [i] * incr
        incr -= 1

    return arr

def get_arr(n_from, n_to, n_mid):

    sh_left = n_mid - n_from
    sh_right = n_to - n_mid

    if (sh_left >= sh_right):
        incr = sh_left
    else:
        incr = sh_right + (sh_right - sh_left)

    arr = []

    for i in range(n_from, n_mid + 1):
        arr += [i] * incr
        incr += 1

    incr -= 2
    
    for i in range(n_mid + 1, n_to + 1):
        arr += [i] * incr
        incr -= 1

    return arr

    
arr = get_arr(4, 8, 5)

def f():
    return random.choice(arr)

distr = [0] * 15
shift = 2

for i in range(100000):
    distr[f() + shift] += 1

print(distr)
