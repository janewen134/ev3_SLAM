package ijev3as2;


import java.util.Arrays;
import java.util.*;

import lejos.hardware.lcd.LCD;

public class Maze {

    Node[][] nodes;
    Set<Node> unvisited = new HashSet<>();
    Set<Node> visited = new HashSet<>();
    public Maze(int length, int width) {
        this.nodes = new Node[length][width];
        initializeMaze();
    }

	private void initializeMaze() {
		for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 13; j++) {
                nodes[i][j] = new Node(i, j);
                nodes[i][j].setCondition(-1);
                unvisited.add(nodes[i][j]);
                printMz();
            }
        }
		nodes[1][1].setCondition(0);
	}

    public void printMz() {
    	initializeMaze();
    	int k = 0;
    	int l = 0;
    	for (int i = 0; i < nodes.length; i+=1) {
            for (int j = 0; j < nodes[i].length; j+=1) {
            	
                LCD.setPixel(k, l, 1);
                LCD.setPixel(k+1, l, 1);
                //LCD.setPixel(i+2, j, 1);
                LCD.setPixel(k, l+1, 1);
                LCD.setPixel(++k, ++l, 1);
                //LCD.setPixel(i, j, 1);
                
            }
           
        }

    }

}

