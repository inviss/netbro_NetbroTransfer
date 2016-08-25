package kr.co.d2net.netbro.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unchecked")
public class ObjectUtils {

	/**
	 * Map에 저장된 k,v 형태의 데이타를 Entity Class에 맵핑한다.
	 * @param target
	 * @param propertyValues
	 */
	public static void setProperties(Object target, Map<String, Object> propertyValues) {
		if (target==null || propertyValues == null || propertyValues.isEmpty())
			return;
		try {
			PropertyDescriptor[] pdArray = ClassUtils.getPropertyDescriptorsArray(target.getClass());
			if(pdArray == null) return;
			for (PropertyDescriptor pd : pdArray) {
				String propertyName = pd.getName();
				Object value = propertyValues.get(unCapitalize(propertyName));
				if (value != null) {
					setPropertyValue(target, pd, value);
				}
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 입력된 문자열에 '_'이 존재한다면 분리하여 각 문장의 첫글자만 대문자로 변환하여 병합하고 없다면 해당 문장의 첫글자만 대문자로
	 * 변환한 후 맨 첫부분에 prefix 문자열을 합해 setter 메소드명 을 만들어서 반환한다.
	 * 
	 * @param name
	 * @return
	 */
	public static String capitalize(String prefix, String name) {
		String tmp = StringUtils.isEmpty(prefix) ? "" : prefix;
		if (name.indexOf("_") > 0) {
			String[] tmp2 = name.split("\\_");
			for (int j = 0; j < tmp2.length; j++)
				if (j == 0)
					tmp += tmp2[j].toLowerCase();
				else
					tmp += StringUtils.capitalize(tmp2[j].toLowerCase());
		} else {
			tmp = name;
		}
		return tmp;
	}
	
	/**
	 * 대문자가 존재하면 앞에 "_" 언더바를 추가하고 대문자를 소문자로 변환한다.
	 * 
	 * @param name
	 * @return
	 */
	public static String unCapitalize(String name) {
		String tmp = "";
		char[] chars = name.toCharArray();
		for(char ch : chars) {
			if (Character.isUpperCase(ch) == true) {
				tmp += "_"+String.valueOf(Character.toLowerCase(ch));
			} else {
				tmp += String.valueOf(ch);
			}
		}
		return tmp;
	}

	/**
	 * Entity Class의 name과 동일한 변수에 value를 저장한다.
	 * 
	 * @param target
	 * @param name
	 * @param value
	 */
	public static void setProperty(Object target, String name, Object value) {

		PropertyDescriptor pd = ClassUtils.getPropertyDescriptor(target.getClass(), name);
		if (pd == null)
			return;
		setPropertyValue(target, pd, value);

	}

	/**
	 * Entity Class에서 propertyName과 동일한 변수의 value값을 가져온다.
	 * 
	 * @param target
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(Object target, String propertyName) {

		PropertyDescriptor pd = ClassUtils.getPropertyDescriptor(target.getClass(), propertyName);

		if (pd == null){
			throw new RuntimeException("the property '" + propertyName + "' not found");
		}

		return getPropertyValue(target, pd);

	}
	
	public static Object getPropertyValue(Object objInstance , PropertyDescriptor pd){
		if (pd == null)
			return null;

		Method getter = pd.getReadMethod();
		if (getter == null) {
			throw new RuntimeException("the property '" + pd.getName() + "' getter not found - " + objInstance.getClass().getName());
		}
		return invokeMethod(objInstance, getter);
	}
	
	protected static void wirtePropertyValue(Object objInstance , PropertyDescriptor pd,Object value){
		if (pd == null){
			return ;
		}
		Method setter = pd.getWriteMethod();
		if (setter == null) {
			throw new RuntimeException("the property '" + pd.getName() + "' getter not found - " + objInstance.getClass().getName());
		}
		invokeMethod(objInstance, setter,value);
	}

	/**
	 * Entity Class에 Property에서 읽어온 정보를 맵핑한다.
	 * 
	 * @param target
	 * @param pd
	 * @param value
	 */
	protected static void setPropertyValue(Object target, PropertyDescriptor pd, Object value) {
		
		if (target==null || pd == null){
			return;
		}
		
		if(value == null){
			wirtePropertyValue(target, pd, value);
			return ;
		}
		
		Class<?> propType = pd.getPropertyType();
		Object desValue = value;
		if(!value.getClass().isAssignableFrom(propType) && value instanceof Map){
			desValue = convertToObject((Map<String, Object>)value, propType);
			wirtePropertyValue(target, pd, desValue);
			return ;
		}
		
		if(!value.getClass().isAssignableFrom(propType)){
			desValue = TypeConvertUtils.convertValue(desValue, propType);
		}
		
		wirtePropertyValue(target, pd, desValue);
	}
	
	public static <T>T convertToObject(Map<String, Object> map, Class<T> claxx){
		try {
			T desObject = claxx.newInstance();
			setProperties(desObject, map);
			return desObject;
		} catch (Exception e) {
			throw new RuntimeException("the class '" +claxx.getName() + "' not cerate new instance," + e.getMessage());
		}
	}
	
	
	public static Object invokeMethod(Object obj, Method invokeMethod, Object... args) {
		boolean hasChangeAccessibleFlag = false;
		if (!invokeMethod.isAccessible()) {
			invokeMethod.setAccessible(true);
			hasChangeAccessibleFlag = true;
		}
		try {
			return invokeMethod.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException("cannot invoke " + invokeMethod.getName() + " for " + obj.getClass().getName(), e);
		} finally {
			if (hasChangeAccessibleFlag) {
				invokeMethod.setAccessible(false);
			}
		}
	}

	/**
	 * Object의 복사본을 생성한다.
	 * 
	 * @param source
	 * @param dest
	 * @throws IntrospectionException 
	 */

	public static void copyProperties(Object source, Object dest) {
		
		PropertyDescriptor[] pds = null;
		try {
			pds = ClassUtils.getPropertyDescriptorsArray(source.getClass());
		} catch (IntrospectionException e) {
			
		}
		
		if(pds == null)return ;
		
		for (PropertyDescriptor pd : pds) {
			try {
				Object value = getPropertyValue(source, pd.getName());
				setPropertyValue(dest, pd, value);
			} catch (Exception e) {
			}
		}

	}

}
