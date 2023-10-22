/*
An interface for moving objects to handle collisions with other objects and geometry as an event
*/

package PhysicsUnlocked;


public class MovingCollision
{
	private MovingBoundingObject otherObject;
   private MovingBoundingObject source;


	public MovingBoundingObject getOtherObject(){return otherObject;}
	public boolean isGeometryCollision(){return otherObject != null;}
	public boolean isNonGeometryCollision(){return !isGeometryCollision();}
   public MovingBoundingObject getSource(){return source;}

   public MovingCollision(MovingBoundingObject s, MovingBoundingObject mbo)
   {
      source = s;
      otherObject = mbo;
   }
}