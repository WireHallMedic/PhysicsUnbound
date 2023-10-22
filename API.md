#API

#High-Level Concepts

## General Conventions
This library has a couple of constant conventions. Time is measured in seconds. Distance is measured in tiles. Therefore speeds are in tiles per second, and accelerations in tiles per second per second.

For the most part, this library doesn't know about display sizes and doesn't want to know. The exception is MovingBoundingObject.getDrawOriginX() and MovingBoundingObject.getDrawOriginY(). We need to make some small adjustments when converting between tiles (which are in double precision) and pixels (which are in int precision), so those functions take care of that.

All objects with some sort of size consider the center of their shape to be their origin.

## PhysicsUnlockedEngine
This is the workhorse class. It does its calculations as frequently as it can, up to 1000 Hz. It mainly needs two things; a 2D array for geometry, and MovingBoundingObjects which are the stuff that moves around and collides.

The engine can be paused with setRunFlag(), or terminated with terminate(). terminate() is mainly used for testing, as the engine's thread doesn't otherwise terminate.

It also keeps track of variables global to all the moving stuff; gravity and terminal velocity. If you're doing a side-on game you'll want to set these, if you're doing a top-down game you won't.

## MovingBoundingObject
This is an abstract class for the stuff that can move. There are a number of variables here.

Speed. This is a pair of doubles that represent current speed in tiles per second. You'll be directly setting these for anything where want to set an instantaneous speed; think the enemies in the original Super Mario Brothers. They don't have any horizontal acceleration, they just multiply their horizontal speed by -1 when they bump into stuff.

Acceleration. This is applied to change the speed. For an example, if an object has an x acceleration of .5, it will increase its speed to the right at .5 tiles per second per second. To continue our SMB example, this is how Mario is controlled horizontally.

Deceleration. This is an absolute value; negative numbers are meaningless here. This is how quickly an object's speed will approach 0.0 _if it is not accelerating in that direction._ So if you have an x acceleration of .5, and a positive x speed, deceleration will not be applied. If you are accelerating contrary to your speed, or not accelerating, then you will begin to decelerate. Continuing the SMB example, turtles would not have an x deceleration, because they move at constant speed. Mario would, because if you take your finger off the movement button, he will decelerate to a stop.

Max Speed. This is also an absolute value. This is the maximum speed to which an object can accelerate, but not otherwise a limit. So if you are using acceleration to control movement, this is a cap on how high (and low) you can accelerate, but if you're directly setting speed it is ignored.

AffectedByGravity. This is a boolean that controls if gravity is applied to an object.

PushedByGeometry. This is a boolean that controls if an object stops when they collide with geometry. If false, the object will pass through and send a collision event.


## BoundingBox
This is the main object for stuff on the map. It extends MovingBoundingObject. BoundingBoxes have height and width, as well as precalculating halfHeight and halfWidth as they get used a lot. Their origin is in the center of the box. Since these are the actual classes that you'll be instantiating, they have getDrawOriginX() and getDrawOriginY(), which accept tile size in pixels and return the pixel location to actually draw the box (Java draws from the top right corner).

They also have isColliding() and pointIsIn() to test collision with MovingBoundingObjects and points, respectively.

## FollowingBB
These are BoundingBoxes that who's position is relative to another BoundingBox, its leader. You generally don't directly modify thier loc, you use setRelativeLoc() and getRelativeLoc().

## Collisions
There are two types of collisions; those between MovingBoundingObjects that are pushed by geometry against geometry, and everything else. MovingBoundingObjects with pushedByGeometry == true are pushed when the engine processes this type of collision, and no event is triggered.

For all other collisions (MovingBoundingObjects with pushedByGeometry == false against geometry, and two MovingBoundingObjects colliding), each object throws an event. You will want to set up listeners for this.

The engine maintains five lists, which dictates what objects on that list can collide with:
PLAYER: Collides with ENEMY, ENEMY_PROJECTILE, ENVIRONMENT, and geometry.
PLAYER_PROJECTILE: Collides with ENEMY, ENVIRONMENT, and geometry.
ENEMY: Collides with PLAYER, PLAYER_PROJECTILE, ENVIRONMENT, and geometry.
ENEMY_PROJECTILE: Collides with PLAYER, ENVIRONMENT, and geometry.
ENVIRONMENT: Collides with everything, including other ENVIRONMENT objects.

