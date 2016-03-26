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
public class JTrainPanel extends JPanel {

    public Enviroment env;

    private Image Img;
    private double xx_old = 0;
    private double mean_yy_old = 0;
    private double Dmean_yy_old = 0;
    private double err_yy_old = 0;
    //public int width = 600;
    //public int height = 400;

    public JTrainPanel(Enviroment env) {
        this.env = env;

    }

    @Override
    public void paint(Graphics gg) {
        super.paint(gg); //To change body of generated methods, choose Tools | Templates.

        Graphics2D g2d = (Graphics2D) gg;

        if (Img == null) {
            Img = createImage(this.getWidth(), this.getHeight());

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

        double err = env.data.getDataError();
        double bat = env.Epoch;

        double err_yy = ((1 - err) * this.getHeight());
        double mean_yy = (1 - env.errorTrack.mean()) * this.getHeight();
        double meanD_yy =this.getHeight()-(((0.5 + env.errorTrack.meanDer())) * this.getHeight());

        double xx = ((bat / env.NumOfEpochs) * this.getWidth());

        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(xx_old, err_yy_old, xx, err_yy));

        g2d.setColor(Color.RED);
        g2d.draw(new Line2D.Double(xx_old, mean_yy_old, xx, mean_yy));

        g2d.setColor(Color.BLUE);
        g2d.draw(new Line2D.Double(xx_old, Dmean_yy_old, xx, meanD_yy));

        xx_old = xx;
        err_yy_old = err_yy;
        mean_yy_old = mean_yy;
        Dmean_yy_old = meanD_yy;
    }

    private void drawAxes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.draw(new Line2D.Double(0, ((1 - env.minError) * this.getHeight()), this.getWidth(), (1 - env.minError) * this.getHeight()));
    }

    private void drawDataText(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int fontSize = 18;
        int spacing = 200;
        int border = 30;

        g2d.setColor(Color.black);
        g2d.fillRect(border, 0, (int) (this.getWidth() * 0.8), 80);
        g2d.setColor(Color.WHITE);

        // error
        g2d.setFont(new Font("arial", Font.PLAIN, fontSize));
        StringBuilder str = new StringBuilder();
        str.append(String.format("Err mean: %.5f", env.errorTrack.mean()));
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
        
        //errDer
        str = new StringBuilder();
        //long batcSec = (long) (((double) env.partialTraining / (double) env.delay) * 1000);
        str.append(String.format("errDev: %.5f", env.errorTrack.meanDer()));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize * 4);

//-------------------------------------------------------------
        // samples
        str = new StringBuilder();
        str.append(String.format("Samples: %.0f", env.numOfSamples));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize);

        //samples
        str = new StringBuilder();
        str.append(String.format("ETA/MU: %.2f/%.2f", env.backPropagation.ETA, env.backPropagation.MOMENTUM));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 2);
        //topology
        str = new StringBuilder();
        str.append(String.format("Topology: %s", env.net.getTopology()));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 3);

    }

}
