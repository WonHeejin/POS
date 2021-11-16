package views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import controllers.FrontController;

public class View {

	public View() {
		this.ctlView();
	}

	/* 데이터의 흐름 제어 */
	private void ctlView() {
		FrontController fc = null;
		String title, message = "", jobCode = "";
		int menuCode = -1, subCode;
		String[][] menu = saveMenu();
		title = this.makeTitle();
		String[] userData;
		fc = new FrontController();
		//String message2 = "";
		while (true) {

			if (jobCode.equals("")) {
				if (menuCode != 4) {
					menuCode = this.ctlMain(title, message, menu);
					message="";
				}
				if (menuCode == 0) {
					break;
				} else {
					message=(menuCode==4)?message:"";
				}
				if (menuCode != 4) {
					subCode = this.ctlSub(title, message, menu[menuCode - 1]);
					if (subCode != 0) {
						jobCode = menuCode + "" + subCode;
						message = "";
						/* 서버 서비스 요청 */
						if(jobCode.equals("13")) {
							String[] userDate={this.getDate("yyyyMM")};
							message=fc.getRequest(jobCode, userDate);

						}else if(jobCode.equals("14")){
							this.display("시작일입력(yyyyMMdd) : ");
							String m1 = this.menuInput();
							this.display("마감일입력(yyyyMMdd) : ");
							String m2 = this.menuInput();
							String[] userDate={m1,m2};
							message=fc.getRequest(jobCode, userDate);
						}else {
							message = fc.getRequest(jobCode);
						}
					} else {
						message = "요청이 취소되었습니다.";
					}

				} else {
					String[] finalOder = null;
					String[][] orders = new String[100][];
					int recordIndex = -1;
					boolean posDispCheck = true;
					String pos;
					String memberCode = null;
					while (true) {
						recordIndex++;
						// 판매화면 이동
						pos = this.posDisplay(title, orders, posDispCheck, message);
						if (pos.toUpperCase().equals("Y") || pos.toUpperCase().equals("N"))
							break;

						jobCode = "4S";
						String[] search = { pos };
						// 서버에 상품코드 전달 후 상품정보(1001,(HOT)아메리카노,2500,1,10) 받기
						String[] oder = fc.getRequest("4S", search).split(",");
						posDispCheck = false;
						orders[recordIndex] = oder;
					}
					if (pos.toUpperCase().equals("Y")) {

						jobCode = "4D";
						// finalOrder에 할당
						finalOder = new String[recordIndex];
						/* 포인트 적립 여부 */
						this.display(" ----------- 포인트를 적립하시겠습니까?(y/n)");
						if (this.menuInput().toUpperCase().equals("Y")) {
							this.display(" ----------- 회원코드 입력 : ");
							memberCode = this.menuInput();
						}
						// orders >> finalOder 할당
						for (int i = 0; i < finalOder.length; i++) {
							finalOder[i] = orders[i][0] + "," + orders[i][1] + "," + orders[i][2] + "," + orders[i][3]
									+ "," + orders[i][4] + ((memberCode == null) ? "" : "," + memberCode);
							// 주문데이터 전송
						}
						message = fc.getRequest(jobCode, finalOder);
						jobCode = "";
						menuCode = 4;

					} else {
						jobCode = "";
						menuCode = 4;
					}
				}
			} else {

				if (jobCode.equals("21")) {
					userData = this.regMenu(title, message);
					if (userData != null) {
						jobCode = "2R";
						message = fc.getRequest(jobCode, userData);
						jobCode = "21";
					} else {
						jobCode = "";
						message = "";
					}
				} else if (jobCode.equals("31")) {
					userData = this.ctlRegMember(title, message);
					if (userData != null) {
						jobCode = "3R";
						message = fc.getRequest(jobCode, userData);
						jobCode = "31";
					} else {
						jobCode = "";
						message = "";
					}
				} else if (jobCode.equals("22")) {
					/* 메뉴 수정 */
					userData = this.ctlModMenu(title, message);
					if (userData != null) {
						jobCode = "2M";
						message = fc.getRequest(jobCode, userData);
						jobCode = "22";
					}
				} else if (jobCode.equals("23")) {
					/* 메뉴 삭제 */
					userData = this.ctlDelMenu(title, message);
					if (userData != null) {
						jobCode = "2D";
						message = fc.getRequest(jobCode, userData);
						jobCode = "23";
					}
				} else if (jobCode.equals("32")) {
					/* 회원 수정 */
					userData = this.ctlModMember(title, message);
					if (userData != null) {
						jobCode = "3M";
						message = fc.getRequest(jobCode, userData);
						jobCode = "32";
					}
				} else if (jobCode.equals("33")) {
					/* 회원 삭제 */
					userData = this.ctlDelMember(title, message);
					if (userData != null) {
						jobCode = "3D";
						message = fc.getRequest(jobCode, userData);
						jobCode = "33";
					}
				}else if(jobCode.equals("13")) {

					this.stat(title, message);
					jobCode="";
					message="";
				}else if(jobCode.equals("14")) {

					this.stat2(title, message);
					jobCode="";
					message="";
				}
			}
		}
	}

