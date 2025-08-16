
package model.petespike;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;
import petespike.model.Move;
import petespike.model.Direction;

class PetesPikeTest {
    
    @Test
    void testBoardDimensions() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        assertEquals(5, game.getRows());
        assertEquals(5, game.getCols());
    }
    
    @Test
    void testInitialMoveCount() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        assertEquals(0, game.getMoveCount());
    }
    
    @Test
    void testValidPosition() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position validPos = new Position(0, 4);  // P's position
        Position invalidPos = new Position(-1, 0);
        Position invalidPos2 = new Position(5, 5);
        
        assertTrue(game.isValid(validPos));
        assertFalse(game.isValid(invalidPos));
        assertFalse(game.isValid(invalidPos2));
    }
    
    @Test
    void testGetSymbolAtInvalidPosition() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position invalidPos = new Position(-1, 0);
        assertThrows(PetesPikeException.class, () -> {
            game.getSymbolAt(invalidPos);
        });
    }

    @Test
    void testGetSymbolAt() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position petesPosition = new Position(0, 4);
        Position emptyPos = new Position(1, 1);
        Position goatPositions = new Position(3, 2);
        
        assertEquals('P', game.getSymbolAt(petesPosition));
        assertEquals('-', game.getSymbolAt(emptyPos));
        assertEquals('1', game.getSymbolAt(goatPositions));
    }
    
    @Test
    void testGetPositionOf() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position petesPosition = game.getPositionOf('P');
        Position goatPositions = game.getPositionOf('1');
        Position nonExistentPos = game.getPositionOf('2');
        
        assertNotNull(petesPosition);
        assertNotNull(goatPositions);
        assertNull(nonExistentPos);
        assertEquals(0, petesPosition.getRow());
        assertEquals(4, petesPosition.getCol());
        assertEquals(3, goatPositions.getRow());
        assertEquals(2, goatPositions.getCol());
    }

    @Test
    void testMakeValidMove() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position startPos = new Position(0, 4);  // Pete's starting position
        Move move = new Move(startPos, Direction.LEFT);
        
        game.makeMove(move);
        assertEquals(1, game.getMoveCount());
        assertEquals('-', game.getSymbolAt(startPos));
        assertEquals('P', game.getSymbolAt(new Position(0, 3)));
    }
    
    @Test
    void testMakeInvalidMove() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position emptyPos = new Position(1, 1);
        Move move = new Move(emptyPos, Direction.UP);
        
        assertThrows(PetesPikeException.class, () -> {
            game.makeMove(move);
        });
    }

    @Test
    void testGetMountaintop() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        Position mountaintop = game.getMountaintop();
        
        assertNotNull(mountaintop);
        assertEquals(2, mountaintop.getRow());
        assertEquals(2, mountaintop.getCol());
    }
    
    @Test
    void testReset() throws PetesPikeException {
        PetesPike game = new PetesPike("petes_pike_5_5_2_0.txt");
        game.makeMove(new Move(new Position(0, 4), Direction.LEFT));
        game.reset("petes_pike_5_5_2_0.txt");
        assertEquals(0, game.getMoveCount());
        assertEquals(5, game.getRows());
        assertEquals(5, game.getCols());
        Position petesPosition = game.getPositionOf('P');
        Position goatPositions = game.getPositionOf('1');
        Position mountaintopPos = game.getMountaintop();
        assertNotNull(petesPosition);
        assertEquals(0, petesPosition.getRow());
        assertEquals(4, petesPosition.getCol());
        assertNotNull(goatPositions);
        assertEquals(3, goatPositions.getRow());
        assertEquals(2, goatPositions.getCol());
        assertNotNull(mountaintopPos);
        assertEquals(2, mountaintopPos.getRow());
        assertEquals(2, mountaintopPos.getCol());
    }
}