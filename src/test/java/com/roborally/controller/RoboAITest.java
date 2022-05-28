package com.roborally.controller;

import designpatterns.observer.Itertools;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class RoboAITest {
    RoboAI roboAI;

    @Test
    void testPermutation() {
        String[] str1 = {"1","2","3","4","5","6", "7", "8"};
        int i = 0;
        for (List<String> products :
                Itertools.permutations(Arrays.asList(str1),5)) {
            for (String s :
                    products) {
                System.out.print(s+" ");
            }
            i++;
            System.out.println(i);
        }
    }
}