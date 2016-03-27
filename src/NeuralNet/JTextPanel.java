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
import javax.swing.JPanel;

/**
 *
 * @author alessia
 */
public class JTextPanel extends JPanel {

    public Enviroment env;

    private Image Img;

    public JTextPanel(Enviroment env) {
        this.env = env;
        
    }

    @Override
    public void paint(Graphics gg) {
        super.paint(gg); //To change body of generated methods, choose Tools | Templates.
        
        Graphics2D g2d = (Graphics2D) gg;
        
       
        Img = createImage(this.getWidth(), this.getHeight());

        Graphics2D g2d_buff = (Graphics2D) Img.getGraphics();

        //draw the text data
        drawDataText(g2d_buff);

        g2d.drawImage(Img, 0, 0, null);

    }


    private void drawDataText(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int fontSize = 18;
        int spacing = 200;
        int border = 30;
        g2d.setColor(Color.BLACK);

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

        //ETA
        str = new StringBuilder();
        str.append(String.format("ETA: %.3f", env.backPropagation.ETA));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 2);
        //samples
        str = new StringBuilder();
        str.append(String.format("MU: %.3f", env.backPropagation.MOMENTUM));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 3);
        //topology
        str = new StringBuilder();
        str.append(String.format("Topology: %s", env.net.getTopology()));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 4);

    }

}
