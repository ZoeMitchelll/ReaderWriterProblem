package com.company;

import javax.swing.plaf.basic.BasicTreeUI;
import java.util.concurrent.Semaphore;  //Required

public class Main {
    static int [] data = new int[3];

    static Semaphore rmutex = new Semaphore(1);
    static Semaphore resource = new Semaphore(1);



    static public void Sleep(int ms)
    {
        try{
            Thread.sleep(ms);
        }
        catch (Exception e) {}
    }
    static public class Reader extends Thread
    {
        //Represents reader process
        String name;
        public Reader(String n)
        {
            name = n;
        }
        public static int readcounter = 0;

        public void run() {
            //Read from array and print its contents to the screen
            while (true) {
                try {
                    rmutex.acquire();
                    readcounter++;
                    if (readcounter==1){
                        resource.acquire();
                    }
                    rmutex.release();
                    //BEGIN Critical Section
                    System.out.println("Reader " + name + " reading data[0]: " + data[0]);
                    Sleep(100);
                    System.out.println("Reader " + name + " reading data[1]: " + data[1]);
                    Sleep(100);
                    System.out.println("Reader " + name + " reading data[2]: " + data[2]);
                    Sleep(100);
                    //END Critical Section
                    rmutex.acquire();
                    readcounter--;
                    if(readcounter==0){
                        resource.release();
                    }
                } catch (InterruptedException e) { }
                finally {
                    rmutex.release();
                }
                Sleep(500);
            }
        }
    }

    static public class Writer extends Thread
    {
        //Represents writer process
        String name;
        public Writer(String n)
        {
            name = n;
        }

        public void run()
        {
            //Increment value of all the array elements
            while(true)
            {
                try {
                    resource.acquire();
                    //BEGIN Critical Section
                    System.out.println("------------------");
                    System.out.println("Writer " + name + " writing value ");
                    data[0]++;
                    Sleep(100);
                    data[1]++;
                    Sleep(100);
                    data[2]++;
                    Sleep(100);
                    //END Critical Section
                } catch (InterruptedException e) { }
                finally {
                    resource.release();
                }
                Sleep(500);
            }

        }
    }

    public static void main(String[] args)
    {
        data[0] = 0;
        data[1] = 0;
        data[2] = 0;

        Reader r1 = new Reader("r1");
        Reader r2 = new Reader("r2");
        Reader r3 = new Reader("r3");
        Reader r4 = new Reader("r4");
        Reader r5 = new Reader("r5");
        Writer w1 = new Writer("w1");


        r1.start();
        r2.start();
        r3.start();
        r4.start();
        r5.start();
        w1.start();


    }
}
