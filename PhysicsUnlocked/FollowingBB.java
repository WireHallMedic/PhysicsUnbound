/*
   This is a bounding box that follows some other MovingBoundingObject
   The FollowingBB's loc variable is relative to the leader's loc variable.
   Setting the loc will change its relative position, and getting the loc
   will return the sum of this.loc and leader.loc
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
   
   @Override
	public double getXLoc()
   {
      return super.getXLoc() + leader.getXLoc();
   }
   
   @Override
	public double getYLoc()
   {
      return super.getYLoc() + leader.getYLoc();
   }
   
   @Override
   public DoublePair getLoc()
   {
      return DoublePair.sum(getLoc(), leader.getLoc());
   }
}