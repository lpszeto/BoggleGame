package edu.up.cs301.boggle;

import java.util.Vector;

public class TrieNode {

    public char letter = ' ';
    public TrieNode parent = null;
    public boolean isWord;
    public int [] coords = new int[2];
    public Vector<TrieNode> children = new Vector<TrieNode>(26);

    public TrieNode(char c, boolean makesWord) {
        letter = c;
        isWord = makesWord;
    }
    public TrieNode(char c, boolean makesWord, int x, int y) {
        letter = c;
        isWord = makesWord;
        coords[0] = x;
        coords[1] = y;
    }

//    public TrieNode getParent(){return parent;}
//    public Vector getChildren(){return children;}
//    public char getLetter() {return letter;}
}

