package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import beans.OrderBean;

public class SalesManagement {
	 DataAccessObject dao;
	public SalesManagement() {}
	
	public String backController(String request) {
		ArrayList orders= new ArrayList();
		String message=null;
		String jobCode=request.substring(0,request.indexOf("?"));
		/* Client Data 추출 
		 * jobCode 4S : String 상품코드
		 *         20211115160051,1001,아메리카노,2000,1,10,1001
		 * jobCode 4D : String 상품코드  int 수량   String 회원코드
		 *            + String 날짜  String 상품명    int 가격   int 할인율  
		 * */
		/*  4S?1001&1 */
		String[] data=request.substring(request.indexOf("?")+1).split("&");
		if(data.length==1) {
			/*4S?1001*/
			OrderBean ob= new OrderBean();
			ob.setGoodsCode(data[0]);
			orders.add(ob);
		}else{
			/*4S?1001&1&1003&2*/
	
			/*4S?1001&1&1001*/
			/*4S?1001&1&1003&2&1004*/
			String memberCode=data[data.length-1];
			for(int i=0; i<data.length-1; i+=2) {
				OrderBean ob= new OrderBean();
				ob.setGoodsCode(data[i]);
				ob.setOrderQuantity(Integer.parseInt(data[i+1]));
				if(data.length%2==1) {
					ob.setMemberCode(memberCode);
				}
				orders.add(ob);
			}
		}
		switch(jobCode) {
		case "4S":
			message=this.ctlSales(orders);
			break;
		case "4D":
			message=this.ctlOrder(orders);
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
	private String ctlOrder(ArrayList orders) {	
		dao=new DataAccessObject();
		String date= now();
		for(int recordIndex=0;recordIndex<orders.size();recordIndex++) {
			((OrderBean)orders.get(recordIndex)).setOrderCode(date);
		}
		String[] daoOrders= new String[orders.size()];

		
		for(int i=0;i<daoOrders.length;i++) {
			daoOrders[i]=((OrderBean)orders.get(i)).getOrderCode()+
			","+((OrderBean)orders.get(i)).getGoodsCode()+
			","+((OrderBean)orders.get(i)).getOrderQuantity()+
			(((OrderBean)orders.get(i)).getMemberCode()!=null?","+((OrderBean)orders.get(i)).getMemberCode():"");
		}
		return (dao.setOrders(daoOrders))?"주문이 완료되었습니다.":"주문에 실패하였습니다.";				 
	}
	/* 판매개시 */
	private String ctlSales(ArrayList orders) {
		dao= new DataAccessObject(); 
		String goodsInfo;
		String[] rawData=dao.getGoodsInfo(((OrderBean)orders.get(0)).getGoodsCode());
		goodsInfo=rawData[0]+ ","+rawData[1]+","+rawData[2]+","+"0"+","+rawData[5];
		 return goodsInfo;		
	}
}
