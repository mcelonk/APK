/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Delaunay;

import java.util.List;
import java.awt.geom.Point2D;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Matus and Marek
 */
public class Algorithms {

    //variables
    public static final double EPSILON = 0.000001;

    public enum PositionEnum {
        INSIDE, OUTSIDE, BOUNDARY
    }

    public enum OrientationEnum {
        CW, COLINEAR, CCW
    }

    // method, which does the determinant test
    // and determinates orientation of third point based on a test
    public static OrientationEnum getOrientation(Point2D p1, Point2D p2, Point2D p3) {
        double val = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX())
                - (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());
        if (abs(val) < EPSILON) {
            return OrientationEnum.COLINEAR;
        } else if (val > 0) {
            return OrientationEnum.CW;
        } else {
            return OrientationEnum.CCW;
        }
    }

    //method, which calculate orientation
    public static OrientationEnum getOrientation(Point3D p1, Point3D p2, Point3D p3) {
        return getOrientation(p1.toPoint2D(), p2.toPoint2D(), p3.toPoint2D());
    }

    //method, which counts the dot product
    public static double dotProd(double ux, double uy, double vx, double vy) {
        return ux * vx + uy * vy;
    }

    //method, which counts the dot product (point3D)
    public static double dotProd(Point3D u, Point3D v) {
        return u.getX() * v.getX() + u.getY() * v.getY() + u.getZ() * v.getZ();

    }

    // method for counting length of vectors
    public static double len(double ux, double uy) {
        return sqrt(ux * ux + uy * uy);
    }

    // method for counting length of vectors
    public static double len(Point3D v) {
        return sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
    }

    // method, which counts angle between two vectors
    public static double dotProdNorm(double ux, double uy, double vx, double vy) {
        double prod = dotProd(ux, uy, vx, vy);
        double ulen = len(ux, uy);
        double vlen = len(vx, vy);
        return prod / (ulen * vlen);
    }

    // method, which counts angle between two vectors
    public static double angle(double ux, double uy, double vx, double vy) {
        return acos(dotProdNorm(ux, uy, vx, vy));
    }

    //method, which calculate angle 
    public static double angle(Point3D u, Point3D v) {
        return acos(dotProd(u, v) / (len(u) * len(v)));
    }

    //method, which calculate distance from line
    public static double distanceFromLine(Point2D start, Point2D end, Point2D pt) {
        double nom;
        double denom;

        nom = abs((end.getY() - start.getY()) * pt.getX()
                - (end.getX() - start.getX()) * pt.getY()
                + end.getX() * start.getY()
                - end.getY() * start.getX());
        denom = sqrt((end.getY() - start.getY())
                * (end.getY() - start.getY())
                + (end.getX() - start.getX())
                * (end.getX() - start.getX()));
        return nom / denom;
    }

    // method which calculates distance between two points
    public static double dist(Point3D p1, Point3D p2) {
        return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
                + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    // method which calculates circle radius and middle point
    public static double circleRadius(Point3D p1, Point3D p2, Point3D p3) {
        double x1 = p1.getX();
        double x2 = p2.getX();
        double x3 = p3.getX();
        double y1 = p1.getY();
        double y2 = p2.getY();
        double y3 = p3.getY();

        double k1 = x1 * x1 + y1 * y1;
        double k2 = x2 * x2 + y2 * y2;
        double k3 = x3 * x3 + y3 * y3;
        double k4 = y1 - y2;
        double k5 = y1 - y3;
        double k6 = y2 - y3;
        double k7 = x1 - x2;
        double k8 = x1 - x3;
        double k9 = x2 - x3;
        double k10 = x1 * x1;
        double k11 = x2 * x2;
        double k12 = x3 * x3;

        double mnom = (k12 * (-k4) + k11 * k5 - (k10 + k4 * k5) * k6);
        double mdenom = (-k4) * x3 + x2 * k5 + x1 * (-k6);
        double m = 0.5 * mnom / mdenom;

        double nnom = k1 * (-k9) + k2 * k8 + k3 * (-k7);
        double ndenom = y1 * (-k9) + y2 * k8 + y3 * (-k7);

        double n = 0.5 * nnom / ndenom;
        //middle point
        Point3D middle = new Point3D(m, n, 0);

        double radius = dist(middle, p2);

        if (getOrientation(p1, p2, middle) == OrientationEnum.CW) {
            radius = -radius;
        }

        return radius;
    }

    // method which searches a point which makes minimum bounding circle 
    public static Point3D minimalBoundingCircle(Edge e, Point3D[] points) {
        Point3D minPoint = null;
        double minradius = Double.MAX_VALUE;

        //minimum radius
        for (Point3D p : points) {

            if (p == e.p1 || p == e.p2) {
                continue;
            }

            // ignores points on the other side
            if (getOrientation(e.p1, e.p2, p) != OrientationEnum.CCW) {
                continue;
            }

            double radius = circleRadius(e.p1, e.p2, p);
            //calculate radius
            if (radius < minradius) {
                minPoint = p;
                minradius = radius;
            }

        }
        return minPoint;
    }

    // method which checks if edge already exists in list 
    private static void addToAel(List<Edge> ael, Edge edge) {
        Iterator<Edge> it = ael.iterator();
        Edge swapped = edge.swappedEdge();

        while (it.hasNext()) {
            Edge e;
            e = it.next();
            if (e.equals(swapped)) {
                it.remove();
                return;
            }
        }
        ael.add(edge);
    }

    // method, which calculates delaunay triangulation
    public static List<Triangle> delaunay(Point3D[] points) {
        List<Triangle> dt = new LinkedList<>();

        Point3D p1 = points[0];
        Point3D p2 = null;
        double mindist = Double.MAX_VALUE;

        // finds point with minimum distance
        for (Point3D p : points) {
            if (p == p1) {
                continue;
            }
            if (dist(p1, p) < mindist) {
                p2 = p;
                mindist = dist(p1, p);
            }
        }
        Edge e = new Edge(p1, p2);
        // finds a point which makes minimum bounding circle
        Point3D p = minimalBoundingCircle(e, points);

        Edge e2;
        Edge e3;
        // if point which makes minimum bounding circle does not exist on the 
        // one side
        if (p == null) {
            e.swap();
            p = minimalBoundingCircle(e, points);
            e2 = new Edge(p1, p);
            e3 = new Edge(p, p2);
        } else {
            e2 = new Edge(p2, p);
            e3 = new Edge(p, p1);
        }
        if (p == null) {
            System.out.println("Nemam bod!");

        }
        //1.triangle
        dt.add(new Triangle(p1, p2, p));
        //list of triangles
        List<Edge> ael;

        ael = new LinkedList<>();
        // adds edges to AEL
        ael.add(e);
        ael.add(e2);
        ael.add(e3);

        //iterates until AEL is not empty
        while (!ael.isEmpty()) {
            Iterator<Edge> it = ael.iterator();
            e = it.next();
            it.remove();

            e.swap();

            p = minimalBoundingCircle(e, points);

            if (p == null) {
                continue;
            }

            e2 = new Edge(e.p2, p);
            e3 = new Edge(p, e.p1);

            dt.add(new Triangle(e.p1, e.p2, p));

            addToAel(ael, e3);
            addToAel(ael, e2);

        }

        return dt;
    }

    // method, which calculates contours
    public static List<Point3D> calcContourPoints(Point3D p1, Point3D p2, double z) {
        Point3D lower;
        Point3D upper;
        if (p1.getZ() < p2.getZ()) {
            lower = p1;
            upper = p2;
        } else {
            lower = p2;
            upper = p1;
        }
        //height difference
        double dh = upper.getZ() - lower.getZ();
        //distance beetwen points
        double d = dist(lower, upper);

        List<Point3D> pts;
        pts = new LinkedList<>();

        double tmp = Math.floor(lower.getZ() / z);
        double dhtoCont = z - (lower.getZ() - (tmp * z));
        int k = 0;

        // cycles until next contour is higher than height of upper point
        while (k * z + lower.getZ() + dhtoCont < upper.getZ()) {

            // scales vector, shifts it to 0 and scales it back       
            double scale = (dhtoCont + z * k) / dh;
            double x = (upper.getX() - lower.getX()) * scale + lower.getX();
            double y = (upper.getY() - lower.getY()) * scale + lower.getY();
            pts.add(new Point3D(x, y, lower.getZ() + dhtoCont + z * k));
            k++;
        }
        return pts;
    }

    // method, which creates list of lines representing contours
    public static List<Edge> calcContours(Triangle t, double interval) {
        List<Edge> edges;
        edges = new LinkedList<>();

        List<Point3D> ptsp1p2;
        List<Point3D> ptsp2p3;
        List<Point3D> ptsp3p1;
        // calculates intersection points of contours
        ptsp1p2 = calcContourPoints(t.p1, t.p2, interval);
        ptsp2p3 = calcContourPoints(t.p2, t.p3, interval);
        ptsp3p1 = calcContourPoints(t.p3, t.p1, interval);

        // cycle through a lists of intersection points and creates a line
        // from points with the same height
        for (Point3D p : ptsp1p2) {
            for (Point3D p2 : ptsp2p3) {
                if (p.getZ() == p2.getZ()) {
                    edges.add(new Edge(p, p2));
                }
            }
            for (Point3D p2 : ptsp3p1) {
                if (p.getZ() == p2.getZ()) {
                    edges.add(new Edge(p, p2));
                }
            }
        }
        for (Point3D p : ptsp2p3) {
            for (Point3D p2 : ptsp3p1) {
                if (p.getZ() == p2.getZ()) {
                    edges.add(new Edge(p, p2));
                }
            }
        }

        return edges;
    }

    //method, which calculate contours
    public static List<Edge> calcContours(List<Triangle> tl, double interval) {
        List<Edge> edges;
        edges = new LinkedList<>();
        for (Triangle t : tl) {
            List<Edge> tedges;
            tedges = calcContours(t, interval);
            edges.addAll(tedges);
        }
        return edges;
    }

    //calculating determinants
    public static double det3(double a1, double b1, double a2, double b2, double a3, double b3) {
        double a, b, c, d, e, f, g, h, i;
        a = a1;
        b = b1;
        c = 1;
        d = a2;
        e = b2;
        f = 1;
        g = a3;
        h = b3;
        i = 1;
        double det = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);
        return det;
    }

}
