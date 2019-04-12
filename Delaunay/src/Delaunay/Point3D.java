/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Delaunay;

import java.awt.geom.Point2D;

/**
 *
 * @author Matus and Marek
 */
// class, which representing data structure Point3D
public class Point3D {

    //variables
    private double x;
    private double y;
    private double z;

    public Point3D(double ax, double ay, double az) {
        //initialized variables
        x = ax;
        y = ay;
        z = az;
    }

    public Point2D toPoint2D() {
        return new Point2D.Double(x, y);
    }

    //getters and setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

}
