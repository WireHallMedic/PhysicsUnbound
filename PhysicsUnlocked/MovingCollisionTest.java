package PhysicsUnlocked;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;


public class MovingCollisionTest 
{
   private static BoundingBox player;
   private static BoundingBox enemy;
   private static BoundingBox playerProjectile;
   private static BoundingBox enemyProjectile;
   private static BoundingBox environmental1;
   private static BoundingBox environmental2;
   private static TestCollisionListener listener;
   private static PhysicsUnlockedEngine engine;

   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() 
   {
      listener = new TestCollisionListener();
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
      player.addCollisionListener(listener);
      engine.add(player, PhysicsUnlockedEngine.PLAYER);
      
      enemy = new BoundingBox(1.0, 1.0);
      enemy.setLoc(4.0, 2.0);
      enemy.addCollisionListener(listener);
      engine.add(enemy, PhysicsUnlockedEngine.ENEMY);
      
      playerProjectile = new BoundingBox(.5, .5);
      playerProjectile.setLoc(2.0, 5.0);
      playerProjectile.setPushedByGeometry(false);
      playerProjectile.addCollisionListener(listener);
      engine.add(playerProjectile, PhysicsUnlockedEngine.PLAYER_PROJECTILE);
      
      enemyProjectile = new BoundingBox(.5, .5);
      enemyProjectile.setLoc(4.0, 5.0);
      enemyProjectile.setPushedByGeometry(false);
      enemyProjectile.addCollisionListener(listener);
      engine.add(enemyProjectile, PhysicsUnlockedEngine.ENEMY_PROJECTILE);
      
      environmental1 = new BoundingBox(.5, .5);
      environmental1.setLoc(6.0, 2.0);
      environmental1.addCollisionListener(listener);
      engine.add(environmental1, PhysicsUnlockedEngine.ENVIRONMENT);
      
      environmental2 = new BoundingBox(.5, .5);
      environmental2.setLoc(6.0, 4.0);
      environmental2.addCollisionListener(listener);
      engine.add(environmental2, PhysicsUnlockedEngine.ENVIRONMENT);
      
      engine.terminate(); // we don't need it running for this
   }


   
   @Test public void noCollisionTest() 
   {
      engine.doCollisionChecks();
      assertEquals("No collisions with nothing overlapping", 0, listener.getTotalCollisions());
      
      playerProjectile.setLoc(player.getLoc());
      enemyProjectile.setLoc(enemy.getLoc());
      engine.doCollisionChecks();
      assertEquals("Projectiles do not collide with friendlies", 0, listener.getTotalCollisions());
      
      playerProjectile.setLoc(5.0, 5.0);
      enemyProjectile.setLoc(playerProjectile.getLoc());
      engine.doCollisionChecks();
      assertEquals("Projectiles do not collide with each other", 0, listener.getTotalCollisions());
      
   }
   
   @Test public void actorCollisionTest() 
   {
      player.setLoc(enemy.getLoc());
      engine.doCollisionChecks();
      assertTrue("Player collides with enemy", listener.hasCollisionBetween(player, enemy));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
      
      listener.clearList();
      player.setLoc(-2.0, -2.0);
      engine.doCollisionChecks();
      assertTrue("Player collides with OOB", engine.isCollidingWithGeometry(player));
      assertEquals("Push collision doesn't trigger event", 0, listener.getTotalCollisions());
      
      
      listener.clearList();
      player.setLoc(.5, .5);
      engine.doCollisionChecks();
      assertFalse("pushedByGeometry = false turns off geometry collision (as they should be pushed instead)", 
                  listener.hasCollisionFor(player));
      assertEquals("No events triggered", 0, listener.getTotalCollisions());
   }
   
   @Test public void projectileCollisionTest() 
   {
      playerProjectile.setLoc(enemy.getLoc());
      enemyProjectile.setLoc(player.getLoc());
      engine.doCollisionChecks();
      assertTrue("Player projectile hits enemy", listener.hasCollisionBetween(playerProjectile, enemy));
      assertTrue("Enemy projectile hits player", listener.hasCollisionBetween(enemyProjectile, player));
      assertEquals("All involved have event", 4, listener.getTotalCollisions());
      
      setUp();
      playerProjectile.setLoc(.5, .5);
      engine.doCollisionChecks();
      assertTrue("Projectiles collide with geometry", listener.hasCollisionFor(playerProjectile));
      assertEquals("All involved have event", 1, listener.getTotalCollisions());
   }
   
   @Test public void environmentalCollisionTest() 
   {
      environmental1.setLoc(environmental2.getLoc());
      engine.doCollisionChecks();
      assertTrue("Environmental boxes collide with each other", 
                 listener.hasCollisionBetween(environmental1, environmental2));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
      
      environmental1.setLoc(player.getLoc());
      listener.clearList();
      engine.doCollisionChecks();
      assertTrue("Environmental boxes collide with players",  
                 listener.hasCollisionBetween(environmental1, player));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
      
      environmental1.setLoc(enemy.getLoc());
      listener.clearList();
      engine.doCollisionChecks();
      assertTrue("Environmental boxes collide with enemies",  
                 listener.hasCollisionBetween(environmental1, enemy));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
      
      environmental1.setLoc(playerProjectile.getLoc());
      listener.clearList();
      engine.doCollisionChecks();
      assertTrue("Environmental boxes collide with player projectiles",  
                 listener.hasCollisionBetween(environmental1, playerProjectile));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
      
      environmental1.setLoc(enemyProjectile.getLoc());
      listener.clearList();
      engine.doCollisionChecks();
      assertTrue("Environmental boxes collide with enemy projectiles",  
                 listener.hasCollisionBetween(environmental1, enemyProjectile));
      assertEquals("All involved have event", 2, listener.getTotalCollisions());
   }
   
   private class TestCollisionListener implements MovingCollisionListener
   {
      private Vector<MovingCollision> list;
      
      public TestCollisionListener()
      {
         list = new Vector<MovingCollision>();
      }
      
      public void clearList()
      {
         list = new Vector<MovingCollision>();
      }
      
      public void movingCollisionOccured(MovingCollision mc)
      {
         list.add(mc);
      }
      
      public boolean hasCollisionFor(MovingCollidable mc)
      {
         for(MovingCollision col : list)
         {
            if(col.getSource() == mc)
               return true;
         }
         return false;
      }
      
      public boolean hasCollisionBetween(MovingCollidable mc1, MovingCollidable mc2)
      {
         for(MovingCollision col : list)
         {
            if((col.getSource() == mc1 && col.getMovingBoundingObject() == mc2) ||
               (col.getSource() == mc2 && col.getMovingBoundingObject() == mc1))
               return true;
         }
         return false;
      }
      
      public int getTotalCollisions()
      {
         return list.size();
      }
      
   }
}
