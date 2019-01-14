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

  public static void highlight(int row, int col, Terminal t, Sheet sheet) {
		int r = row + 2;
    int c = 0;
    String data = sheet.getString(row,col);
    int spaceLength = sheet.longestInCol(col) + 3;
    String entry = String.format("%-" + spaceLength + "." + spaceLength + "s", data);
    
		for (int i = 0; i < col; i++) {
      c += sheet.longestInCol(i) + 3;
    }
    t.moveCursor(c,r);
		
    t.applyBackgroundColor(Terminal.Color.YELLOW);
    t.applyForegroundColor(Terminal.Color.BLACK);
    
		for (int j = 0; j < spaceLength; j++) {
      t.putCharacter(entry.charAt(j));
    }
		
  }

  public static void main(String[] args) {
		
    Terminal terminal = TerminalFacade.createTextTerminal();
		terminal.enterPrivateMode();
    TerminalSize size = terminal.getTerminalSize();
    terminal.setCursorVisible(false);
		
    boolean running = true;
		
		if (args.length < 1) {
			System.out.println("Incorrec format. Use: java -cp lanterna.jar:. MenuDemo <file.csv>");
			System.exit(0);
		}	
		
    String filename = args[0];
    Sheet file = new Sheet(filename);
    int row = 0;
    int col = 0;
		
    while(running){
      Key key = terminal.readInput();
      if (key != null)
      {
        //YOU CAN PUT DIFFERENT SETS OF BUTTONS FOR DIFFERENT MODES!!!
        if (key.getKind() == Key.Kind.Escape) {
          terminal.exitPrivateMode();
          running = false;
        } 
				else if (key.getKind() == Key.Kind.ArrowDown) {
          file.down();
        } 
				else if (key.getKind() == Key.Kind.ArrowUp) {
          file.up();
				} 
				else if (key.getKind() == Key.Kind.ArrowLeft) {
          file.left();
				} 
				else if (key.getKind() == Key.Kind.ArrowRight) {
          file.right();
        }
				else if (key.getCharacter() == 'w'){ // w for write
          Terminal t = terminal;
					t.enterPrivateMode();
					t.clearScreen();
          t.applyBackgroundColor(Terminal.Color.YELLOW);
          t.applyForegroundColor(Terminal.Color.BLACK);
        }
      }
			
			/*
      terminal.applySGR(Terminal.SGR.RESET_ALL);
      terminal.applySGR(Terminal.SGR.ENTER_BOLD);
			*/
			
      //DO GAME STUFF HERE
      putString(0,0,terminal, "Spreadsheet: " + filename,Terminal.Color.WHITE,Terminal.Color.RED);
      putString(0,2,terminal,file.toString(),Terminal.Color.WHITE,Terminal.Color.RED);
      highlight(file.getUserR(),file.getUserC(),terminal,file);
      
    }

    
  }
}