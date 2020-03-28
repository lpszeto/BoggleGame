package edu.up.cs301.boggle;

import android.os.CountDownTimer;
import android.util.Log;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

/**
 * The BogLogalGame class for a simple tic-tac-toe game.  Defines and enforces
 * the game rules; handles interactions with players.
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */

public class BogLogalGame extends LocalGame {
	//Tag for logging
	private static final String TAG = "BogLocalGame";
	// the game's state
	protected BogState state;

	// the marks for player 0 and player 1, respectively
	private final static char[] mark = {'X','O'};

	// the number of moves that have been played so far, used to
	// determine whether the game is over
	protected int moveCount;

	protected CountDownTimer countDownTimer;

	/**
	 * Constructor for the BogLocalGame.
	 */
	public BogLogalGame() {

		// perform superclass initialization
		super();

		// create a new, shuffled BogState object
		state = new BogState();// 
	}
	public int scores(int player){
		return (player == 0)? state.getPlayer0Score() : state.getPlayer1Score();
	}
	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 *
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	protected String checkIfGameOver() {

		if (state.gameOver) {
            if (state.getPlayer0Score() >= state.getPlayer1Score()) {
                int gameWinner = 0;
                return playerNames[gameWinner]+" is the winner.";
            }
            else {
                int gameWinner = 1;
                return playerNames[gameWinner]+" is the winner.";
            }
        }
        else {
            return null; //  game not over
        }

	}

	/**
	 * Notify the given player that its state has changed. This should involve sending
	 * a GameInfo object to the player. If the game is not a perfect-information game
	 * this method should remove any information from the game that the player is not
	 * allowed to know.
	 *
	 * @param p
	 * 			the player to notify
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// make a copy of the state, and send it to the player
		p.sendInfo(new BogState(state));

	}

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game.
	 *
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 * 		true iff the player is allowed to move
	 */
	protected boolean canMove(int playerIdx) {
		return playerIdx == state.getWhoseMove();
	}

	/**
	 * Makes a move on behalf of a player.
	 *
	 * @param action
	 * 			The move that the player has sent to the game
	 * @return
	 * 			Tells whether the move was a legal one.
	 */
	@Override
	protected boolean makeMove(GameAction action) {

		// get the row and column position of the player's move
		BogMoveAction tm = (BogMoveAction) action;
		int row = tm.getRow();
		int col = tm.getCol();

		// get the 0/1 id of our player
		int playerId = getPlayerIdx(tm.getPlayer());

		// if that space is not blank, indicate an illegal move
		if (state.getPiece(row, col) != ' ') {
			return false;
		}

		// get the 0/1 id of the player whose move it is
		int whoseMove = state.getWhoseMove();

		// place the player's piece on the selected square
		state.setPiece(row, col, mark[playerId]);

		// make it the other player's turn
		state.setWhoseMove(1-whoseMove);

		// bump the move count
		moveCount++;

		// return true, indicating the it was a legal move
		return true;
	}

}
