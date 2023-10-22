package dev.javadrinker.entertainer.mapGame.commands;


import dev.javadrinker.entertainer.mapGame.util.CoordinateSet;
import dev.javadrinker.entertainer.mapGame.objects.GameObject;
import dev.javadrinker.entertainer.mapGame.GameState;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

//
public class DataDump extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("datadump") && !PermissionUtil.checkPermission(event.getMember(), Permission.ADMINISTRATOR)) {
            Map<CoordinateSet, GameObject> gameObjectMap = GameState.getOccupyingObjects();

            event.reply("Please wait...").queue();

            String outputString = "";
            for (Map.Entry<CoordinateSet, GameObject> gameObjectEntry : gameObjectMap.entrySet()) {
                if (outputString.length() < 1800) {
                    outputString += gameObjectEntry.getKey().toString() + gameObjectEntry.getValue().toString();
                } else {
                    event.getHook().sendMessage(outputString).queue();
                    outputString = "";
                }
            }

            event.getHook().sendMessage(outputString).setEphemeral(true).queue();
            outputString = "";

            for (Map.Entry<CoordinateSet, GameObject> gameObjectEntry : gameObjectMap.entrySet()) {
                if (outputString.length() < 1500) {
                    outputString += gameObjectEntry.getKey().x() + "," + gameObjectEntry.getKey().y() + "=";
                    switch (gameObjectEntry.getValue().type) {
                        case DANGER_OBJECT:
                            outputString += "d";
                            break;
                        case MOVABLE_OBJECT:
                            outputString += "m";
                            break;
                        case WALL_OBJECT:
                            outputString += "w";
                            break;
                    }
                    outputString += "/";
                } else {
                    event.getHook().sendMessage(outputString).setEphemeral(true).queue();
                    outputString = "";
                }
            }
            event.getHook().sendMessage(outputString).setEphemeral(true).queue();


        }
    }
}
