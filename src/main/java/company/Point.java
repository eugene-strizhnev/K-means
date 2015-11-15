package company;

import java.util.Arrays;

/**
 * Created by root on 06.11.15.
 */
public class Point {

    private double[] coords;

    private Point() {
    }

    public double getCoord(int coord) {
        return coords[coord];
    }

    public double[] getCoords() {
        return coords;
    }

    public void setCoord(int coord, double value) {
        coords[coord] = value;
    }

    public int getSize() {
        return coords.length;
    }

    public static Point of(int dim, double... x) {
        if (x.length != dim) {
            throw new IllegalArgumentException("Invalid point dimension");
        }

        Point point = new Point();
        point.coords = new double[dim];
        for (int i = 0; i < dim; i++) {
            point.setCoord(i, x[i]);
        }
        return point;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return Arrays.equals(coords, point.coords);

    }

    @Override
    public int hashCode() {
        return coords != null ? Arrays.hashCode(coords) : 0;
    }
}