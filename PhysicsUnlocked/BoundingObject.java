package PhysicsUnlocked;

public abstract class BoundingObject
{
   protected DoublePair loc;


	public double getXLoc(){return loc.x;}
	public double getYLoc(){return loc.y;}
   public DoublePair getLoc(){return new DoublePair(loc);}


	public void setXLoc(double x){loc.x = x;}
	public void setYLoc(double y){loc.y = y;}
   public void setLoc(double x, double y){loc = new DoublePair(x, y);}
   public void setLoc(DoublePair p){loc = new DoublePair(p);}

   
   public BoundingObject()
   {
      loc = new DoublePair(0.0, 0.0);
   }
   
   public abstract boolean isColliding(BoundingObject that);
   public abstract boolean pointIsIn(double x, double y);
   public abstract boolean pointIsIn(DoublePair point);
   public abstract double getHalfWidth();
   public abstract double getHalfHeight();
   public abstract double getWidth();
   public abstract double getHeight();
}