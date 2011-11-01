package com.ballew.samples.cli.client;

import com.ballew.tools.cli.api.CLIContext;
import com.ballew.tools.cli.api.CommandLineApplication;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.annotations.CLIEntry;
import com.ballew.tools.cli.api.exceptions.CLIInitException;

@CLIEntry
public class SampleCLIClient extends CommandLineApplication<SampleCLIContext> {

	public SampleCLIClient(CommandLineArguments startupArgs) throws CLIInitException {
		super(startupArgs);
	}

	@Override
	protected String getCommandBasePackage() {
		return "com.ballew.samples.cli.client.commands";
	}

	@Override
	protected void shutdown() {
		System.out.println("Shutting down SampleClient.");
	}
	
	/**
	 * Note that this returns the same type as specified by the Command<SampleCLIContext> commands!
	 * This must be the same type.
	 */
	@Override
	protected CLIContext createContext() {
		return new SampleCLIContext(this);
	}

}
