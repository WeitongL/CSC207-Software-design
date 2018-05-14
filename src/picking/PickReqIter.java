package picking;

import orders.FaxMachine;
import shared.Request;
import shared.RequestIter;

/**
 * Iterator for picking request.
 * 
 * @author zhengboj
 * @status Complete
 */
public class PickReqIter extends RequestIter {

  public PickReqIter(Request request, int index) {
    super(request, index);
  }
  
  /**
   * Return the dereferenced SKU.
   */
  public String dereference() {
    return super.proRequest.getTraversalOrder().get(proIndex);
  }
  
  /**
   * Return the next picking request.
   */
  public RequestIter next() {
    
    if (super.proIndex == FaxMachine.MAX_ORDER_LIST_SIZE * 2 - 1) {
      return null;
    }
    return new PickReqIter(super.proRequest, proIndex + 1);
  }
}
