SchemeInterpreter
=================

A parse tree constructor, recursive descent parser, and interpreter for Scheme.

Limitations:  
	- Only runs single-line expressions.  
        - Only supports integer arithmetic.

Run REPL mode:
	java -jar scheme.jar

Run .scm files:
    java -jar scheme.jar {file_name}

Examples:

$ java -jar scheme.jar sample_scheme/test.scm  
3628800  
3  
4

----------------------------------------------------------------------------------------------------------

$ java -jar scheme.jar  
Scheme Interpreter v3 by Steely Morneau  

1 ]=> (define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))  
;Value: fact

2 ]=> (display (fact 10))  
3628800

3 ]=> (define first car)  
;Value: first

4 ]=> (define rest cdr)  
;Value: rest

5 ]=> (define count (lambda (item L) (if (not (null? L)) (+ (if (equal? item (first L)) 1 0) (count item (rest L))) 0)))  
;Value: count

6 ]=> (display (count 0 (list 0 1 2 3 0 0)))  
3

7 ]=> (display (count (quote the) (quote (the more the merrier the bigger the better))))  
4

8 ]=> ^C
