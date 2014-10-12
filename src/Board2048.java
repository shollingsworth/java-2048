import java.util.Arrays;
import java.util.Random;

///////////////////////////////////////////////////////////
// Name: Steven Hollingsworth
// Date: 2014-07-02
// Description:
//    Class made to setup the 2048 Game Board and corresponding 
//    Tiles and methods associated with it
///////////////////////////////////////////////////////////

public class Board2048  {
   /* These do not change */
   static private final int percDist = 50; //percentage of twos to "randomly" generate
   static boolean DEBUG = false; //in case of testing

   /* new instance variable types */
   private int boardSize; //e.g 4=4x4 , 5=5x5
   private int [] board ; //array holding playing board
   private int[][] rows ; //row list and order
   private int[][] cols ; //column list and order
   private int score = 0; //score


   /**
   Main Class, nothing special here
   @param args Command line arguments
   */

   public static void main(String[] args) {
      Board2048 b = new Board2048(4);
      /*
       Sample Boards
       */
      b.board = 
       new int[] {
          2, 4, 16, 32
         ,4, 16, 32,2 
         ,16, 32, 2,4 
         ,32, 2, 4, 4
      } ;

      //b.printBoard();
      b.moveTiles("DOWN");
      //System.out.print("Score:" + b.score + "\n");
      //System.out.println( "all moves:" + b.getAllMoves() );
      //b.printBoard();
      /*
      */
   }

   /* Constructors  */
   public Board2048() { //new object DEFAULT
      boardSize = 4;
      score = 0;
      init_board();
   }

   public Board2048( int bs) { //new object create with boardsize passed as arg
      boardSize = bs;
      init_board();
   }

   public int getScore() {
      return score;
   }

   private static enum Direction {
      UP("cols","reverse","UP (cols)")
      ,DOWN("cols","forward","DOWN (cols)")
      ,LEFT("rows","reverse","LEFT (rows)")
      ,RIGHT("rows","forward","RIGHT (rows)")
      ;

      String direction;
      String description;
      String arr;
      private Direction (String arr ,String direction, String description) {
         this.arr = arr;
         this.direction = direction;
         this.description = description;
      }
   }   

   public boolean moveTiles (String this_dir) {
      if(DEBUG) System.out.println("-----------------------------------");
      if(DEBUG) System.out.println(Direction.valueOf(this_dir).description + "/" + Direction.valueOf(this_dir).direction);
      if (getPossibleMoves(this_dir) > 0) { //If we can move in that direction go for it
         setMove(this_dir);
         if(DEBUG) printBoard();
         if(spawnTile()) { if(DEBUG) System.out.println("Tile Spawned"); }
         return true;
      } else {
         return false;
      }
   }

   /* This is a reference shim for the Direction Enum above */
   private int[][] getSortArray (String arr_name) {
      if (arr_name == "rows") {
            return rows;
      } else {
            return cols;
      }
   }

   /* Actually Set the Board */
   private void setMove(String direction) {
      board = shiftTiles(getSortArray(Direction.valueOf(direction).arr),Direction.valueOf(direction).direction); //first shift to line up
      board = combineTiles(getSortArray(Direction.valueOf(direction).arr),true); //then combine similar tiles (2,2,2,2) will turn into 0,0,4,4 NOT 0,0,0,8
      board = shiftTiles(getSortArray(Direction.valueOf(direction).arr),Direction.valueOf(direction).direction); //then shift tiles again for final rest
   }

   public int[] getBoard() {
      return this.board;
   }

   public int getAllMoves () {
      int all_moves = 0;
      all_moves += getPossibleMoves("UP");
      all_moves += getPossibleMoves("LEFT");
      return all_moves;
   }


   /* Get Possible Moves/tile combines in that direction (if zero, we're not moving) */
   private int getPossibleMoves(String direction) {
      int bdiff = 0; //differences
      int [] arr1; //array 1 (original board)
      int [] arr2; //array 2 (combine board)
      int [] arr3; //array 3 (shifted board)

      arr1 = Arrays.copyOf(board, board.length);
      arr2 = combineTiles(getSortArray(Direction.valueOf(direction).arr),false);
      arr3 = shiftTiles(getSortArray(Direction.valueOf(direction).arr),Direction.valueOf(direction).direction);

      for (int i = 0; i < arr1.length; i++) {
         if( arr1[i] != arr2[i]) { bdiff++; }
      }

      for (int i = 0; i < arr1.length; i++) {
         if( arr1[i] != arr3[i]) { bdiff++; }
      }
      if(DEBUG) System.out.println("tile_diff:" + bdiff);
      return bdiff;
   }

