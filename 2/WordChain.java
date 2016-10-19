/*
 * NOTE: It was assumed that a single word was not a valid chain, that is to say that if N == 1 the output will
 * be "IMPOSSIBLE"
*/

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class WordChain {
    private static int numWords;
    private static String[] inputWords;

    public static void main(String[] args) {
        //Assumed input will be at least one word
        Scanner in = new Scanner(System.in);
        numWords = in.nextInt();
        inputWords = new String[numWords];
        for(int i = 0; i < numWords; i++) {
            inputWords[i] = in.next();
        }
        in.close();
        ArrayList<String> currentChain = new ArrayList<String>();
        currentChain.add(inputWords[0]);

        //Attempted paths stored as their indices in the original input array to allow for duplicates
        HashMap<String, ArrayList<Integer>> attempts = new HashMap<>();

        for(int i = 0; i < inputWords.length; i++) {
            attempts.put(inputWords[i], new ArrayList<Integer>());
        }

        if(numWords > 1 && getWordChain(currentChain, 0, attempts)) {
            for(int i = 0; i < currentChain.size(); i++) {
                System.out.println(currentChain.get(i));
            }
        } else {
            System.out.println("IMPOSSIBLE");
        }
    }

    public static boolean getWordChain(ArrayList<String> words, int depth, HashMap<String, ArrayList<Integer>> attemptedPaths) {
        String currentWord = words.get(depth);

        //Allows last word to be from a previously failed path, as it won't have to link to anything
        if(depth == numWords - 2) {
            int missingWordIndex = -1;
            for(int i = 0; i < inputWords.length; i++) {
                if(!words.contains(inputWords[i])) {
                    missingWordIndex = i;
                }
            }
            ArrayList<Integer> traversed = attemptedPaths.get(currentWord);
            if(traversed.contains(missingWordIndex)) {
                traversed.remove((Object)missingWordIndex);
            }
        }

        String next = findLink(currentWord, attemptedPaths);
        if(depth == numWords - 1) {
            return true;
        } else if(depth > 0 && next == null) {
            words.remove(depth);
            return getWordChain(words, --depth, attemptedPaths);
        } else if(next != null) {
            words.add(next);
            return getWordChain(words, ++depth, attemptedPaths);
        } else {
            return false;
        }
    }

    private static String findLink(String word, HashMap<String, ArrayList<Integer>> attempted) {
        ArrayList<Integer> attemptedPaths = attempted.get(word);
        String next = null;
        int i = 0;
        boolean hasMatch = false;

        while(i < inputWords.length && !hasMatch) {
            int charIndex = 3;
            if (inputWords[i].equals(word) || attemptedPaths.contains(i)) {
                i++;
            } else {
                while (charIndex <= word.length() && charIndex <= inputWords[i].length() && hasMatch == false) {
                    String end = word.substring(word.length() - (charIndex), (word.length()));
                    String start = inputWords[i].substring(0, charIndex);
                    if (end.equals(start)) {
                        hasMatch = true;
                        next = inputWords[i];
                        attemptedPaths.add(i);
                    } else {
                        charIndex++;
                    }
                }
                i++;
            }
        }

        if(hasMatch) return next;
        else return null;
    }
}
