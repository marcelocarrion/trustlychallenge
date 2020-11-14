package com.mcarrion.trustlychallenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.mcarrion.trustlychallenge.controllers.GithubRepoController;
import com.mcarrion.trustlychallenge.dto.RepoFilesetDTO;
import com.mcarrion.trustlychallenge.services.GithubRepoService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=GithubRepoController.class)
public class GithubRepoControllerTest
{
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GithubRepoService githubRepoService;
	
	private String requestUri = "/githubscan/loadFileInfo?owner=marcelocarrion&repo=trustlychalleng";
	private String owner = "marcelocarrion";
	private String repo = "trustlychalleng";
	private static List<RepoFilesetDTO> filesets;
	
	@BeforeAll
	public static void setUp()
	{
		filesets = Collections.singletonList(new RepoFilesetDTO("could not find github repo"));
	}
	
	@Test
	public void shouldReturnRepoNotFoundWhenMisspelledName() throws Exception
	{
		Mockito.when(githubRepoService.loadFileInfo(owner, repo)).thenReturn(filesets);
		
		mockMvc.perform(get(requestUri))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value(filesets.get(0).getError()));
		
		Mockito.verify(githubRepoService).loadFileInfo(owner, repo);
	}
}
