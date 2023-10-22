package dev.javadrinker.entertainer.mapGame;

import dev.javadrinker.entertainer.mapGame.objects.GameObject;
import dev.javadrinker.entertainer.mapGame.objects.ObjectTypes;
import dev.javadrinker.entertainer.mapGame.util.CoordinateSet;

import java.util.HashMap;
import java.util.Map;
//
public class MapData {

    public static Map<CoordinateSet, GameObject> readLevelString(String input) {
        input = input.replace("{", "");
        input = input.replace("}", "");
        input = input.replace("\n", "");

        Map<CoordinateSet, GameObject> objectMap = new HashMap<>();

        // '/' splits each object.
        String[] sets = input.split("/");

        // Taking the split up string and going through each
        for (String s : sets) {

            // Breaking away the identification of the type of object.
            String[] coordinateAndType = s.split("\\=");
            ObjectTypes type = ObjectTypes.MOVABLE_OBJECT;

            // Reading the type of object.
            switch (coordinateAndType[1]) {
                case "w":
                    type = ObjectTypes.WALL_OBJECT;
                    break;
                case "b":
                    type = ObjectTypes.MOVABLE_OBJECT;
                    break;
                case "d":
                    type = ObjectTypes.DANGER_OBJECT;
                    break;
            }

            // Taking the remaining two numbers and separating them to get a coordinate set.
            String[] stringCoordinate = coordinateAndType[0].split(",");
            //Trying to convert to a set of coordinates.
            try {
                CoordinateSet coordinateSet = new CoordinateSet(Integer.parseInt(stringCoordinate[0]), Integer.parseInt(stringCoordinate[1]));
                GameObject gameObject = new GameObject(type, coordinateSet);
                objectMap.put(coordinateSet, gameObject);
            } catch (Exception e) {

            }
        }
        return objectMap;
    }

    public static String getLevelString(Map<CoordinateSet, GameObject> gameObjectMap) {
        String outputString = "";
        for (Map.Entry<CoordinateSet, GameObject> gameObjectEntry : gameObjectMap.entrySet()) {
            outputString += gameObjectEntry.getKey().x() + "," + gameObjectEntry.getKey().y() + "=";
            switch (gameObjectEntry.getValue().type) {
                case DANGER_OBJECT:
                    outputString += "d";
                    break;
                case MOVABLE_OBJECT:
                    outputString += "m";
                    break;
                case WALL_OBJECT:
                    outputString += "w";
                    break;
            }
            outputString += "/";
        }
        return outputString;
    }
}
