/*
   The main object used for stuff that moves around. All dimensions are in tiles.
*/


package PhysicsUnlocked;

public class BoundingBox extends MovingBoundingObject
{
   private DoublePair size;
   private DoublePair halfSize;


   public DoublePair getSize(){return new DoublePair(size);}
	public double getWidth(){return size.x;}
	public double getHeight(){return size.y;}
   public DoublePair getHalfSize(){return new DoublePair(halfSize);}
   public double getHalfWidth(){return halfSize.x;}
   public double getHalfHeight(){return halfSize.y;}


	public void setWidth(double w){size.x = w; halfSize.x = w / 2;}
	public void setHeight(double h){size.y = h; halfSize.y = h / 2;}

   public BoundingBox(double w, double h)
   {
      super();
      setSize(w, h);
   }
   
   public void setSize(double w, double h)
   {
      size = new DoublePair(w, h);
      halfSize = new DoublePair(w / 2.0, h / 2.0);
   }
   
   public void setSize(DoublePair newSize)
   {
      setSize(newSize.x, newSize.y);
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