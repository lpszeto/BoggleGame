package edu.up.cs301.boggle;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Logger;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

/**
 * A computerized tic-tac-toe player that recognizes an immediate win
 * or loss, and plays appropriately.  If there is not an immediate win
 * (which it plays) or loss (which it blocks), it moves randomly.
 * 
 * @author Steven R. Vegdahl 
 * @version September 2016
 * 
 */
public class BogComputerPlayer2 extends GameComputerPlayer {
	//Tag for logging
	private static final String TAG = "BogComputerPlayer2";
	/**
	 * instance variable that tells which piece am I playing ('X' or 'O').
	 * This is set once the player finds out which player they are, in the
	 * 'initAfterReady' method.
	 */
	protected char piece;
	protected DictionaryTrie vocabulary;
	protected GuessTrie guessTrie;
	protected boolean gotInfo = false;

	/**
	 * constructor for a computer player
	 *
	 * @param name
	 * 		the player's name
	 */
	public BogComputerPlayer2(String name) {
		// invoke superclass constructor
		super(name);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~My Stuff~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//generate guessTrie based on the boggle board.
		//iterate through it comparing against the dictionary as we go...
		//if the word is in the dictionary, make a series of moves in order to guess that word
		//continue iterating through the guessTrie.


	}// constructor

	/**
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 */
	protected void initAfterReady() {
		// initialize our piece
		piece = "XO".charAt(playerNum);
	}// initAfterReady

	/**
	 * Called when the player receives a game-state (or other info) from the
	 * game.
	 *
	 * @param info
	 * 		the message from the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {

		// if it's not a BogState message, ignore it; otherwise
		// cast it
		if (!(info instanceof BogState)) return;

		if(gotInfo) {
			return;
		}
		gotInfo = true;
		BogState myState = (BogState) info;

		char [][] board = {{'a', 'x', 'a', 'l'}, {'z', 's', 'y', 'l'}, {'k', 's', 'u', 't'}, {'i', 'u', 'e', 'n'}};

		if(myState.dictionaryTrie.top.size() >= 1) {
			guessTrie = new GuessTrie(board, myState.dictionaryTrie);
			for(int i = 0; i < guessTrie.top.size(); i++) {
				guessTrie.printWordsInTrie(guessTrie.top.get(i));
			}
			//guessTrie = new GuessTrie(myState.getBoard()); TODO: use this!!!
		}

		// if it's not our move, ignore it
//		if (myState.getWhoseMove() != this.playerNum) return;


		// if we find a win, select that move
//		Point win = findWin(myState, piece);
//		if (win != null) {
//			Logger.log("TTTComputer", "sending action");
//			game.sendAction(new BogMoveAction(this, ));
//		}
	}
}
