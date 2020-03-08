package com.GoldenMine.application.controllers;

import com.GoldenMine.application.*;
import com.GoldenMine.application.models.SerendivocaModel;
import com.GoldenMine.graphic.elements.*;
import com.GoldenMine.graphic.elements.textures.TextTexture;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class SerendivocaController {

    SerendivocaModel model;
    SerendivocaView view;

    SerendivocaSinglePlayController vocaController;

    public SerendivocaController(SerendivocaModel model, SerendivocaView view) {
        this.model = model;
        this.view = view;

        vocaController = new SerendivocaSinglePlayController(model, view);
    }

    /*public void disableAllFolders() {
        view.singlePlayMiddleFolderGroup.setEnabled(false);
        view.singlePlayHighFolderGroup.setEnabled(false);
        view.singlePlayEverydayFolderGroup.setEnabled(false);
        view.singlePlayMiddleFolderGroup.setOpacity(0);
        view.singlePlayHighFolderGroup.setOpacity(0);
        view.singlePlayEverydayFolderGroup.setOpacity(0);
    }*/

    public void initialize() {
        initalizeMainTitle();

        vocaController.initialize();
    }


    public void initalizeMainTitle() {
        model.getTitleModel().getSinglePlaySprite().getSprite().addButtonClickedEvent(clicked -> {
            if (clicked) {
                view.titleGroupFinish();
                view.singlePlayGroupStart(null);

                //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
            }
        });

        model.getTitleModel().getMultiPlaySprite().getSprite().addButtonClickedEvent(clicked -> {
            if (clicked) {
                view.titleGroupFinish();
                view.multiPlayGroupStart();
                //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
            }
        });

        model.getTitleModel().getTutorialSprite().getSprite().addButtonClickedEvent(clicked -> {
            if (clicked) {
                view.titleGroupFinish();
                view.tutorialGroupStart();
                //singlePlayTapGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
                //singlePlaySelectMiddleGroup.addEffects(new EffectFader(), new Interval(60, 120, new CalculateModelNaturalSin()), 0f, 1f);
            }
        });
    }


}
