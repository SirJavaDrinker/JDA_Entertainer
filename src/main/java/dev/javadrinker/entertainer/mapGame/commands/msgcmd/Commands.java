package dev.javadrinker.entertainer.mapGame.commands.msgcmd;

import dev.javadrinker.entertainer.mapGame.GameState;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.Map;

public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!PermissionUtil.checkPermission(event.getMember(), Permission.ADMINISTRATOR)) {
            return;
        }


        // Allows for objects to be added to the current mapGame level.
        if (firstEquals(event.getMessage().getContentRaw(), ".addtolevel:")) {
            String string = event.getMessage().getContentRaw().replace(".addtolevel:","");
            event.getMessage().delete().queue();
            GameState.addToLevel(string);
        }

        // Allows for the mapGame level to forcefully set to a certain thing.
        else if (firstEquals(event.getMessage().getContentRaw(), ".setlevel:")) {
            String string = event.getMessage().getContentRaw().replace(".setlevel:","");
            event.getMessage().delete().queue();
            GameState.setLevel(string);
        }

        // Lists all connections.
        else if (firstEquals(event.getMessage().getContentRaw(), ".allconnections:")) {
            String string = event.getMessage().getContentRaw().replace(".allconnections","");
            event.getMessage().delete().queue();
            String str = "";
            if (GameState.getDisplayWindows().isEmpty()) {
                str="No connections found.";
            } else {
                str="Found ("+GameState.getDisplayWindows().size()+") connections: \n";
            }
            for (Map.Entry entry : GameState.getDisplayWindows().entrySet()) {
                Message message = (Message) entry.getValue();

                String name = message.getJDA().getUserById(entry.getKey().toString()).getName();
                String guild = message.getGuild().getName();

                str+="- [**"+name+"** connected via **"+guild+"**](<"+message.getJumpUrl()+">)\n";
            }
            event.getMessage().reply(str).queue();
        }
    }

    private static boolean firstEquals(String str1, String str2) {

        // Ensures that the check should run in the first place.
        if (str1.length()<str2.length()) {
            return false;
        }

        // Checks if the first [x] characters in string 1 are equal to string 2.
        for (int i = 0; i<str2.length(); i++) {
            if (str1.charAt(i)!=str2.charAt(i)) {
                return false;
            }
        }

        return true;
    }

}
