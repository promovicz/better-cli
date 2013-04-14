package net.dharwin.common.tools.cli.sample.client.commands;

import net.dharwin.common.tools.cli.api.Command;
import net.dharwin.common.tools.cli.api.CommandResult;
import net.dharwin.common.tools.cli.api.annotations.CLICommand;
import net.dharwin.common.tools.cli.api.console.Console;
import net.dharwin.common.tools.cli.sample.client.SampleCLIContext;

import com.beust.jcommander.Parameter;

/**
 * Login a user.
 * @author Sean
 *
 */
@CLICommand(name="login", description="Logs a user into the client.")
public class LoginCommand extends Command<SampleCLIContext> {
	
	@Parameter(names={"-u", "--user"}, description="The username to login with.", required=true)
	private String _user;
	
	@Override
	public CommandResult innerExecute(SampleCLIContext context) {
		context.setLoggedInUser(_user);
		Console.info("Logged in as user ["+_user+"].");
		return CommandResult.OK;
	}

}
