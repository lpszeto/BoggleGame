package edu.up.cs301.boggle;

import android.util.Log;

public class GuessTrie extends DictionaryTrie {

    char [][] board;
    TrieNode [][] trieBoard = new TrieNode[4][4];
    DictionaryTrie vocabulary;


    GuessTrie(char [][] newBoard, DictionaryTrie dictionary) {

        board = newBoard;
        vocabulary = dictionary;

        for (int x = 0; x < board[0].length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                top.add(new TrieNode(board[x][y], false, x, y));
                loadValidNeighbors(top.get(top.size()-1));
                //                trieBoard[x][y] = new TrieNode(board[x][y], false);
            }
        }
        printSubTries(top.get(2)); //Test
    }

    //loads the valid neighbors of the given node at the given coordinates on the board
    public void loadValidNeighbors(TrieNode node) {

        //iterate over all of the valid surrounding letters on the board.
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y++) {

                int [] newCoords = new int[2];
                newCoords[0] = node.coords[0] + x;
                newCoords[1] = node.coords[1] + y;

                if(!validCoords(newCoords, node))
                    continue;

                TrieNode dummyNode = node;
                String guess = makeStringFromParents(dummyNode) + board[newCoords[0]][newCoords[1]];

                int vocabCheck = vocabulary.searchDictionary(guess);

                if(vocabCheck == 0) {
                    continue; //no possible words have this letter.
                }
                else if(vocabCheck == 1) { //the node parents-based string is not a word itself yet, but it is at least a prefix of possible words.
                    //make this letter a child of the node, and establish parental relationship
                    //recurse, since there could be other neighbors that finish the word.
                    node.children.add(new TrieNode(board[newCoords[0]][newCoords[1]], false, newCoords[0], newCoords[1]));
                    node.children.get(node.children.size() - 1).parent = node;
                    loadValidNeighbors(node.children.get(node.children.size() - 1)); //RECURSE!!!!!!!!!!!!!!!!!!
                }
                else if(vocabCheck == 2) { //the node parents-based string is a word itself! However, this branch of the trie should end here since no other words can come from it.
                    //make this letter a child of the node, and establish parental relationship
                    //return, since there cannot be other neighbors that finish the word.
                    node.children.add(new TrieNode(board[newCoords[0]][newCoords[1]], true, newCoords[0], newCoords[1]));
                    node.children.get(node.children.size() - 1).parent = node;
                    return;
                }
                else if(vocabCheck == 3) { //The node parents-based string is a word itself, and it is also the root of other words!!!!!!!!!!!!!!!!!!!!!!!
                    node.children.add(new TrieNode(board[newCoords[0]][newCoords[1]], true, newCoords[0], newCoords[1]));
                    node.children.get(node.children.size() - 1).parent = node;
                    loadValidNeighbors(node.children.get(node.children.size() - 1)); //RECURSE!!!!!!!!!!!!!!!!!!
                }
                else { //should never get here, but if we do, respond gracefully...
                    return;
                }
            }
        }
        return;
    }

    //helper method

    public boolean validCoords(int [] newCoords, TrieNode  node) {

        if (newCoords[0] <= -1 || newCoords[0] >= 4) {
            return false;
        }
        if (newCoords[1] <= -1 || newCoords[1] >=4) {
            return false;
        }

        //Check to make sure they are not the coordinates of the node itself
        if(newCoords[0] == node.coords[0] && newCoords[1] == node.coords[1]) {
            return false;
        }

        //Check to make sure they are not the coordinates of ANY of the node's parents
        TrieNode dummyNode = node;
        boolean foundCoordsOfParent = false;
        while (true) {
            if (dummyNode.parent == null) {
                break;
            }
            else {
                dummyNode = dummyNode.parent;
                if (dummyNode.coords[0] == newCoords[0] && dummyNode.coords[1] == newCoords[1]) {
                    foundCoordsOfParent = true;
                    break;
                }
            }
        }

        if(foundCoordsOfParent) {
            return false;
        }

        return true;
    }

    //helper method

    public String makeStringFromParents(TrieNode dummyNode) {

        String guess = "";

        while (true) {
            guess = dummyNode.letter + guess;
            if (dummyNode.parent == null) {
                break;
            } else {
                dummyNode = dummyNode.parent;
            }
        }
    return guess;
    }

}
