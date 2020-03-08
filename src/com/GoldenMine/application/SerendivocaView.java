package com.GoldenMine.application;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.application.models.SerendivocaModel;
import com.GoldenMine.event.EffectFader;
import com.GoldenMine.event.EffectMover;
import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.Sprite;
import com.GoldenMine.graphic.elements.SpriteData;
import com.GoldenMine.graphic.event.PaletteHandler;
import com.GoldenMine.graphic.util.SpriteGroup;
import com.GoldenMine.util.interval.CalculateModelNaturalSin;
import com.GoldenMine.util.interval.Interval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class SerendivocaView {
    Palette palette;

    private SpriteData mainTitleData;
    private SpriteData singlePlayData;
    private SpriteData multiPlayData;
    private SpriteData tutorialData;
    private SpriteData singlePlayMiddleData;
    private SpriteData singlePlayHighData;
    private SpriteData singlePlayEverydayData;
    private SpriteData singlePlayBackgroundData;
    private SpriteData singlePlayModeToggleData;

    private SpriteData TestExecuteData;
    private SpriteData TestXData;
    private SpriteData TestOData;

    private SpriteData leftIndexData;
    private SpriteData rightIndexData;

    private SpriteData backToSinglePlayData;

    private List<SpriteData> testHangData;

    private List<SpriteData> keyboardData = new ArrayList<>();

    public SpriteData getTestExecuteData() {
        return TestExecuteData;
    }

    public SpriteData getTestXData() {
        return TestXData;
    }

    //private List<SpriteData> singlePlayMiddleFoldersData = new ArrayList<>();
    //private List<SpriteData> singlePlayHighFoldersData = new ArrayList<>();
    //private List<SpriteData> singlePlayEverydayFoldersData = new ArrayList<>();

    private HashMap<String, List<SpriteData>> singlePlayFoldersData = new HashMap<>();
    private HashMap<String, SpriteGroup> singlePlayFoldersGroup = new HashMap<>();

    private SpriteGroup titleGroup;
    private SpriteGroup singlePlayTapGroup;
    private SpriteGroup testGroup;
    private SpriteGroup keyboardGroup;
    //SpriteGroup singlePlayMiddleFolderGroup;
    //SpriteGroup singlePlayHighFolderGroup;
    //SpriteGroup singlePlayEverydayFolderGroup;
    private SpriteGroup multiPlaySelectGroup;
    private SpriteGroup multiPlayRoomGroup;
    private SpriteGroup singlePlayGroup;
    private SpriteGroup multiPlayPlayGroup;
    private SpriteGroup tutorialGroup;

    private SerendivocaModel model;
    
    public SerendivocaView(SerendivocaModel model) {
        this.model = model;



        palette = new Palette("Serendivoca Application", 60, new Point(1400, 900), new PaletteHandler() {
            //int fps = 0;

            @Override
            public void onRenderStart() {
                
            }

            @Override
            public void onFrameStart() {
                //fps++;
            }
        }, true, GL_ALWAYS);

        //palette.getCamera().setPosition(-0.5f, 0.5f, 0);
        //palette.getCamera().setRotation(20, 20, 20);
        palette.setVSync(true);

    }

    public void initialize() {
        palette.getWindow().setClearColor(0.49f, 0.70f, 0.86f, 1f); // bright blue

        mainTitleData = palette.addSprite(model.getTitleModel().getMainTitleSprite().getSprite());
        mainTitleData.setOpacity(0);

        singlePlayData = palette.addSprite(model.getTitleModel().getSinglePlaySprite().getSprite());
        singlePlayData.setOpacity(0);

        multiPlayData = palette.addSprite(model.getTitleModel().getMultiPlaySprite().getSprite());
        multiPlayData.setOpacity(0);

        tutorialData = palette.addSprite(model.getTitleModel().getTutorialSprite().getSprite());
        tutorialData.setOpacity(0);


        singlePlayMiddleData = palette.addSprite(model.getSinglePlayModel().getSinglePlayMiddleSprite().getSprite());

        singlePlayHighData = palette.addSprite(model.getSinglePlayModel().getSinglePlayHighSprite().getSprite());

        singlePlayEverydayData = palette.addSprite(model.getSinglePlayModel().getSinglePlayEverydaySprite().getSprite());

        singlePlayModeToggleData = palette.addSprite(model.getSinglePlayModel().getModeToggleTexture().getSprite());

        singlePlayBackgroundData = palette.addSprite(model.getSinglePlayModel().getSinglePlayBackgroundSprite().getSprite());

        leftIndexData = palette.addSprite(model.getSinglePlayModel().getLeftIndex().getSprite());

        rightIndexData = palette.addSprite(model.getSinglePlayModel().getRightIndex().getSprite());

        initalizeFolders();
        initalizeTitleGroup();
        initalizeSinglePlayGroup();

        initializeKeyboard();

        initializeTest();
        initializeBack();
    }

    public void initializeBack() {
        backToSinglePlayData = palette.addSprite(model.getSinglePlayModel().getBackToSinglePlay().getSprite());
        backToSinglePlayData.setEnabled(false);
        backToSinglePlayData.setOpacity(0);
    }

    public void startBackToSinglePlay() {
        backToSinglePlayData.setEnabled(true);
        backToSinglePlayData.addEffect(new EffectFader(), new Interval(0,  15, new CalculateModelNaturalSin()), 0f, 1f);
    }

    public void finishBackToSinglePlay() {
        backToSinglePlayData.setEnabled(false);
        backToSinglePlayData.addEffect(new EffectFader(), new Interval(0,  15, new CalculateModelNaturalSin()), 1f, 0f);
    }



    public void finishTest(int interval) {
        if(TestExecuteData.isEnabled()) {
            TestExecuteData.addEffect(new EffectFader(), new Interval(0, interval, new CalculateModelNaturalSin()), 1f, 0f);
            TestExecuteData.setEnabled(false);
        }

        for(int i = 0; i < testHangData.size(); i++) {
            //if(testHangData.get(i).getOpacity() > 0.99f)
                testHangData.get(i).addEffect(new EffectFader(), new Interval(0, interval, new CalculateModelNaturalSin()), testHangData.get(i).getOpacity(), 0f);
        }
    }

    public void startTest(int interval) {
        if(!TestExecuteData.isEnabled()) {
            TestExecuteData.addEffect(new EffectFader(), new Interval(0, interval, new CalculateModelNaturalSin()), 0f, 1f);
            TestExecuteData.setEnabled(true);
        }


        for(int i = 0; i < testHangData.size(); i++) {
                testHangData.get(i).addEffect(new EffectFader(), new Interval(0, interval, new CalculateModelNaturalSin()), testHangData.get(i).getOpacity(), 0f);
        }
    }

    public void initializeTest() {
        testGroup = new SpriteGroup();
        testHangData = new ArrayList<>();

        TestExecuteData = palette.addSprite(model.getSinglePlayModel().getTestExecute().getSprite());
        for(int i = 0; i < model.getSinglePlayModel().getTestHang().size(); i++) {
            testHangData.add(palette.addSprite(model.getSinglePlayModel().getTestHang().get(i).getSprite()));
        }
        TestOData = palette.addSprite(model.getSinglePlayModel().getTestO().getSprite());
        TestXData = palette.addSprite(model.getSinglePlayModel().getTestX().getSprite());

        testGroup.addSprite(model.getSinglePlayModel().getTestExecute().getSprite(), TestExecuteData);
        for(int i = 0; i < testHangData.size(); i++) {
            testGroup.addSprite(model.getSinglePlayModel().getTestHang().get(i).getSprite(), testHangData.get(i));
        }
        testGroup.addSprite(model.getSinglePlayModel().getTestO().getSprite(), TestOData);
        testGroup.addSprite(model.getSinglePlayModel().getTestX().getSprite(), TestXData);

        testGroup.setOpacity(0);
        testGroup.setEnabled(false);
    }

    public List<SpriteData> getTestHangData() {
        return testHangData;
    }

    public void initializeKeyboard() {
        keyboardGroup = new SpriteGroup();

        List<TextureButtonSprite> sprites = model.getSinglePlayModel().getKeyboard();

        //System.out.println(sprites.size());

        for(int i = 0; i < sprites.size(); i++) {
            TextureButtonSprite sprite = sprites.get(i);
            //System.out.println(sprite);
            SpriteData d = palette.addSprite(sprite.getSprite());

            keyboardGroup.addSprite(sprite.getSprite(), d);

            keyboardData.add(d);
        }

        keyboardGroup.setEnabled(false);
        keyboardGroup.setOpacity(0);
    }

    public void initalizeFolders() {
        //List<TextureButtonSprite> middle = model.getSinglePlayModel().getMiddleSections().getList();
        //List<TextureButtonSprite> high = model.getSinglePlayModel().getHighSections().getList();
        //List<TextureButtonSprite> everyday = model.getSinglePlayModel().getEverydaySections().getList();

        for(String str : model.getSinglePlayModel().getStrLists()) {
            List<TextureButtonSprite> list = model.getSinglePlayModel().getSections(str).getList();

            SpriteGroup group = new SpriteGroup();
            List<SpriteData> spriteData = new ArrayList<>();

            for(int i = 0; i < list.size(); i++) {
                Sprite sprite = list.get(i).getSprite();

                SpriteData data = palette.addSprite(sprite);
                spriteData.add(data);
                group.addSprite(sprite, data);
            }

            singlePlayFoldersData.put(str, spriteData);
            singlePlayFoldersGroup.put(str, group);

            group.setEnabled(false);
            group.setOpacity(0);
        }
    }

    public void titleGroupStart() {
        new Thread() {
            public void run() {
                mainTitleData.addEffect(new EffectFader(), new Interval(0, 90, new CalculateModelNaturalSin()), 0f, 1f);
                mainTitleData.addEffect(new EffectMover(), new Interval(150, 120, new CalculateModelNaturalSin()), "y", 0f, 0.50f);

                try {
                    Thread.sleep(3500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                singlePlayData.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 0f, 1f);
                multiPlayData.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 0f, 1f);
                tutorialData.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 0f, 1f);
            }
        }.start();
    }

    public void disableSprites(List<TextureButtonSprite> sprites, boolean smooth) {
        if(sprites!=null) {
            //System.out.println("executed");
            for (int i = 0; i < sprites.size(); i++) {
                SpriteData d = palette.getSpriteData(sprites.get(i).getSprite());
                d.setEnabled(false);
                if (smooth) {
                    d.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), d.getOpacity(), 0f);
                } else {
                    d.setOpacity(0);
                }
            }
        }
    }

    public void enableSprites(List<TextureButtonSprite> sprites, boolean smooth) {
        if(sprites!=null) {
            for (int i = 0; i < sprites.size(); i++) {
                TextureButtonSprite sprite = sprites.get(i);
                SpriteData data = palette.getSpriteData(sprite.getSprite());
                data.setEnabled(true);
                if (smooth) {
                    data.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), data.getOpacity(), 1f);
                } else {
                    data.setEnabled(true);
                    data.setOpacity(1);
                }
            }
        }
    }

    public void singlePlayGroupStart(List<TextureButtonSprite> sprites) {
        System.out.println("singlestart");
        singlePlayGroup.addEffects(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 0f, 1f);
        singlePlayGroup.setEnabled(true);
        enableSprites(sprites, true);
    }

    public void singlePlayGroupFinish(List<TextureButtonSprite> sprites) {

        //System.out.println("singlefinish" + (lastSprites!=null));

        //for(String s : singlePlayFoldersGroup.keySet()) {
        //    singlePlayFoldersGroup.get(s).setEnabled(false);
        //}

        singlePlayGroup.setEnabled(false);
        singlePlayGroup.addEffects(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 1f, 0f);

        disableSprites(sprites, true);
        //singlePlayMiddleFolderGroup.addEffects(new EffectFader(), new Interval(0, 120, new CalculateModelNaturalSin()), 1f, 0f);
        //singlePlayHighFolderGroup.addEffects(new EffectFader(), new Interval(0, 120, new CalculateModelNaturalSin()), 1f, 0f);
        //singlePlayEverydayFolderGroup.addEffects(new EffectFader(), new Interval(0, 120, new CalculateModelNaturalSin()), 1f, 0f);
    }

    public void multiPlayGroupStart() {

    }

    public void tutorialGroupStart() {

    }

    public void titleGroupFinish() {
        titleGroup.addEffects(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), 1f, 0f);
        titleGroup.setEnabled(false);
    }

    public void initalizeTitleGroup() {
        titleGroup = new SpriteGroup();
        titleGroup.addSprite(model.getTitleModel().getMainTitleSprite().getSprite(), mainTitleData);
        titleGroup.addSprite(model.getTitleModel().getSinglePlaySprite().getSprite(), singlePlayData);
        titleGroup.addSprite(model.getTitleModel().getMultiPlaySprite().getSprite(), multiPlayData);
        titleGroup.addSprite(model.getTitleModel().getTutorialSprite().getSprite(), tutorialData);
    }

    /*
    public List<SpriteData> getSpriteList(String type) {
        return singlePlayFoldersData.get(type);
    }*/

    public void initalizeSinglePlayGroup() {
        singlePlayGroup = new SpriteGroup();

        singlePlayGroup.addSprite(model.getSinglePlayModel().getSinglePlayBackgroundSprite().getSprite(), singlePlayBackgroundData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getSinglePlayMiddleSprite().getSprite(), singlePlayMiddleData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getSinglePlayHighSprite().getSprite(), singlePlayHighData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getSinglePlayEverydaySprite().getSprite(), singlePlayEverydayData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getModeToggleTexture().getSprite(), singlePlayModeToggleData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getLeftIndex().getSprite(), leftIndexData);
        singlePlayGroup.addSprite(model.getSinglePlayModel().getRightIndex().getSprite(), rightIndexData);

        for(List<SpriteData> s : singlePlayFoldersData.values()) {
            for(SpriteData s2 : s) {
                s2.setOpacity(0);
                s2.setEnabled(false);
            }
        }

        singlePlayGroup.setEnabled(false);
        singlePlayGroup.setOpacity(0);
    }

    public void start() {
        titleGroupStart();

        palette.start();
    }

    public Palette getPalette() {
        return palette;
    }

    public SpriteGroup getTestGroup() {
        return testGroup;
    }

    public void keyboardGroupStart() {
        keyboardGroup.setEnabled(true);
        for(int i = 0; i < keyboardGroup.getSprites().size(); i++) {
            SpriteData data = keyboardGroup.getSpriteData(keyboardGroup.getSprites().get(i));

            //if(data.getOpacity() < 0.51f)
            //{
                data.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), data.getOpacity(), 1f);
            //}
        }
        //keyboardGroup.addEffects(new EffectFader(), new Interval(0, 120, new CalculateModelNaturalSin()), 0f ,1f);
    }

    public void keyboardGroupFinish() {
        for(int i = 0; i < keyboardGroup.getSprites().size(); i++) {
            SpriteData data = keyboardGroup.getSpriteData(keyboardGroup.getSprites().get(i));
            data.addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), data.getOpacity(), 0f);
        }
        keyboardGroup.setEnabled(false);
    }

    public void enableFolder(String wordType, int index) {
        SpriteData spriteData = singlePlayFoldersData.get(wordType).get(index);

        spriteData.setEnabled(true);
        spriteData.setOpacity(1);
    }
    /*
    public List<SpriteData> getKeyboardData() {
        return keyboardData;
    }*/

    public void disableKeyboard(char ch) {
        keyboardData.get(ch-65).addEffect(new EffectFader(), new Interval(0, 60, new CalculateModelNaturalSin()), keyboardData.get(ch-65).getOpacity(), 0.5f);
    }
}
