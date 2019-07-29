void quicksort(int a[10], int p, int r) {
    int x;
    int i;
    i = p - 1;
    int j;
    int t;
    int v;
    v = r - 1;
    if (p < r) {
        x = a[r];
        for (j = p; j <= v; j++) {
            if (a[j] <= x) {
                i++;
                t = a[i];
                a[i] = a[j];
                a[j] = t;
            }
        }
        v = i + 1;
        t = a[v];
        a[v] = a[r];
        a[r] = t;
        t = v - 1;
        quicksort(a, p, t);
        t = v + 1;
        quicksort(a, t, r);
    }
}
void main () {
    int a[10];
    int i;
    int t;
    printf("before quick sort:");
    for(i = 0; i < 10; i++) {
        t = (10 - i);
        a[i] = t;
        printf("value of a[%d] is %d", i, a[i]);
    }
    quicksort(a, 0, 9);
    printf("after quick sort:");
    for (i = 0; i < 10; i++) {
        printf("value of a[%d] is %d", i, a[i]);
    }
}