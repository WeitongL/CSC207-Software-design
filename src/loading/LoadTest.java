package loading;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;

import java.util.ArrayList;

import orders.Order;

import org.junit.Test;

import shared.Request;
import shared.Worker;

public class LoadTest {

  @Test
  public void testLoad() {
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
      final Loader loader = new Loader("Jack");
      
      request.state = Request.State.PRELOAD;
      Request.resetGlobalId();
      request.resetId();
      
      assertEquals(loader.status, Worker.Status.IDLE);
      loader.setReady();
      assertEquals(request.state, Request.State.PRELOAD);
      assertEquals(loader.status, Worker.Status.READY);
      loader.consume(request);
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("1");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("2");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("3");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("4");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("5");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("6");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("7");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("8");
      assertEquals(request.state, Request.State.COMPLETE);
      assertEquals(loader.status, Worker.Status.IDLE);
    } catch (Exception ex) {
      // Empty Block
    }
  }
  
  @Test
  public void testLoad_exIncorrectSku() {
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
      final Loader loader = new Loader("Jack");
      
      request.state = Request.State.PRELOAD;
      Request.resetGlobalId();
      request.resetId();
      
      loader.setReady();
      // Oops, Jack makes a mistake!
      loader.consume(request);
      loader.procFascia("9");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectSku);
    }
  }
  
  @Test
  public void testLoad_exIncorrectWorkerStatus1() {
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
      final Loader loader = new Loader("Jack");
      
      request.state = Request.State.PRELOAD;
      Request.resetGlobalId();
      request.resetId();
      
      // loader.setReady();
      loader.consume(request);
      // Oops, Jack process a fascia when he is not ready!
      loader.procFascia("1");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testLoad_exIncorrectWorkerStatus2() {
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
      final Loader loader = new Loader("Jack");
      
      request.state = Request.State.PRELOAD;
      Request.resetGlobalId();
      request.resetId();
      
      // Oops, Jack tries to rescan when he is IDLE.
      loader.rescan();
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testLoad_rescan() {
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
      final Loader loader = new Loader("Jack");
      
      request.state = Request.State.PRELOAD;
      Request.resetGlobalId();
      request.resetId();
      
      assertEquals(loader.status, Worker.Status.IDLE);
      loader.setReady();
      assertEquals(request.state, Request.State.PRELOAD);
      assertEquals(loader.status, Worker.Status.READY);
      loader.consume(request);
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("1");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.rescan();
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("1");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("2");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("3");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("4");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("5");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("6");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("7");
      assertEquals(request.state, Request.State.LOAD);
      assertEquals(loader.status, Worker.Status.BUSY);
      loader.procFascia("8");
      assertEquals(request.state, Request.State.COMPLETE);
      assertEquals(loader.status, Worker.Status.IDLE);
      assertEquals(request.begin(), null);
      
      new Request(lcOrderList);
      final Request lcNextRequest = new Request(lcOrderList);
      
      Request.resetGlobalId();
      assertFalse(lcNextRequest.isNextRequest());
    } catch (Exception ex) {
      // Empty Block
    }
  }
}
