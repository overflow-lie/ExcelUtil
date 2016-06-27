package com.sirun.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.sirun.util.excel.bean.SsssShopInfo;
import com.sirun.util.excel.dao.SsssShopInfoMapper;
import com.sirun.util.excel.service.impl.SsssShopServiceImpl;
import com.sirun.util.model.AmapResultModel;
import com.sirun.util.model.Postion;

public class ExcelUtilTest {

	// private static SqlSession sqlSession;
	//
	// @Before
	// public void openSession(){
	// String resource = "tempMyiBatisConfiguration.xml";
	// Reader reader = null;
	// try {
	// reader = Resources.getResourceAsReader(resource);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// SqlSessionFactory sqlMapper = new
	// SqlSessionFactoryBuilder().build(reader);
	// sqlSession = sqlMapper.openSession();
	// }
	//
	// @After
	// public void closeSession(){
	// if(sqlSession!=null){
	// sqlSession.close();
	// }
	// }
	/**
	 * 众泰大迈X5售后服务站信息
	 */
	@Test 
	public void importExcelAndChangeSsssShopInfoPhone2(){
		String resource = "tempMyiBatisConfiguration.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		SqlSession sqlSession = sqlMapper.openSession();
		SsssShopInfoMapper ssssShopInfoMapper = sqlSession.getMapper(SsssShopInfoMapper.class);
		List<SsssShopInfo> ssiList = null;
		SsssShopServiceImpl sssi = new SsssShopServiceImpl();
		File file = new File("C:\\Users\\sirun\\Desktop\\众泰金坛售后服务站信息列表2016-1-25.xls");
		InputStream is = null;
		try{
			is = new FileInputStream(file);
			ssiList = sssi.importExcel2(is);
			for (SsssShopInfo ssssShopInfo : ssiList) {
				setLaLi(ssssShopInfo);
				if(ssssShopInfo.getLatitude()==null || ssssShopInfo.getLongitude() == null){
					setLaliByAddress(ssssShopInfo);
				}
				System.out.println(ssssShopInfo);
			}
			System.out.println("=====================================");
			int i = 0;
			List<String> vehicleBrands = new ArrayList<>(); 
			vehicleBrands.add("X5");
			for (SsssShopInfo ssssShopInfo : ssiList) {
				ssssShopInfoMapper.insertBatchByVehicle(ssssShopInfo, vehicleBrands);
				if (ssssShopInfo.getLatitude()==null || ssssShopInfo.getLongitude() == null) {
					System.out.println(ssssShopInfo+"======================="+"    "+ ++i);
				}
			}
			
			sqlSession.commit();
			
			
		}catch(Exception e){
			
		}
		
	}
	/**
	 * 
	 * 导入表格，修改SsssShopInfo表中的Phone信息
	 * 
	 */
	@Test
	public void importExcelAndChangeSsssShopInfoPhone() {
		List<SsssShopInfo> ssiList = null;
		SsssShopServiceImpl sssi = new SsssShopServiceImpl();
		List<String> newSsssNameList = new ArrayList<>();
		List<String> missAddress = new ArrayList<>();
		File file = new File("C:\\Users\\sirun\\Desktop\\众泰Z700售后服务站信息列表12-14 (2).xls");
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			ssiList = sssi.importExcel(is);
			if (ssiList == null) {
				return;
			}
//			int i = 0;  //用于记录未查到经纬度的公司
			for (SsssShopInfo ssi : ssiList) {
				String ssssName = ssi.getSsssName();
				getDetailAddress(ssi);
				double[] data = ExcelUtilTest.addressToGPS(ssssName);
				if (data != null) {
					ssi.setLatitude(data[1] + "");
					ssi.setLongitude(data[0] + "");
				}
				if (data == null) {
					setLatitudeLongitude(ssssName, ssi);
					if (ssi.getLatitude() == "" || ssi.getLatitude() == null) {
						newSsssNameList.add(ssssName);
//						System.out.println(++i);
					}
				}
				if (ssi.getAddress() == null) {
					missAddress.add(ssssName + "");
				}
//				System.out.println(
//						ssi + "经度：" + ssi.getLatitude() + "纬度：" + ssi.getLongitude() + "地址：" + ssi.getAddress());
			}

			System.out.println("============================================");
			for (String newSsssName : newSsssNameList) {
				System.out.println(newSsssName);
			}
			for (String miss : missAddress) {
				System.out.println("===========" + miss);
			}
			
			List<SsssShopInfo> otherList = new ArrayList<>();
			List<SsssShopInfo> okList = new ArrayList<>();
			for (SsssShopInfo ssssShopInfo : ssiList) {
				
				if(ssssShopInfo.getAddress()!=null && ssssShopInfo.getAddress()!="" && ssssShopInfo.getLatitude() != null && ssssShopInfo.getLatitude() != ""){
					okList.add(ssssShopInfo);
				}else{
					otherList.add(ssssShopInfo);
				}
				
			}
			if(okList!=null){
				sssi.insertSsssShopInfo(okList);
			}
			if(otherList!=null){
				System.out.println("如下信息不完善未插入：");
				for (SsssShopInfo ssssShopInfo : otherList) {
					System.out.println(ssssShopInfo);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String KEY = "aa4a48297242d22d2b3fd6eddfe62217";

	private static Pattern pattern = Pattern.compile("\"location\":\"(\\d+\\.\\d+),(\\d+\\.\\d+)\"");
	
	/**
	 * http://restapi.amap.com/v3/geocode/geo?key=%s&address=%s&city=%s
	 * @param ssi
	 */
	public static void setLaliByAddress (SsssShopInfo ssi){
		
		try {
//"7a9beeb5f99fcb89217baf50bdc13394"
			String url = String.format("http://restapi.amap.com/v3/geocode/geo?key=%s&address=%s&city=%s", "8726e94e521325daaee5b4738d83bb56",ssi.getAddress(),ssi.getCity());
			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				// String data = "";
				String line = null;
				while ((line = br.readLine()) != null) {
					 System.out.println(line);
					AmapResultModel parseObject = null;
					parseObject = JSON.parseObject(line, AmapResultModel.class);
					// data += line;
					if (parseObject != null) {
						List<Postion> pois = parseObject.getGeocodes();
						if (pois != null) {
							for (Postion p : pois) {
								String location = p.getLocation();
//								System.out.println(location);
								if(location!=null){
									String[] split = location.split(",");
									ssi.setLatitude(split[0]);
									ssi.setLongitude(split[1]);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setLaLi (SsssShopInfo ssi){
		try{
			
			String url = String.format(
					"http://restapi.amap.com/v3/place/text?key=%s&keywords=%s&types=&city=&children=&offset=&page=&extensions=all",
					"8726e94e521325daaee5b4738d83bb56", ssi.getSsssName());

			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				// String data = "";
				String line = null;
				while ((line = br.readLine()) != null) {
//					 System.out.println(line);
					AmapResultModel parseObject = null;
					parseObject = JSON.parseObject(line, AmapResultModel.class);
					// data += line;
					if (parseObject != null) {
						List<Postion> pois = parseObject.getPois();
						if (pois != null) {
							for (Postion p : pois) {
								String location = p.getLocation();
								if(location!=null){
									String[] split = location.split(",");
									ssi.setLatitude(split[0]);
									ssi.setLongitude(split[1]);
								}
							//	ssi.setLatitude(p.get);
							}
						}
					}
				}
			}
		}catch(Exception e){}
	}
	
	public static String getDetailAddress(SsssShopInfo ssi) {
		try {
			String url = String.format(
					"http://restapi.amap.com/v3/place/text?key=%s&keywords=%s&types=&city=&children=&offset=&page=&extensions=all",
					"7a9beeb5f99fcb89217baf50bdc13394", ssi.getSsssName());

			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				// String data = "";
				String line = null;
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					AmapResultModel parseObject = null;
					parseObject = JSON.parseObject(line, AmapResultModel.class);
					// data += line;
					if (parseObject != null) {
						List<Postion> pois = parseObject.getPois();
						if (pois != null) {
							for (Postion p : pois) {
								ssi.setAddress(p.getAddress());
								ssi.setProvince(p.getPname());
								ssi.setCity(p.getCityname());
							}
						}
					}
				}
			}
		} catch (IOException e) {

		}
		return null;
	}

	public static double[] addressToGPS(String address) {
		try {

			String url = String.format("http://restapi.amap.com/v3/geocode/geo?&s=rsv3&address=%s&key=%s", address,
					KEY);
			URL myURL = null;
			URLConnection httpsConn = null;
			try {
				myURL = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			InputStreamReader insr = null;
			BufferedReader br = null;
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				String data = "";
				String line = null;
				while ((line = br.readLine()) != null) {
					data += line;
				}
				Matcher matcher = pattern.matcher(data);
				if (matcher.find() && matcher.groupCount() == 2) {
					double[] gps = new double[2];
					gps[0] = Double.valueOf(matcher.group(1));
					gps[1] = Double.valueOf(matcher.group(2));
					return gps;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 给经纬度赋值
	 * 
	 * @param ssssName
	 * @param ssi
	 * @return
	 */
	public SsssShopInfo setLatitudeLongitude(String ssssName, SsssShopInfo ssi) {
		switch (ssssName) {
		case "七台河市众和汽车销售服务有限责任公司":
			ssi.setLatitude("45.779500");
			ssi.setLongitude("130.93658300000004");
			break;
		case "嵊州市骏泰汽车销售服务有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "绍兴骏泰汽车销售服务有限公司":
			ssi.setLatitude("30.090755");
			ssi.setLongitude("120.51882599999999");
			break;
		case "江苏健泰汽车销售服务有限公司盐城分公司":
			ssi.setLatitude("33.326518");
			ssi.setLongitude("120.13431800000001");
			break;
		case "池州市贵池联运汽车维修有限公司":
			ssi.setLatitude("30.61862");
			ssi.setLongitude("117.53084999999999");
			break;
		case "安徽耀恒杰汽车销售服务有限公司":
			ssi.setLatitude("31.88543");
			ssi.setLongitude("117.33626900000002");
			break;
		case "济宁众泰汽车销售服务有限公司（李志斌）":
			ssi.setLatitude("35.427867");
			ssi.setLongitude("116.54587100000003");
			break;
		case "山东联业汽车销售服务有限公司":
			ssi.setLatitude("35.016779");
			ssi.setLongitude("118.26032900000001");
			break;
		case "巴中市众和汽车销售有限公司":
			ssi.setLatitude("31.859308");
			ssi.setLongitude("106.75594899999999");
			break;
		case "四川海天物流有限责任公司洋洋汽车修理厂":
			ssi.setLatitude("29.32867");
			ssi.setLongitude("104.71395999999999");
			break;
		case "遂宁市新纪元汽车销售服务有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "西藏众博汽车销售有限公司":
			ssi.setLatitude("29.64392");
			ssi.setLongitude("91.03379999999999");
			break;
		case "鄂尔多斯市恒融汽车销售有限责任公司":
			ssi.setLatitude("39.843402");
			ssi.setLongitude("111.23933899999997");
			break;
		case "武威一人一车汽车贸易有限公司":
			ssi.setLatitude("37.913822");
			ssi.setLongitude("102.63655");
			break;
		case "伊犁吉宏汽车维修有限公司":
			ssi.setLatitude("43.9667");
			ssi.setLongitude("81.25327299999998");
			break;
		case "阿克苏西北宏泰商贸有限责任公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "新疆佳琪汽车销售有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "陕西众力隆汽车贸易服务有限公司":
			ssi.setLatitude("36.503219");
			ssi.setLongitude("109.46356200000002");
			break;
		case "宝鸡市慧力商贸有限公司":
			ssi.setLatitude("34.337574");
			ssi.setLongitude("107.29074300000002");
			break;
		case "北京市清悦美商贸有限公司":
			ssi.setLatitude("40.094614");
			ssi.setLongitude("116.41794400000003");
			break;
		case "天津鹏峰宝润汽车贸易有限公司":
			ssi.setLatitude("39.159076");
			ssi.setLongitude("117.368965");
			break;
		case "山西英驰汽车贸易有限公司":
			ssi.setLatitude("39.347862");
			ssi.setLongitude("112.46608900000001");
			break;
		case "大同市泽录凯汽车销售服务有限公司":
			ssi.setLatitude("40.064980");
			ssi.setLongitude("113.407255");
			break;
		case "惠州市智坤实业有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "阳春市顺捷汽车维修服务有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "英德市创昇汽车销售有限公司":
			ssi.setLatitude("24.171765");
			ssi.setLongitude("113.44095600000003");
			break;
		case "潮州市帝豪汽车销售有限公司枫溪分公司":
			ssi.setLatitude("23.661955");
			ssi.setLongitude("116.612718");
			break;
		case "福建青顺汽车贸易有限公司莆田分公司":
			ssi.setLatitude("25.470154");
			ssi.setLongitude("119.07378299999999");
			break;
		case "福建恒越汽车销售服务有限公司":
			ssi.setLatitude("27.053684");
			ssi.setLongitude("118.36356599999999");
			break;
		case "新余市永泰汽车销售服务有限公司":
			ssi.setLatitude("27.805077");
			ssi.setLongitude("114.97047900000001");
			break;
		case "贵州众车之家汽车销售有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "黔南州恒泰汽车销售服务有限公司":
			ssi.setLatitude("34.292887");
			ssi.setLongitude("108.81456700000001");
			break;
		case "怀化福泽汽车销售服务有限公司":
			ssi.setLatitude("27.518793");
			ssi.setLongitude("110.00163900000001");
			break;
		case "益阳市腾达飞汽车维修有限公司":
			ssi.setLatitude(null);
			ssi.setLongitude(null);
			break;
		case "岳阳市国众汽车销售服务有限公司（原岳阳瑞捷汽车有限责任公司）":
			ssi.setLatitude("29.370444");
			ssi.setLongitude("113.17787800000002");
			break;
		case "武汉世纪天诚汽车销售服务有限公司":
			ssi.setLatitude("");
			ssi.setLongitude("");
			break;
		case "武汉中环华普商贸有限公司":
			ssi.setLatitude("30.425298");
			ssi.setLongitude("114.258537");
			break;
		case "武汉恒顺鑫隆汽车销售有限公司":
			ssi.setLatitude("30.52819");
			ssi.setLongitude("114.189794");
			break;
		case "随州市国鹏商贸有限公司":
			ssi.setLatitude("31.754376");
			ssi.setLongitude("113.37143900000001");
			break;
		default:
			System.out.println("请自行百度！");
		}
		return ssi;
	}

	public SsssShopInfo setAddress(String ssssName, SsssShopInfo ssi) {
		switch (ssssName) {
		case "沈阳庞大龙翔汽车销售有限公司":
			ssi.setAddress("");
			break;
		case "嵊州市骏泰汽车销售服务有限公司":
			ssi.setAddress("");
			break;
		case "诸暨市赛腾汽车销售有限公司":
			ssi.setAddress("");
			break;
		case "遂宁市新纪元汽车销售服务有限公司":
			ssi.setAddress("");
			break;
		case "新疆佳琪汽车销售有限公司":
			ssi.setAddress("");
			break;
		case "阿克苏西北宏泰商贸有限责任公司":
			ssi.setAddress("");
			break;
		case "惠州市美车世界维修服务部（原惠城区旭东汽车修配厂）":
			ssi.setAddress("");
			break;
		case "惠州市智坤实业有限公司":
			ssi.setAddress("");
			break;
		case "阳春市顺捷汽车维修服务有限公司":
			ssi.setAddress("");
			break;
		case "贵州众车之家汽车销售有限公司":
			ssi.setAddress("");
			break;
		case "黔南州恒泰汽车销售服务有限公司":
			ssi.setAddress("");
			break;
		case "张家界顺泰汽车服务有限公司":
			ssi.setAddress("");
			break;
		case "岳阳市国众汽车销售服务有限公司（原岳阳瑞捷汽车有限责任公司）":
			ssi.setAddress("岳阳市岳阳楼区八字门白石岭北路168号");
			break;
		}
		return ssi;
	}
}
