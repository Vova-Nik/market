package com;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

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


    static Model model;
    static Parcer parcer;

    @BeforeAll
    static void init() {
        model = new Model();
        parcer = new Parcer(model);
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

        model.clear();
        Scanner scanner = new Scanner(inpFile);
        String toParce;

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            parcer.parce(toParce);
        }
        System.out.println("testQParcer");
        System.out.println("pr bid ask");

        scanner.close();
        assertEquals(9,parcer.parce("q,best_bid"));
        assertEquals(11,parcer.parce("q,best_ask"));
        assertEquals(52,parcer.parce("q,size,12"));
        assertEquals(4,parcer.parce("q,size,11"));

    }

    @Test
    public void testCons(){
        model.clear();
        assertEquals(-1,parcer.parce("q,best_bid\n"));
        assertTrue(parcer.parce("q,best_ask\n")>1000);
        assertEquals(0,parcer.parce("q,size,10\n"));
        parcer.parce("u,1024,5535,bid");
        parcer.parce("u,1087,55350,ask");
        model.showState(0);
        assertEquals(1024,parcer.parce("q,best_bid\n"));
        assertEquals(1087,parcer.parce("q,best_ask\n"));
        assertEquals(5535,parcer.parce("q,size,1024\n"));
        assertEquals(55350,parcer.parce("q,size,1087\n"));

        model.clear();
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
        model.showState(0);
    }


    @Test
    public void testInitialBids() {
        model = new Model();
        parcer = new Parcer(model);

        parcer.parce("u,8,3,bid");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");

        assertEquals(1,parcer.parce("q,size,9\n"));
        assertEquals(8,parcer.parce("q,size,7\n"));
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_ask\n"));
        parcer.parce("u,8,3,ask");
        parcer.parce("u,9,1,ask");
        parcer.parce("u,7,8,ask");
        parcer.parce("u,6,12,ask");
        model.showState(2);
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,12\n"));

        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_ask\n"));
    }

    @Test
    public void testInitialAsks() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,12,8,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,13,12,ask");
        model.showState(2);

        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(12,parcer.parce("q,size,13\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
    }

    @Test
    public void testInitialBoth() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,12,8,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,13,12,ask");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");
        model.showState(2);

        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(12,parcer.parce("q,size,13\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,9\n"));
        assertEquals(8,parcer.parce("q,size,7\n"));
        assertEquals(9,parcer.parce("q,best_bid\n"));
    }
    @Test
    public void testBidEqAsk() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,12,8,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,13,12,ask");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");

        parcer.parce("u,10,1,bid");
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(11,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));

        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,ask");

        model.showState(2);
        assertEquals(8,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));

    }

}
