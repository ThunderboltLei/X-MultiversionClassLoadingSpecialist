package com.pyramid.entities;

import com.alibaba.fastjson.JSON;
import com.pyramid.loaders.XClassLoader;

import lombok.Getter;
import lombok.Setter;

/**
 * @author thunderbolt.lei
 *
 */
public class XClassAnnotationEntity {

	// fieldName-jarName
	@Setter
	@Getter
	private String key;

	// key 对应的类加载器
	@Setter
	@Getter
	private XClassLoader xClassLoader;

	public XClassAnnotationEntity() {
		this.key = null;
		this.xClassLoader = null;
	}

	public XClassAnnotationEntity(String key, XClassLoader xClassLoader) {
		this.key = key;
		this.xClassLoader = xClassLoader;
	}

	@Override
	public String toString() {
		return "XClassAnnotationEntity [key=" + key + ", xClassLoader=" + JSON.toJSONString(xClassLoader) + "]";
	}
}
