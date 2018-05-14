package orders;

/**
 * SkuPair stores the front and back SKU number.
 * 
 * @author luoweito & zhengboj
 * @status Complete
 */
public class SkuPair {
  private String pvtSkuFront;
  private String pvtSkuBack ;
  
  /**
   * Constructor.
   */
  public SkuPair(String argSkuFront, String argSkuBack) {
    this.pvtSkuFront = argSkuFront;
    this.pvtSkuBack  = argSkuBack ;
  }
  
  /**
   * Get the front SKU number.
   */
  public String front() {
    return pvtSkuFront;
  }
  
  /**
   * Get the back  SKU number.
   */
  public String back()  {
    return pvtSkuBack ;
  }
  
  /**
   * toString.
   */
  @Override
  public String toString() {
    return "SkuPair [pvtSkuFront=" + String.format("%5s", pvtSkuFront) 
        + ", pvtSkuBack=" + String.format("%5s", pvtSkuBack) + "]";
  }
}
