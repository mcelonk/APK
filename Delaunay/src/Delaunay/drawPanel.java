/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Delaunay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Matus and Marek
 */
public class drawPanel extends javax.swing.JPanel {

    //variables
    Point3D[] points;
    List<Edge> edges;
    List<Triangle> triangles;
    Path2D triangulation;
    boolean expositionCalc;
    boolean slopeCalc;
    boolean hypsCalc;

    /**
     * Creates new form drawPanel
     */
    public drawPanel() {
        //initialized variables
        points = new Point3D[0];
        triangulation = new Path2D.Double();
        edges = new LinkedList<>();
        triangles = new LinkedList<>();
        expositionCalc = false;
        slopeCalc = false;
        initComponents();
    }

    @Override
    // method, where all the drawing
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;

        int width;
        int height;
        // width and heigth drawpanel
        width = this.getWidth();
        height = this.getHeight();

        gfx.setColor(Color.BLACK);
        //draws the points
        for (int i = 0; i < points.length; i++) {
            int x;
            int y;
            x = (int) (points[i].getX() * width);
            y = (int) ((1 - points[i].getY()) * height);

            gfx.drawLine(x - 5, y - 5, x + 5, y + 5);
            gfx.drawLine(x - 5, y + 5, x + 5, y - 5);

        }

        int i = 255;
        triangulation = new Path2D.Double();
        // cycles through all triangles
        for (Triangle t : triangles) {
            gfx.setColor(Color.black);
            Path2D epath = new Path2D.Double();
            // adds edges from the triangles 
            epath.moveTo(t.p1.getX(), t.p1.getY());
            epath.lineTo(t.p2.getX(), t.p2.getY());
            epath.lineTo(t.p3.getX(), t.p3.getY());
            epath.lineTo(t.p1.getX(), t.p1.getY());
            //scale 
            AffineTransform at = AffineTransform.getScaleInstance(width, -height);
            epath.transform(at);
            at = AffineTransform.getTranslateInstance(0, height);
            epath.transform(at);
            gfx.draw(epath);
            // calculate hypsometric and set color
            if (hypsCalc != false) {
                double hypsColor = (t.alt * 255 * 5);
                System.out.println(hypsColor);
                if (hypsColor >= 0 && hypsColor < 25.5) {
                    gfx.setColor(new Color(112, 153, 89));
                    gfx.fill(epath);
                }
                if (hypsColor >= 25.5 && hypsColor < 2 * 25.5) {
                    gfx.setColor(new Color(169, 191, 120));
                    gfx.fill(epath);
                }
                if (hypsColor >= 2 * 25.5 && hypsColor < 3 * 25.5) {
                    gfx.setColor(new Color(231, 232, 155));
                    gfx.fill(epath);
                }
                if (hypsColor >= 3 * 25.5 && hypsColor < 4 * 25.5) {
                    gfx.setColor(new Color(242, 229, 153));
                    gfx.fill(epath);
                }
                if (hypsColor >= 4 * 25.5 && hypsColor < 5 * 25.5) {
                    gfx.setColor(new Color(242, 213, 141));
                    gfx.fill(epath);
                }
                if (hypsColor >= 5 * 25.5 && hypsColor < 6 * 25.5) {
                    gfx.setColor(new Color(230, 187, 131));
                    gfx.fill(epath);
                }
                if (hypsColor >= 6 * 25.5 && hypsColor < 7 * 25.5) {
                    gfx.setColor(new Color(209, 159, 130));
                    gfx.fill(epath);
                }
                if (hypsColor >= 7 * 25.5 && hypsColor < 8 * 25.5) {
                    gfx.setColor(new Color(201, 147, 137));
                    gfx.fill(epath);
                }
                if (hypsColor >= 8 * 25.5 && hypsColor < 9 * 25.5) {
                    gfx.setColor(new Color(230, 190, 201));
                    gfx.fill(epath);
                }
                if (hypsColor >= 9 * 25.5 && hypsColor <= 10 * 25.5) {
                    gfx.setColor(new Color(255, 242, 255));
                    gfx.fill(epath);
                }
            }

            // calculate slope and set color
            if (slopeCalc != false) {
                int slopecolor = (int) (t.sl);
                slopecolor = (int) (2.8 * slopecolor);
                int colors = (int) t.getSlope();
                gfx.setColor(new Color(255 - slopecolor, 255 - slopecolor, 255 - slopecolor));
                gfx.fill(epath);
            }

            // calculate exposition and set color
            if (expositionCalc != false) {
                double exp = Math.toDegrees(t.getExp());
                if (exp >= 347.5 || exp < 22.5) {
                    gfx.setColor(new Color(255, 0, 0));
                    gfx.fill(epath);
                }
                if (exp >= 22.5 && exp < 67.5) {
                    gfx.setColor(new Color(255, 0, 180));
                    gfx.fill(epath);
                }
                if (exp >= 67.5 && exp < 112.5) {
                    gfx.setColor(new Color(0, 0, 255));
                    gfx.fill(epath);
                }
                if (exp >= 112.5 && exp < 157.5) {
                    gfx.setColor(new Color(255, 127, 80));
                    gfx.fill(epath);
                }
                if (exp >= 157.5 && exp < 202.5) {
                    gfx.setColor(new Color(255, 255, 0));
                    gfx.fill(epath);
                }
                if (exp >= 202.5 && exp < 257.5) {
                    gfx.setColor(new Color(152, 251, 152));
                    gfx.fill(epath);
                }
                if (exp >= 257.5 && exp < 302.5) {
                    gfx.setColor(new Color(0, 250, 0));
                    gfx.fill(epath);
                }
                if (exp >= 302.5 && exp < 347.5) {
                    gfx.setColor(new Color(0, 150, 250));
                    gfx.fill(epath);
                }

            }

        }
        // cycles through all triangles
        for (Triangle t : triangles) {
            int[] x, y;
            x = new int[3];
            y = new int[3];
            //set triangles coordinates
            x[0] = (int) (t.p1.getX() * width);
            y[0] = (int) (t.p1.getY() * -height + height);
            x[1] = (int) (t.p2.getX() * width);
            y[1] = (int) (t.p2.getY() * -height + height);
            x[2] = (int) (t.p3.getX() * width);
            y[2] = (int) (t.p3.getY() * -height + height);
        }

        gfx.setColor(Color.RED);
        // cycles through all edges
        for (Edge e : edges) {
            //draw contours
            Path2D epath = new Path2D.Double();
            epath.moveTo(e.p1.getX(), e.p1.getY());
            epath.lineTo(e.p2.getX(), e.p2.getY());
            AffineTransform at = AffineTransform.getScaleInstance(width, -height);
            epath.transform(at);
            at = AffineTransform.getTranslateInstance(0, height);
            epath.transform(at);
            gfx.draw(epath);
        }

        gfx.setColor(Color.RED);
        AffineTransform at = AffineTransform.getScaleInstance(width, -height);
        triangulation.transform(at);
        at = AffineTransform.getTranslateInstance(0, height);
        triangulation.transform(at);
        gfx.draw(triangulation);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
