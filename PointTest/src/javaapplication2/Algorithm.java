/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.Point;
import java.awt.Polygon;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;


/**
 *
 * @author Marek and Matus
 */
public class Algorithm {    
    
    //enum variable
    public enum PositionEnum {
        INSIDE, OUTSIDE, BOUNDARY, POINT
    }
    
    //method, which counts the dot product
    public static double dotProd(double ux, double uy, double vx, double vy) {
        return ux * vx + uy * vy;
    }

    // method for counting length of vectors
    public static double len(double ux, double uy) {
        return sqrt(ux * ux + uy * uy);
    }

    // method, which counts angle between two vectors
    public static double angle(double ux, double uy, double vx, double vy) {
        double prod = dotProd(ux, uy, vx, vy);
        double ulen = len(ux, uy);
        double vlen = len(vx, vy);
        return acos(prod / (ulen * vlen));
    }

    // method, which uses winding algorithm to find out if point lies within or out polygon
    public static PositionEnum pointPolygonWinding(Point pt, Polygon poly) {
        double sumAngle = 0;
        final double eps = 0.01;
        boolean bound = false;
        boolean spoint = false;

        double ux = poly.xpoints[0] - pt.x;
        double uy = poly.ypoints[0] - pt.y;
        double anglepoint = 0;
        //cycle through all points of polygon
        for (int i = 1; i < poly.npoints + 1; i++) {
            if (poly.xpoints[i - 1] == pt.x && poly.ypoints[i - 1] == pt.y) {
                spoint = true;
            }
            double vx = poly.xpoints[(i) % poly.npoints] - pt.x;
            double vy = poly.ypoints[(i) % poly.npoints] - pt.y;
            anglepoint = angle(ux, uy, vx, vy);
            sumAngle += angle(ux, uy, vx, vy);
            // check if the point lies on polygon boundary
            if (abs(anglepoint - Math.PI) < eps) {
                bound = true;
            }
            ux = vx;
            uy = vy;
        }
        
        //// result, if point is within or out of polygon or on the boundary
        //or point is identical with polygons vertex
        if (bound) {
            return PositionEnum.BOUNDARY;
        } else if (abs(sumAngle - 2 * Math.PI) < eps) {
            return PositionEnum.INSIDE;
        } else if (spoint) {
            return PositionEnum.POINT;
        } else {
            return PositionEnum.OUTSIDE;
        }
    }

    // method, which uses winding algorithm to find out if point lies within or out polygon
    public static PositionEnum pointPolygonRay(Point pt, Polygon poly) {
        int k = 0;
        boolean spoint = false;

        double xir = poly.xpoints[0] - pt.x;
        double yir = poly.ypoints[0] - pt.y;
        
        //cycle through all points of polygon
        for (int i = 1; i < poly.npoints + 1; i++) {
            if (poly.xpoints[i - 1] == pt.x && poly.ypoints[i - 1] == pt.y) {
                spoint = true;
                
            }
            double xiir = poly.xpoints[(i) % poly.npoints] - pt.x;
            double yiir = poly.ypoints[(i) % poly.npoints] - pt.y;

            if ((yiir > 0) && (yir - 1 <= 0) || (yir - 1 > 0) && (yiir <= 0)) {
                double xm = (xiir * yir - xir * yiir) / (yiir - yir);
                if (xm > 0) {
                    k++;
                }
            }
            xir = xiir;
            yir = yiir;

        }
        /// result, if point is within or out of polygon
        //or point is identical with polygons vertex
        if (k % 2 == 1) {
            return PositionEnum.INSIDE;
        } else if (spoint) {
            return PositionEnum.POINT;
        } else {
            return PositionEnum.OUTSIDE;
        }
    }

}
