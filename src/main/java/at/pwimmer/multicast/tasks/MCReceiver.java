package at.pwimmer.multicast.tasks;

import at.pwimmer.multicast.cli.Arguments;
import at.pwimmer.multicast.models.OutputType;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class MCReceiver implements Runnable {
    private final AtomicLong packets = new AtomicLong();

    private final String group;
    private final int port;
    private final int timeout;
    private final long restriction;
    private final OutputType output;

    private volatile boolean running = true;

    public MCReceiver(Arguments arguments) {
        this.group = arguments.getGroup();
        this.port = arguments.getPort();
        this.timeout = arguments.getTimeout();
        this.output = arguments.getOutput();
        this.restriction = arguments.getRestriction();
    }

    public MCReceiver(String group, int port, int timeout, int restriction, OutputType output) {
        this.group = group;
        this.port = port;
        this.timeout = timeout;
        this.output = output;
        this.restriction = restriction;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MC-Receiver ["+group+"::"+port+"]");

        try(final MulticastSocket socket = new MulticastSocket(port)) {
            socket.setSoTimeout(timeout);

            // Then get the Group-Address and join the multicast-group.
            InetAddress address = InetAddress.getByName(group);
            socket.joinGroup(address);

            System.out.println("Initialized multicast-socket for ["+group+"::"+port+"]");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            // As long as running, socket not closed and thread not interrupted receive data.
            while(running && !socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                receiveData(socket);
            }

            System.out.println("The multicast-receiver has been stopped and will shutdown!");

            // Then leave the Multicast-Group and disconnect from the socket.
            socket.leaveGroup(address);
            socket.disconnect();
        }
        catch(IOException ex) {
            System.err.println("Could not create the multicast-socket for ["+group+"::"+port+"]");
        }
        finally {
            shutdown();
        }
    }

    public void shutdown() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private void receiveData(final MulticastSocket socket) {
        if(socket.isClosed())  return;

        try {
            final byte[] buffer = new byte[socket.getReceiveBufferSize()];
            final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Then receive the data from the multicast-socket.
            socket.receive(packet);
            final long counter = packets.incrementAndGet();

            // Then copy the bytes from the datagram-packet into a new byte-array and output it.
            byte[] data = Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet.getOffset()+packet.getLength());
            outputData(data);

            // Check if any restriction is set and if it has been reached.
            if(restriction > 0 && counter >= restriction) {     // Check if restriction >0 and if restriction reached.
                shutdown();                                     // If so, then shutdown this mc-receiver.
            }
        }
        catch(SocketTimeoutException ex) {      // Will be thrown if the so-timeout has been set and reached.
            System.err.println("ERROR: Socket-Timeout of " + timeout + "ms reached for ["+group+"::"+port+"] - shutdown!");
            shutdown();                         // If so, then shutdown the task/thread.
        }
        catch(IOException ex) {
            System.err.println("Failed to receive data on multicast-socket for "+group+"::"+port);
        }
    }

    private void outputData(byte[] buffer) {
        if(buffer == null || buffer.length == 0) return;

        final String data;

        switch(output) {
            case UTF8:
            case ASCII:     data = new String(buffer, StandardCharsets.UTF_8); break;
            case BINARY:
            case BYTES:     data = Arrays.toString(buffer); break;
            case HEX:       data = Hex.encodeHexString(buffer); break;
            case OCTAL:     data = (new BigInteger(buffer)).toString(8); break;
            default:        data = "INVALID-TYPE";
        }

        System.out.println(data);
    }
}
