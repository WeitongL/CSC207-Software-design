package orders;

/**
 * ColorModelPair pairs the model and color.
 * 
 * @author zhengboj
 * @status Complete
 */
class ColorModelPair {
  private String pvtColor;
  private String pvtModel;
  
  /**
   * Constructor.
   */
  public ColorModelPair(String color, String model) {
    this.pvtColor = color;
    this.pvtModel = model;
  }
  
  /**
   * hashCode.
   * 
   * @param1 color
   * @param2 model
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    
    result = prime * result + ((pvtColor == null) ? 0 : pvtColor.hashCode());
    result = prime * result + ((pvtModel == null) ? 0 : pvtModel.hashCode());
    return result;
  }
  
  /**
   * equals.
   * 
   * @param1 color
   * @param2 model
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
    ColorModelPair other = (ColorModelPair) obj;
    if (pvtColor == null) {
      if (other.pvtColor != null) {
        return false;
      }
    } else if (!pvtColor.equals(other.pvtColor)) {
      return false;
    }
    if (pvtModel == null) {
      if (other.pvtModel != null) {
        return false;
      }
    } else if (!pvtModel.equals(other.pvtModel)) {
      return false;
    }
    return true;
  }
  
  /**
   * toString.
   */
  @Override
  public String toString() {
    return "ColorModelPair [pvtColor=" + String.format("%10s", pvtColor) 
        + ", pvtModel=" + String.format("%5s", pvtModel) + "]";
  }
}
