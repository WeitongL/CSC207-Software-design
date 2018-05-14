package shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import picking.Picker;
import sequencing.Sequencer;

/**
 * Unit-test cases for global system.
 * 
 * @author zhengboj
 * @status Complete
 */
public class GlobalSystemTest {
  
  @Test
  public void testWorker_equals() {
    
    final Worker picker = new Picker("Jack");
    final Worker sequencer = new Sequencer("Sue");
    
    final Worker lcNullPicker = new Picker(null);
    
    System.out.println(picker.hashCode());
    
    assertTrue(picker.equals(picker));
    assertFalse(picker.equals(null));
    assertFalse(picker.equals(lcNullPicker));
    assertFalse(picker.equals(sequencer));
    assertFalse(lcNullPicker.equals(picker));
  }
  
  @Test
  public void testGlobalSystem_order1() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order1.txt");
  }
  
  @Test
  public void testGlobalSystem_order2() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order2.txt");
  }
  
  @Test
  public void testGlobalSystem_order3() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order3.txt");
  }
  
  @Test
  public void testGlobalSystem_order4() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order4.txt");
  }
  
  @Test
  public void testGlobalSystem_order5() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order5.txt");
  }
  
  @Test
  public void testGlobalSystem_order6() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order6.txt");
  }
  
  @Test
  public void testGlobalSystem_order7() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order7.txt");
  }
  
  @Test
  public void testGlobalSystem_order8() {
    Request.resetGlobalId();
    GlobalSystem.executeEventFile("orders/order8.txt");
  }
}