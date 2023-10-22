/*
Enum for tile types.
*/

package PhysicsUnlocked;

public enum GeometryType
{
   EMPTY (false),
   FULL (false),
   BLOCKS_RIGHT (true),     // passable unless you're on the left 
   BLOCKS_LEFT (true),      // passable unless you're on the right
   BLOCKS_UP (true),        // passable unless you're below
   BLOCKS_DOWN (true);      // passable unless you're above
   
   public boolean variableCollision;
   
   private GeometryType(boolean vc)
   {
      variableCollision = vc;
   }
}