These events are MovingCollision objects. They will always have a source, the object making the event. If it's a non-geometric collision, they will have an otherObject, the thing they bumped into. Otherwise, otherObject will be null.

To be clear, when a and b collide, there will be two events; one with source = a, otherObject = b and one with source = b, otherObject = a. The engine has no concept of iFrames, so that might be something you'll need in your project.

## Hitscans
A hitscan is when you figure out where the first collision would occur on a given vector (a ray with a defined length). The PhysicsUnlocedEngine.calculateHitscan() will return a HitscanResult object. It culls what it cannot hit by the same object types listed under Collisions, above. A HitscanResult object contains the following:
MovingBoundingObject movingObject: The MovingBoundingObject that got hit, or null if none.
DoublePair pointOfImpact: The x, y coordinates (in tiles) of where the ray terminated.
boolean geometryImpact: True if the ray terminated early by hitting geometry, else false.
boolean movingObjectImpact: True if the ray terminated early by hitting a MovingBoundingObject, else false.

## GeometryType
This enumerator holds the tile types for geometry. They are as follows:
EMPTY. This type does not block movement or collide.
FULL. This type blocks movement and collides.
BLOCKS_RIGHT. This type blocks movement from the left, and triggers collisions if the object is moving to the right.
BLOCKS_LEFT. This type blocks movement from the right, and triggers collisions if the object is moving to the left.
BLOCKS_UP. This type blocks movement from the bottom, and triggers collisions if the object is moving up.
BLOCKS_DOWN. This type blocks movement from the top, and triggers collisions if the object is moving down.

# Implementation

##class BoundingBox extends MovingBoundingObject

BoundingBox(double width, double height)
*Constructor. Creates a bounding box of the indicated width and height, in tiles.*

DoublePair getSize()
double getWidth()
double getHeight()
DoublePair getHalfSize()
double getHalfWidth()
double getHalfHeight()
*Standard getters.*

void setWidth(double w)
void setHeight(double h)
void setSize(double width, double height)
void setSize(DoublePair size)
*Standard setters.*

int getDrawOriginX(int tileSizePixels)
int getDrawOriginY(int tileSizePixels)
*As all values are kept at double precision, and there's some fudging we need to do when going to int precision for drawing, that is handled by these functions. If you just cast the location to ints, you'll be one pixel up and left of where you actually want to be. Additionally, these return the location of the upper-right corner, acknowledging conventions of java.awt.Graphics.*

boolean isColliding(BoundingObject that)
*Returns true if these object overlap, else false.*

boolean pointIsIn(double x, double y)
boolean pointIsIn(DoublePair point)
*Returns true if the passed point lies inside this box, else false.*

##abstract class BoundingObject

BoundingObject()
*Basic constructor.*

double getXLoc()
double getYLoc()
DoublePair getLoc()
*Basic getters. Location is in tiles, and refers to the centerpoint of the object.*

void setXLoc(double x)
void setYLoc(double y)
void setLoc(double x, double y)
void setLoc(DoublePair p)
*Basic getters. Location is in tiles, and refers to the centerpoint of the object.*

abstract boolean isColliding(BoundingObject that);
abstract boolean pointIsIn(double x, double y);
abstract boolean pointIsIn(DoublePair point);
abstract double getHalfWidth();
abstract double getHalfHeight();
abstract double getWidth();
abstract double getHeight();
*Abstract methods that child classes must implement.*

##class DoublePair
*A convient way to bundle related pairs of doubles. In most places this represents Cartesian coordinates at double precision, but can also be used to hold mathematical vectors (angle and magnitude pairs).*

final static double FULL_CIRCLE
final static double THREE_QUARTER_CIRCLE
final static double HALF_CIRCLE
final static double QUARTER_CIRCLE
final static double EIGHTH_CIRCLE
*Constants for common values. Angles are in radians, rotation is counter-clockwise.*

double x
*The x component of this object.*

double y
*The y component of this object.*

DoublePair(double xVal, double yVal)
*Basic constructor.*

DoublePair()
*Empty constructor, initializes to 0.0, 0.0.*

DoublePair(DoublePair that)
*Copy constructor.*

void add(DoublePair that)
*Adds that DoublePair's values to this one's, does not alter that.*

