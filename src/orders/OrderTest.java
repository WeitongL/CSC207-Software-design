package orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import exception.InvalidTranslationTable;
import exception.UndefColorModelPair;

import java.util.ArrayList;
import org.junit.Test;

import shared.Request;

/**
 * Unit-test cases for order package.
 * 
 * @author zhengboj
 * @status Complete
 */
public class OrderTest {
  
  @Test
  public void testFaxMachine_receiveOrder() {
    FaxMachine lcFaxMachine = new FaxMachine();
    try {
      Order order1 = new Order("White", "S");
      Order order2 = new Order("White", "SE");
      Order order3 = new Order("White", "SES");
      Order order4 = new Order("White", "SEL");
      ArrayList<Order> lcOrderList = new ArrayList<>();
      lcOrderList.add(order1);
      lcOrderList.add(order2);
      lcOrderList.add(order3);
      lcOrderList.add(order4);
      final Request lcfPickReq = new Request(lcOrderList);
      
      assertEquals(lcFaxMachine.receiveOrder(order1), null);
      assertEquals(lcFaxMachine.receiveOrder(order2), null);
      assertEquals(lcFaxMachine.receiveOrder(order3), null);
      assertEquals(lcFaxMachine.receiveOrder(order4), lcfPickReq);
    } catch (Exception ex) {
      // Empty Block
    }
  }
  
  @Test
  public void testFaxMachine_receiveOrder_exUndefColorModelPair() {
    // FaxMachine lcFaxMachine = new FaxMachine();
    try {
      new Order("UndefColor", "UndefModel");
    } catch (Exception ex) {
      assertTrue(ex instanceof UndefColorModelPair);
    }
  }
  
  @Test
  public void testSkuLut_exInvalidTranslationTable1() {
    try {
      new SkuLut("./lut/invalid_translation1.csv");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTranslationTable);
    }
  }
  
  @Test
  public void testSkuLut_exInvalidTranslationTable2() {
    try {
      new SkuLut("./lut/invalid_translation2.csv");
    } catch (Exception ex) {
      assertTrue(ex instanceof InvalidTranslationTable);
    }
  }
  
  @Test
  public void testColorModelPair_hashCode_equals() {
    
    ColorModelPair lcColorModelPair = new ColorModelPair("color", "model");
    
    System.out.println(lcColorModelPair.hashCode());
    
    assertTrue(lcColorModelPair.equals(lcColorModelPair));
    assertFalse(lcColorModelPair.equals(null));
    assertFalse(lcColorModelPair.equals(0));
    
    ColorModelPair lcNullColorModelPair  = new ColorModelPair(null, null);
    ColorModelPair lcNullColorModelPair1 = new ColorModelPair(null, "model");
    ColorModelPair lcNullColorModelPair2 = new ColorModelPair("color", null);
    
    System.out.println(lcNullColorModelPair .hashCode());
    System.out.println(lcNullColorModelPair1.hashCode());
    System.out.println(lcNullColorModelPair2.hashCode());
    
    assertTrue(lcNullColorModelPair .equals(lcNullColorModelPair));
    assertFalse(lcNullColorModelPair .equals(lcColorModelPair));
    assertFalse(lcNullColorModelPair1.equals(lcColorModelPair));
    assertFalse(lcNullColorModelPair2.equals(lcColorModelPair));
    
    assertFalse(lcColorModelPair.equals(new ColorModelPair("diff_color", "model")));
    assertFalse(lcColorModelPair.equals(new ColorModelPair("color", "diff_model")));
  }
}