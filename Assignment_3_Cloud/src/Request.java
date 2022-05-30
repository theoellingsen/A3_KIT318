/*
 * @author Joshua Perrin
 * KIT318
 * Request class definition
 * Defines the request object type
 */

public class Request {

	private String requestingUsername;
    private String type;
    private String stringInput;
    private String inputFilename;
    private int deadline;
    private int code;
    private int priority;
    private String status;
    private String stringOutput;
    private String outputFilename;
	
	//request constructor
    public Request(String requestingUsername, String type, String stringInput, String inputFilename, 
    		int deadline, int code, int priority, String status, String stringOutput, String outputFilename) {

    	this.requestingUsername = requestingUsername;
    	this.type = type;
    	this.stringInput = stringInput;
    	this.inputFilename = inputFilename;
    	this.deadline = deadline;
    	this.code = code;
        this.priority = priority;
        this.status = status;
        this.stringOutput = stringOutput;
        this.outputFilename = outputFilename;
    }
    
    public String getUsername(Request r) {
        return requestingUsername;
    }
    
    public String getType(Request r) {
        return type;
    }
    
    public String getStringInput(Request r) {
        return stringInput;
    }
    
    public String getInputFilename(Request r) {
        return inputFilename;
    }
    
    public int getDeadline(Request r) {
        return deadline;
    }
    
    public int getCode(Request r) {
        return code;
    }
    
    public int getPriority(Request r) {
        return priority;
    }
    
    public String getStatus(Request r) {
        return status;
    }
    
    public void setStatus(String s) {
        status = s;
    }
    
    public String getStringOutput(Request r) {
        return stringOutput;
    }
    
    public String getOutputFilename(Request r) {
        return outputFilename;
    }
}
