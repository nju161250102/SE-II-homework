package presentation.component.choosewindow;

import java.awt.Component;
import java.awt.Container;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextField;

import presentation.component.DateChooser;
import presentation.component.InfoWindow;
import presentation.tools.TableTools;
import vo.MyTableModel;

public abstract class BillChooseWin extends ChooseWindow {
    
    protected String customerId;
    private Container searchPanel;
    private DateChooser dateFromChooser, dateToChooser;
    private JTextField dateFromField, dateToField;
    
    public BillChooseWin(String customerId){
        super();
        this.customerId = customerId;
        table.setModel(getInitModel());
        table.setDragEnabled(false);
        TableTools.autoFit(table);
        frame.setTitle("选择源销售单");
    }
    
    abstract protected void initBLService();

    abstract protected MyTableModel getInitModel();
    
    abstract protected MyTableModel search(String type, String key);
    
    abstract protected MyTableModel getBillByDate(String from, String to);
    
    abstract protected void setBill(String id);

    @Override
    public void init() {
        searchPanel = searchTypeBox.getParent();
        initDateLabels();
        initBLService();
        setTypes(new String[]{"按编号搜索", "按时间搜索"});
        searchTypeBox.addItemListener(e -> handleItemChanged());
    }

    @Override
    protected void yesAction() {
        int index = table.getSelectedRow();
        if(index >= 0){
            String id = (String)table.getValueAt(index, 1);
            setBill(id);
            frame.dispose();
        }
    }
    
	@Override
	protected void searchAction() {
		String type = searchTypeBox.getSelectedItem().toString();
		if(type.equals("按编号搜索")){
		    String key = keyField.getText();
		    if(key.length() == 0){
		        table.setModel(getInitModel());
		    } else {
                table.setModel(search(type, key));
		    }
		} else {
		    String from = dateFromField.getText(), to = dateToField.getText();
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    try {
                Date fromDate = dateFormat.parse(from), toDate = dateFormat.parse(to);
                if(fromDate.after(toDate)){
                    new InfoWindow("请选择正确的日期@_@");
                    return;
                }
                table.setModel(getBillByDate(from, to));
            } catch (ParseException e) {
                e.printStackTrace();
            }
		}
		TableTools.autoFit(table);
	}

	private void initDateLabels(){
        dateFromField = new JTextField("开始日期");
        dateToField = new JTextField("结束日期");
        dateFromField.setEditable(false);
        dateToField.setEditable(false);
        dateFromChooser = DateChooser.getInstance();
        dateToChooser = DateChooser.getInstance();
        dateFromChooser.register(dateFromField);
        dateToChooser.register(dateToField);
	}

	private void handleItemChanged(){
	    String selected = searchTypeBox.getItemAt(searchTypeBox.getSelectedIndex());
        Component[] components = searchPanel.getComponents();
	    if(selected.equals("按编号搜索")){
	        int index = indexOf(components, dateFromField);
	        if(index < 0) return;
	        keyField.setText("");
	        searchPanel.remove(dateFromField);
	        searchPanel.remove(dateToField);
	        searchPanel.add(keyField, index);
	    } else {
	        int index = indexOf(components, keyField);
	        if(index < 0) return;
            int x = keyField.getX(),
                   y = keyField.getY(),
                   width = keyField.getWidth() / 2,
                   height = keyField.getHeight();
            dateFromField.setLocation(x, y);
            dateFromField.setSize(width, height);
            dateToField.setLocation(x + width, y);
            dateToField.setSize(width, height);

	        searchPanel.remove(keyField);
	        searchPanel.add(dateFromField, index);
	        searchPanel.add(dateToField, index + 1);
	    }
	    searchPanel.repaint();
	}
	
	private int indexOf(Component[] components, Component c){
	    for(int i = 0; i < components.length; i++){
	        if(components[i].equals(c)){
	            return i;
	        }
	    }
	    return -1;
	}

}
