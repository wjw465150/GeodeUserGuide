# Apache Geode Reference

# gemfire.properties and gfsecurity.properties: Geode Properties

You use the `gemfire.properties` settings to join a cluster and configure system member behavior. Cluster members include applications, the cache server, the locator, and other Geode processes.

You can place any security-related (properties that begin with `security-*`) configuration properties in `gemfire.properties` into a separate `gfsecurity.properties` file. Placing these configuration settings in a separate file allows you to restrict access to security configuration data. This way, you can still allow read or write access for your `gemfire.properties` file.

You can also define provider-specific properties (“ssl” properties) in `gfsecurity.properties` instead of defining them at the command-line or in your environment.

You can specify non-ASCII text in your properties files by using Unicode escape sequences. See [Using Non-ASCII Strings in Apache Geode Property Files](https://geode.apache.org/docs/guide/17/reference/topics/non-ascii_strings_in_config_files.html) for more details.

**Note:** Unless otherwise indicated, these settings only affect activities within this cluster - not activities between clients and servers or between a gateway sender and gateway receiver in a multi-site installation.

| Setting                                              | Definition                                                   | Applies to (Server Locator, Client) | Default                                                      |
| :--------------------------------------------------- | :----------------------------------------------------------- | :---------------------------------- | :----------------------------------------------------------- |
| ack-severe-alert-threshold                           | Number of seconds the cluster will wait after the `ack-wait-threshold` for a message to be acknowledged before it issues an alert at severe level. A value of zero disables this feature. | S, L                                | 0                                                            |
| ack-wait-threshold                                   | Number of seconds a distributed message can wait for acknowledgment before it sends an alert to signal that something might be wrong with the system member that is unresponsive.The waiter continues to wait. The alerts are logged in the system member’s log as warnings.Valid values are in the range 0…2147483647 | S, L                                | 15                                                           |
| archive-disk-space-limit                             | Maximum size (in megabytes) of all inactive statistic archive files combined. If this limit is exceeded, inactive archive files are deleted, oldest first, until the total size is within the limit. If set to zero, disk space use is unlimited. | S, L                                | 0                                                            |
| archive-file-size-limit                              | The maximum size (in megabytes) of a single statistic archive file. Once this limit is exceeded, a new statistic archive file is created, and the current archive file becomes inactive. If set to zero, file size is unlimited. | S, L                                | 0                                                            |
| async-distribution-timeout                           | The number of milliseconds a process that is publishing to this process should attempt to distribute a cache operation before switching over to asynchronous messaging for this process. The switch to asynchronous messaging lasts until this process catches up, departs, or some specified limit is reached, such as async-queue-timeout or async-max-queue-size.To enable asynchronous messaging, the value must be set above zero. Valid values are in the range 0…60000.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S                                   | 0                                                            |
| async-max-queue-size                                 | Affects non-conflated asynchronous queues for members that publish to this member. This is the maximum size the queue can reach (in megabytes) before the publisher asks this member to leave the cluster.Valid values are in the range 0..1024.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S                                   | 8                                                            |
| async-queue-timeout                                  | Affects asynchronous queues for members that publish to this member. This is the maximum milliseconds the publisher should wait with no distribution to this member before it asks this member to leave the cluster. Used for handling slow receivers.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S, L                                | 60000                                                        |
| bind-address                                         | Relevant only for multi-homed hosts - machines with multiple network interface cards. Specifies the adapter card the cache binds to for peer-to-peer communication. Also specifies the default location for Geode servers to listen on, which is used unless overridden by the `server-bind-address`. An empty string causes the member to listen on the default card for the machine. This is a machine-wide attribute used for system member and client/server communication. It has no effect on locator location, unless the locator is embedded in a member process.Specify the IP address, not the hostname, because each network card may not have a unique hostname. An empty string (the default) causes the member to listen on the default card for the machine. | S, L                                | *not set*                                                    |
| cache-xml-file                                       | Declarative initialization file for the member’s cache.      | S                                   | cache.xml                                                    |
| cluster-configuration-dir                            | This property specifies the directory in which the cluster configuration related disk-store and artifacts are stored. This property is only applicable to dedicated locators that have “enable-cluster-configuration” set to true. | L                                   | *not set*                                                    |
| conflate-events                                      | Used only by clients in a client/server installation. This is a client-side property that is passed to the server. Affects subscription queue conflation in this client’s servers. Specifies whether to conflate (true setting), not conflate (false), or to use the server’s conflation setting (server). | S                                   | server                                                       |
| conserve-sockets                                     | Specifies whether sockets are shared by the system member’s threads. If true, threads share, and a minimum number of sockets are used to connect to the cluster. If false, every application thread has its own sockets for distribution purposes. You can override this setting for individual threads inside your application. Where possible, it is better to set conserve-sockets to true and enable the use of specific extra sockets in the application code if needed. WAN deployments increase the messaging demands on a Geode system. To avoid hangs related to WAN messaging, always set `conserve-sockets=false` for Geode members that participate in a WAN deployment. | S, L                                | true                                                         |
| delta-propagation                                    | Specifies whether to distribute the deltas for entry updates, instead of the full values, between clients and servers and between peers. | S                                   | true                                                         |
| deploy-working-dir                                   | Working directory used when deploying JAR application files to cluster members. This directory can be local and unique to the member or a shared resource. See [Deploying Application JARs to Apache Geode Members](https://geode.apache.org/docs/guide/17/configuring/cluster_config/deploying_application_jars.html) for more information. | S                                   | . (current directory)                                        |
| disable-auto-reconnect                               | By default, a Geode member (both locators and servers) will attempt to reconnect and reinitialize the cache after it has been forced out of the cluster by a network partition event or has otherwise been shunned by other members. Use this property to turn off the autoreconnect behavior. See [Handling Forced Cache Disconnection Using Autoreconnect](https://geode.apache.org/docs/guide/17/managing/member-reconnect.html) for more details. | S, L                                | false                                                        |
| disable-tcp                                          | Boolean indicating whether to disable the use of TCP/IP sockets for inter-cache point-to-point messaging. If disabled, the cache uses datagram (UDP) sockets. | S, L                                | false                                                        |
| distributed-system-id                                | Identifier used to distinguish messages from different clusters. This is required for Portable Data eXchange (PDX) data serialization. Set `distributed-system-id`to different values for different systems in a multi-site (WAN) configuration, and to different values for production vs. development environments. This setting must be the same for every member of a given cluster and unique to each cluster within a WAN installation.  Valid values are integers in the range -1…255. -1 means no setting. | S, L                                | -1                                                           |
| durable-client-id                                    | Used only for clients in a client/server installation. If set, this indicates that the client is durable and identifies the client. The ID is used by servers to reestablish any messaging that was interrupted by client downtime. | C                                   | *not set*                                                    |
| durable-client-timeout                               | Used only for clients in a client/server installation. Number of seconds this client can remain disconnected from its server and have the server continue to accumulate durable events for it. | C                                   | 300                                                          |
| enable-network-partition-detection                   | Boolean instructing the system to detect and handle splits in the cluster, typically caused by a partitioning of the network (split brain) where the cluster is running. You must set this property to the same value across all your cluster members. In addition, this property must be set to `true` if you are using persistent regions and configure your regions to use DISTRIBUTED_ACK or GLOBAL scope to avoid potential data conflicts. | S, L                                | true                                                         |
| enable-cluster-configuration                         | A value of “true” causes the creation of cluster configuration on dedicated locators. The cluster configuration service on dedicated locator(s) with this property set to “true” would serve the configuration to new members joining the cluster and also save the configuration changes caused by the `gfsh` commands. This property is only applicable to dedicated locators.. | L                                   | true                                                         |
| enable-time-statistics                               | Boolean instructing the system to track time-based statistics for the cluster and caching. Disabled by default for performance reasons and not recommended for production environments. You must also configure `statistics-sampling-enabled`to true and specify a `statistics-archive-file`. | S, L                                | false                                                        |
| enforce-unique-host                                  | Whether partitioned regions will put redundant copies of the same data in different members running on the same physical machine. By default, Geode tries to put redundant copies on different machines, but it will put them on the same machine if no other machines are available. Setting this property to true prevents this and requires different machines for redundant copies. | S                                   | false                                                        |
| geode.disallow-internal-messages-without-credentials | A boolean that enables internal message validation when true. Set this system property to true on the `gfsh start server` command line when restarting servers to work with upgraded clients. | S                                   | false                                                        |
| groups                                               | Defines the list of groups that this member belongs to. Use commas to separate group names. Note that anything defined by the roles gemfire property will also be considered a group. See [Using Member Groups](https://geode.apache.org/docs/guide/17/configuring/cluster_config/using_member_groups.html) for more information. | S                                   | *not set*                                                    |
| http-service-bind-address                            | If set, then the Geode member binds the embedded HTTP service to the specified address. If this property is not set but the HTTP service is enabled using `http-service-port`, then Geode binds the HTTP service to the member’s local address. Used by the Geode Pulse Web application and the developer REST API service. | S                                   | *not set*                                                    |
| http-service-port                                    | If non-zero, then Geode starts an embedded HTTP service that listens on this port. The HTTP service is used to host the Geode Pulse Web application and the development REST API service. If you are hosting the Pulse web app on your own Web server and are not using the development REST API service, then disable this embedded HTTP service by setting this property to zero. Ignored if `jmx-manager` and `start-dev-rest-api` are both set to false. | S                                   | 7070                                                         |
| jmx-manager                                          | If true then this member is willing to be a JMX Manager. All the other JMX Manager properties will be used when it does become a manager. If this property is false then all other `jmx-manager-*` properties are ignored. | S, L                                | false (except on locators)                                   |
| jmx-manager-bind-address                             | By default the jmx-manager (when configured with a port) will listen on all the local host’s addresses. You can use this property to configure what IP address or host name the JMX Manager will listen on for non-HTTP connections. Ignored if JMX Manager is false or `jmx-manager-port` is zero. | S, L                                | *not set*                                                    |
| jmx-manager-hostname-for-clients                     | Lets you control what hostname will be given to clients that ask the locator for the location of a JMX Manager. By default the IP address that the jmx-manager reports is used. But for clients on a different network this property allows you to configure a different hostname that will be given to clients. Ignored if `jmx-manager` is false or `jmx-manager-port` is zero. | S, L                                | *not set*                                                    |
| jmx-manager-http-port                                | *Deprecated.* Use `http-service-port` instead.               | S, L                                | 7070                                                         |
| jmx-manager-port                                     | The port this JMX Manager will listen to for client connections. If this property is set to zero then Geode will not allow remote client connections but you can alternatively use the standard system properties supported by the JVM for configuring access from remote JMX clients. Ignored if `jmx-manager` is false. | S, L                                | 1099                                                         |
| jmx-manager-start                                    | If true then this member will start a jmx manager when it creates a cache. Management tools like gfsh can be configured to connect to the jmx-manager. In most cases you should not set this because a jmx manager will automatically be started when needed on a member that sets “jmx-manager” to true. Ignored if jmx-manager is false. | S, L                                | false                                                        |
| jmx-manager-update-rate                              | The rate, in milliseconds, at which this member will push updates to any JMX Managers. Currently this value should be greater than or equal to the statistic-sample-rate. Setting this value too high will cause stale values to be seen by gfsh and Geode Pulse. | S, L                                | 2000                                                         |
| load-cluster-configuration-from-dir                  | Setting this property to “true” causes loading of cluster configuration from “cluster_config” directory in the locator. This property is only applicable to dedicated locators that have “enable-cluster-configuration” set to true. | L                                   | false                                                        |
| locator-wait-time                                    | The number of seconds that a member should wait for a locator to start if a locator is not available when attempting to join the cluster. Use this setting when you are starting locators and peers all at once. This timeout allows peers to wait for the locators to finish starting up before attempting to join the cluster. | S                                   | 0                                                            |
| locators                                             | The list of locators used by system members. The list must be configured consistently for every member of the cluster. If the list is empty, locators are not used.For each locator, provide a host name and/or address (separated by ‘@’, if you use both), followed by a port number in brackets. Examples:`locators=address1[port1],address2[port2] ``locators=hostName1@address1[port1],hostName2@address2[port2] ``locators=hostName1[port1],hostName2[port2]`**Note:**On multi-homed hosts, this last notation will use the default address. If you use bind addresses for your locators, explicitly specify the addresses in the locators list—do not use just the hostname.If you have values specified for the `locators` property, the `mcast-port` property defaults to 0.**Note:**If you specify invalid DNS hostnames in this property, any locators or servers started with `gfsh` will not produce log files. Make sure you provide valid DNS hostnames before starting the locator or server with `gfsh`. | S, L                                | *not set*                                                    |
| lock-memory                                          | When `true`, locks heap and off-heap memory into RAM to prevent the operating system from paging the memory out to disk. | S, L                                | false                                                        |
| log-disk-space-limit                                 | Maximum size in megabytes of all inactive log files combined. If this limit is exceeded, inactive log files are deleted, oldest first, until the total size is within the limit. If set to zero, disk space use is unlimited. | S, L                                | 0                                                            |
| log-file                                             | File to which a running system member writes log messages. If set to null, the default is used.Each member type has its own default output:application: standard outlocator: `<locator_name>.log`server: `<server_name>.log` | S, L                                | null                                                         |
| log-file-size-limit                                  | Maximum size in megabytes of a log file before it is closed and logging rolls on to a new (child) log file. If set to 0, log rolling is disabled. | S, L                                | 0                                                            |
| log-level                                            | Level of detail of the messages written to the system member’s log. Setting log-level to one of the ordered levels causes all messages of that level and greater severity to be printed.Valid values from lowest to highest are fine, config, info, warning, error, severe, and none. | S, L                                | config                                                       |
| max-wait-time-reconnect                              | Maximum number of milliseconds to wait for the cluster to reconnect on each reconnect attempt. | S, L                                | 60000                                                        |
| mcast-address                                        | Address used to discover other members of the cluster. Only used if mcast-port is non-zero. This attribute must be consistent across the cluster. Select different multicast addresses and different ports for different clusters. Do not just use different addresses. Some operating systems may not keep communication separate between systems that use unique addresses but the same port number.This default multicast address was assigned by IANA (http://www.iana.org/assignments/multicast-addresses). Consult the IANA chart when selecting another multicast address to use with Geode.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. If multicast is enabled, distributed regions use it for most communication. Partitioned regions only use multicast for a few purposes, and mainly use either TCP or UDP unicast. | S, L                                | 239.192.81.1 for IPv4 (the default IP version)FF38::1234 for IPv6 |
| mcast-flow-control                                   | Tuning property for flow-of-control protocol for unicast and multicast no-ack UDP messaging. Compound property made up of three settings separated by commas: byteAllowance, rechargeThreshold, and rechargeBlockMs.Valid values range from these minimums: 10000,0.1,500 to these maximums: no_maximum ,0.5,60000.**Note:**This setting controls only peer-to-peer communication, generally between distributed regions. | S, L                                | 1048576,0.25, 5000                                           |
| mcast-port                                           | Port used, along with the mcast-address, for multicast communication with other members of the cluster. If zero, multicast is disabled.**Note:**Select different multicast addresses and ports for different clusters. Do not just use different addresses. Some operating systems may not keep communication separate between systems that use unique addresses but the same port number.Valid values are in the range 0..65535.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication.If you have values specified for the `locators` property, the `mcast-port` property defaults to 0. | S, L                                | 10334                                                        |
| mcast-recv-buffer-size                               | Size of the socket buffer used for incoming multicast transmissions. You should set this high if there will be high volumes of messages.Valid values are in the range 2048.. OS_maximum.**Note:**The default setting is higher than the default OS maximum buffer size on Unix, which should be increased to at least 1 megabyte to provide high-volume messaging on Unix systems.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S, L                                | 1048576                                                      |
| mcast-send-buffer-size                               | The size of the socket buffer used for outgoing multicast transmissions.Valid values are in the range 2048.. OS_maximum.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S, L                                | 65535                                                        |
| mcast-ttl                                            | How far multicast messaging goes in your network. Lower settings may improve system performance. A setting of 0 constrains multicast messaging to the machine.**Note:**This setting controls only peer-to-peer communication and does not apply to client/server or multi-site communication. | S, L                                | 32                                                           |
| member-timeout                                       | Geode uses the `member-timeout` server configuration, specified in milliseconds, to detect the abnormal termination of members. The configuration setting is used in two ways: 1) First it is used during the UDP heartbeat detection process. When a member detects that a heartbeat datagram is missing from the member that it is monitoring after the time interval of 2 * the value of `member-timeout`, the detecting member attempts to form a TCP/IP stream-socket connection with the monitored member as described in the next case. 2) The property is then used again during the TCP/IP stream-socket connection. If the suspected process does not respond to the *are you alive* datagram within the time period specified in `member-timeout`, the membership coordinator sends out a new membership view that notes the member’s failure.Valid values are in the range 1000..600000. | S, L                                | 5000                                                         |
| membership-port-range                                | The range of ports available for unicast UDP messaging and for TCP failure detection. This is specified as two integers separated by a hyphen. Different members can use different ranges.Geode randomly chooses at least two unique integers from this range for the member, one for UDP unicast messaging and the other for TCP failure detection messaging. If tcp-port is configured to 0, it will also randomly select a port from this range for TCP sockets used for peer-to-peer communication only.Therefore, the specified range must include at least three available port numbers (UDP, FD_SOCK, and TCP DirectChannel).The system uniquely identifies the member using the combined host IP address and UDP port number.You may want to restrict the range of ports that Geode uses so the product can run in an environment where routers only allow traffic on certain ports. | S, L                                | 1024-65535                                                   |
| memcached-port                                       | If specified and is non-zero, sets the port number for an embedded Gemcached server and starts the Gemcached server. | S                                   | 0                                                            |
| memcached-protocol                                   | Sets the protocol used by an embedded Gemcached server. Valid values are `BINARY` and `ASCII.` If you omit this property, the ASCII protocol is used. | S                                   | ASCII                                                        |
| name                                                 | Symbolic name used to identify this system member.           | S, L                                | *not set*                                                    |
| off-heap-memory-size                                 | Specifies the size of off-heap memory in megabytes (m) or gigabytes (g). For example:`off-heap-memory-size=4096m off-heap-memory-size=120g` | S                                   | *not set*                                                    |
| redundancy-zone                                      | Defines this member’s redundancy zone. Used to separate member’s into different groups for satisfying partitioned region redundancy. If this property is set, Geode will not put redundant copies of data in members with the same redundancy zone setting. See [Configure High Availability for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html)for more details. | S                                   | *not set*                                                    |
| remote-locators                                      | Used to configure the locators that a cluster will use in order to connect to a remote site in a multi-site (WAN) configuration. To use locators in a WAN configuration, you must specify a unique distributed system ID (`distributed-system-id`) for the local cluster and remote locator(s) for the remote clusters to which you will connect.For each remote locator, provide a host name and/or address (separated by ‘@’, if you use both), followed by a port number in brackets. Examples:`remote-locators=address1[port1],address2[port2] ``remote-locators=hostName1@address1[port1],hostName2@address2[port2] ``remote-locators=hostName1[port1],hostName2[port2]` | L                                   | *not set*                                                    |
| remove-unresponsive-client                           | When this property is set to true, the primary server drops unresponsive clients from all secondaries and itself. Clients are deemed unresponsive when their messaging queues become full on the server. While a client’s queue is full, puts that would add to the queue block on the server. | S                                   | false                                                        |
| security-*                                           | Any security-related (properties that begin with `security-`) configuration properties that are normally configured in `gemfire.properties` can be moved to a separate `gfsecurity.properties` file. Placing these configuration settings in a separate file allows you to restrict access to security configuration data. This way, you can still allow read or write access for your `gemfire.properties` file. | S, L                                | *not set*                                                    |
| security-client-accessor                             | **Deprecated.** Used for authorization. Static creation method returning an `AccessControl` object, which determines authorization of client-server cache operations. This specifies the callback that should be invoked in the pre-operation phase, which is when the request for the operation is received from the client. | S, L                                | *not set*                                                    |
| security-client-accessor-pp                          | **Deprecated.** Used for authorization. The callback that should be invoked in the post-operation phase, which is when the operation has completed on the server but before the result is sent to the client. The post-operation callback is also invoked for the updates that are sent from server to client through the notification channel. | S, L                                | *not set*                                                    |
| security-client-auth-init                            | Used for authentication. Static creation method returning an `AuthInitialize`object, which obtains credentials for peers in a cluster. The obtained credentials should be acceptable to the `Authenticator` specified through the security-peer-authenticator property on the peers. | S, L                                | *not set*                                                    |
| security-client-authenticator                        | **Deprecated.** Used for authentication. Static creation method returning an `Authenticator` object, which is used by a peer to verify the credentials of the connecting peer. | S, L                                | *not set*                                                    |
| security-client-dhalgo                               | **Deprecated.** Use `ssl-enabled-components` instead.  Used for authentication. For secure transmission of sensitive credentials like passwords, you can encrypt the credentials using the Diffie-Hellman key-exchange algorithm. Do this by setting the security-client-dhalgo system property on the clients to the name of a valid, symmetric key cipher supported by the JDK. | S, L                                | *not set*                                                    |
| security-log-file                                    | Used with authentication. The log file for security log messages. If not specified, the member’s regular log file is used. | S, L                                | *not set*                                                    |
| security-log-level                                   | Used with authentication. Logging level detail for security log messages.Valid values from lowest to highest are fine, config, info, warning, error, severe, and none. | S, L                                | config                                                       |
| security-manager                                     | Specifies the implementation of the `SecurityManager` interface that implements the callbacks that do authentication and authorization. | S, L                                | *not set*                                                    |
| security-peer-auth-init                              | **Deprecated.** Used with authentication. Static creation method returning an `AuthInitialize` object, which obtains credentials for peers in a cluster. The obtained credentials should be acceptable to the `Authenticator` specified through the security-peer-authenticator property on the peers. | S, L                                | *not set*                                                    |
| security-peer-authenticator                          | **Deprecated.** Used with authentication. Static creation method returning an `Authenticator` object, which is used by a peer to verify the credentials of the connecting peer. | S, L                                | *not set*                                                    |
| security-peer-verifymember-timeout                   | Used with authentication. Timeout in milliseconds used by a peer to verify membership of an unknown authenticated peer requesting a secure connection. | S, L                                | 1000                                                         |
| security-post-processor                              | Specifies the implementation of the `PostProcessor` interface that implements user-defined callbacks that can change the returned results of region get operations. | S, L                                | *not set*                                                    |
| security-udp-dhalgo                                  | Specifies a string that defines the name of a valid, symmetric key cipher supported by the JDK. When defined, the named cipher will be used for server-to-server UDP communications. | S                                   | *not set*                                                    |
| serializable-object-filter                           | A semicolon-separated list of items that become full class names of objects that the system will serialize when the property validate-serializable-objects is set to true. The list is expanded using the patterns specified in the `createFilter`method athttps://docs.oracle.com/javase/9/docs/api/java/io/ObjectInputFilter.Config.html. | S, C                                | “!*”                                                         |
| server-bind-address                                  | Relevant only for multi-homed hosts - machines with multiple network interface cards. Network adapter card a Geode server binds to for client/server communication. You can use this to separate the server’s client/server communication from its peer-to-peer communication, spreading the traffic load.This is a machine-wide attribute used for communication with clients in client/server and multi-site installations. This setting has no effect on locator configuration.Specify the IP address, not the hostname, because each network card may not have a unique hostname.An empty string causes the servers to listen on the same card used for peer-to-peer communication. This is either the `bind-address` or, if that is not set, the machine’s default card. | S                                   | *not set*                                                    |
| socket-buffer-size                                   | Receive buffer sizes in bytes of the TCP/IP connections used for data transmission. To minimize the buffer size allocation needed for distributing large, serializable messages, the messages are sent in chunks. This setting determines the size of the chunks. Larger buffers can handle large messages more quickly, but take up more memory. | S, L                                | 32768                                                        |
| socket-lease-time                                    | Time, in milliseconds, a thread can have exclusive access to a socket it is not actively using. A value of zero causes socket leases to never expire. This property is ignored if conserve-sockets is true.Valid values are in the range 0..600000. | S, L                                | 60000                                                        |
| ssl-enabled-components                               | Components for which to enable SSL. Comma-separated list of one or more of (cluster, gateway, jmx, locator, server, web) or “all”. When defining this property, also set `security-udp-dhalgo` to secure UDP cluster communications. | S, L                                | all                                                          |
| ssl-endpoint-identification-enabled                  | A boolean value that, when set to true, causes clients to validate the server’s hostname using the server’s certificate. | C, S, L                             | false                                                        |
| ssl-require-authentication                           | Boolean. Require two-way authentication for SSL-enabled components. Applies to all components except web. | S, L                                | true                                                         |
| ssl-http-require-authentication                      | Boolean. Require two-way authentication for web component.   | S, L                                | false                                                        |
| ssl-default-alias                                    | String. Default certificate name. If empty, use first certificate in key store. | S, L                                |                                                              |
| ssl-*component*-alias                                | String. Certificate name for specified *component*, which is one of: cluster, gateway, jmx, locator, server, or web. | S, L                                |                                                              |
| ssl-ciphers                                          | Comma-separated list of SSL ciphers or “any”                 | S, L                                | any                                                          |
| ssl-protocols                                        | Comma-separated list of SSL protocols or “any”               | S, L                                | any                                                          |
| ssl-keystore, ssl-keystore-password                  | Strings. Path to key store, key store password.              | S, L                                |                                                              |
| ssl-truststore, ssl-truststore-password              | Strings. Path to trust store, trust store password.          | S, L                                |                                                              |
| ssl-keystore-type, ssl-truststore-type               | Strings. Type of key store or trust store. “JKS” indicates Java. One common alternative is “pkcs12”. | S, L                                | JKS                                                          |
| start-dev-rest-api                                   | If set to true, then the developer REST API service will be started when cache is created. REST service can be configured using `http-service-port` and `http-service-bind-address` properties. | S                                   | false                                                        |
| start-locator                                        | If set, automatically starts a locator in the current process when the member connects to the cluster and stops the locator when the member disconnects.To use, specify the locator with an optional address or host specification and a required port number, in one of these formats:`start-locator=address[port1] ``start-locator=port1`If you only specify the port, the address assigned to the member is used for the locator.If not already there, this locator is automatically added to the list of locators in this set of `gemfire properties`. | S                                   | *not set*                                                    |
| statistic-archive-file                               | The file to which the running system member writes statistic samples. For example: “StatisticsArchiveFile.gfs”. An empty string disables archiving. Adding .gz suffix to the file name causes it to be compressed. | S, L                                | *not set*                                                    |
| statistic-sample-rate                                | How often to sample statistics, in milliseconds.Valid values are in the range 100..60000. | S, L                                | 1000                                                         |
| statistic-sampling-enabled                           | Whether to collect and archive statistics on the member.Statistics sampling provides valuable information for ongoing system tuning and troubleshooting purposes. Sampling statistics at the default sample rate does not impact system performance. We recommend enabling statistics sampling in production environments.**Note:**This setting does not apply to partitioned regions, where statistics are always enabled. | S, L                                | false                                                        |
| tcp-port                                             | The TCP port to listen on for cache communications. If set to zero, the operating system selects an available port. Each process on a machine must have its own TCP port. Note that some operating systems restrict the range of ports usable by non-privileged users, and using restricted port numbers can cause runtime errors in Geode startup.Valid values are in the range 0..65535. | S, L                                | 0                                                            |
| tombstone-gc-threshold                               | The number of tombstones that can accumulate before the Geode member triggers garbage collection for tombstones. See [How Destroy and Clear Operations Are Resolved](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_321B05044B6641FCAEFABBF5066BD399). | S                                   | 100000                                                       |
| udp-fragment-size                                    | Maximum fragment size, in bytes, for transmission over UDP unicast or multicast sockets. Smaller messages are combined, if possible, for transmission up to the fragment size setting.Valid values are in the range 1000..60000. | S, L                                | 60000                                                        |
| udp-recv-buffer-size                                 | The size of the socket buffer used for incoming UDP point-to-point transmissions. If disable-tcp is false, a reduced buffer size of 65535 is used by default.The default setting of 1048576 is higher than the default OS maximum buffer size on Unix, which should be increased to at least 1 megabyte to provide high-volume messaging on Unix systems.Valid values are in the range 2048.. OS_maximum. | S, L                                | 1048576                                                      |
| udp-send-buffer-size                                 | The size of the socket buffer used for outgoing UDP point-to-point transmissions.Valid values are in the range 2048..OS_maximum. | S, L                                | 65535                                                        |
| use-cluster-configuration                            | This property is only applicable for data members (non-client and non-locator). A value of “true” causes a member to request and use the configuration from cluster configuration services running on dedicated locators. Setting this property to “false” causes a member to not request the configuration from the configuration services running on the locator(s). | S                                   | true                                                         |
| user-command-packages                                | A comma separated list of Java packages that contain classes implementing the `CommandMarker` interface. Matching classes will be loaded when the VM starts and will be available in the GFSH command-line utility. | S                                   | *not set*                                                    |
| validate-serializable-objects                        | A boolean that defaults to false. When true, instances of classes that are not internal to Geode and whose class name is not allowed by the list defined in the serializable-object-filter property will not be permitted to be deserialized. An `IncompatibleClassException` is thrown for objects not listed. JDK 8 build 121 or a later build must be installed to use this property. Servers and clients that do not meet this requirement will throw an exception upon startup. | S, C                                | false                                                        |

- **Using Non-ASCII Strings in Apache Geode Property Files**

  You can specify Unicode (non-ASCII) characters in Apache Geode property files by using a `\uXXXX` escape sequence.



### Using Non-ASCII Strings in Apache Geode Property Files

You can specify Unicode (non-ASCII) characters in Apache Geode property files by using a `\uXXXX`escape sequence.

For a supplementary character, you need two escape sequences, one for each of the two UTF-16 code units. The XXXX denotes the 4 hexadecimal digits for the value of the UTF-16 code unit. For example, a properties file might have the following entries:

```
s1=hello there
s2=\u3053\u3093\u306b\u3061\u306f
```

For example, in `gemfire.properties`, you might write:

```
log-file=my\u00df.log
```

to indicate the desired propery definition of `log-file=myß.log`.

If you have edited and saved the file in a non-ASCII encoding, you can convert it to ASCII with the `native2ascii` tool included in your Oracle Java distribution. For example, you might want to do this when editing a properties file in Shift_JIS, a popular Japanese encoding.

For more information on internationalization in Java, see http://www.oracle.com/technetwork/java/javase/tech/intl-139810.html.



# cache.xml

Use the cache.xml file to set up general cache facilities and behavior and to create and initialize cached data regions. These sections document cache.xml requirements; provide hierarchical diagrams of `<cache>` and `<client-cache>`elements; and describe the function of each element.

**Note:** You can configure most elements of the cache.xml file and apply it to your entire cluster by using the [gfsh](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html) and [cluster configuration service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html). See [gfsh Limitations](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl__section_bn3_23p_y4) for a list of items you cannot configure in gfsh and must still configure in cache.xml.



# cache.xml Quick Reference





This section documents cache.xml file requirements and variables. It also points you to specific element sections for server, client, and WAN configuration.

- [Cache XML Requirements](https://geode.apache.org/docs/guide/17/reference/topics/elements_ref.html#topic_7B1CABCAD056499AA57AF3CFDBF8ABE3__section_A6B050113DCC4D12A6A9C0F250527AF8)
- [Variables in cache.xml](https://geode.apache.org/docs/guide/17/reference/topics/elements_ref.html#topic_7B1CABCAD056499AA57AF3CFDBF8ABE3__section_5DBA12F9FC08406AAD5557E13A3DEDF2)
- [Configuration Quick Reference](https://geode.apache.org/docs/guide/17/reference/topics/elements_ref.html#topic_7B1CABCAD056499AA57AF3CFDBF8ABE3__section_2076DDF1F0464CF8894B42ABC32AE4CB)

## Cache XML Requirements

The cache.xml file has these requirements:

- The contents must conform to the XML schema definition provided in cache-1.0.xsd. The schema definition file is available at http://geode.apache.org/schema/cache/cache-1.0.xsd.

- The file must include a <cache> schema declaration of one of the following forms:

  - Server or peer cache:

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <cache
        xmlns="http://geode.apache.org/schema/cache"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
        version="1.0">
    ...
    </cache>
    ```

  - Client cache:

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <client-cache
        xmlns="http://geode.apache.org/schema/cache"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
        version="1.0">
    ...
    </client-cache>
    ```

- Any class name specified in the file **must have a public zero-argument constructor** and must implement the `org.apache.geode.cache.Declarable` interface. Parameters declared in the XML for the class are passed to the class init method.

## Variables in cache.xml

You can use variables in the `cache.xml` to customize your settings without modifying the XML file.

Set your variables in Java system properties when you start your cache server or application process.

Example cache.xml with variables and the gfsh `start server` command that sets the variables:

```
<?xml version="1.0" encoding="UTF-8"?>
<cache
    xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0">
  <cache-server port="${PORT}" max-connections="${MAXCNXS}"/>
  <region name="root">
    <region-attributes refid="REPLICATE"/>
  </region>
</cache>
gfsh>start server --name=server2 --cache-xml-file=cache.xml --J=-DPORT=30333 --J=-DMAXCNXS=77
```

## Configuration Quick Reference

To configure cache servers, clients, and WAN topologies, see the following sections:

- Server Configuration

  - [ Element Reference](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#cache_xml_cache)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#cache-server)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes)

  You can set the same server configuration properties using the `org.apache.geode.cache.server.CacheServer` and `org.apache.geode.cache.Cache` interfaces. For detailed information, see the online Java API documentation.

- Client Configuration

  - [ Element Reference](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html#cc-client-cache)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html#cc-pool)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html#cc-region)

  You can set the same client configuration properties using the `org.apache.geode.cache.clientClientCache` and `Pool` interfaces. For detailed information, see the online Java API documentation.

- Multi-site (WAN) Configuration and Asynchronous Event Queue Configuration

  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#gateway-sender)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#gateway-receiver)
  - [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#id_zrr_scq_rr)

  The gateway sender and receiver APIs in `org.apache.geode.cache.util` provide corresponding getter and setter methods for these attributes.



# <cache> Element Hierarchy





This section shows the hierarchy of `<cache>` element sub-elements that you use to configure Geode caches and servers.

For details, see [ Element Reference](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#cache_xml_cache).

```
<cache>
   <cache-transaction-manager>
      <transaction-listeners>
      <transaction-writer>
   <gateway-hub>
      <gateway>
         <gateway-endpoint>
         <gateway-listener>
         <gateway-queue>
   <gateway-sender>
      <gateway-event-filter>
      <gateway-transport-filter>
   <gateway-receiver>
      <gateway-transport-filter>
   <gateway-conflict-resolver>
      <async-event-queue>
      <async-event-listener>
         <class-name>
         <parameter>
            <string>
            <declarable>
   <cache-server>
      <group>
      <client-subscription>
      <custom-load-probe>
          <class-name>
          <parameter>
             <string>
             <declarable>
   <pool>
      <locator>
      <server>
   <disk-store>
      <disk-dirs>
         <disk-dir>
   <pdx>
      <pdx-serializer>
         <class-name>
         <parameter>
            <string>
            <declarable>
   <region-attributes>
      <key-constraint>
      <value-constraint>
      <region-time-to-live>
         <expiration-attributes>
            <custom-expiry>
               <class-name>
               <parameter>
                  <string>
                  <declarable>
      <region-idle-time>
         <expiration-attributes>
            <custom-expiry>
               <class-name>
               <parameter>
                  <string>
                  <declarable>
      <entry-time-to-live>
         <expiration-attributes>
            <custom-expiry>
               <class-name>
               <parameter>
                  <string>
                  <declarable>
      <entry-idle-time>
         <expiration-attributes>
            <custom-expiry>
               <class-name>
               <parameter>
                  <string>
                  <declarable>    
      <partition-attributes>
         <partition-resolver>
            <class-name>
               <parameter>
                  <string>
                  <declarable>
         <partition-listener>
            <class-name>
               <parameter>
                  <string>
                  <declarable>
         <fixed-partition=attributes>
      <membership-attributes>
         <required-role>
      <subscription-attributes>
         <cache-loader>
            <class-name>
            <parameter>
               <string>
               <declarable>
         <cache-writer>
            <class-name>
            <parameter>
               <string>
               <declarable>          
         <cache-listener>
            <class-name>
            <parameter>
               <string>
               <declarable>
      <compressor>        
         <class-name>
         <parameter>
            <string>
            <declarable>
      <eviction-attributes>
         <lru-entry-count>
         <lru-heap-percentage>
            <class-name>
               <parameter>
                  <string>
                  <declarable>
         <lru-memory-size>
            <class-name>
               <parameter>
                  <string>
                  <declarable>
   <jndi-bindings>
      <jndi-binding>
         <config-property>
            <config-property-name>
            <config-property-type>
            <config-property-value>
   <region>
      <region-attributes>
      <index>
      <lucene:index>
         <field>
      <entry>
         <key>
            <string>
            <declarable>
         <value>
            <string>
            <declarable>
   <vm-root-region>
   <function-service>
      <function>
         <class-name>
         <parameter>
            <string>
            <declarable>
   <resource-manager>
   <serialization-registration>
      <serializer>
         <class-name>
            <instantiator>
         <class-name>
            <backup>
   <initializer><initializer>
      <class-name>
      <parameter>
         <string>
         <declarable>
</cache>
```



# `<cache>` Element Reference

This section documents the `cache.xml` sub-elements used for Geode server configuration. All elements are sub-elements of the `<cache>` element.

For Geode client configuration, see [ Element Reference](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html).

**API**:`org.apache.geode.cache.CacheFactory`

**<cache> Attributes**

| Attribute             | Description                                                  | Default |
| :-------------------- | :----------------------------------------------------------- | :------ |
| copy-on-read          | Boolean indicating whether entry value retrieval methods return direct references to the entry value objects in the cache (false) or copies of the objects (true). | False   |
| is-server             | Boolean indicating whether this member is a cache server.    | False   |
| lock-timeout          | The timeout, in seconds, for implicit object lock requests. This setting affects automatic locking only, and does not apply to manual locking. If a lock request does not return before the specified timeout period, it is cancelled and returns with a failure. | 60      |
| lock-lease            | The timeout, in seconds, for implicit and explicit object lock leases. This affects both automatic locking and manual locking. Once a lock is obtained, it can remain in force for the lock lease time period before being automatically cleared by the system. | 120     |
| message-sync-interval | Used for client subscription queue synchronization when this member acts as a server to clients and server redundancy is used. Sets the frequency (in seconds) at which the primary server sends messages to its secondary servers to remove queued events that have already been processed by the clients. | 1       |
| search-timeout        | How many seconds a `netSearch` operation can wait for data before timing out. You may want to change this based on your knowledge of the network load or other factors. | 300     |

**Example:**

```
<cache>
  <cache-server port="40404" />
  <region name="root">
    <region-attributes refid="REPLICATE"/>
    <region name="cs_region" refid="REPLICATE">
      <region-attributes>
        <cache-loader>
          <class-name>cacheRunner.StringLoader</class-name>
        </cache-loader>
          <cache-listener>
          <class-name>cacheRunner.LoggingCacheListener</class-name>
        </cache-listener>
      </region-attributes>
    </region>
  </region>
</cache>
```

## `<cache-transaction-manager>`

Specifies a transaction listener.

**API:** `CacheTransactionManager`

**Example:**

```
<cache search-timeout="60">
   <cache-transaction-manager>
     <transaction-listener>
       <class-name>com.company.data.MyTransactionListener</class-name>
       <parameter name="URL">
         <string>jdbc:cloudscape:rmi:MyData</string>
       </parameter>
     </transaction-listener>
     <transaction-listener>... </transaction-listener> 
     <transaction-writer>
       <class-name>com.company.data.MyTransactionWriter</class-name>
       <parameter name="URL">
         <string>jdbc:cloudscape:rmi:MyData</string>
       </parameter>
       <parameter>
     </transaction-writer>
   </cache-transaction-manager> .. .
</cache>
```

## `<transaction-listener>`

When a transaction ends, its thread calls the TransactionListener to perform the appropriate follow-up for successful commits, failed commits, or voluntary rollbacks.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

## <transaction-writer>

When you commit a transaction, a TransactionWriter can perform additional tasks, including aborting the transaction.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

## <dynamic-region-factory>

The `<dynamic-region-factory>` element configures a dynamic region factory for this cache. You can use this element to dynamically create regions in your application code. Use the createDynamicRegion() method of the org.apache.geode.cache.DynamicRegionFactory class in your Java code to dynamically create regions.

**Note:** You can not use this element to dynamically create *partitioned* regions.

We recommend that you use functions to dynamically create regions. See [Creating Regions Dynamically](https://geode.apache.org/docs/guide/17/developing/region_options/dynamic_region_creation.html).

The optional `<disk-dir>` sub-element specifies the directory to store the persistent files that are used for dynamic region bookkeeping. It defaults to the current directory.

Set the `pool-name` attribute to set the name of the connection pool used by client applications in a client/server cache configuration. Do not specify the `pool-name` attribute in servers or peers.

**API:** `org.apache.geode.cache.DynamicRegionFactory`

**<dynamic-region-factory> Attributes**

| Attribute                 | Description                                                  | Default |
| :------------------------ | :----------------------------------------------------------- | :------ |
| disable-persist-backup    | When set to false, the factory is persisted on disk.         | false   |
| disable-register-interest | When set to false, client regions created by the factory register interest in all keys in a corresponding server cache region. | false   |
| pool-name                 | The name of a connection pool used by the client factory to communicate with the server-side factory. | none    |

**Example:**

```
<dynamic-region-factory 
     pool-name=myPool>
     <disk-dir>/home/gemfire/myDiskdir</disk-dir>
</dynamic-region-factory>
```

## <disk-dir>

Specifies a region or disk store’s disk directory.

**<disk-dir> Attributes**

| Attribute | Description                                                  | Default                 |
| :-------- | :----------------------------------------------------------- | :---------------------- |
| dir-size  | Maximum amount of space to use for the disk store, in megabytes. | 214748364 (2 petabytes) |

**Example:**

```
<disk-dir 
    dir-size="20480">/host3/users/gf/memberA_DStore</disk-dir> 
```

## <gateway-sender>

Configures a gateway sender to distribute region events to another Geode site. See [Configuring a Multi-site (WAN) System](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html#setting_up_a_multisite_system).

**API:** `GatewaySender`

**<gateway-sender> Attributes**

| Attribute                    | Description                                                  | Default |
| :--------------------------- | :----------------------------------------------------------- | :------ |
| parallel                     | Value of “true” or “false” that specifies the type of gateway sender that Geode creates. | false   |
| dispatcher-threads           | Number of dispatcher threads that are used to process region events from a gateway sender queue or asynchronous event queue. | 5       |
| order-policy                 | When the `dispatcher-threads` attribute is greater than 1, `order-policy` configures the way in which multiple dispatcher threads process region events from a serial gateway queue or serial asynchronous event queue. This attribute can have one of the following values:**key**. When distributing region events from the local queue, multiple dispatcher threads preserve the order of key updates.**thread**. When distributing region events from the local queue, multiple dispatcher threads preserve the order in which a given thread added region events to the queue.**partition**. This option is valid for parallel event queues. When distributing region events from the local queue, multiple dispatcher threads preserve the order in which region events were added to the local queue. For a partitioned region, this means that all region events delivered to a specific partition are delivered in the same order to the remote Geode site. For a distributed region, this means that all key updates delivered to the local queue are distributed to the remote site in the same order.You cannot configure the `order-policy` for a parallel event queue, because parallel queues cannot preserve event ordering for regions. Only the ordering of events for a given partition (or in a given queue of a distributed region) can be preserved. | key     |
| id                           | Unique identifier for the gateway sender, usually an identifier associated with a physical location. This attribute is required. | null    |
| remote-distributed-system-id | Integer that uniquely identifies the remote Geode cluster to which this gateway sender will send region events. This value corresponds to the `distributed-system-id`property specified in locators for the remote cluster. This attribute is required. | null    |
| manual-start                 | **Deprecated.** Boolean value that specifies whether you need to manually start the gateway sender. If you supply a null value, the default value of false is used, and the gateway sender attempts to start automatically. *A manual start is likely to cause data loss, so manual start should never be used in a production system.* | false   |
| socket-buffer-size           | Size of the socket buffer that sends messages to remote sites. This size should match the size of the `socket-buffer-size` attribute of remote gateway receivers that process region events. | 32768   |
| socket-read-timeout          | Amount of time in milliseconds that the gateway sender will wait to receive an acknowledgment from a remote site. By default this is set to 0, which means there is no timeout. If you do set this timeout, you must set it to a minimum of 30000 (milliseconds). Setting it to a lower number will generate an error message and reset the value to the default of 0. | 0       |
| enable-batch-conflation      | Boolean value that determines whether Geode should conflate messages. | false   |
| batch-size                   | Maximum number of messages that a batch can contain.         | 100     |
| batch-time-interval          | Maximum number of milliseconds that can elapse between sending batches. | 1000    |
| enable-persistence           | Boolean value that determines whether Geode persists the gateway queue. | false   |
| disk-store-name              | Named disk store to use for storing the queue overflow, or for persisting the queue. If you specify a value, the named disk store must exist. If you specify a null value, Geode uses the default disk store for overflow and queue persistence. |         |
| disk-synchronous             | For regions that write to disk, boolean that specifies whether disk writes are done synchronously for the region. | true    |
| maximum-queue-memory         | Maximum amount of memory in megabytes that the queue can consume before overflowing to disk. | 100 MB  |
| alert-threshold              | Maximum number of milliseconds that a region event can remain in the gateway sender queue before Geode logs an alert. | 0       |



**Example:**

```
<cache>

  <gateway-sender 
    id="remoteA" 
    parallel="true" 
    remote-distributed-system-id="1"> 
    <gateway-event-filter>
      <class-name>org.apache.geode.util.SampleEventFilter</class-name>
      <parameter 
       name="param1">
        <string>"value1"</string>
      </parameter>
    </gateway-event-filter>
    <gateway-transport-filter>
      <class-name>org.apache.geode.util.SampleTransportFilter</class-name>
      <parameter 
       name="param1">
        <string>"value1"</string>
      </parameter>
    </gateway-transport-filter>
  </gateway-sender> 
  ...
</cache>
```

## <gateway-event-filter>

A GatewayEventFilter implementation determines whether a region event is placed in a gateway sender queue and/or whether an event in a gateway queue is distributed to a remote site. You can optionally add one or more GatewayEventFilter implementations to a gateway sender, either in the cache.xml configuration file or using the Java API.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Example:**

```
<gateway-event-filter>
      <class-name>org.apache.geode.util.SampleEventFilter</class-name>
      <parameter name="param1">
        <string>"value1"</string>
      </parameter>
</gateway-event-filter>
```

## <gateway-event-substitution-filter>

A GatewayEventSubstitutionFilter provides a way to specify a substitute value to be stored in the GatewayQueueEvent and enqueued in the RegionQueue. You can optionally add one GatewayEventSubstitutionFilter implementation to a gateway sender.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Example:**

```
<gateway-event-substitution-filter>
      <class-name>org.apache.geode.util.SampleEventSubstitutionFilter</class-name>
      <parameter name="param1">
        <string>"value1"</string>
      </parameter>
</gateway-event-substitution-filter>
```

## <gateway-transport-filter>

Use a GatewayTransportFilter implementation to process the TCP stream that sends a batch of events that is distributed from one Geode cluster to another over a WAN. A GatewayTransportFilter is typically used to perform encryption or compression on the data that distributed. You install the same GatewayTransportFilter implementation on both a gateway sender and gateway receiver.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Example:**

```
<gateway-transport-filter>
      <class-name>org.apache.geode.util.SampleTransportFilter</class-name>
      <parameter 
       name="param1">
        <string>"value1"</string>
      </parameter>
</gateway-transport-filter>
```

## <gateway-receiver>

Configures a gateway receiver to receive and apply region events that were distributed from another Geode site. You can only specify one gateway receiver on a member. See [Configuring a Multi-site (WAN) System](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html#setting_up_a_multisite_system).

**API:** `GatewayReceiverFactory`, `GatewayTransportFilter`

**<gateway-receiver> Attributes**

| Attribute                  | Description                                                  | Default |
| :------------------------- | :----------------------------------------------------------- | :------ |
| start-port                 | Starting port number to use when specifying the range of possible port numbers this gateway receiver will use to connects to gateway senders in other sites. Geode chooses an unused port number in the specified port number range to start the receiver. If no port numbers in the range are available, an exception is thrown.The `STARTPORT` value is inclusive while the `ENDPORT` value is exclusive. For example, if you specify `STARTPORT="50510"` and `ENDPOINT="50520"`, Geode chooses a port value from 50510 to 50519. | 5000    |
| end-port                   | Defines the upper bound port number to use when specifying the range of possible port numbers this gateway receiver will use to for connections from gateway senders in other sites. Geode chooses an unused port number in the specified port number range to start the receiver. If no port numbers in the range are available, an exception is thrown.The `ENDPORT` value is exclusive while the `STARTPORT` value is inclusive. For example, if you specify `STARTPORT="50510"` and `ENDPOINT="50520"`, Geode chooses a port value from 50510 to 50519. | 5500    |
| bind-address               | Network address for connections from gateway senders in other sites. Specify the address as a literal string value. |         |
| hostname-for-senders       | Attribute where you can specify an IP address or hostname for gateway sender connections. If you configure hostname-for-senders, locators will use the provided hostname or IP address when instructing gateway senders on how to connect to gateway receivers. If you provide “” or null as the value, by default the gateway receiver’s bind-address will be sent to clients. |         |
| manual-start               | When set to false, the gateway receiver will automatically start when the receiver is created. If set to true, you must manually start the receiver. | true    |
| maximum-time-between-pings | Integer value that specifies the time interval (in milliseconds) to use between pings to connected WAN sites. This value determines the maximum amount of time that can elapse before a remote WAN site is considered offline. | 60000   |
| socket-buffer-size         | An integer value that sets the buffer size (in bytes) of the socket connection for this gateway receiver. This value should match the `socket-buffer-size` setting of gateway senders that connect to this receiver. | 32768   |

**Example:**

```
<cache>
     <gateway-receiver start-port="1530" end-port="1551"> 
    <gateway-transport-filter>>
      <class-name>org.apache.geode.util.SampleTransportFilter</class-name>
      <parameter 
       name="param1">
        <string>"value1"</string>
      </parameter>
    </gateway-transport-filter>
  </gateway-receiver>
</cache>
```

## <gateway-transport-filter>

Use a GatewayTransportFilter implementation to process the TCP stream that sends a batch of events that is distributed from one Geode cluster to another over a WAN. A GatewayTransportFilter is typically used to perform encryption or compression on the data that distributed. You install the same GatewayTransportFilter implementation on both a gateway sender and gateway receiver.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Example:**

```
<gateway-transport-filter>
      <class-name>org.apache.geode.util.SampleTransportFilter</class-name>
      <parameter 
       name="param1">
        <string>"value1"</string>
      </parameter>
</gateway-transport-filter>
```

## <gateway-conflict-resolver>

An event-handler plug-in that is called in order to determine whether a potentially conflicting WAN update should be applied to the local cache. A GatewayConflictResolver is invoked if the current value in a cache entry was established by a different cluster (with a different distributed-system-id) than an event that is attempting to modify the entry. It is not invoked if the event has the same distributed system ID as the event that last changed the entry. See [Resolving Conflicting Events](https://geode.apache.org/docs/guide/17/developing/events/resolving_multisite_conflicts.html#topic_E97BB68748F14987916CD1A50E4B4542)

Specify the Java class for the gateway conflict resolver plug-in and its initialization parameters with the `<class-name>` and `<parameter>` sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**API:** `org.apache.geode.cache.util.GatewayConflictResolver`

**Example:**

```
<gateway-conflict-resolver>
    <class-name>
     myPackage.MyConflictResolver
    </class-name>
  </gateway-conflict-resolver>
```

## <async-event-queue>

Configures a queue for sending region events to an AsyncEventListener implementation (for example, for write-behind event handling).

**API:** `org.apache.geode.cache.asyncqueue.AsyncEventQueue`

**<async-event-queue> Attributes**

| Attribute                  | Description                                                  | Default                               |
| :------------------------- | :----------------------------------------------------------- | :------------------------------------ |
| id                         | Unique identifier for the queue. This attribute is required. | null                                  |
| parallel                   | Value of “true” or “false” that specifies the type of queue that Geode creates. | false                                 |
| batch-size                 | Maximum number of messages that a batch can contain.         | 100                                   |
| batch-time-interval        | Maximum number of milliseconds that can elapse between sending batches. | 5                                     |
| enable-batch-conflation    | Boolean value that determines whether Geode should conflate messages. | false                                 |
| disk-store-name            | Named disk store to use for storing queue overflow, or for persisting the queue. If you specify a value, the named disk store must exist. If you specify a null value, Geode uses the default disk store for overflow and queue persistence. | null specifies the default disk store |
| disk-synchronous           | For regions that write to disk, boolean that specifies whether disk writes are done synchronously for the region. | true                                  |
| dispatcher-threads         | Number of dispatcher threads that are used to process region events from the queue. | 5                                     |
| forward-expiration-destroy | When true, forwards expiration destroy operations to AsyncEventListener. | false                                 |
| maximum-queue-memory       | Maximum amount of memory in megabytes that the queue can consume before overflowing to disk. | 100 mb                                |
| order-policy               | When the `dispatcher-threads` attribute is greater than 1, `order-policy` configures the way in which multiple dispatcher threads process region events from the queue. This attribute can have one of the following values:**key**. When distributing region events from the local queue, multiple dispatcher threads preserve the order of key updates.**thread**. When distributing region events from the local queue, multiple dispatcher threads preserve the order in which a given thread added region events to the queue.**partition**. This option is valid for parallel event queues. When distributing region events from the local queue, multiple dispatcher threads preserve the order in which region events were added to the local queue. For a partitioned region, this means that all region events delivered to a specific partition are delivered in the same order to the remote Geode site. For a distributed region, this means that all key updates delivered to the local queue are distributed to the remote site in the same order.You cannot configure the `order-policy` for a parallel event queue, because parallel queues cannot preserve event ordering for regions. Only the ordering of events for a given partition (or in a given queue of a distributed region) can be preserved. | key                                   |
| persistent                 | Boolean value that determines whether Geode persists this queue. | False                                 |

**Example:**

```
<cache>
   <async-event-queue 
    id="sampleQueue" 
    persistent="true"
    disk-store-name="exampleStore" 
    parallel="false">
      <async-event-listener>
         <class-name>MyAsyncEventListener</class-name>
         <parameter name="url"> 
           <string>jdbc:db2:SAMPLE</string> 
         </parameter> 
         <parameter name="username"> 
           <string>gfeadmin</string> 
         </parameter> 
         <parameter name="password"> 
           <string>admin1</string> 
         </parameter>
      </async-event-listener>
   </async-event-queue>
    ...
</cache>
```

## <async-event-listener>

An AsyncEventListener receives callbacks for events that change region data. You can use an AsyncEventListener implementation as a write-behind cache event handler to synchronize region updates with a database.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**API:** `org.apache.geode.cache.asyncqueue.AsyncEventListener`

**Example:**

```
...
    <async-event-listener>
         <class-name>MyAsyncEventListener</class-name>
         <parameter name="url"> 
           <string>jdbc:db2:SAMPLE</string> 
         </parameter> 
         <parameter name="username"> 
           <string>gfeadmin</string> 
         </parameter> 
         <parameter name="password"> 
           <string>admin1</string> 
         </parameter> 
    </async-event-listener>
...
```

## <cache-server>

Configures the cache to serve region data to clients in a client/server caching system. This element indicates the port the server listens on for client communication.

The `cacheserver` process uses only `cache.xml` configuration. For application servers, you can set the same configuration properties using the `org.apache.geode.cache.server.CacheServer` and `org.apache.geode.cache.Cache` interfaces. For detailed information, see the online Java API documentation.

**API:** `org.apache.geode.cache.server.CacheServer`

**<cache-server> Attributes**

| Attribute                  | Description                                                  | Default Value   |
| :------------------------- | :----------------------------------------------------------- | :-------------- |
| bind-address               | Hostname or IP address that the server is to listen on for client connections. If null, the server listens on the machine’s default address. | null            |
| hostname-for-clients       | Hostname or IP address to pass to the client as the location where the server is listening. When the server connects to the locator it tells the locator the host and port where it is listening for client connections. If the host the server uses by default is one that the client can’t translate into an IP address, the client will have no route to the server’s host and won’t be able to find the server. For this situation, you must supply the server’s alternate hostname for the locator to pass to the client. If null, the server’s `bind-address`setting is used. | null            |
| load-poll-interval         | Frequency, in milliseconds, to poll the load probe for load information on the server. | 5000(5 seconds) |
| max-connections            | Maximum number of client connections for the server. When the maximum is reached, the server refuses additional client connections.**Note:** Set this at least as high as max-threads. | 800             |
| max-threads                | Maximum number of threads allowed in this server to service client connections. When the limit is reached, server threads begin servicing multiple connections. A zero setting causes the server to use a thread for every client connection.**Note:** Set this no higher than max-connections. | 0               |
| maximum-message-count      | Maximum number of messages allowed in a subscription queue. When the queue reaches this limit, messages block.**Note:** Used only if `client-subscription` is not configured. | 230000          |
| maximum-time-between-pings | Maximum time, in milliseconds, the server allows to pass between messages or pings indicating a client is healthy.**Note:** A setting of 0 or a negative number turns off client health monitoring. Be careful not to do this accidentally. | 60000(1 minute) |
| message-time-to-live       | Setting used for highly available subscription queues. The expiration time, in seconds, for non-durable messages in the secondary server’s client subscription queue. The system removes non-durable messages that have been in the queue beyond this time. If set to 0 (zero), the messages are never removed.**Note:** Set this high enough to avoid removing messages that are still valid, to aovid losing messages during server failover. | 180(3 minutes)  |
| port                       | Port that the server listens on for client communication.    | 40404           |
| socket-buffer-size         | Size for socket buffers used for server-to-client communication. | 32768           |
| tcp-no-delay               | When set to true, enables TCP_NODELAY for Geode server connections to clients. | false           |

**Example:**

```
<cache>
    <cache-server 
     port="40404" 
      />
   ...
</cache>
```

## <client-subscription>

Overflow specification for client subscription queues. Sets a capacity limit on the in-memory queue and specifies where to overflow when capacity is reached. By default no overflow is used. Specified in three parts:

**Default:** no overflow

**API:** `org.apache.geode.cache.server.ClientSubscriptionConfig`

**<client-subscription> Attributes**

| Attribute       | Description                                                  | Default            |
| :-------------- | :----------------------------------------------------------- | :----------------- |
| eviction-policy | How the capacity is calculated. The options are mem for memory use, entry for message count, and null for no overflow. | null               |
| capacity        | Used if eviction-policy is not null. Specified in megabytes for mem and as a positive integer for entry. | 1                  |
| disk-store-name | Used if eviction-policy is not null. Default: default disk store. If specified, the disk-store-name must specify a disk store that is already defined in the cache. | default disk store |

**Example:**

```
<cache>
 <cache-server port=4444>
   <client-subscription eviction-policy="entry | mem" capacity=35 overflow-directory="OverflowDir"></client-subscription>
   ...
 </cache-server>
</cache>
```

## <custom-load-probe>

Application plug-in used to provide current and predicted server load information to the server locators.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Default:** If this is not defined, the default Geode load probe is used.

**API:** `org.apache.geode.cache.server.setLoadProbe`

**Example:**

```
<custom-load-probe>
    <class-name>
     myPackage.MyLoadProbe
    </class-name>
  </custom-load-probe>
```

## <pool>

Use for client caches. Defines a client’s server pool used to communicate with servers running in a different cluster.

**API:** `org.apache.geode.cache.client.PoolFactory`

**<pool> Attributes**

| Attribute                             | Description                                                  | Default            |
| :------------------------------------ | :----------------------------------------------------------- | :----------------- |
| free-connection-timeout               | Amount of time a thread will wait to get a pool connection before timing out with an exception. This timeout keeps threads from waiting indefinitely when the pool’s `max-connections` has been reached and all connections in the pool are in use by other threads. | 10000              |
| idle-timeout                          | Maximum time, in milliseconds, a pool connection can stay open without being used when there are more than `min-connections` in the pool. Pings over the connection do not count as connection use. If set to -1, there is no idle timeout. | 5000               |
| load-conditioning-interval            | Amount of time, in milliseconds, a pool connection can remain open before being eligible for silent replacement to a less-loaded server. | 300000(5 minutes)  |
| max-connections                       | Maximum number of pool connections the pool can create. If the maximum connections are in use, an operation requiring a client-to-server connection blocks until a connection becomes available or the `free-connection-timeout` is reached. If set to -1, there is no maximum. The setting must indicate a cap greater than `min-connections`.**Note:** If you need to use this to cap your pool connections, you should disable the pool attribute `pr-single-hop-enabled`. Leaving single hop enabled can increase thrashing and lower performance. | -1                 |
| min-connections                       | Minimum number of pool connections to keep available at all times. Used to establish the initial connection pool. If set to 0 (zero), no connection is created until an operation requires it. This number is the starting point, with more connections added later as needed, up to the `max-connection` setting. The setting must be an integer greater than or equal to 0. | 1                  |
| multiuser-authentication              | Used for installations with security where you want to accommodate multiple users within a single client. If set to true, the pool provides authorization for multiple user instances in the same client application, and each user accesses the cache through its own `RegionService` instance. If false, the client either uses no authorization or just provides credentials for the single client process. | false              |
| name                                  | Name of this pool. Used in the client region pool-name to assign this pool to a region in the client cache.**Note:** This is a required property with no default setting. | none               |
| ping-interval                         | How often to communicate with the server to show the client is alive, set in milliseconds. Pings are only sent when the ping-interval elapses between normal client messages.**Note:** Set this lower than the server’s `maximum-time-between-pings`. | 10000              |
| pr-single-hop-enabled                 | Setting used to improve access to partitioned region data in the servers. Indicates whether to use metadata about the partitioned region data storage locations to decide where to send some data requests. This allows a client to send a data operation directly to the server hosting the key. Without this, the client contacts any available server and that server contacts the data store. This is used only for operations that can be carried out on a server-by-server basis, like put, get, and destroy. | true               |
| read-timeout                          | Maximum time, in milliseconds, for the client to wait for a response from a server. | 10000              |
| retry-attempts                        | Number of times to retry a client request before giving up. If one server fails, the pool moves to the next, and so on until it is successful or it hits this limit. If the available servers are fewer than this setting, the pool will retry servers that have already failed until it reaches the limit. If this is set to -1, the pool tries every available server once. | -1                 |
| server-group                          | Logical named server group to use from the pool. A null value uses the global server group to which all servers belong.**Note:** This is only used when the `locator` list is defined. | null               |
| socket-buffer-size                    | Size for socket buffers from the client to the server. Default: 32768. | 32768              |
| statistic-interval                    | Interval, in milliseconds, at which to send client statistics to the server. If set to -1, statistics are not sent. | -1                 |
| subscription-ack-interval             | Time, in milliseconds, between messages to the primary server to acknowledge event receipt.**Note:** Used only when `subscription-redundancy` is not ‘0’ (zero). | 100                |
| subscription-enabled                  | Boolean indicating whether the server should connect back to the client and automatically sends server-side cache update information. Any bind address information for the client is automatically passed to the server for use in the callbacks. | false              |
| subscription-message-tracking-timeout | Time-to-live, in milliseconds, for entries in the client’s message tracking list. | 900000(15 minutes) |
| subscription-redundancy               | Number of servers to use as backup to the primary for highly available subscription queue management. If set to 0, none are used. If set to -1, all available servers are used. | 0                  |
| thread-local-connections              | Boolean specifying whether connections are sticky. True causes the connection to stick to the thread for multiple requests. False causes each connection to be returned to the pool after a request finishes. A sticky connection is returned to the pool when the thread releases it through the `Pool` method `releaseThreadLocalConnection`, when the `idle-timeout` is reached, or when the pool is destroyed. | false              |

**Example:**

```
<pool 
   name="publisher" 
   subscription-enabled="true">
     <locator 
       host="myLocatorAddress1" 
       port="12345"/>
     <locator 
       host="myLocatorAddress2" 
       port="45678"/>
</pool>
```

## <locator>

Addresses and ports of the locators to connect to. You can define multiple locators for the pool.

**Note:** Provide a locator list or `server` list, but not both.

**API:** `org.apache.geode.distributed.LocatorLauncher`

**<locator> Attributes**

| Attribute | Description                | Default |
| :-------- | :------------------------- | :------ |
| host      | Hostname of the locator    |         |
| port      | Port number of the locator |         |

**Example:**

```
<pool ...>
<locator 
       host="myLocatorHost" 
       port="12345"/>
```

## <server>

Addresses and ports of the servers to connect to.

**Note:** Provide a server list or `locator` list, but not both.

**Default:**

**API:** `org.apache.geode.distributed.ServerLauncher`

**<server> Attributes**

| Attribute | Description               | Default |
| :-------- | :------------------------ | :------ |
| host      | Hostname of the server    |         |
| port      | Port number of the server |         |

**Example:**

```
<pool ...>
   <server 
       host="myServerHost" 
       port="123456"/>
</pool>
```

## <disk-store>

Defines a pool of one or more disk stores, which can be used by regions, and client subscription queues.

**Default:** The cache default disk store, named “DEFAULT”, is used when disk is used but no disk store is named.

**API:** `org.apache.geode.cache.DiskStore`

**<disk-store> Attributes**

| Attribute                      | Description                                                  | Default |
| :----------------------------- | :----------------------------------------------------------- | :------ |
| name                           | The name of the Disk Store.                                  |         |
| auto-compact                   | Set to true to automatically compact the disk files.         |         |
| compaction-threshold           | The threshold at which an oplog will become compactable. Until it reaches this threshold the oplog will not be compacted.The threshold is a percentage in the range 0 to 100. |         |
| allow-force-compaction         | Set to true to allow disk compaction to be forced on this disk store. |         |
| max-oplog-size                 | The maximum size, in megabytes, of an oplog (operation log) file. |         |
| time-interval                  | The number of milliseconds that can elapse before unwritten data is written to disk. |         |
| write-buffer-size              | The size of the write buffer that this disk store uses when writing data to disk. Larger values may increase performance but use more memory. The disk store allocates one direct memory buffer of this size. |         |
| queue-size                     | Maximum number of operations that can be asynchronously queued to be written to disk. |         |
| disk-usage-warning-percentage  | Disk usage above this threshold generates a warning message. For example, if the threshold is set to 90%, then on a 1 TB drive falling under 100 GB of free disk space generates the warning.Set to “0” (zero) to disable. | 90      |
| disk-usage-critical-percentage | Disk usage above this threshold generates an error message and shuts down the member’s cache. For example, if the threshold is set to 99%, then falling under 10 GB of free disk space on a 1 TB drive generates the error and shuts down the cache.Set to “0” (zero) to disable. | 99      |

**Example:**

```
<disk-store 
    name="DEFAULT" 
    allow-force-compaction="true">
     <disk-dirs>
        <disk-dir>/export/thor/customerData</disk-dir>
        <disk-dir>/export/odin/customerData</disk-dir>
        <disk-dir>/export/embla/customerData</disk-dir>
     </disk-dirs>
</disk-store>
```

## <disk-dirs>

An element of a disk store that defines a set of `<disk-dir>` elements.

## <disk-dir>

Specifies a region or disk store’s disk directory.

**<disk-dir> Attributes**

| Attribute | Description                                                  | Default                |
| :-------- | :----------------------------------------------------------- | :--------------------- |
| dir-size  | Maximum amount of space to use for the disk store, in megabytes. | 214748364(2 petabytes) |

**Example:**

```
<disk-dir 
    dir-size="20480">/host3/users/gf/memberA_DStore</disk-dir> 
```

## <pdx>

Specifies the configuration for the Portable Data eXchange (PDX) method of serialization.

**API:** `org.apache.geode.cache.CacheFactory.setPdxReadSerialized`, `setPdxDiskStore`, `setPdxPersistent`, `setPdxIgnoreUnreadFields` and `org.apache.geode.cache.ClientCacheFactory.setPdxReadSerialized`, `setPdxDiskStore`, `setPdxPersistent`, `setPdxIgnoreUnreadFields`

**<pdx> Attributes**

| Attribute            | Description                                                  | Default |
| :------------------- | :----------------------------------------------------------- | :------ |
| read-serialized      | Set it to true if you want PDX deserialization to produce a PdxInstance instead of an instance of the domain class. |         |
| ignore-unread-fields | Set it to true if you do not want unread PDX fields to be preserved during deserialization. You can use this option to save memory. Set to true only in members that are only reading data from the cache. |         |
| persistent           | Set to true if you are using persistent regions. This causes the PDX type information to be written to disk. |         |
| disk-store-name      | If using persistence, this attribute allows you to configure the disk store that the PDX type data will be stored in. By default, the default disk store is used. |         |

**Example:**

```
<cache>
  <pdx persistent="true" disk-store-name="myDiskStore">
    <pdx-serializer>
      <class-name>
       org.apache.geode.pdx.ReflectionBasedAutoSerializer
      </class-name>
    <parameter name="classes">
      <string>com.company.domain.DomainObject</string>>
    </parameter>
  </pdx-serializer>
 </pdx>
  ...
</cache>
```

## <pdx-serializer>

Allows you to configure the PdxSerializer for this Geode member.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Default:**

**API:** `org.apache.geode.cache.CacheFactory.setPdxSerializer`

**Example:**

```
<cache>
  <pdx>
    <pdx-serializer>
     <class-name>com.company.ExamplePdxSerializer</class-name>
    </pdx-serializer>
  </pdx> 
  ...
</cache>
```

## <region-attributes>

Specifies a region attributes template that can be named (by `id`) and referenced (by `refid`) later in the `cache.xml` and through the API.

**API:** `org.apache.geode.cache.RegionFactory` or `org.apache.geode.cache.ClientRegionFactory`

**<region-attributes> Attributes**

| Attribute                      | Description                                                  | Default             |
| :----------------------------- | :----------------------------------------------------------- | :------------------ |
| concurrency-level              | Gives an estimate of the maximum number of application threads that will concurrently access a region entry at one time. This attribute does not apply to partitioned regions. This attribute helps Geode optimize the use of system resources and reduce thread contention. This sets an initial parameter on the underlying `java.util.ConcurrentHashMap`used for storing region entries.**Note:** Before you modify this, read the concurrency level description, then see the Java API documentation for `java.util.ConcurrentHashMap`.**API:** `setConcurrencyLevel`**Example:**`<region-attributes    concurrency-level="10"> </region-attributes>` | 16 (threads)        |
| data-policy                    | Specifies how the local cache handles data for a region. This setting controls behavior such as local data storage and region initialization.**Note:** Configure the most common options using the region shortcuts, `RegionShortcut` and `ClientRegionShortcut`. The default `data-policy` of `normal` specifies local cache storage. The empty policy specifies no local storage. In the region shortcuts, empty corresponds to the settings with the string `PROXY`. You can use an empty region for event delivery to and from the local cache without the memory overhead of data storage.You can specify the following data policies:emptyNo data storage in the local cache. The region always appears empty. Use this for event delivery to and from the local cache without the memory overhead of data storage - zero-footprint producers that only distribute data to others and zero-footprint consumers that only see events. To receive events with this, set the region’s `subscription-attributes` `interest-policy`to all.normalData used locally (accessed with `get`, stored with `put`, etc.) is stored in the local cache. This policy allows the contents in the cache to differ from other caches.partitionData is partitioned across local and remote caches using the automatic data distribution behavior of partitioned regions. Additional configuration is done in the `partition-attributes`.replicateThe region is initialized with the data from other caches. After initialization, all events for the distributed region are automatically copied into the local region, maintaining a replica of the entire distributed region in the local cache. Operations that would cause the contents to differ with other caches are not allowed. This is compatible with local `scope`, behaving the same as for normal.persistent-partitionBehaves the same as `partition` and also persists data to disk.persistent-replicateBehaves the same as `replicate` and also persists data to disk.preloadedInitializes like a replicated region, then, once initialized, behaves like a normal region.**API:** `setDataPolicy`**Example**:`<region-attributes    data-policy="replicate">  </region-attributes>`This is similar to using a region shortcut with`refid`, however when you use the REPLICATE region shortcut, it automatically sets the region’s scope to `distributed-ack`.`<region-attributes    refid="REPLICATE">  </region-attributes>`If you use `data-policy`, you must set the scope explicitly. | normal              |
| enable-async-conflation        | For TCP/IP distributions between peers, specifies whether to allow aggregation of asynchronous messages sent by the producer member for the region. This is a special-purpose voolean attribute that applies only when asynchronous queues are used for slow consumers. A false value disables conflation so that all asynchronous messages are sent individually. This special-purpose attribute gives you extra control over peer-to-peer communication between distributed regions using TCP/IP. This attribute does not apply to client/server communication or to communication using the UDP unicast or IP multicast protocols.**Note:** To use this attribute, the `multicast-enabled` region attribute `disable-tcp` in `gemfire.properties` must be false (the default for both). In addition, asynchronous queues must be enabled for slow consumers, specified with the `async`* gemfire properties.**API:** `setEnableAsyncConflation`**Example:**`<region-attributes    enable-async-conflation="false"> </region-attributes>` | true                |
| enable-subscription-conflation | Boolean for server regions that specifies whether the server can conflate its messages to the client. A true value enables conflation.**Note:**The client can override this setting with the `conflate-events` property in its `gemfire.properties`.**API:** `setEnableSubscriptionConflation`**Example:**`<region-attributes   enable-subscription-conflation="true">  </region-attributes>` | false               |
| gateway-sender-ids             | Specifies one or more gateway sender IDs to use for distributing region events to remote Geode sites. Specify multiple IDs as a comma-separated list.**API:** `addGatewaySenderId`**Example:**`<region-attributes    gateway-sender-ids="nwsender,swsender"> </region-attributes>` | not set             |
| async-event-queue-ids          | Specifies one or more asynchronous event queues to use for distributing region events an `AsyncEventListener`implementation (for example, for write-behind cache event handling). Specify multiple IDs as a comma-separated list.**API**: `addAsyncEventQueueId`**Example**:`<region-attributes    async-event-queue-ids="customerqueue,ordersqueue"> </region-attributes>` | not set             |
| hub-id                         | If the `enable-gateway` attribute is set to true, a comma-separated list of gateway hub IDs that receive events from the region.Used only with GemFire version 6.x gateway configurations. For GemFire 7.0 configuration, see the `gateway-sender-id`attribute of the `<region-attributes>` element. | null                |
| id                             | Stores the region attribute settings in the cache with this identifier. Once stored, the attributes can be retrieved using the region attribute `refid`.**API:** `setId`**Example:**`<region-attributes    id="persistent-replicated"> </region-attributes>` | not set             |
| ignore-jta                     | Boolean that determines whether operations on this region participate in active JTA transactions or ignore them and operate outside of the transactions. This is primarily used in cache loaders, writers, and listeners that need to perform non-transactional operations on a region, such as caching a result set.**API:** `setIgnoreJTA`**Example:**`<region-attributes    ignore-jta="true"> </region-attributes>` | false               |
| index-update-type              | Specifies whether region indexes are maintained synchronously with region modifications, or asynchronously in a background thread. In the `cache.xml`file, this is set as a value, asynchronous or synchronous, assigned to the `index-update-type` region attribute. Set this through the API by passing a boolean to the `setIndexMaintenanceSynchronous` method.**API:** `setIndexMaintenanceSynchronous`**Example:**`<region-attributes    index-update-type="asynchronous"> </region-attributes>` | synchronous updates |
| initial-capacity               | Together with the `load-factor` region attribute, sets the initial parameters on the underlying `java.util.ConcurrentHashMap` used for storing region entries.**API:** `setInitialCapacity`**Example:**`<region-attributes    initial-capacity="20"> </region-attributes>` | 16                  |
| is-lock-grantor                | Determines whether this member defines itself as the lock grantor for the region at region creation time. This only specifies whether the member becomes lock grantor at creation and does not reflect the current state of the member’s lock grantor status. The member’s lock grantor status may change if another member subsequently defines the region with `is-lock-grantor` set to true. This attribute is only relevant for regions with `global` `scope`, as only they allow locking. It affects implicit and explicit locking.**API:** `setLockGrantor`**Example:**`<region-attributes    is-lock-grantor="true"> </region-attributes>` | false               |
| load-factor                    | Together with the initial-capacity region attribute, sets the initial parameters on the underlying `java.util.ConcurrentHashMap` used for storing region entries. This must be a floating point number between 0 and 1, inclusive.**Note:**Before you set this attribute, read the discussion of initial capacity and load factor, then see the Java API documentation for `java.util.ConcurrentHashMap`.**API:** `setLoadFactor`**Example:**`<region-attributes    load-factor="0.85"> </region-attributes>` | .75                 |
| mirror-type                    | Deprecated                                                   |                     |
| multicast-enabled              | Boolean that specifies whether distributed operations on a region should use multicasting. To enable this, multicast must be enabled for the cluster with the `mcast-port` `gemfire.properties` setting.**API:** `setMulticastEnabled`**Example:**`<region-attributes    multicast-enabled="true"> </region-attributes>` | false               |
| pool-name                      | Identifies the region as a client region and specifies the server pool the region is to use. The named pool must be defined in the client cache before the region is created. If this is not set, the region does not connect to the servers as a client region.**API:** `setPoolName`**Examples:**This declaration creates the region as a client region with a server pool named DatabasePool. This pool-name specification is required, as there are multiple pools in the client cache:`<client-cache>     <pool name="DatabasePool"     subscription-enabled="true">         ...     </pool>     <pool >    name="OtherPool"     subscription-enabled="true">         ...     </pool>     <region ...         <region-attributes            pool-name="DatabasePool">          </region-attributes>         ...`This declaration creates the region as a client region assigned the single pool that is defined for the client cache. Here the pool-name specification is implied to be the only pool that exists in the cache:`<client-cache>     <pool     name="publisher"     subscription-enabled="true">         ...     </pool>     <region     name="myRegion"     refid="CACHING_PROXY">     </region> </client-cache>` | not set             |
| disk-store-name                | Assigns the region to the disk store with this name from the disk stores defined for the cache. Persist region data to disk by defining the region as persistent using the Shortcut Attribute Options or data-policy settings. Overflow data to disk by implementing LRU eviction-attributes with an action of overflow to disk. Each disk store defines the file system directories to use, how data is written to disk, and other disk storage maintenance properties. In addition, the `disk-synchronous` region attribute specifies whether writes are done synchronously or asynchronously.**API:** `setDiskStoreName`**Example:**`<region-attributes    disk-store-name="myStoreA" > </region-attributes>` | null                |
| disk-synchronous               | For regions that write to disk, boolean that specifies whether disk writes are done synchronously for the region.**API:** `setDiskSynchronous`**Example:**`<region-attributes    disk-store-name="myStoreA"    disk-synchronous="true"> </region-attributes>` | true                |
| refid                          | Retrieves region shortcuts and user-defined named region attributes for attributes initialization**API:** `setRefId`**Example:**`<region-attributes    refid="persistent-replicated">   <!-- Override any stored      attribute settings that you      need to ... --> </region-attributes>` | not set             |
| scope                          | Definition: Determines how updates to region entries are distributed to the other caches in the cluster where the region and entry are defined. Scope also determines whether to allow remote invocation of some of the region’s event handlers, and whether to use region entry versions to provide consistent updates across replicated regions.**Note:**You can configure the most common of these options with Geode region shortcuts in `RegionShortcut` and `ClientRegionShortcut`.**Note:**Server regions that are not partitioned must be replicated with `distributed-ack` or `global` scope. The region shortcuts that specify `REPLICATE` have `distributed-ack`scope.Set one of the following scope values:localNo distribution. The region is visible only to threads running inside the member.distributed-no-ackEvents are distributed to remote caches with no acknowledgement required.distributed-ackEvents are distributed to remote caches with receipt acknowledgement required. Region entry versions are used to provide consistent updates across members of the cluster.globalEvents are distributed to remote caches with global locking to ensure distributed cache consistency.**API:** `setScope`**Example:**`<region-attributes    scope="distributed-ack"> </region-attributes>` | distributed-no-ack  |
| statistics-enabled             | Boolean specifying whether to gather statistics on the region. Must be true to use expiration on the region. Geode provides a standard set of statistics for cached regions and region entries, which give you information for fine-tuning your cluster. Unlike other Geode statistics, statistics for local and distributed regions are not archived and cannot be charted. They are kept in instances of `org.apache.geode.cache.CacheStatistics` and made available through the region and its entries through the `Region.getStatistics` and `Region.Entry.getStatistics`methods.**API:** `setStatisticsEnabled`**Example:**`<region-attributes    statistics-enabled="true"> </region-attributes>` | false               |
| cloning-enabled                | Determines how `fromDelta` applies deltas to the local cache for delta propagation. When true, the updates are applied to a clone of the value and then the clone is saved to the cache. When false, the value is modified in place in the cache.**API:** `setCloningEnabled`**Example:**`<region-attributes    cloning-enabled="true"> </region-attributes>` | false               |
| concurrency-checks-enabled     | Determines whether members perform checks to provide consistent handling for concurrent or out-of-order updates to distributed regions. See [Consistency for Region Updates](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).**Note:** Applications that use a `client-cache` may want to disable concurrency checking in order to see all events for a region. Geode server members can continue using concurrency checks for the region, but they will pass all events to the client cache. This configuration ensures that the client sees all events, but it does not prevent the client cache from becoming out-of-sync with the server cache.**API:** `setConcurrencyChecksEnabled`**Example:**`<region-attributes   concurrency-checks-enabled="true"> </region-attributes>` | true                |
| off-heap                       | Specifies that the region uses off-heap memory to store entry values, including values for region entries and queue entries. The region will still use heap memory for everything else, such as entry keys and the ConcurrentHashMap.**API:** `setOffHeap`**Example:**`<region-attributes    off-heap="true"> </region-attributes>` | false               |



## <key-constraint>

Defines the type of object to be allowed for the region entry keys. This must be a fully-qualified class name. The attribute ensures that the keys for the region entries are all of the same class. If key-constraint is not used, the region’s keys can be of any class. This attribute, along with value-constraint, is useful for querying and indexing because it provides object type information to the query engine.

**Note:** Set the constraint in every cache where you create or update the region entries. For client/server installations, match constraints between client and server and between clusters. The constraint is only checked in the cache that does the entry `put` or `create` operation. To avoid deserializing the object, the constraint is not checked when the entry is distributed to other caches.

**Default:** not set

**API:** `org.apache.geode.cache.RegionFactory.setKeyConstraint`

**Example:**

```
<region-attributes>
  <key-constraint>
   java.lang.String
  </key-constraint>
</region-attributes>
```

## <value-constraint>

Defines the type of object to be allowed for the region entry values. This must be a fully-qualified class name. If value constraint isn’t used, the region’s value can be of any class. This attribute, along with `key-constraint`, is useful for querying and indexing because it provides object type information to the query engine.

**Note:** Set the constraint in every cache where you create or update the region entries. For client/server installations, match constraints between client and server and between clusters. The constraint is only checked in the cache that does the entry `put` or `create` operation. To avoid deserializing the object, the constraint is not checked when the entry is distributed to other caches.

**Default:** not set

**API:** `org.apache.geode.cache.RegionFactory.setValueConstraint`

**Example:**

```
<region-attributes>
  <value-constraint>
   cacheRunner.Portfolio
  </value-constraint>
</region-attributes>
```

## <region-time-to-live>

Expiration setting that specifies how long the region can remain in the cache without anyone accessing or updating it.

**Default:** not set - no expiration of this type

**API:** `org.apache.geode.cache.RegionFactory.setRegionTimeToLive`

**Example:**

```
<region-attributes 
  statistics-enabled="true">
  <region-time-to-live>
    <expiration-attributes 
      timeout="3600" 
      action="local-destroy"/>
  </region-time-to-live>
</region-attributes>
```

## <expiration-attributes>

Within the `entry-time-to-live` or `entry-idle-time` element, this element specifies the expiration rules for removing old region entries that you are not using. You can destroy or invalidate entries, either locally or across the cluster. Within the `region-time-to-live` or `region-idle-time` element, this element specifies the expiration rules for the entire region.

**API:** See APIs for `entry-time-to-live`, `entry-idle-time`, `region-time-to-live`, `region-idle-time`

**<expiration-attributes> Attributes**

| Attribute | Description                                                  | Default    |
| :-------- | :----------------------------------------------------------- | :--------- |
| timeout   | Number of seconds before a region or an entry expires. If timeout is not specified, it defaults to zero (which means no expiration). | 0          |
| action    | Action that should take place when a region or an entry expires.Select one of the following expiration actions:local-destroyRemoves the region or entry from the local cache, but does not distribute the removal operation to remote members. You cannot use this action on partitioned region entries.destroyRemoves the region or entry completely from the cache. Destroy actions are distributed according to the region’s distribution settings. Use this option when the region or entry is no longer needed for any application in the cluster.invalidateDefault expiration action. Marks an entry or all entries in the region as invalid. Distributes the invalidation according to the region’s scope. This is the proper choice when the region or the entry is no longer valid for any application in the cluster.local-invalidateMarks an entry or all entries in the region as invalid but does not distribute the operation. You cannot use this action on partitioned region entries. Local region invalidation is only supported for regions that are not configured as replicated regions. | invalidate |



**Example:**

```
<region-attributes 
  statistics-enabled="true">
   <region-time-to-live>
      <expiration-attributes 
        timeout="60" 
        action="local-destroy"/>
   </region-time-to-live>
</region-attributes>
```

## <custom-expiry>

Specifies the custom class that implements `org.apache.geode.cache.CustomExpiry`. You define this class in order to override the region-wide settings for specific entries. See [Configure Data Expiration](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html) for an example.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `org.apache.geode.cache.RegionFactory.setCustomEntryIdleTimeout`, `setCustomeEntryTimeToLive`

**Example:**

```
<region-attributes>
    <expiration-attributes 
       timeout="60" 
       action="local-destroy">
      <custom-expiry> 
        <class-name>
          com.megaconglomerate.mypackage.MyClass
        </class-name> 
      </custom-expiry>
</region-attributes>
```

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

## <region-idle-time>

Expiration setting that specifies how long the region can remain in the cache without anyone accessing it.

**Note:** To ensure reliable read behavior across the partitioned region, use `region-time-to-live` for region expiration instead of this setting.

**Default:** not set - no expiration of this type

**API:** `org.apache.geode.cache.RegionFactory.setRegionIdleTimeout`

**Example:**

```
<region-attributes statistics-enabled="true">
  <region-idle-time>
    <expiration-attributes 
      timeout="3600" 
      action="local-destroy"/>
  </region-idle-time>
</region-attributes>
```

## <expiration-attributes>

Within the `entry-time-to-live` or `entry-idle-time` element, this element specifies the expiration rules for removing old region entries that you are not using. You can destroy or invalidate entries, either locally or across the cluster. Within the `region-time-to-live` or `region-idle-time` element, this element specifies the expiration rules for the entire region.

**API:** See APIs for `entry-time-to-live`, `entry-idle-time`, `region-time-to-live`, `region-idle-time`

**<expiration-attributes> Attributes**

| Attribute | Description                                                  | Default    |
| :-------- | :----------------------------------------------------------- | :--------- |
| timeout   | Number of seconds before a region or an entry expires. If timeout is not specified, it defaults to zero (which means no expiration). | 0          |
| action    | Action that should take place when a region or an entry expires.Select one of the following expiration actions:local-destroyRemoves the region or entry from the local cache, but does not distribute the removal operation to remote members. You cannot use this action on partitioned region entries.destroyRemoves the region or entry completely from the cache. Destroy actions are distributed according to the region’s distribution settings. Use this option when the region or entry is no longer needed for any application in the cluster.invalidateDefault expiration action. Marks an entry or all entries in the region as invalid. Distributes the invalidation according to the region’s scope. This is the proper choice when the region or the entry is no longer valid for any application in the cluster.local-invalidateMarks an entry or all entries in the region as invalid but does not distribute the operation. You cannot use this action on partitioned region entries. Local region invalidation is only supported for regions that are not configured as replicated regions. | invalidate |



**Example:**

```
<region-attributes 
  statistics-enabled="true">
   <region-idle-time>
      <expiration-attributes 
        timeout="60" 
        action="local-destroy"/>
   </region-idle-time>
</region-attributes>
```

## <custom-expiry>

Specifies the custom class that implements `org.apache.geode.cache.CustomExpiry`. You define this class in order to override the region-wide settings for specific entries. See [Configure Data Expiration](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html) for an example.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `org.apache.geode.cache.RegionFactory.setCustomEntryIdleTimeout`, `setCustomeEntryTimeToLive`

**Example:**

```
<region-attributes>
    <expiration-attributes 
       timeout="60" 
       action="local-destroy">
      <custom-expiry> 
        <class-name>
          com.megaconglomerate.mypackage.MyClass
        </class-name> 
      </custom-expiry>
</region-attributes>
```

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

## <entry-time-to-live>

Expiration setting that specifies how long the region’s entries can remain in the cache without anyone accessing or updating them. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#expiration-attributes) for details.

**Default:** not set - no expiration of this type.

**API:** `org.apache.geode.cache.RegionFactory.setEntryTimeToLive`

**Example:**

```
<region-attributes
  statistics-enabled="true">
  <entry-time-to-live>
    <expiration-attributes
     timeout="60"
     action="local-destroy"/>
  </entry-time-to-live>
</region-attributes>
```

## <expiration-attributes>

Within the `entry-time-to-live` or `entry-idle-time` element, this element specifies the expiration rules for removing old region entries that you are not using. You can destroy or invalidate entries, either locally or across the cluster. Within the `region-time-to-live` or `region-idle-time` element, this element specifies the expiration rules for the entire region.

**API:** See APIs for `entry-time-to-live`, `entry-idle-time`, `region-time-to-live`, `region-idle-time`

**<expiration-attributes> Attributes**

| Attribute | Description                                                  | Default    |
| :-------- | :----------------------------------------------------------- | :--------- |
| timeout   | Number of seconds before a region or an entry expires. If timeout is not specified, it defaults to zero (which means no expiration). | 0          |
| action    | Action that should take place when a region or an entry expires.Select one of the following expiration actions:local-destroyRemoves the region or entry from the local cache, but does not distribute the removal operation to remote members. You cannot use this action on partitioned region entries.destroyRemoves the region or entry completely from the cache. Destroy actions are distributed according to the region’s distribution settings. Use this option when the region or entry is no longer needed for any application in the cluster.invalidateDefault expiration action. Marks an entry or all entries in the region as invalid. Distributes the invalidation according to the region’s scope. This is the proper choice when the region or the entry is no longer valid for any application in the cluster.local-invalidateMarks an entry or all entries in the region as invalid but does not distribute the operation. You cannot use this action on partitioned region entries. Local region invalidation is only supported for regions that are not configured as replicated regions. | invalidate |



**Example:**

```
<region-attributes 
  statistics-enabled="true">
   <entry-time-to-live>
      <expiration-attributes 
        timeout="60" 
        action="local-destroy"/>
   </entry-time-to-live>
</region-attributes>
```

## <custom-expiry>

Specifies the custom class that implements `org.apache.geode.cache.CustomExpiry`. You define this class in order to override the region-wide settings for specific entries. See [Configure Data Expiration](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html) for an example.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `org.apache.geode.cache.RegionFactory.setCustomEntryIdleTimeout`, `setCustomeEntryTimeToLive`

**Example:**

```
<region-attributes>
    <expiration-attributes 
       timeout="60" 
       action="local-destroy">
      <custom-expiry> 
        <class-name>
          com.megaconglomerate.mypackage.MyClass
        </class-name> 
      </custom-expiry>
</region-attributes>
```

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

## <entry-idle-time>

Expiration setting that specifies how long the region’s entries can remain in the cache without anyone accessing them. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#expiration-attributes) for details.

**Note:** To ensure reliable read behavior across the partitioned region, use `entry-time-to-live` for entry expiration instead of this setting.

**API:** `org.apache.geode.cache.RegionFactory.setEntryIdleTimeout`

**Example:**

```
<region-attributes 
  statistics-enabled="true">
  <entry-idle-time>
    <expiration-attributes 
      timeout="60" 
      action="local-invalidate"/>
    </expiration-attributes>
  </entry-idle-time>
</region-attributes>
```

## <expiration-attributes>

Within the `entry-time-to-live` or `entry-idle-time` element, this element specifies the expiration rules for removing old region entries that you are not using. You can destroy or invalidate entries, either locally or across the cluster. Within the `region-time-to-live` or `region-idle-time` element, this element specifies the expiration rules for the entire region.

**API:** See APIs for `entry-time-to-live`, `entry-idle-time`, `region-time-to-live`, `region-idle-time`

**<expiration-attributes> Attributes**

| Attribute | Description                                                  | Default    |
| :-------- | :----------------------------------------------------------- | :--------- |
| timeout   | Number of seconds before a region or an entry expires. If timeout is not specified, it defaults to zero (which means no expiration). | 0          |
| action    | Action that should take place when a region or an entry expires.Select one of the following expiration actions:local-destroyRemoves the region or entry from the local cache, but does not distribute the removal operation to remote members. You cannot use this action on partitioned region entries.destroyRemoves the region or entry completely from the cache. Destroy actions are distributed according to the region’s distribution settings. Use this option when the region or entry is no longer needed for any application in the cluster.invalidateDefault expiration action. Marks an entry or all entries in the region as invalid. Distributes the invalidation according to the region’s scope. This is the proper choice when the region or the entry is no longer valid for any application in the cluster.local-invalidateMarks an entry or all entries in the region as invalid but does not distribute the operation. You cannot use this action on partitioned region entries. Local region invalidation is only supported for regions that are not configured as replicated regions. | invalidate |



**Example:**

```
<region-attributes 
  statistics-enabled="true">
   <entry-idle-time>
      <expiration-attributes 
        timeout="60" 
        action="local-destroy"/>
   </entry-idle-time>
</region-attributes>
```

## <custom-expiry>

Specifies the custom class that implements `org.apache.geode.cache.CustomExpiry`. You define this class in order to override the region-wide settings for specific entries. See [Configure Data Expiration](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html) for an example.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `org.apache.geode.cache.RegionFactory.setCustomEntryIdleTimeout`, `setCustomeEntryTimeToLive`

**Example:**

```
<region-attributes>
    <expiration-attributes 
       timeout="60" 
       action="local-destroy">
      <custom-expiry> 
        <class-name>
          com.megaconglomerate.mypackage.MyClass
        </class-name> 
      </custom-expiry>
</region-attributes>
```

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

## <partition-attributes>

Defines the region as partitioned and controls partitioning behavior. This is set during the region creation in the first data store for the partitioned region.

**Note:** With the exception of `local-max-memory`, all members defining a partitioned region must use the same partition attribute settings.

**API:** `org.apache.geode.cache.RegionFactory.setPartitionAttributes`

**<partition-attributes> Attributes**

| Attribute              | Description                                                  | Default              |
| :--------------------- | :----------------------------------------------------------- | :------------------- |
| colocated-with         | The full name of a region to colocate with this region. The named region must exist before this region is created. | null                 |
| local-max-memory       | Maximum megabytes of memory set aside for this region in the local member. This is all memory used for this partitioned region - for primary buckets and any redundant copies. This value must be smaller than the Java settings for the initial or maximum JVM heap. When the memory use goes above this value, Geode issues a warning, but operation continues. Besides setting the maximum memory to use for the member, this setting also tells Geode how to balance the load between members where the region is defined. For example, if one member sets this value to twice the value of another member’s setting, Geode works to keep the ratio between the first and the second at two-to-one, regardless of how little memory the region consumes. This is a local parameter that applies only to the local member. A value of 0 disables local data caching. | 90% (of local heap)  |
| recovery-delay         | Applies when `redundant-copies` is greater than zero. The number of milliseconds to wait after a member crashes before reestablishing redundancy for the region. A setting of -1 disables automatic recovery of redundancy after member failure. | -1                   |
| redundant-copies       | Number of extra copies that the partitioned region must maintain for each entry. Range: 0-3. If you specify 1, this partitioned region maintains the original and one backup, for a total of two copies. A value of 0 disables redundancy. | 0                    |
| startup-recovery-delay | Applies when `redundant-copies` is greater than zero. The number of milliseconds a newly started member should wait before trying to satisfy redundancy of region data stored on other members. A setting of -1 disables automatic recovery of redundancy after new members join. | 0                    |
| total-max-memory       | Maximum combined megabytes of memory to be used by all processes hosting this region for all copies, primary and redundant. | ` Integer.MAX_VALUE` |
| total-num-buckets      | Total number of buckets or data storage areas allotted for the entire partitioned region in the distributed cache. As data moves from one member to another, the entries in a bucket move as one unit. This value should be a prime number at least four times the number of data stores. More buckets increases overhead, however, especially when redundant-copies = 2 or 3 . | 113                  |

**Example:**

```
<region-attributes>
  <partition-attributes 
   redundant-copies="1" 
   total-num-buckets= "613"/>
</region-attributes>
```

## <partition-resolver>

Describes a custom PartitionResolver for a region.

**API:** `org.apache.geode.cache.PartitionAttributesFactory.setPartitionResolver`

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**<partition-resolver> Attributes**

| Attribute | Description                                | Default |
| :-------- | :----------------------------------------- | :------ |
| name      | The name of this custom PartitionResolver. |         |

**Example:**

```
<region name="trades">
  <region-attributes>
      <partition-attributes>
          <partition-resolver name="TradesPartitionResolver"> 
              <class-name>myPackage.TradesPartitionResolver
              </class-name>
          </partition-resolver>
      </partition-attributes>
  </region-attributes>
</region>
```

## <partition-listener>

Defines a custom PartitionListener for a partitioned region.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**API:** `org.apache.geode.cache.PartitionAttributesFactory.PartitionListener`

**Example:**

```
<partition-attributes redundant-copies="1">
     <partition-listener>
         <class-name>com.myCompany.ColocatingPartitionListener</class-name.
          <parameter name="viewRegions">
              <string>/customer/ViewA,/customer/ViewB</string>
          </parameter>             
     </partition-listener>
 </partition-attributes>
```

## <fixed-partition-attributes>

Describes a partition in a Fixed Partitioned Region.

**API:** `org.apache.geode.cache.PartitionAttributesFactory.addFixedPartitionAttributes`

**<fixed-partition-attributes> Attributes**

| Attribute      | Description                                             | Default |
| :------------- | :------------------------------------------------------ | :------ |
| partition-name | The name of this fixed partition.                       |         |
| is-primary     | Set to true if this partition is the primary partition. | false   |
| num-buckets    | The number of buckets assigned to this partition.       |         |

**Example:**

```
<cache>
   <region name="Trades">
      <region-attributes>
         <partition-attributes 
          redundant-copies="1"> 
          <partition-resolver name="QuarterFixedPartitionResolver">
             <fixed-partition-attributes 
               partition-name="Q1" 
               is-primary="true"/>
             <fixed-partition-attributes 
               partition-name="Q3" 
               is-primary="false" 
               num-buckets="6"/>
         </partition-attributes> 
      </region-attributes>
   </region>
</cache>
```

## <membership-attributes>

Establishes reliability requirements and behavior for a region. Use this to configure the region to require one or more membership roles to be running in the system for reliable access to the region. You can set up your own roles, such as producer or backup, specifying each role as a string. Membership attributes have no effect unless one or more required roles are specified.

**API:** `org.apache.geode.cache.RegionFactory.setMembershipAttributes`

**<membership-attributes> Attributes**

| Attribute         | Description                                                  | Default      |
| :---------------- | :----------------------------------------------------------- | :----------- |
| loss-action       | Set one of the following values to specify how access to the region is affected when one or more required roles are lost.full-accessAccess to the region is unaffected when required roles are missing.limited-accessOnly local access to the region is allowed when required roles are missing.no-accessThe region is unavailable when required roles are missing.reconnectLoss of required roles causes the entire cache to be closed. | no_access    |
| resumption-action | Specifies how the region is affected by resumption of reliability when one or more missing required roles return to the distributed membership. Set one of the following values:noneNo special action takes place when reliability resumes.reinitializeResumption of reliability causes the region to be cleared of all data and replicated regions will do a new getInitialImage operation to repopulate the region. | reinitialize |



**Example:**

```
<!-- If there is no "producer" member 
  running, do not allow access to the region -->
<region-attributes>
  <membership-attributes 
    loss-action="no-access" 
    resumption-action="none">
    <required-role 
      name="producer">
    </required-role>
  </membership-attributes>
</region-attributes>
```

## <required-role>

Specifies a role that is required for reliable access to the region.

**API:** `org.apache.geode.cache.MembershipAttributes`

**<required-role> Attributes**

| Attribute | Description                    | Default |
| :-------- | :----------------------------- | :------ |
| name      | The name of the required role. |         |

**Example:**

```
<membership-attributes 
  loss-action="no-access" 
  resumption-action="none">
    <required-role name="producer"/>
</membership-attributes>
```

## <subscription-attributes>

Specifies subscriber requirements and behavior for the region. There is one subscription attribute, `interest-policy`, that defines which distributed entry events are delivered to the local region.

**Note:** The interest policy determines which events are delivered, but the `data-policy` determines how the events are applied to the cache.

**API:** `org.apache.geode.cache.RegionFactory.setSubscriptionAttributes`

**<subscription-attributes> Attributes**

| Attribute       | Description                                                  | Default       |
| :-------------- | :----------------------------------------------------------- | :------------ |
| interest-policy | The two `interest-policy` options are:cache-contentThe default, registers interest in events only for entries that are already in the local region. For partitioned regions, the local member must hold the primary copy of the entry’s data.allRegisters interest in events for all entries that are anywhere in the distributed or partitioned region, regardless of whether they are already present in the local cache. | cache-content |



**Example:**

```
<region-attributes>
  <subscription-attributes 
   interest-policy="all"/>
</region-attributes>
```

## <cache-loader>

An event-handler plug-in that allows you to program for cache misses. At most, one cache loader can be defined in each member for the region. For distributed regions, a cache loader may be invoked remotely from other members that have the region defined. When an entry get results in a cache miss in a region with a cache loader defined, the loader’s load method is called. This method is usually programmed to retrieve data from an outside data source, but it can do anything required by your application.

For partitioned regions, if you want to have a cache loader, install an instance of the cache loader in every data store. Partitioned regions support partitioned loading, where each cache loader loads only the data entries in the local member. If data redundancy is configured, data is loaded only if the local member holds the primary copy.

**API:** `org.apache.geode.cache.RegionFactory.setCacheLoader`

**Example:**

```
<region-attributes>
  <cache-loader>
    <class-name>quickstart.SimpleCacheLoader</class-name>
  </cache-loader>
</region-attributes>
```

## <cache-writer>

An event-handler plug-in that allows you to receive before-event notification for changes to the region and its entries. It also has the ability to abort events. At most, one cache writer can be defined in each member for the region. A cache writer may be invoked remotely from other members that have the region defined.

**API:** `org.apache.geode.cache.RegionFactory.setCacheWriter`

**Example:**

```
<region-attributes>
  <cache-writer>
    <class-name>quickstart.SimpleCacheWriter</class-name>
  </cache-writer>
</region-attributes>
```

## <cache-listener>

An event-handler plug-in that receives after-event notification of changes to the region and its entries. Any number of cache listeners can be defined for a region in any member. Geode offers several listener types with callbacks to handle data and process events. Depending on the `data-policy` and the `interest-policy` subscription attributes, a cache listener may receive only events that originate in the local cache, or it may receive those events along with events that originate remotely.

Specify the Java class for the cache listener and its initialization parameters with the `<class-name>`and `<parameter>` sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**API:** `org.apache.geode.cache.RegionFactory.addCacheListener`

**Example:**

```
<region-attributes>
  <cache-listener>
    <class-name>
      quickstart.SimpleCacheListener
    </class-name>
  </cache-listener>
</region-attributes>
```

## <compressor>

A compressor registers a custom class that extends `Compressor` to support compression on a region.

**Example:**

```
 ...
<compressor>
<class-name>
            <parameter>
               <string>
               <declarable>
</compressor>
...
```

## <eviction-attributes>

Specifies whether and how to control a region’s size. Size is controlled by removing least recently used (LRU) entries to make space for new ones. This may be done through destroy or overflow actions. You can configure your region for lru-heap-percentage with an eviction action of local-destroy using stored region attributes.

**Default:** Uses the lru-entry-count algorithm.

**API:** `org.apache.geode.cache.RegionFactory.setEvictionAttributes`

**Example:**

```
<region-attributes>
  <eviction-attributes> 
    <lru-entry-count 
      maximum="1000" 
      action="overflow-to-disk"/>
  </eviction-attributes>
</region-attributes
```

## <lru-entry-count>

Using the maximum attribute, specifies maximum region capacity based on entry count.

**<lru-entry-count> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |
| maximum   | The maximum number of entries allowed in a region.           |               |



## <lru-heap-percentage>

Runs evictions when the Geode resource manager says to. The manager orders evictions when the total cache size is over the heap or off-heap percentage limit specified in the resource manager configuration. You can declare a Java class that implements the ObjectSizer interface to measure the size of objects in the Region.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**<lru-heap-percentage> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |



## <lru-memory-size>

Using the maximum attribute, specifies maximum region capacity based on the amount of memory used, in megabytes. You can declare a Java class that implements the ObjectSizer interface to measure the size of objects in the Region.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**<lru-memory-size> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |
| maximum   | The maximum amount of memory used in the region, in megabytes. |               |



## <jndi-bindings>

Specifies the binding for a data-source used in transaction management. See [Configuring Database Connections Using JNDI](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html).

**Example:**

```
<jndi-bindings>
      <jndi-binding type="XAPooledDataSource" 
    jndi-name="newDB2trans" 
    init-pool-size="20" 
    max-pool-size="100"
    idle-timeout-seconds="20"
    blocking-timeout-seconds="5" 
    login-timeout-seconds="10"
    xa-datasource-class="org.apache.derby.jdbc.EmbeddedXADataSource"
    user-name="mitul" 
    password="encrypted(83f0069202c571faf1ae6c42b4ad46030e4e31c17409e19a)">
         <config-property>
          <config-property-name>Description</config-property-name>
          <config-property-type>java.lang.String</config-property-type>
          <config-property-value>pooled_transact</config-property-value>
       </config-property>
          <config-property>
             <config-property-name>DatabaseName</config-property-name>
             <config-property-type>java.lang.String</config-property-type>
             <config-property-value>newDB</config-property-value>
          </config-property>
          <config-property>
             <config-property-name>CreateDatabase</config-property-name>
             <config-property-type>java.lang.String</config-property-type>
             <config-property-value>create</config-property-value>
          </config-property>         
       . . .
      </jndi-binding>
   </jndi-bindings>
```

## <jndi-binding>

For every datasource that is bound to the JNDI tree, there should be one `<jndi-binding>` element. This element describes the property and the configuration of the datasource. Geode uses the attributes of the `<jndi-binding>` element for configuration. Use the `<config-property>` element to configure properties for the datasource.

We recommend that you set the username and password with the `user-name` and `password` jndi-binding attributes rather than using the `<config-property>` element.

**<jndi-binding> Attributes**

| Attribute                    | Description                                                  | Default |
| :--------------------------- | :----------------------------------------------------------- | :------ |
| blocking-timeout-seconds     | The number of seconds that a connection remains associated with a transaction. If this value is exceeded, the connection is disassociated from the transaction. | 120     |
| conn-pooled-datasource-class | Java class used for the `PooledDataSource` type.             |         |
| connection-url               | URL for connecting to the datasource.**Note:**If you are connecting to a JCA data source driver that implements XA transactions (where the jndi-binding type is **XAPooledDataSource**), do not use this attribute. Instead, define configuration properties for your database. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#config-property) for an example. |         |
| idle-timeout-seconds         | The maximum number of seconds that a connection can remain idle in a pool. When this threshold is reached, the connection is removed. | 600     |
| init-pool-size               | The initial pool size of a PooledConnection (an XAConnection or a non-XAConnection). | 10      |
| jdbc-driver-class            | Java class used for the `SimpleDataSource` type.             |         |
| jndi-name                    | The `jndi-name` attribute is the key binding parameter. If the value of jndi-name is a DataSource, it is bound as java:/myDatabase, where myDatabase is the name you assign to your data source. If the data source cannot be bound to JNDI at runtime, Geode logs a warning. |         |
| login-timeout-seconds        | The maximum number of seconds for which a thread seeking a connection from a connection pool may be blocked. If the thread is unable to obtain connection in the stipulated time, a PoolException is thrownIf a connection is available the thread returns immediately.If an existing connection is not available and the maximum number of connections in the pool has not been reached, a new connection is created and the thread returns immediately with the connection.If a connection is not available, the thread blocks for the specified time while waiting for an available connection. | 30      |
| managed-conn-factory-class   | If the Resource Adapter is of type ManagedDataSource, this class becomes the source of the PooledConnection. (This class interface complies with the J2CA Java 2 Connector Architecture.) |         |
| max-pool-size                | The maximum size of the PooledConnection.                    | 30      |
| password                     | Password to access the datasource.                           |         |
| transaction-type             | When the `type` attribute is set to `ManagedDataSource`, specifies the type of transaction. Set one of the following `transaction-type`s:XATransactionSelect this option when you want to use aManagedConnection interface with a Java Transaction Manager to define transaction boundries. This option allows a ManagedDataSource to participate in a transaction with a Geode cache.NoTransactionNo transactional behavior is used.LocalTransactionSelect this option when using a ManagedDataSource that is not managed by the Java Transaction manager. | none    |
| type                         | Set one of the following types:XAPooledDataSourcePooled SQL connections. For this type, you must also set the `xa-datasource-class`attribute.ManagedDataSourceJNDI binding type for the J2EE Connector Architecture (JCA). ManagedConnectionFactory. For information on the ManagedConnection interface, See the [Oracle ManagedConnection docs](http://docs.oracle.com/javaee/6/api/javax/resource/spi/ManagedConnection.html).PooledDataSourcePooled SQL connections. For this type, you must also set the `conn-pooled-datasource-class`attribute.SimpleDataSourceSingle SQL connection. No pooling of SQL connections is done. Connections are generated on the fly and cannot be reused. For this type, you must also set the `jdbc-driver-class` attribute. | none    |
| user-name                    | User name to access to the datasource.                       |         |
| xa-datasource-class          | Java class used for the `XAPooledDataSource` type.           |         |



## <config-property>

A configuration property of the datasource. Use the sub-elements to identify the name, datatype, and value of the property.

**Default:**

**Example:**

```
<config-property>
     <config-property-name>DatabaseName</config-property-name>
     <config-property-type>java.lang.String</config-property-type>
     <config-property-value>newDB</config-property-value>
</config-property>
```

Configuration properties vary depending on the database vendor. See [Configuring Database Connections Using JNDI](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html) for examples of different configuration property configurations.

## <config-property-name>

The name of this datasource property.

## <config-property-type>

The data type of this datasource property.

## <config-property-value>

The value of this datasource property.

## <region>

Defines a region in the cache. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes) for more details on configuring regions. You can specify zero or more subregions within a region. See [Create and Access Data Subregions](https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html#data_regions__section_jn1_sry_5m) for restrictions on creating subregions. For example, you cannot create a partitioned subregion.

**Default:**

**API:** `org.apache.geode.cache.RegionFactory` or `org.apache.geode.cache.ClientRegionFactory`

**<region> Attributes**

| Attribute | Description                                                  | Default |
| :-------- | :----------------------------------------------------------- | :------ |
| name      | Specify the name for the region. See [Region Management](https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html) for details. |         |
| refid     | Used to apply predefined attributes to the region being defined. If the nested “region-attributes” element has its own “refid”, then it will cause the “refid” on the region to be ignored. The “refid” region attriibute can be set to the name of a RegionShortcut or a ClientRegionShortcut. For more information, see [Region Shortcuts and Custom Named Region Attributes](https://geode.apache.org/docs/guide/17/basic_config/data_regions/region_shortcuts.html) and [Storing and Retrieving Region Shortcuts and Custom Named Region Attributes](https://geode.apache.org/docs/guide/17/basic_config/data_regions/store_retrieve_region_shortcuts.html). |         |

**Example:**

```
<!--Using region shortcut-->
<region 
  name="PartitionedRegion" 
  refid="PARTITION_REDUNDANT">
...
</region>

<!-- Retrieving and storing attributes -->
<region-attributes 
  id="myPartition" 
  refid="PARTITION_REDUNDANT">
    <partition-attributes 
   local-max-memory="512"/>
</region-attributes>

<!-- Attributes are retrieved and applied in the first region -->
<region name="PartitionedRegion1" refid="myPartition"/>
```

See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes) for a complete listing of region attributes.

## <index>

Describes an index to be created on a region. The index node, if any, should all come immediately after the “region-attributes” node. The “name” attribute is a required field which identifies the name of the index. See [Working with Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/query_index.html) for more information on indexes.

**Default:**

**API:** `org.apache.geode.cache.query.QueryService.createIndex, createKeyIndex, createHashIndex`

**<index> Attributes**

| Attribute   | Description                                                  | Default |
| :---------- | :----------------------------------------------------------- | :------ |
| name        | Required. Name of the index.                                 |         |
| from-clause | Specifies the collection(s) of objects that the index ranges over. The from-clause must only contain one and only one region path. |         |
| expression  | Specifies the lookup value of the index.                     |         |
| imports     | String containing the imports used to create the index. String should be specified in the query language syntax with each import statement separated by a semicolon. The imports statement provides packages and classes used in variable typing in the indexed and FROM expressions. |         |
| key-index   | True or false. Whether the index should be a key index. If true, the region key specified in the indexed expression is used to evaluate queries |         |
| type        | Possible values are “hash” or “range”.                       | range   |

**Example:**

```
<region name=exampleRegion>
 <region-attributes . . . >
 </region-attributes>
 <index 
  name="myIndex" 
  from-clause="/exampleRegion" 
  expression="status"/>
 <index 
  name="myKeyIndex" 
  from-clause="/exampleRegion" 
  expression="id" key-index="true"/>
 <index 
  name="myHashIndex" 
  from-clause="/exampleRegion p" 
  expression="p.mktValue" type="hash"/>
 ...
</region>
```

## <lucene:index>

Describes a Lucene index to be created on a region. The `lucene` namespace and the scoping operator (`:`) must be specified, as the Geode `cache` namespace also defines an `index` element (for OQL indexes).

**API:** `org.apache.geode.cache.lucene` package

**<lucene:index> Attributes** | Attribute | Description | Default | |————-|—————————————————————————————————————————————————————————————————————————————————————————————|———| | name | Required. Name of the Lucene index. |   |

**Example:**

```
<cache
    xmlns="http://geode.apache.org/schema/cache"
    xmlns:lucene="http://geode.apache.org/schema/lucene"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache
        http://geode.apache.org/schema/cache/cache-1.0.xsd
        http://geode.apache.org/schema/lucene
        http://geode.apache.org/schema/lucene/lucene-1.0.xsd"
    version="1.0">

    <region name="regionA" refid="PARTITION">
        <lucene:index name="myIndex">
            <lucene:field name="x" />
            <lucene:field name="y" />
        </lucene:index>
    </region>
</cache>
```

## <lucene:field>

Describes a field to be included in a Lucene index. Including the `lucene` namespace and the scoping operator (`:`) clarifies, but is not required.

**API:** `org.apache.geode.cache.lucene` package

**<lucene:field&gt Attributes** | Attribute | Description | Default | |————-|————-|———| | name | Required. A string that defines the name of the field. If a single field is defined by the value `"__REGION_VALUE_FIELD"`, then the entire value is used as a single field. |   | | analyzer | A string that provides the path to the analyzer to use for this field. A value of `"null"` uses the default analyzer. | `"null"` |

**Example:**

```
<region name="dataregion" refid="PARTITION_REDUNDANT">
   <lucene:index name="full_value_index">
     <lucene:field name="__REGION_VALUE_FIELD"/>
   </lucene:index>
</region>
```

## <entry>

An “entry” element describes an entry to be added to a region. Note that if an entry with the given key already exists in the region, it will be replaced.

**Default:**

**API:** `org.apache.geode.cache.Region.create`, `put`, `get`, `putAll`, `getAll`

**Example:**

```
<region ...>
 <region-attributes ...>
   ...
 </region-attributes>
 <entry>
   <key><string>MyKey</string></key>
   <value><string>MyValue</string></value>
 </entry>
</region>
```

## <key>

Required. Describes the key in a region entry. A key can contain either a <string> or a <declarable> sub-element.

## <string>

Specifies a String to be placed in a Region entry.

**Example:**

```
<region ...>
 <region-attributes ...>
   ...
 </region-attributes>
 <entry>
   <key><string>MyKey</string></key>
   <value><string>MyValue</string></value>
 </entry>
</region>
```

## <declarable>

Specifies a Declarable object to be placed in a Region entry.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `Declarable`

**Example:**

```
<parameter name="cacheserver">
  <declarable>
    <class-name>org.apache.geode.addon.cache.CacheServerInitializer</class-name>
    <parameter name="system.property.prefix">
    <string>cacheserver</string>
    </parameter>
  </declarable>
</parameter>
```

## <value>

Required. Describes the value of a region entry. A `<value>` can contain either a `<string>` or a `<declarable>` sub-element.

## <string>

Specifies a String to be placed in a Region entry.

**Example:**

```
<region ...>
 <region-attributes ...>
   ...
 </region-attributes>
 <entry>
   <key><string>MyKey</string></key>
   <value><string>MyValue</string></value>
 </entry>
</region>
```

## <declarable>

Specifies a Declarable object to be placed in a Region entry.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `Declarable`

**Example:**

```
<parameter name="cacheserver">
  <declarable>
    <class-name>org.apache.geode.addon.cache.CacheServerInitializer</class-name>
    <parameter name="system.property.prefix">
    <string>cacheserver</string>
    </parameter>
  </declarable>
</parameter>
```

## <region>

When nested within a `<region>` element, defines a subregion. See [Create and Access Data Subregions](https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html#data_regions__section_jn1_sry_5m) for restrictions on creating subregions. For example, you cannot create a partitioned subregion.

See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region)

## <function-service>

Configures the behavior of the function execution service.

**Example:**

```
<cache>
    ...
    </region>
<function-service>
  <function>
    <class-name>com.myCompany.tradeService.cache.func.TradeCalc</class-name>
  </function>
</function-service>
```

## <function>

Defines a function for registration in the function service

Specify the Java class for the function and its initialization parameters with the `<class-name>` and `<parameter>` sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Default:**

**API:** `org.apache.geode.cache.execute.FunctionService`

**Example:**

```
<function>
  <class-name>
    com.myCompany.tradeService.cache.func.TradeCalc
  </class-name>
</function>
```

## <resource-manager>

A memory monitor that tracks cache size as a percentage of total heap or off-heap memory and controls size by restricting access to the cache and prompting eviction of old entries from the cache. For tenured heap, used in conjunction with settings for JVM memory and Java garbage collection. For off-heap memory, used with the off-heap memory manager.

**API:** `org.apache.geode.cache.control.ResourceManager`

**<resource-manager> Attributes**

| Attribute                    | Description                                                  | Default |
| :--------------------------- | :----------------------------------------------------------- | :------ |
| critical-heap-percentage     | Percentage of heap at or above which the cache is considered in danger of becoming inoperable due to garbage collection pauses or out of memory exceptions.Only one change to this attribute or critical heap percentage will be allowed at any given time and its effect will be fully realized before the next change is allowed. This feature requires additional VM flags to perform properly. See `setCriticalHeapPercentage()` for details. | 0       |
| eviction-heap-percentage     | Set the percentage of heap at or above which the eviction should begin on regions configured for HeapLRU eviction.Changing this value may cause eviction to begin immediately.The default is 0, if no region is configured with heap evictionIf `critical-heap-percentage` is set to a non-zero value, 5% less than that value.80%, if `critical-heap-percentage` is not configured. | 0       |
| critical-off-heap-percentage | Percentage of off-heap memory at or above which the cache is considered in danger of becoming inoperable due to garbage collection pauses or out of memory exceptions. | 0       |
| eviction-off-heap-percentage | Set the percentage of off-heap memory at or above which the eviction should begin on Regions configured for HeapLRU eviction.The default is 0, if no region is configured with heap evictionIf `critical-off-heap-percentage` is set to a non-zero value, 5% less than that value.80%, if `critical-off-heap-percentage` is not configured. | 0       |

**Example:**

```
<cache>
...
   <resource-manager 
      critical-heap-percentage="99.9" 
      eviction-heap=-percentage="85"/>
...
</cache>
```

## <serialization-registration>

Set of serializer or instantiator tags to register customer DataSerializer extensions or DataSerializable implementations respectively.

**Example:**

```
<serialization-registration>
    <instantiator id="30">        
       <class-name>com.package.MyClass</class-name>
    </instantiator>
</serialization-registration> 
```

## <serializer>

Allows you to configure the DataSerializer for this Geode member. It registers a custom class which extends DataSerializer to support custom serialization of non-modifiable object types inside Geode.

Specify the Java class for the `DataSerializer` and its initialization parameters with the `<class-name>` sub-element.

**API:** You can also register a `DataSerializer` by using the `org.apache.geode.DataSerializer.register`API. Use the `org.apache.geode.Instantiator` API to register a `DataSerializable` implementation.

## <instantiator>

An Instantiator registers a custom class which implements the `DataSerializable` interface to support custom object serialization inside Geode.

Specify the Java class and its initialization parameters with the `<class-name>` sub-element.

**API:** `DataSerializable`

You can also directly specify `<instantiator>` as a sub-element of `<cache>`. Use the `org.apache.geode.Instantiator` API to register a `DataSerializable` implementation as the serialization framework for the cache. The following table lists the attribute that can be specified for an `<instantiator>`.

**<instantiator> Attributes**

| Attribute | Description                                                  | Default |
| :-------- | :----------------------------------------------------------- | :------ |
| id        | Required. ID that the Instantiator should associate with the `DataSerializable` type. |         |

## <backup>

Defines additional files or directories that should be backed up when the system wide backup command is invoked. Disk stores with persistent data are automatically backed up and do not need to be listed with this element.

**Example:**

```
<backup>./systemConfig/gf.jar</backup>
<backup>/users/jpearson/gfSystemInfo/myCustomerConfig.doc</backup>
```

## <initializer>

Used to specify a callback class (and optionally its parameters) that will be run after the cache is initialized. This element can be specified for both server and client caches.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**Default:**

**API:** `Declarable`

**Example:**

```
<initializer>
   <class-name>MyInitializer</class-name>
      <parameter name="members">
         <string>2</string>
      </parameter>
</initializer>
```

## <declarable>

Specifies a Declarable object to be placed in a Region entry.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `Declarable`

**Example:**

```
<parameter name="cacheserver">
  <declarable>
    <class-name>org.apache.geode.addon.cache.CacheServerInitializer</class-name>
    <parameter name="system.property.prefix">
    <string>cacheserver</string>
    </parameter>
  </declarable>
</parameter>
```

## <class-name> and <parameter>

Specify the name of a Java class with the `<class-name>` sub-element.

Specify initialization parameters for the class using the `<parameter>` sub-element. Use the `name`attribute to specify the name of the parameter and specify its value in the content of the [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#string)sub-element or by specifying a Java class with the [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#declarable) sub-element.

**Example:**

The following transaction writer configuration example specifies a Java class named `com.company.data.MyTransactionWritet`. The class is initialized with a parameter named `URL` whose value is `jdbc:cloudscape:rmi:MyData`.

```
<transaction-writer>
       <class-name>com.company.data.MyTransactionWriter</class-name>
       <parameter name="URL">
         <string>jdbc:cloudscape:rmi:MyData</string>
       </parameter>
</transaction-writer>
```

## <declarable>

Specifies a Declarable object to be placed in a Region entry.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements.

**API:** `Declarable`

**Example:**

```
<parameter name="cacheserver">
  <declarable>
    <class-name>org.apache.geode.addon.cache.CacheServerInitializer</class-name>
    <parameter name="system.property.prefix">
    <string>cacheserver</string>
    </parameter>
  </declarable>
</parameter>
```

## <string>

Specifies a String to be placed in a Region entry.

**Example:**

```
<region ...>
 <region-attributes ...>
   ...
 </region-attributes>
 <entry>
   <key><string>MyKey</string></key>
   <value><string>MyValue</string></value>
 </entry>
</region>
```



# Region Shortcuts





This topic describes the various region shortcuts you can use to configure Geode regions.

Region shortcuts are groupings of pre-configured attributes that define the characteristics of a region. You can use a region shortcut as a starting point when configuring regions and you can add additional configurations to customize your application. To reference a region shortcut in a Geode `cache.xml` file, use the `refid` attribute of the `<region>` element. For example:

```
<region name="myRegion" refid="PARTITION_REDUNDANT"/>
```

You can override the default values and add additional configurations within a `<region-attributes>` element of the `cache.xml` file. For example, the following configuration overrides the local-max-memory setting and adds the recovery-delay attribute:

```
<region name="myRegion" refid="PARTITION_REDUNDANT">
    <region-attributes>
        <partition-attributes 
          local-max-memory="512" 
           recovery-delay=-1/>
    </region-attributes>
</region>
```

You can also create your own, named region shortcuts for common custom configurations. See [Region Shortcuts and Custom Named Region Attributes](https://geode.apache.org/docs/guide/17/basic_config/data_regions/region_shortcuts.html).

To configure a region using the gfsh command-line tool, specify the shortcut name with the `--type` argument. For example:

```
gfsh>create region --name=myRegion --type=PARTITION_REDUNDANT
```

**Note:** If you change the cache.xml file that defines a region, you must restart the member before the changes take effect.

For more information about configuring regions, see [Region Management](https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html).

For more information about using the various types of Geode regions and when to use them, see [Region Types](https://geode.apache.org/docs/guide/17/developing/region_options/region_types.html#region_types).

- **Region Shortcuts Quick Reference**

  This section provides a quick reference for all region shortcuts.

- **LOCAL**

- **LOCAL_HEAP_LRU**

- **LOCAL_OVERFLOW**

- **LOCAL_PERSISTENT**

- **LOCAL_PERSISTENT_OVERFLOW**

- **PARTITION**

- **PARTITION_HEAP_LRU**

- **PARTITION_OVERFLOW**

- **PARTITION_PERSISTENT**

- **PARTITION_PERSISTENT_OVERFLOW**

- **PARTITION_PROXY**

- **PARTITION_PROXY_REDUNDANT**

- **PARTITION_REDUNDANT**

- **PARTITION_REDUNDANT_HEAP_LRU**

- **PARTITION_REDUNDANT_OVERFLOW**

- **PARTITION_REDUNDANT_PERSISTENT**

- **PARTITION_REDUNDANT_PERSISTENT_OVERFLOW**

- **REPLICATE**

- **REPLICATE_HEAP_LRU**

- **REPLICATE_OVERFLOW**

- **REPLICATE_PERSISTENT**

- **REPLICATE_PERSISTENT_OVERFLOW**

- **REPLICATE_PROXY**



# Region Shortcuts Quick Reference





This section provides a quick reference for all region shortcuts.

[Region Shortcuts Default Configurations](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_table.html#reference_ufj_5kz_4k__table_lkl_hws_mk) summarizes the default configurations for each region shortcut. See the cross reference for additional information about each shortcut.



| Region Shortcut                                              | Region Attributes                                        | Other Attributes                                             |
| :----------------------------------------------------------- | :------------------------------------------------------- | :----------------------------------------------------------- |
| [LOCAL](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_w2h_3cd_lk) | scope: local data-policy: NORMAL                         |                                                              |
| [LOCAL_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wd5_lpy_lk) | scope: local data-policy: NORMAL                         | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:local-destroy |
| [LOCAL_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_adk_y4y_lk) | scope: local data-policy: NORMAL                         | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [LOCAL_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_l5r_y4y_lk) | scope: local data-policy: PERSISTENT_REPLICATE           |                                                              |
| [LOCAL_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_a45_y4y_lk) | scope: local data-policy: PERSISTENT_REPLICATE           | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [PARTITION](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_ow5_4qy_lk) | data-policy: PARTITION                                   |                                                              |
| [PARTITION_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_twx_y4y_lk) | data-policy: PARTITION                                   | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:local-destroy |
| [PARTITION_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_js1_z4y_lk) | data-policy: PARTITION                                   | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [PARTITION_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_d4k_jpy_lk) | data-policy: PERSISTENT_PARTITION                        |                                                              |
| [PARTITION_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_v5l_jpy_lk) | data-policy: PERSISTENT_PARTITION                        | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [PARTITION_PROXY](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_v4m_jpy_lk) | data-policy: PARTITION                                   | Partition Attributeslocal-max-memory:0                       |
| [PARTITION_PROXY_REDUNDANT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_c1n_jpy_lk) | data-policy: PARTITION                                   | Partition Attributesredundant-copies:1local-max-memory0      |
| [PARTITION_REDUNDANT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_shn_jpy_lk) | data-policy: PARTITION                                   | Partition Attributesredundant-copies:1                       |
| [PARTITION_REDUNDANT_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_m4n_jpy_lk) | data-policy: PARTITION                                   | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:local-destroyPartition Attributesredundant-copies:1 |
| [PARTITION_REDUNDANT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_own_jpy_lk) | data-policy: PARTITION                                   | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-diskPartition Attributesredundant-copies:1 |
| [PARTITION_REDUNDANT_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_bd4_jpy_lk) | data-policy: PERSISTENT_PARTITION                        | Partition Attributesredundant-copies:1                       |
| [PARTITION_REDUNDANT_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_xqq_tvc_lk) | data-policy: PERSISTENT_PARTITION                        | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-diskPartition Attributesredundant-copies:1 |
| [REPLICATE](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wq4_jpy_lk) | data-policy: REPLICATE scope: distributed-ack            |                                                              |
| [REPLICATE_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_xx4_jpy_lk) | data-policy: PRELOADED scope: distributed-ack            | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:local-destroy**Note:**Normally, you cannot have a `local-destroy`eviction policy on replicated regions. However, the REPLICATE_HEAP_LRU region is not the same as other replicated regions. This region is preloaded and remains in synch by registering interest in the keys of its peer’s replicated regions.Subscription Attributesinterest-policy:all |
| [REPLICATE_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_t2p_jpy_lk) | data-policy: REPLICATE scope: distributed-ack            | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [REPLICATE_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_emp_jpy_lk) | data-policy: PERSISTENT_REPLICATE scope: distributed-ack |                                                              |
| [REPLICATE_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_tsp_jpy_lk) | data-policy: PERSISTENT_REPLICATE scope: distributed-ack | Eviction Attributeseviction-algorithm:lru-heap-percentageeviction-action:overflow-to-disk |
| [REPLICATE_PROXY](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_n1q_jpy_lk) | data-policy: EMPTY scope: distributed-ack                |                                                              |

Table 1. Region Shortcuts Default Configurations



