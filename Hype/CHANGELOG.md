# Changelog
All notable changes to this project will be documented in this file.
## [2.0.55] - 2018-01-22
### Fixed
- Android
	- Compatibility with API 26 by changing the broadcast receivers

## [2.0.53] - 2018-12-21
### Fixed
- Android
	- Bug on observer removal
- iOS
	- Bug on observer removal
	- Bug on Bonjour driver during background transitions

## [2.0.50] - 2018-12-19
### Fixed
- Android
	- Bluetooth Low Energy discovery improvements 
- iOS
	- Bug on observer removal
- Error messages improvements

## [2.0.42] - 2018-10-02
### Fixed
- Android
	- Bluetooth connection improvements
- iOS
	- Bluetooth connection timeout
	- Bug on output stream using Bluetooth
	- Improvements on Bluetooth discovery
	- Performance improvements on Bluetooth
- Mesh performance improvements

## [2.0.32] - 2018-08-03
### Fixed
- Stability improvements on the segmentation layer

## [2.0.29] - 2018-07-09
### Fixed
- Android
    - Bluetooth Low Energy improvements on discovery process
- Improvements on negotiation process
- Improvements on framework stability upon reconnections

### Removed
- Android
	- Wi-Fi Direct from default start

## [2.0.0] - 2018-06-14
### Fixed
- Android
    - Bluetooth Low Energy disconnection bug
    - Wi-Fi Infrastructure and Direct throughput improvements
- Memory performance improvements
- Processing performance improvements
- Network performance improvements
### Added
- Authentication
- Encryption

## [1.0.20] - 2018-04-12
### Fixed
- Android
	- Reliability issues on Bluetooth Low Energy connections
	- Documentation issues
- Improved multi transport management

## [1.0.17] - 2018-03-23
### Fixed
- Android
    - Segment ordering on large messages
    - Memory issues
    - Reliability issues on Bluetooth Low Energy connections

## [1.0.15] - 2018-03-14
### Fixed
- Android
	- Improvements on device discovery for Wi-Fi Direct
	- Improvements on device discovery for Bluetooth Classic
	- Improved support for Bluetooth Low Energy device advertisement
- Fixed I/O crash on stream EOF

## [1.0.8] - 2018-02-12
### Added
- Android driver support for:
	- Wi-Fi Direct 
	- Wi-Fi Infrastructure
	- Bluetooth Low Energy
	- Bluetooth Classic
- iOS driver support for:
	- Wi-Fi Direct 
	- Wi-Fi Infrastructure
	- Bluetooth Low Energy
	- Bluetooth Classic
- tvOS driver support for:
	- Wi-Fi Direct 
	- Wi-Fi Infrastructure
	- Bluetooth Low Energy (partial)
	- Bluetooth Classic
- macOS driver support for:
	- Wi-Fi Direct 
	- Wi-Fi Infrastructure
	- Bluetooth Low Energy
	- Bluetooth Classic
- Windows driver support for:
	- Wi-Fi Infrastructure
	- Bluetooth Classic
- Linux driver support for:
	- Bluetooth Classic
- 5 hop mesh discovery protocol
- Data segmentation
- Delivery acknowledgement
- Progress tracking
- Announcements
- User identifiers
- User authentication
- Choice of transport
- Background execution
- Network segregation
- Patented Loop Prevention mechanism
- Adapter state observers
