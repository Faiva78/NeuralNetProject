/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.util.ArrayList;

public class MainSim {

    /**
     * main method
     *
     * @param args
     */
    public static void main(String[] args) {

        Enviroment env = new Enviroment();

        Func function = new Func("(sin((2*x)*3.1416)+1)/2");

        System.out.println("-------START OF TRAINING CYCLE ----------");

        //train the net with the function        
        Train.useErrorsMode = false;
        BackPropagation.ETA = 1;
        BackPropagation.MOMENTUM = 0.1;
        env.NumOfEpochs = 50000;
        env.delay = 250;
        env.minError = 0.0010;
        env.numOfSamples = 30;
        env.TrainFunction(function, false);

        System.out.println("-------END OF TRAINING CYCLE ----------");

        ArrayList<Net> SaveLList = new ArrayList<>();

        SaveLList.add(env.net);
        Uti.printNetData(SaveLList, env.data);

        // populate all net NetData with function
        for (Net net : SaveLList) {

            env.data.DataFunc1P(function, 0, 1, 0.01);
            Feedforward.evaluate(net, env.data);
            //net.evaluatSamples();
        }

        //export and save nets NetData on CSV
        Uti.saveNetAndDataCSV(SaveLList, env.data);
    }

}
