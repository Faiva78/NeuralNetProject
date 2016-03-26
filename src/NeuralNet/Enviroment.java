package NeuralNet;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Enviroment to test the nets
 *
 * @author Faiva78
 */
public class Enviroment {

    //-----------------------------------------//
    //----------- CLASS FIELDS ----------------//
    //-----------------------------------------//
    public Data data = new Data();
    public Net net = new Net();

    public JFrame frame;
    public TrainPanel trainPanel;

    public long Epoch = 0;
    public long partialTraining = 0;

    public int NumOfEpochs = 1;
    public String topo = "1,1";
    public double minError = 1;
    public long delay = 1000;
    public double numOfSamples = 10;

    BackPropagation backPropagation = new BackPropagation(net, data);
    Annealing annealing = new Annealing(net, data);

    //-----------------------------------------//
    //----------- CLASS METHODS----------------//
    //-----------------------------------------//
    public static void main(String[] args) {

        Enviroment env = new Enviroment();

        //Func function = new Func("e^-(((x-0.5)^2)/(0.2^2))"); // gaussiana : min 3 neurons
        //Func function = new Func("(sin((3*x)*3.1416)+1)/2"); // no va!
        Func function = new Func("(sin((2*x)*3.1416)+1)/2");
        //Func function = new Func("(tanh((x*4)-2)+1)/2");

        env.backPropagation.ETA = 1;
        env.backPropagation.MOMENTUM = 0.1;
        
        env.annealing.cycles=50;
        env.annealing.start=10;
        env.annealing.stop=0;
        
        
        env.topo = "1,7,1";
        env.NumOfEpochs = 50000;
        env.delay = 500;
        env.minError = 0.0050;
        env.numOfSamples = 30;

        env.TrainFunction(function, false);

        Uti.printNetData(env.net, env.data);

        Uti.DataFunc1P(function, 0, 1, 0.01, env.data);

        env.backPropagation.evaluate(env.net, env.data);

        //export and save nets NetData on CSV
        Uti.saveNetAndDataCSV(env.net, env.data);
    }

    public long batSec() {
        long batcSec = (long) (((double) partialTraining / (double) delay) * 1000);
        partialTraining = 0;
        return batcSec;

    }

    /**
     * train enviroment for afunctions
     *
     * @param function he function to train
     * @param load
     */
    public void TrainFunction(Func function, boolean load) {

        initFrame();
        String fileLocation = "C:\\Users\\alessia/TestNet0.net";
 
        data = new Data();
        Uti.DataFunc1P(function, 0, 1, 1 / numOfSamples, data);

        net = new Net();

        if (load) {
            Uti.loadNet(fileLocation, net);
        } else {
            net.createNet(topo);
        }
        long time = System.currentTimeMillis();

        // runs counter
        Epoch = 0;

        //for all trainig loops until fitness solution found or max training loops 
        while ((Epoch < NumOfEpochs)) {

            //increase the counter of total trainings
            Epoch++;

            //increase the counter of partial trainings
            partialTraining++;

            backPropagation.train(net, data);
            annealing.train(net, data);
            //annealing.train(net, data);

            if ((System.currentTimeMillis() - time) > delay) {

                // paint the data
                trainPanel.repaint();

                // reset the current time
                time = System.currentTimeMillis();

            }

            boolean err = (data.getDataError() < minError);
            if (err) {
                trainPanel.repaint();
                break;
            }

        }

    }

    private void initFrame() {

        frame = new JFrame(topo);
        frame.setSize(640, 480);

        trainPanel = new TrainPanel(this);

        trainPanel.setVisible(true);

        frame.add(trainPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}
