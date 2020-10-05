package com;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Market {
    static String inpFile = "" +
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "q,best_bid\n" +
            "u,10,2,bid\n" +
            "q,best_bid\n" +
            "o,sell,1\n" +
            "q,size,10\n" +
            "u,9,3,bid\n" +
            "u,11,5,ask\n";

    public static void main(String... ignored){

        final long then = System.nanoTime();

        Scanner scanner = new Scanner(inpFile);
        String toParce;

        Model model = new Model();
        Parcer parcer = new Parcer(model);

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            parcer.parce(toParce);
        }
        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
        System.out.println("Executed in (ms): " + millis);
    }
}
