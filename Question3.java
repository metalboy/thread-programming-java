package exercise1;
import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class Question3 {

    private static final int THREAD_COUNT = 8; //threads to make a number of calls
    private static final int CALLS = 50;      //k calls per thread
    private static final String API_URL = "https://loripsum.net/api/10/plaintext/";
    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray(); //Letter board

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //Thread board
        ProcessThread[] threads = new ProcessThread[THREAD_COUNT];

        for (int i = 0; i < threads.length; i++) { //For each cell in the table
            threads[i] = new ProcessThread();      // Create thread
        }
        for (int i = 0; i < threads.length; i++) { //For each thread in the table
            threads[i].start();                    // I start thread
        }

        for (ProcessThread thread : threads) {   //For each thread in the table
            try {
                thread.join();                   //join
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Extract results
        int SumLength = 0;     //word size
        float Count = 0.0f;    // sum of words
        HashMap<Character, Integer> charCount = new HashMap<>();

        for (ProcessThread thread : threads) {   // For each Thread
            SumLength+=thread.getSumLength();    // Add the sum of letters
            Count+=thread.getCount();            // Add the sum of words
            for (char charX : alphabet) {        // For each letter
                if (charCount.containsKey(charX)) { // if it is in the Table
                    charCount.put(charX, thread.getCharCount().get(charX)+charCount.get(charX)); //added the sum from Thread
                } else {
                    charCount.put(charX, thread.getCharCount().get(charX));  //Swamp on the table with the whole of it Thread
                }
            }
        }

        System.out.println("\n---------- RESULTS -----------");
        System.out.println("RESULTS 1:");
        System.out.println("the average length of the words in the text of all texts produced is :"+(SumLength/Count));
        System.out.println("RESULTS 2:");
        System.out.println("the percentage of characters of the English alphabet is:");
        float charSum = 0.0f;
        for (char charX : alphabet) {
            charSum += charCount.get(charX);  // Sum of letters
        }
        for (int i=0; i<alphabet.length; i++) {  //For each letter
            //Print the integer percentage % for all threads
            System.out.print(alphabet[i] +" : "+Math.round(charCount.get(alphabet[i])/charSum*100)+"%  ");
            if(i == (alphabet.length/2)-1){
                System.out.println();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("\n\nElapsed Time in milli seconds: " + (end - start));
    }

    /**
     * This thread processes the file lines.
     */
    static class ProcessThread extends Thread {
        //Table for measuring characters of the alphabet
        private final HashMap<Character, Integer> charCount = new HashMap<>();
        private int sumLength = 0;  //sum of characters
        private float count = 0.0F; //sum of words

        @Override
        public void run() {
            for (int i = 0; i < CALLS; i++) {   //for k times
                String data = loadDataFromUrl();//make a call to API
                data = data.toLowerCase();      //Make all the characters small
                String[] words = data.split(" ");
                for (String word : words) { //each word
                    sumLength+=word.length(); // added the sum of letters to the sum
                    count +=1;                // raise the counter by 1
                }
                for (char charX : alphabet) { // each letter
                    //We find abstract appearances of the letter
                    int characterCount = data.length() - data.replaceAll(String.valueOf(charX), "").length();
                    if (charCount.containsKey(charX)) {  // if it is in the Table
                        charCount.put(charX, this.charCount.get(charX)+ characterCount); //add to sum
                    }
                    else {
                        charCount.put(charX, characterCount); //swamp on the table with the sums
                    }
                }
            }
        }

        //Μέθοδος για την ανάγνωσή του API
        private String loadDataFromUrl() {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(API_URL);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                    result.append(" ");
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }


        // getters για μεταβλητές τών Thread
        public HashMap<Character, Integer> getCharCount() {
            return charCount;
        }

        public int getSumLength() {
            return sumLength;
        }

        public float getCount() {
            return count;
        }
    }
}