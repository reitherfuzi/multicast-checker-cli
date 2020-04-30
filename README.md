# Multicast-Checker [CLI]

A small java-application which runs on the command-line only and can receive incoming multicast-traffic.
The app can be configured via passed arguments and will output the received data in different output-types.

## Commands
The following commands can be passed to the application.

| Command     | Description  |
------------------------------
| -g, --group | The multicast group-address to reiceive from |
| -p, --port  | The multicast port of the address to receive from |
| -t, --timeout | The socket-timeout in ms, if no data received after this time the app will shutdown |
| -o, --output | The type of how the data should be outputted to the console [ASCII, HEX, BINARY, OCTAL, BYTE] |
| -r, --restriction | allows to restrict the amount of received data before automatic termination, 0 value means unlimited packets |
| -v, --verbose | enables verbose output on the beginning of the application to check the config |

## Thanks
Many thanks to com.buest for the really nice `jcommander` library used by this app!
