/*
This is an implementation of swept axis-aligned bounding boxes, also called projected rectangle collision.
All calculations are done when the constructor is called.

The object is essentially the return value of a function. The values normalX and normalY are, well, normals. If
no collision occured, they'll both be zero, otherwise they represent "pushback" along their respective axis.

Time represents the portion of the distance the object would travel that occurs before a collision. So if the 
objects are already in collision, this would be 0.0. If no collision occurs, this will be 1.0. If a collision
would occur at three-quarters of the distance the object is trying to move, this number would be .75 regardless
of what the attempted distance would be.
*/

package PhysicsUnlocked;

public class SweptAABB
{
	private int normalX;    // pushback on x axis
	private int normalY;    // pushback on y axis
	private double time;    // in range of [0.0, 1.0]
   private boolean collision; // did we collide
   private DoublePair collisionLoc;


	public int getNormalX(){return normalX;}
	public int getNormalY(){return normalY;}
	public double getTime(){return time;}
   public boolean isCollision(){return collision;}
   public DoublePair getCollisionLoc(){return new DoublePair(collisionLoc);}


   public SweptAABB()
   {
      normalX = 0;
   	normalY = 0;
   	time = 1.0;
      collision = false;
      collisionLoc = null;
   }
   
   public SweptAABB(DoublePair point, DoublePair distance, DoublePair boxOrigin, DoublePair boxSize)
   {
      doCheck(point, distance, boxOrigin, boxSize);
   }
   
   public SweptAABB(DoublePair point, DoublePair distance, MovingBoundingObject obj)
   {
      DoublePair boxOrigin = obj.getLoc();
      boxOrigin.x -= obj.getHalfWidth();
      boxOrigin.y -= obj.getHalfHeight();
      doCheck(point, distance, boxOrigin, new DoublePair(obj.getWidth(), obj.getHeight()));
   }
   
   public SweptAABB(MovingBoundingObject obj, double secondsElapsed, int geometryX, int geometryY)
   {
      DoublePair boxOrigin = new DoublePair((1.0 * geometryX) - obj.getHalfWidth(), (1.0 * geometryY) - obj.getHalfHeight());
      DoublePair boxSize = new DoublePair(1.0 + obj.getWidth(), 1.0 + obj.getHeight());
      DoublePair distance = new DoublePair(obj.getSpeed().x * secondsElapsed, obj.getSpeed().y * secondsElapsed);
      doCheck(obj.getLoc(), distance, boxOrigin, boxSize);
   }

   // do all the work
   private void doCheck(DoublePair point, DoublePair distance, DoublePair boxOrigin, DoublePair boxSize)
   {
      normalX = 0;
      normalY = 0;
      time = 1.0;
      collision = false;
      
      DoublePair nearIntercept = new DoublePair();
      DoublePair farIntercept = new DoublePair();
      
      // determine intercepts
      if(distance.x > 0.0)
      {
         nearIntercept.x = boxOrigin.x - point.x;
         farIntercept.x = (boxOrigin.x + boxSize.x) - point.x;
      }
      else
      {
         nearIntercept.x = (boxOrigin.x + boxSize.x) - point.x;
         farIntercept.x = boxOrigin.x - point.x;
      }
      
      if(distance.y > 0.0)
      {
         nearIntercept.y = boxOrigin.y - point.y;
         farIntercept.y = (boxOrigin.y + boxSize.y) - point.y;
      }
      else
      {
         nearIntercept.y = (boxOrigin.y + boxSize.y) - point.y;
         farIntercept.y = boxOrigin.y - point.y;
      }
      
      // calculate entry and exit times. Using MAX_VALUES because we don't have infinity
      DoublePair entryTime = new DoublePair();
      DoublePair exitTime = new DoublePair();
      
      // use max and min values if not moving on x axis
      if(distance.x == 0.0)
      {
         if(point.x <= boxOrigin.x + boxSize.x &&
            point.x >= boxOrigin.x)
            entryTime.x = Double.MIN_VALUE;
         else
            entryTime.x = Double.MAX_VALUE;
         exitTime.x = Double.MAX_VALUE; 
      }
      // determine when would enter and exit on x axis
      else
      {
         entryTime.x = nearIntercept.x / distance.x;
         exitTime.x = farIntercept.x / distance.x; 
      }
      // use max and min values if not moving on y axis
      if(distance.y == 0.0)
      {
         if(point.y <= boxOrigin.y + boxSize.y &&
            point.y >= boxOrigin.y)
            entryTime.y = Double.MIN_VALUE;
         else
            entryTime.y = Double.MAX_VALUE;
         exitTime.y = Double.MAX_VALUE; 
      }
      // determine when would enter and exit on y axis
      else
      {
         entryTime.y = nearIntercept.y / distance.y;
         exitTime.y = farIntercept.y / distance.y; 
      }
      
      // calculate when the actual interceptions occur
      double firstIntersection = Math.max(entryTime.x, entryTime.y);
      double secondIntersection = Math.min(exitTime.x, exitTime.y);
      
      // no collision
      if((firstIntersection > secondIntersection) ||  // enters after it exits
         (entryTime.x < 0.0 && entryTime.y < 0.0) ||  // enters at negative time
         (entryTime.x > 1.0 || entryTime.y > 1.0))    // enters after time
      {
         return;
      }
      // collision
      else
      { 
         if(entryTime.x > entryTime.y)
            if(nearIntercept.x < 0.0)
               normalX = 1;
            else
               normalX = -1;
         else
            if(nearIntercept.y < 0.0)
               normalY = 1;
            else
               normalY = -1;
         time = firstIntersection;
         if(time == Double.MIN_VALUE)
            time = 0.0;
         collisionLoc = new DoublePair(point.x + (distance.x * time), point.y + (distance.y * time));
         collision = true;
      }
   }
   
   public String serialize()
   {
      return String.format("Time %1.3f, normalX %d, normalY %d, collision at %s", time, normalX, normalY, collisionLoc);
   }
   
   @Override
   public String toString()
   {
      return serialize();
   }
}