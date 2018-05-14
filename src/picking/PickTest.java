package picking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;
import exception.UndefColorModelPair;

import java.util.ArrayList;

import orders.Order;

import org.junit.Test;

import shared.Request;
import shared.Worker;

/**
 * Unit-test cases for picking package.
 * 
 * @author zhengboj
 * @status Complete
 */
public class PickTest {
  
  @Test
  public void testPicker() {
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
      final Request request = new Request(lcOrderList);
      final Picker picker = new Picker("Jack");
      
      assertEquals(picker.status, Worker.Status.IDLE);
      picker.setReady();
      assertEquals(request.state, Request.State.PREPICK);
      assertEquals(picker.status, Worker.Status.READY);
      picker.consume(request);
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("8");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("7");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("6");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("5");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("4");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("3");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("2");
      assertEquals(request.state, Request.State.PICK);
      assertEquals(picker.status, Worker.Status.BUSY);
      picker.procFascia("1");
      assertEquals(request.state, Request.State.PRESEQUENCE);
      assertEquals(picker.status, Worker.Status.DONE);
      picker.toMarshaling();
      assertEquals(picker.status, Worker.Status.IDLE);
    } catch (Exception ex) {
      // Empty Block
    }
  }
  
  @Test
  public void testPicker_exUndefColorModelPair() {
    try {
      // Oops, incorrect color and model!
      new Order("UndefColor", "UndefModel");
    } catch (UndefColorModelPair exUndefColorModelPair) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testPicker_exIncorrectWorkerStatus1() {
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
      final Request request = new Request(lcOrderList);
      final Picker picker = new Picker("Jack");
      
      // Oops, Jack is not ready!
      picker.setReady();
      picker.consume(request);
      picker.procFascia("8");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testPicker_exIncorrectWorkerStatus2() {
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
      final Request request = new Request(lcOrderList);
      final Picker picker = new Picker("Jack");
      
      picker.setReady();
      picker.consume(request);
      picker.procFascia("8");
      
      // Oops, Jack is not ready to be sent to the marshaling area!
      picker.toMarshaling();
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testPicker_exIncorrectWorkerStatus3() {
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
      final Request request = new Request(lcOrderList);
      final Picker picker = new Picker("Jack");
      
      picker.setReady();
      picker.consume(request);
      picker.procFascia("8");
      // Oops, Jack is handling request and therefore cannot be set to ready!
      picker.setReady();
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testPicker_exIncorrectSku() {
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
      final Request request = new Request(lcOrderList);
      Picker picker = new Picker("Jack");
      
      picker.setReady();
      picker.consume(request);
      picker.procFascia("8");
      picker.procFascia("7");
      picker.procFascia("6");
      picker.procFascia("5");
      picker.procFascia("4");
      picker.procFascia("3");
      // Oops, Jack makes a mistake!
      picker.procFascia("9");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectSku);
    }
  }
}
