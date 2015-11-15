
package company;

import company.distance.DistanceCounter;
import company.distance.EuclideanDistanceCounter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    private DistanceCounter distanceCounter;
    private int dim;


    public Main(int dim, DistanceCounter distanceCounter) {
        this.distanceCounter = distanceCounter;
        this.dim = dim;
    }

    private static Stream<String> readFile(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath)).lines();
    }

    private Point[] init(List<Point> pointList, int clusterCount) {
        Random random = new Random();
        Point centroids[] = new Point[clusterCount];
        for (int i = 0; i < clusterCount; i++) {
            centroids[i] = Point.of(dim,
                    pointList.get(random.nextInt(pointList.size())).getCoords());
        }
        return centroids;
    }

    public Point getNewCentroid(List<Point> pointsInCluster) {
        double[] newCoords = new double[dim];

        for (int i = 0; i < newCoords.length; i++) {
            for (Point point : pointsInCluster) {
                newCoords[i] += point.getCoord(i);
            }
            newCoords[i] /= pointsInCluster.size();
        }
        return Point.of(dim, newCoords);
    }

    private Map<Point, List<Point>> splitByClusters(List<Point> points, Point[] centroids) {
        Map<Point, List<Point>> pointsByCentroids = new HashMap<>();
        Arrays.stream(centroids).forEach(
                (centroid) -> pointsByCentroids.put(centroid, new LinkedList<>())
        );


        for (Point point : points) {
            double[] distances = Arrays.stream(centroids)
                    .map((centroid) -> distanceCounter.countDistance(point, centroid))
                    .mapToDouble((el) -> el).toArray();
            double min = Arrays.stream(distances).min().getAsDouble();
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] == min) {
                    pointsByCentroids.get(centroids[i]).add(point);
                    break;
                }
            }

        }
        return pointsByCentroids;
    }

    private Map<Point, List<Point>> splutByClusters(Point[] centroids, List<Point> allPoints) {
        int i = 0;
        while (true) {
            Map<Point, List<Point>> pointListMap = splitByClusters(allPoints, centroids);
            // TODO: make 5 a property
            if (i == 5) {
                return pointListMap;
            }
            centroids = pointListMap.keySet().stream().map(
                    (centroid) -> getNewCentroid(pointListMap.get(centroid))
            ).toArray((size) -> new Point[size]);
            i++;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int dim = 4;
        int clusterCount = 3;
        Stream<String> data = readFile("iris/iris.data");
        Map<String, Point> pointsByType = new HashMap<>();
        List<Point> allPoints = new LinkedList<>();
        data.forEach((line) -> {
            String[] dataPoint = line.split(",");
            Point point = Point.of(dim,
                    Double.valueOf(dataPoint[0]),
                    Double.valueOf(dataPoint[1]),
                    Double.valueOf(dataPoint[2]),
                    Double.valueOf(dataPoint[3])
            );
            String type = dataPoint[5];
            pointsByType.put(type, point);
            allPoints.add(point);
        });
        Main knn = new Main(dim, new EuclideanDistanceCounter());
        Point[] centroids = knn
                .init(allPoints, clusterCount);
        knn.splutByClusters(centroids, allPoints);
    }
}
