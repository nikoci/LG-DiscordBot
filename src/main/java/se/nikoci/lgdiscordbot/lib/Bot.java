package se.nikoci.lgdiscordbot.lib;

import lombok.Data;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nikoci.lgdiscordbot.lib.command.Command;
import se.nikoci.lgdiscordbot.lib.command.CommandHandler;
import se.nikoci.lgdiscordbot.lib.command.CommandType;

import javax.security.auth.login.LoginException;
import java.util.*;

@Data
public class Bot {

    public static Logger logger = LoggerFactory.getLogger(Bot.class);
    public static List<Bot> bots = new ArrayList<>();

    private JDA jda;
    private CommandHandler commandHandler;
    private Map<String, Command> commands = new HashMap<>(); //String name, Command obj
    private String token;
    private Set<GatewayIntent> intents;

    private String prefix = "!";

    public Bot(@NonNull String token, @NonNull Set<GatewayIntent> intents) throws LoginException {
        this.setToken(token);
        this.setIntents(intents);
        jda = JDABuilder.create(token, intents).build();
        bots.add(this);
    }

    public void setCommandHandler(@NotNull CommandHandler commandHandler){
        if (this.commandHandler != null) jda.removeEventListener(this.commandHandler);
        this.commandHandler = commandHandler;
        jda.addEventListener(commandHandler);
    }

    public void addCommand(@NotNull Command command){
        //restrict adding multiple with the same name
        for (var entrySet : commands.entrySet()){
            if (entrySet.getKey().equalsIgnoreCase(command.getName())){
                logger.error("Cannot add command with the same name");
                return;
            }
        }

        commands.put(command.getName(), command);
        updateCommandData();
    }

    public void removeCommand(@NotNull Command command){
        removeCommand(command.getName());
    }

    public void removeCommand(@NotNull String commandName){
        commands.forEach((name, cmd) -> {
            if (name.equalsIgnoreCase(commandName)) commands.remove(name, cmd);
        });
    }

    public Command getCommand(@NotNull String name){
        for (var entrySet : commands.entrySet()){
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

        commands.forEach((name, cmd) -> {
            //Make sure we only select Command objects that accept slash commands
            if (cmd.getCommandType().contains(CommandType.GUILD_SLASH)
                    || cmd.getCommandType().contains(CommandType.DM_SLASH)
                    || cmd.getCommandType().contains(CommandType.ALL)){
                commandDataList.add(cmd.getCommandData());
            }
        });

        getJda()
                .updateCommands()
                .addCommands(commandDataList)
                .queue();

    }
}