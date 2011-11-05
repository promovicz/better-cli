package com.ballew.samples.cli.client.commands;

import com.ballew.samples.cli.client.SampleCLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Print the username of the current logged in user.
 * @author Sean
 *
 */
@CLICommand(name="who", description="Prints the username of the logged in user.")
public class WhoCommand extends Command<SampleCLIContext> {

	@Override
	public CommandResult innerExecute(SampleCLIContext context) {
		Console.info("Username of logged in user: ["+context.getLoggedInUser()+"].");
		return CommandResult.OK;
	}

}
