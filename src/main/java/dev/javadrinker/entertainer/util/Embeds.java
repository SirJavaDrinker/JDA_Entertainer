package dev.javadrinker.entertainer.util;

import net.dv8tion.jda.api.EmbedBuilder;
//
import java.awt.*;


public class Embeds {

    static public EmbedBuilder customEmbed(String title, String field) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.gray);
        eb.addField(title, field, false);
        eb.build();

        return eb;
    }
}
