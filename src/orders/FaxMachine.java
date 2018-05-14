package orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import shared.Request;

/**
 * Fax Machine receives, stores, and finally transmits the order.
 * 
 * @author zhengboj
 * @status Complete
 */
public class FaxMachine {
  private ArrayList<Order> pvtOrderList = new ArrayList<>();
  
  public static final int MAX_ORDER_LIST_SIZE = 4;
  //***********************************************************************************************
  private static final Logger  pvtsfLogger  = Logger.getLogger(FaxMachine.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + FaxMachine.class.getName() + ".log");
      pvtsFileHandler.setLevel(Level.ALL);
      pvtsFileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file handler for logging.", lcIoException);
    }
    pvtsfLogger.setLevel(Level.ALL);
    pvtsfLogger.addHandler(pvtsFileHandler);
  }
  //***********************************************************************************************
  
  /**
   * Receive an order from the outside system.
   * 
   * @return If the list size reaches the MAX_ORDER_LIST_SIZE, 
   *         a list of orders is returned; Otherwise, return null.
   */
  public Request receiveOrder(Order order) {
    
    // Add the order to the order list.
    pvtOrderList.add(order);
    pvtsfLogger.log(Level.INFO, "A new " + order + " is received in the fax machine.");
    
    // Return a copy of the order list once it is full.
    if (pvtOrderList.size() == MAX_ORDER_LIST_SIZE) {
      
      Request retRequest = new Request(pvtOrderList);
      pvtsfLogger.log(Level.INFO, "A new " + retRequest + " is created.");
      
      pvtOrderList.clear();
      return retRequest;
    } else {
      return null;
    }
  }
}
