package ut.com.atlassian.maven.plugins.jgitflow.testutils;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * @since version
 */
public class RepoUtil
{
    public static Git createEmptyRepository(File dir) throws GitAPIException
    {
        Git git = Git.init().setDirectory(dir).setBare(true).call();

        return git;
    }

    public static Git createRepositoryWithMaster(File dir) throws GitAPIException
    {
        Git git = Git.init().setDirectory(dir).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("initial commit").call();

        return git;
    }

    public static Git createRepositoryWithMasterAndDevelop(File dir) throws GitAPIException
    {
        Git git = Git.init().setDirectory(dir).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("initial commit").call();
        git.branchCreate().setName("develop").call();
        git.commit().setMessage("added develop branch").call();

        return git;
    }

    public static Git createRepositoryWithMasterAndDevelopAndRelease(File dir,String release) throws GitAPIException
    {
        Git git = Git.init().setDirectory(dir).call();
        git.add().addFilepattern(".").call();
        git.commit().setMessage("initial commit").call();
        git.branchCreate().setName("release/" + release).call();
        git.commit().setMessage("added release branch").call();
        git.branchCreate().setName("develop").call();
        git.commit().setMessage("added develop branch").call();

        return git;
    }

    public static Git createRepositoryWithBranches(File dir, String... branches) throws GitAPIException
    {
        Git git = Git.init().setDirectory(dir).call();
        git.commit().setMessage("initial commit").call();

        for (String branch : branches)
        {
            git.branchCreate().setName(branch).call();
            git.commit().setMessage("added branch " + branch).call();
        }

        return git;
    }
}
