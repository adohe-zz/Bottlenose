package com.xqbase.bn.rpc.server.tomcat;

import java.io.PrintStream;

import static com.xqbase.bn.rpc.server.tomcat.AnsiElement.GREEN;
import static com.xqbase.bn.rpc.server.tomcat.AnsiElement.DEFAULT;
import static com.xqbase.bn.rpc.server.tomcat.AnsiElement.FAINT;

/**
 * Default Banner implementation which writes the 'Baiji' banner.
 *
 * @author Tony He
 */
public class BaijiBanner {

    private static final String[] BANNER = { "",
            "  .   ____                    _       _ _ _",
            " /\\\\ |    \\             _    (_)  _   \\ \\ \\ \\",
            "( ( )|___ /   ____     (_)   | | (_)   \\ \\ \\ \\",
            " \\\\/ |    \\ //    \\\\   | |   | | | |    ) ) ) )",
            "  .  |____/ \\\\ __ //\\_ |_|   | | |_|   / / / /",
            " ========================\\\\_/_/ ======/_/_/_/" };


    private static final String SPRING_BOOT = " :: Baiji :: ";

    private static final int STRAP_LINE_SIZE = 42;

    public void printBanner(PrintStream printStream) {
        for (String line : BANNER) {
            printStream.println(line);
        }
        String version = "(v 1.6.0.RELEASE) ^_^ Tony made";
        String padding = "";
        while (padding.length() < STRAP_LINE_SIZE
                - (version.length() + SPRING_BOOT.length())) {
            padding += " ";
        }

        printStream.println(AnsiOutput.toString(GREEN, SPRING_BOOT, DEFAULT, padding,
                FAINT, version));
        printStream.println();
    }

    public static void main(String[] args) {
        BaijiBanner baijiBanner = new BaijiBanner();
        baijiBanner.printBanner(System.out);
    }
}
