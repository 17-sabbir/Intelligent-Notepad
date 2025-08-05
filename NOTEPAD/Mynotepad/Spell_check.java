package Mynotepad;

import java.io.*;
import java.util.*;

public class Spell_check {
    private HashSet<String> Dictionary = new HashSet<>();

    public Spell_check() {
        loadDictionary();
    }

    public void loadDictionary() {
        try {
            File file = new File("Mynotepad\\dictionary.txt");

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String word = sc.nextLine().trim().toLowerCase();
                Dictionary.add(word);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        // for (String word : Dictionary) {
        // System.out.println(word);
        // }

    }

    public int checkword(String check) {
        if (Dictionary.contains(check)) {
            return 1;
        } else {
            return 0;
        }
    }

    public String suggest_word(String word) {
        String suggestword = "";
        int max_distance = Integer.MAX_VALUE;
        if (Dictionary.contains(word)) {
            return "";
        } else {
            for (String string : Dictionary) {
                if (string.charAt(0) == word.charAt(0)) {

                    int min_distance = levenshtien(string, word);
                    if (min_distance < max_distance) {
                        suggestword = string;
                        max_distance = min_distance;
                    }
                }
            }
            return suggestword;
        }
    }

    public int levenshtien(String a, String b) {
        int[][] arr = new int[a.length() + 1][b.length() + 1];
        for (int row = 0; row < a.length() + 1; row++) {
            for (int col = 0; col < b.length() + 1; col++) {
                if (row == 0) {
                    arr[row][col] = col;
                } else if (col == 0) {
                    arr[row][col] = row;
                } else {
                    int cost;
                    if (a.charAt(row - 1) == b.charAt(col - 1)) {
                        cost = 0;
                    } else {
                        cost = 1;
                    }
                    arr[row][col] = cost
                            + Math.min(Math.min(arr[row - 1][col], arr[row - 1][col - 1]), arr[row][col - 1]);
                }
            }
        }
        return arr[a.length()][b.length()];
    }
}