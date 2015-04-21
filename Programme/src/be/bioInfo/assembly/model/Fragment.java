package be.bioInfo.assembly.model;


import java.util.ArrayList;

import be.bioInfo.assembly.exception.FragmentException;

/**
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Fragment
{
	private String code;
	private ArrayList<Fragment> fragmentInclu;
	
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
	
	public ArrayList<Fragment> getFragmentInclu() {
		return fragmentInclu;
	}
	
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public Fragment()
	{
		this.fragmentInclu = new ArrayList<Fragment>();
	}
	
}
