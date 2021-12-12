package ktso.course.work;

public class LenstraFactorizationContext {

  private int base;
  private int iterationCount;

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
