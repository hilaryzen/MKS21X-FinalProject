public class Driver {
  public static void main(String[] args) {
    Cell<String> cell1 = new Cell<String>("Hi");
    System.out.println(cell1.getValue());

    Cell<Integer> cell2 = new Cell<Integer>(5);
    System.out.println(cell2.getValue());
  }
}