void subtract(DoublePair that)
*Subtracts that DoublePair's values from this one's, does not alter that.*

String serialize()
*String representation of this object. Only displays three decimals of precision for readability.*

void rotate(double theta)
*Rotates this objects values relative to 0.0, 0.0. For example, if the initial values are 1.0, 0.0 (straight right), and it is rotated by QUARTER_CIRCLE, the values will then be 0.0, -1.0 (stright up).*

double getAngle()
*Returns the angle (in radians) from 0.0, 0.0 to the x, y values of this object.*

double getMagnitude()
*Returns the distance from 0.0, 0.0 to the x, y values of this object.*

boolean equals(DoublePair that)
boolean equals(DoublePair that, double threshold)
*Returns whether the x, y values of that are equal to the x, y values of this. Threshold is a level of precision; .01 = .015 if threshold is .01, for example. Since we're doing double math, we need fuzzy matching. If no threshold is provided, .001 is used.*

static DoublePair sum(DoublePair a, DoublePair b)
*Returns a new DoublePair that is the sum of a and b.*

static DoublePair difference(DoublePair a, DoublePair b)
*Returns a new DoublePair that is the difference between a and b.*

static DoublePair getFromAngle(double theta)
*Returns a new DoublePair representing the point at angle theta and magnitude 1.0.*

static double simplifyAngle(double angle)
*Returns a value which is the angle bound in the range of 0.0, 2 * PI. For example, -1 radian would come back as (2 * PI) - radian, and a full circle + 1 radian would come back as 1 radian.*


##class FollowingBB extends BoundingBox
*This is a bounding box that follows some other MovingBoundingObject. The FollowingBB's loc variable is relative to the leader's loc variable. Setting the loc will change its relative position, and getting the loc will return the sum of this.loc and leader.loc.*

FollowingBB(double width, double height, MovingBoundingObject leader)
*Constructor. In addition to the height and width for a standard BoundingBox, another MovingBoundingObject is passed in as the leader.*

MovingBoundingObject getLeader()
*Standard getter.*

double getRelativeXLoc()
double getRelativeYLoc()
DoublePair getRelativeLoc()
*This returns the object's location relative to the leader.*

void setLeader(MovingBoundingObject l)
*Standard setter.*

void setRelativeXLoc(double x)
void setRelativeYLoc(double y)
void setRelativeLoc(double x, double y)
void setRelativeLoc(DoublePair l)
*These set the location, relative to the leader.*

double getXLoc()
double getYLoc()
DoublePair getLoc()
*These return the absolute position of this object.*

void setXLoc(double x)
void setYLoc(double y)
void setLoc(double x, double y)
void setLoc(DoublePair p)
*These set the absolute position of this object. Its new relative position is calculated from this.*


##enum GeometryType
*An enumerator used for different types of geometry tiles.*

EMPTY
*A fully passable tile.*

FULL
*A fully impassable tile.*

BLOCKS_RIGHT
*A tile that is impassable if the moving object is moving to the right, else passable.*

BLOCKS_LEFT
*A tile that is impassable if the moving object is moving to the left, else passable.*

BLOCKS_UP
*A tile that is impassable if the moving object is moving up, else passable.*

BLOCKS_DOWN
*A tile that is impassable if the moving object is moving down, else passable.*

boolean variableCollision
*False for EMPTY and FULL, else true.*


##class HitscanResult
*Holds the results of an instantaneous scan of a ray. The ray might terminate early on a movingBoundingObject, geometry, or go its full distance.*

MovingBoundingObject getMovingObject()
*If the ray struck a movingBoundingObject before anything else, this is that object. Else null.*

DoublePair getPointOfImpact()
*If the ray struck something, this point where that happened. Else the sum of the origin and distance.*

boolean isGeometryImpact()
*If the ray struck something, this point where that happened. Else the sum of the origin and distance.*

boolean isMovingObjectImpact()
*True if the scan hit a movingBoundingObject, else false.*

HitscanResult(DoublePair origin, DoublePair distance, PhysicsUnlockedEngine engine, int team)
*Calculate the hitscan. The team value here refers to the constants listed in PhysicsUnlockedEngine.*

