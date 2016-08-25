package kr.co.d2net.netbro.transfer.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Status extends BaseBean {
	
	@JsonProperty("device_id")
	private String deviceId;
	@JsonProperty("device_num")
	private Integer deviceNum;
	@JsonProperty("device_state")
	private String deviceState;
	@JsonProperty("seq")
	private Long seq;
	@JsonProperty("work_statcd")
	private String wrkStatcd;
	@JsonProperty("progress")
	private Integer progress;
	@JsonProperty("error_cd")
	private String errorCd;
	
	
	public String getDeviceState() {
		return deviceState;
	}
	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public String getWrkStatcd() {
		return wrkStatcd;
	}
	public void setWrkStatcd(String wrkStatcd) {
		this.wrkStatcd = wrkStatcd;
	}
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	public String getErrorCd() {
		return errorCd;
	}
	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}
	
}
