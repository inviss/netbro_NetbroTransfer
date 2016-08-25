package kr.co.d2net.netbro.transfer.model;

import java.beans.PropertyChangeEvent;

public class EqPreference extends BaseBean {
	
	private String eqId;
	private String serverType;
	private String serverIp;
	private String serverPort;
	private String serverCtx;
	private String jobIn;
	private String statRep;
	private String storagePath;
	
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		firePropertyChange(new PropertyChangeEvent(this, "storagePath",
				this.storagePath, this.storagePath = storagePath));
	}
	public String getServerCtx() {
		return serverCtx;
	}
	public void setServerCtx(String serverCtx) {
		firePropertyChange(new PropertyChangeEvent(this, "serverCtx",
				this.serverCtx, this.serverCtx = serverCtx));
	}
	public String getEqId() {
		return eqId;
	}
	public void setEqId(String eqId) {
		firePropertyChange(new PropertyChangeEvent(this, "eqId",
				this.eqId, this.eqId = eqId));
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		firePropertyChange(new PropertyChangeEvent(this, "serverType",
				this.serverType, this.serverType = serverType));
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		firePropertyChange(new PropertyChangeEvent(this, "serverIp",
				this.serverIp, this.serverIp = serverIp));
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		firePropertyChange(new PropertyChangeEvent(this, "serverPort",
				this.serverPort, this.serverPort = serverPort));
	}
	public String getJobIn() {
		return jobIn;
	}
	public void setJobIn(String jobIn) {
		firePropertyChange(new PropertyChangeEvent(this, "jobIn",
				this.jobIn, this.jobIn = jobIn));
	}
	public String getStatRep() {
		return statRep;
	}
	public void setStatRep(String statRep) {
		firePropertyChange(new PropertyChangeEvent(this, "statRep",
				this.statRep, this.statRep = statRep));
	}
	
}
