package replenishing;

/**
 * Replenisher picks up replenishing request.
 * 
 * @author zhuzhao2
 * @status Complete
 */
public class Replenisher {
  private String pvtName;
  
  /**
   * Constructor.
   */
  public Replenisher(String name) {
    this.pvtName = name;
  }
  
  /**
   * Take replenish request.
   */
  public void takeReplenishRequest(ReplenishReq argReplenishReq) {
    argReplenishReq.getLocation().replenish();
  }
  
  @Override
  public String toString() {
    return "Replenisher [pvtName=" + pvtName + "]";
  }
}
