package sequencing;

import orders.FaxMachine;
import shared.Request;
import shared.RequestIter;

/**
 * Iterator for sequencing request.
 * 
 * @author luoweito
 * @status Complete
 */
public class SequenceReqIter extends RequestIter {

  public SequenceReqIter(Request request, int index) {
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
    return new SequenceReqIter(super.proRequest, super.proIndex + 1);
  }
}
