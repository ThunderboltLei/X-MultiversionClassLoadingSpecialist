Project:
    X-MultiversionClassLoadingSpecialist

AUTHOR:
    thunderbolt.lei

Title:
    Based on maven secondary development, multi-version dependency 
package transaction isolation is implemented

Information:
    This project is designed to address the use of multiple versions of JAR packages
    that use the same groupID and the same artifactID in MAVEN projects.
    
    For example, for corporate business reasons, it is possible that our application
    will use multiple versions of kafka or other components (versions are not uniform). 
    But when we build a project, we can only specify one version of the dependency package. 
    So that we can only put the application apart into multiple small applications, so that
    our development cost would be increased, and the realization of the multiple applications
    will be relatively tedious, has increased the deployment, the workload of the test.
    
    So it would be nice if only one application could solve the problem of 
    multiversion-dependent packages.
    
    Therefore, this program is based on maven source code to implement new features 
    that support multiple versions of the dependency package to solve such problems.

Notes:
    1、mess in mind, need to rearrange.
    
Milestones:
    1、2018-08-02 Complete the initial version development & all tests. 
