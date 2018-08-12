package com.pyramid.entities;

import java.util.List;

import org.apache.maven.settings.Mirror;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class XYamlEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1360726885675308891L;

    private XRepoEntity localRepo;
    private List<XRepoEntity> remoteRepoList;
    private List<XRepoEntity> pluginRepoList;
    private List<String> goalList;
    private List<Mirror> mirrorList;

    public XYamlEntity() {

    }

    public XRepoEntity getLocalRepo() {
        return localRepo;
    }

    public void setLocalRepo(XRepoEntity localRepo) {
        this.localRepo = localRepo;
    }

    public List<XRepoEntity> getRemoteRepoList() {
        return remoteRepoList;
    }

    public void setRemoteRepoList(List<XRepoEntity> remoteRepoList) {
        this.remoteRepoList = remoteRepoList;
    }

    public List<XRepoEntity> getPluginRepoList() {
        return pluginRepoList;
    }

    public void setPluginRepoList(List<XRepoEntity> pluginRepoList) {
        this.pluginRepoList = pluginRepoList;
    }

    public List<String> getGoalList() {
        return goalList;
    }

    public void setGoalList(List<String> goalList) {
        this.goalList = goalList;
    }

    public List<Mirror> getMirrorList() {
        return mirrorList;
    }

    public void setMirrorList(List<Mirror> mirrorList) {
        this.mirrorList = mirrorList;
    }

}
