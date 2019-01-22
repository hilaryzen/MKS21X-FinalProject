import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.googlecode.lanterna.terminal.Terminal.SGR;
//API : http://mabe02.github.io/lanterna/apidocs/2.1/
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.LanternaException;
import com.googlecode.lanterna.input.CharacterPattern;
import com.googlecode.lanterna.input.InputDecoder;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.KeyMappingProfile;
import com.googlecode.lanterna.screen.Screen;

//For Windows
//javac -cp "lanterna.jar;." MenuDemo.java
//java -cp "lanterna.jar;." MenuDemo TestCSV.csv

//For school
//javac -cp lanterna.jar:. MenuDemo.java
//java -cp lanterna.jar:. MenuDemo TestCSV.csv

public class MenuDemo {

  public static void putString(int r, int c,Terminal t, String s){
    t.moveCursor(r,c);
    for(int i = 0; i < s.length();i++){
      t.putCharacter(s.charAt(i));
    }
  }

  public static void putString(int r, int c,Terminal t,
    String s, Terminal.Color forg, Terminal.Color back ){
    t.moveCursor(r,c);
    t.applyBackgroundColor(forg);
    t.applyForegroundColor(Terminal.Color.BLACK);

    for(int i = 0; i < s.length();i++){
      t.putCharacter(s.charAt(i));
    }
    t.applyBackgroundColor(Terminal.Color.DEFAULT);
    t.applyForegroundColor(Terminal.Color.DEFAULT);
  }

  //Clears a given number of spaces
  public static void clearSpace(Terminal t, int spaces) {
    t.applyBackgroundColor(Terminal.Color.DEFAULT);
    t.applyForegroundColor(Terminal.Color.DEFAULT);
    for (int i = 0; i < spaces; i++) {
      t.putCharacter(' ');
    }
  }

  //Adds yellow background behind given cell
  public static void highlight(int row, int col, Terminal t, Sheet sheet) {
    int r = findR(row);
    int c = findC(col, sheet);
    //Formatting the text
    String data = sheet.getString(row,col);
    int spaceLength = sheet.longestInCol(col) + 3;
    String entry = String.format("%-" + spaceLength + "." + spaceLength + "s", data);
    t.moveCursor(c,r);
    t.applyBackgroundColor(Terminal.Color.YELLOW);
    t.applyForegroundColor(Terminal.Color.BLACK);
    //Rewrites cell with yellow background
    for (int j = 0; j < spaceLength; j++) {
      t.putCharacter(entry.charAt(j));
    }
    t.applyBackgroundColor(Terminal.Color.DEFAULT);
    t.applyForegroundColor(Terminal.Color.DEFAULT);
  }

  //Highlights all selected cells
  public static void highlightAll(ArrayList<Integer> rows, ArrayList<Integer> cols, Terminal t, Sheet sheet) {
    for (int i = 0; i < rows.size(); i++) {
      highlight(rows.get(i), cols.get(i), t, sheet);
    }
  }

  //Converts cell row in the data array to cursor location in the terminal
  public static int findR(int row) {
    return row + 9;
  }

  //Converts cell col in data array to cursor location in the terminal
  public static int findC(int col, Sheet sheet) {
    int c = 0;
    for (int i = 0; i < col; i++) {
      c = c + sheet.longestInCol(i) + 3;
    }
    return c;
  }

  public static String printBoolean(boolean value, String t, String f) {
    if (value) {
      return t;
    } else {
      return f;
    }
  }

  public static int numLength(int value) {
    int length = 0;
    while (value > 9) {
      value = value / 10;
      length++;
    }
    return length + 1;
  }

