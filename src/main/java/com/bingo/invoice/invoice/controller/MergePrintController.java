package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.*;
import com.bingo.invoice.invoice.dao.InvoiceMapper;
import com.bingo.invoice.invoice.entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: lizk
 * @Date: 2019/6/24 10:31
 * @Description:
 */
@Controller
@RequestMapping("/merge")
public class MergePrintController {

    private final Logger logger = LoggerFactory.getLogger(MergePrintController.class);

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    // 保存上传文件的主目录
    @Value("${uploads}")
    private String uploads;
    // 访问路径
    @Value("${staticServer}")
    private String staticServer;

    @RequestMapping("/html")
    public String html(){
        return "mergePdf/Frame";
    }
    @RequestMapping("/print")
    public String print(String imageUrlsId, HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        List<String>imageUrls =(List<String>)session.getAttribute(imageUrlsId);
        model.addAttribute("imageUrls",imageUrls);
        return "mergePdf/print";
    }


    @RequestMapping("/files")
    @ResponseBody
    public AjaxResult UploadPdfs(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile[] files,String reiId)throws Exception{
        AjaxResult result=new AjaxResult();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFileList=multipartHttpServletRequest.getFiles("file");
        String urls="";
        List<String> paths = new ArrayList<String>();
        for(MultipartFile file:multipartFileList){
           String url=saveFile(file);
           paths.add(url);
            //得到解析发票信息
        }
        List<String> imgUrls= PdfToImg.pdfToImg(paths,uploads,staticServer);
        HttpSession session=request.getSession();
        String imageUrlsId=UUID.randomUUID().toString();
        session.setAttribute(imageUrlsId,imgUrls);
        System.out.println(imageUrlsId+"----------------------");
        result.setData(imageUrlsId);
        result.setCode(AjaxResult.RESULT_CODE_0000);
        return result;
    }

    public String saveFile(MultipartFile file) throws ServiceException {
        // 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        // 目录形式为：根目录/日期
        String s=format.format(new Date());
        String savePath = uploads + "/" + s;
        File f = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!f.exists() && !f.isDirectory()) {
            logger.debug(savePath + "目录不存在，需要创建");
            // 创建目录
            f.mkdir();
        }
        try {
            String path = savePath + "/" + file.getOriginalFilename();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path)));
            out.write(file.getBytes());
            out.flush();
            out.close();
            //访问路径
            return path;
        } catch (Exception e) {
            throw new ServiceException("文件上传失败:" + e.getMessage(), e);
        }
    }

}
