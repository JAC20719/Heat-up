import java.util.Scanner;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Color;

/** Computer processor heat plate simulator.
   @author Justin Canedy
   jcanedy1
   10/21/2018
*/
public class Proj3B {
   /** Creates an 2D array(plate) with the regulated plate.
     * temperatures H, C, and non regulated parts "."
     * @param openedFile the 2D array to be manipulated to represent the plate 
     * @param plateTemps the string of hot and cold locations for the array
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     */
   public static void transfer(char[][] openedFile, String plateTemps, 
      int rows, int columns) { 
      int p = 0; //Index of String
      
      //Puts each string character in the 2D array openedFile   
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            openedFile[r][c] = plateTemps.charAt(p);
            p++;
         }
      }
   }
   
   /** Replaces H and C in 2D array with respective.
     * hot and cold temperatures, "." with 0
     * @param openedFile the 2D array with H's, C's, and .'s 
     * @param cold the low regulated temperature
     * @param hot the high regulated temperature
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @return new 2D array reference to plate with temperatures
     */
   public static double[][] loadHotandCold(char[][] openedFile, 
      double cold, double hot, int rows, int columns) {
      double[][] openedFileModified = new double[rows][columns];
        
      //Create an array of regulated temperatures
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            if (openedFile[r][c] == 'C') {
               openedFileModified[r][c] = cold;
            }
            else if (openedFile[r][c] == 'H') {
               openedFileModified[r][c] = hot;
            }
            else {
               openedFileModified[r][c] = 0.00;
            }
         }
      }
      return openedFileModified;
   }
   
   /** Checks if the current position in 2D array is an edge
     * and which edge it is.
     * @param r the current row 
     * @param c the current column
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @return the location of edge or not an edge
     */
   public static String edgeCheck(int r, int c, int rows, int columns) {
      String edge = "Not an edge."; //Default condition
      
      //Determines if position in 2D array is a left edge
      if (c == 0) {
         edge = "Left edge.";
      }
      //Determines if position in 2D array is a right edge
      else if (c == columns - 1) {
         edge = "Right edge.";
      }
      //Determines if position in 2D array is a bottom edge
      else if (r == rows - 1) {
         edge = "Bottom edge.";
      }
      //Determines if position in 2D array is a top edge
      else if (r == 0) {
         edge = "Top edge.";
      }
      //Returns the edge location
      return edge;
   }
   
   /** Checks if the current position in 2D array is a corner.
     * and which corner it is
     * @param r the current row 
     * @param c the current column
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @return the location of corner or not a corner
     */
   public static String cornerCheck(int r, int c, int rows, int columns) {
      String corner = "Not a corner."; //Default condition
      
      //Determines if position in 2D array is a top left corner
      if (r == 0 && c == 0) {
         corner = "Top left corner.";
      }
      //Determines if position in 2D array is a top right corner
      else if (r == 0 && c == (columns - 1)) {
         corner = "Top right corner.";
      }
      //Determines if position in 2D array is a bottom left corner
      else if (r == (rows - 1) && c == 0) {
         corner = "Bottom left corner.";
      }
      //Determines if position in 2D array is a bottom right corner
      else if (r == (rows - 1) && c == (columns - 1)) {
         corner = "Bottom right corner.";
      }
      //Returns the corner location
      return corner;  
   }
   
   /** Creates an 2D array(plate) with booleans representing.
     * if temperature in array is fixed or not
     * @param openedFile the 2D array with H's, C's, and .'s 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @return new 2D array reference of booleans
     */
   public static boolean[][] restricted(char[][] openedFile, 
      int rows, int columns) {
      //New 2D array to hold locations of the regulated locations
      boolean[][] controlledTemps = new boolean[rows][columns];
      
      //Puts true in 2D array if location is regulated, otherwise, false
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            if (openedFile[r][c] == 'H' || openedFile[r][c] == 'C') {
               controlledTemps[r][c] = true;
            }
            else {
               controlledTemps[r][c] = false;
            }
         }
      }
      
      //Returns 2D array reference of regulated temperature locations
      return controlledTemps;
   }
   
   /** Checks if current plate is stabalized
     * i.e. the temperatures on the current plate
     * are within epsilon of the new plate
     * @param currentPlate plate with most recent averaged temperatures
     * @param oldPlate plate with previous averaged temperatures 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @param epsilon the number the values of current plate 
     *   must be away from values of old plate
     * @return false if plate is not within epsilon of the other
     */
   public static boolean epsilonCheck(double[][] currentPlate, 
      double[][] oldPlate, int rows, int columns, double epsilon) {
      //Default condition
      boolean within = false; 
      
      //Number of values in 2D array within epsilon of previous 2D array
      int trueCount = 0;
      
      //Difference between previous and new plate
      double epsCompare = 0;
      
      //Checks to see if each location in 2D array 
      //is within epsilon of the previous 2D array
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            epsCompare = currentPlate[r][c] - oldPlate[r][c];
            if (epsilon >= epsCompare && 0 - epsilon <= epsCompare) {
               trueCount = trueCount + 1;
            }
         }
      }
      if (trueCount == (rows * columns)) {
         within = true;
      }
      //Returns a boolean, signifyng within or not
      return within;
   }
   
   /** Creates an array(plate) with stabalized temps, each number.
     * in 2D array an average of numbers around them, regulated
     * temperatures do not change
     * @param openedFileModified the 2D array temperatures 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @param epsilon the number the values of current plate 
     *   must be away from values of old plate
     * @param controlledTemps the 2D array of booleans, giving which 
     *   temperatures are regulated
     * @return new 2D array reference with regulated temperatures
     */
   public static double[][] heatAverages(double[][] openedFileModified, 
      int rows, int columns, double epsilon, boolean[][] controlledTemps) {
      
      //Initializes a new 2D array for the current plate
      double[][] currentPlate = new double[rows][columns];
      
      //Initializes a new 2D array for the old plate
      double[][] oldPlate = new double[rows][columns];
      
      //Fills current plate with the values of the initial plate
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            currentPlate[r][c] = openedFileModified[r][c];
         }
      }
      
      //Averages the temperatures across the 2D array plate
      do {
         // System.out.print("\n");
      //          for (int r = 0; r < rows; ++r) {
      //             for (int c = 0; c < columns; ++c) {
      //                System.out.printf("%.3f ", currentPlate[r][c]);
      //             }
      //             System.out.print("\n");
      //          }
         for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < columns; ++c) {
               oldPlate[r][c] = currentPlate[r][c];
            }
         }
         for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < columns; ++c) {
               if (!(controlledTemps[r][c])) {
                  cornerAverage(currentPlate, oldPlate, r, c, rows, columns);
                  if ((cornerCheck(r, c, rows, 
                     columns).equals("Not a corner."))) {
                     edgeAverage(currentPlate, oldPlate, r, c, rows, columns);
                  }
               }
            }
         }
      } while (!(epsilonCheck(currentPlate, oldPlate, rows, columns, epsilon)));
      
      //Returns the reference of the heat distributed plate
      return currentPlate;
   }
   
    /** Calculates the average temperature of
     * a corner in a 2D array.
     * are within epsilon of the new plate
     * @param currentPlate plate with most recent averaged temperatures
     * @param oldPlate plate with previous averaged temperatures
     * @param r the current row 
     * @param c the current column 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     */
   public static void cornerAverage(double[][] currentPlate, 
      double[][] oldPlate, int r, int c, int rows, int columns) {
      
      //Averages 2D array location if a top left corner
      if (cornerCheck(r, c, rows, 
                     columns).equals("Top left corner.")) { 
         currentPlate[r][c] = (oldPlate[r + 1][c] + 
                        oldPlate[r][c + 1]) / 2; 
      }
      //Averages 2D array location if a top right corner
      else if (cornerCheck(r, c, rows, 
                     columns).equals("Top right corner.")) {
         currentPlate[r][c] = (oldPlate[r + 1][columns - 1] + 
                        oldPlate[r][columns - 2]) / 2;
      }
      //Averages 2D array location if a bottom left corner
      else if (cornerCheck(r, c, rows, 
                     columns).equals("Bottom left corner.")) {
         currentPlate[r][c] = (oldPlate[rows - 2][c] + 
                        oldPlate[rows - 1][c + 1]) / 2;
      }
      //Averages 2D array location if a bottom right corner
      else if (cornerCheck(r, c, rows, 
                     columns).equals("Bottom right corner.")) {
         currentPlate[r][c] = (oldPlate[rows - 2][columns - 1] + 
                        oldPlate[rows - 1][columns - 2]) / 2;
      }
   }
   
    /** Calculates the average temperature of
     * an edge in a 2D array.
     * are within epsilon of the new plate
     * @param currentPlate plate with most recent averaged temperatures
     * @param oldPlate plate with previous averaged temperatures
     * @param r the current row 
     * @param c the current column 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     */
   public static void edgeAverage(double[][] currentPlate, 
      double[][] oldPlate, int r, int c, int rows, int columns) {
      //Averages 2D array location of a top edge
      if (edgeCheck(r, c, rows, columns).equals("Top edge.")) {
         currentPlate[r][c] = (oldPlate[r][c - 1] + 
                           oldPlate[r][c + 1] + oldPlate[r + 1][c]) / 3;
      }
      //Averages 2D array location of a bottom edge
      else if (edgeCheck(r, c, rows, 
                        columns).equals("Bottom edge.")) {
         currentPlate[r][c] = (oldPlate[rows - 1][c + 1] + 
                           oldPlate[rows - 1][c - 1] + 
                              oldPlate[rows - 2][c]) / 3;
      }
      //Averages 2D array location of a right edge
      else if (edgeCheck(r, c, rows, 
                        columns).equals("Right edge.")) {
         currentPlate[r][c] = (oldPlate[r + 1][columns - 1] + 
                           oldPlate[r - 1][columns - 1] + 
                              oldPlate[r][columns - 2]) / 3;
      }
      //Averages 2D array location of a left edge
      else if (edgeCheck(r, c, rows, 
                        columns).equals("Left edge.")) {
         currentPlate[r][c] = (oldPlate[r + 1][c] + 
                           oldPlate[r - 1][c] + oldPlate[r][c + 1]) / 3;
      }
      //Averages 2D array location if not an edge or a corner
      else {
         currentPlate[r][c] = (oldPlate[r - 1][c] + 
            oldPlate[r + 1][c] + oldPlate[r][c + 1] + 
            oldPlate[r][c - 1]) / 4;
      }
   
   }
   
   /** Creates a picture visualization of the regulated plate.
     * @param heatAverages the 2D array with regulated temperatures 
     * @param rows the number of rows in the 2D array
     * @param columns the number of columns in the 2D array
     * @param cold the low regulated temperature
     * @param hot the high regulated temperature
     * @return new Picture reference of plate
     */
   public static Picture createPic(double[][] heatAverages, int rows, 
      int columns, int cold, int hot) {
      
      //Creates a range for the ratio between the hot and cold temperatures
      int range = hot - cold;
      
      //Create a new 2D array to hold the ratios
      double[][] ratio = new double[rows][columns];
      
      //Adds ratios of plate temperatures
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            ratio[r][c] = ((heatAverages[r][c] - cold) * 2) / range;  
         }
      }
      
      //Creates a 2D array of the red values
      double[][] red = new double[rows][columns];
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            red[r][c] = 255 * (ratio[r][c] - 1);
            if (red[r][c] < 0) {
               red[r][c] = 0;
            }
         }
      }
      //Creates a 2D array of the blue values
      double[][] blue = new double[rows][columns];
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            blue[r][c] = 255 * (1 - ratio[r][c]);
            if (blue[r][c] < 0) {
               blue[r][c] = 0;
            }
         }
      }
      //Creates a 2D array of the green values
      double[][] green = new double[rows][columns];
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            green[r][c] = 255 - red[r][c] - blue[r][c];
         }
      }
      
      //Creates a picture representation of the plate
      Picture picture = new Picture(columns, rows);
      for (int r = 0; r < picture.height(); ++r) {
         for (int c = 0; c < picture.width(); ++c) {
            Color newColor = new Color((int) red[r][c], 
               (int) green[r][c], (int) blue[r][c]);
            picture.set(c, r, newColor);
         }
      }
      
      //Returns a picture
      return picture;
   }
   
   /** Prints a 2D array to the screen. 
    *  @param toBePrinted the 2D array to be printed
    */
   // public static void printArray(int[][] toBePrinted) {
