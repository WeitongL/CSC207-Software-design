package exception;

import java.lang.Exception;

/**
 * UndefColorModelPairException informs the user that 
 * the color-model pair is not defined in the translation table.
 * 
 * @author zhengboj
 * @status Complete
 */
public class UndefColorModelPair extends Exception {
  private static final long serialVersionUID = 0L;
  
  /**
   * Constructor.
   */
  public UndefColorModelPair(String msg) {
    super(msg);
  }
}
