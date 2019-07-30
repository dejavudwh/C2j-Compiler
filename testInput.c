void quicksort(int A[10], int p, int r) {
int x;
int i;
i = p - 1;
int j;
int t;
int v;
v = r - 1;
if (p < r) {
x = A[r];
for (j = p; j <= v; j++) {
if (A[j] <= x) {
i++;
t = A[i];
A[i] = A[j];
A[j] = t;
}
}
v = i + 1;
t = A[v];
A[v] = A[r];
A[r] = t;
t = v - 1;
quicksort(A, p, t);
t = v + 1;
quicksort(A, t, r);
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