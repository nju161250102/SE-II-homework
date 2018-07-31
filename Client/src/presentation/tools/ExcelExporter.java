package presentation.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import vo.MyTableModel;

public final class ExcelExporter {

    private ExcelExporter() {}
    
    public static void export(MyTableModel model){
        if(model == null) return;
        String path = openFileChooser();
        if(path == null) return;
        if(export(model, path)){
            JOptionPane.showMessageDialog(null, "导出成功^_^");
        } else {
            JOptionPane.showMessageDialog(null, "导出失败，请重试@_@");
        }
    }
    
    public static boolean export(MyTableModel model, String path){
        if(model == null) return false;
        HSSFWorkbook workbook = toExcel(model);
        return saveExcel(workbook, path);
    }
    
    public static String openFileChooser(){
 		try {
            String platform = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(platform);
		} catch (Exception e) {
			e.printStackTrace();
		}
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFileChooser f = new JFileChooser();
        f.setFileFilter(new FileFilter(){

            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) return true;
                String[] parts = f.getName().split("[.]");
                int length = parts.length;
                String suffix = parts[length - 1];
                return suffix.equals("xls");
            }

            @Override
            public String getDescription() {
                return "Excel工作表(*.xls)";
            }
            
        });

        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        f.showSaveDialog(null);
        File file = f.getSelectedFile();
        return file == null ? null : file.getAbsolutePath() + ".xls";
    }
    
    private static HSSFWorkbook toExcel(MyTableModel model){
        // create a workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        // create a sheet in the workbook
        HSSFSheet sheet = workbook.createSheet();
        // create the header row in the sheet
        HSSFRow row = sheet.createRow(0);
        // set cell style
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        // create several cells in the header row
        HSSFCell cell;
        int rowCount = model.getRowCount(), columnCount = model.getColumnCount();
        for(int i = 0; i < columnCount; i++){
            cell = row.createCell(i);
            cell.setCellValue(model.getColumnName(i));
            cell.setCellStyle(style);
        }
        // fill data into the sheet
        for(int i = 0; i < rowCount; i++){
            row = sheet.createRow(i + 1);
            for(int j = 0; j < columnCount; j++){
                String value = String.valueOf(model.getValueAt(i, j));
                row.createCell(j).setCellValue(value);
            }
        }
        return workbook;
    }
    
    private static boolean saveExcel(HSSFWorkbook workbook, String path){
        try{
            FileOutputStream fout = new FileOutputStream(path);
            workbook.write(fout);
            fout.flush();
            fout.close();
            return true;
        }catch(IOException e){
            return false;
        }
    }

}
