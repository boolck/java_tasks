package com.boolck.tasks.monkeysequence;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonkeySequenceTest {

    @Test
    public void whenSequenceRuns_ThenEveryPeachIsEatenInMonkeyOrder() throws InterruptedException {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            MonkeySequence.main(new String[0]);
        } finally {
            System.setOut(originalOutput);
        }

        List<String> lines = output.toString().lines().collect(Collectors.toList());
        assertEquals(List.of(
                "Monkey:A is eating Peach: 0",
                "Monkey:B is eating Peach: 1",
                "Monkey:C is eating Peach: 2",
                "Monkey:A is eating Peach: 3",
                "Monkey:B is eating Peach: 4",
                "Monkey:C is eating Peach: 5",
                "Monkey:A is eating Peach: 6",
                "Monkey:B is eating Peach: 7",
                "Monkey:C is eating Peach: 8",
                "Monkey:A is eating Peach: 9"
        ), lines);
    }
}
