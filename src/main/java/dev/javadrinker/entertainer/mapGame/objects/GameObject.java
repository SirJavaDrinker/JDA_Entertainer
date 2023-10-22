package dev.javadrinker.entertainer.mapGame.objects;

import dev.javadrinker.entertainer.mapGame.GameState;
import dev.javadrinker.entertainer.mapGame.util.CoordinateSet;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
//
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.getPlayers;
import static dev.javadrinker.entertainer.mapGame.objects.PlayerObject.spawn;

public class GameObject {
    protected static ArrayList<GameObject> allObjects = new ArrayList();

    public ObjectTypes type;
    public CoordinateSet location;
    public Long ID;
    public String sprite;

    public GameObject(ObjectTypes objectType, CoordinateSet objectLocation) {
        super();
        Random random = new Random();
        type = objectType;
        location = objectLocation;
        ID = random.nextLong(2147483647L);
        allObjects.add(this);
        defSprite();
    }

    public GameObject(ObjectTypes objectType, CoordinateSet objectLocation, Long objectID) {
        super();
        type = objectType;
        location = objectLocation;
        ID = objectID;
        allObjects.add(this);
        defSprite();
    }

    private void defSprite() {
        switch (type){
            case DANGER_OBJECT -> sprite=":warning:";
            case MOVABLE_OBJECT -> sprite=":package:";
            case WALL_OBJECT -> sprite=":white_square_button:";
            case ENTITY_OBJECT -> {
                if (this instanceof PlayerObject) {
                    sprite = PlayerObject.get(ID).getSprite();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "[UUID=" + ID + ", Location=" + location + ", Type=" + type + "]";
    }


    public boolean moveObject(int x, int y, InteractionHook hook, User user) {
        CoordinateSet newLocation = new CoordinateSet(location.x() + x, location.y() + y);
        CoordinateSet moveAmount = new CoordinateSet(x, y);

        if (occupying(newLocation)) {
            GameObject target = get(newLocation);
            if (target.type.equals(ObjectTypes.MOVABLE_OBJECT)) {
                boolean pushWorked = target.moveObject(moveAmount.x(), moveAmount.y(), hook, user);
                if (pushWorked) {
                    replace(newLocation, this);
                    remove(location);
                    location = newLocation;
                    allObjects.add(target);
                    return true;
                } else {
                    return false;
                }
            } else if (target.type.equals(ObjectTypes.WALL_OBJECT)) {
               return false;
            } else if (target.type.equals(ObjectTypes.DANGER_OBJECT)) {
                if (this.type.equals(ObjectTypes.ENTITY_OBJECT)) {
                    spawn(user);
                }
                return false;
            } else if (target.type.equals(ObjectTypes.ENTITY_OBJECT)){
                if (target instanceof PlayerObject) {
                    ((PlayerObject) (target)).setTemporarySprite(":rage:", 1);
                }
                return false;
            } else {
                return false;
            }
        } else {
            remove(location);
            location = newLocation;
            allObjects.add(this);
            return true;
        }
    }

    public static GameObject get(CoordinateSet coordinateSet) {
        ArrayList<GameObject> objects = new ArrayList<>();
        for (GameObject o:allObjects) {
            if (o.location.equals(coordinateSet)) {
                return o;
            }
        }
        return null;
    }
    public static GameObject get(long ID) {
        for (GameObject o:allObjects) {
            if (o.ID.equals(ID)) {
                return o;
            }
        }
        return null;
    }
    public static ArrayList<GameObject> getAll(CoordinateSet coordinateSet) {
        ArrayList<GameObject> objects = new ArrayList<>();
        for (GameObject o:allObjects) {
            if (o.location.equals(coordinateSet)) {
                objects.add(o);
            }
        }
        return objects;
    }

    public static boolean occupying(CoordinateSet location) {
        for(GameObject o:allObjects) {
            if (o.location.equals(location)) {
                return true;
            }
        }
        return false;
    }

    public static void replace(CoordinateSet location, GameObject object) {
        allObjects.remove(get(location)); allObjects.add(object); // Don't judge me.
    }

    public static void remove(CoordinateSet location) {
        allObjects.remove(get(location));
    }

    public static void remove(long ID) {
        allObjects.remove(get(ID));
    }

}
