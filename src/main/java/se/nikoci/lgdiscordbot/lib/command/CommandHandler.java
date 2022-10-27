package se.nikoci.lgdiscordbot.lib.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nikoci.lgdiscordbot.lib.Bot;

@RequiredArgsConstructor
public class CommandHandler extends ListenerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull private Bot instance;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(this.instance.getPrefix())) return;
        if (event.getAuthor().equals(this.instance.getJda().getSelfUser())) return;

        Member member = event.getMember();
        String label = event.getMessage().getContentRaw().split(" ")[0].replaceFirst(this.instance.getPrefix(), "");
        Command command = this.instance.getCommand(label);

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

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = event.getMember();
        Command command = instance.getCommand(event.getName());

        if (command == null) return;

        logger.info("SlashCommandEvent: calling execution for command {} from user {}",
                command.getName(),
                event.getUser().getId());

        if (event.isFromGuild()){
            if (member == null) return;
            if (member.hasPermission(command.getPermissions())) {
                command.execute(event);
            } else {
                event.getHook().sendMessage("You do not have permission for this command.").setEphemeral(true).queue();
            }
        } else {
            command.execute(event);
        }


    }


}
