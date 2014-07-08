package com.example.entity;

/**
 * 封装在售商品的类
 * @author Song Shi Chao
 *
 */
public class CangkuItem {
	// num_iid,title,type,pic_url,num,props,valid_thru,price,delist_time
	
	String num_iid;
	String title;
	String type;
	String pic_url;
	String num;
	String props;
	String valid_thru;
	String price;
	String delist_time;
	
	public CangkuItem(){
		
	}
	
	public String getNum_iid() {
		return num_iid;
	}
	public void setNum_iid(String num_iid) {
		this.num_iid = num_iid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}
	public String getValid_thru() {
		return valid_thru;
	}
	public void setValid_thru(String valid_thru) {
		this.valid_thru = valid_thru;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDelist_time() {
		return delist_time;
	}
	public void setDelist_time(String delist_time) {
		this.delist_time = delist_time;
	}
	 
}
