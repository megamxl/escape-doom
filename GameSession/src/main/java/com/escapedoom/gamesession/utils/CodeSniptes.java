package com.escapedoom.gamesession.utils;




public class CodeSniptes {

    private static final String javaClassBeing =
            "import java.util.*;\n" +
                    "public class app {\n" +
                    "\n";


    private static final String mainJavaBegin =
            "    public static void main(String[] args) {\n" +
                    "         System.out.println(solve(";

    private static final String mainJavaEnd =
            " ));\n    }\n";

    public static final String javaClassEnding =
            "}";

    private static String mainGenerator(String inputVariable) {
        return mainJavaBegin + inputVariable + mainJavaEnd;
    }

    public static String javaClassGenerator(String systemInput, String variable , String userCode) {
        return javaClassBeing + systemInput + mainGenerator(variable) +  userCode + javaClassEnding;
    }
}
