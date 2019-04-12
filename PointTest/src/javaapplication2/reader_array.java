/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek and Matus
 */
public class reader_array {

    public static void main(String args[]) {
        retArray(null);
    }

    // method which reads data from .txt file and create Polygon array 
    public static Polygon[] retArray(Polygon args[]) {
        try {

            // variables
            File file = JFrame.f;
            Scanner fileScanner = new Scanner(file);
            int n;

            // scanning of lines and values
            // variables got from .txt file
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String token = lineScanner.next();
            int size = Integer.parseInt(token);
            Polygon[] f_polygons = new Polygon[size];
            int position = 0;

            // while cyclus which reads all lines in .txt 
            while (fileScanner.hasNextLine()) {

                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);
                token = lineScanner.next();
                n = Integer.parseInt(token);

                // create new arrays of coordinations 
                int[] xpoints = new int[n];
                int[] ypoints = new int[n];
                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);

                // add x coordinations to array 
                for (int i = 0; i < xpoints.length; i++) {
                    token = lineScanner.next();
                    String[] arr = token.split(" ");
                    xpoints[i] = Integer.parseInt(token);
                }

                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);

                // add y coordinations to array
                for (int i = 0; i < ypoints.length; i++) {
                    token = lineScanner.next();
                    String[] arr = token.split(" ");
                    ypoints[i] = Integer.parseInt(token);
                }

                // add polygons to array
                f_polygons[position] = new Polygon(xpoints, ypoints, n);
                position++;

            }
            
            // close scanner function and return array of polygons
            fileScanner.close();
            return f_polygons;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reader_array.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