	private void stat2(String title, String message) {

		this.display(title);
		this.display(" --------------------------------------------------\n");
		this.display("  해당범위  		건수     매출액    할인적용매출액 \n\n");
		this.display(message);
		this.display("\n\n --------------------------------------------------\n");
		this.display(" ------------------------------ 확인(press any key) : ");
		this.menuInput();
	}

	private void stat(String title, String message) {

		this.display(title);
		this.display(" --------------------------------------------------\n");
		this.display(" 해당월   건수     매출액    할인적용매출액 \n\n");
		this.display(message);
		this.display("\n\n --------------------------------------------------\n");
		this.display(" ------------------------------ 확인(press any key) : ");
		this.menuInput();
	}

	private String getDate(String condition) {
		SimpleDateFormat sdf = new SimpleDateFormat(condition);
		Date d=new Date();
		return sdf.format(d);		
	}
	private int countRecord(String[][] orders) {
		int i;
		for (i = 0; i < orders.length; i++) {
			if (orders[i] == null) {
				break;
			}
		}
		return i;
	}

	private String posDisplay(String title, String[][] orders, boolean check, String message) {
		String code = "Y";
		int countOrders = this.countRecord(orders);
		String qty;
		// 프로그램 타이틀 출력
		while (true) {
			if (check) {
				this.display(title);
				//this.display(message2);
				// orders !=null >> message 출력 >> 장바구니(1)
				if (message.length() == 0) {
					if (orders[0] != null) {
						this.display("장바구니(" + countOrders + ")");
					}
				} else {
					this.display("   " + message + "\n");
				}

				if (countOrders > 0) {
					this.display(
							" --------------------------------------------------\n    순번     코드          상품        가격   수량   할인율\n   --------------------------------------------------\n");
					for (int recordIndex = 0; recordIndex < countOrders; recordIndex++) {
						this.display("   " + (recordIndex + 1) + "\t" + orders[recordIndex][0] + "\t "
								+ orders[recordIndex][1] + ((orders[recordIndex][1].length() >= 6) ? "\t  " : "\t\t  ")
								+ orders[recordIndex][2] + "\t" + orders[recordIndex][3] + "\t" + orders[recordIndex][4]
										+ "%\n");
					}

					this.display(" --------------------------------------------------\n");
				}
				if (code.equals("Y")) {
					this.display(" [ GOODS SEARCH ] : ");
				} else {
					this.display(" ---------------------------------- Sure?(y/n) : ");
				}
				code = this.menuInput();
				break;
			} else {
				this.display("         " + orders[countOrders - 1][0] + "\t" + orders[countOrders - 1][1] + "\t"
						+ orders[countOrders - 1][2] + "\t" + orders[countOrders - 1][4] + "\n");
				while (true) {
					this.display(" ____________________________________ QUANTITY : ");
					qty = this.menuInput();
					if (this.toInt(qty)) {
						orders[countOrders - 1][3] = qty;
						break;
					}

				}
				this.display("  ____________________________________ ADD?(Y/N):");
				code = this.menuInput().toUpperCase();
				check = true;
			}
		}
		// ctlView >> orders
		return code;
	}

