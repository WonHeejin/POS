package models;

import java.util.ArrayList;

import beans.MemberBean;

public class MemberManagement {
	private DataAccessObject dao;
	public MemberManagement() {

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
		case "31": case "32": case "33":
			message = this.ctlReadMember();
			break;
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

	private String toStringFromArray(ArrayList<MemberBean> data) {
		StringBuffer sb = new StringBuffer();
		
		for(int recordIndex=0; recordIndex<data.size(); recordIndex++) {
			sb.append(" ");
			sb.append(data.get(recordIndex).getMemberCode());
			sb.append("\t");
			sb.append(data.get(recordIndex).getMemberName());
			sb.append(data.get(recordIndex).getMemberName().length()<6?"\t":"");
			sb.append("\t");
			sb.append(data.get(recordIndex).getCallNumber());			
			sb.append("\n");
		}


		return sb.toString();
	}

	/* 회원정보등록 */
	private String ctlRegMember(String[] memberInfo) {
		String message = null;
		dao = new DataAccessObject();
		MemberBean mb= new MemberBean();
		mb.setMemberCode(memberInfo[0]);
		mb.setMemberName(memberInfo[1]);
		mb.setCallNumber(memberInfo[2]);
		if(dao.setMember(mb)) {
			message = this.toStringFromArray(dao.getMemberList());
		}else {
			message = "회원등록작업이 실패하였습니다.\n다시 등록해 주시기 바랍니다.";
		}
		
		return message;
	}

	/* 회원정보수정 */
	private String ctlModMember(String[] data) {
		dao = new DataAccessObject();
		MemberBean mb= new MemberBean();
		mb.setMemberCode(data[0]);
		mb.setCallNumber(data[1]);
		ArrayList<MemberBean> list=dao.getMemberList();
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getMemberCode().equals(mb.getMemberCode())){
				list.get(i).setCallNumber(mb.getCallNumber());
				break;
			}
		}
		
		 return (dao.setMember(list))?this.toStringFromArray(dao.getMemberList()):"회원수정에 실패하였습니다. 다시 입력해주세요.";
	}

	/* 회원정보삭제 */
	private String ctlDegMember(String[] data) {
		MemberBean mb= new MemberBean();
		mb.setMemberCode(data[0]);
		dao = new DataAccessObject();
		boolean check=true;
		ArrayList<MemberBean> list=dao.getMemberList();
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getMemberCode().equals(mb.getMemberCode())) {
				list.remove(i);
			}
		}
		 return (dao.setMember(list))?this.toStringFromArray(dao.getMemberList()):"회원삭제에 실패하였습니다. 다시 입력해주세요.";
	}
}
