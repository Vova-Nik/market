package com;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

        assertEquals("1\n",parcer.parce("q,size,9"));
        assertEquals("8\n",parcer.parce("q,size,7"));
        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("0,0\n",parcer.parce("q,best_ask"));
    }

    @Test
    public void testBigInitialBids() {
        model = new Model();
        parcer = new Parcer(model);

        parcer.parce("u,821,3,bid");
        parcer.parce("u,945,1,bid");
        parcer.parce("u,700,8,bid");
        parcer.parce("u,600,12,bid");

//        model.showState(2);
        assertEquals("1\n",parcer.parce("q,size,945"));
        assertEquals("8\n",parcer.parce("q,size,700"));
        assertEquals("945,1\n",parcer.parce("q,best_bid"));
        assertEquals("0,0\n",parcer.parce("q,best_ask"));
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

        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("12\n",parcer.parce("q,size,13"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
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

        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("12\n",parcer.parce("q,size,13"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,9"));
        assertEquals("8\n",parcer.parce("q,size,7"));
        assertEquals("9,1\n",parcer.parce("q,best_bid"));

        parcer.parce("u,10,1,ask");
        parcer.parce("u,9,1,bid");
        assertEquals("2\n",parcer.parce("q,size,10"));
        assertEquals("2\n",parcer.parce("q,size,9"));
        assertEquals("10,2\n",parcer.parce("q,best_ask"));
        assertEquals("9,2\n",parcer.parce("q,best_bid"));

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
        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("11,2\n",parcer.parce("q,best_ask"));
        assertEquals("0\n",parcer.parce("q,size,10"));
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
//        model.showState(3);
        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        parcer.parce("u,7,9,ask");
//        model.showState(3);
        assertEquals("7,3\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,8"));
        assertEquals("3\n",parcer.parce("q,size,7"));
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

        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("12,2\n",parcer.parce("q,best_ask"));
        assertEquals("2\n",parcer.parce("q,size,12"));       assertEquals("0\n",parcer.parce("q,size,11"));
        assertEquals("0\n",parcer.parce("q,size,10"));
        assertEquals("1\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,20"));
        assertEquals("0\n",parcer.parce("q,size,0"));
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
        assertEquals("7,7\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));

        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,8"));
        assertEquals("7\n",parcer.parce("q,size,7"));   }
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
        assertEquals("0,0\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,8"));
        assertEquals("0\n",parcer.parce("q,size,7"));
        assertEquals("0\n",parcer.parce("q,size,6"));

        parcer.parce("o,sell,2");
        assertEquals("0,0\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,8"));
        assertEquals("0\n",parcer.parce("q,size,7"));
        assertEquals("0\n",parcer.parce("q,size,6"));
//        model.showState(2);
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
        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("0,0\n",parcer.parce("q,best_ask"));
        assertEquals("0\n",parcer.parce("q,size,13"));
        assertEquals("0\n",parcer.parce("q,size,12"));
        assertEquals("0\n",parcer.parce("q,size,11"));
        assertEquals("0\n",parcer.parce("q,size,10"));
        assertEquals("1\n",parcer.parce("q,size,09"));
        assertEquals("3\n",parcer.parce("q,size,08"));

        parcer.parce("o,buy,2");
        assertEquals("9,1\n",parcer.parce("q,best_bid"));
        assertEquals("0,0\n",parcer.parce("q,best_ask"));
        assertEquals("0\n",parcer.parce("q,size,13"));
        assertEquals("0\n",parcer.parce("q,size,12"));
        assertEquals("0\n",parcer.parce("q,size,11"));
        assertEquals("0\n",parcer.parce("q,size,10"));
        assertEquals("1\n",parcer.parce("q,size,09"));
        assertEquals("3\n",parcer.parce("q,size,08"));
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
        assertEquals("0,0\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("0\n",parcer.parce("q,size,8"));
        parcer.parce("u,8,3,bid");
        assertEquals("8,2\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("1\n",parcer.parce("q,size,10"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("2\n",parcer.parce("q,size,8"));
        parcer.parce("u,9,2,bid");
        parcer.parce("o,sell,5");
        parcer.parce("o,sell,5");
        parcer.parce("u,8,10,bid");
        assertEquals("8,4\n",parcer.parce("q,best_bid"));
        assertEquals("10,1\n",parcer.parce("q,best_ask"));
        assertEquals("0\n",parcer.parce("q,size,9"));
        assertEquals("4\n",parcer.parce("q,size,8"));
//        model.showState(3);
    }
}
