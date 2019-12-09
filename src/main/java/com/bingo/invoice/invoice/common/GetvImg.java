package com.bingo.invoice.invoice.common;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.*;

import com.bingo.invoice.invoice.InvoiceService.InvoiceService;
import com.bingo.invoice.invoice.entity.Invoice;
import com.bingo.invoice.invoice.entity.vo.CheckVo;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public  class GetvImg {
	private static final String SYSVer = "V1.0.07_001";//国税局发票系统版本
	private static final String urlBegin="https://fpcy.";
	private static final String urlEnd=".chinatax.gov.cn/WebQuery";
	@Autowired
	private  InvoiceService invoiceService;
	/**
	 * 获取验证码
	 */
	public static String  getvImg(InvInfor inv,String nowtime,String publickey,String areacode,String url_prefix){
		String vImg_infor = "";
		String r = String.valueOf(Math.random());
		String url_str=url_prefix + "/yzmQuery?"+"callback="+"jQuery1102029747756162651856_1557278393427"+"&_=&fphm="+inv.getInvNo()+"&fpdm="+inv.getInvCode()+"&r="+r+"&v="+SYSVer+"&nowtime="+nowtime+"&area="+areacode+"&publickey="+publickey;
		//String url_str="https://fpcy.jiangsu.chinatax.gov.cn/WebQuery/yzmQuery?callback=jQuery1102029747756162651856_1557278393427&fpdm=032001600611&fphm=90905091&r=0.30211937310713&v=V1.0.07_001&nowtime=1557287303347&area=3200&publickey=3D6366E657BF8B0EFEEED40C7DB45F49&_=1557278393453";
		System.out.println("url_str:" + url_str);
		Map<String,String> args_map = new HashMap<String,String>();
		//args_map.put("callback","jQuery");
		args_map.put("fpdm",inv.getInvCode());
		args_map.put("fphm",inv.getInvNo());
		args_map.put("r",r);
		args_map.put("v",SYSVer);
		args_map.put("nowtime",nowtime);
		//args_map.put("area","4400");
		args_map.put("area",areacode);
		args_map.put("pulickey",publickey);
		JSONObject json = JSONObject.fromObject(args_map);
		String args_str = json.toString();
		//String s="https://fpcy.beijing.chinatax.gov.cn/WebQuery/yzmQuery?fpdm=011001700112&fphm=12103785&r=0.9820404305492046&v=V1.0.07_001&nowtime=782930827414393&area=1100&pulickey=92FBFA89768233734717E989361E54EE";
		GetvImg get = new GetvImg();
		vImg_infor = get.sendRequest(url_str,args_str,null);
		System.out.println("vImg_infor: " + vImg_infor);
		return vImg_infor;
	}
	/**
	 * 发票校验
	 */
	public static String getInvInfor(String url_prefix, CheckVo checkVo,String area){
		String inv_infor = "";
		String publickey="";
		try {
			String s=URLEncoder.encode(checkVo.getYzm(),"UTF-8");
			String yzhSj=URLEncoder.encode(checkVo.getKey2(),"UTF-8");
			String url_str=url_prefix+"/vatQuery?"+"callback="+"jQuery"+"&key1="+checkVo.getFpdm()+"&key2="+checkVo.getFphm()+"&key3="+checkVo.getInvDate()+"&key4="+checkVo.getJym()+"&fplx="+checkVo.getFplx()
					+"&yzm="+s+"&yzmSj="+yzhSj+"&index="+checkVo.getKey3()+"&area="+"3700"+"&publickey="+publickey+"&_="+System.currentTimeMillis();
			System.out.println("url_str2: " + url_str);
			Map<String,String> args_map = new HashMap<String,String>();
			args_map.put("callback","jQuery");
			args_map.put("key1",checkVo.getFpdm());
			args_map.put("key2",checkVo.getFphm());
			args_map.put("key3",checkVo.getInvDate());
			args_map.put("key4",checkVo.getJym());
			args_map.put("fplx",checkVo.getFplx());
			args_map.put("yzm",checkVo.getYzm());
			args_map.put("yzmSj",checkVo.getKey2());
			args_map.put("index",checkVo.getKey3());
			args_map.put("area","3700");
			args_map.put("publickey","");
			args_map.put("_",String.valueOf(System.currentTimeMillis()));
			JSONObject json = JSONObject.fromObject(args_map);
			String args_str = json.toString();
			GetvImg get = new GetvImg();
			inv_infor = get.sendRequest(url_str,args_str,checkVo.getCookie());
			System.out.println("inv_infor: " + inv_infor);
		}catch (Exception e){

		}
		return inv_infor;
	}


	 private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


	/**
	 * 专门发送请求，并返回请求结果的字符串
	 */
	public String sendRequest(String url_str,String args_str,String cookie){
		String reply = "";
		String newReply="";
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null,null, null);
			SSLSocketFactory sslSocketFactory = context.getSocketFactory();
			URL url = new URL(url_str);
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname,SSLSession session){
					return true;
				}
			});
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(sslSocketFactory);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
			System.setProperty("https.proxyHost", "127.0.0.1");
			System.setProperty("https.proxyPort", "8888");
			if(StringUtils.isNotEmpty(cookie)){
				conn.setRequestProperty("Cookie", cookie);
			}
			if (conn instanceof HttpsURLConnection)  {
	        	SSLContext sc = SSLContext.getInstance("SSL");
	        	sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
	        	((HttpsURLConnection) conn).setSSLSocketFactory(sc.getSocketFactory());
	        	((HttpsURLConnection) conn).setHostnameVerifier(new TrustAnyHostnameVerifier());
	        }
			conn.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312"));
			StringBuilder builder = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				builder.append(inputLine);
			}
			in.close();
			String data = builder.toString();
			reply = data;
			System.out.println("The response from the server is: " + data);
			conn.disconnect();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return reply;
	}

	/**
	 * 生成get请求参数
	 */
	private String genGetUrlParam(JSONObject param) {
		StringBuffer reqParam = new StringBuffer();
		for (Object key : param.keySet()) {
			reqParam.append("&").append(key).append("=").append(param.get(key));
		}
		System.out.println(reqParam);
		return reqParam.toString();
	}
	/**
	 * 通过发票代码按照一定的逻辑得出发票所属的区域的areacode
	 * @param inv_code - 发票代码
	 * @return area_code
	 */
	public static String getAreaCode(String inv_code){
		String area_code = "";
		if(inv_code.length() == 12){
			area_code = inv_code.substring(1,5);
		}else{
			area_code = inv_code.substring(0,4);
		}
		List<String> city_code_list = new ArrayList<String>();
		city_code_list.add("2102");
		city_code_list.add("3302");
		city_code_list.add("3502");
		city_code_list.add("3702");
		city_code_list.add("4403");
		for(int i = 0;i<city_code_list.size();i++){
			if(area_code.equals(city_code_list.get(i))){
				area_code = area_code.substring(0,2) + "00";
				break;
			}
		}
		System.out.println("area_code: " + area_code);
		return area_code;
	}
	/**
	 *
	 * 功能描述: 转map
	 *
	 * @param:
	 * @return:
	 * @auther: lizk
	 * @date: 2019/5/7 18:15
	 */

	public static Map<String,String> convert2Map(String data){
		JSONObject json = JSONObject.fromObject(data);
		Map<String,String> vImg_map = new HashMap<String,String>();
		vImg_map = (Map<String,String>)json;
		for(String key : vImg_map.keySet()){
			System.out.println(key + " : " + vImg_map.get(key));
		}
		return vImg_map;
	}

	/**
	 * 得到访问前缀
	 * @param province
	 * @return
	 */
	public static String getUrlPrefix(String province){
		String pinyin=ToPinyin(province);
		String url_prefix=urlBegin+pinyin+urlEnd;
		System.out.println("访问前缀"+url_prefix+"---------------------");
		return url_prefix;
	}

	public  static  void main(String[] args) {

	}

	/**
	 * 汉字转为拼音
	 * @param chinese
	 * @return
	 */
	public  static String ToPinyin(String chinese){
		String pinyinStr = "";
		char[] newChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < newChar.length; i++) {
			if (newChar[i] > 128) {
				try {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}else{
				pinyinStr += newChar[i];
			}
		}
		return pinyinStr;
	}
}


