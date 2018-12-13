package circuitSolver;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类
 */
public class ImageGenerator {
    private Result[][] table;
    private int[][] detected;
    private int[][] circuitTable;

    private BufferedImage image_w_l;
    private BufferedImage image_w_t;
    private BufferedImage image_w_p;
    private BufferedImage image_w_s;
    private BufferedImage image_c;
    private BufferedImage image_i;
    private BufferedImage image_r;
    private BufferedImage image_v;
    private BufferedImage image_l;
    private BufferedImage image_b;
    private BufferedImage image_error;

    /**
     * 旋转图片为指定角度
     * 
     * @param bufferedImage 目标图像
     * @param degree        旋转角度
     * @return BufferedImage 修改后图片
     */
    private BufferedImage rotateImage(BufferedImage image, int degree) {
        try {
            int w = image.getWidth(); // 得到图片宽度。
            int h = image.getHeight(); // 得到图片高度。
            int type = image.getColorModel().getTransparency(); // 得到图片透明度。

            BufferedImage newImage; // 空的图片。
            Graphics2D g2D; // 空的画笔。
            (g2D = (newImage = new BufferedImage(w, h, type)).createGraphics())
                    .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2D.rotate(Math.toRadians(degree), w / 2, h / 2); // 旋转，degree是整型，度数，比如垂直90度。
            g2D.drawImage(image, 0, 0, null); // 从bufferedImagecopy图片至img，0,0是img的坐标。
            g2D.dispose();

            return newImage; // 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片拼接 （注意：必须两张图片长宽一致哦）
     * 
     * @param files      要拼接的文件列表
     * @param type       1 横向拼接, 2 纵向拼接
     * @param targetFile 输出文件目标
     * @return BufferedImage 修改后图片
     */
    private BufferedImage mergeImage(BufferedImage[] images, int type) {
        int len = images.length;
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
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
            if (type == 1) { // 横向
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) { // 纵向
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

        // 生成新图片
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
     * 图片换色 (黑色 -> 彩色)
     * 
     * @param image      要换色的图片
     * @param color      换色目标 16进制色值
     * @param targetFile 输出文件目标
     * @return BufferedImage 修改后图片
     */
    private BufferedImage setAlpha(BufferedImage image, int color) {
        for (int y = image.getMinY(); y < image.getHeight(); y++) {
            for (int x = image.getMinX(); x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y); // j2横坐标,j1竖坐标

                int[] rgb = new int[3];
                rgb[0] = (pixel & 0x00ff0000) >> 16; // 按位与获取red然后右移
                rgb[1] = (pixel & 0x0000ff00) >> 8; // 按位与获取green然后右移
                rgb[2] = (pixel & 0x000000ff);
                int a = (pixel & 0xff000000) >>> 24; // 无符号右移获取alpha值

                if ((rgb[0] == 255 && rgb[1] == 255 && rgb[2] == 255) || a == 0) {
                    pixel = pixel | 0xffffffff; // 透明或偏向白色射为白色
                } else {
                    pixel = (pixel & 0xff000000) | color; // 否则为设定颜色
                }
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }

    /**
     * 图片换色 (彩色 -> 黑色)
     * 
     * @param image      要换色的图片
     * @param targetFile 输出文件目标
     * @return BufferedImage 修改后图片
     */
    private BufferedImage setNonAlpha(BufferedImage image) {
        for (int y = image.getMinY(); y < image.getHeight(); y++) {
            for (int x = image.getMinX(); x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y); // j2横坐标,j1竖坐标

                int[] rgb = new int[3];
                rgb[0] = (pixel & 0x00ff0000) >> 16; // 按位与获取red然后右移
                rgb[1] = (pixel & 0x0000ff00) >> 8; // 按位与获取green然后右移
                rgb[2] = (pixel & 0x000000ff);
                int a = (pixel & 0xff000000) >>> 24; // 无符号右移获取alpha值

                if ((rgb[0] == 255 && rgb[1] == 255 && rgb[2] == 255) || a == 0) {
                    pixel = pixel | 0xffffffff; // 透明或偏向白色射为白色
                } else {
                    pixel = (pixel & 0xff000000) | 0xff000000; // 否则为设定颜色
                }
                image.setRGB(x, y, pixel);
            }
        }
        return image;
    }

    /**
     * 图片添加文字
     * 
     * @param image  要处理的文件
     * @param text   添加的文字内容
     * @param direct 添加的位置 0-下 1-右
     * @return BufferedImage 修改后图片
     */
    private BufferedImage addText(BufferedImage image, String text) {
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        int fontNum = text.length() < 3 ? 60 : 120 / text.length();

        g.setFont(new Font("微软雅黑", Font.ITALIC, fontNum));

        if (text.charAt(0) == 'r')
            g.drawString(text, 170, 217);
        else
            g.drawString(text, 70, 90);

        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

    private void readImage(String path) {
        try {
            image_w_l = ImageIO.read(new File(path + "\\elements\\w_l.png"));
            image_w_t = ImageIO.read(new File(path + "\\elements\\w_t.png"));
            image_w_p = ImageIO.read(new File(path + "\\elements\\w_p.png"));
            image_w_s = ImageIO.read(new File(path + "\\elements\\w_s.png"));
            image_c = ImageIO.read(new File(path + "\\elements\\c.png"));
            image_i = ImageIO.read(new File(path + "\\elements\\i.png"));
            image_r = ImageIO.read(new File(path + "\\elements\\r.png"));
            image_v = ImageIO.read(new File(path + "\\elements\\v.png"));
            image_l = ImageIO.read(new File(path + "\\elements\\l.png"));
            image_b = ImageIO.read(new File(path + "\\elements\\b.png"));
            image_error = ImageIO.read(new File(path + "\\elements\\error.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateSimpleImage(BufferedImage image, String targetFile) {
        try {
            ImageIO.write(setNonAlpha(image), targetFile.split("\\.")[1], new File(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage generateVoltageImage(String targetFile) {
        try {
            BufferedImage imageResult = image_error;

            for (int i = 0; i < table.length; i++) {
                BufferedImage imageRow = image_error;
                for (int j = 0; j < table[0].length; j++) {
                    // 图片染色
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
                        color = 0xff00ffff;
                        break;
                    case 7:
                        color = 0xffff8888;
                        break;
                    case 8:
                        color = 0xff88ff88;
                        break;
                    case 9:
                        color = 0xff8888ff;
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
                            throw new IllegalArgumentException("特殊错误, 请检查编译环境:");
                        }
                    } else {
                        int angle = 90 * (-detected[i][j] - 1);
                        if (name.charAt(0) == 'v') {
                            imageToInsert = addText(rotateImage(image_v, angle), name);
                        } else if (name.charAt(0) == 'i') {
                            imageToInsert = addText(rotateImage(image_i, angle), name);
                        } else if (name.charAt(0) == 'r') {
                            imageToInsert = addText(rotateImage(image_r, angle), name);
                        } else if (name.charAt(0) == 'l') {
                            imageToInsert = addText(rotateImage(image_l, angle), name);
                        } else if (name.charAt(0) == 'c') {
                            imageToInsert = addText(rotateImage(image_c, angle), name);
                        } else if (name.charAt(0) == 'b') {
                            imageToInsert = image_b;
                        } else {
                            imageToInsert = image_error;
                        }
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
            // 输出想要的图片
            ImageIO.write(imageResult, targetFile.split("\\.")[1], new File(targetFile));

            return imageResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void generateCircuitImage(BufferedImage image, String targetFile) {
        for (int i = 0; i < circuitTable.length; i++) {
            for (int j = 0; j < circuitTable[0].length; j++) {
                // 图片染色
                int color;
                switch (circuitTable[i][j]) {
                case 0:
                    color = 0;
                    break;
                case 1:
                    color = 0;
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
                    color = 0xff00ffff;
                    break;
                case 7:
                    color = 0xffff8888;
                    break;
                case 8:
                    color = 0xff88ff88;
                    break;
                case 9:
                    color = 0xff8888ff;
                    break;
                default:
                    color = 0;
                    break;
                }

                for (int y = i * 133 + 46; y < i * 133 + 87; y++) {
                    for (int x = j * 133 + 46; x < j * 133 + 87; x++) {
                        int pixel = image.getRGB(x, y); // j2横坐标,j1竖坐标
                        if (pixel == 0xffffffff)
                            pixel = color; // 否则为设定颜色
                        if (pixel != 0)
                            image.setRGB(x, y, pixel);
                    }
                }
            }
        }

        try {
            ImageIO.write(image, targetFile.split("\\.")[1], new File(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageGenerator(String path, Result[][] table, int[][] detected, int[][] circuitTable) {
        this.table = table;
        this.detected = detected;
        this.circuitTable = circuitTable;

        readImage(path);

        BufferedImage voltageResultImage = generateVoltageImage(path + "\\VoltageResult.png");
        generateSimpleImage(voltageResultImage, path + "\\SimpleResult.png");
        generateCircuitImage(voltageResultImage, path + "\\CircuitResult.png");
    }
}