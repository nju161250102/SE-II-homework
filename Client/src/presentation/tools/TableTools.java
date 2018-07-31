package presentation.tools;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class TableTools {

	public static void autoFit(JTable table) {
		JTableHeader header = table.getTableHeader();
	    int rowCount = table.getRowCount();
	
	    Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
	    while(columns.hasMoreElements()){
	        TableColumn column = columns.nextElement();
	        int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
	        int width = (int)table.getTableHeader().getDefaultRenderer()
	                 .getTableCellRendererComponent(table, column.getIdentifier()
	                         , false, false, -1, col).getPreferredSize().getWidth();
	        for(int row = 0; row < rowCount; row++){
	        	int preferedWidth = (int)table.getCellRenderer(row, col).getTableCellRendererComponent(table,
	               table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
	             width = Math.max(width, preferedWidth);
	        }
	        header.setResizingColumn(column); // 此行很重要
	        column.setWidth(width + table.getIntercellSpacing().width + 15);
		}
	}
}
