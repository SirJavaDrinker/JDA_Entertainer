package dev.javadrinker.entertainer;

import dev.javadrinker.entertainer.commands.RegisterCommands;
import dev.javadrinker.entertainer.events.JoinUpsertCommandsEvent;
import dev.javadrinker.entertainer.mapGame.GameState;
import dev.javadrinker.entertainer.GitHidden.NickForce;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Experiments {
    private static final Dotenv config = Dotenv.configure().ignoreIfMissing().load();
    ///
    public static Dotenv getConfig() {
        return config;
    }

    private static String token = config.get("TOKEN");
    private static JDABuilder builder = JDABuilder.createDefault(token);
    private static JDA jda;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static LocalDateTime startTime = LocalDateTime.now();

    public static void main(String args[]) throws Exception {

        builder = JDABuilder.createDefault(token);
        RegisterCommands.register(builder);

        jda = builder.setActivity(Activity.watching("You."))
                .addEventListeners(
                        new JoinUpsertCommandsEvent(),
                        new GameState()
                )
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.SCHEDULED_EVENTS
                )
                .build()
                .awaitReady();

        upsertCommands();

    }

    public static JDA getJDA() {
        return jda;
    }

    public static void upsertCommands() {


        jda.getGuilds();
        for (Guild guild : jda.getGuilds()) {
            if (guild != null) {
                System.out.println("Loading upserting commands for guild [" + guild.getId() + "].");

                jda.getGuildById(guild.getId()).upsertCommand("archive", "Archive a channel.")
                        .addOption(OptionType.CHANNEL, "channel", "The channel you would like to archive.", false)
                        .queue();

                jda.getGuildById(guild.getId()).upsertCommand("start", "Start the game...").queue();

                jda.getGuildById(guild.getId()).upsertCommand("datadump", "Datadump...").queue();

                jda.getGuildById(guild.getId()).upsertCommand("unixepochtime", "Get the current epoch time in milliseconds.").queue();
            } else {
                System.out.println("Guild ID was null!");
            }
        }
    }
}
