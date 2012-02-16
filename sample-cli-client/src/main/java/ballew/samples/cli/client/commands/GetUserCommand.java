package ballew.samples.cli.client.commands;

import ballew.samples.cli.client.SampleCLIContext;
import ballew.samples.cli.client.datastore.User;
import ballew.tools.cli.api.Command;
import ballew.tools.cli.api.CommandResult;
import ballew.tools.cli.api.annotations.CLICommand;
import ballew.tools.cli.api.console.Console;

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