##class Line
*This class represents a mathematical line, in the form of y = mx + b. As this form cannot represent a vertical line (where the slope is infinite), Double.MAX_VALUE is used in that case.*

Line(DoublePair origin, DoublePair slope)
*Constructor. Sets the line from a point, and a slope (change in x, change in y).*

static Line getFromPoints(DoublePair pointA, DoublePair pointB)
*Factory function. Creates a line from two points.*

double getM()
double getSlope()
*Returns the slope of the line.*

double getB()
double getIntercept()
*Returns the intercept of the line.*

DoublePair getPoint()
*Returns a point which is on the line.*

boolean isVertical()
*True if the slope is infinite, else false.*

boolean hasIntersection(Line that)
*Returns true if this line and that line intersect, else false. All lines intersect unless their slopes are identical (ie, they are parallel [zero intersections] or colinear [infinite intersections]).*

DoublePair getIntersection(Line that)
*Returns the point at which this line and that line intersect.*

double[] getStandardForm()
*Returns an array representing the standard form rather than the slope-intercept form ({m, -1.0, -b}).*

boolean pointIsBelow(DoublePair thatPoint)
*Returns true if the passed point is below the line.*

boolean pointIsAbove(DoublePair thatPoint)
*Returns true if the passed point is below the line.*

double getYAtX(double thatX)
*Returns the y value of the line at the given x value.*


##abstract class MovingBoundingObject extends BoundingObject implements MovingCollidable
*An abstract class for moving bounding objects. Speeds are in tiles per second. Acceleration and deceleration are in tiles per second per second.*

MovingBoundingObject()
*Simple constructor.*

double getXSpeed()
double getYSpeed()
DoublePair getSpeed()
double getXAcceleration()
double getYAcceleration()
DoublePair getAcceleration()
*Basic getters.*

double getXDeceleration()
double getYDeceleration()
DoublePair getDeceleration()
*Basic getters. Decelration is only applied if the object has 0.0 for acceleration on that axis. Deceleration is an unsigned value, and moves the speed towards 0.0.*

double getXMaxSpeed()
double getYMaxSpeed()
DoublePair getMaxSpeed()
*Basic getters. Max speed is a cap on acceleration; using setSpeed() ignores maxes.*

boolean isAffectedByGravity()
*True if the object is affected by gravity, else false.*

boolean isPushedByGeometry()
*True if the object is prevented from entering solid geometry.*

abstract int getDrawOriginX(int tileSizePixels)
abstract int getDrawOriginY(int tileSizePixels)
*Returns the location to start drawing the shape, in pixels.*

synchronized void setXSpeed(double x)
synchronized void setYSpeed(double y)
synchronized void setSpeed(double x, double y)
synchronized void setSpeed(DoublePair dp)
synchronized void setXAcceleration(double x)
synchronized void setYAcceleration(double y)
synchronized void setAcceleration(double x, double y)
*Basic setters.*

synchronized void setXDeceleration(double x)
synchronized void setYDeceleration(double y)
synchronized void setDeceleration(double x, double y)
*Basic setters. Decelration is only applied if the object has 0.0 for acceleration on that axis. Deceleration is an unsigned value, and moves the speed towards 0.0.*

synchronized void setXMaxSpeed(double x)
synchronized void setYMaxSpeed(double y)
synchronized void setMaxSpeed(double x, double y)
*Basic setters. Max speed is a cap on acceleration; using setSpeed() ignores maxes.*

synchronized void setAffectedByGravity(boolean a)
*True if the object is affected by gravity, else false.*

synchronized void setPushedByGeometry(boolean c)
*True if the object is prevented from entering solid geometry.*

synchronized void applyXImpulse(double x)
*Adjust the x speed.*

synchronized void applyYImpulse(double y)
*Adjust the y speed.*

synchronized void applyImpulse(double x, double y)
*Adjust the x and y speeds.*

synchronized void applySpeeds(double secondsElapsed)
*Apply the speeds to the position. Let the engine do this.*

synchronized void applyGravityImpulse(double speed, double terminalVelocity)
*Gravity is like other y-impulses, except limited by terminalVelocity.*

synchronized int[] getPotentialCollisionOrigin(double seconds)
*Returns the upper-left tile location (x, y) to begin seraching for geometric collisions.*

synchronized int[] getPotentialCollisionEnd(double seconds)
*Returns the lower-right tile location (x, y) to end seraching for geometric collisions.*

