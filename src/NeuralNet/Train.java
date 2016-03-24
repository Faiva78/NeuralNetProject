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
public  class Train {

    /**
     * number of training batches performed on this
     */
    public static  long trainingBatches = 0;
    
    public static  boolean  useErrorsMode=false;
    
    
     public static void TrainNet(Net net, Data data) {

         
         
        double totErr = data.getDataError();  // FIXIT
        Random rand = new Random();

        ArrayList<Integer> ListeRepetitions = new ArrayList<>();

        for (Data.Sample sample : data.SampleList) {

            int sampleErrorCountNumber = 0;//FIXIT
            if (useErrorsMode) {
                sampleErrorCountNumber = (int) (data.SampleList.size() * (sample.sampleError / totErr));
            }
            ListeRepetitions.add(sampleErrorCountNumber + 1);

        }
        for (Integer ListeRepetition : ListeRepetitions) { //FIXIT
            int e = ListeRepetition;
            for (int j = 0; j < e; j++) {

                //get a sample
                Data.Sample sample = data.SampleList.get(rand.nextInt(data.SampleList.size()));

                // evaluate the net output          
                Feedforward.evaluate(net, sample);

                // backpropoagation algorythm
                BackPropagation.evaluate(net, sample);


            }
        }

        // increase the  counter of training batches
        trainingBatches++;

    }
    
    
}
