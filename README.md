# thread-programming-java
exercises about thread programming with java

# Question1
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

# Question2

You will need to write a program in JAVA that will initially load its lines
file in a table, and then create k threads, each of which will
undertakes the editing of a part of the table. The program will calculate the
following:
1) the episode in which the dialogues had the largest number of words
2) the location where most of the verse tales took place
3) For each of the characters Bart, Homer, Margie and Lisa, print the most
common word they use (from 5 characters and up) as well as how many times the
used.

# Question3

We will use open APIs which give us information
as a text and from them we will extract various statistics. Indicatively, you can
use the following APIs, which generate plain text HTTP dialing
GET method:
https://loripsum.net/api/10/plaintext/

For the job you need to develop a program in JAVA which will use 1,
2, 4 or 8 threads to make a number of calls k per thread, to one of the
above APIs (will be given parametrically) to calculate the following:
1) the average length of the words in the text from all the texts produced. In every
execution (k-calls on n-threads) will print a number.
2) the percentage of appearances of the characters of the English alphabet from all the texts that
were produced. In each execution (k-calls on n-threads) 26 numbers will be printed,
whose sum will be 100.
For both questions ignore punctuation.

# Question4  (Semaphores)

In this subproject we will deal with a problem that contains synchronization and
mutual exclusion. We will simulate the operation of a sales store
clothing. The operation of the store is based on the following rules that must
implement:

- The store can accommodate up to 40 people at a time.
- There are two clothing testing areas, one for women, one for men and each
rehearsal room has 5 separate rooms.
- There is a cash register in the store but due to COVID restrictions it is not
allows more than 10 people to be in line.

Implement the above functionality using semantics assuming that
percentage of women-men in the store is 50% -50%, considering the time
Cashier's service is fixed once it is our turn to pay
(eg, 5 seconds) and also assuming that each person needs 3 to 10
seconds to try on clothes in the lab before going to the checkout. Also to
Consider that every 2 to 5 seconds a new customer enters the store for
to try and buy clothes 

Note: you can change the above intervals to try them out
system capabilities in less time. Just make sure it is checked
functionality in all cases. For example, do we only allow 40 people in the store?
Do we only allow 5 people in the women's lab?


