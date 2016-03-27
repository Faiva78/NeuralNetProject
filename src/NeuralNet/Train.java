/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author faiva78
 */
public class Train {

    /**
     * number of training batches performed on this
     */
    public static long trainingBatches = 0;

    //public static boolean useErrorsMode = false;

    private static final Random rand = new Random();

    /**
     * train the net using backpropagation and samples randomly selected from
     * all the dataset
     *
     * @param net
     * @param data
     */
    public static void trainBackPropagation(Net net, Data data) {
        BackPropagation backPropagation =new BackPropagation(net, data);
        // for as many as samples
        for (int i = 0; i < data.SampleList.size(); i++) {

            // get a random sample
            Data.Sample sample = data.SampleList.get(rand.nextInt(data.SampleList.size()));
            
            //evaluate
            backPropagation.evaluate(net, sample);

            
            //learn
            backPropagation.train(net, sample);
        }
        trainingBatches++;
    }
    
    /**
     *@deprecated 
     * @param net
     * @param data
     */
    public  static  void  TrainAnnealing(Net net, Data data){
        
        for (int i = 0; i < data.SampleList.size(); i++) {
            //Data.Sample get = data.SampleList.get(i);
            
            Data.Sample sample = data.SampleList.get(rand.nextInt(data.SampleList.size()));
            
           // Feedforward.evaluate(net, sample);
            
            //Annealing.train(net, data);
            
            
        }
    
         trainingBatches++;
    }

    /**
     *@deprecated 
     * @param net
     * @param data
     */
    public static void TrainNet(Net net, Data data) {

        double totalError = data.getDataError();  // FIX IT
        
        //ArrayList<Integer> ListeRepetitions = new ArrayList<>();
        
        ArrayList<Data.Sample> newSampleList= new ArrayList<>();
        ArrayList<Data.Sample> oldSampleList= new ArrayList<>();
        
        oldSampleList.addAll(data.SampleList);
        
        for (int i = 0; i < data.SampleList.size(); i++) {
        
            double f= data.SampleList.size()/totalError;
            double cnum=f* data.SampleList.get(i).sampleRms.getSampletError();
            //int cint= (int) Math.ceil(cnum);
            //int cint= (int) Math.floor(cnum);
            int cint= (int) Math.round(cnum)+1;
            //ListeRepetitions.add(cint);
            for (int j = 0; j < cint; j++) {
                if (newSampleList.size()<(oldSampleList.size()*2)) {
                    newSampleList.add(data.SampleList.get(i));
                }
            }
        }
        
        
        data.SampleList.addAll(newSampleList);
        
        trainBackPropagation(net, data);
        
        data.SampleList.clear();
        data.SampleList.addAll(oldSampleList);
        
        // increase the  counter of training batches
        trainingBatches++;

    }

}
