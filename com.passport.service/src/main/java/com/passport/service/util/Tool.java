package com.passport.service.util;


import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Luo
 * @date 2018/9/25 15:04
 */
public class Tool {
    /**
     * 生成二维码
     * @param url
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void generateQRCode(String url, String filePath, String fileName) throws IOException {
        Qrcode qrcode = new Qrcode();
        //错误修正容量
        //L水平   7%的字码可被修正
        //M水平   15%的字码可被修正
        //Q水平   25%的字码可被修正
        //H水平   30%的字码可被修正
        //QR码有容错能力，QR码图形如果有破损，仍然可以被机器读取内容，最高可以到7%~30%面积破损仍可被读取。
        //相对而言，容错率愈高，QR码图形面积愈大。所以一般折衷使用15%容错能力。
        qrcode.setQrcodeErrorCorrect('M');
        qrcode.setQrcodeEncodeMode('B');
        qrcode.setQrcodeVersion(13);
        byte[] d = url.getBytes("UTF-8");
        BufferedImage bi = new BufferedImage(281, 281, BufferedImage.TYPE_INT_RGB);
        // createGraphics
        Graphics2D g = bi.createGraphics();
        // set background
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, 281, 281);
        //设置二维码图片颜色
        g.setColor(Color.BLACK);

        if (d.length > 0 && d.length < 123) {
            boolean[][] b = qrcode.calQrcode(d);
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b.length; j++) {
                    if (b[j][i]) {
                        g.fillRect(j * 4 + 2, i * 4 + 2, 4, 4);
                    }
                }
            }
        }

        g.dispose();
        bi.flush();

        File qrCodeFile = new File(filePath + "/" + fileName + ".png");

        ImageIO.write(bi, "png", qrCodeFile);

    }
}
