/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Delaunay;

import java.util.Objects;

/**
 *
 * @author Matus and Marek
 */
// class, which representing data structure Edge
public class Edge {

    //variables
    public Point3D p1;
    public Point3D p2;

    public Edge(Point3D p1, Point3D p2) {
        //initialized variables
        this.p1 = p1;
        this.p2 = p2;
    }

    // method, which changes the orientation of the edge
    public void swap() {
        Point3D tmp;
        tmp = p1;
        p1 = p2;
        p2 = tmp;
    }

    @Override
    // method for comparing objects
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) obj;
        if ((e.p1 == p1) && (e.p2 == p2)) {
            return true;
        }
        return false;
    }

    @Override
    //method, which Returns a hash code value for the object
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    //method, which swapp edges
    public Edge swappedEdge() {
        return new Edge(p2, p1);
    }

    @Override
    // method, which set string format
    public String toString() {
        return String.format("E: p1: %h, p2: %h", p1, p2);
    }

}
