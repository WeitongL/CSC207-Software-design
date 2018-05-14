package exception;

import java.lang.Exception;

/**
 * WorkerStatusException informs the user that the worker is not available for certain operation.
 * 
 * @author zhengboj
 * @status Complete
 */
public class IncorrectWorkerStatus extends Exception {
  private static final long serialVersionUID = 0L;
  
  /**
   * Constructor.
   */
  public IncorrectWorkerStatus(String msg) {
    super(msg);
  }
}
