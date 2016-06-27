package com.sirun.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class BaseExcelUtil<T> {
	
	static{
		
	}
	
	/**
	 * 读取表格中的数据并转化为T的对象
	 * @return
	 */
	public T excel2Pojo(){
		return null;
	}
	
	/**
	 * @Description:导入表格数据
	 */
	public List<T> listFromSheet(Sheet sheet) throws ParseException {
		List<T> list = new ArrayList<T>();
//		T c;
		// 从数据行开始解析
		for (int r = sheet.getFirstRowNum() + 1 + 1 +1; r <= sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			Object[] cells = new Object[row.getLastCellNum()+1];
			//从单元格开始解析
			for (int i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				cells[i] = getValueFromCell(cell);
			}
			
//			c = new T();
//			c.setSsssName(cells[0].toString());
//			c.setPhone(cells[4].toString());
//			list.add(c);
		}
		
		return list;
	}
	
	/**
	 * @Description:获取单元格数据
	 */
	public static String getValueFromCell(Cell cell) {
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
	
}
