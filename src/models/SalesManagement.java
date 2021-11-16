package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesManagement {
	 DataAccessObject dao;
	public SalesManagement() {}
	
	public String backController(String jobCode, String[] data) {
		String message=null;
		switch(jobCode) {
		case "4S":
			message=this.ctlSales(data);
			break;
		case "4D":
			message=this.ctlOrder(data);
			break;	
		}
		return message;
	}
	private String now() {
		Date d= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(d);		 
	}
	/* 주문 처리*/
	private String ctlOrder(String[] order) {	
		dao=new DataAccessObject();
		String date= now();
		for(int recordIndex=0;recordIndex<order.length;recordIndex++) {
			order[recordIndex]=date+","+order[recordIndex];
		}
		return (dao.setOrders(order))?"주문이 완료되었습니다.":"주문에 실패하였습니다.";				 
	}
	/* 판매개시 */
	private String ctlSales(String[] goodsCode) {
		dao= new DataAccessObject(); 
		String goodsInfo;
		String[] rawData=dao.getGoodsInfo(goodsCode[0]);
		goodsInfo=rawData[0]+ ","+rawData[1]+","+rawData[2]+","+"0"+","+rawData[5];
		
		 return goodsInfo;		
	}
}
