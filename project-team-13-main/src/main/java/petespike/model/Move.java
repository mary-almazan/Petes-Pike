package petespike.model;

import java.util.Objects;

public class Move {
    Position position;
    Direction direction;
    
    public Move(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
    return direction;
    }

    // verify if two objects the same
    //i stole this from the position class
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false; // verify null
        Move other = (Move) obj; 
        return position.equals(other.position) && direction.equals(other.direction); //verify
    }

    @Override // has code generator for -> position
    public int hashCode() {
        return Objects.hash(position, direction);
    }

    @Override
    public String toString() {
        return position + " " + direction;
    }
}
