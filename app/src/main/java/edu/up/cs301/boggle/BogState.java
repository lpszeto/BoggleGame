package edu.up.cs301.boggle;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

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

    // an int that tells whose move it is
    private int playerToMove;

    //Wordlist for player1
    ArrayList<String> player0Words = new ArrayList<String>();

    //Wordlist for player2
    ArrayList<String> player1Words = new ArrayList<String>();

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
    public BogState()
    {
        Random ran = new Random();
        // initialize the state to be a brand new game
        board = new char[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = alphabet[ran.nextInt(alphabet.length)];
            }
        }

        //start game clock
        countDownTimer = new CountDownTimer(180000, 1000) { //3min timer with ticks every second.
            @Override
            public void onTick(long l000) {
                Log.i("secondsLeft","" + secondsLeft);
                Log.i("minutesLeft","" + minutesLeft);
                if (secondsLeft == 0) {
                    secondsLeft = 59;
                    minutesLeft--;
                }
                else {
                    secondsLeft--;
                }
            }

            @Override
            public void onFinish() {
                Log.i("TIMER", "DONE!");
                gameOver = true;
                player0Score++; //TODO DELETE THIS!!! (Temporary fix to test BogLocalGame.checkIfGameOver())
            }
        }.start();

        // make it player 0's move
        playerToMove = 0;
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

        // copy the player-to-move information
        playerToMove = original.playerToMove;
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
     * @param
     * 		piece the piece to place
     */
    public void setPiece(int row, int col, char piece) {
        // if we're out of bounds or anything, return;
        if (board == null || row < 0 || col < 0) return;
        if (row >= board.length || col >= board[row].length) return;

        // return the character that is in the proper position
        board[row][col] = piece;
    }

    /**
     * Tells whose move it is.(TEMPORARY SINCE ITS TECHNICALLY A ONE PLAYER GAME)
     *
     * @return the index (0 or 1) of the player whose move it is.
     */
    public int getWhoseMove() {
        return playerToMove;
    }

    public char[][] getBoard() {
        return board;
    }

    /**
     * set whose move it is (TEMPORARY SINCE ITS TECHNICALLY A ONE PLAYER GAME)
     * @param id
     * 		the player we want to set as to whose move it is
     */
    public void setWhoseMove(int id) {
        playerToMove = id;
    }

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
