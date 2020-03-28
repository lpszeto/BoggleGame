package edu.up.cs301.boggle;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;
import edu.up.cs301.game.R;

/**
 * this is the primary activity for Counter game
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class BogMainActivity extends GameMainActivity {
	//Tag for logging
	private static final String TAG = "TTTMainActivity";
	public static final int PORT_NUMBER = 5213;

	/**
	 * a tic-tac-toe game is for two players. The default is human vs. computer
	 */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

		// yellow-on-blue GUI
		playerTypes.add(new GamePlayerType("Local Human Player 1") {
			public GamePlayer createPlayer(String name) {
				return new BogHumanPlayer1(name, R.layout.bog_human_player1);
			}
		});

		// red-on-yellow GUI (TEMPORARY SINCE WE ARE UNSURE OF FLIPPING THE SCREEN)
		/*playerTypes.add(new GamePlayerType("Local Human Player (yellow-red)") {
			public GamePlayer createPlayer(String name) {
				return new BogHumanPlayer1(name, R.layout.bog_human_player1_flipped);
			}
		});*/

		// game of 33
		playerTypes.add(new GamePlayerType("Local Human Player 2") {
			public GamePlayer createPlayer(String name) {
				return new BogHumanPlayer2(name);
			}
		});

		// dumb computer player
		playerTypes.add(new GamePlayerType("Computer Player (dumb)") {
			public GamePlayer createPlayer(String name) {
				return new BogComputerPlayer1(name);
			}
		});

		// smarter computer player
		playerTypes.add(new GamePlayerType("Computer Player (smart)") {
			public GamePlayer createPlayer(String name) {
				return new BogComputerPlayer2(name);
			}
		});

		// Create a game configuration class for Tic-tac-toe
		GameConfig defaultConfig = new GameConfig(playerTypes, 2,2, "Boggle", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0); // yellow-on-blue GUI
		defaultConfig.addPlayer("Computer", 3); // dumb computer player

		// Set the initial information for the remote player
		defaultConfig.setRemoteData("Remote Player", "", 1); // red-on-yellow GUI

		//done!
		return defaultConfig;

	}//createDefaultConfig


	/**
	 * createLocalGame
	 *
	 * Creates a new game that runs on the server tablet,
	 *
	 * @return a new, game-specific instance of a sub-class of the LocalGame
	 *         class.
	 */
	@Override
	public LocalGame createLocalGame() {
		return new BogLogalGame();
	}

}
