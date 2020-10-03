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

    public DataHolder() {
        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public int updateBid(int price, int quant) {
        bidCol[price] += quant;
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
        return 0;
    }

    public int updateAsk(int price, int quant) {
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
        return 0;
    }

    public int queryBestBid() {
        return bestBid();
    }

    public int queryBestAsk() {
        return bestAsk();
    }

    public int querySize(int price) {
        return Math.max(askCol[price], bidCol[price]);
    }

    public int orderSell(int quant) {
        return 0;
    }

    public int orderBuy(int quant) {
        return 0;
    }

    private int bestBid() {
        for (int i = maxPrice; i >= minPrice; i--) {
            if (bidCol[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    private int bestAsk() {
        for (int i = minPrice; i <= maxPrice; i++) {
            if (askCol[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    public int sell(int quant){
        while(true) {
            int ba = bestAsk();
            askCol[ba] = askCol[ba] - quant;
            if (askCol[ba] >= 0) {
                break;
            }
            quant = - askCol[ba];
            askCol[ba] =0;
        }
        return 0;
    }

    public int buy(int quant){
        while(true) {
            int bb = bestBid();
            bidCol[bb] = bidCol[bb] - quant;
            if (bidCol[bb] >= 0) {
                break;
            }
            quant = - bidCol[bb];
            bidCol[bb] =0;
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
        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

}
