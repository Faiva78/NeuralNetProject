package NeuralNet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class Uti {

    public static char tab = (char) 9;

    
    public static double[] copyArray( double[] source){
        double[] ret = new double[source.length];
        
        for (int i = 0; i < source.length; i++) {
            ret[i] = source[i];
        }
        return ret;
    }
    
    
    
    public static int countWeights(Net net) {

        int count = 0;
        for (Layer LayerList : net.LayerList) {
            for (Neurone layerNeuron : LayerList.layerNeurons) {
                for (Assone axonFrom : layerNeuron.axonFrom) {
                    count++;
                }
            }
        }

        return count;

    }

    static public double[] serialize(Net net) {

        double[] out = new double[countWeights(net)];

        int count = 0;
        for (Layer LayerList : net.LayerList) {
            for (Neurone layerNeuron : LayerList.layerNeurons) {
                for (Assone axonFrom : layerNeuron.axonFrom) {
                    out[count] = axonFrom.weight;
                    count++;
                }
            }
        }

        return out;
    }

    static public void deserialize(Net net, double[] serial) {

        if (countWeights(net) == serial.length) {
            int count = 0;
            for (Layer LayerList : net.LayerList) {
                for (Neurone layerNeuron : LayerList.layerNeurons) {
                    for (Assone axonFrom : layerNeuron.axonFrom) {
                        axonFrom.weight = serial[count];
                        count++;
                    }
                }
            }
        }

    }

    static public void PrintAssone(Assone assone) {

        System.out.print(new StringBuilder().append("Axon").append(tab));
        System.out.print(new StringBuilder().append("weight").append(tab));
        System.out.print(new StringBuilder().append("from").append(tab));
        System.out.println();
        System.out.print(new StringBuilder().append(assone.idGet()).append(tab));
        System.out.print(new StringBuilder().append(String.format("%.2g", assone.weight)).append(tab));
        System.out.print(new StringBuilder().append(CkNullIdFrom(assone)).append(tab));
        System.out.println();
    }

    private static int CkNullIdFrom(Assone obj) {
        if (obj.fromNeuron == null) {
            return 0;
        }
        return obj.fromNeuron.idGet();
    }

    public static void fileAppendLine(String string, String filename) {

        try (FileWriter filewriter = new FileWriter(filename, true)) {
            filewriter.append(string);
            filewriter.flush();
        } catch (Exception e) {
            throw new Error("Errore in Uti.fileAppend");
        }

    }

    /**
     * string to int arraytokenizer style
     *
     * @deprecated NOT WORKING
     * @param string to be splitted with "," character
     * @return int array of string numbers
     */
    public static int[] stringToIntArrayToken(String string) {
        StringTokenizer tokenator;
        tokenator = new StringTokenizer(string, ",");
        int[] arr = new int[tokenator.countTokens() - 1];
        int c = 0;
        for (int i = 0; i < tokenator.countTokens(); i++) {
            arr[i] = Integer.getInteger(tokenator.nextToken());
        }
        return arr;
    }

    public static void PrintAssone(List<Assone> lista) {
        for (Assone lista1 : lista) {
            PrintAssone(lista1);
        }
    }

    public static void printResults(ArrayList<double[]> in) {
        StringBuilder txt = new StringBuilder();
        txt.append("I_0").append(tab);
        txt.append("I_1").append(tab);
        txt.append("O_0").append(tab);
        txt.append("Fx").append(tab);
        txt.append("E").append(tab);
        txt.append("M").append(tab);
        printL(txt);

        for (double[] get : in) {
            for (int j = 0; j < get.length; j++) {
                double h = get[j];
                print(roundStr(h) + tab);
            }
            printL("");
        }

    }

    public static void printNetElement(Object obj) {
        double id = 0;
        double value = 0;
        String type = "";
        if (obj.getClass() == Assone.class) {
            Assone as = new Assone();
            as = (Assone) obj;
            id = as.idGet();
            //value = as.value;
            type = " A(";
        } else if (obj.getClass() == Neurone.class) {
            Neurone ne = new Neurone();
            ne = (Neurone) obj;
            id = ne.idGet();
            value = ne.A_activation;
            type = "N(";
        }
        printL(new StringBuilder().append(type).append((id)).append("):").append(tab).append(roundStr(value)));

    }

    public static void printL(Object inp) {
        System.out.println(inp);
    }

    public static void print(Object inp) {
        System.out.print(inp);
    }

    public static double RoundDouble(double x, int digits) {
        double pow = Math.pow(10, digits);
        return (double) Math.round(x * (pow)) / (pow);
    }

    public static String roundStr(double x) {
        return String.format("%.2g", RoundDouble(x, 3));
    }

    public static void testsig() {
        Func sigmoid = new Sigmoid();
        double initial = -10;
        double until = 10;
        double step = 0.1;
        printL(new StringBuilder().append("x").append(tab).append("Fx").append(tab).append("Fx'").append(tab).append("Fx''").append(tab).append("int"));
        for (double i = initial; i < until; i = i + step) {

            double aaa = i;
            double bbb = sigmoid.evaluate(i);
            double ccc = sigmoid.derivation(i);
            double ddd = sigmoid.derivation(ccc);
            double hhh = sigmoid.integration(initial, i);
            double ggg = sigmoid.gradient();

            printL(new StringBuilder().append(roundStr(aaa)).append(tab).append(roundStr(bbb)).append(tab).append(roundStr(ccc)).append(tab).append(roundStr(ddd)).append(tab).append(roundStr(hhh)));

        }

    }

    /**
     * String to array of integers
     *
     * @param str input string
     * @return array of integeres
     *
     */
    public static int[] str2ArrayInt(String str) {
        String[] spl = str.split(",");
        int out[] = new int[spl.length];
        for (int i = 0; i < spl.length; i++) {
            String spl1 = spl[i];
            out[i] = Integer.parseInt(spl1);
        }
        return out;
    }

    public static double[] str2ArDbl(String str) {
        String[] spl = str.split(",");
        double out[] = new double[spl.length];
        for (int i = 0; i < spl.length; i++) {
            String spl1 = spl[i];
            out[i] = Double.parseDouble(spl1);
        }
        return out;
    }

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
     * load a net
     *
     * @param location where to load
     * @param net the net where to load
     */
    public static void loadNet(String location, Net net) {
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
            net.createNet(line);

            for (Layer layer : net.LayerList) {

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
     * @param net the net to be saved
     */
    public static void saveNet(String Location, Net net) {

        //set the carachters
        try (FileWriter fileWriter = new FileWriter(Location) // --------------- save the net-----------//
                ) {
            //set the carachters
            char newLine = (char) 13;  //return
            char separator = (char) 9; //tab

            //add the topology
            fileWriter.append(net.getTopology() + newLine);

            // for each layer
            for (Layer layer : net.LayerList) {

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
     * convert a function with 2 free parameters in a data format
     *
     * @param function
     * @param from
     * @param to
     * @param step
     * @param data
     */
    public static void DataFunc2D(Func function, double from, double to, double step, Data data) {
        data.SampleList.clear();
        double dat1 = from;
        double dat2 = from;
        while (dat1 < to) {
            if ((to - from) > 0) {
                dat1 = dat1 + step;
            } else {
                dat1 = dat1 - step;
            }
            while (dat2 < to) {
                if ((to - from) > 0) {
                    dat2 = dat2 + step;
                } else {
                    dat2 = dat2 - step;
                }

            }
            data.addSample(new double[]{dat1, dat2}, new double[]{function.evaluate(dat1, dat2)});
        }
    }

    static  String[] DoubleArrayToString(double[] d){
        String[] str = new String[d.length];
        for (int i = 0; i < d.length; i++) {
            str[i] = String.valueOf(  d[i]);
        }
    return str;
    }
    
    static public String ArrayToString(String[] str){
        StringBuilder ss = new StringBuilder();
        for (int i = 0; i < str.length-1; i++) {
            ss.append(str[i]).append(",");
        }
        ss.append(str[str.length-1]);
    return ss.toString();
    }
    
    
    /**
     * convert a function with 1 free parameter in a data format
     *
     * @param function
     * @param from
     * @param to
     * @param step
     * @param data
     */
    public static void DataFunc1P(Func function, double from, double to, double step, Data data) {
        data.SampleList.clear();
        double dat = from;
        while (dat <= to) {
            data.addSample(new double[]{dat}, new double[]{function.evaluate(dat)});
            if ((to - from) > 0) {
                dat = dat + step;
            } else {
                dat = dat - step;
            }

        }
    }

    /**
     * print sample output data
     *
     * @deprecated ONLY FOR DEBUGGING
     */
    private static void PrintData(Data.Sample sample) {

        for (int i = 0; i < sample.outputData.length; i++) {

            System.out.format("in:%.0f out:%.2f test:%.0f err:%.2f%n", sample.inputData[0], sample.outputData[0], sample.testData[0], sample.sampleRms.getSampletError());
        }
    }

    public static void saveDataCSV(Net net, Data data,String fileLocation) {


        //StringBuilder fileLocation = new StringBuilder();

        //create a file string
        //fileLocation.append(Dir).append(file).append(String.valueOf(num)).append(Extension);

        //append net informations
        StringBuilder info = new StringBuilder();
        char delimiter = (char) 9;
        info.append("Topology;");
        info.append(net.getTopology());
        info.append(delimiter);

        //info.append("Training cost: ");
        //info.append(String.valueOf(Train.TrainingCost));
        //info.append(delimiter);
        info.append("Overall error: ");
        info.append(String.valueOf(data.getDataError()));
        info.append(delimiter);
        info.append((char) 13);

        //export net sample NetData
        exportSampleDataToCSV(fileLocation, data);
        //add the NetData info
        Uti.fileAppendLine(info.toString(), fileLocation);

    }
    
    

    public static void printNetData(Net net, Data data) {
        int N = 0;
        double error = data.getDataError();
        StringBuilder str = new StringBuilder();

        str.append(String.format("N:%d ", N));
        str.append((char) 9);
        str.append(String.format("E:%.3f", error));
        str.append((char) 9);
        // str.append(String.format("C:%.4f", Train.TrainingCost));
        // str.append((char) 9);

        str.append(net.getTopology());

        str.append(String.format("%n"));
        System.out.print(str.toString());
        N++;

        System.out.println("");
    }

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

    /**
     * export all the sample data to a CSV file
     *
     * @param location where to save the file
     * @param data the data to save
     */
    public static void exportSampleDataToCSV(String location, Data data) {
        char delimiter = (char) 9;
        char newLine = (char) 13;
        try (FileWriter filewriter = new FileWriter(location)) {
            filewriter.append("Datasample");
            filewriter.append(delimiter);

            filewriter.append("Input");
            filewriter.append(delimiter);

            filewriter.append("Test");
            filewriter.append(delimiter);

            filewriter.append("Output");
            filewriter.append(delimiter);

            filewriter.append("Error");
            filewriter.append(newLine);

            int samNum = 0;
            for (Data.Sample sample : data.SampleList) {

                filewriter.append(String.valueOf(samNum));
                filewriter.append(delimiter);
                for (int i = 0; i < sample.inputData.length; i++) {
                    double data1 = sample.inputData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }
                for (int i = 0; i < sample.testData.length; i++) {
                    double data1 = sample.testData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }

                for (int i = 0; i < sample.outputData.length; i++) {
                    double data1 = sample.outputData[i];
                    filewriter.append(String.valueOf(data1));
                    filewriter.append(delimiter);
                }

                filewriter.append(String.valueOf(sample.sampleRms.getSampletError()));
                filewriter.append(newLine);

                samNum++;
            }
            filewriter.flush();
        } catch (IOException e) {
            System.out.println("Error in exporting CSV !!!");
        }

    }

    /**
     * print all the sample in data
     *
     * @param data the collection of data
     * @deprecated ONLY FOR DEBUGGING
     */
    public static void printSampleData(Data data) {

        int samNum = 0;
        for (Data.Sample sample : data.SampleList) {

            StringBuilder str = new StringBuilder();
            str.append(String.format("D%d ", samNum)).append((char) 9);
            for (int i = 0; i < sample.inputData.length; i++) {
                double data1 = sample.inputData[i];
                str.append(String.format("I%d=%.3f ", i, data1)).append((char) 9);
            }
            for (int i = 0; i < sample.testData.length; i++) {
                double data1 = sample.testData[i];
                str.append(String.format("T%d=%.3f ", i, data1)).append((char) 9);
            }

            for (int i = 0; i < sample.outputData.length; i++) {
                double data1 = sample.outputData[i];
                str.append(String.format("O%d=%.3f ", i, data1)).append((char) 9);
            }
            str.append(String.format("E=%.3f ", sample.sampleRms.getSampletError()));
            System.out.println(str);
            samNum++;
        }

    }

}
