/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convexhull;

import java.util.List;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 *
 * @author Marek a Matus
 */
public class Algorithms {
    //variable
    public static final double EPSILON = 0.0001;
    
    //enum variable
    public enum OrientationEnum {CW,COLINEAR,CCW}
    
    //method, which determines the orientation
    public static OrientationEnum getOrientation(Point2D p1, Point2D p2, Point2D p3){
         double val = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX()) - 
                  (p2.getX() - p1.getX()) * (p3.getY() - p2.getY()); 
         if (abs(val) < EPSILON){
             return OrientationEnum.COLINEAR;
         }else if (val > 0){
             return OrientationEnum.CW;
         }else {
             return OrientationEnum.CCW;
         }
    }
    
    //method, which counts the dot product
    public static double dotProd(double ux, double uy, double vx, double vy){
        return ux*vx + uy*vy;
    }
    
    
    // method for counting length of vectors
    public static double len(double ux, double uy){
        return sqrt(ux*ux + uy*uy);
    }
    // method, which counts angle between two vectors
    public static double dotProdNorm(double ux, double uy, double vx, double vy){
        double prod = dotProd(ux, uy, vx, vy);
        double ulen = len(ux,uy);
        double vlen = len(vx,vy);
        return prod/(ulen*vlen);
    }
    // method, which counts angle between two vectors
    public static double angle(double ux, double uy, double vx, double vy){
        return acos(dotProdNorm(ux,uy,vx,vy));
    }
    
    // method, which uses jarvis scan algorithm to find convex hull
    public static Path2D jarvisScan(Point2D [] points){
       //variables
        Path2D hull;
        hull = new Path2D.Double();
        
        LinkedList<Point2D> hullPoints;
        hullPoints = new LinkedList<>();
        
        Point2D miny;
        miny = points[0];
        //find min y
        for (Point2D pt : points){
            if (pt.getY() < miny.getY()){
                miny = pt;
            }
        }
        //add points in hull
        hull.moveTo(miny.getX(),miny.getY());
        hullPoints.add(new Point2D.Double(miny.getX()-1,miny.getY()));
        hullPoints.add(miny);
        
        //inicialized points
        Point2D prevPt;
        Point2D curPt;
        curPt = miny;
        prevPt = hullPoints.getFirst();
        
        int count = 0;
        //cycle through all points 
        do {
            double ux;
            double uy;
            //create ux uy vector
            ux = prevPt.getX()- curPt.getX();
            uy = prevPt.getY()- curPt.getY();
            //initialized points      
            double min;
            Point2D minPt;
            min = Double.MAX_VALUE;
            minPt = null;
            
            for (Point2D pt: points){
                if ((pt == curPt)||(pt == prevPt)){
                    continue;
                }
                //create vx, vy vector
                double vx,vy;
                vx = curPt.getX() - pt.getX();
                vy = curPt.getY() - pt.getY();
                double m = angle(ux,uy,vx,vy);
                
                // control for singularity if points are located on same line
                if (m == min){
                    double first = Math.sqrt((curPt.getX() - minPt.getX())*(curPt.getX() - minPt.getX()) + 
                                   (curPt.getY() - minPt.getY())*(curPt.getY() - minPt.getY()));
                    double second = Math.sqrt((curPt.getX() - pt.getX())*(curPt.getX() - pt.getX()) + 
                                   (curPt.getY() - pt.getY())*(curPt.getY() - pt.getY()));
                    if (first < second){
                        minPt = pt;
                    }
                }
                else if (m < min){
                    min = m;
                    minPt = pt;
                }   
                
            }
            //add min point and inicialized points
            hullPoints.add(minPt);
            prevPt = curPt;
            curPt = minPt;
            
        }
        while (hullPoints.getLast()!=miny);
        //create 2 arrays
        double xs[];
        double ys[];
        xs = new double[hullPoints.size()-1];
        ys = new double[hullPoints.size()-1];
        
        //create hull
        for (int i=1;i<hullPoints.size();i++){
            hull.lineTo(hullPoints.get(i).getX(), hullPoints.get(i).getY());
        }
        hull.lineTo(hullPoints.get(1).getX(),hullPoints.get(1).getY());
    
        return hull;
    }
    
    
   
    
    // method, which uses Sweep line algorithm to find convex hull
    public static Path2D sweepHull(Point2D [] points){
        Path2D hull;
        hull = new Path2D.Double();
        //sort array by x
        Arrays.sort(points, (Point2D p1, Point2D p2) -> Double.compare(p1.getX(), p2.getX()));
        
        //variable
        List<Point2D> upperHull;
        List<Point2D> lowerHull;
        
        upperHull = new LinkedList<>();
        lowerHull = new LinkedList<>();
        
        //add points in upperhull and lov=werhull
        for (Point2D pt: points){
            upperHull.add(pt);
            lowerHull.add(pt);
        }
        
        //fix convexity
        fixConvexity(upperHull, OrientationEnum.CW);
        fixConvexity(lowerHull, OrientationEnum.CCW);
        
        hull.moveTo(lowerHull.get(0).getX(), lowerHull.get(0).getY());
        //create convex hull
        for (Point2D pt : lowerHull){
            hull.lineTo(pt.getX(), pt.getY());
        }
        // convert upperhull list
        Collections.reverse(upperHull);
        
        for (Point2D pt : upperHull){
            hull.lineTo(pt.getX(), pt.getY());
        }
        
        return hull;
    }
    
    //method for determining the point of the line
    public static void fixConvexity(List<Point2D> points,OrientationEnum orientation){
        if (points.size() < 3){
            return;
        }
        //create and inicialized variables
        ListIterator<Point2D> iterator;
        iterator = points.listIterator();
        Point2D prev;
        Point2D cur;
        Point2D next;
        prev = iterator.next();
        cur = iterator.next();
        next = iterator.next();

        
        while (true){
            ///points have a proper orientation
            if (getOrientation(prev,cur,next) == orientation){
                prev = cur;
                cur = next;
                if (!iterator.hasNext()){
                    break;
                }
                next = iterator.next();
                continue;
            }
            //points have not a proper orientation
            Point2D tmp;
            iterator.previous();//move between cur a next
            iterator.previous();//move between prev a cur 
            iterator.remove(); // delete cur
            
            iterator.previous();//move ahead prev
            
            if (!iterator.hasPrevious()){
                iterator.next();//mvoe for prev
                cur = iterator.next();//nove for  cur
                if (!iterator.hasNext()){
                    break;
                }
                next = iterator.next();
                              
            }else{
                
            iterator.next();
            //inicialized points
            cur = iterator.previous();
            prev = iterator.previous();
            iterator.next();
            iterator.next();
            tmp = iterator.next();
            }
         }
        
    }
    
    // method, which uses Quick hull algorithm to find convex hull
    public static Path2D quickHull(Point2D [] points){
        //variables
        Path2D hull;
        hull = new Path2D.Double();
        
        Point2D minx;
        Point2D maxx;
        minx = points[0];
        maxx = points[0];
        //find min x and max x 
        for (Point2D pt : points){
            if (pt.getX() < minx.getX()){
                minx = pt;
            }
            if (pt.getX() > maxx.getX()){
                maxx = pt;
            }
        }
        
        hull.moveTo(maxx.getX(),maxx.getY());
        
    
        
        List<Point2D>[] splitted = splitPoints(minx, maxx, Arrays.asList(points));
        //recursiv call method qh
        Point2D[] upperHull = qh(maxx,minx,splitted[0]);
        Point2D[] lowerHull = qh(minx,maxx,splitted[1]);
        
        //create convex hull
        for (Point2D pt: lowerHull){
            hull.lineTo(pt.getX(), pt.getY());
        }

        hull.lineTo(minx.getX(), minx.getY());
        for (Point2D pt: upperHull){
            hull.lineTo(pt.getX(), pt.getY());
        }

        hull.lineTo(maxx.getX(),maxx.getY());

        return hull;
    }
    //global procedur for quic hull
    private static Point2D[] qh(Point2D start, Point2D end, List<Point2D> points){
        if (points.size() == 0){
            return new Point2D[0];
        }
        
        double max = -1;
        Point2D farthestPt = null;
        
        for (Point2D pt: points){
            //distance from line
            double dist = distanceFromLine(start, end, pt);
            if (dist > max){
                max = dist;
                farthestPt = pt;
            }
        }
        
        if (farthestPt == null){
        }
        //devide points to startPTS and endPts
        List<Point2D>[] startPts = splitPoints(farthestPt,start,points);
        List<Point2D>[] endPts = splitPoints(end,farthestPt, points);
        if (startPts[0].size() + startPts[1].size() + 2 != points.size()){
        }
        
        //recursive call qh 
        Point2D[] startHull = qh(start,farthestPt,startPts[0]);
        Point2D[] endHull = qh(farthestPt,end,endPts[0]);
        
        Point2D[] res;
        res = new Point2D[startHull.length+endHull.length+1];
        
        //determination of position points
        int residx = 0;
        for (Point2D pt: endHull){
            res[residx] = pt;
            residx++;
        }
        res[residx] = farthestPt;
        residx++;
        for (Point2D pt: startHull){
            res[residx] = pt;
            residx++;
        }
        
        return res;
        
      
    
    }
    
    //method that divides the points according to the position of the line
    public static List<Point2D> [] splitPoints(Point2D start, Point2D end, List<Point2D> points){
        //variables
        double epsilon = 0.0001;
        
        List<Point2D>[] res = new List[2];
        res[0] = new LinkedList<>();
        res[1] = new LinkedList<>();
        //storing points in the list
        for (Point2D pt: points){
            if (pt == start || pt == end){
                continue;
            }
            double det = (end.getX()-start.getX())*(pt.getY()-start.getY()) - 
                    (end.getY()-start.getY())*(pt.getX() - start.getX());
            if (det > epsilon){
                res[0].add(pt);
            }else if (det < -epsilon){
                res[1].add(pt);
            }
        }
        
        return res;
        
    
    }
    //method, distance from line
    public static double distanceFromLine(Point2D start, Point2D end, Point2D pt){
        double nom;
        double denom;
        
        nom = abs((end.getY()-start.getY())*pt.getX() - 
                  (end.getX()-start.getX())*pt.getY() +
                  end.getX()*start.getY() - 
                  end.getY()*start.getX());
        denom = sqrt((end.getY()-start.getY())*
                     (end.getY()-start.getY())+
                     (end.getX()-start.getX())*
                     (end.getX()-start.getX()));
        return nom/denom;
    }
    // method, which uses Graham scan algorithm to find convex hull
    public static Path2D grahamScan(Point2D[] points){
        //variables
        Path2D hull = new Path2D.Double();
        Point2D miny = points[0];
        //find miny
        for (Point2D pt : points) {
            if (pt.getY() < miny.getY()) {
                miny = pt;
            }
        }
        //point representing x axis
        Point2D X;
        X = new Point2D.Double(0, miny.getY());
        //vector u
        double ux = X.getX() - miny.getX();
        double uy = X.getY() - miny.getY();
        
        //Creation of map, which automaticly sort values
        Map<Double,Point2D> map;
        map = new TreeMap();

        double angle = 0;
        // cycle through all points
        for (Point2D addPoint : points) {
            angle=0;
            //vector v
            double vx = addPoint.getX() - miny.getX();
            double vy = addPoint.getY() - miny.getY();
            // angle between vectors
            angle = angle(ux, uy, vx, vy);
            map.put(angle,addPoint);
        }
        //create stack
        Stack convexHull = new Stack();
        //inicialized points
        Point2D prevPt = miny;
        Point2D curPt = (Point2D) map.values().toArray()[0];
        
        //add points in hull
        convexHull.push(prevPt);
        convexHull.push(curPt);

        for (Point2D nextPt : map.values()) {
            
            if (nextPt == curPt) {
                continue;
            }
            //calculate determinant to find out, if nextPt is positioned left or right 
            double det = (curPt.getX() - prevPt.getX()) 
                        * (nextPt.getY() - prevPt.getY()) 
                        - (curPt.getY() - prevPt.getY()) 
                        * (nextPt.getX() - prevPt.getX());
            //if point is left 
            if (det <=0) {
                //push point
                convexHull.push(nextPt);
                prevPt = curPt;
                curPt = nextPt;
            } else {
                //pop point
                convexHull.pop();
                //continue with next points
                while (convexHull.size() > 1) {

                    curPt = (Point2D) convexHull.pop();
                    prevPt = (Point2D) convexHull.peek();

                    det = (curPt.getX() - prevPt.getX()) 
                        * (nextPt.getY() - prevPt.getY()) 
                        - (curPt.getY() - prevPt.getY()) 
                        * (nextPt.getX() - prevPt.getX());
                    //if point is left
                    if (det <=0) {
                        convexHull.push(curPt);
                        convexHull.push(nextPt);
                        prevPt = curPt;
                        curPt = nextPt;

                        break;
                    }
                    
                }
            }
        }
        
        hull.moveTo(miny.getX(), miny.getY());
        //create convex hull
        while (convexHull.size() > 0) {
            Point2D pt = (Point2D) convexHull.pop();
            hull.lineTo(pt.getX(), pt.getY());
        }
        
        return hull;
    }
}
    

