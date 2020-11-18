package com;

public class Parcer {
    String file;
    private final Model model;
    private final Result result;

    public Parcer(Model model) {
        this.result = new Result(0, 0);
        this.model = model;
    }

    public int parce(String file) {

        char[] str = file.toCharArray();
        int toReturn;
        switch (str[0]) {
            case 'u':
                toReturn = parceU(str);
                break;
            case 'q':
                toReturn = parceQ(str);
                break;
            case 'o':
                toReturn = parceO(str);
                break;
            default:
                return -1;
        }
        return toReturn;
    }

    int parceU(char[] str) {
        int priceLength = 0;
        int quantLength;
        int price = 0;
        int quantity = 0;
        char operation = ' '; //b == bid, a == ask;

        for (int i = 2; i < model.U_LENGTH; i++) {
            if (str[i] == ',') {
                if (priceLength == 0) {
                    priceLength = i - 2;
                    for (int j = 1; j <= priceLength; j++) {
                        price += pow(j) * (str[i - j] - 0x30);
                    }
                    // continue;
                } else {
                    quantLength = i - 3 - priceLength;
                    operation = str[i + 1];
                    for (int j = 1; j <= quantLength; j++) {
                        quantity += pow(j) * (str[i - j] - 0x30);
                    }
                    break;
                }
            }
        }
        if (operation == 'a') {
            model.updateAsk(price, quantity);
        } else {
            model.updateBid(price, quantity);
        }
        return 0;
    }

    int parceQ(char[] str) {
        if (str[7] == 'b') {     //best bid
            return model.bestBid();
        }
        if (str[7] == 'a') {     //best ask
            return model.bestAsk();
        }
        int price = 0;//size
        for (int i = str.length - 2, j = 1; i >= 7; i--, j++) {
            price += (str[i] - 0x30) * pow(j);
        }
        return model.querySize(price);
    }

    int parceO(char[] str) {
        //        o,buy,<size> - removes <size> shares out of asks, most cheap ones.
        //        o,sell,<size> - removes <size> shares out of bids, most expensive.

        int quant = 0;
        if (str[2] == 'b') {
            for (int i = str.length - 1, j = 1; i >= 6; i--, j++) {
                quant += (str[i] - 0x30) * pow(j);
            }
            model.buy(quant);
            return 0;
        }
        if (str[2] == 's') {
            for (int i = str.length - 1, j = 1; i >= 7; i--, j++) {
                quant += (str[i] - 0x30) * pow(j);
            }
            model.sell(quant);
            return 0;
        }
        return -1;
    }

    final int[] exp10 = {1, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    int pow(int exp) {
        return exp10[exp];
    }

}

class Result {
    Result(int size, int quant) {
        this.size = size;
        this.quant = quant;
    }
    int size;
    int quant;
}