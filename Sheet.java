import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Sheet {
  private ArrayList<ArrayList<Cell>> data = new ArrayList<ArrayList<Cell>>(); //array
  private int[] rows;
  private int[] cols;

  public Sheet(String filename) {

    try {

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

  }

  //Returns the cell at the row and col given
  public Cell get(int row, int col) {
    return data.get(row).get(col);
  }

	//extracts usable String from cell
	private String getString(int row, int col) {
		Cell placeholder = this.get(row, col);
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
      String entry = get(index, i).toString();
      int spaceLength = longestInCol(i);
      ans = ans + String.format("%-" + spaceLength + "." + spaceLength + "s", entry);
    }
    return ans;
  }

  //Returns String containing the contents of the column at the given index
  //Does not work if not all rows are the same length
  public String getCol(int index) {
    String ans = get(0, index).toString();
    for (int i = 1; i < data.size(); i++) {
      ans = ans + ", " + get(i, index).getValue();
    }
    return ans;
  }

  public int longestInCol(int index) {
    int longest = get(0, index).toString().length();
    int length = 0;
    for (int i = 1; i < data.size(); i++) {
      length = get(i, index).toString().length();
      if (length > longest) {
        longest = length;
      }
    }
    return longest;
  }

  //Returns String of the entire table
  public String getTable() {
    String ans = "";
    for (int i = 0; i < data.size(); i++) {
      ans = ans + getRow(i) + "\n";
    }
    return ans;
  }

  public String set(int row, int col, String newValue) {
    String old = get(row, col).toString();
    get(row, col).setValue(newValue);
    return old;
  }
}
