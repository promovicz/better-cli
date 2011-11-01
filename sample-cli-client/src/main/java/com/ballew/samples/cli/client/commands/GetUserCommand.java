package com.ballew.samples.cli.client.commands;

import com.ballew.samples.cli.client.SampleCLIContext;
import com.ballew.samples.cli.client.datastore.User;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Lookup a user in the user datastore.
 * Sample usage: -f=teemo -l=corki
 * @author Sean
 *
 */
@CLICommand(name="getuser")
public class GetUserCommand extends Command<SampleCLIContext> {

	@Override
	public CommandResult innerExecute(SampleCLIContext context, CommandLineArguments args) {
		String firstName = args.getValue("f", true);
		String lastName = args.getValue("l", true);
		
		User user = context.getUserDatastore().getUser(firstName, lastName);
		
		if (user == null) {
			Console.info("User not found.");
		}
		else {
			Console.info("Found user! The user is ["+user.getAge()+"] years old, in case you were wondering.");
		}
		
		return CommandResult.OK;
	}

}
