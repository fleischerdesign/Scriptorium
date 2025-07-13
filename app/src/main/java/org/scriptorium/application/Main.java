package org.scriptorium.application;

import org.scriptorium.cli.CommandExecutor;

public class Main {
    public static void main(String[] args) {
        System.out.println("Book Import System");
        System.out.println("-----------------");
        
        CommandExecutor executor = new CommandExecutor();
        executor.start();
    }
}
