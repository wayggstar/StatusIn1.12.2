package org.wayggstar.status.commands;

import org.wayggstar.status.Status;

public class CommandManager {
    private final Status plugin;

    public CommandManager(Status plugin){
        this.plugin = plugin;
    }

    public void registerCommands(){
        plugin.getCommand("코인").setExecutor(new Balance());
    }
}
