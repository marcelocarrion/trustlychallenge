package com.mcarrion.trustlychallenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Marcelo Carrion
 */
@JsonInclude(Include.NON_NULL)
public class RepoFilesetDTO
{
	private String extension;
	private Long lineCount;
	private Long byteSize;
	private String error;
	
	public RepoFilesetDTO() {}
	
	public RepoFilesetDTO(String extension, Long lineCount, Long byteSize)
	{
		this.extension = extension;
		this.lineCount = lineCount;
		this.byteSize = byteSize;
		this.error = null;
	}
	
	public RepoFilesetDTO(String error)
	{
		this.error = error;
		this.extension = null;
		this.lineCount = null;
		this.byteSize = null;
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
	
	public String getError()
	{
		return error;
	}
	
	public void setError(String error)
	{
		this.error = error;
	}
}
