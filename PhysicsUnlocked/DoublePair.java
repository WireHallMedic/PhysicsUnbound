/*
A convient way to bundle related pairs of doubles, generally representing x and y
*/

package PhysicsUnlocked;

public class DoublePair
{
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
   
   public String serialize()
   {
      return String.format("[%1.3f, %1.3f]", x, y);
   }
   
   @Override
   public String toString()
   {
      return serialize();
   }
}