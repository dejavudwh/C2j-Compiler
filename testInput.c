void swap(int arr[10], int i, int j) {
    int temp;
    temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

void quickSort(int a[10], int p, int r) {
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
                swap(a, i, j);
            }
        }
        v = i + 1;
        swap(a, v, r);
        t = v - 1;
        quickSort(a, p, t);
        t = v + 1;
        quickSort(a, t, r);
    }
}

void main () {
    int a[10];
    int i;
    int t;
    int b;
    printf("Array before quicksort:");
    for(i = 0; i < 10; i++) {
        t = (10 - i);
        a[i] = t;
        printf("value of a[%d] is %d", i, a[i]);
    }

    quickSort(a, 0, 9);

    printf("Array after quicksort:");
    for (i = 0; i < 10; i++) {
        printf("value of a[%d] is %d", i, a[i]);
    }
}