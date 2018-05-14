package warehouse;

import exception.InvalidTraversalTable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import replenishing.ReplenishReq;

/**
 * Warehouse monitors the picking process.
 * 
 * @author zhengboj
 * @status Complete
 */
public class Warehouse {
  private static final String pvtsfCsvFname = "./stock/final.csv";
  //***********************************************************************************************
  private static TraversalTable pvtsTraversalTable;
  
  private static final Logger  pvtsfLogger = Logger.getLogger(Warehouse.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + Warehouse.class.getName() + ".log");
      pvtsFileHandler.setLevel(Level.ALL);
      pvtsFileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file handler for logging.", lcIoException);
    }
    pvtsfLogger.setLevel(Level.ALL);
    pvtsfLogger.addHandler(pvtsFileHandler);
    
    // Initialize a private traversal table.
    pvtsTraversalTable = null;
    try {
      pvtsTraversalTable = new TraversalTable();
    } catch (InvalidTraversalTable lcInvalidTraversalTableException) {
      /* pvtsfLogger.log(Level.SEVERE, "The traversal table is not valid.", 
          lcInvalidTraversalTableException); */
    }
  }
  //***********************************************************************************************

  /**
   * Process a picking request.
   */
  public static ReplenishReq procPickReq(String sku) {
    
    Location location = pvtsTraversalTable.get(sku);
      
    pvtsfLogger.log(Level.INFO, location 
        + " should provide the supply per picking request.");
    location.decrStock();
    
    if (location.getStock() <= 5) {
      ReplenishReq lcReplenishReq = new ReplenishReq(location);
      
      pvtsfLogger.log(Level.INFO, "A new " + lcReplenishReq + " has been created.");
      return lcReplenishReq;
    } else {
      return null;
    }
  }
  
  /**
   * Generate the final.csv.
   */
  public static void reportStock() {
    BufferedWriter lcBufferedWriter = null;
    
    try {
      lcBufferedWriter = new BufferedWriter(new FileWriter(pvtsfCsvFname));
      
      for (Map.Entry<String, Location> iter : pvtsTraversalTable.entrySet()) {
        String lcSupplyString = iter.getValue().toSupplyStr();
        
        if (lcSupplyString != null) {
          lcBufferedWriter.write(lcSupplyString + "\n");
        }
      }
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + pvtsfCsvFname 
          + " for the final supply table.", lcIoException);
    } finally {
      if (lcBufferedWriter != null) {
        try {
          lcBufferedWriter.close();
        } catch (IOException lcIoException) {
          pvtsfLogger.log(Level.SEVERE, "Cannot close the file " + pvtsfCsvFname 
              + ".", lcIoException);
        }
      }
    }
  }
  
  /**
   * Provide an optimized path based on the provided SKU numbers.
   */
  public static List<String> optimize(List<String> skus) {
    
    List<String> retLocation = new ArrayList<>();
    
    for (String iter : skus) {
      
      Location location = pvtsTraversalTable.get(iter);
        
      retLocation.add(0, location.getSku());
      pvtsfLogger.log(Level.INFO, "The system suggested " 
          + location + " for SKU " + String.format("%5s", iter) + ".");
    }
    return retLocation;
  }
  
  /**
   * Find the location of a given SKU.
   */
  public static Location findLocation(String sku) {
    return pvtsTraversalTable.get(sku);
  }
}
