import datetime
import time
filename='pipelining.py'



while(1):


    try:
        with open(filename, "rb") as source_file:
            code = compile(source_file.read(), filename, "exec")

        d = datetime.datetime.now().minute

        # print(d)
        #print(d,type(d))
        if d==0:
            exec(code)
            f = open('./log.txt', 'a')
            f.write(str(datetime.datetime.now())+ " EXECUTED\n ")
            f.close()
        else:
            time.sleep(55)

    except Exception as e:
        f = open('./log.txt', 'a')
        print()
        f.write(str(datetime.datetime.now()) + " NOT EXECUTED "+ str(e))
        f.close()


# exec(code, globals, locals)
# # exec(compile(open('pipelining.py', "rb").read(), 'pipelining.py', 'exec'))
# print('this\nankur')