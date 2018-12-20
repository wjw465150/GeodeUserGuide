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



### How Events Work

Members in your Geode cluster receive cache updates from other members through cache events. The other members can be peers to the member, clients or servers or other clusters.

**Events Features**

These are the primary features of Geode events:

- Content-based events
- Asynchronous event notifications with conflation
- Synchronous event notifications for low latency
- High availability through redundant messaging queues
- Event ordering and once and only-once delivery
- Distributed event notifications
- Durable subscriptions
- Continuous querying

**Types of Events**

There are two categories of events and event handlers.

- Cache events in the caching API are used by applications with a cache. Cache events provide detail-level notification for changes to your data. Continuous query events are in this category.
- Administrative events in the administration API are used by administrative applications without caches.

Both kinds of events can be generated by a single member operation.

**Note:** You can handle one of these categories of events in a single system member. You cannot handle both cache and administrative events in a single member.

Because Geode maintains the order of administrative events and the order of cache events separately, using cache events and administrative events in a single process can cause unexpected results.

**Event Cycle**

The following steps describe the event cycle:

1. An operation begins, such as data put or a cache close.
2. The operation execution generates these objects:
   - An object of type `Operation` that describes the method that triggered the event.
   - An event object that describes the event, such as the member and region where the operation originated.
3. The event handlers that can handle the event are called and passed the event objects. Different event types require different handler types in different locations. If there is no matching event handler, that does not change the effect of the operation, which happens as usual.
4. When the handler receives the event, it triggers the handler’s callback method for this event. The callback method can hand off the event object as input to another method. Depending on the type of event handler, the callbacks can be triggered before or after the operation. The timing depends on the event handler, not on the event itself. **Note:** For transactions, after-operation listeners receive the events after the transaction has committed.
5. If the operation is distributed, so that it causes follow-on operations in other members, those operations generate their own events, which can be handled by their listeners in the same way.

**Event Objects**

Event objects come in several types, depending on the operation. Some operations generate multiple objects of different types. All event objects contain data describing the event, and each event type carries slightly different kinds of data appropriate to its matching operation. An event object is stable. For example, its content does not change if you pass it off to a method on another thread.

For cache events, the event object describes the operation performed in the local cache. If the event originated remotely, it describes the local application of the remote entry operation, not the remote operation itself. The only exception is when the local region has an empty data policy; then the event carries the information for the remote (originating) cache operation.

**Event Distribution**

After a member processes an event in its local cache, it distributes it to remote caches according to the member’s configuration and the configurations of the remote caches. For example, if a client updates its cache, the update is forwarded to the client’s server. The server distributes the update to its peers and forwards it to any other clients according to their interest in the data entry. If the server system is part of a multi-site deployment and the data region is configured to use a gateway sender, then the gateway sender also forwards the update to a remote site, where the update is further distributed and propagated.

**Event Handlers and Region Data Storage**

You can configure a region for no local data storage and still send and receive events for the region. Conversely, if you store data in the region, the cache is updated with data from the event regardless of whether you have any event handlers installed.

**Multiple Listeners**

When multiple listeners are installed, as can be done with cache listeners, the listeners are invoked sequentially in the order they were added to the region or cache. Listeners are executed one at a time. So, unless you program a listener to pass off processing to another thread, you can use one listener’s work in later listeners.

**Event Ordering**

During a cache operation, event handlers are called at various stages of the operation. Some event handlers are called before a region update and some are called after the region update operation. Depending on the type of event handler being called, the event handler can receive the events in-order or out-of-order in which they are applied on Region.

- `CacheWriter` and `AsyncEventListener` always receive events in the order in which they are applied on region.
- `CacheListener` and `CqListener` can receive events in a different order than the order in which they were applied on the region.

**Note:** An `EntryEvent` contains both the old value and the new value of the entry, which helps to indicate the value that was replaced by the cache operation on a particular key.



#### Peer-to-Peer Event Distribution

When a region or entry operation is performed, Geode distributes the associated events in the cluster according to system and cache configurations.

Install a cache listener for a region in each system member that needs to receive notification of region and entry changes.

**Events in a Partitioned Region**

A distributed operation follows this sequence in a partitioned region:

1. Apply the operation to the cache with the primary data entry, if appropriate.
2. Do the distribution based on the subscription-attributes interest-policy of the other members.
3. Invoke any listeners in the caches that receive the distribution.
4. Invoke the listener in the cache with the primary data entry.

In the following figure:

1. An API call in member M1 creates an entry.
2. The partitioned region creates the new entry in the cache in M2. M2, the holder of the primary copy, drives the rest of the procedure.
3. These two operations occur simultaneously:
   - The partitioned region creates a secondary copy of the entry in the cache in M3. Creating the secondary copy does not invoke the listener on M3.
   - M2 distributes the event to M4. This distribution to the other members is based on their interest policies. M4 has an interest-policy of all , so it receives notification of all events anywhere in the region. Since M1 and M3 have an interest-policy of cache-content , and this event does not affect any pre-existing entry in their local caches, they do not receive the event.
4. The cache listener on M4 handles the notification of the remote event on M2.
5. Once everything on the other members has completed successfully, the original create operation on M2 succeeds and invokes the cache listener on M2.

![img](assets/Events-2.gif)

**Events in a Distributed Region**

A distributed operation follows this sequence in a distributed region:

1. Apply the operation to the local cache, if appropriate.
2. Invoke the local listeners.
3. Do the distribution.
4. Each member that receives the distribution carries out its own operation in response, which invokes any local listeners.

In the following figure:

1. An entry is created through a direct API call on member M1.
2. The create invokes the cache listener on M1.
3. M1 distributes the event to the other members.
4. M2 and M3 apply the remote change through their own local operations.
5. M3 does a create, but M2 does an update, because the entry already existed in its cache.
6. The cache listener on M2 receives callbacks for the local update. Since there is no cache listener on M3, the callbacks from the create on M3 are not handled. An API call in member M1 creates an entry.

![img](assets/Events-3.gif)

**Managing Events in Multi-threaded Applications**

For partitioned regions, Geode guarantees ordering of events across threads, but for distributed regions it doesn’t. For multi-threaded applications that create distributed regions, you need to use your application synchronization to make sure that one operation completes before the next one begins. Distribution through the distributed-no-ack queue can work with multiple threads if you set the `conserve-sockets` attribute to true. Then the threads share one queue, preserving the order of the events in distributed regions. Different threads can invoke the same listener, so if you allow different threads to send events, it can result in concurrent invocations of the listener. This is an issue only if the threads have some shared state - if they are incrementing a serial number, for example, or adding their events to a log queue. Then you need to make your code thread safe.



#### Client-to-Server Event Distribution

Clients and servers distribute events according to client activities and according to interest registered by the client in server-side cache changes.

When the client updates its cache, changes to client regions are automatically forwarded to the server side. The server-side update is then propagated to the other clients that are connected and have subscriptions enabled. The server does not return the update to the sending client.

The update is passed to the server and then passed, with the value, to every other client that has registered interest in the entry key. This figure shows how a client’s entry updates are propagated.

![img](assets/client_server_event_dist.svg)

The figure shows the following process:

1. Entry X is updated or created in Region A through a direct API call on Client1.
2. The update to the region is passed to the pool named in the region.
3. The pool propagates the event to the cache server, where the region is updated.
4. The server member distributes the event to its peers and also places it into the subscription queue for Client2 because that client has previously registered interest in entry X.
5. The event for entry X is sent out of the queue to Client2. When this happens is indeterminate.

Client to server distribution uses the client pool connections to send updates to the server. Any region with a named pool automatically forwards updates to the server. Client cache modifications pass first through a client `CacheWriter`, if one is defined, then to the server through the client pool, and then finally to the client cache itself. A cache writer, either on the client or server side, may abort the operation.

| Change in Client Cache                              | Effect on Server Cache                                       |
| --------------------------------------------------- | ------------------------------------------------------------ |
| Entry create or update                              | Creation or update of entry.                                 |
| Distributed entry destroy                           | Entry destroy. The destroy call is propagated to the server even if the entry is not in the client cache. |
| Distributed region destroy/clear (distributed only) | Region destroy/clear                                         |

**Note:** Invalidations on the client side are not forwarded to the server.

**Server-to-Client Event Distribution**

The server automatically sends entry modification events only for keys in which the client has registered interest. In the interest registration, the client indicates whether to send new values or just invalidations for the server-side entry creates and updates. If invalidation is used, the client then updates the values lazily as needed.

This figure shows the complete event subscription event distribution for interest registrations, with value receipt requested (receiveValues=true) and without.

![img](assets/server_client_event_dist.svg)

| Change in Server Cache                      | Effect on Client Cache                                       |
| ------------------------------------------- | ------------------------------------------------------------ |
| Entry create/update                         | For subscriptions with `receiveValues` set to true, entry create or update.For subscriptions with `receiveValues` set to false, entry invalidate if the entry already exists in the client cache; otherwise, no effect. The next client get for the entry is forwarded to the server. |
| Entry invalidate/destroy (distributed only) | Entry invalidate/destroy                                     |
| Region destroy/clear (distributed only)     | Region destroy or local region clear                         |

Server-side distributed operations are all operations that originate as a distributed operation in the server or one of its peers. Region invalidation in the server is not forwarded to the client.

**Note:** To maintain a unified set of data in your servers, do not do local entry invalidation in your server regions.

**Server-to-Client Message Tracking**

The server uses an asynchronous messaging queue to send events to its clients. Every event in the queue originates in an operation performed by a thread in a client, a server, or an application in the server’s or some other cluster. The event message has a unique identifier composed of the originating thread’s ID combined with its member’s distributed system member ID, and the sequential ID of the operation. So the event messages originating in any single thread can be grouped and ordered by time from lowest sequence ID to highest. Servers and clients track the highest sequential ID for each member thread ID.

A single client thread receives and processes messages from the server, tracking received messages to make sure it does not process duplicate sends. It does this using the process IDs from originating threads.

![img](assets/client_server_message_tracking.svg)

The client’s message tracking list holds the highest sequence ID of any message received for each originating thread. The list can become quite large in systems where there are many different threads coming and going and doing work on the cache. After a thread dies, its tracking entry is not needed. To avoid maintaining tracking information for threads that have died, the client expires entries that have had no activity for more than the `subscription-message-tracking-timeout`.

**Client Interest Registration on the Server**

The system processes client interest registration following these steps:

1. The entries in the client region that may be affected by this registration are silently destroyed. Other keys are left alone.
   - For the `registerInterest` method, the system destroys all of the specified keys, leaving other keys in the client region alone. So if you have a client region with keys A, B, and C and you register interest in the key list A, B, at the start of the `registerInterest` operation, the system destroys keys A and B in the client cache but does not touch key C.
   - For the `registerInterestRegex` method, the system silently destroys all keys in the client region.
2. The interest specification is sent to the server, where it is added to the client’s interest list. The list can specify entries that are not in the server region at the time interest is registered.
3. If a bulk load is requested in the call’s `InterestResultPolicy` parameter, before control is returned to the calling method, the server sends all data that currently satisfies the interest specification. The client’s region is updated automatically with the downloaded data. If the server region is partitioned, the entire partitioned region is used in the bulk load. Otherwise, only the server’s local cache region is used. The interest results policy options are:
   - KEYS—The client receives a bulk load of all available keys matching the interest registration criteria.
   - KEYS_VALUES—The client receives a bulk load of all available keys and values matching the interest registration criteria. This is the default interest result policy.
   - NONE—The client does not receive any immediate bulk loading.

Once interest is registered, the server continually monitors region activities and sends events to its clients that match the interest.

- No events are generated by the register interest calls, even if they load values into the client cache.
- The server maintains the union of all of the interest registrations, so if a client registers interest in key ‘A’, then registers interest in regular expression “B*”, the server will send updates for all entries with key ‘A’ or key beginning with the letter ‘B’.
- The server maintains the interest registration list separate from the region. The list can contain specifications for entries that are not currently in the server region.
- The `registerInterestRegex` method uses the standard `java.util.regex` methods to parse the key specification.

**Server Failover**

When a server hosting a subscription queue fails, the queueing responsibilities pass to another server. How this happens depends on whether the new server is a secondary server. In any case, all failover activities are carried out automatically by the Geode system.

- **Non-HA failover:** The client fails over without high availability if it is not configured for redundancy or if all secondaries also fail before new secondaries can be initialized. As soon as it can attach to a server, the client goes through an automatic reinitialization process. In this process, the failover code on the client side silently destroys all entries of interest to the client and refetches them from the new server, essentially reinitializing the client cache from the new server’s cache. For the notify all configuration, this clears and reloads all of the entries for the client regions that are connected to the server. For notify by subscription, it clears and reloads only the entries in the region interest lists. To reduce failover noise, the events caused by the local entry destruction and refetching are blocked by the failover code and do not reach the client cache listeners. Because of this, your clients could receive some out-of-sequence events during and after a server failover. For example, entries that exist on the failed server and not on its replacement are destroyed and never recreated during a failover. Because the destruction events are blocked, the client ends up with entries removed from its cache with no associated destroy events.

- **HA failover:** If your client pool is configured with redundancy and a secondary server is available at the time the primary fails, the failover is invisible to the client. The secondary server resumes queueing activities as soon as the primary loss is detected. The secondary might resend a few events, which are discarded automatically by the client message tracking activities.

  **Note:** There is a very small potential for message loss during HA server failover. The risk is not present for failover to secondaries that have fully initialized their subscription queue data. The risk is extremely low in healthy systems that use at least two secondary servers. The risk is higher in unstable systems where servers often fail and where secondaries do not have time to initialize their subscription queue data before becoming primaries. To minimize the risk, the failover logic chooses the longest-lived secondary as the new primary.

  **Note:** Redundancy management is handled by the client, so when a durable client is disconnected from the server, client event redundancy is not maintained. Even if the servers fail one at a time, so that running clients have time to fail over and pick new secondary servers, an offline durable client cannot fail over. As a result, the client loses its queued messages.



#### Multi-Site (WAN) Event Distribution

Geode distributes a subset of cache events between clusters, with a minimum impact on each system’s performance. Events are distributed only for regions that you configure to use a gateway sender for distribution.

**Queuing Events for Distribution**

In regions that are configured with one or more gateway senders (`gateway-sender-ids` attribute), events are automatically added to a gateway sender queue for distribution to other sites. Events that are placed in a gateway sender queue are distributed asynchronously to remote sites. For serial gateway queues, the ordering of events sent between sites can be preserved using the `order-policy` attribute.

If a queue becomes too full, it is overflowed to disk to keep the member from running out of memory. You can optionally configure the queue to be persisted to disk (with the `enable-persistence` `gateway-sender` attribute). With persistence, if the member that manages the queue goes down, the member picks up where it left off after it restarts.

**Operation Distribution from a Gateway Sender**

The multi-site installation is designed for minimal impact on cluster performance, so only the farthest-reaching entry operations are distributed between sites.

These operations are distributed:

- entry create
- entry put
- entry distributed destroy, providing the operation is not an expiration action

These operations are not distributed:

- get
- invalidate
- local destroy
- expiration actions of any kind
- region operations

**How a Gateway Sender Processes Its Queue**

Each primary gateway sender contains a processor thread that reads messages from the queue, batches them, and distributes the batches to a gateway receiver in a remote site. To process the queue, a gateway sender thread takes the following actions:

1. Reads messages from the queue
2. Creates a batch of the messages
3. Synchronously distributes the batch to the other site and waits for a reply
4. Removes the batch from the queue after the other site has successfully replied

Because the batch is not removed from the queue until after the other site has replied, the message cannot get lost. On the other hand, in this mode a message could be processed more than once. If a site goes offline in the middle of processing a batch of messages, then that same batch will be sent again once the site is back online.

You can configure the batch size for messages as well as the batch time interval settings. A gateway sender processes a batch of messages from the queue when either the batch size or the time interval is reached. In an active network, it is likely that the batch size will be reached before the time interval. In an idle network, the time interval will most likely be reached before the batch size. This may result in some network latency that corresponds to the time interval.

**How a Gateway Sender Handles Batch Processing Failure**

Exceptions can occur at different points during batch processing:

- The gateway receiver could fail with acknowledgment. If processing fails while the gateway receiver is processing a batch, the receiver replies with a failure acknowledgment that contains the exception, including the identity of the message that failed, and the ID of the last message that it successfully processed. The gateway sender then removes the successfully processed messages and the failed message from the queue and logs an exception with the failed message information. The sender then continues processing the messages remaining in the queue.
- The gateway receiver can fail without acknowledgment. If the gateway receiver does not acknowledge a sent batch, the gateway sender does not know which messages were successfully processed. In this case the gateway sender re-sends the entire batch.
- No gateway receivers may be available for processing. If a batch processing exception occurs because there are no remote gateway receivers available, then the batch remains in the queue. The gateway sender waits for a time, and then attempts to re-send the batch. The time period between attempts is five seconds. The existing server monitor continuously attempts to connect to the gateway receiver, so that a connection can be made and queue processing can continue. Messages build up in the queue and possibly overflow to disk while waiting for the connection.



#### List of Event Handlers and Events

Geode provides many types of events and event handlers to help you manage your different data and application needs.

**Event Handlers**

Use either cache handlers or membership handlers in any single application. Do not use both. The event handlers in this table are cache handlers unless otherwise noted.

