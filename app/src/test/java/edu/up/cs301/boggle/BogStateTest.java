package edu.up.cs301.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class BogStateTest {

    @Test
    public void getPiece() {
        BogState test = new BogState();
        test.setPiece(0,0,'c');
        char accepted = test.getPiece(0,0);
        assertEquals('c',test.getPiece(0,0));
    }

    @Test
    public void setPiece() {
        BogState test = new BogState();
        test.setPiece(0,0,'a');
        assertEquals('a',test.getPiece(0,0));
    }

    @Test
    public void getWhoseMove() {
        BogState test = new BogState();
        assertEquals(0,test.getWhoseMove());
    }

    @Test
    public void setWhoseMove() {
        BogState test = new BogState();
    }

    @Test
    public void getPlayer0Score() {
        BogState test = new BogState();
        assertEquals(0,test.getPlayer0Score());
    }

    @Test
    public void getPlayer1Score() {
        BogState test = new BogState();
        assertEquals(0,test.getPlayer1Score());
    }
}