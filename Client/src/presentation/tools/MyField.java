package presentation.tools;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.Document;


@SuppressWarnings("serial")
public abstract class MyField extends JTextField {
    
    public MyField(Document doc, String text, int columns){
        super(doc, text, columns);
        setKeyListener();
        setInputMethodListener();
        setPasteAction();
    }
    
    abstract protected boolean valid(char c);
    
    abstract protected void pasteAction();
    
    private void setKeyListener(){
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                if(e.isControlDown()){
                    e.consume();
                }
                char c = e.getKeyChar();
                if(!valid(c)){
                    e.consume();
                }
            }
        });
    }
    
    private void setInputMethodListener(){
         this.addInputMethodListener(new InputMethodListener(){

            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                int count = event.getCommittedCharacterCount();
                if(count > 1){
                    event.consume();
                }else if(count == 1){
                    char c = event.getText().first();
                    if(!valid(c))event.consume();
                }
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {
                // NO need
            }
            
        });
    }

    private void setPasteAction(){
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "MyPaste");
        this.getActionMap().put("MyPaste", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e) {
                pasteAction();
            }
            
        });

    }

}
