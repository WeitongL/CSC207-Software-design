package warehouse;

import exception.InvalidTraversalTable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Location records the location, SKU, and in-stock number.
 * 
 * @author zhengboj
 * @status Complete
 */
public class Location {
  private char pvtZone ;
  private int  pvtAisle;
  private int  pvtRack ;
  private int  pvtLevel;
  
  private String pvtSku;
  
  private static final int MAX_STOCK = 30;
  private int pvtStock = MAX_STOCK;
  
  private static final String  pvtsfCsvFname = "./stock/initial.csv";
  //***********************************************************************************************
  private static final Logger  pvtsfLogger = Logger.getLogger(Location.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + Location.class.getName() + ".log");
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
  public Location(char zone, int aisle, int rack, int level) {
    this.pvtZone  = zone ;
    this.pvtAisle = aisle;
    this.pvtRack  = rack ;
    this.pvtLevel = level;
  }
  
  /**
   * Constructor.
   */
  public Location(String lcTraversalStr) throws InvalidTraversalTable {
    String[] lcTraversalEntry = lcTraversalStr.split(",");
    
    // The number of entries must be equal to 5.
    if (lcTraversalEntry.length != 5) {
      throw new InvalidTraversalTable(lcTraversalStr + " does not have 5 entries.");
    }
    pvtZone = lcTraversalEntry[0].charAt(0);
    if (pvtZone < 'A' || pvtZone > 'Z') {
      throw new InvalidTraversalTable(lcTraversalStr + " has an invalid zone.");
    }
    try {
      pvtAisle  = Integer.parseInt(lcTraversalEntry[1]);
      pvtRack   = Integer.parseInt(lcTraversalEntry[2]);
      pvtLevel  = Integer.parseInt(lcTraversalEntry[3]);
      pvtSku    = lcTraversalEntry[4];

      String lcSupplyStr;
      BufferedReader lcBufferedReader = null; 
      
      try {
        lcBufferedReader = new BufferedReader(new FileReader(pvtsfCsvFname));
        
        while ((lcSupplyStr = lcBufferedReader.readLine()) != null) {
          String[] lcSupplyEntry = lcSupplyStr.split(",");
          
          if (   lcSupplyEntry[0].equals(lcTraversalEntry[0]) 
              && lcSupplyEntry[1].equals(lcTraversalEntry[1]) 
              && lcSupplyEntry[2].equals(lcTraversalEntry[2])
              && lcSupplyEntry[3].equals(lcTraversalEntry[3])) {
            pvtStock = Integer.parseInt(lcSupplyEntry[4]);
          }
        }
      } catch (FileNotFoundException lcFileNotFoundException) {
        pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + pvtsfCsvFname 
            + " for the initial supply table: file DNE!", lcFileNotFoundException);
      } catch (IOException lcIoException) {
        pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + pvtsfCsvFname 
            + " for the initial supply table.", lcIoException);
      } catch (NumberFormatException lcNumberFormatException) {
        pvtsfLogger.log(Level.SEVERE, "Invalid SKU supply in file " + pvtsfCsvFname 
            + " for the initial supply table.", lcNumberFormatException);
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
    } catch (NumberFormatException lcNumberFormatException) {
      throw new InvalidTraversalTable(lcTraversalStr 
          + " has an invalid aisle/rack/level.");
    }
  }
  //***********************************************************************************************
  
  /**A,0,0,0,1
   * Get the SKU.
   */
  String getSku() {
    return pvtSku;
  }
  //***********************************************************************************************
  // Stock-Related Methods.
  //***********************************************************************************************
  
  /**
   * Set the stock number.
   */
  void decrStock() {
    --pvtStock;
    pvtsfLogger.log(Level.INFO,
        this + "'s stock has been reduced by 1 in response to picking request.");
  }
  
  /**
   * Get the stock number.
   */
  int getStock() {
    return pvtStock;
  }
  
  /**
   * Replenish the stock.
   */
  public void replenish() {
    pvtsfLogger.log(Level.INFO,
        this + "'s stock has been replenished in response to replenish request.");
    pvtStock = MAX_STOCK;
  }
  //***********************************************************************************************
  // Conversion to CSV-String
  //***********************************************************************************************
  
  /**
   * Convert to CSV-string: location + stock.
   */
  String toSupplyStr() {
    if (pvtStock == MAX_STOCK) {
      return null;
    } else {
      return pvtZone + "," + pvtAisle + "," + pvtRack + "," + pvtLevel + "," + pvtStock;
    }
  }
  
  //***********************************************************************************************
  // Auto-Generated Methods
  //***********************************************************************************************
  
  /**
   * equals.
   *
   * @param1 zone
   * @param2 aisle
   * @param3 rack
   * @param4 level
   */
  
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
    Location other = (Location) obj;
    if (pvtAisle != other.pvtAisle) {
      return false;
    }
    if (pvtLevel != other.pvtLevel) {
      return false;
    }
    if (pvtRack != other.pvtRack) {
      return false;
    }
    if (pvtZone != other.pvtZone) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return String.format("Location[SKU=%5s, Zone=%3c, Aisle=%3d, Rack=%3d, Level=%3d, stock=%3d]", 
        pvtSku, pvtZone, pvtAisle, pvtRack, pvtLevel, pvtStock);
  }
}
