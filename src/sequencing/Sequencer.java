package sequencing;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;

import shared.Request;
import shared.Worker;

/**
 * Picker handles picking request.
 * 
 * @author luoweito
 * @status Complete
 */
public class Sequencer extends Worker {
  
  public Sequencer(String name) {
    super(name, Worker.Type.SEQUENCER);
  }
  
  /**
   * Take a sequencing request.
   */
  protected void takeRequest(Request request) {
    
    super.proRequest     = request;
    super.proRequestIter = request.begin();
    super.status = Worker.Status.BUSY;
    super.proRequest.state = Request.State.SEQUENCE;
  }
  
  /**
   * Consume a request.
   */
  public void consume(Request request) {
    
    if (status == Worker.Status.READY) {
      if (request.state == Request.State.PRESEQUENCE) {
        takeRequest(request);
      }
    }
  }
  
  /**
   * Process a SKU.
   */
  public void procFascia(String sku) throws IncorrectWorkerStatus, IncorrectSku {
    
    // Check the worker status.
    if (super.status != Worker.Status.BUSY) {
      throw new IncorrectWorkerStatus(this + " is unable to pick SKU " + sku + " at this time.");
    }
    
    // Check whether the SKU is correct or not.
    if (!super.proRequestIter.dereference().equals(sku)) {
      
      final IncorrectSku exIncorrectSku = new IncorrectSku(this 
          + " is trying to sequence SKU " + sku + " that does not match what is required: " 
          + super.proRequestIter.dereference() + ".");
      
      super.status = Worker.Status.IDLE;
      super.proRequest.state = Request.State.PREPICK;
      super.proRequest     = null;
      super.proRequestIter = null;
      throw exIncorrectSku;
    }
    
    super.proRequestIter = super.proRequestIter.next();
    
    if (super.proRequestIter == super.proRequest.end()) {
      super.status = Worker.Status.DONE;
      super.status = Worker.Status.IDLE;
      super.proRequest.state = Request.State.PRELOAD;
    }
  }
  
  /**
   * Rescan the entire picking request (newly added in Phase 2).
   */
  public void rescan() throws IncorrectWorkerStatus {
    
    // Check the worker status.
    if (super.status != Worker.Status.BUSY) {
      throw new IncorrectWorkerStatus(this + " is unable to handle the rescan request.");
    }
    
    super.proRequestIter = super.proRequest.begin();
  }
}
