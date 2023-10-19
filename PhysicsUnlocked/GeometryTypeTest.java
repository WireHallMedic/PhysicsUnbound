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
      assertEquals("Quarter check on ascending floor", .25, GeometryType.ASCENDING_FLOOR.getYFromX(.75), .001);
      assertEquals("Quarter check on ascending ceiling", .25, GeometryType.ASCENDING_CEILING.getYFromX(.75), .001);
      assertEquals("Half check on ascending floor", .5, GeometryType.ASCENDING_FLOOR.getYFromX(.5), .001);
      assertEquals("Half check on ascending ceiling", .5, GeometryType.ASCENDING_CEILING.getYFromX(.5), .001);
      
      assertEquals("Quarter check on descending floor", .25, GeometryType.DESCENDING_FLOOR.getYFromX(.25), .001);
      assertEquals("Quarter check on descending ceiling", .25, GeometryType.DESCENDING_CEILING.getYFromX(.25), .001);
      assertEquals("Half check on descending floor", .5, GeometryType.DESCENDING_FLOOR.getYFromX(.5), .001);
      assertEquals("Half check on descending ceiling", .5, GeometryType.DESCENDING_CEILING.getYFromX(.5), .001);
   }
}
