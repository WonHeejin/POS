package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import beans.DateBean;
import beans.MemberBean;
import beans.MenuBean;
import beans.OrderBean;

public class DataAccessObject {

	public DataAccessObject() {

	}
	boolean setOrders(ArrayList<OrderBean> list) {
		boolean check= false;
		File file = new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\orders.txt");
		FileWriter writer = null;
		BufferedWriter buffer = null;
		StringBuffer sb=new StringBuffer();
		try {
			writer = new FileWriter(file, true);
			buffer = new BufferedWriter(writer);

			for(int colIndex=0; colIndex<list.size(); colIndex++) {
				sb.append(list.get(colIndex).getOrderCode());
				sb.append(","+list.get(colIndex).getGoodsCode());
				sb.append(","+list.get(colIndex).getGoodsName());
				sb.append(","+list.get(colIndex).getGoodsPrice());
				sb.append(","+list.get(colIndex).getOrderQuantity());
				sb.append(","+list.get(colIndex).getDiscountRate());
				if(list.get(colIndex).getMemberCode()!=null){
					sb.append(","+list.get(colIndex).getMemberCode());
				}
				sb.append("\n");
			}
			buffer.write(sb.toString());
			check = true;
		}catch(IOException e) {

		} 
		finally {
			try {buffer.close();} catch (IOException e) {}
		}

		return check;
	}
	
	ArrayList<OrderBean> getSalesStat(DateBean month){
		ArrayList<OrderBean> salesMonthStat = new ArrayList<OrderBean>();
		
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\orders.txt")));
			String line;
			String[] order;
			OrderBean ob;
			while((line=buffer.readLine()) != null) {
				if(Integer.parseInt(month.getStartDay())<=Integer.parseInt(line.substring(0,8))&&Integer.parseInt(line.substring(0,8))<=Integer.parseInt(month.getLastDay())) {			
					order = line.split(",");
					ob=new OrderBean();
					ob.setOrderCode(order[0]);
					ob.setGoodsCode(order[1]);
					ob.setGoodsName((order[2]));
					ob.setGoodsPrice(Integer.parseInt(order[3]));
					ob.setOrderQuantity(Integer.parseInt(order[4]));
					ob.setDiscountRate(Integer.parseInt(order[5]));				
					salesMonthStat.add(ob);
					
				}
			}
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(IOException e2){
			e2.printStackTrace();
		}
		finally {
			try {buffer.close();} catch (IOException e) {}
		}
				
		return salesMonthStat;
	}
	
