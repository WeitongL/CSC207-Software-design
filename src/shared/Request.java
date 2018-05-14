package shared;

import java.util.ArrayList;
import java.util.List;

import loading.LoadReqIter;
import orders.Order;
import picking.PickReqIter;
import sequencing.SequenceReqIter;
import warehouse.Warehouse;

/**
 * Requests.
 * 
 * @author zhengboj
 * @status Complete
 */
public class Request extends ArrayList<String> {

  static final long serialVersionUID = 0L;
  
  //***********************************************************************************************
  public enum State {
    PREPICK, PICK, PRESEQUENCE, SEQUENCE, PRELOAD, LOAD, COMPLETE
  }
  
  public State state;
  //***********************************************************************************************
  private        long pvtThisId;
  private static long pvtsGlobalId = 0;
  private static long pvtsGlobalNextId = 0;
  //***********************************************************************************************
  // TraversalOrder is used only at the picking phase.
  private List<String> pvtTraversalOrder;
  //***********************************************************************************************
  
  /**
   * Constructor.
   */
  public Request(ArrayList<Order> argOrderList) {
    super();
    
    for (Order order : argOrderList) {
      super.add(order.getSkuPair().front());
      super.add(order.getSkuPair().back() );
    }
    state = State.PREPICK;
    pvtTraversalOrder = Warehouse.optimize(this);
    pvtThisId = pvtsGlobalId;
    ++pvtsGlobalId;
  }
  
  //***********************************************************************************************
  
  /**
   * C++-style iterator.
   */
    
  /**
   * begin.
   */
  public RequestIter begin() {
    
    if (state == State.PREPICK) {
      return new PickReqIter(this, 0);
    } else if (state == State.PRESEQUENCE || state == State.SEQUENCE) {
      return new SequenceReqIter(this, 0);
    } else if (state == State.PRELOAD || state == State.LOAD) {
      return new LoadReqIter(this, 0);
    } else {
      return null;
    }
  }
    
  /**
   * end.
   */
  public RequestIter end() {
    return null;
  }
  //***********************************************************************************************
  // ID-related Methods
  //***********************************************************************************************
  
  /**
   * Reset the request ID (used by unit-tests).
   */
  public void resetId() {
    pvtThisId = 0;
  }
  
  /**
   * Reset the global request ID (used by unit-tests).
   */
  public static void resetGlobalId() {
    pvtsGlobalId = 0;
    pvtsGlobalNextId = 0;
  }
  
  /**
   * Check whether this request is the next to be processed.
   */
  public boolean isNextRequest() {
    return pvtThisId == pvtsGlobalNextId;
  }
  
  /**
   * Increment the processed request.
   */
  public static void incrProcedId() {
    ++pvtsGlobalNextId;
  }
  //***********************************************************************************************
  
  /**
   * Get the traversal order.
   */
  public List<String> getTraversalOrder() {
    return pvtTraversalOrder;
  }
  //***********************************************************************************************

  /**
   * toString.
   */
  @Override
  public String toString() {
    return "Request [list=" + super.toString()
        + ", state=" + state 
        + ", pvtThisId=" + pvtThisId 
        + ", pvtTraversalOrder="
        + pvtTraversalOrder + "]";
  }
}
