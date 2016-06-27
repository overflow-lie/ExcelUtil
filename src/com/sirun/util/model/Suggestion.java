package com.sirun.util.model;

import java.util.List;

/**
 * 
 * 高德数据返回类型
 * @author jiaxin
 *
 */
public class Suggestion {
	
	private List<String> keywords;
	
	private List<String> cities;
	

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

}
