package dev.javadrinker.entertainer.commands;

import dev.javadrinker.entertainer.GitHidden.Archive;
import dev.javadrinker.entertainer.GitHidden.AsBotCmd;
import dev.javadrinker.entertainer.mapGame.commands.DataDump;
import dev.javadrinker.entertainer.mapGame.commands.StartGame;
import dev.javadrinker.entertainer.mapGame.commands.msgcmd.Commands;
import net.dv8tion.jda.api.JDABuilder;

public class RegisterCommands {
    public static JDABuilder register(JDABuilder builder) {
        builder.addEventListeners(
                new Commands(),
                new DataDump(),
                new StartGame(),
                new UnixEpochTime()
        );

        return builder;
    }
}
