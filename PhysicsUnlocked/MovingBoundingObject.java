/*
   Is the parent object of anything that might move around.
   All distances are in tiles.
   All speeds are in tiles per second.
   All accelerations are in tiles per second per second.
*/

package PhysicsUnlocked;

import java.util.*;

public abstract class MovingBoundingObject extends BoundingObject implements MovingCollidable
{
   protected DoublePair speed;
   protected DoublePair acceleration;
   protected DoublePair deceleration;     // this is an absolute value
   protected DoublePair maxSpeed;         // note that this is a cap on acceleration, not otherwise enforced
	protected boolean affectedByGravity;
	protected boolean pushedByGeometry;
   protected Vector<MovingCollisionListener> listenerList;


	public double getXSpeed(){return speed.x;}
	public double getYSpeed(){return speed.y;}
   public DoublePair getSpeed(){return new DoublePair(speed);}
	public double getXAcceleration(){return acceleration.x;}
	public double getYAcceleration(){return acceleration.y;}
   public DoublePair getAcceleration(){return new DoublePair(acceleration);}
	public double getXDeceleration(){return deceleration.x;}
	public double getYDeceleration(){return deceleration.y;}
   public DoublePair getDeceleration(){return new DoublePair(deceleration);}
	public double getXMaxSpeed(){return maxSpeed.x;}
	public double getYMaxSpeed(){return maxSpeed.y;}
   public DoublePair getMaxSpeed(){return new DoublePair(maxSpeed);}
	public boolean isAffectedByGravity(){return affectedByGravity;}
	public boolean isPushedByGeometry(){return pushedByGeometry;}
   public abstract int getDrawOriginX(int tileSizePixels);
   public abstract int getDrawOriginY(int tileSizePixels);


	public synchronized void setXSpeed(double x){speed.x = x;}
	public synchronized void setYSpeed(double y){speed.y = y;}
   public synchronized void setSpeed(double x, double y){setXSpeed(x); setYSpeed(y);}
   public synchronized void setSpeed(DoublePair dp){setXSpeed(dp.x); setYSpeed(dp.y);}
	public synchronized void setXAcceleration(double x){acceleration.x = x;}
	public synchronized void setYAcceleration(double y){acceleration.y = y;}
   public synchronized void setAcceleration(double x, double y){setXAcceleration(x); setYAcceleration(y);}
	public synchronized void setXDeceleration(double x){deceleration.x = x;}
	public synchronized void setYDeceleration(double y){deceleration.y = y;}
   public synchronized void setDeceleration(double x, double y){setXDeceleration(x); setYDeceleration(y);}
	public synchronized void setXMaxSpeed(double x){maxSpeed.x = x;}
	public synchronized void setYMaxSpeed(double y){maxSpeed.y = y;}
   public synchronized void setMaxSpeed(double x, double y){setXMaxSpeed(x); setYMaxSpeed(y);}
	public synchronized void setAffectedByGravity(boolean a){affectedByGravity = a;}
	public synchronized void setPushedByGeometry(boolean c){pushedByGeometry = c;}



   public MovingBoundingObject()
   {
      super();
      speed = new DoublePair(0.0, 0.0);
      maxSpeed = new DoublePair(10.0, 10.0);
      acceleration = new DoublePair(0.0, 0.0);
      deceleration = new DoublePair(0.0, 0.0);
      setAffectedByGravity(true);
      setPushedByGeometry(true);
      listenerList = new Vector<MovingCollisionListener>();
   }
   
   // adjust x speed
   public synchronized void applyXImpulse(double x)
   {
      speed.x += x;
   }
   
   // adjust y speed
   public synchronized void applyYImpulse(double y)
   {
      speed.y += y;
   }
   
   // adjust x and y speeds
   public synchronized void applyImpulse(double x, double y)
   {
      applyXImpulse(x);
      applyYImpulse(y);
   }
   
   // adjust position based on speeds
   public synchronized void applySpeeds(double secondsElapsed)
   {
      setXLoc(getXLoc() + (getXSpeed() * secondsElapsed));
      setYLoc(getYLoc() + (getYSpeed() * secondsElapsed));
   }
   
   // gravity is like other y-impulses, except cannot be greater than terminal velocity
   public synchronized void applyGravityImpulse(double speed, double terminalVelocity)
   {
      applyYImpulse(speed);
      setYSpeed(Math.min(getYSpeed(), terminalVelocity));
   }
   
   // returns x, y to start searching for geometric collisions
   public synchronized int[] getPotentialCollisionOrigin(double seconds)
   {
      double baseXLoc = getXLoc() - getHalfWidth();
      double baseYLoc = getYLoc() - getHalfHeight();
      double xOrigin = Math.min(baseXLoc, baseXLoc + (getXSpeed() * seconds));
      double yOrigin = Math.min(baseYLoc, baseYLoc + (getYSpeed() * seconds));
      int[] returnArr = {(int)xOrigin, (int)yOrigin};
      // because the direction of truncating reverses as you traverse zero
      if(xOrigin < 0.0)
         returnArr[0]--;
      if(yOrigin < 0.0)
         returnArr[1]--;
      return returnArr;
   }
   