synchronized void adjustForCollision(SweptAABB collision)
*Adjust speeds to stop short of collision.*

synchronized void applyAccelerations(double secondsElapsed)
*Apply the accelerations to speed. Let the engine do this.*

void movingCollisionOccured(MovingCollision mc)
*Notifies listeners that a collision has occured.*

void addCollisionListener(MovingCollisionListener listener)
*Add a listener to be notified when a collision occurs.*

void removeCollisionListener(MovingCollisionListener listener)
*Remove a listener.*

##interface MovingCollidable
*An interface for moving objects that can collide. The implementation for this is not explicitly threaded; it occurs on the engine's thread.*

void movingCollisionOccured(MovingCollision mc)
*Notifies listeners that a collision has occured.*

void addCollisionListener(MovingCollisionListener listener)
*Add a listener to be notified when a collision occurs.*

void removeCollisionListener(MovingCollisionListener listener)
*Remove a listener.*


##class MovingCollision
*A class for bundling up information about a non-push collision.*

MovingCollision(MovingBoundingObject s, MovingBoundingObject mbo)
*Constructor. For MBO on geometry collisions, the second argument is null.*

MovingBoundingObject getSource()
*Returns the first movingBoundingObject in the collision.*

MovingBoundingObject getOtherObject()
*Returns the second movingBoundingObject in the collision, or null if there is no such object.*

boolean isGeometryCollision()
*True if this is a collision with geometry, else false. Logically equivalent to getOtherObject() == null.*

boolean isNonGeometryCollision()
*True if this is a collision with a movingBoundingObject, else false. Logically equivalent to getOtherObject() != null.*


##interface MovingCollisionListener
*An interface for object to be notified when non-push collisions occur.*

void movingCollisionOccured(MovingCollision mc)
*Function to be called when a collision happens.*


##class PhysicsUnlockedEngine implements Runnable
*The physics engine. Manages movement and speeds for the objects given to it, taking into account the geometry given to it. Some public methods have not been listed here, in the case that they are only marked public for testing reasons.

static final int PLAYER
static final int PLAYER_PROJECTILE
static final int ENEMY
static final int ENEMY_PROJECTILE
static final int ENVIRONMENT
*Constants for culling potential collisions and hitscan results.*

PhysicsUnlockedEngine()
*Basic constructor. runFlag is initially set to false, you will need to set it to true to being the engine.*

double getGravity()
double getTerminalVelocity()
Vector<MovingBoundingObject> getObjList(){return objList;}
GeometryType[][] getGeometry()
boolean getRunFlag()
*Basic getters.*

int getCPS()
*The engine keeps tracks of how many cycles per second it executes. This is the getter for that number, which is updated once per second.*

void setGravity(double g)
void setTerminalVelocity(double t)
void setObjList(Vector<MovingBoundingObject> newList)
void setGeometry(GeometryType[][] g)
*Basic setters.*

void setRunFlag(boolean rf)
*Setter for the run flag. If this is false, the engine will idle until it is turned back to true.*

void terminate()
*This allows the engine thread to terminate.*

void add(MovingBoundingObject obj)
void add(MovingBoundingObject obj, int list)
*Adds a movingBoundingObject to have physics done to it. If no list is specified (see the constants for this class), ENVIRONMENT is the default.*

void remove(MovingBoundingObject obj)
*Removes a movingBoundingObject.*

boolean touchingFloor(MovingBoundingObject obj)
*Returns true if the object is touching geometry in the Y+ direction.*

boolean touchingCeiling(MovingBoundingObject obj)
*Returns true if the object is touching geometry in the Y- direction.*

boolean touchingLeftWall(MovingBoundingObject obj)
*Returns true if the object is touching geometry in the X- direction.*

boolean touchingRightWall(MovingBoundingObject obj)
*Returns true if the object is touching geometry in the X+ direction.*

DoublePair getOrthoGeometryCollisionNormals(MovingBoundingObject obj)
*Returns a pair of normals, checking one tile up, down, left, and right. Not intended for sizes greater than 1.0.*

boolean isCollidingWithGeometry(MovingBoundingObject obj)
*Returns true if the object is overlapping geometry.*

