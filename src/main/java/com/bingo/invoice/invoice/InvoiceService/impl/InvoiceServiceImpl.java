package com.bingo.invoice.invoice.InvoiceService.impl;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.common.InvoiceData;
import com.bingo.invoice.invoice.common.InvoiceExtractor;
import com.bingo.invoice.invoice.dao.Detail1Mapper;
import com.bingo.invoice.invoice.dao.DetailMapper;
import com.bingo.invoice.invoice.dao.InvoiceMapper;
import com.bingo.invoice.invoice.dao.ReimbursementMapper;
import com.bingo.invoice.invoice.entity.Detail;
import com.bingo.invoice.invoice.entity.Detail1;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.Reimbursement;
import com.bingo.invoice.invoice.entity.vo.DetailVo;
import com.bingo.invoice.invoice.entity.vo.InvoiceVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.misc.Cache;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: lizk
 * @Date: 2019/4/28 14:31
 * @Description:
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    private InvoiceExtractor extrator;
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private DetailMapper detailMapper;
    @Autowired
    private Detail1Mapper detail1Mapper;

    @Autowired
    private ReimbursementMapper reimbursementMapper;
    @Override
    public InvoiceData extract(String filename) {
        InvoiceData data = null;
        try {
            data = extrator.extract(filename);
            try
            {   String seq=UUID.randomUUID().toString();
                invoiceMapper.insert(data.getInvoice());
                //如果有明细，则插入
                if(data.getDetail() != null)
                {
                    for(Object detail : data.getDetail())
                    {
                        if(data.getType().equals("00"))
                        {
                            ((Detail)detail).setInvoiceSeq(seq);
                            ((Detail)detail).setSeq(UUID.randomUUID().toString());
                            detailMapper.insert((Detail)detail);
                        }else
                        {
                            ((Detail1)detail).setInvoiceSeq(seq);
                            ((Detail1)detail).setSeq(UUID.randomUUID().toString());
                            detail1Mapper.insert((Detail1)detail);
                        }
                    }
                }
            }catch(Exception ee)
            {
                logger.warn("插入数据库失败：" + ee.getMessage(), ee);
            }
            data.setMessage("解析发票成功");
            data.setCode("0000");
        } catch (Exception e) {
            logger.error("解析发票失败：" + e.getMessage(), e);
            if (data == null)
                data = new InvoiceData();
            data.setMessage("解析发票失败");
            data.setCode("9999");
        }
        return data;
    }

    @Override
    public void insertInvoice(InvoiceData data,String reiId) {
        try {
            String seq=UUID.randomUUID().toString();
            data.getInvoice().setSeq(seq);
            data.getInvoice().setReiId(reiId);
            invoiceMapper.insert(data.getInvoice());
            //如果有明细，则插入
            if(data.getDetail() != null)
            {
                for(Object detail : data.getDetail())
                {
                    if(data.getType().equals("00"))
                    {
                        ((Detail)detail).setInvoiceSeq(seq);
                        ((Detail)detail).setSeq(UUID.randomUUID().toString());
                        detailMapper.insert((Detail)detail);
                    }else
                    {
                        ((Detail1)detail).setInvoiceSeq(seq);
                        ((Detail1)detail).setSeq(UUID.randomUUID().toString());
                        detail1Mapper.insert((Detail1)detail);
                    }
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public void insertInvoiceError(String reiId) {
        Invoice invoice=new Invoice();
        invoice.setSeq(UUID.randomUUID().toString());
        invoice.setReiId(reiId);
        invoice.setJqbh("未知");
        invoice.setTitle("未知");
        invoice.setFphm("未知");
        invoice.setFpdm("未知");
        invoice.setKprq("未知");
        invoice.setMc1("未知");
        invoice.setMc2("未知");
        invoice.setHjje(BigDecimal.valueOf(0.0));
        invoice.setIdenDtatus("0");
        invoiceMapper.insertSelective(invoice);
    }

    @Override
    public List<Invoice> getInvoiceListByReiId(String reiId) {
       Map<String,Object> map=new HashMap<String,Object>();
       map.put("reiId",reiId);
       List<Invoice> invoiceList=invoiceMapper.getInvoiceListByReiId(map);
        return invoiceList;
    }

    @Override
    public List<InvoiceVo> getInvoiceVoListByReiId(String reiId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("reiId",reiId);
        List<InvoiceVo> invoiceVoList=invoiceMapper.getInvoiceVoListByReiId(map);
        for(InvoiceVo invoiceVo:invoiceVoList){
            String invoiceId=invoiceVo.getSeq();
            map.clear();
            map.put("invoiceId",invoiceId);
            if(invoiceVo.getType().equals("00")){
            //detail
             List<Detail> detailList=detailMapper.getDetails(map);
             String invoiceDetail="";
             for(Detail detail:detailList){
                 invoiceDetail= invoiceDetail+detail.getWwmc()+"--"+detail.getSe()+"</br>";
             }
             invoiceVo.setDetails(invoiceDetail);
            }else{
             //detail1
                String invoiceDetail1="";
                List<Detail1> detail1List=detail1Mapper.getDetail1s(map);
                for(Detail1 detail1:detail1List){
                    invoiceDetail1=invoiceDetail1+detail1.getXmmc()+"--"+detail1.getSe()+"</br>";
                }
                invoiceVo.setDetails(invoiceDetail1);
            }

        }
        return invoiceVoList;
    }

    @Override
    public List<Invoice> getInvoiceListByNo(String fphm) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("fphm",fphm);
        List<Invoice> invoiceList=invoiceMapper.getInvoiceListByNo(map);
        return invoiceList;
    }

    @Override
    @Transactional
    public void invoicesIdentify(String ids) {
        Map<String,Object> map=new HashMap<String,Object>();
        String idArray[]=ids.split(",");
        for(String id:idArray){
            Invoice invoice=invoiceMapper.selectByPrimaryKey(id);
            //未识别的发票进行识别
            if(StringUtils.isEmpty(invoice.getIdenDtatus())){
                //先验证是否是我公司的发票
                if(invoice.getMc1().equals("北京品高辉煌科技有限责任公司")){
                    List<Invoice> invoiceList=checkInvoiceUse(invoice);
                    //说明该发票没有使用
                    if(invoiceList.size()==0){
                        //将该发票更新成功
                        invoice.setIdenDtatus("3");
                        invoiceMapper.updateByPrimaryKeySelective(invoice);
                    }else{
                        //将该发票更新已使用,并找到该发票的id
                        String invoiceIds="";
                        for(Invoice invoice1:invoiceList){
                            invoiceIds+=invoice1.getReiId();
                        }
                        invoice.setIdenDtatus("2"+"---报销单Id为:"+invoiceIds);
                        invoiceMapper.updateByPrimaryKeySelective(invoice);
                    }
                }else{
                    //不是为我公司的发票
                    invoice.setIdenDtatus("1");
                    invoiceMapper.updateByPrimaryKeySelective(invoice);
                }

            }
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Invoice invoice=invoiceMapper.selectByPrimaryKey(id);
        //先删除发票信息
        invoiceMapper.deleteByPrimaryKey(id);
        //再删除detail信息
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("invoiceId",id);
        if(invoice.getType().equals("00")){
            //删除detail表
            detailMapper.deleteByInvoiceId(map);
        }else{
            //删除detail1表
            detail1Mapper.deleteByInvoiceId(map);
        }
    }

    @Override
    public PageInfo<InvoiceVo> getInvoicePageByParams(Integer pageNum, Integer pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum,pageSize);
        List<InvoiceVo> invoiceVoList=invoiceMapper.getInvoicePageByParams(map);
        PageInfo<InvoiceVo> pageInfo=new PageInfo<InvoiceVo>(invoiceVoList);
        return pageInfo;
    }

    @Override
    public List<InvoiceVo> getInvoicesByParams(Map<String, Object> map) {
        List<InvoiceVo> invoiceVoList=invoiceMapper.getInvoicePageByParams(map);
        return invoiceVoList;
    }

    @Override
    public void updateReimbursement(String reiId) {
        Reimbursement reimbursement=reimbursementMapper.selectByPrimaryKey(reiId);
        reimbursement.setStatus("1");
        //计算总价格
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("reiId",reiId);
        map.put("status","3");
        List<Invoice> invoiceList=invoiceMapper.getInvoiceListByReiIdAndStatus(map);
        BigDecimal totalManey=new BigDecimal("0");
        BigDecimal totalJe=new BigDecimal("0");
        BigDecimal totalSe=new BigDecimal("0");
        for(Invoice invoice:invoiceList){
            totalManey=totalManey.add(invoice.getJshjxx());
            totalJe=totalJe.add(invoice.getHjje());
            totalSe=totalSe.add(invoice.getHjse());
        }
        reimbursement.setTotalMoney(totalManey);
        reimbursement.setTotalJe(totalJe);
        reimbursement.setTotalSe(totalSe);
        //更新报销时间
        reimbursement.setCreateDate(new Date());
        reimbursementMapper.updateByPrimaryKeySelective(reimbursement);

    }

    @Override
    public PageInfo<DetailVo> getDetailPageList(Integer pageNum, Integer pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum,pageSize);
        List<DetailVo> detailVoList=invoiceMapper.getDetailByParams(map);
        PageInfo<DetailVo> pageInfo=new PageInfo<DetailVo>(detailVoList);
        return pageInfo;
    }

    @Override
    public List<DetailVo> getDetailVoList(Map<String, Object> map) {
        List<DetailVo> detailVoList=invoiceMapper.getDetailByParams(map);
        return detailVoList;
    }

    @Override
    @Transactional
    public void updateInvoiceStatus(String fphm) {
        //根据发票号查出发票
        List<Invoice> invoiceList=this.getInvoiceListByNo(fphm);
        Invoice invoice=new Invoice();
        for(Invoice invoice1:invoiceList){
            if(invoice1.getIdenDtatus().equals("3")){
                invoice=invoice1;
            }
        }
        //0:不是发票文件，1，不是本公司的发票，2已使用的发票，3，可以使用的发票，4：核验失败的发票',
        invoice.setIdenDtatus("4");
        invoiceMapper.updateByPrimaryKeySelective(invoice);
        //更新报销单合计金额和税额
        Reimbursement reimbursement=reimbursementMapper.selectByPrimaryKey(invoice.getReiId());
        reimbursement.setTotalSe(reimbursement.getTotalJe().subtract(invoice.getHjse()));
        reimbursement.setTotalJe(reimbursement.getTotalJe().subtract(invoice.getHjje()));
        reimbursement.setTotalMoney(reimbursement.getTotalMoney()
                .subtract(invoice.getHjje()).subtract(invoice.getHjse()));
        reimbursementMapper.updateByPrimaryKeySelective(reimbursement);
    }

    @Override
    public void taskDeleteData() {
        //查询发票状态不为1的记录
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("status","0");
        List<Reimbursement> reimbursementList=reimbursementMapper.getNoSubmitData(map);
        List<String> reiIds=new ArrayList<>();
        reimbursementList.stream().forEach(r->reiIds.add(r.getId()));
        System.out.println(reiIds.size());
        //删除报销单
        if(reiIds.size()>0){
            reimbursementMapper.deleteBatch(reiIds);
            //删除发票
            invoiceMapper.deleteBatchInvoice(reiIds);
        }

    }

    /**
     *
     * 功能描述: 检查电子发票
     *
     * @param:
     * @return:
     * @auther: lizk
     * @date: 2019/4/28 14:55
     */
    private  List<Invoice> checkInvoiceUse(Invoice invoice){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("jqbh",invoice.getJqbh());
        map.put("fpdm",invoice.getFpdm());
        map.put("fphm",invoice.getFphm());
        map.put("kprq",invoice.getKprq());
        List<Invoice> invoiceList=invoiceMapper.getListByParams(map);
        return invoiceList;
    }
}
