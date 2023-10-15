/*
An interface for moving objects to handle collisions with other objects and geometry as an event
*/

package PhysicsUnlocked;

import java.util.*;
import java.awt.*;

public interface MovingCollidable
{
   public void movingCollisionOccured(MovingCollision mc);
   public void addCollisionListener(MovingCollisionListener listener);
   public void removeCollisionListener(MovingCollisionListener listener);
}