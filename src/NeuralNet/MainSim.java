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
        
        Enviroment env =new Enviroment();

        
        
        
        Func function = new Func("(sin((2*x)*3.1416)+1)/2");
        
        System.out.println("-------START OF TRAINING CYCLE ----------");
        //train the net with the function 
        
        
        env.TrainFunction(function);
        
        System.out.println("-------END OF TRAINING CYCLE ----------");

        ArrayList<Net> SaveLList = new ArrayList<>();

        if (!env.FitnessList.isEmpty()) {
            System.out.println("-------FITNESS SAVING----------");
            SaveLList = env.FitnessList;

        } else {
            SaveLList = env.NetList;
            System.out.println("-------NETLIST SAVING ----------");
        }

        env.printNetData(SaveLList);

        // populate all net NetData with function
        for (Net net : SaveLList) {
            net.NetData.DataFunc1P(function, 0, 1, 0.01);
            net.evaluatSamples();
        }

        //export and save nets NetData on CSV
        env.saveNetAndDataCSV(SaveLList);
    }
    
}
