# Developing with Apache Geode

*Developing with Apache Geode* explains main concepts of application programming with Apache Geode. It describes how to plan and implement regions, data serialization, event handling, delta propagation, transactions, and more.

For information about Geode REST application development, see [Developing REST Applications for Apache Geode](https://geode.apache.org/docs/guide/17/rest_apps/book_intro.html).

- **Region Data Storage and Distribution**

  The Apache Geode data storage and distribution models put your data in the right place at the right time. You should understand all the options for data storage in Geode before you start configuring your data regions.

- **Partitioned Regions**

  In addition to basic region management, partitioned regions include options for high availability, data location control, and data balancing across the cluster.

- **Distributed and Replicated Regions**

  In addition to basic region management, distributed and replicated regions include options for things like push and pull distribution models, global locking, and region entry versions to ensure consistency across Geode members.

- **Consistency for Region Updates**

  Geode ensures that all copies of a region eventually reach a consistent state on all members and clients that host the region, including Geode members that distribute region events.

- **General Region Data Management**

  For all regions, you have options to control memory use, back up your data to disk, and keep stale data out of your cache.

- **Data Serialization**

  Data that you manage in Geode must be serialized and deserialized for storage and transmittal between processes. You can choose among several options for data serialization.

- **Events and Event Handling**

  Geode provides versatile and reliable event distribution and handling for your cached data and system member events.

- **Delta Propagation**

  Delta propagation allows you to reduce the amount of data you send over the network by including only changes to objects rather than the entire object.

- **Querying**

  Geode provides a SQL-like querying language called OQL that allows you to access data stored in Geode regions.

- **Continuous Querying**

  Continuous querying continuously returns events that match the queries you set up.

- **Transactions**

  Geode provides a transactions API, with `begin`, `commit`, and `rollback` methods. These methods are much the same as the familiar relational database transactions methods.

- **Function Execution**

  A function is a body of code that resides on a server and that an application can invoke from a client or from another server without the need to send the function code itself. The caller can direct a data-dependent function to operate on a particular dataset, or can direct a data-independent function to operate on a particular server, member, or member group.



## Region Data Storage and Distribution

The Apache Geode data storage and distribution models put your data in the right place at the right time. You should understand all the options for data storage in Geode before you configure your data regions.

- **Storage and Distribution Options**

  Geode provides several models for data storage and distribution, including partitioned or replicated regions as well as distributed or non-distributed regions (local cache storage).

- **Region Types**

  Region types define region behavior within a single cluster. You have various options for region data storage and distribution.

- **Region Data Stores and Data Accessors**

  Understand the difference between members that store data for a region and members that act only as data accessors to the region.

- **Creating Regions Dynamically**

  You can dynamically create regions in your application code and automatically instantiate them on members of a cluster.



### Storage and Distribution Options

Geode provides several models for data storage and distribution, including partitioned or replicated regions as well as distributed or non-distributed regions (local cache storage).

#### Peer-to-Peer Region Storage and Distribution

At its most general, data management means having current data available when and where your applications need it. In a properly configured Geode installation, you store your data in your local members and Geode automatically distributes it to the other members that need it according to your cache configuration settings. You may be storing very large data objects that require special consideration, or you may have a high volume of data requiring careful configuration to safeguard your application’s performance or memory use. You may need to be able to explicitly lock some data during particular operations. Most data management features are available as configuration options, which you can specify either using the `gfsh` cluster configuration service, `cache.xml` file or the API. Once configured, Geode manages the data automatically. For example, this is how you manage data distribution, disk storage, data expiration activities, and data partitioning. A few features are managed at run-time through the API.

At the architectural level, data distribution runs between peers in a single cluster and between clients and servers.

- Peer-to-peer provides the core distribution and storage models, which are specified as attributes on the data regions.
- For client/server, you choose which data regions to share between the client and server tiers. Then, within each region, you can fine-tune the data that the server automatically sends to the client by subscribing to subsets.

Data storage in any type of installation is based on the peer-to-peer configuration for each individual cluster. Data and event distribution is based on a combination of the peer-to-peer and system-to-system configurations.

Storage and distribution models are configured through cache and region attributes. The main choices are partitioned, replicated, or just distributed. All server regions must be partitioned or replicated. Each region’s `data-policy` and `subscription-attributes`, and its `scope` if it is not a partitioned region, interact for finer control of data distribution.

Storing Data in the Local Cache

To store data in your local cache, use a region `refid` with a `RegionShortcut` or `ClientRegionShortcut` that has local state. These automatically set the region `data-policy` to a non-empty policy. Regions without storage can send and receive event distributions without storing anything in your application heap. With the other settings, all entry operations received are stored locally.



### Region Types

Region types define region behavior within a single cluster. You have various options for region data storage and distribution.

Within a Geode cluster, you can define distributed regions and non-distributed regions, and you can define regions whose data is spread across the cluster, and regions whose data is entirely contained in a single member.

Your choice of region type is governed in part by the type of application you are running. In particular, you need to use specific region types for your servers and clients for effective communication between the two tiers:

- Server regions are created inside a `Cache` by servers and are accessed by clients that connect to the servers from outside the server’s cluster. Server regions must have region type partitioned or replicated. Server region configuration uses the `RegionShortcut` enum settings.
- Client regions are created inside a `ClientCache` by clients and are configured to distribute data and events between the client and the server tier. Client regions must have region type `local`. Client region configuration uses the `ClientRegionShortcut` enum settings.
- Peer regions are created inside a `Cache`. Peer regions may be server regions, or they may be regions that are not accessed by clients. Peer regions can have any region type. Peer region configuration uses the `RegionShortcut` enum settings.

When you configure a server or peer region using `gfsh` or with the `cache.xml` file, you can use *region shortcuts* to define the basic configuration of your region. A region shortcut provides a set of default configuration attributes that are designed for various types of caching architectures. You can then add additional configuration attributes as needed to customize your application. For more information and a complete reference of these region shortcuts, see [Region Shortcuts Reference](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_lt4_54c_lk).



These are the primary configuration choices for each data region.

| Region Type                | Description                                                  | Best suited for…                                             |
| -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Partitioned                | System-wide setting for the data set. Data is divided into buckets across the members that define the region. For high availability, configure redundant copies so each bucket is stored in multiple members with one member holding the primary. | Server regions and peer regionsVery large data setsHigh availabilityWrite performancePartitioned event listeners and data loaders |
| Replicated (distributed)   | Holds all data from the distributed region. The data from the distributed region is copied into the member replica region. Can be mixed with non-replication, with some members holding replicas and some holding non-replicas. | Server regions and peer regionsRead heavy, small datasetsAsynchronous distributionQuery performance |
| Distributed non-replicated | Data is spread across the members that define the region. Each member holds only the data it has expressed interest in. Can be mixed with replication, with some members holding replicas and some holding non-replicas. | Peer regions, but not server regions and not client regionsAsynchronous distributionQuery performance |
| Non-distributed (local)    | The region is visible only to the defining member.           | Client regions and peer regionsData that is not shared between applications |

Partitioned Regions

Partitioning is a good choice for very large server regions. Partitioned regions are ideal for data sets in the hundreds of gigabytes and beyond.

**Note:** Partitioned regions generally require more JDBC connections than other region types because each member that hosts data must have a connection.

Partitioned regions group your data into buckets, each of which is stored on a subset of all of the system members. Data location in the buckets does not affect the logical view - all members see the same logical data set.

Use partitioning for:

- **Large data sets**. Store data sets that are too large to fit into a single member, and all members will see the same logical data set. Partitioned regions divide the data into units of storage called buckets that are split across the members hosting the partitioned region data, so no member needs to host all of the region’s data. Geode provides dynamic redundancy recovery and rebalancing of partitioned regions, making them the choice for large-scale data containers. More members in the system can accommodate more uniform balancing of the data across all host members, allowing system throughput (both gets and puts) to scale as new members are added.
- **High availability**. Partitioned regions allow you configure the number of copies of your data that Geode should make. If a member fails, your data will be available without interruption from the remaining members. Partitioned regions can also be persisted to disk for additional high availability.
- **Scalability**. Partitioned regions can scale to large amounts of data because the data is divided between the members available to host the region. Increase your data capacity dynamically by simply adding new members. Partitioned regions also allow you to scale your processing capacity. Because your entries are spread out across the members hosting the region, reads and writes to those entries are also spread out across those members.
- **Good write performance**. You can configure the number of copies of your data. The amount of data transmitted per write does not increase with the number of members. By contrast, with replicated regions, each write must be sent to every member that has the region replicated, so the amount of data transmitted per write increases with the number of members.

In partitioned regions, you can colocate keys within buckets and across multiple partitioned regions. You can also control which members store which data buckets.

#### Replicated Regions

Replicated regions provide the highest performance in terms of throughput and latency. Replication is a good choice for small to medium size server regions.

Use replicated regions for:

- **Small amounts of data required by all members of the cluster**. For example, currency rate information and mortgage rates.
- **Data sets that can be contained entirely in a single member**. Each replicated region holds the complete data set for the region
- **High performance data access**. Replication guarantees local access from the heap for application threads, providing the lowest possible latency for data access.
- **Asynchronous distribution**. All distributed regions, replicated and non-replicated, provide the fastest distribution speeds.

#### Distributed, Non-Replicated Regions

Distributed regions provide the same performance as replicated regions, but each member stores only data in which it has expressed an interest, either by subscribing to events from other members or by defining the data entries in its cache.

Use distributed, non-replicated regions for:

- **Peer regions, but not server regions or client regions**. Server regions must be either replicated or partitioned. Client regions must be local.
- **Data sets where individual members need only notification and updates for changes to a subset of the data**. In non-replicated regions, each member receives only update events for the data entries it has defined in the local cache.
- **Asynchronous distribution**. All distributed regions, replicated and non-replicated, provide the fastest distribution speeds.

#### Local Regions

**Note:** When created using the `ClientRegionShortcut` settings, client regions are automatically defined as local, since all client distribution activities go to and come from the server tier.

The local region has no peer-to-peer distribution activity.

Use local regions for:

- **Client regions**. Distribution is only between the client and server tier.
- **Private data sets for the defining member**. The local region is not visible to peer members.



### Region Data Stores and Data Accessors

Understand the difference between members that store data for a region and members that act only as data accessors to the region.

In most cases, when you define a data region in a member’s cache, you also specify whether the member is a data store. Members that store data for a region are referred to as data stores or data hosts. Members that do not store data are referred to as accessor members, or empty members. Any member, store or accessor, that defines a region can access it, put data into it, and receive events from other members. To configure a region so the member is a data accessor, you use configurations that specify no local data storage for the region. Otherwise, the member is a data store for the region.

For server regions, suppress local data storage at region creation by specifying a region shortcut that contains the term “PROXY” in its name, such as `PARTITION_PROXY` or `REPLICATE_PROXY`.

For client regions, suppress local data storage at region creation by specifying the `PROXY` region shortcut. Do not use the `CACHING_PROXY` shortcut for this purpose, as it allows local data storage.



### Creating Regions Dynamically

You can dynamically create regions in your application code and automatically instantiate them on members of a cluster.

If your application does not require partitioned regions, you can use the org.apache.geode.cache.DynamicRegionFactory class to dynamically create regions, or you can create them using the `<dynamic-region-factory>` element in the cache.xml file that defines the region. See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#dynamic-region-factory).

Due to the number of options involved, most developers use functions to create regions dynamically in their applications, as described in this topic. Dynamic regions can also be created from the `gfsh` command line.

For a complete discussion of using Geode functions, see [Function Execution](https://geode.apache.org/docs/guide/17/developing/function_exec/chapter_overview.html). Functions use the org.apache.geode.cache.execute.FunctionService class.

For example, the following Java classes define and use a function for dynamic region creation:

The CreateRegionFunction class defines a function invoked on a server by a client using the onServer() method of the FunctionService class. This function call initiates region creation by putting an entry into the region attributes metadata region. The entry key is the region name and the value is the set of region attributes used to create the region.

```java
#CreateRegionFunction.java

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionAttributes;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.Scope;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

import java.util.Properties;

public class CreateRegionFunction implements Function, Declarable {

  private final Cache cache;

  private final Region<String,RegionAttributes> regionAttributesMetadataRegion;

  private static final String REGION_ATTRIBUTES_METADATA_REGION = 
                                     "_regionAttributesMetadata";

  public enum Status {SUCCESSFUL, UNSUCCESSFUL, ALREADY_EXISTS};

  public CreateRegionFunction() {
    this.cache = CacheFactory.getAnyInstance();
    this.regionAttributesMetadataRegion = createRegionAttributesMetadataRegion();
  }

  public void execute(FunctionContext context) {
    Object[] arguments = (Object[]) context.getArguments();
    String regionName = (String) arguments[0];
    RegionAttributes attributes = (RegionAttributes) arguments[1];

    // Create or retrieve region
    Status status = createOrRetrieveRegion(regionName, attributes);

    // Return status
    context.getResultSender().lastResult(status);
  }

  private Status createOrRetrieveRegion(String regionName, 
                                        RegionAttributes attributes) {
    Status status = Status.SUCCESSFUL;
    Region region = this.cache.getRegion(regionName);
    if (region == null) {
      // Put the attributes into the metadata region. The afterCreate call will
      // actually create the region.
      this.regionAttributesMetadataRegion.put(regionName, attributes);

      // Retrieve the region after creating it
      region = this.cache.getRegion(regionName);
      if (region == null) {
        status = Status.UNSUCCESSFUL;
      }
    } else {
      status = Status.ALREADY_EXISTS;
    }
    return status;
  }

  private Region<String,RegionAttributes> 
  createRegionAttributesMetadataRegion() {
    Region<String, RegionAttributes> metaRegion = 
                         this.cache.getRegion(REGION_ATTRIBUTES_METADATA_REGION);
    if (metaRegion == null) {
      RegionFactory<String, RegionAttributes> factory =
                              this.cache.createRegionFactory();
      factory.setDataPolicy(DataPolicy.REPLICATE);
      factory.setScope(Scope.DISTRIBUTED_ACK);
      factory.addCacheListener(new CreateRegionCacheListener());
      metaRegion = factory.create(REGION_ATTRIBUTES_METADATA_REGION);
    }
    return metaRegion;
  }

  public String getId() {
    return getClass().getSimpleName();
  }

  public boolean optimizeForWrite() {
    return false;
  }

  public boolean isHA() {
    return true;
  }

  public boolean hasResult() {
    return true;
  }

  public void init(Properties properties) {
  }
}
```

The CreateRegionCacheListener class is a cache listener that implements two methods, afterCreate() and afterRegionCreate(). The afterCreate() method creates the region. The afterRegionCreate() method causes each new server to create all the regions defined in the metadata region.

```java
#CreateRegionCacheListener.java

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionAttributes;
import org.apache.geode.cache.RegionEvent;
import org.apache.geode.cache.RegionExistsException;

import org.apache.geode.cache.util.CacheListenerAdapter;

import java.util.Map;
import java.util.Properties;

public class CreateRegionCacheListener 
             extends CacheListenerAdapter<String,RegionAttributes>
             implements Declarable {

  private Cache cache;

  public CreateRegionCacheListener() {
    this.cache = CacheFactory.getAnyInstance();
  }

  public void afterCreate(EntryEvent<String,RegionAttributes> event) {
    createRegion(event.getKey(), event.getNewValue());
  }

  public void afterRegionCreate(RegionEvent<String,RegionAttributes> event) {
    Region<String,RegionAttributes> region = event.getRegion();
    for (Map.Entry<String,RegionAttributes> entry : region.entrySet()) {
      createRegion(entry.getKey(), entry.getValue());
    }
  }

  private void createRegion(String regionName, RegionAttributes attributes) {
    if (this.cache.getLogger().fineEnabled()) {
      this.cache.getLogger().fine(
                             "CreateRegionCacheListener creating region named: "
                             + regionName + " with attributes: " + attributes);
    }
    try {
      Region region = this.cache.createRegionFactory(attributes)
        .create(regionName);
      if (this.cache.getLogger().fineEnabled()) {
        this.cache.getLogger().fine("CreateRegionCacheListener created: "
                               + region);
      }
      System.out.println("CreateRegionCacheListener created: " + region);
    } catch (RegionExistsException e) {/* ignore */}
  }

  public void init(Properties p) {
  }
}
```



## Partitioned Regions

In addition to basic region management, partitioned regions include options for high availability, data location control, and data balancing across the cluster.

- **Understanding Partitioning**

  To use partitioned regions, you should understand how they work and your options for managing them.

- **Configuring Partitioned Regions**

  Plan the configuration and ongoing management of your partitioned region for host and accessor members and configure the regions for startup.

- **Configuring the Number of Buckets for a Partitioned Region**

  Decide how many buckets to assign to your partitioned region and set the configuration accordingly.

- **Custom-Partitioning and Colocating Data**

  You can customize how Apache Geode groups your partitioned region data with custom partitioning and data colocation.

- **Configuring High Availability for Partitioned Regions**

  By default, Apache Geode stores only a single copy of your partitioned region data among the region’s data stores. You can configure Geode to maintain redundant copies of your partitioned region data for high availability.

- **Configuring Single-Hop Client Access to Server-Partitioned Regions**

  Single-hop data access enables the client pool to track where a partitioned region’s data is hosted in the servers. To access a single entry, the client directly contacts the server that hosts the key, in a single hop.

- **Rebalancing Partitioned Region Data**

  In a cluster with minimal contention to the concurrent threads reading or updating from the members, you can use rebalancing to dynamically increase or decrease your data and processing capacity.

- **Checking Redundancy in Partitioned Regions**

  Under some circumstances, it can be important to verify that your partitioned region data is redundant and that upon member restart, redundancy has been recovered properly across partitioned region members.

- **Moving Partitioned Region Data to Another Member**

  You can use the `PartitionRegionHelper` `moveBucketByKey` and `moveData` methods to explicitly move partitioned region data from one member to another.



### Understanding Partitioning

To use partitioned regions, you should understand how they work and your options for managing them.

During operation, a partitioned region looks like one large virtual region, with the same logical view held by all of the members where the region is defined. ![img](assets/how_partitioning_works_1.svg)

For each member where you define the region, you can choose how much space to allow for region data storage, including no local storage at all. The member can access all region data regardless of how much is stored locally. ![img](assets/how_partitioning_works_2.svg)

A cluster can have multiple partitioned regions, and it can mix partitioned regions with distributed regions and local regions. The usual requirement for unique region names, except for regions with local scope, still applies. A single member can host multiple partitioned regions.

#### Data Partitioning

Geode automatically determines the physical location of data in the members that host a partitioned region’s data. Geode breaks partitioned region data into units of storage known as buckets and stores each bucket in a region host member. Buckets are distributed in accordance to the member’s region attribute settings.

When an entry is created, it is assigned to a bucket. Keys are grouped together in a bucket and always remain there. If the configuration allows, the buckets may be moved between members to balance the load.

You must run the data stores needed to accommodate storage for the partitioned region’s buckets. You can start new data stores on the fly. When a new data store creates the region, it takes responsibility for as many buckets as allowed by the partitioned region and member configuration.

You can customize how Geode groups your partitioned region data with custom partitioning and data colocation.

#### Partitioned Region Operation

A partitioned region operates much like a non-partitioned region with distributed scope. Most of the standard `Region` methods are available, although some methods that are normally local operations become distributed operations, because they work on the partitioned region as a whole instead of the local cache. For example, a `put` or `create` into a partitioned region may not actually be stored into the cache of the member that called the operation. The retrieval of any entry requires no more than one hop between members.

Partitioned regions support the client/server model, just like other regions. If you need to connect dozens of clients to a single partitioned region, using servers greatly improves performance.

#### Additional Information About Partitioned Regions

Keep the following in mind about partitioned regions:

- Partitioned regions never run asynchronously. Operations in partitioned regions always wait for acknowledgement from the caches containing the original data entry and any redundant copies.
- A partitioned region needs a cache loader in every region data store (`local-max-memory` > 0).
- Geode distributes the data buckets as evenly as possible across all members storing the partitioned region data, within the limits of any custom partitioning or data colocation that you use. The number of buckets allotted for the partitioned region determines the granularity of data storage and thus how evenly the data can be distributed. The number of buckets is a total for the entire region across the cluster.
- In rebalancing data for the region, Geode moves buckets, but does not move data around inside the buckets.
- You can query partitioned regions, but there are certain limitations. See [Querying Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/querying_basics/querying_partitioned_regions.html#querying_partitioned_regions) for more information.



### Configuring Partitioned Regions

Plan the configuration and ongoing management of your partitioned region for host and accessor members and configure the regions for startup.

Before you begin, understand [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).

1. Start your region configuration using one of the `PARTITION` region shortcut settings. See [Region Shortcuts and Custom Named Region Attributes](https://geode.apache.org/docs/guide/17/basic_config/data_regions/region_shortcuts.html).
2. If you need high availability for your partitioned region, configure for that. See [Configure High Availability for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
3. Estimate the amount of space needed for the region. If you use redundancy, this is the max for all primary and secondary copies stored in the member. For example, with redundancy of one, each region data entry requires twice the space than with no redundancy, because the entry is stored twice. See [Memory Requirements for Cached Data](https://geode.apache.org/docs/guide/17/reference/topics/memory_requirements_for_cache_data.html#calculating_memory_requirements).
4. Configure the total number of buckets for the region. This number must be the same for colocated regions. See [Configuring the Number of Buckets for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_bucket_for_pr.html#configuring_total_buckets).
5. Configure your members’ data storage and data loading for the region:
   1. You can have members with no local data storage and members with varying amounts of storage. Determine the max memory available in your different member types for this region. These will be set in the `partition-attributes` `local-max-memory`. This is the only setting in `partition-attributes` that can vary between members. Use these max values and your estimates for region memory requirements to help you figure how many members to start out with for the region.
   2. For members that store data for the region (`local-max-memory` greater than 0), define a data loader. See [Implement a Data Loader](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/implementing_data_loaders.html#implementing_data_loaders).
   3. If you have members with no local data storage (`local-max-memory` set to 0), review your system startup/shutdown procedures. Make sure there is always at least one member with local data storage running when any members with no storage are running.
6. If you want to custom partition the data in your region or colocate data between multiple regions, code and configure accordingly. See [Understanding Custom Partitioning and Data Colocation](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/custom_partitioning_and_data_colocation.html#custom_partitioning_and_data_colocation).
7. Plan your partition rebalancing strategy and configure and program for that. See [Rebalancing Partitioned Region Data](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data).

**Note:** To configure a partitioned region using gfsh, see [gfsh Command Help](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/gfsh_command_index.html#concept_C291647179C5407A876CC7FCF91CF756).



### Configuring the Number of Buckets for a Partitioned Region

Decide how many buckets to assign to your partitioned region and set the configuration accordingly.

The total number of buckets for the partitioned region determines the granularity of data storage and thus how evenly the data can be distributed. Geode distributes the buckets as evenly as possible across the data stores. The number of buckets is fixed after region creation.

The partition attribute `total-num-buckets` sets the number for the entire partitioned region across all participating members. Set it using one of the following:

- XML:

  ```
  <region name="PR1"> 
    <region-attributes refid="PARTITION"> 
      <partition-attributes total-num-buckets="7"/> 
    </region-attributes> 
  </region> 
  ```

- Java:

  ```
  RegionFactory rf = 
      cache.createRegionFactory(RegionShortcut.PARTITION);
  rf.setPartitionAttributes(new PartitionAttributesFactory().setTotalNumBuckets(7).create());
  custRegion = rf.create("customer");
  ```

- gfsh:

  Use the –total-num-buckets parameter of the `create region` command. For example:

  ```
  gfsh>create region --name="PR1" --type=PARTITION --total-num-buckets=7
  ```

#### Calculate the Total Number of Buckets for a Partitioned Region

Follow these guidelines to calculate the total number of buckets for the partitioned region:

- Use a prime number. This provides the most even distribution.
- Make it at least four times as large as the number of data stores you expect to have for the region. The larger the ratio of buckets to data stores, the more evenly the load can be spread across the members. Note that there is a trade-off between load balancing and overhead, however. Managing a bucket introduces significant overhead, especially with higher levels of redundancy.

You are trying to avoid the situation where some members have significantly more data entries than others. For example, compare the next two figures. This figure shows a region with three data stores and seven buckets. If all the entries are accessed at about the same rate, this configuration creates a hot spot in member M3, which has about fifty percent more data than the other data stores. M3 is likely to be a slow receiver and potential point of failure.

![img](assets/partitioned_data_buckets_1.svg)

Configuring more buckets gives you fewer entries in a bucket and a more balanced data distribution. This figure uses the same data as before but increases the number of buckets to 13. Now the data entries are distributed more evenly.

![img](assets/partitioned_data_buckets_2.svg)



### Custom-Partitioning and Colocating Data

You can customize how Apache Geode groups your partitioned region data with custom partitioning and data colocation.

- **Understanding Custom Partitioning and Data Colocation**

  Custom partitioning and data colocation can be used separately or in conjunction with one another.

- **Standard Custom Partitioning**

  By default, Geode partitions each data entry into a bucket using a hashing policy on the key. Additionally, the physical location of the key-value pair is abstracted away from the application. You can change these policies for a partitioned region by providing a standard partition resolver that maps entries to a set of buckets called a partition.

- **Fixed Custom Partitioning**

  By default, Geode partitions each data entry into a bucket using a hashing policy on the key. Additionally, the physical location of the key-value pair is abstracted away from the application. You can change these policies for a partitioned region by providing a fixed partition resolver that not only maps entries to a set of buckets called a partition, but also specifies which members host which data buckets.

- **Colocate Data from Different Partitioned Regions**

  By default, Geode allocates the data locations for a partitioned region independent of the data locations for any other partitioned region. You can change this policy for any group of partitioned regions, so that cross-region, related data is all hosted by the same member. Colocation is required for some operations, and it increases performance for others by reducing the number of data accesses to entries that are hosted on other cluster members.



#### Understanding Custom Partitioning and Data Colocation

Custom partitioning and data colocation can be used separately or in conjunction with one another.

##### Custom Partitioning

Use custom partitioning to group like entries into region buckets within a region. By default, Geode assigns new entries to buckets based on the entry key’s hash code. With custom partitioning, you can assign your entries to buckets in whatever way you want.

You can generally get better performance if you use custom partitioning to group similar data within a region. For example, a query run on all accounts created in January runs faster if all January account data is hosted by a single member. Grouping all data for a single customer can improve performance of data operations that work on customer data. Data aware function execution also takes advantage of custom partitioning.

With custom partitioning, you have two choices:

- **Standard custom partitioning**. With standard custom partitioning, you group entries into buckets, but you do not specify where the buckets reside. Geode always keeps the entries in the buckets you have specified, but may move the buckets around for load balancing. See [Standard Custom Partitioning](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/standard_custom_partitioning.html) for implementation and configuration details.

- **Fixed custom partitioning**. With fixed custom partitioning, you specify the exact member where each region entry resides. You assign an entry to a partition and then to a bucket within that partition. You name specific members as primary and secondary hosts of each partition.

  This gives you complete control over the locations of your primary and any secondary buckets for the region. This can be useful when you want to store specific data on specific physical machines or when you need to keep data close to certain hardware elements.

  Fixed partitioning has these requirements and caveats:

  - Geode cannot rebalance fixed partition region data, because it cannot move the buckets around among the host members. You must carefully consider your expected data loads for the partitions you create.
  - With fixed partitioning, the region configuration is different between host members. Each member identifies the named partitions it hosts, and whether it is hosting the primary copy or a secondary copy. You then program a fixed-partition resolver to return the partition id, so the entry is placed on the right members. Only one member can be primary for a particular partition name, and that member cannot be the partition’s secondary.

  See [Fixed Custom Partitioning](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/fixed_custom_partitioning.html) for implementation and configuration details.

##### Data Colocation Between Regions

With data colocation, Geode stores entries that are related across multiple data regions in a single member. Geode does this by storing all of the regions’ buckets with the same ID together in the same member. During rebalancing operations, Geode moves these bucket groups together or not at all.

So, for example, if you have one region with customer contact information and another region with customer orders, you can use colocation to keep all contact information and all orders for a single customer in a single member. This way, any operation done for a single customer uses the cache of only a single member.

This figure shows two regions with data colocation where the data is partitioned by customer type.

![img](assets/colocated_partitioned_regions.svg)

Data colocation requires the same data partitioning mechanism for all of the colocated regions. You can use the default partitioning provided by Geode or any of the custom partitioning strategies.

You must use the same high availability settings across your colocated regions.

See [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html) for implementation and configuration details.



#### Standard Custom Partitioning

By default, Geode partitions each data entry into a bucket using a hashing policy on the key. Additionally, the physical location of the key-value pair is abstracted away from the application. You can change these policies for a partitioned region by providing a standard custom partition resolver that maps entries in a custom manner.

**Note:** If you are both colocating region data and custom partitioning, all colocated regions must use the same custom partitioning mechanism. See [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data).

To custom-partition your region data, follow two steps:

- implement the interface
- configure the regions

**Implementing Standard Custom Partitioning**

- Implement the `org.apache.geode.cache.PartitionResolver` interface in one of the following ways, listed here in the search order used by Geode:

  - **Using a custom class**. Implement the `PartitionResolver` within your class, and then specify your class as the partition resolver during region creation.
  - **Using the key’s class**. Have the entry key’s class implement the `PartitionResolver` interface.
  - **Using the callback argument’s class**. Have the class that implements your callback argument implement the `PartitionResolver` interface. When using this implementation, any and all `Region` operations must be those that specify the callback argument.

- Implement the resolver’s `getName`, `init`, and `close` methods.

  A simple implementation of `getName` is

  ```
  return getClass().getName();
  ```

  The `init` method does any initialization steps upon cache start that relate to the partition resolver’s task. This method is only invoked when using a custom class that is configured by `gfsh` or by XML (in a `cache.xml` file).

  The `close` method accomplishes any clean up that must be accomplished before a cache close completes. For example, `close` might close files or connections that the partition resolver opened.

- Implement the resolver’s `getRoutingObject` method to return the routing object for each entry. A hash of that returned routing object determines the bucket. Therefore, `getRoutingObject`should return an object that, when run through its `hashCode`, directs grouped objects to the desired bucket.

  **Note:** Only the key, as returned by `getKey`, should be used by `getRoutingObject`. Do not use the value associated with the key or any additional metadata in the implementation of `getRoutingObject`. Do not use `getOperation` or `getValue`.

**Implementing the String Prefix Partition Resolver**

Geode provides an implementation of a string-based partition resolver in `org.apache.geode.cache.util.StringPrefixPartitionResolver`. This resolver does not require any further implementation. It groups entries into buckets based on a portion of the key. All keys must be strings, and each key must include a ’|’ character, which delimits the string. The substring that precedes the ’|’ delimiter within the key will be returned by `getRoutingObject`.

**Configuring the Partition Resolver Region Attribute**

Configure the region so Geode finds your resolver for all region operations.

- **Custom class**. Use one of these methods to specify the partition resolver region attribute:

  **gfsh:**

  Add the option `--partition-resolver` to the `gfsh create region` command, specifying the package and class name of the standard custom partition resolver.

  **gfsh for the String Prefix Partition Resolver:**

  Add the option `--partition-resolver=org.apache.geode.cache.util.StringPrefixPartitionResolver`to the `gfsh create region` command.

  **Java API:**

  ```
  PartitionResolver resolver = new TradesPartitionResolver();
  PartitionAttributes attrs = 
      new PartitionAttributesFactory()
      .setPartitionResolver(resolver).create();
  
  Cache c = new CacheFactory().create();
  
  Region r = c.createRegionFactory()
      .setPartitionAttributes(attrs)
      .create("trades");
  ```

  **Java API for the String Prefix Partition Resolver:**

  ```
  PartitionAttributes attrs = 
      new PartitionAttributesFactory()
      .setPartitionResolver(new StringPrefixPartitionResolver()).create();
  
  Cache c = new CacheFactory().create();
  
  Region r = c.createRegionFactory()
      .setPartitionAttributes(attrs)
      .create("customers");
  ```

  **XML:**

  ```
  <region name="trades">
      <region-attributes>
          <partition-attributes>
              <partition-resolver> 
                  <class-name>myPackage.TradesPartitionResolver
                  </class-name>
              </partition-resolver>
          <partition-attributes>
      </region-attributes>
  </region>
  ```

  **XML for the String Prefix Partition Resolver:**

  ```
  <region name="customers">
      <region-attributes>
          <partition-attributes>
              <partition-resolver> 
                  <class-name>org.apache.geode.cache.util.StringPrefixPartitionResolver
                  </class-name>
              </partition-resolver>
          <partition-attributes>
      </region-attributes>
  </region>
  ```

- If your colocated data is in a server system, add the `PartitionResolver` implementation class to the `CLASSPATH` of your Java clients. For Java single-hop access to work, the resolver class needs to have a zero-argument constructor, and the resolver class must not have any state; the `init`method is included in this restriction.



#### Fixed Custom Partitioning

By default, Geode partitions each data entry into a bucket using a hashing policy on the key. Additionally, the physical location of the key-value pair is abstracted away from the application. You can change these policies for a partitioned region by providing a fixed custom partition resolver that not only maps entries to a set of buckets called a partition, but also specifies which members host which data buckets.

**Note:** If you are both colocating region data and custom partitioning, all colocated regions must use the same custom partitioning mechanism. See [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data).

To custom-partition your region data, follow two steps:

- implement the interface
- configure the regions

These steps differ based on which partition resolver is used.

**Implementing Fixed Custom Partitioning**

- Implement the `org.apache.geode.cache.FixedPartitionResolver` interface within one of the following locations, listed here in the search order used by Geode:

  - **Custom class**. Specify this class as the partition resolver during region creation.
  - **Entry key**. For keys implemented as objects, define the interface for the key’s class.
  - **Within the cache callback class**. Implement the interface within a cache callback’s class. When using this implementation, any and all `Region` operations must be those that specify the callback as a parameter.

- Implement the resolver’s `getName`, `init`, and `close` methods.

  A simple implementation of `getName` is

  ```
  return getClass().getName();
  ```

  The `init` method does any initialization steps upon cache start that relate to the partition resolver’s task.

  The `close` method accomplishes any clean up that must be accomplished before a cache close completes. For example, `close` might close files or connections that the partition resolver opened.

- Implement the resolver’s `getRoutingObject` method to return the routing object for each entry. A hash of that returned routing object determines the bucket within a partition.

  This method can be empty for fixed partitioning where there is only one bucket per partition. That implementation assigns partitions to servers such that the application has full control of grouping entries on servers.

  **Note:** Only fields on the key should be used when creating the routing object. Do not use the value or additional metadata for this purpose.

- Implement the `getPartitionName` method to return the name of the partition for each entry, based on where you want the entries to reside. All entries within a partition will be on a single server.

  This example places the data based on date, with a different partition name for each quarter-year and a different routing object for each month.

  ```
  /**
   * Returns one of four different partition names
   * (Q1, Q2, Q3, Q4) depending on the entry's date
   */
  class QuarterFixedPartitionResolver implements
      FixedPartitionResolver<String, String> {
  
    @Override
    public String getPartitionName(EntryOperation<String, String> opDetails,
        Set<String> targetPartitions) {
  
       Date date = (Date)opDetails.getKey();
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       int month = cal.get(Calendar.MONTH);
       if (month >= 0 && month < 3) {
          if (targetPartitions.contains("Q1")) return "Q1";
       }
       else if (month >= 3 && month < 6) {
          if (targetPartitions.contains("Q2")) return "Q2";
       }
       else if (month >= 6 && month < 9) {
          if (targetPartitions.contains("Q3")) return "Q3";
       }
       else if (month >= 9 && month < 12) {
          if (targetPartitions.contains("Q4")) return "Q4";
       }
       return "Invalid Quarter";
    }
  
    @Override
    public String getName() {
       return "QuarterFixedPartitionResolver";
    }
  
    @Override
    public Serializable getRoutingObject(EntryOperation<String, String> opDetails) {
       Date date = (Date)opDetails.getKey();
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       int month = cal.get(Calendar.MONTH);
       return month;
    }
  
    @Override
    public void close() {
    }
  }
  ```

**Configuring Fixed Custom Partitioning**

- Set the fixed-partition attributes for each member.

  These attributes define the data stored for the region by the member and must be different for different members. See `org.apache.geode.cache.FixedPartitionAttributes` for definitions of the attributes. Define each `partition-name` in your data-host members for the region. For each partition name, in the member you want to host the primary copy, define it with `is-primary` set to `true`. In every member you want to host the secondary copy, define it with `is-primary` set to `false` (the default). The number of secondaries must match the number of redundant copies you have defined for the region. See [Configure High Availability for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).

  **Note:** Buckets for a partition are hosted only by the members that have defined the partition name in their `FixedPartitionAttributes`.

  These examples set the partition attributes for a member to be the primary host for the “Q1” partition data and a secondary host for “Q3” partition data.

  - XML:

    ```
    <cache>
       <region name="Trades">
          <region-attributes>
             <partition-attributes redundant-copies="1">
               <partition-resolver>
                  <class-name>myPackage.QuarterFixedPartitionResolver</class-name>
               </partition-resolver>
               <fixed-partition-attributes partition-name="Q1" is-primary="true"/>
               <fixed-partition-attributes partition-name="Q3" is-primary="false"
                    num-buckets="6"/>
             </partition-attributes> 
          </region-attributes>
       </region>
    </cache>
    ```

  - Java:

    ```
    FixedPartitionAttribute fpa1 = FixedPartitionAttributes
         .createFixedPartition("Q1", true);
    FixedPartitionAttribute fpa3 = FixedPartitionAttributes
         .createFixedPartition("Q3", false, 6);
    
    PartitionAttributesFactory paf = new PartitionAttributesFactory()
         .setPartitionResolver(new QuarterFixedPartitionResolver())
         .setTotalNumBuckets(12)
         .setRedundantCopies(2)
         .addFixedPartitionAttribute(fpa1)
         .addFixedPartitionAttribute(fpa3);
    
    Cache c = new CacheFactory().create();
    
    Region r = c.createRegionFactory()
        .setPartitionAttributes(paf.create())
        .create("Trades");
    ```

  - gfsh:

    You cannot specify a fixed partition resolver using gfsh.

- If your colocated data is in a server system, add the class that implements the `FixedPartitionResolver` interface to the `CLASSPATH` of your Java clients. For Java single-hop access to work, the resolver class needs to have a zero-argument constructor, and the resolver class must not have any state; the `init` method is included in this restriction.



#### Colocate Data from Different Partitioned Regions

By default, Geode allocates the data locations for a partitioned region independent of the data locations for any other partitioned region. You can change this policy for any group of partitioned regions, so that cross-region, related data is all hosted by the same member. Colocation is required for some operations, and it increases performance for others by reducing the number of data accesses to entries that are hosted on other cluster members.



Data colocation between partitioned regions generally improves the performance of data-intensive operations. You can reduce network hops for iterative operations on related data sets. Compute-heavy applications that are data-intensive can significantly increase overall throughput. For example, a query run on a patient’s health records, insurance, and billing information is more efficient if all data is grouped in a single member. Similarly, a financial risk analytical application runs faster if all trades, risk sensitivities, and reference data associated with a single instrument are together.

**Procedure**

1. Identify one region as the central region, with which data in the other regions is explicitly colocated. If you use persistence for any of the regions, you must persist the central region.

   1. Create the central region before you create the others, either in the `cache.xml` or your code. Regions in the XML are created before regions in the code, so if you create any of your colocated regions in the XML, you must create the central region in the XML before the others. Geode will verify its existence when the others are created and return `IllegalStateException` if the central region is not there. Do not add any colocation specifications to this central region.

   2. For all other regions, in the region partition attributes, provide the central region’s name in the `colocated-with` attribute. Use one of these methods:

      - XML:

        ```
        <cache> 
            <region name="trades"> 
                <region-attributes> 
                    <partition-attributes>  
                        ...
                    <partition-attributes> 
                </region-attributes> 
            </region> 
            <region name="trade_history"> 
                <region-attributes> 
                    <partition-attributes colocated-with="trades">   
                        ...
                    <partition-attributes> 
                </region-attributes> 
            </region> 
        </cache> 
        ```

      - Java:

        ```
        PartitionAttributes attrs = ...
        Region trades = new RegionFactory().setPartitionAttributes(attrs)
            .create("trades");
        ...
        attrs = new PartitionAttributesFactory().setColocatedWith(trades.getFullPath())
            .create();
        Region trade_history = new RegionFactory().setPartitionAttributes(attrs)
            .create("trade_history");
        ```

      - gfsh:

        ```
        gfsh>create region --name="trades" type=PARTITION
        gfsh> create region --name="trade_history" --colocated-with="trades"
        ```

2. For each of the colocated regions, use the same values for these partition attributes related to bucket management:

   - `recovery-delay`
   - `redundant-copies`
   - `startup-recovery-delay`
   - `total-num-buckets`

3. If you custom partition your region data, specify the custom resolver for all colocated regions. This example uses the same partition resolver for both regions:

   - XML:

     ```
     <cache> 
         <region name="trades"> 
             <region-attributes> 
                 <partition-attributes>  
                 <partition-resolver name="TradesPartitionResolver"> 
                     <class-name>myPackage.TradesPartitionResolver
                     </class-name>
                 <partition-attributes> 
             </region-attributes> 
         </region> 
         <region name="trade_history"> 
             <region-attributes> 
                 <partition-attributes colocated-with="trades">   
                 <partition-resolver name="TradesPartitionResolver"> 
                     <class-name>myPackage.TradesPartitionResolver
                     </class-name>
                 <partition-attributes> 
             </region-attributes> 
         </region> 
     </cache> 
     ```

   - Java:

     ```
     PartitionResolver resolver = new TradesPartitionResolver();
     PartitionAttributes attrs = 
         new PartitionAttributesFactory()
         .setPartitionResolver(resolver).create();
     Region trades = new RegionFactory().setPartitionAttributes(attrs)
         .create("trades");
     attrs = new PartitionAttributesFactory()
         .setColocatedWith(trades.getFullPath()).setPartitionResolver(resolver)
         .create();
     Region trade_history = new RegionFactory().setPartitionAttributes(attrs)
         .create("trade_history");
     ```

   - gfsh:

     Specify a partition resolver as described in the configuration section of [Custom-Partition Your Region Data](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/using_custom_partition_resolvers.html).

4. If you want to persist data in the colocated regions, persist the central region and then persist the other regions as needed. Use the same disk store for all of the colocated regions that you persist.



### Configuring High Availability for Partitioned Regions

By default, Apache Geode stores only a single copy of your partitioned region data among the region’s data stores. You can configure Geode to maintain redundant copies of your partitioned region data for high availability.

- **Understanding High Availability for Partitioned Regions**

  With high availability, each member that hosts data for the partitioned region gets some primary copies and some redundant (secondary) copies.

- **Configure High Availability for a Partitioned Region**

  Configure in-memory high availability for your partitioned region. Set other high-availability options, like redundancy zones and redundancy recovery strategies.



#### Understanding High Availability for Partitioned Regions

With high availability, each member that hosts data for the partitioned region gets some primary copies and some redundant (secondary) copies.



With redundancy, if one member fails, operations continue on the partitioned region with no interruption of service:

- If the member hosting the primary copy is lost, Geode makes a secondary copy the primary. This might cause a temporary loss of redundancy, but not a loss of data.
- Whenever there are not enough secondary copies to satisfy redundancy, the system works to recover redundancy by assigning another member as secondary and copying the data to it.

**Note:** You can still lose cached data when you are using redundancy if enough members go down in a short enough time span.

You can configure how the system works to recover redundancy when it is not satisfied. You can configure recovery to take place immediately or, if you want to give replacement members a chance to start up, you can configure a wait period. Redundancy recovery is also automatically attempted during any partitioned data rebalancing operation. Use the `gemfire.MAX_PARALLEL_BUCKET_RECOVERIES` system property to configure the maximum number of buckets that are recovered in parallel. By default, up to 8 buckets are recovered in parallel any time the system attempts to recover redundancy.

Without redundancy, the loss of any of the region’s data stores causes the loss of some of the region’s cached data. Generally, you should not use redundancy when your applications can directly read from another data source, or when write performance is more important than read performance.

##### Controlling Where Your Primaries and Secondaries Reside

By default, Geode places your primary and secondary data copies for you, avoiding placement of two copies on the same physical machine. If there are not enough machines to keep different copies separate, Geode places copies on the same physical machine. You can change this behavior, so Geode only places copies on separate machines.

You can also control which members store your primary and secondary data copies. Geode provides two options:

- **Fixed custom partitioning**. This option is set for the region. Fixed partitioning gives you absolute control over where your region data is hosted. With fixed partitioning, you provide Geode with the code that specifies the bucket and data store for each data entry in the region. When you use this option with redundancy, you specify the primary and secondary data stores. Fixed partitioning does not participate in rebalancing because all bucket locations are fixed by you.
- **Redundancy zones**. This option is set at the member level. Redundancy zones let you separate primary and secondary copies by member groups, or zones. You assign each data host to a zone. Then Geode places redundant copies in different redundancy zones, the same as it places redundant copies on different physical machines. You can use this to split data copies across different machine racks or networks, This option allows you to add members on the fly and use rebalancing to redistribute the data load, with redundant data maintained in separate zones. When you use redundancy zones, Geode will not place two copies of the data in the same zone, so make sure you have enough zones.

##### Running Processes in Virtual Machines

By default, Geode stores redundant copies on different machines. When you run your processes in virtual machines, the normal view of the machine becomes the VM and not the physical machine. If you run multiple VMs on the same physical machine, you could end up storing partitioned region primary buckets in separate VMs, but on the same physical machine as your secondaries. If the physical machine fails, you can lose data. When you run in VMs, you can configure Geode to identify the physical machine and store redundant copies on different physical machines.

##### Reads and Writes in Highly-Available Partitioned Regions

Geode treats reads and writes differently in highly-available partitioned regions than in other regions because the data is available in multiple members:

- Write operations (like `put` and `create`) go to the primary for the data keys and then are distributed synchronously to the redundant copies. Events are sent to the members configured with `subscription-attributes` `interest-policy` set to `all`.
- Read operations go to any member holding a copy of the data, with the local cache favored, so a read intensive system can scale much better and handle higher loads.

In this figure, M1 is reading W, Y, and Z. It gets W directly from its local copy. Since it doesn’t have a local copy of Y or Z, it goes to a cache that does, picking the source cache at random.

![img](assets/partitioned_data_HA.svg)



#### Configure High Availability for a Partitioned Region

Configure in-memory high availability for your partitioned region. Set other high-availability options, like redundancy zones and redundancy recovery strategies.

Here are the main steps for configuring high availability for a partitioned region. See later sections for details.

1. Set the number of redundant copies the system should maintain of the region data. See [Set the Number of Redundant Copies](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_pr_redundancy.html#set_pr_redundancy).
2. (Optional) If you want to group your data store members into redundancy zones, configure them accordingly. See [Configure Redundancy Zones for Members](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_redundancy_zones.html#set_redundancy_zones).
3. (Optional) If you want Geode to only place redundant copies on different physical machines, configure for that. See [Set Enforce Unique Host](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_enforce_unique_host.html#set_pr_redundancy).
4. Decide how to manage redundancy recovery and change Geode’s default behavior as needed.
   - **After a member crashes**. If you want automatic redundancy recovery, change the configuration for that. See [Configure Member Crash Redundancy Recovery for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_crash_redundancy_recovery.html#set_crash_redundancy_recovery).
   - **After a member joins**. If you do *not* want immediate, automatic redundancy recovery, change the configuration for that. See [Configure Member Join Redundancy Recovery for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_join_redundancy_recovery.html#set_join_redundancy_recovery).
5. Decide how many buckets Geode should attempt to recover in parallel when performing redundancy recovery. By default, the system recovers up to 8 buckets in parallel. Use the `gemfire.MAX_PARALLEL_BUCKET_RECOVERIES` system property to increase or decrease the maximum number of buckets to recover in parallel any time redundancy recovery is performed.
6. For all but fixed partitioned regions, review the points at which you kick off rebalancing. Redundancy recovery is done automatically at the start of any rebalancing. This is most important if you run with no automated recovery after member crashes or joins. See [Rebalancing Partitioned Region Data](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data).

During runtime, you can add capacity by adding new members for the region. For regions that do not use fixed partitioning, you can also kick off a rebalancing operation to spread the region buckets among all members.

- **Set the Number of Redundant Copies**

  Configure in-memory high availability for your partitioned region by specifying the number of secondary copies you want to maintain in the region’s data stores.

- **Configure Redundancy Zones for Members**

  Group members into redundancy zones so Geode will separate redundant data copies into different zones.

- **Set Enforce Unique Host**

  Configure Geode to use only unique physical machines for redundant copies of partitioned region data.

- **Configure Member Crash Redundancy Recovery for a Partitioned Region**

  Configure whether and how redundancy is recovered in a partition region after a member crashes.

- **Configure Member Join Redundancy Recovery for a Partitioned Region**

  Configure whether and how redundancy is recovered in a partition region after a member joins.



##### Set the Number of Redundant Copies

Configure in-memory high availability for your partitioned region by specifying the number of secondary copies you want to maintain in the region’s data stores.

Specify the number of redundant copies you want for your partitioned region data in the partition attribute `redundant-copies` setting. The default setting is 0.

For example:

- XML:

  ```
  <region name="PR1"> 
    <region-attributes refid="PARTITION"> 
      <partition-attributes redundant-copies="1"/> 
    </region-attributes> 
  </region> 
  ```

- Java:

  ```
  PartitionAttributes pa = 
      new PartitionAttributesFactory().setRedundantCopies(1).create(); 
  ```

- gfsh:

  ```
  gfsh>create region --name="PR1" --type=PARTITION --redundant-copies=1
  ```


##### Configure Redundancy Zones for Members

Group members into redundancy zones so Geode will separate redundant data copies into different zones.

Understand how to set a member’s `gemfire.properties` settings. See [Reference](https://geode.apache.org/docs/guide/17/reference/book_intro.html#reference).

Group your partition region hosts into redundancy zones with the `gemfire.properties` setting `redundancy-zone`.

For example, if you had redundancy set to 1, so you have one primary and one secondary copy of each data entry, you could split primary and secondary data copies between two machine racks by defining one redundancy zone for each rack. To do this, you set this zone in the `gemfire.properties` for all members that run on one rack: `pre redundancy-zone=rack1`

You would set this zone `gemfire.properties` for all members on the other rack: `pre redundancy-zone=rack2`

Each secondary copy would be hosted on the rack opposite the rack where its primary copy is hosted.



##### Set Enforce Unique Host

Configure Geode to use only unique physical machines for redundant copies of partitioned region data.

Understand how to set a member’s `gemfire.properties` settings. See [Reference](https://geode.apache.org/docs/guide/17/reference/book_intro.html#reference).

Configure your members so Geode always uses different physical machines for redundant copies of partitioned region data using the `gemfire.properties` setting `enforce-unique-host`. The default for this setting is false.

Example:

```
enforce-unique-host=true
```



##### Configure Member Crash Redundancy Recovery for a Partitioned Region

Configure whether and how redundancy is recovered in a partition region after a member crashes.

Use the partition attribute `recovery-delay` to specify member crash redundancy recovery.

| recovery-delay partition attribute | Effect following a member failure                            |
| ---------------------------------- | ------------------------------------------------------------ |
| -1                                 | No automatic recovery of redundancy following a member failure. This is the default. |
| long greater than or equal to 0    | Number of milliseconds to wait after a member failure before recovering redundancy. |

By default, redundancy is not recovered after a member crashes. If you expect to quickly restart most crashed members, combining this default setting with member join redundancy recovery can help you avoid unnecessary data shuffling while members are down. By waiting for lost members to rejoin, redundancy recovery is done using the newly started members and partitioning is better balanced with less processing.

Set crash redundancy recovery using one of the following:

- XML:

  ```
  // Give a crashed member 10 seconds to restart 
  // before recovering redundancy
  <region name="PR1"> 
    <region-attributes refid="PARTITION"> 
      <partition-attributes recovery-delay="10000"/> 
    </region-attributes> 
  </region> 
  ```

- Java:

  ```
  PartitionAttributes pa = new PartitionAttributesFactory().setRecoveryDelay(10000).create(); 
  ```

- gfsh:

  ```
  gfsh>create region --name="PR1" type=PARTITION --recovery-delay=10000
  ```



##### Configure Member Join Redundancy Recovery for a Partitioned Region

This section covers configuring whether and how redundancy is recovered in a partitioned region, after a member joins.

Use the partition attribute `startup-recovery-delay` to specify member join redundancy recovery.

| value of `startup-recovery-delay` | Effect following a member join                               |
| --------------------------------- | ------------------------------------------------------------ |
| -1                                | No automatic recovery of redundancy after a new member comes online. With this value and the default `recovery-delay` setting, redundancy recovery is only achieved by a rebalance operation. |
| long >= 0                         | Number of milliseconds to wait after a member joins before recovering redundancy. The default is 0 (zero), which causes immediate redundancy recovery whenever a member that hosts the partitioned region joins. |

Setting `startup-recovery-delay` to a value higher than the default of 0 allows multiple new members to join before redundancy recovery begins. With the multiple members present during recovery, the system will spread redundancy recovery among them. With no delay, if multiple members are started in close succession, the system may choose only the first member started for most or all of the redundancy recovery.

**Note:** Satisfying redundancy is not the same as adding capacity. If redundancy is satisfied, new members do not take buckets until the invocation of a rebalance operation.

The parallel recovery implementation recovers quickly. For this reason, it is even more important to configure `startup-recovery-delay` to an appropriate value when restarting multiple members at the same time. Set `startup-recovery-delay` to a value that ensures all members are up and available *before* redundancy recovery kicks in.

Set join redundancy recovery using one of the following:

- XML:

  ```
  // Wait 5 seconds after a new member joins before  
  // recovering redundancy
  <region name="PR1"> 
    <region-attributes refid="PARTITION"> 
      <partition-attributes startup-recovery-delay="5000"/> 
    </region-attributes> 
  </region> 
  ```

- Java:

  ```
  PartitionAttributes pa = new PartitionAttributesFactory().setStartupRecoveryDelay(5000).create(); 
  ```

- gfsh:

  ```
  gfsh>create region --name="PR1" --type=PARTITION --startup-recovery-delay=5000
  ```



### Configuring Single-Hop Client Access to Server-Partitioned Regions

Single-hop data access enables the client pool to track where a partitioned region’s data is hosted in the servers. To access a single entry, the client directly contacts the server that hosts the key, in a single hop.

- **Understanding Client Single-Hop Access to Server-Partitioned Regions**

  With single-hop access the client connects to every server, so more connections are generally used. This works fine for smaller installations, but is a barrier to scaling.

- **Configure Client Single-Hop Access to Server-Partitioned Regions**

  Configure your client/server system for direct, single-hop access to partitioned region data in the servers.



#### Understanding Client Single-Hop Access to Server-Partitioned Regions

With single-hop access the client connects to every server, so more connections are generally used. This works fine for smaller installations, but is a barrier to scaling.

If you have a large installation with many clients, you may want to disable single hop by setting the pool attribute, `pr-single-hop-enabled` to false in your pool declarations.

Without single hop, the client uses whatever server connection is available, the same as with all other operations. The server that receives the request determines the data location and contacts the host, which might be a different server. So more multiple-hop requests are made to the server system.

**Note:** Single hop is used for the following operations: `put`, `get`, `destroy`, `putAll`, `getAll`, `removeAll` and `onRegion` function execution.

Even with single hop access enabled, you will occasionally see some multiple-hop behavior. To perform single-hop data access, clients automatically get metadata from the servers about where the entry buckets are hosted. The metadata is maintained lazily. It is only updated after a single-hop operation ends up needing multiple hops, an indicator of stale metadata in the client.

##### Single Hop and the Pool max-connections Setting

Do not set the pool’s `max-connections` setting with single hop enabled. Limiting the pool’s connections with single hop can cause connection thrashing, throughput loss, and server log bloat.

If you need to limit the pool’s connections, either disable single hop or keep a close watch on your system for these negative effects.

Setting no limit on connections, however, can result in too many connections to your servers, possibly causing you to run up against your system’s file handle limits. Review your anticipated connection use and make sure your servers are able to accommodate it.

##### Balancing Single-Hop Server Connection Use

Single-hop gives the biggest benefits when data access is well balanced across your servers. In particular, the loads for client/server connections can get out of balance if you have these in combination:

- Servers that are empty data accessors or that do not host the data the clients access through single-key operations
- Many single-key operations from the clients

If data access is greatly out of balance, clients can thrash trying to get to the data servers. In this case, it might be faster to disable single hop and go through servers that do not host the data.



#### Configure Client Single-Hop Access to Server-Partitioned Regions

Configure your client/server system for direct, single-hop access to partitioned region data in the servers.

This requires a client/server installation that uses one or more partitioned regions on the server.

1. Verify the client’s pool attribute, `pr-single-hop-enabled` is not set or is set to true. It is true by default.
2. If possible, leave the pool’s `max-connections` at the default unlimited setting (-1).
3. If possible, use a custom data resolver to partition your server region data according to your clients’ data use patterns. See [Custom-Partition Your Region Data](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/using_custom_partition_resolvers.html). Include the server’s partition resolver implementation in the client’s `CLASSPATH`. The server passes the name of the resolver for each custom partitioned region, so the client uses the proper one. If the server does not use a partition resolver, the default partitioning between server and client matches, so single hop works.
4. Add single-hop considerations to your overall server load balancing plan. Single-hop uses data location rather than least loaded server to pick the servers for single-key operations. Poorly balanced single-hop data access can affect overall client/server load balancing. Some counterbalancing is done automatically because the servers with more single-key operations become more loaded and are less likely to be picked for other operations.



#### Rebalancing Partitioned Region Data

In a cluster with minimal contention to the concurrent threads reading or updating from the members, you can use rebalancing to dynamically increase or decrease your data and processing capacity.

Rebalancing is a member operation. It affects all partitioned regions defined by the member, regardless of whether the member hosts data for the regions. The rebalancing operation performs two tasks:

1. If the configured partition region redundancy is not satisfied, rebalancing does what it can to recover redundancy. See [Configure High Availability for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
2. Rebalancing moves the partitioned region data buckets between host members as needed to establish the most fair balance of data and behavior across the cluster.

For efficiency, when starting multiple members, trigger the rebalance a single time, after you have added all members.

**Note:** If you have transactions running in your system, be careful in planning your rebalancing operations. Rebalancing may move data between members, which could cause a running transaction to fail with a `TransactionDataRebalancedException`. Fixed custom partitioning prevents rebalancing altogether. All other data partitioning strategies allow rebalancing and can result in this exception unless you run your transactions and your rebalancing operations at different times.

Kick off a rebalance using one of the following:

- `gfsh` command. First, starting a `gfsh` prompt and connect to the cluster. Then type the following command:

  ```
  gfsh>rebalance
  ```

  Optionally, you can specify regions to include or exclude from rebalancing, specify a time-out for the rebalance operation or just [simulate a rebalance operation](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data__section_495FEE48ED60433BADB7D36C73279C89). Type `help rebalance` or see [rebalance](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/rebalance.html) for more information.

- API call:

  ```
  ResourceManager manager = cache.getResourceManager(); 
  RebalanceOperation op = manager.createRebalanceFactory().start(); 
  //Wait until the rebalance is complete and then get the results
  RebalanceResults results = op.getResults(); 
  //These are some of the details we can get about the run from the API
  System.out.println("Took " + results.getTotalTime() + " milliseconds\n"); 
  System.out.println("Transfered " + results.getTotalBucketTransferBytes()+ "bytes\n");
  ```

You can also just simulate a rebalance through the API, to see if it’s worth it to run:

```
ResourceManager manager = cache.getResourceManager(); 
RebalanceOperation op = manager.createRebalanceFactory().simulate(); 
RebalanceResults results = op.getResults(); 
System.out.println("Rebalance would transfer " + results.getTotalBucketTransferBytes() +" bytes "); 
System.out.println(" and create " + results.getTotalBucketCreatesCompleted() + " buckets.\n");
```

##### How Partitioned Region Rebalancing Works

The rebalancing operation runs asynchronously.

By default, rebalancing is performed on one partitioned region at a time. For regions that have colocated data, the rebalancing works on the regions as a group, maintaining the data colocation between the regions.

You can optionally rebalance multiple regions in parallel by setting the `gemfire.resource.manager.threads` system property. Setting this property to a value greater than 1 enables Geode to rebalance multiple regions in parallel, any time a rebalance operation is initiated using the API.

You can continue to use your partitioned regions normally while rebalancing is in progress. Read operations, write operations, and function executions continue while data is moving. If a function is executing on a local data set, you may see a performance degradation if that data moves to another host during function execution. Future function invocations are routed to the correct member.

Geode tries to ensure that each member has the same percentage of its available space used for each partitioned region. The percentage is configured in the `partition-attributes` `local-max-memory` setting.

Partitioned region rebalancing:

- Does not allow the `local-max-memory` setting to be exceeded unless LRU eviction is enabled with overflow to disk.
- Places multiple copies of the same bucket on different host IP addresses whenever possible.
- Resets entry time to live and idle time statistics during bucket migration.
- Replaces offline members.

##### When to Rebalance a Partitioned Region

You typically want to trigger rebalancing when capacity is increased or reduced through member startup, shut down or failure.

You may also need to rebalance when:

- You use redundancy for high availability and have configured your region to not automatically recover redundancy after a loss. In this case, Geode only restores redundancy when you invoke a rebalance. See [Configure High Availability for a Partitioned Region](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
- You have uneven hashing of data. Uneven hashing can occur if your keys do not have a hash code method, which ensures uniform distribution, or if you use a `PartitionResolver` to colocate your partitioned region data (see [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data)). In either case, some buckets may receive more data than others. Rebalancing can be used to even out the load between data stores by putting fewer buckets on members that are hosting large buckets.

##### How to Simulate Region Rebalancing

You can simulate the rebalance operation before moving any actual data around by executing the `rebalance` command with the following option:

```
gfsh>rebalance --simulate
```

**Note:** If you are using `heap_lru` for data eviction, you may notice a difference between your simulated results and your actual rebalancing results. This discrepancy can be due to the VM starting to evict entries after you execute the simulation. Then when you perform an actual rebalance operation, the operation will make different decisions based on the newer heap size.

##### Automated Rebalancing

The experimental [automated rebalance feature](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/automated_rebalance.html) triggers a rebalance operation based on a time schedule.



#### Checking Redundancy in Partitioned Regions

Under some circumstances, it can be important to verify that your partitioned region data is redundant and that upon member restart, redundancy has been recovered properly across partitioned region members.

You can verify partitioned region redundancy by making sure that the `numBucketsWithoutRedundancy`statistic is **zero** for all your partitioned regions. To check this statistic, use the following `gfsh`command:

```
gfsh>show metrics --categories=partition --region=region_name
```

For example:

```
gfsh>show metrics --categories=partition --region=posts

Cluster-wide Region Metrics
--------- | --------------------------- | -----
partition | putLocalRate                | 0
          | putRemoteRate               | 0
          | putRemoteLatency            | 0
          | putRemoteAvgLatency         | 0
          | bucketCount                 | 1
          | primaryBucketCount          | 1
          | numBucketsWithoutRedundancy | 1
          | minBucketSize               | 1
          | maxBucketSize               | 0
          | totalBucketSize             | 1
          | averageBucketSize           | 1
```

If you have `start-recovery-delay=-1` configured for your partitioned region, you will need to perform a rebalance on your region after you restart any members in your cluster in order to recover redundancy.

If you have `start-recovery-delay` set to a low number, you may need to wait extra time until the region has recovered redundancy.



#### Moving Partitioned Region Data to Another Member

You can use the `PartitionRegionHelper` `moveBucketByKey` and `moveData` methods to explicitly move partitioned region data from one member to another.

The `moveBucketByKey` method moves the bucket that contains the specified key from a source member to a destination member. For example, you could use the method to move a popular product item to a new, empty member to reduce load on the source member.

For example:

```
Object product = ...
Region r = ...
DistributedSystem ds = ...
String memberName = ...

//Find the member that is currently hosting the product.
Set<DistributedMember> sourceMembers =
PartitionRegionHelper.getAllMembersForKey(r, product);

//Find the member to move the product to.
DistributedMember destination = ds.findDistributedMember(memberName);

//In this example we assume there is always at least one source.
//In practice, you should check that at least one source
//for the data is available.
source = sourceMembers.iterator().next();

//Move the bucket to the new node. The bucket will
//be moved when this method completes. It throws an exception
//if there is a problem or invalid arguments.
PartitionRegionHelper.moveBucketByKey(r, source, destination, product);
```

See the Java API documentation for `org.apache.geode.cache.partition.PartitionRegionHelper.moveBucketByKey` for more details.

The `moveData` method moves data up to a given percentage (measured in bytes) from a source member to a destination member. For example, you could use this method to move a specified percentage of data from an overloaded member to another member to improve distribution.

For example:

```
Region r = ...
DistributedSystem ds = ...
String sourceName = ...
String destName = ...

//Find the source member.
DistributedMember source = ds.findDistributedMember(sourceName);
DistributedMember destination = ds.findDistributedMember(destName);

//Move up to 20% of the data from the source to the destination node.
PartitionRegionHelper.moveData(r, source, destination, 20);
```

See the Java API documentation for `org.apache.geode.cache.partition.PartitionRegionHelper.moveData` for more details.

For more information on partitioned regions and rebalancing, see [Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/chapter_overview.html).



## Distributed and Replicated Regions

In addition to basic region management, distributed and replicated regions include options for things like push and pull distribution models, global locking, and region entry versions to ensure consistency across Geode members.

- **How Distribution Works**

  To use distributed and replicated regions, you should understand how they work and your options for managing them.

- **Options for Region Distribution**

  You can use distribution with and without acknowledgment, or global locking for your region distribution. Regions that are configured for distribution with acknowledgment can also be configured to resolve concurrent updates consistently across all Geode members that host the region.

- **How Replication and Preloading Work**

  To work with replicated and preloaded regions, you should understand how their data is initialized and maintained in the cache.

- **Configure Distributed, Replicated, and Preloaded Regions**

  Plan the configuration and ongoing management of your distributed, replicated, and preloaded regions, and configure the regions.

- **Locking in Global Regions**

  In global regions, the system locks entries and the region during updates. You can also explicitly lock the region and its entries as needed by your application. Locking includes system settings that help you optimize performance and locking behavior between your members.



### How Distribution Works

To use distributed and replicated regions, you should understand how they work and your options for managing them.

**Note:** The management of replicated and distributed regions supplements the general information for managing data regions provided in [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html). See also `org.apache.geode.cache.PartitionAttributes`.

A distributed region automatically sends entry value updates to remote caches and receives updates from them.

- Distributed entry updates come from the `Region` `put` and `create` operations (the creation of an entry with a non-null value is seen as an update by remote caches that already have the entry key). Entry updates are distributed selectively - only to caches where the entry key is already defined. This provides a pull model of distribution, compared to the push model that you get with replication.
- Distribution alone does not cause new entries to be copied from remote caches.
- A distributed region shares cache loader and cache writer application event handler plug-ins across the cluster.

In a distributed region, new and updated entry values are automatically distributed to remote caches that already have the entries defined.

**Step 1:** The application updates or creates the entry. At this point, the entry in the M1 cache may not yet exist.

![img](assets/distributed_how_1.svg)

**Step 2:** The new value is automatically distributed to caches holding the entry.

![img](assets/distributed_how_2.svg)

**Step 3:** The entry’s value is the same throughout the cluster.

![img](assets/distributed_how_3.svg)



### Options for Region Distribution

You can use distribution with and without acknowledgment, or global locking for your region distribution. Regions that are configured for distribution with acknowledgment can also be configured to resolve concurrent updates consistently across all Geode members that host the region.

Each distributed region must have the same scope and concurrency checking setting throughout the cluster.

Distributed scope is provided at three levels:

- **distributed-no-ack**. Distribution operations return without waiting for a response from other caches. This scope provides the best performance and uses the least amount of overhead, but it is also most prone to having inconsistencies caused by network problems. For example, a temporary disruption of the network transport layer could cause a failure in distributing updates to a cache on a remote machine, while the local cache continues being updated.

- **distributed-ack**. Distribution waits for acknowledgment from other caches before continuing. This is slower than `distributed-no-ack`, but covers simple communication problems such as temporary network disruptions.

  In systems where there are many `distributed-no-ack` operations, it is possible for `distributed-ack` operations to take a long time to complete. The cluster has a configurable time to wait for acknowledgment to any `distributed-ack` message before sending alerts to the logs about a possible problem with the unresponsive member. No matter how long the wait, the sender keeps waiting in order to honor the distributed-ack region setting. The `gemfire.properties` attribute governing this is `ack-wait-threshold`.

- **global**. Entries and regions are automatically locked across the cluster during distribution operations. All load, create, put, invalidate, and destroy operations on the region and its entries are performed with a distributed lock. The global scope enforces strict consistency across the cluster, but it is the slowest mechanism for achieving consistency. In addition to the implicit locking performed by distribution operations, regions with global scope and their contents can be explicitly locked through the application APIs. This allows applications to perform atomic, multi-step operations on regions and region entries.



### How Replication and Preloading Work

To work with replicated and preloaded regions, you should understand how their data is initialized and maintained in the cache.

Replicated and preloaded regions are configured by using one of the `REPLICATE` region shortcut settings, or by setting the region attribute `data-policy` to `replicate`, `persistent-replicate`, or `preloaded`.

#### Initialization of Replicated and Preloaded Regions

At region creation, the system initializes the preloaded or replicated region with the most complete and up-to-date data set it can find. The system uses these data sources to initialize the new region, following this order of preference:

1. Another replicated region that is already defined in the cluster.
2. For persistent replicate only. Disk files, followed by a union of all copies of the region in the distributed cache.
3. For preloaded region only. Another preloaded region that is already defined in the cluster.
4. The union of all copies of the region in the distributed cache.

![img](assets/distributed_replica_preload.svg)

While a region is being initialized from a replicated or preloaded region, if the source region crashes, the initialization starts over.

If a union of regions is used for initialization, as in the figure, and one of the individual source regions goes away during the initialization (due to cache closure, member crash, or region destruction), the new region may contain a partial data set from the crashed source region. When this happens, there is no warning logged or exception thrown. The new region still has a complete set of the remaining members’ regions.

#### Behavior of Replicated and Preloaded Regions After Initialization

Once initialized, the preloaded region operates like the region with a `normal` `data-policy`, receiving distributions only for entries it has defined in the local cache.

![img](assets/distributed_preload.svg)

If the region is configured as a replicated region, it receives all new creations in the distributed region from the other members. This is the push distribution model. Unlike the preloaded region, the replicated region has a contract that states it will hold all entries that are present anywhere in the distributed region.

![img](assets/distributed_replica.svg)

### Configure Distributed, Replicated, and Preloaded Regions

Plan the configuration and ongoing management of your distributed, replicated, and preloaded regions, and configure the regions.

Before you begin, understand [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).

1. Choose the region shortcut setting that most closely matches your region configuration. See **org.apache.geode.cache.RegionShortcut** or [Region Shortcuts](https://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_regionshortcuts.html#concept_ymp_rkz_4dffhdfhk). To create a replicated region, use one of the `REPLICATE` shortcut settings. To create a preloaded region, set your region `data-policy` to `preloaded`. This `cache.xml` declaration creates a replicated region:

   ```
   <region-attributes refid="REPLICATE"> 
   </region-attributes>
   ```

   You can also use gfsh to configure a region. For example:

   ```
   gfsh>create region --name=regionA --type=REPLICATE
   ```

   See [Region Types](https://geode.apache.org/docs/guide/17/developing/region_options/region_types.html#region_types).

2. Choose the level of distribution for your region. The region shortcuts in `RegionShortcut` for distributed regions use `distributed-ack` scope. If you need a different scope, set the `region-attributes` `scope` to `distributed-no-ack` or `global`.

   Example:

   ```
   <region-attributes refid="REPLICATE" scope="distributed-no-ack"> 
   </region-attributes>
   ```

3. If you are using the `distributed-ack` scope, optionally enable concurrency checks for the region.

   Example:

   ```
   <region-attributes refid="REPLICATE" scope="distributed-ack" concurrency-checks-enabled="true"> 
   </region-attributes>
   ```

4. If you are using `global` scope, program any explicit locking you need in addition to the automated locking provided by Geode.

#### Local Destroy and Invalidate in the Replicated Region

Of all the operations that affect the local cache only, only local region destroy is allowed in a replicated region. Other operations are not configurable or throw exceptions. For example, you cannot use local destroy as the expiration action on a replicated region. This is because local operations like entry invalidation and destruction remove data from the local cache only. A replicated region would no longer be complete if data were removed locally but left intact.



### Locking in Global Regions

In global regions, the system locks entries and the region during updates. You can also explicitly lock the region and its entries as needed by your application. Locking includes system settings that help you optimize performance and locking behavior between your members.

In regions with global scope, locking helps ensure cache consistency.

Locking of regions and entries is done in two ways:

1. **Implicit**. Geode automatically locks global regions and their data entries during most operations. Region invalidation and destruction do not acquire locks.

2. **Explicit**. You can use the API to explicitly lock the region and its entries. Do this to guarantee atomicity in tasks with multi-step distributed operations. The `Region` methods `org.apache.geode.cache.Region.getDistributedLock` and `org.apache.geode.cache.Region.getRegionDistributedLock` return instances of `java.util.concurrent.locks.Lock` for a region and a specified key.

   **Note:** You must use the `Region` API to lock regions and region entries. Do not use the `DistributedLockService` in the `org.apache.geode.distributed` package. That service is available only for locking in arbitrary distributed applications. It is not compatible with the `Region`locking methods.

#### Lock Timeouts

Getting a lock on a region or entry is a two-step process of getting a lock instance for the entity and then using the instance to set the lock. Once you have the lock, you hold it for your operations, then release it for someone else to use. You can set limits on the time spent waiting to get a lock and the time spent holding it. Both implicit and explicit locking operations are affected by the timeouts:

- The lock timeout limits the wait to get a lock. The cache attribute `lock-timeout` governs implicit lock requests. For explicit locking, specify the wait time through your calls to the instance of `java.util.concurrent.locks.Lock` returned from the `Region` API. You can wait a specific amount of time, return immediately either with or without the lock, or wait indefinitely.

  ```
  <cache lock-timeout="60"> 
  </cache>
  ```

  gfsh:

  ```
  gfsh>alter runtime --lock-timeout=60 
  ```

- The lock lease limits how long a lock can be held before it is automatically released. A timed lock allows the application to recover when a member fails to release an obtained lock within the lease time. For all locking, this timeout is set with the cache attribute `lock-lease`.

  ```
  <cache lock-lease="120"> </cache>
  ```

  gfsh:

  ```
  gfsh>alter runtime --lock-lease=120
  ```

#### Optimize Locking Performance

For each global region, one of the members with the region defined will be assigned the job of lock grantor. The lock grantor runs the lock service that receives lock requests from system members, queues them as needed, and grants them in the order received.

The lock grantor is at a slight advantage over other members as it is the only one that does not have to send a message to request a lock. The grantor’s requests cost the least for the same reason. Thus, you can optimize locking in a region by assigning lock grantor status to the member that acquires the most locks. This may be the member that performs the most puts and thus requires the most implicit locks or this may be the member that performs many explicit locks.

The lock grantor is assigned as follows:

- Any member with the region defined that requests lock grantor status is assigned it. Thus at any time, the most recent member to make the request is the lock grantor.
- If no member requests lock grantor status for a region, or if the current lock grantor goes away, the system assigns a lock grantor from the members that have the region defined in their caches.

You can request lock grantor status:

1. At region creation through the `is-lock-grantor` attribute. You can retrieve this attribute through the region method, `getAttributes`, to see whether you requested to be lock grantor for the region. **Note:** The `is-lock-grantor` attribute does not change after region creation.
2. After region creation through the region `becomeLockGrantor` method. Changing lock grantors should be done with care, however, as doing so takes cycles from other operations. In particular, be careful to avoid creating a situation where you have members vying for lock grantor status.

#### Examples

These two examples show entry locking and unlocking. Note how the entry’s `Lock` object is obtained and then its lock method invoked to actually set the lock. The example program stores the entry lock information in a hash table for future reference.

```
/* Lock a data entry */ 
HashMap lockedItemsMap = new HashMap(); 
...
  String entryKey = ... 
  if (!lockedItemsMap.containsKey(entryKey)) 
  { 
    Lock lock = this.currRegion.getDistributedLock(entryKey); 
    lock.lock(); 
    lockedItemsMap.put(name, lock); 
  } 
  ...
/* Unlock a data entry */ 
  String entryKey = ... 
  if (lockedItemsMap.containsKey(entryKey)) 
  { 
    Lock lock = (Lock) lockedItemsMap.remove(name);
    lock.unlock();
  }
```



## Consistency for Region Updates

Geode ensures that all copies of a region eventually reach a consistent state on all members and clients that host the region, including Geode members that distribute region events.

- **Consistency Checking by Region Type**

  Geode performs different consistency checks depending on the type of region you have configured.

- **Configuring Consistency Checking**

  Geode enables consistency checking by default. You cannot disable consistency checking for persistent regions. For all other regions, you can explicitly enable or disable consistency checking by setting the `concurrency-checks-enabled` region attribute in `cache.xml` to “true” or “false.”

- **Overhead for Consistency Checks**

  Consistency checking requires additional overhead for storing and distributing version and timestamp information, as well as for maintaining destroyed entries for a period of time to meet consistency requirements.

- **How Consistency Checking Works for Replicated Regions**

  Each region stores version and timestamp information for use in conflict detection. Geode members use the recorded information to detect and resolve conflicts consistently before applying a distributed update.

- **How Destroy and Clear Operations Are Resolved**

  When consistency checking is enabled for a region, a Geode member does not immediately remove an entry from the region when an application destroys the entry. Instead, the member retains the entry with its current version stamp for a period of time in order to detect possible conflicts with operations that have occurred. The retained entry is referred to as a *tombstone*. Geode retains tombstones for partitioned regions and non-replicated regions as well as for replicated regions, in order to provide consistency.

- **Transactions with Consistent Regions**

  A transaction that modifies a region having consistency checking enabled generates all necessary version information for region updates when the transaction commits.



### Consistency Checking by Region Type

Geode performs different consistency checks depending on the type of region you have configured.

**Partitioned Region Consistency**

For a partitioned region, Geode maintains consistency by routing all updates on a given key to the Geode member that holds the primary copy of that key. That member holds a lock on the key while distributing updates to other members that host a copy of the key. Because all updates to a partitioned region are serialized on the primary Geode member, all members apply the updates in the same order and consistency is maintained at all times. See [Understanding Partitioning](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/how_partitioning_works.html).

**Replicated Region Consistency**

For a replicated region, any member that hosts the region can update a key and distribute that update to other members without locking the key. It is possible that two members can update the same key at the same time (a concurrent update). It is also possible that, due to network latency, an update in one member is distributed to other members at a later time, after those members have already applied more recent updates to the key (an out-of-order update). By default, Geode members perform conflict checking before applying region updates in order to detect and consistently resolve concurrent and out-of-order updates. Conflict checking ensures that region data eventually becomes consistent on all members that host the region. The conflict checking behavior for replicated regions is summarized as follows:

- If two members update the same key at the same time, conflict checking ensures that all members eventually apply the same value, which is the value of one of the two concurrent updates.
- If a member receives an out-of-order update (an update that is received after one or more recent updates were applied), conflict checking ensures that the out-of-order update is discarded and not applied to the cache.

[How Consistency Checking Works for Replicated Regions](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_C5B74CCDD909403C815639339AA03758) and [How Destroy and Clear Operations Are Resolved](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_321B05044B6641FCAEFABBF5066BD399) provide more details about how Geode performs conflict checking when applying an update.

**Non-Replicated Regions and Client Cache Consistency**

When a member receives an update for an entry in a non-replicated region and applies an update, it performs conflict checking in the same way as for a replicated region. However, if the member initiates an operation on an entry that is not present in the region, it first passes that operation to a member that hosts a replicate. The member that hosts the replica generates and provides the version information necessary for subsequent conflict checking. See [How Consistency Checking Works for Replicated Regions](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_C5B74CCDD909403C815639339AA03758).

Client caches also perform consistency checking in the same way when they receive an update for a region entry. However, all region operations that originate in the client cache are first passed onto an available Geode server, which generates the version information necessary for subsequent conflict checking.

### Configuring Consistency Checking

Geode enables consistency checking by default. You cannot disable consistency checking for persistent regions. For all other regions, you can explicitly enable or disable consistency checking by setting the `concurrency-checks-enabled` region attribute in `cache.xml` to “true” or “false.”

All Geode members that host a region must use the same `concurrency-checks-enabled` setting for that region.

A client cache can disable consistency checking for a region even if server caches enable consistency checking for the same region. This configuration ensures that the client sees all events for the region, but it does not prevent the client cache region from becoming out-of-sync with the server cache.

See [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes).

**Note:** Regions that do not enable consistency checking remain subject to race conditions. Concurrent updates may result in one or more members having different values for the same key. Network latency can result in older updates being applied to a key after more recent updates have occurred.

### Overhead for Consistency Checks

Consistency checking requires additional overhead for storing and distributing version and timestamp information, as well as for maintaining destroyed entries for a period of time to meet consistency requirements.

To provide consistency checking, each region entry uses an additional 16 bytes. When an entry is deleted, a tombstone entry of approximately 13 bytes is created and maintained until the tombstone expires or is garbage-collected in the member. (When an entry is destroyed, the member temporarily retains the entry with its current version stamp to detect possible conflicts with operations that have occurred. The retained entry is referred to as a tombstone.) See [How Destroy and Clear Operations Are Resolved](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_321B05044B6641FCAEFABBF5066BD399).

If you cannot support the additional overhead in your deployment, you can disable consistency checks by setting `concurrency-checks-enabled` to “false” for each region. See [Consistency for Region Updates](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).

### How Consistency Checking Works for Replicated Regions

Each region stores version and timestamp information for use in conflict detection. Geode members use the recorded information to detect and resolve conflicts consistently before applying a distributed update.

By default, each entry in a region stores the ID of the Geode member that last updated the entry, as well as a version stamp for the entry that is incremented each time an update occurs. The version information is stored in each local entry, and the version stamp is distributed to other Geode members when the local entry is updated.

A Geode member or client that receives an update message first compares the update version stamp with the version stamp recorded in its local cache. If the update version stamp is larger, it represents a newer version of the entry, so the receiving member applies the update locally and updates the version information. A smaller update version stamp indicates an out-of-order update, which is discarded.

An identical version stamp indicates that multiple Geode members updated the same entry at the same time. To resolve a concurrent update, a Geode member always applies (or keeps) the region entry that has the highest membership ID; the region entry having the lower membership ID is discarded.

**Note:** When a Geode member discards an update message (either for an out-of-order update or when resolving a concurrent update), it does not pass the discarded event to an event listener for the region. You can track the number of discarded updates for each member using the `conflatedEvents` statistic. See [Geode Statistics List](https://geode.apache.org/docs/guide/17/reference/statistics_list.html#statistics_list). Some members may discard an update while other members apply the update, depending on the order in which each member receives the update. For this reason, the `conflatedEvents` statistic differs for each Geode member. The example below describes this behavior in more detail.

The following example shows how a concurrent update is handled in a cluster of three Geode members. Assume that Members A, B, and C have membership IDs of 1, 2, and 3, respectively. Each member currently stores an entry, X, in their caches at version C2 (the entry was last updated by member C):

**Step 1:** An application updates entry X on Geode member A at the same time another application updates entry X on member C. Each member increments the version stamp for the entry and records the version stamp with their member ID in their local caches. In this case the entry was originally at version C2, so each member updates the version to 3 (A3 and C3, respectively) in their local caches.

![img](assets/region_entry_versions_1.svg)

**Step 2:** Member A distributes its update message to members B and C.

Member B compares the update version stamp (3) to its recorded version stamp (2) and applies the update to its local cache as version A3. In this member, the update is applied for the time being, and passed on to configured event listeners.

Member C compares the update version stamp (3) to its recorded version stamp (3) and identifies a concurrent update. To resolve the conflict, member C next compares the membership ID of the update to the membership ID stored in its local cache. Because the distributed system ID the update (A3) is lower than the ID stored in the cache (C3), member C discards the update (and increments the `conflatedEvents` statistic).

![img](assets/region_entry_versions_2.svg)

**Step 3:** Member C distributes the update message to members A and B.

Members A and B compare the update version stamp (3) to their recorded version stamps (3) and identify the concurrent update. To resolve the conflict, both members compare the membership ID of the update with the membership ID stored in their local caches. Because the distributed system ID of A in the cache value is less than the ID of C in the update, both members record the update C3 in their local caches, overwriting the previous value.

At this point, all members that host the region have achieved a consistent state for the concurrent updates on members A and C.

![img](assets/region_entry_versions_3.svg)

### How Destroy and Clear Operations Are Resolved

When consistency checking is enabled for a region, a Geode member does not immediately remove an entry from the region when an application destroys the entry. Instead, the member retains the entry with its current version stamp for a period of time in order to detect possible conflicts with operations that have occurred. The retained entry is referred to as a *tombstone*. Geode retains tombstones for partitioned regions and non-replicated regions as well as for replicated regions, in order to provide consistency.

A tombstone in a client cache or a non-replicated region expires after 8 minutes, at which point the tombstone is immediately removed from the cache.

A tombstone for a replicated or partitioned region expires after 10 minutes. Expired tombstones are eligible for garbage collection by the Geode member. Garbage collection is automatically triggered after 100,000 tombstones of any type have timed out in the local Geode member. You can optionally set the `gemfire.tombstone-gc-threshold` property to a value smaller than 100000 to perform garbage collection more frequently.

**Note:** To avoid out-of-memory errors, a Geode member also initiates garbage collection for tombstones when the amount of free memory drops below 30 percent of total memory.

You can monitor the total number of tombstones in a cache using the `tombstoneCount` statistic in `CachePerfStats`. The `tombstoneGCCount` statistic records the total number of tombstone garbage collection cycles that a member has performed. `replicatedTombstonesSize` and `nonReplicatedTombstonesSize` show the approximate number of bytes that are currently consumed by tombstones in replicated or partitioned regions, and in non-replicated regions, respectively. See [Geode Statistics List](https://geode.apache.org/docs/guide/17/reference/statistics_list.html#statistics_list).

**About Region.clear() Operations**

Region entry version stamps and tombstones ensure consistency only when individual entries are destroyed. A `Region.clear()` operation, however, operates on all entries in a region at once. To provide consistency for `Region.clear()` operations, Geode obtains a distributed read/write lock for the region, which blocks all concurrent updates to the region. Any updates that were initiated before the clear operation are allowed to complete before the region is cleared.

### Transactions with Consistent Regions

A transaction that modifies a region having consistency checking enabled generates all necessary version information for region updates when the transaction commits.

If a transaction modifies a normal, preloaded or empty region, the transaction is first delegated to a Geode member that holds a replicate for the region. This behavior is similar to the transactional behavior for partitioned regions, where the partitioned region transaction is forwarded to a member that hosts the primary for the partitioned region update.

The limitation for transactions on normal, preloaded or or empty regions is that, when consistency checking is enabled, a transaction cannot perform a `localDestroy` or `localInvalidate` operation against the region. Geode throws an `UnsupportedOperationInTransactionException` exception in such cases. An application should use a `Destroy` or `Invalidate` operation in place of a `localDestroy` or `localInvalidate` when consistency checks are enabled.



## General Region Data Management

For all regions, you have options to control memory use, back up your data to disk, and discard stale data from your cache.

- **Persistence and Overflow**

  You can persist data on disk for backup purposes and overflow it to disk to free up memory without completely removing the data from your cache.

- **Eviction**

  Use eviction to control data region size. Eviction actions are triggered by space-based thresholds.

- **Expiration**

  Use expiration to keep data current and to reduce region size by removing stale entries. Expiration actions are triggered by time-based thresholds.

- **Keeping the Cache in Sync with Outside Data Sources**

  Keep your distributed cache in sync with an outside data source by programming and installing application plug-ins for your region.



### Persistence and Overflow

You can persist data on disk for backup purposes and overflow it to disk to free up memory without completely removing the data from your cache.

**Note:** This supplements the general steps for managing data regions provided in [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).

All disk storage uses Apache Geode [Disk Storage](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

- **How Persistence and Overflow Work**

  To use Geode persistence and overflow, you should understand how they work with your data.

- **Configure Region Persistence and Overflow**

  Plan persistence and overflow for your data regions and configure them accordingly.

- **Overflow Configuration Examples**

  The `cache.xml` examples show configuration of region and server subscription queue overflows.



#### How Persistence and Overflow Work

To use Geode persistence and overflow, you should understand how they work with your data.

Geode persists and overflows several types of data. You can persist or overflow the application data in your regions. In addition, Geode persists and overflows messaging queues, to manage memory consumption and provide high availability.

Persistent data outlives the member where the region resides and can be used to initialize the region at creation. Overflow acts only as an extension of the region in memory.

The data is written to disk according to the configuration of Geode disk stores. For any disk option, you can specify the name of the disk store to use or use the Geode default disk store. See [Disk Storage](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

**How Data Is Persisted and Overflowed**

For persistence, the entry keys and values are copied to disk. For overflow, only the entry values are copied. Other data, such as statistics and user attributes, are retained in memory only.

- Data regions are overflowed to disk by least recently used (LRU) entries because those entries are deemed of least interest to the application and therefore less likely to be accessed.
- Server subscription queues overflow most recently used (MRU) entries. These are the messages that are at the end of the queue and so are last in line to be sent to the client.

**Persistence**

Persistence provides a disk backup of region entry data. The keys and values of all entries are saved to disk, like having a replica of the region on disk. Region entry operations such as put and destroy are carried out in memory and on disk.

![img](assets/developing_persistence.svg)

When the member stops for any reason, the region data on disk remains. In partitioned regions, where data buckets are divided among members, this can result in some data only on disk and some on disk and in memory. The disk data can be used at member startup to populate the same region.

**Overflow**

Overflow limits region size in memory by moving the values of least recently used (LRU) entries to disk. Overflow basically uses disk as a swap space for entry values. If an entry is requested whose value is only on disk, the value is copied back up into memory, possibly causing the value of a different LRU entry to be moved to disk. As with persisted entries, overflowed entries are maintained on disk just as they are in memory.

In this figure, the value of entry X has been moved to disk to make space in memory. The key for X remains in memory. From the distributed system perspective, the value on disk is as much a part of the region as the data in memory.

![img](assets/developing_overflow.svg)

**Persistence and Overflow Together**

Used together, persistence and overflow keep all entry keys and values on disk and only the most active entry values in memory. The removal of an entry value from memory due to overflow has no effect on the disk copy as all entries are already on disk.

![img](assets/developing_persistence_and_overflow.svg)

**Persistence and Multi-Site Configurations**

Multi-site gateway sender queues overflow most recently used (MRU) entries. These are the messages that are at the end of the queue and so are last in line to be sent to the remote site. You can also configure gateway sender queues to persist for high availability.



#### Configure Region Persistence and Overflow

Plan persistence and overflow for your data regions and configure them accordingly.

Use the following steps to configure your data regions for persistence and overflow:

1. Configure your disk stores as needed. See [Designing and Configuring Disk Stores](https://geode.apache.org/docs/guide/17/managing/disk_storage/using_disk_stores.html#defining_disk_stores). The cache disk store defines where and how the data is written to disk.

   ```
   <disk-store name="myPersistentStore" . . . >
   <disk-store name="myOverflowStore" . . . >
   ```

2. Specify the persistence and overflow criteria for the region. If you are not using the default disk store, provide the disk store name in your region attributes configuration. To write asynchronously to disk, specify `disk-synchronous="false"`.

   - For overflow, specify the overflow criteria in the region’s `eviction-attributes` and name the disk store to use.

     Example:

     ```
     <region name="overflowRegion" . . . >
       <region-attributes disk-store-name="myOverflowStore" disk-synchronous="true">
         <eviction-attributes>
           <!-- Overflow to disk when 100 megabytes of data reside in the
                region -->
           <lru-memory-size maximum="100" action="overflow-to-disk"/>
         </eviction-attributes>
       </region-attributes>
     </region>
     ```

     gfsh:

     You cannot configure `lru-memory-size` using gfsh.

   - For persistence, set the `data-policy` to `persistent-replicate` and name the disk store to use.

     Example:

     ```
     <region name="partitioned_region" refid="PARTITION_PERSISTENT">
       <region-attributes disk-store-name="myPersistentStore">
         . . . 
       </region-attributes>
     </region> 
     ```

When you start your members, overflow and persistence will be done automatically, with the disk stores and disk write behaviors.

**Note:** You can also configure Regions and Disk Stores using the gfsh command-line interface. See [Region Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD) and [Disk Store Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_1ACC91B493EE446E89EC7DBFBBAE00EA).



| Related Topics                                               |
| ------------------------------------------------------------ |
| `org.apache.geode.cache.RegionAttributes` for data region persistence information |
| `org.apache.geode.cache.EvictionAttributes` for data region overflow information |
| `org.apache.geode.cache.server.ClientSubscriptionConfig`     |



#### Overflow Configuration Examples

The `cache.xml` examples show configuration of region and server subscription queue overflows.

Configure overflow criteria based on one of these factors:

- Entry count
- Absolute memory consumption
- Memory consumption as a percentage of the application heap (not available for server subscription queues)

Configuration of region overflow:

```
<!-- Overflow when the region goes over 10000 entries -->
<region-attributes>
  <eviction-attributes>
    <lru-entry-count maximum="10000" action="overflow-to-disk"/>
  </eviction-attributes>
</region-attributes>
```

Configuration of server’s client subscription queue overflow:

```
<!-- Overflow the server's subscription queues when the queues reach 1 Mb of memory -->
<cache> 
  <cache-server> 
    <client-subscription eviction-policy="mem" capacity="1"/> 
  </cache-server> 
</cache>
```



### Eviction

Use eviction to control data region size. Eviction actions are triggered by space-based thresholds.

- **How Eviction Works**

  Eviction settings cause Apache Geode to work to keep a region’s resource use under a specified level by removing least recently used (LRU) entries to make way for new entries.

- **Configure Data Eviction**

  Configure a region’s `eviction-attributes` settings to keep your region within a specified limit.



#### How Eviction Works

Eviction keeps a region’s resource use under a specified level by removing least recently used (LRU) entries to make way for new entries. You can choose whether expired entries are overflowed to disk or destroyed. See [Persistence and Overflow](https://geode.apache.org/docs/guide/17/developing/storing_data_on_disk/chapter_overview.html).

Eviction is triggered when a size-based threshold is exceeded. A region’s eviction threshold can be based on:

- entry count
- absolute memory usage
- percentage of available heap

These eviction algorithms are mutually exclusive; only one can be in effect for a given region.

When Geode determines that adding or updating an entry would take the region over the specified level, it overflows or removes enough older entries to make room. For entry count eviction, this means a one-to-one trade of an older entry for the newer one. For the memory settings, the number of older entries that need to be removed to make space depends on the sizes of the older and newer entries.

For efficiency, the selection of items for removal is not strictly LRU, but does choose eviction candidates from among the region’s oldest entries. As a result, eviction may leave older entries for the region in the local data store.

**Eviction Actions**

Apache Geode provides the following eviction actions:

- **local destroy** - Removes the entry from the local cache, but does not distribute the removal operation to remote members. This action can be applied to an entry in a partitioned region, but it not recommended if redundancy is enabled (redundant-copies > 0), as it introduces inconsistencies between the redundant buckets. When applied to an entry in a replicated region, Geode silently changes the region type to “preloaded” to accommodate the local modification.
- **overflow to disk** - The entry’s value is overflowed to disk and set to null in memory. The entry’s key is retained in the cache. This is the only eviction action fully supported for partitioned regions.

**Eviction in Partitioned Regions**

In partitioned regions, Geode removes the oldest entry it can find *in the bucket where the new entry operation is being performed*. Geode maintains LRU entry information on a bucket-by-bucket basis, as the cost of maintaining information across the partitioned region would slow the system’s performance.

- For memory and entry count eviction, LRU eviction is done in the bucket where the new entry operation is being performed until the overall size of the combined buckets in the member has dropped enough to perform the operation without going over the limit.
- For heap eviction, each partitioned region bucket is treated as if it were a separate region, with each eviction action only considering the LRU for the bucket, and not the partitioned region as a whole.


#### Configure Data Eviction

Configure a region’s `eviction-attributes` settings to keep your region within a specified limit.

Configure data eviction as follows. You do not need to perform these steps in the sequence shown.

1. Decide whether to evict based on:
   - Entry count (useful if your entry sizes are relatively uniform).
   - Total bytes used. In partitioned regions, this is set using `local-max-memory`. In non-partitioned regions, it is set in `eviction-attributes`.
   - Percentage of application heap used. This uses the Geode resource manager. When the manager determines that eviction is required, the manager orders the eviction controller to start evicting from all regions where the eviction algorithm is set to `lru-heap-percentage`. Eviction continues until the manager calls a halt. Geode evicts the least recently used entry hosted by the member for the region. See [Managing Heap and Off-heap Memory](https://geode.apache.org/docs/guide/17/managing/heap_use/heap_management.html#resource_manager).
2. Decide what action to take when the limit is reached:
   - Locally destroy the entry.
   - Overflow the entry data to disk. See [Persistence and Overflow](https://geode.apache.org/docs/guide/17/developing/storing_data_on_disk/chapter_overview.html).
3. Decide the maximum amount of data to allow in the member for the eviction measurement indicated. This is the maximum for all storage for the region in the member. For partitioned regions, this is the total for all buckets stored in the member for the region, including any secondary buckets used for redundancy.
4. Decide whether to program a custom sizer for your region. If you are able to provide such a class, it might be faster than the standard sizing done by Geode. Your custom class must follow the guidelines for defining custom classes and, additionally, must implement `org.apache.geode.cache.util.ObjectSizer`. See [Requirements for Using Custom Classes in Data Caching](https://geode.apache.org/docs/guide/17/basic_config/data_entries_custom_classes/using_custom_classes.html).

**Examples:**

Set an LRU memory eviction threshold of 1000 MB. Use a custom class for measuring the size of each object in the region:

```
gfsh>create region --name=myRegion --type=REPLICATE --eviction-max-memory=1000 \
--eviction-action=overflow-to-disk --eviction-object-sizer=com.myLib.MySizer
```

Create an eviction threshold on a partitioned region with a maximum entry count of 512:

```
gfsh>create region --name=myRegion --type=PARTITION --eviction-entry-count=512 \
--eviction-action=overflow-to-disk 
```

To configure a partitioned region for heap LRU eviction, first configure the resource manager on server startup, then create a region with eviction enabled:

```
gfsh>start server --name=Server1 --eviction-heap-percentage=80
...
gfsh>create region --name=myRegion --type=PARTITION --eviction-action=overflow-to-disk
```



### Expiration

Use expiration to keep data current and to reduce region size by removing stale entries. Expiration actions are triggered by time-based thresholds.

- **How Expiration Works**

  Expiration removes old entries and entries that you are not using. You can choose whether expired entries are invalidated or destroyed.

- **Configure Data Expiration**

  Configure the type of expiration and the expiration action to use.



#### How Expiration Works

Expiration keeps a region’s data fresh by removing old entries and entries that you are not using. You can choose whether expired entries are invalidated or destroyed.

Expiration activities in distributed regions can be distributed or local. Thus, one cache could control expiration for a number of caches in the system.

This figure shows two basic expiration settings for a client/server system. The server (on the right) populates the region from a database and the data is automatically distributed throughout the system. The data is valid for only one hour, so the server performs a distributed destroy on entries that are an hour old. The client applications are consumers. The clients free up space in their caches by removing their local copies of the entries for which there is no local interest (idle-time expiration). Requests for entries that have expired on the clients will be forwarded to the server.

![img](assets/expiration.png)

**Expiration Types**

Apache Geode provides two types of expiration, each triggered by a time-based threshold. These can co-exist; they are not mutually exclusive.

- **Time to live (TTL)**. The amount of time, in seconds, the object may remain in the cache after the last creation or update. For entries, the counter is set to zero for create and put operations. Region counters are reset when the region is created and when an entry has its counter reset. The TTL expiration attributes are `region-time-to-live` and `entry-time-to-live`.
- **Idle timeout**. The amount of time, in seconds, the object may remain in the cache after the last access. The idle timeout counter for an object is reset any time its TTL counter is reset. In addition, an entry’s idle timeout counter is reset any time the entry is accessed through a get operation or a netSearch . The idle timeout counter for a region is reset whenever the idle timeout is reset for one of its entries. Idle timeout expiration attributes are: `region-idle-time`and `entry-idle-time`.

**Expiration Actions**

Apache Geode provides the following expiration actions:

- **invalidate (default)** - The data item’s value is deleted, but the key remains in the cache. Applies to all distributed members in which the data item is replicated.
- **destroy** - The data item’s key and value are both deleted. Applies to all distributed members in which the data item is replicated.
- **local invalidate** - Deletes the data item’s value. Applies only to the local member.
- **local destroy** - Deletes the data item’s key and value. Applies only to the local member.

You cannot use `local-destroy` or `local-invalidate` expiration actions in replicated or partitioned regions. You can use the local options only on distributed regions with a data-policy of empty, normal or preloaded.

**Entry Expiration in Replicated Regions and Partitioned Regions**

In replicated regions, entry updates are performed in the most convenient available copy of the data, then replicated to the other members, resetting their last-updated statistics to the same time. In partitioned regions, entry updates are always done in the primary copy, resetting the primary copy’s last-updated and last-accessed statistics, then the secondary copies are updated to match.

In both replicated and partitioned regions, entry retrieval uses the most convenient available copy of the data, which may be any of the distributed copies. Retrievals are not propagated to other members. Differences in last-access times are reconciled when the data item is considered for expiration.

Expiration can be triggered in any copy of a replicated region, if the time elapsed since the last update or read access exceeds the established threshold. Expiration in partitioned regions is executed in the primary copy, based on the primary’s last-accessed and last-updated statistics. In both cases, the expiration mechanism checks the last-accessed dates of all copies of the data item and updates the last-access date of all copies to the most recent last-accessed date. Then, if the elapsed time still puts the data item over the expiration threshold, the item is deleted in accordance with the expiration action specified for the region.

**Interaction Between Expiration Settings and netSearch**

Before `netSearch` retrieves an entry value from a remote cache, it validates the *remote* entry’s statistics against the *local* region’s expiration settings. Entries that would have already expired in the local cache are passed over. Once validated, the entry is brought into the local cache and the local access and update statistics are updated for the local copy. The last-accessed time is reset and the last-modified time is updated to the time in the remote cache, with corrections made for system clock differences. Thus the local entry is assigned the true last time the entry was modified in the cluster. The `netSearch` operation has no effect on the expiration counters in remote caches.

The `netSearch` method operates only on distributed regions with a data-policy of empty, normal and preloaded.



#### Configure Data Expiration

Configure the type of expiration and the expiration action to use.

- Expiration actions require setting the region attribute of `statistics-enabled` to `true`. This can be done in the region element of a `cache.xml` file, the `gfsh` command line, or through the API.
- Set the expiration attributes by expiration type, with the max times and expiration actions. See the region attributes listings for `entry-time-to-live`, `entry-idle-time`, `region-time-to-live`, and `region-idle-time` in [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes).

The statistics used for expiration are available directly to the application through the `CacheStatistics` object returned by the `Region` and `Region.Entry` `getStatistics` methods. The `CacheStatistics` object also provides a method for resetting the statistics counters.

**For partitioned regions:**

- On a partitioned region, expiration is supported only for the region’s entries, not for the region itself. Region-wide expiration attributes, such as `region-time-to-live` and `region-idle-time` do not apply to the data items in partitioned regions.
- To ensure reliable read behavior when working with partitioned regions, use the `entry-time-to-live` attribute, not the `entry-idle-time` attribute.
- You cannot use `local-destroy` or `local-invalidate` expiration actions in partitioned regions.

**Replicated regions example:**

```
// Setting standard expiration on an entry
<region-attributes statistics-enabled="true"> 
  <entry-idle-time> 
    <expiration-attributes timeout="60" action="local-invalidate"/> 
  </entry-idle-time> 
</region-attributes> 
```

- Override the region-wide settings for specific entries, if required by your application. To do this:

  1. Program a custom expiration class that implements `org.apache.geode.cache.CustomExpiry`. Example:

     ```
     // Custom expiration class
     // Use the key for a region entry to set entry-specific expiration timeouts of 
     //   10 seconds for even-numbered keys with a DESTROY action on the expired entries
     //   Leave the default region setting for all odd-numbered keys. 
     public class MyClass implements CustomExpiry, Declarable 
     { 
         private static final ExpirationAttributes CUSTOM_EXPIRY = 
                 new ExpirationAttributes(10, ExpirationAction.DESTROY); 
         public ExpirationAttributes getExpiry(Entry entry) 
         { 
             int key = (Integer)entry.getKey(); 
             return key % 2 == 0 ? CUSTOM_EXPIRY : null; 
         }
     }
     ```

  2. Define the class inside the expiration attributes settings for the region. Example:

     ```
     <!-- Set default entry idle timeout expiration for the region --> 
     <!-- Pass entries to custom expiry class for expiration overrides -->
     <region-attributes statistics-enabled="true"> 
         <entry-idle-time> 
             <expiration-attributes timeout="60"> 
                 <custom-expiry> 
                     <class-name>com.company.mypackage.MyClass</class-name> 
                 </custom-expiry> 
             </expiration-attributes> 
         </entry-idle-time> 
     </region-attributes>
     ```

  The gfsh equivalent of the above XML is:

  ```
  gfsh> create region --name=region1 --type=REPLICATE --enable-statistics \
  --entry-idle-time-expiration=60 --entry-idle-time-custom-expiry=com.company.mypackage.MyClass
  ```

- When the primary expires entries, it requests last-accessed statistics from the secondaries. The primary adopts the most recent access time and reschedules the expiration, if warranted. This is done only for distributed expiration actions, and applies to both partitioned and replicated regions.

You can also configure regions using the gfsh command-line interface. See [Region Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD).

**Configuring the Number of Threads for Expiration**

You can use the `gemfire.EXPIRY_THREADS` system property to increase the number of threads that handle expiration. By default, one thread handles expiration, and it is possible for the thread to become overloaded when entries expire faster than the thread can expire them. If a single thread is handling too many expirations, it can result in an OOME. Set the gemfire.EXPIRY_THREADS system property to the desired number when starting the cache server.



### Keeping the Cache in Sync with Outside Data Sources

Keep your distributed cache in sync with an outside data source by programming and installing application plug-ins for your region.

- **Overview of Outside Data Sources**

  Apache Geode has application plug-ins to read data into the cache and write it out.

- **Configuring Database Connections Using JNDI**.

  Use JNDI to maintain a connection pool that includes outside data sources.

- **How Data Loaders Work**

  By default, a region has no data loader defined. Plug an application-defined loader into any region by setting the region attribute cache-loader on the members that host data for the region.

- **Implement a Data Loader**

  Program a data loader and configure your region to use it.



#### Overview of Outside Data Sources

Apache Geode has application plug-ins to read data into the cache and write it out.

The application plug-ins:

1. Load data on cache misses using an implementation of a `org.apache.geode.cache.CacheLoader`. The `CacheLoader.load` method is called when the `get` operation can’t find the value in the cache. The value returned from the loader is put into the cache and returned to the `get`operation. You might use this in conjunction with data expiration to get rid of old data, and your other data loading applications, which might be prompted by events in the outside data source. See [Configure Data Expiration](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html).

2. Write data out to the data source using the cache event handlers, `CacheWriter` and `CacheListener`. For implementation details, see [Implementing Cache Event Handlers](https://geode.apache.org/docs/guide/17/developing/events/implementing_cache_event_handlers.html).

   Implementing Cache Event Handlers

   - `CacheWriter` is run synchronously. Before performing any operation on a region entry, if any cache writers are defined for the region in the cluster, the system invokes the most convenient writer. In partitioned and distributed regions, cache writers are usually defined in only a subset of the caches holding the region - often in only one cache. The cache writer can abort the region entry operation.
   - `CacheListener` is run synchronously after the cache is updated. This listener works only on local cache events, so install your listener in every cache where you want it to handle events. You can install multiple cache listeners in any of your caches.

In addition to using application plug-ins, you can also configure external JNDI database sources in your cache.xml and use these data sources in transactions. See [Configuring Database Connections Using JNDI](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html) for more information.



#### Configuring Database Connections Using JNDI

To connect to external databases, for example when using JTA transactions, you can configure database JNDI data sources in `cache.xml`. The `DataSource` object points to either a JDBC connection or, more commonly, a JDBC connection pool. The connection pool is usually preferred, because a program can use and reuse a connection as long as necessary and then free it for another thread to use.

The following list shows `DataSource` connection types used in JTA transactions:

- **XAPooledDataSource**. Pooled SQL connections.
- **ManagedDataSource**. JNDI binding type for the J2EE Connector Architecture (JCA) ManagedConnectionFactory.
- **PooledDataSource**. Pooled SQL connections.
- **SimpleDataSource**. Single SQL connection. No pooling of SQL connections is done. Connections are generated on the fly and cannot be reused.

The `jndi-name` attribute of the `jndi-binding` element is the key binding parameter. If the value of `jndi-name` is a DataSource, it is bound as `java:/`*myDatabase*, where *myDatabase* is the name you assign to your data source. If the data source cannot be bound to JNDI at runtime, Geode logs a warning. For information on the `DataSource` interface, see: <http://docs.oracle.com/javase/8/docs/api/javax/sql/DataSource.html>

Geode supports JDBC 2.0 and 3.0.

**Note:** Include any data source JAR files in your CLASSPATH.

**Example DataSource Configurations in cache.xml**

The following sections show example `cache.xml` files configured for each of the `DataSource`connection types.

**XAPooledDataSource cache.xml Example (Derby)**

The example shows a `cache.xml` file configured for a pool of `XAPooledDataSource` connections connected to the data resource `newDB`.

The log-in and blocking timeouts are set lower than the defaults. The connection information, including `user-name` and `password`, is set in the `cache.xml` file, instead of waiting until connection time. The password is not encrypted.

When specifying the configuration properties for JCA-implemented database drivers that support XA transactions (in other words, **XAPooledDataSource**), you must use configuration properties to define the datasource connection instead of the `connection-url` attribute of the `<jndi-binding>`element. Configuration properties differ depending on your database vendor. Specify JNDI binding properties through the `config-property` tag, as shown in this example. You can add as many `config-property` tags as required.

```
<?xml version="1.0" encoding="UTF-8"?>
<cache
    xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0"
    lock-lease="120" lock-timeout="60" search-timeout="300"> 
   <region name="root">
      <region-attributes scope="distributed-no-ack" data-policy="cached" initial-capacity="16"
load-factor="0.75" concurrency-level="16" statistics-enabled="true">
    . . .
   </region>
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
    password="thecleartextpassword">
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
</cache>
```

**JNDI Binding Configuration Properties for Different XAPooledDataSource Connections**

The following are some example data source configurations for different databases. Consult your vendor database’s documentation for additional details.

**MySQL**

```
...
<jndi-bindings>
   <jndi-binding type="XAPooledDataSource" 
    ...
    xa-datasource-class="com.mysql.jdbc.jdbc2.optional.MysqlXADataSource">
    <config-property>
    <config-property-name>URL</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>"jdbc:mysql://mysql-servername:3306/databasename"</config-property-value>
    </config-property>
    ...
    </jndi-binding>
    ...
</jndi-bindings>
```

**PostgreSQL**

```
...
<jndi-bindings>
   <jndi-binding type="XAPooledDataSource" 
    ...
    xa-datasource-class="org.postgresql.xa.PGXADataSource">
    <config-property>
    <config-property-name>ServerName</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>postgresql-hostname</config-property-value>
    </config-property>
    <config-property>
    <config-property-name>DatabaseName</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>postgresqldbname</config-property-value>
    </config-property>
    ...
   </jndi-binding>
    ...
</jndi-bindings>
```

**Oracle**

```
...
<jndi-bindings>
   <jndi-binding type="XAPooledDataSource" 
    ...
    xa-datasource-class="oracle.jdbc.xa.client.OracleXADataSource">
    <config-property>
    <config-property-name>URL</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>jdbc:oracle:oci8:@tc</config-property-value>
    </config-property>
    ...
    </jndi-binding>
    ...
</jndi-bindings>
```

**Microsoft SQL Server**

```
...
<jndi-bindings>
   <jndi-binding type="XAPooledDataSource" 
      ...
    xa-datasource-class="com.microsoft.sqlserver.jdbc.SQLServerXADataSource">
    <config-property>
    <config-property-name>ServerName</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>mysqlserver</config-property-value>
    </config-property>
    <config-property>
    <config-property-name>DatabaseName</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>databasename</config-property-value>
    </config-property>
    <config-property>
    <config-property-name>SelectMethod</config-property-name>
    <config-property-type>java.lang.String</config-property-type>
    <config-property-value>cursor</config-property-value>
    </config-property>
    ...
    </jndi-binding>
    ...
</jndi-bindings>
```

**ManagedDataSource Connection Example (Derby)**

`ManagedDataSource` connections for the JCA `ManagedConnectionFactory` are configured as shown in the example. This configuration is similar to `XAPooledDataSource` connections, except the type is `ManagedDataSource`, and you specify a `managed-conn-factory-class` instead of an `xa-datasource-class`.

```
<?xml version="1.0"?>
<cache xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0"
    lock-lease="120" 
    lock-timeout="60"
    search-timeout="300">
   <region name="root">
      <region-attributes scope="distributed-no-ack" data-policy="cached" initial-capacity="16"
load-factor="0.75" concurrency-level="16" statistics-enabled="true">
      . . .
    </region>
    <jndi-bindings>
      <jndi-binding type="ManagedDataSource" 
    jndi-name="DB3managed" 
    init-pool-size="20" 
    max-pool-size="100" 
    idle-timeout-seconds="20" 
    blocking-timeout-seconds="5" 
    login-timeout-seconds="10"
    managed-conn-factory-class="com.myvendor.connection.ConnFactory"
    user-name="mitul"  
    password="thecleartextpassword">
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
 </cache>
```

**PooledDataSource Example (Derby)**

Use the `PooledDataSource` and `SimpleDataSource` connections for operations executed outside of any transaction. This example shows a `cache.xml` file configured for a pool of `PooledDataSource`connections to the data resource `newDB`. For this non-transactional connection pool, the log-in and blocking timeouts are set higher than for the transactional connection pools in the two previous examples. The connection information, including `user-name` and `password`, is set in the `cache.xml` file, instead of waiting until connection time. The password is not encrypted.

```
<?xml version="1.0"?>
<cache xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0"
    lock-lease="120"
    lock-timeout="60"
    search-timeout="300">
    <region name="root">
         <region-attributes scope="distributed-no-ack" data-policy="cached" 
initial-capacity="16" load-factor="0.75" concurrency-level="16" statistics-enabled="true">
            . . .
    </region>
    <jndi-bindings>
      <jndi-binding
    type="PooledDataSource"
    jndi-name="newDB1" 
    init-pool-size="2" 
    max-pool-size="7" 
    idle-timeout-seconds="20" 
    blocking-timeout-seconds="20"
    login-timeout-seconds="30" 
    conn-pooled-datasource-class="org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource"
    user-name="mitul"
    password="thecleartextpassword">
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
</cache>
```

**SimpleDataSource Connection Example (Derby)**

The example below shows a very basic configuration in the `cache.xml` file for a `SimpleDataSource`connection to the data resource `oldDB`. You only need to configure a few properties like a `jndi-name` for this connection pool, `oldDB1`, and the `databaseName`, `oldDB`. This password is in clear text.

A simple data source connection does not generally require vendor-specific property settings. If you need them, add `config-property` tags as shown in the earlier examples.

```
<?xml version="1.0"?>
<cache xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0"
    lock-lease="120" 
    lock-timeout="60" 
    search-timeout="300">
   <region name="root">
      <region-attributes scope="distributed-no-ack" data-policy="cached" initial-capacity="16"
load-factor="0.75" concurrency-level="16" statistics-enabled="true">
        . . .
      </region-attributes>   
    </region>
    <jndi-bindings>
      <jndi-binding type="SimpleDataSource"
    jndi-name="oldDB1" 
    jdbc-driver-class="org.apache.derby.jdbc.EmbeddedDriver"
    user-name="mitul" 
    password="thecleartextpassword" 
    connection-url="jdbc:derby:newDB;create=true">
        . . .
       </jndi-binding>
   </jndi-bindings>
</cache>
```



#### How Data Loaders Work

By default, a region has no data loader defined. Plug an application-defined loader into any region by setting the region attribute cache-loader on the members that host data for the region.

The loader is called on cache misses during get operations, and it populates the cache with the new entry value in addition to returning the value to the calling thread.

A loader can be configured to load data into the Geode cache from an outside data store. To do the reverse operation, writing data from the Geode cache to an outside data store, use a cache writer event handler. See [Implementing Cache Event Handlers](https://geode.apache.org/docs/guide/17/developing/events/implementing_cache_event_handlers.html).

How to install your cache loader depends on the type of region.

**Data Loading in Partitioned Regions**

Because of the huge amounts of data they can handle, partitioned regions support partitioned loading. Each cache loader loads only the data entries in the member where the loader is defined. If data redundancy is configured, data is loaded only if the member holds the primary copy. So you must install a cache loader in every member where the partitioned attributes `local-max-memory` is not zero.

If you depend on a JDBC connection, every data store must have a connection to the data source, as shown in the following figure. Here the three members require three connections. See [Configuring Database Connections Using JNDI](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html) for information on how to configure data sources.

**Note:** Partitioned regions generally require more JDBC connections than distributed regions.

![img](assets/cache_data_loader-1545192090096.svg)


**Data Loading in Distributed Regions**

In a non-partitioned distributed region, a cache loader defined in one member is available to all members that have the region defined. Loaders are usually defined in just a subset of the caches holding the region. When a loader is needed, all available loaders for the region are invoked, starting with the most convenient loader, until the data is loaded or all loaders have been tried.

In the following figure, these members of one cluster can be running on different machines. Loading for the distributed region is performed from M1.

![img](assets/cache_data_loader_2-1545192090106.svg)

**Data Loading in Local Regions**

For local regions, the cache loader is available only in the member where it is defined. If a loader is defined, it is called whenever a value is not found in the local cache.



#### Implement a Data Loader

To use a data loader:

1. Implement the `org.apache.geode.cache.CacheLoader` interface.
2. Configure and deploy the implementation.

**Implement the CacheLoader Interface**

For a get operation, if the key is not in the cache, the thread serving the get operation invokes the `CacheLoader.load` method. Implement `load` to return the value for the key, which will be placed into the region in addition to being returned to the caller.

`org.apache.geode.cache.CacheLoader` inherits from `Declarable`, so implement the `Declarable.initialize` method if your `CacheLoader` implementation needs to be initialized with some arguments. Specify the required arguments either in your `cache.xml` file or in a gfsh `create region` or `alter region` command. Do not define the `Declarable.init()` method; it is deprecated.

Here is an example implementation:

```
public class SimpleCacheLoader implements CacheLoader {
    public Object load(LoaderHelper helper) {
        String key = (String) helper.getKey();

        // Return an entry value created from the key, assuming that
        // all keys are of the form "key1", "key2", "keyN"
        return "LoadedValue" + key.substring(3);
    }
}
```

If you need to run `Region` API calls from your implementation, spawn separate threads for them. Do not make direct calls to `Region` methods from your `load` method, as it could cause the cache loader to block, hurting the performance of the cluster.

**Configure and Deploy**

Use one of these three ways to configure and deploy the cache loader:

**Option 1:** If configuring a cluster by defining a `cache.xml` file, deploy by adding the cache loader to the classpath when starting servers.

Here is an example configuration within the `cache.xml` file that specifies the loader without arguments:

```
<region-attributes>
    <cache-loader>
        <class-name>myLoader</class-name>
    </cache-loader>
</region-attributes>
```

Or, here is an example configuration within the `cache.xml` file that specifies the loader with an argument:

```
<cache-loader>
    <class-name>com.company.data.DatabaseLoader</class-name>
    <parameter name="URL">
        <string>jdbc:cloudscape:rmi:MyData</string>
    </parameter>
</cache-loader>
```

To deploy the JAR file, add the cache loader JAR file to the classpath when starting servers. For example:

```
gfsh>start server --name=s2 --classpath=/var/data/lib/myLoader.jar
```

**Option 2:** If deploying the JAR file at server startup, add the JAR file to the classpath and use gfsh to apply the configuration to the region.

To deploy the JAR file, add the cache loader JAR file to the classpath when starting servers. For example:

```
gfsh>start server --name=s2 --classpath=/var/data/lib/myLoader.jar
```

Use gfsh to apply the configuration of the `CacheLoader` implementation to the region with `gfsh create region` or `gfsh alter region`. Here is an example of region creation without arguments:

```
gfsh>create region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

Here is an example of region creation with an argument:

```
gfsh>create region --name=r3 \
--cache-loader=com.example.appname.myCacheLoader{'URL':'jdbc:cloudscape:rmi:MyData'}
```

Here is an example of altering a region:

```
gfsh>alter region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

**Option 3 applies to partitioned regions:** If deploying the JAR file with the gfsh deploy command after servers have been started, use gfsh to apply the configuration to the region.

After server creation use gfsh to deploy the JAR file to all the servers. For example:

```
gfsh>deploy --jars=/var/data/lib/myLoader.jar
```

We do not generally use the gfsh deploy command when the servers host replicated regions, as detailed in [How Data Loaders Work](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/how_data_loaders_work.html).

Use gfsh to apply the configuration of the `CacheLoader` implementation to the region with `gfsh create region` or `gfsh alter region`. Here is an example of region creation without arguments:

```
gfsh>create region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

Here is an example of region creation with an argument:

```
gfsh>create region --name=r3 \
--cache-loader=com.example.appname.myCacheLoader{'URL':'jdbc:cloudscape:rmi:MyData'}
```

Here is an example of altering a region:

```
gfsh>alter region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

**Implementing a Server or Peer with a Cache Loader**

Servers and peers with an embedded cache can configure a cache loader in only the members where it makes sense to do so. The design might, for example, assign the job of loading from a database to one or two members for a region hosted by many more members. This can be done to reduce the number of connections when the outside source is a database.

Implement the `org.apache.geode.cache.CacheLoader` interface. Region creation configures the the cache loader as in this example:

```
RegionFactory<String,Object> rf = cache.createRegionFactory(REPLICATE);
rf.setCacheLoader(new QuoteLoader());
quotes = rf.create("NASDAQ-Quotes");
```



## Data Serialization

Data that you manage in Geode must be serialized and deserialized for storage and transmittal between processes. You can choose among several options for data serialization.

- **Overview of Data Serialization**

  Geode offers serialization options other than Java serialization that give you higher performance and greater flexibility for data storage, transfers, and language types.

- **Geode PDX Serialization**

  Geode’s Portable Data eXchange (PDX) is a cross-language data format that can reduce the cost of distributing and serializing your objects. PDX stores data in named fields that you can access individually, to avoid the cost of deserializing the entire data object. PDX also allows you to mix versions of objects where you have added or removed fields.

- **Geode Data Serialization (DataSerializable and DataSerializer)**

  Geode’s `DataSerializable` interface gives you quick serialization of your objects.

- **Standard Java Serialization**

  You can use standard Java serialization for data you only distribute between Java applications. If you distribute your data between non-Java clients and Java servers, you need to do additional programming to get the data between the various class formats.



### Overview of Data Serialization

Geode offers serialization options other than Java serialization that give you higher performance and greater flexibility for data storage, transfers, and language types.

All data that Geode moves out of the local cache must be serializable. However, you do not necessarily need to implement `java.io.Serializable` since other serialization options are available in Geode. Region data that must be serializable falls under the following categories:

- Partitioned regions
- Distributed regions
- Regions that are persisted or overflowed to disk
- Server or client regions in a client/server installation
- Regions configured with a gateway sender for distributing events in a multi-site installation
- Regions that receive events from remote caches
- Regions that provide function arguments and results

**Note:** If you are storing objects with the [HTTP Session Management Modules](https://geode.apache.org/docs/guide/17/tools_modules/http_session_mgmt/chapter_overview.html), these objects must be serializable since they are serialized before being stored in the region.

To minimize the cost of serialization and deserialization, Geode avoids changing the data format whenever possible. This means your data might be stored in the cache in serialized or deserialized form, depending on how you use it. For example, if a server acts only as a storage location for data distribution between clients, it makes sense to leave the data in serialized form, ready to be transmitted to clients that request it. Partitioned region data is always initially stored in serialized form.

**Data Serialization Options**

With Geode, you have the option to serialize your domain objects automatically or to implement serialization using one of Geode’s interfaces. Enabling automatic serialization means that domain objects are serialized and deserialized without your having to make any code changes to those objects. This automatic serialization is performed by registering your domain objects with a custom `PdxSerializer` called the `ReflectionBasedAutoSerializer`, which uses Java reflection to infer which fields to serialize.

If autoserialization does not meet your needs, you can serialize your objects by implementing one of the Geode interfaces, `PdxSerializable` or `DataSerializable`. You can use these interfaces to replace any standard Java data serialization for better performance. If you cannot or do not want to modify your domain classes, each interface has an alternate serializer class, `PdxSerializer` and `DataSerializer`. To use these, you create your custom serializer class and then associate it with your domain class in the Geode cache configuration.

Geode Data serialization is about 25% faster than PDX serialization, however using PDX serialization will help you to avoid the even larger costs of performing deserialization.



| Capability                                                   | Geode Data Serializable | Geode PDX Serializable |
| ------------------------------------------------------------ | ----------------------- | ---------------------- |
| Implements Java Serializable.                                | X                       |                        |
| Handles multiple versions of application domain objects, providing the versions differ by the addition or subtraction of fields. |                         | X                      |
| Provides single field access of serialized data, without full deserialization - supported also for OQL querying. |                         | X                      |
| Automatically ported to other languages by Geode             |                         | X                      |
| Works with .NET clients.                                     | X                       | X                      |
| Works with C++ clients.                                      | X                       | X                      |
| Works with Geode delta propagation.                          | X                       | X (See note below.)    |

**Table 1.** Serialization Options: Comparison of Features

**Note:** By default, you can use Geode delta propagation with PDX serialization. However, delta propagation will not work if you have set the Geode property `read-serialized` to “true”. In terms of deserialization, to apply a change delta propagation requires a domain class instance and the `fromDelta`method. If you have set `read-serialized` to true, then you will receive a `PdxInstance`instead of a domain class instance and `PdxInstance` does not have the `fromDelta` method required for delta propagation.

**Differences between Geode Serialization (PDX or Data Serializable) and Java Serialization**

Geode serialization (either PDX Serialization or Data Serialization) does not support circular object graphs whereas Java serialization does. In Geode serialization, if the same object is referenced more than once in an object graph, the object is serialized for each reference, and deserialization produces multiple copies of the object. By contrast in this situation, Java serialization serializes the object once and when deserializing the object, it produces one instance of the object with multiple references.



### Geode PDX Serialization

Geode’s Portable Data eXchange (PDX) is a cross-language data format that can reduce the cost of distributing and serializing your objects. PDX stores data in named fields that you can access individually, to avoid the cost of deserializing the entire data object. PDX also allows you to mix versions of objects where you have added or removed fields.

- **Geode PDX Serialization Features**

  Geode PDX serialization offers several advantages in terms of functionality.

- **High Level Steps for Using PDX Serialization**

  To use PDX serialization, you can configure and use Geode’s reflection-based autoserializer, or you can program the serialization of your objects by using the PDX interfaces and classes.

- **Using Automatic Reflection-Based PDX Serialization**

  You can configure your cache to automatically serialize and deserialize domain objects without having to add any extra code to them.

- **Serializing Your Domain Object with a PdxSerializer**

  For a domain object that you cannot or do not want to modify, use the `PdxSerializer` class to serialize and deserialize the object’s fields. You use one `PdxSerializer` implementation for the entire cache, programming it for all of the domain objects that you handle in this way.

- **Implementing PdxSerializable in Your Domain Object**

  For a domain object with source that you can modify, implement the `PdxSerializable` interface in the object and use its methods to serialize and deserialize the object’s fields.

- **Programming Your Application to Use PdxInstances**

  A `PdxInstance` is a light-weight wrapper around PDX serialized bytes. It provides applications with run-time access to fields of a PDX serialized object.

- **Adding JSON Documents to the Geode Cache**

  The `JSONFormatter` API allows you to put JSON formatted documents into regions and retrieve them later by storing the documents internally as PdxInstances.

- **Using PdxInstanceFactory to Create PdxInstances**

  You can use the `PdxInstanceFactory` interface to create a `PdxInstance` from raw data when the domain class is not available on the server.

- **Persisting PDX Metadata to Disk**

  Geode allows you to persist PDX metadata to disk and specify the disk store to use.

- **Using PDX Objects as Region Entry Keys**

  Using PDX objects as region entry keys is highly discouraged.



#### Geode PDX Serialization Features

Geode PDX serialization offers several advantages in terms of functionality.

**Application Versioning of PDX Domain Objects**

Domain objects evolve along with your application code. You might create an address object with two address lines, then realize later that a third line is required for some situations. Or you might realize that a particular field is not used and want to get rid of it. With PDX, you can use old and new versions of domain objects together in a cluster if the versions differ by the addition or removal of fields. This compatibility lets you gradually introduce modified code and data into the cluster, without bringing the cluster down.

Geode maintains a central registry of the PDX domain object metadata. Using the registry, Geode preserves fields in each member’s cache regardless of whether the field is defined. When a member receives an object with a registered field that the member is not aware of, the member does not access the field, but preserves it and passes it along with the entire object to other members. When a member receives an object that is missing one or more fields according to the member’s version, Geode assigns the Java default values for the field types to the missing fields.

**Portability of PDX Serializable Objects**

When you serialize an object using PDX, Geode stores the object’s type information in the central registry. The information is passed among clients and servers, peers, and clusters.

This centralization of object type information is advantageous for client/server installations in which clients and servers are written in different languages. Clients pass registry information to servers automatically when they store a PDX serialized object. Clients can run queries and functions against the data in the servers without compatibility between server and the stored objects. One client can store data on the server to be retrieved by another client, with no requirements on the part of the server.

**Reduced Deserialization of Serialized Objects**

The access methods of PDX serialized objects allow you to examine specific fields of your domain object without deserializing the entire object. Depending on your object usage, you can reduce serialization and deserialization costs significantly.

Java and other clients can run queries and execute functions against the objects in the server caches without deserializing the entire object on the server side. The query engine automatically recognizes PDX objects, retrieves the `PdxInstance` of the object and uses only the fields it needs. Likewise, peers can access only the necessary fields from the serialized object, keeping the object stored in the cache in serialized form.



#### High Level Steps for Using PDX Serialization

To use PDX serialization, you can configure and use Geode’s reflection-based autoserializer, or you can program the serialization of your objects by using the PDX interfaces and classes.

Optionally, program your application code to deserialize individual fields out of PDX representations of your serialized objects. You may also need to persist your PDX metadata to disk for recovery on startup.

**Procedure**

1. Use one of these serialization options for each object type that you want to serialize using PDX serialization:

   - [Using Automatic Reflection-Based PDX Serialization](https://geode.apache.org/docs/guide/17/developing/data_serialization/auto_serialization.html)
   - [Serializing Your Domain Object with a PdxSerializer](https://geode.apache.org/docs/guide/17/developing/data_serialization/use_pdx_serializer.html)
   - [Implementing PdxSerializable in Your Domain Object](https://geode.apache.org/docs/guide/17/developing/data_serialization/use_pdx_serializable.html)

2. To ensure that your servers do not need to load the application classes, set the `pdx` `read-serialized` attribute to true. In gfsh, execute the following command before starting up your servers:

   ```
   gfsh>configure pdx --read-serialized=true
   ```

   By using gfsh, this configuration can propagated across the cluster through the [Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html). Alternately, you would need to configure `pdx read-serialized` in each server’s `cache.xml` file.

3. If you are storing any Geode data on disk, then you must configure PDX serialization to use persistence. See [Persisting PDX Metadata to Disk](https://geode.apache.org/docs/guide/17/developing/data_serialization/persist_pdx_metadata_to_disk.html) for more information.

4. (Optional) Wherever you run explicit application code to retrieve and manage your cached entries, you may want to manage your data objects without using full deserialization. To do this, see [Programming Your Application to Use PdxInstances](https://geode.apache.org/docs/guide/17/developing/data_serialization/program_application_for_pdx.html).

**PDX and Multi-Site (WAN) Deployments**

For multisite (WAN) installations only– if you will use PDX serialization in any of your WAN-enabled regions, for each cluster, you must choose a unique integer between 0 (zero) and 255 and set the `distributed-system-id` in every member’s `gemfire.properties` file. See [Configuring a Multi-site (WAN) System](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html).



#### Using Automatic Reflection-Based PDX Serialization

You can configure your cache to automatically serialize and deserialize domain objects without having to add any extra code to them.

You can automatically serialize and deserialize domain objects without coding a `PdxSerializer`class. You do this by registering your domain objects with a custom `PdxSerializer` called `ReflectionBasedAutoSerializer` that uses Java reflection to infer which fields to serialize.

You can also extend the ReflectionBasedAutoSerializer to customize its behavior. For example, you could add optimized serialization support for BigInteger and BigDecimal types. See [Extending the ReflectionBasedAutoSerializer](https://geode.apache.org/docs/guide/17/developing/data_serialization/extending_the_autoserializer.html#concept_9E020566EE794A81A48A90BA798EC279) for details.

**Note:** Your custom PDX autoserializable classes cannot use the `org.apache.geode` package. If they do, the classes will be ignored by the PDX auto serializer.



**Prerequisites**

- Understand generally how to configure the Geode cache.
- Understand how PDX serialization works and how to configure your application to use `PdxSerializer`.

**Procedure**

In your application where you manage data from the cache, provide the following configuration and code as appropriate:

1. In the domain classes that you wish to autoserialize, make sure each class has a zero-arg constructor. For example:

   ```
   public PortfolioPdx(){}
   ```

2. Using one of the following methods, set the PDX serializer to `ReflectionBasedAutoSerializer`.

   1. In gfsh, execute the following command prior to starting up any members that host data:

      ```
      gfsh>configure pdx --auto-serializable-classes=com\.company\.domain\..*
      ```

      By using gfsh, this configuration can propagated across the cluster through the [Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

   2. Alternately, in `cache.xml`:

      ```
      <!-- Cache configuration configuring auto serialization behavior -->
      <cache>
        <pdx>
          <pdx-serializer>
            <class-name>
             org.apache.geode.pdx.ReflectionBasedAutoSerializer
            </class-name>
            <parameter name="classes">
            <string>com.company.domain.DomainObject</string>
           </parameter>
        </pdx-serializer>
       </pdx>
        ...
      </cache>
      ```

      The parameter, `classes`, takes a comma-separated list of class patterns to define the domain classes to serialize. If your domain object is an aggregation of other domain classes, you need to register the domain object and each of those domain classes explicitly for the domain object to be serialized completely.

   3. Using the Java API:

      ```
      Cache c = new CacheFactory()
        .setPdxSerializer(new ReflectionBasedAutoSerializer("com.company.domain.DomainObject"))
        .create();
      ```

3. Customize the behavior of the `ReflectionBasedAutoSerializer` using one of the following mechanisms:

   - By using a class pattern string to specify the classes to auto-serialize and customize how the classes are serialized. Class pattern strings can be specified in the API by passing strings to the `ReflectionBasedAutoSerializer` constructor or by specifying them in cache.xml. See [Customizing Serialization with Class Pattern Strings](https://geode.apache.org/docs/guide/17/developing/data_serialization/autoserialization_with_class_pattern_strings.html#concept_9B67BBE94B414B7EA63BD7E8D61D0312) for details.
   - By creating a subclass of `ReflectionBasedAutoSerializer` and overriding specific methods. See [Extending the ReflectionBasedAutoSerializer](https://geode.apache.org/docs/guide/17/developing/data_serialization/extending_the_autoserializer.html#concept_9E020566EE794A81A48A90BA798EC279) for details.

4. If desired, configure the `ReflectionBasedAutoSerializer` to check the portability of the objects it is passed before it tries to autoserialize them. When this flag is set to true, the `ReflectionBasedAutoSerializer` will throw a `NonPortableClassException` error when trying to autoserialize a non-portable object. To set this, use the following configuration:

   - In gfsh, use the following command:

     ```
     gfsh>configure pdx --portable-auto-serializable-classes=com\.company\.domain\..*
     ```

     By using gfsh, this configuration can propagated across the cluster through the [Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

   - In cache.xml:

     ```
     <!-- Cache configuration configuring auto serialization behavior -->
     <cache>
       <pdx>
         <pdx-serializer>
           <class-name>
            org.apache.geode.pdx.ReflectionBasedAutoSerializer
           </class-name>
         <parameter name="classes">
           <string>com.company.domain.DomainObject</string>
         </parameter>
         <parameter name="check-portability">
           <string>true</string>
         </parameter>
       </pdx-serializer>
      </pdx>
       ...
     </cache>
     ```

   - Using the Java API:

     ```
     Cache c = new CacheFactory()
       .setPdxSerializer(new ReflectionBasedAutoSerializer(true,"com.company.domain.DomainObject"))
       .create();
     ```

For each domain class you provide, all fields are considered for serialization except those defined as `static` or `transient` and those you explicitly exclude using the class pattern strings.

**Note:** The `ReflectionBasedAutoSerializer` traverses the given domain object’s class hierarchy to retrieve all fields to be considered for serialization. So if `DomainObjectB` inherits from `DomainObjectA`, you only need to register `DomainObjectB` to have all of `DomainObjectB` serialized.



##### Customizing Serialization with Class Pattern Strings

Use class pattern strings to name the classes that you want to serialize using Geode’s reflection-based autoserializer and to specify object identity fields and to specify fields to exclude from serialization.

The class pattern strings used to configured the `ReflectionBasedAutoSerializer` are standard regular expressions. For example, this expression would select all classes defined in the `com.company.domain` package and its subpackages:

```
com\.company\.domain\..*
```

You can augment the pattern strings with a special notation to define fields to exclude from serialization and to define fields to mark as PDX identity fields. The full syntax of the pattern string is:

```
<class pattern> [# (identity|exclude) = <field pattern>]... [, <class pattern>...]
```

The following example pattern string sets these PDX serialization criteria:

- Classes with names matching the pattern `com.company.DomainObject.*` are serialized. In those classes, fields beginning with `id` are marked as identity fields and fields named `creationDate`are not serialized.
- The class `com.company.special.Patient` is serialized. In the class, the field, `ssn` is marked as an identity field

```
com.company.DomainObject.*#identity=id.*#exclude=creationDate, 
com.company.special.Patient#identity=ssn
```

**Note:** There is no association between the `identity` and `exclude` options, so the pattern above could also be expressed as:

```
com.company.DomainObject.*#identity=id.*, com.company.DomainObject.*#exclude=creationDate, 
com.company.special.Patient#identity=ssn
```

**Note:** The order of the patterns is not relevant. All defined class patterns are used when determining whether a field should be considered as an identity field or should be excluded.

Examples:

- This XML uses the example pattern shown above:

  ```
  <parameter name="classes">
    <string>com.company.DomainObject.*#identity=id.*#exclude=creationDate, 
  com.company.special.Patient#identity=ssn</string>
  </parameter>
  ```

- This application code sets the same pattern:

  ```
  classPatterns.add("com.company.DomainObject.*#identity=id.*#exclude=creationDate,
     com.company.special.Patient#identity=ssn");
  ```

- This application code has the same effect:

  ```
   Cache c = new CacheFactory().set("cache-xml-file", cacheXmlFileName)
       .setPdxSerializer(new ReflectionBasedAutoSerializer("com.foo.DomainObject*#identity=id.*",
           "com.company.DomainObject.*#exclude=creationDate","com.company.special.Patient#identity=ssn"))
       .create();
  ```



##### Extending the ReflectionBasedAutoSerializer

You can extend the `ReflectionBasedAutoSerializer` to handle serialization in a customized manner. This section provides an overview of the available method-based customization options and an example of extending the serializer to support BigDecimal and BigInteger types.

**Reasons to Extend the ReflectionBasedAutoSerializer**

One of the main use cases for extending the `ReflectionBasedAutoSerializer` is that you want it to handle an object that would currently need to be handled by standard Java serialization. There are several issues with having to use standard Java serialization that can be addressed by extending the PDX `ReflectionBasedAutoSerializer`.

- Each time we transition from a Geode serialized object to an object that will be Java I/O serialized, extra data must get serialized. This can cause a great deal of serialization overhead. This is why it is worth extending the `ReflectionBasedAutoSerializer` to handle any classes that normally would have to be Java I/O serialized.
- Expanding the number of classes that can use the `ReflectionBasedAutoSerializer` is beneficial when you encounter object graphs. After we use Java I/O serialization on an object, any objects under that object in the object graph will also have to be Java I/O serialized. This includes objects that normally would have been serialized using PDX or `DataSerializable`.
- If standard Java I/O serialization is done on an object and you have enabled check-portability, then an exception will be thrown. Even if you are not concerned with the object’s portability, you can use this flag to find out what classes would use standard Java serialization (by getting an exception on them) and then enhancing your auto serializer to handle them.

**Overriding ReflectionBasedAutoSerializer Behavior**

You can customize the specific behaviors in `ReflectionBasedAutoSerializer` by overriding the following methods:

- **isClassAutoSerialized** customizes which classes to autoserialize.
- **isFieldIncluded** specifies which fields of a class to autoserialize.
- **getFieldName** defines the specific field names that will be generated during autoserialization.
- **isIdentifyField** controls which field is marked as the identity field. Identity fields are used when a PdxInstance computes its hash code to determine whether it is equal to another object.
- **getFieldType** determines the field type that will be used when autoserializing the given field.
- **transformFieldValue** controls whether specific field values of a PDX object can be transformed during serialization.
- **writeTransform** controls what field value is written during auto serialization.
- **readTransform** controls what field value is read during auto deserialization.

These methods are only called the first time the `ReflectionBasedAutoSerializer` sees a new class. The results will be remembered and used the next time the same class is seen.

For details on these methods and their default behaviors, see the JavaDocs on [ReflectionBasedAutoSerializer](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/pdx/ReflectionBasedAutoSerializer.html) for details.

**Example of Optimizing Autoserialization of BigInteger and BigDecimal Types**

This section provides an example of extending the `ReflectionBasedAutoSerializer` to optimize the automatic serialization of BigInteger and BigDecimal types.

The following code sample illustrates a subclass of the `ReflectionBasedAutoSerializer` that optimizes BigInteger and BigDecimal autoserialization:

```
public static class BigAutoSerializer extends ReflectionBasedAutoSerializer {
   public BigAutoSerializer(Boolean checkPortability, string… patterns) {
    super(checkPortability, patterns);
}

@Override
public FieldType get FieldType(Field f, Class<?> clazz) {
   if (f.getType().equals(BigInteger.class)) {
        return FieldType.BYTE_ARRAY; 
      } else if (f.getType().equals(BigDecimal.class)) {
        return FieldType.STRING; 
      } else {
        return super.getFieldType(f, clazz);
      }
    }
@Override
    public boolean transformFieldValue(Field f, Class<?> clazz) {
      if (f.getType().equals(BigInteger.class)) {
        return true;
      } else if (f.getType().equals(BigDecimal.class)) {
        return true;
      } else {
        return super.transformFieldValue(f, clazz);
      }
    }

@Override
    public Object writeTransform(Field f, Class<?> clazz, Object originalValue) {
      if (f.getType().equals(BigInteger.class)) {
        byte[] result = null;
        if (originalValue != null) {
          BigInteger bi = (BigInteger)originalValue;
          result = bi.toByteArray();
        }
        return result;
      } else if (f.getType().equals(BigDecimal.class)) {
        Object result = null;
        if (originalValue != null) {
          BigDecimal bd = (BigDecimal)originalValue;
          result = bd.toString();
        }
        return result;
      } else {
        return super.writeTransform(f, clazz, originalValue);
      }
    }

@Override
    public Object readTransform(Field f, Class<?> clazz, Object serializedValue) {
      if (f.getType().equals(BigInteger.class)) {
        BigInteger result = null;
        if (serializedValue != null) {
          result = new BigInteger((byte[])serializedValue);
        }
        return result;
      } else if (f.getType().equals(BigDecimal.class)) {
        BigDecimal result = null;
        if (serializedValue != null) {
          result = new BigDecimal((String)serializedValue);
        }
        return result;
      } else {
        return super.readTransform(f, clazz, serializedValue);
      }
    }

  }
```



#### Serializing Your Domain Object with a PdxSerializer

For a domain object that you cannot or do not want to modify, use the `PdxSerializer` class to serialize and deserialize the object’s fields. You use one `PdxSerializer` implementation for the entire cache, programming it for all of the domain objects that you handle in this way.

With `PdxSerializer`, you leave your domain object as-is and handle the serialization and deserialization in the separate serializer. You register the serializer in your cache PDX configuration. Program the serializer to handle all of the domain objects you need.

If you write your own `PdxSerializer` and you also use the `ReflectionBasedAutoSerializer`, then the `PdxSerializer` needs to own the `ReflectionBasedAutoSerializer` and delegate to it. A Cache can only have a single `PdxSerializer` instance.

**Note:** The `PdxSerializer` `toData` and `fromData` methods differ from those for `PdxSerializable`. They have different parameters and results.

**Procedure**

1. In the domain classes that you wish to PDX serialize, make sure each class has a zero-arg constructor. For example:

   ```
   public PortfolioPdx(){}
   ```

2. If you have not already implemented `PdxSerializer` for some other domain object, perform these steps:

   1. Create a new class as your cache-wide serializer and make it implement `PdxSerializer`. If you want to declare your new class in the `cache.xml` file, have it also implement `Declarable`.

      Example:

      ```
      import org.apache.geode.cache.Declarable;
      import org.apache.geode.pdx.PdxReader;
      import org.apache.geode.pdx.PdxSerializer;
      import org.apache.geode.pdx.PdxWriter;
      
      public class ExamplePdxSerializer implements PdxSerializer, Declarable {
      ...
      ```

   2. In your cache pdx configuration, register the serializer class in the cache’s `<pdx>` `<pdx-serializer>` `<class-name>` attribute.

      Example:

      ```
      // Configuration setting PDX serializer for the cache
      <cache>
        <pdx>
          <pdx-serializer>
           <class-name>com.company.ExamplePdxSerializer</class-name>
          </pdx-serializer>
        </pdx> 
        ...
      </cache>
      ```

      Or use the `CacheFactory.setPdxSerializer` API.

      ```
      Cache c = new CacheFactory
         .setPdxSerializer(new ExamplePdxSerializer())
         .create();
      ```

   **Note:** You cannot specify a custom `pdx-serializer` class using gfsh, however the `configure pdx` command automatically configures the org.apache.geode.pdx.ReflectionBasedAutoSerializer class. See [configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh).

3. Program `PdxSerializer.toData` to recognize, cast, and handle your domain object:

   1. Write each standard Java data field of your domain class using the `PdxWriter` write methods.
   2. Call the `PdxWriter` `markIdentityField` method for each field you want to have Geode use to identify your object. Put this after the field’s write method. Geode uses this information to compare objects for operations like distinct queries. If you do not set as least one identity field, then the `equals` and `hashCode` methods will use all PDX fields to compare objects and consequently, will not perform as well. It is important that the fields used by your `equals` and `hashCode` implementations are the same fields that you mark as identity fields.
   3. For a particular version of your class, you need to consistently write the same named field each time. The field names or number of fields must not change from one instance to another for the same class version.
   4. For best performance, do fixed width fields first and then variable length fields.
   5. If desired, you can check the portability of the object before serializing it by adding the `checkPortability` parameter when using the`PdxWriter` `writeObject`, `writeObjectArray`, and `writeField` methods.

   Example `toData` code:

   ```
   public boolean toData(Object o, PdxWriter writer)
     {
       if(!(o instanceof PortfolioPdx)) {
         return false;
       }
   
       PortfolioPdx instance = (PortfolioPdx) o;
       writer.writeInt("id", instance.id)
       //identity field
       .markIdentityField("id")
       .writeDate("creationDate", instance.creationDate)
       .writeString("pkid", instance.pkid)
       .writeObject("positions", instance.positions)
       .writeString("type", instance.type)
       .writeString("status", instance.status)
       .writeStringArray("names", instance.names)
       .writeByteArray("newVal", instance.newVal)
   
       return true;
     }
   ```

   1. Program `PdxSerializer.fromData` to create an instance of your class, read your data fields from the serialized form into the object’s fields using the `PdxReader` read methods, and return the created object.

      Provide the same names that you did in `toData` and call the read operations in the same order as you called the write operations in your `toData` implementation.

      Geode provides the domain class type and `PdxReader` to the `fromData` method.

      Example `fromData` code:

      ```
        public Object fromData(Class<?> clazz, PdxReader reader)
        {
          if(!clazz.equals(PortfolioPdx.class)) {
            return null;
          }
      
          PortfolioPdx instance = new PortfolioPdx();
          instance.id = reader.readInt("id");
          instance.creationDate = reader.readDate("creationDate");
          instance.pkid = reader.readString("pkid");
          instance.positions = (Map<String, PositionPdx>)reader.readObject("positions");
          instance.type = reader.readString("type");
          instance.status = reader.readString("status");
          instance.names = reader.readStringArray("names");
          instance.newVal = reader.readByteArray("newVal");
      
          return instance;
        }
      ```

4. If desired, you can also enable extra validation in your use of `PdxWriter`. You can set this by setting the system property `gemfire.validatePdxWriters` to **true**. Note that you should only set this option if you are debugging new code as this option can decrease system performance.



#### Implementing PdxSerializable in Your Domain Object

For a domain object with source that you can modify, implement the `PdxSerializable` interface in the object and use its methods to serialize and deserialize the object’s fields.

**Procedure**

1. In your domain class, implement `PdxSerializable`, importing the required `org.apache.geode.pdx` classes.

   For example:

   ```
   import org.apache.geode.pdx.PdxReader;
   import org.apache.geode.pdx.PdxSerializable;
   import org.apache.geode.pdx.PdxWriter;
   
   public class PortfolioPdx implements PdxSerializable {
     ...
   ```

2. If your domain class does not have a zero-arg constructor, create one for it.

   For example:

   ```
   public PortfolioPdx(){}
   ```

3. Program `PdxSerializable.toData.`

   1. Write each standard Java data field of your domain class using the `PdxWriter` write methods. Geode automatically provides `PdxWriter` to the `toData` method for `PdxSerializable` objects.

   2. Call the `PdxWriter` `markIdentifyField` method for each field you want to have Geode use to identify your object. Put this after the field’s write method. Geode uses this information to compare objects for operations like distinct queries. If you do not set as least one identity field, then the `equals` and `hashCode` methods will use all PDX fields to compare objects and consequently, will not perform as well. It is important that the fields used by your `equals` and `hashCode` implementations are the same fields that you mark as identity fields.

   3. For a particular version of your class, you need to consistently write the same named field each time. The field names or number of fields must not change from one instance to another for the same class version.

   4. For best performance, do fixed width fields first and then variable length fields.

      Example `toData` code:

      ```
      // PortfolioPdx fields
        private int id;
        private String pkid;
        private Map<String, PositionPdx> positions;
        private String type;
        private String status;
        private String[] names;
        private byte[] newVal;
        private Date creationDate;
        ...
      
        public void toData(PdxWriter writer)
        {
          writer.writeInt("id", id)
      // The markIdentifyField call for a field must 
      // come after the field's write method 
          .markIdentityField("id")
          .writeDate("creationDate", creationDate) //fixed length field
          .writeString("pkid", pkid)
          .writeObject("positions", positions)
          .writeString("type", type)
          .writeString("status", status)
          .writeStringArray("names", names)
          .writeByteArray("newVal", newVal)
        }
      ```

4. Program `PdxSerializable.fromData` to read your data fields from the serialized form into the object’s fields using the `PdxReader` read methods.

   Provide the same names that you did in `toData` and call the read operations in the same order as you called the write operations in your `toData` implementation.

   Geode automatically provides `PdxReader` to the `fromData` method for `PdxSerializable`objects.

   Example `fromData` code:

   ```
   public void fromData(PdxReader reader)
     {
       id = reader.readInt("id");
       creationDate = reader.readDate("creationDate");
       pkid = reader.readString("pkid");
       position1 = (PositionPdx)reader.readObject("position1");
       position2 = (PositionPdx)reader.readObject("position2");
       positions = (Map<String, PositionPdx>)reader.readObject("positions");
       type = reader.readString("type");
       status = reader.readString("status");
       names = reader.readStringArray("names");
       newVal = reader.readByteArray("newVal");
       arrayNull = reader.readByteArray("arrayNull");
       arrayZeroSize = reader.readByteArray("arrayZeroSize");
     }
   ```

**What to do next**

- As needed, configure and program your Geode applications to use `PdxInstance` for selective object deserialization. See [Programming Your Application to Use PdxInstances](https://geode.apache.org/docs/guide/17/developing/data_serialization/program_application_for_pdx.html).



#### Programming Your Application to Use PdxInstances

A `PdxInstance` is a light-weight wrapper around PDX serialized bytes. It provides applications with run-time access to fields of a PDX serialized object.

You can configure your cache to return a `PdxInstance` when a PDX serialized object is deserialized instead of deserializing the object to a domain class. You can then program your application code that reads your entries to handle `PdxInstance`s fetched from the cache.

**Note:** This applies only to entry retrieval that you explicitly code using methods like `EntryEvent.getNewValue` and `Region.get`, as you do inside functions or in cache listener code. This does not apply to querying because the query engine retrieves the entries and handles object access for you.

If you configure your cache to allow PDX serialized reads, a fetch from the cache returns the data in the form it is found. If the object is not serialized, the fetch returns the domain object. If the object is serialized, the fetch returns the `PdxInstance` for the object.

**Note:** If you are using `PdxInstance`s, you cannot use delta propagation to apply changes to PDX serialized objects.

For example, in client/server applications that are programmed and configured to handle all data activity from the client, PDX serialized reads done on the server side will always return a `PdxInstance`. This is because all of data is serialized for transfer from the client, and you are not performing any server-side activities that would deserialize the objects in the server cache.

In mixed situations, such as where a server cache is populated from client operations and also from data loads done on the server side, fetches done on the server can return a mix of `PdxInstance`s and domain objects.

When fetching data in a cache with PDX serialized reads enabled, the safest approach is to code to handle both types, receiving an `Object` from the fetch operation, checking the type and casting as appropriate. However, if you know that the class is not available in the JVM, then you can avoid performing the type check.

`PdxInstance` overrides any custom implementation you might have coded for your object’s `equals` and `hashcode` methods. Make sure you have marked at least one identity field when writing PDX serialized objects. If you do not set as least one identity field, then the PdxInstance`equals` and `hashCode` methods will use all PDX fields to compare objects and consequently, will not perform as well.



**Prerequisites**

- Understand generally how to configure the Geode cache. See [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html#basic_config_management).



**Procedure**

In your application where you fetch data from the cache, provide the following configuration and code as appropriate:

1. In the cache.xml file of the member where entry fetches are run, set the `<pdx>` `read-serialized` attribute to true. Data is not necessarily accessed on the member that you have coded for it. For example, if a client application runs a function on a server, the actual data access is done on the server, so you set `read-serialized` to true on the server.

   For example:

   ```
   // Cache configuration setting PDX read behavior 
   <cache>
     <pdx read-serialized="true" />
     ...
   </cache>
   ```

2. Write the application code that fetches data from the cache to handle a `PdxInstance`. If you are sure you will only retrieve `PdxInstance`s from the cache, you can code only for that. In many cases, a `PdxInstance` or a domain object may be returned from your cache entry retrieval operation, so you should check the object type and handle each possible type.

   For example:

   ```
   // put/get code with serialized read behavior
   // put is done as normal
   myRegion.put(myKey, myPdxSerializableObject);
   
   // get checks Object type and handles each appropriately
   Object myObject = myRegion.get(myKey);
   if (myObject instanceof PdxInstance) {
     // get returned PdxInstance instead of domain object    
     PdxInstance myPdxInstance = (PdxInstance)myObject;
   
     // PdxInstance.getField deserializes the field, but not the object
     String fieldValue = myPdxInstance.getField("stringFieldName"); 
   
     // Update a field and put it back into the cache 
     // without deserializing the entire object
     WritablePdxInstance myWritablePdxI = myPdxInstance.createWriter();
     myWritablePdxI.setField("fieldName", fieldValue);
     region.put(key, myWritablePdxI);
   
     // Deserialize the entire object if needed, from the PdxInstance
     DomainClass myPdxObject = (DomainClass)myPdxInstance.getObject();
   }
   else if (myObject instanceof DomainClass) {
     // get returned instance of domain object  
     // code to handle domain object instance  
     ...  
   }
   ...
   ```

   **Note:** Due to a limitation with PDX, if your PDX-enabled cache contains TreeSet domain objects, you should implement a Comparator that can handle both your domain objects and PdxInstance objects. You will also need to make the domain classes available on the server.



#### Adding JSON Documents to the Geode Cache

The `JSONFormatter` API allows you to put JSON formatted documents into regions and retrieve them later by storing the documents internally as PdxInstances.

Geode supports the use of JSON formatted documents natively. When you add a JSON document to a Geode cache, you call the JSONFormatter APIs to transform them into the PDX format (as a `PdxInstance`), which enables Geode to understand the JSON document at a field level.

In terms of querying and indexing, because the documents are stored internally as PDX, applications can index on any field contained inside the JSON document including any nested field (within JSON objects or JSON arrays.) Any queries run on these stored documents will return PdxInstances as results. To update a JSON document stored in Geode , you can execute a function on the PdxInstance.

You can then use the `JSONFormatter` to convert the PdxInstance results back into the JSON document.

`JSONFormatter` uses a streaming parser ([Jackson](http://wiki.fasterxml.com/JacksonHome), JSON processor) to turn JSON documents into the optimized PDX format. To use the JSONFormatter, make sure that `lib/geode-dependencies.jar` is available in your application’s CLASSPATH.

The `JSONFormatter` class has four static methods that are used to convert JSON document into PdxInstances and then to convert those PdxInstances back into JSON document.

You need to call the following methods before putting any JSON document into the Geode region:

- `fromJSON`. Creates a PdxInstance from a JSON byte array. Returns the PdxInstance.
- `fromJSON`. Creates a PdxInstance from a JSON string. Returns the PdxInstance.

After putting the JSON document into a region as a PdxInstance, you can execute standard Geode queries and create indexes on the JSON document in the same manner you would query or index any other Geode PdxInstance.

After executing a Geode query or calling `region.get`, you can use the following methods to convert a PdxInstance back into the JSON format:

- `toJSON`. Reads a PdxInstance and returns a JSON string.
- `toJSONByteArray`. Reads a PdxInstance and returns a JSON byte array.

For more information on using the JSONFormatter, see the Java API documentation for `org.apache.geode.pdx.JSONFormatter`.

**Sorting Behavior of Serialized JSON Fields**

By default, Geode serialization creates a unique pdx typeID for each unique JSON document, even if the only difference between the JSON documents is the order in which their fields are specified.

If you prefer that JSON documents which differ only in the order in which their fields are specified map to the same typeID, set the property `gemfire.pdx.mapper.sort-json-field-names` to `true`. This tells the system to sort the JSON fields prior to serialization, allowing the system to identify matching entries, and helps reduce the number of pdx typeIDs that are generated by the serialization mechanism.



#### Using PdxInstanceFactory to Create PdxInstances

You can use the `PdxInstanceFactory` interface to create a `PdxInstance` from raw data when the domain class is not available on the server.

This can be particularly useful when you need an instance of a domain class for plug in code such as a function or a loader. If you have the raw data for the domain object (the class name and each field’s type and data), then you can explicitly create a `PdxInstance`. The `PdxInstanceFactory` is very similar to the `PdxWriter` except that after writing each field, you need to call the create method which returns the created PdxInstance.

To create a factory call `RegionService.createPdxInstanceFactory`. A factory can only create a single instance. To create multiple instances create multiple factories or use `PdxInstance.createWriter()`to create subsequent instances. Using `PdxInstance.createWriter()` is usually faster.

When you create a PdxInstance, set as least one identity field using the `markIndentityField`method. If you do not mark an identity field, the PdxInstance`equals` and `hashCode` methods will use all PDX fields to compare objects and consequently, will not perform as well. It is important that the fields used by your `equals` and `hashCode` implementations are the same fields that you mark as identity fields.

The following is a code example of using `PdxInstanceFactory`:

```
PdxInstance pi = cache.createPdxInstanceFactory("com.company.DomainObject")
   .writeInt("id", 37)
   .markIdentityField("id")
   .writeString("name", "Mike Smith")
   .writeObject("favoriteDay", cache.createPdxEnum("com.company.Day", "FRIDAY", 5))
   .create();
```

For more information, see `PdxInstanceFactory` in the Java API documentation.

**Enum Objects as PdxInstances**

You can now work with enum objects as PdxInstances. When you fetch an enum object from the cache, you can now deserialize it as a `PdxInstance`. To check whether a `PdxInstance` is an enum, use the `PdxInstance.isEnum` method. An enum `PdxInstance` will have one field named “name” whose value is a String that corresponds to the enum constant name.

An enum `PdxInstance` is not writable; if you call `createWriter` it will throw an exception.

The `RegionService` has a method that allows you to create a `PdxInstance` that represents an enum. See `RegionService.createPdxEnum` in the Java API documentation.



#### Persisting PDX Metadata to Disk

Geode allows you to persist PDX metadata to disk and specify the disk store to use.

**Prerequisites**

- Understand generally how to configure the Geode cache. See [Basic Configuration and Programming](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).
- Understand how Geode disk stores work. See [Disk Storage](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

**Procedure**

1. Set the `<pdx>` attribute `persistent` to true in your cache configuration. This is required for caches that use PDX with persistent regions and with regions that use a gateway sender to distribute events across a WAN.. Otherwise, it is optional.
2. (Optional) If you want to use a disk store that is not the Geode default disk store, set the `<pdx>`attribute `disk-store-name` to the name of your non-default disk store. **Note:** If you are using PDX serialized objects as region entry keys and you are using persistent regions, then you must configure your PDX disk store to be a different one than the disk store used by the persistent regions.
3. (Optional) If you later want to rename the PDX types that are persisted to disk, you can do so on your offline disk-stores by executing the `pdx rename` command. See [pdx rename](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/pdx.html).

**Example cache.xml:**

This example `cache.xml` enables PDX persistence and sets a non-default disk store in a server cache configuration:

```
  <pdx read-serialized="true" 
       persistent="true" disk-store-name="SerializationDiskStore">
    <pdx-serializer>
      <class-name>pdxSerialization.defaultSerializer</class-name>
    </pdx-serializer>
  </pdx>
  <region ...
```



#### Using PDX Objects as Region Entry Keys

Using PDX objects as region entry keys is highly discouraged.

The best practice for creating region entry keys is to use a simple key; for example, use a String or Integer. If the key must be a domain class, then you should use a non-PDX-serialized class.

If you must use PDX serialized objects as region entry keys, ensure that you do not set `read-serialized` to `true`. This configuration setting will cause problems in partitioned regions because partitioned regions require the hash code of the key to be the same on all JVMs in the distributed system. When the key is a `PdxInstance` object, its hash code will likely not be the same as the hash code of the domain object.

If you are using PDX serialized objects as region entry keys and you are using persistent regions, then you must configure your PDX disk store to be a different one than the disk store used by the persistent regions.  



### Geode Data Serialization (DataSerializable and DataSerializer)

Geode’s `DataSerializable` interface gives you quick serialization of your objects.

**Data Serialization with the DataSerializable Interface**

Geode’s `DataSerializable` interface gives you faster and more compact data serialization than the standard Java serialization or Geode PDX serialization. However, while Geode `DataSerializable`interface is generally more performant than Geode’s `PdxSerializable`, it requires full deserialization on the server and then reserialization to send the data back to the client.

You can further speed serialization by registering the instantiator for your `DataSerializable` class through `Instantiator`, eliminating the need for reflection to find the right serializer. You can provide your own serialization through the API.

The recommended way to register your custom `Instantiator` is by specifying it in the `serialization-registration` element of cache.xml.

For more information, see the online Java documentation for `DataSerializable` and `DataSerializer`.

**Example cache.xml:**

The following provides an example of how to register an instantiator using cache.xml.

```
<serialization-registration>
<instantiator id="30">
   <class-name>com.package.MyClass</class-name>
</instantiator>
</serialization-registration>
```

In addition to speeding standard object serialization, you can use the `DataSerializable` interface to serialize any custom objects you store in the cache.

**Serializing Your Domain Object with DataSerializer**

You can also use `DataSerializer` to serialize domain objects. It serializes data in the same way as `DataSerializable` but allows you to serialize classes without modifying the domain class code.

See the JavaDocs on [DataSerializable](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/DataSerializable.html) and [DataSerializer](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/DataSerializer.html) for more information.



### Standard Java Serialization

You can use standard Java serialization for data you only distribute between Java applications. If you distribute your data between non-Java clients and Java servers, you need to do additional programming to get the data between the various class formats.

Standard Java types are serializable by definition. For your domain classes, implement `java.io.Serializable`, then make sure to mark your transient and static variables as needed for your objects. For information, see the online documentation for `java.io.Serializable` for your Java version.

Mixing `DataSerializable` with `Serializable` or `PdxSerializable` use on the same data can result in increased memory use and lower throughput than using just `Serializable` on the entire data, especially if the `Serializable` entries are in collections. The bigger the data collection, the lower the throughput as the metadata for the collection entries is not shared when using `DataSerializable`.



## Events and Event Handling

Geode provides versatile and reliable event distribution and handling for your cached data and system member events.

- **How Events Work**

  Members in your cluster receive cache updates from other members through cache events. The other members can be peers to the member, clients or servers or other clusters.

- **Implementing Geode Event Handlers**

  You can specify event handlers for region and region entry operations and for administrative events.

- **Configuring Peer-to-Peer Event Messaging**

  You can receive events from cluster peers for any region that is not a local region. Local regions receive only local cache events.

- **Configuring Client/Server Event Messaging**

  You can receive events from your servers for server-side cache events and query result changes.

- **Configuring Multi-Site (WAN) Event Queues**

  In a multi-site (WAN) installation, Geode uses gateway sender queues to distribute events for regions that are configured with a gateway sender. AsyncEventListeners also use an asynchronous event queue to distribute events for configured regions. This section describes additional options for configuring the event queues that are used by gateway senders or AsyncEventListener implementations.




