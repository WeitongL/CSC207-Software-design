package replenishing;

import warehouse.Location;

/**
 * ReplenishReq is generated when the stock of a certain SKU reaches below a certain level.
 * 
 * @author zhuzhao2
 * @status Complete
 */
public class ReplenishReq {
  Location pvtLocation;
  
  /**
   * Constructor.
   */
  public ReplenishReq(char zone, int aisle, int rack, int level) {
    pvtLocation = new Location(zone, aisle, rack, level);
  }
  
  /**
   * Constructor.
   */
  public ReplenishReq(Location location) {
    pvtLocation = location;
  }
  
  /**
   * Get the location.
   */
  public Location getLocation() {
    return pvtLocation;
  }
  
  /**
   * equals.
   * 
   * @param1 location
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ReplenishReq other = (ReplenishReq) obj;
    if (pvtLocation == null) {
      if (other.pvtLocation != null) {
        return false;
      }
    } else if (!pvtLocation.equals(other.pvtLocation)) {
      return false;
    }
    return true;
  }
  
  /**
   * toString method.
   */
  @Override
  public String toString() {
    return "ReplenishReq [pvtLocation=" + pvtLocation + "]";
  }
}
