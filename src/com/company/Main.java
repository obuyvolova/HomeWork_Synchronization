package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int maxValue = 0;
    public static int maxCount = 0;
    public static final int ROUTES = 1000;

    public static void main(String[] args) {


        for (int i = 0; i < ROUTES; i++) {
            new Thread(() -> {
                String str = generateRoute("RLRFR", 100);
                int count = 0;
                for (int j = 0; j < str.length(); j++) {
                    if ('R' == str.charAt(j)) {
                        count++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.replace(count, sizeToFreq.get(count) + 1);
                        if (sizeToFreq.get(count) > maxCount) {
                            maxCount = sizeToFreq.get(count);
                            maxValue = count + 1;
                        }
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            }).start();
        }

        System.out.println("Maximum R is " + maxValue + ", " + maxCount + " times");
        System.out.println("Other:");
        sizeToFreq.forEach((key, value) -> System.out.println(key + "(" + value + " times)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}