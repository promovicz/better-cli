package com.ballew.tools.cli.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ballew.tools.cli.api.console.Console;

/**
 * Parses a command line into a map of key/values and a list
 * of switches.
 * @author Sean
 *
 */
public class CommandLineArguments {
	
	private static final String ARG_PREFIX = "-";
	
	private Map<String, String> _keyValues;
	private List<String> _switches;
	
	public CommandLineArguments(String[] args) {
		_keyValues = new HashMap<String, String>();
		_switches = new ArrayList<String>();
		parse(args);
	}
	
	/**
	 * Returns true if there are no key-value pairs and there are no switches.
	 * @return
	 */
	public boolean isEmpty() {
		return _keyValues.isEmpty() && _switches.isEmpty();
	}
	
	/**
	 * Check whether or not the given key exists.
	 * @param key The key to check.
	 * @return Whether or not the given key exists.
	 */
	public boolean containsKey(String key) {
		return containsKey(key, false);
	}
	
	/**
	 * Check whether or not the given key exists.
	 * TODO: This will return false when the key exists but the value is a literal null.
	 * @param key The key to check.
	 * @param required Whether or not the key is required.
	 * @return Whether or not the given key exists.
	 */
	public boolean containsKey(String key, boolean required) {
		return getValue(key, required) != null;
	}
	
	/**
	 * Get the value associated with the given key.
	 * This does not assume the key to be required and will return null
	 * when the key does not exist.
	 * @param key The key to lookup.
	 * @return The value, or null if not found.
	 */
	public String getValue(String key) {
		return getValue(key, false);
	}
	
	/**
	 * Get the value associated with the key.
	 * If the key does not exist and required is specified to be true, then
	 * an IllegalStateException is thrown.
	 * @param key The key to lookup.
	 * @param required Whether or not this key is required.
	 * @return The value associated with the key.
	 * @throws IllegalStateException If the key does not exist and required is specified to be true, then
	 * an IllegalStateException is thrown.
	 */
	public String getValue(String key, boolean required) throws CommandLineArgMissingException {
		String val = _keyValues.get(key);
		if (val == null && required) {
			throw new CommandLineArgMissingException(key);
		}
		return val;
	}
	
	/**
	 * Get an integer from the arguments.
	 * If none is found, then Integer.MIN_VALUE is returned.
	 * TODO: We could use a return type of Integer here and return null
	 * when not found, but that makes it dangerous for a consumer to
	 * use int i = getInteger("i");
	 * It is safest to prefix a call to getInteger with containsKey.
	 * @param keyName The keyname.
	 * @return The value, or Integer.MIN_VALUE if not found.
	 */
	public int getInteger(String keyName) {
		return getInteger(keyName, false);
	}
	
	/**
	 * Get an integer from the arguments.
	 * If none is found, then Integer.MIN_VALUE is returned.
	 * TODO: We could use a return type of Integer here and return null
	 * when not found, but that makes it dangerous for a consumer to
	 * use int i = getInteger("i");
	 * It is safest to prefix a call to getInteger with containsKey.
	 * @param keyName The keyname.
	 * @param required Whether or not the value is required.
	 * @return The value, or Integer.MIN_VALUE if not found.
	 */
	public int getInteger(String keyName, boolean required) throws CommandLineArgInvalidException {
		String val = getValue(keyName, required);
		if (val == null) {
			return Integer.MIN_VALUE;
		}
		try {
			return Integer.parseInt(val);
		}
		catch (NumberFormatException e) {
			throw new CommandLineArgInvalidException(keyName);
		}
	}
	
	/**
	 * Get a Float from the arguments.
	 * If none is found, then Float is returned.
	 * TODO: See discussion on getInteger.
	 * It is safest to prefix a call to getFloat with containsKey.
	 * @param keyName The keyname.
	 * @return The value, or Float if not found.
	 */
	public float getFloat(String keyName) {
		return getFloat(keyName, false);
	}
	
	/**
	 * Get a Float from the arguments.
	 * If none is found, then Float is returned.
	 * TODO: See discussion on getInteger.
	 * It is safest to prefix a call to getFloat with containsKey.
	 * @param keyName The keyname.
	 * @param required Whether or not the value is required.
	 * @return The value, or Float if not found.
	 */
	public float getFloat(String keyName, boolean required) throws CommandLineArgInvalidException {
		String val = getValue(keyName, required);
		if (val == null) {
			return Float.MIN_VALUE;
		}
		try {
			return Float.parseFloat(val);
		}
		catch (NumberFormatException e) {
			throw new CommandLineArgInvalidException(keyName);
		}
	}
	
	// TODO: Add more cast methods (double, long, etc).
	
	/**
	 * Returns whether or not the given switch exists.
	 * This does not assume the switch to be required and will return false
	 * when the switch does not exist.
	 * @param switchName The switch name to lookup.
	 * @return Whether or not the switch exists.
	 */
	public boolean containsSwitch(String switchName) {
		return containsSwitch(switchName, false);
	}
	
	/**
	 * Returns whether or not the given switch exists.
	 * This does not assume the switch to be required and will return false
	 * when the key does not exist.
	 * @param switchName The switch name to lookup.
	 * @param required Whether or not the switch is required.
	 * @return Whether or not the switch exists.
	 * @throws IllegalStateException If the specified switch does not exist but is required,
	 * then an IllegalStateException is thrown.
	 */
	public boolean containsSwitch(String switchName, boolean required) throws CommandLineArgMissingException {
		boolean contains = _switches.contains(switchName);
		if (!contains && required) {
			throw new CommandLineArgMissingException(switchName);
		}
		return contains;
	}
	
	private void parse(String[] args) {
		if (args == null) {
			return;
		}
		
		for (String s : args) {
			if (s.length() <= 0) {
				continue;
			}
			if (s.startsWith(ARG_PREFIX)) {
				s = s.substring(1);
			}
			if (s.contains("=")) {
				String[] keyVal = s.split("=");
				if (keyVal.length < 2) {
					Console.error("Error code [1] processing arg ["+s+"].");
				}
				String key = keyVal[0];
				StringBuilder val = new StringBuilder(keyVal[1]);
				for (int i = 2; i < keyVal.length; i++) {
					val.append("="+keyVal[i]);
				}
				_keyValues.put(key, val.toString());
			}
			else {
				_switches.add(s);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("KeyValues: {");
		Iterator<String> keys = _keyValues.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			b.append("[");
			b.append(key);
			b.append("]=[");
			b.append(_keyValues.get(key));
			b.append("]");
			if (keys.hasNext()) {
				b.append(", ");
			}
		}
		b.append("}, Switches: {");
		for (int i = 0; i < _switches.size(); i++) {
			b.append("[");
			b.append(_switches.get(i));
			b.append("]");
			if (i < (_switches.size()-1)) {
				b.append(", ");
			}
		}
		b.append("}");
		return b.toString();
	}
}
