package enginuity.NewGUI.etable;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import enginuity.NewGUI.data.TableNodeMetaData;

public class ETable extends JTable{
	public static final int INCREMENT = 0;
	public static final int DECREMENT = 1;
	public static final int MULTIPLY = 2;
	public static final int SET = 3;
	
	private ETableModel theModel;
	private Vector tempSelectedCells = new Vector();
	
	ETable(TableNodeMetaData metaData, double[][] data){
		this.theModel = new ETableModel(metaData.getTableName(), data);
		super.setModel(this.theModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.setCellSelectionEnabled(true);
		this.setDefaultRenderer(Object.class, new ETableCellRenderer(metaData.getMinValue(), metaData.getMaxValue(), metaData.getIgnoredValues(), metaData.isInvertedColoring()));
	}
	
	
	/**
	 * Zero based row and columns, inclusive
	 * 
	 * @param rowStart
	 * @param rowEnd
	 * @param colStart
	 * @param colEnd
	 */
	public void setSelectedQuadrilateral(int rowStart, int rowEnd, int colStart, int colEnd){
		for(int i = rowStart; i < rowEnd + 1; i++){
			for(int j = colStart; j < colEnd + 1; j++){
				this.changeSelection(i, j, false, true);
			}
		}
	}
	
	
	/**
	 * Set a cell as being selected.
	 * 
	 * @param rowIndex
	 * @param colIndex
	 */
	public void setSelected(int rowIndex, int colIndex){

	    boolean toggle = false;
	    boolean extend = true;
	    //this.changeSelection(rowIndex, colIndex, toggle, extend);
	    //this.changeSelection(1, 1, toggle, extend);
	    //this.changeSelection(2, 2, toggle, extend);
	    //this.setSelectedQuadrilateral(1,3,4,6);
	    //this.addColumnSelectionInterval(3, 6);
	    
	    System.out.println(" >"+ this.getSelectedColumnCount()+"  >"+this.getSelectedRowCount());
	}
	
	/**
	 * Increment cell values by passed double amount.
	 * @param amount
	 */
	public void changeSelectedCells(double amount, int type){
		Vector tempCells = new Vector();
		
		int rowStart = this.getSelectedRow();
		int rowEnd = this.getSelectionModel().getMaxSelectionIndex();
		
		int colStart = this.getSelectedColumn();
		int colEnd = this.getColumnModel().getSelectionModel().getMaxSelectionIndex();
		
		int counter = 0;
		for(int i = rowStart; i <= rowEnd; i++){
			for(int j = colStart; j <= colEnd; j++){
				if(this.isCellSelected(i,j)){
					counter++;
					tempCells.add(new Coordinate(i,j));
					// The cell is selected
					Object value = theModel.getValueAt(i, j);
					
					if(value instanceof Double){
						double temp = 0.0;
						if(type == ETable.INCREMENT){
							 temp = (Double)value + amount;
							 System.out.println("INC");
						}
						
						if(type == ETable.DECREMENT){
							 temp = (Double)value - amount;
							 System.out.println("DEC");
						}
						
						if(type == ETable.MULTIPLY){
							 temp = (Double)value * amount;
							 System.out.println("MULT");
						}
						
						if(type == ETable.SET){
							 temp = amount;
							 System.out.println("SET");
						}
						
						//theModel.setValueAt(temp, i, j);
						theModel.setDoubleData(i,j,temp);
					}
				}
			}
		}
		
		if(counter == 0){
			Iterator tempIterate = this.tempSelectedCells.iterator();
			while(tempIterate.hasNext()){
				Coordinate tempCoord = (Coordinate)tempIterate.next();
				
				Object value = theModel.getValueAt(tempCoord.getX(), tempCoord.getY());
				
				if(value instanceof Double){
					Double temp = (Double)value + amount;
					//theModel.setValueAt(temp, i, j);
					theModel.setDoubleData(tempCoord.getX(),tempCoord.getY(),temp);
				}
			}
		}else{
			this.tempSelectedCells = tempCells;
		}
		
		theModel.refresh();
		
	}
	
	/**
	 * Replace all table data with passed data.
	 * @param newData
	 */
	public void replaceAlltableData(double[][] newData){
		((ETableModel)this.dataModel).replaceData(newData);
	}


	public ETableModel getTheModel() {
		return theModel;
	}
	
	private class Coordinate{
		private int x;
		private int y;
		
		public Coordinate(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public int getX(){
			return this.x;
		}
		
		public int getY(){
			return this.y;
		}
	}
}
