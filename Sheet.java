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
  }

  //Returns the number of rows in the sheet
  public int rows() {
    return data.size();
  }

  //Returns the max number in cols in a row
  public int cols() {
    int max = data.get(0).size();
    for (int i = 1; i < rows(); i++) {
      if (data.get(i).size() > max) {
        max = data.get(i).size();
      }
    }
    return max;
  }

  //Returns first selected row
  public ArrayList<Integer> selectedRow() {
    return rows;
  }

  //Returns first selected col
  public ArrayList<Integer> selectedCol() {
    return cols;
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
  //returns zero if not an Integer
	public Integer getInt(int row, int col) {
		try{
      return Integer.parseInt(getString(row, col));
    }
    catch (NumberFormatException e){
      return null;
    }
	}

  //Returns String contains the contents of the row at the index given
  public String getRow(int index) {
    String ans = "";
    for (int i = 0; i < data.get(index).size(); i++) {
      String entry = getString(index, i);
      int spaceLength = longestInCol(i) + 3;
      ans += String.format("%-" + spaceLength + "." + spaceLength + "s", entry);
    }
    return ans;
  }

  //Returns String containing the contents of the column at the given index
  //Does not work if not all rows are the same length
  public String getCol(int index) {
    String ans = "";
    for (int i = 0; i < data.size(); i++) {
      ans += getString(i, index) + ", ";
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

  //Deselects all current cells and selects only the given cell
  public boolean jumpTo(int row, int col) {
    rows.clear();
    rows.add(row);
    cols.clear();
    cols.add(col);
    return true;
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

  //Adds empty row
  public void addRow(int index) {
    data.add(index, new ArrayList<Cell>());
    for (int i = 0; i < cols(); i++) {
      data.get(index).add(new Cell<String>(" "));
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

  //Adds empty col
  public void addCol(int index) {
    for (int i = 0; i < rows(); i++) {
      data.get(i).add(index, new Cell<String>(" "));
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

  //Finds the sum of all selected cells
  public int sum() {
    int output = 0;
    for (int i = 0; i < rows.size(); i++) {
      if (getInt(rows.get(i), cols.get(i)) != null) {
        output += getInt(rows.get(i), cols.get(i));
      }
    }
    return output;
  }

  //Finds the sum of cells in a given column
  public int findColSum(int col) {
    int output = 0;
    for (int x = 0; x < rows(); x++) {
      if (getInt(x, col) != null) {
        output += getInt(x, col);
      }
    }
    return output;
  }

  //Finds the sum of cells in a given row
  public int findRowSum(int row) {
    int output = 0;
    for (int x = 0; x < cols(); x++) {
      if (getInt(row, x) != null) {
        output += getInt(row, x);
      }
    }
    return output;
  }

  //Finds the average of all selected cells
  public int avg() {
    //Counts how many integer cells there are
    int size = 0;
    for (int i = 0; i < rows.size(); i++) {
      if (getInt(rows.get(i), cols.get(i)) != null) {
        size++;
      }
    }
    return sum() / size;
  }

  //Finds the max of all selected cells
  public int max() {
    int i = 0;
    //Finds the first integer value
    while (getInt(rows.get(i), cols.get(i)) == null) {
      i++;
    }
    int output = getInt(rows.get(i), cols.get(i)); //output is set to the first integer value
    //Checks rest of values to see if they are larger
    for (int j = i + 1; j < rows.size(); j++) {
      if (getInt(rows.get(j), cols.get(j)) != null && getInt(rows.get(j), cols.get(j)) > output) {
        output = getInt(rows.get(j), cols.get(j));
      }
    }
    return output;
  }

  //Finds the min of selected cells
  public int min() {
    int i = 0;
    //Finds the first integer value
    while (getInt(rows.get(i), cols.get(i)) == null) {
      i++;
    }
    int output = getInt(rows.get(i), cols.get(i)); //set to the first integer value
    //Checks rest of values to see if they are larger
    for (int j = i + 1; j < rows.size(); j++) {
      if (getInt(rows.get(j), cols.get(j)) != null && getInt(rows.get(j), cols.get(j)) < output) {
        output = getInt(rows.get(j), cols.get(j));
      }
    }
    return output;
  }

  // takes a column and sorts entire chart by rows
  //uses insertion sort
  public void sortRows(int col) {
    if (findColSum(col) == 0) { //is this a String col?
      for (int x = 2; x < rows(); x++) { //ignore first row
        String value = getString(x, col);
        if (value.compareTo(getString(x - 1,col)) < 0) { //check if row above is bigger
          for (int y = x; y > 1; y--) {// move back down, stop before first row
            if (value.compareTo(getString(y - 1, col)) < 0) {
              data.add(y - 1, data.get(y));
              data.remove(y + 1);
            }
          }
        }
      }
    }
    else{
      for (int x = 2; x < rows(); x++) { //ignore first row
        Integer value = getInt(x, col);
        if (getInt(x, col) < getInt(x - 1, col)) { //check if row above is bigger
          for (int y = x; y > 1; y--) {// move back down, stop before first row
            if (getInt(y, col) < getInt(y - 1, col)) {
              data.add(y - 1, data.get(y));
              data.remove(y + 1);
            }
          }
        }
      }
    }

  }

}
