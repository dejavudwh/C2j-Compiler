void f() {
    struct tag {
        int a;
        int b;
    } st;

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
    return;
}