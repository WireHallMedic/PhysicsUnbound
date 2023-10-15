package PhysicsUnlocked;

public class SweptAABB
{
	private int normalX;    // pushback on x axis
	private int normalY;    // pushback on y axis
	private double time;    // in range of [0.0, 1.0]
   private boolean collision; // did we collide


	public int getNormalX(){return normalX;}
	public int getNormalY(){return normalY;}
	public double getTime(){return time;}
   public boolean isCollision(){return collision;}


   public SweptAABB()
   {
      normalX = 0;
   	normalY = 0;
   	time = 1.0;
      collision = false;
   }
   
   public SweptAABB(DoublePair point, DoublePair speed, DoublePair boxOrigin, DoublePair boxSize)
   {
      doCheck(point, speed, boxOrigin, boxSize);
   }
   
   public SweptAABB(MovingBoundingObject obj, double secondsElapsed, int geometryX, int geometryY)
   {
      DoublePair boxOrigin = new DoublePair((1.0 * geometryX) - obj.getHalfWidth(), (1.0 * geometryY) - obj.getHalfHeight());
      DoublePair boxSize = new DoublePair(1.0 + obj.getWidth(), 1.0 + obj.getHeight());
      DoublePair speed = new DoublePair(obj.getSpeed().x * secondsElapsed, obj.getSpeed().y * secondsElapsed);
      doCheck(obj.getLoc(), speed, boxOrigin, boxSize);
   }

   // do all the work
   private void doCheck(DoublePair point, DoublePair speed, DoublePair boxOrigin, DoublePair boxSize)
   {
      normalX = 0;
      normalY = 0;
      time = 1.0;
      collision = false;
      
      DoublePair nearIntercept = new DoublePair();
      DoublePair farIntercept = new DoublePair();
      
      // determine intercepts
      if(speed.x > 0.0)
      {
         nearIntercept.x = boxOrigin.x - point.x;
         farIntercept.x = (boxOrigin.x + boxSize.x) - point.x;
      }
      else
      {
         nearIntercept.x = (boxOrigin.x + boxSize.x) - point.x;
         farIntercept.x = boxOrigin.x - point.x;
      }
      
      if(speed.y > 0.0)
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
      if(speed.x == 0.0)
      {
         entryTime.x = Double.MIN_VALUE;
         exitTime.x = Double.MAX_VALUE; 
      }
      // determine when would enter and exit on x axis
      else
      {
         entryTime.x = nearIntercept.x / speed.x;
         exitTime.x = farIntercept.x / speed.x; 
      }
      // use max and min values if not moving on y axis
      if(speed.y == 0.0)
      {
         entryTime.y = Double.MIN_VALUE;
         exitTime.y = Double.MAX_VALUE; 
      }
      // determine when would enter and exit on y axis
      else
      {
         entryTime.y = nearIntercept.y / speed.y;
         exitTime.y = farIntercept.y / speed.y; 
      }
      
      // calculate when the actual interceptions occur
      double firstIntersection = Math.max(entryTime.x, entryTime.y);
      double secondIntersection = Math.min(exitTime.x, exitTime.y);
      
      // no collision
      if(firstIntersection > secondIntersection ||    // enters after it exits
         entryTime.x < 0.0 && entryTime.y < 0.0 ||  // enters at negative time
         entryTime.x > 1.0 || entryTime.y > 1.0)    // enters after time
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
         collision = true;
      }
   }
   
   public String serialize()
   {
      return String.format("Time %1.3f, normalX %d, normalY %d", time, normalX, normalY);
   }
   
   @Override
   public String toString()
   {
      return serialize();
   }
}