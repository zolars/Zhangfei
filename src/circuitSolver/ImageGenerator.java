package circuitSolver;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * ͼƬ��������
 */
public class ImageGenerator {

    private String path;
    private Result[][] table;
    private int[][] detected;

    /**
     * ��תͼƬΪָ���Ƕ�
     * 
     * @param bufferedImage Ŀ��ͼ��
     * @param degree        ��ת�Ƕ�
     * @return BufferedImage �޸ĺ�ͼƬ
     */
    private BufferedImage rotateImage(BufferedImage image, int degree) {
        try {
            int w = image.getWidth(); // �õ�ͼƬ��ȡ�
            int h = image.getHeight(); // �õ�ͼƬ�߶ȡ�
            int type = image.getColorModel().getTransparency(); // �õ�ͼƬ͸���ȡ�

            BufferedImage newImage; // �յ�ͼƬ��
            Graphics2D g2D; // �յĻ��ʡ�
            (g2D = (newImage = new BufferedImage(w, h, type)).createGraphics())
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2D.rotate(Math.toRadians(degree), w / 2, h / 2); // ��ת��degree�����ͣ����������紹ֱ90�ȡ�
            g2D.drawImage(image, 0, 0, null); // ��bufferedImagecopyͼƬ��img��0,0��img�����ꡣ
            g2D.dispose();

            return newImage; // ���ظ��ƺõ�ͼƬ��ԭͼƬ��Ȼû�б䣬û����ת���´λ�����ʹ�á�
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ͼƬƴ�� ��ע�⣺��������ͼƬ����һ��Ŷ��
     * 
     * @param files      Ҫƴ�ӵ��ļ��б�
     * @param type       1 ����ƴ��, 2 ����ƴ��
     * @param targetFile ����ļ�Ŀ��
     * @return BufferedImage �޸ĺ�ͼƬ
     */
    private BufferedImage mergeImage(BufferedImage[] images, int type) {
        int len = images.length;
        if (len < 1) {
            throw new RuntimeException("ͼƬ����С��1");
        }

        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            if (type == 1) { // ����
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) { // ����
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return null; // todo
        }
        if (type == 2 && newHeight < 1) {
            return null; // todo
        }

        // ������ͼƬ
        try {
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    newImage.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    newImage.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            return newImage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ͼƬ��ɫ (��ɫ -> ��ɫ)
     * 
     * @param file       Ҫ��ɫ���ļ�
     * @param color      ��ɫĿ�� 16����ɫֵ
     * @param targetFile ����ļ�Ŀ��
     * @return BufferedImage �޸ĺ�ͼƬ
     */
    private BufferedImage setAlpha(String file, int color) {
        try {
            ImageIcon imageIcon = new ImageIcon(file);
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
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            g2D.dispose();

            return bufferedImage;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    private void generateImage() {
        String image_w_l = path + "\\elements\\w_l.png";
        String image_w_t = path + "\\elements\\w_t.png";
        String image_w_p = path + "\\elements\\w_p.png";
        String image_w_s = path + "\\elements\\w_s.png";
        String image_c = path + "\\elements\\c.png";
        String image_i = path + "\\elements\\i.png";
        String image_r = path + "\\elements\\r.png";
        String image_v = path + "\\elements\\v.png";
        String image_l = path + "\\elements\\l.png";
        String image_b = path + "\\elements\\b.png";

        String targetFile = path + "\\result\\new.png";

        try {
            BufferedImage imageResult = ImageIO.read(new File(path + "\\error.png"));

            for (int i = 0; i < table.length; i++) {
                BufferedImage imageRow = ImageIO.read(new File(path + "\\error.png"));
                for (int j = 0; j < table[0].length; j++) {
                    // ͼƬȾɫ
                    int color;
                    switch (detected[i][j]) {
                    case 1:
                        color = 0xffff0000;
                        break;
                    case 2:
                        color = 0xff00ff00;
                        break;
                    case 3:
                        color = 0xff0000ff;
                        break;
                    case 4:
                        color = 0xffffff00;
                        break;
                    case 5:
                        color = 0xffff00ff;
                        break;
                    case 6:
                        color = 0xff0000ff;
                        break;
                    default:
                        color = 0;
                        break;
                    }

                    String name = table[i][j].getName();
                    BufferedImage imageToInsert;
                    if (name.charAt(0) == 'w') {
                        switch (name.charAt(1)) {
                        case '0':
                            imageToInsert = setAlpha(image_w_s, color);
                            break;
                        case '1':
                            imageToInsert = rotateImage(setAlpha(image_w_s, color), 90);
                            break;
                        case 'l':
                            imageToInsert = rotateImage(setAlpha(image_w_l, color), 90 * (name.charAt(2) - 48));
                            break;
                        case 't':
                            imageToInsert = rotateImage(setAlpha(image_w_t, color), 90 * (name.charAt(2) - 48));
                            break;
                        case 'p':
                            imageToInsert = setAlpha(image_w_p, color);
                            break;
                        default:
                            throw new IllegalArgumentException("�������, ������뻷��:");
                        }
                    } else if (name.charAt(0) == 'v') {
                        imageToInsert = rotateImage(setAlpha(image_v, color), 90 * (-detected[i][j] - 1));
                    } else if (name.charAt(0) == 'i') {
                        imageToInsert = rotateImage(setAlpha(image_i, color), 90 * (-detected[i][j] - 1));
                    } else if (name.charAt(0) == 'r') {
                        imageToInsert = rotateImage(setAlpha(image_r, color), 90 * (-detected[i][j] - 1));
                    } else if (name.charAt(0) == 'l') {
                        imageToInsert = rotateImage(setAlpha(image_l, color), 90 * (-detected[i][j] - 1));
                    } else if (name.charAt(0) == 'c') {
                        imageToInsert = rotateImage(setAlpha(image_c, color), 90 * (-detected[i][j] - 1));
                    } else if (name.charAt(0) == 'b') {
                        imageToInsert = setAlpha(image_b, color);
                    } else {
                        imageToInsert = ImageIO.read(new File(path + "\\error.png"));
                    }
                    if (j == 0) {
                        imageRow = imageToInsert;
                    } else {
                        BufferedImage[] images = { imageRow, imageToInsert };
                        imageRow = mergeImage(images, 1);
                    }
                }
                if (i == 0) {
                    imageResult = imageRow;
                } else {
                    BufferedImage[] images = { imageResult, imageRow };
                    imageResult = mergeImage(images, 2);
                }
            }
            // �����Ҫ��ͼƬ
            ImageIO.write(imageResult, targetFile.split("\\.")[1], new File(targetFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageGenerator(String path, Result[][] table, int[][] detected) {
        this.path = path;
        this.table = table;
        this.detected = detected;
        generateImage();
    }
}