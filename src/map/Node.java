package map;

public class Node {
    public int x, y;
    public boolean walkable;
    public double gCost, hCost, fCost;
    public Node parent;

    public Node(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
    }

    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }
}

