package kr.co.d2net.netbro.common.utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeConvertUtils {
	
	static final Map<Class<?>,Object> DefaultValues = new HashMap<Class<?>,Object>();

	static {
		DefaultValues.put(Boolean.TYPE, Boolean.FALSE);
		DefaultValues.put(Boolean.class, Boolean.FALSE);
		DefaultValues.put(Byte.TYPE, 0);
		DefaultValues.put(Byte.class, 0);
		DefaultValues.put(Short.TYPE, 0);
		DefaultValues.put(Short.class, 0);
		DefaultValues.put(Character.TYPE, new Character('\000'));
		DefaultValues.put(Integer.TYPE, new Integer(0));
		DefaultValues.put(Long.TYPE, new Long(0L));
		DefaultValues.put(Float.TYPE, new Float(0.0F));
		DefaultValues.put(Double.TYPE, new Double(0.0D));

		DefaultValues.put(BigInteger.class, new BigInteger("0"));
		DefaultValues.put(BigDecimal.class, new BigDecimal(0.0D));
	}

	@SuppressWarnings("unchecked")
	private static <T> T getDefaultValue(Class<?> forClass) {
		return (T)DefaultValues.get(forClass);
	}

	public static boolean booleanValue(boolean value) {
		return value;
	}

	public static boolean booleanValue(int value) {
		return value > 0;
	}

	public static boolean booleanValue(float value) {
		return value > 0;
	}

	public static boolean booleanValue(long value) {
		return value > 0;
	}

	public static boolean booleanValue(double value) {
		return value > 0;
	}

	public static boolean booleanValue(Object value) {
		if (value == null)
			return false;
		Class<?> c = value.getClass();

		if (c == Boolean.class)
			return ((Boolean) value).booleanValue();

		// if ( c == String.class )
		// return ((String)value).length() > 0;

		if (c == Character.class)
			return ((Character) value).charValue() != 0;
		if (value instanceof Number)
			return ((Number) value).doubleValue() != 0;

		return true; // non-null
	}

	public static long longValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0L;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).longValue();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		return Long.parseLong(stringValue(value, true));
	}
	
	public static int intValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).intValue();
		if (c.getSuperclass() == String.class)
			return Integer.parseInt((String)value);
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		return Integer.parseInt(stringValue(value, true));
	}

	public static double doubleValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0.0;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).doubleValue();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		String s = stringValue(value, true);

		return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
	}
	
	public static float floatValue(Object value) throws NumberFormatException {
		if (value == null)
			return 0.0f;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).floatValue();
		if (c == Boolean.class)
			return ((Boolean) value).booleanValue() ? 1 : 0;
		if (c == Character.class)
			return ((Character) value).charValue();
		
		String s = stringValue(value, true);
		return (s.length() == 0) ? 0.0f : Float.parseFloat(s);
	}

	public static BigInteger bigIntValue(Object value) throws NumberFormatException {
		if (value == null)
			return BigInteger.valueOf(0L);
		Class<?> c = value.getClass();
		if (c == BigInteger.class)
			return (BigInteger) value;
		if (c == BigDecimal.class)
			return ((BigDecimal) value).toBigInteger();
		if (c.getSuperclass() == Number.class)
			return BigInteger.valueOf(((Number) value).longValue());
		if (c == Boolean.class)
			return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		if (c == Character.class)
			return BigInteger.valueOf(((Character) value).charValue());
		return new BigInteger(stringValue(value, true));
	}

	public static BigDecimal bigDecValue(Object value) throws NumberFormatException {
		if (value == null)
			return BigDecimal.valueOf(0L);
		Class<?> c = value.getClass();
		if (c == BigDecimal.class)
			return (BigDecimal) value;
		if (c == BigInteger.class)
			return new BigDecimal((BigInteger) value);
		if (c == Boolean.class)
			return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
		if (c == Character.class)
			return BigDecimal.valueOf(((Character) value).charValue());
		return new BigDecimal(stringValue(value, true));
	}

	public static String stringValue(Object value, boolean trim) {
		String result;

		if (value == null) {
			result = null;
		} else {
			result = value.toString();
			if (trim) {
				result = result.trim();
			}
		}
		return result;
	}
	
	public static int getIntValue(Object value) {
		try {
			if (value == null)
				return -1;

			if (Number.class.isInstance(value)) {

				return ((Number) value).intValue();
			}

			String str = String.class.isInstance(value) ? (String) value : value.toString();

			return Integer.parseInt(str);
		} catch (Throwable t) {
			throw new RuntimeException("Error converting " + value + " to integer:", t);
		}
	}

	public static String stringValue(Object value) {
		return stringValue(value, false);
	}

	public static Object toArray(char value, Class<?> toType) {
		return toArray(new Character(value), toType);
	}

	public static Object toArray(byte value, Class<?> toType) {
		return toArray(new Byte(value), toType);
	}

	public static Object toArray(int value, Class<?> toType) {
		return toArray(new Integer(value), toType);
	}

	public static Object toArray(long value, Class<?> toType) {
		return toArray(new Long(value), toType);
	}

	public static Object toArray(float value, Class<?> toType) {
		return toArray(new Float(value), toType);
	}

	public static Object toArray(double value, Class<?> toType) {
		return toArray(new Double(value), toType);
	}

	public static Object toArray(boolean value, Class<?> toType) {
		return toArray(new Boolean(value), toType);
	}

	public static Object convertValue(char value, Class<?> toType) {
		return convertValue(new Character(value), toType);
	}

	public static Object convertValue(byte value, Class<?> toType) {
		return convertValue(new Byte(value), toType);
	}

	public static Object convertValue(int value, Class<?> toType) {
		return convertValue(new Integer(value), toType);
	}

	public static Object convertValue(long value, Class<?> toType) {
		return convertValue(new Long(value), toType);
	}

	public static Object convertValue(float value, Class<?> toType) {
		return convertValue(new Float(value), toType);
	}

	public static Object convertValue(double value, Class<?> toType) {
		return convertValue(new Double(value), toType);
	}

	public static Object convertValue(boolean value, Class<?> toType) {
		return convertValue(new Boolean(value), toType);
	}


	public static Object convertValue(char value, Class<?> toType, boolean preventNull) {
		return convertValue(new Character(value), toType, preventNull);
	}

	public static Object convertValue(byte value, Class<?> toType, boolean preventNull) {
		return convertValue(new Byte(value), toType, preventNull);
	}

	public static Object convertValue(int value, Class<?> toType, boolean preventNull) {
		return convertValue(new Integer(value), toType, preventNull);
	}

	public static Object convertValue(long value, Class<?> toType, boolean preventNull) {
		return convertValue(new Long(value), toType, preventNull);
	}

	public static Object convertValue(float value, Class<?> toType, boolean preventNull) {
		return convertValue(new Float(value), toType, preventNull);
	}

	public static Object convertValue(double value, Class<?>toType, boolean preventNull) {
		return convertValue(new Double(value), toType, preventNull);
	}

	public static Object convertValue(boolean value, Class<?>toType, boolean preventNull) {
		return convertValue(new Boolean(value), toType, preventNull);
	}


	public static Object toArray(char value, Class<?>toType, boolean preventNull) {
		return toArray(new Character(value), toType, preventNull);
	}

	public static Object toArray(byte value, Class<?>toType, boolean preventNull) {
		return toArray(new Byte(value), toType, preventNull);
	}

	public static Object toArray(int value, Class<?>toType, boolean preventNull) {
		return toArray(new Integer(value), toType, preventNull);
	}

	public static Object toArray(long value, Class<?>toType, boolean preventNull) {
		return toArray(new Long(value), toType, preventNull);
	}

	public static Object toArray(float value, Class<?>toType, boolean preventNull) {
		return toArray(new Float(value), toType, preventNull);
	}

	public static Object toArray(double value, Class<?>toType, boolean preventNull) {
		return toArray(new Double(value), toType, preventNull);
	}

	public static Object toArray(boolean value, Class<?> toType, boolean preventNull) {
		return toArray(new Boolean(value), toType, preventNull);
	}

	public static Object convertValue(Object value, Class<?> toType) {
		return convertValue(value, toType, false);
	}

	public static Object toArray(Object value, Class<?> toType) {
		return toArray(value, toType, false);
	}

	public static Object toArray(Object value, Class<?> toType, boolean preventNulls) {
		if (value == null)
			return null;

		Object result = null;

		if (value.getClass().isArray() && toType.isAssignableFrom(value.getClass().getComponentType()))
			return value;

		if (!value.getClass().isArray()) {

			if (toType == Character.TYPE)
				return stringValue(value).toCharArray();

			Object arr = Array.newInstance(toType, 1);
			Array.set(arr, 0, convertValue(value, toType, preventNulls));

			return arr;
		}

		result = Array.newInstance(toType, Array.getLength(value));
		for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
			Array.set(result, i, convertValue(Array.get(value, i), toType));
		}

		if (result == null && preventNulls)
			return value;

		return result;
	}

	
	public static Object convertValue(Object value, Class<?> toType, boolean preventNulls) {
		Object result = null;

		if (value != null && toType.isAssignableFrom(value.getClass()))
			return value;

		if (value != null) {
			/* If array -> array then convert components of array individually */
			if (value.getClass().isArray() && toType.isArray()) {
				Class<?> componentType = toType.getComponentType();

				result = Array.newInstance(componentType, Array.getLength(value));
				for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
					Array.set(result, i, convertValue(Array.get(value, i), componentType));
				}
			} else if (value.getClass().isArray() && !toType.isArray()) {

				return convertValue(Array.get(value, 0), toType);
			} else if (!value.getClass().isArray() && toType.isArray()) {

				if (toType.getComponentType() == Character.TYPE) {
					result = stringValue(value).toCharArray();
				} else if (toType.getComponentType() == Object.class) {
					return new Object[] { value };
				}else if (toType.getComponentType() == String.class) {
					return new String[] { stringValue(value) };
				}else if (toType.getComponentType() == Long.class) {
					return new Long[] { longValue(value) };
				}else if (toType.getComponentType() == Double.class) {
					return new Double[] { doubleValue(value) };
				}if (toType.getComponentType() == Integer.class) {
					return new Integer[] { intValue(value) };
				}if (toType.getComponentType() == Float.class) {
					return new Float[] { floatValue(value) };
				}
				
			} else {
				if ((toType == Integer.class) || (toType == Integer.TYPE)) {
					result = new Integer((int) longValue(value));
				}
				if ((toType == Double.class) || (toType == Double.TYPE))
					result = new Double(doubleValue(value));
				if ((toType == Boolean.class) || (toType == Boolean.TYPE))
					result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
				if ((toType == Byte.class) || (toType == Byte.TYPE))
					result = new Byte((byte) longValue(value));
				if ((toType == Character.class) || (toType == Character.TYPE))
					result = new Character((char) longValue(value));
				if ((toType == Short.class) || (toType == Short.TYPE))
					result = new Short((short) longValue(value));
				if ((toType == Long.class) || (toType == Long.TYPE))
					result = new Long(longValue(value));
				if ((toType == Float.class) || (toType == Float.TYPE))
					result = new Float(doubleValue(value));
				if (toType == BigInteger.class)
					result = bigIntValue(value);
				if (toType == BigDecimal.class)
					result = bigDecValue(value);
				if (toType == String.class)
					result = stringValue(value);
				if (toType == Date.class)
					result = new Date(longValue(value));
			}
		} else {
			if (toType.isPrimitive()) {
				result = getDefaultValue(toType);
			} else if (preventNulls && toType == Boolean.class) {
				result = Boolean.FALSE;
			} else if (preventNulls && Number.class.isAssignableFrom(toType)) {
				result = getDefaultValue(toType);
			}
		}

		if (result == null && preventNulls)
			return value;

		if (value != null && result == null) {

			throw new IllegalArgumentException("Unable to convert type " + value.getClass().getName() + " of " + value + " to type of " + toType.getName());
		}

		return result;
	}

}