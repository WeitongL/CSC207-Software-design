package orders;

import exception.InvalidTranslationTable;
import exception.UndefColorModelPair;

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
 * SKU Lookup Table.
 * 
 * @author zhengboj
 * @status Complete
 */
class SkuLut {
  private HashMap<ColorModelPair, SkuPair> pvtColorModelSkuPairMap = new HashMap<>();
  
  private static final String  pvtsfCsvFname = "./lut/translation.csv";
  //***********************************************************************************************
  private static final Logger  pvtsfLogger = Logger.getLogger(SkuLut.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + SkuLut.class.getName() + ".log");
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
  public SkuLut(String... args) throws InvalidTranslationTable {
    
    // Read the CSV file and build up a hash table that 
    // relates the model-color pair with the SKU pair.
    String line;
    BufferedReader lcBufferedReader = null; 
    
    try {
      
      lcBufferedReader = new BufferedReader(new FileReader(
          args.length == 0 ? pvtsfCsvFname : args[0]));
      
      // Read the 1st line and check its correctness.
      if (!(line = lcBufferedReader.readLine()).equals("Colour,Model,SKU (front),SKU (back)")) {
        throw new InvalidTranslationTable("1st row is not valid!");
      }
      
      while ((line = lcBufferedReader.readLine()) != null) {
        String[] entry = line.split(",");
        
        // The number of entries must be equal to 4.
        if (entry.length != 4) {
          throw new InvalidTranslationTable(line + " does not have 4 entries.");
        }
        // Under the condition that the line is valid, read the color, model, and the SKU pair.
        ColorModelPair lcColorModelPair = new ColorModelPair(entry[0], entry[1]); 
        SkuPair lcSkuPair = new SkuPair(entry[2], entry[3]);
        
        pvtColorModelSkuPairMap.put(lcColorModelPair, lcSkuPair);
        pvtsfLogger.log(Level.INFO, "Entry " + lcColorModelPair + " w/ SKU pair "
            + lcSkuPair + " is added to the translate table.");
      }
    } catch (FileNotFoundException lcFileNotFoundException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " 
          + (args.length == 0 ? pvtsfCsvFname : args[0]) 
          + " for the translation table: file DNE!", lcFileNotFoundException);
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " 
          + (args.length == 0 ? pvtsfCsvFname : args[0]) 
          + " for the translation table.", lcIoException);
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
  
  /**
   * Translates a color-model pair into a SKU pair.
   * 
   * @return SKU pair of the color and model (as is specified in the translation table pfCsvFname);
   */
  public SkuPair translate(String color, String model) throws UndefColorModelPair  {
    
    ColorModelPair lcColorModelPair = new ColorModelPair(color, model);
    
    if (pvtColorModelSkuPairMap.containsKey(lcColorModelPair)) {
      return pvtColorModelSkuPairMap.get(lcColorModelPair);
    } else {
      UndefColorModelPair lcUndefColorModelPairException = 
          new UndefColorModelPair("Cannot find the "
              + lcColorModelPair + " in the traversal table.");
      
      pvtsfLogger.log(Level.SEVERE, "", lcUndefColorModelPairException);
      throw lcUndefColorModelPairException;
    }
  }
}
