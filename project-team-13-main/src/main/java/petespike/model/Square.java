package petespike.model;

public class Square {
    private boolean isOccupied;
    private String type; 

    public Square(String type) {
        this.type = type;
        this.isOccupied = false;
    }

    public String getType() {
        return type;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

}
