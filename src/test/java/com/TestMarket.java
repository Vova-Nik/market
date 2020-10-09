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
    public void testInitialBids() {
        model = new Model();
        parcer = new Parcer(model);

        parcer.parce("u,8,3,bid");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");

//        model.showState(2);
        assertEquals(1,parcer.parce("q,size,9\n"));
        assertEquals(8,parcer.parce("q,size,7\n"));
        assertEquals(9,parcer.parce("q,best_bid\n"));
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
//        model.showState(2);

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

        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(12,parcer.parce("q,size,13\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,9\n"));
        assertEquals(8,parcer.parce("q,size,7\n"));
        assertEquals(9,parcer.parce("q,best_bid\n"));

        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");
        assertEquals(2,parcer.parce("q,size,10\n"));
        assertEquals(2,parcer.parce("q,size,9\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(9,parcer.parce("q,best_bid\n"));

//        model.showState(2);
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
    }

    @Test
    public void testNewAskLessBestBid() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,13,12,ask");
        parcer.parce("u,12,8,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        parcer.parce("u,7,9,ask");
        assertEquals(7,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,8\n"));
        assertEquals(3,parcer.parce("q,size,7\n"));
    }

    @Test
    public void testNewBidGreaterBestAsk() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,13,12,ask");
        parcer.parce("u,12,8,ask");//2
        parcer.parce("u,11,2,ask");//0
        parcer.parce("u,10,1,ask");//0
        parcer.parce("u,9,1,bid");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");

        parcer.parce("u,12,9,bid");

        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(12,parcer.parce("q,best_ask\n"));
        assertEquals(2,parcer.parce("q,size,12\n"));
        assertEquals(0,parcer.parce("q,size,11\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));
        assertEquals(1,parcer.parce("q,size,9\n"));

        assertEquals(0,parcer.parce("q,size,20\n"));
        assertEquals(0,parcer.parce("q,size,0\n"));
    }

    @Test
    public void testOSellSimply() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,13,12,ask");
        parcer.parce("u,12,8,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");//0
        parcer.parce("u,8,3,bid");//0
        parcer.parce("u,7,8,bid");//7
        parcer.parce("u,6,12,bid");//12

        parcer.parce("o,sell,5");

        // model.showState(2);
        assertEquals(7,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));

        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,8\n"));
        assertEquals(7,parcer.parce("q,size,7\n"));
        assertEquals(12,parcer.parce("q,size,6\n"));
    }
    @Test
    public void testOSellGreaterThenMarket() {
        model = new Model();
        parcer = new Parcer(model);
        parcer.parce("u,13,12,ask");
        parcer.parce("u,12,8,ask");
        parcer.parce("u,11,2,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");//0
        parcer.parce("u,8,3,bid");//0
        parcer.parce("u,7,8,bid");//0
        parcer.parce("u,6,12,bid");//0

        parcer.parce("o,sell,25");
        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,8\n"));
        assertEquals(0,parcer.parce("q,size,7\n"));
        assertEquals(0,parcer.parce("q,size,6\n"));

        parcer.parce("o,sell,2");
        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,8\n"));
        assertEquals(0,parcer.parce("q,size,7\n"));
        assertEquals(0,parcer.parce("q,size,6\n"));
        model.showState(2);
    }

    @Test
    public void testOBuyGreaterThenMarket() {
        model = new Model();
        parcer = new Parcer(model);
        //differed //1
        parcer.parce("u,13,12,ask");//0
        parcer.parce("u,12,8,ask");//0
        parcer.parce("u,11,2,ask");//0
        parcer.parce("u,10,1,ask");//0
        parcer.parce("u,9,1,bid");
        parcer.parce("u,8,3,bid");
        parcer.parce("u,7,8,bid");
        parcer.parce("u,6,12,bid");

        parcer.parce("o,buy,24");
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,13\n"));
        assertEquals(0,parcer.parce("q,size,12\n"));
        assertEquals(0,parcer.parce("q,size,11\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));
        assertEquals(1,parcer.parce("q,size,09\n"));
        assertEquals(3,parcer.parce("q,size,08\n"));

        parcer.parce("o,buy,2");
        assertEquals(9,parcer.parce("q,best_bid\n"));
        assertEquals(0,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,13\n"));
        assertEquals(0,parcer.parce("q,size,12\n"));
        assertEquals(0,parcer.parce("q,size,11\n"));
        assertEquals(0,parcer.parce("q,size,10\n"));
        assertEquals(1,parcer.parce("q,size,09\n"));
        assertEquals(3,parcer.parce("q,size,08\n"));
//        model.showState(2);
    }

    @Test
    public void testDifferedSell() {
        model = new Model();
        parcer = new Parcer(model);

        parcer.parce("u,11,2,ask");
        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");
        parcer.parce("u,8,3,bid");
        parcer.parce("o,sell,5");
        assertEquals(0,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(0,parcer.parce("q,size,8\n"));
        parcer.parce("u,8,3,bid");
        assertEquals(8,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(1,parcer.parce("q,size,10\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(2,parcer.parce("q,size,8\n"));
        parcer.parce("u,9,2,bid");
        parcer.parce("o,sell,5");
        parcer.parce("o,sell,5");
        parcer.parce("u,8,10,bid");
        assertEquals(8,parcer.parce("q,best_bid\n"));
        assertEquals(10,parcer.parce("q,best_ask\n"));
        assertEquals(0,parcer.parce("q,size,9\n"));
        assertEquals(4,parcer.parce("q,size,8\n"));
//        model.showState(3);
    }
}
