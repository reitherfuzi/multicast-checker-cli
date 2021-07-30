package at.pwimmer.multicast;

import at.pwimmer.multicast.cli.Arguments;
import at.pwimmer.multicast.tasks.MCReceiver;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MulticastChecker {

    public static void main(String[] args) {
        final Arguments arguments = new Arguments();        // Init the object containing the JCommander annotations.

        try {
            // Parse the args with the JCommander.
            JCommander.newBuilder().addObject(arguments).build().parse(args);

            // Check if help should be displayed, help will terminate the app right now.
            if(arguments.isHelp())  outputHelpAndExit();

            // Check if "verbose" is enabled, then output more info.
            if(arguments.isVerbose())   outputVerbose(arguments);

            final MCReceiver receiver = new MCReceiver(arguments);                  // Init the MC-Receiver with the parsed arguments.
            final ExecutorService exService = Executors.newSingleThreadExecutor();  // Also create a new Single Thread-Executor.
            exService.execute(receiver);                                            // Then execute the "receiver" on the executor.

            Runtime.getRuntime().addShutdownHook(new Thread(receiver::shutdown));       // Add a Shutdown-Hook for the receiver.
            Runtime.getRuntime().addShutdownHook(new Thread(exService::shutdownNow));   // And for the Executor-Service.

            while(receiver.isRunning()) {                   // As long as the Thread is NOT interrupted and receiver is RUNNING.
                TimeUnit.MILLISECONDS.sleep(500);    // Sleep for about 500ms and then check again.
            }
        }
        catch(InterruptedException ex) {            // Will be thrown if the Thread has been interrupted.
            Thread.currentThread().interrupt();     // Simply re-interrupt the thread.
        }
        catch(ParameterException ex) {              // Will be thrown if a required argument has not been passed.
            System.err.println(ex.getMessage());    // Output the pretty good exception-message of the "jcommander".
        }
        finally {                       // Finally, will be called always if exception or not.
            System.exit(0);         // Exit the application with an zero exit-code.
        }
    }

    private static void outputHelpAndExit() {
        // TODO: Output help how to use the multicast-checker.
    }

    private static void outputVerbose(Arguments arguments) {
        System.out.println("~~~~ Multicast-Checker ~~~~");
        System.out.println("MC-Group    : " + arguments.getGroup());
        System.out.println("MC-Port     : " + arguments.getPort());
        System.out.println("So-Timeout  : " + arguments.getTimeout());
        System.out.println("Output-Type : " + arguments.getOutput());
        System.out.println("Restriction : " + arguments.getRestriction());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}