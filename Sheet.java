import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Sheet {
  private ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>(); //array
  //Stores all the row values of the cells selected by the user
  private ArrayList<Integer> rows = new ArrayList<Integer>();
  //Stores all the col values of the cells selected
  private ArrayList<Integer> cols = new ArrayList<Integer>();
  //Stores filename for saving
  private String originalFile = "";
	private user cursor;
	
  public Sheet(String filename) {
    try {
      originalFile = filename;
      File csv = new File(filename);
      Scanner in = new Scanner(csv); //import value
      int Row = 0; //counts index rows
      while(in.hasNextLine()) {
        data.add(new ArrayList<Cell>()); // initialize this row
        String[] line = in.nextLine().split(",");
        for (int x = 0; x < line.length; x++) {
          String storage = line[x];
          try {
            data.get(Row).add(new Cell<Integer>(Integer.parseInt(storage)));
          }
          catch(NumberFormatException e) {
            data.get(Row).add(new Cell<String>(storage));
          }
        }
        Row++;
      }
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
      System.exit(1);
    }

    //Automatically selects the first cell
    rows.add(0);
    cols.add(0);
		
		cursor = new user(data.size(), data.get(0).size());
  }

  //Returns the number of rows in the sheet
  public int rows() {
    return data.size();
  }

  public int selectedRow() {
    return rows.get(0);
  }

  public int selectedCol() {
    return cols.get(0);
  }

  //Returns the cell at the row and col given
  public Cell getCell(int row, int col) {
    return data.get(row).get(col);
  }

  //Returns a String listing the values of all the selected cells
  public String get() {
    String selected = "";
    for (int i = 0; i < rows.size(); i++) {
      selected += getCell(rows.get(i), cols.get(i)) + "\n";
    }
    return selected;
  }

	//extracts usable String from cell
	public String getString(int row, int col) {
		Cell placeholder = this.getCell(row, col);
		return "" + placeholder.getValue();
	}

	//extracts usable Integer from a cell
	private Integer getInt(int row, int col) {
		return Integer.parseInt(getString(row, col));
	}

  // adds the internal value of cells
  // throws error if vales are Strings
  public int findSum(int row1, int col1, int row2, int col2) {
      return getInt(row1, col1) + getInt(row2,col2);
  }

  //Returns String contains the contents of the row at the index given
  public String getRow(int index) {
    String ans = "";
    for (int i = 0; i < data.get(index).size(); i++) {
      String entry = getString(index, i);
      int spaceLength = longestInCol(i) + 3;
      ans = ans + String.format("%-" + spaceLength + "." + spaceLength + "s", entry);
    }
    return ans;
  }

  //Returns String containing the contents of the column at the given index
  //Does not work if not all rows are the same length
  public String getCol(int index) {
    String ans = "";
    for (int i = 0; i < data.size(); i++) {
      ans += getString(i, index) + ", " ;
    }
    return ans;
  }

  //Finds the length of the longest entry in a col
  public int longestInCol(int index) {
    int longest = getString(0, index).length();
    int length = 0;
    for (int i = 1; i < data.size(); i++) {
      length = getString(i, index).length();
      if (length > longest) {
        longest = length;
      }
    }
    return longest;
  }

  //Returns String of the entire table
  public String toString() {
    String ans = "";
    for (int i = 0; i < data.size(); i++) {
      ans = ans + getRow(i) + "\n";
    }
    return ans;
  }

 
  //Adds given cell to list of selected cells
  public boolean select(int row, int col) {
    rows.add(row);
    cols.add(col);
    return true;
  }

  //Changes value of the cell at the coordinates given, returns old value
  public String set(int row, int col, String newValue) {
    String old = getString(row, col);
    getCell(row, col).setValue(newValue);
    return old;
  }

  //Changes value of all the cells selected to the new value given
  public void set(String newValue) {
    for (int i = 0; i < rows.size(); i++) {
      set(rows.get(i), cols.get(i), newValue);
    }
  }

  //Changes value of all cells in the row given to the same new value
  public void setRow(int row, String newValue) {
    for (int i = 0; i < data.get(row).size(); i++) {
      set(row, i, newValue);
    }
  }

  //Changes value of all cells in the col given to the same new value
  public void setCol(int col, String newValue) {
    for (int i = 1; i < data.size(); i++) {
      set(i, col, newValue);
    }
  }

  //Changes value of all cells in the sheet to the same new value
  public void setAll(String newValue) {
    for (int i = 1; i < data.size(); i++) {
      setRow(i, newValue);
    }
  }

  //Takes array of values and creates new row at the bottom of the sheet
  public void addRow(String[] values) {
    //Initializing new row
    data.add(new ArrayList<Cell>());
    //Looping through values to add them to cells
    for (int i = 0; i < values.length; i++) {
      try {
        data.get(rows() - 1).add(new Cell<Integer>(Integer.parseInt(values[i])));
      }
      catch(NumberFormatException e) {
        data.get(rows() - 1).add(new Cell<String>(values[i]));
      }
    }
  }

  //Inserts row of values at given index
  public void addRow(int index, String[] values) {
    data.add(index, new ArrayList<Cell>());
    for (int i = 0; i < values.length; i++) {
      try {
        data.get(index).add(new Cell<Integer>(Integer.parseInt(values[i])));
      }
      catch(NumberFormatException e) {
        data.get(index).add(new Cell<String>(values[i]));
      }
    }
  }

  //Adds array of values as a new column at the very right
  public void addCol(String[] values) {
    //Loops through rows to add a new cell to each one
    for (int i = 0; i < rows(); i++) {
      try {
        data.get(i).add(new Cell<Integer>(Integer.parseInt(values[i])));
      } catch (NumberFormatException e) {
        data.get(i).add(new Cell<String>(values[i]));
      }
    }
  }

  //Adds a new column of values at the given index
  public void addCol(int index, String[] values) {
    for (int i = 0; i < rows(); i++) {
      try {
        data.get(i).add(index, new Cell<Integer>(Integer.parseInt(values[i])));
      } catch (NumberFormatException e) {
        data.get(i).add(index, new Cell<String>(values[i]));
      }
    }
  }

  //Deletes the row at the given index
  public void removeRow(int index) {
    data.remove(index);
  }

  //Deletes the column at the given index
  public void removeCol(int index) {
    //Loops through rows to delete specified cell
    for (int i = 0; i < rows(); i++) {
      data.get(i).remove(index);
    }
  }

  //Returns a string of the table in CSV format
  public String getTable() {
    String ans = "";
    for (int i = 0; i < rows(); i++) {
      ans += getString(i,0);
      for (int j = 1; j < data.get(i).size(); j++) {
        ans = ans + "," + getString(i,j);
      }
      ans += "\n";
    }
    return ans;
  }

  //Overwrites old file to save changes
  public void save() {
    try {
      FileWriter filewriter = new FileWriter(originalFile);
      filewriter.write(getTable());
      filewriter.close();
    } catch (IOException e) {
      System.out.println("File could not be saved");
      e.printStackTrace();
      System.exit(1);
    }
  }

  //Changes new sheet in file given by user
  public void save(String filename) {
    try {
      FileWriter filewriter = new FileWriter(filename);
      filewriter.write(getTable());
      filewriter.close();
    } catch (IOException e) {
      System.out.println("File could not be saved");
      e.printStackTrace();
      System.exit(1);
    }
  }

	/// navigation commands vvv start 
	public void up() {
		cursor.jumpTo(cursor.getURow() - 1, cursor.getUCol());
	}
		
	public void down() {
		cursor.jumpTo(cursor.getURow() + 1, cursor.getUCol());
	}
		
	public void left() {
		cursor.jumpTo(cursor.getURow(), cursor.getUCol() - 1);
	}
		
	public void right() {
		cursor.jumpTo(cursor.getURow(), cursor.getUCol() + 1);
	}
	
	public int getUserR() {
		return cursor.getURow();
	}
	
	public int getUserC() {
		 return cursor.getUCol();
	 }
	/// navigation commands ^^^ end
	
	// used to track movement of the selection
	private class user{
		private int uCol; // column
		private int uRow; // row
		private int colLim; // column edge; data.size()
		private int rowLim; // row edge; data.get(x).size()
		
		public user(int x, int y) {
			uCol = 0;
			uRow = 0;
			colLim = x - 1; 
			rowLim = y - 1;
		}
		
		// general update command
		public void jumpTo(int y, int x) {
			if (!(x < 0 || x > (colLim) || y < 0 || y > (rowLim))){
				uCol = x;
				uRow = y;
			}
		}
		
		/// update commands vvv start
		// set right edge
		// returns old edge
		// use when addColl or removeCol is used
		public int setColLim(int x) {
			int old = colLim;
			colLim = x;
			return old;
		}
		
		//set bottom edge
		//returns old bottom
		//use when addRow or removeRow is used
		public int setRowLim(int y) {
			int old = rowLim;
			rowLim = y;
			return old;
		}
		/// update commands ^^^ end
		
		/// location commands vvv
		public int getUCol() {
			return uCol;
		}
		
		public int getURow() {
			return uRow;
		}
		
		public int getColLim() {
			return colLim;
		}
		
		public int getRowLim() {
			return rowLim;
		}
		/// location comands ^^^
	}
	
}