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
import javax.swing.JPanel;

/**
 *
 * @author fauiva78
 */
public final class JFunctionPanel extends JPanel {

    public  Func func;
    public int steps;
    private Data data;
    private Image img;
    private int old_XX;
    private int old_Y ;

    public JFunctionPanel() {
    }

    public JFunctionPanel(Func func, int steps) {
        this.func = func;
        this.steps = steps;
       
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        if (func !=null) {
            data =new Data();
            double STE = 1/ (double)steps;
            Uti.DataFunc1P(func, 0, 1, STE, data);
        }
        
        if (data != null) {
            

            Graphics2D g2d = (Graphics2D) g;
            //if (img == null) {
            img = createImage(this.getWidth(), this.getHeight());
            //}
            Graphics2D g2dBuffer = (Graphics2D) img.getGraphics();
            //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            old_XX=-10;
            old_Y=-10;
            
            for (int i = 0; i < data.SampleList.size(); i++) {
                Data.Sample sample = data.SampleList.get(i);
                
                double test = sample.testData[0];
                
                int testYY = (int) (test * (this.getHeight()));
                double wi=(this.getWidth());
                double ff=(wi / data.SampleList.size());
                
                 int XX = (int) ( ff* i);

                g2dBuffer.setColor(Color.red);
                g2dBuffer.drawLine(old_XX, old_Y, XX, testYY);

                old_XX = XX;
                old_Y = testYY;
            }
            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            g2d.drawImage(img, 0, 0, null);
        }
    }

}
