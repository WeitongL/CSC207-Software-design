package replenishing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import warehouse.Location;

public class ReplenishTest {

  @Test
  public void testReplenish() {
    Location location = new Location('A', 0, 0, 0);
    Replenisher replenisher = new Replenisher("Jack");
    ReplenishReq lcReplenishReq = new ReplenishReq(location);

    System.out.println(replenisher);
    
    replenisher.takeReplenishRequest(lcReplenishReq);
  }
  
  @Test
  public void testReplenish_equals() {
    
    ReplenishReq lcReplenishReq = new ReplenishReq('A', 0, 0, 0);
    
    assertTrue(lcReplenishReq.equals(lcReplenishReq));
    assertFalse(lcReplenishReq.equals(null));
    assertFalse(lcReplenishReq.equals(0));
    
    ReplenishReq lcNullReplenishReq = new ReplenishReq(null);
    
    assertFalse(lcNullReplenishReq.equals(lcReplenishReq));
    
    assertFalse(lcReplenishReq.equals(new ReplenishReq(new Location('B', 0, 0, 0))));
    assertFalse(lcReplenishReq.equals(new ReplenishReq(new Location('A', 1, 0, 0))));
    assertFalse(lcReplenishReq.equals(new ReplenishReq(new Location('A', 0, 1, 0))));
    assertFalse(lcReplenishReq.equals(new ReplenishReq(new Location('A', 0, 0, 1))));
  }
  
  @Test
  public void testReplenishReq() {
    try {
      final Replenisher person = new Replenisher("Tommy");
      
      Location location1 = new Location('A',0,0,1);
      Location location2 = new Location("A,0,0,2,3");
      Location location3 = new Location("A,0,0,1,2");
      
      ReplenishReq request1 = new ReplenishReq(location1);
      ReplenishReq request2 = new ReplenishReq(location2);
      ReplenishReq request3 = new ReplenishReq(location3);
      ReplenishReq request4 = new ReplenishReq('A',0,0,2);
      
      assertTrue(request2.equals(request4));
      assertFalse(request3.equals(request4));
      assertTrue(request1.equals(request3));
      assertFalse(request1.equals(request2));
      assertFalse(request1.equals(person));
      
      person.takeReplenishRequest(request1);
      
      System.out.println(request1);
    } catch (Exception ex) {
      // Empty Block
    }
  }
}
