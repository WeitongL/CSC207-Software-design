package shared;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;
import exception.UndefColorModelPair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import loading.Loader;
import orders.FaxMachine;
import orders.Order;
import picking.Picker;
import replenishing.ReplenishReq;
import replenishing.Replenisher;
import sequencing.Sequencer;
import warehouse.Warehouse;

/**
 * GlobalSystem is the whole system that monitors every behavior.
 * 
 * @author zhengboj
 * @status Complete
 */
public class GlobalSystem {
  
  private static final String pvtsfCsvFname = "./orders/orders.csv";
  //***********************************************************************************************
  private static final Logger  pvtsfLogger = Logger.getLogger(GlobalSystem.class.getName());
  private static       Handler pvtsFileHandler;
  
  static {
    pvtsFileHandler = null;
    // Construct a file handler for outputting all the info.
    try {
      pvtsFileHandler = new FileHandler("./log/" + GlobalSystem.class.getName() + ".log");
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
   * main.
   */
  public static void main(String[] args) {
    GlobalSystem.executeEventFile(args);
  }
  
  /**
   * Execute the event file.
   */
  public static void executeEventFile(String... args) {
    
    FaxMachine lcFaxMachine = new FaxMachine();
    
    Map<Worker, Worker> lcWorkerPool = new HashMap<>();
    List<Request> lcRequestPool = new ArrayList<>();
    List<ReplenishReq> lcReplenishReqPool = new ArrayList<>();
    
    BufferedReader lcBufferedReader = null;
    String line;
    
    try {
      
      lcBufferedReader = new BufferedReader(new FileReader(args[0]));
      
      while ((line = lcBufferedReader.readLine()) != null) {
        
        pvtsfLogger.log(Level.INFO, "Global system is parsing line \"" + line + "\".");
        
        String[] entry = line.split(" ");
        
        if (entry[0].equals("Order")) {
          
          pvtsfLogger.log(Level.INFO, "New order has been created in response to line " 
              + line + ".");
          try {
            Request request = lcFaxMachine.receiveOrder(new Order(entry[2], entry[1]));
          
            if (request != null) {
              lcRequestPool.add(request);
              pvtsfLogger.log(Level.INFO, "A new " + request  + " is created.");
            }
          } catch (UndefColorModelPair exUndefColorModelPair) {
            pvtsfLogger.log(Level.WARNING, "", exUndefColorModelPair);  
          }
        } // if (entry[0].equals("Order"))
        //*****************************************************************************************
        if (entry[0].equals("Picker")) {
          
          if (!lcWorkerPool.containsKey(new Picker(entry[1]))) {
            lcWorkerPool.put(new Picker(entry[1]), new Picker(entry[1]));
            pvtsfLogger.log(Level.INFO, "New picker has been created in response to line " 
                + line + ".");
          }
          
          Picker picker = (Picker) lcWorkerPool.get(new Picker(entry[1]));
          
          try {
            if (entry[2].equals("ready")) {
              picker.setReady();
              pvtsfLogger.log(Level.INFO, picker + " gets set to ready.");
            } else if (entry[2].equals("picks")) {
              picker.procFascia(entry[3]);
              pvtsfLogger.log(Level.INFO, picker + " picks SKU " + entry[3] + ".");
              
              ReplenishReq lcReplenishReq = Warehouse.procPickReq(entry[3]);
              if (lcReplenishReq != null) {
                lcReplenishReqPool.add(lcReplenishReq);
              }
            } else if (entry[2].equals("to")) {
              picker.toMarshaling();
              pvtsfLogger.log(Level.INFO, picker + " gets sent to marshaling area.");
            }
          } catch (IncorrectWorkerStatus exIncorrectWorkerStatus) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectWorkerStatus);
          } catch (IncorrectSku exIncorrectSku) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectSku);
          }
        } // if (entry[0].equals("Picker"))
        //*****************************************************************************************
        if (entry[0].equals("Sequencer")) {
          
          if (!lcWorkerPool.containsKey(new Sequencer(entry[1]))) {
            lcWorkerPool.put(new Sequencer(entry[1]), new Sequencer(entry[1]));
            pvtsfLogger.log(Level.INFO, "New sequencer has been created in response to line " 
                + line + ".");
          }
          
          Sequencer sequencer = (Sequencer) lcWorkerPool.get(new Sequencer(entry[1]));
          
          try {
            if (entry[2].equals("ready")) {
              sequencer.setReady();
              pvtsfLogger.log(Level.INFO, sequencer + " gets set to ready.");
            } else if (entry[2].equals("sequences")) {
              sequencer.procFascia(entry[3]);
              pvtsfLogger.log(Level.INFO, sequencer + " sequences SKU " + entry[3] + ".");
            } else if (entry[2].equals("rescans")) {
              sequencer.rescan();
              pvtsfLogger.log(Level.INFO, sequencer + " rescans.");
            }
          } catch (IncorrectWorkerStatus exIncorrectWorkerStatus) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectWorkerStatus);
          } catch (IncorrectSku exIncorrectSku) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectSku);
          }
        } // if (entry[0].equals("Sequencer"))
        //*****************************************************************************************
        if (entry[0].equals("Loader")) {
          
          if (!lcWorkerPool.containsKey(new Loader(entry[1]))) {
            lcWorkerPool.put(new Loader(entry[1]), new Loader(entry[1]));
            pvtsfLogger.log(Level.INFO, "New loader has been created in response to line " 
                + line + ".");
          }
          
          Loader loader = (Loader) lcWorkerPool.get(new Loader(entry[1]));
          
          try {
            if (entry[2].equals("ready")) {
              loader.setReady();
              pvtsfLogger.log(Level.INFO, loader + " gets set to ready.");
            } else if (entry[2].equals("loads")) {
              loader.procFascia(entry[3]);
              pvtsfLogger.log(Level.INFO, loader + " loads SKU " + entry[3] + ".");
            } else if (entry[2].equals("rescans")) {
              loader.rescan();
              pvtsfLogger.log(Level.INFO, loader + " rescans.");
            }
          } catch (IncorrectWorkerStatus exIncorrectWorkerStatus) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectWorkerStatus);
          } catch (IncorrectSku exIncorrectSku) {
            pvtsfLogger.log(Level.SEVERE, "", exIncorrectSku);
          }
        } // if (entry[0].equals("Loader"))
        //*****************************************************************************************
        if (entry[0].equals("Replenisher")) {
          Replenisher replenisher = new Replenisher(entry[1]);
          ReplenishReq lcReplenishReq = new ReplenishReq(entry[3].charAt(0), 
              Integer.parseInt(entry[4]), 
              Integer.parseInt(entry[5]), 
              Integer.parseInt(entry[6]));
          
          if (lcReplenishReqPool.contains(lcReplenishReq)) {
            
            ReplenishReq lcProcedReplenishReq = null;
            
            for (ReplenishReq iter : lcReplenishReqPool) {
              if (iter.equals(lcReplenishReq)) {
                replenisher.takeReplenishRequest(iter);
                pvtsfLogger.log(Level.INFO, lcReplenishReq 
                    + " is handled by replenisher "
                    + replenisher + ".");
                lcProcedReplenishReq = iter;
              }
            }
            final ReplenishReq lcfProcedReplenishReq = lcProcedReplenishReq;
              
            lcReplenishReqPool.removeIf(argReplenishReq -> 
                argReplenishReq.equals(lcfProcedReplenishReq));
          } else {
            pvtsfLogger.log(Level.WARNING, lcReplenishReq 
                + " does not belong to the outstanding replenish requests "
                + lcReplenishReqPool + ".");
          }
        } // if (entry[0].equals("Replenisher"))
        //*****************************************************************************************
        for (Map.Entry<Worker, Worker> worker : lcWorkerPool.entrySet()) {
          for (Request request : lcRequestPool) {
            worker.getValue().consume(request);
          }
        }
      }
    } catch (FileNotFoundException lcFileNotFoundException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot find the event file " + args[0] + ".",
          lcFileNotFoundException);
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file " + args[0] + " for events.",
          lcIoException);
    } finally {
      if (lcBufferedReader != null) {
        try {
          lcBufferedReader.close();
        } catch (IOException lcIoException) {
          pvtsfLogger.log(Level.SEVERE, "Cannot close the file " + args[0] + " for events.",
              lcIoException);
        }
      }
    }
    
    /**
     * Output requests that have been completed.
     */
    BufferedWriter lcBufferedWriter = null;
    
    try {
    
      lcBufferedWriter = new BufferedWriter(new FileWriter(pvtsfCsvFname));
      
      for (Request request : lcRequestPool) {
        if (request.state == Request.State.COMPLETE) {
          lcBufferedWriter.write(request + "\n");
        }
      }
    } catch (IOException lcIoException) {
      pvtsfLogger.log(Level.SEVERE, "Cannot open the file handler for reporting stock.",
          lcIoException);
    } finally {
      if (lcBufferedWriter != null) {
        try {
          lcBufferedWriter.close();
        } catch (IOException lcIoException) {
          pvtsfLogger.log(Level.SEVERE, "Cannot close the file handler for reporting stock.",
              lcIoException);
        }
      }
    }
    
    /**
     * Let the warehouse report the remaining stock.
     */
    Warehouse.reportStock();
  }
}
