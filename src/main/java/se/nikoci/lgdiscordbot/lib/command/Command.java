package se.nikoci.lgdiscordbot.lib.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public interface Command {

    String getName();

    default Collection<CommandType> getCommandType(){
        return Set.of(CommandType.ALL);
    }

    default String getDescription(){
        return "No description provided for this command";
    }

    default Collection<Permission> getPermissions() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.MESSAGE_SEND);
        return permissions;
    }

    default Command[] getSubcommands(){
        return new Command[0];
    }

    default Collection<OptionData> getOptionData(){return Set.of();}

    default CommandData getCommandData(){
        List<SubcommandData> subcommandDataList = new ArrayList<>();

        //Loops through all subcommands to create a SubCommandData object for each, also adds their OptionData to the SubCommandData obj
        for (Command command : getSubcommands()){
            subcommandDataList.add(new SubcommandData(command.getName(), command.getDescription())
                    .addOptions(command.getOptionData()));
        }

        //Returning the CommandData obj with all it's subcommands and data.
        return new CommandData(getName(), getDescription())
                .addOptions(getOptionData())
                .addSubcommands(subcommandDataList);
    }

    default Logger getLogger(){
        return LoggerFactory.getLogger(getName()+"Command");
    }



    default void execute(MessageReceivedEvent event){
        if (!getCommandType().contains(CommandType.ALL)){
            if (getCommandType().contains(CommandType.GUILD_SLASH) || getCommandType().contains(CommandType.DM_SLASH)){
                if (!getCommandType().contains(CommandType.GUILD_NORMAL) || !getCommandType().contains(CommandType.DM_NORMAL)){
                    return;
                }
            }
        }

        event.getChannel().sendMessage("Executed " + getName() + " command.").queue();
        getLogger().info("User [{}] executed command '{}'", event.getAuthor().getId(), getName());
    }


    default void execute(SlashCommandEvent event){
        if (!getCommandType().contains(CommandType.ALL)){
            if (getCommandType().contains(CommandType.GUILD_NORMAL) || !getCommandType().contains(CommandType.DM_NORMAL)){
                if (!getCommandType().contains(CommandType.GUILD_SLASH) || getCommandType().contains(CommandType.DM_SLASH)){
                    return;
                }
            }
        }

        event.deferReply().complete();
        event.getHook().sendMessage("Executed " + getName() + " command.").setEphemeral(true).queue();
        getLogger().info("User [{}] executed command '{}'", event.getUser().getId(), getName());
    }
}
