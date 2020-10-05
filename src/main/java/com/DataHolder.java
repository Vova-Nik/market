package com;

import java.util.Arrays;

public class DataHolder {

    public int PRICE_LENGTH = 8;
    public int QUANTITY_LENGTH = 8;
    public int U_LENGTH = PRICE_LENGTH + QUANTITY_LENGTH + 8;
    int maxPrice = 1;
    int minPrice = (int) Math.pow(10, PRICE_LENGTH);
    int MAX_POSSIBLE_PRICE = 9999;
    final int[] askCol = new int[MAX_POSSIBLE_PRICE];
    final int[] bidCol = new int[MAX_POSSIBLE_PRICE];

    private int differedBuy = 0;
    private int differedSell = 0;

    private int bestBid = 0;
    private int bestAsk = Integer.MAX_VALUE;

    public DataHolder() {
        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public int updateBid(int price, int quant) {
        if (differedSell > 0) {
            quant = quant - differedSell;
            if (quant < 0) {
                quant = 0;
                differedSell = -quant;
                updateMinMaxPrices(price);
                return 0;
            }
            differedSell = 0;
        }
        if (price <= bestBid) {
            bidCol[price] += quant;
            updateMinMaxPrices(price);
            return 0;
        }



        bidCol[price] += quant;
        int qq = askCol[price] - bidCol[price];
        if (qq >= 0) {
            askCol[price] = qq;
            bidCol[price] = 0;
        } else {
            askCol[price] = 0;
            bidCol[price] = -qq;
        }


        if (price > bestBid) {
            bestBid = price;
        }
        return 0;
    }

    private void updateMinMaxPrices(int price) {
        if (price > maxPrice) {
            maxPrice = price;
        }
        if (price < minPrice && price > 0) {
            minPrice = price;
        }
    }



    private int normalizeBid(int price, int quant) {
        while (true) {
            if(bestAsk<=price)
            askCol[bestAsk] = askCol[bestAsk] - quant;
            if (askCol[bestAsk] > 0) {
                break;
            }
            if (askCol[bestAsk] == 0) {
                updateBests();
                break;
            }
        }
        return 0;
    }

    private void updateBests() {
        bestBid = 0;
        for (int i = maxPrice; i >= minPrice; i--) {
            if (bidCol[i] != 0) {
                bestBid = i;
                break;
            }
        }
        bestAsk = Integer.MAX_VALUE;
        for (int i = minPrice; i <= maxPrice; i++) {
            if (askCol[i] != 0) {
                bestAsk = i;
                break;
            }
        }
    }


    public int updateAsk(int price, int quant) {
        if (differedBuy > 0) {
            quant = quant - differedBuy;
            if (quant < 0) {
                quant = 0;
                differedBuy = -quant;
                return 0;
            }
            differedBuy = 0;
        }

        askCol[price] += quant;
        int qq = askCol[price] - bidCol[price];
        if (qq >= 0) {
            askCol[price] = qq;
            bidCol[price] = 0;
        } else {
            askCol[price] = 0;
            bidCol[price] = -qq;
        }
        if (price > maxPrice) {
            maxPrice = price;
        }
        if (price < minPrice && price > 0) {
            minPrice = price;
        }
        if (price < bestAsk) {
            bestAsk = price;
        }
        return 0;
    }

    public int querySize(int price) {
        return Math.max(askCol[price], bidCol[price]);
    }


    public int bestBid() {
        return bestBid;

//        for (int i = maxPrice; i >= minPrice; i--) {
//            if (bidCol[i] != 0) {
//                return i;
//            }
//        }
//        return -1;
    }

    public int bestAsk() {
        return bestAsk;
//        for (int i = minPrice; i <= maxPrice; i++) {
//            if (askCol[i] != 0) {
//                return i;
//            }
//        }
//        return -1;
    }

    public int sell(int quant) {
        while (true) {
            int ba = bestAsk();
            if (ba <= 0) {
                differedSell = quant;
                return -1;
            }
            askCol[ba] = askCol[ba] - quant;
            if (askCol[ba] >= 0) {
                break;
            }
            quant = -askCol[ba];
            askCol[ba] = 0;
        }
        return 0;
    }

    public int buy(int quant) {
        while (true) {
            int bb = bestBid();
            if (bb <= 0) {
                differedBuy = quant;
                return -1;
            }
            bidCol[bb] = bidCol[bb] - quant;
            if (bidCol[bb] >= 0) {
                break;
            }
            quant = -bidCol[bb];
            bidCol[bb] = 0;
        }
        return 0;
    }

    /*  ****************** for testing purposes **********************/
    public int[] getAskCol() {
        return askCol;
    }

    public int[] getBidCol() {
        return bidCol;
    }

    public void clear() {
        minPrice = (int) Math.pow(10, PRICE_LENGTH);
        maxPrice = 0;
        differedBuy = 0;
        differedSell = 0;

        bestBid = 0;
        bestAsk = Integer.MAX_VALUE;


        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public void showState() {
        System.out.println("pr bid ask");

        for (int i = minPrice; i <= maxPrice; i++) {
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }
    }
}
