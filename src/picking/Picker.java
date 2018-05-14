package picking;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import shared.Request;
import shared.Worker;
import warehouse.Location;
import warehouse.Warehouse;

/**
 * Picker handles picking request.
 * 
 * @author zhengboj
 * @status Complete
 */
public class Picker extends Worker {
  
  //***********************************************************************************************
  private static final Logger  pvtsfLogger = Logger.getLogger(Picker.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + Picker.class.getName() + ".log");
      pvtsFileHandler.setLevel(Level.ALL);
      pvtsFileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file handler for logging.", lcIoException);
    }
    pvtsfLogger.setLevel(Level.ALL);
    pvtsfLogger.addHandler(pvtsFileHandler);
  }
  //***********************************************************************************************

  public Picker(String name) {
    super(name, Worker.Type.PICKER);
    pvtsfLogger.log(Level.INFO, "A new " + this + " has been created.");
  } 
  
  /**
   * Take a picking request.
   */
  protected void takeRequest(Request request) {
    
    super.proRequest     = request;
    super.proRequestIter = request.begin();
    super.status = Worker.Status.BUSY;
    super.proRequest.state = Request.State.PICK;
    pvtsfLogger.log(Level.INFO, "System is suggesting picker to goto " 
        + Warehouse.findLocation(super.proRequestIter.dereference()) 
        + " for the next fascia.");
  }
  
  /**
   * Consume a request.
   */
  public void consume(Request request) {
    
    if (status == Worker.Status.READY) {
      if (request.state == Request.State.PREPICK) {
        this.takeRequest(request);
        pvtsfLogger.log(Level.INFO, this + " has consumed " + request + ".");
      }
    }
  }
  
  /**
   * Process a SKU.
   */
  public void procFascia(String sku) throws IncorrectWorkerStatus, IncorrectSku {
    
    // Check the worker status.
    if (super.status != Worker.Status.BUSY) {
      
      IncorrectWorkerStatus exIncorrectWorkerStatus = new IncorrectWorkerStatus(this 
          + " is unable to pick SKU " + sku + " at this time.");
      pvtsfLogger.log(Level.SEVERE, "", exIncorrectWorkerStatus);
      throw exIncorrectWorkerStatus;
    }
    
    // Check whether the SKU is correct or not.
    if (!super.proRequestIter.dereference().equals(sku)) {
      
      IncorrectSku exIncorrectSku = new IncorrectSku(this + " is trying to pick up SKU " + sku 
          + " that does not match what is required: " 
          + super.proRequestIter.dereference() + ".");
      pvtsfLogger.log(Level.SEVERE, "", exIncorrectSku);
      throw exIncorrectSku;
    }
    
    super.proRequestIter = super.proRequestIter.next();
    
    if (super.proRequestIter == super.proRequest.end()) {
      
      super.status = Worker.Status.DONE;
      super.proRequest.state = Request.State.PRESEQUENCE;
      pvtsfLogger.log(Level.INFO, this + " has finished the picking request " + super.proRequest);
    } else {
      
      Location location = Warehouse.findLocation(super.proRequestIter.dereference());
      
      pvtsfLogger.log(Level.INFO, "System is suggesting picker to goto " + location 
          + " for the next fascia.");
    }
  }
  
  /**
   * Goto marshaling area.
   */
  public void toMarshaling() throws IncorrectWorkerStatus {
    
    if (super.status != Worker.Status.DONE) {
      
      IncorrectWorkerStatus exIncorrectWorkerStatus = new IncorrectWorkerStatus(this 
          + " is unable to go to the marshaling area.");
      pvtsfLogger.log(Level.SEVERE, "", exIncorrectWorkerStatus);
      throw exIncorrectWorkerStatus;
    }
    super.status = Worker.Status.IDLE;
    pvtsfLogger.log(Level.INFO, this + " has been sent to marshaling area.");
  }
}
