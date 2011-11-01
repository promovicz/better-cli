package com.ballew.tools.cli.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.ballew.tools.cli.api.CommandResult.CommandResultType;
import com.ballew.tools.cli.api.annotations.CLICommand;
import com.ballew.tools.cli.api.console.Console;
import com.ballew.tools.cli.api.console.Console.ConsoleLevel;
import com.ballew.tools.cli.api.exceptions.CLIInitException;

/**
 * The CommandLineApplication is the base class for any application wanting
 * to run via the command line. This base class will load the known commands, as well
 * as the default commands (unless the implementing class disables the default commands).
 * Furthermore, this base class handles the input, output and looping of the application.
 * @author Sean
 *
 */
public abstract class CommandLineApplication<T extends CLIContext> {
	
	/** The package in which the default commands reside. **/
	private static final String DEFAULT_COMMANDS_PACKAGE = "com.ballew.tools.cli.api.defaultcommands";
	
	/** The property key to define the default log level. **/
	private static final String DEFAULT_LOG_LEVEL_KEY = "default_log_level";
	
	/** The property key to define whether or not to print log levels. **/
	private static final String PRINT_LOG_LEVEL_KEY = "print_log_levels";
	
	/** The command line arguments given during initial startup. **/
	private CommandLineArguments _startupArgs;
	
	/** The map of command names to commands. **/
	private Map<String, Command<? extends CLIContext>> _commands;
	
	/** The application context. **/
	private CLIContext _appContext;
	
	/**
	 * Initialize the application. This loads the known commands.
	 * @param startupArgs The startup args.
	 * @throws CLIInitException Thrown when commands fail to properly load.
	 */
	public CommandLineApplication(CommandLineArguments startupArgs) throws CLIInitException {
		_startupArgs = startupArgs;
		_commands = loadCommands();
		_appContext = createContext();
	}
	
	/**
	 * Start the application. This will continuously loop until
	 * the user exits the application.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start() {
		setDefaultLogLevel();
		
		Scanner scan = new Scanner(System.in);
		while (true) {
			Console.prompt();
			String nextLine = scan.nextLine();
			String[] rawArgs = nextLine.split("\\s");
			if (rawArgs.length == 0) {
				continue;
			}
			String commandName = rawArgs[0];
			Command command = _commands.get(commandName.toLowerCase());
			if (command == null) {
				Console.error("Command not recognized: " + commandName);
				continue;
			}
			
			CommandLineArguments clArgs = new CommandLineArguments(StringUtils.stripArgs(rawArgs, 1));
			CommandResult result = command.execute((T)_appContext, clArgs);
			if (result.getStatusCode() != 0) {
				Console.error("Command returned type ["+
						result.getType().name()+"] with status code ["+result.getStatusCode()+"].");
			}
			
			if (result.getType() == CommandResultType.EXIT) {
				this.shutdown();
				System.exit(0);
			}
		}
	}
	
	protected void setDefaultLogLevel() {
		String defaultLogLevel = _appContext.getString(DEFAULT_LOG_LEVEL_KEY);
		if (defaultLogLevel != null) {
			try {
				ConsoleLevel l = ConsoleLevel.valueOf(defaultLogLevel);
				Console.setLevel(l);
			}
			catch (IllegalArgumentException e) {
				Console.error("Unknown default log level ["+defaultLogLevel+"].");
			}
		}
		if (_appContext.containsKey(PRINT_LOG_LEVEL_KEY)) {
			Console.setPrintLogLevel(_appContext.getBoolean(PRINT_LOG_LEVEL_KEY));
		}
	}
	
	/**
	 * Get the startup args specified during the initial application startup.
	 * @return The startup args specified during the initial application startup.
	 */
	protected CommandLineArguments getStartupArgs() {
		return _startupArgs;
	}
	
	/**
	 * Load the necessary commands for this application.
	 * @return The map of commands.
	 * @throws CLIInitException Thrown when commands fail to properly load.
	 */
	private Map<String, Command<? extends CLIContext>> loadCommands() throws CLIInitException{
		Map<String, Command<? extends CLIContext>> commands = new HashMap<String, Command<? extends CLIContext>>();
		
		ClassPathScanningCandidateComponentProvider scanner =
			new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(CLICommand.class));
		
		loadCommandsFromBasePackage(commands, getCommandBasePackage(), scanner);
		if (commands.isEmpty()) {
			throw new CLIInitException("No commands could be loaded from package ["+getCommandBasePackage()+"].");
		}
		
		// Optionally load default commands.
		if (loadDefaultCommands()) {
			loadCommandsFromBasePackage(commands, DEFAULT_COMMANDS_PACKAGE, scanner);
		}
		
		return commands;
	}
	
	private void loadCommandsFromBasePackage(Map<String, Command<? extends CLIContext>> out,
			String basePackage, ClassPathScanningCandidateComponentProvider scanner) throws CLIInitException {
		Set<BeanDefinition> commandBeans =
			scanner.findCandidateComponents(basePackage);
		
		if (commandBeans == null || commandBeans.isEmpty()) {
			return;
		}
		
		Iterator<BeanDefinition> it = commandBeans.iterator();
		while (it.hasNext()) {
			BeanDefinition def = it.next();
			String commandClassName = def.getBeanClassName();
			try {
				Class<?> commandClass = Class.forName(commandClassName);
				if (!Command.class.isAssignableFrom(commandClass)) {
					Console.severe("Command class ["+commandClassName+"] is not of type Command.");
				}
				
				@SuppressWarnings("unchecked")
				Command<? extends CLIContext> command = (Command<? extends CLIContext>)commandClass.newInstance();
				CLICommand annotation = command.getClass().getAnnotation(CLICommand.class);
				
				out.put(annotation.name().toLowerCase(), command);
				Console.info("Loaded command ["+annotation.name()+"].");
			}
			catch (ClassNotFoundException e) {
				throw new CLIInitException("Unable to find command class ["+commandClassName+"].");
			}
			catch (InstantiationException e) {
				throw new CLIInitException("Unable to create command class ["+commandClassName+"].");
			}
			catch (IllegalAccessException e) {
				throw new CLIInitException("Unable to access command class ["+commandClassName+"].");
			}
		}
	}
	
	/**
	 * Create the context. By default, this creates a
	 * standard CLIContext. Implementing classes should override
	 * this to specify a custom context.
	 * This MUST be the same type (or a subclass of) those specified
	 * by the command implementations.
	 * @return The context.
	 */
	protected CLIContext createContext() {
		return new CLIContext(this);
	}
	
	/**
	 * Return the list of commands known to this application.
	 * @return The list of commands known to this application.
	 */
	public Set<String> getCommands() {
		return _commands.keySet();
	}
	
	/**
	 * Decide whether or not to load the default commands.
	 * By default, this returns true.
	 * @return Whether or not the defualt commands should load.
	 */
	protected boolean loadDefaultCommands() {
		return true;
	}
	
	/**
	 * Get the base package for where to load commands from.
	 * @return The base package for where to load commands from.
	 */
	protected abstract String getCommandBasePackage();
	
	/**
	 * Called when the user has cleanly exited the application.
	 */
	protected abstract void shutdown();
}
