package com.atlassian.maven.plugins.jgitflow.mojo;

import com.atlassian.maven.plugins.jgitflow.ReleaseContext;
import com.atlassian.maven.plugins.jgitflow.exception.MavenJGitFlowException;
import com.atlassian.maven.plugins.jgitflow.manager.FlowReleaseManager;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @since version
 */
@Mojo(name = "feature-start", aggregator = true)
public class FeatureStartMojo extends AbstractJGitFlowMojo
{

    /**
     * Default name of the feature. This option is primarily useful when starting the goal in non-interactive mode.
     */
    @Parameter(property = "featureName")
    private String featureName;

    /**
     * Whether to append the feature name to the version on the feature branch.
     */
    @Parameter(defaultValue = "false", property = "enableFeatureVersions")
    private boolean enableFeatureVersions = false;

    /**
     * Whether to push feature branches to the remote upstream.
     */
    @Parameter(defaultValue = "false", property = "pushFeatures")
    private boolean pushFeatures = false;

    /**
     * A SHA, short SHA, or branch name to use as the starting point for the new branch
     */
    @Parameter(property = "startCommit", defaultValue = "")
    private String startCommit = "";

    @Component(hint = "feature")
    FlowReleaseManager releaseManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        ReleaseContext ctx = new ReleaseContext(getBasedir());
        ctx.setInteractive(getSettings().isInteractiveMode())
           .setDefaultFeatureName(featureName)
           .setEnableFeatureVersions(enableFeatureVersions)
           .setEnableSshAgent(enableSshAgent)
           .setAllowUntracked(allowUntracked)
           .setAllowSnapshots(allowSnapshots)
           .setPushFeatures(pushFeatures)
           .setStartCommit(startCommit)
           .setAllowRemote(isRemoteAllowed())
           .setAlwaysUpdateOrigin(alwaysUpdateOrigin)
           .setDefaultOriginUrl(defaultOriginUrl)
           .setPullMaster(pullMaster)
           .setPullDevelop(pullDevelop)
           .setScmCommentPrefix(scmCommentPrefix)
           .setScmCommentSuffix(scmCommentSuffix)
           .setUseReleaseProfile(false)
           .setUsername(username)
           .setPassword(password)
                .setEol(eol)
           .setFlowInitContext(getFlowInitContext().getJGitFlowContext());

        try
        {
            releaseManager.start(ctx, getReactorProjects(), session);
        }
        catch (MavenJGitFlowException e)
        {
            throw new MojoExecutionException("Error starting feature: " + e.getMessage(), e);
        }
    }
}
