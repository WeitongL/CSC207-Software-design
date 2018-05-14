package exception;

import java.lang.Exception;

/**
 * InvalidTraversalTableException informs the user that the translation table is not valid.
 * 
 * @author zhengboj
 * @status Complete
 */
public class InvalidTraversalTable extends Exception {
  static final long serialVersionUID = 0L;
  
  /**
   * Constructor.
   */
  public InvalidTraversalTable(String msg) {
    super(msg);
  }
}
