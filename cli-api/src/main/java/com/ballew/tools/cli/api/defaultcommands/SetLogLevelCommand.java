package com.ballew.tools.cli.api.defaultcommands;

import com.ballew.tools.cli.api.CLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;
import com.ballew.tools.cli.api.console.Console.ConsoleLevel;

/**
 * Sets the log level of the application.
 * @author Sean
 *
 */
@CLICommand(name="loglevel")
public class SetLogLevelCommand extends Command<CLIContext> {

	@Override
	public CommandResult innerExecute(CLIContext context, CommandLineArguments args) {
		String levelStr = args.getValue("l", true);
		ConsoleLevel level = null;
		try {
			level = ConsoleLevel.valueOf(levelStr.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			ConsoleLevel[] knownLevels = ConsoleLevel.values();
			StringBuilder knownLevelsStr = new StringBuilder();
			for (int i = 0; i < knownLevels.length; i++) {
				knownLevelsStr.append(knownLevels[i].name());
				if (i < knownLevels.length-1) {
					knownLevelsStr.append(", ");
				}
			}
			Console.error("Unknown log level ["+levelStr+"]. Known levels are: " + knownLevelsStr.toString());
			return CommandResult.BAD_ARGS;
		}
		Console.setLevel(level);
		return CommandResult.OK;
	}

}
