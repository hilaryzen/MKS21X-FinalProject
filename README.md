# MKS21X-FinalProject
CSV Bucks

# Instructions

Our CSV editor can either open an existing file for editing, if the user gives the filename, or allow the user to create a new sheet, if the program does not receive an argument.

It can be run using bash runTest.sh, which automatically opens TestCSV.csv. You can edit the second command in the file to open a different CSV.

Use the arrow keys to move around the table. If you turn on selecting mode by pressing Ctrl + S, you can use the arrows to highlight multiple cells and edit them at the same time.

You can switch between working with rows or columns with Ctrl + R. The Insert key either creates an empty row below or an empty column to the right, and the Delete key removes the row or column that you are currently on. You can highlight an entire row or column with Ctrl + L, and then use the Enter and Backspace keys to shift it around.

Pressing the Tab key will sort the rows into increasing order based on the column that you are currently on. Once you're done editing, press Escape to save your changes and exit the program.

# Daily Development Log

1/3/19:

Hilary: Created Sheet java file

David: Created test spreadsheet

1/4/19:

Hilary: Wrote generic Cell class

David: Working on Sheet constructor

1/6/19:

Hilary: Finished testing Cell class

1/7/19:

Hilary: Added to Sheet constructor but didn't finish

1/8/19:

Hilary: Fixed Sheet constructor, added methods to print columns and the entire table

1/9/19:

Hilary: Wrote longestInCol, worked on the spacing for printing the table

David: Working on constructor for proper String and Integer Cells; working on Adding Cells
>>>>>>> alternateConstructor

1/10/19:

Hilary: Wrote methods to change values in the sheet, added way to track selected cells

David: wrote findSum; merged alternateConstructor and master

1/12/19:

David: determined issue with Sheet toString;

Hilary: Wrote addRow, addCol, removeRow, and removeCol methods

1/13/19:

Hilary: Wrote save methods, created MenuDemo.java and worked on displaying sheet, highlighting cells, editing the table, and saving it in the terminal

1/14/19:

Hilary: Fixed bugs in the terminal display, wrote cols and highlightAll methods

1/15/19:

Hilary: Created selecting branch to work on selecting multiple cells

David: Working on using Screens instead of terminal itself; yet to figure out refreshing only once

1/17/19:

Hilary: Added ability to select cells directly next to current cell, edit all selected cells, and insert and delete rows

1/18/19:

Hilary: Merged selecting branch with master, made selecting multiple cells and inserting/deleting more user-friendly with booleans to track modes

1/19/19:

Hilary: Wrote sum and average functions for selected cells and added it to the terminal program, fixed bugs with clearScreen and in inserting/deleting rows and columns

David: Removed redundant user class in Terminal Branch; merged master and selecting; merged master and Terminal

1/20/19:

Hilary: Wrote max and min functions and added them to terminal

1/21/19:

Hilary: Changed getInt to return null if Cell was a string and fixed math functions; made updating the terminal with the sum, avg, max, and min automatic; added shifting rows up or down and cols left or right; added ability to create a new file by not giving an argument

David Xiedeng: Fixed selection overlap, fixed visual bug: col would remain the highlighted length of a deleted word after backspace
