package kr.co.d2net.netbro.transfer.model;

import java.beans.PropertyChangeEvent;


public class Content extends BaseBean {
	
	private String contentNm;
	private String fileName;
	private String fileType;
	private String filePath;
	private String timecode;
	private String status;
	private Integer progress;
	private Long fileSize;
	private Long duration;
	private Integer vdVresol;
	private Integer vdHresol;
	private Integer bitRt;
	private String aspRto;
	private String frmPerSec;
	private String makeFilePath;
	private String makeFileName;
	private String vdQlty;
	
	public String getVdQlty() {
		return vdQlty;
	}
	public void setVdQlty(String vdQlty) {
		firePropertyChange(new PropertyChangeEvent(this, "vdQlty", this.vdQlty,
				this.vdQlty = vdQlty));
	}
	public String getMakeFilePath() {
		return makeFilePath;
	}
	public void setMakeFilePath(String makeFilePath) {
		firePropertyChange(new PropertyChangeEvent(this, "makeFilePath", this.makeFilePath,
				this.makeFilePath = makeFilePath));
	}
	public String getMakeFileName() {
		return makeFileName;
	}
	public void setMakeFileName(String makeFileName) {
		firePropertyChange(new PropertyChangeEvent(this, "makeFileName", this.makeFileName,
				this.makeFileName = makeFileName));
	}
	public Integer getBitRt() {
		return bitRt;
	}
	public void setBitRt(Integer bitRt) {
		firePropertyChange(new PropertyChangeEvent(this, "bitRt", this.bitRt,
				this.bitRt = bitRt));
	}
	public String getAspRto() {
		return aspRto;
	}
	public void setAspRto(String aspRto) {
		firePropertyChange(new PropertyChangeEvent(this, "aspRto", this.aspRto,
				this.aspRto = aspRto));
	}
	public String getFrmPerSec() {
		return frmPerSec;
	}
	public void setFrmPerSec(String frmPerSec) {
		firePropertyChange(new PropertyChangeEvent(this, "frmPerSec", this.frmPerSec,
				this.frmPerSec = frmPerSec));
	}
	public Integer getVdVresol() {
		return vdVresol;
	}
	public void setVdVresol(Integer vdVresol) {
		firePropertyChange(new PropertyChangeEvent(this, "vdVresol", this.vdVresol,
				this.vdVresol = vdVresol));
	}
	public Integer getVdHresol() {
		return vdHresol;
	}
	public void setVdHresol(Integer vdHresol) {
		firePropertyChange(new PropertyChangeEvent(this, "vdHresol", this.vdHresol,
				this.vdHresol = vdHresol));
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		firePropertyChange(new PropertyChangeEvent(this, "duration", this.duration,
				this.duration = duration));
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		firePropertyChange(new PropertyChangeEvent(this, "fileSize", this.fileSize,
				this.fileSize = fileSize));
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		firePropertyChange(new PropertyChangeEvent(this, "status", this.status,
				this.status = status));
	}
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		firePropertyChange(new PropertyChangeEvent(this, "progress", this.progress,
				this.progress = progress));
	}
	public String getContentNm() {
		return contentNm;
	}
	public void setContentNm(String contentNm) {
		firePropertyChange(new PropertyChangeEvent(this, "contentNm", this.contentNm,
				this.contentNm = contentNm));
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		firePropertyChange(new PropertyChangeEvent(this, "fileName", this.fileName,
				this.fileName = fileName));
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		firePropertyChange(new PropertyChangeEvent(this, "fileType", this.fileType,
				this.fileType = fileType));
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		firePropertyChange(new PropertyChangeEvent(this, "finalYn", this.filePath,
				this.filePath = filePath));
	}
	public String getTimecode() {
		return timecode;
	}
	public void setTimecode(String timecode) {
		firePropertyChange(new PropertyChangeEvent(this, "finalYn", this.timecode,
				this.timecode = timecode));
	}
}
