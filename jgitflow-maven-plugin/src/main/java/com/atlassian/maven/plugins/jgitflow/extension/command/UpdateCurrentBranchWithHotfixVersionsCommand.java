package com.atlassian.maven.plugins.jgitflow.extension.command;

import java.util.List;

import com.atlassian.jgitflow.core.BranchType;
import com.atlassian.jgitflow.core.GitFlowConfiguration;
import com.atlassian.jgitflow.core.command.JGitFlowCommand;
import com.atlassian.jgitflow.core.exception.JGitFlowExtensionException;
import com.atlassian.jgitflow.core.extension.ExtensionCommand;
import com.atlassian.jgitflow.core.extension.ExtensionFailStrategy;
import com.atlassian.maven.plugins.jgitflow.ReleaseContext;
import com.atlassian.maven.plugins.jgitflow.helper.BranchHelper;
import com.atlassian.maven.plugins.jgitflow.helper.PomUpdater;
import com.atlassian.maven.plugins.jgitflow.helper.ProjectHelper;
import com.atlassian.maven.plugins.jgitflow.provider.ContextProvider;
import com.atlassian.maven.plugins.jgitflow.provider.VersionCacheProvider;
import com.atlassian.maven.plugins.jgitflow.provider.VersionProvider;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.eclipse.jgit.api.Git;

@Component(role = UpdateCurrentBranchWithHotfixVersionsCommand.class)
public class UpdateCurrentBranchWithHotfixVersionsCommand implements ExtensionCommand
{
    @Requirement
    private VersionCacheProvider versionCacheProvider;

    @Requirement
    private PomUpdater pomUpdater;

    @Requirement
    private BranchHelper branchHelper;

    @Requirement
    private VersionProvider versionProvider;

    @Requirement
    private ProjectHelper projectHelper;

    @Requirement
    private ContextProvider contextProvider;

    @Override
    public void execute(GitFlowConfiguration configuration, Git git, JGitFlowCommand gitFlowCommand) throws JGitFlowExtensionException
    {
        try
        {
            ReleaseContext ctx = contextProvider.getContext();

            versionCacheProvider.cacheCurrentBranchVersions();

            String currentName = branchHelper.getCurrentBranchName();

            List<MavenProject> currentProjects = branchHelper.getProjectsForCurrentBranch();

            List<MavenProject> hotfixProjects = branchHelper.getProjectsForTopicBranch(BranchType.HOTFIX);

            pomUpdater.copyPomVersionsFromProject(hotfixProjects, currentProjects);
            projectHelper.commitAllPoms(git, currentProjects, ctx.getScmCommentPrefix() + "Updating " + currentName + " poms to hotfix version to avoid merge conflicts" + ctx.getScmCommentSuffix());
        }
        catch (Exception e)
        {
            throw new JGitFlowExtensionException("Error updating current branch poms to hotfix version", e);
        }

    }

    @Override
    public ExtensionFailStrategy failStrategy()
    {
        return ExtensionFailStrategy.ERROR;
    }
}
