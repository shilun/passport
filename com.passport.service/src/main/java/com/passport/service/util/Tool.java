package com.passport.service.util;

import com.common.upload.UploadUtil;
import com.common.util.Result;
import com.mongodb.DBObject;
import com.swetake.util.Qrcode;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

/**
 * @author Luo
 * @date 2018/9/25 15:04
 */
@Component
public class Tool {
    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    private Logger logger=Logger.getLogger(Tool.class);
    @Autowired(required = false)
    private UploadUtil uploadUtil;
    /**
     * 生成二维码
     * @param url
     * @param fileName
     * @throws IOException
     */
    public Result<String> generateQRCode(String url,String fileName,String imgTempDir) throws Exception {
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

        File qrCodeFile = new File(imgTempDir + fileName + ".jpg");
        ImageIO.write(bi, "jpg", qrCodeFile);
        Result<String> res = uploadUtil.uploadFile(qrCodeFile);
        qrCodeFile.delete();
        if(!res.getSuccess()){
            logger.error("保存图片失败");
            logger.error("uploadParams->code:"+uploadUtil.getCode()+" scode:"+uploadUtil.getScode()+" domain:"+uploadUtil.getDomainName());
        }
        return res;
    }

    /**
     * 将DBObject转为实体
     * @param dbObject
     * @param bean
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public <T> T dbObjectToBean(DBObject dbObject, T bean) throws Exception{
        if (bean == null) {
            return null;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            Object object = dbObject.get(varName);
            if (object != null) {
                BeanUtils.setProperty(bean, varName, object);
            }

        }
        return bean;
    }

    public boolean isNo(String str){
        return pattern.matcher(str).matches();
    }
}
