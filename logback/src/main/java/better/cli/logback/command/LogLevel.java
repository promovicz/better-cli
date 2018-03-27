package better.cli.logback.command;

import better.cli.CLIContext;
import better.cli.Command;
import better.cli.CommandResult;
import better.cli.annotations.CLICommand;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.Parameter;
import org.slf4j.LoggerFactory;

import java.util.List;

@CLICommand(name = "log-level", description = "Set logback log level")
public class LogLevel extends Command<CLIContext> {

    @Parameter(description = "([logger] [level])... or [root-level]")
    private List<String> pLogger;

    @Override
    protected CommandResult innerExecute(CLIContext context) {
        if(pLogger.size() == 1) {
            Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            Level level = Level.toLevel(pLogger.get(0));
            if(level != null) {
                rootLogger.setLevel(level);
            }
            return CommandResult.OK;
        }

        if(pLogger.size() % 2 != 0) {
            System.err.println("Must provide pairs of loggers and levels");
            return CommandResult.ERROR;
        }

        for(int i = 0; i < pLogger.size(); i += 2) {
            String loggerString = pLogger.get(i);
            String levelString = pLogger.get(i+1);
            Logger logger = (Logger) LoggerFactory.getLogger(loggerString);
            Level level = Level.toLevel(levelString);
            if(level != null) {
                logger.setLevel(level);
            }
        }

        return CommandResult.OK;
    }

}
