package com;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestMarket {

    static String inpFile =
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "u,11,4,bid\n" +
            "q,best_bid\n" +
            "u,10,2,bid\n" +
            "q,best_bid\n" +
            "q,size,10\n" +
            "u,9,31,bid\n" +
            "u,11,5,ask\n" +
            "u,14,58,ask\n" +
            "u,12,52,ask\n" +
            "o,sell,12\n" +
            "o,buy,02\n"
    ;


    static DataHolder dataHolder;
    static Parcer parcer;

    @BeforeAll
    static void init() {
        dataHolder = new DataHolder();
        parcer = new Parcer(dataHolder);
    }

    @Test
    public void testQParcer() {
        String inpFile = "" +
                "u,9,1,bid\n" +
                "u,11,5,ask\n" +
                "u,11,4,bid\n" +
                "q,best_bid\n" +
                "u,10,2,bid\n" +
                "q,best_bid\n" +
                "q,size,10\n" +
                "u,9,31,bid\n" +
                "u,11,5,ask\n" +
                "u,14,58,ask\n" +
                "u,12,52,ask\n" +
                "o,sell,12\n" +
                "o,buy,02\n"
                ;

        dataHolder.clear();
        Scanner scanner = new Scanner(inpFile);
        String toParce;

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            parcer.parce(toParce);
        }
        System.out.println("testQParcer");
        System.out.println("pr bid ask");
        int[] askCol = dataHolder.getAskCol();
        int[] bidCol = dataHolder.getBidCol();
        for (int i = dataHolder.minPrice; i <= dataHolder.maxPrice; i++) {
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }
        scanner.close();
        assertEquals(9,parcer.parce("q,best_bid"));
        assertEquals(11,parcer.parce("q,best_ask"));
        assertEquals(52,parcer.parce("q,size,12"));
        assertEquals(4,parcer.parce("q,size,11"));

    }

    @Test
    public void testCons(){
        dataHolder.clear();
        assertEquals(-1,parcer.parce("q,best_bid\n"));
        assertTrue(parcer.parce("q,best_ask\n")>1000);
        assertEquals(0,parcer.parce("q,size,10\n"));
        parcer.parce("u,1024,5535,bid");
        parcer.parce("u,1087,55350,ask");
        dataHolder.showState();
        assertEquals(1024,parcer.parce("q,best_bid\n"));
        assertEquals(1087,parcer.parce("q,best_ask\n"));
        assertEquals(5535,parcer.parce("q,size,1024\n"));
        assertEquals(55350,parcer.parce("q,size,1087\n"));

        dataHolder.clear();
        parcer.parce("u,9,1,bid");
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertTrue(parcer.parce("q,best_ask\n")>1000);
        assertEquals(0,parcer.parce("q,size,10\n"));
        assertEquals(1,parcer.parce("q,size,9\n"));
        parcer.parce("u,9,1,ask");
        assertEquals(-1,parcer.parce("q,best_bid\n"));
        assertEquals(-1,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));

        parcer.parce("u,10,3,ask");
        assertEquals(-1,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(3,parcer.parce("q,size,10\n"));
        parcer.parce("u,10,3,bid");
        assertEquals(-1,parcer.parce("q,best_bid\n"));
        assertEquals(-1,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));
        parcer.parce("u,8,3,bid");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,9,2,bid");
        parcer.parce("u,10,1,bid");
        assertEquals(10,parcer.parce("q,best_bid\n"));
        assertEquals(-1,parcer.parce("q,best_ask\n"));
        assertEquals(6,parcer.parce("q,size,8\n"));
        assertEquals(0,parcer.parce("q,size,8001\n"));

        dataHolder.showState();

    }
}
