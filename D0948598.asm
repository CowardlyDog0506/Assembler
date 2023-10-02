HW3		START	128
.主程式
HERE	LDS		#3
		JSUB	INPUT
		JSUB	LCM	
		ADDR	S, X
		LDB		#15
		COMPR	X, B 
		JLT		HERE
		LDX		#0
		LDA		#128
		J		OUTPUT
		
.最大公因數
MCF		LDA		NUM1
.判斷NUM1跟NUM2誰比較小，可節省時間
		COMP	NUM2
		JLT		USE1
		+LDA	NUM2, X
		J		USE2
		BASE	SEC2
.判斷是NUM2的因數	
USE1	LDB		NUM2
		DIVR	A, B
		MULR	A, B
		LDT		NUM2
		SUBR	B, T
		STT		R
		LDB		#0
		COMPR	T, B
		STA		MANS, X
		+JEQ	SEC1
		STA		MANS, X
		SUB		#1
		J		USE1
.判斷是NUM1的因數		
SEC1	LDB		NUM1
		DIVR	A, B
		MULR	A, B
		LDT		NUM1
		SUBR	B, T
		STT		R
		LDB		#0
		COMPR	T, B
		STA		MANS, X
		SUB		#1	
		JGT		USE1
		RSUB
.判斷是NUM1的因數		
USE2	LDB		NUM1
		DIVR	A, B
		MULR	A, B														
		LDT		NUM1
		SUBR	B, T
		STT		R
		LDB		#0
		COMPR	T, B
		JEQ		SEC2
		STA		MANS, X
		SUB		#1
		J		USE2	
.判斷是NUM2的因數
SEC2	LDB		NUM2
		DIVR	A, B
		MULR	A, B
		LDT		NUM2
		SUBR	B, T
		STT		R
		LDB		#0
		COMPR	T, B
		STA		MANS, X
		SUB		#1
		JGT		USE2
		RSUB
.最小公倍數		
LCM		LDA		NUM1
		LDB		NUM2
		MULR	B, A
		STA		TMP
		STL		RTMP
		JSUB	MCF
		LDA		TMP
		DIV		MANS, X
		STA		LANS, X
		LDL		RTMP
		RSUB
JK		RESW 	1
.輸入		
INPUT	TD 		DEV5
		JEQ 	INPUT
		LDA		#0
		RD		DEV5
		SUB		#48
		MUL		#10
		STA		NUM1
		RD		DEV5
		SUB		#48
		ADD		NUM1
		STA		NUM1
		RD		DEV5
		RD		DEV5
		SUB		#48
		MUL		#10
		STA		NUM2
		RD		DEV5
		SUB		#48
		ADD		NUM2
		STA		NUM2
		STA		NUM2
		RD		DEV5
		RD		DEV5
		RSUB
.輸出最大公因數和最小公倍數
OUTPUT	LDA		#0
		STA		TMP
		LDA		MANS, X
		DIV		#1000
		JSUB	ZERO
		LDA		MANS, X
		DIV		#100
		JSUB	ZERO
		LDA		MANS, X
		DIV		#10
		JSUB	ZERO
		LDA		MANS, X
		JSUB	ZERO
		LDA		#32
		WD		DEV6
		LDA		#0
		STA		TMP
		LDA		LANS, X
		DIV		#1000
		JSUB	ZERO
		LDA		LANS, X
		DIV		#100
		JSUB	ZERO
		LDA		LANS, X
		DIV		#10
		JSUB	ZERO
		LDA		LANS, X
		JSUB	ZERO
		LDA		#10
		WD		DEV6
		LDA		#0
		STA		TMP
		ADDR	S, X
		LDB		#15
		COMPR	X, B 
		JLT		OUTPUT
		J		FINAL
		
ZERO	STA		NUM1
		LDA		#10
		LDB		NUM1
		DIVR	A, B
		MULR	A, B
		LDT		NUM1
		SUBR	B, T
		LDA		#0
		STT		R
.判斷是不是開頭
		COMP	TMP
		JLT		PRINT
.判斷是否為零
		COMPR	T, A
		JGT		PRINT
		RSUB
.印出
PRINT	LDA		R
		ADD		#48
		WD		DEV6
		LDA		#1
		STA		TMP
		TIXR 	X
		RSUB
FINAL	J		FINAL		
DEV5	BYTE  	X'05'	
DEV6	BYTE  	X'06'
Q		RESW	1
R		RESW	1
TMP		RESW	1
RTMP	RESW	1
NUM1	RESW	1
NUM2	RESW	1
MANS	RESW	5
LANS	RESW	5
	    END		HERE