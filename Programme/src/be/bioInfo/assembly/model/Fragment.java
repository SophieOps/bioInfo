package be.bioInfo.assembly.model;


import be.bioInfo.assembly.exception.FragmentException;

/**
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Fragment
{
	private String code;

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 * @throws FragmentException
	 */
	public void setCode(String code) throws FragmentException {
		this.code = code.toUpperCase();
	
	}
	
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public Fragment() {}
	
}
