package company.distance;

import company.Point;
import company.Space;

/**
 * Created by root on 06.11.15.
 */
public class EuclideanDistanceCounter implements DistanceCounter {

    @Override
    public double countDistance(Point from, Point to) {
        int total = 0;
        for (int i = 0; i < from.getSize(); i++) {
            total += Math.pow(from.getCoord(i) - to.getCoord(i), 2);
        }
        return Math.sqrt(total);
    }

}
