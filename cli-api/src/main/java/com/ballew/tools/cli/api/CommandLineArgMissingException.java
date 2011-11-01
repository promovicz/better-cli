package com.ballew.tools.cli.api;

public class CommandLineArgMissingException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -969728549269309927L;
	private String _argName;
	
	public CommandLineArgMissingException(String arg) {
		super("Missing required command line arg ["+arg+"].");
		_argName = arg;
	}
	
	public String getArgName() {
		return _argName;
	}
}
