package com.mcarrion.trustlychallenge.services.utils;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.mcarrion.trustlychallenge.domain.GithubRepo;
import com.mcarrion.trustlychallenge.domain.RepoFileset;

/**
 * @author Marcelo Carrion
 */
public class RepoScanner
{
	public static long obtainLineCountOfStream(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader.lines().count();
	}
	
	public static GithubRepo scan(String owner, String repo, String defaultBranchZip, String lastCommitId, Long githubRepoId) throws IOException
	{
		URL url = new URL("https://github.com/" + owner + "/" + repo + "/archive/" + defaultBranchZip);
		ZipInputStream stream = new ZipInputStream(url.openStream());
		
		Map<String, Long> lineCount = new HashMap<>();
		Map<String, Long> byteSize = new HashMap<>();
		
		ZipEntry entry;
		while ((entry = stream.getNextEntry()) != null)
		{
			if (entry.getSize() >= 0)
			{
				String file = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
				int ind = file.lastIndexOf('.');
				String extension = (ind != -1) ? file.substring(ind + 1) : "<none>";
				
				byteSize.put(extension, entry.getSize() + (byteSize.containsKey(extension) ? byteSize.get(extension) : 0));
				
				long count = obtainLineCountOfStream(new FilterInputStream(stream) {
					@Override
					public void close() throws IOException
					{
						stream.closeEntry();
					}
				});
				lineCount.put(extension, count + (lineCount.containsKey(extension) ? lineCount.get(extension) : 0));
			}
		}
		stream.close();
		
		GithubRepo githubRepo = new GithubRepo(githubRepoId, owner, repo, lastCommitId);
		List<RepoFileset> filesets = new ArrayList<>();
		for (String extension : lineCount.keySet())
		{
			RepoFileset fileset = new RepoFileset(githubRepo, extension, lineCount.get(extension), byteSize.get(extension));
			filesets.add(fileset);
		}
		githubRepo.setFilesets(filesets);
		
		return githubRepo;
	}
}
