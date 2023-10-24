package dev.javadrinker.entertainer.mapGame;

import dev.javadrinker.entertainer.Experiments;
import dev.javadrinker.entertainer.mapGame.objects.GameObject;
import dev.javadrinker.entertainer.mapGame.objects.PlayerObject;
import dev.javadrinker.entertainer.mapGame.util.CoordinateSet;
import dev.javadrinker.entertainer.mapGame.util.Window;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.getPlayers;
import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.spawn;
import static dev.javadrinker.entertainer.mapGame.util.Draw.draw;

public class GameState extends ListenerAdapter implements EventListener {
    private static Map<CoordinateSet, GameObject> occupyingObjects = new HashMap<CoordinateSet, GameObject>();

    private static Boolean hasBeenInitialized = false;
    public static ArrayList<String> usedDefaultSprites = new ArrayList<>();
    private static int frameNumber = 0;
    private static long lastInteraction = System.currentTimeMillis();

    private static Map<Long,Message> displayWindowsID = new HashMap<>();
    private static Map<Long, InteractionHook> displayWindowsHook = new HashMap<>();

    private static String level = "" + "-1,1=b/0,1=b/1,1=b/-1,2=b/0,2=b/1,2=b/-1,3=b/0,3=b/1,3=b/" + "0,-1=d/";

    public static Map<Long, Message> getDisplayWindows() {
        return displayWindowsID;
    }

    public static void addToLevel(String string) {
        occupyingObjects.putAll(MapData.readLevelString(string));
    }
    public static void setLevel(String string) {
        occupyingObjects.clear();
        occupyingObjects.putAll(MapData.readLevelString(string));
    }
    public static String getLevel() {
        return level;
    }

    public static boolean checkInitializationStatus() {
        return hasBeenInitialized;
    }

    public static void incrementFrameNumber(String reason) {
        frameNumber++;
        System.out.println("Incrementing to: ["+frameNumber + "] because: [" + reason+"]");
    }

    public static int getFrame() {
        return frameNumber;
    }

    public static void init(InteractionHook hook, User user, boolean shouldStartNewGame) {
        hasBeenInitialized=true;

        if (shouldStartNewGame) {
            occupyingObjects.clear();
            metronome();

            Map<CoordinateSet, GameObject> additiveMap = MapData.readLevelString(level);
            for (Map.Entry<CoordinateSet, GameObject> entry : additiveMap.entrySet()) {
                occupyingObjects.put(entry.getKey(), entry.getValue());
            }
        }

        if (Window.exists(user)) {
            Window.remove(user.getIdLong());
        }
        spawn(user);

        new Window(hook, user.getIdLong()).paint(hook, user);

        gameLoop(hook, user, "Game state init.");
    }

    
    private static void metronome() {
        System.out.println("Metronome init");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis()-lastInteraction<1000*60*5) {
                    incrementFrameNumber("Metronome ticked.");
                    Window.varnish();
                }
            }
        }, 0, 20000 );
    }

    // Function below needs to be removed/replaced.
    private static boolean ensurePlayerExistence(InteractionHook hook, User user) {
        if (!PlayerObject.exists(user)) {
            return false;
        }
        return true;
    }

    // Look into seperating from GameState class into some kind of input handling class for function below.
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        InteractionHook hook = event.getHook();
        User user = event.getUser();
        String str = event.getButton().getId();

        if (!ensurePlayerExistence(hook, user)) {
            event.reply("Please use /start to join.").setEphemeral(true).queue();
        } else {
            if (!hasBeenInitialized) {
                init(event.getHook(), event.getUser(), true);
            }

            if (!event.isAcknowledged()) {
                if (Window.exists(user)) {
                    event.getInteraction().deferEdit().queue();
                } else {
                    event.getInteraction().deferReply().queue();
                }
            }
            str = str.replaceAll("_button", "");
            movePlayer(user, str, hook);
        }
    }

    // The below function needs to be removed and placed elsewhere 
    public static void movePlayer(User user, String str, InteractionHook hook) {
        GameObject player = PlayerObject.get(user);

        boolean valid = true;
        if (str.equalsIgnoreCase("up")) {
            player.moveObject(0, 1, hook, user);
            gameLoop(hook, user, "Player moved up.");
        }
        else if (str.equalsIgnoreCase("right")) {
            player.moveObject(1, 0, hook, user);
            gameLoop(hook, user, "Player moved right.");
        }
        else if (str.equalsIgnoreCase("down")) {
            player.moveObject(0, -1, hook, user);
            gameLoop(hook, user, "Player moved down.");
        }
        else if (str.equalsIgnoreCase("left")) {
            player.moveObject(-1, 0, hook, user);
            gameLoop(hook, user, "Player moved left.");
        }
        else {
            valid=false;
        }
        if (valid){
            lastInteraction=System.currentTimeMillis();
        }
    }

    
    public static void gameLoop(InteractionHook hook, User user, String reason) {
        ensurePlayerExistence(hook, user);

        for (PlayerObject p: getPlayers()) {
            p.perFrameUpdates();
        }
        
        Window.varnish();
        incrementFrameNumber("Game loop ran because: ["+reason+"]");
    }

    
    // To be removed.
    public static Map getOccupyingObjects() {
        return occupyingObjects;
    }


    // Needs to be fixed and added over to the Window class.
    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        for (Map.Entry<Long,Message> entry: displayWindowsID.entrySet()){
            if(entry.getValue().getIdLong() == event.getMessageIdLong()) {
                displayWindowsID.remove(entry.getKey());
            }
        }
        super.onMessageDelete(event);
    }
}
