package exception;

import java.lang.Exception;

/**
 * OrderMismatchException informs the user that 
 * there is a mismatch in previously and currently stored orders.
 * 
 * @author zhengboj
 * @status Complete
 */
public class IncorrectSku extends Exception {
  static final long serialVersionUID = 0L;
  
  /**
   * Constructor.
   */
  public IncorrectSku(String msg) {
    super(msg);
  }
}
