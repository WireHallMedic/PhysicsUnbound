package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SweptAABBTest 
{
   private static BoundingBox player = null;
   private static BoundingBox enemy = null;
   private static BoundingBox playerProjectile = null;
   private static BoundingBox enemyProjectile = null;
   private static BoundingBox environmental = null;
   private static PhysicsUnlockedEngine engine = null;

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
   
   @Test public void hitscanTest()
   {
      PhysicsUnlockedEngine engine = engineSetUp();
      DoublePair origin = new DoublePair(6.0, 1.0);
      DoublePair distance = new DoublePair(0.0, 10.0);
      DoublePair expectedDist = new DoublePair(0.0, 8.0);
      DoublePair calcDist = new DoublePair();
      calcDist = engine.getHitscanImpactGeometry(origin, distance);
      MovingBoundingObject hitObject = null;
      assertTrue("Hitscan detects wall", expectedDist.equals(calcDist));
      
      origin = new DoublePair(6.0, 8.0);
      distance = new DoublePair(0.0, -10.0);
      expectedDist = new DoublePair(0.0, -8.0);
      calcDist = engine.getHitscanImpactGeometry(origin, distance);
      assertTrue("Negative direction hitscan detects wall", expectedDist.equals(calcDist));
      
      origin = new DoublePair(6.0, 2.0);
      distance = new DoublePair(0.001, 10.0);
      hitObject = engine.getHitscanImpact(origin, distance, engine.ENEMY_PROJECTILE);
      assertEquals("Enemy projectile that misses misses", null, hitObject);
      
      origin = new DoublePair(5.0, 2.0);
      distance = new DoublePair(-10.0, 0.1);
      hitObject = engine.getHitscanImpact(origin, distance, engine.ENEMY_PROJECTILE);
      assertEquals("Enemy projectile ignores player projectile and hits player", player, hitObject);
      
      hitObject = null;
      origin = new DoublePair(7.0, 4.0);
      distance = new DoublePair(-10.0, 0.0);
      hitObject = engine.getHitscanImpact(origin, distance, engine.PLAYER_PROJECTILE);
    //   if(hitObject == player)System.out.println("Player");
//       if(hitObject == enemy)System.out.println("Enemy");
//       if(hitObject == playerProjectile)System.out.println("Player Proj");
//       if(hitObject == enemyProjectile)System.out.println("Enemy Proj");
//       if(hitObject == environmental)System.out.println("Env");
      assertEquals("Player projectile ignores enemy projectile and hits enemy", enemy, hitObject);
   }
   
   private PhysicsUnlockedEngine engineSetUp() 
   {
      engine = new PhysicsUnlockedEngine();
      boolean[][] geometry = new boolean[10][10];
      for(int i = 0; i < 10; i++)
      {
         geometry[0][i] = true;
         geometry[9][i] = true;
         geometry[i][0] = true;
         geometry[i][9] = true;
      }
      engine.setGeometry(geometry);
      
      player = new BoundingBox(1.0, 1.0);
      player.setLoc(2.0, 2.0);
      engine.add(player, PhysicsUnlockedEngine.PLAYER);
      
      enemy = new BoundingBox(1.0, 1.0);
      enemy.setLoc(2.0, 4.0);
      engine.add(enemy, PhysicsUnlockedEngine.ENEMY);
      
      playerProjectile = new BoundingBox(1.0, 1.0);
      playerProjectile.setLoc(4.0, 2.0);
      playerProjectile.setPushedByGeometry(false);
      engine.add(playerProjectile, PhysicsUnlockedEngine.PLAYER_PROJECTILE);
      
      enemyProjectile = new BoundingBox(1.0, 1.0);
      enemyProjectile.setLoc(4.0, 6.0);
      enemyProjectile.setPushedByGeometry(false);
      engine.add(enemyProjectile, PhysicsUnlockedEngine.ENEMY_PROJECTILE);
      
      environmental = new BoundingBox(1.0, 1.0);
      environmental.setLoc(4.0, 8.0);
      engine.add(environmental, PhysicsUnlockedEngine.ENVIRONMENT);
      
      engine.terminate(); // we don't need it running for this
      
      return engine;
   }
}