   // returns x, y to end searching for geometric collisions
   public synchronized int[] getPotentialCollisionEnd(double seconds)
   {
      double baseXLoc = getXLoc() + getHalfWidth();
      double baseYLoc = getYLoc() + getHalfHeight();
      double xOrigin = Math.max(baseXLoc, baseXLoc + (getXSpeed() * seconds));
      double yOrigin = Math.max(baseYLoc, baseYLoc + (getYSpeed() * seconds));
      int[] returnArr = {(int)xOrigin, (int)yOrigin};
      // because the direction of truncating reverses as you traverse zero
      if(xOrigin < 0.0)
         returnArr[0]--;
      if(yOrigin < 0.0)
         returnArr[1]--;
      return returnArr;
   }
   
   // adjust speeds to stop short of a collision
   public synchronized void adjustForCollision(SweptAABB collision){adjustForCollision(collision, GeometryType.FULL);}
   public synchronized void adjustForCollision(SweptAABB collision, GeometryType geoType)
   {
      DoublePair spd = getSpeed();
      DoublePair spdAdj = new DoublePair();
      double remainingTime = 1.0 - collision.getTime();
      
      // rotate to make angle 0.0, we don't need normals because we know there's only a y collision
      if(geoType.isAngled())
      {
         switch(geoType)
         {
            case ASCENDING_FLOOR :     spd.rotate(-DoublePair.EIGHTH_CIRCLE);
                                       break;
            case DESCENDING_FLOOR :    spd.rotate(DoublePair.EIGHTH_CIRCLE);
                                       break;
            case ASCENDING_CEILING :   spd.rotate(-DoublePair.EIGHTH_CIRCLE);
                                       break;
            case DESCENDING_CEILING :  spd.rotate(DoublePair.EIGHTH_CIRCLE);
                                       break;
         
         }
         spd.y = 0.0;
         
         // rotate back
         switch(geoType)
         {
            case ASCENDING_FLOOR :     spd.rotate(DoublePair.EIGHTH_CIRCLE);
                                       break;
            case DESCENDING_FLOOR :    spd.rotate(-DoublePair.EIGHTH_CIRCLE);
                                       break;
            case ASCENDING_CEILING :   spd.rotate(DoublePair.EIGHTH_CIRCLE);
                                       break;
            case DESCENDING_CEILING :  spd.rotate(-DoublePair.EIGHTH_CIRCLE);
                                       break;
         }
         
         setXSpeed(spd.x);
         setYSpeed(spd.y);
      }
      else
      {
         setXSpeed(spd.x + (Math.abs(spd.x) * collision.getNormalX()));
         setYSpeed(spd.y + (Math.abs(spd.y) * collision.getNormalY()));
      }
   }
   
   // accelerations
   public synchronized void applyAccelerations(double secondsElapsed)
   {
      // moving in direction we're accelerating, cap speed
      if(signsMatch(speed.x, acceleration.x))
      {
         if(Math.abs(speed.x) < maxSpeed.x)
         {
            speed.x += (acceleration.x * secondsElapsed);
            if(speed.x < 0.0)
               speed.x = Math.max(speed.x, -maxSpeed.x);
            else
               speed.x = Math.min(speed.x, maxSpeed.x);
         }
      }
      else
         // accelerating counter to movement
         speed.x += (acceleration.x * secondsElapsed);
         
      // moving in direction we're accelerating, cap speed
      if(signsMatch(speed.y, acceleration.y))
      {
         if(Math.abs(speed.y) < maxSpeed.y)
         {
            speed.y += (acceleration.y * secondsElapsed);
            if(speed.y < 0.0)
               speed.y = Math.max(speed.y, -maxSpeed.y);
            else
               speed.y = Math.min(speed.y, maxSpeed.y);
         }
      }
      else
         // accelerating counter to movement
         speed.y += (acceleration.y * secondsElapsed);
      
      // don't apply deceleration if we're accelerating in the direction we're going
      if(deceleration.x > 0.0 && !signsMatch(speed.x, acceleration.x))
      {
         if(speed.x < 0.0) // moving left
         {
            speed.x += (deceleration.x * secondsElapsed);
            speed.x = Math.min(0.0, speed.x);
         }
         else if(speed.x > 0.0) // moving right
         {
            speed.x -= (deceleration.x * secondsElapsed);
            speed.x = Math.max(0.0, speed.x);
         }
      }
      // don't apply deceleration if we're accelerating in the direction we're going
      if(deceleration.y > 0.0 && !signsMatch(speed.y, acceleration.y))
      {
         if(speed.y < 0.0) // moving up
         {
            speed.y += (deceleration.y * secondsElapsed);
            speed.y = Math.min(0.0, speed.y);
         }
         else if(speed.y > 0.0) // moving down
         {
            speed.y -= (deceleration.y * secondsElapsed);
            speed.y = Math.max(0.0, speed.y);
         }
      }
   }
   
   // do the two signs match, with either being zero returning false
   private boolean signsMatch(double a, double b)
   {
      if(a == 0.0 || b == 0.0)
         return false;
      return a < 0.0 == b < 0.0;
   }
   
   // collision event handling
   //////////////////////////////////////////////////////////////
   public void movingCollisionOccured(MovingCollision mc)
   {
      for(MovingCollisionListener listener : listenerList)
         listener.movingCollisionOccured(mc);
   }
   
   public void addCollisionListener(MovingCollisionListener listener)
   {
      listenerList.add(listener);
   }
   
   
   public void removeCollisionListener(MovingCollisionListener listener)
   {
      listenerList.remove(listener);
   }
   
}