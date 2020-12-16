# cache.xml

使用cache.xml文件设置常规缓存功能和行为，以及创建和初始化缓存的数据区域。 这些部分介绍了cache.xml要求； 提供`<cache>`和`<client-cache>`元素的层次图； 并描述每个元素的功能。

**注意:** 您可以使用[gfsh](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html) 配置cache.xml文件的大多数元素并将其应用于整个集群。 [集群配置服务](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。 请参阅[gfsh限制](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl__section_bn3_23p_y4) ，以获取您无法在gfsh中配置但仍必须在cache.xml中配置的项目列表 。



### cache.xml 快速参考

本节介绍cache.xml文件的要求和变量。 它还指向服务器，客户端和WAN配置的特定元素部分。

**Cache XML Requirements**
cache.xml文件具有以下要求：

- 内容必须符合`cache-1.0.xsd`中提供的XML模式定义。 模式定义文件位于"http://geode.apache.org/schema/cache/cache-1.0.xsd"。

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



**Variables in cache.xml**

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



**Configuration Quick Reference**

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



### `<cache>` Element Hierarchy {#_cache__ Element Hierarchy}

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

## `<cache-transaction-manager>` {#cache_transaction_manager>}
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

## `<transaction-listener>` {#transaction_listener>}
When a transaction ends, its thread calls the TransactionListener to perform the appropriate follow-up for successful commits, failed commits, or voluntary rollbacks.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

## <transaction-writer> {#transaction_writer>}
When you commit a transaction, a TransactionWriter can perform additional tasks, including aborting the transaction.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

## <dynamic-region-factory> {#dynamic_region_factory>}
The `<dynamic-region-factory>` element configures a dynamic region factory for this cache. You can use this element to dynamically create regions in your application code. Use the createDynamicRegion() method of the org.apache.geode.cache.DynamicRegionFactory class in your Java code to dynamically create regions.

**注意:** You can not use this element to dynamically create *partitioned* regions.

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

## <disk-dir> {#disk_dir>}
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

## <gateway-sender> {#gateway_sender>}
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

## <gateway-event-filter> {#gateway_event_filter>}
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

## <gateway-event-substitution-filter> {#gateway_event_substitution_filter>}
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

## <gateway-transport-filter> {#gateway_transport_filter>}
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

## <gateway-receiver> {#gateway_receiver>}
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

## <gateway-transport-filter> {#gateway_transport_filter>}
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

## <gateway-conflict-resolver> {#gateway_conflict_resolver>}
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

## <async-event-queue> {#async_event_queue>}
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

## <async-event-listener> {#async_event_listener>}
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

## <cache-server> {#cache_server>}
Configures the cache to serve region data to clients in a client/server caching system. This element indicates the port the server listens on for client communication.

The `cacheserver` process uses only `cache.xml` configuration. For application servers, you can set the same configuration properties using the `org.apache.geode.cache.server.CacheServer` and `org.apache.geode.cache.Cache` interfaces. For detailed information, see the online Java API documentation.

**API:** `org.apache.geode.cache.server.CacheServer`

**<cache-server> Attributes**

| Attribute                  | Description                                                  | Default Value   |
| :------------------------- | :----------------------------------------------------------- | :-------------- |
| bind-address               | Hostname or IP address that the server is to listen on for client connections. If null, the server listens on the machine’s default address. | null            |
| hostname-for-clients       | Hostname or IP address to pass to the client as the location where the server is listening. When the server connects to the locator it tells the locator the host and port where it is listening for client connections. If the host the server uses by default is one that the client can’t translate into an IP address, the client will have no route to the server’s host and won’t be able to find the server. For this situation, you must supply the server’s alternate hostname for the locator to pass to the client. If null, the server’s `bind-address`setting is used. | null            |
| load-poll-interval         | Frequency, in milliseconds, to poll the load probe for load information on the server. | 5000(5 seconds) |
| max-connections            | Maximum number of client connections for the server. When the maximum is reached, the server refuses additional client connections.**注意:** Set this at least as high as max-threads. | 800             |
| max-threads                | Maximum number of threads allowed in this server to service client connections. When the limit is reached, server threads begin servicing multiple connections. A zero setting causes the server to use a thread for every client connection.**注意:** Set this no higher than max-connections. | 0               |
| maximum-message-count      | Maximum number of messages allowed in a subscription queue. When the queue reaches this limit, messages block.**注意:** Used only if `client-subscription` is not configured. | 230000          |
| maximum-time-between-pings | Maximum time, in milliseconds, the server allows to pass between messages or pings indicating a client is healthy.**注意:** A setting of 0 or a negative number turns off client health monitoring. Be careful not to do this accidentally. | 60000(1 minute) |
| message-time-to-live       | Setting used for highly available subscription queues. The expiration time, in seconds, for non-durable messages in the secondary server’s client subscription queue. The system removes non-durable messages that have been in the queue beyond this time. If set to 0 (zero), the messages are never removed.**注意:** Set this high enough to avoid removing messages that are still valid, to aovid losing messages during server failover. | 180(3 minutes)  |
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

## <client-subscription> {#client_subscription>}
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

## <custom-load-probe> {#custom_load_probe>}
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

## <pool> {#pool>}
Use for client caches. Defines a client’s server pool used to communicate with servers running in a different cluster.

**API:** `org.apache.geode.cache.client.PoolFactory`

**<pool> Attributes**

| Attribute                             | Description                                                  | Default            |
| :------------------------------------ | :----------------------------------------------------------- | :----------------- |
| free-connection-timeout               | Amount of time a thread will wait to get a pool connection before timing out with an exception. This timeout keeps threads from waiting indefinitely when the pool’s `max-connections` has been reached and all connections in the pool are in use by other threads. | 10000              |
| idle-timeout                          | Maximum time, in milliseconds, a pool connection can stay open without being used when there are more than `min-connections` in the pool. Pings over the connection do not count as connection use. If set to -1, there is no idle timeout. | 5000               |
| load-conditioning-interval            | Amount of time, in milliseconds, a pool connection can remain open before being eligible for silent replacement to a less-loaded server. | 300000(5 minutes)  |
| max-connections                       | Maximum number of pool connections the pool can create. If the maximum connections are in use, an operation requiring a client-to-server connection blocks until a connection becomes available or the `free-connection-timeout` is reached. If set to -1, there is no maximum. The setting must indicate a cap greater than `min-connections`.**注意:** If you need to use this to cap your pool connections, you should disable the pool attribute `pr-single-hop-enabled`. Leaving single hop enabled can increase thrashing and lower performance. | -1                 |
| min-connections                       | Minimum number of pool connections to keep available at all times. Used to establish the initial connection pool. If set to 0 (zero), no connection is created until an operation requires it. This number is the starting point, with more connections added later as needed, up to the `max-connection` setting. The setting must be an integer greater than or equal to 0. | 1                  |
| multiuser-authentication              | Used for installations with security where you want to accommodate multiple users within a single client. If set to true, the pool provides authorization for multiple user instances in the same client application, and each user accesses the cache through its own `RegionService` instance. If false, the client either uses no authorization or just provides credentials for the single client process. | false              |
| name                                  | Name of this pool. Used in the client region pool-name to assign this pool to a region in the client cache.**注意:** This is a required property with no default setting. | none               |
| ping-interval                         | How often to communicate with the server to show the client is alive, set in milliseconds. Pings are only sent when the ping-interval elapses between normal client messages.**注意:** Set this lower than the server’s `maximum-time-between-pings`. | 10000              |
| pr-single-hop-enabled                 | Setting used to improve access to partitioned region data in the servers. Indicates whether to use metadata about the partitioned region data storage locations to decide where to send some data requests. This allows a client to send a data operation directly to the server hosting the key. Without this, the client contacts any available server and that server contacts the data store. This is used only for operations that can be carried out on a server-by-server basis, like put, get, and destroy. | true               |
| read-timeout                          | Maximum time, in milliseconds, for the client to wait for a response from a server. | 10000              |
| retry-attempts                        | Number of times to retry a client request before giving up. If one server fails, the pool moves to the next, and so on until it is successful or it hits this limit. If the available servers are fewer than this setting, the pool will retry servers that have already failed until it reaches the limit. If this is set to -1, the pool tries every available server once. | -1                 |
| server-group                          | Logical named server group to use from the pool. A null value uses the global server group to which all servers belong.**注意:** This is only used when the `locator` list is defined. | null               |
| socket-buffer-size                    | Size for socket buffers from the client to the server. Default: 32768. | 32768              |
| statistic-interval                    | Interval, in milliseconds, at which to send client statistics to the server. If set to -1, statistics are not sent. | -1                 |
| subscription-ack-interval             | Time, in milliseconds, between messages to the primary server to acknowledge event receipt.**注意:** Used only when `subscription-redundancy` is not ‘0’ (zero). | 100                |
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

## <locator> {#locator>}
Addresses and ports of the locators to connect to. You can define multiple locators for the pool.

**注意:** Provide a locator list or `server` list, but not both.

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

## <server> {#server>}
Addresses and ports of the servers to connect to.

**注意:** Provide a server list or `locator` list, but not both.

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

## <disk-store> {#disk_store>}
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

## <disk-dirs> {#disk_dirs>}
An element of a disk store that defines a set of `<disk-dir>` elements.

## <disk-dir> {#disk_dir>}
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

## <pdx> {#pdx>}
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

## <pdx-serializer> {#pdx_serializer>}
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

## <region-attributes> {#region_attributes>}
Specifies a region attributes template that can be named (by `id`) and referenced (by `refid`) later in the `cache.xml` and through the API.

**API:** `org.apache.geode.cache.RegionFactory` or `org.apache.geode.cache.ClientRegionFactory`

**<region-attributes> Attributes**

| Attribute                      | Description                                                  | Default             |
| :----------------------------- | :----------------------------------------------------------- | :------------------ |
| concurrency-level              | Gives an estimate of the maximum number of application threads that will concurrently access a region entry at one time. This attribute does not apply to partitioned regions. This attribute helps Geode optimize the use of system resources and reduce thread contention. This sets an initial parameter on the underlying `java.util.ConcurrentHashMap`used for storing region entries.**注意:** Before you modify this, read the concurrency level description, then see the Java API documentation for `java.util.ConcurrentHashMap`.**API:** `setConcurrencyLevel`**Example:**`<region-attributes    concurrency-level="10"> </region-attributes>` | 16 (threads)        |
| data-policy                    | Specifies how the local cache handles data for a region. This setting controls behavior such as local data storage and region initialization.**注意:** Configure the most common options using the region shortcuts, `RegionShortcut` and `ClientRegionShortcut`. The default `data-policy` of `normal` specifies local cache storage. The empty policy specifies no local storage. In the region shortcuts, empty corresponds to the settings with the string `PROXY`. You can use an empty region for event delivery to and from the local cache without the memory overhead of data storage.You can specify the following data policies:emptyNo data storage in the local cache. The region always appears empty. Use this for event delivery to and from the local cache without the memory overhead of data storage - zero-footprint producers that only distribute data to others and zero-footprint consumers that only see events. To receive events with this, set the region’s `subscription-attributes` `interest-policy`to all.normalData used locally (accessed with `get`, stored with `put`, etc.) is stored in the local cache. This policy allows the contents in the cache to differ from other caches.partitionData is partitioned across local and remote caches using the automatic data distribution behavior of partitioned regions. Additional configuration is done in the `partition-attributes`.replicateThe region is initialized with the data from other caches. After initialization, all events for the distributed region are automatically copied into the local region, maintaining a replica of the entire distributed region in the local cache. Operations that would cause the contents to differ with other caches are not allowed. This is compatible with local `scope`, behaving the same as for normal.persistent-partitionBehaves the same as `partition` and also persists data to disk.persistent-replicateBehaves the same as `replicate` and also persists data to disk.preloadedInitializes like a replicated region, then, once initialized, behaves like a normal region.**API:** `setDataPolicy`**Example**:`<region-attributes    data-policy="replicate">  </region-attributes>`This is similar to using a region shortcut with`refid`, however when you use the REPLICATE region shortcut, it automatically sets the region’s scope to `distributed-ack`.`<region-attributes    refid="REPLICATE">  </region-attributes>`If you use `data-policy`, you must set the scope explicitly. | normal              |
| enable-async-conflation        | For TCP/IP distributions between peers, specifies whether to allow aggregation of asynchronous messages sent by the producer member for the region. This is a special-purpose voolean attribute that applies only when asynchronous queues are used for slow consumers. A false value disables conflation so that all asynchronous messages are sent individually. This special-purpose attribute gives you extra control over peer-to-peer communication between distributed regions using TCP/IP. This attribute does not apply to client/server communication or to communication using the UDP unicast or IP multicast protocols.**注意:** To use this attribute, the `multicast-enabled` region attribute `disable-tcp` in `gemfire.properties` must be false (the default for both). In addition, asynchronous queues must be enabled for slow consumers, specified with the `async`* gemfire properties.**API:** `setEnableAsyncConflation`**Example:**`<region-attributes    enable-async-conflation="false"> </region-attributes>` | true                |
| enable-subscription-conflation | Boolean for server regions that specifies whether the server can conflate its messages to the client. A true value enables conflation.**注意:**The client can override this setting with the `conflate-events` property in its `gemfire.properties`.**API:** `setEnableSubscriptionConflation`**Example:**`<region-attributes   enable-subscription-conflation="true">  </region-attributes>` | false               |
| gateway-sender-ids             | Specifies one or more gateway sender IDs to use for distributing region events to remote Geode sites. Specify multiple IDs as a comma-separated list.**API:** `addGatewaySenderId`**Example:**`<region-attributes    gateway-sender-ids="nwsender,swsender"> </region-attributes>` | not set             |
| async-event-queue-ids          | Specifies one or more asynchronous event queues to use for distributing region events an `AsyncEventListener`implementation (for example, for write-behind cache event handling). Specify multiple IDs as a comma-separated list.**API**: `addAsyncEventQueueId`**Example**:`<region-attributes    async-event-queue-ids="customerqueue,ordersqueue"> </region-attributes>` | not set             |
| hub-id                         | If the `enable-gateway` attribute is set to true, a comma-separated list of gateway hub IDs that receive events from the region.Used only with GemFire version 6.x gateway configurations. For GemFire 7.0 configuration, see the `gateway-sender-id`attribute of the `<region-attributes>` element. | null                |
| id                             | Stores the region attribute settings in the cache with this identifier. Once stored, the attributes can be retrieved using the region attribute `refid`.**API:** `setId`**Example:**`<region-attributes    id="persistent-replicated"> </region-attributes>` | not set             |
| ignore-jta                     | Boolean that determines whether operations on this region participate in active JTA transactions or ignore them and operate outside of the transactions. This is primarily used in cache loaders, writers, and listeners that need to perform non-transactional operations on a region, such as caching a result set.**API:** `setIgnoreJTA`**Example:**`<region-attributes    ignore-jta="true"> </region-attributes>` | false               |
| index-update-type              | Specifies whether region indexes are maintained synchronously with region modifications, or asynchronously in a background thread. In the `cache.xml`file, this is set as a value, asynchronous or synchronous, assigned to the `index-update-type` region attribute. Set this through the API by passing a boolean to the `setIndexMaintenanceSynchronous` method.**API:** `setIndexMaintenanceSynchronous`**Example:**`<region-attributes    index-update-type="asynchronous"> </region-attributes>` | synchronous updates |
| initial-capacity               | Together with the `load-factor` region attribute, sets the initial parameters on the underlying `java.util.ConcurrentHashMap` used for storing region entries.**API:** `setInitialCapacity`**Example:**`<region-attributes    initial-capacity="20"> </region-attributes>` | 16                  |
| is-lock-grantor                | Determines whether this member defines itself as the lock grantor for the region at region creation time. This only specifies whether the member becomes lock grantor at creation and does not reflect the current state of the member’s lock grantor status. The member’s lock grantor status may change if another member subsequently defines the region with `is-lock-grantor` set to true. This attribute is only relevant for regions with `global` `scope`, as only they allow locking. It affects implicit and explicit locking.**API:** `setLockGrantor`**Example:**`<region-attributes    is-lock-grantor="true"> </region-attributes>` | false               |
| load-factor                    | Together with the initial-capacity region attribute, sets the initial parameters on the underlying `java.util.ConcurrentHashMap` used for storing region entries. This must be a floating point number between 0 and 1, inclusive.**注意:**Before you set this attribute, read the discussion of initial capacity and load factor, then see the Java API documentation for `java.util.ConcurrentHashMap`.**API:** `setLoadFactor`**Example:**`<region-attributes    load-factor="0.85"> </region-attributes>` | .75                 |
| mirror-type                    | Deprecated                                                   |                     |
| multicast-enabled              | Boolean that specifies whether distributed operations on a region should use multicasting. To enable this, multicast must be enabled for the cluster with the `mcast-port` `gemfire.properties` setting.**API:** `setMulticastEnabled`**Example:**`<region-attributes    multicast-enabled="true"> </region-attributes>` | false               |
| pool-name                      | Identifies the region as a client region and specifies the server pool the region is to use. The named pool must be defined in the client cache before the region is created. If this is not set, the region does not connect to the servers as a client region.**API:** `setPoolName`**Examples:**This declaration creates the region as a client region with a server pool named DatabasePool. This pool-name specification is required, as there are multiple pools in the client cache:`<client-cache>     <pool name="DatabasePool"     subscription-enabled="true">         ...     </pool>     <pool >    name="OtherPool"     subscription-enabled="true">         ...     </pool>     <region ...         <region-attributes            pool-name="DatabasePool">          </region-attributes>         ...`This declaration creates the region as a client region assigned the single pool that is defined for the client cache. Here the pool-name specification is implied to be the only pool that exists in the cache:`<client-cache>     <pool     name="publisher"     subscription-enabled="true">         ...     </pool>     <region     name="myRegion"     refid="CACHING_PROXY">     </region> </client-cache>` | not set             |
| disk-store-name                | Assigns the region to the disk store with this name from the disk stores defined for the cache. Persist region data to disk by defining the region as persistent using the Shortcut Attribute Options or data-policy settings. Overflow data to disk by implementing LRU eviction-attributes with an action of overflow to disk. Each disk store defines the file system directories to use, how data is written to disk, and other disk storage maintenance properties. In addition, the `disk-synchronous` region attribute specifies whether writes are done synchronously or asynchronously.**API:** `setDiskStoreName`**Example:**`<region-attributes    disk-store-name="myStoreA" > </region-attributes>` | null                |
| disk-synchronous               | For regions that write to disk, boolean that specifies whether disk writes are done synchronously for the region.**API:** `setDiskSynchronous`**Example:**`<region-attributes    disk-store-name="myStoreA"    disk-synchronous="true"> </region-attributes>` | true                |
| refid                          | Retrieves region shortcuts and user-defined named region attributes for attributes initialization**API:** `setRefId`**Example:**`<region-attributes    refid="persistent-replicated">   <!-- Override any stored      attribute settings that you      need to ... --> </region-attributes>` | not set             |
| scope                          | Definition: Determines how updates to region entries are distributed to the other caches in the cluster where the region and entry are defined. Scope also determines whether to allow remote invocation of some of the region’s event handlers, and whether to use region entry versions to provide consistent updates across replicated regions.**注意:**You can configure the most common of these options with Geode region shortcuts in `RegionShortcut` and `ClientRegionShortcut`.**注意:**Server regions that are not partitioned must be replicated with `distributed-ack` or `global` scope. The region shortcuts that specify `REPLICATE` have `distributed-ack`scope.Set one of the following scope values:localNo distribution. The region is visible only to threads running inside the member.distributed-no-ackEvents are distributed to remote caches with no acknowledgement required.distributed-ackEvents are distributed to remote caches with receipt acknowledgement required. Region entry versions are used to provide consistent updates across members of the cluster.globalEvents are distributed to remote caches with global locking to ensure distributed cache consistency.**API:** `setScope`**Example:**`<region-attributes    scope="distributed-ack"> </region-attributes>` | distributed-no-ack  |
| statistics-enabled             | Boolean specifying whether to gather statistics on the region. Must be true to use expiration on the region. Geode provides a standard set of statistics for cached regions and region entries, which give you information for fine-tuning your cluster. Unlike other Geode statistics, statistics for local and distributed regions are not archived and cannot be charted. They are kept in instances of `org.apache.geode.cache.CacheStatistics` and made available through the region and its entries through the `Region.getStatistics` and `Region.Entry.getStatistics`methods.**API:** `setStatisticsEnabled`**Example:**`<region-attributes    statistics-enabled="true"> </region-attributes>` | false               |
| cloning-enabled                | Determines how `fromDelta` applies deltas to the local cache for delta propagation. When true, the updates are applied to a clone of the value and then the clone is saved to the cache. When false, the value is modified in place in the cache.**API:** `setCloningEnabled`**Example:**`<region-attributes    cloning-enabled="true"> </region-attributes>` | false               |
| concurrency-checks-enabled     | Determines whether members perform checks to provide consistent handling for concurrent or out-of-order updates to distributed regions. See [Consistency for Region Updates](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).**注意:** Applications that use a `client-cache` may want to disable concurrency checking in order to see all events for a region. Geode server members can continue using concurrency checks for the region, but they will pass all events to the client cache. This configuration ensures that the client sees all events, but it does not prevent the client cache from becoming out-of-sync with the server cache.**API:** `setConcurrencyChecksEnabled`**Example:**`<region-attributes   concurrency-checks-enabled="true"> </region-attributes>` | true                |
| off-heap                       | Specifies that the region uses off-heap memory to store entry values, including values for region entries and queue entries. The region will still use heap memory for everything else, such as entry keys and the ConcurrentHashMap.**API:** `setOffHeap`**Example:**`<region-attributes    off-heap="true"> </region-attributes>` | false               |



## <key-constraint> {#key_constraint>}
Defines the type of object to be allowed for the region entry keys. This must be a fully-qualified class name. The attribute ensures that the keys for the region entries are all of the same class. If key-constraint is not used, the region’s keys can be of any class. This attribute, along with value-constraint, is useful for querying and indexing because it provides object type information to the query engine.

**注意:** Set the constraint in every cache where you create or update the region entries. For client/server installations, match constraints between client and server and between clusters. The constraint is only checked in the cache that does the entry `put` or `create` operation. To avoid deserializing the object, the constraint is not checked when the entry is distributed to other caches.

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

## <value-constraint> {#value_constraint>}
Defines the type of object to be allowed for the region entry values. This must be a fully-qualified class name. If value constraint isn’t used, the region’s value can be of any class. This attribute, along with `key-constraint`, is useful for querying and indexing because it provides object type information to the query engine.

**注意:** Set the constraint in every cache where you create or update the region entries. For client/server installations, match constraints between client and server and between clusters. The constraint is only checked in the cache that does the entry `put` or `create` operation. To avoid deserializing the object, the constraint is not checked when the entry is distributed to other caches.

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

## <region-time-to-live> {#region_time_to_live>}
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

## <expiration-attributes> {#expiration_attributes>}
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

## <custom-expiry> {#custom_expiry>}
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

## <region-idle-time> {#region_idle_time>}
Expiration setting that specifies how long the region can remain in the cache without anyone accessing it.

**注意:** To ensure reliable read behavior across the partitioned region, use `region-time-to-live` for region expiration instead of this setting.

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

## <expiration-attributes> {#expiration_attributes>}
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

## <custom-expiry> {#custom_expiry>}
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

## <entry-time-to-live> {#entry_time_to_live>}
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

## <expiration-attributes> {#expiration_attributes>}
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

## <custom-expiry> {#custom_expiry>}
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

## <entry-idle-time> {#entry_idle_time>}
Expiration setting that specifies how long the region’s entries can remain in the cache without anyone accessing them. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#expiration-attributes) for details.

**注意:** To ensure reliable read behavior across the partitioned region, use `entry-time-to-live` for entry expiration instead of this setting.

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

## <expiration-attributes> {#expiration_attributes>}
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

## <custom-expiry> {#custom_expiry>}
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

## <partition-attributes> {#partition_attributes>}
Defines the region as partitioned and controls partitioning behavior. This is set during the region creation in the first data store for the partitioned region.

**注意:** With the exception of `local-max-memory`, all members defining a partitioned region must use the same partition attribute settings.

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

## <partition-resolver> {#partition_resolver>}
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

## <partition-listener> {#partition_listener>}
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

## <fixed-partition-attributes> {#fixed_partition_attributes>}
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

## <membership-attributes> {#membership_attributes>}
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

## <required-role> {#required_role>}
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

## <subscription-attributes> {#subscription_attributes>}
Specifies subscriber requirements and behavior for the region. There is one subscription attribute, `interest-policy`, that defines which distributed entry events are delivered to the local region.

**注意:** The interest policy determines which events are delivered, but the `data-policy` determines how the events are applied to the cache.

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

## <cache-loader> {#cache_loader>}
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

## <cache-writer> {#cache_writer>}
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

## <cache-listener> {#cache_listener>}
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

## <compressor> {#compressor>}
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

## <eviction-attributes> {#eviction_attributes>}
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

## <lru-entry-count> {#lru_entry_count>}
Using the maximum attribute, specifies maximum region capacity based on entry count.

**<lru-entry-count> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |
| maximum   | The maximum number of entries allowed in a region.           |               |



## <lru-heap-percentage> {#lru_heap_percentage>}
Runs evictions when the Geode resource manager says to. The manager orders evictions when the total cache size is over the heap or off-heap percentage limit specified in the resource manager configuration. You can declare a Java class that implements the ObjectSizer interface to measure the size of objects in the Region.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**<lru-heap-percentage> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |



## <lru-memory-size> {#lru_memory_size>}
Using the maximum attribute, specifies maximum region capacity based on the amount of memory used, in megabytes. You can declare a Java class that implements the ObjectSizer interface to measure the size of objects in the Region.

Specify the Java class and its initialization parameters with the `<class-name>` and `<parameter>`sub-elements. See [ and ](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#class-name_parameter).

**<lru-memory-size> Attributes**

| Attribute | Description                                                  | Default       |
| :-------- | :----------------------------------------------------------- | :------------ |
| action    | Set one of the following eviction actions:local-destroyRemoves the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but is not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.overflow-to-diskThe entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. | local-destroy |
| maximum   | The maximum amount of memory used in the region, in megabytes. |               |



## <jndi-bindings> {#jndi_bindings>}
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

## <jndi-binding> {#jndi_binding>}
For every datasource that is bound to the JNDI tree, there should be one `<jndi-binding>` element. This element describes the property and the configuration of the datasource. Geode uses the attributes of the `<jndi-binding>` element for configuration. Use the `<config-property>` element to configure properties for the datasource.

We recommend that you set the username and password with the `user-name` and `password` jndi-binding attributes rather than using the `<config-property>` element.

**<jndi-binding> Attributes**

| Attribute                    | Description                                                  | Default |
| :--------------------------- | :----------------------------------------------------------- | :------ |
| blocking-timeout-seconds     | The number of seconds that a connection remains associated with a transaction. If this value is exceeded, the connection is disassociated from the transaction. | 120     |
| conn-pooled-datasource-class | Java class used for the `PooledDataSource` type.             |         |
| connection-url               | URL for connecting to the datasource.**注意:**If you are connecting to a JCA data source driver that implements XA transactions (where the jndi-binding type is **XAPooledDataSource**), do not use this attribute. Instead, define configuration properties for your database. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#config-property) for an example. |         |
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



## <config-property> {#config_property>}
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

## <config-property-name> {#config_property_name>}
The name of this datasource property.

## <config-property-type> {#config_property_type>}
The data type of this datasource property.

## <config-property-value> {#config_property_value>}
The value of this datasource property.

## <region> {#region>}
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

## <index> {#index>}
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

## <lucene:index> {#lucene:index>}
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

## <lucene:field> {#lucene:field>}
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

## <entry> {#entry>}
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

## <key> {#key>}
Required. Describes the key in a region entry. A key can contain either a <string> or a <declarable> sub-element.

## <string> {#string>}
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

## <declarable> {#declarable>}
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

## <value> {#value>}
Required. Describes the value of a region entry. A `<value>` can contain either a `<string>` or a `<declarable>` sub-element.

## <string> {#string>}
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

## <declarable> {#declarable>}
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

## <region> {#region>}
When nested within a `<region>` element, defines a subregion. See [Create and Access Data Subregions](https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html#data_regions__section_jn1_sry_5m) for restrictions on creating subregions. For example, you cannot create a partitioned subregion.

See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region)

## <function-service> {#function_service>}
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

## <function> {#function>}
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

## <resource-manager> {#resource_manager>}
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

## <serialization-registration> {#serialization_registration>}
Set of serializer or instantiator tags to register customer DataSerializer extensions or DataSerializable implementations respectively.

**Example:**

```
<serialization-registration>
    <instantiator id="30">        
       <class-name>com.package.MyClass</class-name>
    </instantiator>
</serialization-registration> 
```

## <serializer> {#serializer>}
Allows you to configure the DataSerializer for this Geode member. It registers a custom class which extends DataSerializer to support custom serialization of non-modifiable object types inside Geode.

Specify the Java class for the `DataSerializer` and its initialization parameters with the `<class-name>` sub-element.

**API:** You can also register a `DataSerializer` by using the `org.apache.geode.DataSerializer.register`API. Use the `org.apache.geode.Instantiator` API to register a `DataSerializable` implementation.

## <instantiator> {#instantiator>}
An Instantiator registers a custom class which implements the `DataSerializable` interface to support custom object serialization inside Geode.

Specify the Java class and its initialization parameters with the `<class-name>` sub-element.

**API:** `DataSerializable`

You can also directly specify `<instantiator>` as a sub-element of `<cache>`. Use the `org.apache.geode.Instantiator` API to register a `DataSerializable` implementation as the serialization framework for the cache. The following table lists the attribute that can be specified for an `<instantiator>`.

**<instantiator> Attributes**

| Attribute | Description                                                  | Default |
| :-------- | :----------------------------------------------------------- | :------ |
| id        | Required. ID that the Instantiator should associate with the `DataSerializable` type. |         |

## <backup> {#backup>}
Defines additional files or directories that should be backed up when the system wide backup command is invoked. Disk stores with persistent data are automatically backed up and do not need to be listed with this element.

**Example:**

```
<backup>./systemConfig/gf.jar</backup>
<backup>/users/jpearson/gfSystemInfo/myCustomerConfig.doc</backup>
```

## <initializer> {#initializer>}
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

## <declarable> {#declarable>}
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

## <class-name> and <parameter> {#class_name>_and_<parameter>}
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

## <declarable> {#declarable>}
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

## <string> {#string>}
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



# `<client-cache>` Element Hierarchy {#__client_cache___Element_Hierarchy}

This section shows the hierarchy of `` element sub-elements that you use to configure Geode caches and clients.

For details, see [ Element Reference.](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html)

```xml
<client-cache>
   <cache-transaction-manager>
      <transaction-listener>
      <transaction-writer>
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
         <entry>
         <key>
            <string>
            <declarable>
         <value>
            <string>
            <declarable>
      <region>
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
   <initializer>
      <class-name>
      <parameter>
         <string>
         <declarable>
</client-cache>
```

