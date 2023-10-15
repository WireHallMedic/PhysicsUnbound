/*
An interface for moving objects to handle collisions with other objects and geometry as an event
*/

package PhysicsUnlocked;


public class MovingCollision
{
	private MovingBoundingObject movingBoundingObject;
	private boolean geometryCollision;
	private boolean nonGeometryCollision;
   private MovingBoundingObject source;


	public MovingBoundingObject getMovingBoundingObject(){return movingBoundingObject;}
	public boolean isGeometryCollision(){return movingBoundingObject != null;}
	public boolean isNonGeometryCollision(){return !isGeometryCollision();}
   public MovingBoundingObject getSource(){return source;}


	public void setMovingBoundingObject(MovingBoundingObject m){movingBoundingObject = m;}
   public void setSource(MovingBoundingObject s){source = s;}

   public MovingCollision(MovingBoundingObject s, MovingBoundingObject mbo)
   {
      source = s;
      movingBoundingObject = mbo;
   }
}