package better.cli.sample.datastore;

import java.util.HashMap;
import java.util.Map;

/**
 * A complex implementation using the most cutting edge database technology to store users.
 * @author Sean
 *
 */
public class UserDatastore {
	
	private Map<String, User> _database;
	
	public UserDatastore() {
		_database = new HashMap<String, User>();
	}
	
	/**
	 * Add a user to the datastore.
	 * @param user The user to add.
	 * @return The previous user that was replaced in the database, or null if there was none.
	 */
	public User addUser(User user) {
		return _database.put(createKey(user), user);
	}
	
	/**
	 * Get a user from the database.
	 * @param firstName The first name.
	 * @param lastName The last name.
	 * @return The user, or null if not found.
	 */
	public User getUser(String firstName, String lastName) {
		return _database.get(createKey(firstName, lastName));
	}
	
	/**
	 * Generate a key for the user.
	 * @param u The user to generate a key for.
	 * @return The "unique" key.
	 */
	private String createKey(User u) {
		return createKey(u.getFirstName(), u.getLastName());
	}
	
	/**
	 * We don't support people of the same name but different ages.
	 * Deal with it.
	 * @param firstName The user's first name.
	 * @param lastName The user's last name.
	 * @return The "unique" key.
	 */
	private String createKey(String firstName, String lastName) {
		return firstName+lastName;
	}
}