//       for (int r = 0; r < toBePrinted.length; ++r) {
//          for (int c = 0; c < toBePrinted[0].length; ++c) {
//             System.out.print(toBePrinted[r][c]);
//          }
//          System.out.print("\n");
//       }
//    }
   /** Prints a 2D array to the screen. 
    *  @param toBePrinted the 2D array to be printed
    */
   // public static void printArray(char[][] toBePrinted) {
//       for (int r = 0; r < toBePrinted.length; ++r) {
//          for (int c = 0; c < toBePrinted[0].length; ++c) {
//             System.out.print(toBePrinted[r][c]);
//          }
//          System.out.print("\n");
//       }
//    }
   /** Prints a 2D array to the screen. 
    *  @param toBePrinted the 2D array to be printed
    */
   // public static void printArray(double[][] toBePrinted) {
//       for (int r = 0; r < toBePrinted.length; ++r) {
//          for (int c = 0; c < toBePrinted[0].length; ++c) {
//             System.out.printf("%.5f   ", toBePrinted[r][c]);
//          }
//          System.out.print("\n");
//       }
//    }
   
   /** Main method that allows for user input and 
    *  method handling.
    *  @param args not used
    *  @throws IOException if there is an error
    */
   public static void main(String[]args) throws IOException {
      Scanner scnr = new Scanner(System.in);
      
      //Opens a txt file of users choice
      System.out.print("Enter input filename: ");
      String fileName = scnr.next();
      FileInputStream fileByteStream = new FileInputStream(fileName);
      Scanner outIS = new Scanner(fileByteStream);
      
      //Retrieves columns and rows from a txt file
      int rows = outIS.nextInt();
      int columns = outIS.nextInt();
      
      //Intializes an empty string to hold opened txt file's content
      String plateTemps = "";
      
      //Adds remainder of txt file to plateTemps
      while (outIS.hasNext()) {
         plateTemps = plateTemps + outIS.next();
      }
      
      //Creates a 2D array of characters to hold opened txt file content
      char[][] openedFile = new char[rows][columns];
      
      //Transfers characters in plateTemps to 2D array of characters openedFile
      transfer(openedFile, plateTemps, rows, columns);
      
      //Creates a 2D array of booleans to determine 
      //which location temperatures are regulated
      boolean[][] controlledTemps = restricted(openedFile, rows, columns); 
      
      //printArray(openedFile);
      
      //Retrieve hot and cold temperatures from input
      System.out.print("Enter the cold temperature: ");
      int cold = scnr.nextInt();
      System.out.print("Enter the hot temperature: ");
      int hot = scnr.nextInt();
      
      //Create new 2D array to hold the temperatures of the plate
      double[][] openedFileModified = new double[rows][columns];
      
      //Replaces the openedFile array with temperatures 
      //and stores it in the array openedFileModified
      openedFileModified = loadHotandCold(openedFile, cold, hot, rows, columns);
      
      //Retrieve epsilon from input             
      System.out.print("Enter an epsilon value < 1: ");
      double epsilon = scnr.nextDouble();
      
      //Cerate new 2D array that holds the distributed heat temperatures
      double[][] heatAverages = heatAverages(openedFileModified, 
         rows, columns, epsilon, controlledTemps);
      
      //Creates a Picture, picture, that represents 
      //the temperatures across the stabelized plate
      Picture picture = createPic(heatAverages, rows, columns, cold, hot);
      
      //Scales the picture x50
      picture = picture.scale(20);
      
      //Create a PrintWriter to write to a new txt file
      String txtDirectory = fileName.substring(0, fileName.length() - 4);
      txtDirectory = txtDirectory + "_" + cold + "_" + hot + ".txt";
      FileOutputStream outfileByteStream = new FileOutputStream(txtDirectory);
      PrintWriter outFS = new PrintWriter(outfileByteStream);
      
      
      outFS.println("epsilon: " + epsilon);
      outFS.print("\n");
      
      //Print and format the initial plate temperatures to a new txt file
      String[][] cutFile = new String[rows][columns];
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            cutFile[r][c] = String.format("%10.5f", openedFileModified[r][c]);
         }
      }
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            outFS.print(cutFile[r][c]);
         }
         outFS.print("\n");
      }
      outFS.print("\n");
      
      //Print and format the final plate temperatures to a new txt file
      String[][] cutAverages = new String[rows][columns];
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            cutAverages[r][c] = String.format("%10.5f", heatAverages[r][c]);
         }
      }
      for (int r = 0; r < rows; ++r) {
         for (int c = 0; c < columns; ++c) {
            outFS.print(cutAverages[r][c]);
         }
         outFS.print("\n");
      }
      outFS.flush();
      
      //Create a new jpg file with the plate temperatures picture representation
      String jpgDirectory = fileName.substring(0, fileName.length() - 4);
      jpgDirectory = jpgDirectory + "_" + cold + "_" + hot + ".jpg";
      picture.save(jpgDirectory);
      
      outfileByteStream.close();
      fileByteStream.close();
      
      System.out.println();
      System.out.println("Output is going to files: " + 
         txtDirectory + " and " + jpgDirectory);
      picture.show();
      
   }
}