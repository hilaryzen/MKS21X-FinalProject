import java.io.FileNotFoundException;

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
    return row + 2;
  }

  //Converts cell col in data array to cursor location in the terminal
  public static int findC(int col, Sheet sheet) {
    int c = 0;
    for (int i = 0; i < col; i++) {
      c = c + sheet.longestInCol(i) + 3;
    }
    return c;
}

  //execute after user action
  public static void update(Sheet sh, String f, Terminal t,  Screen sc) {
    putString(0,0,t, "Spreadsheet: " + f,Terminal.Color.WHITE,Terminal.Color.RED);
    putString(0,2,t,sh.toString(),Terminal.Color.WHITE,Terminal.Color.RED);
    highlight(sh.getUserR(),sh.getUserC(),t,sh);
    //noting ^^^ refreshes screen rather than be suppressed
    sc.refresh();
  }

  public static void main(String[] args) {
    Terminal terminal = TerminalFacade.createTextTerminal();
		terminal.setCursorVisible(false);

    boolean running = true;

    Screen screen = new Screen(terminal, 500, 500); // initialize screen
		screen.startScreen(); // puts terminal in private; updates screen
    
		// catches no CSV provided
		if (args.length < 1 ) {
			System.out.println("Incorrect format. Use: java -cp lanterna.jar:. MenuDemo <file.csv>");
      System.exit(1);
		}

		//imports file when given
    String filename = args[0];
    Sheet file = new Sheet(filename);
    int row = 0;
    int col = 0;
    int writing = 0; // ?
    //prints the Screen once at the beginning
    update(file, filename, terminal, screen);

    while(running){
      Key key = terminal.readInput();
      if (key != null)
      {
        //YOU CAN PUT DIFFERENT SETS OF BUTTONS FOR DIFFERENT MODES!!!
        if (key.getKind() == Key.Kind.Escape) {
					screen.stopScreen();
          running = false;
        }
				else if (key.getKind() == Key.Kind.ArrowDown) {
          row = (row + 1) % file.rows();
          writing = 0;
          file.jumpTo(row,col);
          update(file, filename, terminal, screen);
        }
				else if (key.getKind() == Key.Kind.ArrowUp) {
          row = (row - 1) % file.rows();
          writing = 0;
          file.jumpTo(row,col);
          update(file, filename, terminal, screen);
				}
				else if (key.getKind() == Key.Kind.ArrowLeft) {
          col = (col - 1) % file.cols();
          writing = 0;
          file.jumpTo(row,col);
          update(file, filename, terminal, screen);
				}
				else if (key.getKind() == Key.Kind.ArrowRight) {
          col = (col + 1) % file.cols();
          writing = 0;
          file.jumpTo(row,col);
          update(file, filename, terminal, screen);
        }
				/*
				else if (key.getCharacter() == 'w'){ // w for write
          Terminal t = terminal;
					t.enterPrivateMode();
          t.clearScreen();
          t.applyBackgroundColor(Terminal.Color.YELLOW);
          t.applyForegroundColor(Terminal.Color.BLACK);
        }
				*/
      }
    }
  }
}
