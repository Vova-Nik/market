package com;

import java.util.Arrays;

public class Model {

    public int PRICE_LENGTH = 8;
    public int QUANTITY_LENGTH = 8;
    public int U_LENGTH = PRICE_LENGTH + QUANTITY_LENGTH + 8;

    int MAX_POSSIBLE_PRICE = 9999;
    final int[] askCol = new int[MAX_POSSIBLE_PRICE + 1];
    final int[] bidCol = new int[MAX_POSSIBLE_PRICE + 1];

    int maxPrice = 0;
    int minPrice = 0;

    private int differedBuy = 0;
    private int differedSell = 0;

    private int bestBid = 0;
    private int bestAsk = 0;

    public Model() {
        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public int updateBid(int price, int quant) {
        updateMinMaxPrices(price);
        if (differedSell > 0) {
            differedSell = differedSell - quant;
            if (differedSell > 0) {
                return 0;
            }
            differedSell = 0;
            quant = -differedSell;
        }
        int state = 0;
        if (bestAsk != 0) state |= 0b0001;
        if (price >= bestAsk()) state |= 0b0010;

        switch (state) {
            case 0:
            case 1:
                bidCol[price] += quant;
                updateBestBid();
                break;
            case 2:
                bidCol[price] = buyForPrice(price, quant);
                updateBestAsk();
                updateBestBid();
                break;
            case 3:
                break;
        }
        return 0;
    }

    private int buyForPrice(int price, int quant) {
        for (int i = bestAsk; i <= price; i++) {
            askCol[i] -= quant;
            if (askCol[i] > 0) {
                return 0;
            }
            quant = -askCol[i];
            askCol[i] = 0;
        }
        return quant;
    }

    private int sellForPrice(int price, int quant) {
        for (int i = bestBid; i >= price; i--) {
            bidCol[i] -= quant;
            if (bidCol[i] >= 0) {
                return 0;
            }
            quant = -bidCol[i];
            bidCol[i] = 0;
        }
        return quant;
    }

    private void updateMinMaxPrices(int price) {
        if (price > maxPrice) {
            maxPrice = price;
        }
        if (minPrice == 0)
            minPrice = price;

        if (price < minPrice) {
            minPrice = price;
        }
    }

    private int normalizeBid(int price, int quant) {
        while (true) {
            if (bestAsk <= price)
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

    private void updateBestBid() {
        bestBid = 0;
        for (int i = maxPrice; i >= minPrice; i--) {
            if (bidCol[i] > 0) {
                bestBid = i;
                break;
            }
        }
    }

    private void updateBests() {
        bestBid = Integer.MAX_VALUE;
        for (int i = maxPrice; i >= minPrice; i--) {
            if (bidCol[i] != 0) {
                bestBid = i;
                break;
            }
        }
        bestAsk = 0;
        for (int i = minPrice; i <= maxPrice; i++) {
            if (askCol[i] != 0) {
                bestAsk = i;
                break;
            }
        }
    }

    public int updateAsk(int price, int quant) {
        updateMinMaxPrices(price);
        if (differedBuy > 0) {
            quant = quant - differedBuy;
            if (quant < 0) {
                quant = 0;
                differedBuy = -quant;
                return 0;
            }
            differedBuy = 0;
        }
        int state = 0;
        if (bestBid != 0) state |= 0b0001;
        if (price <= bestBid()) state |= 0b0010;
        switch (state) {
            case 0:
            case 1:
                askCol[price] += quant;
                updateBestAsk();
                break;

            case 2:
                updateBestAsk();
                updateBestBid();
                break;

            case 3:
                sellForPrice(price,quant);
                updateBestAsk();
                updateBestBid();
                break;
         }
        return 0;
    }

    private int updateBestAsk() {
        for (int i = minPrice; i <= maxPrice; i++) {
            if (askCol[i] > 0) {
                bestAsk = i;
                break;
            }
        }
        return 0;
    }


    public int querySize(int price) {
        return Math.max(askCol[price], bidCol[price]);
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

    public int bestBid() {
        return bestBid;
    }

    public int bestAsk() {
        return bestAsk;
    }

    public void clear() {
        minPrice = (int) Math.pow(10, PRICE_LENGTH);
        maxPrice = MAX_POSSIBLE_PRICE;
        differedBuy = 0;
        differedSell = 0;

        bestBid = 0;
        bestAsk = Integer.MAX_VALUE;

        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public void showState(int way) {
        System.out.println("pr bid ask");
//        for (int i = maxPrice; i >= minPrice; i--) {
        for (int i = 16; i >= 6; i--) {
            System.out.println(i + "\t" + bidCol[i] + "\t" + askCol[i]);
        }
        if (way > 0) {
            System.out.println("Best Bid = " + bestBid + " Best Ask = " + bestAsk);
        }
        if (way > 1) {
            System.out.println("Min Price = " + minPrice + " Max Price = " + maxPrice);
        }
    }
}
