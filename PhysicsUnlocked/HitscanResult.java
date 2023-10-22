/*
   A class for holding the results of a hitscan. Hitscans could hit an actor, or geometry.
*/

package PhysicsUnlocked;

import java.util.*;

public class HitscanResult
{
	private MovingBoundingObject movingObject;
	private DoublePair pointOfImpact;
	private boolean geometryImpact;
	private boolean movingObjectImpact;


	public MovingBoundingObject getMovingObject(){return movingObject;}
	public DoublePair getPointOfImpact(){return pointOfImpact;}
	public boolean isGeometryImpact(){return geometryImpact;}
	public boolean isMovingObjectImpact(){return movingObjectImpact;}
   

   public HitscanResult(DoublePair origin, DoublePair distance, PhysicsUnlockedEngine engine, int team)
   {
      movingObject = null;
      pointOfImpact = new DoublePair(origin);
      doWork(origin, distance, engine, team);
   }
   
   private void doWork(DoublePair origin, DoublePair distance, PhysicsUnlockedEngine engine, int team)
   {
      double geoDist = engine.getDistanceMetric(new DoublePair(0.0, 0.0), distance) + 1.0;
      double movingObjDist = geoDist;
      DoublePair geoPoI = new DoublePair();
      DoublePair movingObjPoI = new DoublePair();
      
      // determing geometry collision
      DoublePair modifiedDist = engine.getHitscanImpactGeometry(origin, distance);
      if(!modifiedDist.equals(distance))
      {
         int targetX = (int)(origin.x + modifiedDist.x);
         int targetY = (int)(origin.y + modifiedDist.y);
         if(origin.x + modifiedDist.x < 0.0)
            targetX -= 1;
         if(origin.y + modifiedDist.y < 0.0)
            targetY -= 1;
         SweptAABB collision = new SweptAABB(origin, modifiedDist, targetX, targetY);
         geoPoI = collision.getCollisionLoc();
         geoDist = engine.getDistanceMetric(origin, geoPoI);
      }
      
      // determing movingObject collision
      movingObject = engine.getHitscanImpact(origin, distance, team);
      if(movingObject != null)
      {
         SweptAABB collision = new SweptAABB(origin, distance, movingObject);
         movingObjPoI = collision.getCollisionLoc();
         movingObjDist = engine.getDistanceMetric(origin, movingObjPoI);
      }
      
      if(movingObjDist < geoDist)
      {
         movingObjectImpact = true;
         pointOfImpact = movingObjPoI;
      }
      if(movingObjDist > geoDist)
      {
         geometryImpact = true;
         pointOfImpact = geoPoI;
      }
      if(!geometryImpact && !movingObjectImpact)
      {
         pointOfImpact = DoublePair.sum(origin, distance);
      }
   }
}