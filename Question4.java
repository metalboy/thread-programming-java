package exercise1;

import java.util.concurrent.Semaphore;


public class Question4 {

    private static final int customers = 50;
    private static final boolean male = true;
    private static final boolean female = false;
    Semaphore store = new Semaphore(40);            //The store can accommodate up to 40 people at a time.
    Semaphore fittingRoomMan = new Semaphore(5);    //There are two clothing testing sites, one for men
    Semaphore fittingRoomWoman = new Semaphore(5);  //one for women and each (rehearsal room has 5 separate rooms).
    Semaphore cashDeskQueue = new Semaphore(10, true);    //does not allow more than 10 people to be in line
    Semaphore cashDeskServ = new Semaphore(1);      //there is a Cash service



    public static void main(String[] args) throws InterruptedException {
        Question4 obj = new Question4();
        long start = System.currentTimeMillis();
        for (int i= 1;  i<=customers; i++){ // For every customer
            Thread t;                       //create thread
            Customer customer;              //We create a client and determine the gender
            if (i%2 == 0) {   //he percentage of women-men in the store is 50% -50% (we assume that they enter alternately)
                customer = obj.new Customer(male);    //true
            }else{
                customer = obj.new Customer(female);  //false

            }
            t = new Thread(customer);  //We put the customer in the thread
            t.setName("My Thread "+i); //we name the thread
            t.start();                 //We start the thread
            if (i == customers){
                t.join();
            }
            Thread.sleep(2000 +(long)(Math.random() *3000)); //every 2 to 5 seconds a new customer enters the store
        }

        long end = System.currentTimeMillis();
        System.out.println("\n\nElapsed Time in milli seconds: " + (end - start));

    }
    //class Customer
    private class Customer implements Runnable{

        boolean gender;   //gender determination (women-men)
        //constructor
        public Customer(boolean gender) {

            this.gender = gender;
        }

        @Override
        public void run(){
            store.acquireUninterruptibly();                                 //Entrance to the store

           if (this.gender){
                fittingRoomMan.acquireUninterruptibly();                    //Men's rehearsal
                try {
                    Thread.sleep(3000 +(long) (Math.random() *7000));  //each person needs from 3 to 10
                } catch (InterruptedException e) {                           //seconds to try on clothes in the lab
                    e.printStackTrace();
                }
                fittingRoomMan.release();                                     //Exit from Laboratory
            }else{
                fittingRoomWoman.acquireUninterruptibly();                    //Women's lab
                try {
                    Thread.sleep(3000 +(long) (Math.random() *7000));   //each person needs from 3 to 10
                } catch (InterruptedException e) {                            //seconds to try on clothes in the lab
                    e.printStackTrace();
                }
                fittingRoomWoman.release();                                   //Exit from Laboratory
            }


            cashDeskQueue.acquireUninterruptibly();                          //Entrance to the cash queue
            cashDeskServ.acquireUninterruptibly();                           //Cash service

            try {
                Thread.sleep(5000);       // cash service time is fixed
            } catch (InterruptedException e) { //from the moment it is our turn to pay
                e.printStackTrace();
            }
            cashDeskQueue.release();                                           //cash queue exit
            cashDeskServ.release();                                             //Exit from cash service
            store.release();                                                    //Exit the store

        }
    }

}


