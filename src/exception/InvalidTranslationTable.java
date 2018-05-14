package exception;

import java.lang.Exception;

/**
 * InvalidTranslationTableException informs the user that the translation table is not valid.
 * 
 * @author zhengboj
 * @status Complete
 */
public class InvalidTranslationTable extends Exception {
  static final long serialVersionUID = 0L;
  
  /**
   * Constructor.
   */
  public InvalidTranslationTable(String msg) {
    super(msg);
  }
}
