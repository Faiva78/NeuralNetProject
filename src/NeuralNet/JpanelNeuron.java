/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author alessia
 */
public class JpanelNeuron extends JPanel {

    public Net net;
    public int layerSelected;
    public int neuronSelected;
    private Image img;
    public boolean first = true;

    /**
     *
     */
    public ArrayList<ArrayList<Punto>> layerspoints;

    private void mouseListener() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                Point point = e.getPoint();
                int d = 15;
                for (int L = 0; L < layerspoints.size(); L++) {
                    ArrayList<Punto> layerspoint = layerspoints.get(L);
                    for (int N = 0; N < layerspoint.size(); N++) {
                        Punto punto = layerspoint.get(N);
                        if (((punto.getX(getWidth()) > point.x - d) && (punto.getX(getWidth()) < point.x + d)) && ((punto.getY(getHeight()) > point.y - d) && (punto.getY(getHeight()) < point.y + d))) {
                            layerSelected = L;
                            neuronSelected = N;
                        }
                    }
                }
            }
        });
    }

    public JpanelNeuron() {
        this.layerspoints = new ArrayList<>();
        mouseListener();
    }

    public JpanelNeuron(Net net) {
        this();
        this.net = net;
    }

    private class Punto {

        public double x;
        public double y;
        public boolean isNeuron = true;

        public Punto(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int getX(int width) {
            double ret = x * width;
            return (int) ret;
        }

        public int getY(int height) {
            double ret = y * height;
            return (int) ret;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            if (net != null) {
                Graphics2D g2d = (Graphics2D) g;
                int border = 2;
                if (first) {
                    System.out.println("paint neuron");
                    img = createImage(this.getWidth() - border, this.getHeight() - border);
                    Graphics2D g2dBuffer = (Graphics2D) img.getGraphics();
                    //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
                    calculatePoints();
                    paintLayer(g2dBuffer);
                    drawAxons(g2dBuffer);
                    drawNeurons(g2dBuffer);
                    ///first = false;
                    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                }
                g2d.drawImage(img, border / 2, border / 2, null);
            }
        } catch (Exception e) {
            System.out.println("Eror in panel Neuron:" + e.getMessage());
        }

    }

    private void paintLayer(Graphics g) {
        // draw neurons
        int size = 10;
        double layersN = layerspoints.size();
        for (int layerN = 0; layerN < layersN; layerN++) {
            ArrayList<Punto> punti = layerspoints.get(layerN);
            int npunti = punti.size();
            g.setColor(Color.green);
            if (npunti > 0) {
                int x1 = punti.get(0).getX(getWidth()) - size;
                int y1 = punti.get(0).getY(getHeight()) - size;
                int y2 = punti.get(punti.size() - 1).getY(getHeight());
                int ww = size * 2;
                int hh = (y2 - y1) + size;
                g.fillRoundRect(x1, y1, ww, hh, size, size);
            }
        }
    }

    private void calculatePoints() {
        layerspoints = new ArrayList<>();
        double layersN = net.LayerList.size();
        for (int X = 0; X < layersN; X++) {
            double neuronsN = net.LayerList.get((int) X).layerNeurons.size();
            ArrayList<Punto> neuronpoints = new ArrayList<>();

            if (neuronsN == 0) {
                double dy = 0.5;
                double dx = (X + 1) * (1 / (layersN + 1));
                Punto punto = new Punto((dx), dy);
                punto.isNeuron = false;
                neuronpoints.add(punto);
            }
            for (double Y = 0; Y < neuronsN; Y++) {
                double dy = (Y + 1) / (neuronsN + 1);
                double dx = (X + 1) * (1 / (layersN + 1));
                Punto punto = new Punto((dx), dy);
                neuronpoints.add(punto);
            }
            layerspoints.add(neuronpoints);
        }
    }

    private void drawNeurons(Graphics g) {
        // draw neurons
        int diam = 10;
        double layersN = layerspoints.size();
        for (int layerN = 0; layerN < layersN; layerN++) {
            ArrayList<Punto> punti = layerspoints.get(layerN);
            for (Punto punto : punti) {
                if (punto.isNeuron) {
                    int halfDiam = diam / 2;
                    int x1 = punto.getX(getWidth()) - halfDiam;
                    int y1 = punto.getY(getHeight()) - halfDiam;
                    g.setColor(Color.BLACK);
                    g.fillOval(x1, y1, diam, diam);
                }

            }
        }
    }

    private void drawAxons(Graphics g) {
        //draw axons
        for (int l = 1; l < layerspoints.size(); l++) {
            for (int n = 0; n < layerspoints.get(l).size(); n++) {
                Punto punto = layerspoints.get(l).get(n);
                for (int pl = 0; pl < layerspoints.get(l - 1).size(); pl++) {
                    ArrayList<Punto> layerPrevpoints = layerspoints.get(l - 1);
                    for (int pn = 0; pn < layerPrevpoints.size(); pn++) {
                        // XXX erronero
                        //double weight = net.LayerList.get(l).layerNeurons.get(n).axonFrom.get((pl)).weight;
                        //System.out.println(weight);
                        Punto puntoPrec = layerPrevpoints.get(pn);
                        if ((punto.isNeuron) && (puntoPrec.isNeuron)) {
                            g.setColor(Color.BLUE);
                            g.drawLine(punto.getX(getWidth()), punto.getY(getHeight()), puntoPrec.getX(getWidth()), puntoPrec.getY(getHeight()));
                        }

                    }
                }
            }
        }
    }
}
