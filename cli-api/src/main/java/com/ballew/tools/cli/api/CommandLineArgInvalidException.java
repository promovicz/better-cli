package com.ballew.tools.cli.api;

public class CommandLineArgInvalidException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -969728549269309927L;
	private String _argName;
	
	public CommandLineArgInvalidException(String arg) {
		super("Command line arg ["+arg+"] is invalid.");
		_argName = arg;
	}
	
	public String getArgName() {
		return _argName;
	}
}
