package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import beans.DateBean;
import beans.OrderBean;

public class StoreManagement {
	private Date d;
	private DataAccessObject dao;
	private DateBean date;
	
	public StoreManagement() {
		// 1. 현재 시스템 날짜와 시간을 취득  yyyyMMddHHmmss
		d = new Date();
		dao = new DataAccessObject();
	}

	public String backController(String request) {
		String message = null;
		String jobCode=null;
		String[] data=null;
		if(request.indexOf('?')!=-1) {
			jobCode=request.substring(0,request.indexOf('?'));
			data=request.substring(request.indexOf('?')+1).split("&");
		}else {
			jobCode=request;
		}
		switch(jobCode) {
		case "11":
			message = this.ctlStoreOpen();
			break;
		case "12":
			message=this.ctlStoreClose();
			break;
		case "13":			
			message = this.getMonthStat(data);			
			break;
		case "14":			
			message = this.getSelectedStat(data);			
			break;	
		}
		return message;
	}
	
	
	
	/*jobCode :: 14 >> 해당범위 매출현황*/
	private String getSelectedStat(String[] data) {	
		ArrayList<OrderBean> salesStat = null;
		date= new DateBean();
		date.setStartDay(data[0]);
		date.setLastDay(data[1]);
		dao = new DataAccessObject();
		
		salesStat = dao.getSalesStat(date);
		/* 해당월   건수    매출액    할인적용매출액 
		 * */
		StringBuffer buffer = new StringBuffer();
		buffer.append(" "+date.getStartDay()+"~"+date.getLastDay());
		buffer.append("\t");
		buffer.append(salesStat.size());
		buffer.append("\t");
		buffer.append(this.salesAmount(salesStat));
		buffer.append("\t");
		buffer.append(this.salesDiscountAmount(salesStat));
		return buffer.toString();
	}
	/*jobCode :: 13  >> 금월통계 */
	private String getMonthStat(String[] data) {
		date= new DateBean();
		date.setToday(data[0]);
		ArrayList<OrderBean> salesMonthStat=dao.getMonthStat(date);
		
		/* 해당월   건수    매출액    할인적용매출액 */
		StringBuffer buffer= new StringBuffer();
		buffer.append(" ");
		buffer.append(date.getToday());
		buffer.append("\t");
		buffer.append(salesMonthStat.size());
		buffer.append("\t");
		buffer.append(this.salesAmount(salesMonthStat));
		buffer.append("\t");
		buffer.append(this.salesDiscountAmount(salesMonthStat));
		return buffer.toString();	
	}
	/*매출액 메소드*/
	private int salesAmount(ArrayList<OrderBean> data) {
		int sum=0;
		// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
		for(int i=0; i<data.size(); i++) {
			sum+=data.get(i).getGoodsPrice()*data.get(i).getOrderQuantity();
		}
		return sum;
	}
	/*할인매출액 메소드*/
	private int salesDiscountAmount(ArrayList<OrderBean> data) {
		int sum=0;
		// 202109060918,1005,카푸치노(HOT),2700,8,0,1002
		for(int i=0; i<data.size(); i++) {
			sum+=data.get(i).getGoodsPrice()*data.get(i).getOrderQuantity()*((100-data.get(i).getDiscountRate())/100.0);
		}
		return sum;
	}
	/*jobCode :: 11  >> 매장오픈 처리 */
	private String ctlStoreOpen() {
		boolean response;
		//  1-1. yyyyMMdd & HHmmss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 2. 파일 저장
		//  2-1. 파일 접근 클래스 호출 :: DataAccessObject.class
		// 3. 매장오픈이 처리되었는지 안되었는지 응답 받기  true ::오픈성공 false:오픈실패
		response = dao.setStoreState(sdf.format(d).substring(0, 8), 
				sdf.format(d).substring(8), 1);
		 
		// 4. View에 전달에 메세지 리턴
		return (response)? "매장이 오픈되었습니다." : "매장 오픈이 실패하였습니다.";
	}

	/*jobCode :: 12  >> 매장클로즈 처리 */
	private String ctlStoreClose() {
		boolean response;
		//  1-1. yyyyMMdd & HHmmss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 2. 파일 저장
		//  2-1. 파일 접근 클래스 호출 :: DataAccessObject.class
		// 3. 매장오픈이 처리되었는지 안되었는지 응답 받기  true ::오픈성공 false:오픈실패
		response = dao.setStoreState(sdf.format(d).substring(0, 8), 
				sdf.format(d).substring(8), 1);
		 
		// 4. View에 전달에 메세지 리턴
		return (response)? "매장이 클로즈되었습니다." : "매장 클로즈에 실패하였습니다.";
	}

	/*jobCode :: 13  >> 금일 매출 현황 처리 */
	private void ctlTodaySales() {

	}

	/*jobCode :: 14  >> 매출 통계 처리 */
	private void ctlSalesAnalisis(){

	}
}
