/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author alessia
 */
public class DataSamplesModifier {

    
    public double errorRatioCorrection = 0;
    public boolean dataShuffle = false;
    public boolean dataRandomize = false;
    public double randomInput = 0;

    private final Random rand = new Random();

    public DataSamplesModifier() {

    }

    private ArrayList<Sample> getSampleListErrored(Data data, double correction) {

        // get the data required
        int NumSamples = data.SampleList.size();
        double totalError = data.getDataError();
        //create the temp list
        ArrayList<Sample> list = new ArrayList<>();
        //fow all samples
        for (int i = 0; i < NumSamples; i++) {
            //calculate how many of this samble need to insert to list
            double totalErrorInv = 1 / totalError;
            double sampleError = data.SampleList.get(i).sampleRms.getSampletError();
            double UnaryError = totalErrorInv * sampleError;
            double MultError = UnaryError * correction;
            double numsSamCorr = MultError * NumSamples;
            // insert to list
            for (int j = 0; j < numsSamCorr; j++) {
                list.add(data.SampleList.get(i));
            }
        }
        // if too much samples remove randomInput ones
        while (list.size() > NumSamples) {
            list.remove(rand.nextInt(list.size()));
        }
        // if too low, add missing data from the copy of sampleList
        ArrayList<Sample> copy = new ArrayList<>();
        copy.addAll(data.SampleList);
        while (list.size() < NumSamples) {
            Sample sa = copy.get(0);
            list.add(sa);
            copy.remove(sa);
        }
        return list;
    }

    public ArrayList<Sample> modifyList(Data data) {
        ArrayList<Sample> list = new ArrayList<>();

        if (errorRatioCorrection == 0) {
            list.addAll(data.SampleList);
        } else {
            list.addAll(getSampleListErrored(data, errorRatioCorrection));
        }

        if (dataShuffle) {
            //random bucket
            Collections.shuffle(list, rand);
        }
        if (dataRandomize) {
            //random
            ArrayList<Sample> tmp=new ArrayList<>();
            tmp.addAll(list);
            
            list.addAll(Uti.getRandom(tmp));
        }
        return list;
    }

}
