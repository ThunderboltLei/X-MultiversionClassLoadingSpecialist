package com.pyramid.entities;

/**
 * @author thunderbolt.lei <br>
 *
 */
public class XRepoEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8573518686689705368L;

    private String mirrorOf;
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

    public String getMirrorOf() {
        return mirrorOf;
    }

    public void setMirrorOf(String mirrorOf) {
        this.mirrorOf = mirrorOf;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
