# PhysicsUnbound
A 2D physics engine with double precision and a cycle rate up to 1000 Hz. The name is a reflection that unlike its predecessor, it is not bound to framerate.

## Objective
The objective of this project is to provide a reasonably robust and clean physics engine for 2D games, both side-on and top-down. Further, it needs to be resistant to concurrency problems.

PhysicsUnbound is heavily inspired with my previous similar project, Physics2D. In the end, the interface was a bit cumbersome, and I wanted behavior to be as smooth as possible, meaning it needed to handle frames of arbitrary length.

Initially, I was going to support both axis-aligned bounding boxes and bounding circles. While I may add circles back in at some point, at this time there is little incentive to do so.
