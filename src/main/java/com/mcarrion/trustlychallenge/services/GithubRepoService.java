package com.mcarrion.trustlychallenge.services;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import com.mcarrion.trustlychallenge.domain.GithubRepo;
import com.mcarrion.trustlychallenge.domain.RepoFileset;
import com.mcarrion.trustlychallenge.dto.RepoFilesetDTO;
import com.mcarrion.trustlychallenge.repositories.IGithubRepoRepository;
import com.mcarrion.trustlychallenge.services.utils.RepoScanner;

/**
 * @author Marcelo Carrion
 */
@Service
public class GithubRepoService
{
	@Autowired
	private IGithubRepoRepository githubRepoRepository;
	
	public List<RepoFilesetDTO> loadFileInfo(String owner, String repo)
	{
		GithubRepo filter = new GithubRepo(null, owner, repo, null);
		Optional<GithubRepo> entity = githubRepoRepository.findOne(Example.of(filter));
		
		try
		{
			String baseUrl = "/" + owner + "/" + repo;
			URL url = new URL("https://github.com" + baseUrl);
			
			// retrieve contents of specified page
			Scanner sc = new Scanner(url.openStream());
			sc.useDelimiter("\\Z");
			
			String defaultBranch = null, lastCommit = null;
			int ind;
			while (sc.hasNextLine())
			{
				String content = sc.nextLine().trim();
				
				if (defaultBranch == null)
				{
					// branch info still to be retrieved
					if ((ind = content.indexOf("data-open-app=\"link\"")) != -1)
					{
						content = content.substring(ind);
						content = content.substring(0, content.indexOf("\">"));
						defaultBranch = content.substring(content.lastIndexOf("/") + 1);
					}
				}
				else
				{
					// branch info retrieved, commit info pending
					if ((ind = content.indexOf("js-permalink-shortcut")) != -1)
					{
						content = content.substring(ind);
						content = content.substring(0, content.indexOf("\">"));
						lastCommit = content.substring(content.lastIndexOf("/") + 1);
						break;
					}
				}
			}
			sc.close();
			
			if (defaultBranch == null || lastCommit == null)
			{
				throw new Exception("could not retrieve repo info");
			}
			
			GithubRepo repoInfo;
			if (entity.isPresent())
			{
				// repository was already scanned once
				repoInfo = entity.get();
				
				if (!lastCommit.equals(repoInfo.getLastCommitId()))
				{
					// repository has to be scanned again
					GithubRepo updatedInfo = RepoScanner.scan(owner, repo, defaultBranch, lastCommit, repoInfo.getId());
					
					repoInfo.setLastCommitId(lastCommit);
					refreshUpdatedFilesets(updatedInfo.getFilesets(), repoInfo.getFilesets());
					repoInfo.getFilesets().clear();
					repoInfo.getFilesets().addAll(updatedInfo.getFilesets());
					this.save(repoInfo);
				}
			}
			else
			{
				// first scan of repository
				repoInfo = RepoScanner.scan(owner, repo, defaultBranch, lastCommit, null);
				
				this.save(repoInfo);
			}
			
			return repoInfo.getFilesets().stream().map(RepoFileset::makeDTO).collect(Collectors.toList());
		}
		catch (FileNotFoundException e)
		{
			return Collections.singletonList(new RepoFilesetDTO("could not find github repo"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Collections.singletonList(new RepoFilesetDTO("exception! " + e.getMessage()));
		}
	}
	
	private void refreshUpdatedFilesets(List<RepoFileset> updatedFilesets, List<RepoFileset> oldFilesets)
	{
		Map<String, Long> extensionIds = new HashMap<>();
		
		// get ids from fileset extensions that already exist in repo
		for (RepoFileset fileset : oldFilesets)
		{
			extensionIds.put(fileset.getExtension(), fileset.getId());
		}
		
		// set their ids on the updated fileset list
		for (RepoFileset fileset : updatedFilesets)
		{
			fileset.setId(extensionIds.get(fileset.getExtension()));
		}
	}
	
	@Transactional
	public void save(GithubRepo entity)
	{
		githubRepoRepository.save(entity);
	}
}
