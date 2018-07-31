package presentation.promotionui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;
import presentation.component.InfoWindow;
import presentation.component.choosewindow.CommodityChooseWin;
import presentation.tools.DoubleField;
import presentation.tools.IntField;
import presentation.tools.StyleTools;
import vo.CommodityVO;

public class GiftInputWin {
    
    private JDialog frame;
    private String[] data;
    private JTextField idField, nameField, typeField;
    private DoubleField priceField;
    private IntField numField;
    
    public GiftInputWin(){
        this(null);
    }
    
    public GiftInputWin(String[] data){
        super();
        frame = new JDialog();
        StyleTools.setNimbusLookAndFeel();
        frame.setModal(true);
        frame.setSize(400, 300);
        frame.setLocation(400, 200);
        frame.setLayout(new BorderLayout());
        frame.add(initCenter(data), BorderLayout.CENTER);
        frame.add(initSouth(), BorderLayout.SOUTH);
        frame.setTitle("赠品信息");
        frame.setVisible(true);
    }
    
    public String[] getData(){
        return data;
    }
    
    private JPanel initCenter(String[] data){
        idField = new JTextField(15);
        idField.setEditable(false);
        idField.addMouseListener(chooseCommodity());
        nameField = new JTextField(10);
        nameField.setEditable(false);
        nameField.addMouseListener(chooseCommodity());
        typeField = new JTextField(10);
        typeField.setEditable(false);
        typeField.addMouseListener(chooseCommodity());
        priceField = new DoubleField(10);
        priceField.setEditable(false);
        priceField.addMouseListener(chooseCommodity());
        numField = new IntField(10);
        if(data != null){
            idField.setText(data[0]);
            nameField.setText(data[1]);
            typeField.setText(data[2]);
            priceField.setText(data[3]);
            numField.setText(data[4]);
        }
        
        double[][] size = {
                {-1.0, -2.0, 10.0, -2.0, -1.0},
                {-1.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, 10.0, -2.0, -1.0}
        };
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(new JLabel("商品编号"), "1 1");
        panel.add(idField, "3 1");
        panel.add(new JLabel("名称"), "1 3");
        panel.add(nameField, "3 3");
        panel.add(new JLabel("型号"), "1 5");
        panel.add(typeField, "3 5");
        panel.add(new JLabel("单价"), "1 7");
        panel.add(priceField, "3 7");
        panel.add(new JLabel("数量"), "1 9");
        panel.add(numField, "3 9");
        return panel;
    }
    
    private JPanel initSouth(){
        JButton okButton = new JButton("确认");
        okButton.addActionListener(e->ok());
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e->cancel());
        double[][] size = {{-1.0, -2.0, 10.0, -2.0, 20.0}, {10.0, -2.0, 10.0}};
        JPanel panel = new JPanel(new TableLayout(size));
        panel.add(okButton, "1 1");
        panel.add(cancelButton, "3 1");
        return panel;
    }
    
    private void ok(){
        String id = idField.getText(),
               name = nameField.getText(),
               type = typeField.getText(),
               price = priceField.getText(),
               num = numField.getText();
        if(id.length() == 0){
            new InfoWindow("请选择商品。");
            return;
        }
        if(numField.getValue() == 0){
            new InfoWindow("请输入大于零的数量。");
            return;
        }
        data = new String[]{id, name, type, price, num};
        frame.dispose();
    }
    
    private void cancel(){
        frame.dispose();
    }

    private MouseListener chooseCommodity(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                CommodityVO commodity = new CommodityChooseWin().getCommodity();
                if(commodity == null) return;
                idField.setText(commodity.getId());
                nameField.setText(commodity.getName());
                typeField.setText(commodity.getType());
                priceField.setValue(commodity.getRecentInPrice());
            }
        };
    }

}
