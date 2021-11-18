package beans;

public class MenuBean {
	private String menuCode;
	private String menuName;
	private int menuPrice;
	private char menuState;
	private String menuCat;
	private int menuDiscount;
	
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}
	public char getMenuState() {
		return menuState;
	}
	public void setMenuState(char menuState) {
		this.menuState = menuState;
	}
	public String getMenuCat() {
		return menuCat;
	}
	public void setMenuCat(String menuCat) {
		this.menuCat = menuCat;
	}
	public int getMenuDiscount() {
		return menuDiscount;
	}
	public void setMenuDiscount(int menuDiscount) {
		this.menuDiscount = menuDiscount;
	}
}
