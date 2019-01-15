//API : http://mabe02.github.io/lanterna/apidocs/2.1/
import com.googlecode.lanterna.terminal.Terminal.SGR;
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
import java.util.ArrayList;

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

  public static void highlight(int row, int col, Terminal t, Sheet sheet) {
    int r = findR(row);
    int c = findC(col, sheet);
    String data = sheet.getString(row,col);
    int spaceLength = sheet.longestInCol(col) + 3;
    String entry = String.format("%-" + spaceLength + "." + spaceLength + "s", data);
    t.moveCursor(c,r);
    t.applyBackgroundColor(Terminal.Color.YELLOW);
    t.applyForegroundColor(Terminal.Color.BLACK);
    for (int j = 0; j < spaceLength; j++) {
      t.putCharacter(entry.charAt(j));
    }
    t.applyBackgroundColor(Terminal.Color.DEFAULT);
    t.applyForegroundColor(Terminal.Color.DEFAULT);
  }

  public static void highlightAll(ArrayList<Integer> rows, ArrayList<Integer> cols, Terminal t, Sheet sheet) {
    for (int i = 0; i < rows.size(); i++) {
      highlight(rows.get(i), cols.get(i), t, sheet);
    }
  }

  public static int findR(int row) {
    return row + 2;
  }

  public static int findC(int col, Sheet sheet) {
    int c = 0;
    for (int i = 0; i < col; i++) {
      c = c + sheet.longestInCol(i) + 3;
    }
    return c;
  }

  public static void main(String[] args) {

    Terminal terminal = TerminalFacade.createTextTerminal();
    terminal.enterPrivateMode();

    TerminalSize size = terminal.getTerminalSize();
    terminal.setCursorVisible(false);

    boolean running = true;
    int mode = 0;
    //long lastTime =  System.currentTimeMillis();
    //long currentTime = lastTime;
    //long timer = 0;

    String filename = args[0];
    Sheet file = new Sheet(filename);
    int row = 0;
    int col = 0;
    int writing = 0;

    while(running){
      Key key = terminal.readInput();
      if (key != null)
      {
        //YOU CAN PUT DIFFERENT SETS OF BUTTONS FOR DIFFERENT MODES!!!

        if (key.getKind() == Key.Kind.Escape) {
          //Saves data to the same file before closing the terminal
          file.save();
          terminal.exitPrivateMode();
          running = false;
        } else if (key.getKind() == Key.Kind.ArrowDown) {
          if (key.isCtrlPressed()) {
            writing = 0;
            row += 1;
            file.addRow(row);
            file.jumpTo(row,col);
          } else {
            row = (row + 1) % file.rows();
            writing = 0;
            file.jumpTo(row,col);
          }
        } else if (key.getKind() == Key.Kind.ArrowUp) {
          row = (row - 1) % file.rows();
          writing = 0;
          file.jumpTo(row,col);
        } else if (key.getKind() == Key.Kind.ArrowLeft) {
          col = (col - 1) % file.cols();
          writing = 0;
          file.jumpTo(row,col);
        } else if (key.getKind() == Key.Kind.ArrowRight) {
          col = (col + 1) % file.cols();
          writing = 0;
          file.jumpTo(row,col);
        } else if (key.getKind() == Key.Kind.Enter) {
          //Moves down one row and ends writing mode
          row = (row + 1) % file.rows();
          writing = 0;
          file.jumpTo(row,col);
        } else {
          //Takes char that user enters
          Terminal t = terminal;
          char newChar = key.getCharacter();
          //Changes text of cell
          String data = file.getString(row,col);
          //file.getCell(row,col).setValue(data.substring(0,writing) + newChar + data.substring(writing + 1, data.length()));
          file.getCell(row,col).setValue(data.substring(0,writing) + newChar);
          writing++;
        }
      }

      terminal.applySGR(Terminal.SGR.ENTER_BOLD);
      //putString(1,1,terminal, "This is mode "+mode,Terminal.Color.WHITE,Terminal.Color.RED);
      terminal.applySGR(Terminal.SGR.RESET_ALL);

      //DO GAME STUFF HERE
      putString(0,0,terminal, "Spreadsheet: " + filename,Terminal.Color.WHITE,Terminal.Color.RED);
      putString(0,2,terminal,file.toString(),Terminal.Color.WHITE,Terminal.Color.RED);
      highlightAll(file.selectedRow(),file.selectedCol(),terminal,file);
        
    }

    
  }
}