package be.bioInfo.assembly.model;


import be.bioInfo.assembly.exception.FragmentException;

public class Fragment
{
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) throws FragmentException {
		this.code = code.toUpperCase();
	
	}
	
}
