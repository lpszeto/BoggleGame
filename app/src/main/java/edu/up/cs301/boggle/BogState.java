package edu.up.cs301.boggle;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import edu.up.cs301.game.GameFramework.infoMessage.GameState;


/**
 * Contains the state of a Boggle game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class BogState extends GameState {
    //Tag for logging
    private static final String TAG = "BogState";
    private static final long serialVersionUID = 7552321013488624386L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

    // the 4x4 array of char that represents the letters on the board
    private char[][] board;

    public char [] alphabet = {'a','b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','u','v','w','x','y','z'};

    public DictionaryTrie dictionaryTrie = new DictionaryTrie();

    // an int that tells whose move it is
    //private int playerToMove;

    private String player0NewWord = "";

    private String player1NewWord = "";

    //Wordlist for player0
    private Vector<String> player0Words = new Vector<String>();

    //Wordlist for player1
    private Vector<String> player1Words = new Vector<String>();

    //Scores
    private int player0Score = 0;
    private int player1Score = 0;

    protected CountDownTimer countDownTimer;
    protected int minutesLeft = 3;
    protected int secondsLeft = 0;
    boolean gameOver = false;


    /**
     * Constructor for objects of class BogState
     */
    public BogState(Context context)
    {
        Random ran = new Random();
        // initialize the state to be a brand new game
        board = new char[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = alphabet[ran.nextInt(alphabet.length)];
            }
        }
        dictionaryTrie.initializeTop();
        dictionaryTrie.readWordsFromFile(context);
        dictionaryTrie.initializeEnglishDictionaryTrie();

//        for (int i = 0; i < dictionaryTrie.top.size(); i++) {
//            dictionaryTrie.printWordsInTrie(dictionaryTrie.top.get(i));
//        }

        // make it player 0's move
//        playerToMove = 0;
    }// constructor

    /**
     * Copy constructor for class BogState
     *
     * @param original
     * 		the BogState object that we want to clong
     */
    public BogState(BogState original)
    {
        // create a new 4x4 array, and copy the values from
        // the original
        board = new char[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = original.board[i][j];
            }
        }
        dictionaryTrie = original.dictionaryTrie;
        player0NewWord = original.player0NewWord;
        player1NewWord = original.player1NewWord;
        //Wordlist for player0
        player0Words = original.player0Words;
        //Wordlist for player1
        player1Words = original.player1Words;
        //Scores
        player0Score = original.player0Score;
        player1Score = original.player1Score;
        minutesLeft = original.minutesLeft;
        secondsLeft = original.secondsLeft;
        gameOver = original.gameOver;

        // copy the player-to-move information
 //       playerToMove = original.playerToMove;
    }


    //player's newWord is assumed to be in the dictionary
    //Time to add the word to the player's wordlist (if not already there),
    //and score the word according to the Boggle scoring system.
    //If the word is already in the list, do nothing.

    public void scoreWord(int playerId) {
        String possibleWord = getPlayerNewWord(playerId);
        //check if already in that player's wordlist...
        Vector<String> playerWordList;

        if(playerId==0) {
            playerWordList = player0Words;
        }
        else {
            playerWordList = player1Words;
        }

        for (int i = 0; i < playerWordList.size(); i++) {
            if (playerWordList.get(i).equals(possibleWord)) { //Bug fix; needed to be .equals()
                return; // Word is already in the list!!!
            }
        }
        //word was not on the list already. Add it!!!
        playerWordList.add(possibleWord);

        if(possibleWord.length() <= 4 && possibleWord.length() >= 3) {
            setPlayerPlayerScore(playerId, 1);
        }
        else if(possibleWord.length() == 5) {
            setPlayerPlayerScore(playerId, 2);
        }
        else if(possibleWord.length() == 6) {
            setPlayerPlayerScore(playerId, 3);
        }
        else if(possibleWord.length() == 7) {
            setPlayerPlayerScore(playerId,4);
        }
        else if (possibleWord.length() > 7 && possibleWord.length() <= 16) {
            setPlayerPlayerScore(playerId, 11);
        }

        return;
    }

    //helper method
    private void setPlayerPlayerScore(int playerId, int increment) {
        if(playerId == 0) {
            player0Score += increment;
        }
        else {
            player1Score += increment;
        }
    }
    /**
     * Find out which piece is on a square
     *
     * @param row
     *		the row being queried
     * @param col
     * 		the column being queried
     * @return
     * 		the piece at the given square; ' ' if no piece there;
     * 		'?' if it is an illegal square
     */

    public char getPiece(int row, int col) {
        // if we're out of bounds or anything, return '?';
        if (board == null || row < 0 || col < 0) return '?';
        if (row >= board.length || col >= board[row].length) return '?';

        // return the character that is in the proper position
        return board[row][col];
    }

    /**
     * Sets a piece on a square
     *
     * @param row
     * 		the row being queried
     * @param
     * 		col the column being queried
    // * @param
   //  * 		piece the piece to place
     */
    public void addPiece(int row, int col, int playerId) {
        // if we're out of bounds or anything, return;
        if (board == null || row < 0 || col < 0) return;
        if (row >= board.length || col >= board[row].length) return;
        char piece = board[row][col];
        // add the character that is in the proper position to the current word in the wordlist
        if(playerId==0)
        {
         player0NewWord = player0NewWord + piece;
        }
        else {
            player1NewWord = player1NewWord + piece;
        }

    }

//    /**
//     * Tells whose move it is.(TEMPORARY SINCE ITS TECHNICALLY A ONE PLAYER GAME)
//     *
//     * @return the index (0 or 1) of the player whose move it is.
//     */
//    public int getWhoseMove() {
//        return playerToMove;
//    }
//
//    /**
//     * set whose move it is (TEMPORARY SINCE ITS TECHNICALLY A ONE PLAYER GAME)
//     * @param id
//     * 		the player we want to set as to whose move it is
//     */
//    public void setWhoseMove(int id) {
//        playerToMove = id;
//    }
    public char[][] getBoard() {
        return board;
    }

    /**
     * set whose move it is (TEMPORARY SINCE ITS TECHNICALLY A ONE PLAYER GAME)
     * @param id
     * 		the player we want to set as to whose move it is
     */
    //public void setWhoseMove(int id) {
    //    playerToMove = id;
    //}

    public String getPlayerNewWord(int playerId) {
        if(playerId == 0) {
            return player0NewWord;
        }
        else {
            return player1NewWord;
        }
    }

    public void resetPlayerNewWord(int playerId) {
        if(playerId == 0) {
            player0NewWord = "";
        }
        else {
            player1NewWord = "";
        }
    }

    public Vector<String> getPlayer0Words(){return player0Words;}
    public Vector<String> getPlayer1Words() { return player1Words;}

    public int getPlayer0Score() {return player0Score;}


    public int getPlayer1Score() {return player1Score;}
    public void shuffle(){
        for (int i = 0; i < 4; i++) {
            Random ran = new Random();
            for (int j = 0; j < 4; j++) {
                board[i][j] = alphabet[ran.nextInt(alphabet.length)];
            }
        }
    }

}
