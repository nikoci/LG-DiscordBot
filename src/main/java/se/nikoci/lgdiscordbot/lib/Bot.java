package se.nikoci.lgdiscordbot.lib;

import lombok.Data;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nikoci.lgdiscordbot.lib.command.Command;
import se.nikoci.lgdiscordbot.lib.command.CommandHandler;

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

    public void addCommand(Command command){
        //restrict adding multiple with the same name
        for (var entrySet : commands.entrySet()){
            if (entrySet.getKey().equalsIgnoreCase(command.getName())){
                logger.error("Cannot add command with the same name");
                return;
            }
        }

        //Check for the CommandType and updateSlashCommand accordingly


    }

    private void updateSlashCommand(){

    }

    public Command getCommand(String name){
        for (var entrySet : commands.entrySet()){
            if (entrySet.getKey().equalsIgnoreCase(name)){
                return entrySet.getValue();
            }
        }
        return null;
    }

}