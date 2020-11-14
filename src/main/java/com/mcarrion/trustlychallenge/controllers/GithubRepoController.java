package com.mcarrion.trustlychallenge.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mcarrion.trustlychallenge.dto.RepoFilesetDTO;
import com.mcarrion.trustlychallenge.services.GithubRepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Marcelo Carrion
 */
@RestController
@RequestMapping(value="/githubscan")
@Api(value="githubscan")
public class GithubRepoController
{
	@Autowired
	private GithubRepoService githubRepoService;
	
	@GetMapping(value="/loadFileInfo")
	@ApiOperation(value="Gets the total number of lines and bytes of all the files in a given Github repository, grouped by file extension")
	public ResponseEntity<List<RepoFilesetDTO>> loadFileInfo(
		@ApiParam(value="Repository owner", required=true) @RequestParam(name="owner") String owner,
		@ApiParam(value="Repository name", required=true) @RequestParam(name="repo") String repo)
	{
		List<RepoFilesetDTO> repoInfo = githubRepoService.loadFileInfo(owner, repo);
		return ResponseEntity.ok().body(repoInfo);
	}
}
