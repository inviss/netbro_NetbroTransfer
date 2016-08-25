package kr.co.d2net.netbro.common.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

@SuppressWarnings("unchecked")
public class JSON extends AbstractMap<String, Object> implements Serializable{
	
	private static final long serialVersionUID = -727790525757168402L;
	private final Map<String, Object> propMap ;
	
	public JSON(){
		propMap = new HashMap<String, Object>();
	}
	
	public JSON(String json){
		propMap = JSON.toMap(json);
	}

	public JSON(Map<String, Object> map){
		this.propMap = map;
	}

	public void clear() {
		propMap.clear();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return propMap.entrySet();
	}

	public Object get(Object key) {
		return propMap.get(key);
	}

	public Object put(String key, Object value) {
		return propMap.put(key, value);
	}

	public Object remove(Object key) {
		return propMap.remove(key);
	}
	
	public String toString(){
		return JSON.toJsonString(propMap);
	}
	
	public <T> T toObject(Class<?> cls){
		return JSON.toObject(toString(), cls);
	}
	
	public static JSON parseFromJSONString(String jsonString){
		Map<String, Object> map = JSON.toMap(jsonString);
		return new JSON(map);
	}
	
	public static JSON parseFromMap(Map<String,Object> map){
		return new JSON(map);
	}
	
	public static JSON parseFromObject(Object obj){
		Map<String, Object> map = JSON.toMap(obj);
		return new JSON(map);
	}
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final JsonFactory jsonFactory = new JsonFactory();

	public static String toString(Object object){
		return toJsonString(object);
	}
	
	/**
	 * Bean 객체를 Json 형태로 변환
	 * @param bean
	 * @return
	 */
	public static String toJsonString(Object bean){
		StringWriter sw = new StringWriter();   
		JsonGenerator gen = null;
		try {
			gen = jsonFactory.createJsonGenerator(sw);
			mapper.writeValue(gen, bean);    
			String json = sw.toString(); 
			return json;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			try {
				gen.close();
			} catch (IOException e) {
			}
		}
	}
	
	
	/**
	 * Json 형태 문자열을 Bean 객체로 생성
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String jsonString, Class<?> clazz){
		return toObject(jsonString, clazz, false);
	}
	
	/**
	 * Json 형태 문자열을 Bean 객체로 생성
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @param unquoted
	 * @return
	 */
	
	public static <T> T toObject(String jsonString,Class<?> clazz, boolean unquoted){
		try {
			mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, unquoted);
			return (T)mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * Json 형태 문자열을 Map 객체로 생성
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(String jsonString){
		return toObject(jsonString, Map.class, false);
	}
	
	/**
	 * Object 형태 문자열을 Map 형태로 생성
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> toMap(Object bean) {
		Map<String, Object> fieldMap = mapper.convertValue(bean, Map.class);
		return fieldMap;
	}
}
