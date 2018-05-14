package shared;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;

public abstract class Worker {
  
  protected String proName;
  
  public enum Type {
    PICKER, SEQUENCER, LOADER
  }
  
  protected Type proType;
  //***********************************************************************************************
  
  public enum Status {
    IDLE, READY, BUSY, DONE
  }
  
  public Status status;

  protected Request     proRequest;
  protected RequestIter proRequestIter;
  //***********************************************************************************************
  
  /**
   * Constructor.
   */
  public Worker(String name, Type type) {
    proName = name;
    proType = type;
    status = Status.IDLE;
  }
  //***********************************************************************************************
  // Event-Related Methods.
  //***********************************************************************************************
  
  /**
   * Set the worker status to ready.
   */
  public void setReady() throws IncorrectWorkerStatus {
    
    // Worker's status must be IDLE in order for setReady to be successful.
    if (status != Status.IDLE) {
      throw new IncorrectWorkerStatus(this 
          + " is unable to have its status set to READY.");
    }
    
    status = Status.READY;
  }
  
  /**
   * Take a request.
   */
  protected abstract void takeRequest(Request request);
  
  /**
   * Consume a request.
   */
  public abstract void consume(Request request);
  
  /**
   * Process a SKU.
   */
  public abstract void procFascia(String sku) throws IncorrectWorkerStatus, IncorrectSku;
  //***********************************************************************************************
  // Auto-Generated Methods.
  //***********************************************************************************************
  
  /**
   * hashCode and equals.
   * 
   * @param1 name
   * @param2 type
   */
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((proName == null) ? 0 : proName.hashCode());
    result = prime * result + ((proType == null) ? 0 : proType.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Worker other = (Worker) obj;
    if (proName == null) {
      if (other.proName != null) {
        return false;
      }
    } else if (!proName.equals(other.proName)) {
      return false;
    }
    /*
    if (proType != other.proType) {
      return false;
    }
     */
    return true;
  }

  @Override
  public String toString() {
    return "Worker [proName=" + String.format("%10s", proName) + ", proType=" + proType 
        + ", proStatus=" + status + ", proRequest=" + proRequest 
        + ", proRequestIter=" + (proRequestIter == null ? "" : proRequestIter.dereference()) + "]";
  }
}
