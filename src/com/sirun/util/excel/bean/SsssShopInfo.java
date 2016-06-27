package com.sirun.util.excel.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class SsssShopInfo implements Serializable {

    private static final long serialVersionUID = 8955601165774067764L;
    private Long ssssId;

    private String province;

    private String city;

    private String county;

    private String ssssName;

    private String address;

    private String phone;

    private String complaintPhone;

    private String recommend;

    private String remark;

    private String geoHash;

    private String latitude;

    private String longitude;

    private String vehicleBrand; //4s店支持的车型  不同车型   “，” 隔开
    
    public Long getSsssId() {
        return ssssId;
    }

    public void setSsssId(Long ssssId) {
        this.ssssId = ssssId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getSsssName() {
        return ssssName;
    }

    public void setSsssName(String ssssName) {
        this.ssssName = ssssName == null ? null : ssssName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getComplaintPhone() {
        return complaintPhone;
    }

    public void setComplaintPhone(String complaintPhone) {
        this.complaintPhone = complaintPhone == null ? null : complaintPhone.trim();
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend == null ? null : recommend.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash == null ? null : geoHash.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}