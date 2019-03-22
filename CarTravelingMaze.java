package ijev3as2;


import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.*;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.mapping.*;
import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.*;
import lejos.utility.Delay;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class CarTravelingMaze {
    //Stack<>
	static Car car = new Car();
    static Maze maze = new Maze(13, 19);
//	// nodes and links define the physical creation of your Graph
//	ArrayList<Node> nodes;
//	ArrayList<Link> links;

	// Two lists used for traversing
	ArrayList<Node> dfsTraverse;
	Stack<Link> travelStack = new Stack<>();

	// List used to define where you would like to move
	ArrayList<Node> dfsPath = new ArrayList<>();
	
	/**
	 * The stack containing the portals to backtrack through when all other
	 * doorways of the current chamber have been explored (see assignment
	 * handout). Note that in Java, the standard collection class for both
	 * Stacks and Queues are Deques
	 */
	private static Deque<Node> visitStack = new ArrayDeque<>();
	private static Deque<Link> visitLinkStack = new ArrayDeque<>();
	/**
	 * The set of portals that have been explored so far.
	 */
	static Set<Node> visited = new HashSet<>();
	/**
	 * The Queue that contains the sequence of portals that the Drone has
	 * followed from the start
	 */
	static Deque<Node> visitQueue = new ArrayDeque<>();
	static Deque<Link> visitLinkQueue = new ArrayDeque<>();
	
	
	
	static int heading = 0;
	static int i, j;
	static boolean hasNorthWall = false;
    static boolean hasEastWall = false;
    static boolean hasWestWall = false;
    public static void main(String[] args) {        
        //float distance;
       
        i = 1;
        j = 1;
        //int tmpHeading = 0; 
        visited.add(maze.nodes[1][1]);
        visitStack.push(maze.nodes[1][1]);
        while(car.isEscapeUp()) {
        	maze.nodes[i][j].setPoint(car.navi.getWaypoint());
        	maze.nodes[i][j].setCondition(0);
        	// if the codition is not -1, that means the position has been visited
        	while (!car.isRed2() || visited.size() != 216) {
        		visited.add(maze.nodes[i][j]);
        		visitQueue.addLast(maze.nodes[i][j]);
        		visitStack.pop();
        		if (car.isRed2()) {
        			maze.nodes[i][j].isDestination = true;
        			// tell whether we could find the best path now
        			// if yes,just do below
        			findShortestPath();
        			// if not, continue searching but mark the position of this point
        		}
        		if (car.isGreen2()) {
        			while (!maze.nodes[i][j].isRoot) {
        				backtrack();
        			}
        			while (maze.nodes[i][j].unvisitedChild == 0) {
        				backtrack();
        			}
        			if (maze.nodes[i][j].isWestAvail) {
        				goToWest();
        				break;
        			}
        			if (maze.nodes[i][j].isEastAvail) {
        				goToEast();
        				break;
        			}
        			continue;
        		}
        		checkAround(); 
        		
        		if (!hasNorthWall) {  
                	goToNorth(); 
                	break;
                } else if (!hasWestWall) {
                	goToWest();
                	break;
                } else if (!hasEastWall) {
                	goToEast();
                	break;
                } else {
                	backtrack();
                	break;

                }
        	}  
        }
    }

	private static void checkAround() {
		float distance;
		
		LCD.drawString("heading: "+ heading, 0, 1);            
		// checking the north
		Delay.msDelay(2000);
		distance = car.getUltrasp();
		hasNorthWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			hasNorthWall = true;
			setNeighValue(0, 1); 
			
		} else {
			setNeighValue(0, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isNorthAvail = true;
		}          
		
		        
		distance = 0;
		// checking the west
		car.turnWest1();
		Delay.msDelay(3000);
		distance = car.getUltrasp();
		hasWestWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			hasWestWall = true;
			setNeighValue(2, 1);
		} else {
			setNeighValue(2, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isWestAvail = true;
		}            
		Delay.msDelay(2000);
		car.turnWest2();
		
		distance = 0;
		// checking the east
		car.turnEast1();
		Delay.msDelay(3000);
		distance = car.getUltrasp();
		hasEastWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			hasNorthWall = true;
			setNeighValue(3, 1);
		} else {
			setNeighValue(3, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isEastAvail = true;
		}           
		Delay.msDelay(2000);
		car.turnEast2();
		Delay.msDelay(2000);    
	}

	private static void goToNorth() {
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isNorthAvail = false;
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		Delay.msDelay(2000);
		updatePosition(0, 2);
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		updateHeading(0);
		car.pilot.travel(40);
	}

	private static void goToWest() {
		car.pilot.rotate(-90);
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		Delay.msDelay(2000);
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isWestAvail = false;
		updatePosition(2, 2);
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		updateHeading(2);
		car.pilot.travel(40);
	}

	private static void goToEast() {
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		updatePosition(3, 2);
		LCD.drawString("i: " + i, 3, 0);
		LCD.drawString("j: "+j, 3, 0);
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isEastAvail = false;
		updateHeading(3);
		car.pilot.rotate(90);
		car.pilot.travel(40);
	}

    private static void backtrack() {
		// TODO Auto-generated method stub
    	LCD.drawString("i: " + i, 3, 0);
    	LCD.drawString("j: "+j, 3, 0);
    	Delay.msDelay(2000);
    	car.pilot.rotate(180);
    	car.pilot.travel(40);
    	updatePosition(1, 2);
    	LCD.drawString("i: " + i, 3, 0);
    	LCD.drawString("j: "+j, 3, 0);
    	updateHeading(1);
	}

    
    public static void findShortestPath() {
		
	}
    
	public static int updateHeading(int newHeading) {
        heading = newHeading % 4;
        while (heading < 0) {
            heading+=4;
        }
        return heading;
    }
    
    public static void setNeighValue(int position, int value) {
    	switch (heading){
    	case 0:
    		switch (position) {
    		case 0:
    			maze.nodes[i][j+1].setCondition(value);
    			//visited.add(maze.nodes[i][j+1]);
    			break;
    		case 1:
    			maze.nodes[i][j-1].setCondition(value);
    			//maze.nodes[i][j-1].setParent(maze.nodes[i][j]);
    			break;
    		case 2:
    			maze.nodes[i-1][j].setCondition(value);
    			//maze.nodes[i-1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 3:
    			maze.nodes[i+1][j].setCondition(value);
    			//maze.nodes[i+1][j].setParent(maze.nodes[i][j]);
    			break;
    		}
    		break;
    	case 1:
    		switch (position) {
    		case 0: 
    			maze.nodes[i][j-1].setCondition(value);
    			//maze.nodes[i][j-1].setParent(maze.nodes[i][j]);
    			
    			break;
    		case 1:
    			maze.nodes[i][j+1].setCondition(value);
    			//maze.nodes[i][j+1].setParent(maze.nodes[i][j]);
    			break;
    		case 2:
    			maze.nodes[i+1][j].setCondition(value);
    			//maze.nodes[i+1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 3:
    			maze.nodes[i-1][j].setCondition(value);
    			//maze.nodes[i-1][j].setParent(maze.nodes[i][j]);
    			break;
    		}
    	case 2: 
    		switch (position) {
    		case 0:     			
    			maze.nodes[i-1][j].setCondition(value);
    			//maze.nodes[i-1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 1:
    			maze.nodes[i+1][j].setCondition(value);
    			//maze.nodes[i+1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 2:
    			maze.nodes[i][j-1].setCondition(value);
    			//maze.nodes[i][j-1].setParent(maze.nodes[i][j]);
    			break;
    		case 3:
    			maze.nodes[i][j+1].setCondition(value);
    			//maze.nodes[i][j+1].setParent(maze.nodes[i][j]);
    			break;
    		}    		
    	case 3: 
    		switch (position) {
    		case 0:     			
    			maze.nodes[i+1][j].setCondition(value);
    			//maze.nodes[i+1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 1:
    			maze.nodes[i-1][j].setCondition(value);
    			//maze.nodes[i-1][j].setParent(maze.nodes[i][j]);
    			break;
    		case 2:
    			maze.nodes[i][j+1].setCondition(value);
    			//maze.nodes[i][j+1].setParent(maze.nodes[i][j]);
    			break;
    		case 3:
    			maze.nodes[i][j-1].setCondition(value);
    			//maze.nodes[i][j-1].setParent(maze.nodes[i][j]);
    			break;
    		}	
    	}
    	
    			
    }

    public static void updatePosition(int newHeading, int step) {
        switch (heading) {
        case 0:
	        switch (newHeading) {
	        	case 0: // robot moves north
	                j += step;
	                break;
	            case 1: // robot moves south
	                j -= step;
	                break;
	            case 2: // robot moves west
	                i -= step;
	                break;
	            case 3: // robot moves east
	                i += step;
	                break;
	        	}
        case 1: 
        	switch (newHeading) {
        	case 0: // robot moves north
                j -= step;
                break;
            case 1: // robot moves south
                j += step;
                break;
            case 2: // robot moves west
                i += step;
                break;
            case 3: // robot moves east
                i -= step;
                break;
        	}
        case 2:
        	switch (newHeading) {
        	case 0: // robot moves north
                i -= step;
                break;
            case 1: // robot moves south
                i += step;
                break;
            case 2: // robot moves west
                j -= step;
                break;
            case 3: // robot moves east
                j += step;
                break;
        	}
        case 3:
        	switch (newHeading) {
        	case 0: // robot moves north
                i += step;
                break;
            case 1: // robot moves south
                i -= step;
                break;
            case 2: // robot moves west
                j += step;
                break;
            case 3: // robot moves east
                j -= step;
                break;
        	}
        }
       
    }

    
    private static int[] getCoordinates() {
		int[] coords = new int[2];
        coords[0] = i;
        coords[1] = j;
        return coords;
	}

	public void dfsTraverse(Node from, Node to) {
		boolean matched;
		Link found;

		// determine if there is a link between from and to
		// if there is a match then add the link to the travelStack and
		// add the nodes to dfsPath
		// This will ultimately repeated by the end of the search

		matched = match(from, to);
		if (matched) {
			travelStack.push(new Link(from, to));
			dfsPath.add(new Node(to.getX(), to.getY()));
			dfsPath.add(new Node(from.getX(), from.getY()));
			return;
		}

		// if there is no match found you could another path findings
		found = find(from);

		// if you find a new connection then you could add it to the travelStack
		// and
		// and the start node to dfsPath
		// recursively call dfsTraverse with the link's to as start and our
		// destination as the end

		if (found != null) {
			travelStack.push(new Link(from, to));
			dfsTraverse(found.to, to);
			dfsPath.add(new Node(from.getX(), from.getY()));
		}

		// backtrack if you cannot find a new connection
		else if (travelStack.size() > 0) {
			found = travelStack.pop();
			dfsTraverse(found.from, found.to);
			dfsPath.add(new Node(from.getX(), from.getY()));
		}
	}// end dfsTraverse()

    
 // match() method is used to determine if there is a link between a starting
 	// node and an ending node

 	public boolean match(Node from, Node to) {

 		// iterate through list of links
 		for (int a = links.size() - 1; a >= 0; a--) {
 			if (links.get(a).from.equals(from) && links.get(a).to.equals(to)
 					&& !links.get(a).skip) {
 				links.get(a).skip = true;
 				return true;
 			}
 		}
 		return false;
 	}// end match()
 	
 // find() method is used to
 	// find the next link to try exploring

 	public Link find(Node from) {

 		// iterate through the list of links
 		for (int a = 0; a < links.size(); a++) {
 			// link found
 			if (links.get(a).from.equals(from) && !links.get(a).skip) {
 				Link foundList = new Link(links.get(a).from,
 						links.get(a).to);
 				// mark this link as used so we don't match it again
 				links.get(a).skip = true;
 				return foundList;
 			}
 		}
 		return null; // not found
 	}// end find()
    
	private static boolean tellWall(float distance) {
		boolean hasWall;
		if (distance <= 0.20 && distance >= 0) {
			hasWall = true;
			return hasWall;
		} else {
			hasWall = false;
			return hasWall;
		}
	}

}



