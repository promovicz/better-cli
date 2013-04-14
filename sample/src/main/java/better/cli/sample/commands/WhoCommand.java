package better.cli.sample.commands;

import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import better.cli.console.Console;
import better.cli.sample.SampleCLIContext;


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