  //execute after user action; refreshes the screen
  public static void update(Sheet file, String filename, Terminal terminal, boolean selecting, boolean editRows, int sum, int avg, int max, int min) {
    putString(0,0,terminal, "Spreadsheet: " + filename,Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,2,terminal, "Selecting? (press Ctrl + S to switch): " + printBoolean(selecting, "Y", "N"),Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,3,terminal, "Inserting/deleting rows or columns? (press Ctrl + R to switch): " + printBoolean(editRows, "Rows", "Cols"),Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,4,terminal, "Sum: " + sum,Terminal.Color.WHITE,Terminal.Color.RED);
    clearSpace(terminal,20);
    sum = file.sum();
    putString(5,4,terminal, sum + "", Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,5,terminal, "Average: " + avg,Terminal.Color.WHITE,Terminal.Color.RED);
    clearSpace(terminal, 20);
    avg = file.avg();
    putString(9,5,terminal, avg + "", Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,6,terminal, "Maximum: " + max, Terminal.Color.WHITE,Terminal.Color.RED);
    clearSpace(terminal, 20);
    max = file.max();
    putString(9,6,terminal, max + "", Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,7,terminal, "Minimum: " + min, Terminal.Color.WHITE,Terminal.Color.RED);
    clearSpace(terminal, 20);
    min = file.min();
    putString(9,7,terminal, min + "", Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,9,terminal,file.toString(),Terminal.Color.WHITE,Terminal.Color.RED);
    highlightAll(file.selectedRow(),file.selectedCol(),terminal,file);
  }

