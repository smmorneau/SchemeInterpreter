(define foo (lambda (x) (lambda (y) (+ x y))))
(define a (foo 3))
(a 1)
; x and y should not be defined in the global environment (only in the closure)
; so the below statements should throw an error
x
y