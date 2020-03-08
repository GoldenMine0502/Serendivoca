package com.GoldenMine.application;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class WordStorage {
    HashMap<String, List<Word>> words = new HashMap<>();

    List<String> wordNames = new ArrayList<>();

    List<Word> lastLoaded;

    File srcFolder;
    String srcRoute;

    public WordStorage(String route) {
        srcFolder = new File(route);
        srcRoute = route;

        for(File file : srcFolder.listFiles()) {

            if(!file.isDirectory()) {
                String fileName = removeFileNameExtention(file.getName());

                List<Word> fileWord = new ArrayList<>();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                    String line;

                    while ((line = reader.readLine()) != null) {
                        int index = line.indexOf(", ");
                        String word = line.substring(0, index);
                        String mean = line.substring(index + 2, line.length());
                        fileWord.add(new Word(word, mean));
                    }

                    wordNames.add(fileName);
                    words.put(fileName, fileWord);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(StringIndexOutOfBoundsException ex) {
                    System.out.println(fileName + ", " + file.getPath());
                    ex.printStackTrace();
                }
            }
        }

        Collections.sort(wordNames);
    }

    public List<? extends String> getWordLists() {
        return wordNames;
    }

    public void loadImage(String section) {
        if(lastLoaded!=null) {
            for(Word w : lastLoaded) {
                w.resetImage();
            }
        }
        List<Word> wordList = words.get(section);

        lastLoaded = wordList;

        String currentRoot = srcRoute + "/images/" + formatWithStorage(section) + "/";

        for(Word w : wordList) {

            File jpg = new File( currentRoot + w.getWord() + ".jpg");
            if (jpg.exists()) {
                w.setImage(jpg);
            } else {
                w.setImage(new File( currentRoot + w.getWord() + ".png"));
            }
        }
    }

    public static String formatWithStorage(String src) {
        char ch = src.charAt(src.length()-1);
        if('0'<=ch && ch<='9') {
            int index = src.lastIndexOf(" ");

            return src.substring(0, index);
        }
        return src;
    }

    public static String removeFileNameExtention(String src) {
        int index = src.lastIndexOf(".");
        //System.out.println(src);
        return src.substring(0, index);
    }

    public List<Word> getWords(String section) {
        return words.get(section);
    }
}
