package edu.up.cs301.boggle;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import edu.up.cs301.game.R;

public class DictionaryTrie {

    public String [] words;
    public String word = new String();
    public boolean [] letters;
    int idx = 0;

    public char [] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public Vector<TrieNode> top = new Vector<TrieNode>(26); //TODO MAKE THIS PRIVATE!!!

    public void readWordsFromFile(Context context) {
        if (words == null) {
            words = readWordsFromResourceFile(context);
        }
    }

    public void initializeEnglishDictionaryTrie(){
        for(int i = 0; i < words.length; i++) {
            addWord(words[i]);
        }
        return;
    }

    public void initializeTop() { // whole alphabet for now. Should be just the 16 Boggle characters eventually.
        for (int i = 0; i < alphabet.length; i++) {
            top.add( new TrieNode(alphabet[i], false));
        }
    }

    public boolean searchDictionary(String word) {
        char[] chars = word.toCharArray();
        TrieNode nodePointer;
        int child;
        for (child = 0; child < top.size(); child++) {
            if (top.get(child).letter == chars[0]) {
                break;
            }
            if (child == top.size() - 1) {
                return false; //first character not in top level of dictionary...
            }
        }
        nodePointer = top.get(child);

        for (int i = 1; i < chars.length; i++) {
            for (child = 0; child < nodePointer.children.size(); child++) {
                if (nodePointer.children.get(child).letter == chars[i]) { //this char is in the dictionary!
                    nodePointer = nodePointer.children.get(child);
                    break; //return to outer loop, and continue iterating through the word...
                }
                if (child == nodePointer.children.size() - 1) {
                    return false; //this is NOT a word in our dictionary!
                }
            }
            if (i == chars.length - 1 && nodePointer.isWord) {
                Log.i("searchDictionary(" + word + ")", "FOUND THE WORD!!!!");
                return true; //WE FOUND THE WORD!!!
            }
        }

    return false;}

    public void printSubTries(TrieNode node) {
        Log.i("node.letter = ", "" + node.letter);
        String kids = "";
        if(node.children.size() == 0) {
            Log.i("Children: ", "NONE");
            if(!node.isWord) {
                Log.i("ERROR!!!!", "isWord SHOULD HAVE BEEN SET TO TRUE!!!!");
            }
        }
        else { //the node has children...
            for (int i = 0; i < node.children.size(); i++) {
                kids = kids + node.children.get(i).letter + ' ';
            }
            Log.i("Children: ", kids);
        }
        for (int i = 0; i < node.children.size(); i++) {
            printSubTries(node.children.get(i)); //Recurse!!!!!!
        }

    }

    public void printWordsInTrie(TrieNode node) {
        if (node.isWord) {
            TrieNode dummyNode = node;
            String word = "";
            while (true) {
                word = dummyNode.letter + word;
                if (dummyNode.parent == null) {
                    Log.i("!!!!", word);
                    break;
                } else {
                    dummyNode = dummyNode.parent;
                }
            }
        }
        for (int i = 0; i < node.children.size(); i++) {
            printWordsInTrie(node.children.get(i)); //Recurse!!!!!!
        }
    }

    public void addWord(String word) { //breaks a string into chars and then adds to the trie. Smartly...
//
//        if(word.equals("abundantly")) {
//            System.out.println(word);
//            Log.i("Last word in the list is: ", word + "!!!!!!!!!!!!!!!!");
//
//        }

        char [] chars = word.toCharArray();
        int i = 0;
        //search for top entry point...
        for(i = 0; i < top.size(); i++) {
            if(top.get(i).letter==chars[0]) {
                break;
            }
            else if(i==top.size()) {
                return; // first letter not in dictionary; reject it
            }
        }// found top letter...
        TrieNode nodePointer = top.get(i);

        //two cases: either the child already has been filled, or the spot is empty and needs to be filled.
        //there's some subtlety here; i.e. if we're at the end of the word etc.
        for (int j = 1; j < chars.length; j++) { //for each character in the word...
            int child;

            if(nodePointer.children.size()==0) {
                nodePointer.children.add( new TrieNode(chars[j], false));
                nodePointer.children.get(0).parent = nodePointer; //Luke Skywalker learns that Darth Vader is his dad...
                nodePointer = nodePointer.children.get(0);

            } else {
                for (child = 0; child < nodePointer.children.size(); child++) {//go through all the children at the current pointer location

                    if (nodePointer.children.get(child).letter == chars[j]) { //see if the proper child already exists...
                        nodePointer = nodePointer.children.get(child); //if so, change the pointer target and process the next char from chars.
                        break;
                    }
                    else if(child == nodePointer.children.size()-1) { //if we've iterated through all children without any luck, it's time to add a new node with this char, change the pointer target to this new node, and repeat.
                        nodePointer.children.add(new TrieNode(chars[j], false));
                        nodePointer.children.get(child+1).parent = nodePointer; //Luke Skywalker learns that Darth Vader is his dad...
                        nodePointer = nodePointer.children.get(child+1); //point to the NEW child...

                    }
                }
            }
            if (j == chars.length-1) { //if we're at the end of the word, mark it in the trie.
                nodePointer.isWord = true;
                //            Log.i("Last letter in word is: ", nodePointer.letter + "!!!!!!!!!!!!!!!!");
            }
//            if (j == 0) { //if we're at the end of the word, mark it in the trie.
//                Log.i("FIRST letter in word is: ", nodePointer.letter + "!!!!!!!!!!!!!!!!");
//            }
        }
    }
    /** NOTE: this method was moved from the FANGMAN Assignment here; it made sense to have this
     * in the dictionary class.
     *
     * Method reads list of game-words from the resource file
     *
     * @param context the context object
     * @return the array of words
     */
    private String[] readWordsFromResourceFile(Context context) {

        // create input reader for the word file
        InputStream is = context.getResources().openRawResource(R.raw.word_list);
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(ir);

        // create array-list for holding the words
        ArrayList<String> lines = new ArrayList<String>();

        // read each line as a word; add each to the array-list
        try {
            for (;;) {
                String line = br.readLine();
                if (line == null) break;
                lines.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
        }
        if (lines.size() == 0) {
            // if we did not get any words, put a dummy "word" in
            lines.add("ERROR READING FILE");
        }

        // return the array-version of the word
        return lines.toArray(new String[0]);
    }

}

