package com.GoldenMine.application.controllers;

import com.GoldenMine.ImageUtility;
import com.GoldenMine.SpriteUtility;
import com.GoldenMine.Utility.Point;
import com.GoldenMine.application.*;
import com.GoldenMine.application.models.SerendivocaModel;
import com.GoldenMine.event.EffectFader;
import com.GoldenMine.event.EffectMover;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.SpriteData;
import com.GoldenMine.graphic.elements.Text;
import com.GoldenMine.graphic.elements.textures.TextTexture;
import com.GoldenMine.graphic.elements.textures.Texture;
import com.GoldenMine.graphic.event.SpriteClickedEvent;
import com.GoldenMine.util.interval.CalculateModelNaturalSin;
import com.GoldenMine.util.interval.Interval;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.*;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Created by ehe12 on 2018-09-12.
 */
public class SerendivocaSinglePlayController {
    SerendivocaModel model;
    SerendivocaView view;

    Thread vocaTestThread;
    Thread vocaLearnThread;
    //int vocaTestIndex = 0;
    private static final String WORD_MIDDLE = "middle";
    private static final String WORD_HIGH = "high";
    private static final String WORD_EVERYDAY = "everyday";

    String wordType;
    int index;
    int maxIndex;

    final float centerPos = 0.4f;
    //final float centerPos2 = -0.5f;
    final float plma = 0.2f;
    //final float plma2 = 0.15f;

    boolean inTesting = false;

    int wordTestIndex;
    List<List<TextureSprite>> wordSprites = new ArrayList<>();
    List<List<SpriteData>> wordSpriteData = new ArrayList<>();

    String currentWord;
    int lastLen = 0;

    int error = 0;
    int correct = 0;
    int wordContains = 0;
    int hang = 0;

    //Queue<Character> queue = new LinkedList<>();

    HashSet<Character> queued = new HashSet<>();

    List<TextureButtonSprite> lastSprites;

    public SerendivocaSinglePlayController(SerendivocaModel model, SerendivocaView view) {
        this.model = model;
        this.view = view;
    }

    public void initialize() {
        initializeTestEvent();
        initializeTest();
        initializeFromSingle();
        initalizeSinglePlay();
    }

    public void initializeFromSingle() {
        model.getSinglePlayModel().getBackToSinglePlay().getSprite().addButtonClickedEvent(b -> {
            if (b) {
                if(vocaLearnThread!=null) {
                    vocaLearnThread.interrupt();
                    vocaLearnThread = null;
                }
                if(vocaTestThread!=null) {
                    vocaTestThread.interrupt();
                    vocaTestThread=null;
                }
            }
        });
    }

