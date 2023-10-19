/*
A class for mathematical lines. Kept in the form of y = mx + b. We hold  m, b, and a point on the line
*/

package PhysicsUnlocked;


public class Line
{
	private double m;
	private double b;
	private DoublePair point;


	public double getM(){return m;}
   public double getSlope(){return m;}
	public double getB(){return b;}
   public double getIntercept(){return b;}
	public DoublePair getPoint(){return new DoublePair(point);}


   public Line(DoublePair origin, DoublePair slope)
   {
      point = new DoublePair(origin);
      m = slope.y / slope.x;
      b = origin.y - (m * origin.x);
   }
   
   @Override
   public String toString()
   {
      return String.format("y = %.3fx + %.3f", m, b);
   }
   
   public boolean hasIntersection(Line that)
   {
      return this.m != that.m;
   }
   
   public DoublePair getIntersection(Line that)
   {
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