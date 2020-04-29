package edu.up.cs301.boggle;

import android.content.Context;

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
	private static final String TAG = "BogMainActivity";
	public static final int PORT_NUMBER = 5213;

	/**
	 * a tic-tac-toe game is for two players. The default is human vs. computer
	 */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

		// GUI
		playerTypes.add(new GamePlayerType("Local Human Player") {
			public GamePlayer createPlayer(String name) {
				return new BogHumanPlayer1(name, R.layout.bog_human_player1);
			}
		});

		// dumb computer player
		playerTypes.add(new GamePlayerType("Computer Player (50% difficulty)") {
			public GamePlayer createPlayer(String name) {
				return new BogComputerPlayer1(name);
			}
		});

		// smarter computer player
		playerTypes.add(new GamePlayerType("Computer Player (70% difficulty)") {
			public GamePlayer createPlayer(String name) {
				return new BogComputerPlayer2(name);
			}
		});

		// smartest computer player
		playerTypes.add(new GamePlayerType("Computer Player (god-player)") {
			public GamePlayer createPlayer(String name) {
				return new BogComputerPlayer3(name);
			}
		});

		// Create a game configuration class for Boggle
		GameConfig defaultConfig = new GameConfig(playerTypes, 1,2, "Boggle", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0); // yellow-on-blue GUI
		defaultConfig.addPlayer("Computer", 2); // dumb computer player

		// Set the initial information for the remote player
//		defaultConfig.setRemoteData("Remote Player", "", 1); // red-on-yellow GUI

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
		return new BogLogalGame(this);
	}

}
