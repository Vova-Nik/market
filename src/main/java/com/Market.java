package com;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Market {
//    static String inpFile = "" +
//            "u,9,1,bid\n" +
//            "u,11,5,ask\n" +
//            "q,best_bid\n" +
//            "u,10,2,bid\n" +
//            "q,best_bid\n" +
//            "o,sell,1\n" +
//            "q,size,10\n" +
//            "u,9,3,bid\n" +
//            "u,11,5,ask\n";

    public static void main(String[] args){

        if(args.length==0||args[0].length()<1||args[1].length()<1){
            System.out.println("Vova, Invalid input");
            System.out.println("use");
            System.out.println("market outputfile inputfile");
            return;
        }

        final long then = System.nanoTime();

//        Scanner scanner = new Scanner(inpFile);
//        String toParce;

        Model model = new Model();
        Parcer parcer = new Parcer(model);

//        while (scanner.hasNextLine()) {
//            toParce = scanner.nextLine();
//            parcer.parce(toParce);
//        }
        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
        System.out.println("Executed in (ms): " + millis);
    }
}

/*
     javac market.java Model.java parcer.java

    javac [ options ] [ sourcefiles ] [ classes ] [ @argfiles ]

        -sourcepath C:\java\pubs\ws\1.3\src\share\classes

        "options":

        -d classes
        -g
        -sourcepath C:\java\pubs\ws\1.3\src\share\classes
______________________________________________
        "classes":

        MyClass1.java
        MyClass2.java
        MyClass3.java
________________________________________________

        C:> javac @options @classes
        */
