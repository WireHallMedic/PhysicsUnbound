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


   @Test public void lineTesting() 
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
      
      DoublePair pointA = new DoublePair(5.0, 5.0);
      DoublePair pointB = new DoublePair(10.0, 10.0);
      Line lineD = Line.getFromPoints(pointA, pointB);
      Assert.assertEquals("Line generated from points has expected slope", 1.0, lineD.getSlope(), .001);
      Assert.assertTrue("Line generated from points has point", pointA.equals(lineD.getPoint()));
   }
   
   @Test public void rotationTest()
   {
      double eighthCircle = DoublePair.QUARTER_CIRCLE / 2;
      double fullCircle = DoublePair.HALF_CIRCLE + DoublePair.HALF_CIRCLE;
      DoublePair loc = new DoublePair(1.0, 0.0);
      Assert.assertEquals("Origin to point is angle 0.0", 0.0, loc.getAngle(), .001);
      Assert.assertEquals("Origin to point is magnitude 1.0", 1.0, loc.getMagnitude(), .001);
      loc.rotate(eighthCircle);
      
      Assert.assertEquals("Origin to rotated point is angle .25 pi", eighthCircle, loc.getAngle(), .001);
      Assert.assertEquals("Origin to rotated point is magnitude 1.0", 1.0, loc.getMagnitude(), .001);
      Assert.assertEquals("Rotated point.x is .707", .707, loc.x, .001);
      Assert.assertEquals("Rotated point.y is -.707", -.707, loc.y, .001);
      
      loc.rotate(fullCircle);
      Assert.assertEquals("Rotation normalizes angles more than a full circle", eighthCircle, loc.getAngle(), .001);
      
      loc.rotate(-fullCircle);
      Assert.assertEquals("Rotation normalizes angles below 0.0", eighthCircle, loc.getAngle(), .001);
   }
   
   @Test public void relativeLocationTest()
   {
      DoublePair pointA = new DoublePair(5.0, 5.0);
      DoublePair slope = new DoublePair(1.0, 1.0);
      DoublePair pointB = new DoublePair(5.0, 4.0);
      DoublePair pointC = new DoublePair(5.0, 6.0);
      Line line = new Line(pointA, slope);
      assertFalse("Point on line is not above line", line.pointIsAbove(pointA));
      assertFalse("Point on line is not below line", line.pointIsBelow(pointA));
      assertTrue("Point above line is above line", line.pointIsAbove(pointB));
      assertTrue("Point below line is below line", line.pointIsBelow(pointC));
   }
}
