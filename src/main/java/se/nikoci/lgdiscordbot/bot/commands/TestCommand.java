package se.nikoci.lgdiscordbot.bot.commands;

import net.dv8tion.jda.api.Permission;
import se.nikoci.lgdiscordbot.lib.command.Command;

import java.util.Collection;
import java.util.Set;

public class TestCommand implements Command {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "This is a test command";
    }

    @Override
    public Collection<Permission> getPermissions() {
        return Set.of(Permission.ADMINISTRATOR);
    }
}
