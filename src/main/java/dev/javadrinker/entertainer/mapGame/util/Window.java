package dev.javadrinker.entertainer.mapGame.util;

import dev.javadrinker.entertainer.Experiments;
import dev.javadrinker.entertainer.mapGame.GameState;
import dev.javadrinker.entertainer.mapGame.objects.PlayerObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.getPlayers;
import static dev.javadrinker.entertainer.mapGame.util.Draw.draw;

public class Window {

    private static ArrayList<Window> allWindows = new ArrayList();

    private InteractionHook hook;

    private Message message;
    private long userID;

    public Window(InteractionHook hook, long userID) {
        this.userID=userID;
        this.hook=hook;

        if (exists(userID)) {
            allWindows.remove(get(userID));
            allWindows.add(this);
        }

    }

    public long getUserID() {
        return userID;
    }

    public InteractionHook getHook() {
        return hook;
    }

    public Message getMessage() {
        return message;
    }

    public static ArrayList<Window> getAllWindows() {
        return allWindows;
    }

    public static Window get(long userID) {
        for(Window w: allWindows) {
            if (w.userID==userID) {
                return w;
            }
        }
        return null;
    }

    public static boolean remove(long userID) {
        for(Window w:allWindows) {
            if (w.getUserID()==userID) {
                allWindows.remove(w);
                w.message.delete().queue();
                return true;
            }
        }
        return false;
    }

    public static boolean exists(User user) {
        for (Window w:allWindows) {
            if (w.userID==user.getIdLong()) {
                return true;
            }
        }
        return false;
    }
    public static boolean exists(long userID) {
        for (Window w:allWindows) {
            if (w.userID==userID) {
                return true;
            }
        }
        return false;
    }


    public static void varnish() {
        EmbedBuilder eb = new EmbedBuilder();
        for (Window w : allWindows) {
            InteractionHook windowHook = w.hook;

            if (PlayerObject.get(w.userID) != null) {
                eb.clearFields();

                CoordinateSet location = PlayerObject.get(w.userID).location;

                eb.addField("mapGame: v"+Experiments.getConfig().get("VERSION"), draw(location.x(), location.y()), true);
                eb.addField("", "Currently displaying for: " + w.userID
                        + "\n Version: " + Experiments.getConfig().get("VERSION")
                        + "\n Currently at coordinates: " + PlayerObject.get(w.userID).location, true);
                eb.setFooter("Heavily inspired by PolyMars. https://www.youtube.com/@PolyMars");
                windowHook.editMessageEmbedsById((w.message.getId()), eb.build()).queue();
            }
        }
    }


    public static void paint(InteractionHook hook, @NotNull User user) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.gray);

        Button leftButton = Button.primary("left_button", "←");
        Button rightButton = Button.primary("right_button", "→");
        Button upButton = Button.primary("up_button", "↑");
        Button downButton = Button.primary("down_button", "↓");

        CoordinateSet location = PlayerObject.get(user).location;
        eb.addField("mapGame: v"+Experiments.getConfig().get("VERSION"), draw(location.x(), location.y()), true);
        eb.addField("", "Currently displaying for: " + user.getAsMention()
                + "\n Version: " + Experiments.getConfig().get("VERSION")
                + "\n Currently at coordinates: " + PlayerObject.get(user).location, true);
        eb.setFooter("Heavily inspired by PolyMars. https://www.youtube.com/@PolyMars");

        if (!Window.exists(user)) {
            hook.sendMessageEmbeds(eb
                     .build())
                    .addActionRow(leftButton, rightButton, upButton, downButton)
                    .setEphemeral(false)
                    .queue( message -> {
                        if (Window.exists(user)) {
                            Window.get(user.getIdLong()).message.delete().queue();
                        }
                        allWindows.remove(get(user.getIdLong()));
                        Window window = new Window(hook, user.getIdLong());
                        window.message=message;
                        allWindows.add(window);
                    });
        } else {
            varnish();
        }
    }
}
