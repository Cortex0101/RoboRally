package com.roborally.controller;

import com.google.common.collect.Collections2;
import designpatterns.observer.Itertools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Collections2.orderedPermutations;
import static org.junit.jupiter.api.Assertions.*;

class RoboAITest {
    RoboAI roboAI;

    /*
    @BeforeEach
    void setup() {
        roboAI = new RoboAI("testboard");
    }

     */

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