   /** Print Board for debugging */
   private void printBoard() {
      for(int x = 0; x < board.length; x++) {
         int mod = (x % boardSize) ;
         String s = "\t";
         s +=  "sq{" + x + "}";
         s +=  "val{" + board[x] + "}";
         if(mod == (boardSize - 1)) { s += "\n"; }
         System.out.print(s); //don't if debug this statement i call it explicitly
      }
      System.out.println("--------------------------------------------");
   }

   /* Pass an array and direction, return the shifted tile layout */
   private int [] shiftTiles (int [][] arr, String direction) {
      int [] board_copy = Arrays.copyOf(board, board.length); //copy the current board into the temporary board
      int [] new_board = new int [board.length];

      String debug_string = "";

      for (int i = 0; i < arr.length; i++) { 
         debug_string = " i:" + i;
         if(DEBUG) System.out.println(debug_string);

         int [] tmparr = new int [arr.length];
         int tmparr_e = 0;

         //Do the shifting ... direction doesn't matter at this point
         int skip_place = 0;
         for (int z = 0 ; z < arr[i].length; z++) {  
            int val = board_copy[arr[i][z]];
            if(val == 0) { skip_place++; continue; }
            tmparr[tmparr_e] = val;
            tmparr_e++;
         }

         if(DEBUG) System.out.println("skip_place:" + skip_place);

         int s_place = 0;
         int place = 0;
         for (int z = skip_place; z < tmparr.length && s_place < tmparr.length + skip_place; z++) {
            place = z - skip_place;
            s_place = z;
            int val = tmparr[place];
            if(direction == "forward") {
               new_board[arr[i][s_place]] = val;
            } else {
               new_board[arr[i][place]] = val;
            }
            if(DEBUG) System.out.print(" place:" + place);
            if(DEBUG) System.out.print(" s_place:" + s_place);
            if(DEBUG) System.out.print(" val:" + val);
            if(DEBUG) System.out.println("");
         }
      }
      if(DEBUG) System.out.println("returning:"  + " " + Arrays.toString(new_board));
      return new_board;
   }

   private int [] combineTiles (int [][] arr, boolean keep_score ) //WE don't really need direction here
   {
      int [] board_copy = Arrays.copyOf(board, board.length);
      for (int i = 0; i < arr.length; i++)
      {
         for (int z = arr[i].length - 1; z >= 0; z--) 
         {
            int current = z;
            int next ;
            if((z + 1) == arr[i].length) { continue; } //avoid array out of bounds

            next = z + 1;
            int a1 = arr[i][current];
            int a2 = arr[i][next];
            int v1 = board_copy[a1];
            int v2 = board_copy[a2];
            if(v1 == v2) {
               if(v1 == 0) continue;
               int newval ;
               //newval = v1 * 2;
               newval = v1 + 1;
               if(keep_score) score += Math.pow(2,newval);
               board_copy[a2] = newval;
               board_copy[a1] = 0;
               z--;
            }
         }  
      }
      return board_copy;
   }

   private int getTile() {
      int rnum = new Random().nextInt(100);
      if(rnum <= percDist) {
         return 1;
      } else {
         return 2;
      }
   }

   private boolean spawnTile () {
      if(getFree() >= 1) {
         int tile = getTile();
         while(true) {
            int rnum = new Random().nextInt(board.length);
            if(board[rnum] == 0 ) {
               board[rnum] = tile;
               return true;
            }
         }
      } 
      return false; //if we get here we didn't find a free tile
   }


   /**
   Get Number of Free/null/0 Board spaces
   @return number of free tiles in board object
   */
   public int getFree() { //Get Blank / Free Tiles
      int free = 0;
      for (int i = 0; i < board.length; i++) { if(board[i] == 0 ) { free ++; } }
      return free;
   }

   private int [] getGroup (int row,String type) {
      int [] arr = new int [this.boardSize];
      int el = 0; //element number
      int rnum = -1;
      for(int x = 0; x < board.length; x++) {
         int mod = (x % boardSize) ;
         if(mod == 0) { rnum++; }
         if(type == "row") {
            if(rnum == row) { arr[el++] = x; }
         } else {
            if(mod == row) { arr[el++] = x; }
         }
      }
      return arr;
   }

   /* Initialize board  */
   private void init_board() {
      rows = new int[boardSize][boardSize];
      cols = new int[boardSize][boardSize];
      int blen = boardSize * boardSize;
      board = new int [blen]; //active board .. will initialize with zero values
      for(int v = 0; v < boardSize; v++) {
         rows[v] = getGroup(v,"row");
         cols[v] = getGroup(v,"col");
      }
      //spawn two tiles
      spawnTile();
      spawnTile();
   }
}