    public void initializeTestEvent() {
        glfwSetKeyCallback(view.getPalette().getWindow().getWindowId(), new GLFWKeyCallbackI() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //System.out.println("key: " + key + ", keyChar: " + (char)key + ", scancode: " + scancode + ", action: " + action + ", mods: " + mods);

                if ('A' <= key && key <= 'Z') {
                    if (action == 1) {
                        notifyKeyboardInput((char) key);
                        model.getSinglePlayModel().getKeyboard().get(key - 65).getSprite().invoke("click", true);
                    } else {
                        model.getSinglePlayModel().getKeyboard().get(key - 65).getSprite().invoke("click", false);
                    }
                }
            }
        });
    }

    public synchronized void notifyKeyboardInput(char ch) {
        if (inTesting && currentWord != null) {
            if (!queued.contains(ch)) {
                view.disableKeyboard(ch);

                queued.add(ch);

                List<Integer> contain = contains(ch);
                System.out.println("S: " + wordContains + ", " + contain.size() + ", " + hang + ", " + correct + ", " + error + ", " + currentWord.length() + ", " + view.getTestHangData().size());
                if (contain.size() == 0) {

                    if (hang < view.getTestHangData().size()) {
                        SpriteData hangData = view.getTestHangData().get(hang);
                        hangData.removeAllEffects();
                        hangData.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), hangData.getOpacity(), 1f);
                        hangData.setEnabled(true);
                        if (hang == view.getTestHangData().size() - 1) {
                            error++;
                            //currentWord = null;
                            vocaTestThread.interrupt();
                        }
                    }
                    hang++;
                } else {
                    wordContains += contain.size();

                    List<SpriteData> words = wordSpriteData.get(wordTestIndex);

                    for (int point : contain) {
                        SpriteData wordData = words.get(point);
                        wordData.setEnabled(true);
                        wordData.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 0f, 1f);
                    }


                    if (wordContains == currentWord.length()) {
                        correct++;
                        //currentWord = null;
                        vocaTestThread.interrupt();
                        //System.out.println("correct");
                    }
                }
                System.out.println("F: " + wordContains + ", " + contain.size() + ", " + hang + ", " + correct + ", " + error + ", " + view.getTestHangData().size());
            }
        } else

        {
            queued.clear();
        }

    }

    public List<Integer> contains(char verify) {
        List<Integer> contain = new ArrayList<>();

        String tempWord = currentWord.toUpperCase();

        if (tempWord != null) {
            for (int i = 0; i < tempWord.length(); i++) {
                char ch = tempWord.charAt(i);

                if (ch == verify) {
                    contain.add(i);
                }
            }
        }
        return contain;
    }

    public List<Word> getShuffleList(List<Word> list) {
        List<Word> word = new ArrayList<>();
        word.addAll(list);
        Collections.shuffle(word);

        return word;
    }

    public void startVocaLearn(String type, String wordType) {
        WordStorage storage = model.getWordStorages().get(wordType);
        storage.loadImage(type);

        List<Word> words = storage.getWords(type);
        Font font = model.getSinglePlayModel().getVocaLearnFont();

        List<TextureSprite> sprites = new ArrayList<>();
        List<SpriteData> spriteData = new ArrayList<>();


        List<Word> shuffleList = getShuffleList(words);

        List<BufferedImage> toFlushImages = new ArrayList<>();

        for (Word word : shuffleList) {
            BufferedImage wordImage = ImageUtility.createImageFromText(font, word.getWord(), Color.BLACK);
            BufferedImage meanImage = ImageUtility.createImageFromText(font, word.getMean(), Color.BLACK);

            toFlushImages.add(wordImage);
            toFlushImages.add(meanImage);

            float magnifyWordImage = (float) wordImage.getWidth() / (float) (wordImage.getHeight());
            float magnifyMeanImage = (float) meanImage.getWidth() / (float) (meanImage.getHeight());


            //System.out.println(magnifyWordImage/2);

            float ySize = 0.15f;
            float xSizeWord = ySize * (magnifyWordImage);
            float xSizeMean = ySize * (magnifyMeanImage);

            TextureSprite wordTexture = new TextureSprite();
            TextureSprite meanTexture = new TextureSprite();
            TextureSprite imageTexture = null;
            SpriteData data3 = null;
            try {
                if (word.getImage() != null) {
                    imageTexture = new TextureSprite();
                    BufferedImage imageImage = word.getImage();
                    //toFlushImages.add(imageImage);
                    float magnifyImageImage = (float) imageImage.getWidth() / imageImage.getHeight();

                    imageTexture.setTexture(new Texture(SpriteUtility.getDefaultPosition(0.6f * (magnifyImageImage), 0.6f), imageImage));
                    imageTexture.getSprite().setPosition(0f, -0.35f, -0.0002f);
                    data3 = view.getPalette().addSprite(imageTexture.getSprite());
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            wordTexture.setTexture(new Texture(SpriteUtility.getDefaultPosition(xSizeWord, ySize), wordImage));
            meanTexture.setTexture(new Texture(SpriteUtility.getDefaultPosition(xSizeMean, ySize), meanImage));
            wordTexture.getSprite().setPosition(-1.7f + xSizeWord, 0.9f, -0.0002f);
            meanTexture.getSprite().setPosition(1.7f - xSizeMean, 0.9f, -0.0002f);

            SpriteData data = view.getPalette().addSprite(wordTexture.getSprite());
            SpriteData data2 = view.getPalette().addSprite(meanTexture.getSprite());

            sprites.add(wordTexture);
            sprites.add(meanTexture);
            sprites.add(imageTexture);

            spriteData.add(data);
            spriteData.add(data2);
            spriteData.add(data3);

            data.setOpacity(0);
            data2.setOpacity(0);
            if (data3 != null)
                data3.setOpacity(0);
            data.setEnabled(false);
            data2.setEnabled(false);
            if (data3 != null)
                data3.setEnabled(false);
        }

        if(vocaLearnThread==null) {
            vocaLearnThread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000L);
                        //List<TextureButtonSprite> sprite = model.getSinglePlayModel().getSections(wordType).getSortedPageList(wordTestIndex);

                        view.startBackToSinglePlay();

                        for (int index = 0; index < sprites.size() / 3; index++) {
                            if (vocaLearnThread == null) {
                                break;
                            }
                            SpriteData word = spriteData.get(3 * index);
                            SpriteData mean = spriteData.get(3 * index + 1);
                            SpriteData image = spriteData.get(3 * index + 2);

                            try {
                                word.setEnabled(true);
                                mean.setEnabled(true);
                                if (image != null)
                                    image.setEnabled(true);

                                word.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), 0f, 1f);
                                Thread.sleep(3000L);
                                if (image != null)
                                    image.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), 0f, 1f);
                                Thread.sleep(3000L);
                                mean.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), 0f, 1f);
                                Thread.sleep(3000L);

                            } catch (InterruptedException ex) {
                                //ignore
                            }

                            word.removeAllEffects();
                            word.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), word.getOpacity(), 0f);
                            if (image != null) {
                                image.removeAllEffects();
                                image.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), image.getOpacity(), 0f);
                            }
                            mean.removeAllEffects();
                            mean.addEffect(new EffectFader(), new Interval(0, 20, new CalculateModelNaturalSin()), mean.getOpacity(), 0f);

                            Thread.sleep(1000L);

                            word.setEnabled(false);
                            mean.setEnabled(false);
                            if (image != null)
                                image.setEnabled(false);
                        }


                        System.out.println("end");

                        for (int i = 0; i < sprites.size(); i++) {
                            TextureSprite sprite = sprites.get(i);

                            if (sprite != null) {
                                //view.getPalette().deleteSpriteOnNextFrame(sprite.getSprite());
                                view.getPalette().deleteElementOnNextFrame(sprite.getTexture());
                                view.getPalette().deleteSprite(sprite.getSprite());
                                //sprite.getTexture().cleanUp();
                                //sprite.getSprite().getObjectElement().cleanUp();
                                //sprite.getTexture().cleanUp();
                                //sprite.getSprite().getObjectElement().cleanUp();
                            }
                        }
                        for(int i = 0; i < toFlushImages.size(); i++) {
                            toFlushImages.get(i).flush();
                        }

                        for(int i = 0; i < shuffleList.size(); i++) {
                            shuffleList.get(i).resetImage();
                        }

                        System.gc();

                        vocaLearnThread = null;
                        view.finishBackToSinglePlay();
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    view.singlePlayGroupStart(lastSprites);
                }
            };
        }

        try {
            if (vocaLearnThread != null) {
                synchronized (vocaLearnThread) {
                    vocaLearnThread.start();
                }
            }
        } catch(Exception ex) {
            System.out.println("동시실행learn");
        }

        //System.out.println("learn: " + type);


        //model.getEverydayWordStorage();
    }

    public void startVocaTest(String type, String wordType) {


        WordStorage storage = model.getWordStorages().get(wordType);
        storage.loadImage(type);

        List<Word> words = storage.getWords(type);
        Font font = model.getSinglePlayModel().getVocaLearnFontSmall();

        List<TextureSprite> sprites = new ArrayList<>();
        List<SpriteData> spriteData = new ArrayList<>();

        List<TextureSprite> taps = new ArrayList<>();
        List<SpriteData> tapData = new ArrayList<>();

        List<Word> shuffleList = getShuffleList(words);

        List<BufferedImage> toFlushImages = new ArrayList<>();

        float magWord = .0015f;
        float magTap =  .0020f;
        Point axis = new Point(0, 0);

        int maxLen = 0;

        for (Word word : shuffleList) {
            int wordLen = word.getWord().replace(" ", "").length();

            if (maxLen < wordLen) {
                maxLen = wordLen;
            }

            List<TextureSprite> wordSprite = new ArrayList<>();
            List<SpriteData> wordSpriteData = new ArrayList<>();

            TextureSprite imageTexture = null;
            SpriteData data = null;
            try {
                if (word.getImage() != null) {
                    imageTexture = new TextureSprite();
                    BufferedImage imageImage = word.getImage();
                    float magnifyImageImage = (float) imageImage.getWidth() / imageImage.getHeight();

                    imageTexture.setTexture(new Texture(SpriteUtility.getDefaultPosition(0.5f * (magnifyImageImage), 0.5f), imageImage));
                    imageTexture.getSprite().setPosition(0.4f, 0.6f, -0.0002f);
                    data = view.getPalette().addSprite(imageTexture.getSprite());
                    data.setEnabled(false);
                    data.setOpacity(0);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            String wordStr = word.getWord();

            float wordPos = centerPos - plma * (wordLen - 1) / 2 + plma / 4;

            for (int i = 0; i < wordStr.length(); i++) {
                char ch = wordStr.charAt(i);

                if (ch != ' ') {
                    TextureSprite text = SpriteUtility.getTextSprite(magWord, new Text(font, String.valueOf(ch), Color.BLACK), axis);
                    text.getSprite().setPosition(wordPos, -0.1f, 0f);

                    toFlushImages.add(((Texture)text.getTexture()).getSourceImage());

                    SpriteData textSpriteData = view.getPalette().addSprite(text.getSprite());
                    textSpriteData.setEnabled(false);
                    textSpriteData.setOpacity(0);
                    wordSprite.add(text);
                    wordSpriteData.add(textSpriteData);

                    wordPos += plma;
                }
            }

            sprites.add(imageTexture);
            spriteData.add(data);
            this.wordSprites.add(wordSprite);
            this.wordSpriteData.add(wordSpriteData);
        }

        float tapPos = centerPos - plma * (maxLen - 1) / 2 + plma / 4 + 0.02f;

        for (int i = 0; i < maxLen; i++) {
            TextureSprite tap = SpriteUtility.getTextSprite(magTap, new Text(font, "_", Color.BLACK), axis);
            SpriteData tapDataElement = view.getPalette().addSprite(tap.getSprite());

            tapDataElement.setEnabled(false);
            tapDataElement.setOpacity(0);
            tap.getSprite().setPosition(tapPos, -0.15f, 0);

            taps.add(tap);
            tapData.add(tapDataElement);

            tapPos += plma;
        }

        if (vocaTestThread == null) {
            vocaTestThread = new Thread() {
                public void run() {
                    view.startBackToSinglePlay();

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        inTesting = true;

                        view.keyboardGroupStart();

                        //String currentText;
                        view.startTest(60);

                        lastLen = 0;

                        for (int index = 0; index < sprites.size(); index++) {
                            if (vocaTestThread == null) {
                                break;
                            }
                            //TextureSprite sprite = sprites.get(wordTestIndex);

                            SpriteData data = spriteData.get(index);
                            List<SpriteData> segments = wordSpriteData.get(index);
                            Word word = shuffleList.get(index);

                            queued.clear();
                            wordContains = 0;
                            hang = 0;
                            view.keyboardGroupStart();
                            wordTestIndex = index;

                            currentWord = word.getWord().replace(" ", "");
                            //String text = ((TextTexture) sprite.getTexture()).getText();


                            if(currentWord.length() < lastLen) {
                                for(int i = currentWord.length(); i < lastLen; i++) {
                                    SpriteData tapDataElement = tapData.get(i);

                                    System.out.println(i + "on1");
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 0f);
                                }
                            } else if(currentWord.length() > lastLen) {

                                float tapPos = centerPos - plma * (currentWord.length() - 1) / 2 + plma / 4 + 0.02f;
                                tapPos+=lastLen*plma;

                                for(int i = lastLen; i < currentWord.length(); i++) {
                                    Sprite tap = taps.get(i).getSprite();
                                    SpriteData tapDataElement = tapData.get(i);

                                    System.out.println(i + "on2");
                                    tap.setPosition(tapPos, tap.getPosition().y, 0f);
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 1f);
                                    tapPos+=plma;
                                }
                            }

                            float tapPos = centerPos - plma * (currentWord.length() - 1) / 2 + plma / 4 + 0.02f;

                            for(int tapIndex = 0; tapIndex < currentWord.length(); tapIndex++) {
                                Sprite sprite = taps.get(tapIndex).getSprite();
                                SpriteData tapDataElement = tapData.get(tapIndex);

                                System.out.println(tapIndex + "move or fade");
                                if(index == 0) {
                                    sprite.setPosition(tapPos, sprite.getPosition().y, 0f);
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 1f);
                                } else {
                                    tapDataElement.addEffect(new EffectMover(), new Interval(0, 45, new CalculateModelNaturalSin()), "x", sprite.getPosition().x, tapPos);
                                }
                                tapPos += plma;
                            }

                            if(data!=null) {
                                data.setEnabled(true);

                                data.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), 0f, 1f);
                            }

                            try {
                                while (true) {
                                    Thread.sleep(Integer.MAX_VALUE);
                                }
                            } catch (InterruptedException ex) {
                                //ignore
                            }
                            if (vocaTestThread != null) {
                                for (int i = 0; i < segments.size(); i++) {
                                    SpriteData segment = segments.get(i);

                                    segment.setEnabled(true);
                                    segment.removeAllEffects();
                                    segment.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), segment.getOpacity(), 1f);
                                }

                                Thread.sleep(2000L);
                            }
                            synchronized (currentWord) {
                                lastLen = currentWord.length();
                                currentWord = null;
                            }

                            if(data!=null) {
                                data.setEnabled(false);
                                data.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), 1f, 0f);
                            }
                            view.startTest(30);
                            for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
                                SpriteData segment = segments.get(segmentIndex);
                                segment.removeAllEffects();
                                segment.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), segment.getOpacity(), 0f);
                            }

                            Thread.sleep(500L);
                        }

                        for(int tapIndex = 0; tapIndex < taps.size(); tapIndex++) {
                            SpriteData tap = tapData.get(tapIndex);
                            tap.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), tap.getOpacity(), 0f);
                        }

                        view.keyboardGroupFinish();
                        view.finishTest(60);
                        view.finishBackToSinglePlay();

                        vocaTestThread = null;
                        lastLen = 0;

                        Thread.sleep(1000L);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    view.singlePlayGroupStart(lastSprites);

                    for (int i = 0; i < sprites.size(); i++) {
                        TextureSprite sprite = sprites.get(i);

                        if (sprite != null) {
                            view.getPalette().deleteSpriteOnNextFrame(sprite.getSprite());
                            //sprite.getTexture().cleanUp();
                            //sprite.getSprite().getObjectElement().cleanUp();
                        }
                    }

                    for(int i = 0; i < taps.size(); i++) {
                        view.getPalette().deleteSpriteOnNextFrame(taps.get(i).getSprite());
                    }

                    for(int i = 0; i < toFlushImages.size(); i++) {
                        toFlushImages.get(i).flush();
                    }

                    for(int i = 0; i < shuffleList.size(); i++) {
                        shuffleList.get(i).resetImage();
                    }

                    inTesting = false;

                    System.gc();
                }
            };
            try {
                if (vocaTestThread != null) {
                    synchronized (vocaTestThread) {
                        vocaTestThread.start();
                    }
                }
            } catch (Exception ex) {
                System.out.println("동시실행");
            }
        }
    }

    public void initializeTest() {
        List<TextureButtonSprite> sprites = model.getSinglePlayModel().getKeyboard();

        for (int i = 0; i < sprites.size(); i++) {
            int index = i;

            sprites.get(index).getSprite().addButtonClickedEvent(new SpriteClickedEvent() {
                @Override
                public void onClicked(boolean b) {
                    if (b) {
                        notifyKeyboardInput(((TextTexture) sprites.get(index).getTexture()).getText().charAt(0));
                    }
                }
            });
        }
    }

    public void setPage() {
        //System.out.println(lastSprites!=null);
        if(lastSprites!=null) {
            view.disableSprites(lastSprites, false);
        }
        //view.disableSprites(true);

        //disableAllFolders();
        float startX = -1.2f;
        float startY = 0.42f;
        float plusX = 0.6f;
        float plusY = -0.5f;


        List<TextureButtonSprite> sprites = model.getSinglePlayModel().getSections(wordType).getList();

        List<TextureButtonSprite> last = new ArrayList<>();

        //System.out.println("set last sprite");

        //System.out.println(sprites.size());

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
                float posX = startX + x * plusX;
                float posY = startY + y * plusY;

                int listIndex = x + y * 5;
                int allListIndex = 15 * (index - 1) + listIndex;

                //System.out.println(allListIndex + ", " + sprites.size() + ", " + posX + ", " + posY);
                if (allListIndex < sprites.size()) {
                    TextureButtonSprite sprite = sprites.get(allListIndex);
                    sprite.getSprite().setPosition(posX, posY, 0.001f);
                    view.enableFolder(wordType, allListIndex);

                    last.add(sprite);
                    //List<SpriteData> spriteData = view.getSpriteList(wordType);

                    //spriteData.get(allListIndex).setEnabled(true);
                    //spriteData.get(allListIndex).setOpacity(1);
                }
            }
        }

        lastSprites = last;
    }

    public void initalizeSinglePlay() {
        for (String str : model.getSinglePlayModel().getStrLists()) {
            List<TextureButtonSprite> list = model.getSinglePlayModel().getSections(str).getList();
            for (int i = 0; i < list.size(); i++) {
                TextureButtonSprite sprite = list.get(i);

                sprite.getSprite().addButtonClickedEvent(b -> {
                    if (b)
                        invokeFolder(sprite);
                });
            }
        }

        wordType = WORD_MIDDLE;
        index = 1;

        model.getSinglePlayModel().getSinglePlayMiddleSprite().getSprite().addButtonClickedEvent(b -> {
            if (b) {

                wordType = WORD_MIDDLE;
                index = 1;
                setPage();
            }
        });
        model.getSinglePlayModel().getSinglePlayHighSprite().getSprite().addButtonClickedEvent(b -> {
            if (b) {
                wordType = WORD_HIGH;
                index = 1;
                setPage();
            }
        });
        model.getSinglePlayModel().getSinglePlayEverydaySprite().getSprite().addButtonClickedEvent(b -> {
            if (b) {
                wordType = WORD_EVERYDAY;
                index = 1;
                setPage();
            }
        });

        model.getSinglePlayModel().getLeftIndex().getSprite().addButtonClickedEvent(b -> {
            if (b) {
                if (index >= 2) {
                    index--;
                }
                setPage();
            }
        });

        model.getSinglePlayModel().getRightIndex().getSprite().addButtonClickedEvent(b -> {
            if (b) {
                List<TextureButtonSprite> sprites = model.getSinglePlayModel().getSections(wordType).getList();

                if ((index) * 15 < sprites.size()) {
                    index++;
                }
                setPage();
            }
        });
    }

    public void invokeFolder(TextureButtonSprite sprite) {
        //lastSprites = null;
        //System.out.println(wordType + ", " + index + ", " + lastSprites);

        int index = model.getSinglePlayModel().getModeToggleTexture().getSprite().getIndex();
        switch (index) {
            case 0:
                startVocaLearn(((TextTexture) sprite.getTexture()).getText(), wordType);
                break;
            case 1:
                startVocaTest(((TextTexture) sprite.getTexture()).getText(), wordType);
                break;
            case 2:
                startVocaTest2(((TextTexture) sprite.getTexture()).getText(), wordType);
                break;
            case 3:
                startVocaTest3(((TextTexture) sprite.getTexture()).getText(), wordType);
                break;
        }

        view.singlePlayGroupFinish(lastSprites);
        //System.out.println("NULL");
    }

    public void startVocaTest2(String type, String wordType) {
        WordStorage storage = model.getWordStorages().get(wordType);
        storage.loadImage(type);

        List<Word> words = storage.getWords(type);
        Font font = model.getSinglePlayModel().getVocaLearnFontSmall();

        List<TextureSprite> sprites = new ArrayList<>();
        List<SpriteData> spriteData = new ArrayList<>();

        List<TextureSprite> taps = new ArrayList<>();
        List<SpriteData> tapData = new ArrayList<>();

        List<Word> shuffleList = getShuffleList(words);

        List<BufferedImage> toFlushImages = new ArrayList<>();

        float magWord = .00075f;
        float magTap =  .0010f;
        Point axis = new Point(0, 0);

        int maxLen = 0;

        for (Word word : shuffleList) {
            int wordLen = word.getWord().replace(" ", "").length();

            if (maxLen < wordLen) {
                maxLen = wordLen;
            }

            List<TextureSprite> wordSprite = new ArrayList<>();
            List<SpriteData> wordSpriteData = new ArrayList<>();

            TextureSprite imageTexture = null;
            SpriteData data = null;
            try {
                if (word.getImage() != null) {
                    imageTexture = new TextureSprite();
                    BufferedImage imageImage = word.getImage();
                    float magnifyImageImage = (float) imageImage.getWidth() / imageImage.getHeight();

                    imageTexture.setTexture(new Texture(SpriteUtility.getDefaultPosition(0.25f * (magnifyImageImage), 0.25f), imageImage));
                    imageTexture.getSprite().setPosition(-0.4f, 0.6f, -0.0002f);
                    data = view.getPalette().addSprite(imageTexture.getSprite());
                    data.setEnabled(false);
                    data.setOpacity(0);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            String wordStr = word.getWord();

            float wordPos = centerPos - plma * (wordLen - 1) / 2 + plma / 4;

            for (int i = 0; i < wordStr.length(); i++) {
                char ch = wordStr.charAt(i);

                if (ch != ' ') {
                    TextureSprite text = SpriteUtility.getTextSprite(magWord, new Text(font, String.valueOf(ch), Color.BLACK), axis);
                    text.getSprite().setPosition(wordPos, -0.1f, 0f);

                    toFlushImages.add(((Texture)text.getTexture()).getSourceImage());

                    SpriteData textSpriteData = view.getPalette().addSprite(text.getSprite());
                    textSpriteData.setEnabled(false);
                    textSpriteData.setOpacity(0);
                    wordSprite.add(text);
                    wordSpriteData.add(textSpriteData);

                    wordPos += plma;
                }
            }

            sprites.add(imageTexture);
            spriteData.add(data);
            this.wordSprites.add(wordSprite);
            this.wordSpriteData.add(wordSpriteData);
        }

        float tapPos = centerPos - plma * (maxLen - 1) / 2 + plma / 4 + 0.02f;

        for (int i = 0; i < maxLen; i++) {
            TextureSprite tap = SpriteUtility.getTextSprite(magTap, new Text(font, "_", Color.BLACK), axis);
            SpriteData tapDataElement = view.getPalette().addSprite(tap.getSprite());

            tapDataElement.setEnabled(false);
            tapDataElement.setOpacity(0);
            tap.getSprite().setPosition(tapPos, -0.15f, 0);

            taps.add(tap);
            tapData.add(tapDataElement);

            tapPos += plma;
        }

        if (vocaTestThread == null) {
            vocaTestThread = new Thread() {
                public void run() {
                    view.startBackToSinglePlay();

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        inTesting = true;

                        view.keyboardGroupStart();

                        //String currentText;
                        view.startTest(60);

                        lastLen = 0;

                        for (int index = 0; index < sprites.size(); index++) {
                            if (vocaTestThread == null) {
                                break;
                            }
                            //TextureSprite sprite = sprites.get(wordTestIndex);

                            SpriteData data = spriteData.get(index);
                            List<SpriteData> segments = wordSpriteData.get(index);
                            Word word = shuffleList.get(index);

                            queued.clear();
                            wordContains = 0;
                            hang = 0;
                            view.keyboardGroupStart();
                            wordTestIndex = index;

                            currentWord = word.getWord().replace(" ", "");
                            //String text = ((TextTexture) sprite.getTexture()).getText();


                            if(currentWord.length() < lastLen) {
                                for(int i = currentWord.length(); i < lastLen; i++) {
                                    SpriteData tapDataElement = tapData.get(i);

                                    System.out.println(i + "on1");
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 0f);
                                }
                            } else if(currentWord.length() > lastLen) {

                                float tapPos = centerPos - plma * (currentWord.length() - 1) / 2 + plma / 4 + 0.02f;
                                tapPos+=lastLen*plma;

                                for(int i = lastLen; i < currentWord.length(); i++) {
                                    Sprite tap = taps.get(i).getSprite();
                                    SpriteData tapDataElement = tapData.get(i);

                                    System.out.println(i + "on2");
                                    tap.setPosition(tapPos, tap.getPosition().y, 0f);
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 1f);
                                    tapPos+=plma;
                                }
                            }

                            float tapPos = centerPos - plma * (currentWord.length() - 1) / 2 + plma / 4 + 0.02f;

                            for(int tapIndex = 0; tapIndex < currentWord.length(); tapIndex++) {
                                Sprite sprite = taps.get(tapIndex).getSprite();
                                SpriteData tapDataElement = tapData.get(tapIndex);

                                System.out.println(tapIndex + "move or fade");
                                if(index == 0) {
                                    sprite.setPosition(tapPos, sprite.getPosition().y, 0f);
                                    tapDataElement.setEnabled(true);
                                    tapDataElement.addEffect(new EffectFader(), new Interval(0, 45, new CalculateModelNaturalSin()), tapDataElement.getOpacity(), 1f);
                                } else {
                                    tapDataElement.addEffect(new EffectMover(), new Interval(0, 45, new CalculateModelNaturalSin()), "x", sprite.getPosition().x, tapPos);
                                }
                                tapPos += plma;
                            }

                            if(data!=null) {
                                data.setEnabled(true);

                                data.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), 0f, 1f);
                            }

                            try {
                                while (true) {
                                    Thread.sleep(Integer.MAX_VALUE);
                                }
                            } catch (InterruptedException ex) {
                                //ignore
                            }
                            if (vocaTestThread != null) {
                                for (int i = 0; i < segments.size(); i++) {
                                    SpriteData segment = segments.get(i);

                                    segment.setEnabled(true);
                                    segment.removeAllEffects();
                                    segment.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), segment.getOpacity(), 1f);
                                }

                                Thread.sleep(2000L);
                            }
                            synchronized (currentWord) {
                                lastLen = currentWord.length();
                                currentWord = null;
                            }

                            if(data!=null) {
                                data.setEnabled(false);
                                data.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), 1f, 0f);
                            }
                            view.startTest(30);
                            for (int segmentIndex = 0; segmentIndex < segments.size(); segmentIndex++) {
                                SpriteData segment = segments.get(segmentIndex);
                                segment.removeAllEffects();
                                segment.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), segment.getOpacity(), 0f);
                            }

                            Thread.sleep(500L);
                        }

                        for(int tapIndex = 0; tapIndex < taps.size(); tapIndex++) {
                            SpriteData tap = tapData.get(tapIndex);
                            tap.addEffect(new EffectFader(), new Interval(0, 30, new CalculateModelNaturalSin()), tap.getOpacity(), 0f);
                        }

                        view.keyboardGroupFinish();
                        view.finishTest(60);
                        view.finishBackToSinglePlay();

                        vocaTestThread = null;
                        lastLen = 0;

                        Thread.sleep(1000L);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    view.singlePlayGroupStart(lastSprites);

                    for (int i = 0; i < sprites.size(); i++) {
                        TextureSprite sprite = sprites.get(i);

                        if (sprite != null) {
                            view.getPalette().deleteSpriteOnNextFrame(sprite.getSprite());
                            //sprite.getTexture().cleanUp();
                            //sprite.getSprite().getObjectElement().cleanUp();
                        }
                    }

                    for(int i = 0; i < taps.size(); i++) {
                        view.getPalette().deleteSpriteOnNextFrame(taps.get(i).getSprite());
                    }

                    for(int i = 0; i < toFlushImages.size(); i++) {
                        toFlushImages.get(i).flush();
                    }

                    for(int i = 0; i < shuffleList.size(); i++) {
                        shuffleList.get(i).resetImage();
                    }

                    inTesting = false;

                    System.gc();
                }
            };
            try {
                if (vocaTestThread != null) {
                    synchronized (vocaTestThread) {
                        vocaTestThread.start();
                    }
                }
            } catch (Exception ex) {
                System.out.println("동시실행");
            }
        }
    }

    public void startVocaTest3(String type, String wordType) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(4000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                view.singlePlayGroupStart(lastSprites);
            }
        }.start();
    }


    public List<TextureButtonSprite> getLastSprites() {
        return lastSprites;
    }
}
