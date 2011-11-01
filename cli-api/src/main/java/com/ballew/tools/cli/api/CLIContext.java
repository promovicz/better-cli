package com.ballew.tools.cli.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.ballew.tools.cli.api.console.Console;

public class CLIContext {
	
	private Map<String, Object> _properties;
	
	private CommandLineApplication<? extends CLIContext> _app;
	
	public CLIContext(CommandLineApplication<? extends CLIContext> app) {
		_properties = new HashMap<String, Object>();
		_app = app;
		loadProperties(getEmbeddedPropertiesFile());
		loadProperties(getExternalPropertiesFile());
	}
	
	public CommandLineApplication<? extends CLIContext> getHostApplication() {
		return _app;
	}
	
	private void loadProperties(File propFile) {
		if (propFile == null) {
			return;
		}
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(propFile);
			Properties props = new Properties();
			props.load(stream);
			
			Iterator<Object> keyIt = props.keySet().iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next().toString();
				_properties.put(key, props.get(key));
			}
		}
		catch (Exception e) {
			Console.warn("Unable to load properties file ["+propFile.getAbsolutePath()+"].");
		}
		finally {
			if (stream != null) {
				try {
					stream.close();
				}
				catch (Exception e) {
					Console.warn("Unable to close properties file ["+propFile.getAbsolutePath()+"].");
				}
			}
		}
	}
	
	/**
	 * Get the embedded property file. If none should be used, specify null.
	 * @return
	 */
	protected File getEmbeddedPropertiesFile() {
		return null;
	}
	
	/**
	 * Get the external property file. If none should be used, specify null.
	 * @return
	 */
	protected File getExternalPropertiesFile() {
		return null;
	}
	
	/**
	 * Add an object to the context.
	 * @param key The key to add.
	 * @param o The object to add.
	 * @return The previous object associated with this key, or null if there was none.
	 */
	public Object put(String key, Object o) {
		return _properties.put(key, o);
	}
	
	public Object getObject(String key) {
		return _properties.get(key);
	}
	
	public String getString(String key) {
		Object o = getObject(key);
		if (o == null) {
			return null;
		}
		if (!(o instanceof String)) {
			throw new IllegalArgumentException("Object ["+o+"] associated with key ["+key+"] is not of type String.");
		}
		return (String)o;
	}
	
	public boolean getBoolean(String key) {
		Object o = getObject(key);
		if (o == null) {
			return false;
		}
		boolean b = false;
		try {
			b = Boolean.parseBoolean(o.toString());
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Object ["+o+"] associated with key ["+key+"] is not of type Boolean.");
		}
		return b;
	}
	
	public boolean containsKey(String key) {
		return _properties.containsKey(key);
	}
	
}
