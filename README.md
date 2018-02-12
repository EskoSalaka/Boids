# Boids

Boids is a simple flocking simulator named after Greg Reynolds' "Boids" [https://en.wikipedia.org/wiki/Boids]() , which aims to simulate flocking behaviour of birds.

Shortly, the flocking behaviour in this app is based on three seperate forces acting on each boid (birdoid object). The forces are, as described in Wikipedia:

* separation: steer to avoid crowding local flockmates
* alignment: steer towards the average heading of local flockmates
* cohesion: steer to move toward the average position (center of mass) of local flockmates

The boids are basically simple vehicles with certain velocity, mass, strengh, vision distance and other related
characteristics.

<img src="git_images\Capture1.JPG">

## Requirements and installation

Only Scala and Swing are needed to run the app. Swing-jar is supplied with the repo.


## Features and usage

During the simulation, a number of factors and characteristics can be adjusted real-time. Changing these will affect the strengths of different kinds of forces and behaviour of the boids. These include:

* Cohesion, alignement and seperation factors
* Vision distance and angle, maximum speed, maximum force and vision distance
* Repulsion factor and distance for obstacles
* Repulsion/attraction factor and distance of mouse cursor

The app itself is relatively straightforward to use. Left-clicking will add more boids and right-clicking will add obstacles. The app can be paused and restarted with desired effects and tickrate can be adjusted. Finally, mouse cursor can be
made repulsive or attractive.