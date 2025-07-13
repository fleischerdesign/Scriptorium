package org.scriptorium.cli;

public interface Command {
    String getName();
    void execute();
}