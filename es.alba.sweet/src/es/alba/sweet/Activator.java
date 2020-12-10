package es.alba.sweet;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import es.alba.sweet.core.Output;

public class Activator implements BundleActivator {
	public final static Logger		LOGGER	= Logger.getLogger(Activator.class.getName());

	private static BundleContext	context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		logger();
		Output.DEBUG.info("es.alba.sweet.Activator.start", "Activator started");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	private void logger() {

		// suppress the logging output to the console
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			rootLogger.removeHandler(handlers[0]);
		}

		LOGGER.setLevel(Level.INFO);
		FileHandler fileTxt;
		try {
			fileTxt = new FileHandler("C:\\temp\\Logging.txt");
			// create a TXT formatter
			MessageFormatter formatterTxt = new MessageFormatter();
			fileTxt.setFormatter(formatterTxt);
			LOGGER.addHandler(fileTxt);

		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		LOGGER.setLevel(Level.INFO);
	}

	public class MessageFormatter extends Formatter {

		@Override
		public String format(LogRecord record) {
			return record.getMessage();
		}

	}

}
