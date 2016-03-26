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
public class JDataPanel extends JPanel {

    public Enviroment env;
    private Image img;
    private int old_XX, old_test_Y, old_out_y,old_err_Y;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        //if (img == null) {
        img = createImage(this.getWidth(), this.getHeight());
        //}
        Graphics2D g2dBuffer = (Graphics2D) img.getGraphics();
        //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

        for (int i = 0; i < env.data.SampleList.size(); i++) {

            double test = env.data.SampleList.get(i).testData[0];
            int testYY = (int) (test * (this.getHeight()));

            double out = env.data.SampleList.get(i).outputData[0];
            int outYY = (int) (out * (this.getHeight()));

            double err = env.data.SampleList.get(i).sampleRms.getSampletError();
            int errYY = (int) (this.getHeight()- ((err/env.data.getMaxError()) * (this.getHeight())));

            int XX = (int) (((this.getWidth()) / env.data.SampleList.size()) * i);

            g2dBuffer.setColor(Color.red);
            g2dBuffer.drawLine(old_XX, old_test_Y, XX, testYY);

            g2dBuffer.setColor(Color.blue);
            g2dBuffer.drawLine(old_XX, old_out_y, XX, outYY);

            g2dBuffer.setColor(Color.MAGENTA);
            g2dBuffer.drawLine(old_XX, old_err_Y, XX, errYY);

            old_XX = XX;
            old_test_Y = testYY;
            old_out_y = outYY;
            old_err_Y=errYY;
        }

        old_XX = 0;
        old_out_y = 0;
        old_test_Y = 0;

        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        g2d.drawImage(img, 0, 0, null);
    }

    public JDataPanel(Enviroment env) {
        this.env = env;
    }

}
