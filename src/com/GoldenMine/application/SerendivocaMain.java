package com.GoldenMine.application;

import com.GoldenMine.application.controllers.SerendivocaController;
import com.GoldenMine.application.models.SerendivocaModel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import org.lwjgl.opengl.GL11;

/**
 * Created by ehe12 on 2018-08-22.
 */
public class SerendivocaMain {
    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/a뉴굴림1.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/a뉴굴림2.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/a뉴굴림3.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/a뉴굴림4.ttf")));

            for(Font font : ge.getAllFonts()) {
                System.out.println(font.getName());
            }
            //System.out.println(ge.getAllFonts());
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        opengl sorted transparency
         */



        String mincpu = "Pentium";
        String minram = "2G";
        String minvga = "Intel Graphics";

        String cpu = "i3";
        String ram = "3G";
        String vga = "Intel HD Graphics 4400";


        System.out.println("===최소사양===");
        System.out.println("CPU: " + mincpu + " 이상");
        System.out.println("RAM: " + minram + " 이상");
        System.out.println("VGA: " + minvga + " 이상");

        System.out.println("\n===권장사양===");
        System.out.println("CPU: " + cpu + " 이상");
        System.out.println("RAM: " + ram + " 이상");
        System.out.println("VGA: " + vga + " 이상");

        SerendivocaModel model = new SerendivocaModel();
        SerendivocaView view = new SerendivocaView(model);
        SerendivocaController controller = new SerendivocaController(model, view);
        model.initialize();
        view.initialize();
        controller.initialize();

        view.start();

    }
}
