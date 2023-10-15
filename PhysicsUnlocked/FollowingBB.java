/*
   This is a bounding box that follows some other MovingBoundingObject
*/


package PhysicsUnlocked;

public class FollowingBB extends BoundingBox
{
	private MovingBoundingObject leader;


	public MovingBoundingObject getLeader(){return leader;}


	public void setLeader(MovingBoundingObject l){leader = l;}


   public FollowingBB(double w, double h, MovingBoundingObject l)
   {
      super(w, h);
      leader = l;
   }
}