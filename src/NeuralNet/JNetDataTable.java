/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author alessia
 */
public class JNetDataTable extends JPanel {
   
    public Data data;
    public JTable table;

    public JNetDataTable() {

    }

    public JNetDataTable(Data data) {
        this.data = data;
        
        this.setLayout( new  FlowLayout(FlowLayout.LEFT));
        table = new JTable(tablem);
        table.setVisible(true);
        this.add(table); 
        System.out.println(table.getValueAt(1, 2));
        ;
        
    }
    

    private final TableModel tablem = new AbstractTableModel() {

        @Override
        public int getRowCount() {
           
            
            
            return data.SampleList.size();
            //return 0;
        }

        @Override
        public int getColumnCount() {
           
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            double ret = 0;
            if (columnIndex == 0) {
                ret = data.SampleList.get(rowIndex).inputData[0];

            } else if (columnIndex == 1) {
                ret = data.SampleList.get(rowIndex).testData[0];
            } else if (columnIndex == 2) {
                ret = data.SampleList.get(rowIndex).outputData[0];
            } else {
                throw new Error("colerr");
            }

            return ret;
        }
    };

}
