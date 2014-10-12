import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

////////////////////////////////////////////////////////////////////////////////////
// Name: Steven Hollingsworth
// Date: 2014-07-02 22:00
// Description:
//		UI Game Demo Relying mostly on Board2048 class
////////////////////////////////////////////////////////////////////////////////////

public class GameDemo  implements KeyListener, ActionListener, ComponentListener {
   private final boolean DEBUG = true;
   private final boolean DEBUG_LEVEL2 = false;

	/* Counter for Testing */
   private int cnt = 0;

   /* square size */
   private int BoardSize;

   /* our gameBoard */
   private Board2048 gameBoard;

   /* Contructor */
   public GameDemo() {
      BoardSize = 4;
      gameBoard = new Board2048(BoardSize);
      init_bgcolors();
      prepareGUI();
   }

   /* Main Frame / Status Bars */
   JFrame mainFrame = new JFrame("2048!");
   /* Middle Frame */
   JPanel controlPanel = new JPanel();
   /* Game Panel */
   JPanel BoardLayout = new JPanel();
   /* Status Panel */
   JPanel statusPanel = new JPanel();

   /* Array holding all the tiles */
   JLabel[] all_tiles ;

   JLabel statusLabel = new JLabel("moves:" + cnt++,JLabel.CENTER);
   JLabel scoreLabel = new JLabel("score:",JLabel.CENTER);
   JTextField typingArea = new JTextField(20);
   JButton okButton = new JButton("Clear");

   Color[] bgcolor = new Color[20]; //array with X colors
   //base background color
   int sR = 0;
   int sG = 0;
   int sB = 255;


   /* Main Loop if executed standalone */
   public static void main(String[] args) {
      GameDemo game = new GameDemo();
      System.out.println(game.all_tiles);
   }

   public void init_bgcolors() {
      float hsbVals[] = Color.RGBtoHSB(sR,sG,sB,null);
      if(DEBUG) System.out.println("hsbvals:" + Arrays.toString(hsbVals));

      int sat_start = 15;
      float adder = (100 - sat_start - 1) / bgcolor.length + 1;

      if(DEBUG) System.out.print("adder:" + adder);
      if(DEBUG) System.out.println("");
      float sat = sat_start;
      for (int i = 0; i < bgcolor.length; i++) {
         if((sat % 100) != sat) {
            sat = 100;
         }
         bgcolor[i] = Color.getHSBColor(hsbVals[0], sat / 100, hsbVals[1]);
         /* Color.getHSBColor(float (0-1) h, float (0-1) s, float (0-1) b)
            h = the hue component
            s = the saturation of the color
            b = the brightness of the color
         */
         float tmphsb[] = Color.RGBtoHSB(bgcolor[i].getRed(),bgcolor[i].getGreen(),bgcolor[i].getBlue(),null);
         if(DEBUG) System.out.println("hsbvals:" + Arrays.toString(tmphsb));
         if(DEBUG) System.out.print("sat:" + sat);
         if(DEBUG) System.out.print("diff:" + sat % 100);
         if(DEBUG) System.out.println("");
         sat += adder;
         if(i == 0) bgcolor[i] = Color.WHITE; 
      }
   }

