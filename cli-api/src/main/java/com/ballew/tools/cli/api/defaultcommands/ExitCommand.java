package com.ballew.tools.cli.api.defaultcommands;

import com.ballew.tools.cli.api.CLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandLineArguments;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.CommandResult.CommandResultType;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;

/**
 * Returns an exit result type. This will exit the application when executed.
 * @author Sean
 *
 */
@CLICommand(name="exit")
public class ExitCommand extends Command<CLIContext> {

	@Override
	public CommandResult innerExecute(CLIContext context, CommandLineArguments args) {
		if (!args.isEmpty()) {
			Console.error("Exit command is not expected to have arguments.");
			return new CommandResult(1);
		}
		return new CommandResult(CommandResultType.EXIT, 0);
	}

}
