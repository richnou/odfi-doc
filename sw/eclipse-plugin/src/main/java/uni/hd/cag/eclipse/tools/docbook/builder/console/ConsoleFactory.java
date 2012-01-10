/**
 * 
 */
package uni.hd.cag.eclipse.tools.docbook.builder.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.MessageConsole;

/**
 * @author rleys
 *
 */
public class ConsoleFactory implements IConsoleFactory {

	
	private static MessageConsole console = null;
	
	/**
	 * 
	 */
	public ConsoleFactory() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.console.IConsoleFactory#openConsole()
	 */
	@Override
	public void openConsole() {
		// TODO Auto-generated method stub
		
		if (console==null) {
			console = new MessageConsole("Docbook Plugin console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
		}
		

	}
	
	public static synchronized void logInfo(String message) {
		if (console!=null) {
			
			console.activate();
			console.newMessageStream().println(message);
			
		}
	}
	
	public static synchronized void logError(String message) {
		if (console!=null) {
			
			console.activate();
			console.newMessageStream().println("[Error] "+message);
			
		}
	}
	
	public static synchronized void logThrowable(Exception e) {
		if (console!=null) {
			
			console.activate();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			e.printStackTrace(ps);
			ps.flush();
			console.newMessageStream().print(new String(os.toByteArray()));
			
		}
	}

}
