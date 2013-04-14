package better.cli.sample.commands;

import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import better.cli.console.Console;
import better.cli.sample.SampleCLIContext;
import better.cli.sample.datastore.User;

import com.beust.jcommander.Parameter;

/**
 * Add a user to the datastore.
 * Usage: adduser -f blitzcrank -l maokai -a 1337
 * @author Sean
 *
 */
@CLICommand(name="adduser", description="Add a user to the database.")
public class AddUserCommand extends Command<SampleCLIContext> {
	
	@Parameter(names={"-f", "--first"}, description="User's first name.", required=true)
	private String _firstName;
	
	@Parameter(names={"-l", "--last"}, description="User's last name.", required=true)
	private String _lastName;
	
	@Parameter(names={"-a", "--age"}, description="User's age.", required=true)
	private int _age;
	
	@Override
	public CommandResult innerExecute(SampleCLIContext context) {
		context.getUserDatastore().addUser(new User(_firstName, _lastName, _age));
		Console.info("Added user.");
		return CommandResult.OK;
	}

}
