package com.example.connection.util.JsonParse.lex;

public class UnexpectedException extends RuntimeException {
	private static final long serialVersionUID = 4468284382650630883L;

	private Integer charNum = null;

	private Integer lineNum = null;

	private Integer colNum = null;

	private String desc = null;

	private Throwable cause = null;

	public UnexpectedException() {
		super();
	}

	public UnexpectedException(Integer charNum, Integer lineNum,
			Integer colNum, String message, Throwable cause) {
		this.charNum = charNum;
		this.colNum = colNum;
		this.lineNum = lineNum;
		this.desc = message;
		this.cause = cause;
	}

	public UnexpectedException(Integer charNum, Integer lineNum,
			Integer colNum, String message) {
		this.charNum = charNum;
		this.colNum = colNum;
		this.lineNum = lineNum;
		this.desc = message;
	}

	public UnexpectedException(Throwable cause) {
		super(cause);
	}

	public String getMessage() {
		return "[char:" + charNum + ",line:" + lineNum + ",column:" + colNum
				+ "]" + desc + (cause == null ? "" : cause.toString());
	}

	public String getLocalMessage() {
		return getMessage();
	}

	public String toString() {
		return getMessage();
	}

	public String toLocalString() {
		return getMessage();
	}
}
