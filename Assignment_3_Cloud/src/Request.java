/*
 * @author Joshua Perrin
 * KIT318
 * class definition
 * Defines the object type
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
	
	//constructor
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
    
    public String getUsername() {
        return requestingUsername;
    }
    
    public String getType() {
        return type;
    }
    
    public String getStringInput() {
        return stringInput;
    }
    
    public String getInputFilename() {
        return inputFilename;
    }
    
    public int getDeadline() {
        return deadline;
    }
    
    public int getCode() {
        return code;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String s) {
        status = s;
    }
    
    public void setCode(int c) {
        code = c;
    }
    
    public String getStringOutput() {
        return stringOutput;
    }
    
    public String getOutputFilename() {
        return outputFilename;
    }
}