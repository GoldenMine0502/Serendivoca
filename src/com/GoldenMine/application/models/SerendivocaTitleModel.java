package com.GoldenMine.application.models;

import com.GoldenMine.SpriteUtility;
import com.GoldenMine.application.TextureButtonSprite;
import com.GoldenMine.application.TextureSprite;
import com.GoldenMine.graphic.elements.textures.Texture;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**v
 * Created by ehe12 on 2018-08-24.
 */
public class SerendivocaTitleModel {
    TextureSprite mainTitleSprite;
    TextureButtonSprite singlePlaySprite;
    TextureButtonSprite multiPlaySprite;
    TextureButtonSprite tutorialSprite;

    /*Texture mainTitleTexture;
    ObjectSprite mainTitleSprite;

    Texture singlePlayTexture;
    Texture singlePlayTextureClicked;
    ButtonSprite singlePlaySprite;

    Texture multiPlayTexture;
    Texture multiPlayTextureClicked;
    ButtonSprite multiPlaySprite;

    Texture tutorialTexture;
    Texture tutorialTextureClicked;
    ButtonSprite tutorialSprite;*/

    public SerendivocaTitleModel() {

    }

    public void initalize() {
        initalizeMainTitle();
        initalizeMultiPlay();
        initalizeSinglePlay();
        initalizeTutorial();
    }

    public TextureSprite getMainTitleSprite() {
        return mainTitleSprite;
    }

    public TextureButtonSprite getSinglePlaySprite() {
        return singlePlaySprite;
    }

    public TextureButtonSprite getMultiPlaySprite() {
        return multiPlaySprite;
    }

    public TextureButtonSprite getTutorialSprite() {
        return tutorialSprite;
    }

    private void initalizeSinglePlay() {
        float d1 = 0.71f; // 472
        float d2 = 0.0768f; //51

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            singlePlaySprite = new TextureButtonSprite();
            singlePlaySprite.setTexture(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/singleplay.png"))));
            singlePlaySprite.setTextureClicked(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/singleplayclicked.png"))));
            //singlePlaySprite = new ButtonSprite(singlePlayTexture, singlePlayTextureClicked);
            //singlePlaySprite.setPosition(0, -0.3f, 0);
            singlePlaySprite.getSprite().setPosition(0, -0.3f, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initalizeMultiPlay() {
        float d1 = 0.71f;
        float d2 = 0.0768f;

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            multiPlaySprite = new TextureButtonSprite();
            multiPlaySprite.setTexture(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/multiplay.png"))));
            multiPlaySprite.setTextureClicked(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/multiplayclicked.png"))));
            multiPlaySprite.getSprite().setPosition(0, -0.6f, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initalizeTutorial() {
        float d1 = 0.71f;
        float d2 = 0.0768f;

        float[] positions = new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };

        try {
            tutorialSprite = new TextureButtonSprite();
            tutorialSprite.setTexture(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/tutorial.png"))));
            tutorialSprite.setTextureClicked(new Texture(positions, SpriteUtility.getDefaultTex(), SpriteUtility.getDefaultIndices(), ImageIO.read(new File("resources/title/tutorialclicked.png"))));
            tutorialSprite.getSprite().setPosition(0, -0.9f, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void initalizeMainTitle() {
        float[] positions = new float[]{
                -1.4f,  0.5f,  0f, //
                -1.4f, -0.5f,  0f,
                1.4f, -0.5f,  0f,
                1.4f,  0.5f,  0f,
        };

        try {
            mainTitleSprite = new TextureSprite();
            mainTitleSprite.setTexture(new Texture(positions, ImageIO.read(new File("resources/title/title.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
