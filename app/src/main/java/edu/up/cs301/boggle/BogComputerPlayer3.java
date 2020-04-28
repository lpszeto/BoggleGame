package edu.up.cs301.boggle;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Logger;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;
import java.util.Vector;

/**
 * A computerized Boggle player that finds all possible words. This player is TERRIFYING.
 *
 * @author Connor J. Gilliland
 * @version April 2020
 *
 */
public class BogComputerPlayer3 extends GameComputerPlayer {
    //Tag for logging
    private static final String TAG = "BogComputerPlayer3";
    protected char piece;
    private GuessTrie guessTrie;
    protected boolean gotInfo = false;

    /**
     * constructor for a computer player
     *
     * @param name
     * 		the player's name
     */
    public BogComputerPlayer3(String name) {
        // invoke superclass constructor
        super(name);
    }// constructor

    /**
     * perform no initialization after the player
     * knows what their game-position and opponents' names are.
     */
    protected void initAfterReady() {
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

        if(myState.dictionaryTrie.top.size() >= 1) {
            guessTrie = new GuessTrie(myState.getBoard(), myState.dictionaryTrie);

            for(int i = 0; i < guessTrie.top.size(); i++) {
                guessTrie.printWordsInTrie(guessTrie.top.get(i));
            }

            for(TrieNode node : guessTrie.wordNodes) {
                Vector<TrieNode> parentBasedWord = new Vector<TrieNode>();
                TrieNode dummyNode = node;
                String test = guessTrie.makeStringFromParents(node);
                Log.i("guessWord: ", test);
                while (true) {
                    parentBasedWord.add(dummyNode);
                    if (dummyNode.parent == null) {
                        break;
                    } else {
                        dummyNode = dummyNode.parent;
                    }
                }
                for (int i = parentBasedWord.size() - 1; i >= 0; i--) {
                    boolean end = false;
                    if(i == 0) {
                        end = true;
                    }
                    Random ran = new Random();
                    if(ran.nextBoolean()) {  //guess the word 50% of the time...
                        game.sendAction(new BogMoveAction(this, parentBasedWord.get(i).coords[0], parentBasedWord.get(i).coords[1], end));
                    }
                }
            }

        }
    }
}
