package se.nikoci.lgdiscordbot.lib.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nikoci.lgdiscordbot.lib.Bot;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Bot instance;

    public CommandHandler(Bot instance){
        this.instance = instance;
        instance.setCommandHandler(this);
    }

    //TODO: Implement checking for subcommands and handle them accordingly, calling the right execute method.
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(this.instance.getPrefix())) return;
        if (event.getAuthor().equals(this.instance.getJda().getSelfUser())) return;

        Member member = event.getMember();
        String label = event.getMessage().getContentRaw().split(" ")[0].replaceFirst(this.instance.getPrefix(), "");
        Command command = getCommand(label);

        System.out.println("LABEL: " + label);
        System.out.println("COMMAND: " + command);
        System.out.println("PREFIX: " + this.instance.getPrefix());

        if ( command == null) return;

        logger.info("SlashCommandEvent: calling execution for command {} from user {}",
                command.getName(),
                event.getAuthor().getId());

        if (event.isFromGuild()){
            if (member == null) return;
            if (member.hasPermission(command.getPermissions())){
                command.execute(event);
            } else {
                event.getChannel().sendMessage("You do not have permission for this command.").queue();
            }
        } else {
            command.execute(event);
        }
    }

    //TODO: Implement checking CommandData for Options and SubCommands and handle them accordingly, calling the right execute method.
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = event.getMember();
        Command command = getCommand(event.getName());

        if (command == null) return;

        logger.info("SlashCommandEvent: calling execution for command {} from user {}",
                command.getName(),
                event.getUser().getId());

        if (event.isFromGuild()){
            if (member == null) return;
            if (member.hasPermission(command.getPermissions())) {
                command.execute(event);
            } else {
                event.deferReply().complete();
                event.getHook().sendMessage("You do not have permission for this command.").setEphemeral(true).queue();
            }
        } else {
            command.execute(event);
        }
    }

    public void addCommand(@NotNull Command command){
        //restrict adding multiple with the same name
        for (var entrySet : instance.getCommands().entrySet()){
            if (entrySet.getKey().equalsIgnoreCase(command.getName())){
                logger.error("Cannot add command with the same name");
                return;
            }
        }

        instance.getCommands().put(command.getName(), command);
        updateCommandData();
    }

    public void removeCommand(@NotNull Command command){
        removeCommand(command.getName());
    }

    public void removeCommand(@NotNull String commandName){
        instance.getCommands().forEach((name, cmd) -> {
            if (name.equalsIgnoreCase(commandName)) instance.getCommands().remove(name, cmd);
        });
    }

    public Command getCommand(@NotNull String name){
        for (var entrySet : instance.getCommands().entrySet()){
            if (entrySet.getKey().equalsIgnoreCase(name)){
                return entrySet.getValue();
            }
        }
        return null;
    }

    public void addCommands(Command @NotNull ... commands){
        for (Command command : commands) addCommand(command);
    }

    public void removeCommands(Command @NotNull ... commands){
        for (Command command : commands) removeCommand(command);
    }

    //Updates command data for jda, only Command obj's that accept slash commands will be updated.
    private void updateCommandData(){
        List<CommandData> commandDataList = new ArrayList<>();

        instance.getCommands().forEach((name, cmd) -> {
            //Make sure we only select Command objects that accept slash commands
            if (cmd.getCommandType().contains(CommandType.GUILD_SLASH)
                    || cmd.getCommandType().contains(CommandType.DM_SLASH)
                    || cmd.getCommandType().contains(CommandType.ALL)){
                commandDataList.add(cmd.getCommandData());
            }
        });

        instance.getJda()
                .updateCommands()
                .addCommands(commandDataList)
                .queue();

    }
}
