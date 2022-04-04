package com.atlassian.maven.jgitflow.api;

import com.atlassian.jgitflow.core.JGitFlowInfo;
import com.atlassian.maven.jgitflow.api.exception.MavenJGitFlowExtensionException;

public interface MavenReleaseStartExtension extends StartProductionBranchExtension
{
    /**
     * Called when the version changes on the develop branch.
     * <p></p>
     * This method is called AFTER the poms have been committed.
     * Any changes made to the project within this method will need to also be committed within this method.
     * A helper class is provided to make this easier. {@link com.atlassian.maven.jgitflow.api.util.JGitFlowCommitHelper}
     *
     * @throws com.atlassian.maven.jgitflow.api.exception.MavenJGitFlowExtensionException
     */
    void onDevelopBranchVersionChange(String newVersion, String oldVersion, JGitFlowInfo flow) throws MavenJGitFlowExtensionException;
}
