package ijev3as2;

import lejos.robotics.navigation.Waypoint;

public class Node {
    public int X = 1;
    public int Y = 1;
    public int condition = -1;
    public Node parent = null; // back
    public Node child1; // left
    public Node child2; // mid
    public Node child3; // right
    //public int heading = 0;
    public Waypoint point;
    boolean isDestination = false;
    public int unvisitedChild = 0;
    public boolean isRoot = false;
    public boolean isNorthAvail = false;
    public boolean isWestAvail = false;
    public boolean isEastAvail = false;
    

    

	public Node(int x, int y) {
        this.X = x;
        this.Y = y;
        this.parent = null;
    }
	
	public Waypoint getPoint() {
		return point;
	}

	public void setPoint(Waypoint point) {
		this.point = point;
	}

    public Node(int X, int Y, Node parent) {
        this.X = X;
        this.Y = Y;
        this.parent = parent;
    }

    public String toString() {
        return "x = " + X + " y = " + Y;
    }

    public Node getChild1() {
        return child1;
    }

    public void setChild1(Node child1) {
        this.child1 = child1;
    }

    public Node getChild2() {
        return child2;
    }

    public void setChild2(Node child2) {
        this.child2 = child2;
    }

    public Node getChild3() {
        return child3;
    }

    public void setChild3(Node child3) {
        this.child3 = child3;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public int getCondition() {
        return condition;
    }

    public boolean isOccupied() {
        if (condition == 0)
            return false;
        else return true;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setParent(int i, int j) {
        this.parent = new Node(i, j);
        Car car = new Car();
        //boolean yes = car.touchSensor.isPressed();
    }

    public int[] getCoordinates() {
        int[] coords = new int[2];
        coords[0] = X;
        coords[1] = Y;
        return coords;
    }


}
