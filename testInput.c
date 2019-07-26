void f() {
    printf("pTag[3] < 3");
}

void t() {
    printf("pTag[3] > 3");
}

void main() {
    struct TAG {
        char c;
        char b[3];
        int p;
    } tag;
    struct TAG* pTag;
    int sz ;
    sz = sizeof(tag);
    pTag = malloc(sz);

    pTag[0] = 3;
    pTag[1] = 4;
    pTag[2] = 5;
    pTag[3] = 6;

    printf("value of c is %d, b[0] is %d, b[1] is %d, b[2] is %d ", pTag->c, pTag->b[0], pTag->b[1], pTag->b[2]);

    int t;
    pTag->p = 5;
    t = pTag->p;
    t = 5-1;
    printf("t value: %d", t);
    if (t < 3) {
        f();
    } else {
        t();
    }
}