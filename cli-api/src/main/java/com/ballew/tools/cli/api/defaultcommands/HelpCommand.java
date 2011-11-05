package com.ballew.tools.cli.api.defaultcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ballew.tools.cli.api.CLIContext;
import com.ballew.tools.cli.api.Command;
import com.ballew.tools.cli.api.CommandResult;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;
import com.beust.jcommander.Parameter;

/**
 * Prints all known commands. A parameter "c" can be specified to
 * print the usage for a specific command.
 * @author Sean
 *
 */
@CLICommand(name="help", description="Prints known commands and usage.")
public class HelpCommand extends Command<CLIContext> {
	
	@Parameter(names={"-c", "--command"}, description="The command name to get specific help for.")
	private String _commandName;
	
	@Override
	public CommandResult innerExecute(CLIContext context) {
		if (_commandName == null) {
			// Print command list.
			Set<String> knownCommandsSet = context.getHostApplication().getCommandNames();
			
			List<String> knownCommands = new ArrayList<String>(knownCommandsSet);
			Collections.sort(knownCommands);
			
			Console.info("Known commands:");
			for (String command : knownCommands) {
				String description =
					context.getHostApplication().getCommands().get(command).getAnnotation(CLICommand.class).description();
				if (description != null && !description.isEmpty()) {
					Console.info(command + "\t\t" + description);
				}
				else {
					Console.info(command);
				}
			}
		}
		else {
			// Print help for a specific command.
			Command<? extends CLIContext> command = null;
			try {
				Class<? extends Command<? extends CLIContext>> commandClass =
					context.getHostApplication().getCommands().get(_commandName.toLowerCase());
				if (commandClass == null) {
					Console.error("Command ["+_commandName+"] not recognized.");
					return CommandResult.BAD_ARGS;
				}
				command = commandClass.newInstance();
			}
			catch (Exception e) {
				Console.error("Error loading command help for ["+_commandName+"].");
				return CommandResult.ERROR;
			}
			
			command.usage();
		}
		
		return CommandResult.OK;
	}

}
