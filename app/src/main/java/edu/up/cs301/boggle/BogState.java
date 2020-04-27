package edu.up.cs301.boggle;

import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import edu.up.cs301.game.GameFramework.infoMessage.GameState;

import static java.lang.StrictMath.abs;


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
    //valid characters
    public char [] alphabet = {'a','b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','u','v','w','x','y','z'};
    //English dictionary data structure.
    public static DictionaryTrie dictionaryTrie = new DictionaryTrie();
    //Wordlist variables for player0
    private String player0NewWord = "";
    private Vector<Point> player0WordCoords = new Vector<Point>();
    private Vector<String> player0Words = new Vector<String>();
    //Wordlist variables for player1
    private Vector<String> player1Words = new Vector<String>();
    private String player1NewWord = "";
    private Vector<Point> player1WordCoords = new Vector<Point>();
    //Scores
    private int player0Score = 0;
    private int player0Wins = 0;
    private int player1Score = 0;
    private int player1Wins = 0;
    //Timing
    protected int minutesLeft = 0;
    protected int secondsLeft = 5;
    public boolean gameOver;
    boolean isHuman = false;

    //GUI Info
    int localGuiPlayerId = -1;


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

        //valid characters
        alphabet = original.alphabet;
        //English dictionary data structure.
        dictionaryTrie = original.dictionaryTrie;
        //Wordlist variables for player0
        player0NewWord = original.player0NewWord;
        player0WordCoords = original.player0WordCoords;
        player0Words = original.player0Words;
        //Wordlist variables for player1
        player1Words = original.player1Words;
        player1NewWord = original.player1NewWord;
        player1WordCoords = original.player1WordCoords;
        //Scores
        player0Score = original.player0Score;
        player0Wins = original.player0Wins;
        player1Score = original.player1Score;
        player1Wins = original.player1Wins;
        //Timing
        minutesLeft = original.minutesLeft;
        secondsLeft = original.secondsLeft;
        gameOver = original.gameOver;
        //GUI Info
        localGuiPlayerId = original.localGuiPlayerId;
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
        Vector<Point> playerWordCoords;

        if(playerId==0) {
            playerWordCoords = player0WordCoords;
        }
        else {
            playerWordCoords = player1WordCoords;

        }
        //Can't use the same cell twice...

            for (int j = 0; j < playerWordCoords.size(); j++) {
                if (row == playerWordCoords.get(j).x && col == playerWordCoords.get(j).y) {
                    return;
                }
            } //TODO ENFORCE MUST BE A NEIGHBOR OF THE LAST CELL

        //Must be a valid neighbor...
        if (playerWordCoords.size() > 0) {
            if (abs(row - playerWordCoords.get(playerWordCoords.size() - 1).x) > 1) {
                return;
            }

            if (abs(col - playerWordCoords.get(playerWordCoords.size() - 1).y) > 1) {
                return;
            }
        }

        char piece = board[row][col];
        // add the character that is in the proper position to the current word in the wordlist
        if(playerId==0)
        {
         player0NewWord = player0NewWord + piece;
         player0WordCoords.add(new Point(row, col));
        }
        else {
            player1NewWord = player1NewWord + piece;
            player1WordCoords.add(new Point(row, col));
        }

   }

   public char[][] getBoard() {
        return board;
    }
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
            player0WordCoords = new Vector<Point>();
        }
        else {
            player1NewWord = "";
            player1WordCoords = new Vector<Point>();

        }
   }

   public void incrementWins(int playerId) {
        if(playerId == 0) {
            player0Wins++;
        }
        else {
            player1Wins++;
        }
        return;
   }

   public Vector<String> getPlayer0Words() {return player0Words;}
   public Vector<String> getPlayer1Words() {return player1Words;}

   public int getPlayer0Wins() {return player0Wins;};
   public int getPlayer1Wins() {return player1Wins;}

   public String getplayer0NewWord() {return player0NewWord;}
   public String getPlayer1NewWord() {return player1NewWord;}

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
  /* public void reset(){
       minutesLeft = 0;
       secondsLeft = 5;
       gameOver = false;

   }*/
}
