package better.cli.sample.commands;

import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import better.cli.console.Console;
import better.cli.sample.SampleCLIContext;

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
