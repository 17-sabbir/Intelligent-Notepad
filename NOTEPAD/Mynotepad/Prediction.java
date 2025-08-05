package Mynotepad;

import java.util.*;
import java.io.*;

public class Prediction {
    Map<String, Map<String, Integer>> unigram_freq = new HashMap<>();
    Map<String, Map<String, Integer>> bigram_freq = new HashMap<>();
    Map<String, Map<String, Integer>> trigram_freq = new HashMap<>();

    public Prediction() {
        loadTxtfile();
    }

    private void loadTxtfile() {

        try {
            File f = new File("Mynotepad\\TrainData.txt");
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().toLowerCase();
                String[] words = line.replaceAll("[,.:;!?]", " ").split("\\s+");

                for (int i = 0; i < words.length; i++) {

                    if (i < words.length - 1 && !words[i].isEmpty() && !words[i + 1].isEmpty()) {
                        String uni_g= words[i];
                        String next_w = words[i + 1];

                        if (unigram_freq.containsKey(uni_g)) {

                            Map<String, Integer> nextWordMap = unigram_freq.get(uni_g);
                            if (nextWordMap.containsKey(next_w)) {
                                nextWordMap.put(next_w, nextWordMap.get(next_w) + 1);
                            } else {
                                nextWordMap.put(next_w, 1);
                            }
                        } else {
                            Map<String, Integer> newMap = new HashMap<>();
                            newMap.put(next_w, 1);
                            unigram_freq.put(uni_g, newMap);
                        }

                    }

                    if (i < words.length - 2 && !words[i].isEmpty() && !words[i + 1].isEmpty()) {
                        String bi_g= words[i] + " " + words[i + 1];
                        String next_w = words[i + 2];
                        if (bigram_freq.containsKey(bi_g)) {

                            Map<String, Integer> nextWordMap = bigram_freq.get(bi_g);
                            if (nextWordMap.containsKey(next_w)) {
                                nextWordMap.put(next_w, nextWordMap.get(next_w) + 1);
                            } else {
                                nextWordMap.put(next_w, 1);
                            }
                        } else {
                            Map<String, Integer> newMap = new HashMap<>();
                            newMap.put(next_w, 1);
                            bigram_freq.put(bi_g, newMap);
                        }

                    }

                    if (i < words.length - 3 && !words[i].isEmpty() && !words[i + 1].isEmpty()
                            && !words[i + 2].isEmpty()) {
                        String tri_g = words[i] + " " + words[i + 1] + " " + words[i + 2];
                        String next_w = words[i + 3];
                        if (trigram_freq.containsKey(tri_g)) {

                            Map<String, Integer> nextWordMap = trigram_freq.get(tri_g);
                            if (nextWordMap.containsKey(next_w)) {
                                nextWordMap.put(next_w, nextWordMap.get(next_w) + 1);
                            } else {
                                nextWordMap.put(next_w, 1);
                            }
                        } else {
                            Map<String, Integer> newMap = new HashMap<>();
                            newMap.put(next_w, 1);
                            trigram_freq.put(tri_g, newMap);
                        }
                    }
                }
                sc.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Map.Entry<String, Integer>> predictNextWord(String sentence) {
        ArrayList<Map.Entry<String, Integer>> topPredictions = new ArrayList<>();
        String[] words = sentence.toLowerCase().split("\\s+");

        if (words.length == 1) {
            String unigram = words[0];
            if (unigram_freq.containsKey(unigram)) {
                Map<String, Integer> predictions = unigram_freq.get(unigram);
                ArrayList<Map.Entry<String, Integer>> sortedPredictions = new ArrayList<>(predictions.entrySet());
                sortedPredictions.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                topPredictions = new ArrayList<>(sortedPredictions.subList(0, Math.min(3, sortedPredictions.size())));

            }
        } else if (words.length == 2) {
            String bigram = words[0] + " " + words[1];
            if (bigram_freq.containsKey(bigram)) {
                Map<String, Integer> predictions = bigram_freq.get(bigram);
                ArrayList<Map.Entry<String, Integer>> sortedPredictions = new ArrayList<>(predictions.entrySet());
                sortedPredictions.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                topPredictions = new ArrayList<>(sortedPredictions.subList(0, Math.min(3, sortedPredictions.size())));
            }
        } else if (words.length >= 3) {
            String trigram = words[words.length - 3] + " " + words[words.length - 2] + " " + words[words.length - 1];
            if (trigram_freq.containsKey(trigram)) {
                Map<String, Integer> predictions = trigram_freq.get(trigram);
                ArrayList<Map.Entry<String, Integer>> sortedPredictions = new ArrayList<>(predictions.entrySet());
                sortedPredictions.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                topPredictions = new ArrayList<>(sortedPredictions.subList(0, Math.min(3, sortedPredictions.size())));
            }
        } else {
            System.out.println("Input is too short for prediction.");
        }
        return topPredictions;
    }
}