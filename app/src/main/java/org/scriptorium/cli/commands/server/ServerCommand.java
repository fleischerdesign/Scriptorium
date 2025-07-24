package org.scriptorium.cli.commands.server;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import org.scriptorium.cli.ScriptoriumCommand;

/**
 * Parent command for all server-related operations (start, stop).
 * This groups the server management commands under a single 'server' namespace.
 */
@Command(name = "server", description = "Manages the Scriptorium API server.", subcommands = {
    ServerStartCommand.class,
    ServerStopCommand.class
})
public class ServerCommand {

    @ParentCommand
    ScriptoriumCommand parent; // Injects the main ScriptoriumCommand

    // No direct execution logic for this parent command, it just groups subcommands.
}
