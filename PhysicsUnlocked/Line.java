/*
A class for mathematical lines. Kept in the form of y = mx + b. We hold  m, b, and a point on the line
*/

package PhysicsUnlocked;


public class Line
{
	private double m;
	private double b;
	private DoublePair point;
   private boolean vertical;


	public double getM(){return m;}
   public double getSlope(){return m;}
	public double getB(){return b;}
   public double getIntercept(){return b;}
	public DoublePair getPoint(){return new DoublePair(point);}
   public boolean isVertical(){return vertical;}


   public Line(DoublePair origin, DoublePair slope)
   {
      point = new DoublePair(origin);
      if(slope.x == 0.0)
      {
         m = Double.MAX_VALUE;
         vertical = true;
      }
      else
         m = slope.y / slope.x;
      b = origin.y - (m * origin.x);
   }
   
   public static Line getFromPoints(DoublePair pointA, DoublePair pointB)
   {
      return new Line(pointA, new DoublePair(pointB.x - pointA.x, pointB.y - pointA.y));
   }
   
   @Override
   public String toString()
   {
      return String.format("y = %.3fx + %.3f", m, b);
   }
   
   public boolean hasIntersection(Line that)
   {
      if(this.vertical && that.vertical)
         return false;
      return this.m != that.m;
   }
   
   public DoublePair getIntersection(Line that)
   {
      if(this.isVertical())
      {
         DoublePair intersection = new DoublePair(this.point.x, 0.0);
         intersection.y = (that.m * intersection.x) + that.b;
         return intersection;
      }
      else if(that.isVertical())
      {
         DoublePair intersection = new DoublePair(that.point.x, 0.0);
         intersection.y = (this.m * intersection.x) + this.b;
         return intersection;
      }
      double[] a = this.getStandardForm();
      double[] b = that.getStandardForm();
      double d  = (a[0] * b[1]) - (a[1] * b[0]);
      double dx = (a[2] * b[1]) - (a[1] * b[2]);
      double dy = (a[0] * b[2]) - (a[2] * b[0]);
      return new DoublePair(dx / d, dy / d);
   }
   
   public double[] getStandardForm()
   {
      double[] answer = {m, -1.0, -b};
      return answer;
   }
}