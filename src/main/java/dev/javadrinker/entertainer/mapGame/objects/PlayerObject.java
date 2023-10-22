package dev.javadrinker.entertainer.mapGame.objects;

import dev.javadrinker.entertainer.mapGame.GameState;
import dev.javadrinker.entertainer.mapGame.util.CoordinateSet;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.ArrayList;
import java.util.Random;

//
public class PlayerObject extends GameObject {
    private long lastMovement;
    private String sprite = ":neutral_face:";
    private String defaultSprite = ":neutral_face:";
    private int returnFrame = 0;

    private static ArrayList<PlayerObject> players = new ArrayList<>();
    public static ArrayList<PlayerObject> getPlayers() {
        return players;
    }


    public void perFrameUpdates() {
        if (returnFrame<= GameState.getFrame()) {
            sprite=defaultSprite;
        }
    }

    public void setTemporarySprite(String spriteString, int spriteFrames) {
        returnFrame=GameState.getFrame()+spriteFrames;
        sprite=spriteString;
    }

    public PlayerObject(CoordinateSet objectLocation, User user) {
        super(ObjectTypes.ENTITY_OBJECT, objectLocation, user.getIdLong());
        generateDefaultSprite();
    }

    public void ForceLocation(CoordinateSet newLocation) {
        GameState.getOccupyingObjects().remove(location);
        GameState.getOccupyingObjects().put(newLocation, this);
        location=newLocation;
    }

    public String getSprite() {
        return sprite;
    }

    public void generateDefaultSprite() {
        System.out.println("uh");
        Random random = new Random();
        String str = "smile";
        int randomInt = random.nextInt(16);
        switch (randomInt) {
            case 1:
                str="grinning";
                break;
            case 2:
                str="smiley";
                break;
            case 3:
                str="smile";
                break;
            case 4:
                str="grin";
                break;
            case 5:
                str="laughing";
                break;
            case 6:
                str="face_holding_back_tears";
                break;
            case 7:
                str="sweat_smile";
                break;
            case 8:
                str="joy";
                break;
            case 9:
                str="rofl";
                break;
            case 10:
                str="relaxed";
                break;
            case 11:
                str="blush";
                break;
            case 12:
                str="slight_smile";
                break;
            case 13:
                str="face_with_raised_eyebrow";
                break;
            case 14:
                str="zany_face";
                break;
            case 15:
                str="sunglasses";
                break;
            case 16:
                str="flushed";
                break;
        }


        if (GameState.usedDefaultSprites == null) {
            GameState.usedDefaultSprites.add(str);
            sprite = ":"+str+":";
            defaultSprite = ":"+str+":";
            return;
        }

        if (GameState.usedDefaultSprites.contains(str)) {
            generateDefaultSprite();
        } else {
            GameState.usedDefaultSprites.add(str);
            sprite = ":"+str+":";
            defaultSprite = ":"+str+":";
        }
    }

    @Override
    public boolean moveObject(int x, int y, InteractionHook hook, User user) {
        lastMovement=System.currentTimeMillis();
        return super.moveObject(x, y, hook, user);
    }


    public static void spawn(User user) {
        CoordinateSet coordinateSet;
        coordinateSet = new CoordinateSet(0, 0);
        if (exists(user)) {
            PlayerObject playerObject = get(user);
            players.remove(get(user));
            playerObject.ForceLocation(coordinateSet);
            players.add(playerObject);
        } else {
            PlayerObject playerObject = new PlayerObject(coordinateSet, user);
            playerObject.generateDefaultSprite();
            players.add(playerObject);
        }
    }

    public static PlayerObject get(User user) {
        for (GameObject p:allObjects) {
            if (p instanceof PlayerObject) {
                if (p.ID==user.getIdLong()) {
                    return (PlayerObject) p;
                }
            }
        }
        return null;
    }
    public static PlayerObject get(long userID) {
        for (GameObject p:allObjects) {
            if (p instanceof PlayerObject) {
                if (p.ID==userID) {
                    return (PlayerObject) p;
                }
            }
        }
        return null;
    }
    public static PlayerObject get(CoordinateSet coordinateSet) {
        for (GameObject p:allObjects) {
            if (p instanceof PlayerObject) {
                if (p.location.equals(coordinateSet)) {
                    return (PlayerObject) p;
                }
            }
        }
        return null;
    }

    public static boolean exists(User user) {
        for (GameObject p:allObjects) {
            if (p.ID==user.getIdLong()) {
                return true;
            }
        }
        return false;
    }

    public static boolean exists(CoordinateSet c) {
        for (PlayerObject p : players) {
            if (p.location.equals(c)) {
                return true;
            }
        }
        return false;
    }
    public static boolean exists(long userID) {
        for (GameObject p:allObjects) {
            if (p.ID==userID) {
                return true;
            }
        }
        return false;
    }

}
