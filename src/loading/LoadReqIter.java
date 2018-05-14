package loading;

import orders.FaxMachine;
import shared.Request;
import shared.RequestIter;

/**
 * Iterator for loading request.
 * 
 * @author luoweito (Phase 1), huzhuoyi(Phase 2)
 * @status Complete
 */
public class LoadReqIter extends RequestIter {
  
  public LoadReqIter(Request request, int index) {
    super(request, index);
  }
  
  /**
   * Return the dereferenced SKU.
   */
  public String dereference() {
    return super.proRequest.get(proIndex);
  }
  
  /**
   * Return the next picking request.
   */
  public RequestIter next() {
    
    if (super.proIndex == FaxMachine.MAX_ORDER_LIST_SIZE * 2 - 1) {
      return null;
    }
    return new LoadReqIter(super.proRequest, super.proIndex + 1);
  }
}
