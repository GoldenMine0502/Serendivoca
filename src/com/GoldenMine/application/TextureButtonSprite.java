package com.GoldenMine.application;

import com.GoldenMine.graphic.elements.sprite.ButtonSprite;
import com.GoldenMine.graphic.elements.textures.Texture;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class TextureButtonSprite {
    private Texture texture;
    private Texture texture2;
    private ButtonSprite sprite;

    //boolean created;

    public TextureButtonSprite() {

    }

    public void setTexture(Texture texture) {
        if(sprite == null)
            this.texture = texture;
        else
            throw new RuntimeException("cannot edit after getSprite() is called");
    }

    public void setTextureClicked(Texture texture) {
        if(sprite == null)
            this.texture2 = texture;
        else
            throw new RuntimeException("cannot edit after getSprite() is called");
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getTextureClicked() {
        return texture2;
    }

    public ButtonSprite getSprite() {
        if(sprite == null) {
            sprite = new ButtonSprite(texture, texture2);
        }
        return sprite;
    }
}
