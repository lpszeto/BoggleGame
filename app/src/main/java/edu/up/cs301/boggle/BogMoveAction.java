package edu.up.cs301.boggle;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

/**
 * A game-move object that a boggle player sends to the game to make
 * a move.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2001
 */
public class BogMoveAction extends GameAction {
    //Tag for logging
    private static final String TAG = "BogMoveAction";
    private static final long serialVersionUID = -2242980258970485343L;

    // instance variables: the selected row and column
    private int row;
    private int col;

    /**
     * Constructor for BogMoveAction
     *
     //@param source the player making the move
     * @param row the row of the square selected (0-3)
     * @param col the column of the square selected
     */
    public BogMoveAction(GamePlayer player, int row, int col)
    {
        // invoke superclass constructor to set the player
        super(player);

        // set the row and column as passed to us
        this.row = Math.max(0, Math.min(3, row));
        this.col = Math.max(0, Math.min(3, col));
    }

    /**
     * get the object's row
     *
     * @return the row selected
     */
    public int getRow() { return row; }

    /**
     * get the object's column
     *
     * @return the column selected
     */
    public int getCol() { return col; }

}
