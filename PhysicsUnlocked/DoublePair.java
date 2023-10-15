/*
A convient way to bundle related pairs of doubles, generally representing x and y
*/

package PhysicsUnlocked;

public class DoublePair
{
   public final static double FULL_CIRCLE = 2 * Math.PI;                // 360 degrees
   public final static double THREE_QUARTER_CIRCLE = 3 * (Math.PI / 2); // 270 degrees
   public final static double HALF_CIRCLE = Math.PI;                    // 180 degrees
   public final static double QUARTER_CIRCLE = Math.PI / 2;             // 90 degrees
   
   public double x;
   public double y;
   
   public DoublePair(double xVal, double yVal)
   {
      x = xVal;
      y = yVal;
   }
   
   public DoublePair()
   {
      this(0.0, 0.0);
   }
   
   public DoublePair(DoublePair that)
   {
      this(that.x, that.y);
   }
   
   public void add(DoublePair that)
   {
      this.x += that.x;
      this.y += that.y;
   }
   
   public void subtract(DoublePair that)
   {
      this.x -= that.x;
      this.y -= that.y;
   }
   
   public String serialize()
   {
      return String.format("[%1.3f, %1.3f]", x, y);
   }
   
   @Override
   public String toString()
   {
      return serialize();
   }
   
   // returns the angle to the Cartesian coordinates of this pair
   public double getAngle()
   {
      if(x == 0.0)
      {
         if(y > 0.0)
            return THREE_QUARTER_CIRCLE;
         else
            return QUARTER_CIRCLE;
      }
      double angle = 0.0;
      angle = Math.atan( (-1.0 * y) / x);
      if(x < 0.0)
         angle += HALF_CIRCLE;
      return simplifyAngle(angle);
   }
   
   // returns the angle to the Cartesian coordinates of this pair
   public double getMagnitude()
   {
      return Math.sqrt( (x * x) + (y * y) );
   }
   
   
   public static DoublePair sum(DoublePair a, DoublePair b)
   {
      DoublePair dp = new DoublePair(a);
      a.add(b);
      return a;
   }
   
   public static DoublePair difference(DoublePair a, DoublePair b)
   {
      DoublePair dp = new DoublePair(a);
      dp.subtract(b);
      return dp;
   }
   
   // returns the x, y coordinates to an angle, with a magnitude of 1.0
   public static DoublePair getFromAngle(double theta)
   {
      DoublePair dp = new DoublePair();
      dp.x = Math.cos(theta);
      dp.y = Math.sin(theta) * -1.0;
      return dp;
   }
   
   public static double simplifyAngle(double angle)
   {
      while(angle <= 0.0)
         angle += FULL_CIRCLE;
      return angle % FULL_CIRCLE;
   }
}