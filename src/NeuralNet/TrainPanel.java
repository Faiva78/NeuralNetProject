/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

/**
 *
 * @author alessia
 */
public class TrainPanel extends JPanel {

    public Enviroment env;

    private Image Img;
    private double xx_old = 0;
    private double yy_old = 0;
    
    public int width=600;
    public int height=400;

    public TrainPanel(Enviroment env) {
        this.env = env; 

        
    }


    
    @Override
    public void paint(Graphics gg) {
       super.paint(gg); //To change body of generated methods, choose Tools | Templates.

        Graphics2D g2d = (Graphics2D) gg;
        
        if (Img==null) {
                                width=getWidth();
        height=getHeight();
            Img=createImage(width, height);    

        }
        
        
        
        Graphics2D g2d_buff = (Graphics2D) Img.getGraphics();

        // draw the min error line
        drawAxes(g2d_buff);

        // draw the error data
        drawErrorData(g2d_buff);

        //draw the text data
        drawDataText(g2d_buff);

        g2d.drawImage(Img, 0, 0, null);

    }

    private void drawErrorData(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int h = this.getHeight();
        int w = this.getWidth();
        g2d.setColor(Color.BLACK);

        double xx = 0;
        double yy = 0;

        double err = 0;
        double bat = 0;

        err = env.data.getDataError();
        bat = env.backPropagation.trainingBatches;

        yy = ((1 - err) * h);
        xx = ((bat / env.NumOfEpochs) * w);

        g2d.draw(new Line2D.Double(xx_old, yy_old, xx, yy));
        xx_old = xx;
        yy_old = yy;
    }

    private void drawAxes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.draw(new Line2D.Double(0, ((1 - env.minError) * height), width, (1 - env.minError) * height));
    }

    private void drawDataText(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int fontSize = 18;
        int spacing = 200;
        int border = 30;

        g2d.setColor(Color.black);
        g2d.fillRect(border, 0, this.getHeight(), 80);
        g2d.setColor(Color.WHITE);

        // error
        g2d.setFont(new Font("arial", Font.PLAIN, fontSize));
        StringBuilder str = new StringBuilder();
        str.append(String.format("Error: %.5f", env.data.getDataError()));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize);

        //epoch
        str = new StringBuilder();
        str.append(String.format("Epoch: %d", env.Epoch));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize * 2);

//        epoch/sec
        str = new StringBuilder();
        //long batcSec = (long) (((double) env.partialTraining / (double) env.delay) * 1000);
        str.append(String.format("Epoch/sec: %d", env.batSec()));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize * 3);

        // samples
        str = new StringBuilder();
        str.append(String.format("Samples: %.0f", env.numOfSamples));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize);

//        //samples
//        str = new StringBuilder();
//        str.append(String.format("ETA/MU: %.2f/%.2f", BackPropagation.ETA, BackPropagation.MOMENTUM));
//        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 2);

        //topology
        str = new StringBuilder();
        str.append(String.format("Topology: %s", env.net.getTopology()));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 3);

    }

}
