package models;

public class MemberManagement {
	private DataAccessObject dao;
	public MemberManagement() {

	}

	public String backController(String jobCode) {
		String message = null;
		switch(jobCode) {
		case "31": case "32": case "33":
			message = this.ctlReadMember();
			break;
		}
		return message;
	}
	
	public String backController(String jobCode, String[] data) {
		String message = null;
		switch(jobCode) {
		case "3R":
			message = this.ctlRegMember(data);
			break;
		case "3M":
			message=this.ctlModMember(data);
			break;
		case "3D":
			message=this.ctlDegMember(data);
			break;
		}
		return message;
	}

	private String ctlReadMember() {
		dao = new DataAccessObject();
		return this.toStringFromArray(dao.getMemberList());
	}

	private String toStringFromArray(String[][] data) {
		StringBuffer sb = new StringBuffer();

		for(int recordIndex=0; recordIndex<data.length; recordIndex++) {
			sb.append(" ");
			for(int colIndex=0; colIndex<data[recordIndex].length; colIndex++) {
				sb.append(data[recordIndex][colIndex]);
				
				if(colIndex != data[recordIndex].length - 1) {
					sb.append("\t");
					if(colIndex == 1 && data[recordIndex][colIndex].length()<6) {
						sb.append("\t");
					}
				}
			}
			sb.append("\n");
		}


		return sb.toString();
	}

	/* 회원정보등록 */
	private String ctlRegMember(String[] memberInfo) {
		String message = null;
		dao = new DataAccessObject();
		if(dao.setMember(memberInfo)) {
			message = this.toStringFromArray(dao.getMemberList());
		}else {
			message = "회원등록작업이 실패하였습니다.\n다시 등록해 주시기 바랍니다.";
		}
		
		return message;
	}

	/* 회원정보수정 */
	private String ctlModMember(String[] data) {
		dao = new DataAccessObject();
		String[][] list=dao.getMemberList();
		for(int i=0;i<list.length;i++) {
			if(list[i][0].equals(data[0])){
				list[i][2]=data[1];
				break;
			}
		}
		
		 return (dao.setMember(list))?this.toStringFromArray(dao.getMemberList()):"회원수정에 실패하였습니다. 다시 입력해주세요.";
	}

	/* 회원정보삭제 */
	private String ctlDegMember(String[] data) {
		dao = new DataAccessObject();
		boolean check=true;
		String[][] list=dao.getMemberList();
		String[][] newList=new String[list.length-1][list[0].length];
		for(int i=0;i<list.length;i++) {
			if(!list[i][0].equals(data[0])) {
				newList[(check)?i:i-1]=list[i];
			}else {check=false;}
		}
		 return (dao.setMember(newList))?this.toStringFromArray(dao.getMemberList()):"회원삭제에 실패하였습니다. 다시 입력해주세요.";
	}
}
