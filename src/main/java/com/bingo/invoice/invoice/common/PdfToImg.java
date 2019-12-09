package com.bingo.invoice.invoice.common;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: lizk
 * @Date: 2019/6/24 13:52
 * @Description:
 */
public class PdfToImg {
    public static List<String> pdfToImg(List<String> filePaths,String imgDir,String staticServer)throws IOException{
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<String> imgUrls=new ArrayList<>();
        for(String filePath:filePaths){
            String s=format.format(new Date());
            String u=UUID.randomUUID().toString();
            //保存路经(绝对路径:E:/upload/invoice)
            String savePath = imgDir + "/image/" + s;
            File f = new File(savePath);
            if (!f.exists() && !f.isDirectory()) {
                // 创建目录
                f.mkdir();
            }
            String path=savePath+"/"+u+".jpg";
            //访问路经(http://127.0.0.1:8088)
            String accessUrl=staticServer+ "/image/" + s+ "/"+u+".jpg";
            PDDocument pd = PDDocument.load(new File(filePath));
            PDFRenderer pdfRenderer = new PDFRenderer(pd);
            BufferedImage combined = null;
            for (int page = 0; page < pd.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 284);
                if (page == 0) {
                    combined = bim;
                } else {
                    //combined = merge(combined, bim);
                }
            }
            ImageIO.write(combined, "JPG", new File(path));
            pd.close();
            System.out.println("生成完成");
            imgUrls.add(accessUrl);
        }
        return imgUrls;
    }
    private static BufferedImage merge(BufferedImage image1, BufferedImage image2) {
        BufferedImage combined = new BufferedImage(
                image1.getWidth(),
                image1.getHeight() + image2.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics g = combined.getGraphics();
        g.drawImage(image1, 0, 0, null);
        g.drawImage(image2, 0, image1.getHeight(), null);
        g.dispose();
        return combined;
    }
}
