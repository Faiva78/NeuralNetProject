/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

/**
 *
 * @author alessia
 */
public class JTrainHistoryPanel extends JPanel {

   // public Enviroment env;
    public Data data;
    public int epoch;
    
    public int totalEpoch;
    public double minimumError;
    
    private Image Img;
    private double xx_old = 0;
    private double mean_yy_old = 0;
    private double Dmean_yy_old = 0;
    private double err_yy_old = 0;

    public JTrainHistoryPanel() {
    }

    public JTrainHistoryPanel(Data data,  int totalEpoch, double minimumError) {
        this.data = data;
        this.totalEpoch = totalEpoch;
        this.minimumError = minimumError;
    }
    
    
    

    public void clear() {

        xx_old = -10;
        mean_yy_old = -10;
        Dmean_yy_old = -10;
        err_yy_old = -10;
        Img = createImage(this.getWidth(), this.getHeight());
    }

    @Override
    public void paint(Graphics gg) {
        super.paint(gg); //To change body of generated methods, choose Tools | Templates.
        
        if (data !=null) {
            
      
        
        Graphics2D g2d = (Graphics2D) gg;

        if (Img == null) {
            Img = createImage(this.getWidth(), this.getHeight());

        }

        Graphics2D g2d_buff = (Graphics2D) Img.getGraphics();

        // draw the min error line
        drawAxes(g2d_buff);

        // draw the error data
        drawErrorData(g2d_buff);

        g2d.drawImage(Img, 0, 0, null);
        }
    }

    private void drawErrorData(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        double err = data.getDataError();
        double bat = epoch;

        double err_yy = ((1 - err) * this.getHeight());
        double mean_yy = (1 - data.errorTrack.mean()) * this.getHeight();
        double meanD_yy = this.getHeight() - (((0.5 + data.errorTrack.meanDer())) * this.getHeight());

        double xx = ((bat / totalEpoch) * this.getWidth());

        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(xx_old, err_yy_old, xx, err_yy));

        g2d.setColor(Color.RED);
        g2d.draw(new Line2D.Double(xx_old, mean_yy_old, xx, mean_yy));

        g2d.setColor(Color.BLUE);
        g2d.draw(new Line2D.Double(xx_old, Dmean_yy_old, xx, meanD_yy));
        
        //g2d.setColor(Color.WHITE);
        //g2d.fillRect((int)xx+1, 0, 15, this.getHeight());
        
        xx_old = xx;
        err_yy_old = err_yy;
        mean_yy_old = mean_yy;
        Dmean_yy_old = meanD_yy;
    }

    private void drawAxes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.draw(new Line2D.Double(0, ((1 - minimumError) * this.getHeight()), this.getWidth(), (1 - minimumError) * this.getHeight()));
    }

}