HitscanResult calculateHitscan(DoublePair origin, DoublePair distance)
HitscanResult calculateHitscan(DoublePair origin, DoublePair distance, int scanType)
*Does a hitscan. If no scanType is specified (see constants for this class), ENVIRONMENT is defaulted.*

MovingBoundingObject getHitscanImpact(DoublePair origin, DoublePair distance)
MovingBoundingObject getHitscanImpact(DoublePair origin, DoublePair distance, int scanType)
*Does a hitscan, returning the movingBoundingObject hit or null if no such hit occured. If no scanType is specified (see constants for this class), ENVIRONMENT is defaulted.*

DoublePair getHitscanImpactGeometry(DoublePair origin, DoublePair distance)
*Does a hitscan and returns a point within the tile hit. This position is relative to the origin, not an absolute position.*

boolean isInBounds(int x, int y)
*Returns true if the x, y index passed in is within the geometry array, else false.*

static double getDistanceMetric(DoublePair boxLoc, DoublePair loc)
*Returns a^2 + b^2 for quickly comparing distances.*

GeometryType getGeometryType(int x, int y)
*Returns the GeometryType of the indexed tile, or GeometryType.FULL if the index is out of bounds.*

boolean pointCollidesWithGeometry(DoublePair point)
*Returns true if the point collides with the GeometryType of the tile in which it lies, else false.*

boolean pointCollidesWithGeometry(DoublePair point, int x, int y)
*Returns true if the point collides with the GeometryType of the indexed tile, else false.*


##class SweptAABB
*This is an implementation of swept axis-aligned bounding boxes, also called projected rectangle collision.

All calculations are done when the constructor is called. The object is essentially the return value of a function. 

The values normalX and normalY are, well, normals. If no collision occured, they'll both be zero, otherwise they represent "pushback" along their respective axis.

Time represents the portion of the distance the object would travel that occurs before a collision. So if the objects are already in collision, this would be 0.0. If no collision occurs, this will be 1.0. If a collision would occur at three-quarters of the distance the object is trying to move, this number would be .75 regardless of what the attempted distance would be.*

SweptAABB()
*Empty constructor. Since all the work is done upon instantiation, there's not much reason to call this.*

SweptAABB(DoublePair point, DoublePair distance, DoublePair boxOrigin, DoublePair boxSize)
SweptAABB(DoublePair point, DoublePair distance, DoublePair boxOrigin, DoublePair boxSize, GeometryType type)
*A scan between two boxes. If no GeometryType is passed in, GeometryType.FULL is the default. The default is the appropriate value for non-geometric collisions.*

SweptAABB(DoublePair point, DoublePair distance, MovingBoundingObject obj)
*A scan between a single point, and a movingBoundingObject. This is mostly used for hitscans on movingBoundingObjects.*

SweptAABB(MovingBoundingObject obj, double secondsElapsed, int geometryX, int geometryY)
SweptAABB(MovingBoundingObject obj, double secondsElapsed, int geometryX, int geometryY, GeometryType type)
*A scan a movingBoundingObject and geometry. If no GeometryType is passed in, GeometryType.FULL is the default.*

// for hitscan
SweptAABB(DoublePair origin, DoublePair distance, int geometryX, int geometryY)
SweptAABB(DoublePair origin, DoublePair distance, int geometryX, int geometryY, GeometryType type)
*A scan hitscans and geometry. If no GeometryType is passed in, GeometryType.FULL is the default.*

int getNormalX()
*-1 if colliding with the left side of a box, 1 if colliding with the right side, else 0.*

int getNormalY()
*-1 if colliding with the top side of a box, 1 if colliding with the bottom side, else 0.*

double getTime()
*Returns the portion of the movement or distance at which the collision occurs, scaled to [0.0, 1.0]. A time of 0.0 means the objects are already in collision. A time of 1.0 means no collision occurs. A time of .75 means that a collision occurs at three-quarters of the full distance.*

boolean isCollision()
*True if a collision occurs. Logically equivalent to getTime() < 1.0.*

DoublePair getCollisionLoc()
*Returns the x, y coordinates at which the collision occurs. In the case where the moving object is a box, this point is on a line drawn from the center of the box in the direction of movement.*








boolean shouldIgnore(DoublePair point, DoublePair boxOrigin, DoublePair boxSize, GeometryType type)


String serialize()
