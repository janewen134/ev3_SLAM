package ijev3as2;

import java.util.BitSet;

import lejos.hardware.lcd.GraphicsLCD;

public class DrawingBase
{
 private int x;
 private int y;
 private int heading;
 private int numRows;
 private int numColumns;
 private boolean redTile;
 private boolean greenTile;
 
 //this bitset conclude information of every tile, "N,E,S,W" means the presence of the tile, "N',E',S',W'" means direction
 //of tile, "visited" means whether this tile has been visited , "red,green" means whether this tile is red or green.
 private BitSet[][] tileInfo; // N,E,S,W,N',E',S',W',visited,red,green
 private MappingOnRobot robotLCD;
 
 public DrawingBase() {
 }

 public DrawingBase(GraphicsLCD LCD,int original_X,int original_Y,int startHeading, int numRows,int numColumns,Boolean redTile, Boolean greenTile ) {
  x=original_X;
  y=original_Y;
  this.redTile=redTile;
  this.greenTile=greenTile;
  this.numRows = numRows;
  this.numColumns = numColumns;
  robotLCD = new MappingOnRobot(LCD, numColumns, numColumns);
  tileInfo = new BitSet[numRows][numColumns];
  Car robot= new Car();
 // greenTile = robot.isGreen();
 // redTile = robot.isRed();
  }
 
 public void initializeTileAndSetBoundaries(int startX, int startY,int heading) {
 //initialize map on robot
  robotLCD.resetMapOnRobot();
  x = startX;
  y = startY;
  this.heading = heading;
  for (int i = 0; i < tileInfo.length; i++) {
   for (int j = 0; j < tileInfo[i].length; j++) {
    tileInfo[i][j].clear();
   }
  }
  // every tile set 11 different properties and set walls on the maze boundaries
 for (int i = 0; i < tileInfo.length; i++) {
    for (int j = 0; j < tileInfo[i].length; j++) {
        tileInfo[x][y]= new BitSet(11);
        if (i == 0) {
        }
         tileInfo[i][j].set(3);
     if (j == 0) {
      tileInfo[i][j].set(2);
     }
     if (i == numColumns-1) {
      tileInfo[i][j].set(1);
     }
     if (j == numRows-1) {
      tileInfo[i][j].set(0);
     
     }  
    }
   }
 }
 public int getHeading() {
  return heading;
 }
 public int getX() {
  return x;
 }
 public int getY() {
  return y;
 }
 public int[] getCoordinates() {
  int[] coordinates = new int[2];
  coordinates[0] = x;
  coordinates[1] = y;
  return coordinates;
 }
 public BitSet getWalls(int x, int y) {
  return tileInfo[x][y].get(0, 4);
 }
 public BitSet getDirectionsTaken(int x, int y) {
  return tileInfo[x][y].get(4, 8);
 }
 
 public boolean getVisited(int x, int y) {
  return tileInfo[x][y].get(8);
 }
public void setVisited(int x, int y) {
  tileInfo[x][y].set(8);
 }
public void setDirectionTaken(int x, int y, int direction) {
  tileInfo[x][y].set(direction+4);
}
public boolean setWalls(int x, int y, BitSet walls) {
 if (!tileInfo[x][y].get(8)) {
  for (int index = 0; index < 4; index++) {
   if (walls.get(index)) {
    tileInfo[x][y].set(index);
    robotLCD.drawMap(x, y, UpdataTest.getHeading());
   }
  }
  return true;
 } else {
  return false;
 }
}
public void setColor(Boolean greenTile, Boolean redTile) {
 if(redTile) {
  tileInfo[x][y].set(9, true);
 }
 if(greenTile) {
  tileInfo[x][y].set(10,true);
 }
}
public int updateHeading(int newHeading) {
 heading = newHeading % 4;
 while (heading < 0) {
  heading+=4;
 }
 return heading;
}
  
 public int[] updatePosition(int step) {
  switch (heading) {
  case 0: 
   y += step;
   break;
  case 1: 
   x += step;
   break;
  case 2: 
   y -= step;
   break;
  case 3: 
   x -= step;
   break;
  }
  return getCoordinates();
 }
 
 public boolean getVisitedNeighbor(int direction) {
  int neighborX = x;
  int neighborY = y;
  switch (direction) {
  case 0:
   neighborY+=1;
   break;
  case 1:
   neighborX+=1;
   break;
  case 2:
   neighborY-=1;
   break;
  case 3:
   neighborX-=1;
   break;
  }
  return tileInfo[neighborX][neighborY].get(8);
 }  
}

