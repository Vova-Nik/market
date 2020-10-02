import java.util.Arrays;
import java.util.Scanner;

public class Parcer {
    String file;
    final int[] askCol = new int[128];
    final int[] bidCol = new int[128];
    int maxPrice = 1;
    int minPrice = 1;
    int PRICE_LENGTH = 8;
    int QUANTITY_LENGTH = 8;
    int U_LENGTH = PRICE_LENGTH + QUANTITY_LENGTH + 8;

    public Parcer() {
        Arrays.fill(askCol, 0);
        Arrays.fill(bidCol, 0);
    }

    public boolean parce(String file) {
        Scanner scanner = new Scanner(file);
        String toParce;
        char[] str = new char[U_LENGTH];

        int priceLength = 0;
        int quantLength = 0;
        int price = 0;
        int quantity = 0;

        while (scanner.hasNextLine()) {
            toParce = scanner.nextLine();
            str = toParce.toCharArray();
            char operation = ' '; //b == bid, a == ask;

            switch (str[0]) {
                case 'u':

                    for (int i = 2; i < U_LENGTH; i++) {
                        if (str[i] == ',') {
                            if (priceLength == 0) {
                                priceLength = i - 2;
                                for (int j = 1; j <= priceLength; j++) {
//                                    price+=Math.pow(10,j-1)*(str[i-j]-0x30);
                                    price += pow(j) * (str[i - j] - 0x30);
                                }
                                continue;
                            } else {
                                quantLength = i - 3 - priceLength;
                                operation = str[i + 1];

                                for (int j = 1; j <= quantLength; j++) {
//                                    quantity+=Math.pow(10,j-1)*(str[i-j]-0x30);
                                    quantity += pow(j) * (str[i - j] - 0x30);
                                }
                                break;
                            }
                        }
                    }

                    if (operation == 'a') {
                        askCol[price] = askCol[price] + quantity;
                    } else {
                        bidCol[price] = bidCol[price] + quantity;
                    }
                    int qq = askCol[price] - bidCol[price];
                    if (qq >= 0) {
                        askCol[price] = qq;
                        bidCol[price] = 0;
                    } else {
                        askCol[price] = 0;
                        bidCol[price] = -qq;
                    }
                    System.out.println(toParce + "\t " + askCol[price] + "\t " + bidCol[price]);
                    priceLength = 0;
                    price = 0;
                    quantity = 0;
                    break;

                case 'q':

                    break;
                case 'o':

                    break;

                default:
                    return false;

            }

        }
        return true;
    }

    int pow1(int exp) {
        int res = 1;
        for (int i = 1; i < exp; i++) {
            res *= 10;
        }
        return res;
    }

    int pow2(int exp) {

        switch (exp) {
            case 0:
                return 1;

            case 1:
                return 1;

            case 2:
                return 10;

            case 3:
                return 100;

            case 4:
                return 1000;

            case 5:
                return 10000;

            case 6:
                return 100000;

            case 7:
                return 1000000;

            case 8:
                return 10000000;

        }
        return 1;
    }

    final int[] exp10 = {1, 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    int pow(int exp) {
        return exp10[exp];
    }
}
