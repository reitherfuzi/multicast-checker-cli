# Multicast-Checker [CLI]

A small java-application which runs on the command-line only and can receive incoming multicast-traffic.
The app can be configured via passed arguments and will output the received data in different output-types.

## Usage
On a windows-machine, open up a command-line `cmd.exe` or powershell `powershell.exe`.
Navigate to the folder where the multicast-checker has been saved.

	> java -jar multicast-checker-1.0-run.jar -g [group] -p [port]
	
Will start the multicast-checker with the passed group-address and portnumber.
More arguments can be passed to the application. Take a look at the next chapter.

## Commands
The following commands can be passed to the application.

| Command     | Description  |
|-------------|--------------|
| -g, --group | The multicast group-address to receive the data from. **[required]** |
| -p, --port  | The multicast port of the address to receive the data from. **[required]** |
| -t, --timeout | The socket-timeout in ms, if no data received after this time the application will shutdown. |
| -o, --output | The type of how the data should be outputted to the console [ASCII, HEX, BINARY, OCTAL, BYTE] |
| -r, --restriction | Allows to restrict the amount of received packets before automatic termination, value `0` means unlimited packets. |
| -v, --verbose | enables verbose output on the beginning of the application to check the configuration. |

## Thanks
Many thanks to `com.buest` for the really nice `jcommander` library used by this app!
