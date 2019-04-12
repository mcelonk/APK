/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setoperations;

import java.awt.geom.Point2D;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 
 */
public class Algorithms {
    
    // vzorce pro výpočet průsečíku
    public static IntersectionPoint calcIntersection(Edge e1, Edge e2) {
        double ux, uy, vx, vy, wx, wy;
        ux = e1.end.getX() - e1.start.getX();
        uy = e1.end.getY() - e1.start.getY();
        vx = e2.end.getX() - e2.start.getX();
        vy = e2.end.getY() - e2.start.getY();
        wx = e1.start.getX() - e2.start.getX();
        wy = e1.start.getY() - e2.start.getY();

        double k1, k2, k3;
        k1 = vx * wy - vy * wx;
        k2 = ux * wy - uy * wx;
        k3 = vy * ux - vx * uy;

        double alpha, beta;
        alpha = k1 / k3;
        beta = k2 / k3;

        // není průsečík
        if (alpha > 1 || beta > 1 || alpha < 0 || beta < 0) {
            return null;
        }

        Point2D pt;
        // * ux a uy posunuji se po vektoru o alpha
        pt = new Point2D.Double(e1.start.getX() + alpha * ux, e1.start.getY() + alpha * uy);
        
        // vracím průsečík
        return new IntersectionPoint(pt, e1, e2, alpha, beta);
    }

    public static List<IntersectionPoint> allIntersections(Polygon polyA, Polygon polyB) {
        List<IntersectionPoint> ints;
        ints = new LinkedList<>();

        // přidá všechny intersec body
        for (Edge ea : polyA.edges) {
            for (Edge eb : polyB.edges) {
                IntersectionPoint pt;
                pt = calcIntersection(ea, eb);
                if (pt != null) {
                    ints.add(pt);
                }
            }
        }

        return ints;
    }
    
    
    public static List<Edge> divideEdge(Edge e, List<IntersectionPoint> points) {
        List<IntersectionPoint> myInts;
        myInts = new LinkedList<>();
        
        // pokud jsou průsečíky na linii e - přidat do listu
        for (IntersectionPoint pt : points) {
            if (pt.e1 == e || pt.e2 == e) {
                myInts.add(pt);
            }
        }

        // lambada funkce
        // dostane dva prvky které porovná a na zákaldě toho je třídí
        Collections.sort(myInts, (IntersectionPoint t, IntersectionPoint t1) -> {
            double c1, c2;

            // pokud linie e je e1 pro průsečík porovnáme alphu jinak betu
            if (t.e1 == e) {
                c1 = t.alpha;
            } else {
                c1 = t.beta;
            }
            if (t1.e1 == e) {
                c2 = t1.alpha;
            } else {
                c2 = t1.beta;
            }
            if (c1 < c2) {
                return -1;
            } else if (c1 > c2) {
                return 1;
            }
            return 0;
        });

        List<Edge> divided;
        divided = new LinkedList<>();

        // linie bez průsečíků
        if (myInts.size() == 0) {
            divided.add(e);
            return divided;
        }

        // nasegmetování linie na více linií podle průsečíků
        divided.add(new Edge(e.start, myInts.get(0).point));
        for (int i = 0; i < myInts.size() - 1; i++) {
            divided.add(new Edge(myInts.get(i).point, myInts.get(i + 1).point));
        }
        divided.add(new Edge(myInts.get(myInts.size() - 1).point, e.end));

        return divided;
    }

    // nasegmentuje cely polygon
    public static Polygon divideAll(Polygon poly, List<IntersectionPoint> points) {
        Polygon out;
        out = new Polygon();
        for (Edge e : poly.edges) {
            out.edges.addAll(divideEdge(e, points));
        }
        return out;
    }
    
    
    // position enum
    public enum PositionEnum {
        INSIDE, OUTSIDE, BOUNDARY
    }

    // skalární součin
    public static double dotProd(double ux, double uy, double vx, double vy) {
        return ux * vx + uy * vy;
    }
    
    // délka vektoru
    public static double len(double ux, double uy) {
        return sqrt(ux * ux + uy * uy);
    }
    
    // úhel vektorů
    public static double angle(double ux, double uy, double vx, double vy) {
        double prod = dotProd(ux, uy, vx, vy);
        double ulen = len(ux, uy);
        double vlen = len(vx, vy);
        return acos(prod / (ulen * vlen));
    }
    
    // urceni orientace
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
    
    
    protected enum OrientationEnum {
        CW, COLINEAR, CCW
    }
    protected static final double EPSILON = Double.MIN_VALUE;

    // winding pro určování polohy bod linie
    public static PositionEnum pointPolygonWinding(Point2D pt, Polygon poly) {
        double sumAngle = 0;
        final double eps = 0.01;

        for (Edge e : poly.edges) {
            double ux = e.start.getX() - pt.getX();
            double uy = e.start.getY() - pt.getY();

            double vx = e.end.getX() - pt.getX();
            double vy = e.end.getY() - pt.getY();

            if (getOrientation(pt, e.start, e.end) == OrientationEnum.CCW) {
                sumAngle = sumAngle - angle(ux, uy, vx, vy);
            } else {
                sumAngle = sumAngle + angle(ux, uy, vx, vy);
            }
        }

        if (abs(sumAngle) >= (2 * Math.PI - eps)) {
            return PositionEnum.INSIDE;
        }
        return PositionEnum.OUTSIDE;
    }

