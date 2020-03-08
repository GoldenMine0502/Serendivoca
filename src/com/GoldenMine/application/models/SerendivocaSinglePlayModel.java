package com.GoldenMine.application.models;

import com.GoldenMine.Collection.List.ListSorter;
import com.GoldenMine.SpriteUtility;
import com.GoldenMine.Utility.Point;
import com.GoldenMine.application.TextureButtonSprite;
import com.GoldenMine.application.TextureSprite;
import com.GoldenMine.application.TextureToggleSprite;
import com.GoldenMine.application.WordStorage;
import com.GoldenMine.graphic.elements.*;
import com.GoldenMine.graphic.elements.textures.TextTexture;
import com.GoldenMine.graphic.elements.textures.Texture;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import org.joml.Vector3f;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class SerendivocaSinglePlayModel {
    TextureButtonSprite middleSelectSprite;
    TextureButtonSprite highSelectSprite;
    TextureButtonSprite everydaySelectSprite;
    TextureSprite backgroundTexture;

    TextureToggleSprite modeToggleTexture;

    Font singlePlayTapFont = new Font("a뉴굴림1", 2, 30);
    Font singlePlayLearnFont = new Font("a뉴굴림1", 2, 120);
    private Font singlePlayLearnFontSmall = new Font("a뉴굴림1", 2, 60);

    List<String> wordLists;
    HashMap<String,WordStorage> wordStorages;
    HashMap<String, List<TextureButtonSprite>> selectSprites = new HashMap<>();
    HashMap<String, ListSorter<TextureButtonSprite>> spriteSorters = new HashMap<>();

    TextureSprite TestExecute;

    List<float[]> testHangImagePos = new ArrayList<>();
    List<Vector3f> testHangScreenPos = new ArrayList<>();
    List<TextureSprite> testHang = new ArrayList<>();

    TextureSprite TestO;
    TextureSprite TestX;

    List<TextureButtonSprite> keyboard = new ArrayList<>();

    TextureButtonSprite leftIndex;
    TextureButtonSprite rightIndex;

    TextureButtonSprite backToSinglePlay;

    public TextureSprite getTestExecute() {
        return TestExecute;
    }

    public List<TextureSprite> getTestHang() {
        return testHang;
    }

    public List<TextureButtonSprite>  getKeyboard() {
        return keyboard;
    }

    public TextureButtonSprite getLeftIndex() {
        return leftIndex;
    }

    public TextureButtonSprite getRightIndex() {
        return rightIndex;
    }


    public SerendivocaSinglePlayModel(HashMap<String,WordStorage> wordStorages) {
        this.wordStorages = wordStorages;
    }

    public List<String> getStrLists() {
        return wordLists;
    }

    public void initialize() {
        initializeSelectFolder();
    }

    private void initializeSelectFolder() {
        initalizeSelectFolderMiddle();
        initalizeSelectFolderHigh();
        initalizeSelectFolderEveryday();
        initalizeSingleplayBackground();

        initializeSinglePlayWords();
        initializeSelectMode();
        initializeTest();

        initializeIndex();
        initializeBack();
    }

    public void initializeBack() {
        backToSinglePlay = new TextureButtonSprite();

        try {
            float mag = 0.15f;
            float[] indexPos = SpriteUtility.getDefaultPosition(0.3935f * mag, 0.3971f * mag);

            backToSinglePlay.setTexture(new Texture(indexPos, ImageIO.read(new File("resources/pageleft.png"))));
            backToSinglePlay.setTextureClicked(new Texture(indexPos, ImageIO.read(new File("resources/pageleftclicked.png"))));

            backToSinglePlay.getSprite().setPosition(-1.95f, -1.2f, 0.002f);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initializeIndex() {
        leftIndex = new TextureButtonSprite();
        rightIndex = new TextureButtonSprite();

        try {
            float mag = 0.3f;
            float[] indexPos = SpriteUtility.getDefaultPosition(0.3935f * mag, 0.3971f * mag);


            leftIndex.setTexture(new Texture(indexPos, ImageIO.read(new File("resources/pageleft.png"))));
            leftIndex.setTextureClicked(new Texture(indexPos, ImageIO.read(new File("resources/pageleftclicked.png"))));

            rightIndex.setTexture(new Texture(indexPos, ImageIO.read(new File("resources/pageright.png"))));
            rightIndex.setTextureClicked(new Texture(indexPos, ImageIO.read(new File("resources/pagerightclicked.png"))));

            leftIndex.getSprite().setPosition(-0.5f, -1.03f, 0.002f);
            rightIndex.getSprite().setPosition(0.5f, -1.03f, 0.002f);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initializeTest() {
        try {



            float[] XOPos = SpriteUtility.getDefaultPosition(0.4f, 0.4f);
            float[] executePos = SpriteUtility.getDefaultPosition(0.288f, 0.6f);
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.2f, 0.2f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.05f, 0.05f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.05f, 0.05f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.0118f, 0.15f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.1f, 0.1f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.1f, 0.1f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.1f, 0.1f));
            testHangImagePos.add(SpriteUtility.getDefaultPosition(0.1f, 0.1f));

            testHangScreenPos.add(new Vector3f(-0.8f, 0.5f, -0.00020f));
            testHangScreenPos.add(new Vector3f(-0.86f, 0.54f, -0.00018f));
            testHangScreenPos.add(new Vector3f(-0.74f, 0.54f, -0.00017f));
            testHangScreenPos.add(new Vector3f(-0.8f, 0.15f, -0.00016f));
            testHangScreenPos.add(new Vector3f(-0.88f, 0.23f, -0.00015f));
            testHangScreenPos.add(new Vector3f(-0.72f, 0.23f, -0.00014f));
            testHangScreenPos.add(new Vector3f(-0.88f, -0.04f, -0.00013f));
            testHangScreenPos.add(new Vector3f(-0.72f, -0.04f, -0.00012f));
            testHangScreenPos.add(new Vector3f(-0.8f, 0.5f, -0.00019f));


            TestExecute = new TextureSprite();
            TestX = new TextureSprite();
            TestO = new TextureSprite();

            try {
                for (int i = 0; i < testHangImagePos.size(); i++) {
                    TextureSprite sprite = new TextureSprite();
                    sprite.setTexture(new Texture(testHangImagePos.get(i), ImageIO.read(new File("resources/tests/hang" + (i+1) + ".png"))));

                    Vector3f pos = testHangScreenPos.get(i);

                    sprite.getSprite().setPosition(pos.x, pos.y, pos.z);
                    testHang.add(sprite);
                }
                System.out.println("hang size: " + testHang.size());
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("hang size(e): " + testHang.size() + ", " + testHangImagePos.size());
            }

            TestExecute.setTexture(new Texture(executePos, ImageIO.read(new File("resources/tests/처형대.png"))));
            TestX.setTexture(new Texture(XOPos, ImageIO.read(new File("resources/tests/testX.png"))));
            TestO.setTexture(new Texture(XOPos, ImageIO.read(new File("resources/tests/testO.png"))));

            TestExecute.getSprite().setPosition(-1.075f, 0.3f, -0.0002f);
            TestX.getSprite().setPosition(0f, 0f, 0f);
            TestO.getSprite().setPosition(0f, 0f, 0f);





            //System.out.println(keyboardImage + ", " + keyboardImageClicked);

            float posX = -1.2f;
            float posY = -0.5f;

            int count = 0;

            Font font = new Font("a뉴굴림1", 2, 30);

            for(int index = 'A'; index <= 'Z'; index++) {
                float[] pos = SpriteUtility.getDefaultPosition(0.1f, 0.1f);
                float[] pos2 = SpriteUtility.getDefaultPosition(0.1f, 0.1f);
                BufferedImage keyboardImage = ImageIO.read(new File("resources/keyboard.png"));
                BufferedImage keyboardImageClicked = ImageIO.read(new File("resources/keyboardclicked.png"));

                TextureButtonSprite sprite = new TextureButtonSprite();


                // 크기 66 * 66으로 해야함
                TextTexture textureKeyboardImage = new TextTexture(pos, keyboardImage, new Text(font, String.valueOf((char)index), Color.BLACK), new Point(21, 11));
                TextTexture textureKeyboardImageClicked = new TextTexture(pos2, keyboardImageClicked, new Text(font, String.valueOf((char)index), Color.BLACK), new Point(22, 12));

                //System.out.println(String.valueOf((char)index));

                sprite.setTexture(textureKeyboardImage);
                sprite.setTextureClicked(textureKeyboardImageClicked);

                sprite.getSprite().setPosition(posX, posY, 0.00001f);

                posX+=0.3f;

                count++;
                if(count%9 == 0) {
                    posY-=0.3f;
                    posX = -1.2f;
                    if(count == 18) {
                        posX+=0.15f;
                    }
                }

                //System.out.println(textureKeyboardImage);

                keyboard.add(sprite);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeSelectMode() {
        modeToggleTexture = new TextureToggleSprite();

        float[] position = SpriteUtility.getDefaultPosition(0.298f, 0.1f);

        try {
            modeToggleTexture.addTexture(new Texture(position, ImageIO.read(new File("resources/singleplaymain/wordlearn.png"))));
            modeToggleTexture.addTexture(new Texture(position, ImageIO.read(new File("resources/singleplaymain/wordtest.png"))));
            modeToggleTexture.addTexture(new Texture(position, ImageIO.read(new File("resources/singleplaymain/wordtest2.png"))));
            modeToggleTexture.addTexture(new Texture(position, ImageIO.read(new File("resources/singleplaymain/wordtest3.png"))));
            modeToggleTexture.getSprite().setPosition(1.5f, -1.2f, 0f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeSinglePlayWords() {
        wordLists = new ArrayList<>();

        for(String folder : wordStorages.keySet()) {
            wordLists.add(folder);
            List<TextureButtonSprite> selectSprites = new ArrayList<>();
            for (String section : wordStorages.get(folder).getWordLists()) {
                try {
                    TextTexture texture = SpriteUtility.getWordText(SpriteUtility.getDefaultPosition(0.252f, 0.1917f), ImageIO.read(new File("resources/singleplaymain/folder.png")), new Text(singlePlayTapFont, section, new Color(62, 27, 105)), new Point(0, 0));
                    TextTexture textureClicked = SpriteUtility.getWordText(SpriteUtility.getDefaultPosition(0.252f, 0.1917f), ImageIO.read(new File("resources/singleplaymain/folderclicked.png")), new Text(singlePlayTapFont, section, new Color(255, 255, 255)), new Point(0, 0));

                    TextureButtonSprite buttonSprite = new TextureButtonSprite();
                    buttonSprite.setTexture(texture);
                    buttonSprite.setTextureClicked(textureClicked);

                    selectSprites.add(buttonSprite);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ListSorter<TextureButtonSprite> spriteSorter = new ListSorter<>(selectSprites, null, 15);

            this.selectSprites.put(folder, selectSprites);
            this.spriteSorters.put(folder, spriteSorter);
        }

        //selectMiddleSpritesSorter = new ListSorter<>(selectMiddleSprites, null, 15);
        //selectHighSpritesSorter = new ListSorter<>(selectHighSprites, null, 15);
        //selectEverydaySpritesSorter = new ListSorter<>(selectEverydaySprites, null, 15);
    }

    public ListSorter<TextureButtonSprite> getSections(String type) {
        return spriteSorters.get(type);
    }

    public TextureButtonSprite getSinglePlayMiddleSprite() {
        return middleSelectSprite;
    }

    public TextureButtonSprite getSinglePlayHighSprite() {
        return highSelectSprite;
    }

    public TextureButtonSprite getSinglePlayEverydaySprite() {
        return everydaySelectSprite;
    }

    public TextureSprite getSinglePlayBackgroundSprite() {
        return backgroundTexture;
    }

    public TextureToggleSprite getModeToggleTexture() {
        return modeToggleTexture;
    }

    private void initalizeSelectFolderHigh() {
        float d1 = 0.253f; // 168
        float d2 = 0.172f; //114

        float[] position = SpriteUtility.getDefaultPosition(d1, d2);

        try {
            highSelectSprite = new TextureButtonSprite();
            highSelectSprite.setTexture(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytap.png")), new Text(singlePlayTapFont, "고등", Color.WHITE), new Point(0, -15)));
            highSelectSprite.setTextureClicked(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytapclicked.png")), new Text(singlePlayTapFont, "고등", Color.BLACK), new Point(0, -15)));
            highSelectSprite.getSprite().setPosition(0.8f, 0.93f, -0.003f);
            //singlePlayMiddleSelectTexture = new TextTexture(position, , new Text(mainFont, "중등", Color.WHITE), new Point());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initalizeSelectFolderEveryday() {
        float d1 = 0.253f; // 168
        float d2 = 0.172f; //114

        float[] position = SpriteUtility.getDefaultPosition(d1, d2);

        try {
            everydaySelectSprite = new TextureButtonSprite();
            everydaySelectSprite.setTexture(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytap.png")), new Text(singlePlayTapFont, "생활", Color.WHITE), new Point(0, -15)));
            everydaySelectSprite.setTextureClicked(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytapclicked.png")), new Text(singlePlayTapFont, "생활", Color.BLACK), new Point(0, -15)));
            everydaySelectSprite.getSprite().setPosition(1.4f, 0.93f, -0.003f);
            //singlePlayEverydaySelectSprite = new ButtonSprite(singlePlayEverydaySelectTexture, singlePlayEverydaySelectTextureClicked);
            //singlePlayEverydaySelectSprite.setPosition(1.4f, 0.93f, 0f);


            //singlePlayMiddleSelectTexture = new TextTexture(position, , new Text(mainFont, "중등", Color.WHITE), new Point());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initalizeSingleplayBackground() {
        float d1 = 1.791f; // 0.2f 공백 0.5f
        float d2 = 0.946f;

        float[] position = SpriteUtility.getDefaultPosition(d1, d2);

        try {
            backgroundTexture = new TextureSprite();
            backgroundTexture.setTexture(new Texture(position, ImageIO.read(new File("resources/singleplaymain/singleplayback.png"))));
            backgroundTexture.getSprite().setPosition(0, -0.10f, 0f);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initalizeSelectFolderMiddle() {
        float d1 = 0.253f; // 168
        float d2 = 0.172f; //114

        float[] position = SpriteUtility.getDefaultPosition(d1, d2);

        try {
            middleSelectSprite = new TextureButtonSprite();
            middleSelectSprite.setTexture(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytap.png")), new Text(singlePlayTapFont, "중등", Color.WHITE), new Point(0, -15)));
            middleSelectSprite.setTextureClicked(SpriteUtility.getWordText(position, ImageIO.read(new File("resources/singleplaymain/singleplaytapclicked.png")), new Text(singlePlayTapFont, "중등", Color.BLACK), new Point(0, -15)));
            middleSelectSprite.getSprite().setPosition(0.2f, 0.93f, -0.003f);
            //singlePlayMiddleSelectTexture = new TextTexture(position, , new Text(mainFont, "중등", Color.WHITE), new Point());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Font getVocaLearnFont() {
        return singlePlayLearnFont;
    }

    public Font getVocaLearnFontSmall() {
        return singlePlayLearnFontSmall;
    }

    public TextureSprite getTestO() {
        return TestO;
    }

    public TextureSprite getTestX() {
        return TestX;
    }

    public TextureButtonSprite getBackToSinglePlay() {
        return backToSinglePlay;
    }
}
