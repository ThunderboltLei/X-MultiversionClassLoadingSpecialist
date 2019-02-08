package com.pyramid.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class XRepoEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8573518686689705368L;

	@Setter
	@Getter
	private String mirrorOf;

	@Setter
	@Getter
	private String url;

	/**
	 * 
	 */
	public XRepoEntity() {
		super();
	}

	/**
	 * @param mirrorOf
	 * @param url
	 */
	public XRepoEntity(String mirrorOf, String url) {
		super();
		this.mirrorOf = mirrorOf;
		this.url = url;
	}

}
