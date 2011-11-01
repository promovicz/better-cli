package com.ballew.samples.cli.client.commands;

import com.ballew.samples.cli.client.SampleCLIContext;
import com.ballew.samples.cli.client.datastore.User;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Add a user to the datastore.
 * Usage: -f=blitzcrank -l=maokai -a=1337
 * @author Sean
 *
 */
@CLICommand(name="adduser")
public class AddUserCommand extends Command<SampleCLIContext> {

	@Override
	public CommandResult innerExecute(SampleCLIContext context, CommandLineArguments args) {
		String firstName = args.getValue("f", true);
		String lastName = args.getValue("l", true);
		Integer age = args.getInteger("a", true);
		
		// Age not null, as the above call would have broken if it weren't valid.
		context.getUserDatastore().addUser(new User(firstName, lastName, age));
		
		Console.info("Added user.");
		
		return CommandResult.OK;
	}

}
