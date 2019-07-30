.class public C2Bytecode
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
	sipush	10
	newarray	int
	astore	0
	sipush	0
	istore	1
	sipush	0
	istore	2
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"Array before quicksort:"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"
"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	sipush	0
	istore	1

loop0:
	iload	1
	sipush	10
if_icmpge branch0
	sipush	10
	iload	1
	isub
	istore	2
	aload	0
	iload	1
	iload	2
	iastore
	aload	0
	iload	1
	iaload
	istore	3
	iload	1
	istore	4
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"value of a["
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	iload	4
	invokevirtual	java/io/PrintStream/print(I)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"] is "
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	iload	3
	invokevirtual	java/io/PrintStream/print(I)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"
"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	iload	1
	sipush	1
	iadd
	istore	1
goto loop0
branch0:
	aload	0
	sipush	0
	sipush	9
	invokestatic	C2Bytecode/quickSort([III)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"Array after quicksort:"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"
"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	sipush	0
	istore	1

loop2:
	iload	1
	sipush	10
if_icmpge branch4
	aload	0
	iload	1
	iaload
	istore	3
	iload	1
	istore	4
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"value of a["
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	iload	4
	invokevirtual	java/io/PrintStream/print(I)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"] is "
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	iload	3
	invokevirtual	java/io/PrintStream/print(I)V
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"
"
	invokevirtual	java/io/PrintStream/print(Ljava/lang/String;)V
	iload	1
	sipush	1
	iadd
	istore	1
goto loop2
branch4:
	return
.end method
.method public static quickSort([III)V
	sipush	0
	istore	7
	sipush	0
	istore	3
	iload	1
	sipush	1
	isub
	istore	3
	sipush	0
	istore	4
	sipush	0
	istore	5
	sipush	0
	istore	6
	iload	2
	sipush	1
	isub
	istore	6
	iload	1
	iload	2
if_icmpge branch1

	aload	0
	iload	2
	iaload
	istore	7
	iload	1
	istore	4

loop1:

	iload	4
	iload	6
if_icmpgt ibranch1

	aload	0
	iload	4
	iaload
	iload	7
if_icmpgt ibranch2

	iload	3
	sipush	1
	iadd
	istore	3
	aload	0
	iload	3
	iaload
	istore	5
	aload	0
	iload	3
	aload	0
	iload	4
	iaload
	iastore
	aload	0
	iload	4
	iload	5
	iastore
ibranch2:

	iload	4
	sipush	1
	iadd
	istore	4
goto loop1

ibranch1:

	iload	3
	sipush	1
	iadd
	istore	6
	aload	0
	iload	6
	iaload
	istore	5
	aload	0
	iload	6
	aload	0
	iload	2
	iaload
	iastore
	aload	0
	iload	2
	iload	5
	iastore
	iload	6
	sipush	1
	isub
	istore	5
	aload	0
	iload	1
	iload	5
	invokestatic	C2Bytecode/quickSort([III)V
	iload	6
	sipush	1
	iadd
	istore	5
	aload	0
	iload	5
	iload	2
	invokestatic	C2Bytecode/quickSort([III)V
branch1:

	return
.end method

.end class
