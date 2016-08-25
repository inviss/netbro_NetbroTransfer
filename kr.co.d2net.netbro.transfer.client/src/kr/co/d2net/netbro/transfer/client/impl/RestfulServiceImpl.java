package kr.co.d2net.netbro.transfer.client.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.d2net.netbro.common.utils.JSON;
import kr.co.d2net.netbro.transfer.model.IRestfulService;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulServiceImpl implements IRestfulService {
	final Logger logger = LoggerFactory.getLogger(getClass());

	public <T> T  post(String url, Map<String, Object> params, String encoding, T t) {
		HttpClient client = new DefaultHttpClient();

		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 300000);
		HttpConnectionParams.setSoTimeout(httpParams, 300000);

		try{
			HttpPost post = new HttpPost(url);
			if(logger.isDebugEnabled()) {
				logger.debug("POST : " + post.getURI());
			}
			
			if(params != null && !params.isEmpty()) {
				List<NameValuePair> paramList = convertParam(params);
				post.setEntity(new UrlEncodedFormEntity(paramList, encoding));
			} else {
				StringEntity entity = new StringEntity(JSON.toString(t), encoding);
				entity.setContentType("application/json");
				post.setEntity(entity);
				
				if(logger.isDebugEnabled()) {
					logger.debug("status msg: "+JSON.toString(t));
				}
			}
			
			ResponseHandler<String> rh = new BasicResponseHandler();
			String ret = client.execute(post, rh);
			/*if(logger.isDebugEnabled()) {
				logger.debug("return msg: "+ret);
			}*/
			
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			client.getConnectionManager().shutdown();
		}

		return null;
	}

	public <T> T  post(String url, Map<String, Object> params, T t) {
		return post(url, params, "UTF-8", t);
	}

	
	/**
	 * GET 요청
	 * POST 와 동일
	 */
	public <T> T  get(String url, Map<String, Object> params, String encoding, Class<T> clazz){
		HttpClient client = new DefaultHttpClient();

		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 300000);
		HttpConnectionParams.setSoTimeout(httpParams, 300000);

		try{
			HttpGet get = null;
			if(params != null && !params.isEmpty()) {
				List<NameValuePair> paramList = convertParam(params);
				get = new HttpGet(url+"?"+URLEncodedUtils.format(paramList, encoding));
			} else {
				get = new HttpGet(url);
			}
			if(logger.isDebugEnabled()) {
				logger.debug("GET : " + get.getURI());
			}

			ResponseHandler<String> rh = new BasicResponseHandler();
			String ret = client.execute(get, rh);
			
			if(StringUtils.isNotBlank(ret)) {
				if(logger.isDebugEnabled()) {
					logger.debug("return msg: "+ret);
				}
				return JSON.toObject(ret, clazz);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			client.getConnectionManager().shutdown();
		}

		return null;
	}

	public <T> T get(String url, Map<String, Object> params, Class<T> clazz){
		return get(url, params, "UTF-8", clazz);
	}



	private List<NameValuePair> convertParam(Map<String, Object> params){
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
		}

		return paramList;
	}

}
