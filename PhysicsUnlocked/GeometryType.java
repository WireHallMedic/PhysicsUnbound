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
   
   private static final double ASCENDING_ANGLE = Math.PI / 4.0;
   private static final double DESCENDING_ANGLE = (Math.PI * 2) - (Math.PI / 4.0);
   
   // returns the height of the slope at x
   public double getYFromX(double x)
   {
      if(this == ASCENDING_FLOOR || this == ASCENDING_CEILING)
         return 1.0 - x;
      if(this == DESCENDING_FLOOR || this == DESCENDING_CEILING)
         return x;
      if(this == FULL)
         return 1.0;
      return 0.0;
   }
   
   public boolean isAngled()
   {
      switch(this)
      {
         case EMPTY : 
         case FULL :                return false;
         case ASCENDING_FLOOR : 
         case DESCENDING_FLOOR : 
         case ASCENDING_CEILING : 
         case DESCENDING_CEILING :  return true;
      }
      throw new Error("Unknown Geometry type");
   }
   
   public double getSlope()
   {
      switch(this)
      {
         case EMPTY : 
         case FULL :                return 0.0;
         case ASCENDING_FLOOR : 
         case ASCENDING_CEILING :   return ASCENDING_ANGLE;
         case DESCENDING_FLOOR : 
         case DESCENDING_CEILING :  return DESCENDING_ANGLE;
      }
      throw new Error("Unknown Geometry type");
   }
}