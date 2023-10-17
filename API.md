#API

## General Conventions
This library has a couple of constant conventions. Time is measured in seconds. Distance is measured in tiles. Therefore speeds are in tiles per second, and accelerations in tiles per second per second.

For the most part, this library doesn't know about display sizes and doesn't want to know. The exception is MovingBoundingObject.getDrawOriginX() and MovingBoundingObject.getDrawOriginY(). We need to make some small adjustments when converting between tiles (which are in double precision) and pixels (which are in int precision), so those functions take care of that.

All objects with some sort of size consider the center of their shape to be their origin.

## PhysicsUnlockedEngine
This is the workhorse class. It does its calculations as frequently as it can, up to 1000 Hz. It mainly needs two things; a 2D boolean array for geometry, where true means solid and false means non-solid; and MovingBoundingObject which are the stuff that moves around and collides.

The engine can be paused with setRunFlag(), or terminated with terminate().

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
This is the main object for stuff on the map. It extends MovingBoundingObject. BoundingBoxes have height and width, as well as precalculating halfHeight and halfWidth as they get used a lot. Their origin is in the center of the box. Since these are the actual object classes that you'll be instantiating, they have getDrawOriginX() and getDrawOriginY(), which accept tile size in pixels and return the pixel location to actually draw the box (Java draws from the top right corner).

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