package sequencing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import exception.IncorrectSku;
import exception.IncorrectWorkerStatus;
import java.util.ArrayList;

import orders.Order;

import org.junit.Test;

import shared.Request;
import shared.Worker;

/**
 * Unit-test cases for sequencing package.
 * 
 * @author luoweito
 * @status Complete
 */
public class SequenceTest {

  @Test
  public void testSequencer() {
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
      final Sequencer sequencer = new Sequencer("Jack");

      request.state = Request.State.PRESEQUENCE;

      assertEquals(sequencer.status, Worker.Status.IDLE);
      sequencer.setReady();
      assertEquals(request.state, Request.State.PRESEQUENCE);
      assertEquals(sequencer.status, Worker.Status.READY);
      sequencer.consume(request);
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("1");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("2");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("3");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("4");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("5");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("6");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("7");
      assertEquals(request.state, Request.State.SEQUENCE);
      assertEquals(sequencer.status, Worker.Status.BUSY);
      sequencer.procFascia("8");
      assertEquals(request.state, Request.State.PRELOAD);
      assertEquals(sequencer.status, Worker.Status.IDLE);

    } catch (Exception ex) {
      // Empty Block
    }
  }

  @Test
  public void testSequencer_exIncorrectWorkerStatus1() {
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
      final Sequencer sequencer = new Sequencer("Jack");

      request.state = Request.State.PRESEQUENCE;

      // Oops, Jack is not ready!
      // sequencer.setReady();
      sequencer.consume(request);
      sequencer.procFascia("1");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }

  @Test
  public void testSequencer_exIncorrectWorkerStatus2() {
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
      final Sequencer sequencer = new Sequencer("Jack");

      request.state = Request.State.PRESEQUENCE;

      // Oops, Jack tries to rescan when he is IDLE.
      sequencer.rescan();
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectWorkerStatus);
    }
  }
  
  @Test
  public void testSequencer_exIncorrectSku() {
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
      final Sequencer sequencer = new Sequencer("Jack");

      request.state = Request.State.PRESEQUENCE;

      sequencer.setReady();
      sequencer.consume(request);
      // Oops, Jack makes a mistake!
      sequencer.procFascia("9");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectSku);
    }
  }

  @Test
  public void testSequencer_rescan() {
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
      final Sequencer sequencer = new Sequencer("Jack");

      request.state = Request.State.PRESEQUENCE;

      sequencer.setReady();
      sequencer.consume(request);
      sequencer.procFascia("1");
      sequencer.procFascia("2");
      sequencer.procFascia("3");
      sequencer.rescan();
      sequencer.procFascia("3");
    } catch (Exception ex) {
      assertTrue(ex instanceof IncorrectSku);
    }
  }
}
