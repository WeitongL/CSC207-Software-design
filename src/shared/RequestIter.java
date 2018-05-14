package shared;

/**
 * C++-style iterator.
 * 
 * @author zhengboj
 * @status Complete
 */
public abstract class RequestIter {

  protected Request proRequest;
  protected int          proIndex;
  
  public RequestIter(Request request, int index) {
    proRequest = request;
    proIndex   = index  ;
  }
  //***********************************************************************************************
  
  /**
   * C++-style iterator.
   */
  
  public abstract String dereference();

  public abstract RequestIter next();
}
