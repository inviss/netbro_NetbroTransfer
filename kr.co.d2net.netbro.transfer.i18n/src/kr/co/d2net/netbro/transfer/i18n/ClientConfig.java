package kr.co.d2net.netbro.transfer.i18n;

import org.eclipse.osgi.util.NLS;

public class ClientConfig {
	private static final String BUNDLE_NAME = "kr.co.d2net.netbro.transfer.i18n.client"; //$NON-NLS-1$

	private ClientConfig() {}
	
	public static String eqId;
	public static String serverIp;
	public static String serverPort;
	public static String serverType;
	public static String serverCtx;
	public static String jobReqPath;
	public static String jobRepPath;
	public static String storagePath;
	public static String jobInPath;
	public static String statRepPath;
	public static String updateSite;
	
	// More string values....
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ClientConfig.class);
	}
}
