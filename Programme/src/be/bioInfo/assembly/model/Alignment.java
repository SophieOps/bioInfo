package be.bioInfo.assembly.model;

/**
 * Data of an alignment 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Alignment
{
	public static final char GAP ='-';
	public static final char BORDER ='*';
	
	//type de l'alignement
	private AlignmentType type;
	//index du debut de l'alignement
	private int startIndex;
	//alignement 1
    private String code1;
    //alignement 2
    private String code2;
    //cout de l'alignement
    private int cost;
    
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
    public Alignment() {        
    }
    
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getCode1() {
		return code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public AlignmentType getType() {
		return type;
	}

	public void setType(AlignmentType type) {
		this.type = type;
	}
    
    

}
