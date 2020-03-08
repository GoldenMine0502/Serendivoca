package com.GoldenMine.application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created by ehe12 on 2018-08-24.
 */
public class Word {
    private String word;
    private String mean;
    private BufferedImage source;

    private File file;

    public Word(String word, String mean) {
        this.word = word;
        this.mean = mean;
    }

    public void setImage(File file) {
        this.file = file;

        if(source!=null) {
            source.flush();
            source = null;
        }
    }

    public void resetImage() {
        if(source!=null) {
            source.flush();
            source = null;
        }
    }

    public BufferedImage getImage() throws IOException {
        try {
            if (source == null) {
                source = ImageIO.read(file);
            }
        } catch(Exception ex) {
            System.out.println(file.getPath() + "읽기 실패");
            //ex.printStackTrace();
        }
        return source;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }
}
