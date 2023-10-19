package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class LineTest 
{


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test that always fails. **/
   @Test public void defaultTest() 
   {
      DoublePair origin = new DoublePair(0.0, 1.0);
      DoublePair slope = new DoublePair(-1.0, 1.0);
      Line lineA = new Line(origin, slope);
      Assert.assertEquals("Line A has expected slope", -1.0, lineA.getSlope(), .001);
      Assert.assertEquals("Line A has expected intercept", 1.0, lineA.getIntercept(), .001);
      
      origin = new DoublePair(0.0, -1.0);
      slope = new DoublePair(1.0, 1.0);
      Line lineB = new Line(origin, slope);
      Assert.assertEquals("Line B has expected slope", 1.0, lineB.getSlope(), .001);
      Assert.assertEquals("Line B has expected intercept", -1.0, lineB.getIntercept(), .001);
      
      DoublePair expectedIntersection = new DoublePair(1.0, 0.0);
      Assert.assertTrue("Lines A and B intersect", lineA.hasIntersection(lineB));
      Assert.assertTrue("Lines A and B have expected intersection", expectedIntersection.equals(lineA.getIntersection(lineB)));
      Assert.assertTrue("Reverse returns same result", expectedIntersection.equals(lineB.getIntersection(lineA)));
      
      Assert.assertFalse("Colinear lines do not have intersection", lineA.hasIntersection(lineA));
      origin.x += 3.4;
      Line lineC = new Line(origin, slope);
      Assert.assertFalse("Parallel lines do not have intersection", lineB.hasIntersection(lineC));
      
   }
}
