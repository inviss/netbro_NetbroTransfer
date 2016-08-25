package kr.co.d2net.netbro.transfer.model;

public class EqTbl {
	private Integer num;
	private String eqId;
	private String eqNm;
	private String serverIp;
	private String serverPort;
	private String serverCtx;
	private String jobRepPath;
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getServerCtx() {
		return serverCtx;
	}
	public void setServerCtx(String serverCtx) {
		this.serverCtx = serverCtx;
	}
	public String getJobRepPath() {
		return jobRepPath;
	}
	public void setJobRepPath(String jobRepPath) {
		this.jobRepPath = jobRepPath;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getEqId() {
		return eqId;
	}
	public void setEqId(String eqId) {
		this.eqId = eqId;
	}
	public String getEqNm() {
		return eqNm;
	}
	public void setEqNm(String eqNm) {
		this.eqNm = eqNm;
	}
	
}
