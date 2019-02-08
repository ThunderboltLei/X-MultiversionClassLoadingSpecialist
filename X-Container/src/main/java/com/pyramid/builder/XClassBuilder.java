package com.pyramid.builder;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pyramid.annotations.XClassAnnotation;
import com.pyramid.annotations.XJarAnnotation;
import com.pyramid.entities.XClassAnnotationEntity;
import com.pyramid.loaders.XClassLoader;
import com.pyramid.utils.JarUtil;

/**
 * @author thunderbolt.lei <br>
 * 
 *         To structure the XClassEntity object according to the
 *         annotations.<br>
 */
public class XClassBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(XClassBuilder.class);

	Object obj;

	public XClassBuilder() {

	}

	public XClassBuilder(Class<?> clazz) {
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		init(clazz);
	}

	/**
	 * @param clazz
	 *            the class which use self-defined annotations, is to be structured
	 *            the XClassEntity object.
	 * @return
	 */
	public void init(Class<?> clazz) {

		List<Field> fields = FieldUtils.getAllFieldsList(clazz);

		fields.forEach(field -> {

			Class<?> klass = field.getDeclaringClass();
			field.setAccessible(true);

			if (field.isAnnotationPresent(XJarAnnotation.class)) {

				XJarAnnotation xjea = (XJarAnnotation) field.getAnnotation(XJarAnnotation.class);

				XClassLoader xLoader = null;
				List<File> jarList = JarUtil.getAllJars(xjea.jarPath());
				xLoader = loadJars(jarList);

				try {
					// // method 1 - ok
					// Class c = XClassEntity.class;
					// XClassEntity obj = (XClassEntity) c.newInstance();
					// Field keyField = c.getDeclaredField("key");
					// keyField.setAccessible(true);
					// keyField.set(obj,
					// field.getName() + "|" + xjea.jarName() + "-" + xjea.version());
					// Field loaderField = c.getDeclaredField("xClassLoader");
					// loaderField.setAccessible(true);
					// loaderField.set(obj, xLoader);

					if (field.isAnnotationPresent(XClassAnnotation.class)) {
						XClassAnnotation xcea = (XClassAnnotation) field.getAnnotation(XClassAnnotation.class);

						// Method m1 = c.getMethod("setKey", String.class);
						// m1.invoke(obj,
						// field.getName() + "|" + xjea.jarName() + "-" + xjea.version());
						// Method m2 = c.getMethod("setxClassLoader", XClassLoader.class);
						// m2.invoke(obj, xLoader);

						// Finally - OK
						// field.setAccessible(true);
						// field.set(klass.newInstance(), new XClassEntity(
						// field.getName() + "|" + xjea.jarName() + "-" + xjea.version(),
						// xLoader));

						XClassAnnotationEntity xClassAnnoEntity = new XClassAnnotationEntity(
								field.getName() + "|" + xjea.jarName() + "-" + xjea.version(), xLoader);
						FieldUtils.writeField(obj, field.getName(), xClassAnnoEntity, true);
						// FieldUtils.writeDeclaredField(obj, field.getName(), xClassAnnoEntity,
						// true);

						// Field f = FieldUtils.getField(clazz, field.getName(), true);
						// System.out.println("field is null: " + (null == f));
						// FieldUtils.writeDeclaredField(obj, f.getName(), xClassAnnoEntity, true);
						// // System.out.println(xClassAnnoEntity.toString());
						// System.out.println("--->>> " + field.getName());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * To load jar files into memory<br>
	 * 
	 * @param jarFile
	 * @return
	 */
	public static XClassLoader loadJar(String jarFile) {
		XClassLoader xLoader = null;
		try {
			File file = new File(jarFile);
			if (!file.exists()) {
				throw new RuntimeException("[" + jarFile + "] is not exist.");
			}
			xLoader = new XClassLoader(deduplication(new URL[] { file.toURI().toURL() }),
					Thread.currentThread().getContextClassLoader());
			return xLoader;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// // to do nothing
			// ClassLoaderUtil.releaseLoader(xLoader);
		}
		return null;
	}

	/**
	 * To create a XClassLoader object by a jar list.
	 * 
	 * @param jarList
	 *            a jar list
	 * @return
	 */
	public static XClassLoader loadJars(List<File> jarList) {

		List<URL> urlList = new ArrayList<URL>();
		jarList.forEach(x -> {
			try {
				urlList.add(x.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		});
		return loadJars(urlList.toArray(new URL[urlList.size()]));
	}

	/**
	 * 
	 * @param urls
	 * @return
	 */
	public static XClassLoader loadJars(URL[] urls) {
		// LOG.info("before duplication --->>> \n{}\n", urls.length);
		URL[] dedupURLs = deduplication(urls);
		// LOG.info("after duplication --->>> \n{}\n", dedupURLs.length);
		XClassLoader xLoader = new XClassLoader(dedupURLs,
				// Thread.currentThread().getContextClassLoader());
				ClassLoader.getSystemClassLoader());
		return xLoader;
	}

	/**
	 * To reflect a class to an object.<br>
	 * 
	 * @param xClassEntity
	 * @param className
	 * @return
	 */
	public static Object reflectClass2Object(XClassAnnotationEntity xClassEntity, String className) {
		try {
			return Class.forName(className, true, xClassEntity.getXClassLoader()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * To deduplicate.<br>
	 * 
	 * @param urls
	 * @return
	 */
	protected static URL[] deduplication(URL[] urls) {
		List<URL> list = Arrays.asList(urls);
		Set<URL> set = new HashSet<URL>(list);
		return set.toArray(new URL[set.size()]);
	}

}
