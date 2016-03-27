package NeuralNet;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
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
    public JTrainHistoryPanel trainPanel;
    public JDataPanel dataPanel;
    public JTexInfotPanel textPanel;

    public JPanel controls;
    public JSlider sliderMU;
    public JSlider sliderETA;
    public JTextField textMU;
    public JTextField textETA;
    public JButton startButton;

   // public JDataManagerPanel jdataManagerPanel;
            
    public long Epoch = 0;
    public long partialTraining = 0;
   // public ErrorTrack errorTrack = new ErrorTrack();

    public int NumOfEpochs = 1;
    public String topo = "1,1";
    public double minError = 1;
    public long delay = 1000;
    public double numOfSamples = 20;

    public boolean Simu = true;

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
        topo = "1,8,1";
        NumOfEpochs = 25000;
        delay = 250;
        minError = 0.020;
        numOfSamples = 30;
        data.errorTrack.lenght = 2500;

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


        while (Simu) {

            //for all trainig loops until fitness solution found or max training loops 
            while ((Epoch < NumOfEpochs)) {

                // increase counters
                Epoch++;
                partialTraining++;

                // train the net
                backPropagation.train(net, data);

                //add the error to track
               data.errorTrack.add(data.getDataError());

                // paint the panel
                if (timer.check()) {
                    trainPanel.repaint();
                    dataPanel.repaint();
                    textPanel.repaint();
                    timer.reset();
                }

                // exit if satisfied minimum error
                boolean err = (data.errorTrack.mean() < minError);
                if (err) {
                    Simu = false;
                    break;
                }
            }
            Epoch=0;
            //trainPanel.clear();
        }
    }

    private void updateMU(ChangeEvent e) {

        double val = (double) sliderMU.getValue();
        double res = (val * val) / 500;
        backPropagation.MOMENTUM = res;
        textMU.setText(String.valueOf(res));
    }

    private void updateETA(ChangeEvent e) {

        double val = (double) sliderETA.getValue();
        double res = (val * val) / 500;
        backPropagation.ETA = res;
        textETA.setText(String.valueOf(res));
    }

    private void toggleSim() {
        //TrainFunction(null, true);
        Simu = !Simu;

    }

    // set up the frame
    private void initFrame() {

        // set up frame
        frame = new JFrame("NNet Sim 0.1");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 2));

        // text panel
        textPanel = new JTexInfotPanel(this);
        textPanel.setVisible(true);
        textPanel.setSize(frame.getWidth(), 120);

        // set up trainPanel
        trainPanel = new JTrainHistoryPanel();
        trainPanel.setVisible(true);

        //set up dataPanel
        //dataPanel = new JDataPanel(this);
        dataPanel.setVisible(true);

        initPanelSliders();

        //jdataManagerPanel = new JDataManagerPanel(this);
        //jdataManagerPanel.setVisible(true);
        
        //add frame panels and controls
      
        frame.add(trainPanel);
        frame.add(dataPanel);
        frame.add(textPanel);
        frame.add(controls);
        //frame.add(jdataManagerPanel);
        //frame.add(jdataManagerPanel);

        // show frame
        frame.setVisible(true);
    }

    private void initPanelSliders() {
        // set up control panel

        int max = 100;
        int min = 0;
        int Mspacing = 10;
        int mSpacing = 1;
        boolean ticks = true;
        boolean snap = false;

        controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.setVisible(true);

        // set up  slider 1
        sliderMU = new JSlider(0);
        sliderMU.setMaximum(max);
        sliderMU.setMinimum(min);
        sliderMU.setPaintTicks(ticks);
        sliderMU.setMajorTickSpacing(Mspacing);
        sliderMU.setMinorTickSpacing(1);
        sliderMU.setSnapToTicks(snap);
        sliderMU.setVisible(true);
        sliderMU.setValue(1);
        ChangeListener MUlistener;
        MUlistener = (ChangeEvent e) -> {
            updateMU(e); //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        };
        sliderMU.addChangeListener(MUlistener);

        // add text MU
        textMU = new JTextField("MU");
        textMU.setVisible(true);

        // slider ETA
        sliderETA = new JSlider(0);
        sliderETA.setMaximum(max);
        sliderETA.setMinimum(min);
        sliderETA.setPaintTicks(ticks);
        sliderETA.setMajorTickSpacing(Mspacing);
        sliderETA.setMinorTickSpacing(mSpacing);
        sliderETA.setSnapToTicks(snap);
        sliderETA.setVisible(true);
        sliderETA.setValue(1);
        ChangeListener ETAlistener;
        ETAlistener = (ChangeEvent e) -> {
            updateETA(e);
        };
        sliderETA.addChangeListener(ETAlistener);

        //text eta
        textETA = new JTextField("ETA");
        textETA.setVisible(true);

        //start button
        startButton = new JButton("start");
        startButton.setVisible(true);

        ActionListener ToggleSimButLis = (ActionEvent e) -> {
            toggleSim();
        };
        startButton.addActionListener(ToggleSimButLis);

        // add sliders and text to control panel
        controls.add(sliderMU, BorderLayout.PAGE_END);
        controls.add(textMU);
        controls.add(sliderETA);
        controls.add(textETA);
        controls.add(startButton);

    }

}
