package com.ballew.tools.cli.api.defaultcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ballew.tools.cli.api.CLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Prints all known commands.
 * @author Sean
 *
 */
@CLICommand(name="help")
public class HelpCommand extends Command<CLIContext> {

	@Override
	public CommandResult innerExecute(CLIContext context, CommandLineArguments args) {
		Set<String> knownCommandsSet = context.getHostApplication().getCommands();
		
		List<String> knownCommands = new ArrayList<String>(knownCommandsSet);
		Collections.sort(knownCommands);
		
		Console.info("Known commands:");
		for (String command : knownCommands) {
			Console.info(command);
		}
		return CommandResult.OK;
	}

}
