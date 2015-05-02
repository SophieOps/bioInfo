package be.bioInfo.assembly.model;



import java.util.regex.Pattern;
import be.bioInfo.assembly.exception.FragmentException;

/**
 * Class of a fragment
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Fragment
{
	//Pattern to check if we have valid DNA
	private static Pattern validCode =Pattern.compile("^[atcgATCG]+$");
	//The charactere in the fragment
	private String code;

	/**
	 * @return The charactere in the fragment
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Constructor of the fragment
	 * @param code : The charactere in the fragment
	 * @throws FragmentException
	 */
	public Fragment(String code) throws FragmentException
	{
		if (!validCode.matcher(code).matches()) {
            throw new FragmentException(code +" invalide");
        }
		this.code = code.toUpperCase();
	}
	
}
