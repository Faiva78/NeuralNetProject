package NeuralNet;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Uti {

    public static char tab = (char) 9;

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

}
