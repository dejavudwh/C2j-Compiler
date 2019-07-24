void f(int x, short y, long z);

void f(int x, short y, long z) {
    enum e {a , b, c};

    struct TAG {
        int v1;
        int v2;
        char v3;
    } tag;

    struct TAG myTag;
    struct TAG herTag;
    myTag.v1 = 1;
    herTag.v1 = 2;

    int a = 0;
    int i = 0;

    if (i < 1)
        a = 1;
    else if (i < 2)
        a = 2;
    else
        a = 3;

    for (i = 0;i < 3;i++) {
        int a =2;
    }

    while (true) {
        print("Being parsed %d", time);
    }

    switch(a) {
       case 1:
           print("1");
           break;
       case 2:
           print("2");
           break;
     }

    return;
}