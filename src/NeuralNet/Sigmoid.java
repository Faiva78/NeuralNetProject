package NeuralNet;

public class Sigmoid extends Func {

    @Override
    public double evaluate(double x) {
        return (1/( Math.pow(Math.E, (1-x)) +1));

    }

    

    public Sigmoid() {
        // the textual sigmoid function
        formlua=" 1 / ( (e ^ ( 1- x  )  ) + 1 )";
    }

}