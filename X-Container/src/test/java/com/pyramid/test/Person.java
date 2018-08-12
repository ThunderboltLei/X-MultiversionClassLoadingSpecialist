package com.pyramid.test;

/**
 * @author thunderbolt.lei
 *
 */
public class Person {

    @Name("阿特罗伯斯")
    private String name;

    @Gender(gender = Gender.GenderType.Male)
    private String gender;

    @Profile(id = 1001, height = 180, nativePlace = "CN")
    private String profile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
