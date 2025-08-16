package petespike.model;

public class Position {
    private int row; // variable
    private int col; // variable

    // constructor
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

   // 15 - 21  -> get row and get col
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Position getPosition(){
        return this;
    }

    // verify if two objects the same
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false; // verify null
        Position other = (Position) obj; 
        return row == other.row && col == other.col; //verify
    }

    @Override // has code generator for -> position
    public int hashCode() {
        return 31 * row + col; 
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")"; // (row, column)
    }
}