| Handler API                          | Events received                                       | Description                                                  |
| ------------------------------------ | ----------------------------------------------------- | ------------------------------------------------------------ |
| `AsyncEventListener`                 | `AsyncEvent`                                          | Tracks changes in a region for write-behind processing. Extends the `CacheCallback`interface. You install a write-back cache listener to an `AsyncEventQueue` instance. You can then add the `AsyncEventQueue` instance to one or more regions for write-behind processing. See [Implementing an AsyncEventListener for Write-Behind Cache Event Handling](https://geode.apache.org/docs/guide/17/developing/events/implementing_write_behind_event_handler.html#implementing_write_behind_cache_event_handling). |
| `CacheCallback`                      |                                                       | Superinterface of all cache event listeners. Functions only to clean up resources that the callback allocated. |
| `CacheListener`                      | `RegionEvent`, `EntryEvent`                           | Tracks changes to region and its data entries. Responds synchronously. Extends `CacheCallback` interface. Installed in region. Receives only local cache events. Install one in every member where you want the events handled by this listener. In a partitioned region, the cache listener only fires in the primary data store. Listeners on secondaries are not fired. |
| `CacheWriter`                        | `RegionEvent`, `EntryEvent`                           | Receives events for *pending*changes to the region and its data entries in this member or one of its peers. Has the ability to abort the operations in question. Extends `CacheCallback` interface. Installed in region. Receives events from anywhere in the distributed region, so you can install one in one member for the entire distributed region. Receives events only in primary data store in partitioned regions, so install one in every data store. |
| `ClientMembershipListener`           | `ClientMembershipEvent`                               | One of the interfaces that replaces the deprecated Admin APIs. You can use the ClientMembershipListener to receive membership events only about clients. This listener’s callback methods are invoked when this process detects connection changes to clients. Callback methods include `memberCrashed`, `memberJoined`, `memberLeft`(graceful exit). |
| `CqListener`                         | `CqEvent`                                             | Receives events from the server cache that satisfy a client-specified query. Extends `CacheCallback` interface. Installed in the client inside a `CqQuery`. |
| `GatewayConflictResolver`            | `TimestampedEntryEvent`                               | Decides whether to apply a potentially conflicting event to a region that is distributed over a WAN configuration. This event handler is called only when the distributed system ID of an update event is different from the ID that last updated the region entry. |
| `MembershipListener`                 | `MembershipEvent`                                     | Use this interface to receive membership events only about peers. This listener’s callback methods are invoked when peer members join or leave the cluster. Callback methods include `memberCrashed`, `memberJoined`, and `memberLeft`(graceful exit). |
| `RegionMembershipListener`           | `RegionEvent`                                         | Provides after-event notification when a region with the same name has been created in another member and when other members hosting the region join or leave the cluster. Extends `CacheCallback` and `CacheListener`. Installed in region as a `CacheListener`. |
| `TransactionListener`                | `TransactionEvent` with embedded list of `EntryEvent` | Tracks the outcome of transactions and changes to data entries in the transaction.**Note:**Multiple transactions on the same cache can cause concurrent invocation of `TransactionListener`methods, so implement methods that do the appropriate synchronizing of the multiple threads for thread-safe operation.Extends `CacheCallback`interface. Installed in cache using transaction manager. Works with region-level listeners if needed. |
| `TransactionWriter`                  | `TransactionEvent` with embedded list of `EntryEvent` | Receives events for *pending*transaction commits. Has the ability to abort the transaction. Extends `CacheCallback`interface. Installed in cache using transaction manager. At most one writer is called per transaction. Install a writer in every transaction host. |
| `UniversalMembershipListenerAdapter` | `MembershipEvent` and `ClientMembershipEvent`         | One of the interfaces that replaces the deprecated Admin APIs. Provides a wrapper for MembershipListener and ClientMembershipListener callbacks for both clients and peers. |

**Events**

The events in this table are cache events unless otherwise noted.

| Event                               | Passed to handler …                                          | Description                                                  |
| ----------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `AsyncEvent`                        | `AsyncEventListener`                                         | Provides information about a single event in the cache for asynchronous, write-behind processing. |
| `CacheEvent`                        |                                                              | Superinterface to `RegionEvent` and `EntryEvent`. This defines common event methods, and contains data needed to diagnose the circumstances of the event, including a description of the operation being performed, information about where the event originated, and any callback argument passed to the method that generated this event. |
| `ClientMembershipEvent`             | `ClientMembershipListener`                                   | An event delivered to a `ClientMembershipListener` when this process detects connection changes to servers or clients. |
| `CqEvent`                           | `CqListener`                                                 | Provides information about a change to the results of a continuous query running on a server on behalf of a client. `CqEvent`s are processed on the client. |
| `EntryEvent`                        | `CacheListener`, `CacheWriter`, `TransactionListener`(inside the `TransactionEvent`) | Extends `CacheEvent` for entry events. Contains information about an event affecting a data entry in the cache. The information includes the key, the value before this event, and the value after this event. `EntryEvent.getNewValue` returns the current value of the data entry. `EntryEvent.getOldValue` returns the value before this event if it is available. For a partitioned region, returns the old value if the local cache holds the primary copy of the entry. `EntryEvent` provides the Geode transaction ID if available.You can retrieve serialized values from `EntryEvent` using the `getSerialized`* methods. This is useful if you get values from one region’s events just to put them into a separate cache region. There is no counterpart `put` function as the put recognizes that the value is serialized and bypasses the serialization step. |
| `MembershipEvent`(membership event) | `MembershipListener`                                         | An event that describes the member that originated this event. Instances of this are delivered to a `MembershipListener` when a member has joined or left the cluster. |
| `RegionEvent`                       | `CacheListener`, `CacheWriter`, `RegionMembershipListener`   | Extends `CacheEvent` for region events. Provides information about operations that affect the whole region, such as reinitialization of the region after being destroyed. |
| `TimestampedEntryEvent`             | `GatewayConflictResolver`                                    | Extends `EntryEvent` to include a timestamp and distributed system ID associated with the event. The conflict resolver can compare the timestamp and ID in the event with the values stored in the entry to decide whether the local system should apply the potentially conflicting event. |
| `TransactionEvent`                  | `TransactionListener`, `TransactionWriter`                   | Describes the work done in a transaction. This event may be for a pending or committed transaction, or for the work abandoned by an explicit rollback or failed commit. The work is represented by an ordered list of `EntryEvent` instances. The entry events are listed in the order in which the operations were performed in the transaction.As the transaction operations are performed, the entry events are conflated, with only the last event for each entry remaining in the list. So if entry A is modified, then entry B, then entry A, the list will contain the event for entry B followed by the second event for entry A. |



### Implementing Geode Event Handlers

You can specify event handlers for region and region entry operations and for administrative events.

- **Implementing Cache Event Handlers**

  Depending on your installation and configuration, cache events can come from local operations, peers, servers, and remote sites. Event handlers register their interest in one or more events and are notified when the events occur.

- **Implementing an AsyncEventListener for Write-Behind Cache Event Handling**

  An `AsyncEventListener` asynchronously processes batches of events after they have been applied to a region. You can use an `AsyncEventListener` implementation as a write-behind cache event handler to synchronize region updates with a database.

- **How to Safely Modify the Cache from an Event Handler Callback**

  Event handlers are synchronous. If you need to change the cache or perform any other distributed operation from event handler callbacks, be careful to avoid activities that might block and affect your overall system performance.

- **Cache Event Handler Examples**

  Some examples of cache event handlers.



#### Implementing Cache Event Handlers

Depending on your installation and configuration, cache events can come from local operations, peers, servers, and remote sites. Event handlers register their interest in one or more events and are notified when the events occur.

For each type of handler, Geode provides a convenience class with empty stubs for the interface callback methods.

**Note:** Write-behind cache listeners are created by extending the `AsyncEventListener` interface, and they are configured with an `AsyncEventQueue` that you assign to one or more regions.

**Procedure**

1. Decide which events your application needs to handle. For each region, decide which events you want to handle. For the cache, decide whether to handle transaction events.

2. For each event, decide which handlers to use. The `*Listener` and `*Adapter` classes in `org.apache.geode.cache.util` show the options.

3. Program each event handler:

   1. Extend the handler’s adapter class.

   2. If you want to declare the handler in the `cache.xml`, implement the `org.apache.geode.cache.Declarable` interface as well.

   3. Implement the handler’s callback methods as needed by your application.

      **Note:** Improperly programmed event handlers can block your distributed system. Cache events are synchronous. To modify your cache or perform distributed operations based on events, avoid blocking your system by following the guidelines in [How to Safely Modify the Cache from an Event Handler Callback](https://geode.apache.org/docs/guide/17/developing/events/writing_callbacks_that_modify_the_cache.html#writing_callbacks_that_modify_the_cache).

      Example:

      ```
      package myPackage;
      import org.apache.geode.cache.Declarable;
      import org.apache.geode.cache.EntryEvent;
      import org.apache.geode.cache.util.CacheListenerAdapter;
      import java.util.Properties;
      
      public class MyCacheListener extends CacheListenerAdapter implements Declarable {
      /** Processes an afterCreate event.
       * @param event The afterCreate EntryEvent received
      */
        public void afterCreate(EntryEvent event) {
          String eKey = event.getKey();
          String eVal = event.getNewValue();
            ... do work with event info
        }
          ... process other event types                     
      }
      ```

4. Install the event handlers, either through the API or the `cache.xml`.

   XML Region Event Handler Installation:

   ```
   <region name="trades">
     <region-attributes ... >
       <!-- Cache listener -->
       <cache-listener>
         <class-name>myPackage.MyCacheListener</class-name>
       <cache-listener>
     </region-attributes>
   </region>
   ```

   Java Region Event Handler Installation:

   ```
   tradesRegion = cache.createRegionFactory(RegionShortcut.PARTITION)
     .addCacheListener(new MyCacheListener())
     .create("trades");
   ```

   XML Transaction Writer and Listener Installation:

   ```
   <cache search-timeout="60">
         <cache-transaction-manager>
           <transaction-listener>
             <class-name>com.company.data.MyTransactionListener</class-name>
                   <parameter name="URL">
                     <string>jdbc:cloudscape:rmi:MyData</string>
                   </parameter>
              </transaction-listener> 
              <transaction-listener>
               . . . 
              </transaction-listener> 
              <transaction-writer>
                   <class-name>com.company.data.MyTransactionWriter</class-name>
                   <parameter name="URL">
                       <string>jdbc:cloudscape:rmi:MyData</string>
                   </parameter>
                   <parameter 
                     ...
                   </parameter>
              </transaction-writer> 
         </cache-transaction-manager>
         . . . 
   </cache>
   ```

The event handlers are initialized automatically during region creation when you start the member.

**Installing Multiple Listeners on a Region**

XML:

```
<region name="exampleRegion">
  <region-attributes>
    . . .
    <cache-listener>
      <class-name>myCacheListener1</class-name>
    </cache-listener>
    <cache-listener>
      <class-name>myCacheListener2</class-name>
    </cache-listener>
    <cache-listener>
      <class-name>myCacheListener3</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

API:

```
CacheListener listener1 = new myCacheListener1(); 
CacheListener listener2 = new myCacheListener2(); 
CacheListener listener3 = new myCacheListener3(); 

Region nr = cache.createRegionFactory()
  .initCacheListeners(new CacheListener[]
    {listener1, listener2, listener3})
  .setScope(Scope.DISTRIBUTED_NO_ACK)
  .create(name);
```



#### Implementing an AsyncEventListener for Write-Behind Cache Event Handling

An `AsyncEventListener` asynchronously processes batches of events after they have been applied to a region. You can use an `AsyncEventListener` implementation as a write-behind cache event handler to synchronize region updates with a database.

**How an AsyncEventListener Works**

An `AsyncEventListener` instance is serviced by its own dedicated thread in which a callback method is invoked. Events that update a region are placed in an internal `AsyncEventQueue`, and one or more threads dispatch batches of events at a time to the listener implementation.

You can configure an `AsyncEventQueue` to be either serial or parallel. A serial queue is deployed to one Geode member, and it delivers all of a region’s events, in order of occurrence, to a configured `AsyncEventListener` implementation. A parallel queue is deployed to multiple Geode members, and each instance of the queue delivers region events, possibly simultaneously, to a local `AsyncEventListener` implementation.

While a parallel queue provides the best throughput for writing events, it provides less control for ordering those events. With a parallel queue, you cannot preserve event ordering for a region as a whole because multiple Geode servers queue and deliver the region’s events at the same time. However, the ordering of events for a given partition (or for a given queue of a distributed region) can be preserved.

For both serial and parallel queues, you can control the maximum amount of memory that each queue uses, as well as the batch size and frequency for processing batches in the queue. You can also configure queues to persist to disk (instead of simply overflowing to disk) so that write-behind caching can pick up where it left off when a member shuts down and is later restarted.

Optionally, a queue can use multiple threads to dispatch queued events. When you configure multiple threads for a serial queue, the logical queue that is hosted on a Geode member is divided into multiple physical queues, each with a dedicated dispatcher thread. You can then configure whether the threads dispatch queued events by key, by thread, or in the same order in which events were added to the queue. When you configure multiple threads for a parallel queue, each queue hosted on a Geode member is processed by dispatcher threads; the total number of queues created depends on the number of members that host the region.

A `GatewayEventFilter` can be placed on the `AsyncEventQueue` to control whether a particular event is sent to a selected `AsyncEventListener`. For example, events associated with sensitive data could be detected and not queued. For more detail, see the Javadocs for `GatewayEventFilter`.

A `GatewayEventSubstitutionFilter` can specify whether the event is transmitted in its entirety or in an altered representation. For example, to reduce the size of the data being serialized, it might be a more efficient to represent a full object by only its key. For more detail, see the Javadocs for `GatewayEventSubstitutionFilter`.

**Operation Distribution from an AsyncEventQueue**

An `AsyncEventQueue` distributes these operations:

- Entry create
- Entry put
- Entry distributed destroy, providing the operation is not an expiration action
- Expiration destroy, if the `forward-expiration-destroy` attribute is set to `true`. By default, this attribute is `false`, but you can set it to `true` using `cache.xml` or `gfsh`. To set this attribute in the Java API, use `AsyncEventQueueFactory.setForwardExpirationDestroy()`. See the javadocs for details.

These operations are not distributed:

- Get
- Invalidate
- Local destroy
- Region operations
- Expiration actions
- Expiration destroy, if the `forward-expiration-destroy` attribute is set to `false`. The default value is `false`.

**Guidelines for Using an AsyncEventListener**

Review the following guidelines before using an AsyncEventListener:

- If you use an `AsyncEventListener` to implement a write-behind cache listener, your code should check for the possibility that an existing database connection may have been closed due to an earlier exception. For example, check for `Connection.isClosed()` in a catch block and re-create the connection as needed before performing further operations.
- Use a serial `AsyncEventQueue` if you need to preserve the order of region events within a thread when delivering events to your listener implementation. Use parallel queues when the order of events within a thread is not important, and when you require maximum throughput for processing events. In both cases, serial and parallel, the order of operations on a given key is preserved within the scope of the thread.
- You must install the `AsyncEventListener` implementation on a Geode member that hosts the region whose events you want to process.
- If you configure a parallel `AsyncEventQueue`, deploy the queue on each Geode member that hosts the region.
- You can install a listener on more than one member to provide high availability and guarantee delivery for events, in the event that a member with the active `AsyncEventListener` shuts down. At any given time only one member has an active listener for dispatching events. The listeners on other members remain on standby for redundancy. For best performance and most efficient use of memory, install only one standby listener (redundancy of at most one).
- Install no more than one standby listener (redundancy of at most one) for performance and memory reasons.
- To preserve pending events through member shutdowns, configure Geode to persist the internal queue of the `AsyncEventListener` to an available disk store. By default, any pending events that reside in the internal queue of an `AsyncEventListener` are lost if the active listener’s member shuts down.
- To ensure high availability and reliable delivery of events, configure the event queue to be both persistent and redundant.

**Implementing an AsyncEventListener**

To receive region events for processing, you create a class that implements the `AsyncEventListener`interface. The `processEvents` method in your listener receives a list of queued `AsyncEvent` objects in each batch.

Each `AsyncEvent` object contains information about a region event, such as the name of the region where the event occurred, the type of region operation, and the affected key and value.

The basic framework for implementing a write-behind event handler involves iterating through the batch of events and writing each event to a database. For example:

```
class MyAsyncEventListener implements AsyncEventListener {

  public boolean processEvents(List<AsyncEvent> events) {

      // Process each AsyncEvent

      for(AsyncEvent event: events) {

          // Write the event to a database

      }
    }
}
```

**Processing AsyncEvents**

Use the [AsyncEventListener.processEvents](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/asyncqueue/AsyncEventListener.html) method to process AsyncEvents. This method is called asynchronously when events are queued to be processed. The size of the list reflects the number of batch events where batch size is defined in the AsyncEventQueueFactory. The `processEvents`method returns a boolean; true if the AsyncEvents are processed correctly, and false if any events fail processing. As long as `processEvents` returns false, Geode continues to re-try processing the events.

You can use the `getDeserializedValue` method to obtain cache values for entries that have been updated or created. Since the `getDeserializedValue` method will return a null value for destroyed entries, you should use the `getKey` method to obtain references to cache objects that have been destroyed. Here’s an example of processing AsyncEvents:

```
public boolean processEvents(@SuppressWarnings("rawtypes") List<AsyncEvent> list)   
 {  
     logger.log (Level.INFO, String.format("Size of List<GatewayEvent> = %s", list.size()));  
     List<JdbcBatch> newEntries = new ArrayList<JdbcBatch>();  

     List<JdbcBatch> updatedEntries = new ArrayList<JdbcBatch>();  
     List<String> destroyedEntries = new ArrayList<String>();  
     int possibleDuplicates = 0;  

     for (@SuppressWarnings("rawtypes") AsyncEvent ge: list)  
     {  

       if (ge.getPossibleDuplicate())  
        possibleDuplicates++;  

       if ( ge.getOperation().equals(Operation.UPDATE))   
       {  
      updatedEntries.add((JdbcBatch) ge.getDeserializedValue());  
       }  
       else if ( ge.getOperation().equals(Operation.CREATE))  
       {  
         newEntries.add((JdbcBatch) ge.getDeserializedValue());  
       }  
       else if ( ge.getOperation().equals(Operation.DESTROY))  
       {  
      destroyedEntries.add(ge.getKey().toString());  
       }  

     }  
```

**Configuring an AsyncEventListener**

To configure a write-behind cache listener, you first configure an asynchronous queue to dispatch the region events, and then create the queue with your listener implementation. You then assign the queue to a region in order to process that region’s events.

**Procedure**

1. Configure a unique `AsyncEventQueue` with the name of your listener implementation. You can optionally configure the queue for parallel operation, persistence, batch size, and maximum memory size. See [WAN Configuration](https://geode.apache.org/docs/guide/17/reference/topics/elements_ref.html#topic_7B1CABCAD056499AA57AF3CFDBF8ABE3) for more information.

   **gfsh configuration**

   ```
   gfsh>create async-event-queue --id=sampleQueue --persistent --disk-store=exampleStore --listener=com.myCompany.MyAsyncEventListener --listener-param=url#jdbc:db2:SAMPLE,username#gfeadmin,password#admin1
   ```

   The parameters for this command uses the following syntax:

   ```
   create async-event-queue --id=value --listener=value [--group=value] [--batch-size=value] 
   [--persistent(=value)?] [--disk-store=value] [--max-queue-memory=value] [--listener-param=value(,value)*]
   ```

   For more information, see [create async-event-queue](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_ryz_pb1_dk).

   **cache.xml Configuration**

   ```
   <cache>
      <async-event-queue id="sampleQueue" persistent="true"
       disk-store-name="exampleStore" parallel="false">
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

   **Java Configuration**

   ```
   Cache cache = new CacheFactory().create();
   AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
   factory.setPersistent(true);
   factory.setDiskStoreName("exampleStore");
   factory.setParallel(false);
   AsyncEventListener listener = new MyAsyncEventListener();
   AsyncEventQueue asyncQueue = factory.create("sampleQueue", listener);
   ```

2. If you are using a parallel `AsyncEventQueue`, the gfsh example above requires no alteration, as gfsh applies to all members. If using cache.xml or the Java API to configure your `AsyncEventQueue`, repeat the above configuration in each Geode member that will host the region. Use the same ID and configuration settings for each queue configuration. **Note:** You can ensure other members use the sample configuration by using the cluster configuration service available in gfsh. See [Overview of the Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

3. On each Geode member that hosts the `AsyncEventQueue`, assign the queue to each region that you want to use with the `AsyncEventListener` implementation.

   **gfsh Configuration**

   ```
   gfsh>create region --name=Customer --async-event-queue-id=sampleQueue 
   ```

   Note that you can specify multiple queues on the command line in a comma-delimited list.

   **cache.xml Configuration**

   ```
   <cache>
   <region name="Customer">
       <region-attributes async-event-queue-ids="sampleQueue">
       </region-attributes>
     </region>
   ...
   </cache>
   ```

   **Java Configuration**

   ```
   RegionFactory rf1 = cache.createRegionFactory();
   rf1.addAsyncEventQueue(sampleQueue);
   Region customer = rf1.create("Customer");
   
   // Assign the queue to multiple regions as needed
   RegionFactory rf2 = cache.createRegionFactory();
   rf2.addAsyncEventQueue(sampleQueue);
   Region order = rf2.create("Order");
   ```

   Using the Java API, you can also add and remove queues to regions that have already been created:

   ```
   AttributesMutator mutator = order.getAttributesMutator();
   mutator.addAsyncEventQueueId("sampleQueue");        
   ```

   See the [Geode API documentation](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/AttributesMutator.html) for more information.

4. Optionally configure persistence and conflation for the queue. **Note:** You must configure your AsyncEventQueue to be persistent if you are using persistent data regions. Using a non-persistent queue with a persistent region is not supported.

5. Optionally configure multiple dispatcher threads and the ordering policy for the queue using the instructions in [Configuring Dispatcher Threads and Order Policy for Event Distribution](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html).

The `AsyncEventListener` receives events from every region configured with the associated `AsyncEventQueue`.



#### How to Safely Modify the Cache from an Event Handler Callback

Event handlers are synchronous. If you need to change the cache or perform any other distributed operation from event handler callbacks, be careful to avoid activities that might block and affect your overall system performance.

**Operations to Avoid in Event Handlers**

Do not perform distributed operations of any kind directly from your event handler. Geode is a highly distributed system and many operations that may seem local invoke distributed operations.

These are common distributed operations that can get you into trouble:

- Calling `Region` methods, on the event’s region or any other region.
- Using the Geode `DistributedLockService`.
- Modifying region attributes.
- Executing a function through the Geode `FunctionService`.

To be on the safe side, do not make any calls to the Geode API directly from your event handler. Make all Geode API calls from within a separate thread or executor.

**How to Perform Distributed Operations Based on Events**

If you need to use the Geode API from your handlers, make your work asynchronous to the event handler. You can spawn a separate thread or use a solution like the `java.util.concurrent.Executor`interface.

This example shows a serial executor where the callback creates a `Runnable` that can be pulled off a queue and run by another object. This preserves the ordering of events.

```
public void afterCreate(EntryEvent event) {
  final Region otherRegion = cache.getRegion("/otherRegion");
  final Object key = event.getKey();
  final Object val = event.getNewValue();

  serialExecutor.execute(new Runnable() {
    public void run() {
      try {
        otherRegion.create(key, val);
      }
      catch (org.apache.geode.cache.RegionDestroyedException e) {
        ...
      }
      catch (org.apache.geode.cache.EntryExistsException e) {
        ...
      }
    }
  });
  }
```

For additional information on the `Executor`, see the `SerialExecutor` example on the Oracle Java web site.



#### Cache Event Handler Examples

Some examples of cache event handlers.

**Declaring and Loading an Event Handler with Parameters**

This declares an event handler for a region in the `cache.xml`. The handler is a cache listener designed to communicate changes to a DB2 database. The declaration includes the listener’s parameters, which are the database path, username, and password.

```
<region name="exampleRegion"> 
  <region-attributes> 
  . . . 
    <cache-listener> 
      <class-name>JDBCListener</class-name> 
      <parameter name="url"> 
        <string>jdbc:db2:SAMPLE</string> 
      </parameter> 
      <parameter name="username"> 
        <string>gfeadmin</string> 
      </parameter> 
      <parameter name="password"> 
        <string>admin1</string> 
      </parameter> 
    </cache-listener> 
  </region-attributes> 
  </region>
```

This code listing shows part of the implementation of the `JDBCListener` declared in the `cache.xml`. This listener implements the `Declarable` interface. When an entry is created in the cache, this listener’s `afterCreate` callback method is triggered to update the database. Here the listener’s properties, provided in the `cache.xml`, are passed into the `Declarable.init` method and used to create a database connection.

```
. . .
public class JDBCListener
extends CacheListenerAdapter
implements Declarable {
  public void afterCreate(EntryEvent e) {
  . . .
    // Initialize the database driver and connection using input parameters
    Driver driver = (Driver) Class.forName(DRIVER_NAME).newInstance();
    Connection connection =
      DriverManager.getConnection(_url, _username, _password);
      System.out.println(_connection);
        . . .
  }
    . . .
  public void init(Properties props) {
    this._url = props.getProperty("url");
    this._username = props.getProperty("username");
    this._password = props.getProperty("password");
  }
}
```

**Installing an Event Handler Through the API**

This listing defines a cache listener using the `RegionFactory` method `addCacheListener`.

```
Region newReg = cache.createRegionFactory()
          .addCacheListener(new SimpleCacheListener())
          .create(name);
```

You can create a cache writer similarly, using the `RegionFactory` method `setCacheWriter`, like this:

```
Region newReg = cache.createRegionFactory()
          .setCacheWriter(new SimpleCacheWriter())
          .create(name);
```

**Installing Multiple Listeners on a Region**

XML:

```
<region name="exampleRegion">
  <region-attributes>
    . . .
    <cache-listener>
      <class-name>myCacheListener1</class-name>
    </cache-listener>
    <cache-listener>
      <class-name>myCacheListener2</class-name>
    </cache-listener>
    <cache-listener>
      <class-name>myCacheListener3</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

API:

```
CacheListener listener1 = new myCacheListener1(); 
CacheListener listener2 = new myCacheListener2(); 
CacheListener listener3 = new myCacheListener3(); 

Region nr = cache.createRegionFactory()
  .initCacheListeners(new CacheListener[]
    {listener1, listener2, listener3})
  .setScope(Scope.DISTRIBUTED_NO_ACK)
  .create(name);
```

**Installing a Write-Behind Cache Listener**

```
//AsyncEventQueue with listener that performs WBCL work
<cache>
   <async-event-queue id="sampleQueue" persistent="true"
    disk-store-name="exampleStore" parallel="false">
      <async-event-listener>
         <class-name>MyAsyncListener</class-name>
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

// Add the AsyncEventQueue to region(s) that use the WBCL
  <region name="data">
       <region-attributes async-event-queue-ids="sampleQueue">
    </region-attributes>
  </region>
</cache>
```



### Configuring Peer-to-Peer Event Messaging

You can receive events from cluster peers for any region that is not a local region. Local regions receive only local cache events.

Peer distribution is done according to the region’s configuration.

- Replicated regions always receive all events from peers and require no further configuration. Replicated regions are configured using the `REPLICATE` region shortcut settings.

- For non-replicated regions, decide whether you want to receive all entry events from the distributed cache or only events for the data you have stored locally. To configure:

  - To receive all events, set the `subscription-attributes` `interest-policy` to `all`:

    ```
    <region-attributes> 
        <subscription-attributes interest-policy="all"/> 
    </region-attributes>
    ```

  - To receive events just for the data you have stored locally, set the `subscription-attributes` `interest-policy` to `cache-content` or do not set it (`cache-content` is the default):

    ```
    <region-attributes> 
        <subscription-attributes interest-policy="cache-content"/> 
    </region-attributes>
    ```

  For partitioned regions, this only affects the receipt of events, as the data is stored according to the region partitioning. Partitioned regions with interest policy of `all` can create network bottlenecks, so if you can, run listeners in every member that hosts the partitioned region data and use the `cache-content` interest policy.

**Note:** You can also configure Regions using the gfsh command-line interface. See [Region Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD).



### Configuring Client/Server Event Messaging

You can receive events from your servers for server-side cache events and query result changes.

For cache updates, you can configure to receive entry keys and values or just entry keys, with the data retrieved lazily when requested. The queries are run continuously against server cache events, with the server sending the deltas for your query result sets.

Before you begin, set up your client/server installation and configure and program your basic event messaging.

Servers receive updates for all entry events in their client’s client regions.

To receive entry events in the client from the server:

1. Set the client pool `subscription-enabled` to true. See [](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html#cc-pool).

2. Program the client to register interest in the entries you need.

   **Note:** This must be done through the API.

   Register interest in all keys, a key list, individual keys, or by comparing key strings to regular expressions. By default, no entries are registered to receive updates. Specify whether the server is to send values with entry update events. Interest registration is only available through the API.

   1. Get an instance of the region where you want to register interest.

   2. Use the region’s `registerInterest`* methods to specify the entries you want. Examples:

      ```
      // Register interest in a single key and download its entry 
      // at this time, if it is available in the server cache 
      Region region1 = . . . ;
      region1.registerInterest("key-1"); 
      
      // Register Interest in a List of Keys but do not do an initial bulk load
      // do not send values for creater/update events - just send key with invalidation
      Region region2 = . . . ; 
      List list = new ArrayList();
      list.add("key-1"); 
      list.add("key-2"); 
      list.add("key-3"); 
      list.add("key-4");
      region2.registerInterestForKeys(list, InterestResultPolicy.NONE, false); 
      
      // Register interest in all keys and download all available keys now
      Region region3 = . . . ;
      region3.registerInterestForAllKeys(InterestResultPolicy.KEYS); 
      
      // Register Interest in all keys matching a regular expression 
      Region region1 = . . . ; 
      region1.registerInterestRegex("[a-zA-Z]+_[0-9]+"); 
      ```

      You can call the register interest methods multiple times for a single region. Each interest registration adds to the server’s list of registered interest criteria for the client. So if a client registers interest in key ‘A’, then registers interest in regular expression “B*”, the server will send updates for all entries with key ‘A’ or key beginning with the letter ‘B’.

   3. For highly available event messaging, configure server redundancy. See [Configuring Highly Available Servers](https://geode.apache.org/docs/guide/17/developing/events/configuring_highly_available_servers.html).

   4. To have events enqueued for your clients during client downtime, configure durable client/server messaging.

   5. Write any continuous queries (CQs) that you want to run to receive continuously streaming updates to client queries. CQ events do not update the client cache. If you have dependencies between CQs and/or interest registrations, so that you want the two types of subscription events to arrive as closely together on the client, use a single server pool for everything. Using different pools can lead to time differences in the delivery of events because the pools might use different servers to process and deliver the event messages.

- **Configuring Highly Available Servers**
- **Implementing Durable Client/Server Messaging**
- **Tuning Client/Server Event Messaging**



#### Configuring Highly Available Servers

With highly-available servers, one of the backups steps in and takes over messaging with no interruption in service if the client’s primary server crashes.

To configure high availability, set the `subscription-redundancy` in the client’s pool configuration. This setting indicates the number of secondary servers to use. For example:

```
<!-- Run one secondary server -->
<pool name="red1" subscription-enabled="true" subscription-redundancy="1"> 
  <locator host="nick" port="41111"/> 
  <locator host="nora" port="41111"/> 
</pool> 
<!-- Use all available servers as secondaries. One is primary, the rest are secondaries -->
<pool name="redX" subscription-enabled="true" subscription-redundancy="-1"> 
  <locator host="nick" port="41111"/> 
  <locator host="nora" port="41111"/> 
</pool> 
```

When redundancy is enabled, secondary servers maintain queue backups while the primary server pushes events to the client. If the primary server fails, one of the secondary servers steps in as primary to provide uninterrupted event messaging to the client.

The following table describes the different values for the subscription-redundancy setting:

| subscription-redundancy | Description                                                  |
| ----------------------- | ------------------------------------------------------------ |
| 0                       | No secondary servers are configured, so high availability is disabled. |
| > 0                     | Sets the precise number of secondary servers to use for backup to the primary. |
| -1                      | Every server that is not the primary is to be used as a secondary. |

- **Highly Available Client/Server Event Messaging**



##### Highly Available Client/Server Event Messaging

With server redundancy, each pool has a primary server and some number of secondaries. The primaries and secondaries are assigned on a per-pool basis and are generally spread out for load balancing, so a single client with multiple pools may have primary queues in more than one server.

The primary server pushes events to clients and the secondaries maintain queue backups. If the primary server fails, one of the secondaries becomes primary to provide uninterrupted event messaging.

For example, if there are six servers running and `subscription-redundancy` is set to two, one server is the primary, two servers are secondary, and the remaining three do not actively participate in HA for the client. If the primary server fails, the system assigns one of the secondaries as the new primary and attempts to add another server to the secondary pool to retain the initial redundancy level. If no new secondary server is found, then the redundancy level is not satisfied but the failover procedure completes successfully. As soon as another secondary is available, it is added.

When high availability is enabled:

- The primary server sends event messages to the clients.
- Periodically, the clients send received messages to the server and the server removes the sent messages from its queues.
- Periodically, the primary server synchronizes with its secondaries, notifying them of messages that can be discarded because they have already been sent and received. There is a lag in notification, so the secondary servers remain only roughly synchronized with the primary. Secondary queues contain all messages that are contained in the primary queue plus possibly a few messages that have already been sent to clients.
- In the case of primary server failure, one of the secondaries becomes the primary and begins sending event messages from its queues to the clients. Immediately after failover, the new primary usually resends some messages that were already sent by the old primary. The client recognizes these as duplicates and discards them.

In stage 1 of this figure, the primary sends an event message to the client and a synchronization message to its secondary. By stage 2, the secondary and client have updated their queue and message tracking information. If the primary failed at stage two, the secondary would start sending event messages from its queue beginning with message A10. The client would discard the resend of message A10 and then process subsequent messages as usual. ![High Availability Messaging: Server to Client and Primary Server to Secondary Server](assets/ClientServerAdvancedTopics-5.gif)

**Change Server Queue Synchronization Frequency**

By default, the primary server sends queue synchronization messages to the secondaries every second. You can change this interval with the `gfsh alter runtime` command

Set the interval for queue synchronization messages as follows:

- gfsh:

  ```
  gfsh>alter runtime --message-sync-interval=2
  ```

- XML:

  ```
  <!-- Set sync interval to 2 seconds --> 
  <cache ... message-sync-interval="2" />
  ```

- Java:

  ```
  cache = CacheFactory.create();
  cache.setMessageSyncInterval(2);  
  ```

The ideal setting for this interval depends in large part on your application behavior. These are the benefits of shorter and longer interval settings:

- A shorter interval requires less memory in the secondary servers because it reduces queue buildup between synchronizations. In addition, fewer old messages in the secondary queues means reduced message re-sends after a failover. These considerations are most important for systems with high data update rates.
- A longer interval requires fewer distribution messages between the primary and secondary, which benefits overall system performance.

Set Frequency of Orphan Removal from the Secondary Queues

Usually, all event messages are removed from secondary subscription queues based on the primary’s synchronization messages. Occasionally, however, some messages are orphaned in the secondary queues. For example, if a primary fails in the middle of sending a synchronization message to its secondaries, some secondaries might receive the message and some might not. If the failover goes to a secondary that did receive the message, the system will have secondary queues holding messages that are no longer in the primary queue. The new primary will never synchronize on these messages, leaving them orphaned in the secondary queues.

To make sure these messages are eventually removed, the secondaries expire all messages that have been enqueued longer than the time indicated by the servers’ `message-time-to-live`.

Set the time-to-live as follows:

- XML:

  ```
  <!-- Set message ttl to 5 minutes --> 
  <cache-server port="41414" message-time-to-live="300" />
  ```

- Java:

  ```
  Cache cache = ...;
  CacheServer cacheServer = cache.addCacheServer();
  cacheServer.setPort(41414);
  cacheServer.setMessageTimeToLive(200);
  cacheServer.start();                
  ```



#### Implementing Durable Client/Server Messaging

Use durable messaging for subscriptions that you need maintained for your clients even when your clients are down or disconnected. You can configure any of your event subscriptions as durable. Events for durable queries and subscriptions are saved in queue when the client is disconnected and played back when the client reconnects. Other queries and subscriptions are removed from the queue.

Use durable messaging for client/server installations that use event subscriptions.

These are the high-level tasks described in this topic:

1. Configure your client as durable
2. Decide which subscriptions should be durable and configure accordingly
3. Program your client to manage durable messaging for disconnect, reconnect, and event handling

**Configure the Client as Durable**

Use one of the following methods:

- `gemfire.properties` file:

  ```
  durable-client-id=31 
  durable-client-timeout=200 
  ```

- Java:

  ```
  Properties props = new Properties(); 
  props.setProperty("durable-client-id", "31"); 
  props.setProperty("durable-client-timeout", "" + 200); 
  CacheFactory cf = new CacheFactory(props);
  ```



The `durable-client-id` indicates that the client is durable and gives the server an identifier to correlate the client to its durable messages. For a non-durable client, this id is an empty string. The ID can be any number that is unique among the clients attached to servers in the same cluster.

The `durable-client-timeout` tells the server how long to wait for client reconnect. When this timeout is reached, the server stops storing to the client’s message queue and discards any stored messages. The default is 300 seconds. This is a tuning parameter. If you change it, take into account the normal activity of your application, the average size of your messages, and the level of risk you can handle, both in lost messages and in the servers’ capacity to store enqueued messages. Assuming that no messages are being removed from the queue, how long can the server run before the queue reaches the maximum capacity? How many durable clients can the server handle? To assist with tuning, use the Geode message queue statistics for durable clients through the disconnect and reconnect cycles.

**Configure Durable Subscriptions and Continuous Queries**

The register interest and query creation methods all have an optional boolean parameter for indicating durability. By default all are non-durable.

```
// Durable registration
// Define keySpecification, interestResultPolicy, durability 
exampleRegion.registerInterest(keySpecification, interestResultPolicySpecification, true);

// Durable CQ
// Define cqName, queryString, cqAttributes, durability
CqQuery myCq = queryService.newCq(cqName, queryString, cqAttributes, true);
```

Save only critical messages while the client is disconnected by only indicating durability for critical subscriptions and CQs. When the client is connected to its servers, it receives messages for all keys and queries reqistered. When the client is disconnected, non-durable interest registrations and CQs are discontinued but all messages already in the queue for them remain there.

**Note:** For a single durable client ID, you must maintain the same durability of your registrations and queries between client runs.

**Program the Client to Manage Durable Messaging**

Program your durable client to be durable-messaging aware when it disconnects, reconnects, and handles events from the server.

1. Disconnect with a request to keep your queues active by using `Pool.close` or `ClientCache.close` with the boolean `keepalive` parameter.

   ```
   clientCache.close(true);
   ```

   To be retained during client down time, durable continuous queries (CQs) must be executing at the time of disconnect.

2. Program your durable client’s reconnection to:

   1. If desired, detect whether the previously registered subscription queue is available upon durable client reconnection and the count of pending events in the queue. Based on the results, you can then decide whether to receive the remaining events or close the cache if the number is too large.

      For example, for a client with only the default pool created:

      ```
      int pendingEvents = cache.getDefaultPool().getPendingEventCount();
      
      if (pendingEvents == -2) { // client connected for the first time  … // continue
      } 
      else if (pendingEvents == -1) { // client reconnected but after the timeout period  
      … // handle possible data loss
      } 
      else { // pendingEvents >= 0  
      … // decide to invoke readyForEvents() or ClientCache::close(false)/pool.destroy()
      }
      ```

      For a client with multiple pools:

      ```
      int pendingEvents = 0;
      
      int pendingEvents1 = PoolManager.find(“pool1”).getPendingEventCount();
      
      pendingEvents += (pendingEvents1 > 0) ? pendingEvents1 : 0;
      
      int pendingEvents2 = PoolManager.find(“pool2”).getPendingEventCount();
      
      pendingEvents += (pendingEvents2 > 0) ? pendingEvents2 : 0;
      
      // process individual pool counts separately.
      ```

      The `getPendingEventCount` API can return the following possible values:

      - A value representing a count of events pending at the server. Note that this count is an approximate value based on the time the durable client pool connected or reconnected to the server. Any number of invocations will return the same value.
      - A zero value if there are no events pending at server for this client pool
      - A negative value indicates that no queue is available at the server for the client pool.
        - -1 indicates that the client pool has reconnected to the server after its durable-client-timeout period has elapsed. The pool’s subscription queue has been removed possibly causing data loss.
        - A value of -2 indicates that this client pool has connected to server for the first time.

   2. Connect, initialize the client cache, regions, and any cache listeners, and create and execute any durable continuous queries.

   3. Run all interest registration calls.

      **Note:** Registering interest with `InterestResultPolicy.KEYS_VALUES` initializes the client cache with the *current* values of specified keys. If concurrency checking is enabled for the region, any earlier (older) region events that are replayed to the client are ignored and are not sent to configured listeners. If your client must process all replayed events for a region, register with `InterestResultPolicy.KEYS` or `InterestResultPolicy.NONE` when reconnecting. Or, disable concurrency checking for the region in the client cache. See [Consistency for Region Updates](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).

   4. Call `ClientCache.readyForEvents` so the server will replay stored events. If the ready message is sent earlier, the client may lose events.

   ```
   ClientCache clientCache = ClientCacheFactory.create(); 
   // Here, create regions, listeners that are not defined in the cache.xml . . .
   // Here, run all register interest calls before doing anything else
   clientCache.readyForEvents(); 
   ```

3. When you program your durable client `CacheListener`:

   1. Implement the callback methods to behave properly when stored events are replayed. The durable client’s `CacheListener` must be able to handle having events played after the fact. Generally listeners receive events very close to when they happen, but the durable client may receive events that occurred minutes before and are not relevant to current cache state.
   2. Consider whether to use the `CacheListener` callback method, `afterRegionLive`, which is provided specifically for the end of durable event replay. You can use it to perform application-specific operations before resuming normal event handling. If you do not wish to use this callback, and your listener is an instance of `CacheListener` (instead of a `CacheListenerAdapter`) implement `afterRegionLive` as an empty method.

**Initial Operation**

The initial startup of a durable client is similar to the startup of any other client, except that it specifically calls the `ClientCache.readyForEvents` method when all regions and listeners on the client are ready to process messages from the server.

**Disconnection**

While the client and servers are disconnected, their operation varies depending on the circumstances.

- **Normal disconnect**. When a client closes its connection, the servers stop sending messages to the client and release its connection. If the client requests it, the servers maintain the queues and durable interest list information until the client reconnects or times out. The non-durable interest lists are discarded. The servers continue to queue up incoming messages for entries on the durable interest list. All messages that were in the queue when the client disconnected remain in the queue. If the client requests not to have its subscriptions maintained, or if there are no durable subscriptions, the servers unregister the client and do the same cleanup as for a non-durable client.
- **Abnormal disconnect**. If the client crashes or loses its connections to all servers, the servers automatically maintain its message queue and durable subscriptions until it reconnects or times out.
- **Client disconnected but operational**. If the client operates while it is disconnected, it gets what data it can from the local client cache. Since updates are not allowed, the data can become stale. An `UnconnectedException` occurs if an update is attempted.
- **Client stays disconnected past timeout period**. The servers track how long to keep a durable subscription queue alive based on the `durable-client-timeout` setting. If the client remains disconnected longer than the timeout, the servers unregister the client and do the same cleanup that is performed for a non-durable client. The servers also log an alert. When a timed-out client reconnects, the servers treat it as a new client making its initial connection.

**Reconnection**

During initialization, the client cache is not blocked from doing operations, so you might be receiving old stored events from the server at the same time that your client cache is being updated by much more current events. These are the things that can act on the cache concurrently:

- Results returned by the server in response to the client’s interest registrations.
- Client cache operations by the application.
- Callbacks triggered by replaying old events from the queue

Geode handles the conflicts between the application and interest registrations so they do not create cache update conflicts. But you must program your event handlers so they don’t conflict with current operations. This is true for all event handlers, but it is especially important for those used in durable clients. Your handlers may receive events well after the fact and you must ensure your programming takes that into account.

This figure shows the three concurrent procedures during the initialization process. The application begins operations immediately on the client (step 1), while the client’s cache ready message (also step 1) triggers a series of queue operations on the servers (starting with step 2 on the primary server). At the same time, the client registers interest (step 2 on the client) and receives a response from the server. Message B2 applies to an entry in Region A, so the cache listener handles B2’s event. Because B2 comes before the marker, the client does not apply the update to the cache.

![Durable client reconnection. ](assets/ClientServerAdvancedTopics-6.png)

**Durable Event Replay**

When a durable client reconnects before the timeout period, the servers replay the events that were stored while the client was gone and then resume normal event messaging to the client. To avoid overwriting current entries with old data, the stored events are not applied to the client cache. Stored events are distinguished from new normal events by a marker that is sent to the client once all old events are replayed.

1. All servers with a queue for this client place a marker in their queue when the client reconnects.
2. The primary server sends the queued messages to the client up to the marker.
3. The client receives the messages but does not apply the usual automatic updates to its cache. If cache listeners are installed, they handle the events.
4. The client receives the marker message indicating that all past events have been played back.
5. The server sends the current list of live regions.
6. For every `CacheListener` in each live region on the client, the marker event triggers the `afterRegionLive` callback. After the callback, the client begins normal processing of events from the server and applies the updates to its cache.

Even when a new client starts up for the first time, the client cache ready markers are inserted in the queues. If messages start coming into the new queues before the servers insert the marker, those messages are considered as having happened while the client was disconnected, and their events are replayed the same as in the reconnect case.

**Application Operations During Interest Registration**

Application operations take precedence over interest registration responses. The client can perform operations while it is receiving its interest registration responses. When adding register interest responses to the client cache, the following rules are applied:

- If the entry already exists in the cache with a valid value, it is not updated.
- If the entry is invalid, and the register interest response is valid, the valid value is put into the cache.
- If an entry is marked destroyed, it is not updated. Destroyed entries are removed from the system after the register interest response is completed.
- If the interest response does not contain any results, because all of those keys are absent from the server’s cache, the client’s cache can start out empty. If the queue contains old messages related to those keys, the events are still replayed in the client’s cache.



#### Tuning Client/Server Event Messaging

The server uses an asynchronous messaging queue to send events to its clients. Every event in the queue originates in an operation performed by a thread in a client, a server, or an application in the server’s or some other cluster. The event message has a unique identifier composed of the originating thread’s ID combined with its member’s distributed system member ID, and the sequential ID of the operation. So the event messages originating in any single thread can be grouped and ordered by time from lowest sequence ID to highest. Servers and clients track the highest sequential ID for each member thread ID.

A single client thread receives and processes messages from the server, tracking received messages to make sure it does not process duplicate sends. It does this using the process IDs from originating threads.

![img](assets/tune_cs_event_messaging.svg)

The client’s message tracking list holds the highest sequence ID of any message received for each originating thread. The list can become quite large in systems where there are many different threads coming and going and doing work on the cache. After a thread dies, its tracking entry is not needed. To avoid maintaining tracking information for threads that have died, the client expires entries that have had no activity for more than the `subscription-message-tracking-timeout`.

- **Conflate the Server Subscription Queue**
- **Limit the Server’s Subscription Queue Memory Use**
- **Tune the Client’s Subscription Message Tracking Timeout**



##### Conflate the Server Subscription Queue

Conflating the server subscription queue can save space in the server and time in message processing.

Enable conflation at the server level in the server region configuration:

```
<region ... >
  <region-attributes enable-subscription-conflation="true" /> 
</region>
```

Override the server setting as needed, on a per-client basis, in the client’s `gemfire.properties`:

```
conflate-events=false
```

Valid `conflate-events` settings are: - `server`, which uses the server settings - `true`, which conflates everything sent to the client - `false`, which does not conflate anything sent to this client

Conflation can both improve performance and reduce the amount of memory required on the server for queuing. The client receives only the latest available update in the queue for a particular entry key. Conflation is disabled by default.

Conflation is particularly useful when a single entry is updated often and the intermediate updates don’t require processing by the client. With conflation, if an entry is updated and there is already an update in the queue for its key, the existing update is removed and the new update is placed at the end of the queue. Conflation is only done on messages that are not in the process of being sent to the client.

![img](assets/ClientServerAdvancedTopics-7.gif)

**Note:** This method of conflation is different from the one used for multi-site gateway sender queue conflation. It is the same as the method used for the conflation of peer-to-peer distribution messages within a single cluster.



##### Limit the Server's Subscription Queue Memory Use

These are options for limiting the amount of server memory the subscription queues consume.

- Optional: Conflate the subscription queue messages.

- Optional: Increase the frequency of queue synchronization. This only applies to configurations where server redundancy is used for high availability. Increase the client’s pool configuration, `subscription-ack-interval`. The client periodically sends a batch acknowledgment of messages to the server, rather than acknowledging each message individually. A lower setting speeds message delivery and generally reduces traffic between the server and client. A higher setting helps contain server queue size. Example:

  ```
  <!-- Set subscription ack interval to 3 seconds -->
  <cache> 
    <pool ... subscription-enabled="true" 
              subscription-ack-interval="3000"> 
    ... 
  </pool>
  ```

  You might want to lower the interval if you have a very busy system and want to reduce the space required in the servers for the subscription queues. More frequent acknowledgments means fewer events held in the server queues awaiting acknowledgment.

- Optional: Limit Queue Size. Cap the server queue size using overflow or blocking. These options help avoid out of memory errors on the server in the case of slow clients. A slow client slows the rate that the server can send messages, causing messages to back up in the queue, possibly leading to out of memory on the server. You can use one or the other of these options, but not both:

  - Optional: Overflow to Disk. Configure subscription queue overflow by setting the server’s `client-subscription` properties. With overflow, the most recently used (MRU) events are written out to disk, keeping the oldest events, the ones that are next in line to be sent to the client, available in memory. Example:

    ```
    <!-- Set overflow after 10K messages are enqueued -->
    <cache-server port="40404"> 
      <client-subscription 
        eviction-policy="entry" 
        capacity="10000" 
        disk-store-name="svrOverflow"/> 
    </cache-server>
    ```

  - Optional: Block While Queue Full. Set the server’s `maximum-message-count` to the maximum number of event messages allowed in any single subscription queue before incoming messages are blocked. You can only limit the message count, not the size allocated for messages. Examples:

    XML:

    ```
    <!-- Set the maximum message count to 50000 entries -->
      <cache-server port="41414" maximum-message-count="50000" />
    ```

    API:

    ```
    Cache cache = ...; 
    CacheServer cacheServer = cache.addCacheServer(); 
    cacheServer.setPort(41414); 
    cacheServer.setMaximumMessageCount(50000); 
    cacheServer.start(); 
    ```

    **Note:** With this setting, one slow client can slow the server and all of its other clients because this blocks the threads that write to the queues. All operations that add messages to the queue block until the queue size drops to an acceptable level. If the regions feeding these queues are partitioned or have `distributed-ack` or `global` scope, operations on them remain blocked until their event messages can be added to the queue. If you are using this option and see stalling on your server region operations, your queue capacity might be too low for your application behavior.



##### Tune the Client's Subscription Message Tracking Timeout

If the client pool’s `subscription-message-tracking-timeout` is set too low, your client will discard tracking records for live threads, increasing the likelihood of processing duplicate events from those threads.

This setting is especially important in systems where it is vital to avoid or greatly minimize duplicate events. If you detect that duplicate messages are being processed by your clients, increasing the timeout may help. Setting `subscription-message-tracking-timeout` may not completely eliminate duplicate entries, but careful configuration can help minimize occurrences.

Duplicates are monitored by keeping track of message sequence IDs from the source thread where the operation originated. For a long-running system, you would not want to track this information for very long periods or the information may be kept long enough for a thread ID to be recycled. If this happens, messages from a new thread may be discarded mistakenly as duplicates of messages from an old thread with the same ID. In addition, maintaining this tracking information for old threads uses memory that might be freed up for other things.

To minimize duplicates and reduce the size of the message tracking list, set your client `subscription-message-tracking-timeout` higher than double the sum of these times:

- The longest time your originating threads might wait between operations
- For redundant servers add:
  - The server’s `message-sync-interval`
  - Total time required for failover (usually 7-10 seconds, including the time to detect failure)

You risk losing live thread tracking records if you set the value lower than this. This could result in your client processing duplicate event messages into its cache for the associated threads. It is worth working to set the `subscription-message-tracking-timeout` as low as you reasonably can.

```
<!-- Set the tracking timeout to 70 seconds -->
<pool name="client" subscription-enabled="true" subscription-message-tracking-timeout="70000"> 
    ...
</pool>
```



### Configuring Multi-Site (WAN) Event Queues

In a multi-site (WAN) installation, Geode uses gateway sender queues to distribute events for regions that are configured with a gateway sender. AsyncEventListeners also use an asynchronous event queue to distribute events for configured regions. This section describes additional options for configuring the event queues that are used by gateway senders or AsyncEventListener implementations.

Before you begin, set up your multi-site (WAN) installation or configure asynchronous event queues and AsyncEventListener implementations. See [Configuring a Multi-site (WAN) System](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html#setting_up_a_multisite_system) or [Implementing an AsyncEventListener for Write-Behind Cache Event Handling](https://geode.apache.org/docs/guide/17/developing/events/implementing_write_behind_event_handler.html#implementing_write_behind_cache_event_handling).

- **Persisting an Event Queue**

  You can configure a gateway sender queue or an asynchronous event queue to persist data to disk similar to the way in which replicated regions are persisted.

- **Configuring Dispatcher Threads and Order Policy for Event Distribution**

  By default, Geode uses multiple dispatcher threads to process region events simultaneously in a gateway sender queue for distribution between sites, or in an asynchronous event queue for distributing events for write-behind caching. With serial queues, you can also configure the ordering policy for dispatching those events.

- **Conflating Events in a Queue**

  Conflating a queue improves distribution performance. When conflation is enabled, only the latest queued value is sent for a particular key.



#### Persisting an Event Queue

You can configure a gateway sender queue or an asynchronous event queue to persist data to disk similar to the way in which replicated regions are persisted.

Persisting a queue provides high availability for the event messaging that the sender performs. For example, if a persistent gateway sender queue exits for any reason, when the member that hosts the sender restarts it automatically reloads the queue and resumes sending messages. If an asynchronous event queue exits for any reason, write-back caching can resume where it left off when the queue is brought back online. Geode persists an event queue if you set the `enable-persistence` attribute to true. The queue is persisted to the disk store specified in the queue’s `disk-store-name` attribute, or to the default disk store if you do not specify a store name.

You must configure the event queue to use persistence if you are using persistent regions. The use of non-persistent event queues with persistent regions is not supported.

When you enable persistence for a queue, the `maximum-queue-memory` attribute determines how much memory the queue can consume before it overflows to disk. By default, this value is set to 100MB.

**Note:** If you configure a parallel queue and/or you configure multiple dispatcher threads for a queue, the values that are defined in the `maximum-queue-memory` and `disk-store-name` attributes apply to each instance of the queue.

In the example below the gateway sender queue uses “diskStoreA” for persistence and overflow, and the queue has a maximum queue memory of 100MB:

- XML example:

  ```
  <cache>
    <gateway-sender id="persistedsender1" parallel="false" 
     remote-distributed-system-id="1"
     enable-persistence="true"
     disk-store-name="diskStoreA"
     maximum-queue-memory="100"/> 
     ... 
  </cache>
  ```

- API example:

  ```
  Cache cache = new CacheFactory().create();
  
  GatewaySenderFactory gateway = cache.createGatewaySenderFactory();
  gateway.setParallel(false);
  gateway.setPersistenceEnabled(true);
  gateway.setDiskStoreName("diskStoreA");
  gateway.setMaximumQueueMemory(100); 
  GatewaySender sender = gateway.create("persistedsender1", "1");
  sender.start();
  ```

- gfsh:

  ```
  gfsh>create gateway-sender --id="persistedsender1 --parallel=false 
  --remote-distributed-system-id=1 --enable-persistence=true --disk-store-name=diskStoreA 
  --maximum-queue-memory=100
  ```

If you were to configure 10 dispatcher threads for the serial gateway sender, then the total maximum memory for the gateway sender queue would be 1000MB on each Geode member that hosted the sender, because Geode creates a separate copy of the queue per thread..

The following example shows a similar configuration for an asynchronous event queue:

- XML example:

  ```
  <cache>
     <async-event-queue id="persistentAsyncQueue" persistent="true"
      disk-store-name="diskStoreA" parallel="true">
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

- API example:

  ```
  Cache cache = new CacheFactory().create();
  AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
  factory.setPersistent(true);
  factory.setDiskStoreName("diskStoreA");
  factory.setParallel(true);
  AsyncEventListener listener = new MyAsyncEventListener();
  AsyncEventQueue persistentAsyncQueue = factory.create("customerWB", listener);
  ```

- gfsh:

  ```
  gfsh>create async-event-queue --id="persistentAsyncQueue" --persistent=true 
  --disk-store="diskStoreA" --parallel=true --listener=MyAsyncEventListener 
  --listener-param=url#jdbc:db2:SAMPLE --listener-param=username#gfeadmin --listener-param=password#admin1
  ```



#### Configuring Dispatcher Threads and Order Policy for Event Distribution

By default, Geode uses multiple dispatcher threads to process region events simultaneously in a gateway sender queue for distribution between sites, or in an asynchronous event queue for distributing events for write-behind caching. With serial queues, you can also configure the ordering policy for dispatching those events.

By default, a gateway sender queue or asynchronous event queue uses 5 dispatcher threads per queue. This provides support for applications that have the ability to process queued events concurrently for distribution to another Geode site or listener. If your application does not require concurrent distribution, or if you do not have enough resources to support the requirements of multiple dispatcher threads, then you can configure a single dispatcher thread to process a queue.

- [Using Multiple Dispatcher Threads to Process a Queue](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html#concept_6C52A037E39E4FD6AE4C6A982A4A1A85__section_20E8EFCE89EB4DC7AA822D03C8E0F470)
- [Performance and Memory Considerations](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html#concept_6C52A037E39E4FD6AE4C6A982A4A1A85__section_C4C83B5C0FDD4913BA128365EE7E4E35)
- [Configuring the Ordering Policy for Serial Queues](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html#concept_6C52A037E39E4FD6AE4C6A982A4A1A85__section_4835BA30CDFD4B658BD2576F6BC2E23F)
- [Examples—Configuring Dispatcher Threads and Ordering Policy for a Serial Gateway Sender Queue](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html#concept_6C52A037E39E4FD6AE4C6A982A4A1A85__section_752F08F9064B4F67A80DA0A994671EA0)

**Using Multiple Dispatcher Threads to Process a Queue**

When multiple dispatcher threads are configured for a parallel queue, Geode simply uses multiple threads to process the contents of each individual queue. The total number of queues that are created is still determined by the number of Geode members that host the region.

When multiple dispatcher threads are configured for a serial queue, Geode creates an additional copy of the queue for each thread on each member that hosts the queue. To obtain the maximum throughput, increase the number of dispatcher threads until your network is saturated.

The following diagram illustrates a serial gateway sender queue that is configured with multiple dispatcher threads. ![img](assets/MultisiteConcurrency_WAN_Gateway.png)

**Performance and Memory Considerations**

When a serial gateway sender or an asynchronous event queue uses multiple dispatcher threads, consider the following:

- Queue attributes are repeated for each copy of the queue that is created for a dispatcher thread. That is, each concurrent queue points to the same disk store, so the same disk directories are used. If persistence is enabled and overflow occurs, the threads that insert entries into the queues compete for the disk. This applies to application threads and dispatcher threads, so it can affect application performance.
- The `maximum-queue-memory` setting applies to each copy of the serial queue. If you configure 10 dispatcher threads and the maximum queue memory is set to 100MB, then the total maximum queue memory for the queue is 1000MB on each member that hosts the queue.

**Configuring the Ordering Policy for Serial Queues**

When using multiple `dispatcher-threads` (greater than 1) with a serial event queue, you can also configure the `order-policy` that those threads use to distribute events from the queue. The valid order policy values are:

- **key (default)**. All updates to the same key are distributed in order. Geode preserves key ordering by placing all updates to the same key in the same dispatcher thread queue. You typically use key ordering when updates to entries have no relationship to each other, such as for an application that uses a single feeder to distribute stock updates to several other systems.
- **thread**. All region updates from a given thread are distributed in order. Geode preserves thread ordering by placing all region updates from the same thread into the same dispatcher thread queue. In general, use thread ordering when updates to one region entry affect updates to another region entry.
- **partition**. All region events that share the same partitioning key are distributed in order. Specify partition ordering when applications use a [PartitionResolver](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/PartitionResolver.html) to implement [custom partitioning](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/using_custom_partition_resolvers.html). With partition ordering, all entries that share the same “partitioning key” (RoutingObject) are placed into the same dispatcher thread queue.

You cannot configure the `order-policy` for a parallel event queue, because parallel queues cannot preserve event ordering for regions. Only the ordering of events for a given partition (or in a given queue of a distributed region) can be preserved.

**Examples—Configuring Dispatcher Threads and Ordering Policy for a Serial Gateway Sender Queue**

To increase the number of dispatcher threads and set the ordering policy for a serial gateway sender, use one of the following mechanisms.

- **cache.xml configuration**

  ```
  <cache>
    <gateway-sender id="NY" parallel="false" 
     remote-distributed-system-id="1"
     enable-persistence="true"
     disk-store-name="gateway-disk-store"
     maximum-queue-memory="200"
     dispatcher-threads=7 order-policy="key"/> 
     ... 
  </cache>
  ```

- **Java API configuration**

  ```
  Cache cache = new CacheFactory().create();
  
  GatewaySenderFactory gateway = cache.createGatewaySenderFactory();
  gateway.setParallel(false);
  gateway.setPersistenceEnabled(true);
  gateway.setDiskStoreName("gateway-disk-store");
  gateway.setMaximumQueueMemory(200);
  gateway.setDispatcherThreads(7);
  gateway.setOrderPolicy(OrderPolicy.KEY);
  GatewaySender sender = gateway.create("NY", "1");
  sender.start();
  ```

- **gfsh:**

  ```
  gfsh>create gateway-sender -d="NY" 
     --parallel=false 
     --remote-distributed-system-id="1"
     --enable-persistence=true
     --disk-store-name="gateway-disk-store"
     --maximum-queue-memory=200
     --dispatcher-threads=7 
     --order-policy="key"
  ```

The following examples show how to set dispatcher threads and ordering policy for an asynchronous event queue:

- **cache.xml configuration**

  ```
  <cache>
     <async-event-queue id="sampleQueue" persistent="true"
      disk-store-name="async-disk-store" parallel="false"
      dispatcher-threads=7 order-policy="key">
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

- **Java API configuration**

  ```
  Cache cache = new CacheFactory().create();
  AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
  factory.setPersistent(true);
  factory.setDiskStoreName("async-disk-store");
  factory.setParallel(false);
  factory.setDispatcherThreads(7);
  factory.setOrderPolicy(OrderPolicy.KEY);
  AsyncEventListener listener = new MyAsyncEventListener();
  AsyncEventQueue sampleQueue = factory.create("customerWB", listener);
  ```

  Entry updates in the current, in-process batch are not eligible for conflation.

- **gfsh:**

  ```
  gfsh>create async-event-queue --id="sampleQueue" --persistent=true
  --disk-store="async-disk-store" --parallel=false
  --dispatcher-threads=7 order-policy="key"
  --listener=myAsycEventListener 
  --listener-param=url#jdbc:db2:SAMPLE 
  --listener-param=username#gfeadmin 
  --listener-param=password#admin1
  ```



#### Conflating Events in a Queue

Conflating a queue improves distribution performance. When conflation is enabled, only the latest queued value is sent for a particular key.

**Note:** Do not use conflation if your receiving applications depend on the specific ordering of entry modifications, or if they need to be notified of every change to an entry.

Conflation is most useful when a single entry is updated frequently, but other sites only need to know the current value of the entry (rather than the value of each update). When an update is added to a queue that has conflation enabled, if there is already an update message in the queue for the entry key, then the existing message assumes the value of the new update and the new update is dropped, as shown here for key A.

![img](assets/MultiSite-4.gif)

**Note:** This method of conflation is different from the one used for server-to-client subscription queue conflation and peer-to-peer distribution within a cluster.

**Examples—Configuring Conflation for a Gateway Sender Queue**

To enable conflation for a gateway sender queue, use one of the following mechanisms:

- **cache.xml configuration**

  ```
  <cache>
    <gateway-sender id="NY" parallel="true" 
     remote-distributed-system-id="1"
     enable-persistence="true"
     disk-store-name="gateway-disk-store"
     enable-batch-conflation="true"/> 
     ... 
  </cache>
  ```

- **Java API configuration**

  ```
  Cache cache = new CacheFactory().create();
  
  GatewaySenderFactory gateway = cache.createGatewaySenderFactory();
  gateway.setParallel(true);
  gateway.setPersistenceEnabled(true);
  gateway.setDiskStoreName("gateway-disk-store");
  gateway.setBatchConflationEnabled(true);
  GatewaySender sender = gateway.create("NY", "1");
  sender.start();
  ```

  Entry updates in the current, in-process batch are not eligible for conflation.

- **gfsh:**

  ```
  gfsh>create gateway-sender --id="NY" --parallel=true 
     --remote-distributed-system-id="1"
     --enable-persistence=true
     --disk-store-name="gateway-disk-store"
     --enable-batch-conflation=true
  ```

The following examples show how to configure conflation for an asynchronous event queue:

- **cache.xml configuration**

  ```
  <cache>
     <async-event-queue id="sampleQueue" persistent="true"
      disk-store-name="async-disk-store" parallel="false"
      enable-batch-conflation="true">
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

- **Java API configuration**

  ```
  Cache cache = new CacheFactory().create();
  AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
  factory.setPersistent(true);
  factory.setDiskStoreName("async-disk-store");
  factory.setParallel(false);
  factory.setBatchConflationEnabled(true);
  AsyncEventListener listener = new MyAsyncEventListener();
  AsyncEventQueue sampleQueue = factory.create("customerWB", listener);
  ```

  Entry updates in the current, in-process batch are not eligible for conflation.

- **gfsh:**

  ```
  gfsh>create async-event-queue --id="sampleQueue" --persistent=true 
  --disk-store="async-disk-store" --parallel="false" 
  --listener=myAsyncEventListener 
  --listener-param=url#jdbc:db2:SAMPLE 
  --listener-param=username#gfeadmin 
  --listener-param=password#admin1
  ```



## Delta Propagation

Delta propagation allows you to reduce the amount of data you send over the network by including only changes to objects rather than the entire object.

- **How Delta Propagation Works**

  Delta propagation reduces the amount of data you send over the network. You do this by only sending the change, or delta, information about an object, instead of sending the entire changed object. If you do not use cloning when applying the deltas, you can also expect to generate less garbage in your receiving JVMs.

- **When to Avoid Delta Propagation**

  Generally, the larger your objects and the smaller the deltas, the greater the benefits of using delta propagation. Partitioned regions with higher redundancy levels generally benefit more from delta propagation. However, in some application scenarios, delta propagation does not show any significant benefits. On occasion it results in performance degradation.

- **Delta Propagation Properties**

  This topic describes the properties that can be used to configure delta propagation.

- **Implementing Delta Propagation**

  By default, delta propagation is enabled in your cluster. When enabled, delta propagation is used for objects that implement `org.apache.geode.Delta`. You program the methods to store and extract delta information for your entries and to apply received delta information.

- **Errors In Delta Propagation**

  This topic lists the errors that can occur when using delta propagation.

- **Delta Propagation Example**

  This topic provides an example of delta propagation.



### How Delta Propagation Works

Delta propagation reduces the amount of data you send over the network. You do this by only sending the change, or delta, information about an object, instead of sending the entire changed object. If you do not use cloning when applying the deltas, you can also expect to generate less garbage in your receiving JVMs.

In most distributed data management systems, the data stored in the system tends to be created once and then updated frequently. These updates are sent to other members for event propagation, redundancy management, and cache consistency in general. Tracking only the changes in an updated object and sending only the deltas mean lower network transmission costs and lower object serialization/deserialization costs. Performance improvements can be significant, especially when changes to an object are small relative to its overall size.



Geode propagates object deltas using methods that you program. The methods are in the `Delta`interface, which you implement in your cached objects’ classes. If any of your classes are plain old Java objects, you need to wrap them for this implementation.

This figure shows delta propagation for a change to an entry with key, k, and value object, v.

![img](assets/delta-propagation.png)

1. **get operation**. The `get` works as usual: the cache returns the full entry object from the local cache or, if it isn’t available there, from a remote cache or from a loader.
2. **update methods**. You need to add code to the object’s update methods so that they save delta information for object updates, in addition to the work they were already doing.
3. **put operation**. The `put` works as usual in the local cache, using the full value, then calls `hasDelta` to see if there are deltas and `toDelta` to serialize the information. Distribution is the same as for full values, according to member and region configuration.
4. **receipt of delta at remote member**. `fromDelta` extracts the delta information that was serialized by `toDelta` and applies it to the object in the local cache. The delta is applied directly to the existing value or to a clone, depending on how you configure it for the region.
5. **additional distributions**. As with full distributions, receiving members forward the delta according to their configurations and connections to other members. For example, if VM1 is a client and VM2 is a server, VM2 forwards the delta to its peers and its other clients as needed. Receiving members do not recreate the delta; `toDelta` is only called in the originating member.

**General Characteristics of Delta Propagation**

To use the delta propagation feature, all updates on a key in a region must have value types that implement the `Delta` interface. You cannot mix object types for an entry key where some of the types implement delta and some do not. This is because, when a type implementing the delta interface is received for an update, the existing value for the key is cast to a `Delta` type to apply the received delta. If the existing type does not also implement the `Delta` interface, the operation throws a `ClassCastException`.

**Note:** Only the object itself being placed in the cache can implement the `Delta` interface and propagate changes. Any sub-objects of the cache object do not propagate their changes.

Sometimes `fromDelta` cannot be invoked because there is no object to apply the delta to in the receiving cache. When this happens, the system automatically does a full value distribution to the receiver. These are the possible scenarios: 1. If the system can determine beforehand that the receiver does not have a local copy, it sends the initial message with the full value. This is possible when regions are configured with no local data storage, such as with the region shortcut settings `PARTITION_PROXY` and `REPLICATE_PROXY`. These configurations are used to accomplish things like provide data update information to listeners and to pass updates forward to clients. 2. In less obvious cases, such as when an entry has been locally deleted, first the delta is sent, then the receiver requests a full value and that is sent. Whenever the full value is received, any further distributions to the receiver’s peers or clients uses the full value.

Geode also does not propagate deltas for:

- Transactional commit
- The `putAll` operation
- JVMs running Geode versions that do not support delta propagation (6.0 and earlier)

**Supported Topologies and Limitations**

The following topologies support delta propagation (with some limitations):

- Peer-to-peer

  . Geode system members distribute and receive entry changes using delta propagation, with these requirements and caveats:

  - Regions must be partitioned or have their scope set to `distributed-ack` or `global`. The region shortcut settings for distributed regions use `distributed-ack` `scope`. Delta propagation does not work for regions with `distributed-no-ack` `scope` because the receiver could not recover if an exception occurred while applying the delta.
  - For partitioned regions, if a receiving peer does not hold the primary or a secondary copy of the entry, but still requires a value, the system automatically sends the full value.
  - To receive deltas, a region must be non-empty. The system automatically sends the full value to empty regions. Empty regions can send deltas.

- Client/server

  . Geode clients can always send deltas to the servers, and servers can usually sent deltas to clients. These configurations require the servers to send full values to the clients, instead of deltas:

  - When the client’s `gemfire.properties` setting `conflate-events` is set to true, the servers send full values for all regions.
  - When the server region attribute `enable-subscription-conflation` is set to true and the client `gemfire.properties` setting `conflate-events` is set to `server`, the servers send full values for the region.
  - When the client region is configured with the `PROXY` client region shortcut setting (empty client region), servers send full values.

- **Multi-site (WAN)**. Gateway senders do not send Deltas. The full value is always sent.



### When to Avoid Delta Propagation

Generally, the larger your objects and the smaller the deltas, the greater the benefits of using delta propagation. Partitioned regions with higher redundancy levels generally benefit more from delta propagation. However, in some application scenarios, delta propagation does not show any significant benefits. On occasion it results in performance degradation.

By default, delta propagation is enabled in your cluster.

These are the main factors that can reduce the performance benefits of using delta propagation:

- The added costs of deserializing your objects to apply deltas. Applying a delta requires the entry value to be deserialized. Once this is done, the object is stored back in the cache in deserialized form. This aspect of delta propagation only negatively impacts your system if your objects are not already being deserialized for other reasons, such as for indexing and querying or for listener operations. Once stored in deserialized form, there are reserialization costs for operations that send the object outside of the member, like distribution from a gateway sender, values sent in response to `netSearch` or client requests, and storage to disk. The more operations that require reserialization, the higher the overhead of deserializing the object. As with all serialization efforts, you can improve performance in serialization and deserialization by providing custom implementations of `DataSerializable` for your objects.
- Cloning when applying the delta. Using cloning can affect performance and generates extra garbage. Not using cloning is risky however, as you are modifying cached values in place. Without cloning, make sure you synchronize your entry access to keep your cache from becoming inconsistent.
- Problems applying the delta that cause the system to go back to the originator for the full entry value. When this happens, the overall operation costs more than sending the full entry value in the first place. This can be additionally aggravated if your delta is sent to a number of recipients, all or most of them request a full value, and the full value send requires the object to be serialized.
- Disk I/O costs associated with overflow regions. If you use eviction with overflow to disk, on-disk values must be brought into memory in order to apply the delta. This is much more costly than just removing the reference to the disk copy, as you would do with a full value distribution into the cache.



### Delta Propagation Properties

This topic describes the properties that can be used to configure delta propagation.

Delta propagation properties can be configured through the API and through the `gemfire.properties` and `cache.xml` files.

**delta-propagation**

A `gemfire.properties` boolean that enables or disables delta propagation. When false, full entry values are sent for every update. The default setting is true, which enables delta propagation.

Disable delta propagation as follows:

- `gemfire.properties`:

  ```
  delta-propagation=false
  ```

- API:

  ```
  Properties props = new Properties();
  props.setProperty("delta-propagation", false);
  this.cache = new ClientCacheFactory(props).create();
  ```

**cloning-enabled**

A region attributes boolean that affects how `fromDelta` applies deltas to the local cache. When true, the updates are applied to a clone of the value and then the clone is saved to the cache. When false, the value is modified in place in the cache. The default value is false.

Exceptions to this behavior:

- If the `Cache` attribute `copy-on-read` is true, cloning is enabled, regardless of what this attribute is set to.
- If the `Region` attribute `off-heap` is true, cloning is enabled, regardless of what this attribute is set to.

Cloning can be expensive, but it ensures that the new object is fully initialized with the delta before any application code sees it.

When cloning is enabled, by default Geode does a deep copy of the object, using serialization. You may be able to improve performance by implementing `java.lang.Cloneable` and then implementing the `clone` method, making a deep copy of anything to which a delta may be applied. The goal is to reduce significantly the overhead of copying the object while still retaining the isolation needed for your deltas.

Without cloning:

- It is possible for application code to read the entry value as it is being modified, possibly seeing the value in an intermediate, inconsistent state, with just part of the delta applied. You may choose to resolve this issue by having your application code synchronize on reads and writes.
- Geode loses any reference to the old value because the old value is transformed in place into the new value. Because of this, your `CacheListener` sees the same new value returned for `EntryEvent.getOldValue` and `EntryEvent.getNewValue` .
- Exceptions thrown from `fromDelta` may leave your cache in an inconsistent state. Without cloning, any interruption of the delta application could leave you with some of the fields in your cached object changed and others unchanged. If you do not use cloning, keep this in mind when you program your error handling in your `fromDelta` implementation.

With cloning:

- The `fromDelta` method generates more garbage in memory.
- Performance is reduced.

Enable cloning as follows:

- `cache.xml`:

  ```
  <region name="region_with_cloning">
      <region-attributes refid="REPLICATE" cloning-enabled="true">
      </region-attributes>
  </region>
  ```

- API:

  ```
  RegionFactory rf = cache.createRegionFactory(REPLICATE);
  rf.setCloningEnabled(true);
  custRegion = rf.create("customer");
  ```

- gfsh:

  ```
  gfsh>create region --name="region_with_cloning" --type=REPLICATE
  --enable-cloning=true
  ```



### Implementing Delta Propagation

By default, delta propagation is enabled in your cluster. When enabled, delta propagation is used for objects that implement `org.apache.geode.Delta`. You program the methods to store and extract delta information for your entries and to apply received delta information.

Use the following procedure to implement delta propagation in your cluster.

1. Study your object types and expected application behavior to determine which regions can benefit from using delta propagation. Delta propagation does not improve performance for all data and data modification scenarios. See [When to Avoid Delta Propagation](https://geode.apache.org/docs/guide/17/developing/delta_propagation/when_to_use_delta_prop.html#when_to_use_delta_prop).

2. For each region where you are using delta propagation, choose whether to enable cloning using the delta propagation property `cloning-enabled`. Cloning is disabled by default. See [Delta Propagation Properties](https://geode.apache.org/docs/guide/17/developing/delta_propagation/delta_propagation_properties.html#delta_propagation_properties).

3. If you do not enable cloning, review all associated listener code for dependencies on `EntryEvent.getOldValue`. Without cloning, Geode modifies the entry in place and so loses its reference to the old value. For delta events, the `EntryEvent` methods `getOldValue` and `getNewValue` both return the new value.

4. For every class where you want delta propagation, implement

    

   ```
   org.apache.geode.Delta
   ```

    

   and update your methods to support delta propagation. Exactly how you do this depends on your application and object needs, but these steps describe the basic approach:

   1. If the class is a plain old Java object (POJO), wrap it for this implementation and update your code to work with the wrapper class.
   2. Define as transient any extra object fields that you use to manage delta state. This can help performance when the full object is distributed. Whenever standard Java serialization is used, the transient keyword indicates to Java to not serialize the field.
   3. Study the object contents to decide how to handle delta changes. Delta propagation has the same issues of distributed concurrency control as the distribution of full objects, but on a more detailed level. Some parts of your objects may be able to change independent of one another while others may always need to change together. Send deltas large enough to keep your data logically consistent. If, for example, field A and field B depend on each other, then your delta distributions should either update both fields or neither. As with regular updates, the fewer producers you have on a data region, the lower your likelihood of concurrency issues.
   4. In the application code that puts entries, put the fully populated object into the local cache. Even though you are planning to send only deltas, errors on the receiving end could cause Geode to request the full object, so you must provide it to the originating put method. Do this even in empty producers, with regions configured for no local data storage. This usually means doing a get on the entry unless you are sure it does not already exist anywhere in the distributed region.
   5. Change each field’s update method to record information about the update. The information must be sufficient for `toDelta` to encode the delta and any additional required delta information when it is invoked.
   6. Write `hasDelta` to report on whether a delta is available.
   7. Write `toDelta` to create a byte stream with the changes to the object and any other information `fromDelta` will need to apply the changes. Before returning from `toDelta`, reset your delta state to indicate that there are no delta changes waiting to be sent.
   8. Write `fromDelta` to decode the byte stream that `toDelta` creates and update the object.
   9. Make sure you provide adequate synchronization to your object to maintain a consistent object state. If you do not use cloning, you will probably need to synchronize on reads and writes to avoid reading partially written updates from the cache.This synchronization might involve `toDelta`, `fromDelta`, `toData`, `fromData`, and other methods that access or update the object. Additionally, your implementation should take into account the possibility of concurrent invocations of `fromDelta` and one or more of the object’s update methods.



### Errors In Delta Propagation

This topic lists the errors that can occur when using delta propagation.

Errors in delta propagation fall into two categories based on how they are handled by the system:

- Problems applying the delta that can be remedied by requesting the full value in place of the delta. Your`put`operation does not see errors or exceptions related to this type of delta propagation failure. The system automatically does a full value distribution from the sender to the receiver where the problem occurs. This type of error includes:

  - Unavailable entry value in the receiving cache, either because the entry is missing or its value is null. In both cases, there is nothing to apply the delta to and the full value must be sent. This is most likely to occur if you destroy or invalidate your entries locally, either through application calls or through configured actions like eviction or entry expiration.
  - `InvalidDeltaException` thrown by `fromDelta` method, programmed by you. This exception enables you to avoid applying deltas that would violate data consistency checks or other application requirements.
  - Any error applying the delta in a client in server-to-client propagation. The client logs a warning in addition to retrieving the full value from the server.

- Problems creating or distributing the delta that cannot be fixed by distributing the full value. In these cases, your`put` operation fails with an exception. This type of error includes:

  - Error or exception in `hasDelta` or `toDelta`.
  - Error or exception in a server or peer receiver that fall outside of the situations described above in the first category.



### Delta Propagation Example

This topic provides an example of delta propagation.

In this example, the feeder client is connected to the first server, and the receiver client is connected to the second. The servers are peers to each other.

![img](assets/DeltaPropagation-3.gif)

The example demonstrates the following operations:

1. In the Feeder client, the application updates the entry object and puts the entry. In response to the `put`, Geode calls `hasDelta`, which returns true, so Geode calls `toDelta` and forwards the extracted delta to the server. If `hasDelta` returned false, Geode would distribute the full entry value.
2. In Server1, Geode applies the delta to the cache, distributes the received delta to the server’s peers, and forwards it to any other clients with interest in the entry (there are no other clients to Server1 in this example)
3. In Server2, Geode applies the delta to the cache and forwards it to its interested clients, which in this case is just the Receiver client.



This example shows the basic approach to programming a `Delta` implementation.

```java
package delta;

import org.apache.geode.Delta;
import org.apache.geode.InvalidDeltaException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Sample implementation of Delta
 * 
 * @author GemStone Systems, Inc.
 * @since 6.1
 */
public class SimpleDelta implements Delta, Serializable {

// Original object fields
    private int intVal;
    private double doubleVal;

    // Added for delta - one boolean per field to track changed status
    private transient boolean intFldChd = false;
    private transient boolean dblFldChd = false;

    public SimpleDelta(){}

    public SimpleDelta(int i, double d){
        this.intVal = i;
        this.doubleVal = d;
    }

    public boolean hasDelta() {
        return this.intFldChd || this.dblFldChd;
    }

    public void toDelta(DataOutput out) throws IOException {
        System.out.println("Extracting delta from " + this.toString());
    // Write information on what has changed to the 
    // data stream, so fromDelta knows what it's getting 
        out.writeBoolean(intFldChd);
        if (intFldChd) {
    // Write just the changes into the data stream
            out.writeInt(this.intVal);
    // Once the delta information is written, reset the delta status field
            this.intFldChd = false;
            System.out.println(" Extracted delta from field 'intVal' = "
                                                + this.intVal);
        }
        out.writeBoolean(dblFldChd);
        if (dblFldChd) {
            out.writeDouble(this.doubleVal);
            this.dblFldChd = false;
            System.out.println(" Extracted delta from field 'doubleVal' = "
                                                + this.doubleVal);
        }
    }

    public void fromDelta(DataInput in) throws IOException, InvalidDeltaException {
        System.out.println("Applying delta to " + this.toString());
        // For each field, read whether there is a change 
        if (in.readBoolean()) {
            // Read the change and apply it to the object 
            this.intVal = in.readInt();
            System.out.println(" Applied delta to field 'intVal' = "
                        + this.intVal);
        }
        if (in.readBoolean()) {
            this.doubleVal = in.readDouble();
            System.out.println(" Applied delta to field 'doubleVal' = "
                        + this.doubleVal);
        }
    }
    // In the setter methods, add setting of delta-related 
    // fields indicating what has changed 
    public void setIntVal(int anIntVal) {
        this.intFldChd = true;
        this.intVal = anIntVal;
    }

    public void setDoubleVal(double aDoubleVal) {
        this.dblFldChd = true;
        this.doubleVal = aDoubleVal;
    }

    public String toString() {
        return "SimpleDelta [ hasDelta = " + hasDelta() + ", intVal = " + 
                this.intVal + ", doubleVal = {" + this.doubleVal + "} ]";
    }
}
```



## Querying

Geode provides a SQL-like querying language called OQL that allows you to access data stored in Geode regions.

Since Geode regions are key-value stores where values can range from simple byte arrays to complex nested objects, Geode uses a query syntax based on OQL (Object Query Language) to query region data. OQL is very similar to SQL, but OQL allows you to query complex objects, object attributes, and methods.

- **Querying FAQ and Examples**

  This topic answers some frequently asked questions on querying functionality. It provides examples to help you get started with Geode querying.

- **Querying with OQL**

  This section provides a high-level introduction to Geode querying such as building a query string and describes query language features.

- **Advanced Querying**

  This section includes advanced querying topics such as using query indexes, using query bind parameters, querying partitioned regions and query debugging.

- **Working with Indexes**

  The Geode query engine supports indexing. An index can provide significant performance gains for query execution.



### Querying FAQ and Examples

This topic answers some frequently asked questions on querying functionality. It provides examples to help you get started with Geode querying.

For additional information on Geode querying, see [Querying](https://geode.apache.org/docs/guide/17/developing/querying_basics/chapter_overview.html).

- [How do I write and execute a query against a Geode region?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_7A4D2C6A4E2C4F4384C158FFCA9CA1C0)
- [Can I see query string examples, listed by query type?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_641D97CD874D4182961C85429ACA1B05)
- [Which APIs should I use to write my queries?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_5383407F9D004D4EB4E695252EBA1EF0)
- [How do I invoke an object’s method in a query?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_3E6E4B33D57846008EF4404D2B687597)
- [Can I invoke a static method on an object in a query?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_9221C29BC1FD49D7BBD26BB34D5BDEB8)
- [How do I write a reusable query?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_907DBBBA1AEC4570A15B3491B0A7DF0E)
- [When should I create indexes to use in my queries?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_3A9528E8E43140BAA0D5A1457CCAB2D2)
- [How do I create an index?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_76CDCCFBDB134A339DBE556C28D48F11)
- [Can I query a partitioned region? Can I perform a join query on a partitioned region?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_EDD17817450C4FC0B510CD87DB2FCD16)
- [How can I improve the performance of a partitioned region query?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_5FF905E0D10D4CDF9E6F49A70848AF69)
- [Which query language elements are supported in Geode?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_FBC59A5420FD40D6907A302A1D50DF7E)
- [How do I debug queries?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_314B88A55B514B88A12DC36227A2D4EF)
- [Can I use implicit attributes or methods in my query?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__implicit_attributes)
- [How do I perform a case-insensitive search on a field in OQL?](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E__section_ayq_hqw_1r)

**How do I write and execute a query against a Geode region?**

To write and execute a query in Geode, you can use any of the following mechanisms. Sample query code follows.

- Geode querying APIs
- [gfsh](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html) command-line interface; in particular the [query](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/query.html) command
- REST API [query endpoints](https://geode.apache.org/docs/guide/17/rest_apps/rest_queries.html#concept_mmg_d35_m4)

**Sample Geode Query Code (Java)**

```
// Identify your query string.
 String queryString = "SELECT * FROM /exampleRegion";

 // Get QueryService from Cache.
 QueryService queryService = cache.getQueryService();

 // Create the Query Object.
 Query query = queryService.newQuery(queryString);

 // Execute Query locally. Returns results set.
 SelectResults results = (SelectResults)query.execute();

 // Find the Size of the ResultSet.
 int size = results.size();

 // Iterate through your ResultSet.
 Portfolio p = (Portfolio)results.iterator().next(); /* Region containing Portfolio object. */
```

**Can I see query string examples, listed by query type?**

The following example query strings use the `/exampleRegion` whose keys are the portfolio ID and whose values correspond to the summarized data shown in the following class definitions:

```
class Portfolio implements DataSerializable {
   int ID;
   String type;
   String status;
   Map positions;
}
class Position implements DataSerializable {
   String secId;
   double mktValue;
   double qty;
}
```

**Basic WHERE Clause Examples**

In the following examples, the status field is type String and the ID field is type int. See [Supported Literals](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals) for a complete list of literals supported in Geode querying.

1. Select all active portfolios.

   ```
   SELECT * FROM /exampleRegion WHERE status = 'active'
   ```

2. Select all portfolios whose status begins with ‘activ’.

   ```
   SELECT * FROM /exampleRegion p WHERE p.status LIKE 'activ%'
   ```

3. Select all portfolios whose ID is greater than 100.

   ```
   SELECT * from /exampleRegion p WHERE p.ID > 100
   ```

**Using DISTINCT**

Select distinct Objects from the region that satisfy the where clause condition of status = 'active’.

```
SELECT DISTINCT * FROM /exampleRegion WHERE status = 'active'
```

**Aliases and Synonyms**

In the query string, the path expressions (region and its objects) can be defined using an alias. This alias can be used or referred to in other places in the query.

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.status = 'active'
SELECT p.ID, p.status FROM /exampleRegion p WHERE p.ID > 0
```

**Using the NOT Operator**

See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) for a complete list of supported operators.

```
SELECT DISTINCT * FROM /exampleRegion WHERE NOT (status = 'active') AND ID = 2
SELECT * FROM /exampleRegion WHERE NOT (ID IN SET(1,2))
```

**Using the AND and OR Operators**

See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) for a complete list of supported operators.

```
SELECT * FROM /exampleRegion WHERE ID > 4 AND ID < 9
SELECT * FROM /exampleRegion WHERE ID = 0 OR ID = 1
SELECT DISTINCT p.status FROM /exampleRegion p 
WHERE (p.createTime IN SET (10|) OR p.status IN SET ('active')) AND p.ID > 0
```

**Using not equal to**

```
SELECT * FROM /exampleRegion portfolio WHERE portfolio.ID <> 2
SELECT * FROM /exampleRegion portfolio WHERE portfolio.ID != 2
```

**Projection attribute example**

```
SELECT p.get('account') FROM /exampleRegion p
```

**Querying nested collections**

The following query uses Positions of type HashMap.

```
SELECT p, pos FROM /exampleRegion p, p.positions.values pos WHERE pos.secId = 'VMW'
```

**Using LIMIT**

```
SELECT * FROM /exampleRegion p WHERE p.ID > 0 LIMIT 2
```

**Using COUNT**

See [COUNT](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_B2CBA00EB83F463DAF4769D7859C64C8) for more information.

```
SELECT COUNT(*) FROM /exampleRegion WHERE ID > 0
SELECT COUNT(*) FROM /exampleRegion WHERE ID > 0 LIMIT 50
SELECT COUNT(*) FROM /exampleRegion WHERE ID > 0 AND status LIKE 'act%'
SELECT COUNT(*) FROM /exampleRegion WHERE ID IN SET(1,2,3,4,5)
SELECT COUNT(*) FROM /exampleRegion p, p.positions.values pos 
WHERE p.ID > 0 AND pos.secId 'IBM'
SELECT DISTINCT COUNT(*) FROM /exampleRegion p, p.positions.values pos
WHERE p.ID > 0 OR p.status = 'active' OR pos.secId OR pos.secId = 'IBM'
```

**Using LIKE**

```
SELECT * FROM /exampleRegion ps WHERE ps.pkid LIKE '_bc'
SELECT * FROM /exampleRegion ps WHERE ps.status LIKE '_b_' OR ps.pkid = '2'
SELECT * FROM /exampleRegion ps WHERE ps.status LIKE '%b%
```

**Using Region Entry Keys and Values**

```
SELECT * FROM /exampleRegion.keys k WHERE k.ID = 1
SELECT entry.value FROM /exampleRegion.entries entry WHERE entry.key = '1'
SELECT key, positions FROM /exampleRegion.entrySet, value.positions.values positions 
WHERE positions.mktValue >= 25.00
SELECT DISTINCT entry.value FROM /exampleRegion.entries entry WHERE entry.key = '1'
SELECT * FROM /exampleRegion.entries entry WHERE entry.value.ID > 1
SELECT * FROM /exampleRegion.keySet key WHERE key = '1'
SELECT * FROM /exampleRegion.values portfolio 
WHERE portfolio.status = 'active'
```

**Nested Queries**

```
IMPORT "query".Portfolio;
SELECT * FROM /exampleRegion, (SELECT DISTINCT * FROM /exampleRegion p TYPE Portfolio, p.positions 
WHERE value!=null)
SELECT DISTINCT * FROM (SELECT DISTINCT * FROM /exampleRegion portfolios, positions pos) 
WHERE pos.value.secId = 'IBM'
SELECT * FROM /exampleRegion portfolio 
WHERE portfolio.ID IN (SELECT p2.ID FROM /exampleRegion2 p2 WHERE p2.ID > 1)
SELECT DISTINCT * FROM /exampleRegion p, (SELECT DISTINCT pos 
FROM /exampleRegion x, x.positions.values pos WHERE x.ID = p.ID ) AS itrX
```

**Query the results of a FROM clause expression**

```
SELECT DISTINCT * FROM (SELECT DISTINCT * FROM /Portfolios ptf, positions pos) p 
WHERE p.get('pos').value.secId = 'IBM'
```

**Hash Map Query**

Query using a hashmap. In the following examples, 'version’ is one of the keys in the hashmap.

```
SELECT * FROM /exampleRegion p WHERE p['version'] = '1.0'
SELECT entry.key, entry.value FROM /exampleRegion.entries entry 
WHERE entry.value['version'] = '100'
```

**Map example where “map” is a nested HashMap object**

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.portfolios['key2'] >= 3
```

**Example Queries that Fetch Array Values**

```
SELECT * FROM /exampleRegion p WHERE p.names[0] = 'aaa'
SELECT * FROM /exampleRegion p WHERE p.collectionHolderMap.get('1').arr[0] = '0'
```

**Using ORDER BY (and ORDER BY with LIMIT)**

You must use the DISTINCT keyword with ORDER BY queries.

```
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID asc
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID desc
SELECT DISTINCT key.ID, key.status AS st FROM /exampleRegion.keys key 
WHERE key.status = 'inactive' ORDER BY key.status desc, key.ID LIMIT 1
SELECT DISTINCT * FROM /exampleRegion p ORDER BY p.getP1().secId, p.ID dec, p.ID LIMIT 9
SELECT DISTINCT * FROM /exampleRegion p ORDER BY p.ID, val.secId LIMIT 1
SELECT DISTINCT e.key FROM /exampleRegion.entrySet e ORDER BY e.key.ID desc, e.key.pkid desc
SELECT DISTINCT p.names[1] FROM /exampleRegion p ORDER BY p.names[1]
```

**Join Queries**

```
SELECT * FROM /exampleRegion portfolio1, /exampleRegion2 portfolio2 
WHERE portfolio1.status = portfolio2.status
SELECT portfolio1.ID, portfolio2.status FROM /exampleRegion portfolio1, /exampleRegion2 portfolio2 
WHERE portfolio1.status = portfolio2.status
SELECT * FROM /exampleRegion portfolio1, portfolio1.positions.values positions1, 
/exampleRegion2 portfolio2, portfolio2.positions.values positions2 WHERE positions1.secId = positions1.secId
SELECT * FROM /exampleRegion portfolio1, portfolio1.positions.values positions1, 
/exampleRegion2 portfolio2, portfolio2.positions.values positions2 WHERE portfolio1.ID = 1 
AND positions1.secId = positions1.secId
SELECT DISTINCT a, b.price FROM /exampleRegoin1 a, /exampleRegion2 b WHERE a.price = b.price
```

**Using AS**

```
SELECT * FROM /exampleRegion p, p.positions.values AS pos WHERE pos.secId != '1'
```

**Using TRUE**

```
SELECT DISTINCT * FROM /Portfolios WHERE TRUE
```

**Using IN and SET**

See also [IN and SET](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_AC12146509F141378E493078540950C7).

```
SELECT * FROM /exampleRegion portfolio WHERE portfolio.ID IN SET(1, 2)
SELECT * FROM /exampleRegion portfolio, portfolio.positions.values positions 
WHERE portfolio.Pk IN SET ('1', '2') AND positions.secId = '1'
SELECT * FROM /exampleRegion portfolio, portfolio.positions.values positions 
WHERE portfolio.Pk IN SET ('1', '2') OR positions.secId IN SET ('1', '2', '3')
SELECT * FROM /exampleRegion portfolio, portfolio.positions.values positions 
WHERE portfolio.Pk IN SET ('1', '2') OR positions.secId IN SET ('1', '2', '3') 
AND portfolio.status = 'active'
```

**Querying for Set values**

In the following query, sp is of type Set.

```
SELECT * FROM /exampleRegion WHERE sp = set('20', '21', '22')
```

If the Set (sp) only contains '20’ and '21’, then the query will evaluate to false. The query compares the two sets and looks for the presence of elements in both sets.

For other collection types like list (sp is of type List), the query can be written as follows:

```
SELECT * FROM /exampleRegion WHERE sp.containsAll(set('20', '21', '22'))
```

**Invoking Methods on Objects**

See [Method Invocations](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_D2F8D17B52B04895B672E2FCD675A676) for more information.

```
SELECT * FROM /exampleRegion p WHERE p.length > 1
SELECT DISTINCT * FROM /exampleRegion p WHERE p.positions.size >= 2
SELECT DISTINCT * FROM /exampleRegion p WHERE p.positions.isEmpty
SELECT DISTINCT * FROM /exampleRegion p WHERE p.name.startsWith('Bo')
```

**Using Query-Level Debugging**

To set debugging on the query level, add the **<trace>** keyword before the query. (If you are using an IMPORT statement, include it before the IMPORT).

```
<trace>
SELECT * from /exampleRegion, positions.values TYPE myclass
```

**Using Reserved Words in Queries**

To access any method, attribute, or named object that has the same name as a query language reserved word, enclose the name within double quotation marks.

```
SELECT * FROM /exampleRegion WHERE status = 'active' AND "type" = 'XYZ'
SELECT DISTINCT "type" FROM /exampleRegion WHERE status = 'active'
```

**Using IMPORT**

In the case where the same class name resides in two different namescopes (packages), there needs to be a means of referring to different classes of the same name. The IMPORT statement is used to establish a namescope for a class in a query.

```
IMPORT package.Position;
SELECT DISTINCT * FROM /exampleRegion, positions.values positions TYPE Position WHERE positions.mktValue >= 25.00
```

**Using TYPE**

Specifying object type helps the query engine to process the query at optimal speed. Apart from specifying the object types during configuration (using key-constraint and value-constraint), type can be explicitly specified in the query string.

```
SELECT DISTINCT * FROM /exampleRegion, positions.values positions TYPE Position WHERE positions.mktValue >= 25.00
```

**Using ELEMENT**

Using ELEMENT(expr) extracts a single element from a collection or array. This function throws a `FunctionDomainException` if the argument is not a collection or array with exactly one element.

```
ELEMENT(SELECT DISTINCT * FROM /exampleRegion WHERE id = 'XYZ-1').status = 'active'
```

**Which APIs should I use to write my queries?**

If you are querying a Java application’s local cache or querying other members, use [org.apache.geode.cache.Cache.getQueryService](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/QueryService.html).

If you are writing a Java client to server query, use [org.apache.geode.cache.client.Pool.getQueryService](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/Pool.html).

**How do I invoke an object’s method in a query?**

To use a method in a query, use the attribute name that maps to the public method you want to invoke. For example:

```
/*valid method invocation*/ 
SELECT DISTINCT * FROM /exampleRegion p WHERE p.positions.size >= 2 - maps to positions.size()
```

**Can I invoke a static method on an object in a query?**

No, you cannot invoke a static method on an object. For example, the following query is invalid.

```
/*invalid method invocation*/
SELECT DISTINCT * FROM /exampleRegion WHERE aDay = Day.Wednesday
```

To work around this limitation, write a reusable query that uses a query bind parameter to invoke the static method. Then at query run time, set the parameter to the static method invocation (`Day.Wednesday`). For example:

```
SELECT DISTINCT * FROM /exampleRegion WHERE aDay = $1
```

**How do I write a reusable query?**

Using query APIs, you can set query bind parameters that are passed values at query run time. For example:

```
// specify the  query string
 String queryString = "SELECT DISTINCT * FROM /exampleRegion p WHERE p.status = $1";

QueryService queryService = cache.getQueryService();
Query query = queryService.newQuery(queryString);

// set a query bind parameter
Object[] params = new Object[1];
params[0] = "active";

// Execute the query locally. It returns the results set.
SelectResults results = (SelectResults) query.execute(params);

// use the results of the query; this example only looks at the size
 int size = results.size();
```

If you use a query bind parameter in place of a region path in your path expression, the parameter value must reference a collection (and not a String such as the name of the region path.)

See [Using Query Bind Parameters](https://geode.apache.org/docs/guide/17/developing/query_additional/using_query_bind_parameters.html#concept_173E775FE46B47DF9D7D1E40680D34DF) for more details.

**When should I create indexes to use in my queries?**

Determine whether your query’s performance will benefit from an index. For example, in the following query, an index on pkid can speed up the query.

```
SELECT DISTINCT * FROM /exampleRegion portfolio WHERE portfolio.pkid = '123'
```

**How do I create an index?**

An index can be created programmatically using APIs or by using xml. Here are two examples:

**Sample Code**

```
QueryService qs = cache.getQueryService();
 qs.createIndex("myIndex", "status", "/exampleRegion");
 qs.createKeyIndex("myKeyIndex", "id", "exampleRegion");
```

For more information on using this API, see the [JavaDocs](https://geode.apache.org/releases/latest/javadoc/index.html).

**Sample XML**

```
<region name="portfolios">
 <region-attributes . . . >
 </region-attributes>
 <index name="myIndex">
 <functional from-clause="/exampleRegion" 
     expression="status"/>
 </index>
 <index name="myKeyIndex">
 <primary-key field="id"/>
 </index>
 <entry>
```

For more details on indexes, see [Working with Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/query_index.html).

**Can I create indexes on overflow regions?**

You can create indexes on overflow regions, but you are subject to some limitations. For example, the data contained in the index itself cannot be overflowed to disk. See [Using Indexes with Overflow Regions](https://geode.apache.org/docs/guide/17/developing/query_index/indexes_with_overflow_regions.html#concept_87BE7DB32C714EB0BF7532AF93569328) for more information.

**Can I query a partitioned region? Can I perform a join query on a partitioned region?**

You can query partitioned regions, but there are some limitations. You cannot perform join queries on partitioned regions, however you can perform equi-join queries on colocated partitioned regions by executing a function on a local data set.

For a full list of restrictions, see [Partitioned Region Query Restrictions](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_query_restrictions.html#concept_5353476380D44CC1A7F586E5AE1CE7E8).

**How can I improve the performance of a partitioned region query?**

If you know the data you need to query, you can target particular nodes in your queries (thus reducing the number of servers the query needs to access) by executing the query with the FunctionService. See [Querying a Partitioned Region on a Single Node](https://geode.apache.org/docs/guide/17/developing/query_additional/query_on_a_single_node.html#concept_30B18A6507534993BD55C2C9E0544A97) for details. If you are querying data that has been partitioned by a key or specific field, you should first create a key index and then execute the query using the FunctionService with the key or field as a filter. See [Optimizing Queries on Data Partitioned by a Key or Field Value](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_key_or_field_value.html#concept_3010014DFBC9479783B2B45982014454).

**Which query language elements are supported in Geode?**

| Supported elements |          |         |
| ------------------ | -------- | ------- |
| AND                | LIMIT    | TO_DATE |
| AS                 | LIKE     | TYPE    |
| COUNT              | NOT      | WHERE   |
| DISTINCT           | NVL      |         |
| ELEMENT            | OR       |         |
| FROM               | ORDER BY |         |
| <HINT>             | SELECT   |         |
| IMPORT             | SET      |         |
| IN                 | <TRACE>  |         |
| IS_DEFINED         | TRUE     |         |
| IS_UNDEFINED       |          |         |

For more information and examples on using each supported keyword, see [Supported Keywords](https://geode.apache.org/docs/guide/17/developing/query_additional/supported_keywords.html#reference_07214B0F8DC94659B0F2D68B67195BD8).

**How do I debug queries?**

You can debug a specific query at the query level by adding the <trace> keyword before the query string that you want to debug. Here is an example:

```
<trace> SELECT * FROM /exampleRegion
```

You can also write:

```
<TRACE> SELECT * FROM /exampleRegion
```

When the query is executed, Geode will log a message in `$GEMFIRE_DIR/system.log` with the following information:

```
[info 2011/08/29 11:24:35.472 PDT CqServer <main> tid=0x1] Query Executed in 9.619656 ms; rowCount = 99; 
indexesUsed(0) "select *  from /exampleRegion" 
```

If you want to enable debugging for all queries, you can enable query execution logging by setting a System property on the command line during start-up:

```
gfsh>start server --name=server_name -–J=-Dgemfire.Query.VERBOSE=true
```

Or you can set the property programmatically:

```
System.setProperty("gemfire.Query.VERBOSE","true");
```

**Can I use implicit attributes or methods in my query?**

If an implicit attribute or method name can only be associated with one untyped iterator, the Geode query processor will assume that it is associated with that iterator. However, if more than one untyped iterator is in scope, then the query will fail with a `TypeMismatchException`. The following query fails because the query processor does not fully type expressions:

```
select distinct value.secId from /pos , getPositions(23)
```

The following query, however, succeeds because the iterator is either explicitly named with a variable or it is typed:

```
select distinct e.value.secId from /pos , getPositions(23) e
```

**Can I instruct the query engine to use specific indexes with my queries?**

Using HINT *indexname* allows you to instruct the query engine to prefer and filter results from the specified indexes. If you provide multiple index names, the query engine will use all available indexes but prefer the specified indexes.

```
<HINT 'IDIndex'> SELECT * FROM /Portfolios p WHERE p.ID > 10 AND p.owner = 'XYZ'
<HINT 'IDIndex', 'OwnerIndex'> SELECT * FROM /Portfolios p WHERE p.ID > 10 AND p.owner = 'XYZ' AND p.value < 100
```

**How do I perform a case-insensitive search on a field in OQL?**

You can use the Java String class methods `toUpperCase` and `toLowerCase` to transform fields where you want to perform a case-insensitive search. For example:

```
SELECT entry.value FROM /exampleRegion.entries entry WHERE entry.value.toUpperCase LIKE '%BAR%'
```

or

```
SELECT * FROM /exampleRegion WHERE foo.toLowerCase LIKE '%bar%'
```



### Querying with OQL

This section provides a high-level introduction to Geode querying such as building a query string and describes query language features.

Geode provides a SQL-like querying language that allows you to access data stored in Geode regions. Since Geode regions are key-value stores where values can range from simple byte arrays to complex nested objects, Geode uses a query syntax based on OQL (Object Query Language) to query region data. OQL and SQL have many syntactical similarities, however they have significant differences. For example, while OQL does not offer all of the capabilities of SQL like aggregates, OQL does allow you to execute queries on complex object graphs, query object attributes and invoke object methods.

The syntax of a typical Geode OQL query is:

```
[IMPORT package]
SELECT [DISTINCT] projectionList
FROM collection1, [collection2, …]
[WHERE clause]
[ORDER BY order_criteria [desc]]
```

Therefore, a simple Geode OQL query resembles the following:

```
SELECT DISTINCT * FROM /exampleRegion WHERE status = ‘active’
```

An important characteristic of Geode querying to note is that by default, Geode queries on the values of a region and not on keys. To obtain keys from a region, you must use the keySet path expression on the queried region. For example, `/exampleRegion.keySet`.

For those new to the Geode querying, see also the [Geode Querying FAQ and Examples](https://geode.apache.org/docs/guide/17/getting_started/querying_quick_reference.html#reference_D5CE64F5FD6F4A808AEFB748C867189E).



#### Advantages of OQL

The following list describes some of the advantages of using an OQL-based querying language:

- You can query on any arbitrary object
- You can navigate object collections
- You can invoke methods and access the behavior of objects
- Data mapping is supported
- You are not required to declare types. Since you do not need type definitions, you can work across multiple languages
- You are not constrained by a schema



#### Writing and Executing a Query in Geode

The Geode QueryService provides methods to create the Query object. You can then use the Query object to perform query-related operations.

The QueryService instance you should use depends on whether you are querying the local cache of an application or if you want your application to query the server cache.

**Querying a Local Cache**

To query the application’s local cache or to query other members, use `org.apache.geode.cache.Cache.getQueryService`.

**Sample Code**

```
 // Identify your query string.
 String queryString = "SELECT DISTINCT * FROM /exampleRegion";

 // Get QueryService from Cache.
 QueryService queryService = cache.getQueryService();

 // Create the Query Object.
 Query query = queryService.newQuery(queryString);

 // Execute Query locally. Returns results set.
 SelectResults results = (SelectResults)query.execute();

 // Find the Size of the ResultSet.
 int size = results.size();

 // Iterate through your ResultSet.
 Portfolio p = (Portfolio)results.iterator().next(); /* Region containing Portfolio object. */
```

**Querying a Server Cache from a Client**

To perform a client to server query, use `org.apache.geode.cache.client.Pool.getQueryService`.

**Sample Code**

```
// Identify your query string.
 String queryString = "SELECT DISTINCT * FROM /exampleRegion";

 // Get QueryService from client pool.
 QueryService queryService = pool.getQueryService();

 // Create the Query Object.
 Query query = queryService.newQuery(queryString);

 // Execute Query locally. Returns results set.
 SelectResults results = (SelectResults)query.execute();

 // Find the Size of the ResultSet.
 int size = results.size();

 // Iterate through your ResultSet.
 Portfolio p = (Portfolio)results.iterator().next(); /* Region containing Portfolio object. */
```

Refer to the following JavaDocs for specific APIs:

- [Query package](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/package-summary.html)
- [QueryService](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/QueryService.html)

**Note:** You can also perform queries using the gfsh `query` command. See [query](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/query.html).



#### Building a Query String

A query string is a fully formed OQL statement that can be passed to a query engine and executed against a data set. To build a query string, you combine supported keywords, expressions, and operators to create an expression that returns the information you require.

A query string follows the rules specified by the query language and grammar. It can include:

- **Namescopes**. For example, the IMPORT statement. See [IMPORT Statement](https://geode.apache.org/docs/guide/17/developing/query_select/the_import_statement.html#concept_2E9F15B2FE9041238B54736103396BF7).
- **Path expressions**. For example, in the query `SELECT * FROM /exampleRegion`,`/exampleRegion` is a path expression. See [FROM Clause](https://geode.apache.org/docs/guide/17/developing/query_select/the_from_clause.html#the_from_clause).
- **Attribute names**. For example, in the query `SELECT DISTINCT * FROM /exampleRegion p WHERE p.position1.secId = '1'`, we access the `secId`attribute of the Position object. See [WHERE Clause](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause).
- **Method invocations**. For example, in the query `SELECT DISTINCT * FROM /exampleRegion p WHERE p.name.startsWith('Bo')`, we invoke the `startsWith` method on the Name object. See [WHERE Clause](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause).
- **Operators**. For example, comparison operators (=,<,>,<>), unary operators (NOT), logical operators (AND, OR) and so on. See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) for a complete list.
- **Literals**. For example, boolean, date, time and so on. See [Supported Literals](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals) for a complete list.
- **Query bind parameters**. For example, in the query `SELECT DISTINCT * FROM $1 p WHERE p.status = $2`, $1 and $2 are parameters that can be passed to the query during runtime. See [Using Query Bind Parameters](https://geode.apache.org/docs/guide/17/developing/query_additional/using_query_bind_parameters.html#concept_173E775FE46B47DF9D7D1E40680D34DF) for more details.
- **Preset query functions**. For example, ELEMENT(expr) and IS_DEFINED(expr). See [SELECT Statement](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E) for other available functions.
- **SELECT statements**. For example, in the example queries above `SELECT *` or `SELECT DISTINCT *`. See [SELECT Statement](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E) for other available functions.
- **Comments**. OQL permits extra characters to accompany the query string without changing the string’s definition. Form a multi-line comment by enclosing the comment body within `/*` and `*/` delimiters; OQL does not permit nested comments. A single line comment body is all the characters to the right of `--` (two hyphens) up to the end of the line.

The components listed above can all be part of the query string, but none of the components are required. At a minimum, a query string contains an expression that can be evaluated against specified data.

The following sections provide guidelines for the query language building blocks that are used when writing typical Geode queries.

- **IMPORT Statement**
- **FROM Clause**
- **WHERE Clause**
- **SELECT Statement**



##### IMPORT Statement

It is sometimes necessary for an OQL query to refer to the class of an object. In cases where the same class name resides in two different namescopes (packages), you must be able to differentiate the classes having the same name.

The **IMPORT** statement is used to establish a name for a class in a query.

```sql
IMPORT package.Position;
SELECT DISTINCT * FROM /exampleRegion, positions.values positions TYPE Position WHERE positions.mktValue >= 25.00
```



##### FROM Clause

Use the FROM clause to bring the data you need into scope for the rest of your query. The FROM clause also includes object typing and iterator variables.

The query engine resolves names and path expressions according to the name space that is currently in scope in the query.

**Path Expressions**

The initial name space for any query is composed of:

- **Regions.** In the context of a query, the name of a region is specified by its full path starting with a forward slash ( / ) and delimited by the forward slash between region names. For example, **/exampleRegion** or **/root/exampleRegion**.

- **Region querying attributes**. From a region path, you can access the Region object’s public fields and methods, referred to in querying as the region’s attributes. For example, **/exampleRegion.size**.

- Top-level region data.

   

  You can access entry keys and entry data through the region path.

  1. `/exampleRegion.keySet` returns the Set of entry keys in the region
  2. `/exampleRegion.entryset` returns the Set of Region.Entry objects
  3. `/exampleRegion.values` returns the Collection of entry values
  4. `/exampleRegion` returns the Collection of entry values

New name spaces are brought into scope based on the FROM clause in the SELECT statement.

**Examples:**

Query a region for all distinct values. Return a collection of unique entry values from the region:

```
SELECT DISTINCT * FROM /exampleRegion
```

Query the top level region data using entrySet. Return the keys and positions of Region.Entry objects whose mktValue attribute is greater than 25.00:

```
SELECT key, positions FROM /exampleRegion.entrySet, value.positions.values positions WHERE positions.mktValue >= 25.00
```

Query the region for its entry values. Return a set of unique values from Region.Entry objects that have the key equal to 1:

```
SELECT DISTINCT entry.value FROM /exampleRegion.entries entry WHERE entry.key = '1'
```

Query the region for its entry values. Return the set of all entry values in which the `ID` field is greater than 1000:

```
SELECT * FROM /exampleRegion.entries entry WHERE entry.value.ID > 1000
```

Query entry keys in the region. Return a set of entry keys in the region that have the key equal to ‘1’:

```
SELECT * FROM /exampleRegion.keySet key WHERE key = '1'
```

Query values in the region. Return a collection of entry values in the region that have the status attribute value of 'active’:

```
SELECT * FROM /exampleRegion.values portfolio WHERE portfolio.status = 'active'
```

**Aliases and Synonyms**

In query strings, you can use aliases in path expressions (region and its objects) so that you can refer to the region or objects in other places in the query.

You can also use the **AS** keyword to provide a label for joined path expressions.

**Examples:**

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.status = 'active'
SELECT * FROM /exampleRegion p, p.positions.values AS pos WHERE pos.secId != '1'
```

**Object Typing**

Specifying object type in the FROM clause helps the query engine to process the query at optimal speed. Apart from specifying the object types during configuration (using key-constraint and value-constraint), type can be explicitly specified in the query string.

**Example:**

```
SELECT DISTINCT * FROM /exampleRegion, positions.values positions TYPE Position WHERE positions.mktValue >= 25.00
```



##### WHERE Clause

Each FROM clause expression must resolve to a collection of objects. The collection is then available for iteration in the query expressions that follow in the WHERE clause.

For example:

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.status = 'active'
```

The entry value collection is iterated by the WHERE clause, comparing the status field to the string ‘active’. When a match is found, the value object of the entry is added to the return set.

In the next example query, the collection specified in the first FROM clause expression is used by the rest of the SELECT statement, including the second FROM clause expression.

```
SELECT DISTINCT * FROM /exampleRegion, positions.values p WHERE p.qty > 1000.00
```

**Implementing equals and hashCode Methods**

You must implement the `equals` and `hashCode` methods in your custom objects if you are doing ORDER BY and DISTINCT queries on the objects. The methods must conform to the properties and behavior documented in the online Java API documentation for `java.lang.Object`. Inconsistent query results may occur if these methods are absent.

If you have implemented `equals` and `hashCode` methods in your custom objects, you must provide detailed implementations of these methods so that queries execute properly against the objects. For example, assume that you have defined a custom object (CustomObject) with the following variables:

```
int ID
int otherValue
```

Let’s put two CustomObjects (we’ll call them CustomObjectA and CustomObjectB) into the cache:

CustomObjectA:

```
ID=1
otherValue=1
```

CustomObjectB:

```
ID=1
otherValue=2
```

If you have implemented the equals method to simply match on the ID field (ID == ID), queries will produce unpredictable results.

The following query:

```
SELECT * FROM /CustomObjects c 
WHERE c.ID > 1 AND c.ID < 3 
AND c.otherValue > 0 AND c.otherValue < 3
```

returns two objects, however the objects will be two of either CustomObjectA or CustomObjectB.

Alternately, the following query:

```
SELECT * FROM /CustomObjects c 
WHERE c.ID > 1 AND c.ID < 3 
AND c.otherValue > 1 AND c.otherValue < 3
```

returns either 0 results or 2 results of CustomObjectB, depending on which entry is evaluated last.

To avoid unpredictable querying behavior, implement detailed versions of the `equals` and `hashCode` methods.

If you are comparing a non-primitive field of the object in the WHERE clause, use the `equals`method instead of the `=` operator. For example instead of `nonPrimitiveObj = objToBeCompared` use `nonPrimitiveObj.equals(objToBeCompared)`.

**Querying Serialized Objects**

Objects must implement serializable if you will be querying partitioned regions or if you are performing client-server querying.

If you are using PDX serialization, you can access the values of individual fields without having to deserialize the entire object. This is accomplished by using PdxInstance, which is a wrapper around the serialized stream. The PdxInstance provides a helper method that takes field-name and returns the value without deserializing the object. While evaluating the query, the query engine will access field values by calling the getField method thus avoiding deserialization.

To use PdxInstances in querying, ensure that PDX serialization reads are enabled in your server’s cache. In gfsh, execute the following command before starting up your data members:

```
gfsh>configure pdx --read-serialized=true
```

See [configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh) for more information.

In cache.xml, set the following:

```
// Cache configuration setting PDX read behavior 
<cache>
  <pdx read-serialized="true">
  ...
  </pdx>
</cache>
```

**Attribute Visibility**

You can access any object or object attribute that is available in the current scope of a query. In querying, an object’s attribute is any identifier that can be mapped to a public field or method in the object. In the FROM specification, any object that is in scope is valid. Therefore, at the beginning of a query, all locally cached regions and their attributes are in scope.

For attribute Position.secId which is public and has getter method “getSecId()”, the query can be written as the following:

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.position1.secId = '1'
SELECT DISTINCT * FROM /exampleRegion p WHERE p.position1.SecId = '1'
SELECT DISTINCT * FROM /exampleRegion p WHERE p.position1.getSecId() = '1'
```

The query engine tries to evaluate the value using the public field value. If a public field value is not found, it makes a get call using field name (note that the first character is uppercase.)

**Joins**

If collections in the FROM clause are not related to each other, the WHERE clause can be used to join them.

The statement below returns all portfolios from the /exampleRegion and /exampleRegion2 regions that have the same status.

```
SELECT * FROM /exampleRegion portfolio1, /exampleRegion2 portfolio2 WHERE portfolio1.status = portfolio2.status
```

To create indexes for region joins you create single-region indexes for both sides of the join condition. These are used during query execution for the join condition. Partitioned regions do not support region joins. For more information on indexes, see [Working with Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/query_index.html).

**Examples:**

Query two regions. Return the ID and status for portfolios that have the same status.

```
SELECT portfolio1.ID, portfolio2.status FROM /exampleRegion portfolio1, /exampleRegion2 portfolio2 WHERE portfolio1.status = portfolio2.status
```

Query two regions, iterating over all `positions` within each portfolio. Return all 4-tuples consisting of the value from each of the two regions and the value portion of the `positions` map from both regions in which the `secId` field of positions match.

```
SELECT * FROM /exampleRegion portfolio1, portfolio1.positions.values positions1, /exampleRegion2 portfolio2, portfolio2.positions.values positions2 WHERE positions1.secId = positions2.secId
```

Same query as the previous example, with the additional constraint that matches will have a `ID`of 1.

```
SELECT * FROM /exampleRegion portfolio1, portfolio1.positions.values positions1, /exampleRegion2 portfolio2, portfolio2.positions.values positions2 WHERE portfolio1.ID = 1 AND positions1.secId = positions2.secId
```

**LIKE**

Geode offers limited support for the LIKE predicate. LIKE can be used to mean 'equals to’. If you terminate the string with a wildcard (’%’), it behaves like 'starts with’. You can also place a wildcard (either ’%’ or ’_’) at any other position in the comparison string. You can escape the wildcard characters to represent the characters themselves.

**Note:** The ’*’ wildcard is not supported in OQL LIKE predicates.

You can also use the LIKE predicate when an index is present.

**Examples:**

Query the region. Return all objects where status equals 'active’:

```
SELECT * FROM /exampleRegion p WHERE p.status LIKE 'active'
```

Query the region using a wild card for comparison. Returns all objects where status begins with 'activ’:

```
SELECT * FROM /exampleRegion p WHERE p.status LIKE 'activ%'
```

**Case Insensitive Fields**

You can use the Java String class methods `toUpperCase` and `toLowerCase` to transform fields where you want to perform a case-insensitive search. For example:

```
SELECT entry.value FROM /exampleRegion.entries entry WHERE entry.value.toUpperCase LIKE '%BAR%'
```

or

```
SELECT * FROM /exampleRegion WHERE foo.toLowerCase LIKE '%bar%'
```

**Method Invocations**

To use a method in a query, use the attribute name that maps to the public method you want to invoke.

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.positions.size >= 2 - maps to positions.size()
```

Methods declared to return void evaluate to null when invoked through the query processor.

You cannot invoke a static method. See [Enum Objects](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_59E7D64746AE495D942F2F09EF7DB9B5) for more information.

**Methods without parameters**

If the attribute name maps to a public method that takes no parameters, just include the method name in the query string as an attribute. For example, emps.isEmpty is equivalent to emps.isEmpty().

In the following example, the query invokes isEmpty on positions, and returns the set of all portfolios with no positions:

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.positions.isEmpty
```

**Methods with parameters**

To invoke methods with parameters, include the method name in the query string as an attribute and provide method arguments between parentheses.

This example passes the argument “Bo” to the public method, and returns all names that begin with “Bo”.

```
SELECT DISTINCT * FROM /exampleRegion p WHERE p.name.startsWith('Bo')
```

For overloaded methods, the query processor decides which method to call by matching the runtime argument types with the parameter types required by the method. If only one method’s signature matches the parameters provided, it is invoked. The query processor uses runtime types to match method signatures.

If more than one method can be invoked, the query processor chooses the method whose parameter types are the most specific for the given arguments. For example, if an overloaded method includes versions with the same number of arguments, but one takes a Person type as an argument and the other takes an Employee type, derived from Person, Employee is the more specific object type. If the argument passed to the method is compatible with both types, the query processor uses the method with the Employee parameter type.

The query processor uses the runtime types of the parameters and the receiver to determine the proper method to invoke. Because runtime types are used, an argument with a null value has no typing information, and so can be matched with any object type parameter. When a null argument is used, if the query processor cannot determine the proper method to invoke based on the non-null arguments, it throws an `AmbiguousNameException`.

**Methods calls with the SecurityManager enabled**

When the `SecurityManager` is enabled, Geode throws a `NotAuthorizedException` when any method not on this list of whitelisted methods is invoked:

- On a `Map`, `Collection`, or `Region` object: `keySet`, `entrySet`, `values`, `containsKey` or `get`
- On a `Region.Entry` object: `getKey` or `getValue`
- On a `Date` or `Timestamp` object: `after`, `before`, `getNanos`, or `getTime`
- On a `String` object: any method
- On any `Number` object: `intValue`, `longValue`, `shortValue`, etc.
- On any `Boolean` object: `booleanValue`
- On any object: `equals`, `compareTo`, or `toString`

To disable the authorization check, start all servers with the added system property `gemfire.QueryService.allowUntrustedMethodInvocation`. For example:

```
gfsh>start server --name=Server1 \
     --J=-Dgemfire.QueryService.allowUntrustedMethodInvocation=true
```

**Enum Objects**

To write a query based on the value of an Enum object field, you must use the `toString` method of the enum object or use a query bind parameter.

For example, the following query is NOT valid:

```
//INVALID QUERY
select distinct * from /QueryRegion0 where aDay = Day.Wednesday
```

The reason it is invalid is that the call to `Day.Wednesday` involves a static class and method invocation which is not supported.

Enum types can be queried by using toString method of the enum object or by using bind parameter. When you query using the toString method, you must already know the constraint value that you wish to query. In the following first example, the known value is 'active’.

**Examples:**

Query enum type using the toString method:

```
// eStatus is an enum with values 'active' and 'inactive'
select * from /exampleRegion p where p.eStatus.toString() = 'active'
```

Query enum type using a bind parameter. The value of the desired Enum field ( Day.Wednesday) is passed as an execution parameter:

```
select distinct * from /QueryRegion0 where aDay = $1
```

**IN and SET**

The IN expression is a boolean indicating if one expression is present inside a collection of expressions of compatible type. The determination is based on the expressions’ equals semantics.

If `e1` and `e2` are expressions, `e2` is a collection, and `e1` is an object or a literal whose type is a subtype or the same type as the elements of `e2`, then `e1 IN e2` is an expression of type boolean.

The expression returns:

- TRUE if e1 is not UNDEFINED and is contained in collection e2
- FALSE if e1 is not UNDEFINED and is not contained in collection e2 #
- UNDEFINED if e1 is UNDEFINED

For example, `2 IN SET(1, 2, 3)` is TRUE.

Another example is when the collection you are querying into is defined by a subquery. This query looks for companies that have an active portfolio on file:

```
SELECT name, address FROM /company 
  WHERE id IN (SELECT id FROM /portfolios WHERE status = 'active')
```

The interior SELECT statement returns a collection of ids for all /portfolios entries whose status is active. The exterior SELECT iterates over /company, comparing each entry’s id with this collection. For each entry, if the IN expression returns TRUE, the associated name and address are added to the outer SELECT’s collection.

**Comparing Set Values**

The following is an example of a set value type comparison where sp is of type Set:

```
SELECT * FROM /exampleRegion WHERE sp = set('20','21','22')
```

In this case, if sp only contains '20’ and '21’, then the query will evalute to false. The query compares the two sets and looks for the presence of all elements in both sets.

For other collections types like list, the query can be written as follows:

```
SELECT * FROM /exampleRegion WHERE sp.containsAll(set('20','21','22))
```

where sp is of type List.

In order to use it for Set value, the query can be written as:

```
SELECT * FROM /exampleRegion WHERE sp IN SET (set('20','21','22'),set('10',11','12'))
```

where a set value is searched in collection of set values.

One problem is that you cannot create indexes on Set or List types (collection types) that are not comparable. To workaround this, you can create an index on a custom collection type that implements Comparable.

**Double.NaN and Float.NaN Comparisons**

The comparison behavior of Double.NaN and Float.NaN within Geode queries follow the semantics of the JDK methods Float.compareTo and Double.compareTo.

In summary, the comparisons differ in the following ways from those performed by the Java language numerical comparison operators (<, <=, ==, >= >) when applied to primitive double [float] values:

- Double.NaN [Float.NaN] is considered to be equal to itself and greater than all other double [float] values (including Double.POSITIVE_INFINITY [Float.POSITIVE_INFINITY]).
- 0.0d [0.0f] is considered by this method to be greater than -0.0d [-0.0f].

Therefore, Double.NaN[Float.NaN] is considered to be larger than Double.POSITIVE_INFINITY[Float.POSITIVE_INFINITY]. Here are some example queries and what to expect.

| If p.value is NaN, the following query:                      | Evaluates to:     | Appears in the result set?     |
| ------------------------------------------------------------ | ----------------- | ------------------------------ |
| `SELECT * FROM /positions p WHERE p.value = 0`               | false             | no                             |
| `SELECT * FROM /positions p WHERE p.value > 0`               | true              | yes                            |
| `SELECT * FROM /positions p WHERE p.value >= 0`              | true              | yes                            |
| `SELECT * FROM /positions p WHERE p.value < 0`               | false             | no                             |
| `SELECT * FROM /positions p WHERE p.value <= 0`              | false             | no                             |
| **When p.value and p.value1 are both NaN, the following query:** | **Evaluates to:** | **Appears in the result set:** |
| `SELECT * FROM /positions p WHERE p.value = p.value1`        | true              | yes                            |

If you combine values when defining the following query in your code, when the query is executed the value itself is considered UNDEFINED when parsed and will not be returned in the result set.

```
String query = "SELECT * FROM /positions p WHERE p.value =" + Float.NaN
```

Executing this query, the value itself is considered UNDEFINED when parsed and will not be returned in the result set.

To retrieve NaN values without having another field already stored as NaN, you can define the following query in your code:

```
String query = "SELECT * FROM /positions p WHERE p.value > " + Float.MAX_VALUE;
```

**Arithmetic Operations**

Arithmetic operators may be used in any expression.

For example, this query selects all people with a body mass index less than 25:

```
String query = "SELECT * FROM /people p WHERE p.height * p.height/p.weight < 25";
```



##### SELECT Statement

The SELECT statement allows you to filter data from the collection of object(s) returned by a WHERE search operation. The projection list is either specified as * or as a comma delimited list of expressions.

For *, the interim results of the WHERE clause are returned from the query.

**Examples:**

Query all objects from the region using *. Returns the Collection of portfolios (The exampleRegion contains Portfolio as values).

```
SELECT * FROM /exampleRegion
```

Query secIds from positions. Returns the Collection of secIds from the positions of active portfolios:

```
SELECT secId FROM /exampleRegion, positions.values TYPE Position 
WHERE status = 'active'
```

Returns a Collection of struct<type: String, positions: map> for the active portfolios. The second field of the struct is a Map ( jav.utils.Map ) object, which contains the positions map as the value:

```
SELECT "type", positions FROM /exampleRegion 
WHERE status = 'active'
```

Returns a Collection of struct<portfolios: Portfolio, values: Position> for the active portfolios:

```
SELECT * FROM /exampleRegion, positions.values 
TYPE Position WHERE status = 'active'
```

Returns a Collection of struct<pflo: Portfolio, posn: Position> for the active portfolios:

```
SELECT * FROM /exampleRegion portfolio, positions positions 
TYPE Position WHERE portfolio.status = 'active'
```

**SELECT Statement Results**

The result of a SELECT statement is either UNDEFINED or is a Collection that implements the [SelectResults](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/SelectResults.html) interface.

The SelectResults returned from the SELECT statement is either:

1. A collection of objects, returned for these two cases:
   - When only one expression is specified by the projection list and that expression is not explicitly specified using the fieldname:expression syntax
   - When the SELECT list is * and a single collection is specified in the FROM clause
2. A collection of Structs that contains the objects

When a struct is returned, the name of each field in the struct is determined following this order of preference:

1. If a field is specified explicitly using the fieldname:expression syntax, the fieldname is used.
2. If the SELECT projection list is * and an explicit iterator expression is used in the FROM clause, the iterator variable name is used as the field name.
3. If the field is associated with a region or attribute path, the last attribute name in the path is used.
4. If names cannot be decided based on these rules, arbitrary unique names are generated by the query processor.

**DISTINCT**

Use the DISTINCT keyword if you want to limit the results set to unique rows. Note that in the current version of Geode you are no longer required to use the DISTINCT keyword in your SELECT statement.

```
SELECT DISTINCT * FROM /exampleRegion
```

**Note:** If you are using DISTINCT queries, you must implement the equals and hashCode methods for the objects that you query.

**LIMIT**

You can use the LIMIT keyword at the end of the query string to limit the number of values returned.

For example, this query returns at most 10 values:

```
SELECT * FROM /exampleRegion LIMIT 10
```

**ORDER BY**

You can order your query results in ascending or descending order by using the ORDER BY clause. You must use DISTINCT when you write ORDER BY queries.

```
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID
```

The following query sorts the results in ascending order:

```
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID asc
```

The following query sorts the results in descending order:

```
SELECT DISTINCT * FROM /exampleRegion WHERE ID < 101 ORDER BY ID desc
```

**Note:** If you are using ORDER BY queries, you must implement the equals and hashCode methods for the objects that you query.

**Preset Query Functions**

Geode provides several built-in functions for evaluating or filtering data returned from a query. They include the following:

| Function                      | Description                                                  | Example                                                      |
| ----------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ELEMENT(expr)                 | Extracts a single element from a collection or array. This function throws a `FunctionDomainException` if the argument is not a collection or array with exactly one element. | `ELEMENT(SELECT DISTINCT *   FROM /exampleRegion   WHERE id = 'XYZ-1').status = 'active'` |
| IS_DEFINED(expr)              | Returns TRUE if the expression does not evaluate to [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined). Inequality queries include undefined values in their query results. With the IS_DEFINED function, you can limit results to only those elements with defined values. | `IS_DEFINED(SELECT DISTINCT *  FROM /exampleRegion p  WHERE p.status = 'active')` |
| IS_UNDEFINED (expr)           | Returns TRUE if the expression evaluates to [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined). With the exception of inequality queries, most queries do not include undefined values in their query results. The IS_UNDEFINED function allows undefined values to be included, so you can identify elements with undefined values. | `SELECT DISTINCT *  FROM /exampleRegion p  WHERE IS_UNDEFINED(p.status)` |
| NVL(expr1, expr2)             | Returns expr2 if expr1 is null. The expressions can be query parameters (bind arguments), path expressions, or literals. |                                                              |
| TO_DATE(date_str, format_str) | Returns a Java Data class object. The arguments must be String S with date_str representing the date and format_str representing the format used by date_str. The format_str you provide is parsed using java.text.SimpleDateFormat. |                                                              |

**COUNT**

The COUNT keyword returns the number of results that match the query selection conditions specified in the WHERE clause. Using COUNT allows you to determine the size of a results set. The COUNT statement always returns an integer as its result.

The following queries are example COUNT queries that return region entries:

```
SELECT COUNT(*) FROM /exampleRegion
SELECT COUNT(*) FROM /exampleRegion WHERE ID > 0
SELECT COUNT(*) FROM /exampleRegion WHERE ID > 0 LIMIT 50
SELECT COUNT(*) FROM /exampleRegion 
WHERE ID >0 AND status LIKE 'act%'
SELECT COUNT(*) FROM /exampleRegion 
WHERE ID IN SET(1,2,3,4,5)
```

The following COUNT query returns the total number of StructTypes that match the query’s selection criteria.

```
SELECT COUNT(*) 
FROM /exampleRegion p, p.positions.values pos 
WHERE p.ID > 0 AND pos.secId 'IBM'
```

The following COUNT query uses the DISTINCT keyword and eliminates duplicates from the number of results.

```
SELECT DISTINCT COUNT(*)
FROM /exampleRegion p, p.positions.values pos
WHERE p.ID > 0 OR p.status = 'active' OR pos.secId
OR pos.secId = 'IBM'
```



##### OQL Aggregate Functions

The aggregate functions `MIN`, `MAX`, `AVG`, `AVG` over a DISTINCT expression, `SUM` over a DISTINCT expression, `COUNT`, and `COUNT` over a DISTINCT expression are supported. The `GROUP BY` extension is also supported where appropriate.

The `MIN` function returns the smallest of the selected expression. The type of the expression must evaluate to a `java.lang.Comparable`.

The `MAX` function returns the largest of the selected expression. The type of the expression must evaluate to a `java.lang.Comparable`.

The `AVG` function returns the arithmetic mean of the set formed by the selected expression. The type of the expression must evaluate to a `java.lang.Number`. For partitioned regions, each node’s buckets provide both a sum and the number of elements to the node executing the query, such that a correct average may be computed.

The `AVG` function where the DISTINCT modifier is applied to the expression returns the arithmetic mean of the set of unique (distinct) values. The type of the expression must evaluate to a`java.lang.Number`. For partitioned regions, the distinct values in a node’s buckets are returned to the node executing the query. The query node can then calculate the avarage over the values that are unique across nodes, after eliminating duplicate values that come from separate nodes.

The `SUM` function returns the sum over the set formed by the selected expression. The type of the expression must evaluate to a `java.lang.Number`. For partitioned regions, each node’s buckets compute a sum over that node, returning that sum to the node executing the query, when then sums across all nodes.

The `SUM` function where the DISTINCT modifier is applied to the expression returns the sum over the set of unique (distinct) values. The type of the expression must evaluate to a `java.lang.Number`. For partitioned regions, the distinct values in a node’s buckets are returned to the node executing the query. The query node can then calculate the sum over the values that are unique across nodes, after eliminating duplicate values that come from separate nodes.

The `COUNT` function returns the quantity of values in the set formed by the selected expression. For example, to return the quantity of employees who have a positive sales amount:

```
SELECT count(e.sales) FROM /employees e WHERE e.sales > 0.0
```

The `COUNT` function where the DISTINCT modifier is applied returns the quantity of unique (distinct) values in the set formed by the selected expression.

**GROUP BY Extension for Aggregate Functions**

`GROUP BY` is required when aggregate functions are used in combination with other selected items. It permits ordering. For example,

```
SELECT ID, MAX(e.sales) FROM /employees e GROUP BY ID
```



#### OQL Syntax and Semantics

This section covers the following querying language features:

- **Supported Character Sets**
- **Supported Keywords**
- **Case Sensitivity**
- **Comments in Query Strings**
- **Query Language Grammar**
- **Operators**
- **Reserved Words**
- **Supported Literals**



##### Supported Character Sets

Geode query language supports the full ASCII and Unicode character sets.



##### Supported Keywords

| Query Language Keyword | Description                                                  | Example                                                      |
| ---------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| AND                    | Logical operator used to create complex expressions by combining two or more expressions to produce a Boolean result. When you combine two conditional expressions using the AND operator, both conditions must evaluate to true for the entire expression to be true. | See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) |
| AS                     | Used to provide a label for a path expression so you can refer to the path by the label later. | See [Aliases and Synonyms](https://geode.apache.org/docs/guide/17/developing/query_select/the_from_clause.html#the_from_clause__section_AB1734C16DC348479C00FD6829B933AA) |
| COUNT                  | Returns the number of results that match the provided criteria. | See [COUNT](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_B2CBA00EB83F463DAF4769D7859C64C8) |
| DISTINCT               | Restricts the select statement to unique results (eliminates duplicates). | See [DISTINCT](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_972EE73A6F3E4427B6A99DB4EDF5860D) |
| ELEMENT                | Query function. Extracts a single element from a collection or array. This function throws a `FunctionDomainException` if the argument is not a collection or array with exactly one element. | See [Preset Query Functions](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_69DCAD624E9640028BC86FD67649DEB2) |
| FROM                   | You can access any object or object attribute that is available in the current scope of the query. | See [FROM Clause](https://geode.apache.org/docs/guide/17/developing/query_select/the_from_clause.html#the_from_clause) |
| <HINT>                 | Keyword that instructs the query engine to prefer certain indexes. | See [Using Query Index Hints](https://geode.apache.org/docs/guide/17/developing/query_index/query_index_hints.html) |
| IMPORT                 | Used to establish the namescope for objects.                 | See [IMPORT Statement](https://geode.apache.org/docs/guide/17/developing/query_select/the_import_statement.html#concept_2E9F15B2FE9041238B54736103396BF7) |
| IN                     | The IN expression is a Boolean indicating whether one expression is present inside a collection of expressions of a compatible type. | See [IN and SET](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_AC12146509F141378E493078540950C7) |
| IS_DEFINED             | Query function. Returns TRUE if the expression does not evaluate to [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined). Inequality queries include undefined values in their query results. With the IS_DEFINED function, you can limit results to only those elements with defined values. | See [Preset Query Functions](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_69DCAD624E9640028BC86FD67649DEB2) |
| IS_UNDEFINED           | Query function. Returns TRUE if the expression evaluates to [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined). With the exception of inequality queries, most queries do not include undefined values in their query results. The IS_UNDEFINED function allows undefined values to be included, so you can identify elements with undefined values. | See [Preset Query Functions](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_69DCAD624E9640028BC86FD67649DEB2) |
| LIMIT                  | Limits the number of returned results. If you use the limit keyword, you cannot also run operations on the query result set that perform any kind of summary activities. For example trying to run add or addAll or a SelectResult from a query with a LIMIT clause throws an exception. | See [LIMIT](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_25D7055B33EC47B19B1B70264B39212F) |
| LIKE                   | LIKE can be used to mean ‘equals to’, or if you terminate the string with a wildcard character (`%`), it behaves like 'starts with’. Note that the wildcard can only be used at the end of the comparison string. You can escape the wildcard character to represent the `%` character. You can also use the LIKE predicate if an index is present. | See [LIKE](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_D91E0B06FFF6431490CC0BFA369425AD) |
| NOT                    | The example returns the set of portfolios that have positions. Note that NOT cannot use an index. | See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) |
| NVL                    | Returns expr2 if expr1 is null. The expressions can be query parameters (bind arguments), path expressions, or literals. | See [Preset Query Functions](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_69DCAD624E9640028BC86FD67649DEB2) |
| OR                     | If an expression uses both AND and OR operators, the AND expression has higher precedence than OR. | See [Operators](https://geode.apache.org/docs/guide/17/developing/query_additional/operators.html#operators) |
| ORDER BY               | Allows you to order query results (either in ascending or descending order). | See [ORDER BY](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_D9DF0F785CA94EF8B367C3326CC12990) |
| SELECT                 | Allows you to filter data from the collection of object(s) returned by a WHERE search operation. | See [SELECT Statement](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E) |
| SET                    | Specifies a collection of values that can be compared to the returned values of query. | See [IN and SET](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_AC12146509F141378E493078540950C7) |
| <TRACE>                | Enables debugging on the following query string.             | See [Query Debugging](https://geode.apache.org/docs/guide/17/developing/query_additional/query_debugging.html#concept_2D557E24AAB24044A3DB36B3124F6748) |
| TO_DATE                | Returns a Java Data class object. The arguments must be String S with date_str representing the date and format_str representing the format used by date_str. The format_str you provide is parsed using java.text.SimpleDateFormat. | See [Preset Query Functions](https://geode.apache.org/docs/guide/17/developing/query_select/the_select_statement.html#concept_85AE7D6B1E2941ED8BD2A8310A81753E__section_69DCAD624E9640028BC86FD67649DEB2) |
| TYPE                   | Specifying object type in the FROM clause helps the query engine to process the query at optimal speed. | See [Object Typing](https://geode.apache.org/docs/guide/17/developing/query_select/the_from_clause.html#the_from_clause__section_A5B42CCB7C924949954AEC2DAAD51134) |
| WHERE                  | Resolves to a collection of objects. The collection is then available for iteration in the query expressions that follow in the WHERE clause. | See [WHERE Clause](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause) |



##### Case Sensitivity

Query language keywords such as SELECT, NULL, DATE, and <TRACE> are case-insensitive. Identifiers such as attribute names, method names, and path expressions are case-sensitive.

In terms of query string and region entry matching, if you want to perform a case-insensitive search on a particular field, you can use the Java String class `toUpperCase` and `toLowerCase`methods in your query. For example:

```
SELECT entry.value FROM /exampleRegion.entries entry WHERE entry.value.toUpperCase LIKE '%BAR%'
```

or

```
SELECT * FROM /exampleRegion WHERE foo.toLowerCase LIKE '%bar%'
```



##### Comments in Query Strings

Comment lines being with `--` (double dash). Comment blocks begin with `/*` and end with `*/`. For example:

```
SELECT * --my comment 
FROM /exampleRegion /* here is
a comment */ WHERE status = ‘active’
```



##### Query Language Grammar

**Language Grammar**

Notation used in the grammar: n
A nonterminal symbol that has to appear at some place within the grammar on the left side of a rule. All nonterminal symbols have to be derived to be terminal symbols.

**t**
A terminal symbol (shown in italic bold).

x y
x followed by y

x | y
x or y

(x | y)
x or y

[ x ]
x or empty

{ x }
A possibly empty sequence of x.

*comment*
descriptive text

Grammar list:

```
symbol ::= expression
query_program ::= [ imports semicolon ] query [semicolon]
imports ::= import { semicolon import }
import ::= IMPORT qualifiedName [ AS identifier ]
query ::= selectExpr | expr
selectExpr ::= SELECT DISTINCT projectionAttributes fromClause [ whereClause ]
projectionAttributes ::= * | projectionList
projectionList ::= projection { comma projection }
projection ::= field | expr [ AS identifier ]
field ::= identifier colon expr
fromClause ::= FROM iteratorDef { comma iteratorDef }
iteratorDef ::= expr [ [ AS ] identifier ] [ TYPE identifier ] | identifier IN expr [ TYPE identifier ]
whereClause ::= WHERE expr
expr ::= castExpr
castExpr ::= orExpr | left_paren identifier right_paren castExpr
orExpr ::= andExpr { OR andExpr }
andExpr ::= equalityExpr { AND equalityExpr }
equalityExpr ::= relationalExpr { ( = | <> | != ) relationalExpr }
relationalExpr ::= additiveExpr { ( < | <= | > | >= ) additiveExpr }
additiveExpr ::= multiplicativeExpr { (+ | -) multiplicativeExpr }
multiplicativeExpr ::= inExpr { (MOD | % | / | *) inExpr}
inExpr ::= unaryExpr { IN unaryExpr }
unaryExpr ::= [ NOT ] unaryExpr
postfixExpr ::= primaryExpr { left_bracket expr right_bracket }
       | primaryExpr { dot identifier [ argList ] }
argList ::= left_paren [ valueList ] right_paren
qualifiedName ::= identifier { dot identifier }
primaryExpr ::= functionExpr
       | identifier [ argList ]
       | undefinedExpr
       | collectionConstruction
       | queryParam
       | literal
       | ( query )
       | region_path
functionExpr ::= ELEMENT left_paren query right_paren
       | NVL left_paren query comma query right_paren
       | TO_DATE left_paren query right_paren
undefinedExpr ::= IS_UNDEFINED left_paren query right_paren
       | IS_DEFINED left_paren query right_paren
collectionConstruction ::= SET left_paren [ valueList ] right_paren
valueList ::= expr { comma expr }
queryParam ::= $ integerLiteral
region_path ::= forward_slash region_name { forward_slash region_name }
region_name ::= name_character { name_character }
identifier ::= letter { name_character }
literal ::= booleanLiteral
       | integerLiteral
       | longLiteral
       | doubleLiteral
       | floatLiteral
       | charLiteral
       | stringLiteral
       | dateLiteral
       | timeLiteral
       | timestampLiteral
       | NULL
       | UNDEFINED
booleanLiteral ::= TRUE | FALSE
integerLiteral ::= [ dash ] digit { digit }
longLiteral ::= integerLiteral L
floatLiteral ::= [ dash ] digit { digit } dot digit { digit } [ ( E | e ) [ plus | dash ] digit { digit } ] F
doubleLiteral ::= [ dash ] digit { digit } dot digit { digit } [ ( E | e ) [ plus | dash ] digit { digit } ] [ D ]
charLiteral ::= CHAR single_quote character single_quote
stringLiteral ::= single_quote { character } single_quote
dateLiteral ::= DATE single_quote integerLiteral dash integerLiteral dash integerLiteral single_quote
timeLiteral ::= TIME single_quote integerLiteral colon
       integerLiteral colon integerLiteral single_quote
timestampLiteral ::= TIMESTAMP single_quote
       integerLiteral dash integerLiteral dash integerLiteral integerLiteral colon
       integerLiteral colon
       digit { digit } [ dot digit { digit } ] single_quote
letter ::= any unicode letter
character ::= any unicode character except 0xFFFF
name_character ::= letter | digit | underscore
digit ::= any unicode digit
```

The expressions in the following are all terminal characters:

```
dot ::= .
left_paren ::= (
right_paren ::= )
left_bracket ::= [
right_bracket ::= ]
single_quote ::= ’
underscore ::= _
forward_slash ::= /
comma ::= ,
semicolon ::= ;
colon ::= :
dash ::= -
plus ::= +
```

**Language Notes**

- Query language keywords such as SELECT, NULL, and DATE are case-insensitive. Identifiers such as attribute names, method names, and path expressions are case-sensitive.

- Comment lines begin with – (double dash).

- Comment blocks begin with /* and end with */.

- String literals are delimited by single-quotes. Embedded single-quotes are doubled.

  Examples:

  ```
  'Hello' value = Hello
  'He said, ''Hello''' value = He said, 'Hello'
  ```

- Character literals begin with the CHAR keyword followed by the character in single quotation marks. The single-quotation mark character itself is represented as `CHAR ''''` (with four single quotation marks).

- In the TIMESTAMP literal, there is a maximum of nine digits after the decimal point.



##### Operators

Geode supports comparison, logical, unary, arithmetic, map, index, dot, and right arrow operators.

**Comparison Operators**

Comparison operators compare two values and return the results, either TRUE or FALSE.

The following are supported comparison operators:

| Operator | Meaning                  |
| -------- | ------------------------ |
| <        | less than                |
| <=       | less than or equal to    |
| >        | greater than             |
| >=       | greater than or equal to |
|          |                          |
| =        | equal to                 |
| !=       | not equal to             |
| <>       | not equal to             |

Regarding equality and inequality operators:

- The equality and inequality operators have lower precedence than the other comparison operators.
- The equality and inequality operators can be used with null.
- Inequality queries return results for which the search field is [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined).
- To perform equality or inequality comparisons with [UNDEFINED](https://geode.apache.org/docs/guide/17/developing/query_additional/literals.html#literals__section_undefined), use the IS_DEFINED and IS_UNDEFINED preset query functions instead of these comparison operators.

**Logical Operators**

The logical operators AND and OR allow you to create more complex expressions by combining expressions to produce a boolean result. When you combine two conditional expressions using the AND operator, both conditions must evaluate to true for the entire expression to be true. When you combine two conditional expressions using the OR operator, the expression evaluates to true if either one or both of the conditions are true. You can create complex expressions by combining multiple simple conditional expressions with AND and OR operators. When expressions use AND and OR operators, AND has higher precedence than OR.

**Unary Operators**

Unary operators operate on a single value or expression, and have lower precedence than comparison operators in expressions. Geode supports the unary operator NOT. NOT is the negation operator, which changes the value of the operand to its opposite. For example, if an expression evaluates to TRUE, NOT changes it to FALSE. The operand must be a boolean.

**Arithmetic Operators**

Arithmetic operators operate on two values or expressions. Any of the expected arithmetic exceptions may result, such as overflow or a divide by zero. `QueryInvocationTargetException` will be thrown, and `getCause()` will state `ArithmeticException`.

The following are supported arithmetic operators:

| Operator | Meaning        |
| -------- | -------------- |
| +        | addition       |
| -        | subtraction    |
| *        | multiplication |
| /        | division       |
| %        | modulus        |
| MOD      | modulus        |

**Map and Index Operators**

Map and index operators access elements in key/value collections (such as maps and regions) and ordered collections (such as arrays, lists, and `String`s). The operator is represented by a set of square brackets (`[ ]`) immediately following the name of the collection. The mapping or indexing specification is provided inside these brackets.

Array, list, and `String` elements are accessed using an index value. Indexing starts from zero for the first element, 1 for the second element, and so on. If `myList` is an array, list, or `String` and `index` is an expression that evaluates to a non-negative integer, then `myList[index]` represents the (`index + 1`)th element of `myList`. The elements of a `String` are the list of characters that make up the string.

Map and region values are accessed by key using the same syntax. The key can be any `Object`. For a `Region`, the map operator performs a non-distributed `get` in the local cache only - with no use of `netSearch`. So `myRegion[keyExpression]` is the equivalent of `myRegion.getEntry(keyExpression).getValue`.

**Dot, Right Arrow, and Forward Slash Operators**

The dot operator (`.`) separates attribute names in a path expression, and specifies the navigation through object attributes. An alternate equivalent to the dot is the right arrow, (`->`). The forward slash is used to separate region names when navigating into subregions.



##### Reserved Words

These words are reserved for the query language and may not be used as identifiers. The words with asterisk (`*`) after them are not currently used by Geode, but are reserved for future implementation.

| `abs* all and  andthen*  any*  array  as  asc  avg*  bag*  boolean  by  byte  char  collection count  date  declare*  define* desc ` | `dictionary  distinct  double  element  enum*  except*  exists*  false  first*  flatten*  float  for*  from  group*  having*  import  in  int  intersect*  interval* ` | `is_defined  is_undefined  last*  like limit list*  listtoset*  long  map  max*  min*  mod  nil  not  null  nvl  octet  or  order ` | `orelse*  query*  select  set  short  some*  string  struct*  sum*  time  timestamp  to_date  true  type  undefine*  undefined  union*  unique*  where` |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
|                                                              |                                                              |                                                              |                                                              |

To access any method, attribute, or named object that has the same name as a query language reserved word, enclose the name within double quotation marks.

Examples:

```
SELECT DISTINCT "type" FROM /portfolios WHERE status = 'active'
SELECT DISTINCT * FROM /region1 WHERE emps."select"() < 100000 
```



##### Supported Literals

Geode supports the following literal types:



**boolean**

A `boolean` value, either TRUE or FALSE

**int** and **long**

An integer literal is of type `long` if has a suffix of the ASCII letter L. Otherwise it is of type `int`.

**floating point**

A floating-point literal is of type `float` if it has a suffix of an ASCII letter `F`. Otherwise its type is `double`. Optionally, it can have a suffix of an ASCII letter `D`. A double or floating point literal can optionally include an exponent suffix of `E` or `e`, followed by a signed or unsigned number.





**string**

String literals are delimited by single quotation marks. Embedded single-quotation marks are doubled. For example, the character string `'Hello'` evaluates to the value `Hello`, while the character string `'He said, ''Hello'''` evaluates to `He said, 'Hello'`. Embedded newlines are kept as part of the string literal.

**char**

A literal is of type char if it is a string literal prefixed by the keyword `CHAR`, otherwise it is of type `string`. The `CHAR` literal for the single-quotation mark character is `CHAR` `''''` (four single quotation marks).

**date**

A `java.sql.Date` object that uses the JDBC format prefixed with the DATE keyword: `DATE yyyy-mm-dd`. In the `Date`, `yyyy` represents the year, `mm` represents the month, and `dd` represents the day. The year must be represented by four digits; a two-digit shorthand for the year is not allowed.

**time**

A `java.sql.Time` object that uses the JDBC format (based on a 24-hour clock) prefixed with the TIME keyword: `TIME hh:mm:ss`. In the `Time`, `hh` represents the hours, `mm` represents the minutes, and `ss` represents the seconds.

**timestamp**

A `java.sql.Timestamp` object that uses the JDBC format with a TIMESTAMP prefix: `TIMESTAMP yyyy-mm-dd hh:mm:ss.fffffffff` In the `Timestamp`, `yyyy-mm-dd` represents the `date`, `hh:mm:ss` represents the `time`, and `fffffffff` represents the fractional seconds (up to nine digits).

**NIL**

Equivalent alternative of `NULL`.

**NULL**

The same as `null` in Java.



**UNDEFINED**

A special literal, valid value for any data type, indicating that no value (not even NULL) has been designated for a given data item.



**The Difference Between NULL and UNDEFINED**

In OQL, as in Java, NULL is an assignable entity (an object) indicating “no value”.

In OQL, UNDEFINED is a type. There is no Java equivalent. In OQL search results, an UNDEFINED value can be returned in two cases:

- As the result of a search for a key or value that does not exist
- As the result of accessing an attribute of a null-valued attribute.

Searches for inequality return UNDEFINED values in their results.

Note that if you access an attribute that has an explicit value of NULL, then it is not UNDEFINED.

For example, if a query accesses the attribute `address.city` and `address` is NULL, the result is UNDEFINED. If the query accesses `address`, then the result is not UNDEFINED, it is NULL.

**Comparing Values With java.util.Date**

You can compare temporal literal values `DATE`, `TIME`, and `TIMESTAMP` with `java.util.Date`values. There is no literal for `java.util.Date` in the query language.

**Type Conversion**

The Geode query processor performs implicit type conversions and promotions under certain cases in order to evaluate expressions that contain different types. The query processor performs binary numeric promotion, method invocation conversion, and temporal type conversion.

**Binary Numeric Promotion**

The query processor performs binary numeric promotion on the operands of the following operators:

- Comparison operators <, <=, >, and >=
- Equality operators = and <>
- Binary numeric promotion widens the operands in a numeric expression to the widest representation used by any of the operands. In each expression, the query processor applies the following rules in the prescribed order until a conversion is made:
  1. If either operand is of type double, the other is converted to double
  2. If either operand is of type float, the other is converted to float
  3. If either operand is of type long, the other is converted to long
  4. Both operands are converted to type int char

**Method Invocation Conversion**

Method invocation conversion in the query language follows the same rules as Java method invocation conversion, except that the query language uses runtime types instead of compile time types, and handles null arguments differently than in Java. One aspect of using runtime types is that an argument with a null value has no typing information, and so can be matched with any type parameter. When a null argument is used, if the query processor cannot determine the proper method to invoke based on the non-null arguments, it throws an AmbiguousNameException

**Temporal Type Conversion**

The temporal types that the query language supports include the Java types java.util.Date , java.sql.Date , java.sql.Time , and java.sql.Timestamp , which are all treated the same and can be compared and used in indexes. When compared with each other, these types are all treated as nanosecond quantities.

**Enum Conversion**

Enums are not automatically converted. To use Enum values in query, you must use the toString method of the enum object or use a query bind parameter. See [Enum Objects](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_59E7D64746AE495D942F2F09EF7DB9B5) for more information.

**Query Evaulation of Float.NaN and Double.NaN**

Float.NaN and Double.NaN are not evaluated as primitives; instead, they are compared in the same manner used as the JDK methods Float.compareTo and Double.compareTo. See [Double.NaN and Float.NaN Comparisons](https://geode.apache.org/docs/guide/17/developing/query_select/the_where_clause.html#the_where_clause__section_E7206D045BEC4F67A8D2B793922BF213) for more information.



#### Query Language Restrictions and Unsupported Features

At a high level, Geode does not support the following querying features:

- Indexes targeted for joins across more than one region are not supported

- Static method invocations. For example, the following query is invalid:

  ```
  SELECT DISTINCT * FROM /QueryRegion0 WHERE aDay = Day.Wednesday
  ```

- You cannot create an index on fields using Set/List types (Collection types) that are not comparable. The OQL index implementation expects fields to be Comparable. To workaround this, you can create a custom Collection type that implements Comparable.

- ORDER BY is only supported with DISTINCT queries.

In addition, there are some specific limitations on partitioned region querying. See [Partitioned Region Query Restrictions](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_query_restrictions.html#concept_5353476380D44CC1A7F586E5AE1CE7E8).



### Advanced Querying

This section includes advanced querying topics such as using query indexes, using query bind parameters, querying partitioned regions and query debugging.

- **Performance Considerations**

  This topic covers considerations for improving query performance.

- **Monitoring Low Memory When Querying**

  The query monitoring feature prevents out-of-memory exceptions from occurring when you execute queries or create indexes.

- **Timeouts for Long-Running Queries**

  Configure a timeout value for long running queries, such that they do not complete, and Geode throws an exception when a query runs for longer than the configured value.

- **Using Query Bind Parameters**

  Using query bind parameters in Geode queries is similar to using prepared statements in SQL where parameters can be set during query execution. This allows you to build a query once and execute it multiple times by passing the query conditions during run time.

- **Querying Partitioned Regions**

  Geode allows you to manage and store large amounts of data across distributed nodes using partitioned regions. The basic unit of storage for a partitioned region is a bucket, which resides on a Geode node and contains all the entries that map to a single hashcode. In a typical partitioned region query, the system distributes the query to all buckets across all nodes, then merges the result sets and sends back the query results.

- **Query Debugging**

  You can debug a specific query at the query level by adding the `<trace>` keyword before the query string that you want to debug.



#### Performance Considerations

This topic covers considerations for improving query performance.

Some general performance tips:

- Improve query performance whenever possible by creating indexes. See [Tips and Guidelines on Using Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/indexing_guidelines.html#indexing_guidelines) for some scenarios for using indexes.
- Use bind parameters for frequently used queries. When you use a bind parameter, the query is compiled once. This improves the subsequent performance of the query when it is re-run. See [Using Query Bind Parameters](https://geode.apache.org/docs/guide/17/developing/query_additional/using_query_bind_parameters.html#concept_173E775FE46B47DF9D7D1E40680D34DF) for more details.
- When querying partitioned regions, execute the query using the FunctionService. This function allows you to target a particular node, which will improve performance greatly by avoiding query distribution. See [Querying a Partitioned Region on a Single Node](https://geode.apache.org/docs/guide/17/developing/query_additional/query_on_a_single_node.html#concept_30B18A6507534993BD55C2C9E0544A97) for more information.
- Use key indexes when querying data that has been partitioned by a key or field value. See [Optimizing Queries on Data Partitioned by a Key or Field Value](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_key_or_field_value.html#concept_3010014DFBC9479783B2B45982014454).
- The size of a query result set depends on the restrictiveness of the query and the size of the total data set. A partitioned region can hold much more data than other types of regions, so there is more potential for larger result sets on partitioned region queries. This could cause the member receiving the results to run out of memory if the result set is very large.



#### Monitoring Low Memory When Querying

The query monitoring feature prevents out-of-memory exceptions from occurring when you execute queries or create indexes.

You enable this feature when you set a `critical-heap-percentage` attribute for the resource-manager element in the `cache.xml` file or by using the `cache.getResourceManager().setCriticalHeapPercentage(float heapPercentage)` API. When this feature is enabled and heap memory usage exceeds the threshold due to running a query or creating an index, the resource manager throws an exception and cancels the running query or index creation.

You can explicitly disable this feature by setting the system property `gemfire.cache.DISABLE_QUERY_MONITOR_FOR_LOW_MEMORY` to true.

When system memory is low, as determined by the critical heap percentage threshold defined in the `cache.xml` file or in the getResourceManager API, queries will throw a `QueryExecutionLowMemoryException`. Any indexes that are in the process of being created will throw an `InvalidIndexException` with the message indicating the reason.

**Partitioned Region Queries and Low Memory**

Partitioned region queries are likely causes for out-of-memory exceptions. If query monitoring is enabled, partitioned region queries drop or ignore results that are being gathered by other servers if the executing server is low in memory.

Query-monitoring does not address a scenario in which a low-level collection is expanded while the partitioned region query is gathering results. For example, if a row is added and then causes a Java level collection or array to expand, it is possible to then encounter an out-of-memory exception. This scenario is rare and is only possible if the collection size itself expands before a low memory condition is met and then expands beyond the remaining available memory. As a workaround, in the event that you encounter this situation, you may be able to tune the system by additionally lowering the `critical-heap-percentage`.



#### Timeouts for Long-Running Queries

Geode can monitor and throw an exception when a query runs longer than a configured amount of time. This feature is enabled by setting the `critical-heap-percentage` attribute which detects that the JVM has too little heap memory.

The default query timeout is five hours. Set a different amount of time, in milliseconds, by specifying the system variable `gemfire.cache.MAX_QUERY_EXECUTION_TIME`. A value of -1 explicitly disables the timeout.

When enabled, a query that runs longer than the configured timeout will be cancelled such that it does not finish, and Geode throws a `QueryExecutionTimeoutException`.



#### Using Query Bind Parameters

Using query bind parameters in Geode queries is similar to using prepared statements in SQL where parameters can be set during query execution. This allows user to build a query once and execute it multiple times by passing the query conditions during run time.

Query objects are thread-safe.

The use of query bind parameters is now supported in Client-to-Server queries.

The query parameters are identified by a dollar sign, $, followed by a digit that represents the parameter’s position in the parameter array passed to the execute method. Counting begins at 1, so $1 references the first bound attribute, $2 the second attribute, and so on.

The Query interface provides an overloaded execute method that accepts parameters inside an Object array. See the [Query.execute](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/Query.html) JavaDocs for more details.

The 0th element of the Object array is used for the first query parameter, and so on. If the parameter count or parameter types do not match the query specification, the execute method throws an exception. Specifically, if you pass in the wrong number of parameters, the method call throws a `QueryParameterCountInvalidException`. If a parameter object type is not compatible with what is expected, the method call throws a `TypeMismatchException`.

In the following example, the first parameter, the integer **2**, is bound to the first element in the object array. The second parameter, **active**, is bound to the second element.

**Sample Code**

```
// specify the query string
String queryString = "SELECT DISTINCT * FROM /exampleRegion p WHERE p.id = $1 and p.status = $2";

QueryService queryService = cache.getQueryService();
Query query = queryService.newQuery(queryString);

// set query bind parameters
Object[] params = new Object[2];
params[0] = 2;
params[1] = "active";

// Execute the query locally. It returns the results set.
SelectResults results = (SelectResults) query.execute(params);

// use the results of the query; this example only looks at the size 
 int size = results.size();
```

**Using Query Bind Parameters in the Path Expression**

Additionally the query engine supports the use of query bind parameter in place of a region path. When you specify a bind parameter in the query’s FROM clause, the parameter’s referenced value must be bound to a collection.

For example, the following query can be used on any collection by passing in the collection as a query parameter value. In this query you could pass in a Region Object for $1, but not the String name of a region.

```
SELECT DISTINCT * FROM $1 p WHERE p.status = $2
```



#### Querying Partitioned Regions

Geode allows you to manage and store large amounts of data across distributed nodes using partitioned regions. The basic unit of storage for a partitioned region is a bucket, which resides on a Geode node and contains all the entries that map to a single hashcode. In a typical partitioned region query, the system distributes the query to all buckets across all nodes, then merges the result sets and sends back the query results.

The following list summarizes the querying functionality supported by Geode for partitioned regions:

- **Ability to target specific nodes in a query**. If you know that a specific bucket contains the data that you want to query, you can use a function to ensure that your query only runs the specific node that holds the data. This can greatly improve query efficiency. The ability to query data on a specific node is only available if you are using functions and if the function is executed on one single region. In order to do this, you need to use `Query.execute(RegionFunctionContext context)`. See the [Java API](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/Query.html) and [Querying a Partitioned Region on a Single Node](https://geode.apache.org/docs/guide/17/developing/query_additional/query_on_a_single_node.html#concept_30B18A6507534993BD55C2C9E0544A97) for more details.
- **Ability to optimize partitioned region query performance using key indexes**. You can improve query performance on data that is partitioned by key or a field value by creating a key index and then executing the query using use `Query.execute(RegionFunctionContext context)` with the key or field value used as filter. See the [Java API](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/Query.html) and [Optimizing Queries on Data Partitioned by a Key or Field Value](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_key_or_field_value.html#concept_3010014DFBC9479783B2B45982014454) for more details.
- **Ability to perform equi-join queries between partitioned regions and between partitioned regions and replicated regions**. Join queries between partitioned region and between partitioned regions and replicated regions are supported through the function service. In order to perform equi-join operations on partitioned regions or partitioned regions and replicated regions, the partitioned regions must be colocated, and you need to use the need to use `Query.execute(RegionFunctionContext context)`. See the [Java API](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/query/Query.html) and [Performing an Equi-Join Query on Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/join_query_partitioned_regions.html#concept_B930D276F49541F282A2CFE639F107DD) for more details.
- **Using ORDER BY on Partitioned Regions**
- **Querying a Partitioned Region on a Single Node**
- **Optimizing Queries on Data Partitioned by a Key or Field Value**
- **Performing an Equi-Join Query on Partitioned Regions**
- **Partitioned Region Query Restrictions**



##### Using ORDER BY on Partitioned Regions

To execute a query with an ORDER BY clause on a partitioned region, the fields specified in the ORDER BY clause must be part of the projection list.

When an ORDER BY clause is used with a partition region query, the query is executed separately on each region host, the local query coordinator, and all remote members. The results are all gathered by the query coordinator. The cumulative result set is built by applying ORDER BY on the gathered results. If the LIMIT clause is also used in the query, ORDER BY and LIMIT are applied on each node before each node’s results are returned to the coordinator. Then the clauses are applied to the cumulative result set to get the final result set, which is returned to the calling application.

**Example:**

```
// This query works because p.status is part of projection list
select distinct p.ID, p.status from /region p where p.ID > 5 order by p.status
// This query works providing status is part of the value indicated by *
select distinct * from /region where ID > 5 order by status 
```



##### Querying a Partitioned Region on a Single Node

To direct a query to specific partitioned region node, you can execute the query within a function. Use the following steps:

1. Implement a function which executes a query using RegionFunctionContext.

   ```
   /**
    * This function executes a query using its RegionFunctionContext
    * which provides a filter on data which should be queried.
    *
    */
   public class MyFunction extends FunctionAdapter {
   
       private final String id;
   
       @Override
       public void execute(FunctionContext context) {
   
         Cache cache = CacheFactory.getAnyInstance();
         QueryService queryService = cache.getQueryService();
   
         String qstr = (String) context.getArguments();
   
         try {
           Query query = queryService.newQuery(qstr);
   
           //If function is executed on region, context is RegionFunctionContext
           RegionFunctionContext rContext = (RegionFunctionContext)context;
   
           SelectResults results = (SelectResults) query.execute(rContext)
   
           //Send the results to function caller node.
           context.getResultSender().sendResult((ArrayList) (results).asList());
           context.getResultSender().lastResult(null);
   
         } catch (Exception e) {
           throw new FunctionException(e);
         }
       }
   
       @Override
       public boolean hasResult() {
         return true;
       }
   
       @Override
       public boolean isHA() {
         return false;
       }
   
       public MyFunction(String id) {
         super();
         this.id = id;
       }
   
       @Override
       public String getId() {
         return this.id;
       }
     }
   ```

2. Decide on the data you want to query. Based on this decision, you can use `PartitionResolver`to configure the organization of buckets to be queried in the Partitioned Region.

   For example, let’s say that you have defined the PortfolioKey class:

   ```
   public class PortfolioKey implements DataSerializable {
     private int id;
     private long startValidTime;
     private long endValidTime
     private long writtenTime
   
     public int getId() {
       return this.id;
     }
   ...
   }
   ```

   You could use the `MyPartitionResolver` to store all keys with the same ID in the same bucket. This `PartitionResolver` has to be configured at the time of Partition Region creation either declaratively using xml OR using APIs. See [Configuring Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/managing_partitioned_regions.html#configure_partitioned_regions) for more information.

   ```
   /** This resolver returns the value of the ID field in the key. With this resolver, 
    * all Portfolios using the same ID are colocated in the same bucket.
    */
   public class MyPartitionResolver implements PartitionResolver, Declarable {
   
      public Serializable getRoutingObject(EntryOperation operation) {
      return operation.getKey().getId();
   }
   ```

3. Execute the function on a client or any other node by setting the filter in the function call.

   ```
   /**
    * Execute MyFunction for query on specified keys.
    *
    */
   public class TestFunctionQuery {
   
     public static void main(String[] args) {
   
       ResultCollector rcollector = null;
       PortfolioKey portfolioKey1 = ...;
   
       //Filter data based on portfolioKey1 which is the key used in 
       //region.put(portfolioKey1, portfolio1);
       Set filter = Collections.singleton(portfolioKey1);
   
       //Query to get all positions for portfolio ID = 1
       String qStr = "SELECT positions FROM /myPartitionRegion WHERE ID = 1";
   
       try {
         Function func = new MyFunction("testFunction");
   
         Region region = CacheFactory.getAnyInstance().getRegion("myPartitionRegion");
   
         //Function will be routed to one node containing the bucket
         //for ID=1 and query will execute on that bucket.
         rcollector = FunctionService
             .onRegion(region)
             .setArguments(qStr)
             .withFilter(filter)
             .execute(func);
   
         Object result = rcollector.getResult();
   
         //Results from one or multiple nodes.
         ArrayList resultList = (ArrayList)result;
   
         List queryResults = new ArrayList();
   
         if (resultList.size()!=0) {
           for (Object obj: resultList) {
             if (obj != null) {
               queryResults.addAll((ArrayList)obj);
             }
           }
         }
         printResults(queryResults);
   
       } catch (FunctionException ex) {
           getLogger().info(ex);
       }
     }
   }
   ```



##### Optimizing Queries on Data Partitioned by a Key or Field Value

You can improve query performance on data that is partitioned by key or a field value by creating a key index and then executing the query using the `FunctionService` with the key or field value used as filter.

The following is an example how to optimize a query that will be run on data partitioned by region key value. In the following example, data is partitioned by the “orderId” field.

1. Create a key index on the orderId field. See [Creating Key Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/creating_key_indexes.html#concept_09E29507AF0D42CF81D261B030D0B7C8) for more details.

2. Execute the query using the function service with orderId provided as the filter to the function context. For example:

   ```
   /**
    * Execute MyFunction for query on data partitioned by orderId key
    *
    */
   public class TestFunctionQuery {
   
     public static void main(String[] args) {
   
       Set filter =  new HashSet();
       ResultCollector rcollector = null;
   
       //Filter data based on orderId  = '12345'
       filter.add(12345);
   
       //Query to get all orders that match ID 12345 and amount > 1000
       String qStr = "SELECT * FROM /Orders WHERE orderId = '12345' AND amount > 1000";
   
       try {
         Function func = new MyFunction("testFunction");
   
         Region region = CacheFactory.getAnyInstance().getRegion("myPartitionRegion");
   
         //Function will be routed to one node containing the bucket
         //for ID=1 and query will execute on that bucket.
         rcollector = FunctionService
             .onRegion(region)
             .setArguments(qStr)
             .withFilter(filter)
             .execute(func);
   
         Object result = rcollector.getResult();
   
         //Results from one or multiple nodes.
         ArrayList resultList = (ArrayList)result;
   
         List queryResults = new ArrayList();
   
         if (resultList.size()!=0) {
           for (Object obj: resultList) {
             if (obj != null) {
               queryResults.addAll((ArrayList)obj);
             }
           }
         }
         printResults(queryResults);
   
       } catch (FunctionException ex) {
           getLogger().info(ex);
       }
     }
   }
   ```



##### Performing an Equi-Join Query on Partitioned Regions

In order to perform equi-join operations on partitioned regions or partitioned regions and replicated regions, you need to use the `query.execute` method and supply it with a function execution context. You need to use Geode’s FunctionService executor because join operations are not yet directly supported for partitioned regions without providing a function execution context.

See [Partitioned Region Query Restrictions](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_query_restrictions.html#concept_5353476380D44CC1A7F586E5AE1CE7E8) for more information on partitioned region query limitations.

For example, let’s say your equi-join query is the following:

```
SELECT DISTINCT * FROM /QueryRegion1 r1,
/QueryRegion2 r2 WHERE r1.ID = r2.ID
```

In this example QueryRegion2 is colocated with QueryRegion1, and both regions have same type of data objects.

On the server side:

```
 Function prQueryFunction1 = new QueryFunction();
 FunctionService.registerFunction(prQueryFunction1);

 public class QueryFunction extends FunctionAdapter {
    @Override
    public void execute(FunctionContext context) {
      Cache cache = CacheFactory.getAnyInstance();
      QueryService queryService = cache.getQueryService();
      ArrayList allQueryResults = new ArrayList();
      ArrayList arguments = (ArrayList)(context.getArguments());
      String qstr = (String)arguments.get(0);
      try {
           Query query = queryService.newQuery(qstr);
           SelectResults result = (SelectResults)query
             .execute((RegionFunctionContext)context);
           ArrayList arrayResult = (ArrayList)result.asList();
           context.getResultSender().sendResult((ArrayList)result.asList());
           context.getResultSender().lastResult(null);
              } catch (Exception e) {
               // handle exception
             }
       }
} 
```

On the server side, `Query.execute()` operates on the local data of the partitioned region.

On the client side:

```
Function function = new QueryFunction();
String queryString = "SELECT DISTINCT * FROM /QueryRegion1 r1,
        /QueryRegion2 r2 WHERE r1.ID = r2.ID";
ArrayList argList = new ArrayList();
argList.add(queryString);
Object result = FunctionService.onRegion(CacheFactory.getAnyInstance()
     .getRegion("QueryRegion1" ))
     .setArguments(argList).execute(function).getResult();
ArrayList resultList = (ArrayList)result;
resultList.trimToSize();
List queryResults = null;
if (resultList.size() != 0) {
   queryResults = new ArrayList();
   for (Object obj : resultList) {
      if (obj != null ) {
      queryResults.addAll((ArrayList)obj);
         }
   }
}
```

On the client side, note that you can specify a bucket filter while invoking FunctionService.onRegion(). In this case, the query engine relies on FunctionService to direct the query to specific nodes.

**Additional Notes on Using the Query.execute and RegionFunctionContext APIs**

You can also pass multiple parameters (besides the query itself) to the query function by specifying the parameters in the client-side code (`FunctionService.onRegion(..).setArguments()`). Then you can handle the parameters inside the function on the server side using `context.getArguments`. Note that it does not matter which order you specify the parameters as long as you match the parameter handling order on the server with the order specified in the client.



##### Partitioned Region Query Restrictions

**Query Restrictions in Partitioned Regions**

Partitioned region queries function the same as non-partitioned region queries, except for the restrictions listed in this section. Partitioned region queries that do not follow these guidelines generate an `UnsupportedOperationException`.

- Join queries between partitioned region and between partitioned regions and replicated regions are supported through the function service only. Join queries partitioned regions are not supported through the client server API.

- You can run join queries on partitioned regions and on partitioned regions and replicated regions only if they are co-located. Equi-join queries are supported only on partitioned regions that are co-located and where the co-located columns are indicated in the WHERE clause of the query. In the case of multi-column partitioning, there should also be an AND clause in the WHERE specification. See [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data) for more information on partitioned region co-location.

- Equi-join queries are allowed between partitioned regions and between partitioned regions and local replicated regions as long as the local replicated region also exists on all partitioned region nodes. To perform a join query on a partitioned region and another region (partitioned or not), you need to use the `query.execute` method and supply it with a function execution context. See [Performing an Equi-Join Query on Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/join_query_partitioned_regions.html#concept_B930D276F49541F282A2CFE639F107DD) for an example.

- The query must be just a SELECT expression (as opposed to arbitrary OQL expressions), preceded by zero or more IMPORT statements. For example, this query is not allowed because it is not just a SELECT expression:

  ```
  // NOT VALID for partitioned regions
  (SELECT DISTINCT *FROM /prRgn WHERE attribute > 10).size
  ```

  This query is allowed:

  ```
  // VALID for partitioned regions
  SELECT DISTINCT *FROM /prRgn WHERE attribute > 10
  ```

- The SELECT expression itself can be arbitrarily complex, including nested SELECT expressions, as long as only one partitioned region is referenced.

- The partitioned region reference can only be in the first FROM clause iterator. Additional FROM clause iterators are allowed if they do not reference any regions (such as drilling down into the values in the partitioned region).

- The first FROM clause iterator must contain only one reference to the partitioned region (the reference can be a parameter, such as $1).

- The first FROM clause iterator cannot contain a subquery, but subqueries are allowed in additional FROM clause iterators.

- You can use ORDER BY on partitioned region queries, but the fields that are specified in the ORDER BY clause must be part of the projection list.

- If a partition region (or a bucket) being queried has been destroyed, the query is reattempted on the new primary for the destroyed bucket (if it exists). After certain number of attempts, a QueryException is thrown if all buckets (calculated at the startup of the query) cannot be queried.



#### Query Debugging

You can debug a specific query at the query level by adding the `<trace>` keyword before the query string that you want to debug.

Here is an example:

```
<trace> select * from /exampleRegion
```

You can also write:

```
<TRACE> select * from /exampleRegion
```

When the query is executed, Geode will log a message in `$GEMFIRE_DIR/system.log` with the following information:

```
[info 2011/08/29 11:24:35.472 PDT CqServer <main> tid=0x1] Query Executed in 9.619656 ms; rowCount = 99; indexesUsed(0) "select *  from /exampleRegion" 
```

If you want to enable debugging for all queries, you can enable query execution logging by setting a System property on the command line during start-up:

```
gfsh>start server --name=server_name -–J=-Dgemfire.Query.VERBOSE=true
```

Or you can set the property programmatically:

```
System.setProperty("gemfire.Query.VERBOSE","true");
```

As an example, let us say you have an EmployeeRegion that that contains Employee objects as values and the objects have public fields in them like ID and status.

```
Employee.java
Class Employee {
 public int ID;
 public String status;
 - - - - - -
 - - - - - -
}
```

In addition, you have created the following indexes for the region:

```
<index name="sampleIndex-1">
<functional from-clause="/test " expression="ID"/>
</index>
<index name="sampleIndex-2">
<functional from-clause="/test " expression="status"/>
</index>
```

After you have set `gemfire.Query.VERBOSE` to “true”, you could see the following debug messages in the logs after running queries on the EmployeeRegion or its indexes:

- If indexes are not used in the query execution, you would see a debug message like this:

  ```
  [info 2011/08/29 11:24:35.472 PDT CqServer <main> tid=0x1] Query Executed in 9.619656 ms; rowCount = 99; indexesUsed(0) "select * from /test k where ID > 0 and status='active'"
  ```

- When single index is used in query execution, you might see a debug message like this:

  ```
  [info 2011/08/29 11:24:35.472 PDT CqServer <main> tid=0x1] Query Executed in 101.43499 ms; rowCount = 199; indexesUsed(1):sampleIndex-1(Results: 199) "select count *   from /test k where ID > 0"
  ```

- When multiple indexes are used by a query, you might see a debug message like this:

  ```
  [info 2011/08/29 11:24:35.472 PDT CqServer <main> tid=0x1] Query Executed in 79.43847 ms; rowCount = 199; indexesUsed(2):sampleIndex-2(Results: 100),sampleIndex-1(Results: 199) "select * from /test k where ID > 0 OR status='active'"
  ```

In above log messages, the following information is provided:

- “rowCount” represents ResultSet size for the query.
- “indexesUsed(\n) ” shows n indexes were used for finding the results of the query.
- Each index name and its corresponding results are reported respectively.
- The log can be identified with the original query string itself appended in the end.



### Working with Indexes

The Geode query engine supports indexing. An index can provide significant performance gains for query execution.

A query run without the aid of an index iterates through every object in the collection. If an index is available that matches part or all of the query specification, the query iterates only over the indexed set, and query processing time can be reduced.

- **Tips and Guidelines on Using Indexes**

  Optimizing your queries with indexes requires a cycle of careful planning, testing, and tuning. Poorly-defined indexes can degrade the performance of your queries instead of improving it. This section gives guidelines for index usage in the query service.

- **Creating, Listing and Removing Indexes**

  The Geode `QueryService` API provides methods to create, list and remove the index. You can also use `gfsh` command-line interface to create, list and remove indexes, and use cache.xml to create an index.

- **Creating Key Indexes**

  Creating a key index is a good way to improve query performance when data is partitioned using a key or a field value. You can create key indexes by using the `createKeyIndex` method of the QueryService or by defining the index in `cache.xml`. Creating a key index makes the query service aware of the relationship between the values in the region and the keys in the region.

- **Creating Hash Indexes**

  **Hash indexes are deprecated.** Geode supports the creation of hash indexes for the purposes of performing equality-based queries.

- **Creating Indexes on Map Fields (“Map Indexes”)**

  To assist with the quick lookup of multiple values in a Map (or HashMap) type field, you can create an index (sometimes referred to as a “map index”) on specific (or all) keys in that field.

- **Creating Multiple Indexes at Once**

  In order to speed and promote efficiency when creating indexes, you can define multiple indexes and then create them all at once.

- **Maintaining Indexes (Synchronously or Asynchronously) and Index Storage**

  Indexes are automatically kept current with the region data they reference. The region attribute `IndexMaintenanceSynchronous` specifies whether the region indexes are updated synchronously when a region is modified or asynchronously in a background thread.

- **Using Query Index Hints**

  You can use the hint keyword to allow Geode’s query engine to prefer certain indexes.

- **Using Indexes on Single Region Queries**

  Queries with one comparison operation may be improved with either a key or range index, depending on whether the attribute being compared is also the primary key.

- **Using Indexes with Equi-Join Queries**

  Equi-join queries are queries in which two regions are joined through an equality condition in the WHERE clause.

- **Using Indexes with Overflow Regions**

  You can use indexes when querying on overflow regions; however, there are caveats.

- **Using Indexes on Equi-Join Queries using Multiple Regions**

  To query across multiple regions, identify all equi-join conditions. Then, create as few indexes for the equi-join conditions as you can while still joining all regions.

- **Index Samples**

  This topic provides code samples for creating query indexes.



#### Tips and Guidelines on Using Indexes

Optimizing your queries with indexes requires a cycle of careful planning, testing, and tuning. Poorly-defined indexes can degrade the performance of your queries instead of improving it. This section gives guidelines for index usage in the query service.

When creating indexes, keep in mind the following:

- Indexes incur maintenance costs as they must be updated when the indexed data changes. An index that requires many updates and is not used very often may require more system resources than using no index at all.
- Indexes consume memory.
- Indexes have limited support on overflow regions. See [Using Indexes with Overflow Regions](https://geode.apache.org/docs/guide/17/developing/query_index/indexes_with_overflow_regions.html#concept_87BE7DB32C714EB0BF7532AF93569328) for details.
- If you are creating multiple indexes on the same region, first define your indexes and then create the indexes all at once to avoid iterating over the region multiple times. See [Creating Multiple Indexes at Once](https://geode.apache.org/docs/guide/17/developing/query_index/create_multiple_indexes.html) for details.

**Tips for Writing Queries that Use Indexes**

As with query processors that run against relational databases, the way a query is written can greatly affect execution performance. Among other things, whether indexes are used depends on how each query is stated. These are some of the things to consider when optimizing your Geode queries for performance:

- In general an index will improve query performance if the FROM clauses of the query and index match exactly.
- The query evaluation engine does not have a sophisticated cost-based optimizer. It has a simple optimizer which selects best index (one) or multiple indexes based on the index size and the operator that is being evaluated.
- For AND operators, you may get better results if the conditions that use indexes and conditions that are more selective come before other conditions in the query.
- Indexes are not used in expressions that contain NOT, so in a WHERE clause of a query, `qty >= 10` could have an index on `qty` applied for efficiency. However, `NOT(qty < 10)` could not have the same index applied.
- Whenever possible, provide a hint to allow the query engine to prefer a specific index. See [Using Query Index Hints](https://geode.apache.org/docs/guide/17/developing/query_index/query_index_hints.html)



#### Creating, Listing and Removing Indexes

The Geode `QueryService` API provides methods to create, list and remove the index. You can also use `gfsh` command-line interface to create, list and remove indexes, and use cache.xml to create an index.

**Creating Indexes**

Indexes can be created programmatically, by using the `gfsh` command line interface or by using cache.xml.

To create an index, use one of the following `QueryService` methods:

- `createIndex`. Creates the default type of index, a range index. Use this type of index if you will be writing queries that will be doing any kind of comparison operation besides an equality comparison.
- `createKeyIndex`. Creates a key index. See [Creating Key Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/creating_key_indexes.html#concept_09E29507AF0D42CF81D261B030D0B7C8) for more information.
- **Deprecated.** `createHashIndex`. Creates a hash index. See [Creating Hash Indexes](https://geode.apache.org/docs/guide/17/developing/query_index/creating_hash_indexes.html#concept_5C7614F71F394C62ACA1BDC5684A7AC4) for more information.
- `createDefinedIndexes`. Creates multiple indexes that were previously defined using `defineIndex`. See [Creating Multiple Indexes at Once](https://geode.apache.org/docs/guide/17/developing/query_index/create_multiple_indexes.html) for more information.

The following sections provide examples of index creation:

**Using gfsh:**

```
gfsh> create index --name=myIndex --expression=status --region=/exampleRegion
gfsh> create index --name=myKeyIndex --type=key --expression=id --region=/exampleRegion
```

See [Index Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_688C66526B4649AFA51C0F72F34FA45E) for more examples.

**Using Java API:**

```
QueryService qs = cache.getQueryService();
 qs.createIndex("myIndex", "status", "/exampleRegion");
 qs.createKeyIndex("myKeyIndex", "id", "/exampleRegion");
```

**Using cache.xml:**

```
<region name=exampleRegion>
 <region-attributes . . . >
 </region-attributes>
 <index name="myIndex" from-clause="/exampleRegion" expression="status"/>
 <index name="myKeyIndex" from-clause="/exampleRegion" expression="id" key-index="true"/>
 ...
</region>
```

**Note:** If you do not specify the type of index in cache.xml, the type defaults to “range”.

**Listing Indexes**

To retrieve a list of indexes from the cache or region, use the `QueryService.getIndexes` method or the `gfsh` command line interface.

**Using gfsh:**

```
gfsh> list indexes
gfsh> list indexes --with-stats
```

**Using Java API:**

```
QueryService qs = cache.getQueryService();
 qs.getIndexes(); //returns a collection of all indexes in the cache
 qs.getIndexes(exampleRegion); //returns a collection of all indexes in exampleRegion
 qs.getIndexes(exampleRegion, myKeyIndex); //returns the index named myKeyIndex from the exampleRegion
```

**Removing Indexes**

To remove an index or all indexes from the cache or region, use the `QueryService.removeIndexes`method or the `gfsh` command line interface.

**Using gfsh:**

```
gfsh> destroy index
gfsh> destroy index --name=myIndex
gfsh> destroy index --region=/exampleRegion
```

**Using Java API:**

```
QueryService qs = cache.getQueryService();
 qs.removeIndexes(); //removes all indexes from the cache
 qs.removeIndexes(myKeyIndex); //removes the index named myKeyIndex
 qs.removeIndexes(exampleRegion); //removes all indexes from the exampleRegion
```



#### Creating Key Indexes

Creating a key index is a good way to improve query performance when data is partitioned using a key or a field value. You can create key indexes by using the `createKeyIndex` method of the QueryService or by defining the index in `cache.xml`. Creating a key index makes the query service aware of the relationship between the values in the region and the keys in the region.

The FROM clause for a primary key index must be just a region path. The indexed expression is an expression that, when applied to an entry value, produces the key. For example, if a region has Portfolios as the values and the keys are the id field of the Portfolios region, the indexed expression is id.

You can then use the FunctionService (using the partitioned key as a filter passed to the function and as part of the query equality condition) to execute the query against the indexed data. See [Optimizing Queries on Data Partitioned by a Key or Field Value](https://geode.apache.org/docs/guide/17/developing/query_additional/partitioned_region_key_or_field_value.html#concept_3010014DFBC9479783B2B45982014454) for more details.

There are two issues to note with key indexes:

- The key index is not sorted. Without sorting, you can only do equality tests. Other comparisons are not possible. To obtain a sorted index on your primary keys, create a functional index on the attribute used as the primary key.
- The query service is not automatically aware of the relationship between the region values and keys. For this, you must create the key index.

**Note:** Using a key-index with an explicit type=‘range’ in the cache.xml will lead to an exception. Key indexes will not be used in 'range’ queries.

**Examples of Creating a Key Index**

**Using Java API:**

```
QueryService qs = cache.getQueryService();
 qs.createKeyIndex("myKeyIndex", "id", "/exampleRegion");
```

**Using gfsh:**

```
gfsh> create index --name=myKeyIndex --expression=id --region=/exampleRegion
```

**Using cache.xml:**

```
<region name=exampleRegion>
 <region-attributes . . . >
 </region-attributes>
 <index name="myKeyIndex" from-clause="/exampleRegion" expression="id" key-index="true"/>
 ...
</region>
```

**Note:** If you do not specify the type of index when defining indexes using cache.xml, the type defaults to “range”.



#### Creating Hash Indexes

**Hash indexes are deprecated.** Geode supports the creation of hash indexes for the purpose of performing equality-based queries.

**Hash Index Performance**

The performance of put operations and recovery time when using a hash index will be worse than a range index. Queries are expected to be slower due to the implementation of the hash index and the cost of recalculating the key on request. A hash index can improve the memory usage of the index. So, the trade-off of the hash index space savings must be weighed against the performance penalty it imposes. If memory usage is not a concern, a range index is recommended.

Consider the memory usage when an index contains string fields. Copies of the strings are included in the index. With hash indexes, indexed expressions are canonicalized and stored in the index as pointers to the objects residing in the region, thereby using less memory. Tests achieved as high as a 30% reduction in memory footprint, but the savings depend on the keys and data being used.

**Performance Considerations**

**Limitations**

The following limitations must be considered when creating hash indexes:

- You can only use hash indexes with equals and not equals queries.
- Hash index maintenance will be slower than the other indexes due to synchronized add methods.
- Hash indexes cannot be maintained asynchronously. If you attempt to create a hash index on a region with asynchronous set as the maintenance mode, an exception will be thrown.
- You cannot use hash indexes for queries with multiple iterators or nested collections.
- Using a hash index will degrade put operation performance and recovery time substantially. If memory is not a concern, use a range index instead of a hash index.

**Examples of Creating a Hash Index**

**Hash indexes are deprecated.**

**Using the Java API:**

```
QueryService qs = cache.getQueryService();
 qs.createHashIndex("myHashIndex", "mktValue", "/exampleRegion");
```

**Using gfsh:**

```
gfsh> create index --name=myHashIndex --expression=mktValue --region=/exampleRegion
  --type=hash
```

**Using cache.xml:**

```
<region name=exampleRegion>
 <region-attributes . . . >
 </region-attributes>
 <index name="myHashIndex" from-clause="/exampleRegion p" expression="p.mktValue" type="hash"/>
 ...
</region>
```



#### Creating Indexes on Map Fields ("Map Indexes")

To assist with the quick lookup of multiple values in a Map (or HashMap) type field, you can create an index (sometimes referred to as a “map index”) on specific (or all) keys in that field.

For example, you could create a map index to support the following query:

```
SELECT * FROM /users u WHERE u.name['first'] = 'John' OR u.name['last'] = 'Smith'
```

The map index extends regular range indexes created on single key by maintaining indexes for other specified keys, or for all keys if `*` is used. The underlying structure of the map index can be thought of as a wrapper around all these indexes.

The following Java code samples provide examples of how to create a map index:

```
QueryService qs = cache.getQueryService();

//This will create indexes for for keys 'PVTL' and 'VMW'
qs.createIndex("indexName", "p.positions['PVTL', 'VMW']", "/portfolio p");
QueryService qs = cache.getQueryService();

//This will create indexes for all keys
qs.createIndex("indexName", "p.positions[*]", "/portfolio p");
```

In gfsh, the equivalents are:

```
gfsh>create index --name="IndexName" --expression="p.positions['PVTL', 'VMW']" --region="/portfolio p"

gfsh>create index --name="IndexName" --expression="p.positions[*]" --region="/portfolio p"
```

In order to create or query a map index, you must use the bracket notation to list the map field keys you wish to index or query. For example: `[*]`, `['keyX1','keyX2’]`. Note that using `p.pos.get('keyX1')` will not create or query the map index.

**Note:** You can still query against Map or HashMap fields without querying against a map index. For example, you can always create a regular range query on a single key in any Map or HashMap field. However, note that subsequent query lookups will be limited to a single key.



#### Creating Multiple Indexes at Once

In order to speed and promote efficiency when creating indexes, you can define multiple indexes and then create them all at once.

Defining multiple indexes before creating them speeds up the index creation process by iterating over region entries only once.

You can define multiple indexes of different types at once by specifying the `--type` parameter at definition time.

To define multiple indexes, you can use gfsh or the Java API:

**gfsh example:**

```
gfsh> define index --name=myIndex1 --expression=exp1 --region=/exampleRegion 

gfsh> define index --name=myIndex2 --expression="c.exp2" --region="/exampleRegion e, e.collection1 c" 

gfsh> create defined indexes
```

If index creation fails, you may receive an error message in gfsh similar to the following:

```
gfsh>create defined indexes
Exception : org.apache.geode.cache.query.RegionNotFoundException , 
Message : Region ' /r3' not found: from  /r3Occurred on following members
1. india(s1:17866)<v1>:27809
```

**Java API example:**

```
 Cache cache = new CacheFactory().create();
    QueryService queryService = cache.getQueryService();
    queryService.defineIndex("name1", "indexExpr1", "regionPath1");
    queryService.defineIndex("name2", "indexExpr2", "regionPath2");
    queryService.defineKeyIndex("name4", "indexExpr4", "regionPath2");
    List<Index> indexes = queryService.createDefinedIndexes();
```

If one or more index population fails, Geode collect the Exceptions and continues to populate the rest of the indexes. The collected `Exceptions` are stored in a Map of index names and exceptions that can be accessed through `MultiIndexCreationException`.

Index definitions are stored locally on the `gfsh` client. If you want to create a new set of indexes or if one or more of the index creations fail, you might want to clear the definitions stored by using `clear defined indexes`command. The defined indexes can be cleared by using the Java API:

```
queryService.clearDefinedIndexes();
```

or gfsh:

```
gfsh> clear defined indexes
```



#### Maintaining Indexes (Synchronously or Asynchronously) and Index Storage

Indexes are automatically kept current with the region data they reference. The region attribute `IndexMaintenanceSynchronous` specifies whether the region indexes are updated synchronously when a region is modified or asynchronously in a background thread.

**Index Maintenance Behavior**

Asynchronous index maintenance batches up multiple updates to the same region key. The default mode is synchronous, since this provides the greatest consistency with region data.

See [RegionFactory.setIndexMaintenanceSynchronous](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionFactory.html).

This declarative index creation sets the maintenance mode to asynchronous:

```
<region-attributes index-update-type="asynchronous"> 
</region-attributes>
```

Internal Index Structure and Storage

Indexes are stored either as compact or non-compact data structures based on the indexed expression (even if the index key type is the same.) For example, consider the following Passenger object:

```
Passenger {
   String name,
   Date travelDate,
   int age,
   Flight flt,
}
Flight {
   int flightId,
   String origin,
   String dest,
}
```

An index on the Passenger name field will have different memory space requirements in the cache than the Flight origin field even though they are both String field types. The internal data structure selected by Geode for index storage will depend on the field’s level in the object. In this example, name is a top-level field and an index on name can be stored as a compact index. Since origin is a second-level field, any index that uses origin as the indexed expression will be stored as a non-compact index.

**Compact Index**

A compact index has simple data structures to minimize its footprint, at the expense of doing extra work at index maintenance. This index does not support the storage of projection attributes.

Currently compact indexes are only selected only supports the creation of an index on a region path. In addition, the following conditions must be met:

- Index maintenance is synchronous.
- The indexed expression is a path expression.
- The FROM clause has only one iterator. This implies that there is only one value in the index for each region entry and it is directly on the region values (not supported with keys, entries).

**Non-Compact Index**

Used whenever a compact index cannot be used.



#### Using Query Index Hints

You can use the hint keyword to allow Geode’s query engine to prefer certain indexes.

In cases where one index is hinted in a query, the query engine filters off the hinted index (if possible) and then iterates and filters from the resulting values.

**Example:**

```
<HINT 'IDIndex'> SELECT * FROM /Portfolios p WHERE p.ID > 10 AND p.owner = 'XYZ'
```

If multiple indexes are added as hints, then the query engine will try to use as many indexes as possible while giving a preference for the hinted indexes.

**Example:**

```
<HINT 'IDIndex', 'OwnerIndex'> SELECT * FROM /Portfolios p WHERE p.ID > 10 AND p.owner = 'XYZ' AND p.value < 100
```



#### Using Indexes on Single Region Queries

Queries with one comparison operation may be improved with either a key or range index, depending on whether the attribute being compared is also the primary key.

If pkid is the key in the /exampleRegion region, creating a key index on pkid is the best choice as a key index does not have maintenance overhead. If pkid is not the key, a range index on pkid should improve performance.

```
SELECT DISTINCT * FROM /exampleRegion portfolio WHERE portfolio.pkid = '123'
```

With multiple comparison operations, you can create a range index on one or more of the attributes. Try the following:

1. Create a single index on the condition you expect to have the smallest result set size. Check performance with this index.
2. Keeping the first index, add an index on a second condition. Adding the second index may degrade performance. If it does, remove it and keep only the first index. The order of the two comparisons in the query can also impact performance. Generally speaking, in OQL queries, as in SQL queries, you should order your comparisons so the earlier ones give you the fewest results on which to run subsequent comparisons.

For this query, you would try a range index on name, age, or on both:

```
SELECT DISTINCT * FROM /exampleRegion portfolio WHERE portfolio.status = 'active' and portfolio.ID > 45
```

For queries with nested levels, you may get better performance by drilling into the lower levels in the index as well as in the query.

This query drills down one level:

```
SELECT DISTINCT * FROM /exampleRegion portfolio, portfolio.positions.values positions where positions.secId = 'AOL' and positions.MktValue > 1
```



#### Using Indexes with Equi-Join Queries

Equi-join queries are queries in which two regions are joined through an equality condition in the WHERE clause.

To use an index with an equi-join query:

1. Create an index for each side of the equi-join condition. The query engine can quickly evaluate the query’s equi-join condition by iterating over the keys of the left-side and right-side indexes for an equality match.

   **Note:** Equi-join queries require regular indexes. Key indexes are not applied to equi-join queries.

   For this query:

   ```
   SELECT DISTINCT inv.name, ord.orderID, ord.status 
   FROM /investors inv, /orders ord 
   WHERE inv.investorID = ord.investorID 
   ```

   Create two indexes:

   | FROM clause    | Indexed expression |
   | -------------- | ------------------ |
   | /investors inv | inv.investorID     |
   | /orders ord    | ord.investorID     |

2. If there are additional, single-region queries in a query with an equi-join condition, create additional indexes for the single-region conditions only if you are able to create at least one such index for each region in the query. Any indexing on a subset of the regions in the query will degrade performance.

   For this example query:

   ```
   SELECT DISTINCT *
   FROM /investors inv, /securities sc, inv.heldSecurities inv_hs
       WHERE sc.status = "active"
       AND inv.name = "xyz"
       AND inv.age > 75
       AND inv_hs.secName = sc.secName
   ```

   Create the indexes for the equi-join condition:

   | FROM clause                               | Indexed expression |
   | ----------------------------------------- | ------------------ |
   | /investors inv, inv.heldSecurities inv_hs | inv_hs.secName     |
   | /securities sc                            | sc.secName         |

   Then, if you create any more indexes, create one on `sc.status` and one on `inv.age` or `inv.name` or both.



#### Using Indexes with Overflow Regions

You can use indexes when querying on overflow regions; however, there are caveats.

The following are caveats for querying overflow regions:

- You must use synchronous index maintenance for the region. This is the default maintenance setting.
- The index FROM clause must specify only one iterator, and it must refer to the keys or entry values. The index cannot refer to the region’s entrySet.
- The index data itself is not stored on (overflowed to) disk .

**Examples:**

The following example index creation calls DO NOT work for overflow regions.

```
// This index will not work on an overflow region because there are two iterators in the FROM clause.
createIndex("secIdIndex", "b.secId","/portfolios pf, pf.positions.values b");

// This index will not work on an overflow region because the FROM clause specifies the entrySet
createIndex("indx1", "entries.value.getID", "/exampleRegion.entrySet() entries");
```

The following example indexes will work for overflow regions.

```
createIndex("pkidIndex", "p.pkid", "/Portfolios p");

createIndex("indx1", "ks.toString", "/portfolio.keySet() ks");
```

The same working examples in gfsh:

```
gfsh> create index -name="pkidIndex" --expression="p.pkid" --region="/Portfolios p"

gfsh> create index -name="indx1" --expression="ks.toString" --region="/portfolio.keySet() ks"
```



#### Using Indexes on Equi-Join Queries using Multiple Regions

To query across multiple regions, identify all equi-join conditions. Then, create as few indexes for the equi-join conditions as you can while still joining all regions.

If there are equi-join conditions that redundantly join two regions (in order to more finely filter the data, for example), then creating redundant indexes for these joins will negatively impact performance. Create indexes only on one equi-join condition for each region pair.

In this example query:

```
SELECT DISTINCT * 
FROM /investors inv, /securities sc, /orders or, 
inv.ordersPlaced inv_op, or.securities or_sec 
    WHERE inv_op.orderID = or.orderID 
    AND or_sec.secID = sc.secID
```

All conditions are required to join the regions, so you would create four indexes, two for each equi-join condition:

| FROM clause                             | Indexed expression |
| --------------------------------------- | ------------------ |
| /investors inv, inv.ordersPlaced inv_op | inv_op.orderID     |
| /orders or, or.securities or_sec        | or.orderID         |

| FROM clause                      | Indexed expression |
| -------------------------------- | ------------------ |
| /orders or, or.securities or_sec | or_sec.secID       |
| /securities sc                   | sc.secID           |

Adding another condition to the example:

```
SELECT DISTINCT * 
FROM /investors inv, /securities sc, /orders or, 
inv.ordersPlaced inv_op, or.securities or_sec, sc.investors sc_invs 
    WHERE inv_op.orderID = or.orderID 
    AND or_sec.secID = sc.secID
                AND inv.investorID = sc_invs.investorID
```

You would still only want to use four indexes in all, as that’s all you need to join all of the regions. You would need to choose the most performant two of the following three index pairs:

| FROM clause                             | Indexed expression |
| --------------------------------------- | ------------------ |
| /investors inv, inv.ordersPlaced inv_op | inv_op.orderID     |
| /orders or, or.securities or_sec        | or.orderID         |

| FROM clause                          | Indexed expression |
| ------------------------------------ | ------------------ |
| /orders or, or.securities or_sec     | or_sec.secID       |
| /securities sc, sc.investors sc_invs | sc.secID           |

| FROM clause                             | Indexed expression |
| --------------------------------------- | ------------------ |
| /investors inv, inv.ordersPlaced inv_op | inv.investorID     |
| /securities sc, sc.investors sc_invs    | sc_invs.investorID |

The most performant set is that which narrows the data to the smallest result set possible. Examine your data and experiment with the three index pairs to see which provides the best performance.



#### Index Samples

This topic provides code samples for creating query indexes.

```
 // Key index samples. The field doesn't have to be present.
createKeyIndex("pkidIndex","p.pkid1","/root/exampleRegion p");

createKeyIndex("Index4","ID","/portfolios");

// Simple index
createIndex("pkidIndex","p.pkid","/root/exampleRegion p");
createIndex("i", "p.status", "/exampleRegion p")
createIndex("i", "p.ID", "/exampleRegion p")
createIndex("i", "p.position1.secId", "/exampleRegion p"

// On Set type
 createIndex("setIndex","s","/root/exampleRegion p, p.sp s");

// Positions is a map
createIndex("secIdIndex","b.secId","/portfolios pf, pf.positions.values b");

//...
createIndex("i", "pf.collectionHolderMap[(pf.Id).toString()].arr[pf.ID]", "/exampleRegion pf")
createIndex("i", "pf.ID", "/exampleRegion pf", "pf.positions.values pos")
createIndex("i", "pos.secId", "/exampleRegion pf", "pf.positions.values pos")
createIndex("i", "e.value.getID()", "/exampleRegion.entrySet e")
createIndex("i", "e.value.ID", "/exampleRegion.entrySet e")

//...
createIndex("i", "entries.value.getID", "/exampleRegion.entrySet() entries")
createIndex("i", "ks.toString", "/exampleRegion.getKeys() ks")
createIndex("i", "key.status", "/exampleRegion.keys key")
createIndex("i", "secIds.length", "/exampleRegion p, p.secIds secIds")
createIndex("i", "secId", "/portfolios.asList[1].positions.values")
createIndex("i", "secId", "/portfolios['1'].positions.valules")

//Index on Map types
createIndex("i", "p.positions['key1']", "/exampleRegion p")
createIndex("i", "p.positions['key1','key2',key3',key7']", "/exampleRegion p")
createIndex("i", "p.positions[*]", "/exampleRegion p")
```

The following are some sample queries on indexes.

```
SELECT * FROM (SELECT * FROM /R2 m) r2, (SELECT * FROM  /exampleRegion e WHERE e.pkid IN r2.sp) p

SELECT * FROM (SELECT * FROM /R2 m WHERE m.ID IN SET (1, 5, 10)) r2, 
     (SELECT * FROM  /exampleRegion e WHERE e.pkid IN  r2.sp) p

//examples using position index in the collection
SELECT * FROM /exampleRegion p WHERE p.names[0] = 'aaa'

SELECT * FROM /exampleRegion p WHERE p.position3[1].portfolioId = 2

SELECT DISTINCT positions.values.toArray[0], positions.values.toArray[0], status 
FROM /exampleRegion
```



## Continuous Querying

Continuous querying continuously returns events that match the queries you set up.

- **How Continuous Querying Works**

  Clients subscribe to server-side events by using SQL-type query filtering. The server sends all events that modify the query results. CQ event delivery uses the client/server subscription framework.

- **Implementing Continuous Querying**

  Use continuous querying in your clients to receive continuous updates to queries run on the servers.

- **Managing Continuous Querying**

  This topic discusses CQ management options, CQ states, and retrieving initial result sets.



### How Continuous Querying Works

Clients subscribe to server-side events by using SQL-type query filtering. The server sends all events that modify the query results. CQ event delivery uses the client/server subscription framework.

With CQ, the client sends a query to the server side for execution and receives the events that satisfy the criteria. For example, in a region storing stock market trade orders, you can retrieve all orders over a certain price by running a CQ with a query like this:

```
SELECT * FROM /tradeOrder t WHERE t.price > 100.00
```

When the CQ is running, the server sends the client all new events that affect the results of the query. On the client side, listeners programmed by you receive and process incoming events. For this example query on `/tradeOrder`, you might program a listener to push events to a GUI where higher-priced orders are displayed. CQ event delivery uses the client/server subscription framework.

**Logical Architecture of Continuous Querying**

Your clients can execute any number of CQs, with each CQ assigned any number of listeners.

![img](assets/ContinuousQuerying-1.gif)

**Data Flow with CQs**

CQs do not update the client region. This is in contrast to other server-to-client messaging like the updates sent to satisfy interest registration and responses to get requests from the client’s `Pool`. CQs serve as notification tools for the CQ listeners, which can be programmed in any way your application requires.

When a CQ is running against a server region, each entry event is evaluated against the CQ query by the thread that updates the server cache. If either the old or the new entry value satisfies the query, the thread puts a `CqEvent` in the client’s queue. The `CqEvent` contains information from the original cache event plus information specific to the CQ’s execution. Once received by the client, the `CqEvent` is passed to the `onEvent` method of all `CqListener`s defined for the CQ.

Here is the typical CQ data flow for entries updated in the server cache:

1. Entry events come to the server’s cache from the server or its peers, distribution from remote sites, or updates from a client.
2. For each event, the server’s CQ executor framework checks for a match with its running CQs.
3. If the old or new entry value satisfies a CQ query, a CQ event is sent to the CQ’s listeners on the client side. Each listener for the CQ gets the event.

In the following figure:

- Both the new and old prices for entry X satisfy the CQ query, so that event is sent indicating an update to the query results.
- The old price for entry Y satisfied the query, so it was part of the query results. The invalidation of entry Y makes it not satisfy the query. Because of this, the event is sent indicating that it is destroyed in the query results.
- The price for the newly created entry Z does not satisfy the query, so no event is sent.

![img](assets/ContinuousQuerying-3.gif)

**CQ Events**

CQ events do not change your client cache. They are provided as an event service only. This allows you to have any collection of CQs without storing large amounts of data in your regions. If you need to persist information from CQ events, program your listener to store the information where it makes the most sense for your application.

The `CqEvent` object contains this information:

- Entry key and new value.
- Base operation that triggered the cache event in the server. This is the standard `Operation`class instance used for cache events in GemFire.
- `CqQuery` object associated with this CQ event.
- `Throwable` object, returned only if an error occurred when the `CqQuery` ran for the cache event. This is non-null only for `CqListener` onError calls.
- Query operation associated with this CQ event. This operation describes the change affected to the query results by the cache event. Possible values are:
  - `CREATE`, which corresponds to the standard database `INSERT` operation
  - `UPDATE`
  - `DESTROY`, which corresponds to the standard database DELETE operation

Region operations do not translate to specific query operations and query operations do not specifically describe region events. Instead, the query operation describes how the region event affects the query results.

| Query operations based on old and new entry values | New value does not satisfy the query | New value satisfies the query |
| -------------------------------------------------- | ------------------------------------ | ----------------------------- |
| Old value does not satisfy the query               | no event                             | `CREATE` query operation      |
| Old value does satisfies the query                 | `DESTROY` query operation            | `UPDATE` query operation      |

You can use the query operation to decide what to do with the `CqEvent` in your listeners. For example, a `CqListener` that displays query results on screen might stop displaying the entry, start displaying the entry, or update the entry display depending on the query operation.

**Region Type Restrictions for CQs**

You can only create CQs on replicated or partitioned regions. If you attempt to create a CQ on a non-replicated or non-partitioned region, you will receive the following error message:

```
The region <region name> specified in CQ creation is neither replicated nor partitioned; only replicated or partitioned regions are allowed in CQ creation.
```

In addition, you cannot create a CQ on a replicated region with eviction setting of local-destroy since this eviction setting changes the region’s data policy. If you attempt to create a CQ on this kind of region, you will receive the following error message:

```
CQ is not supported for replicated region: <region name> with eviction action: LOCAL_DESTROY
```

See also [Configure Distributed, Replicated, and Preloaded Regions](https://geode.apache.org/docs/guide/17/developing/distributed_regions/managing_distributed_regions.html) for potential issues with setting local-destroy eviction on replicated regions.



### Implementing Continuous Querying





Use continuous querying in your clients to receive continuous updates to queries run on the servers.

CQs are only run by a client on its servers.

Before you begin, you should be familiar with [Querying](https://geode.apache.org/docs/guide/17/developing/querying_basics/chapter_overview.html) and have your client/server system configured.

1. Configure the client pools you will use for CQs with `subscription-enabled` set to true.

   To have CQ and interest subscription events arrive as closely together as possible, use a single pool for everything. Different pools might use different servers, which can lead to greater differences in event delivery time.

2. Write your OQL query to retrieve the data you need from the server.

   The query must satisfy these CQ requirements in addition to the standard GemFire querying specifications:

   - The FROM clause must contain only a single region specification, with optional iterator variable.
   - The query must be a SELECT expression only, preceded by zero or more IMPORT statements. This means the query cannot be a statement such as `"/tradeOrder.name"` or `"(SELECT * from /tradeOrder).size".`
   - The CQ query cannot use:
     - Cross region joins
     - Drill-downs into nested collections
     - DISTINCT
     - Projections
     - Bind parameters
   - The CQ query must be created on a partitioned or replicated region. See [Region Type Restrictions for CQs](https://geode.apache.org/docs/guide/17/developing/continuous_querying/how_continuous_querying_works.html#how_continuous_querying_works__section_bfs_llr_gr).

   The basic syntax for the CQ query is:

   ```
   SELECT * FROM /fullRegionPath [iterator] [WHERE clause]
   ```

   This example query could be used to get all trade orders where the price is over $100:

   ```
   SELECT * FROM /tradeOrder t WHERE t.price > 100.00
   ```

3. Write your CQ listeners to handle CQ events from the server. Implement `org.apache.geode.cache.query.CqListener` in each event handler you need. In addition to your main CQ listeners, you might have listeners that you use for all CQs to track statistics or other general information.

   **Note:** Be especially careful if you choose to update your cache from your `CqListener`. If your listener updates the region that is queried in its own CQ and that region has a `Pool` named, the update will be forwarded to the server. If the update on the server satisfies the same CQ, it may be returned to the same listener that did the update, which could put your application into an infinite loop. This same scenario could be played out with multiple regions and multiple CQs, if the listeners are programmed to update each other’s regions.

   This example outlines a `CqListener` that might be used to update a display screen with current data from the server. The listener gets the `queryOperation` and entry key and value from the `CqEvent` and then updates the screen according to the type of `queryOperation`.

   ```
   // CqListener class
   public class TradeEventListener implements CqListener
   {
     public void onEvent(CqEvent cqEvent)
     {
       // org.apache.geode.cache Operation associated with the query op
       Operation queryOperation = cqEvent.getQueryOperation();
       // key and new value from the event
       Object key = cqEvent.getKey();
       TradeOrder tradeOrder = (TradeOrder)cqEvent.getNewValue();
       if (queryOperation.isUpdate())
       {
         // update data on the screen for the trade order . . .
       }
       else if (queryOperation.isCreate())
       {
         // add the trade order to the screen . . .
       }
       else if (queryOperation.isDestroy())
       {
         // remove the trade order from the screen . . .
       }
     }
     public void onError(CqEvent cqEvent)
     {
       // handle the error
     }
     // From CacheCallback public void close()
     {
       // close the output screen for the trades . . .
     }
   }
   ```

   When you install the listener and run the query, your listener will handle all of the CQ results.

4. If you need your CQs to detect whether they are connected to any of the servers that host its subscription queues, implement a `CqStatusListener` instead of a `CqListener`. `CqStatusListener` extends the current `CqListener`, allowing a client to detect when a CQ is connected and/or disconnected from the server(s). The `onCqConnected()` method will be invoked when the CQ is connected, and when the CQ has been reconnected after being disconnected. The `onCqDisconnected()` method will be invoked when the CQ is no longer connected to any servers.

   Taking the example from step 3, we can instead implement a `CqStatusListener`:

   ```
   public class TradeEventListener implements CqStatusListener
   {
     public void onEvent(CqEvent cqEvent)
     {
       // org.apache.geode.cache Operation associated with the query op
       Operation queryOperation = cqEvent.getQueryOperation();
       // key and new value from the event
       Object key = cqEvent.getKey();
       TradeOrder tradeOrder = (TradeOrder)cqEvent.getNewValue();
       if (queryOperation.isUpdate())
       {
         // update data on the screen for the trade order . . .
       }
       else if (queryOperation.isCreate())
       {
         // add the trade order to the screen . . .
       }
       else if (queryOperation.isDestroy())
       {
         // remove the trade order from the screen . . .
       }
     }
     public void onError(CqEvent cqEvent)
     {
       // handle the error
     }
     // From CacheCallback public void close()
     {
       // close the output screen for the trades . . .
     }
   
     public void onCqConnected() {
       //Display connected symbol
     }
   
     public void onCqDisconnected() {
       //Display disconnected symbol
     }
   }
   ```

   When you install the `CqStatusListener`, your listener will be able to detect its connection status to the servers that it is querying.

5. Program your client to run the CQ:

   1. Create a `CqAttributesFactory` and use it to set your `CqListener`s and `CqStatusListener`.
   2. Pass the attributes factory and the CQ query and its unique name to the `QueryService` to create a new `CqQuery`.
   3. Start the query running by calling one of the execute methods on the `CqQuery` object. You can execute with or without an initial result set.
   4. When you are done with the CQ, close it.

**Continuous Query Implementation**

```
// Get cache and queryService - refs to local cache and QueryService
// Create client /tradeOrder region configured to talk to the server

// Create CqAttribute using CqAttributeFactory
CqAttributesFactory cqf = new CqAttributesFactory();

// Create a listener and add it to the CQ attributes callback defined below
CqListener tradeEventListener = new TradeEventListener();
cqf.addCqListener(tradeEventListener);
CqAttributes cqa = cqf.create();
// Name of the CQ and its query
String cqName = "priceTracker";
String queryStr = "SELECT * FROM /tradeOrder t where t.price > 100.00";

// Create the CqQuery
CqQuery priceTracker = queryService.newCq(cqName, queryStr, cqa);

try
{  // Execute CQ, getting the optional initial result set
   // Without the initial result set, the call is priceTracker.execute();
  SelectResults sResults = priceTracker.executeWithInitialResults();
  for (Object o : sResults) {
       Struct s = (Struct) o;
       TradeOrder to = (TradeOrder) s.get("value");
       System.out.println("Intial result includes: " + to);
  }
}
  catch (Exception ex)
{
  ex.printStackTrace();
}
// Now the CQ is running on the server, sending CqEvents to the listener
. . .

// End of life for the CQ - clear up resources by closing
priceTracker.close();
```

With continuous queries, you can optionally implement:

- Highly available CQs by configuring your servers for high availability.
- Durable CQs by configuring your clients for durable messaging and indicating which CQs are durable at creation.



### Managing Continuous Querying

This topic discusses CQ management options, CQ states, and retrieving initial result sets.

**Using CQs from a RegionService Instance**

If you are running durable client queues from the `RegionService` instance, stop and start the offline event storage for the client as a whole. The server manages one queue for the entire client process, so you need to request the stop and start of durable CQ event messaging for the cache as a whole, through the `ClientCache` instance. If you closed the `RegionService` instances, event processing would stop, but the server would continue to send events, and those events would be lost.

Stop with:

```
clientCache.close(true);
```

Start up again in this order:

1. Create `ClientCache` instance.
2. Create all `RegionService` instances. Initialize CQ listeners.
3. Call `ClientCache` instance `readyForEvents` method.

**States of a CQ**

A CQ has three possible states, which are maintained on the server. You can check them from the client through `CqQuery.getState`.

| Query State | What does this mean?                                         | When does the CQ reach this state?                           | Notes                                                        |
| ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| STOPPED     | The CQ is in place and ready to run, but is not running.     | When CQ is first created and after being stopped from a running state. | A stopped CQ uses system resources. Stopping a CQ only stops the CQ event messaging from server to client. All server-side CQ processing continues, but new CQ events are not placed into the server’s client queue. Stopping a CQ does not change anything on the client side (but, of course, the client stops receiving events for the CQ that is stopped). |
| RUNNING     | The CQ is running against server region events and the client listeners are waiting for CQ events. | When CQ is executed from a stopped state.                    | This is the only state in which events are sent to the client. |
| CLOSED      | The CQ is not available for any further activities. You cannot rerun a closed CQ. | When CQ is closed by the client and when cache or connection conditions make it impossible to maintain or run. | The closed CQ does not use system resources.                 |

**CQ Management Options**

You manage your CQs from the client side. All calls are executed only for the calling client’s CQs.

| Task                                         | For a single CQ use …                                     | For groups of CQs use …                   |
| -------------------------------------------- | --------------------------------------------------------- | ----------------------------------------- |
| Create a CQ                                  | `QueryService.newCq`                                      | N/A                                       |
| Execute a CQ                                 | `CqQuery.execute` and `CqQuery.executeWithInitialResults` | `QueryService.executeCqs`                 |
| Stop a CQ                                    | `CqQuery.stop`                                            | `QueryService.stopCqs`                    |
| Close a CQ                                   | `CqQuery.close`                                           | `QueryService.closeCqs`                   |
| Access a CQ                                  | `CqEvent.getCq` and `QueryService.getCq`                  | `QueryService.getCq`                      |
| Modify CQ Listeners                          | `CqQuery.getCqAttributesMutator`                          | N/A                                       |
| Access CQ Runtime Statistics                 | `CqQuery.getStatistics`                                   | `QueryService.getCqStatistics`            |
| Get all durable CQs registered on the server | N/A                                                       | `QueryService.getAllDurableCqsFromServer` |

**Managing CQs and Durable Clients Using gfsh**

Using the `gfsh` command-line utility, you can perform the following actions:

- Close durable clients and durable client CQs. See [close](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/close.html#topic_27555B1929D7487D9158096BC065D372).
- List all durable CQs for a given durable client ID. See [list](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html).
- Show the subscription event queue size for a given durable client ID. See [show subscription-queue-size](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_395C96B500AD430CBF3D3C8886A4CD2E).

**Retrieving an Initial Result Set of a CQ**

You can optionally retrieve an initial result set when you execute your CQ. To do this, execute the CQ with the `executeWithInitialResults` method. The initial `SelectResults` returned is the same that you would get if you ran the query ad hoc, by calling `QueryService.newQuery.execute` on the server cache, but with the key included. This example retrieves keys and values from an initial result set:

```
SelectResults cqResults = cq.executeWithInitialResults();
for (Object o : cqResults.asList()) {
  Struct s = (Struct) o; // Struct with Key, value pair
  Portfolio p = (Portfolio) s.get("value"); // get value from the Struct
  String id = (String) s.get("key"); // get key from the Struct
}
```

If you are managing a data set from the CQ results, you can initialize the set by iterating over the result set and then updating it from your listeners as events arrive. For example, you might populate a new screen with initial results and then update the screen from a CQ listener.

If a CQ is executed using the `ExecuteWithInitialResults` method, the returned result may already include the changes with respect to the event. This can arise when updates are happening on the region while CQ registration is in progress. The CQ does not block any region operation as it could affect the performance of the region operation. Design your application to synchronize between the region operation and CQ registration to avoid duplicate events from being delivered.



## Transactions

This section describes Geode transactions. Geode offers an API for client applications that do transactional work. Geode implements optimistic transactions, with the familiar `begin`, `commit`, and `rollback` methods that implement the same operations as in relational database transactions methods.

- **Adherence to ACID Promises**

  This section explains the ways in which Geode’s implementation of optimistic transactions provides ACID semantics.

- **Code Examples**

  An application-based transaction and a transaction embedded in a function provide examples to model.

- **Design Considerations**

  Designs that extend beyond the basics introduce other considerations. This section identifies and discusses how transactions interact with other aspects of the system.



### Adherence to ACID Promises

This section introduces Geode transactions. Geode offers an API for client applications that do transactional work. Geode implements optimistic transactions, choosing the much higher transaction performance they offer over the slow, locking methods of a traditional relational database.

Optimistic transaction semantics are not identical to the Atomicity-Consistency-Isolation-Durability (ACID) semantics of a traditional relational database.

**Atomicity**

Atomicity is “all or nothing” behavior: a transaction completes successfully only when all of the operations it contains complete successfully. If problems occur during a transaction, perhaps due to other transactions with overlapping changes, the transaction cannot successfully complete until the problems are resolved.

Optimistic transactions provide atomicity and realize speed by using a reservation system, instead of using the traditional relational database technique of a two-phase locking of rows. The reservation prevents other, intersecting transactions from completing, allowing the commit to check for conflicts and to reserve resources in an all-or-nothing fashion prior to making changes to the data. After all changes have been made, locally and remotely, the reservation is released. With the reservation system, an intersecting transaction is simply discarded. The serialization of obtaining locks is avoided.

**Consistency**

Consistency requires that data written within a transaction must observe the key and value constraints established for the affected region. Note that validity of the transaction is the responsibility of the application.

**Isolation**

Isolation is the level at which transactional state is visible to system components. Geode transactions have repeatable read isolation. Once the committed value is read for a given key, it always returns that same value. If a write within a transaction deletes a value for a key that has already been read, subsequent reads return the transactional reference.

The default configuration isolates transactions at the process thread level. While a transaction is in progress, its changes are visible only inside the thread that is running the transaction. Other threads within that same process and threads in other processes cannot see changes until after the commit operation begins. After beginning the commit, the changes are visible in the cache, but other threads that access the changing data might see partial results of the transaction, leading to a dirty read. See [Changing the Handling of Dirty Reads](https://geode.apache.org/docs/guide/17/developing/transactions/design_considerations.html#transactions-dirty-reads) for how to change the default behavior.

**Durability**

Relational databases provide durability by using disk storage for recovery and transaction logging. Geode is optimized for performance and does not support on-disk durability for transactions.

See [Allowing Transactions to Work on Persistent Regions](https://geode.apache.org/docs/guide/17/developing/transactions/design_considerations.html#transactions-persistence) for how to allow a transaction that operates on a persistent region in a non-durable way.



### Code Examples

An application can run a transaction directly or invoke a function which contains a transaction. This section illustrates these two use cases with code fragments that demonstrate the proper way to program a transaction.

An expected use case operates on two regions within a transaction. For performance purposes the Geode transaction implementation requires that region entries of partitioned regions be colocated. See [Custom-Partitioning and Colocating Data](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/overview_custom_partitioning_and_data_colocation.html) for details on how to colocate region entries.

**Transaction within an Application**

An application/client uses the `CacheTransactionManager` API. This most basic code fragment shows the structure of a transaction, with its `begin` to start the transaction, `commit` to end the transaction, and handling of exceptions that these methods may throw.

```
CacheTransactionManager txManager =
          cache.getCacheTransactionManager();

try {
    txManager.begin();
    // ... do transactional, region operations
    txManager.commit();
} catch (CommitConflictException conflict) {
    // ... do necessary work for a transaction that failed on commit
} finally {
    // All other exceptions will be handled by the caller.
    // Examples of some exceptions: the data is not colocated, a rebalance
    // interfered with the transaction, or the server is gone.
    // Any exception thrown by a method other than commit() needs
    // to do a rollback to avoid leaking the transaction state.
    if(mgr.exists()) {
        mgr.rollback();
    }       
}
```

More details of a transaction appear in this next application/client code fragment example. In this typical transaction, the put operations must be atomic and two regions are involved.

In this transaction, a customer’s purchase is recorded. The `cash` region contains each customer’s cash balance available for making trades. The `trades` region records each customer’s balance spent on trades.

If there is a conflict upon commit of the transaction, an exception is thrown, and this example tries again.

```
// inputs needed for this transaction; shown as variables for simplicity
final String customer = "Customer1";
final Integer purchase = 1000;

// region set up shown to promote understanding
Cache cache = new CacheFactory().create();
Pool pool = PoolManager.createFactory()
           .addLocator("localhost", LOCATOR_PORT)
           .create("pool-name");
Region<String, Integer> cash =
           cache.createClientRegionFactory(ClientRegionShortcut.PROXY)
           .setPoolName(pool.getName())
           .create("cash");
Region<String, Integer> trades = 
           cache.createClientRegionFactory(ClientRegionShortcut.PROXY)
           .setPoolName(pool.getName())
           .create("trades");

// transaction code
CacheTransactionManager txmgr = cache.getCacheTransactionManager();
boolean retryTransaction = false;
do {
  try {
    txmgr.begin();

    // Subtract out the cost of the trade for this customer's balance
    Integer cashBalance = cash.get(customer);
    Integer newBalance = (cashBalance != null ? cashBalance : 0) - purchase;
    cash.put(customer, newBalance);

    // Add in the cost of the trade for this customer
    Integer tradeBalance = trades.get(customer);
    newBalance = (tradeBalance != null ? tradeBalance : 0) + purchase;
    trades.put(customer, newBalance);

    txmgr.commit();
    retryTransaction = false;
  } 
  catch (CommitConflictException conflict) {
    // entry value changed causing a conflict for this customer, so try again
    retryTransaction = true;
  } finally {
    // All other exceptions will be handled by the caller. 
    // Any exception thrown by a method other than commit() needs
    // to do a rollback to avoid leaking the transaction state.
    if(mgr.exists()) {
      mgr.rollback();
    }       
  }       

} while (retryTransaction);
```

Design transactions such that any get operations are within the transaction. This causes those entries to be part of the transactional state, which is desired such that intersecting transactions can be detected and signal commit conficts.

**Transaction within a Function**

A transaction may be embedded in a function. The application invokes the function, and the function contains the transaction that does the `begin`, the region operations, and the `commit` or `rollback`.

This use of a function can have performance benefits. The performance benefit results from both the function and the region data residing on servers. As the function invokes region operations, those operations on region entries stay on the server, so there is no network round trip time to do get or put operations on region data.

This function example accomplishes atomic updates on a single region representing the quantity of products available in inventory. Doing this in a transaction prevents double allocating inventory for two orders placed simultaneously.

```
/**
 * Atomically reduce inventory quantity
 */
public class TransactionalFunction extends Function {

  /**
   * Returns true if the function had the requested quantity of
   * inventory and successfully completed the transaction to 
   * record the reduced inventory that fulfills the order.
   */
  @Override
  public void execute(FunctionContext context) {
    RegionFunctionContext rfc = (RegionFunctionContext) context;
    Region<ProductId, Integer> inventoryRegion = rfc.getDataSet();

    CacheTransactionManager 
        mgr = CacheFactory.getAnyInstance().getCacheTransactionManager();

    // single argument will be a ProductId and a quantity
    ProductRequest request = (ProductRequest) rfc.getArguments();
    ProductId productRequested = request.getProductId();
    Integer qtyRequested = request.getQuantity();

    Boolean success = false;

    do {
      Boolean commitConflict = false;
      try {
        mgr.begin();

        Integer qtyAvailable = inventoryRegion.get(productRequested);
        Integer qtyRequested = request.getQuantity();
        if (qtyAvail >= qtyRequested) {
          // enough inventory is available, so process request
          Integer remaining = qtyAvailable - qtyRequested;
          inventoryRegion.put(productRequested, remaining);
          success = true;
        } 

        mgr.commit();
      } catch (CommitConflictException conflict) {
        // retry transaction, as another request on this same key succeeded,
        // so this transaction attempt failed
        commitConflict = true;
      } finally {
        // All other exceptions will be handled by the caller; however,
        // any exception thrown by a method other than commit() needs
        // to do a rollback to avoid leaking the transaction state.
        if(mgr.exists()) {
          mgr.rollback();
        }       
      }

    } while (commitConflict);

    context.getResultSender().lastResult(success);
  }

  @Override
  public String getId() {
    return "TxFunction";
  }

  /**
   * Returning true causes this function to execute on the server
   * that holds the primary bucket for the given key. It can save a
   * network hop from the secondary to the primary.
   */
  @Override
  public Boolean optimizeForWrite() {
    return true;
  }
}
```

The application-side details on function implementation are not covered in this example. The application sets up the function context and the argument. See the section on [Function Execution](https://geode.apache.org/docs/guide/17/developing/function_exec/chapter_overview.html)for details on functions.

The function implementation needs to catch the commit conflict exception such that it can retry the entire transaction. The exception only occurs if another request for the same product intersected with this one, and that other request’s transaction committed first.

The `optimizeForWrite` method is defined to cause the system to execute the function on the server that holds the primary bucket for the given key. It can save a network hop from the secondary to the primary.

Note that the variable `qtyAvailable` is a reference, because the `Region.get` operation returns a reference within this server-side code. Read [Region Operations Return References](https://geode.apache.org/docs/guide/17/developing/transactions/design_considerations.html#copy-on-read-transactions) for details and how to work around the implications of a reference as a return value when working with server code.



### Design Considerations

Designs that incorporate more complex features introduce further considerations. This section discusses how transactions interact with other Geode features.

- **Colocate Partitioned Regions**
- **Region Operations Return References**
- **First Operation with Mixed Region Types**
- **Allowing Transactions to Work on Persistent Regions**
- **Mixing Transactions with Queries and Indexes**
- **Mixing Transactions with Eviction**
- **Mixing Transactions with Expiration**
- **Changing the Handling of Dirty Reads**

**Colocate Partitioned Regions**

For performance, transactions that operate on more than one partitioned region require that those partitioned regions colocate their entries. [Colocate Data from Different Partitioned Regions](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html)describes how to colocate entries.

**Region Operations Return References**

For performance, server-invoked region operations return references to region entries. Any assignment to that reference changes the entry within the region. This subverts the system’s ability to maintain consistency and the callback chain for handlers such as cache writers and cache loaders.

Changing an entry using a reference from within a transaction executing on a server has the same consistency issues, but is even worse, as the change will not be seen as part of the transactional state.

There are two ways to work with a reference: make a copy, or configure the system to return copies instead of references. There is a performance penalty to having the system return copies. Both ways are detailed in [Safe Entry Modification](https://geode.apache.org/docs/guide/17/basic_config/data_entries_custom_classes/managing_data_entries.html#managing_data_entries__section_A0E0F889AC344EFA8DF304FD64418809).

**First Operation with Mixed Region Types**

When more than one region participates in a transaction, and there is at least one partitioned and at least one replicated region, the code must do its first operation on the partitioned region to avoid a `TransactionDataNotColocatedException`. Write the transaction to do its first operation on a partitioned region, even if the operation will be spurious.

**Allowing Transactions to Work on Persistent Regions**

Geode’s implementation of atomic transactions prohibits regions with persistence from participating in transactions. The invocation of a persistent region operation within a transaction throws an `UnsupportedOperationException` with an associated message of

```
Operations on persist-backup regions are not allowed because this thread
has an active transaction
```

An application that wishes to allow operations on a persistent region during a transaction can set this system property:

```
-Dgemfire.ALLOW_PERSISTENT_TRANSACTIONS=true
```

Setting this system property eliminates the exception. It does not change the fact that atomicity is not enforced for disk writes that occur with the commit of a transaction. A server crash during the commit may succeed in some, but not all of the disk writes.

**Mixing Transactions with Queries and Indexes**

Queries and query results reflect region state, and not any state or changes that occur within a transaction. Likewise, the contents and updates to an index do not intersect with any changes made within a transaction. Therefore, do not mix transactions with queries or indexed regions.

**Mixing Transactions with Eviction**

LRU eviction and transactions work well together. Any eviction operation on a region entry that is operated on from within a transaction is deferred until the transaction is committed. Further, because any entry touched by the transaction has had its LRU clock reset, eviction is not likely to choose those entries as victims immediately after the commit.

**Mixing Transactions with Expiration**

A transaction disables expiration on any region entries affected by the transaction.

**Changing the Handling of Dirty Reads**

An application requiring a strict, but slower isolation model, such that dirty reads of transitional states are not allowed, should set a property and encapsulate read operations within the transaction. Configure this strict isolation model with the property:

```
-Dgemfire.detectReadConflicts=true
```

This property causes read operations to succeed only when they read a consistent pre- or post-transactional state. If not consistent, Geode throws a `CommitConflictException`.



## Function Execution

A function is a body of code that resides on a server and that an application can invoke from a client or from another server without the need to send the function code itself. The caller can direct a data-dependent function to operate on a particular dataset, or can direct a data-independent function to operate on a particular server, member, or member group.

The function execution service provides solutions for a variety of use cases, including:

- An application needs to perform an operation on the data associated with a key. A registered server-side function can retrieve the data, operate on it, and put it back, with all processing performed locally to the server.
- An application needs to initialize some of its components once on each server, which might be used later by executed functions.
- A third-party service, such as a messaging service, requires initialization and startup.
- Any arbitrary aggregation operation requires iteration over local data sets that can be done more efficiently through a single call to the cache server.
- An external resource needs provisioning that can be done by executing a function on a server.
- **How Function Execution Works**
- **Executing a Function in Apache Geode**



### How Function Execution Works

**Where Functions Are Executed**

You can execute data-independent functions or data-dependent functions in Geode in the following places:

**For Data-independent Functions**

- **On a specific member or members—**Execute the function within a peer-to-peer cluster, specifying the member or members where you want to run the function by using `FunctionService` methods `onMember()` and `onMembers()`.
- **On a specific server or set of servers—**If you are connected to a cluster as a client, you can execute the function on a server or servers configured for a specific connection pool, or on a server or servers connected to a given cache using the default connection pool. For data-independent functions on client/server architectures, a client invokes `FunctionService`methods `onServer()` or `onServers()`. (See [How Client/Server Connections Work](https://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/how_the_pool_manages_connections.html) for details regarding pool connections.)
- **On member groups or on a single member within each member group—**You can organize members into logical member groups. (See [Configuring and Running a Cluster](https://geode.apache.org/docs/guide/17/configuring/chapter_overview.html#concept_lrh_gyq_s4) for more information about using member groups.) You can invoke a data independent function on all members in a specified member group or member groups, or execute the function on only one member of each specified member group.

**For Data-dependent Functions**

- **On a region—**If you are executing a data-dependent function, specify a region and, optionally, a set of keys on which to run the function. The method `FunctionService.onRegion()` directs a data-dependent function to execute on a specific region.

See the `org.apache.geode.cache.execute.FunctionService` Java API documentation for more details.

**How Functions Are Executed**

The following things occur when executing a function:

1. For security-enabled clusters, prior to executing the function, a check is made to see that that caller is authorized to execute the function. The required permissions for authorization are provided by the function’s `Function.getRequiredPermissions()` method. See [Authorization of Function Execution](https://geode.apache.org/docs/guide/17/managing/security/implementing_authorization.html#AuthorizeFcnExecution) for a discussion of this method.
2. Given successful authorization, Geode invokes the function on all members where it needs to run. The locations are determined by the `FunctionService` `on*` method calls, region configuration, and any filters.
3. If the function has results, they are returned to the `addResult` method call in a `ResultCollector` object.
4. The originating member collects results using `ResultCollector.getResult`.

**Highly Available Functions**

Generally, function execution errors are returned to the calling application. You can code for high availability for `onRegion` functions that return a result, so Geode automatically retries a function if it does not execute successfully. You must code and configure the function to be highly available, and the calling application must invoke the function using the results collector `getResult`method.

When a failure (such as an execution error or member crash while executing) occurs, the system responds by:

1. Waiting for all calls to return
2. Setting a boolean indicating a re-execution
3. Calling the result collector’s `clearResults` method
4. Executing the function

For client regions, the system retries the execution according to `org.apache.geode.cache.client.Pool` `retryAttempts`. If the function fails to run every time, the final exception is returned to the `getResult` method.

For member calls, the system retries until either it succeeds or no data remains in the system for the function to operate on.

**Function Execution Scenarios**

This figure shows the sequence of events for a data-independent function invoked from a client on all available servers.



![A diagram showing the sequence of events for a data-independent function invoked from a client on all available servers](assets/FuncExecOnServers.png)

The client contacts a locator to obtain host and port identifiers for each server in the cluster and issues calls to each server. As the instigator of the calls, the client also receives the call results.

This figure shows the sequence of events for a data-independent function executed against members in a peer-to-peer cluster.



![The sequence of events for a data-independent function executed against members in a peer-to-peer cluster.](assets/FuncExecOnMembers.png)

You can think of `onMembers()` as the peer-to-peer counterpart of a client-server call to `onServers()`. Because it is called from a peer of other members in the cluster, an `onMembers()`function invocation has access to detailed metadata and does not require the services of a locator. The caller invokes the function on itself, if appropriate, as well as other members in the cluster and collects the results of all of the function executions.

[Data-dependent Function on a Region](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_data_dependent_function_region) shows a data-dependent function run on a region.



Figure: Data-dependent Function on a Region

![The path followed when the client lacks detailed metadata regarding target locations](assets/FuncExecOnRegionNoMetadata.png)

An `onRegion()` call requires more detailed metadata than a locator provides in its host:port identifier. This diagram shows the path followed when the client lacks detailed metadata regarding target locations, as on the first call or when previously obtained metadata is no longer up to date.

The first time a client invokes a function to be executed on a particular region of a cluster, the client’s knowledge of target locations is limited to the host and port information provided by the locator. Given only this limited information, the client sends its execution request to whichever server is next in line to be called according to the pool allocation algorithm. Because it is a participant in the cluster, that server has access to detailed metadata and can dispatch the function call to the appropriate target locations. When the server returns results to the client, it sets a flag indicating whether a request to a different server would have provided a more direct path to the intended target. To improve efficiency, the client requests a copy of the metadata. With additional details regarding the bucket layout for the region, the client can act as its own dispatcher on subsequent calls and identify multiple targets for itself, eliminating at least one hop.

After it has obtained current metadata, the client can act as its own dispatcher on subsequent calls, identifying multiple targets for itself and eliminating one hop, as shown in [Data-dependent function after obtaining current metadata](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_data_dependent_function_obtaining_current_metadata).



Figure: Data-dependent function after obtaining current metadata

![A diagram showing the client acting as its own dispatcher after having obtained current metadata.](assets/FuncExecOnRegionWithMetadata.png)

[Data-dependent Function on a Region with Keys](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_data_dependent_function_region_keys) shows the same data-dependent function with the added specification of a set of keys on which to run.



Figure: Data-dependent Function on a Region with Keys

![A data-dependent function on a region with specification of keys on which to run](assets/FuncExecOnRegionWithFilter.png)

Servers that do not hold any keys are left out of the function execution.

[Peer-to-peer Data-dependent Function](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_peer_data_dependent_function) shows a peer-to-peer data-dependent call.



Figure: Peer-to-peer Data-dependent Function

![A data-dependent function where the caller is not an external client](assets/FuncExecOnRegionPeersWithFilter.png)

The caller is a member of the cluster, not an external client, so the function runs in the caller’s cluster. Note the similarities between this diagram and the preceding figure ([Data-dependent Function on a Region with Keys](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_data_dependent_function_region_keys)), which shows a client-server model where the client has up-to-date metadata regarding target locations within the cluster.

[Client-server system with Up-to-date Target Metadata](https://geode.apache.org/docs/guide/17/developing/function_exec/how_function_execution_works.html#how_function_execution_works__fig_client_server_system_target_metadata) demonstrates a sequence of steps in a call to a highly available function in a client-server system in which the client has up-to-date metadata regarding target locations.

Figure: Client-server system with Up-to-date Target Metadata

![A sequence of steps in a call to a highly available function in a client-server system in which the client has up-to-date metadata regarding target locations](assets/FuncExecOnRegionHAWithFilter-1545276886457.png)

In this example, three primary keys (X, Y, Z) and their secondary copies (X’, Y’, Z’) are distributed among three servers. Because `optimizeForWrite` is `true`, the system first attempts to invoke the function where the primary keys reside: Server 1 and Server 2. Suppose, however, that Server 2 is off-line for some reason, so the call targeted for key Y fails. Because `isHA` is set to `true`, the call is retried on Server 1 (which succeeded the first time, so likely will do so again) and Server 3, where key Y’ resides. This time, the function call returns successfully. Calls to highly available functions retry until they obtain a successful result or they reach a retry limit.



### Executing a Function in Apache Geode

In this procedure it is assumed that you have your members and regions defined where you want to run functions.

Main tasks:

1. Write the function code.
2. Register the function on all servers where you want to execute the function. The easiest way to register a function is to use the `gfsh` `deploy` command to deploy the JAR file containing the function code. Deploying the JAR automatically registers the function for you. See [Register the Function Automatically by Deploying a JAR](https://geode.apache.org/docs/guide/17/developing/function_exec/function_execution.html#function_execution__section_164E27B88EC642BA8D2359B18517B624) for details. Alternatively, you can write the XML or application code to register the function. See [Register the Function Programmatically](https://geode.apache.org/docs/guide/17/developing/function_exec/function_execution.html#function_execution__section_1D1056F843044F368FB76F47061FCD50) for details.
3. Write the application code to run the function and, if the function returns results, to handle the results.
4. If your function returns results and you need special results handling, code a custom `ResultsCollector` implementation and use it in your function execution.

**Write the Function Code**

To write the function code, you implement the `Function` interface in the `org.apache.geode.cache.execute` package.

Code the methods you need for the function. These steps do not have to be done in this order.

- Implement `getId` to return a unique name for your function. You can use this name to access the function through the `FunctionService` API.
- For high availability:
  1. Code `isHa` to return true to indicate to Geode that it can re-execute your function after one or more members fails
  2. Code your function to return a result
  3. Code `hasResult` to return true
- Code `hasResult` to return true if your function returns results to be processed and false if your function does not return any data - the fire and forget function.
- If the function will be executed on a region, implement `optimizeForWrite` to return false if your function only reads from the cache, and true if your function updates the cache. The method only works if, when you are running the function, the `Execution` object is obtained through a `FunctionService` `onRegion` call. `optimizeForWrite` returns false by default.
- If the function should be run with an authorization level other than the default of `DATA:WRITE`, implement an override of the `Function.getRequiredPermissions()` method. See [Authorization of Function Execution](https://geode.apache.org/docs/guide/17/managing/security/implementing_authorization.html#AuthorizeFcnExecution) for details on this method.
- Code the `execute` method to perform the work of the function.
  1. Make `execute` thread safe to accommodate simultaneous invocations.
  2. For high availability, code `execute` to accommodate multiple identical calls to the function. Use the `RegionFunctionContext` `isPossibleDuplicate` to determine whether the call may be a high-availability re-execution. This boolean is set to true on execution failure and is false otherwise. **Note:** The `isPossibleDuplicate` boolean can be set following a failure from another member’s execution of the function, so it only indicates that the execution might be a repeat run in the current member.
  3. Use the function context to get information about the execution and the data:
     - The context holds the function ID, the `ResultSender` object for passing results back to the originator, and function arguments provided by the member where the function originated.
     - The context provided to the function is the `FunctionContext`, which is automatically extended to `RegionFunctionContext` if you get the `Execution` object through a `FunctionService` `onRegion` call.
     - For data dependent functions, the `RegionFunctionContext` holds the `Region` object, the `Set` of key filters, and a boolean indicating multiple identical calls to the function, for high availability implementations.
     - For partitioned regions, the `PartitionRegionHelper` provides access to additional information and data for the region. For single regions, use `getLocalDataForContext`. For colocated regions, use `getLocalColocatedRegions`. **Note:** When you use `PartitionRegionHelper.getLocalDataForContext`, `putIfAbsent` may not return expected results if you are working on local data set instead of the region.
  4. To propagate an error condition or exception back to the caller of the function, throw a FunctionException from the `execute` method. Geode transmits the exception back to the caller as if it had been thrown on the calling side. See the Java API documentation for [FunctionException](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/execute/FunctionException.html) for more information.

Example function code:

```
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;

public class MultiGetFunction implements Function {

  public void execute(FunctionContext fc) {
    if(! (fc instanceof RegionFunctionContext)){
      throw new FunctionException("This is a data aware function, and has
 to be called using FunctionService.onRegion.");
    }
    RegionFunctionContext context = (RegionFunctionContext)fc;
    Set keys = context.getFilter();
    Set keysTillSecondLast = new HashSet();
    int setSize = keys.size();
    Iterator keysIterator = keys.iterator();
    for(int i = 0; i < (setSize -1); i++)
    {
      keysTillSecondLast.add(keysIterator.next());
    }
    for (Object k : keysTillSecondLast) {
      context.getResultSender().sendResult(
          (Serializable)PartitionRegionHelper.getLocalDataForContext(context)
              .get(k));
    }
    Object lastResult = keysIterator.next();
    context.getResultSender().lastResult(
        (Serializable)PartitionRegionHelper.getLocalDataForContext(context)
            .get(lastResult));
  }

  public String getId() {
    return getClass().getName();
  }
}
```

**Register the Function Automatically by Deploying a JAR**

When you deploy a JAR file that contains a Function (in other words, contains a class that implements the Function interface), the Function will be automatically registered via the `FunctionService.registerFunction` method.

To register a function by using `gfsh`:

1. Package your class files into a JAR file.

2. Start a `gfsh` prompt. If necessary, start a locator and connect to the cluster where you want to run the function.

3. At the gfsh prompt, type the following command:

   ```
   gfsh>deploy --jar=group1_functions.jar
   ```

   where group1_functions.jar corresponds to the JAR file that you created in step 1.

If another JAR file is deployed (either with the same JAR filename or another filename) with the same Function, the new implementation of the Function will be registered, overwriting the old one. If a JAR file is undeployed, any Functions that were auto-registered at the time of deployment will be unregistered. Since deploying a JAR file that has the same name multiple times results in the JAR being un-deployed and re-deployed, Functions in the JAR will be unregistered and re-registered each time this occurs. If a Function with the same ID is registered from multiple differently named JAR files, the Function will be unregistered if either of those JAR files is re-deployed or un-deployed.

See [Deploying Application JARs to Apache Geode Members](https://geode.apache.org/docs/guide/17/configuring/cluster_config/deploying_application_jars.html#concept_4436C021FB934EC4A330D27BD026602C) for more details on deploying JAR files.

**Register the Function Programmatically**

This section applies to functions that are invoked using the `Execution.execute(String functionId)`signature. When this method is invoked, the calling application sends the function ID to all members where the `Function.execute` is to be run. Receiving members use the ID to look up the function in the local `FunctionService`. In order to do the lookup, all of the receiving member must have previously registered the function with the function service.

The alternative to this is the `Execution.execute(Function function)` signature. When this method is invoked, the calling application serializes the instance of `Function` and sends it to all members where the `Function.execute` is to be run. Receiving members deserialize the `Function` instance, create a new local instance of it, and run execute from that. This option is not available for non-Java client invocation of functions on servers.

Your Java servers must register functions that are invoked by non-Java clients. You may want to use registration in other cases to avoid the overhead of sending `Function` instances between members.

Register your function using one of these methods:

- XML:

  ```
  <cache>
      ...
      </region>
  <function-service>
    <function>
      <class-name>com.bigFatCompany.tradeService.cache.func.TradeCalc</class-name>
    </function>
  </function-service>
  ```

- Java:

  ```
  myFunction myFun = new myFunction();
  FunctionService.registerFunction(myFun);
  ```

  **Note:** Modifying a function instance after registration has no effect on the registered function. If you want to execute a new function, you must register it with a different identifier.

**Run the Function**

This assumes you’ve already followed the steps for writing and registering the function.

In every member where you want to explicitly execute the function and process the results, you can use the `gfsh` command line to run the function or you can write an application to run the function.

**Running the Function Using gfsh**

1. Start a gfsh prompt.

2. If necessary, start a locator and connect to the cluster where you want to run the function.

3. At the gfsh prompt, type the following command:

   ```
   gfsh> execute function --id=function_id
   ```

   Where *function_id* equals the unique ID assigned to the function. You can obtain this ID using the `Function.getId` method.

See [Function Execution Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_8BB061D1A7A9488C819FE2B7881A1278) for more `gfsh` commands related to functions.

**Running the Function via API Calls**

1. Use one of the `FunctionService` `on*` methods to create an `Execute` object. The `on*`methods, `onRegion`, `onMembers`, etc., define the highest level where the function is run. For colocated partitioned regions, use `onRegion` and specify any one of the colocated regions. The function run using `onRegion` is referred to as a data dependent function - the others as data-independent functions.
2. Use the `Execution` object as needed for additional function configuration. You can:
   - Provide a key `Set` to `withFilters` to narrow the execution scope for `onRegion` `Execution`objects. You can retrieve the key set in your `Function` `execute` method through `RegionFunctionContext.getFilter`.
   - Provide function arguments to `setArguments`. You can retrieve these in your `Function` `execute` method through `FunctionContext.getArguments`.
   - Define a custom `ResultCollector`
3. Call the `Execution` object to `execute` method to run the function.
4. If the function returns results, call `getResult` from the results collector returned from `execute`and code your application to do whatever it needs to do with the results. **Note:** For high availability, you must call the `getResult` method.

Example of running the function - for executing members:

```
MultiGetFunction function = new MultiGetFunction();
FunctionService.registerFunction(function);

writeToStdout("Press Enter to continue.");
stdinReader.readLine();

Set keysForGet = new HashSet();
keysForGet.add("KEY_4");
keysForGet.add("KEY_9");
keysForGet.add("KEY_7");

Execution execution = FunctionService.onRegion(exampleRegion)
    .withFilter(keysForGet)
    .setArguments(Boolean.TRUE)
    .withCollector(new MyArrayListResultCollector());

ResultCollector rc = execution.execute(function);
// Retrieve results, if the function returns results
List result = (List)rc.getResult();
```

**Write a Custom Results Collector**

This topic applies to functions that return results.

When you execute a function that returns results, the function stores the results into a `ResultCollector` and returns the `ResultCollector` object. The calling application can then retrieve the results through the `ResultCollector` `getResult` method. Example:

```
ResultCollector rc = execution.execute(function);
List result = (List)rc.getResult();
```

Geode’s default `ResultCollector` collects all results into an `ArrayList`. Its `getResult` methods block until all results are received. Then they return the full result set.

To customize results collecting:

1. Write a class that extends `ResultCollector` and code the methods to store and retrieve the results as you need. Note that the methods are of two types:

   1. `addResult` and `endResults` are called by Geode when results arrive from the `Function`instance `SendResults` methods
   2. `getResult` is available to your executing application (the one that calls `Execution.execute`) to retrieve the results

2. Use high availability for `onRegion` functions that have been coded for it:

   1. Code the `ResultCollector` `clearResults` method to remove any partial results data. This readies the instance for a clean function re-execution.
   2. When you invoke the function, call the result collector `getResult` method. This enables the high availability functionality.

3. In your member that calls the function execution, create the `Execution` object using the `withCollector` method, and passing it your custom collector. Example:

   ```
   Execution execution = FunctionService.onRegion(exampleRegion)
       .withFilter(keysForGet)
       .setArguments(Boolean.TRUE)
       .withCollector(new MyArrayListResultCollector());
   ```

**Targeting Single Members of a Member Group or Entire Member Groups**

To execute a data independent function on a group of members or one member in a group of members, you can write your own nested function. You will need to write one nested function if you are executing the function from client to server and another nested function if you are executing a function from server to all members.



