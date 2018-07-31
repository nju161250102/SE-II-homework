package vo;

import java.util.Arrays;

import javax.swing.table.DefaultTableModel;
/**
 * 继承DefaultTableModel的自定义类，用于实现不可修改的功能
 * @author user
 *
 */
public class MyTableModel extends DefaultTableModel {

	private int[] columns = {};
	
	public MyTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}
	/**
	 * 设置可以编辑的列
	 * @param columns 列号数组
	 */
	public void setEditable(int[] columns) {
		this.columns = columns;
	}
	
	public boolean isCellEditable(int row, int column) {
		if (Arrays.binarySearch(columns, column) >= 0) {
			return true;
		}
		else return false;
	}
	/**
	 * 获得某一行的数据(String数组形式)
	 * @param n 行号
	 * @return 某一行的数据
	 */
	public String[] getValueAtRow(int n) {
		String[] data = new String[this.getColumnCount()];
		for (int i = 0; i < data.length; i++) {
			data[i] = (String) this.getValueAt(n, i);
		}
		return data;
	}
}
