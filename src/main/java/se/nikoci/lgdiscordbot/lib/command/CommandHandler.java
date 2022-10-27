package se.nikoci.lgdiscordbot.lib.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import se.nikoci.lgdiscordbot.lib.Bot;

@RequiredArgsConstructor
public class CommandHandler extends ListenerAdapter {

    @NonNull private Bot instance;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {


    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

    }


}
