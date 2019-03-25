package ijev3as2;


import java.util.Arrays;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import lejos.hardware.Battery;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Maze {
	
    int[][] nodeFix = new int[19][13];
	Node[][] nodes;
    Set<Node> unvisited = new HashSet<>();
    Set<Node> visited = new HashSet<>();
    GraphicsLCD robotLCD;
    public Maze(int length, int width, GraphicsLCD lcd) {
        this.nodes = new Node[length][width];
        robotLCD = lcd;
        initializeMaze();
        //assignValue();
    }

	private void initializeMaze() {
		for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 13; j++) {
                nodes[i][j] = new Node(i, j);
                nodes[i][j].setCondition(2);
                nodeFix[i][j] = 2;
                //nodeFix[i][j] = nodes[i][j].condition;  
                unvisited.add(nodes[i][j]);
            }
        }
		nodes[1][1].setCondition(0);
		//assignValue();
		//robotLCD.drawRect(0, 0, robotLCD.getWidth()-1, robotLCD.getHeight()-1);
//		for (int i = 0; i < 9; i++) {
//			for (int j = 0; j < 6; j++) {
//				nodeFix[i][j] = 2;
//			}
//		}
	}
	
	private void fillRect(int x, int y) {
		robotLCD.fillRect(x*7, 121-y*7, 7, 7);
		//robotLCD.fillRect((x+1)*9, 121 - (y - 1)*9, 9, 9);
	}
	
	private void drawNodes() {
		
		nodes.toString();
	}
	

	
	
    public void printMz(int i, int j, int heading, int pose) {
    	switch(heading) {
    	case 0: {   // the car heading north
    		switch (pose) {
    		case 0: {    // the car has wall north
    			fillRect(i, j + 1);
    			break;
    		}
    		case 2: {    // the car has wall west
    			fillRect(i - 1, j);
    			fillRect(i - 1, j + 1);
    			break;
    		}
    		case 3: {    // the car has wall east
    			fillRect(i + 1, j);
    			fillRect(i + 1, j + 1);
    			break;
    		}	
    		}
    		break;
    	}
    	case 1: {  // the car heading south
    		switch (pose) {
    		case 0: {    // the car has wall north
    			fillRect(i, j - 1);
    			break;
    		}
    		case 2: {    // the car has wall west
    			fillRect(i + 1, j);
    			fillRect(i + 1, j + 1);
    			break;
    		}
    		case 3: {    // the car has wall east
    			fillRect(i - 1, j);
    			fillRect(i - 1, j + 1);
    			break;
    		}	
    		}
    		break;
    	}
    	case 2: {  // the car heading west
    		switch (pose) {
    		case 0: {    // the car has wall north
    			fillRect(i - 1, j);
    			break;
    		}
    		case 2: {    // the car has wall west
    			fillRect(i, j - 1);
    			fillRect(i-1, j-1 );
    			break;
    		}
    		case 3: {    // the car has wall east
    			fillRect(i, j + 1);
    			fillRect(i-1, j + 1);
    			break;
    		}	
    		}
    		break;
    	}
    	case 3:{  // the car heading east
    		switch (pose) {
    		case 0: {    // the car has wall north
    			fillRect(i + 1, j);
    			break;
    		}
    		case 2: {    // the car has wall west
    			fillRect(i, j + 1);
    			fillRect(i+1, j + 1);
    			break;
    		}
    		case 3: {    // the car has wall east
    			fillRect(i, j - 1);
    			fillRect(i+1, j-1);
    			break;
    		}	
    		}
    		break;
    	}
    	}
    }

//	public void assignValue() {
////		int k = 1;
////		int g = 1;
//		for (int i = 0; i < 19; i++) {
//			//g = 1;
//            for (int j = 0; j < 13; j++) {
//                nodeFix[i][j] = nodes[i][j].condition;   
//                //g+=2;
//                
//            }
//            //k+=2;
//        }		
//	}

	public String arrayToString() {

	    String aString;     
	    aString = "";
	    int column=0;
	    int row=0;

	    for (column = 12; column >= 0; column--) {
	        for (row = 0; row < 19; row++ ) {
	        nodeFix[row][column] = nodes[row][column].condition;
	        for (int i = 0; i < 19; i++) {
	        	nodeFix[i][0] = 1;
	        	nodeFix[i][12] = 1;
	        }
	        for (int j = 0; j < 13; j++) {
	        	nodeFix[0][j] = 1;
	        	nodeFix[18][j] = 1;
	        }
	        aString = aString + " " + nodeFix[row][column];
	        }
	    aString = aString + "\n";
	    }

	    return aString;
	}

}

