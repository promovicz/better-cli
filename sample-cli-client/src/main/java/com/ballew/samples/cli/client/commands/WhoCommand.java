package com.ballew.samples.cli.client.commands;

import com.ballew.samples.cli.client.SampleCLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Print the username of the current logged in user.
 * @author Sean
 *
 */
@CLICommand(name="who")
public class WhoCommand extends Command<SampleCLIContext> {

	@Override
	public CommandResult innerExecute(SampleCLIContext context, CommandLineArguments args) {
		Console.info("Username of logged in user: ["+context.getLoggedInUser()+"].");
		return CommandResult.OK;
	}

}
