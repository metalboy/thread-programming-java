package exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Question2 {
    private static final int THREAD_COUNT = 8;
    private static final String FILE_NAME = "./files/simpsons_script_lines.csv"; //file

    //The wordsPerEpisode is a HashMap that you use to save her words per episode
    private static final ConcurrentHashMap<String, Integer> wordsPerEpisode = new ConcurrentHashMap<>();
    //The placeWithMostLyrics is a HashMap that you use to save the lyrics per episode
    private static final ConcurrentHashMap<String, Integer> placeWithMostLyrics = new ConcurrentHashMap<>();
    //                       Characters :  Bart, Homer, Margie και Lisa
    private static final character bart = new character("8", "Bart");
    private static final character lisa = new character("9", "Lisa");
    private static final character homer = new character("2", "Homer");
    private static final character margie = new character("1", "Margie");

    public static void main(String[] args) {
        ProcessThread[] threads = new ProcessThread[THREAD_COUNT];
        long start = System.currentTimeMillis();
        List<String> lines = loadDataFromFile(); //table to load the rows of the file
        //Make the columns table with the name of each column bearing the first row
        String headers = lines.remove(0);
        String[] columns = headers.split(",");
        //It will first load the rows of the file into a table (by the method cleanLines we remove the problematic elements)
        ArrayList<ConcurrentHashMap<String, String>> linesHash = cleanLines(lines, columns);
        //We divide the table into a table with sub-tables according to the threads (with the method listSpliter)
        ArrayList<ArrayList<ConcurrentHashMap<String, String>>> listForThreads = listSpliter(linesHash);
        System.out.println("The Original lines is " + lines.size() + "\nAfter error check in lines we have " + linesHash.size() + "\n");
        //*************************************************
        //Create a thread table (size THREAD_COUNT)
        //*************************************************
        for (int i=0; i<THREAD_COUNT; i++){
            //We create the thread and insert the part of the file that will be processed
            threads[i] = new ProcessThread(listForThreads.get(i), columns);
            threads[i].setName("My Thread "+(i+1));
        }
        //*********************
        //Starting Treads
        //*********************
        for (int i=0; i<THREAD_COUNT; i++){
            threads[i].start();
        }
        //*********************
        //Join Treads
        //*********************
        for (ProcessThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //*********************
        //  Result
        //*********************
        try {
            //1) the episode in which the dialogues had the largest number of words
            System.out.println("\nThe episode with id: " + maxValue(wordsPerEpisode).replace(" ", ", use ") + " words, it is the episode with the most words");
            //2) the location where most of the verse tales took place
            System.out.println("\nThe place with name: " + maxValue(placeWithMostLyrics).replace(" ", ", have ") + " Lyrics it is the episode with the most Lyrics");
            //3) for each of the characters Bart, Homer, Margie and Lisa, print the most
            // common word they use (from 5 characters and up) as well as how many times the used.
            System.out.println("\n"+bart);
            System.out.println(lisa);
            System.out.println(homer);
            System.out.println(margie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("\nElapsed Time in milli seconds: " + (end - start));
    }

    //Method for reading the file
    static List<String> loadDataFromFile() {
        System.out.println("Loading " + FILE_NAME);
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            //reads the file in the buffer in (essentially opens the file)
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //for each line in the buffer
                //(The in.readLine() works like pop() but does not delete the line
                // reads and moves the cursor to the next)
                lines.add(inputLine);
                //put the line in the list
            }
            in.close();
            //close the file
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    //Method for removing problem lines
    static ArrayList<ConcurrentHashMap<String, String>> cleanLines(List<String> lines, String[] columns) {
        ArrayList<ConcurrentHashMap<String, String>> temp = new ArrayList<>();
        for (String line : lines) {
            String[] field = line.split(",");
            ConcurrentHashMap<String, String> lineMap = new ConcurrentHashMap<>();
            if (field.length == 9) { // If the line does not have 9 fields it is removed
                for (int i = 0; i < columns.length; i++) {
                    lineMap.put(columns[i], field[i]);
                }

                if (lineMap.get(columns[8]).matches("[0-9]*")) { //If the field with the number of words is a number
                    if (lineMap.get("text").isEmpty()) {         //If the text field is empty
                        temp.add(lineMap);                       //put on the list
                    } else {
                        if (lineMap.get("text").split(" ").length == Integer.parseInt(lineMap.get("word_count"))) {
                            //put in the list if the words in the text are what the field says with the number of words
                            temp.add(lineMap);
                        }
                    }
                }
            }
        }
        return temp;
    }

    //Method to cut the board according to the threads
    //returns a list of sub-lists as many as the threads
    static ArrayList<ArrayList<ConcurrentHashMap<String, String>>> listSpliter(ArrayList<ConcurrentHashMap<String, String>> list){
        // The listForThreads is the list to be returned
        ArrayList<ArrayList<ConcurrentHashMap<String, String>>> listForThreads = new ArrayList<>();

        int count = 0; // Table top
        int size = list.size() / THREAD_COUNT; //Cutting point of the board
        for (int i = 0; i <THREAD_COUNT; i++){
            //for each thread
            listForThreads.add(new ArrayList<>(list.subList(count, size)));
            //Put the sub-list in listForThreads
            //Below we set the beginning and the cutting point of the panel
            //depending on the location and number of threads.
            //Discrete Places is the first sub-table starting from 0,
            //the latter reaches the end
            //the rest are large (list.size() / THREAD_COUNT)
            if (i > 0) {
                count += list.size() / THREAD_COUNT;
                if (i == THREAD_COUNT-2){
                    size = list.size();
                }else {
                    size += list.size() / THREAD_COUNT;
                }

            }else {
                count = list.size() / THREAD_COUNT;
                size += list.size() / THREAD_COUNT;
            }
        }
        return listForThreads;
    }

    //Class for creating threads
    static class ProcessThread extends Thread {
        private final ArrayList<ConcurrentHashMap<String, String>> lines; //Lines for editing
        String[] columns; // Columns

        // Constructor for the ProcessThread (creating the thread)
        public ProcessThread(ArrayList<ConcurrentHashMap<String, String>> lines, String[] columns) {
            this.lines = lines;
            this.columns = columns;
        }

        @Override
        public void run() {
            System.out.println(this.getName() + " processing " + lines.size() + " lines");

            for (ConcurrentHashMap<String, String> line : lines) {
            //For each line
                //*******************
                //Words per episode
                //*******************
                Map.Entry<String, Integer> entry = getNumOfWords(line).entrySet().iterator().next();
                //put in the entry variable the number of words and the id of the episode
                String key = entry.getKey();
                // key the episode id
                Integer value = entry.getValue();
                // value number of words
                if (!wordsPerEpisode.containsKey(key)) { //If there is no episode in wordsPerEpisode
                    wordsPerEpisode.put(key, value);     //put it in wordsPerEpisode with the value number of words
                } else {                //Otherwise find it at wordsPerEpisode and add value to the existing value
                    wordsPerEpisode.put(key, wordsPerEpisode.get(key) + value);
                }
                //*************************
                //rhymes per episode
                //*************************
                if (!placeWithMostLyrics.containsKey(line.get(columns[6]))) { //If the location does not exist
                    placeWithMostLyrics.put(line.get(columns[6]), 1);       //enter it with a value of 1 (counter)
                } else {                                                    //Otherwise raise by 1
                    placeWithMostLyrics.put(line.get(columns[6]), placeWithMostLyrics.get(line.get(columns[6])) + 1);
                }
                //*************************************
                //We put the line on each character
                //*************************************
                bart.putLines(line, columns);
                lisa.putLines(line, columns);
                homer.putLines(line, columns);
                margie.putLines(line, columns);
            }
        }
    }

    //Method to count words per episode
    static ConcurrentHashMap<String, Integer> getNumOfWords(ConcurrentHashMap<String, String> line) {
        ConcurrentHashMap<String, Integer> temp = new ConcurrentHashMap<>();

        int words = 0;
        if (!line.get("text").isEmpty()) {
            words = line.get("text").split(" ").length;
        }
        String episode = line.get("episode_id");

        temp.put(episode, words);
        return temp;
    }

    //Method to get the key with the largest value απο το Hashmap
    static String maxValue(ConcurrentHashMap<String, Integer> map) {
        int maxValueInMap = (Collections.max(map.values()));
        for (Map.Entry<String, Integer> entry : map.entrySet()) {  //  Itrate the hashmap
            if (entry.getValue() == maxValueInMap) {
                return entry.getKey().replace(" ", "_") + " " + entry.getValue();     //key with max value
            }
        }
        return "";
    }
    }
//character class to make the 4 characters and export the elements we want
class character {
    private final ConcurrentHashMap<String, Integer> words = new ConcurrentHashMap<>();
    private final String id; //id
    private final String name; // character name

    // Constructor for character
    public character(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //Method for counting words
    private void putWords(String word){
        if (word.matches("[a-zA-Z]{5,}")) { //If the word has more than 5 letters
            if (!this.words.containsKey(word)) { //If it appears for the first time
                this.words.put(word, 1); //we put it in the list with 1 on the counter
            } else {               // Otherwise
                this.words.put(word, this.words.get(word) + 1); //find it in the list and raise the meter
            }
        }
    }

    //Method for entering a line
    public void putLines(ConcurrentHashMap<String, String> line , String[] columns ){

        if (line.get(columns[3]).matches(this.id)) { //If the line corresponds to the character
            String[] temp = line.get(columns[7]).split(" "); // divided into words
            for (String word : temp) { //For every word
                    //remove non-letter characters
                    //and called the putWords method
                    putWords(word.replaceAll("\\W", ""));
            }
        }
    }
    //getter for words
    private ConcurrentHashMap<String, Integer> getWords() {
        return words;
    }

    //Method to get the key with the highest value of Hashmap
    private String maxValue(ConcurrentHashMap<String,Integer> map){
        int maxValueInMap=(Collections.max(map.values()));
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                return entry.getKey() + " " + entry.getValue();
            }
        }

        return "";
    }

    //toString
    @Override
    public String toString() {
        return "character with name: " + name + " and id: " + id + " use the word " + maxValue(this.getWords()) + " times" ;
    }
}
