/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setoperations;

import java.awt.geom.Point2D;

/**
 *
 * @author 
 */
public class Edge {
    protected Point2D start;
    protected Point2D end;
    protected Algorithms.PositionEnum side;
    
    public Edge(Point2D s, Point2D e){
        start = s;
        end = e;
        side = null;
    }
    
    // method, which changes the orientation of the edge
    public void swap() {
        Point2D tmp;
        tmp = start;
        start = end;
        end = tmp;
    }
}
