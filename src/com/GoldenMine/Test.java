package com.GoldenMine;

import com.sun.imageio.plugins.common.ImageUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        Graphics g = frame.getGraphics();

        int size = 300;

        BufferedImage img = ImageUtility.createImageFromText(new Font("나눔고딕", 0, size), "TEST", Color.BLACK);
        g.drawImage(img, 10, (int) (500-size*1.25), null);
    }
}
