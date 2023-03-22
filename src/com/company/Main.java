package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int maxKey = 0;
    public static int maxValue = 0;
    public static final int ROUTES = 1000;

    public static void main(String[] args) throws InterruptedException {

        Runnable taskMax = () -> {
            int count = 1;
            while (!Thread.interrupted()) {
                if (count == ROUTES + 1) {
                    break;
                }
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int key : sizeToFreq.keySet()) {
                        if (maxValue < sizeToFreq.get(key)) {
                            maxValue = sizeToFreq.get(key);
                            maxKey = key;
                        }
                    }
                        System.out.println(count++ + ". Leader is : " + maxKey + " Max Value: " + maxValue);
                }
            }
        };
        Thread threadMax = new Thread(taskMax);
        threadMax.start();

        for (int i = 0; i < ROUTES; i++) {
            Runnable task = () -> {
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
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                    sizeToFreq.notify();
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            Thread.sleep(100);
            System.out.println("This is " + thread.getName());
            thread.interrupt();
        }

        threadMax.interrupt();

        Thread.sleep(1000);

        System.out.println("Maximum R is " + maxKey + ", " + maxValue + " times");
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