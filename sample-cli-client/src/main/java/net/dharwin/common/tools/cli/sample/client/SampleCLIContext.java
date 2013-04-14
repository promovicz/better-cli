package net.dharwin.common.tools.cli.sample.client;

import java.io.File;

import net.dharwin.common.tools.cli.api.CLIContext;
import net.dharwin.common.tools.cli.sample.client.datastore.UserDatastore;


/**
 * A sample implementation of a context.
 * Note that this is optional, as we could just use CLIContext instead.
 * @author Sean
 *
 */
public class SampleCLIContext extends CLIContext {
	
	private static final String USER_DATASTORE = "user_datastore";
	private static final String LOGGED_IN_USER = "logged_in_user";
	
	/**
	 * Initialize the context. This creates the UserDatastore.
	 * @param app The host application.
	 */
	public SampleCLIContext(SampleCLIClient app) {
		super(app);
		this.put(USER_DATASTORE, new UserDatastore());
	}
	
	/**
	 * Set the logged in user.
	 * @param user The username.
	 */
	public void setLoggedInUser(String user) {
		this.put(LOGGED_IN_USER, user);
	}
	
	/**
	 * Get the logged in username. May be null if no login was performed.
	 * @return The username. May be null.
	 */
	public String getLoggedInUser() {
		return this.getString(LOGGED_IN_USER);
	}
	
	/**
	 * Get the UserDatastore instance.
	 * @return The UserDatastore instance.
	 */
	public UserDatastore getUserDatastore() {
		return (UserDatastore)this.getObject(USER_DATASTORE);
	}
	
	@Override
	protected String getEmbeddedPropertiesFilename() {
		return "/embedded_sample_client.properties";
	}
	
	/**
	 * Specify the property file used by this context to load
	 * initial properties from. This property file should sit outside of
	 * the final jar file.
	 */
	@Override
	protected File getExternalPropertiesFile() {
		return new File("sample_client.properties");
	}
	
}
