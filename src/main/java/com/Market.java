package com;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Market {
    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args[0].length() < 1 || args[1].length() < 1) {
            System.out.println("Invalid input");
            System.out.println("use");
            System.out.println("market inputfile outputfile");
            return;
        }

        final long then = System.nanoTime();
        Model model = new Model();
        Parcer parcer = new Parcer(model);

        BufferedWriter writter;
        writter = new BufferedWriter(new FileWriter(args[1]));

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))) {
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                if(line.length()>3) {
                    writter.write(parcer.parce(line));
                }
            }
        }
        writter.close();

        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
        System.out.println("Executed in (ms): " + millis);
    }
}
