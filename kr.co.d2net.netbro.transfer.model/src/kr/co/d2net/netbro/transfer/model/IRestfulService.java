package kr.co.d2net.netbro.transfer.model;

import java.util.Map;

public interface IRestfulService {

	/**
	 * POST 요청
	 * @param url       요청할 url
	 * @param params    파라메터
	 * @param encoding  파라메터 Encoding
	 * @return 서버 응답결과 문자열
	 */
	public <T> T post(String url, Map<String, Object> params, String encoding, T t);
	public <T> T post(String url, Map<String, Object> params, T t);
	public <T> T get(String url, Map<String, Object> params, String encoding, Class<T> clazz);
	public <T> T get(String url, Map<String, Object> params, Class<T> clazz);
	
}
