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

    public void updateBid(int price, int quant) {
        if (price < minPrice || price > maxPrice) {
            updateMinMaxPrices(price);
        }
        if (differedSell > 0) {
            differedSell = differedSell - quant;
            if (differedSell > 0) {
                return;
            }
            quant = -differedSell;
            differedSell = 0;
        }
        if (bestAsk == 0) {
            bidCol[price] += quant;
            updateBestBid();
            return;
        }
        if (price < bestAsk) {
            bidCol[price] += quant;
            updateBestBid();
            return;
        }
        bidCol[price] = buyForPrice(price, quant);
        updateBestAsk();
        updateBestBid();
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

    private void updateBestBid() {
        for (int i = maxPrice; i >= 0; i--) {
            if (bidCol[i] > 0) {
                bestBid = i;
                return;
            }
        }
        bestBid = 0;
    }
    private int updateBestAsk() {
        for (int i = minPrice; i <= askCol.length; i++) {
            if (askCol[i] > 0) {
                bestAsk = i;
                break;
            }
        }
        return 0;
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
        if (price > maxPrice || price < minPrice) {
            updateMinMaxPrices(price);
        }
        if (differedBuy > 0) {
            differedBuy -= quant;
            if (differedBuy > 0) {
                return 0;
            }
            quant = -differedBuy;
            differedBuy = 0;
        }
        if (price > bestBid) {
            askCol[price] += quant;
            updateBestAsk();
            return 0;
        }
        sellForPrice(price, quant); //price <= bestBid;
        updateBestAsk();
        updateBestBid();
        return 0;
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

    public int querySize(int price) {
        // return Math.max(askCol[price], bidCol[price]);
        return askCol[price] >= bidCol[price] ? askCol[price] : bidCol[price];
    }

    public void sell(int quant) {
        quant += differedSell;
        differedSell = 0;
        if (differedBuy > 0) {
            differedBuy -= quant;
            if (differedBuy >= 0) {
                return;
            }
            differedBuy = 0;
            quant = -differedBuy;
        }
        for (int i = bestBid; i >= minPrice; i--) {
            bidCol[i] -= quant;
            if (bidCol[i] > 0) {
                bestBid = i;
                return;
            }
            if (bidCol[i] == 0) {
                updateBestBid();
                return;
            }
            quant = -bidCol[i];
            bidCol[i] = 0;
        }
        differedSell = quant;
        bestBid = 0;
    }

    public void buy(int quant) {
        quant += differedBuy;
        differedBuy = 0;
        if (differedSell > 0) {
            differedSell -= quant;
            if (differedSell >= 0) {
                return;
            }
            differedSell = 0;
            quant = -differedSell;
        }
        for (int i = bestAsk; i <= maxPrice; i++) {
            askCol[i] -= quant;
            if (askCol[i] > 0) {
                bestAsk = i;
                return;
            }
            if (askCol[i] == 0) {
                updateBestAsk();
                return;
            }
            quant = -askCol[i];
            askCol[i] = 0;
        }
        bestAsk = 0;
        differedBuy = quant;
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
        if (way > 2) {
            System.out.println("differedBuy = " + differedBuy + " differedSell = " + differedSell);
        }
    }
}
