package com.sirun.util.excel.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sirun.util.excel.bean.SsssShopInfo;
import com.sirun.util.excel.dao.SsssShopInfoMapper;
import com.sirun.util.excel.service.SsssShopService;

@Service("ssssShopService")
public class SsssShopServiceImpl implements SsssShopService {
//	private static final Logger logger = LoggerFactory.getLogger(SsssShopServiceImpl.class);
	@Autowired
	private SsssShopInfoMapper ssssShopInfoMapper;

	@Transactional(rollbackFor = Exception.class)
	public List<SsssShopInfo> importExcel(InputStream importStream) {

		List<SsssShopInfo> newList = new ArrayList<>();
		String resource = "tempMyiBatisConfiguration.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		SqlSession sqlSession = sqlMapper.openSession();
		// List<City> exitsCity = getModelList(null);
		// CommonsMultipartFile file = (CommonsMultipartFile) importExcel;
		InputStream is = null;
		// try {
		// is = file.getInputStream();
		is = importStream;
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// String fileType = file.getName();
		Workbook wb = null;
		// 判断类型
		// if (fileType.indexOf("xlsx") > 1) {
		// try {
		// wb = new XSSFWorkbook(is);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// } else if (fileType.indexOf("xls") > 1) {
		try {
			wb = new HSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
		Sheet sheet = wb.getSheetAt(0);
		List<SsssShopInfo> list = null;
		try {
			list = listFromSheet(sheet);
			// 校验数据的合法性；并将错误信息放入ResultInfo，ResultInfo维护这一个data
			// Map<String,Object> msg = checkedList(list);
			// resultInfo.setData(msg);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int errorNum = 0;
		for (SsssShopInfo ssssShopInfo : list) {
			System.out.println(ssssShopInfo);
			SsssShopInfo ssi = sssssShopInfoExits("exitsPhone", ssssShopInfo);
			if (ssi != null) {
				ssssShopInfoMapper = sqlSession.getMapper(SsssShopInfoMapper.class);

				int updatePhoneBySsssName = ssssShopInfoMapper.updatePhoneBySsssName(ssi);

				if (updatePhoneBySsssName == 0) {
					errorNum++;
					System.out.println("=======================" + ssssShopInfo.getSsssName() + "Phone信息修改失败！");
					System.out.println("错误数为：" + errorNum);
					newList.add(ssi);
				}

			} else {

			}
		}
		// cityMapper.mutiInsert(list);
		// for (City c : list)
		//
		// //判断当前城市是否存在如果存在则更新
		// City temp = cityExits(exitsCity,c);
		// if(temp != null){
		// c.setId(temp.getId());
		// cityMapper.updateByPK(c);
		// }else{
		// cityMapper.insertWithPK(c);
		// }

		sqlSession.commit();
		return newList;
	}

	private SsssShopInfo sssssShopInfoExits(String string, SsssShopInfo ssssShopInfo) {
		switch (string) {
		case "exitsPhone":
			return exitsPhone(ssssShopInfo);
		}
		return null;
	}

	private SsssShopInfo exitsPhone(SsssShopInfo ssssShopInfo) {

		String phoneStr = ssssShopInfo.getPhone();

		phoneStr.replace("\n", "").replace(" ", "").replace("\t", "");
		ssssShopInfo.setPhone(phoneStr);
		// 通过正则 去掉可能存在的中文字符
		// String reg = "[\u4e00-\u9fa5]";
		// Pattern pat = Pattern.compile(reg);
		// Matcher mat=pat.matcher(phoneStr);
		// String phones = mat.replaceAll("");

		// phones.split("");
		//
		// int lastIndexOf = phones.lastIndexOf("-");
		// int firstIndexOf = phones.indexOf("-");
		// if(lastIndexOf == -1){
		// String phone = phones.replace(" ", "").substring(0,10);
		// ssssShopInfo.setPhone(phone);
		// }
		// if(lastIndexOf == firstIndexOf){
		// phone.substring(firstIndexOf-4,firstIndexOf+);
		// reg = "^($$\d{3,4}-)|\d{3.4}-)?\d{7,8}$" ;

		// }
		return ssssShopInfo;
	}

	/*
	 * private Map<String, Object> checkedList(List<SsssShopInfo> list) {
	 * 
	 * return null; }
	 */

	/**
	 * @Description:导入表格数据
	 */
	public List<SsssShopInfo> listFromSheet(Sheet sheet) throws ParseException {
		List<SsssShopInfo> list = new ArrayList<SsssShopInfo>();
		SsssShopInfo c;
		long l = 4;
		// 从数据行开始解析
		for (int r = sheet.getFirstRowNum() + 1 + 1 + 1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			Object[] cells = new Object[row.getLastCellNum() + 1];
			// 从单元格开始解析
			for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				cells[i] = getValueFromCell(cell);
			}

			c = new SsssShopInfo();
			c.setSsssId(l++);
			c.setSsssName(cells[0].toString());
			c.setProvince(cells[1].toString());
			c.setCity(cells[2].toString());
			/**/
			c.setAddress(cells[3].toString());
			/**/
			c.setPhone(cells[4].toString());
			c.setVehicleBrand("");
			list.add(c);
		}
		return list;
	}

	/**
	 * @Description:获取单元格数据
	 */
	private String getValueFromCell(Cell cell) {
		if (cell == null) {
			return "";
		}
		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (type == Cell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("#");
			return String.valueOf(df.format(cell.getNumericCellValue()));
		} else if (type == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(cell.getCellFormula());
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}

	@Override
	public List<SsssShopInfo> importExcel(MultipartFile importExcel) {
		return null;
	}
	
	@Override
	public void insertSsssShopInfo(List<SsssShopInfo> ssiList) {

		ssssShopInfoMapper.insertBatch(ssiList);
	}

	@Override
	public void insertBatchByVehicle(List<String> vehicleBrands) {
		List<SsssShopInfo> ssssShoplist = ssssShopInfoMapper.selectAllSsssShopInfo();
		
		for (SsssShopInfo ssssShopInfo : ssssShoplist) {
			
			ssssShopInfoMapper.insertBatchByVehicle(ssssShopInfo,vehicleBrands);
			
		}
		
	}

	public List<SsssShopInfo> importExcel2(InputStream importStream) {
		InputStream is = null;
		is = importStream;
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
		Sheet sheet = wb.getSheetAt(0);
		List<SsssShopInfo> list = null;
		try {
			list = listFromSheet(sheet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}




}
