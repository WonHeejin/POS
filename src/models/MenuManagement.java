package models;

public class MenuManagement {
	private DataAccessObject dao;

	public MenuManagement() {

	}

	public String backController(String jobCode) {
		String message = null;
		switch(jobCode) {
		case "21": case "22": case "23":
			message = this.ctlReadMenu();
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

	public String backController(String jobCode, String[] data) {
		String message = null;
		
		switch(jobCode) {
		case "2R":
			message = this.ctlRegMenu(data);
			break;
		case "2M":
			message = this.ctlModMenu(data);
			break;
		case "2D":
			message = this.ctlDelMenu(data);
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
	private String toStringFromArray(String[][] menuList) {
		StringBuffer sb = new StringBuffer();
		
		for(int recordIndex=0; recordIndex<menuList.length; recordIndex++) {
			sb.append(" ");
			for(int colIndex=0; colIndex<menuList[recordIndex].length; colIndex++) {
				if(colIndex == 3) {
					sb.append(menuList[recordIndex][colIndex].equals("1")? "가능": "불가");
				}else {
					sb.append(menuList[recordIndex][colIndex]);
				}

				if(colIndex != menuList[recordIndex].length - 1) {
					sb.append("\t");
					if(colIndex == 1 && menuList[recordIndex][colIndex].length()<6) {
						sb.append("\t");
					}
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/* 메뉴등록 */
	private String ctlRegMenu(String[] menuData) {
		String menuList = null;
		dao = new DataAccessObject();
		// DAO에 메뉴등록 요청
		if(dao.setMenu(menuData)) {
			// DAO에 등록된 메뉴 읽기 요청
			menuList = this.toStringFromArray(dao.getMenu());
		}else {
			menuList = "메뉴등록작업이 실패하였습니다. 다시 한번 입력해주세요";
		}
		
		// 등록메뉴를 리턴
		return menuList;
	}
	
	/* 메뉴수정 */
	private String ctlModMenu(String[] data) {
		dao = new DataAccessObject();
		String[][] list=dao.getMenu();
		for(int i=0;i<list.length;i++) {
			if(list[i][0].equals(data[0])){
				list[i][2]=data[1];
				list[i][5]=data[2];
				break;
			}
		}
		
		 return (dao.setMenu(list))?this.toStringFromArray(dao.getMenu()):"메뉴수정에 실패하였습니다. 다시 입력해주세요.";
	}

	/* 메뉴삭제 */
	private String ctlDelMenu(String[] data) {
		dao = new DataAccessObject();
		String[][] list=dao.getMenu();
		String[][] newList= new String [list.length-1][list[0].length];
		boolean check=true;
		for(int i=0;i<list.length;i++) {
			if(!list[i][0].equals(data[0])) {
//				if(check) {
//					newList[i]=list[i];
//				}else {
//					newList[i-1]=list[i];
//				}
				newList[(check)?i:i-1]=list[i];
			}else {check=false;}
		}	
		 return (dao.setMenu(newList))?this.toStringFromArray(dao.getMenu()):"메뉴삭제에 실패했습니다. 다시 입력해주세요.";
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
