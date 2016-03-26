package NeuralNet;

public class MathParser {

    public static double eval(final String str, double[] args) {

        class Parser {

            public long factorial(int n) {
                if (n > 20) {
                    throw new IllegalArgumentException(n + " is out of range");
                }
                return (1 > n) ? 1 : n * factorial(n - 1);
            }

            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
                //if (pos+1<str.length()) { c=str.charAt(pos); } else { c= -1; }
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) {
                    eatChar();
                }
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) {
                    throw new RuntimeException("Unexpected: " + (char) c);
                }
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`
            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') {
                            eatChar();
                        }
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();

                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') {
                        eatChar();
                    }
                } else { // numbers
                    int startIndex = this.pos;

                    //my modifications
                    double Xvalue = 0;
                    boolean special = false;

                    if (c == 'x') {
                        eatChar();
                        special = true;
                        Xvalue = (double) args[0]; // x parameter       
                    }
                    if (c == 'y') {
                        eatChar();
                        Xvalue = (double) args[1]; // y parameter
                        special = true;
                    }
                    if (c == 'e') {
                        eatChar();
                        Xvalue = Math.E;

                        special = true;
                    }//end modifications

                    while ((c >= '0' && c <= '9') || c == '.') {
                        eatChar();
                    }
                    if (special) { // my mod
                        v = Xvalue;
                    } else if (pos == startIndex) {
                        throw new RuntimeException("Unexpected: " + (char) c);
                    } else {
                        v = Double.parseDouble(str.substring(startIndex, pos));
                    }

                }

                eatSpace();

                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }

                if (c == '!') { // factorization
                    eatChar();
                    v = (double) factorial((int) parseFactor());
                }

                if (negate) {
                    v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                }
                return v;
            }
        }
        return new Parser().parse();
    }

}
