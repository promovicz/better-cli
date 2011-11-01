package com.ballew.samples.cli.client.commands;

import com.ballew.samples.cli.client.SampleCLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Login a user.
 * @author Sean
 *
 */
@CLICommand(name="login")
public class LoginCommand extends Command<SampleCLIContext> {

	@Override
	public CommandResult innerExecute(SampleCLIContext context, CommandLineArguments args) {
		String user = args.getValue("u", true);
		context.setLoggedInUser(user);
		Console.info("Logged in as user ["+user+"].");
		return CommandResult.OK;
	}

}
