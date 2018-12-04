package circuitSolver;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChangeColor {
    public static void setAlpha(String os, int color) {
        try {
            ImageIcon imageIcon = new ImageIcon(os);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int pixel = bufferedImage.getRGB(j2, j1); // j2������,j1������

                    int[] rgb = new int[3];
                    rgb[0] = (pixel & 0x00ff0000) >> 16; // ��λ���ȡredȻ������
                    rgb[1] = (pixel & 0x0000ff00) >> 8; // ��λ���ȡgreenȻ������
                    rgb[2] = (pixel & 0x000000ff);
                    int a = (pixel & 0xff000000) >>> 24; // �޷������ƻ�ȡalphaֵ

                    if (comp(rgb[0], rgb[1], rgb[2]) || a == 0) {
                        pixel = pixel | 0xffffffff; // ͸����ƫ���ɫ��Ϊ��ɫ
                    } else {
                        pixel = (pixel & 0xff000000) | color; // ����Ϊ�趨��ɫ
                    }
                    bufferedImage.setRGB(j2, j1, pixel);
                }
                System.out.println();
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", new File("C:\\Users\\DELL\\Desktop\\new.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean comp(int r, int g, int b) {
        int i = 0;
        if (r > 200) {
            i++;
        }
        if (g > 200) {
            i++;
        }
        if (b > 200) {
            i++;
        }
        if (i >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        setAlpha("C:\\Users\\DELL\\Desktop\\capacitor.png", 0xff00ffff);
    }
}