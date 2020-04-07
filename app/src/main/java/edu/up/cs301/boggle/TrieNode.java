package edu.up.cs301.boggle;

import java.util.Vector;

public class TrieNode {

    public char letter = ' ';
    public TrieNode parent = null;
    public boolean isWord;
    public Vector<TrieNode> children = new Vector<TrieNode>(26);

    public TrieNode(char c, boolean makesWord) {
        letter = c;
        isWord = makesWord;
    }

//    public TrieNode getParent(){return parent;}
//    public Vector getChildren(){return children;} 
//    public char getLetter() {return letter;}
}

