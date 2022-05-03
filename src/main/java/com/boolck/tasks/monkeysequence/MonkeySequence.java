package com.boolck.tasks.monkeysequence;

/*
  3 monkeys with 10 peaches to eat, assume the name of Monkeys are A, B and C.
  And peaches are numbered from 0-9. Each monkey can eat one peach at one time.
  Please simulate the process using multi-thread and output like:
  Monkey A eat Peach 1,
  Monkey B eat Peatch 5...
*/

import java.util.concurrent.atomic.AtomicInteger;

public class MonkeySequence {

    public static void main(String[] args) throws InterruptedException {
        final MonkeySequence holder = new MonkeySequence();
        final AtomicInteger peachCounter = new AtomicInteger(0);

        Thread monkey1 = holder.getMonkeyThread('A',peachCounter);
        Thread monkey2 = holder.getMonkeyThread('B',peachCounter);
        Thread monkey3 = holder.getMonkeyThread('C',peachCounter);

        monkey1.start();
        monkey2.start();
        monkey3.start();

        monkey1.join();
        monkey2.join();
        monkey3.join();
    }

    private Thread getMonkeyThread(final char id, final AtomicInteger peachCounter){
        return new Thread(() -> {
            while (peachCounter.get() < 10) {
                if (peachCounter.get() % 3 == id-'A') {
                    System.out.println("Monkey:" + id + " is eating Peach: " + peachCounter.getAndIncrement());
                }
            }
        });
    }
}



