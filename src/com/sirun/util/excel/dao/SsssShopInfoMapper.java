package com.sirun.util.excel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sirun.util.excel.bean.SsssShopInfo;

public interface SsssShopInfoMapper {

	int updatePhoneBySsssName(SsssShopInfo ssi);

	int insertBatch(@Param("ssiList")List<SsssShopInfo> ssiList);

	List<SsssShopInfo> selectAllSsssShopInfo();

	int insertBatchByVehicle(@Param("ssssShopInfo") SsssShopInfo ssssShopInfo,@Param("vehicleBrandList") List<String> vehicleBrands);

}