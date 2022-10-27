package se.nikoci.lgdiscordbot.bot;

import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import se.nikoci.lgdiscordbot.lib.Bot;
import se.nikoci.lgdiscordbot.lib.command.CommandHandler;

import javax.security.auth.login.LoginException;
import java.util.Set;

public class Main {

    public static void main(String @NotNull [] args) throws LoginException {
        //Making sure token is passed through arguments
        //TODO: Maybe change this to environment variables?
        if (!args[0].equals("")){
            Bot bot = new Bot(args[0], Set.of(
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_WEBHOOKS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.DIRECT_MESSAGE_REACTIONS
            ));

            //TODO: Find a better way to pass the Bot instance to the CommandHandler instance
            bot.setCommandHandler(new CommandHandler(bot));
        } else {
            Bot.logger.error("Bot token not provided!");
            System.exit(1);
        }
    }

}
