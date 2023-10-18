/*

*/

package PhysicsUnlocked;

public enum GeometryType
{
   EMPTY,
   FULL,
   ASCENDING_FLOOR,
   DESCENDING_FLOOR,
   ASCENDING_CEILING,
   DESCENDING_CEILING;
   
   // returns the height of the slope at x
   public double getYFromX(double x)
   {
      if(this == ASCENDING_FLOOR && this == ASCENDING_FLOOR)
         return x;
      if(this == DESCENDING_FLOOR && this == DESCENDING_FLOOR)
         return 1.0 - x;
      if(this == FULL)
         return 1.0;
      return 0.0;
   }
}