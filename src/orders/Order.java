package orders;

import exception.InvalidTranslationTable;
import exception.UndefColorModelPair;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Order is constructed using model and color name and stores the SKU pair. 
 * 
 * @author zhengboj
 * @statuc Complete
 */
public class Order {
  private SkuPair pvtSkuPair;
  //***********************************************************************************************
  private static SkuLut pvtsSkuLut;
 
  private static final Logger  pvtsfLogger = Logger.getLogger(Order.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + Order.class.getName() + ".log");
      pvtsFileHandler.setLevel(Level.ALL);
      pvtsFileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file handler for logging.", lcIoException);
    }
    pvtsfLogger.setLevel(Level.ALL);
    pvtsfLogger.addHandler(pvtsFileHandler);
    
    // Initialize a private look-up table.
    pvtsSkuLut = null;
    try {
      pvtsSkuLut = new SkuLut();
    } catch (InvalidTranslationTable exInvalidTranslationTable) {
      // pvtsfLogger.log(Level.SEVERE, "", exInvalidTranslationTable);
    }
  }
  //***********************************************************************************************
  
  /**
   * Constructor.
   */
  public Order(String color, String model) throws UndefColorModelPair {
    try {
      pvtSkuPair = pvtsSkuLut.translate(color, model);
      pvtsfLogger.log(Level.INFO, "A new " + pvtSkuPair + " has been created.");
    } catch (UndefColorModelPair exUndefColorModelPair) {
      pvtsfLogger.log(Level.SEVERE, "", exUndefColorModelPair);
      throw exUndefColorModelPair;
    }
  }
  
  /**
   * Get the SKU pair.
   */
  public SkuPair getSkuPair() {
    return pvtSkuPair;
  }
  
  /**
   * toString.
   */
  @Override
  public String toString() {
    return "Order [pvtSkuPair=" + pvtSkuPair + "]";
  }
}