	ArrayList<OrderBean> getMonthStat(DateBean month){
		
		BufferedReader br=null;
		ArrayList<OrderBean> salesMonthStat= new ArrayList<OrderBean>();
		OrderBean ob;
		try {
			br= new BufferedReader(new FileReader("D:\\Class\\HEEJIN\\sample\\src\\datafile\\orders.txt"));
			String line;
			String[] order;
			while((line=br.readLine())!=null) {
				if(line.contains(month.getToday())) {
					order = line.split(",");
					ob=new OrderBean();
					ob.setOrderCode(order[0]);
					ob.setGoodsCode(order[1]);
					ob.setGoodsName((order[2]));
					ob.setGoodsPrice(Integer.parseInt(order[3]));
					ob.setOrderQuantity(Integer.parseInt(order[4]));
					ob.setDiscountRate(Integer.parseInt(order[5]));				
					salesMonthStat.add(ob);
				}				
			}
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
		return salesMonthStat;
	}
	OrderBean getGoodsInfo(OrderBean ob) {
		BufferedReader buffer=null;
		OrderBean goodsInfo=new OrderBean();
		try {
			buffer = new BufferedReader(new FileReader("D:\\Class\\HEEJIN\\sample\\src\\datafile\\goodsInfo.txt"));
			String line;
			while((line=buffer.readLine())!=null){
				String[] record=line.split("\\|");
				if(ob.getGoodsCode().equals(record[0])) {
					//상품코드 상품명 가격 수량 할인율
					goodsInfo.setGoodsCode(record[0]);
					goodsInfo.setGoodsName(record[1]);
					goodsInfo.setGoodsPrice(Integer.parseInt(record[2]));
					goodsInfo.setOrderQuantity(0);
					goodsInfo.setDiscountRate(Integer.parseInt(record[5]));
					break;}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(IOException e2) {
			e2.printStackTrace();
		}
		finally {
			try {buffer.close();
		} catch (IOException e) {}}
		return goodsInfo;
	}
	boolean setStoreState(String date, String time, int state) {
		boolean response = false;
		File file = 
				new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\storestate.txt");
		try {
			FileWriter writer = new FileWriter(file, true);
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write(date + "|" + time + "|" + state);
			buffer.newLine();
			buffer.close();
			response = true;
		} catch (IOException e) {

		}
		return response;
	}

	private int countRecord(String path) {
		int count = 0;
		File file = new File(path);
		try {
			FileReader reader = new FileReader(file);
			BufferedReader buffer = new BufferedReader(reader);
			//1001|아메리카노|2000|1|HOT|10
			while((buffer.readLine()) != null) {
				count++;
			}
			buffer.close();
		}catch(IOException e) {

		}
		return count;
	}
			//메뉴 수정, 삭제
	boolean setMenu(ArrayList<MenuBean> list) {
		boolean check = false;
		BufferedWriter buffer = null;
		StringBuffer sb=new StringBuffer();

		for(int colIndex=0; colIndex<list.size(); colIndex++) {
			sb.append(list.get(colIndex).getMenuCode());
			sb.append("|"+list.get(colIndex).getMenuName());
			sb.append("|"+list.get(colIndex).getMenuPrice());
			sb.append("|"+list.get(colIndex).getMenuState());
			sb.append("|"+list.get(colIndex).getMenuCat());
			sb.append("|"+list.get(colIndex).getMenuDiscount()+"\n");
		}
		
		try {			
			buffer = new BufferedWriter(new FileWriter(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\goodsInfo.txt")));			
			buffer.write(sb.toString());			
			check = true;
		}catch(IOException e) {

		} 
		finally {
			try {buffer.close();} catch (IOException e) {}
		}

		return check;
	}
	//메뉴 등록
	boolean setMenu(MenuBean menu) {
		boolean check = false;
		BufferedWriter buffer = null;
		StringBuffer sb=new StringBuffer();
		sb.append(menu.getMenuCode()+"|");
		sb.append(menu.getMenuName()+"|");
		sb.append(menu.getMenuPrice()+"|");
		sb.append(menu.getMenuState()+"|");
		sb.append(menu.getMenuCat()+"|");
		sb.append(menu.getMenuDiscount());
		try {			
			buffer = new BufferedWriter(new FileWriter(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\goodsInfo.txt"), true));						
			buffer.write(sb.toString());
			buffer.newLine();
			check = true;
		}catch(IOException e) {

		} 
		finally {
			try {buffer.close();} catch (IOException e) {}
		}

		return check;
	}

	ArrayList<MenuBean> getMenu(){
		ArrayList menuList= new ArrayList<MenuBean>();
		String menu = null;
		MenuBean menuInfo = null;
		String[] menuRead;	
		
		try {			
			BufferedReader buffer = new BufferedReader(new FileReader("D:\\Class\\HEEJIN\\sample\\src\\datafile\\goodsInfo.txt"));
			//1001|아메리카노|2000|1|HOT|10
			while((menu = buffer.readLine()) != null) {	
				menuInfo=new MenuBean();
				
				menuRead = menu.split("\\|");
				menuInfo.setMenuCode(menuRead[0]);
				menuInfo.setMenuName(menuRead[1]);
				menuInfo.setMenuPrice(Integer.parseInt(menuRead[2]));
				menuInfo.setMenuState(menuRead[3].charAt(0));
				menuInfo.setMenuCat(menuRead[4]);
				menuInfo.setMenuDiscount(Integer.parseInt(menuRead[5]));
				
				menuList.add(menuInfo);
			}
			buffer.close();
		}catch (IOException e) {
			menuList = null;	
		}

		return menuList;
	}

	public ArrayList<MemberBean> getMemberList(){
		/* Service Class 요청에 따른 회원 리스트를 이차원 배열로 작성 후 리턴 */
		ArrayList<MemberBean> memberList=new ArrayList<MemberBean>();
		BufferedReader buffer = null;
		String line;
		MemberBean mb=null;
		try {
			
			buffer = new BufferedReader(new FileReader(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\members.txt")));
			while((line = buffer.readLine()) != null) {
				mb= new MemberBean();
				String[] member = line.split("\\|");
				mb.setMemberCode(member[0]);
				mb.setMemberName(member[1]);
				mb.setCallNumber(member[2]);
				memberList.add(mb);
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {buffer.close();} catch (IOException e) {}
		}
		
		return memberList;
	}
	//멤버 등록
	public boolean setMember(MemberBean member){
		boolean check = false;	
		BufferedWriter buffer = null;
		StringBuffer sb= new StringBuffer();
		sb.append(member.getMemberCode());
		sb.append("|"+member.getMemberName());
		sb.append("|"+member.getCallNumber()+"\n");
		
		try {			
			buffer = new BufferedWriter(new FileWriter(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\members.txt"), true));

			
			buffer.write(sb.toString());
			check = true;
		}catch(IOException e) {

		} 
		finally {
			try {buffer.close();} catch (IOException e) {}
		}

		return check;
	}
	//멤버 수정, 삭제
	public boolean setMember(ArrayList<MemberBean> memberInfo){
		boolean check = false;
		BufferedWriter buffer = null;
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<memberInfo.size();i++) {
			sb.append(memberInfo.get(i).getMemberCode());
			sb.append("|"+memberInfo.get(i).getMemberName());
			sb.append("|"+memberInfo.get(i).getCallNumber()+"\n");		
		}
		try {
			
			buffer = new BufferedWriter(new FileWriter(new File("D:\\Class\\HEEJIN\\sample\\src\\datafile\\members.txt")));
		
			buffer.write(sb.toString());
			check = true;
		}catch(IOException e) {

		} 
		finally {
			try {buffer.close();} catch (IOException e) {}
		}

		return check;
	}

}

/* File >> 사용할 파일의 경로와 이름 지정
 * FileReader, FileWriter
 * BufferedReader, BufferedWriter
 * */
