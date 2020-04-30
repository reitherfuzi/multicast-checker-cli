# Multicast-Checker [CLI]

A small java-application which runs on the command-line only and can receive incoming multicast-traffic.
The app can be configured via passed arguments and will output the received data in different output-types.

## Commands
The following commands can be passed to the application.

| Command     | Description  |
|-------------|--------------|
| -g, --group | The multicast group-address to reiceive the data from. **[required]** |
| -p, --port  | The multicast port of the address to receive the data from. **[required]** |
| -t, --timeout | The socket-timeout in ms, if no data received after this time the application will shutdown. |
| -o, --output | The type of how the data should be outputted to the console [ASCII, HEX, BINARY, OCTAL, BYTE] |
| -r, --restriction | Allows to restrict the amount of received packets before automatic termination, value `0` means unlimited packets. |
| -v, --verbose | enables verbose output on the beginning of the application to check the configuration. |

## Thanks
Many thanks to `com.buest` for the really nice `jcommander` library used by this app!
