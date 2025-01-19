package com.escapedoom.lector.portal.rest.util;

import java.util.List;

public class SanityCheck {

    public static List<List<Boolean>> listOfBinary = List.of(
            List.of(true, false, false, false, true, true),
            List.of(true, true, false, false),
            List.of(true,true,false,true,false,true,true,true,false),
            List.of(true,true,false,true),
            List.of(true,true,false,true,true),
            List.of(true,false,false,true,false,true,false,true,false,false),
            List.of(true,false,false,false,false,false,false,false),
            List.of(true,false,false,true,false,false,false,false,true),
            List.of(true,false,false,false,true,true,true,true,false),
            List.of(true,false,false,false,false,true,true,true,true,true,true)
    );

    public static int solve(List<List<Boolean>> input) {
        int dif = 0;
        for (List<Boolean> row : input) {
            for (Boolean entry : row) {
                if (!entry) {
                    dif += 1;
                } else {
                    dif -= 1;
                }
            }
        }
        return dif;
    }

    public static void main(String[] args) {
        System.out.println(solve(listOfBinary));
    }

}
