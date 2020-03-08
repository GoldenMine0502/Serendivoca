package com.GoldenMine.application.models;

import com.GoldenMine.application.WordStorage;
import java.util.HashMap;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class SerendivocaModel {
    SerendivocaTitleModel titleModel;
    SerendivocaSinglePlayModel singlePlayModel;

    HashMap<String, WordStorage> wordStorages = new HashMap<>();

    //public TextTexture test;
    //public ObjectSprite sprite;



    public SerendivocaModel() {
        
    }

    public void initialize() {
        titleModel = new SerendivocaTitleModel();
        titleModel.initalize();
        initalizeWordStorage();
        singlePlayModel = new SerendivocaSinglePlayModel(wordStorages);
        singlePlayModel.initialize();
    }

    public void initalizeWordStorage() {
        wordStorages.put("middle", new WordStorage("resources/words/middle"));
        wordStorages.put("high", new WordStorage("resources/words/high"));
        wordStorages.put("everyday", new WordStorage("resources/words/everyday"));
    }



    /*
    public Texture getMainTitleTexture() {
        return mainTitleTexture;
    }

    public Texture getSinglePlayTexture() {
        return singlePlayTexture;
    }

    public Texture getSinglePlayTextureClicked() {
        return singlePlayTextureClicked;
    }

    public Texture getMultiPlayTexture() {
        return multiPlayTexture;
    }

    public Texture getMultiPlayTextureClicked() {
        return multiPlayTextureClicked;
    }

    public Texture getTutorialTexture() {
        return tutorialTexture;
    }

    public Texture getTutorialTextureClicked() {
        return tutorialTextureClicked;
    }

    public Texture getSinglePlayMiddleSelectTexture() {
        return singlePlayMiddleSelectTexture;
    }

    public Texture getSinglePlayHighSelectTexture() {
        return singlePlayHighSelectTexture;
    }

    public Texture getSinglePlayEverydaySelectTexture() {
        return singlePlayEverydaySelectTexture;
    }*/




    public HashMap<String, WordStorage> getWordStorages() {
        return wordStorages;
    }

    public SerendivocaTitleModel getTitleModel() {
        return titleModel;
    }

    public SerendivocaSinglePlayModel getSinglePlayModel() {
        return singlePlayModel;
    }
}
