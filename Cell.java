public class Cell<T> {
  private T value;

  public Cell(T start) {
    value = start;
  }

  public T getValue() {
    return value;
  }
}
