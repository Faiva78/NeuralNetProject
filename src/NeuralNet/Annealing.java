/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;


/**
 *
 * @author faiva78
 */
public class Annealing extends Feedforward {

    double cycles = 50;
    double start = 10;
    double stop = 0;
    double temp = start;

    public Annealing(Net net, Data data) {
        super(net, data);
    }

    
    void train(Net net, Data data) {

        evaluate(net, data);
        double bestError = data.getDataError();
        double currentError = 1;
        double[] best = Uti.serialize(net);

        double[] test = Uti.copyArray(best);
        double ratio = Math.exp(Math.log(stop / start) / (cycles - 1));

        for (int i = 0; i < cycles; i++) {

            ramdomi(test);
            Uti.deserialize(net, test);
            evaluate(net, data);
            currentError = data.getDataError();

            if (currentError < bestError) {
                best = Uti.serialize(net);
                test = Uti.copyArray(best);
                bestError = currentError;
            }
            temp *= ratio;

        }
        Uti.deserialize(net, best);
        trainingBatches++;
    }

    private void ramdomi(double[] test) {
        for (int i = 0; i < test.length; i++) {
            double u = test[i];
            double add = 0.5 - (Math.random());
            add /= start;
            add *= temp;
            test[i] = test[i] + add;
        }
    }

}
