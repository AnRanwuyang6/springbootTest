package com.bingo.invoice.invoice.controller;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.AjaxResult;
import com.bingo.invoice.invoice.common.InvoiceData;
import com.bingo.invoice.invoice.common.InvoiceExtractor;
import com.bingo.invoice.invoice.common.ServiceException;
import com.bingo.invoice.invoice.dao.InvoiceMapper;
import com.bingo.invoice.invoice.entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: lizk
 * @Date: 2019/4/28 10:31
 * @Description:
 */
@Controller
@RequestMapping("/upload")
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
public class FileUploadController {

    private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceMapper invoiceMapper;
    // 保存上传文件的主目录
    @Value("${uploads}")
    private String uploads;
    // 访问路径
    @Value("${staticServer}")
    private String staticServer;

    @Autowired
    private InvoiceExtractor extrator;

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
            InvoiceData data=extrator.extract(url);
            if(data.getInvoice()==null){
                //不是发票文件
                invoiceService.insertInvoiceError(reiId);
            }else{
                Invoice invoice=data.getInvoice();
                data.getInvoice().setCreateAt(new Date());
                data.getInvoice().setCreateBy(request.getSession().getAttribute("userName").toString());
                invoiceService.insertInvoice(data,reiId);
            }
            //
        }
        List<Invoice> invoiceList =invoiceService.getInvoiceListByReiId(reiId);
        result.setData(invoiceList);
        result.setCode(AjaxResult.RESULT_CODE_0000);
        return result;
    }

    @RequestMapping("/file")
    @ResponseBody
    public AjaxResult UploadPdf(HttpServletRequest request, HttpServletResponse response)throws Exception{
        AjaxResult result=new AjaxResult();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
        MultipartFile multipartFile=multipartHttpServletRequest.getFile("file");
        //上传文件
        String url=saveFile(multipartFile);
        InvoiceData data=extrator.extract(url);
        String message="";
        if(data.getInvoice()==null){
            //不是发票文件
            message="不是发票文件";
        }else{
            String mc1=data.getInvoice().getMc1();
            String fphm=data.getInvoice().getFphm();
            String fpdm=data.getInvoice().getFpdm();
            List<Invoice> invoiceList=checkInvoiceUse(fpdm,fphm);
            if(mc1.equals("北京品高辉煌科技有限责任公司")){
                if(invoiceList.size()==0){
                    message="可以使用的发票";
                }else{
                    //将该发票更新已使用,并找到该发票的id
                    String invoiceIds="";
                    for(Invoice invoice1:invoiceList){
                        invoiceIds+=invoice1.getReiId();
                    }
                    message="该发票已经使用过了"+"---报销单Id为:"+invoiceIds;
                }
            }else{
                message="不是我公司的发票";
            }
        }
        result.setCode(AjaxResult.RESULT_CODE_0000);
        result.setMessage(message);
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
            // System.out.println(file.getName());
            out.write(file.getBytes());
            out.flush();
            out.close();
            //访问路径
            String accessUrl="http://"+staticServer+"/"+s+"/"+ file.getOriginalFilename();
            return path;
        } catch (Exception e) {
            throw new ServiceException("文件上传失败:" + e.getMessage(), e);
        }
    }

    private  List<Invoice> checkInvoiceUse(String fpdm,String fphm){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("fpdm",fpdm);
        map.put("fphm",fphm);
        List<Invoice> invoiceList=invoiceMapper.getListByParams(map);
        return invoiceList;
    }
}
