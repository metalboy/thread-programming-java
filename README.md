# thread-programming-java
exercises about thread programming with java

# exercise 1
It is known from Linear Algebra that we can
multiply a table by a vector from the right, as long as the number of its columns is sufficient
array is equal to the number of rows of the vector.

For example if we have
an array A with dimensions (n x m) and a vector (v) with dimensions (m x 1), then the product (A * v) equals   
a vector (n x 1) by applying the known array multiplication method with vector. An example is given 
in the following figure.

<img src="https://latex.codecogs.com/gif.latex?%5Cbg_white%20%5Cbegin%7Bbmatrix%7D%20%261%20%260%20%262%20%260%20%26%5C%5C%20%260%20%263%20%260%20%264%20%26%5C%5C%20%260%20%260%20%265%20%260%20%26%5C%5C%20%266%20%260%20%260%20%267%20%26%20%5Cend%7Bbmatrix%7D%20*%20%5Cbegin%7Bbmatrix%7D%202%5C%5C%205%5C%5C%201%5C%5C%208%20%5Cend%7Bbmatrix%7D%3D%20%5Cbegin%7Bbmatrix%7D%204%5C%5C%2047%5C%5C%205%5C%5C%2068%20%5Cend%7Bbmatrix%7D" />

Assuming we have k threads, where k is a power of 2 and the array has
dimensions n x m where n is also a force of 2 and n> k, draw a solution that
calculates the product A * v using the k threads in the best possible way. The
Your program should "fill" table A and vector v with random numbers
between 0 and 10
