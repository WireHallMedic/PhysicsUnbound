package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class BoundingObjectTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   @Test public void testBoxCollision() 
   {
      BoundingBox a = new BoundingBox(1.0, 1.0);
      a.setLoc(0.0, 0.0);
      BoundingBox b = new BoundingBox(1.0, 1.0);
      b.setLoc(0.75, 0.75);
      assertTrue("Overlap on both axes is collision.", a.isColliding(b));
      b.setLoc(2.0, 0.0);
      assertFalse("Overlap on single axis is not a collision.", a.isColliding(b));
      b.setLoc(0.0, 2.0);
      assertFalse("Overlap on other single axis is not a collision.", a.isColliding(b));
      b.setLoc(2.0, 2.0);
      assertFalse("Overlap on no axes is not a collision.", a.isColliding(b));
      
      assertTrue("Point in box detected.", a.pointIsIn(0.45, 0.45));
      assertFalse("Point above box not detected.", a.pointIsIn(0.0, -0.55));
      assertFalse("Point below box not detected.", a.pointIsIn(0.0, 0.55));
      assertFalse("Point left of box not detected.", a.pointIsIn(-0.55, 0.0));
      assertFalse("Point right box not detected.", a.pointIsIn(0.55, 0.0));
   }
}
