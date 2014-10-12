/**
 * JTextFieldTest.java
 * Copyright (c) 2002 by Dr. Herong Yang, http://www.herongyang.com/
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
public class JTextFieldTest {
   public static void main(String[] a) {
      JFrame f = new JFrame("Text Field Test");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      MyTextField t = new MyTextField(16);
      f.getContentPane().add(t,BorderLayout.CENTER);
      f.pack();
      f.setVisible(true);
   }
   private static class MyTextField extends JTextField 
      implements ActionListener, DocumentListener  {
      static int count = 0;
      public MyTextField(int l) {
         super(l);
         addActionListener(this);
         Document doc = this.getDocument();
         System.out.println("The document object: "+doc);
         doc.addDocumentListener(this);
      }
      public void actionPerformed(ActionEvent e) {
         count++;
         System.out.println(count+": Action performed - "+getText());
      }
      public void insertUpdate(DocumentEvent e) {
         count++;
         System.out.println(count+": Insert update - "+getText());
      }
      public void removeUpdate(DocumentEvent e) {
         count++;
         System.out.println(count+": Remove update - "+getText());
      }
      public void changedUpdate(DocumentEvent e) {
         count++;
         System.out.println(count+": Change update - "+getText());
      }
   }
}
