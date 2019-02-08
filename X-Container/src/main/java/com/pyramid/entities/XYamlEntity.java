package com.pyramid.entities;

import java.util.List;

import org.apache.maven.settings.Mirror;

import lombok.Getter;
import lombok.Setter;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class XYamlEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1360726885675308891L;

	@Setter
	@Getter
	private XRepoEntity localRepo;

	@Setter
	@Getter
	private List<XRepoEntity> remoteRepoList;

	@Setter
	@Getter
	private List<XRepoEntity> pluginRepoList;

	@Setter
	@Getter
	private List<String> goalList;

	@Setter
	@Getter
	private List<Mirror> mirrorList;

}
