package dev.javadrinker.entertainer.mapGame.commands;


import dev.javadrinker.entertainer.mapGame.GameState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
//
public class StartGame extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("start")) {
            if (GameState.checkInitializationStatus()) {
                GameState.init(event.getHook(), event.getUser(), false);
            } else {
                GameState.init(event.getHook(), event.getUser(), true);
            }
            event.deferReply().queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {


    }


}
