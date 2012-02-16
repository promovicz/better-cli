package ballew.samples.cli.client.commands;

import ballew.samples.cli.client.SampleCLIContext;
import ballew.tools.cli.api.Command;
import ballew.tools.cli.api.CommandResult;
import ballew.tools.cli.api.annotations.CLICommand;
import ballew.tools.cli.api.console.Console;


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
