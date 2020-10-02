import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class market {
    static String inpFile = "" +
            "u,9,1,bid\n" +
            "u,11,5,ask\n" +
            "q,best_bid\n" +
            "u,10,2,bid\n" +
            "q,best_bid\n" +
            "o,sell,1\n" +
            "q,size,10\n" +
            "u,9,3,bid\n" +
            "u,11,5,ask\n";



    public static void main(String... ignored) throws InterruptedException {
        final long then = System.nanoTime();
//        TimeUnit.SECONDS.sleep(1);

        Parcer parcer = new Parcer();
        parcer.parce(inpFile);



        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
        System.out.println("Executed in (ms): " + millis);
    }
}
