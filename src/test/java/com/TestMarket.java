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
}
