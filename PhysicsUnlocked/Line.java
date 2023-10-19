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
	public double getB(){return b;}
	public DoublePair getPoint(){return new DoublePair(point);}


	public void setM(double m){m = m;}
	public void setB(double b){b = b;}
	public void setPoint(DoublePair p){point = p;}


   public Line(DoublePair origin, DoublePair slope)
   {
      point = new DoublePair(origin);
      m = slope.y / slope.x;
      b = origin.y - (m * origin.x);
   }
   
   @Override
   public String toString()
   {
      return String.format("y = %f.3x + %f.3", m, b);
   }
}