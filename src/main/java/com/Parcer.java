package com;

public class Parcer {
    String file;
//    String toReturn ="";
    private final Model model;
    private String result = "";

    public Parcer(Model model) {
//        this.result = new Result(0, 0);
        this.model = model;
    }

    public String parce(String line) {
        char[] lnCh = line.toCharArray();
        switch (lnCh[0]) {
            case 'u':
                return parceU(lnCh);
//                break;
            case 'q':
                return parceQ(lnCh);
//                break;
            case 'o':
                return parceO(lnCh);
//                break;
//            default:
//                return -1;
        }
        return "";
    }

    private String parceU(char[] str) {
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
        return "";
    }

    private String parceQ(char[] str) {
        if (str[7] == 'b') {     //best bid
            result = model.bestBid() + "," + model.querySize(model.bestBid()) + "\n";
            //            result.size = model.querySize(model.bestBid());
            return result;
        }
        if (str[7] == 'a') {     //best ask
            result = model.bestAsk() + "," +  model.querySize(model.bestAsk()) + "\n";
            //result.size = model.querySize(model.bestAsk());
            return result;
        }
        int price = 0;  //size
        for (int i = str.length - 2, j = 1; i >= 7; i--, j++) {
            price += (str[i] - 0x30) * pow(j);
        }
        return model.querySize(price) +"\n";
    }

    private String parceO(char[] str) {
        //        o,buy,<size> - removes <size> shares out of asks, most cheap ones.
        //        o,sell,<size> - removes <size> shares out of bids, most expensive.

        int quant = 0;
        if (str[2] == 'b') {
            for (int i = str.length - 1, j = 1; i >= 6; i--, j++) {
                quant += (str[i] - 0x30) * pow(j);
            }
            model.buy(quant);
            return "";
        }
        if (str[2] == 's') {
            for (int i = str.length - 1, j = 1; i >= 7; i--, j++) {
                quant += (str[i] - 0x30) * pow(j);
            }
            model.sell(quant);
            return "";
        }
        return "";
    }

    final int[] exp10 = {1, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    int pow(int exp) {
        return exp10[exp];
    }

}

//
//class Result {
//    Result(int size, int price) {
//        this.size = size;
//        this.price = price;
//    }
//    int size;
//    int price;
//}