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

    return;
}