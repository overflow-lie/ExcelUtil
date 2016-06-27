package com.sirun.test;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sirun.util.excel.bean.SsssShopInfo;
import com.sirun.util.excel.dao.SsssShopInfoMapper;

/**
 * 用于临时批量插入4s店信息
 * @author jiaxin
 *
 */
public class Mt_ssss_shop_infoTempTest {
	
	//车型列表
	private List<String> vehicleBrands;
	
	//4s店列表
	List<SsssShopInfo> ssssShoplist; 
	
	private SqlSession sqlSession;
	
	private SsssShopInfoMapper ssssShopInfoMapper;
	
	@Before
	public void before(){
		
		String resource = "tempMyiBatisConfiguration.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		sqlSession = sqlMapper.openSession();
		
		ssssShopInfoMapper = sqlSession.getMapper(SsssShopInfoMapper.class);
		
		vehicleBrands = new ArrayList<>();
//		vehicleBrands.add("A02");
		vehicleBrands.add("B01");
		vehicleBrands.add("S21");
		vehicleBrands.add("T22");
		vehicleBrands.add("B11A");
		vehicleBrands.add("B11");
		vehicleBrands.add("B11B");
		vehicleBrands.add("B12");
		vehicleBrands.add("B15");
		vehicleBrands.add("B15H");
		vehicleBrands.add("B17");
		vehicleBrands.add("B40");
		vehicleBrands.add("M12");
		vehicleBrands.add("A02E");
		vehicleBrands.add("S21E");
		vehicleBrands.add("E01");
		
		//查询出已有数据库的4s店信息
		ssssShoplist = ssssShopInfoMapper.selectAllSsssShopInfo();
	}
	
	@After
	public void afterTest(){
		sqlSession.close();
	}
	
	@Test
	public void tempInsertBatchByVehicle(){
		
		for (SsssShopInfo ssssShopInfo : ssssShoplist) {
			//为每一个4s店赋予车型信息形成多条信息 ，并批量插入到数据库中
			int insertBatchByVehicle = ssssShopInfoMapper.insertBatchByVehicle(ssssShopInfo,vehicleBrands);
			if(insertBatchByVehicle==0){
				sqlSession.rollback();
				System.out.println("回滚");
				return;
			}
		}
		System.out.println("提交");
		sqlSession.commit();
	}
	
}
