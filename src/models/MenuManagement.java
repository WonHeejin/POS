package models;

import java.util.ArrayList;

import beans.MenuBean;

public class MenuManagement {
	private DataAccessObject dao;

	public MenuManagement() {

	}

	public String backController(String request) {
		String message = null;
		String jobCode;
		String[] data;
		MenuBean menu=null;
		if(request.indexOf('?')!=-1) {
			jobCode=request.substring(0, request.indexOf('?'));
			data=request.substring(request.indexOf('?')+1).split("&");
			if(data.length>0) {
				menu=new MenuBean();
				menu.setMenuCode(data[0]);
				if(jobCode.equals("2R")) {
					menu.setMenuName(data[1]);
					menu.setMenuPrice(Integer.parseInt(data[2]));
					menu.setMenuState(data[3].charAt(0));
					menu.setMenuCat(data[4]);
					menu.setMenuDiscount(Integer.parseInt(data[5]));
				}else if(jobCode.equals("2M")) {
					menu.setMenuPrice(Integer.parseInt(data[1]));
					menu.setMenuDiscount(Integer.parseInt(data[2]));
				}
			}						
		}else {
			jobCode=request;
		}
		
		switch(jobCode) {
		case "21": case "22": case "23":
			message = this.ctlReadMenu();
			break;
		case "2R":
			message = this.ctlRegMenu(menu);
			break;
		case "2M":
			message = this.ctlModMenu(menu);
			break;
		case "2D":
			message = this.ctlDelMenu(menu);
			break;
		case "24":
			this.ctlRegGoods();
			break;
		case "25":
			this.ctlModGoods();
			break;
		case "26":
			this.ctlDelGoods();
			break;
		}
		return message;
	}

	
	
	
	/* 메뉴읽기 */
	private String ctlReadMenu() {
		String menuList = null;
		dao = new DataAccessObject();
		menuList=this.toStringFromArray(dao.getMenu());	
		return menuList;
	}
	
	/* 2차원 배열 --> String */
	private String toStringFromArray(ArrayList<MenuBean> menulist) {
		StringBuffer sb = new StringBuffer();		
		for(int recordIndex=0; recordIndex<menulist.size(); recordIndex++) {
			sb.append(" ");
			sb.append(menulist.get(recordIndex).getMenuCode());
			sb.append("\t");
			sb.append(menulist.get(recordIndex).getMenuName());
			sb.append(menulist.get(recordIndex).getMenuName().length()<6?"\t\t":"\t");
			sb.append(menulist.get(recordIndex).getMenuPrice());
			sb.append("\t");
			sb.append(menulist.get(recordIndex).getMenuState()=='1'?"판매가능":"판매불가");
			sb.append("\t");
			sb.append(menulist.get(recordIndex).getMenuCat());
			sb.append("\t");
			sb.append(menulist.get(recordIndex).getMenuDiscount());
			sb.append("\n");
		}
		return sb.toString();
	}

		
	
	/* 메뉴등록 */
	private String ctlRegMenu(MenuBean menu) {
		String menuList = null;
		dao = new DataAccessObject();
		// DAO에 메뉴등록 요청
		if(dao.setMenu(menu)) {
			// DAO에 등록된 메뉴 읽기 요청
			menuList = this.toStringFromArray(dao.getMenu());
		}else {
			menuList = "메뉴등록작업이 실패하였습니다. 다시 한번 입력해주세요";
		}
		
		// 등록메뉴를 리턴
		return menuList;
	}
	
	/* 메뉴수정 */
	private String ctlModMenu(MenuBean menu) {
		dao = new DataAccessObject();
		ArrayList<MenuBean> list=dao.getMenu();
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getMenuCode().equals(menu.getMenuCode())){
				list.get(i).setMenuPrice(menu.getMenuPrice());
				list.get(i).setMenuDiscount(menu.getMenuDiscount());
				
				break;
			}
		}
		
		 return (dao.setMenu(list))?this.toStringFromArray(dao.getMenu()):"메뉴수정에 실패하였습니다. 다시 입력해주세요.";
	}

	/* 메뉴삭제 */
	private String ctlDelMenu(MenuBean menu) {
		dao = new DataAccessObject();
		ArrayList<MenuBean> list=dao.getMenu();
		for(int i=0; i<list.size();i++) {
			if(list.get(i).getMenuCode().equals(menu.getMenuCode())) {
				list.remove(i);
			}
		}
		 return (dao.setMenu(list))?this.toStringFromArray(dao.getMenu()):"메뉴삭제에 실패했습니다. 다시 입력해주세요.";
	}

	/* 굿즈등록 */
	private void ctlRegGoods() {

	}

	/* 굿즈정보수정 */
	private void ctlModGoods() {

	}

	/* 굿즈 삭제 */
	private void ctlDelGoods() {

	}
}
