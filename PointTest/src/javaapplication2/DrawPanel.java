/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;

/**
 *
 * @author Marek and Matus
 */
public class DrawPanel extends javax.swing.JPanel {
    //variables
    public static Point point;
    boolean inside;
    boolean h = false;
    public static Polygon[] poly1;
    public int i = 0;
    public LinkedList winlist1 = new LinkedList();
    Algorithm.PositionEnum res = null;
    

    public DrawPanel() {
        poly1 = JFrame.f_polygons;
        point = new Point(-10, -10);
        inside = false;
        initComponents();
    }
    
    //drawing method
    @Override
    public void paintComponent(Graphics g) {
        poly1 = JFrame.f_polygons;
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        if (JFrame.drawEnable == true) {
            // draw polygons
            for (int i = 0; i < poly1.length; i++) {
                gfx.drawPolygon(poly1[i]);
            }
        }
        
        if (h == true) {
            //cycle through all results
            for (int i = 0; i < winlist1.size(); i++) {
                
                if (winlist1.get(i) == Algorithm.PositionEnum.INSIDE) {
                    //fill polygon, if point is within polygon
                    gfx.setColor(Color.YELLOW);
                    gfx.fillPolygon(poly1[i]);
                    gfx.setColor(Color.BLACK);
                    gfx.drawPolygon(poly1[i]);
                    
                } else if (winlist1.get(i) == Algorithm.PositionEnum.BOUNDARY) {
                    //drawing green polygon, if point is on boundary
                    gfx.setColor(Color.GREEN);
                    gfx.drawPolygon(poly1[i]);
                    
                } else if (winlist1.get(i) == Algorithm.PositionEnum.POINT) {
                    //drawing red point, if point is identical with polygons vertex
                    gfx.setColor(Color.RED);
                    gfx.drawLine(point.x - 5, point.y - 5, point.x + 5, point.y + 5);
                    gfx.drawLine(point.x - 5, point.y + 5, point.x + 5, point.y - 5);
                                        
                } else {
                    //drawing black polygon, if point isout of polygon
                    gfx.setColor(Color.BLACK);
                    gfx.drawPolygon(poly1[i]);                  
                    

                }
                //drawing black point
                gfx.drawLine(point.x - 5, point.y - 5, point.x + 5, point.y + 5);
                gfx.drawLine(point.x - 5, point.y + 5, point.x + 5, point.y - 5);

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        poly1 = JFrame.f_polygons;
        if (JFrame.drawEnable == true) {
            //create new point after mouse clicked
            point.x = evt.getX();
            point.y = evt.getY();
            h = true;
            winlist1.clear();

            //Cycle for algorithm selection
            for (int i = 0; i < poly1.length; i++) {

                if (JFrame.t == (true)) {
                    winlist1.add(Algorithm.pointPolygonWinding(point, poly1[i]));
                } else if (JFrame.t == (false)) {
                    winlist1.add(Algorithm.pointPolygonRay(point, poly1[i]));
                }
            }
            //cycle for decision making
            //if point is within or out of polygon or on the boundary
             //or point is identical with polygons vertex
            for (int j = 0; j < winlist1.size(); j++) {
                if (winlist1.get(j) == Algorithm.PositionEnum.INSIDE) {
                    //point is within polygon
                    inside = true;
                    System.out.println("Inside");
                } else if (winlist1.get(j) == Algorithm.PositionEnum.OUTSIDE) {
                    //point is out of polygon
                    inside = false;
                    System.out.println("Outside");
                } else if (winlist1.get(j) == Algorithm.PositionEnum.POINT) {
                    //point is identical with polygons vertex
                    inside = false;
                    System.out.println("POINT");
                } else {
                    //point is on the boundary 
                    inside = false;
                    System.out.println("Boundary");
                    
                }
                repaint();
    }//GEN-LAST:event_formMouseClicked
        }
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

