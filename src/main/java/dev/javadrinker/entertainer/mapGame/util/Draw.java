package dev.javadrinker.entertainer.mapGame.util;

import dev.javadrinker.entertainer.Experiments;
import dev.javadrinker.entertainer.mapGame.GameState;
import dev.javadrinker.entertainer.mapGame.objects.GameObject;
import dev.javadrinker.entertainer.mapGame.objects.PlayerObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static dev.javadrinker.entertainer.mapGame.objects.ObjectTypes.*;
import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.getPlayers;

public class Draw {
    private static int test1 = 0;
    public static String draw(int cX, int cY) {
        String lineOut = "";
        for (int y = cY + 3; y > cY - 4; y--) {
            for (int x = cX - 3; x < cX + 4; x++) {
                boolean hasAdded = false;

                for (PlayerObject p : getPlayers()) {
                    if (new CoordinateSet(x, y).equals(p.location)) {
                        lineOut += p.getSprite();
                        test1++;
                        System.out.println(p.getSprite() + " "+ test1);
                        hasAdded = true;
                    }
                }

                if (!hasAdded) {
                    if (GameObject.occupying(new CoordinateSet(x,y))) {
                        lineOut += GameObject.get(new CoordinateSet(x,y)).sprite;
                    } else {
                        lineOut += (":black_large_square:");
                    }
                }
            }
            lineOut = lineOut + "\n";
        }
        return lineOut;
    }
}
