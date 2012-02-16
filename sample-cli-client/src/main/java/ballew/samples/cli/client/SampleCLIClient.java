package ballew.samples.cli.client;

import ballew.tools.cli.api.CLIContext;
import ballew.tools.cli.api.CommandLineApplication;
import ballew.tools.cli.api.annotations.CLIEntry;
import ballew.tools.cli.api.exceptions.CLIInitException;


/**
 * A sample CLI client. Notice that it is annotated with CLIEntry, designating it as
 * the CommandLineApplication to use.
 * @author Sean
 *
 */
@CLIEntry
public class SampleCLIClient extends CommandLineApplication<SampleCLIContext> {

	public SampleCLIClient() throws CLIInitException {
		super();
	}

	@Override
	protected void shutdown() {
		System.out.println("Shutting down SampleClient.");
	}
	
	/**
	 * Note that this returns the same type as specified by the Command<SampleCLIContext> commands!
	 * This must be the same type.
	 */
	@Override
	protected CLIContext createContext() {
		return new SampleCLIContext(this);
	}

}
