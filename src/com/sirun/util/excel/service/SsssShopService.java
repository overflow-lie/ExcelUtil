package com.sirun.util.excel.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sirun.util.excel.bean.SsssShopInfo;

/**
 */
public interface SsssShopService {
	/**
	 * 导入表格数据到SsssShopInfo
	 * @param importExcel
	 * @author jiaxin
	 * @param request
	 */
	public List<SsssShopInfo> importExcel(MultipartFile importExcel); 
	
	public void insertSsssShopInfo(List<SsssShopInfo> newSsssNameList);
	
	/**
	 * 根据车型列表批量插入4s店信息
	 * @param vehicleBrands
	 */
	public void insertBatchByVehicle(List<String> vehicleBrands);
}
