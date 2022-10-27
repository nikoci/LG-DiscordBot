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
}