package com.mageddo.codestyle;

public class Stuff {
    public void aVeryLongMethodNameExplainingWhatItDoes(String argumentA, String argumentB,
                                                        String argumentC, String argumentD, String argumentE, String argumentF
    ) {
        System.out.println("do stuff");
    }

    public static void main(String[] args) {
        new Stuff().aVeryLongMethodNameExplainingWhatItDoes(
                "argument A value", "argument B value", "string for argument C", "argument D value",
                "argument E value", "argument F value"
        );

        if (1 == 1) {


        }

        for (int i = 0; i < 10; i++) {

        }
    }
}
