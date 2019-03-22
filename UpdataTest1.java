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
import lejos.robotics.navigation.*;
import lejos.utility.Delay;

import java.io.*;
import java.net.*;
import lejos.hardware.Battery;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
public class UpdataTest1 {
	static Car car = new Car();
	public static final int port = 1234;
    static Maze maze = new Maze(19, 13); //13, 19
    static int heading = 0;
	static int i = 1;
	static int j= 1;
	static boolean hasNorthWall = false;
    static boolean hasEastWall = false;
    static boolean hasWestWall = false;
    static Stack<Node> visitstack = new Stack<Node>();  // u should new it first and then u could use the stack
    static Stack<Node> backstack = new Stack<Node>();
    static ServerSocket server = new ServerSocket(port);
    static Socket client = new server.accept();
	public static void main(String[] args) throws IOException{
		car.buttons.waitForAnyPress();
		
		System.out.println("Awaiting client..");
		
		//visitstack.push(maze.nodes[i][j]);
        while (car.isEscapeUp()) {
        	LCD.clear();
        	LCD.drawString("i"+i, 0, 0);
        	LCD.drawString("j"+j, 0, 1);
        	Delay.msDelay(3000);
        	LCD.clear();
        	LCD.drawString("heading:"+heading, 0, 4);
        	visitstack.push(maze.nodes[i][j]);
        	backstack.push(maze.nodes[i][j]);
        	maze.nodes[i][j].setCondition(0); //!!!!!!!!!!!
        	
        	if (car.isGreen2()) {
        		Delay.msDelay(2000);
        		car.pilot.stop();
        		if (!(i == 1 && j == 1)) {
        			backtrack();
        			continue;
        		} else {
        			car.pilot.stop();
        			System.exit(0);
        		}
        		break;
        	}
        	if (car.isRed2()) {
        		maze.nodes[i][j].isDestination = true;
        		gohome();
        		car.LEFT_MOTOR.stop();
        		car.RIGHT_MOTOR.stop();
        		System.exit(0);
        		
        	}
        	checkAround(); 
        	//visitstack.push(maze.nodes[i][j]);
        	if (!hasNorthWall) { 
        		LCD.clear();
        		LCD.drawInt(23333,0,0);
        		//visitstack.push(maze.nodes[i][j]);
//        		LCD.drawInt(visitstack.size(), 0, 1);
//        		LCD.drawInt(visitstack.peek().getX(), 0, 2);
        		//Delay.msDelay(5000);
            	goToNorth(); 
            	LCD.drawString("updated north", 0, 0);
            	LCD.drawString("updated heading: "+heading, 0, 1);
//            	LCD.clear();
            	LCD.drawString("new i: "+i, 0, 2);
            	LCD.drawString("new j: "+j, 0, 3);
            	Delay.msDelay(200);
            	continue;
           } else if (!hasWestWall) {
        	    //visitstack.push(maze.nodes[i][j]);
//        	    LCD.drawInt(visitstack.size(), 0, 1);
//        	    LCD.drawInt(visitstack.peek().getX(), 0, 2);
//        		Delay.msDelay(5000);
            	goToWest();
            	LCD.drawString("updated west", 0, 0);
            	LCD.drawString("updated heading: "+heading, 0, 1);
//            	LCD.clear();
            	LCD.drawString("new i: "+i, 0, 2);
            	LCD.drawString("new j: "+j, 0, 3);
            	Delay.msDelay(200);
            	continue;
            } else if (!hasEastWall) {
            	//visitstack.push(maze.nodes[i][j]);
//            	LCD.drawInt(visitstack.size(), 0, 1);
//            	LCD.drawInt(visitstack.peek().getX(), 0, 2);
//        		Delay.msDelay(5000);
            	
            	goToEast();
            	LCD.drawString("updatd east", 0, 0	);
            	LCD.drawString("updated heading: "+heading, 0, 1);
//           	LCD.clear();
            	LCD.drawString("new i: "+i, 0, 2);
            	LCD.drawString("new j: "+j, 0, 3);
            	Delay.msDelay(200);
            	continue;
           } else {
        	   //visitstack.push(maze.nodes[i][j]);
        	   LCD.drawString("tmd", 0, 8);
        	   backtrack();
        	   continue;

            }
        }
        LCD.clear();
        LCD.drawString("hhh i: "+i, 0, 2);
    	LCD.drawString("j: "+j, 0, 3);
    	Delay.msDelay(200);
    	car.buttons.waitForAnyPress();
	}
	
	
	
