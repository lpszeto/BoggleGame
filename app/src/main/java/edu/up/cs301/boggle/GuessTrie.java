package edu.up.cs301.boggle;

import android.util.Log;

public class GuessTrie extends DictionaryTrie {

    char [][] board = new char[4][4];

    TrieNode [][] trieBoard = new TrieNode[4][4];

    GuessTrie(char [][] newBoard) {

        board = newBoard;

        for (int x = 0; x < board[0].length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                trieBoard[x][y] = new TrieNode(board[x][y], false);
            }
        }
    }

    //loads the valid neighbors of the given node at the given coordinates on the board
    public void loadValidNeighbors(TrieNode node, int x, int y) {


//        while (true) {
//            word = dummyNode.letter + word;
//            if (dummyNode.parent == null) {
//                Log.i("!!!!", word);
//                break;
//            } else {
//                dummyNode = dummyNode.parent;
//            }
//        }
    }

}
