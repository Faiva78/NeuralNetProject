/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.util.ArrayList;

/**
 *
 * @author alessia
 */
public class ErrorTrack {

    public int lenght = 10;
    public ArrayList<Double> array = new ArrayList<>();

    private double lastMean = 0;

    public void add(double item) {
        lastMean = mean();
        while (array.size() >= lenght) {            
            array.remove(0);
        }
   
        array.add(item);

    }

    public double meanDer() {

        return (mean() - lastMean)*1000;
    }

    public double mean() { // FIXIT this cause exception
        try {
            
        
        double mean = 0;
        for (int i = 0; i < array.size(); i++) {
            mean += array.get(i);
        }

        return mean / array.size();
        
        } catch (Exception e) {
            System.out.println("errore in errortrack:"+e.getMessage());
            return 0;
        }
        
    }

    public double min() {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
            }
        }
        return min;
    }

    public double max() {
        double max = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max) {
                max = array.get(i);
            }
        }
        return max;
    }

    public double rms() {
        double sum = 0;
        for (int i = 0; i < array.size(); i++) {
            Double get = array.get(i);
            sum += Math.pow(sum, 2);
        }
        return Math.sqrt(sum);
    }

}
