package exercise1;

import java.util.Random;

public class Question1 {
    private static final int THREAD_COUNT = 8; //we have k threads, where k is a force of 2
    private static final int N = 4096;         //dimension n
    private static final int M = 100000;       //dimension m
    private static final int[] oneDArray = new int[M];           //vector v with dimensions m x 1
    private static final short[][] twoDArray = new short[N][M];  //table A with dimensions n x m


//    Test commands for function test (assisted in debug):
//    private static final int N = 4;
//    private static final int M = 4;
//    private static int[] oneDArray = {2,5,1,8};
//    private static short[][] twoDArray = {{1,0,2,0},{0,3,0,4},{0,0,5,0},{6,0,0,7}};

    /*
    Your program should "fill" table A and vector v with random numbers
    With RandArray we fill the tables with Random values.
    */
    public static void RandArray(){
        Random rand = new Random();
        for (int i = 0; i < twoDArray.length; i++) {
            // for each line of A. (twoDArray)
            for (int j = 0; j < twoDArray[i].length; j++) {
                // for each cell in line A (twoDArray)
                twoDArray[i][j] = (short)(rand.nextInt(11));
                //enter a number from 0 to 10
            }
        }
        for (int i = 0; i < oneDArray.length; i++) {
            // for each cell of v (oneDArray)
            oneDArray[i] = (short)(rand.nextInt(11));
            //enter a number from 0 to 10
        }
    }

    // Method to print a 2D table (helped in debugging)
    public static void PrintArray(short[][] arr){
        for (short[] shorts : arr) {
            for (int anShort : shorts) {
                System.out.print(anShort+" ");
            }
            System.out.println();
        }
        System.out.println("");
    }
    // Method to print a one-dimensional table (helped in debug)
    public static void PrintArray(int[] arr){
        for (int j : arr) {
            System.out.print(j+" ");
        }
        System.out.println("\n");
    }
    // Class for creating threads
    static class ProcessThread extends Thread {
        int[] resArray = new int[N]; // thread scoreboard
        int cut;    // With the variables cut and start we pass the piece
        int start;  // of the table we want to edit in the thread

        // Constructor for ProcessThread (creating the thread)
        public ProcessThread(int start, int cut){
            this.start = start;
            this.cut = cut;
        }

        @Override
        public void run() {
            for (int i = start; i < cut; i++) {
                // From the starting point to the cut point (lines of A)
                int[] tempArray = new int[M];
                for (int j = 0; j < M; j++) {
                    // for each cell of ν
                    tempArray[j] += twoDArray[i][j] * oneDArray[j];
                    // we multiply cell j of v by cell j from line A
                }
                for (int temp:tempArray){
                    resArray[i]+=temp;
                    //add the cells of tempArray and save them in cell i of resArray
                }
            }
        }
        //getter for resArray
        public int[] getResArray() {
            return resArray;
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ProcessThread [] threads = new ProcessThread[THREAD_COUNT];
        RandArray(); //call to initialize tables
        int[] result = new int[N]; //final table (will save the result)

        int cut = N/ THREAD_COUNT; // initialization of sub-table boundaries


        int startPoint = 0;

        //*********************
        //Creating Treads
        //*********************
        for(int i=0; i<THREAD_COUNT; i++) {
            // For the number of THREAD_COUNT
            int finalStartPoint = startPoint;
            int finalCut = cut;
            threads[i] = new ProcessThread(finalStartPoint, finalCut);
            //create a Tread
            startPoint += N / THREAD_COUNT; // change the limit for the next Loop
            cut += N / THREAD_COUNT;
        }
        //*********************
        //Starting Treads
        //*********************
        for(ProcessThread thread :threads) {
            thread.start(); // Start the Thread
        }
        //*********************
        //Join Treads
        //*********************
        for(ProcessThread thread :threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //*******************************
        //   Extract results and
        //   save to the result table
        //*******************************
        for (ProcessThread thread:threads){
            for (int i=0; i<N; i++){
                result[i]+=thread.getResArray()[i];
            }
        }

        //table printing calls (help debug)
//        PrintArray(twoDArray);
//        PrintArray(oneDArray);
//        PrintArray(result);
        long end = System.currentTimeMillis();
        System.out.println("Number of Thread(s) "+THREAD_COUNT+"\nAll Elapsed Time in milli seconds: " + (end - start));
    }


}

