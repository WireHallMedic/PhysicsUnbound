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
   
   private static final DoublePair flatSlope = new DoublePair(1.0, 0.0);
   private static final DoublePair ascendingSlope = new DoublePair(1.0, -1.0);
   private static final DoublePair descendingSlope = new DoublePair(1.0, 1.0);
   
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
   
   public DoublePair getSlope()
   {
      switch(this)
      {
         case EMPTY : 
         case FULL :                return flatSlope;
         case ASCENDING_FLOOR : 
         case ASCENDING_CEILING :   return ascendingSlope;
         case DESCENDING_FLOOR : 
         case DESCENDING_CEILING :  return descendingSlope;
      }
      throw new Error("Unknown Geometry type");
   }
}