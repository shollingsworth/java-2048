import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class KeyListenerExample {
    public static void main(String[] args) {
       //Use the event dispatch thread for Swing components
       //http://www.javamex.com/tutorials/threads/invokelater.shtml
       EventQueue.invokeLater(new Runnable() {
          @Override
          public void run() { new KeyListenerExample(); }
       });
    }

    public KeyListenerExample() {
        //Text Areas
        JTextArea feedbackText = new JTextArea();
        JTextArea inputText = new JTextArea();
        JScrollPane scrollText = new JScrollPane(feedbackText);

        //Fame
        JFrame guiFrame = new JFrame();
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make sure the program exits when the frame closes
        guiFrame.setTitle("Creating a Table Example");
        guiFrame.setSize(300,300);
        guiFrame.setLocationRelativeTo(null); //This will center the JFrame in the middle of the screen

        inputText.addKeyListener(new KeyListener() {
           @Override public void keyPressed(KeyEvent e) { inputText.setText(""); }
           @Override public void keyReleased(KeyEvent e) { inputText.setText(""); }
           @Override public void keyTyped(KeyEvent e) {
              inputText.setText("");
              char key = e.getKeyChar();
              feedbackText.setText("Key Typed: " + key + " " + KeyEvent.getKeyModifiersText(e.getModifiers()) + "\n");
           }
        });

        //layout
        guiFrame.add(inputText, BorderLayout.NORTH);
        guiFrame.add(scrollText, BorderLayout.CENTER);
        guiFrame.setVisible(true);
    }
}
