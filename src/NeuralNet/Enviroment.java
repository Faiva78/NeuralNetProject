package NeuralNet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    public JTrainPanel trainPanel;
    public JDataPanel dataPanel;

    public JPanel controls;
    public JSlider sliderETA;
    public JSlider slider2;

    public long Epoch = 0;
    public long partialTraining = 0;
    public ErrorTrack errorTrack = new ErrorTrack();

    public int NumOfEpochs = 1;
    public String topo = "1,1";
    public double minError = 1;
    public long delay = 1000;
    public double numOfSamples = 20;

    public BackPropagation backPropagation = new BackPropagation(net, data);
    public Annealing annealing = new Annealing(net, data);

    //-----------------------------------------//
    //----------- CLASS METHODS----------------//
    //-----------------------------------------//
    public static void main(String[] args) {

        Enviroment env = new Enviroment();

        //Func function = new Func("e^-(((x-0.5)^2)/(0.2^2))"); // gaussiana : min 3 neurons
        //Func function = new Func("(sin((3*x)*3.1416)+1)/2"); // no va!
        Func function = new Func("(sin((2*x)*3.1416)+1)/2");
        //Func function = new Func("(tanh((x*4)-2)+1)/2");

        env.TrainFunction(function, false);

        Uti.printNetData(env.net, env.data);

        Uti.DataFunc1P(function, 0, 1, 0.01, env.data);

        env.backPropagation.evaluate(env.net, env.data);

        Uti.saveNetAndDataCSV(env.net, env.data);
    }

    public Enviroment() {
        initFrame();
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

        //set up enviroment
        topo = "1,7,1";
        NumOfEpochs = 100000;
        delay = 250;
        minError = 0.020;
        numOfSamples = 30;
        errorTrack.lenght = 2500;

        // set up the learning algo
        backPropagation.ETA = 1;
        backPropagation.MOMENTUM = 1;
        annealing.start = 100;
        annealing.stop = 1;
        annealing.cycles = 25;

        // set up the data
        data = new Data();
        Uti.DataFunc1P(function, 0, 1, 1 / numOfSamples, data);

        //load or create a new net
        net = new Net();
        if (load) {
            String fileLocation = "C:\\Users\\alessia/TestNet0.net";
            Uti.loadNet(fileLocation, net);
        } else {
            net.createNet(topo);
        }

        //set up a timer
        Timer timer = new Timer(delay);

        double lastMeanDer = 0;

        boolean bProp = true;

        //for all trainig loops until fitness solution found or max training loops 
        while ((Epoch < NumOfEpochs)) {

            // increase counters
            Epoch++;
            partialTraining++;

            // train the net
            if (bProp) {
                backPropagation.train(net, data);
            } else {
                annealing.train(net, data);
            }
            errorTrack.add(data.getDataError());

            // after some cycle store the current error XXX
            if ((Epoch % 1500) == 0) {

            }

            // paint the panel
            if (timer.check()) {
                trainPanel.repaint();
                dataPanel.repaint();
                timer.reset();

            }

            // exit if satisfied minimum error
            boolean err = (errorTrack.mean() < minError);
            if (err) {
                trainPanel.repaint();
                break;
            }

        }

    }

    private void updateETA(ChangeEvent e) {

        backPropagation.ETA = (double) (sliderETA.getValue() / 100);

    }

    // set up the frame
    private void initFrame() {
        // set up frame
        frame = new JFrame("NNet Sim 0.1");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 0));

        // set up trainPanel
        trainPanel = new JTrainPanel(this);
        trainPanel.setVisible(true);
        trainPanel.setSize(320, 200);
        frame.add(trainPanel);

        //set up dataPanel
        dataPanel = new JDataPanel(this);
        dataPanel.setVisible(true);
        dataPanel.setSize(320, 200);
        frame.add(dataPanel);

        // set up control panel
        controls = new JPanel(new GridLayout(2, 0));
        controls.setVisible(true);

        // set up  sliders       
        sliderETA = new JSlider(0);
        sliderETA.setMaximum(10);
        sliderETA.setMinimum(-10);
        sliderETA.setPaintTicks(true);
        sliderETA.setMajorTickSpacing(1);
        sliderETA.setVisible(true);
        sliderETA.setValue(0) ;

        ChangeListener ETAlistener;
        ETAlistener = (ChangeEvent e) -> {
            updateETA(e); //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        };
        sliderETA.addChangeListener(ETAlistener);

        slider2 = new JSlider(0);
        slider2.setVisible(true);

        controls.add(sliderETA);
        controls.add(slider2);
        frame.add(controls);

        // show frame
        frame.setVisible(true);
    }

}
