package com.GoldenMine;

import com.GoldenMine.Utility.Point;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtility {
    private static BufferedImage defaultImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
    private static Graphics2D defaultGraphics2D = (Graphics2D) defaultImage.getGraphics();
    private static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static GraphicsDevice device = env.getDefaultScreenDevice();
    private static GraphicsConfiguration config = device.getDefaultConfiguration();

    public static BufferedImage getCopiedImage(BufferedImage image) {
        BufferedImage copied = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = copied.getGraphics();
        g.drawImage(image, 0, 0, null);

        return copied;
    }

    public static Point getTextSize(Font font, String text) {
        FontMetrics fm = defaultGraphics2D.getFontMetrics(font);

        int strWidth = fm.stringWidth(text);

        return new Point(strWidth, font.getSize());
    }

    public static BufferedImage createImageFromText(Font font, String text, Color color) {
        Point size = getTextSize(font, text);
        FontMetrics fm = defaultGraphics2D.getFontMetrics(font);

        int len = 0;
        if(font.isItalic() || font.isBold()) {
            len += Math.max(fm.stringWidth(text.substring(0, 1)), fm.stringWidth(text.substring(text.length()-1, text.length())))/2;
        }

        BufferedImage image = new BufferedImage(size.getXInt() + len, (int) (size.getYInt()*1.25), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        g.setColor(color);
        g.setFont(font);

        g.drawString(text, len/2, size.getYInt());

        //BufferedImage returnImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        //Graphics g2 = returnImage.getGraphics();
        //g2.drawImage(image, 0, 0, null);

        return image;
    }

    public static BufferedImage loadImage(File file) {
        try {
            BufferedImage img = ImageIO.read(file);

            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BufferedImage createTestImage(int xSize, int ySize, Color color) {
        BufferedImage buffy = config.createCompatibleImage(xSize, ySize, Transparency.TRANSLUCENT);
        Graphics g = buffy.getGraphics();
        //BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        //Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, xSize, ySize);

        return buffy;
    }

    public static void resizeBilinear(int[] pixels, int w, int h, int w2, int h2) {
        //int[] temp = new int[w2*h2] ; // +
        int a, b, c, d, x, y, index ;
        float x_ratio = ((float)(w-1))/w2 ;
        float y_ratio = ((float)(h-1))/h2 ;
        float x_diff, y_diff, blue, red, green, alpha ;
        //int aAlpha, bAlpha, cAlpha, dAlpha;
        int offset = 0 ;



        for (int yIndex=0;yIndex<h2;yIndex++) {
            for (int xIndex=0;xIndex<w2;xIndex++) {
                x = (int)(x_ratio * xIndex) ;
                y = (int)(y_ratio * yIndex) ;
                x_diff = (x_ratio * xIndex) - x ;
                y_diff = (y_ratio * yIndex) - y ;
                index = (y*w+x) ;
                a = pixels[index] ;
                b = pixels[index+1] ;
                c = pixels[index+w] ;
                d = pixels[index+w+1] ;

                // blue element
                // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
                blue = (a&0xff)*(1-x_diff)*(1-y_diff) + (b&0xff)*(x_diff)*(1-y_diff) +
                        (c&0xff)*(y_diff)*(1-x_diff)   + (d&0xff)*(x_diff*y_diff);

                // green element
                // Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
                green = ((a>>8)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>8)&0xff)*(x_diff)*(1-y_diff) +
                        ((c>>8)&0xff)*(y_diff)*(1-x_diff)   + ((d>>8)&0xff)*(x_diff*y_diff);

                // red element
                // Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
                red = ((a>>16)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>16)&0xff)*(x_diff)*(1-y_diff) +
                        ((c>>16)&0xff)*(y_diff)*(1-x_diff)   + ((d>>16)&0xff)*(x_diff*y_diff);

                alpha = ((a>>24)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>24)&0xff)*(x_diff)*(1-y_diff) +
                        ((c>>24)&0xff)*(y_diff)*(1-x_diff)   + ((d>>24)&0xff)*(x_diff*y_diff);
                //alpha = (Math.max((a>>24)&0xff, Math.max((b>>24)&0xff, Math.max((c>>24)&0xff, (d>>24)&0xff))));


                pixels[yIndex*w+xIndex] = ((((int)alpha)<<24)&0xff000000) |
                        //(a & 0xFF000000) |
                        ((((int)red)<<16)&0xff0000) |
                        ((((int)green)<<8)&0xff00) |
                        ((int)blue) ;

                /*temp[offset++] =  ((((int)alpha)<<24)&0xff000000) |
                        ((((int)red)<<16)&0xff0000) |
                        ((((int)green)<<8)&0xff00) |
                        ((int)blue) ;*/
            }
        }
        //return temp ;
    }

    public static void copyImage(BufferedImage image, BufferedImage image2) {
        copyImage(image, image2, 0, 0);
    }

    public static void copyImage(BufferedImage image, BufferedImage image2, int posX, int posY) {
        int[] raster = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        int[] raster2 = ((DataBufferInt)image2.getRaster().getDataBuffer()).getData();

        copyImage(raster, image.getWidth(), image.getHeight(), raster2, image2.getWidth(), image2.getHeight(), posX, posY);
    }

    public static void copyImage(int[] image, int imageWidth, int imageHeight, int[] image2, int imageWidth2, int imageHeight2, int posX, int posY) {
        for(int i = 0; i < imageWidth2; i++) {
            for(int j = 0; j < imageHeight2; j++) {
                int cPosX = i + posX;
                int cPosY = j + posY;

                if(cPosX >= 0 && cPosX < imageWidth) {
                    if(cPosY >= 0 && cPosY < imageHeight) {
                        image[cPosX + cPosY * imageWidth] = image2[i + j * imageWidth2];
                    }
                }
            }
        }
    }
    /*

    int[] antialiasedrotate(int[] src, int srcWidth, int srcHeight, double Rotation, aar_callback callbackfunc, int bgcolor, bool autoblend)
    {
        //Get Rotation between [0, 360)
        int mult = (int)Rotation / 360;
        if (Rotation >= 0)
            Rotation = Rotation - 360.0 * mult;
        else
            Rotation = Rotation - 360.0 * (mult - 1);

        //Calculate the cos and sin values that will be used throughout the program
        double coss = Math.cos(Math.toRadians(Rotation));
        double sins = Math.sin(Math.toRadians(Rotation));

        //Calculate some index values so that values can easily be looked up
        int indminx = ((int)Rotation / 90 + 0) % 4;
        int indminy = ((int)Rotation / 90 + 1) % 4;
        int indmaxx = ((int)Rotation / 90 + 2) % 4;
        int indmaxy = ((int)Rotation / 90 + 3) % 4;

        //Load the source bitmaps information
        //BITMAP srcbmp;
        //GetObject(src, sizeof(srcbmp), &srcbmp);

        //Calculate the sources x and y offset
        double srcxres = (double)srcWidth / 2.0;
        double srcyres = (double)srcHeight / 2.0;

        //Calculate the x and y offset of the rotated image (half the width and height of the rotated image)
        int mx[] = {-1, 1, 1, -1};
        int my[] = {-1, -1, 1, 1};
        double xres = mx[indmaxx] * srcxres * coss - my[indmaxx] * srcyres * sins;
        double yres = mx[indmaxy] * srcxres * sins + my[indmaxy] * srcyres * coss;

        //Get the width and height of the image
        int width = (int) Math.ceil(xres * 2);
        int height = (int) Math.ceil(yres * 2);

        //Create the source dib array and the destdib array
        //RGBQUAD * srcdib = new RGBQUAD[srcbmp.bmWidth * srcbmp.bmHeight];

        int[] srcdib = new int[srcWidth * srcHeight];
        int[] dbldstdib = new int[width * height];

        //Load source bits into srcdib
        //BITMAPINFO srcdibbmap;
        //srcdibbmap.bmiHeader.biSize = sizeof(srcdibbmap.bmiHeader);
        //srcdibbmap.bmiHeader.biWidth = srcbmp.bmWidth;
        //srcdibbmap.bmiHeader.biHeight = -srcbmp.bmHeight;
        //srcdibbmap.bmiHeader.biPlanes = 1;
        //srcdibbmap.bmiHeader.biBitCount = 32;
        //srcdibbmap.bmiHeader.biCompression = BI_RGB;

        //HDC ldc = CreateCompatibleDC(0);
        //GetDIBits(ldc, src, 0, srcbmp.bmHeight, srcdib, &srcdibbmap, DIB_RGB_COLORS);
        //DeleteDC(ldc);

        //Loop through the source's pixels
        double xtrans;
        double ytrans;
        for (int x = 0; x < srcWidth; x++)
        {
            for (int y = 0; y < srcHeight; y++)
            {
                //Construct the source pixel's rotated polygon
                std::vector<aar_pnt> p;
                xtrans = (double)x - srcxres;
                ytrans = (double)y - srcyres;

                p.push_back(aar_pnt( xtrans * coss - ytrans * sins + xres, xtrans * sins + ytrans * coss + yres));
                p.push_back(aar_pnt( (xtrans + 1) * coss - ytrans * sins + xres, (xtrans + 1) * sins + ytrans * coss + yres));
                p.push_back(aar_pnt( (xtrans + 1) * coss - (ytrans + 1) * sins + xres, (xtrans + 1) * sins + (ytrans + 1) * coss + yres));
                p.push_back(aar_pnt( xtrans * coss - (ytrans + 1) * sins + xres, xtrans * sins + (ytrans + 1) * coss + yres));

                //Find the scan area on the destination's pixels
                int mindx = (int)p[indminx].x;
                int mindy = (int)p[indminy].y;
                int maxdx = aar_roundup(p[indmaxx].x);
                int maxdy = aar_roundup(p[indmaxy].y);

                int SrcIndex = x + y * srcWidth;
                //loop through the scan area to find where source(x, y) overlaps with the destination pixels

                for (int xx = mindx; xx < maxdx; xx++)
                {
                    for (int yy = mindy; yy < maxdy; yy++)
                    {
                        //Calculate the area of the source's rotated pixel (polygon p) over the destinations pixel (xx, yy)
                        double dbloverlap = aar_pixoverlap(p, xx, yy, coss, -sins);
                        if (dbloverlap)
                        {
                            int DstIndex = xx + yy * width;

                            //Add the rgb and alpha values in proportion to the overlap area
                            dbldstdib[DstIndex].red += (double)(srcdib[SrcIndex].rgbRed) * dbloverlap;
                            dbldstdib[DstIndex].blue += (double)(srcdib[SrcIndex].rgbBlue) * dbloverlap;
                            dbldstdib[DstIndex].green += (double)(srcdib[SrcIndex].rgbGreen) * dbloverlap;
                            dbldstdib[DstIndex].alpha += dbloverlap;
                        }
                    }
                }

            }
            if (callbackfunc != NULL)
            {
                double percentdone = (double)(x + 1) / (double)(srcbmp.bmWidth);
                if (callbackfunc(percentdone))
                {
                    delete [] srcdib;
                    delete [] dbldstdib;
                    return NULL;
                }
            }
        }
        delete [] srcdib;
        srcdib = NULL;

        //Create final destination bits
        RGBQUAD * dstdib = new RGBQUAD[width * height];

        //load dstdib with information from dbldstdib
        RGBQUAD backcolor;
        backcolor.rgbRed = bgcolor & 0x000000FF;
        backcolor.rgbGreen = (bgcolor & 0x0000FF00) / 0x00000100;
        backcolor.rgbBlue = (bgcolor & 0x00FF0000) / 0x00010000;
        for (int i = 0; i < width * height; i++)
        {
            if (dbldstdib[i].alpha)
            {
                if (autoblend)
                {
                    dstdib[i].rgbReserved = 0;
                    dstdib[i].rgbRed = aar_byterange(dbldstdib[i].red + (1 - dbldstdib[i].alpha) * (double)backcolor.rgbRed);
                    dstdib[i].rgbGreen = aar_byterange(dbldstdib[i].green + (1 - dbldstdib[i].alpha) * (double)backcolor.rgbGreen);
                    dstdib[i].rgbBlue = aar_byterange(dbldstdib[i].blue + (1 - dbldstdib[i].alpha) * (double)backcolor.rgbBlue);
                }
                else
                {
                    dstdib[i].rgbRed = aar_byterange(dbldstdib[i].red / dbldstdib[i].alpha);
                    dstdib[i].rgbGreen = aar_byterange(dbldstdib[i].green / dbldstdib[i].alpha);
                    dstdib[i].rgbBlue = aar_byterange(dbldstdib[i].blue / dbldstdib[i].alpha);
                    dstdib[i].rgbReserved = aar_byterange(255.0 * dbldstdib[i].alpha);
                }
            }
            else
            {
                //No color information
                dstdib[i].rgbRed = backcolor.rgbRed;
                dstdib[i].rgbGreen = backcolor.rgbGreen;
                dstdib[i].rgbBlue = backcolor.rgbBlue;
                dstdib[i].rgbReserved = 0;
            }
        }

        delete [] dbldstdib;
        dbldstdib = NULL;

        //Get Current Display Settings
        DEVMODE screenmode;
        screenmode.dmSize = sizeof(DEVMODE);
        EnumDisplaySettings(NULL, ENUM_CURRENT_SETTINGS, &screenmode);

        //Create the final bitmap object
        HBITMAP dstbmp = CreateBitmap(width, height, 1, screenmode.dmBitsPerPel, NULL);

        //Write the bits into the bitmap and return it
        BITMAPINFO dstdibmap;
        dstdibmap.bmiHeader.biSize = sizeof(dstdibmap.bmiHeader);
        dstdibmap.bmiHeader.biWidth = width;
        dstdibmap.bmiHeader.biHeight = -height;
        dstdibmap.bmiHeader.biPlanes = 1;
        dstdibmap.bmiHeader.biBitCount = 32;
        dstdibmap.bmiHeader.biCompression = BI_RGB;
        SetDIBits(0, dstbmp, 0, height, dstdib, &dstdibmap, DIB_RGB_COLORS);

        delete [] dstdib;
        dstdib = NULL;

        return dstbmp;
    }

    HBITMAP antialiasedrotate(HBITMAP src, double Rotation, aar_callback callbackfunc)
    {
        return antialiasedrotate(src, Rotation, callbackfunc, 0x00FFFFFF, true);
    }

    HBITMAP antialiasedrotate(HBITMAP src, double Rotation, aar_callback callbackfunc, int bgcolor)
    {
        return antialiasedrotate(src, Rotation, callbackfunc, bgcolor, true);
    }

    HBITMAP antialiasedrotate(HBITMAP src, double Rotation, aar_callback callbackfunc, bool autoblend)
    {
        return antialiasedrotate(src, Rotation, callbackfunc, 0x00FFFFFF, autoblend);
    }*/
}
