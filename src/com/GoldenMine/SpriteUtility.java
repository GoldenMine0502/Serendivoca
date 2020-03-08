package com.GoldenMine;

import com.GoldenMine.Utility.Point;
import com.GoldenMine.application.TextureSprite;
import com.GoldenMine.graphic.elements.Text;
import com.GoldenMine.graphic.elements.textures.TextTexture;
import java.awt.image.BufferedImage;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class SpriteUtility {
    public static float[] getDefaultTex() {
        return new float[] {
                0.0f, 0.0f,
                0.0f, 1f,
                1f, 1f,
                1f, 0.0f
        };
    }

    public static int[] getDefaultIndices() {
        return new int[] {
                0, 1, 3, 3, 1, 2
        };
    }

    public static float[] getDefaultPosition(float d1, float d2) {
        return new float[]{
                -d1,  d2,  0f, //
                -d1, -d2,  0f,
                d1, -d2,  0f,
                d1,  d2,  0f,
        };
    }

    public static TextTexture getWordText(float[] position, BufferedImage src, Text text, Point plus) {
        Point delta = ImageUtility.getTextSize(text.getFont(), text.getText());

        //float calDelta = (float) (delta.getY()/delta.getX());

        //float sizeX = 0.5f;

        /*
        float posX = 0; // 0.8
        float posY = 0;

        float[] position = {
                -sizeX+posX,  sizeX*calDelta+posY, 0,
                -sizeX+posX, -sizeX*calDelta+posY, 0,
                 sizeX+posX, -sizeX*calDelta+posY, 0,
                 sizeX+posX,  sizeX*calDelta+posY, 0,
        };*/

        //BufferedImage src = new BufferedImage(delta.getXInt(), delta.getYInt(), BufferedImage.TYPE_INT_ARGB);

        int x = (src.getWidth()-delta.getXInt())/2;
        int y = (src.getHeight()-delta.getYInt())/2;

        TextTexture texture = new TextTexture(position, src, text, new Point(x+plus.getX(), y+plus.getY()));
        //texture.update();
        return texture;
    }

    public static TextureSprite getTextSprite(float mag, Text text, Point axis) {
        TextureSprite sprite = new TextureSprite();

        Point size = ImageUtility.getTextSize(text.getFont(), text.getText());

        size.setY(size.getY()*1.25);
        if(text.getFont().isItalic()) {
            size.setX(size.getX() + text.getFont().getSize()/1.5);
        }

        float[] pos = getDefaultPosition(size.getXInt()*mag, size.getYInt()*mag);

        sprite.setTexture(new TextTexture(pos, new BufferedImage(size.getXInt(), size.getYInt(), BufferedImage.TYPE_INT_ARGB), text, axis));

        return sprite;
    }
}
