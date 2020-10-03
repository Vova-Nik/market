package com;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestMarket {

    static String inpFile1 = "" +
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "u,11,4,bid\n" +
            "q,best_bid\n" +
            "u,10,2,bid\n" +
            "q,best_bid\n" +
            "o,sell,1\n" +
            "q,size,10\n" +
            "u,9,3,bid\n" +
            "u,11,5,ask\n"+
            "u,12,52,ask\n";

    static String inpFile = "" +
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "u,11,4,bid\n" +
            "u,10,2,bid\n" +
            "u,9,3,bid\n" +
            "u,11,5,ask\n"+
            "u,12,52,ask\n";

    static DataHolder dataHolder;
    static Parcer parcer;

    @BeforeAll
    static void init(){
//        final long then = System.nanoTime();

        Scanner scanner = new Scanner(inpFile);
        String toParce;

         dataHolder = new DataHolder();
         parcer = new Parcer(dataHolder);

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            parcer.parce(toParce);
        }

        System.out.println("pr bid ask" );
        int [] askCol = dataHolder.getAskCol();
        int [] bidCol = dataHolder.getBidCol();
        for(int i = dataHolder.minPrice; i<=dataHolder.maxPrice; i++){
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }
        scanner.close();
//        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
//        System.out.println("Executed in (ms): " + millis);
    }

//    @Test
//    public void testShow() {
//        System.out.println("pr bid ask" );
//        int [] askCol = dataHolder.getAskCol();
//        int [] bidCol = dataHolder.getBidCol();
//        for(int i = dataHolder.minPrice; i<=dataHolder.maxPrice; i++){
//            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
//        }
//    }

    @Test
    public void testQParcer() {
         String inpFile12 = "" +
                "u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,4,bid\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "o,sell,1\n" +
                "q,size,10\n" +
                "u,9,3,bid\n" +
                "u,11,5,ask\n"+
                "u,12,52,ask\n";
        dataHolder.clear();
        Scanner scanner = new Scanner(inpFile12);
        String toParce;

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            parcer.parce(toParce);
        }
        System.out.println("testQParcer");
        System.out.println("pr bid ask" );
        int [] askCol = dataHolder.getAskCol();
        int [] bidCol = dataHolder.getBidCol();
        for(int i = dataHolder.minPrice; i<=dataHolder.maxPrice; i++){
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }
        scanner.close();
        assertEquals(10,parcer.parce("q,best_bid"));
        assertEquals(11,parcer.parce("q,best_ask"));
        assertEquals(52,parcer.parce("q,size,12"));
        assertEquals(6,parcer.parce("q,size,11"));

        dataHolder.sell(10);
        dataHolder.buy(3);
        System.out.println("testQParcer after sell 10  and buy 3");
        System.out.println("pr bid ask" );
        for(int i = dataHolder.minPrice; i<=dataHolder.maxPrice; i++){
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }


    }
}
