package com.GoldenMine;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.event.EffectFader;
import com.GoldenMine.event.EffectMover;
import com.GoldenMine.graphic.Palette;
import com.GoldenMine.graphic.elements.SpriteData;
import com.GoldenMine.graphic.event.PaletteHandler;
import com.GoldenMine.graphic.util.SpriteGroup;
import com.GoldenMine.util.interval.CalculateModelNaturalSin;
import com.GoldenMine.util.interval.Interval;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ehe12 on 2018-08-07.
 */
public class MainGUI {
    /*
    Palette palette;

    Texture mainTitleTexture;
    ObjectSprite mainTitleObject;
    SpriteData mainTitleData;

    Texture singlePlayTexture;
    Texture singlePlayTextureClicked;
    ButtonSprite singlePlayObject;
    SpriteData singlePlayData;

    Texture multiPlayTexture;
    Texture multiPlayTextureClicked;
    ButtonSprite multiPlayObject;
    SpriteData multiPlayData;

    Texture tutorialTexture;
    Texture tutorialTextureClicked;
    ButtonSprite tutorialObject;
    SpriteData tutorialData;

    Texture singlePlayMiddleTexture;
    ButtonSprite singlePlayMiddleObject;
    SpriteData singlePlayMiddleData;

    Texture singlePlayHighTexture;
    ButtonSprite singlePlayHighObject;
    SpriteData singlePlayHighData;

    Texture singlePlayEverydayTexture;
    ButtonSprite singlePlayEverydayObject;
    SpriteData singlePlayEverydayData;







    SpriteGroup titleGroup;
    SpriteGroup singlePlayTapGroup;
    SpriteGroup singlePlaySelectMiddleGroup;
    SpriteGroup singlePlaySelectHighGroup;
    SpriteGroup singlePlaySelectEverydayGroup;
    SpriteGroup multiPlaySelectGroup;
    SpriteGroup multiPlayRoomGroup;
    SpriteGroup singlePlayGroup;
    SpriteGroup multiPlayPlayGroup;
    SpriteGroup tutorialGroup;

    public MainGUI() {
        palette = new Palette("Serendivoca", 60, new Point(1400, 900), new PaletteHandler() {
            @Override
            public void onRenderStart() {

            }

            @Override
            public void onRenderFinish() {

            }

            @Override
            public void onFrameStart() {

            }

            @Override
            public void onFrameFinish() {

            }
        });
        palette.setVSync(true);

        palette.getWindow().setClearColor(0.49f, 0.70f, 0.86f, 1f); // bright blue




        glfwSetKeyCallback(palette.getWindow().getWindowId(), new GLFWKeyCallbackI() {
            Vector3f move = new Vector3f(0,0,0);

            boolean first = false;

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

                if(key == GLFW_KEY_W) {
                    if(action == GLFW_PRESS) {
                        move.z = -0.02f;
                    } else {
                        move.z = 0;
                    }
                }

                if(key == GLFW_KEY_S) {
                    if(action == GLFW_PRESS) {
                        move.z = 0.02f;
                    } else {
                        move.z = 0;
                    }
                }

                if(key == GLFW_KEY_A) {
                    if(action == GLFW_PRESS) {
                        move.x = -0.02f;
                    } else {
                        move.x = 0;
                    }
                }

                if(key == GLFW_KEY_D) {
                    if(action == GLFW_PRESS) {
                        move.x = 0.02f;
                    } else {
                        move.x = 0;
                    }
                }

                if(!first) {
                    first = true;
                    palette.getCamera().movePosition(0, 1.5f, 0);
                }
                palette.getCamera().movePosition(move.x, move.y, move.z);
                palette.getCamera().setRotation(50, 30, 0);
            }
        });
        initalizeMainTitle();
        initalizeSinglePlay();
        initalizeMultiPlay();
        initalizeTutorial();
        initalizeTitleGroup();



        titleGroupStart();
    }

    public void start() {
        palette.start();
    }










    public void singlePlayGroupStart() {
        singlePlayGroup.addEffects(new EffectFader(), new Interval(30, 120, new CalculateModelNaturalSin()), 0f, 1f);
    }

    public void multiPlayGroupStart() {

    }

    public void tutorialGroupStart() {
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

    public void titleGroupFinish() {
        titleGroup.addEffects(new EffectFader(), new Interval(0, 120, new CalculateModelNaturalSin()), 1f, 0f);
    }



    public void initalizeTitleGroup() {
        titleGroup = new SpriteGroup();
        titleGroup.addSprite(mainTitleObject, mainTitleData);
        titleGroup.addSprite(singlePlayObject, singlePlayData);
        titleGroup.addSprite(multiPlayObject, multiPlayData);
        titleGroup.addSprite(tutorialObject, tutorialData);
    }














    public void initalizeSinglePlay() {
        float d1 = 0.71f;
        float d2 = 0.0768f;

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            singlePlayTexture = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/singleplay.png")));
            singlePlayTextureClicked = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/singleplayclicked.png")));
            singlePlayObject = new ButtonSprite(singlePlayTexture, singlePlayTextureClicked);
            singlePlayObject.setPosition(0, -0.3f, 0);
            singlePlayData = palette.addSprite(singlePlayObject);
            singlePlayData.setOpacity(0);

            singlePlayObject.addButtonClickedEvent(clicked -> {
                if(clicked) {
                    titleGroupFinish();
                    singlePlayGroupStart();
                    //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                    //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initalizeMultiPlay() {
        float d1 = 0.71f;
        float d2 = 0.0768f;

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            multiPlayTexture = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/multiplay.png")));
            multiPlayTextureClicked = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/multiplayclicked.png")));
            multiPlayObject = new ButtonSprite(multiPlayTexture, multiPlayTextureClicked);
            multiPlayObject.setPosition(0, -0.6f, 0);
            multiPlayData = palette.addSprite(multiPlayObject);
            multiPlayData.setOpacity(0);

            multiPlayObject.addButtonClickedEvent(clicked -> {
                if(clicked) {
                    titleGroupFinish();
                    multiPlayGroupStart();
                    //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                    //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initalizeTutorial() {
        float d1 = 0.71f;
        float d2 = 0.0768f;

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            tutorialTexture = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/tutorial.png")));
            tutorialTextureClicked = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/tutorialclicked.png")));
            tutorialObject = new ButtonSprite(tutorialTexture, tutorialTextureClicked);
            tutorialObject.setPosition(0, -0.9f, 0);
            tutorialData = palette.addSprite(tutorialObject);
            tutorialData.setOpacity(0);

            tutorialObject.addButtonClickedEvent(clicked -> {
                if(clicked) {
                    titleGroupFinish();
                    tutorialGroupStart();
                    //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                    //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void initalizeMainTitle() {
        float[] positions = new float[]{
                -1.4f,  0.5f,  0f, //
                -1.4f, -0.5f,  0f,
                1.4f, -0.5f,  0f,
                1.4f,  0.5f,  0f,
        };

        try {
            mainTitleTexture = new Texture(positions, getDefaultTex(), getDefaultIndices(), ImageIO.read(new File("resources/title/title.png")));
            mainTitleObject = new ObjectSprite(mainTitleTexture);
            mainTitleData = palette.addSprite(mainTitleObject);
            mainTitleData.setOpacity(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }












    public float[] getDefaultTex() {
        return new float[] {
                0.0f, 0.0f,
                0.0f, 1f,
                1f, 1f,
                1f, 0.0f
        };
    }

    public int[] getDefaultIndices() {
        return new int[] {
                0, 1, 3, 3, 1, 2
        };
    }

    public static void main(String[] args) {
        new MainGUI().start();

    }*/
}
