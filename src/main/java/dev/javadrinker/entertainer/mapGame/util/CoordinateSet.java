package dev.javadrinker.entertainer.mapGame.util;

import java.util.Objects;
//
public class CoordinateSet {
    private int intX;
    private int intY;
    int hashCode;

    public CoordinateSet(int xCoordinate, int yCoordinate) {
        super();
        intX = xCoordinate;
        intY = yCoordinate;
        this.hashCode = Objects.hash(intX, intY);
    }

    public int x() {
        return intX;
    }

    public int y() {
        return intY;
    }

    public void set(int xCoordinate, int yCoordinate) {
        intX = xCoordinate;
        intY = yCoordinate;
    }

    public void add(int xCoordinate, int yCoordinate) {
        intX += xCoordinate;
        intY += yCoordinate;
    }


    @Override
    public String toString() {
        return "[" + intX + "," + intY + "]";
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CoordinateSet) {
            if (((CoordinateSet) obj).intX == intX && ((CoordinateSet) obj).intY == intY) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
