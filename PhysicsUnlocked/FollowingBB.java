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
	public double getRelativeXLoc(){return super.getXLoc();}
	public double getRelativeYLoc(){return super.getYLoc();}
   public DoublePair getRelativeLoc(){return super.getLoc();}


	public void setLeader(MovingBoundingObject l){leader = l;}
	public void setRelativeXLoc(double x){super.setXLoc(x);}
	public void setRelativeYLoc(double y){super.setYLoc(y);}
   public void setRelativeLoc(double x, double y){super.setLoc(x, y);}
   public void setRelativeLoc(DoublePair l){super.setLoc(l.x, l.y);}


   public FollowingBB(double w, double h, MovingBoundingObject l)
   {
      super(w, h);
      leader = l;
      setAffectedByGravity(false);
	   setPushedByGeometry(false);
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
      return DoublePair.sum(getRelativeLoc(), leader.getLoc());
   }
   
   @Override
	public void setXLoc(double x)
   {
      setRelativeXLoc(x - leader.getXLoc());
   }
   
   @Override
	public void setYLoc(double y)
   {
      setRelativeYLoc(y - leader.getYLoc());
   }
   
   @Override
   public void setLoc(double x, double y)
   {
      setRelativeLoc(new DoublePair(x, y));
   }
   
   @Override
   public void setLoc(DoublePair p)
   {
      setRelativeLoc(DoublePair.difference(p, leader.getLoc()));
   }
}