	private String[] ctlModMenu(String title, String menuList) {
		String[] modMenu = null;
		String[] item = { "코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 " };

		this.display(title + "\n");
		this.display(this.makeMenuTitle("등록된 메뉴"));
		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}
		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + menuList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 메뉴를 수정하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			modMenu = new String[3];
			this.display("메뉴코드\t:");
			modMenu[0] = this.menuInput();
			this.display("상품가격\t:");
			modMenu[1] = this.menuInput();
			this.display("할인율\t:");
			modMenu[2] = this.menuInput();
		}
		return modMenu;
	}

	private String[] ctlDelMenu(String title, String menuList) {
		String[] delMenu = null;
		String[] item = { "코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 " };
		this.display(title + "\n");
		this.display(this.makeMenuTitle("등록된 메뉴"));
		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}
		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + menuList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 메뉴를 삭제하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			delMenu = new String[1];
			this.display("메뉴코드\t:");
			delMenu[0] = this.menuInput();
		}
		return delMenu;
	}

	private String[] ctlModMember(String title, String memberList) {
		String[] modMember = null;
		String[] item = { "코드 ", "이름 ", "전번 " };

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 회원정보를 수정하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			modMember = new String[2];
			this.display("회원코드\t:");
			modMember[0] = this.menuInput();
			this.display("전화번호\t:");
			modMember[1] = this.menuInput();
		}
		return modMember;
	}

	private String[] ctlDelMember(String title, String memberList) {
		String[] delMember = null;
		String[] item = { "코드 ", "이름 ", "전번 " };

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 회원정보를 삭제하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			delMember = new String[1];
			this.display("회원코드\t:");
			delMember[0] = this.menuInput();
		}
		return delMember;
	}

	private String[] ctlRegMember(String title, String memberList) {
		String[] item = { "코드 ", "이름 ", "전번 " };
		String[] regMember = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("회원리스트"));

		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}

		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + memberList);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 회원등록을 하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			while (true) {
				this.display(this.makeMenuTitle("등록할 회원"));

				for (int index = 0; index < item.length; index++) {
					this.display(item[index] + ": ");
					regMember[index] = this.menuInput();
				}

				this.display("________________________________ CONFIRM?(Y/N) : ");
				if (this.menuInput().toUpperCase().equals("Y")) {
					break;
				}
			}
		} else {
			regMember = null;
		}

		return regMember;
	}

	private String[] regMenu(String title, String message) {
		String[] item = { "코드 ", "이름 ", "가격 ", "상태 ", "분류 ", "할인 " };
		String[] regMenu = new String[item.length];

		this.display(title + "\n");
		this.display(this.makeMenuTitle("등록된 메뉴"));
		for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
			if (itemIndex == 0) {
				this.display(" ");
			}
			this.display(item[itemIndex] + "\t");
			if (itemIndex == 1) {
				this.display("\t");
			}
		}
		this.display("\n ---------------------------------------------------\n");
		this.display("\n" + message);
		this.display(" ---------------------------------------------------\n\n");

		this.display(" 메뉴등록을 하시겠습니까?(y/n) : ");
		if (this.menuInput().toUpperCase().equals("Y")) {
			while (true) {
				this.display(this.makeMenuTitle("등록할 메뉴"));

				for (int index = 0; index < item.length; index++) {
					this.display(item[index] + ": ");
					regMenu[index] = this.menuInput();
				}

				this.display("________________________________ CONFIRM?(Y/N) : ");
				if (this.menuInput().toUpperCase().equals("Y")) {
					break;
				}
			}
		} else {
			regMenu = null;
		}

		return regMenu;
	}

	/* 메인화면 제어 및 사용자 데이터 수집 */
	private int ctlMain(String title, String message, String[][] menu) {
		int menuCode;
		while (true) {
			this.display(title);
			this.display(message + "\n\n");
			this.display(this.makeMenuTitle("MAIN"));
			this.display(this.makeMenu(menu));
			this.display(" _____________________________________ SELECT : ");
			menuCode = this.userInput();
			if (menuCode >= 0 && menuCode <= menu.length) {
				break;
			} else {
				message = "메뉴는 0 ~ " + menu.length + "범위이어야 합니다.";
			}
		}
		return menuCode;
	}

	/* 서브메뉴화면 제어 및 사용자 데이터 수집 */
	private int ctlSub(String title, String message, String[] subMenu) {
		int subCode;
		while (true) {
			this.display(title);
			this.display(message + "\n\n");

			this.display(this.makeMenuTitle(subMenu[0]));
			this.display(this.makeMenu(subMenu));
			this.display(" _____________________________________ SELECT : ");

			subCode = this.userInput();
			if (subCode >= 0 && subCode < subMenu.length) {
				break;
			} else {
				message = "메뉴는 0 ~ " + (subMenu.length - 1) + "범위이어야 합니다.";
			}
		}
		return subCode;
	}

	private String makeMenuTitle(String text) {
		StringBuffer menuTitle = new StringBuffer();
		int startSpace = (21 - text.length()) / 2;
		int endSpace = ((21 - text.length()) % 2 == 1) ? startSpace + 1 : startSpace;
		menuTitle.append(" [");
		for (int space = 0; space < startSpace; space++) {
			menuTitle.append(" ");
		}
		menuTitle.append(text);
		for (int space = 0; space < endSpace; space++) {
			menuTitle.append(" ");
		}
		menuTitle.append("]");
		menuTitle.append("__________________________\n\n");

		return menuTitle.toString();
	}

	private String makeMenu(String[] subMenu) {
		StringBuffer buffer = new StringBuffer();

		for (int colIndex = 1; colIndex < subMenu.length; colIndex++) {
			if (colIndex == 1) {
				buffer.append("  ");
			}
			buffer.append(colIndex + ". " + subMenu[colIndex]);
			if (colIndex == subMenu.length - 1) {
				buffer.append("\n\n");
			} else {
				buffer.append("   ");
			}
		}

		return buffer.toString();
	}

	private String makeMenu(String[][] menu) {
		StringBuffer buffer = new StringBuffer();

		for (int rowIndex = 0; rowIndex < menu.length; rowIndex++) {
			if (rowIndex == 0) {
				buffer.append("  ");
			}
			buffer.append(rowIndex + 1 + ". " + menu[rowIndex][0]);
			if (rowIndex == menu.length - 1) {
				buffer.append("\n\n");
			} else {
				buffer.append("   ");
			}
		}

		return buffer.toString();
	}

	private String[][] saveMenu() {
		String[][] menu = { { "매장관리", "매장오픈", "매장클로즈", "금일매출현황", "매출통계" },
				{ "상품관리", "메뉴등록", "메뉴수정", "메뉴삭제", "굿즈등록", "굿즈수정", "굿즈삭제" }, { "회원관리", "회원등록", "회원수정", "회원삭제" },
				{ "판매관리" } };
		return menu;
	}

	private String makeTitle() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
		buffer.append("           Point Of Sales SYSTEM v1.0\n\n");
		buffer.append("                              Designed by HoonZzang\n");
		buffer.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		return buffer.toString();
	}

	private void display(String text) {
		System.out.print(text);
	}

	private int userInput() {
		Scanner sc = new Scanner(System.in);
		int menuCode;
		while (true) {
			String data = sc.next();
			if (this.toInt(data)) {
				menuCode = Integer.parseInt(data);
				break;
			}
		}
		return menuCode;
	}

	private String menuInput() {
		return (new Scanner(System.in)).next();
	}

	private boolean toInt(String data) {
		boolean isDigit = true;
		try {
			Integer.parseInt(data);
		} catch (Exception e) {
			isDigit = false;
		}
		return isDigit;
	}
}

/*
 * Access Modifier : 접근제한자 public : 모든 클래스(메서드)의 접근을 허용 default : 접근제한자를 생략 ::
 * 같은 패키지 안에 있는 클래스사이에서는 public 다른 패키지에 있는 클래스사이에서는 private 을 적용 protected : 자식
 * 클래스만 접근을 허용 private : 모든 클래스(메서드)의 접근을 제한
 */
