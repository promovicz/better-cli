package better.cli.sample.commands;

import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import better.cli.console.Console;
import better.cli.sample.SampleCLIContext;
import better.cli.sample.datastore.User;

import com.beust.jcommander.Parameter;

/**
 * Lookup a user in the user datastore.
 * Sample usage: getuser -f teemo -l corki
 * @author Sean
 *
 */
@CLICommand(name="getuser", description="Gets a user from the database.")
public class GetUserCommand extends Command<SampleCLIContext> {

	@Parameter(names={"-f", "--first"}, description="User's first name.", required=true)
	private String _firstName;
	
	@Parameter(names={"-l", "--last"}, description="User's last name.", required=true)
	private String _lastName;
	
	@Override
	public CommandResult innerExecute(SampleCLIContext context) {

		User user = context.getUserDatastore().getUser(_firstName, _lastName);
		
		if (user == null) {
			Console.info("User not found.");
		}
		else {
			Console.info("Found user! The user is ["+user.getAge()+"] years old, in case you were wondering.");
		}
		
		return CommandResult.OK;
	}

}
