package ktso.course.work;

/**
 * контекст для хранения данных, необходимых для факторизации Ленстры
 */
public class LenstraFactorizationContext {

  private int base;
  private int iterationCount;

  /**
   * Конструктор
   * @param base - предел нахождения простых чисел
   * @param iterationCount - число итераций выполнения алгоритма
   */
  public LenstraFactorizationContext(int base, int iterationCount) {
    this.base = base;
    this.iterationCount = iterationCount;
  }

  public int getBase() {
    return base;
  }

  public void setBase(int base) {
    this.base = base;
  }

  public int getIterationCount() {
    return iterationCount;
  }

  public void setIterationCount(int iterationCount) {
    this.iterationCount = iterationCount;
  }
}
