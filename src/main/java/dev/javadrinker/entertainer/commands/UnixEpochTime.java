package dev.javadrinker.entertainer.commands;


import dev.javadrinker.entertainer.util.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
//
public class UnixEpochTime extends ListenerAdapter implements EventListener {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("UnixEpochTime")) {
            event.deferReply().queue();
            Button workButton = Button.primary("unix_epoch_time_explanation", "WTF is a Unix Epoch???");

            String strCurrentTimeMillis = Long.toString(System.currentTimeMillis());
            StringBuffer sb = new StringBuffer(strCurrentTimeMillis);
            sb.delete(sb.length()-4, sb.length()-1);


            EmbedBuilder eb = Embeds.customEmbed("Unix Epoch Time", "The current number of milliseconds since Unix Epoch is: "+System.currentTimeMillis() +"\n AKA, <t:"+sb+":F>");
            event.getHook().sendMessageEmbeds(eb.build()).addActionRow(workButton).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId().equalsIgnoreCase("unix_epoch_time_explanation")) {
            event.deferReply().queue();
            EmbedBuilder eb = Embeds.customEmbed("Unix Epoch Time", "The Unix Epoch was an arbitrary date created by programmers to be a reference point for time. The date for Epoch time was originally chosen to be January 1st, 1971 GMT, but it was later adjusted to be January 1st, 1970 to be nice round number.");
            event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
        }
    }


}
