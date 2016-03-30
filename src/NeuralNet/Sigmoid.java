package NeuralNet;

public class Sigmoid extends Func {

    @Override
    public double evaluate(double x) {
        
        // XXX  perche questa si e quella no????
        return (1/( 1+ (Math.pow(Math.E, (1-x))) ));
        //return (1 / (1 + (Math.pow(Math.E,  -(1*((x-0)))))));

    }



    public Sigmoid() {
        // the textual sigmoid function
        formlua = " 1 / ( (e ^ ( - x  )  ) + 1 )";
    }

}
