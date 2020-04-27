package edu.up.cs301.boggle;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import edu.up.cs301.game.GameFramework.Game;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.actionMessage.TimerAction;
import edu.up.cs301.game.GameFramework.utilities.GameTimer;
import edu.up.cs301.game.GameFramework.utilities.Logger;

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

	GameTimer gameTimer;

	/**
	 * Constructor for the BogLocalGame.
	 */
	public BogLogalGame(final Context context) {

		// perform superclass initialization
		super();

		// create a new, shuffled BogState object
		state = new BogState(context);


		gameTimer = super.getTimer();
		gameTimer.setInterval(1000);
		gameTimer.start();
	}
	@Override
	public void timerTicked(){
		Logger.log("timer","Another second");
		Log.i("secondsLeft", "" + state.secondsLeft);
		Log.i("minutesLeft", "" + state.minutesLeft);
		if (state.secondsLeft <= 0) {
			state.secondsLeft = 59;
			state.minutesLeft--;
		} else {
			state.secondsLeft--;
		}
		if (state.minutesLeft < 0)  {
			state.gameOver = true;
			String msg = checkIfGameOver();
			if (msg != null) {
				gameTimer.stop();
				finishUpGame(msg);
				return;
			}
		}
		sendAllUpdatedState();
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
		Log.i("game", "DONE!" + state.gameOver);
        String [] playerWords = {"", ""};
        String message = "";

        for(String i : state.getPlayer0Words()) {
            playerWords[0] = playerWords[0] + i + ", ";
        }

        for(String i : state.getPlayer1Words()) {
            playerWords[1] = playerWords[1] + i + ", ";
        }

		int gameWinner;
		if (state.gameOver) {
			if (state.getPlayer0Score() >= state.getPlayer1Score()) {
				gameWinner = 0;
            }
			else {
				gameWinner = 1;
			}
			message = playerNames[gameWinner] + " is the winner. \n\n";
			state.incrementWins(gameWinner);
			int i = 0;

			for (String x : playerNames) {
			    message = message + x + " Word Bank: " + playerWords[i] + "\n\n" ;
			    i++;
            }
            return message;
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
		if(state.gameOver){
			return false;
		}
		return true;
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

		BogMoveAction tm = (BogMoveAction) action;
		// get the 0/1 id of our player
		int playerId = getPlayerIdx(tm.getPlayer());

		if(state.localGuiPlayerId == -1) {
			if(tm.getPlayer().requiresGui()) {
				state.localGuiPlayerId = playerId;
			}
			else {   //Still works for two computer players, and is needed for font color.
				if (playerId==0) {
					state.localGuiPlayerId = 1;
				}
				else {
					state.localGuiPlayerId = 0;
				}
			}
		}
		// get the row and column position of the player's move

		int row = tm.getRow();
		int col = tm.getCol();
		boolean makesWord = tm.getEndofWord();

		// if the piece has anything except a lowercase letter, indicate an illegal move
		for(int i = 0; i < 26; i++) {
			if (state.getPiece(row, col) == state.alphabet[i]) {
				break;
			}
			if (i==25) {
				return false;
			}
		}

		state.addPiece(row, col, playerId);

		// if the piece is the last letter in a word, then
		// check if the word is in the dictionary and tally points accordingly!
		// searching the dictionary returns 2 or 3 if the word inputted is a word from the word_list resource file.
		//
		if (makesWord && (state.dictionaryTrie.searchDictionary(state.getPlayerNewWord(playerId))) >= 2) { //TODO implement checkWord
			state.scoreWord(playerId); //also checks if the word is already in the list. Only adds the word and the associated points if the word is not already in the list.
		}

		if(makesWord) {
			state.resetPlayerNewWord(playerId); //prepares for the player to enter a new word
		}

		// bump the move count
//		moveCount++;

		for(GamePlayer player : players) {
			sendUpdatedStateTo(player);
		}
		//Legal move. Return true.
		return true;
	}

}
