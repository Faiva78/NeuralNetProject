package NeuralNet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Enviroment to test the nets
 *
 * @author Faiva78
 */
public class Enviroment extends JPanel {

    //-----------------------------------------//
    //----------- CLASS FIELDS ----------------//
    //-----------------------------------------//
    /**
     * list of all the neural nets
     *
     */
    // public ArrayList<Net> NetList = new ArrayList<>();
    /**
     * the data
     *
     */
    public Data data = new Data();
    public Net net = new Net();

    /**
     * List of alll nets that pass the selection
     *
     */
    //public ArrayList<Net> FitnessList = new ArrayList<>();
    public JFrame frame;

    private double xx_old = 0;
    private double yy_old = 0;
    private long Epoch = 0;
    private long partialTraining = 0;
    

    //numner of trainig loops
    public int NumOfEpochs = 250000;

    // minimum fitness error
    public double minError = 0.010;


    public long delay = 250;
    public double numOfSamples = 30;




    private void initFrame() {
        frame = new JFrame();
        frame.add(this);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //this.setSize(frame.getWidth()/2, frame.getHeight()/2);
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g); //To change body of generated methods, choose Tools | Templates.

        // draw the min error line
        drawAxes(g);

        // draw the error data
        drawErrorData(g);

        //draw the text data
        drawDataText(g);

    }

    private void drawErrorData(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int h = this.getHeight();
        int w = this.getWidth();
        g2d.setColor(Color.BLACK);
        double bat = 0;
        double xx = 0;
        double yy = 0;

        bat = Train.trainingBatches;// NetList.get(0).trainingBatches;
        yy = (((1 - data.getDataError())) * h);
        xx = ((bat / NumOfEpochs) * w);

        g2d.draw(new Line2D.Double(xx_old, yy_old, xx, yy));
        xx_old = xx;
        yy_old = yy;
    }

    private void drawAxes(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.draw(new Line2D.Double(0, ((1 - minError) * this.getHeight()), this.getWidth(), (1 - minError) * this.getHeight()));
    }

    private void drawDataText(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        int fontSize = 18;
        int spacing = 200;
        int border = 30;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, this.getHeight(), 80);
        g2d.setColor(Color.WHITE);

        // error
        g2d.setFont(new Font("arial", Font.PLAIN, fontSize));
        StringBuilder str = new StringBuilder();
        str.append(String.format("Error: %.5f", data.getDataError()));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize);

        //epoch
        str = new StringBuilder();
        str.append(String.format("Epoch: %d", Epoch));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize * 2);

        //epoch/sec
        str = new StringBuilder();
        long batcSec = (long) (((double) partialTraining / (double) delay) * 1000);
        partialTraining = 0;
        str.append(String.format("Epoch/sec: %d", batcSec));
        g2d.drawString(str.toString(), border + (spacing * 0), fontSize * 3);

        // samples
        str = new StringBuilder();
        str.append(String.format("Samples: %.0f", numOfSamples));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize);

        //samples
        str = new StringBuilder();
        str.append(String.format("ETA/MU: %.2f/%.2f", BackPropagation.ETA, BackPropagation.MOMENTUM));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 2);

        //topology
        str = new StringBuilder();
        str.append(String.format("Topology: %s", net.getTopology()));
        g2d.drawString(str.toString(), border + (spacing * 1), fontSize * 3);

    }

    /**
     * train enviroment for afunctions
     *
     * @param function he function to train
     */
    public void TrainFunction(Func function, boolean load) {  //TODO  develop enviroment 

        initFrame();
        String fileLocation = "C:\\Users\\alessia/TestNet0.net";

        data.DataFunc1P(function, 0, 1, 1 / numOfSamples);

        net = new Net();
        if (load) {
            Uti.loadNet(fileLocation, net);
        } else {
            net.createNet("1,8,1");
        }
        long time = System.currentTimeMillis();

        //total training
        partialTraining = 0;

        // runs counter
        Epoch = 0;

        //for all trainig loops until fitness solution found or max training loops 
        while ((Epoch < NumOfEpochs)) {

            //increase the counter of total trainings
            Epoch++;

            //increase the counter of partial trainings
            partialTraining++;

            Train.TrainNet(net, data);  //net.TrainNet(errorCompensatrion);

            if ((System.currentTimeMillis() - time) > delay) {

                // paint the data
                this.repaint();

                // reset the current time
                time = System.currentTimeMillis();

            } // ---------------end of output NetData

        }  // ----------------end of trainig cycle 

    }

}
