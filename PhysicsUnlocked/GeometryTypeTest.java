package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class GeometryTypeTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void defaultTest() 
   {
      assertEquals("Quarter check on ascending floor", .25, GeometryType.ASCENDING_FLOOR.getYFromX(.25), .001);
   }
}
