package com.GoldenMine.application;

import com.GoldenMine.graphic.elements.sprite.ToggleSprite;
import com.GoldenMine.graphic.elements.textures.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class TextureToggleSprite {
    private List<Texture> textures = new ArrayList<>();
    private ToggleSprite sprite;

    //boolean created;

    public TextureToggleSprite() {

    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }

    public Texture getTexture(int index) {
        return textures.get(index);
    }

    public ToggleSprite getSprite() {
        if(sprite == null) {
            sprite = new ToggleSprite(textures.get(0));

            for(int i = 1; i < textures.size(); i++) {
                sprite.addTexture(textures.get(i));
            }
        }
        return sprite;
    }
}
