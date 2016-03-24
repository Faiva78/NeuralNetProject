package NeuralNet;

import java.awt.Canvas;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Net extends Canvas {
    //TODO implementing momentum

    /*  ------------------------------------
     -------------CLASS FILEDS-----------
     ------------------------------------*/
    /**
     * List of neuron layers
     */
    public ArrayList<Layer> LayerList = new ArrayList<>();

    /**
     * Data structure for inputData,output and errors
     */
    public Data NetData = new Data();

    /**
     * the total error of the NetData
     *
     */
    public double DataError = 0;

    /**
     * Learning rate
     */
    public double ETA = 1;

    /**
     * Momentum
     */
    public double Momentum = 0.1;

    /**
     * Activation function
     *
     */
    public Func AcivationFunction = new Sigmoid();

    /**
     * Training cost ( batches * error)
     *
     */
    public double TrainingCost;

    /**
     * number of training batches performed on this
     */
    public long TrainingBtaches = 0;

    /**
     * the topology of the net
     *
     */
    private final String Topology = "";

    //  ------------------------------------ 
    //  -------------CLASS METODS-----------
    //  ------------------------------------
    /**
     * find the previous layer
     *
     * @param inLayer layer to be computed
     * @return a layer where is connected
     * @deprecated not in use
     */
    private Layer findLLayer(Layer inLayer) {

        // temporary list of all precedent neurons
        ArrayList<Neurone> Neuronlist = new ArrayList<>();

        // layer to be returned
        Layer returnLayer = new Layer();

        // foe every neuron in inLayer
        for (Neurone layerNeuron : inLayer.layerNeurons) {

            //for every axonfrom the neuron of the layer
            for (Assone axonFrom : layerNeuron.axonFrom) {

                // get the neuron where theaxon is connected
                Neurone neuronFrom = axonFrom.fromNeuron;

                // if is a bias then add the neuron to the layer
                if (neuronFrom.isBias) {
                    returnLayer.neuronBias = neuronFrom;
                } else if (!Neuronlist.contains(neuronFrom)) {// if is not a bias and is not contained in the list so add it to the neurolist
                    Neuronlist.add(neuronFrom);
                }
            } // end of axofrom cycle    
        } // end of neurons cycle
        // now we have all the neurons of layer -1
        returnLayer.layerNeurons = Neuronlist;
        return returnLayer;
    }

 

    /**
     * Run a single batch of backpropagation learning
     *
     *
     * @param withErrorCompensation enable repetition of most erroneus sample
     * data
     */
    public void learnBatch(boolean withErrorCompensation) {

        double totErr = NetData.getDataError();  // FIXIT
        Random rand = new Random();

        ArrayList<Integer> ListeRepetitions = new ArrayList<>();

        for (Data.Sample sample : NetData.SampleList) {

            int sampleErrorCountNumber = 0;//FIXIT
            if (withErrorCompensation) {
                sampleErrorCountNumber = (int) (NetData.SampleList.size() * (sample.sampleError / totErr));
            }
            ListeRepetitions.add(sampleErrorCountNumber + 1);

        }
        for (Integer ListeRepetition : ListeRepetitions) { //FIXIT
            int e = ListeRepetition;
            for (int j = 0; j < e; j++) {

                //get a sample
                Data.Sample sample = NetData.SampleList.get(rand.nextInt(NetData.SampleList.size()));

                // evaluate the net output
                evaluateSample(sample);

                // backpropoagation algorythm
                Backprop(sample);

                // evaluate the net output
                evaluateSample(sample);

            }
        }

        // increase the  counter of training batches
        TrainingBtaches++;

        //fetch the total error of all samples summed
        DataError = NetData.getDataError();

        //calculate the training cost (1000=max efficecncy; 0.016 min efficency )
        TrainingCost = 1000 / ((TrainingBtaches) * DataError);

    }

    /**
     * Create the layers and neuron from the topology array
     *
     * @param topology array with the topology of the net
     */
    private void createNet(int topology[]) {
        LayerList.clear();
        //for as many elements in topology
        for (int i = 0; i < topology.length; i++) {

            // get the current topology v
            int layerTopology = topology[i];

            // create a new layer object (and also the bias neuron)
            Layer layer = new Layer();

            // for as many as current topology
            for (int j = 0; j < layerTopology; j++) {

                //create a new neuron
                Neurone neuron = new Neurone();

                //set the activation function 
                neuron.ActivationFunction = AcivationFunction;

                // add it to current layer 
                layer.layerNeurons.add(neuron);

            }

            //add newly create layer to layer list
            LayerList.add(layer);
        }
        // add the axons links to the net
        createAxons();

    }

    /**
     * Create the layers and neuron from the topology array
     *
     * @param TopologyString String with the topology of the net in the form of
     * "2,3,2"
     */
    public void createNet(String TopologyString) {
        int[] topology = Uti.str2ArrayInt(TopologyString);
        createNet(topology);
    }

    /**
     * Create the axons link in the net
     */
    private void createAxons() {
        // for as many net layers from the second element
        for (int i = 1; i < LayerList.size(); i++) {

            //get the current layer
            ArrayList<Neurone> layerThis = LayerList.get(i).layerNeurons;

            // create connection from the current layer to this layer bias neuron
            addAzonsFrom(layerThis, LayerList.get(i).neuronBias);
            //get the previous layer
            ArrayList<Neurone> layerPrec = LayerList.get(i - 1).layerNeurons;

            // for as many  neurons in previus layer
            for (Neurone layerPrec1 : layerPrec) {

                // create connection from the current layer to previous neuron
                addAzonsFrom(layerThis, layerPrec1);
            }

        }
    }

    /**
     * add axon links from thne neuron N to all neurons of Nlist
     */
    private void addAzonsFrom(ArrayList<Neurone> Nlist, Neurone N) {

        for (Neurone neuron : Nlist) {
            //create a new axon
            Assone asson = new Assone();

            // set axons in neuron reference link
            asson.fromNeuron = N;

            //add the axon to the neuron from list
            neuron.axonFrom.add(asson);
        }

    }

    /**
     * Evaluate all samples in the NetData
     *
     */
    public void evaluatSamples() {

        for (Data.Sample sample : NetData.SampleList) {
            evaluateSample(sample);
        }

    }

    /**
     * evaluate the net with the dataset
     *
     * @param sample
     */
    private void evaluateSample(Data.Sample sample) {

        // reset the sample deltaError
        sample.sampleError = 0;

        // for as many layers in the net
        for (int layerNum = 0; layerNum < LayerList.size(); layerNum++) {

            //get the  neuron layer list
            Layer layer = LayerList.get(layerNum);

            // for every neuron in the layer
            for (int neuronNum = 0; neuronNum < layer.layerNeurons.size(); neuronNum++) {
                Neurone neuron = layer.layerNeurons.get(neuronNum);

                // if is th first neuron layer so update the neuron outputs with input NetData
                if (layerNum == 0) {

                    // add the NetData from dataset inputData to input neuron values
                    layer.layerNeurons.get(neuronNum).A_activation = sample.inputData[neuronNum];

                } else { // else calculate the output

                    //evaluate the neuron output
                    neuron.A_activation = neuron.ActivationFunction.evaluate(neuron.getWeight());
                }
                // we are at the last layer neuron , update the last layer output sample Error
                if (layerNum == LayerList.size() - 1) {

                    // retrieve and add neuron output value to dataset
                    sample.outputData[neuronNum] = layer.layerNeurons.get(neuronNum).A_activation;

                    // Calculate the neuron square mean deltaError 
                    //neuron.E_error = 
                    // sum the total output deltaError
                    sample.sampleError = sample.sampleError + Func.meanError(sample.testData[neuronNum], sample.outputData[neuronNum]);

                }
                // finish parsing the layer neurons
            }
            // finished parsing the layer
        }
        // finished parsing the layers
    }

    /**
     * Backpropagation algorithm
     *
     *
     * @param sample the sample to train the net for
     */
    private void Backprop(Data.Sample sample) {

        // declare the derivations variables NOT WORKING WELL!!!
        // work first on the last output layer
        Layer outlayer = LayerList.get(LayerList.size() - 1);

        //set the last layer total error
        outlayer.LayerError = 0;

        //for every neuron in the last layer CALCULATE OUTPUT ERROR and new weighs
        for (int n = 0; n < outlayer.layerNeurons.size(); n++) {

            double dError_dOut, dOut_dNet;
            double dNet_dWeight, dError_dWeight;

            // get the neuron
            Neurone neuron = outlayer.layerNeurons.get(n);

            //get the neuron axons connected from
            ArrayList<Assone> assoneInList = neuron.axonFrom;

            // differentiate the totale deltaError with respect to neuron output
            dError_dOut = +(sample.testData[n] - neuron.A_activation);
            neuron.dError_dOut = dError_dOut;

            // differentiate the output with respect to net totoal weights
            dOut_dNet = neuron.ActivationFunction.derivation(neuron.getWeight());
            neuron.dOut_dNet = dOut_dNet;

            // for each axon in neuron input
            for (Assone assoneIn : assoneInList) {

                //differentiate the net total weights with respect to axon weight
                dNet_dWeight = assoneIn.fromNeuron.A_activation; //out of prevous neuron

                //  partial derivative of the total error with respect to weight
                dError_dWeight = dError_dOut * dOut_dNet * dNet_dWeight;

                //  set the axon deltaError delta( to be updated)
                assoneIn.deltaError = dError_dWeight;   //assoneIn.newWeight = assoneIn.weight-(learningRate * dError_dW);

                /// the new weights MUST BE updated after the full propagation
            }

            //update the last layer total error
            outlayer.LayerError = outlayer.LayerError + (dError_dOut * dOut_dNet);

        }
        // end of last layer

        // ...................   SECOND PASS ,;;;;;;;;;;;;;;;;;;;;;
        //start from the penultimaate layer thru the second
        //calculate the new weights
        // for every layer starting from second from last
        for (int layerNum = LayerList.size() - 2; layerNum > 0; layerNum--) {

            double dOutH_dNetH, dNet_dEighth, dError_dWeightH;

            // get the layer
            Layer layer = LayerList.get(layerNum);

            //for as many layers in layerslist
            for (Neurone layerNeuron : layer.layerNeurons) {
                //get the neuron list
                ArrayList<Neurone> layerNeurons = layer.layerNeurons;

                //for as many as neurons in neuron list
                for (Neurone neuron : layerNeurons) {

                    //get the neuron axons connected from
                    ArrayList<Assone> assoneInList = neuron.axonFrom;

                    // get the relative desidered output
                    //ArrayList<Neurone> lastlayerNeuronList = LayerList.get(LayerList.size() - 1).layerNeurons;
                    //double dErrotTot_OutH = 0;
                    //Layer lastlayer= LayerList.get(LayerList.size()-1);
                    //lastlayer.LayerError=0;
                    //for every neuron in the last layer
                    //for (Neurone lastneuron : lastlayerNeuronList) {
                        //  calculate delta of output neurons
                    //double dErrorH_dOutH = lastneuron.dError_dOut * lastneuron.dOut_dNet; // ALREADY CALCULATED
                        // sum of the partial devivaives of output 
                    //dErrotTot_OutH = dErrotTot_OutH + dErrorH_dOutH;
                    //}
                    // differentiate the output with respect to net totoal weights
                    dOutH_dNetH = neuron.ActivationFunction.derivation(neuron.getWeight());

                    // for each azon in neuron input
                    for (Assone assoneIn : assoneInList) {

                        //differentiate the net total weights with respect to axon weight
                        dNet_dEighth = assoneIn.fromNeuron.A_activation; //out of prevous neuron

                        //  partial derivative of the total error with respect to weight
                        //dError_dWeightH = dErrotTot_OutH * dOutH_dNetH * dNet_dEighth;
                        dError_dWeightH = outlayer.LayerError * dOutH_dNetH * dNet_dEighth;

                        //  set the axon deltaError delta( to be update)
                        assoneIn.deltaError = dError_dWeightH;   //assoneIn.newWeight = assoneIn.weight-(learningRate * dError_dW);

                    }
                }
            }
        }
        /// the new weights MUST BE updated after the full propagation
        updateAllWeights();
    }

    /**
     * Update all the weight with the eta learning rate
     */
    private void updateAllWeights() {

        //for every layer excluded the first (no weights availables)
        for (int i = 1; i < LayerList.size(); i++) {

            //get the current layer
            Layer layer = LayerList.get(i);

            //for every neuron in layer
            for (Neurone neuron : layer.layerNeurons) {

                //foe everu axon from in ineuron
                for (Assone axonFrom : neuron.axonFrom) {
                    
                    // update the weight with eta and momentum
                    axonFrom.weight = axonFrom.weight - ((ETA * axonFrom.deltaError) + (Momentum * axonFrom.deltaError_1)); // plus momentum * delteE-1
                    
                    //update the last deltaerror with current
                    axonFrom.deltaError_1 = axonFrom.deltaError;
                }

            }
        }

    }

    /**
     * load a net
     *
     * @param location where to load
     */
    public void loadNet(String location) {
        char separator = (char) 9; //tab

        //open a buffered reader
        try ( // open afile reader
                FileReader fileReader = new FileReader(location)) {
            //open a buffered reader
            BufferedReader reader = new BufferedReader(fileReader);

            // init the line string
            String line = "";

            //read topology line
            line = reader.readLine();

            //create the net
            createNet(line);

            for (Layer layer : LayerList) {

                //read layer line
                line = reader.readLine();
                //String[] LayerLine = line.split(String.valueOf(separator));

                for (Neurone neurone : layer.layerNeurons) {

                    //read neuron line
                    line = reader.readLine();
                    //String[] neuronLine = line.split(String.valueOf(separator));

                    for (Assone axonFrom : neurone.axonFrom) {
                        //read axon line

                        line = reader.readLine();
                        String[] axonLine = line.split(String.valueOf(separator));
                        axonFrom.weight = Double.valueOf(axonLine[3]);
                    }
                }
            }
        } catch (IOException e) {
            throw new Error("Error in loadNet");
        }

    }

    /**
     * save the net
     *
     * @param Location location onwhere to save
     */
    public void saveNet(String Location) {

        //set the carachters
        try (FileWriter fileWriter = new FileWriter(Location) // --------------- save the net-----------//
                ) {
            //set the carachters
            char newLine = (char) 13;  //return
            char separator = (char) 9; //tab

            //add the topology
            fileWriter.append(getTopology() + newLine);

            // for each layer
            for (Layer layer : LayerList) {

                //write the layer number and bias neuron number
                fileWriter.append("Layer:" + separator + String.valueOf(layer.idGet()) + separator);
                fileWriter.append("NeuronBias:" + separator + String.valueOf(layer.neuronBias.idGet()) + newLine);

                //for each neuron in layer
                for (Neurone layerNeuron : layer.layerNeurons) {

                    // write the neuron number; Neuron: x
                    fileWriter.append("Neuron:" + separator + String.valueOf(layerNeuron.idGet()) + newLine);

                    // for every axon from
                    for (Assone axonFrom : layerNeuron.axonFrom) {

                        // write the axon number and weight: Axon;  x   Weight: y
                        fileWriter.append("Axon:" + separator + String.valueOf(axonFrom.idGet()) + separator);
                        fileWriter.append("Weight:" + separator + String.valueOf(axonFrom.weight) + separator);
                        fileWriter.append("NeuronFrom:" + separator + String.valueOf(axonFrom.fromNeuron.idGet()) + newLine);
                    }

                }

            }
            // --------------- save the net-----------//
            fileWriter.flush();
        } //return
        catch (IOException ex) {
            throw new Error("Error in saveNet");
        }
    }

    /**
     * get the net topology
     *
     * @return string of topology
     */
    public String getTopology() {
        if ("".equals(Topology)) {

            StringBuilder str = new StringBuilder("");
            for (int i = 0; i < LayerList.size() - 1; i++) {
                Layer get = LayerList.get(i);
                str.append(String.valueOf(get.layerNeurons.size()));
                str.append(",");
            }
            str.append(String.valueOf(LayerList.get(LayerList.size() - 1).layerNeurons.size()));
            return str.toString();

        } else {
            return Topology;
        }
    }
}
