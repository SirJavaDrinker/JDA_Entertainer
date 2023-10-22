package dev.javadrinker.entertainer.events;

import dev.javadrinker.entertainer.Experiments;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
public class JoinUpsertCommandsEvent extends ListenerAdapter implements EventListener {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Experiments.upsertCommands();
    }
}