    // rozhoduje zda je prostředek linie uvnitř či venku polygonu 
    public static void setInside(Edge e, Polygon poly) {
        Point2D middle;
        middle = new Point2D.Double(
                (e.start.getX() + e.end.getX()) / 2,
                (e.start.getY() + e.end.getY()) / 2);

        e.side = pointPolygonWinding(middle, poly);
    }

    // nastaví jestli se linie nachází uvnitř či vně pro oba polygony
    public static void setInside(Polygon polyA, Polygon polyB) {
        for (Edge e : polyA.edges) {
            setInside(e, polyB);
        }
        for (Edge e : polyB.edges) {
            setInside(e, polyA);
        }
    }

    // vytvoří list edgů s požadovanou orientací
    public static List<Edge> filterEdges(Polygon poly, PositionEnum side) {
        List<Edge> out = new LinkedList<>();
        for (Edge e : poly.edges) {
            if (e.side == side) {
                out.add(new Edge(e.start,e.end));
            }
        }

        return out;
    }

    public static OrientationEnum getPolygonOrientation(Polygon poly) {
        double sumAngle = 0;
        final double eps = 0.01;

        Point2D pt;
        pt = poly.edges.get(0).start;
        // vynechám první a poslední
        List<Edge> otherEdges = poly.edges.subList(1, poly.edges.size() - 1);

        for (Edge e : otherEdges) {
            double ux = e.start.getX() - pt.getX();
            double uy = e.start.getY() - pt.getY();

            double vx = e.end.getX() - pt.getX();
            double vy = e.end.getY() - pt.getY();

            if (getOrientation(pt, e.start, e.end) == OrientationEnum.CCW) {
                sumAngle = sumAngle - angle(ux, uy, vx, vy);
            } else {
                sumAngle = sumAngle + angle(ux, uy, vx, vy);
            }
        }

        if (sumAngle > 0) {
            return OrientationEnum.CCW;
        }
        return OrientationEnum.CW;
    }
  

    public static List<Polygon> buildRings(List<Edge> edges) {
        List<Polygon> out = new LinkedList<>();
        Polygon poly = new Polygon();
        
        if(edges.isEmpty()){
            return out;
        }
        
        // vezmu edgu
        Edge cure = edges.get(0);
        // smažu ze seznamu
        edges.remove(0);
        // přidám do polygonu
        poly.edges.add(cure);

        // dokud neni edge prázdný
        while (!edges.isEmpty()) {
            
            // nastavím next na null
            Edge next = null;
            for (Edge e : edges) {
                if (cure.end.equals(e.start)) {
                    // nemazu v cyklu
                    next = e;
                    break;
                }
                // pokud narazím na obrácenou - swap
                else if (cure.end.equals(e.end)){
                    e.swap();
                    next = e;
                    break;
                }
            }         
            
            // když uzavřeš ring
            if(next.end.equals(poly.edges.get(0).start)){
                poly.edges.add(next);
                edges.remove(next);
                
                // orientation
                if (getPolygonOrientation(poly) == OrientationEnum.CCW) {
                    poly.side = PositionEnum.OUTSIDE;
                } else {
                    poly.side = PositionEnum.INSIDE;
                }

                out.add(poly);
                poly = new Polygon();
                
                // když neni seznam prázdný
                if (!edges.isEmpty()) {
                    cure = edges.get(0);
                    edges.remove(cure);
                    // přidám do polygonu
                    poly.edges.add(cure);
                }
            }
            // přidávej pokud si už neudělal ring
            else {
                edges.remove(next);
                poly.edges.add(next);
                cure = next;
            }
        }


        return out;
    }

    // all outs
    public static List<Polygon> polyUnion(Polygon polyA, Polygon polyB) {
        List<Edge> edges = new LinkedList<>();

        edges.addAll(filterEdges(polyA, PositionEnum.OUTSIDE));
        edges.addAll(filterEdges(polyB, PositionEnum.OUTSIDE));

        // ringy - poskládají linie do správného pořadí
        // vytvoří jeden obvod - kvůli správnému kreslení
        return buildRings(edges);
    }

    // all ins
    public static List<Polygon> polyIntersect(Polygon polyA, Polygon polyB) {
        List<Edge> edges = new LinkedList<>();

        edges.addAll(filterEdges(polyA, PositionEnum.INSIDE));
        edges.addAll(filterEdges(polyB, PositionEnum.INSIDE));

        return buildRings(edges);
    }

    // in B and out A
    public static List<Polygon> polyDiff(Polygon polyA, Polygon polyB) {
        List<Edge> edges = new LinkedList<>();
        
        edges.addAll(filterEdges(polyA, PositionEnum.OUTSIDE));
        edges.addAll(filterEdges(polyB, PositionEnum.INSIDE));
        


        return buildRings(edges);
    }
}