   public String getSymbol (int val, String type) {
      String newval = "";
      switch (type) {
         case "alpha_lc": if(val != 0) newval = String.valueOf(Character.toChars(val + 96)); break;
         case "alpha_uc": if(val != 0) newval = String.valueOf(Character.toChars(val + 64)); break;
         case "unicode_1": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2600 -1))); break;
         case "unicode_2": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2620 -1))); break;
         case "unicode_3": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2702 -1))); break;
         case "unicode_4": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2665 -1))); break;
         case "chess": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2654 -1))); break;
         case "brail": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2801 -1))); break;
         case "smiley": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x263A -1))); break;
         case "num_circle": if(val != 0) newval = String.valueOf(Character.toChars(val + (0x2776 -1))); break;
         default : if(val != 0) newval = (int)Math.pow(2,val) + "";
      }
      return  newval;
   }

   private void displayTiles(Board2048 board) {
      int[] vals = board.getBoard();
      for (int i = 0; i < all_tiles.length ; i++) {
         all_tiles[i].setText(getSymbol(vals[i],"unicode_1"));
         all_tiles[i].setForeground(Color.WHITE);
         all_tiles[i].setBackground(bgcolor[vals[i]]);
      }

      //Dynamically Size Buttons and Labels
      scoreLabel.setText("score:" + board.getScore());

      setTileFont(scoreLabel);
      setTileFont(statusLabel);


      //Dynamically Size tiles
      for (int i = 0; i < all_tiles.length; i++) {
         setTileFont(all_tiles[i]);
      }


   }

   private void setTileFont (JLabel label) {
      if(DEBUG_LEVEL2) System.out.println("-------------------------------------------");
      Font labelFont = label.getFont();
      String labelText = label.getText();

      if(DEBUG_LEVEL2) System.out.println("labelFont" + labelFont);
      if(DEBUG_LEVEL2) System.out.println("labelText" + labelText);
      
      int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
      if(DEBUG_LEVEL2) System.out.println("stringWidth" + stringWidth);

      int componentWidth = label.getWidth();
      if(DEBUG_LEVEL2) System.out.println("componentWidth" + componentWidth);
      
      // Find out how much the font can grow in width.
      double widthRatio = (double)componentWidth / (double)stringWidth;
      if(DEBUG_LEVEL2) System.out.println("widthRatio" + widthRatio);
      
      int newFontSize = (int)(labelFont.getSize() * widthRatio);
      if(DEBUG_LEVEL2) System.out.println("newFontSize" + newFontSize);
      int componentHeight = label.getHeight();
      if(DEBUG_LEVEL2) System.out.println("componentHeight" + componentHeight);
      
      // Pick a new font size so it will not be larger than the height of label.
      int fontSizeToUse = Math.min(newFontSize, componentHeight) - 2;
      if(DEBUG_LEVEL2) System.out.println("fontSizeToUse" + fontSizeToUse);
      
      // Set the label's font size to the newly determined size.
      label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
   }


   /* Prepares GUI Elements and Layout */
   private void prepareGUI() {
      ///////////////////////////////////////////////////////////
      // Frame INIT
      ///////////////////////////////////////////////////////////
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new BorderLayout());
      /* center display */
      controlPanel.setLayout(new BorderLayout());

      /* Game Board */
      BoardLayout.setLayout(new GridLayout(BoardSize,BoardSize));

      /* Status Panel Layout */
      statusPanel.setLayout(new GridLayout());
      //AbstractBorder brdrLeft = new TextBubbleBorder(Color.BLACK,2,16,16);
      //AbstractBorder brdrRight = new TextBubbleBorder(Color.BLACK,2,16,16,false);

      all_tiles = new JLabel[BoardSize * BoardSize];
      Border blackline = BorderFactory.createEtchedBorder(EtchedBorder.RAISED ,Color.WHITE ,Color.BLACK);

      for (int i = 0; i < all_tiles.length; i++) {
         JLabel tile = new JLabel("",JLabel.CENTER);
         all_tiles[i] = tile;
         tile.setBorder(blackline);
         tile.setOpaque(true);
         BoardLayout.add(tile);
      }

      /* Commence Putting things together */
      mainFrame.add(typingArea, BorderLayout.PAGE_START); //very top, typing layout


      /* Middle Board Layout */
      mainFrame.add(controlPanel, BorderLayout.CENTER); //container for control panel
      controlPanel.add(BoardLayout,BorderLayout.CENTER);
      controlPanel.add(okButton,BorderLayout.PAGE_END);

      /* End Status bar */
      mainFrame.add(statusPanel, BorderLayout.PAGE_END);
      statusPanel.add(statusLabel);
      statusPanel.add(scoreLabel);


      /* Add Listeners to Buttons and Text Area */
      mainFrame.addComponentListener(this);
      okButton.addActionListener(this);        
      typingArea.addKeyListener(this);

      //displayTiles(gameBoard);

      /* Finally make it all visible */
      mainFrame.setVisible(true);  
   }

   public void actionPerformed(ActionEvent e) {
      statusLabel.setText("Areas all clear");
      //Return the focus to the typing area.
      typingArea.requestFocusInWindow();
   }

   /* Don't care about the next two, but they are required because I implement them */
   public void keyPressed(KeyEvent e) { } public void keyReleased(KeyEvent e) { } 
   /* Don't care about these component events */
   public void componentMoved (ComponentEvent e) { }
   public void componentShown (ComponentEvent e) { }
   public void componentHidden (ComponentEvent e) { }

   /**
   This is where most of the navigation stuff happens
   */

   /* If On then update with values, otherwise clear values  */
   private void updateDisplays(boolean on ) {
      /* Always set the typing area to clear */
      if (on == true ) {
         statusLabel.setText("moves:" + cnt++);
      } 
      displayTiles(gameBoard);
      typingArea.requestFocusInWindow();
   }

   public void componentResized (ComponentEvent e) {
      System.out.println("========RESIZED");

      int statusPanelHeight = controlPanel.getHeight() / 15;
      int statusPanelWidth = controlPanel.getWidth();
      statusPanel.setPreferredSize(new Dimension(statusPanelWidth,statusPanelHeight));

      if(DEBUG) System.out.println("controlPanel.getHeight:" + statusPanelHeight);
      if(DEBUG) System.out.println("controlPanel.getWidth:" + statusPanelWidth);

      updateDisplays(true);

   }

   public void keyTyped(KeyEvent e) { 
      String c = e.getKeyChar() + ""; 
      typingArea.setText(""); 
      switch(c.toLowerCase()) {
         case "k":
            if(gameBoard.moveTiles("UP")) updateDisplays(true);
            return;
         case "j":
            if(gameBoard.moveTiles("DOWN")) updateDisplays(true);
            return;
         case "l":
            if(gameBoard.moveTiles("RIGHT")) updateDisplays(true);
            return;
         case "h":
            if(gameBoard.moveTiles("LEFT")) updateDisplays(true);
            return;
         case "q":
            System.exit(0);
      }
      /* Don't show anything if we didn't press a proper key */
      updateDisplays(false);
   }
}