  public static void main(String[] args) {
    Terminal terminal = TerminalFacade.createTextTerminal();
		terminal.setCursorVisible(false);

    boolean running = true;
    boolean start = false;

    Screen screen = new Screen(terminal, 500, 500); // initialize screen
		screen.startScreen(); // puts terminal in private; updates screen

    String filename = "";

		// catches no CSV provided
		if (args.length < 1 ) {
			putString(0,0,terminal, "Please enter a file name that ends with .csv: ", Terminal.Color.WHITE,Terminal.Color.RED);
      while (! start) {
        Key key = terminal.readInput();
        if (key != null) {
          if (key.getKind() == Key.Kind.Enter) {
            try {
              File data = new File(filename);
              data.createNewFile();
              FileWriter filewriter = new FileWriter(data);
              filewriter.write(" , , \n , , \n , , ");
              filewriter.close();
              start = true;
            } catch (IOException e) {
              System.out.println("File could not be created");
              e.printStackTrace();
              System.exit(1);
            }
          } else {
            filename += key.getCharacter();
            putString(45,0,terminal, filename, Terminal.Color.WHITE,Terminal.Color.RED);
          }
        }
      }
		} else {
      filename = args[0];
      start = true;
    }

		//creates Sheet
    Sheet file = new Sheet(filename);
    //Tracks cell user is on
    int row = 0;
    int col = 0;
    //Tracks how many chars a user has edited in a cell
    int writing = 0;
    //Tracks if user is selecting multiple cells or not
    boolean selecting = false;
    //Tracks if user is inserting or deleting rows
    //If false, user is inserting/deleting cols
    boolean editRows = true;
    //Tracks sum of selected Cells
    int sum = 0;
    //Tracks average of selected Cells
    int avg = 0;
    //Tracks max of selected Cells
    int max = 0;
    //Tracks min of selected Cells
    int min = 0;
    //Tracks if user has selected entire row or col
    boolean line = false;
    //prints the Screen once at the beginning
    update(file, filename, terminal, selecting, editRows, sum, avg, max, min);

    while(running){
      Key key = terminal.readInput();
      if (key != null)
      {
        //YOU CAN PUT DIFFERENT SETS OF BUTTONS FOR DIFFERENT MODES!!!
        if (key.isCtrlPressed()) {
          if (key.getCharacter() == 's') {
            //Changes to selecting mode
            selecting = ! selecting;
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          } else if (key.getCharacter() == 'r') {
            //Changes between rows and cols (used for inserting and deleting)
            editRows = ! editRows;
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          } else if (key.getCharacter() == 'l') {
            //Selects entire row or col that user is on
            line = true;
            if (editRows) {
              file.selectRow(row);
              update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
            } else {
              file.selectCol(col);
              update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
            }
          }
        }
        else { /// normal navigation vvvvv
          if (key.getKind() == Key.Kind.Escape) {
            //Saves data to the same file before closing the terminal
            file.save();
            screen.stopScreen();
            running = false;
          }
          else if (key.getKind() == Key.Kind.ArrowDown) {
            //If user is in selecting mode, the cell below will be highlighted as well
            //If not, user simply jumps to cell below
            writing = 0;
            if (selecting) {
              if (row < file.rows() - 1) {
                row++;
                file.select(row,col);
              }
            }
            else {
              row = (row + 1) % file.rows();
              file.jumpTo(row,col);
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.ArrowUp) {
            //If user is in selecting mode, the cell above will be highlighted as well
            //If not, user simply jumps to cell above
            writing = 0;
            if (selecting) {
              if (row > 0) {
                row--;
                file.select(row,col);
              }
            }
            else {
              if (row == 0) {
                row = file.rows() - 1;
              }
              else {
                row--;
              }
              file.jumpTo(row,col);
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.ArrowLeft) {
            //If user is in selecting mode, the cell left will be highlighted as well
            //If not, user simply jumps to cell left
            writing = 0;
            if (selecting) {
              if (col > 0) {
                col--;
                file.select(row,col);
              }
            }
            else {
              if (col == 0) {
                col = file.cols() - 1;
              }
              else {
                col--;
              }
              file.jumpTo(row,col);
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.ArrowRight) {
            //If user is in selecting mode, the cell right will be highlighted as well
            //If not, user simply jumps to cell right
            writing = 0;
            if (selecting) {
              if (col < file.cols() - 1) {
                col++;
                file.select(row,col);
              }
            }
            else {
              col = (col + 1) % file.cols();
              file.jumpTo(row,col);
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          // normal navigation ^^^
          else if (key.getKind() == Key.Kind.Enter) {
            if (line) {
              //If user has selected row or col, enter moves the row down or col right
              if (editRows) {
                if (row < file.rows() - 1) {
                  file.shiftRowDown(row);
                  file.clearSelect();
                  row++;
                  file.selectRow(row);
                }
              } else {
                if (col < file.cols() - 1) {
                  file.shiftColRight(col);
                  file.clearSelect();
                  col++;
                  file.selectCol(col);
                }
              }
            } else {
              //Moves down one row and ends writing mode
              row = (row + 1) % file.rows();
              writing = 0;
              file.jumpTo(row,col);
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.Insert) {
            //Adds new empty row below or empty col to the right
            writing = 0;
            if (editRows) {
              row++;
              file.addRow(row);
            }
            else {
              col++;
              file.addCol(col);
            }
            file.jumpTo(row,col);
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.Delete) {
            //Deletes selected row or col
            writing = 0;
            if (editRows) {
              file.removeRow(row);
              if (row == file.rows()) {
                row--;
              }
            }
            else {
              file.removeCol(col);
              if (col == file.cols()) {
                col--;
              }
            }
            file.jumpTo(row,col);
            terminal.clearScreen();
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else if (key.getKind() == Key.Kind.Backspace) {
            if (line) {
              if (editRows && row != 0) {
                file.shiftRowUp(row);
                file.clearSelect();
                row--;
                file.selectRow(row);
              } else if (editRows && col != 0) {
                file.shiftColLeft(col);
                file.clearSelect();
                col--;
                file.selectCol(col);
              }
            } else {
              //Deletes last character if user is writing and entire entry if user is not
              String data = file.getString(row,col);
              if (writing > 0) {
                writing--;
                file.set(data.substring(0,writing));
              }
              else {
                file.set("");
              }
            }
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          // tab to sort
          else if (key.getKind() == Key.Kind.Tab) {
            file.sortRows(col);
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
          else {
            //Takes char that user enters
            Terminal t = terminal;
            char newChar = key.getCharacter();
            //Changes text of cell
            String data = file.getString(row,col);
            file.set(data.substring(0,writing) + newChar);
            writing++;
            update(file, filename, terminal, selecting, editRows, sum, avg, max, min);
          }
        }
      }
    }

  }
}
