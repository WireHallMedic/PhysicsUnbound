package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SweptAABBTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   
   @Test public void verticalTest() 
   {
      DoublePair point = new DoublePair(1.5f, 0.0);
      DoublePair speed = new DoublePair(0.0, 0.5f);
      DoublePair boxOrigin = new DoublePair(1.0, 1.0);
      DoublePair boxSize = new DoublePair(1.0, 1.0);
      SweptAABB collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Vertical undershot does not collide.", 1.0, collisionCheck.getTime(), .01);
      speed = new DoublePair(0.0, 1.5f);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Vertical collision collides.", .667f, collisionCheck.getTime(), .01);
      speed = new DoublePair(0.0, 3.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Vertical overshot collides.", .333f, collisionCheck.getTime(), .01);
      assertEquals("Shot from above sets normalX to 0.", 0, collisionCheck.getNormalX());
      assertEquals("Shot from above sets normalY to -1.", -1, collisionCheck.getNormalY());
      point = new DoublePair(1.5f, 3.0);
      speed = new DoublePair(0.0, -3.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Shot from below sets normalX to 0.", 0, collisionCheck.getNormalX());
      assertEquals("Shot from below sets normalY to 1.", 1, collisionCheck.getNormalY());
   }
   
   @Test public void horizontalTest() 
   {
      DoublePair point = new DoublePair(0.0, 1.5f);
      DoublePair speed = new DoublePair(0.5f, 0.0);
      DoublePair boxOrigin = new DoublePair(1.0, 1.0);
      DoublePair boxSize = new DoublePair(1.0, 1.0);
      SweptAABB collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Horizontal undershot does not collide.", 1.0, collisionCheck.getTime(), .01);
      speed = new DoublePair(1.5f, 0.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Horizontal collision collides.", .667f, collisionCheck.getTime(), .01);
      speed = new DoublePair(3.0, 0.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Horizontal overshot collides.", .333f, collisionCheck.getTime(), .01);
      assertEquals("Shot from left sets normalX to -1.", -1, collisionCheck.getNormalX());
      assertEquals("Shot from left sets normalY to 0.", 0, collisionCheck.getNormalY());
      point = new DoublePair(3.0, 1.5f);
      speed = new DoublePair(-3.0, 0.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Shot from right sets normalX to 1.", 1, collisionCheck.getNormalX());
      assertEquals("Shot from brightelow sets normalY to 0.", 0, collisionCheck.getNormalY());
   }
   
   @Test public void diagonalTest() 
   {
      DoublePair point = new DoublePair(0.3, 0.3);
      DoublePair speed = new DoublePair(0.5, 0.5);
      DoublePair boxOrigin = new DoublePair(1.0, 1.0);
      DoublePair boxSize = new DoublePair(1.0, 1.0);
      SweptAABB collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Diagonal undershot does not collide.", 1.0, collisionCheck.getTime(), .01);
      speed = new DoublePair(1.2, 1.2);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertTrue("Diagonal collision collides.", collisionCheck.getTime() < .9);
      speed = new DoublePair(3.0, 3.0);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertTrue("Diagonal overshot collides.", collisionCheck.getTime() < .9);
      point = new DoublePair(0.2, 0.3);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Diagonal shot hitting left sets normalX to -1.", -1, collisionCheck.getNormalX());
      assertEquals("Diagonal shot hitting left sets normalY to 0.", 0, collisionCheck.getNormalY());
      point = new DoublePair(0.3, 0.2);
      collisionCheck = new SweptAABB(point, speed, boxOrigin, boxSize);
      assertEquals("Diagonal shot hitting top sets normalX to 0.", 0, collisionCheck.getNormalX());
      assertEquals("Diagonal shot hitting top sets normalY to -1.", -1, collisionCheck.getNormalY());
   }
   
   @Test public void doublePrecisionTest()
   {
      double counter = 0.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 0.0.", 1.0, counter, .001);
      counter = 10.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 10.0.", 11.0, counter, .001);
      counter = 100.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 100.0.", 101.0, counter, .001);
      counter = 1000.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 1000.0.", 1001.0, counter, .001);
      counter = 10000.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 10000.0.", 10001.0, counter, .001);
      counter = 100000.0;
      for(int i = 0; i < 1000; i++)
         counter += .001;
      assertEquals("Precision maintained at counter = 10000.0.", 100001.0, counter, .001);
   }
}
