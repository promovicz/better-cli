package com.ballew.tools.cli.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.ballew.tools.cli.api.annotations.CLIEntry;
import com.ballew.tools.cli.api.console.Console;

/**
 * The entry point contains the main entry. It is in charge of
 * locating the desired CommandLineApplication implementation, as well
 * as initializing and starting it.
 * @author Sean
 *
 */
public class EntryPoint {
	
	public static void main(String[] args) {
		ClassPathScanningCandidateComponentProvider scanner =
			new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(CLIEntry.class));
		
		Properties props = loadProperties();
		String basePackage = "";
		
		if (props.containsKey("base_package")) {
			basePackage = props.getProperty("base_package");
		}
		else {
			if (args.length < 1) {
				Console.warn("No base package was defined. It may take some time to scan for it.");
			}
			else {
				basePackage = args[0];
			}
		}

		Set<BeanDefinition> beans = scanner.findCandidateComponents(basePackage);
		if (beans == null || beans.size() == 0) {
			Console.severe("Startup failed: Could not find CLIEntry within ["+basePackage+"].");
			System.exit(1);
		}
		
		if (beans.size() > 1) {
			Console.severe("Startup failed: Expected 1 CLIEntry within ["+basePackage+
					"], but ["+beans.size()+"] were found.");
			System.exit(1);
		}
		
		BeanDefinition cliEntry = beans.iterator().next();
		
		String cliEntryClassName = cliEntry.getBeanClassName();
		Console.info("Loading CLIEntry ["+cliEntryClassName+"].");
		
		try {
			Class<?> cliEntryClass = Class.forName(cliEntryClassName);
			if (!CommandLineApplication.class.isAssignableFrom(cliEntryClass)) {
				Console.severe("CLIEntry ["+cliEntryClassName+"] is not of type CommandLineApplication.");
				System.exit(1);
			}
			
			Constructor<?> constructor = cliEntryClass.getConstructor();
			
			@SuppressWarnings("unchecked")
			CommandLineApplication<? extends CLIContext> cla =
					(CommandLineApplication<? extends CLIContext>)constructor.newInstance();
			cla.start();
		}
		catch (ClassNotFoundException e) {
			Console.severe("Unable to find CLIEntry class ["+cliEntryClassName+"].");
			System.exit(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Properties loadProperties() {
		Properties props = new Properties();
		
		
		InputStream propFileStream = null;
		try {
			
			propFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("cli.properties");
			if (propFileStream != null) {
				props.load(propFileStream);
			}
		}
		catch (IOException e) {
			Console.warn("IOException when processing cli.properties: " + e.getMessage());
		}
		finally {
			if (propFileStream != null) {
				try {
					propFileStream.close();
				}
				catch (IOException e) {
				}
			}
		}
		if (props.isEmpty()) {
			Console.warn("Unable to load cli.properties, or no properties were defined.");
		}
		return props;
	}
}