	 private static void gohome() {
		// TODO Auto-generated method stub
		 backstack.pop();
		 while (!visitstack.isEmpty()) {
			 
			 switch (heading) {
				case 0: 
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToNorth();
							//break;
						} else //if (j - 2 == visitstack.pop().getY()) 
						{
							goToSouth();
							//break;
						}
					}
					else if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToEast();
							//break;
						}
						else //if (i - 2 == visitstack.pop().getY()) 
							{
							goToWest();
							//break;
						}
					}
					break;
				case 1:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToSouth();
							//break;
						} else// if (j - 2 == visitstack.pop().getY()) 
							{
							goToNorth();
							//break;
						}
						
					}
					
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToWest();
							//break;
						}
						else //if (i - 2 == visitstack.pop().getY()) 
							{
							goToEast();
							//break;
						}
					}
					break;
				case 2:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToEast();
							//break;
						} else //if (j - 2 == visitstack.pop().getY()) 
							{
							goToWest();
							//break;
						}
					}
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToSouth();
							//break;
						}
						//if (i - 2 == visitstack.pop().getY()) 
						else {
							goToNorth();
							//break;
						}
					}
					break;
				case 3:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToWest();
							//break;
						} else //if (j - 2 == visitstack.pop().getY())
							{
							goToEast();
							//break;
						}
					}
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToNorth();
							//break;
						}
						//if (i - 2 == visitstack.pop().getY())
						else {
							goToSouth();
							//break;
						}
					}
				break;
				}
				continue;
			//}
			
		}
		 
	}



	private static void backtrack() {
		// TODO Auto-generated method stub
		 
//		goToSouth();
//		while (!maze.nodes[i][j].isRoot || (maze.nodes[i][j].isRoot && maze.nodes[i][j].unvisitedChild == 0)) {
//			goToSouth();
//		}
////		if (maze.nodes[i][j].isNorthAvail) {
////			goToNorth();
////		}
//		 if (maze.nodes[i][j].isWestAvail) {
//			goToWest();
//		}
//		else if (maze.nodes[i][j].isEastAvail) {
//			goToEast();
//		}
		visitstack.pop(); //!!!!!!!
		
		while (!visitstack.isEmpty() && (!maze.nodes[i][j].isRoot || (maze.nodes[i][j].isRoot && maze.nodes[i][j].unvisitedChild <= 0))) {
			//while (true) {
				switch (heading) {
				case 0: 
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToNorth();
							//break;
						} else //if (j - 2 == visitstack.pop().getY()) 
						{
							goToSouth();
							//break;
						}
					}
					else if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToEast();
							//break;
						}
						else //if (i - 2 == visitstack.pop().getY()) 
							{
							goToWest();
							//break;
						}
					}
					break;
				case 1:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToSouth();
							//break;
						} else// if (j - 2 == visitstack.pop().getY()) 
							{
							goToNorth();
							//break;
						}
						
					}
					
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToWest();
							//break;
						}
						else //if (i - 2 == visitstack.pop().getY()) 
							{
							goToEast();
							//break;
						}
					}
					break;
				case 2:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToEast();
							//break;
						} else //if (j - 2 == visitstack.pop().getY()) 
							{
							goToWest();
							//break;
						}
					}
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToSouth();
							//break;
						}
						//if (i - 2 == visitstack.pop().getY()) 
						else {
							goToNorth();
							//break;
						}
					}
					break;
				case 3:
					if (i == visitstack.peek().getX()) {
						if (j +2 == visitstack.pop().getY()) {
							goToWest();
							//break;
						} else //if (j - 2 == visitstack.pop().getY())
							{
							goToEast();
							//break;
						}
					}
					if (j == visitstack.peek().getY()) {
						if (i + 2 == visitstack.pop().getX()) {
							goToNorth();
							//break;
						}
						//if (i - 2 == visitstack.pop().getY())
						else {
							goToSouth();
							//break;
						}
					}
				break;
				}
				continue;
			//}
			
		}
		 
		if (maze.nodes[i][j].isRoot && maze.nodes[i][j].unvisitedChild != 0) {
			if (maze.nodes[i][j].isWestAvail) {
				goToEast();
				return;
			} 
			if (maze.nodes[i][j].isEastAvail) {
				goToWest();
				return;
			}
		}
		 
	 
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
	    		break;
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
	    		break;
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
	    		break;
	    	}
	    	
	    			
	    }

	
	public static int updateHeading(int direction) {
        switch (heading) {
        case 0:
        	switch (direction) {
        	case 0: 
        		heading = 0;
        		break;
        	case 1:
        		heading = 1;
        		break;
        	case 2:
        		heading = 2;
        		break;
        	case 3:
        		heading = 3;
        		break;
        	}
        	break;
        case 1:
        	switch (direction) {
        	case 0: 
        		heading = 1;
        		break;
        	case 1:
        		heading = 0;
        		break;
        	case 2: 
        		heading = 3;
        		break;
        	case 3:
        		heading = 2;
        		break;
        	}
        	break;
        case 2:
        	switch (direction) {
        	case 0:
        		heading = 2;
        		break;
        	case 1:
        		heading = 3;
        		break;
        	case 2:
        		heading = 1;
        		break;
        	case 3:
        		heading = 0;
        		break;
        	}
        	break;
        case 3:
        	switch (direction) {
        	case 0: 
        		heading = 3;
        		break;
        	case 1:
        		heading = 2;
        		break;
        	case 2:
        		heading = 0;
        		break;
        	case 3:
        		heading = 1;
        		break;
        	}
        	break;
        }
        
        return heading;
    }
	
	public static void updatePosition(int newHeading, int step) {
        switch (heading) {
        case 0:
	        switch (newHeading) {
	        	case 0: // robot moves north
	                j = j +step;

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
	        break;
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
        	break;
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
        	break;
        case 3:
        	switch (newHeading) {
        	case 0: // robot moves north
                i += step;

                break;
            case 1: // robot moves south
                i -= step;
//                LCD.clear();
//                LCD.drawString("i: "+i, 0, 0);
//                LCD.drawString("j" +j,0, 1);
                break;
            case 2: // robot moves west
                j += step;
//                LCD.clear();
//                LCD.drawString("i: "+i, 0, 0);
//                LCD.drawString("j" +j,0, 1);
                break;
            case 3: // robot moves east
                j -= step;
//                LCD.clear();
//                LCD.drawString("i: "+i, 0, 0);
//                LCD.drawString("j" +j,0, 1);
                break;
        	}
        	break;
        }
       
    }
	
	private static void goToNorth() {
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isNorthAvail = false;
//		LCD.drawString("i: " + i, 0, 2);
//		LCD.drawString("j: "+j, 0, 3);
		Delay.msDelay(200);
		updatePosition(0, 2);
//		LCD.drawString("i: " + i, 0, 4);
//		LCD.drawString("j: "+j, 0, 5);
		updateHeading(0);
		car.pilot.travel(40);
	}
	
	private static void goToSouth() {
		// TODO Auto-generated method stub
		car.pilot.rotate(180);
		updatePosition(1, 2);
		updateHeading(1);
		car.pilot.travel(40);
	}

	private static void goToWest() {
		car.pilot.rotate(-90);
//		LCD.drawString("i: " + i, 0, 3);
//		LCD.drawString("j: "+j, 0, 4);
		Delay.msDelay(200);
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isWestAvail = false;
		updatePosition(2, 2);
//		LCD.drawString("i: " + i, 0, 5);
//		LCD.drawString("j: "+j, 0, 6);
		updateHeading(2);
		car.pilot.travel(40);
	}

	private static void goToEast() {
//		LCD.drawString("i: " + i, 0, 3);
//		LCD.drawString("j: "+j, 0, 4);
		updatePosition(3, 2);
//		LCD.drawString("i: " + i, 0, 5);
//		LCD.drawString("j: "+j, 0, 6);
		maze.nodes[i][j].unvisitedChild--;
		maze.nodes[i][j].isEastAvail = false;
		updateHeading(3);
		car.pilot.rotate(90);
		car.pilot.travel(40);
	}
	
	private static boolean tellWall(float distance) {
		boolean hasWall;
		if (distance <= 20 && distance >= 0.0) {
			hasWall = true;
			return hasWall;
		} else {
			hasWall = false;
			return hasWall;
		}
	}
	
	private static void checkAround() throws IOException {
		float distance;
		
		//LCD.drawString("heading: "+ heading, 0, 1);            
		// checking the north
		Delay.msDelay(200);
		car.getUltrasp();
		Delay.msDelay(200);
		distance = car.getUltrasp();
		hasNorthWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			//hasNorthWall = true;
			setNeighValue(0, 1); 
			
		} else {
			//Sound.twoBeeps();
			setNeighValue(0, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isNorthAvail = true;
		}          
		System.out.println("CONNECTED");
		OutputStream out = client.getOutputStream();
		DataOutputStream dOut = new DataOutputStream(out);
		
		dOut.writeUTF(maze.nodes.toString());
		dOut.flush();
		server.close();
		        
		distance = 0;
		// checking the west
		car.turnWest1();
		Delay.msDelay(200);
		distance = car.getUltrasp();
		hasWestWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			//hasWestWall = true;
			setNeighValue(2, 1);
		} else {
			//Sound.twoBeeps();
			setNeighValue(2, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isWestAvail = true;
		}            
		Delay.msDelay(200);
		car.turnWest2();
		
		distance = 0;
		// checking the east
		car.turnEast1();
		Delay.msDelay(200);
		distance = car.getUltrasp();
		hasEastWall = tellWall(distance);
		if (tellWall(car.getUltrasp())) {
			Sound.beep();
			//hasEastWall = true;
			setNeighValue(3, 1);
		} else {
			//Sound.twoBeeps();
			setNeighValue(3, 0);
			maze.nodes[i][j].unvisitedChild++;
			maze.nodes[i][j].isRoot = true;
			maze.nodes[i][j].isEastAvail = true;
		}           
		Delay.msDelay(200);
		car.turnEast2();
		Delay.msDelay(200);    
	}
	
}
