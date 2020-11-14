package com.mcarrion.trustlychallenge.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.mcarrion.trustlychallenge.dto.RepoFilesetDTO;

/**
 * RepoFileset represents a set of all the files with a given extension in a Github repository:
 * it informs the total number of lines and the total number of bytes of that fileset.
 * @author Marcelo Carrion
 */
@Entity
@Table(name="repofileset")
public class RepoFileset implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="githubrepo_id", nullable=false)
	private GithubRepo repo;
	
	private String extension;
	
	private Long lineCount;
	
	private Long byteSize;
	
	public RepoFileset() {}
	
	public RepoFileset(GithubRepo repo, String extension, Long lineCount, Long byteSize)
	{
		this.repo = repo;
		this.extension = extension;
		this.lineCount = lineCount;
		this.byteSize = byteSize;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public GithubRepo getRepo()
	{
		return repo;
	}
	
	public void setRepo(GithubRepo repo)
	{
		this.repo = repo;
	}
	
	public String getExtension()
	{
		return extension;
	}
	
	public void setExtension(String extension)
	{
		this.extension = extension;
	}
	
	public Long getLineCount()
	{
		return lineCount;
	}
	
	public void setLineCount(Long lineCount)
	{
		this.lineCount = lineCount;
	}
	
	public Long getByteSize()
	{
		return byteSize;
	}
	
	public void setByteSize(Long byteSize)
	{
		this.byteSize = byteSize;
	}
	
	public RepoFilesetDTO makeDTO()
	{
		return new RepoFilesetDTO(extension, lineCount, byteSize);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepoFileset other = (RepoFileset) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}
