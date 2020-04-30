package at.pwimmer.multicast.cli;

import at.pwimmer.multicast.models.OutputType;
import com.beust.jcommander.Parameter;

public class Arguments {
    private static final String DEFAULT_OUTPUT_TYPE = OutputType.ASCII.toString();
    private static final boolean DEFAULT_VERBOSE = false;
    private static final int DEFAULT_RESTRICTION = 0;
    private static final int DEFAULT_TIMEOUT = 15000;

    @Parameter(names = {"-g", "--group"}, description = "group of multicast-address to receive from", required = true)
    private String group;

    @Parameter(names = {"-p", "--port"}, description = "port of multicast-address to receive from", required = true)
    private Integer port;

    @Parameter(names = {"-o", "--output"}, description = "the type of output-encoding")
    private String output = DEFAULT_OUTPUT_TYPE;

    @Parameter(names = {"-v", "--verbose"}, description = "enable or disable verbose logging-output")
    private boolean verbose = DEFAULT_VERBOSE;

    @Parameter(names = {"-t", "--timeout"}, description = "socket-timeout [ms] for multicast-socket before termination")
    private Integer timeout = DEFAULT_TIMEOUT;

    @Parameter(names = {"-r", "--restrict"}, description = "restrict the amount of incoming packets before termination")
    private Integer restriction = DEFAULT_RESTRICTION;

    public String getGroup() {
        return group;
    }

    public Integer getPort() {
        return port;
    }

    public OutputType getOutput() {
        return OutputType.valueOf(output);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getRestriction() {
        return restriction;
    }
}