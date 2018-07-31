package presentation.tools;

import javax.swing.text.Document;


@SuppressWarnings("serial")
public class IntField extends MyField{
    
    private int defaultValue;
    
    public IntField(){
        this(null, 0, 0, 0);
    }
    
    public IntField(int columns){
        this(null, 0, columns, 0);
    }

    public IntField(int value, int columns){
        this(null, value, columns, 0);
    }
    
    public IntField(int value, int columns, int defaultValue){
        this(null, value, columns, defaultValue);
    }
    
    public IntField(Document doc, int value, int columns, int defaultValue){
        super(doc, value + "", columns);
        this.defaultValue = defaultValue;
    }
    
    public int getValue(){
        try{
            return Integer.parseInt(this.getText());
        }catch(Exception e){
            return defaultValue;
        }
    }

    public void setValue(int value){
        super.setText(value + "");
    }
    
    public void setDefaultValue(int defaultValue){
        this.defaultValue = defaultValue;
    }
    
    public int getDefaultValue(){
        return this.defaultValue;
    }

    @Override
    public void setText(String text){
        try{
            Integer.parseInt(text);
            super.setText(text);
        }catch(NumberFormatException e){
            return;
        }
    }

    @Override
    protected boolean valid(char c){
        if(super.getText().length() > 8){
            // exceeded the limit of int
            return false;
        }
        if(c >= '0' && c <= '9'){
            return true;
        }
        if(c >= 32 && c <= 126){
            // seeable chars
            return false;
        }
        return false;
    }

    @Override
    protected void pasteAction() {
        // no paste action
    }
    
}
