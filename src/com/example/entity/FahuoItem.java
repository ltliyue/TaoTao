package com.example.entity;

/**
 * 封装在售商品的类
 * @author Song Shi Chao
 *
 */
public class FahuoItem {
	// num_iid,title,type,pic_url,num,props,valid_thru,price,delist_time
	//tid,seller_nick,buyer_nick,delivery_start,delivery_end,out_sid,item_title,
	// receiver_name,created,modified,status,type,freight_payer,seller_confirm,company_name
	String item_title;
	String freight_payer;
	String buyer_nick;
	String created;
	String receiver_name;
	String seller_confirm;
	String status;
	String seller_nick;
	String modified;
	String tid;
	String type;
	
	public FahuoItem(){
		
	}
	
	public String getItem_title() {
		return item_title;
	}

	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}

	public String getFreight_payer() {
		return freight_payer;
	}

	public void setFreight_payer(String freight_payer) {
		this.freight_payer = freight_payer;
	}

	public String getBuyer_nick() {
		return buyer_nick;
	}

	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getSeller_confirm() {
		return seller_confirm;
	}

	public void setSeller_confirm(String seller_confirm) {
		this.seller_confirm = seller_confirm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeller_nick() {
		return seller_nick;
	}

	public void setSeller_nick(String seller_nick) {
		this.seller_nick = seller_nick;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	 
}
