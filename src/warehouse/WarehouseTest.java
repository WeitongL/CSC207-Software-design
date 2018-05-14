package warehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import exception.InvalidTraversalTable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import replenishing.ReplenishReq;

/**
 * Unit-test cases for order package.
 * 
 * @author zhengboj
 * @status Complete
 */
public class WarehouseTest {

  @Test
  public void testWarehouse_procPickReq() {
    
    ReplenishReq lcReplenishReq = new ReplenishReq('B', 0, 0, 0);
    
    assertEquals(Warehouse.procPickReq( "6"), null);
    assertEquals(Warehouse.procPickReq("19"), null);
    assertEquals(Warehouse.procPickReq("25"), lcReplenishReq);
  }
  
  @Test
  public void testWarehouse_reportStock() {
    
    Warehouse.procPickReq( "6");
    Warehouse.procPickReq("19");
    Warehouse.procPickReq("19");
    Warehouse.procPickReq( "2");
    Warehouse.reportStock();
  }
  
  @Test
  public void testWarehouse_optimize() {
    
    List<String> skus = new ArrayList<>();
    
    skus.add("1");
    skus.add("3");
    skus.add("5");
    skus.add("7");
    skus.add("8");
    skus.add("6");
    skus.add("4");
    skus.add("2");
    
    List<String> lcTraversalOrder = Warehouse.optimize(skus);
    
    assertEquals(lcTraversalOrder.get(0), "2");
    assertEquals(lcTraversalOrder.get(1), "4");
    assertEquals(lcTraversalOrder.get(2), "6");
    assertEquals(lcTraversalOrder.get(3), "8");
    assertEquals(lcTraversalOrder.get(4), "7");
    assertEquals(lcTraversalOrder.get(5), "5");
    assertEquals(lcTraversalOrder.get(6), "3");
    assertEquals(lcTraversalOrder.get(7), "1");
  }
  
  @Test
  public void testLocation_exInvalidTraversalTable1() {
    
    try {
      new Location("A,0,0,0");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTraversalTable);
    }
  }
  
  @Test
  public void testLocation_exInvalidTraversalTable2() {
    
    try {
      new Location("0,0,0,0,1");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTraversalTable);
    }
  }
  
  @Test
  public void testLocation_exInvalidTraversalTable3() {
    
    try {
      new Location("A,:),0,0,1");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTraversalTable);
    }
  }
  
  @Test
  public void testLocation_exInvalidTraversalTable4() {
    
    try {
      new Location("A,0,0,0,:)");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTraversalTable);
    }
  }
  
  @Test
  public void testLocation_equals() {
    
    try {
      Location location = new Location("A,0,0,0,1");
      
      assertTrue(location.equals(location));
      assertFalse(location.equals(null));
      assertFalse(location.equals(0));
    } catch (Exception ex) {
      // Empty Block.
    }
  }
}
