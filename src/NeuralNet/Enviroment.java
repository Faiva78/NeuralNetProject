package NeuralNet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
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
    public ArrayList<Net> NetList = new ArrayList<>();

    /**
     * List of alll nets that pass the selection
     *
     */
    public ArrayList<Net> FitnessList = new ArrayList<>();

    public JFrame frame;

    double xx_old = 0;
    double yy_old = 0;

    //numner of trainig loops
    int NumOfTrainingLoops = 250000;

    // minimum fitness error
    double minError = 0.010;

    //-----------------------------------------//
    //------------- CLASS METHODS -------------//
    //-----------------------------------------//
    /**
     * creaet a single random net
     *
     * @param in how many inputs >1
     * @param out hw many outputs >1
     * @param maxLayer maximum layer number
     * @param minNeurons mininmum of neuron per layer >1
     * @param maxNeurons maximum neuron number per layer
     * @return the newly rcreated random net
     *
     */
    public Net RandomNet(int in, int out, int maxLayer, int minNeurons, int maxNeurons) {
        // set the separato char
        char separator = (char) ",".charAt(0);

        // create a random generator
        Random rnd = new Random();

        //create a new string builder
        StringBuilder topo = new StringBuilder("");

        // add the input neurons to topology
        topo.append(String.valueOf(in));
        topo.append(separator);

        // generate the random number of layers
        int layers = rnd.nextInt(maxLayer);

        // for every layer
        for (int i = 0; i <= layers; i++) {

            // generate a random seuron number
            int neurons = rnd.nextInt(maxNeurons - minNeurons) + minNeurons;

            // append to topology string
            topo.append(neurons);
            topo.append(separator);
        }
        // add the output neurons to topology
        topo.append(out);

        // create a new net object
        Net net = new Net();

        // create the net topology
        net.createNet(topo.toString());

        //return the nrandom net
        return net;
    }

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
        
        Graphics2D g2d = (Graphics2D) g;
        
        int h=this.getHeight();
        int w=this.getWidth();
        
        g2d.setColor(Color.red);
        g2d.draw(new Line2D.Double(0, ((1-minError)*h), w, (1-minError)*h));
        
        g2d.setColor(Color.BLACK);
        double bat=0;
        double xx=0;
        double yy=0;
        if (!NetList.isEmpty()) {
            bat = NetList.get(0).TrainingBtaches;
            yy = (((1- NetList.get(0).DataError)) * h);
            xx =  ((bat / NumOfTrainingLoops) * w);
        }

        g2d.draw(new Line2D.Double(xx_old, yy_old, xx, yy));

        xx_old = xx;
        yy_old = yy;

    }

    /**
     * train enviroment for afunctions
     *
     * @param function he function to train
     */
    public void TrainFunction(Func function) {  //TODO  develop enviroment 
    
        initFrame()
                ;
        String fileLocation = "C:\\Users\\alessia/TestNet0.net";

        boolean load = true;

        boolean errorCompensatrion = true;

        // create the NetData for the net (double because cant divide with int)
        double numOfSamples = 100;

        double eta =1.5;

        //delay of visualization
        long delay = 1000;

        //when change the training NetData
        long traininChange = 5000;

        // threshold of efficency
        long maxEfficency = (16 / 1000);

        int inputs = 1;

        int outputs = 1;

        int layers = 1;

        int maxNeuron = 8;

        int minNeuron = 7;

        // how many nets to create
        int nets = 1;

        //-------------------NO PARMATERS AFTER THIS----------//
        // test load net
        if (load) {
            //setup the loaded net
            Net net = new Net();
            net.loadNet(fileLocation);
            net.NetData.SampleList.clear();
            net.ETA = eta;
            net.NetData.DataFunc1P(function, 0, 1, 1 / numOfSamples);
            NetList.add(net);

        } else {
            for (int i = 0; i < nets; i++) {

                // create randoms net, populate and add to netlist
                Net net = new Net();
                net = (RandomNet(inputs, outputs, layers, minNeuron, maxNeuron));
                net.NetData.DataFunc1P(function, 0, 1, 1 / numOfSamples);
                NetList.add(net);
            }
        }

        // list of excluded nets
        ArrayList<Net> excludedList = new ArrayList<>();

        //current time and delay of output NetData refresh
        long time = System.currentTimeMillis();

        //total training
        long partialTraining = 0;

        // runs counter
        long runs = 0;

        //for all trainig loops until fitness solution found or max training loops 
        while ((!(FitnessList.size() > (nets - 1))) && (runs < NumOfTrainingLoops)) {

            //increase the counter of total trainings
            runs++;

            //increase the counter of partial trainings
            partialTraining++;

            //for all nets in netlist
            for (Net net : NetList) {

                //learn the batch //TODO momentum and adaptable ETA/GAMMA
                net.learnBatch(errorCompensatrion);

                // if  the error is lower than the fitness error
                if (net.DataError < minError) {

                    // move the neto t another list and stop
                    FitnessList.add(net);

                    // create randoms net, populate and add to netlist
                    Net net2 = new Net();
                    net2 = (RandomNet(inputs, outputs, layers, minNeuron, maxNeuron));
                    net2.NetData.DataFunc1P(function, 0, 1, 1 / numOfSamples);
                    NetList.add(net2);

                    // stop evaluating
                    break;
                }

                // if traiing efficency is over a threshold (tto slow or  cant reach minerror
                if ((net.TrainingCost < maxEfficency) & (net.DataError < 1)) {

                    //add this net to excluded list
                    excludedList.add(net);

                    // create randoms net, populate and add to netlist
                    Net net2 = new Net();
                    net2 = (RandomNet(inputs, outputs, layers, minNeuron, maxNeuron));
                    net2.NetData.DataFunc1P(function, 0, 1, 1 / numOfSamples);
                    NetList.add(net2);

                    //stop evaluating
                    break;
                }

            } // end of all nets training loop

            //remove all the fit net from netlist
            NetList.removeAll(FitnessList);

            //remove all the excluded net from netlist
            NetList.removeAll(excludedList);

            //reset the excluded list
            excludedList.clear();

            //change the function samples  after some runs
            if ((runs % traininChange) == 0) {
                for (Net net : NetList) {
                    net.NetData.DataFunc1P(function, 0, 1, 1 / numOfSamples);
                }
            }

            // Output the NetData over delay millisecond time
            if ((System.currentTimeMillis() - time) > delay) {

                this.repaint();

                // get the current time
                time = System.currentTimeMillis();

                // calculate the batch per second
                long batcSec = (long) (((double) partialTraining / (double) delay) * 1000);

                //reset partial training number
                partialTraining = 0;

                //create a stringbuilder
                StringBuilder str = new StringBuilder();

                //add how many runs
                str.append(String.format("Run: %d", runs));
                str.append((char) 9);

                //add how many sampkes
                str.append(String.format("samples: %.0f", numOfSamples));
                str.append((char) 9);

                //add samples/second
                str.append(String.format("Batch/sec: %d", batcSec));
                str.append((char) 9);

                str.append(String.format("Fit list: %d", FitnessList.size()));
                str.append((char) 9);

                //print the string
                System.out.println(str.toString());

                //rint the individual net NetData error and topology
                printNetData(NetList);
            } // ---------------end of output NetData

        }  // ----------------end of trainig cycle 

    }

    public void saveNetAndDataCSV(ArrayList<Net> netList) {

        // export NetData
        String Dir = System.getProperty("user.home");
        String file = "/TestNet";
        String Extension = ".csv";
        int num = 0;

        //for every net
        for (Net net : netList) {

            StringBuilder fileLocation = new StringBuilder();

            //create a file string
            fileLocation.append(Dir).append(file).append(String.valueOf(num)).append(Extension);

            //append net informations
            StringBuilder info = new StringBuilder();
            char delimiter = (char) 9;
            info.append("Topology;");
            info.append(net.getTopology());
            info.append(delimiter);

            info.append("Training cost: ");
            info.append(String.valueOf(net.TrainingCost));
            info.append(delimiter);

            info.append("Overall error: ");
            info.append(String.valueOf(net.NetData.getDataError()));
            info.append(delimiter);
            info.append((char) 13);

            //export net sample NetData
            net.NetData.exportSampleDataToCSV(fileLocation.toString());
            //add the NetData info
            Uti.fileAppendLine(info.toString(), fileLocation.toString());

            // save the net
            fileLocation = new StringBuilder();
            net.saveNet(fileLocation.append(Dir).append(file).append(String.valueOf(num)).append(".net").toString());
            num++;
        }

    }

    public void printNetData(ArrayList<Net> list) {
        int N = 0;
        for (Net net : list) {
            double error = net.NetData.getDataError();
            StringBuilder str = new StringBuilder();

            str.append(String.format("N:%d ", N));
            str.append((char) 9);
            str.append(String.format("E:%.3f", error));
            str.append((char) 9);
            str.append(String.format("C:%.4f", net.TrainingCost));
            str.append((char) 9);

            str.append(net.getTopology());

            str.append(String.format("%n"));
            System.out.print(str.toString());
            N++;
        }
        System.out.println("");
    }

}
