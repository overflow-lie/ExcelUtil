package com.sirun.util.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 高德返回数据模型
 * http://restapi.amap.com/v3/place/text?key=您的key&keywords=南京道谛汽车销售有限公司&types=&city=&children=&offset=&page=&extensions=all
 * @author jiaxin
 *
 */
public class AmapResultModel {
	
	private String status;
	private int count;
	private String info;
	private int infocode;
	private String suggestion;
	private List<String> pois;
	private List<String> geocodes;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getInfocode() {
		return infocode;
	}
	public void setInfocode(int infocode) {
		this.infocode = infocode;
	}
	public Suggestion getSuggestion() {
		return JSON.parseObject(suggestion, Suggestion.class);
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public List<Postion> getPois() {
		List<Postion> postionList = new ArrayList<>();
		if(pois!=null){
			for (String string : pois) {
				Postion parseObject = JSON.parseObject(string, Postion.class);
				postionList.add(parseObject);
			}
		}
		return postionList;
	}

	public void setPois(List<String> pois) {
		this.pois = pois;
	}
	public List<Postion> getGeocodes() {
		List<Postion> postionList = new ArrayList<>();
		if(geocodes!=null){
			for (String string : geocodes) {
				Postion parseObject = JSON.parseObject(string, Postion.class);
				postionList.add(parseObject);
			}
		}
		return postionList;
	}
	public void setGeocodes(List<String> geocodes) {
		this.geocodes = geocodes;
	}
	
	
}
