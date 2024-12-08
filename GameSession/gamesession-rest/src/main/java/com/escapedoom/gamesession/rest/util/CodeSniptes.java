package com.escapedoom.gamesession.rest.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CodeSniptes {

    private final String javaClassBeing =
            "import java.util.*;\n" +
                    "public class app {\n" +
                    "\n";


    private final String mainJavaBegin =
            "    public static void main(String[] args) {\n" +
                    "         System.out.println(solve(";

    private final String mainJavaEnd =
            " ));\n    }\n";

    public final String javaClassEnding =
            "}";

    private String mainGenerator(String inputVariable) {
        return mainJavaBegin + inputVariable + mainJavaEnd;
    }

    public String javaClassGenerator(String systemInput, String variable , String userCode) {
        return javaClassBeing + systemInput + mainGenerator(variable) +  userCode + javaClassEnding;
    }
}
