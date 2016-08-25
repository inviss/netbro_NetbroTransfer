package kr.co.d2net.netbro.common.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class ClassUtils {
	
	final static Logger logger = LoggerFactory.getLogger(ClassUtils.class);
	
	public static final char PACKAGE_SEPARATOR_CHAR = '.';
	public static final String PACKAGE_SEPARATOR = String.valueOf('.');
	public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
	public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');

	private static Map<Class, Map> propertyDescriptorMapCache = new HashMap<Class, Map>();
	private static Map<Class, PropertyDescriptor[]> propertyDescriptorArrayCache = new HashMap<Class, PropertyDescriptor[]>();
	private static final Map genericActualTypeCacheMap = new HashMap();
	private static Map fieldCache = new HashMap();

	private static Map primitiveWrapperMap = new HashMap();
	private static Map abbreviationMap;

	public static String getShortClassName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getShortClassName(object.getClass().getName());
	}

	public static String getShortClassName(Class cls) {
		if (cls == null) {
			return "";
		}
		return getShortClassName(cls.getName());
	}

	public static String getShortClassName(String className) {
		if (className == null) {
			return "";
		}
		if (className.length() == 0) {
			return "";
		}
		char[] chars = className.toCharArray();
		int lastDot = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '.')
				lastDot = i + 1;
			else if (chars[i] == '$') {
				chars[i] = '.';
			}
		}
		return new String(chars, lastDot, chars.length - lastDot);
	}

	public static String getPackageName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getPackageName(object.getClass().getName());
	}

	public static String getPackageName(Class cls) {
		if (cls == null) {
			return "";
		}
		return getPackageName(cls.getName());
	}

	public static String getPackageName(String className) {
		if (className == null) {
			return "";
		}
		int i = className.lastIndexOf('.');
		if (i == -1) {
			return "";
		}
		return className.substring(0, i);
	}
	
	public static Annotation[] getAnnotations(Class cls)
    {
        return cls.getAnnotations();
    }

    public static Annotation getAnnotation(Class target, Class annotationClass)
    {
        if(annotationClass == null || target == null)
            throw new NullPointerException();
        Annotation t = target.getAnnotation(annotationClass);
        if(t != null)
            return t;
        for(Class superclass = target.getSuperclass(); superclass != null; superclass = superclass.getSuperclass())
        {
            t = superclass.getAnnotation(annotationClass);
            if(t != null)
                return t;
        }

        return null;
    }

    public static Field getAnnotationedField(Class<?> target, Class annotationClass) {
        if(annotationClass == null || target == null)
            throw new NullPointerException();
        Map<String, Field> fields = getAllFields(target);
        for(Iterator<Entry<String, Field>> iterator = fields.entrySet().iterator(); iterator.hasNext();)
        {
            Entry<String, Field> e = iterator.next();
            Field field = (Field)e.getValue();
            Annotation ann = field.getAnnotation(annotationClass);
            if(ann != null)
                return field;
        }

        return null;
    }
    
    
    
    public static Map<String, Field> getDeclaredFields(Class<?> targetClass)
    {
        Map<String, Field> result = new HashMap<String, Field>();
        Field fa[] = targetClass.getDeclaredFields();
        for(int i = 0; i < fa.length; i++) {
            result.put(fa[i].getName(), fa[i]);
        }
        return result;
    }

    public static synchronized Map<String, Field> getAllFields(Class<?> inClass)
    {
        Map<String, Field> result = (Map<String, Field>)fieldCache.get(inClass);
        if(result != null)
            return result;
        result = new HashMap<String, Field>();
        result.putAll(getDeclaredFields(inClass));
        List allClass = getAllSuperclasses(inClass);
        for(Iterator iterator = allClass.iterator(); iterator.hasNext();)
        {
            Class cls = (Class)iterator.next();
            Map fields = getDeclaredFields(cls);
            for(Iterator iterator1 = fields.entrySet().iterator(); iterator1.hasNext();)
            {
                java.util.Map.Entry e = (java.util.Map.Entry)iterator1.next();
                if(!result.containsKey(e.getKey()))
                    result.put((String)e.getKey(), (Field)e.getValue());
            }
        }
        fieldCache.put(inClass, result);
        return result;
    }
    
    public static Class<?> getGenericActualType(Class<?> inClass, int index)
    {
        String key = (new StringBuilder(String.valueOf(inClass.getName()))).append("_").append(index).toString();
        Class<?> result = (Class<?>)genericActualTypeCacheMap.get(key);
        if(result != null)
            return result;
        Type genType = inClass.getGenericSuperclass();
        if(!(genType instanceof ParameterizedType))
            return null;
        Type params[] = ((ParameterizedType)genType).getActualTypeArguments();
        if(index >= params.length || index < 0)
            throw new RuntimeException("Index outof bounds");
        if(!(params[index] instanceof Class))
        {
            return null;
        } else
        {
            result = (Class<?>)params[index];
            genericActualTypeCacheMap.put(key, result);
            return result;
        }
    }

	public static List getAllSuperclasses(Class cls) {
		if (cls == null) {
			return null;
		}
		List classes = new ArrayList();
		Class superclass = cls.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}

	public static List getAllInterfaces(Class cls) {
		if (cls == null) {
			return null;
		}
		List list = new ArrayList();
		while (cls != null) {
			Class[] interfaces = cls.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				if (!list.contains(interfaces[i])) {
					list.add(interfaces[i]);
				}
				List superInterfaces = getAllInterfaces(interfaces[i]);
				for (Iterator it = superInterfaces.iterator(); it.hasNext();) {
					Class intface = (Class) it.next();
					if (!list.contains(intface)) {
						list.add(intface);
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return list;
	}

	public static List convertClassNamesToClasses(List classNames) {
		if (classNames == null) {
			return null;
		}
		List classes = new ArrayList(classNames.size());
		for (Iterator it = classNames.iterator(); it.hasNext();) {
			String className = (String) it.next();
			try {
				classes.add(Class.forName(className));
			} catch (Exception ex) {
				classes.add(null);
			}
		}
		return classes;
	}

	public static List convertClassesToClassNames(List classes) {
		if (classes == null) {
			return null;
		}
		List classNames = new ArrayList(classes.size());
		for (Iterator it = classes.iterator(); it.hasNext();) {
			Class cls = (Class) it.next();
			if (cls == null)
				classNames.add(null);
			else {
				classNames.add(cls.getName());
			}
		}
		return classNames;
	}

	public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
		if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
			return false;
		}
		if (classArray == null) {
			classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		if (toClassArray == null) {
			toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		for (int i = 0; i < classArray.length; i++) {
			if (!isAssignable(classArray[i], toClassArray[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAssignable(Class cls, Class toClass) {
		if (toClass == null) {
			return false;
		}

		if (cls == null) {
			return !toClass.isPrimitive();
		}
		if (cls.equals(toClass)) {
			return true;
		}
		if (cls.isPrimitive()) {
			if (!toClass.isPrimitive()) {
				return false;
			}
			if (Integer.TYPE.equals(cls)) {
				return (Long.TYPE.equals(toClass)) || (Float.TYPE.equals(toClass)) || (Double.TYPE.equals(toClass));
			}

			if (Long.TYPE.equals(cls)) {
				return (Float.TYPE.equals(toClass)) || (Double.TYPE.equals(toClass));
			}

			if (Boolean.TYPE.equals(cls)) {
				return false;
			}
			if (Double.TYPE.equals(cls)) {
				return false;
			}
			if (Float.TYPE.equals(cls)) {
				return Double.TYPE.equals(toClass);
			}
			if (Character.TYPE.equals(cls)) {
				return (Integer.TYPE.equals(toClass)) || (Long.TYPE.equals(toClass)) || (Float.TYPE.equals(toClass)) || (Double.TYPE.equals(toClass));
			}

			if (Short.TYPE.equals(cls)) {
				return (Integer.TYPE.equals(toClass)) || (Long.TYPE.equals(toClass)) || (Float.TYPE.equals(toClass)) || (Double.TYPE.equals(toClass));
			}

			if (Byte.TYPE.equals(cls)) {
				return (Short.TYPE.equals(toClass)) || (Integer.TYPE.equals(toClass)) || (Long.TYPE.equals(toClass)) || (Float.TYPE.equals(toClass))
						|| (Double.TYPE.equals(toClass));
			}

			return false;
		}
		return toClass.isAssignableFrom(cls);
	}

	public static Class primitiveToWrapper(Class cls) {
		Class convertedClass = cls;
		if ((cls != null) && (cls.isPrimitive())) {
			convertedClass = (Class) primitiveWrapperMap.get(cls);
		}
		return convertedClass;
	}

	public static Class[] primitivesToWrappers(Class[] classes) {
		if (classes == null) {
			return null;
		}

		if (classes.length == 0) {
			return classes;
		}

		Class[] convertedClasses = new Class[classes.length];
		for (int i = 0; i < classes.length; i++) {
			convertedClasses[i] = primitiveToWrapper(classes[i]);
		}
		return convertedClasses;
	}

	public static boolean isInnerClass(Class cls) {
		if (cls == null) {
			return false;
		}
		return cls.getName().indexOf('$') >= 0;
	}

	public static Class getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
		Class clazz;
		if (abbreviationMap.containsKey(className)) {
			String clsName = "[" + abbreviationMap.get(className);
			clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
		} else {
			clazz = Class.forName(toProperClassName(className), initialize, classLoader);
		}
		return clazz;
	}

	public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
		return getClass(classLoader, className, true);
	}

	public static Class getClass(String className) throws ClassNotFoundException {
		return getClass(className, true);
	}

	public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
		ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
		ClassLoader loader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
		return getClass(loader, className, initialize);
	}

	public static Method getPublicMethod(Class cls, String methodName, Class[] parameterTypes) throws SecurityException, NoSuchMethodException {
		Method declaredMethod = cls.getMethod(methodName, parameterTypes);
		if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
			return declaredMethod;
		}

		List candidateClasses = new ArrayList();
		candidateClasses.addAll(getAllInterfaces(cls));
		candidateClasses.addAll(getAllSuperclasses(cls));

		for (Iterator it = candidateClasses.iterator(); it.hasNext();) {
			Class candidateClass = (Class) it.next();
			if (!Modifier.isPublic(candidateClass.getModifiers()))
				continue;
			Method candidateMethod;
			try {
				candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException ex) {
				continue;
			}
			if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
				return candidateMethod;
			}
		}

		throw new NoSuchMethodException("Can't find a public method for " + methodName );
	}

	private static String toProperClassName(String className) {
		className = StringUtils.deleteWhitespace(className);
		if (className == null)
			throw new NullPointerException("className");
		if (className.endsWith("[]")) {
			StringBuffer classNameBuffer = new StringBuffer();
			while (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
				classNameBuffer.append("[");
			}
			String abbreviation = (String) abbreviationMap.get(className);
			if (abbreviation != null)
				classNameBuffer.append(abbreviation);
			else {
				classNameBuffer.append("L").append(className).append(";");
			}
			className = classNameBuffer.toString();
		}
		return className;
	}

	static {
		primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
		primitiveWrapperMap.put(Byte.TYPE, Byte.class);
		primitiveWrapperMap.put(Character.TYPE, Character.class);
		primitiveWrapperMap.put(Short.TYPE, Short.class);
		primitiveWrapperMap.put(Integer.TYPE, Integer.class);
		primitiveWrapperMap.put(Long.TYPE, Long.class);
		primitiveWrapperMap.put(Double.TYPE, Double.class);
		primitiveWrapperMap.put(Float.TYPE, Float.class);
		primitiveWrapperMap.put(Void.TYPE, Void.TYPE);

		abbreviationMap = new HashMap();

		abbreviationMap.put("int", "I");
		abbreviationMap.put("boolean", "Z");
		abbreviationMap.put("float", "F");
		abbreviationMap.put("long", "J");
		abbreviationMap.put("short", "S");
		abbreviationMap.put("byte", "B");
		abbreviationMap.put("double", "D");
		abbreviationMap.put("char", "C");
	}

	public static PropertyDescriptor[] getPropertyDescriptorsArray(Class targetClass) throws IntrospectionException {
		PropertyDescriptor[] result = null;
		if (targetClass != null) {
			synchronized (propertyDescriptorArrayCache) {
				if ((result = (PropertyDescriptor[]) propertyDescriptorArrayCache.get(targetClass)) == null) {
					propertyDescriptorArrayCache.put(targetClass, result = Introspector.getBeanInfo(targetClass).getPropertyDescriptors());
				}
			}
		}
		return result;
	}

	public static Map<String, PropertyDescriptor> getPropertyDescriptorsMap(Class targetClass) throws IntrospectionException {
		Map<String, PropertyDescriptor> result = null;
		if (targetClass == null)
			return null;

		synchronized (propertyDescriptorMapCache) {
			if ((result = propertyDescriptorMapCache.get(targetClass)) == null) {
				result = new HashMap<String, PropertyDescriptor>();

				PropertyDescriptor[] pds;
					pds = getPropertyDescriptorsArray(targetClass);
					for (PropertyDescriptor pd : pds) {
						result.put(pd.getName(), pd);
					}
					propertyDescriptorMapCache.put(targetClass, result);

			}
		}

		return result;
	}

	public static PropertyDescriptor getPropertyDescriptor(Class targetClass, String name){

		Map<String, PropertyDescriptor> pdMap;
		try {
			pdMap = getPropertyDescriptorsMap(targetClass);
			if (pdMap == null || pdMap.isEmpty()) {
				return null;
			}

			return pdMap.get(name);
		} catch (IntrospectionException e) {
			return null;
		}

		
	}

	public static Map<String, Field> getFields(Class targetClass) {
		Map result;
		synchronized (fieldCache) {
			if ((result = (Map) fieldCache.get(targetClass)) == null) {
				result = new HashMap(23);
				Field[] fa = targetClass.getDeclaredFields();
				for (int i = 0; i < fa.length; i++) {
					result.put(fa[i].getName(), fa[i]);
				}
				fieldCache.put(targetClass, result);
			}
		}
		return result;
	}

	public static Field getField(Class inClass, String name) {
		Field result = null;
		for (Class cls = inClass; (cls != null); cls = inClass.getSuperclass()) {
			Map<String, Field> fields = getFields(inClass);
			result = fields.get(name);
			if (result != null)
				break;
		}

		return result;
	}
}
