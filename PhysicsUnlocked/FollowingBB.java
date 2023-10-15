/*
   This is a bounding box that follows 
*/


package PhysicsUnlocked;

public class FollowingBB extends BoundingBox
{
   private MovingBoundingObject leader;



   public FollowingBB(double w, double h)
   {
      super(w, h);
   }
   
   // as the center point is used for logic, we need some small calculations for drawing
   public double getDrawOriginX()
   {
      return loc.x - halfSize.x;
   }
   
   public double getDrawOriginY()
   {
      return loc.y - halfSize.y;
   }
   
   
   public boolean isColliding(BoundingObject that)
   {
      if(that instanceof BoundingBox)
      {
         BoundingBox thatBox = (BoundingBox)that;
         return this.loc.x - this.halfSize.x < thatBox.loc.x + thatBox.halfSize.x &&
                this.loc.x + this.halfSize.x > thatBox.loc.x - thatBox.halfSize.x &&
                this.loc.y - this.halfSize.y < thatBox.loc.y + thatBox.halfSize.y &&
                this.loc.y + this.halfSize.y > thatBox.loc.y - thatBox.halfSize.y;
      }
      return false;
   }
   
   public boolean pointIsIn(double x, double y)
   {
      return loc.x - halfSize.x <= x &&
             loc.x + halfSize.x > x &&
             loc.y - halfSize.y <= y &&
             loc.y + halfSize.y > y;
   }
   
   public boolean pointIsIn(DoublePair point)
   {
      return pointIsIn(point.x, point.y);
   }
}