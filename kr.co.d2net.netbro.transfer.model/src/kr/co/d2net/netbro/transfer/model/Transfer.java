package kr.co.d2net.netbro.transfer.model;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import kr.co.d2net.netbro.common.utils.TMode;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("rawtypes")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Transfer extends BaseBean {
	
	@JsonProperty("ct_id")
	private Long ctId;
	
	@JsonProperty("priority")
	private String priority;
	
	@JsonProperty("retry_cnt")
	private Integer retryCnt;
	
	@JsonProperty("seq")
	private Long seq;
	
	@JsonProperty("work_statcd")
	private String workStatcd;
	
	@JsonProperty("ct_nm")
	private String ctNm;
	
	@JsonProperty("cti_id")
	private Long ctiId;
	
	@JsonProperty("fl_path")
	private String flPath;
	
	@JsonProperty("org_file_nm")
	private String orgFileNm;
	
	@JsonProperty("wrk_file_nm")
	private String wrkFileNm;
	
	@JsonProperty("fl_ext")
	private String flExt;
	
	@JsonProperty("pro_flnm")
	private String proFlnm;
	
	@JsonProperty("category_nm")
	private String categoryNm;
	
	@JsonProperty("episode_nm")
	private String episodeNm;
	
	@JsonProperty("brd_dd")
	private Date brdDd;
	
	@JsonProperty("ct_leng")
	private String ctLeng;
	
	@JsonProperty("vdo_bit_rate")
	private String vdoBitRate;
	
	@JsonProperty("company")
	private String company;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("ftp_ip")
	private String ftpIp;
	
	@JsonProperty("ftp_port")
	private Integer ftpPort;
	
	@JsonProperty("user_pass")
	private String userPass;
	
	@JsonProperty("remote_dir")
	private String remoteDir;
	
	@JsonProperty("file_size")
	private Long fileSize;
	
	@JsonProperty("method")
	private String connectMode;
	
	private TMode transferType;
	private String transferMode;
	private Integer progress;
	private String deviceId;
	private Integer deviceNum;
	private char errorCode;
	
	
	public TMode getTransferType() {
		return transferType;
	}

	public void setTransferType(TMode transferType) {
		this.transferType = transferType;
	}

	public String getConnectMode() {
		return connectMode;
	}

	public void setConnectMode(String connectMode) {
		firePropertyChange(new PropertyChangeEvent(this, "connectMode",
				this.connectMode, this.connectMode = connectMode));
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		firePropertyChange(new PropertyChangeEvent(this, "transferMode",
				this.transferMode, this.transferMode = transferMode));
	}

	public char getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(char errorCode) {
		firePropertyChange(new PropertyChangeEvent(this, "errorCode",
				this.errorCode, this.errorCode = errorCode));
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		firePropertyChange(new PropertyChangeEvent(this, "deviceId",
				this.deviceId, this.deviceId = deviceId));
	}

	public Integer getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(Integer deviceNum) {
		firePropertyChange(new PropertyChangeEvent(this, "deviceNum",
				this.deviceNum, this.deviceNum = deviceNum));
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		firePropertyChange(new PropertyChangeEvent(this, "progress",
				this.progress, this.progress = progress));
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		firePropertyChange(new PropertyChangeEvent(this, "ftpIp",
				this.ftpIp, this.ftpIp = ftpIp));
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		firePropertyChange(new PropertyChangeEvent(this, "ftpPort",
				this.ftpPort, this.ftpPort = ftpPort));
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		firePropertyChange(new PropertyChangeEvent(this, "fileSize",
				this.fileSize, this.fileSize = fileSize));
	}

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		firePropertyChange(new PropertyChangeEvent(this, "ctId",
				this.ctId, this.ctId = ctId));
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		firePropertyChange(new PropertyChangeEvent(this, "priority",
				this.priority, this.priority = priority));
	}

	public Integer getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(Integer retryCnt) {
		firePropertyChange(new PropertyChangeEvent(this, "retryCnt",
				this.retryCnt, this.retryCnt = retryCnt));
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		firePropertyChange(new PropertyChangeEvent(this, "seq",
				this.seq, this.seq = seq));
	}

	public String getWorkStatcd() {
		return workStatcd;
	}

	public void setWorkStatcd(String workStatcd) {
		firePropertyChange(new PropertyChangeEvent(this, "workStatcd",
				this.workStatcd, this.workStatcd = workStatcd));
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		firePropertyChange(new PropertyChangeEvent(this, "ctNm",
				this.ctNm, this.ctNm = ctNm));
	}

	public Long getCtiId() {
		return ctiId;
	}

	public void setCtiId(Long ctiId) {
		firePropertyChange(new PropertyChangeEvent(this, "ctiId",
				this.ctiId, this.ctiId = ctiId));
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		firePropertyChange(new PropertyChangeEvent(this, "flPath",
				this.flPath, this.flPath = flPath));
	}

	public String getOrgFileNm() {
		return orgFileNm;
	}

	public void setOrgFileNm(String orgFileNm) {
		firePropertyChange(new PropertyChangeEvent(this, "orgFileNm",
				this.orgFileNm, this.orgFileNm = orgFileNm));
	}

	public String getWrkFileNm() {
		return wrkFileNm;
	}

	public void setWrkFileNm(String wrkFileNm) {
		firePropertyChange(new PropertyChangeEvent(this, "wrkFileNm",
				this.wrkFileNm, this.wrkFileNm = wrkFileNm));
	}

	public String getFlExt() {
		return flExt;
	}

	public void setFlExt(String flExt) {
		firePropertyChange(new PropertyChangeEvent(this, "flExt",
				this.flExt, this.flExt = flExt));
	}

	public String getProFlnm() {
		return proFlnm;
	}

	public void setProFlnm(String proFlnm) {
		firePropertyChange(new PropertyChangeEvent(this, "proFlnm",
				this.proFlnm, this.proFlnm = proFlnm));
	}

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		firePropertyChange(new PropertyChangeEvent(this, "categoryNm",
				this.categoryNm, this.categoryNm = categoryNm));
	}

	public String getEpisodeNm() {
		return episodeNm;
	}

	public void setEpisodeNm(String episodeNm) {
		firePropertyChange(new PropertyChangeEvent(this, "episodeNm",
				this.episodeNm, this.episodeNm = episodeNm));
	}

	public Date getBrdDd() {
		return brdDd;
	}

	public void setBrdDd(Date brdDd) {
		firePropertyChange(new PropertyChangeEvent(this, "brdDd",
				this.brdDd, this.brdDd = brdDd));
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		firePropertyChange(new PropertyChangeEvent(this, "ctLeng",
				this.ctLeng, this.ctLeng = ctLeng));
	}

	public String getVdoBitRate() {
		return vdoBitRate;
	}

	public void setVdoBitRate(String vdoBitRate) {
		firePropertyChange(new PropertyChangeEvent(this, "vdoBitRate",
				this.vdoBitRate, this.vdoBitRate = vdoBitRate));
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		firePropertyChange(new PropertyChangeEvent(this, "company",
				this.company, this.company = company));
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		firePropertyChange(new PropertyChangeEvent(this, "userId",
				this.userId, this.userId = userId));
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		firePropertyChange(new PropertyChangeEvent(this, "userPass",
				this.userPass, this.userPass = userPass));
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		firePropertyChange(new PropertyChangeEvent(this, "remoteDir",
				this.remoteDir, this.remoteDir = remoteDir));
	}
	
	
}
