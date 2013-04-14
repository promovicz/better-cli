package better.cli.sample.commands;

import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import better.cli.console.Console;
import better.cli.sample.SampleCLIContext;

import com.beust.jcommander.Parameter;

/**
 * Outputs the value of a context key.
 * This command can show how property files can specify defaults by using CLIContext.getEmbeddedPropertiesFilename() and
 * CLIContext.getExternalPropertiesFile().
 * @author Sean
 *
 */
@CLICommand(name="getproperty", description="Gets the value of a property from the CLI Context.")
public class GetPropertyCommand extends Command<SampleCLIContext> {

	@Parameter(names={"-p", "--property"}, description="The property key to lookup.", required=true)
	private String _propertyName;
	
	@Override
	protected CommandResult innerExecute(SampleCLIContext context) {
		String value = context.getString(_propertyName);
		if (value == null) {
			Console.info("The property ["+_propertyName+"] was not found in the context.");
		}
		else {
			Console.info("The property ["+_propertyName+"] is set to the value ["+value+"].");
		}
		return CommandResult.OK;
	}

}
