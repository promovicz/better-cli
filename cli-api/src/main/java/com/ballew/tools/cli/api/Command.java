package com.ballew.tools.cli.api;

import com.ballew.tools.cli.api.CommandResult.CommandResultType;
import com.ballew.tools.cli.api.console.Console;

/**
 * The Command interface contains a command name, which is matched against the
 * user's input, and it contains a method for command execution.
 * After implementing from this interface, the implementing class should
 * use the @CLICommand annotation to mark it as eligible for use by the API.
 * @author Sean
 *
 */
public abstract class Command<T extends CLIContext> {
	
	/**
	 * Executes the command. The command line arguments contains all of
	 * the key-value pairs and switches, but does not include the command name
	 * that came from the original arguments.
	 * @param args The command line arguments.
	 * @return The result of the execution of this command.
	 */
	public CommandResult execute(T context, CommandLineArguments args) {
		try {
			return innerExecute(context, args);
		}
		catch (CommandLineArgMissingException e) {
			Console.error(e.getMessage());
			return new CommandResult(CommandResultType.BAD_ARGS, 1);
		}
		catch (CommandLineArgInvalidException e) {
			Console.error(e.getMessage());
			return new CommandResult(CommandResultType.BAD_ARGS, 1);
		}
	}
	
	/**
	 * Execute the command.
	 * @param context The context in which the command is being executed.
	 * @param args The command line arguments.
	 * @return The command result.
	 */
	protected abstract CommandResult innerExecute(T context, CommandLineArguments args);
	
	/*
	 * TODO: Eventually, this signature will be implemented. It will be used by the default "help" command
	 * to print usage for specific commands.
	 * Signature:
	 * public HelpText getHelpText();
	 */
}
