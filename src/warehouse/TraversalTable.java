package warehouse;

import exception.InvalidTraversalTable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TraversalTable translates SKU to actual physical location.
 * 
 * @author zhengboj
 * @status Complete
 */
class TraversalTable extends HashMap<String, Location> {
  private static final long serialVersionUID = 0L;
  
  private static final String  pvtsfCsvFname = "./lut/traversal_table.csv";
  //***********************************************************************************************
  private static final Logger  pvtsfLogger = Logger.getLogger(TraversalTable.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + TraversalTable.class.getName() + ".log");
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
   * Constructor.
   */
  public TraversalTable() throws InvalidTraversalTable {
    
    super(new HashMap<String, Location>());
    
    // Read the CSV file and build up a hash table that 
    // relates the model-color pair with the SKU pair.
    String line;
    BufferedReader lcBufferedReader = null; 
    
    try {
      lcBufferedReader = new BufferedReader(new FileReader(pvtsfCsvFname));
      
      while ((line = lcBufferedReader.readLine()) != null) {
        // Under the condition that the line is valid, read the SKU and the Location.
        Location location = new Location(line);
        
        super.put(location.getSku(), location);
        pvtsfLogger.log(Level.INFO, "Entry " 
            + String.format("%5s", location.getSku()) + " w/ "
            + location + " is added to the traversal table.");
      }
    } catch (FileNotFoundException lcFileNotFoundException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + pvtsfCsvFname 
          + " for the traversal table: file DNE!", lcFileNotFoundException);
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + pvtsfCsvFname 
          + " for the traversal table.", lcIoException);
    } finally {
      if (lcBufferedReader != null) {
        try {
          lcBufferedReader.close();
        } catch (IOException lcIoException) {
          pvtsfLogger.log(Level.SEVERE, "Cannot close the file " + pvtsfCsvFname 
              + ".", lcIoException);
        }
      }
    }
  }
}
