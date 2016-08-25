package kr.co.d2net.netbro.transfer.model;




public interface IFtpClientService {
	
	public void connectionCheck(Transfer transfer) throws Exception;
	
	public void uploadCheck(Transfer transfer) throws Exception;
	
	/**
	 * 
	 * @param transfer
	 * @throws Exception
	 */
	public void upload(EqTbl eqTbl, Transfer transfer) throws Exception;
	
	/**
	 * 
	 * @param transfer
	 * @throws Exception
	 */
	public void download(EqTbl eqTbl, Transfer transfer) throws Exception;
	
}
