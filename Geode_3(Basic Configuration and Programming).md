# 基本配置和编程

*基本配置和编程* 介绍如何为Apache Geode安装配置集群和缓存属性。 对于您的应用程序，它提供了编写代码以管理缓存和集群连接，数据区域和数据条目（包括自定义类）的指导。

- **[集群和缓存配置](#集群和缓存配置)**

  要使用Apache Geode应用程序，可以使用配置文件和应用程序代码的组合。

- **[缓存管理](#缓存管理)**

  Geode缓存是Geode缓存管理的入口点。 Geode提供不同的API和XML配置模型来支持不同成员的行为。

- **[数据区域](#数据区域)**

  该区域是Apache Geode集群的核心构建块。 所有缓存的数据都组织到数据区域中，您可以针对它们执行所有数据的放置，获取和查询活动。

- **[数据条目](#数据条目)**

  数据条目是存储数据的键/值对。 您可以单独和批量管理您的条目。 要将域对象用于条目值和键，您需要遵循Apache Geode要求进行数据存储和分发。



## 集群和缓存配置

要使用Apache Geode应用程序，可以使用配置文件和应用程序代码的组合。

- **集群成员**

  集群成员是连接到Geode集群的程序。 您将成员配置为属于单个集群，并且可以选择将它们配置为客户端或服务器以配置到其他集群中的成员，以及与其他集群通信。

- **设置属性**

  Geode为开箱即用的系统提供默认的集群配置。 要使用非默认配置并微调成员通信，可以使用各种选项的混合来自定义集群配置。

- **配置缓存和数据区域的选项**

  要填充Apache Geode缓存并微调其存储和分发行为，您需要定义缓存数据区域并为缓存和区域提供自定义配置。

- **本地和远程成员身份和缓存**

  对于许多Apache Geode讨论，您需要了解本地和远程成员资格和缓存之间的区别。



### 集群成员

集群成员是连接到Geode集群的程序。 您将成员配置为属于单个集群，并且可以选择将它们配置为客户端或服务器以配置到其他集群中的成员，以及与其他集群通信。

**成员概述**

集群成员（或简称“成员”）在创建Geode数据缓存时连接到Geode集群。 成员的集群通过Geode属性配置。 请参阅[gemfire.properties和gfsecurity.properties(Geode Properties)](http://geode.apache.org/docs/guide/17/reference/topics/gemfire_properties.html)。 Geode属性指定成员启动，初始化和通信的所有必要信息。

**注意:** 当成员连接到集群时，您无法更改成员的属性。

使用属性来定义：

- 如何查找和与其他成员沟通
- 如何执行日志记录和统计活动
- 哪个持久性配置或`cache.xml`文件用于缓存和数据区域初始化
- 其他选项，包括事件合并，如何处理网络丢失和安全设置

**成员资格和系统拓扑**

每个Geode进程都是集群的成员，即使集群被定义为独立的，只有一个成员。 您可以单独运行单个集群，也可以组合集群进行垂直和水平缩放。 请参阅[拓扑和通信一般概念](http://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/chapter_overview.html)。

- **Peer-to-Peer Clusters**. 定义相同成员发现属性的成员属于同一集群，并且彼此对等。
- **Client/Server Installations**. 客户端/服务器拓扑使用您在多个集群的成员之间配置的关系。 您可以将一个集群中的部分或全部对等配置为从集群外部连接的客户端的缓存服务器。 每个服务器都可以托管许多客户端进程，在高效的垂直分层缓存配置中管理所有客户端的缓存访问。 您使用客户端缓存配置将客户端应用程序配置为连接到服务器。 客户端作为独立Geode集群的成员运行，没有对等体，因此所有数据更新和请求都将发送到服务器。

**多站点安装**

多站点拓扑使用您在多个集群的成员之间配置的关系。 通过此配置，您可以松散地耦合两个或更多集群以进行自动数据分发。 这通常针对地理位置不同的站点进行。 您可以使用网关发件人和/或网关接收器配置每个集群站点中的对等项子集，以管理在站点之间分发的事件。

在单个集群的上下文中，除非另有说明，否则“远程成员”指的是同一集群的另一个成员。 在客户端/服务器和多站点安装中，“远程”通常是指其他集群中的成员。 例如，所有服务器都是“远程”连接到它们的客户端。 每个客户端独立运行，仅连接到服务器层，因此所有服务器及其他客户端都是“远程”到单个客户端。 所有网关接收器都“远程”到网关发送器，这些发送器从其他集群连接到它们，并连接到那些网关发送器的对等体。

### 设置属性

Geode为开箱即用的系统提供默认配置。 要使用非默认配置并微调成员通信，可以使用各种选项的混合来自定义配置。

Geode属性用于连接集群并配置系统成员行为。 通过`gemfire.properties`文件，Java API或命令行输入配置Geode属性。 通常，您将所有属性存储在`gemfire.properties`文件中，但您可能需要通过其他方式提供属性，例如，为您从键盘输入接收的用户名和密码传入安全属性。

**注意:** 在通过API更改属性之前，请咨询Geode系统管理员，包括`gemfire.properties`和`gfsecurity.properties`设置。 系统管理员可能需要在命令行或配置文件中设置属性。 通过API进行的任何更改都将覆盖其他设置。

**注意:** 产品`defaultConfigs`目录有一个带有所有默认设置的示例`gemfire.properties`文件。

通过以下任意组合设置属性。 系统按列出的顺序查找设置：

1. `java.lang.System`属性设置。 通常在命令行设置。 对于应用程序，请在代码或命令行中设置它们。

   命名：以`gemfire.property-name`格式指定这些属性，其中`property-name`匹配`gemfire.properties`文件中的名称。 要设置gemfire属性文件名，请单独使用`gemfirePropertyFile`

   - 在API中，在缓存创建调用之前设置`System`属性。 例：

     ```
     System.setProperty("gemfirePropertyFile", "gfTest");
     System.setProperty("gemfire.mcast-port", "10999");
     
     Cache cache = new CacheFactory().create();
     ```

   - 在`java`命令行中，使用`-D`开关传入`System`属性。 例：

     ```
     java -DgemfirePropertyFile=gfTest -Dgemfire.mcast-port=10999 test.Program
     ```

2. 在“属性”对象中输入。

   命名：使用`gemfire.properties`文件中的名称指定这些属性。 要设置gemfire属性文件名，请使用`gemfirePropertyFile`。

   - 在API中，创建一个`Properties`对象并将其传递给缓存create方法。 例：

     ```
     Properties properties= new Properties();
     properties.setProperty("log-level", "warning");
     properties.setProperty("name", "testMember2");
     ClientCache userCache = 
         new ClientCacheFactory(properties).create();
     ```

   - 对于缓存服务器，将`gfsh`命令行上的属性文件作为命令行选项传递。 例：

     ```
     gfsh>start server --name=server_name --mcast-port=10338 --properties-file=serverConfig/gemfire.properties --security-properties-file=gfsecurity.properties
     ```

     有关运行缓存服务器的详细信息，请参阅[运行Geode服务器进程](http://geode.apache.org/docs/guide/17/configuring/running/running_the_cacheserver.html)。

3. 在`gemfire.properties`文件中输入。 请参见[不使用集群配置服务部署配置文件](http://geode.apache.org/docs/guide/17/configuring/running/deploying_config_files.html)。 例：

   ```
   cache-xml-file=cache.xml
   conserve-sockets=true
   disable-tcp=false
   ```

4. 默认值。 默认值在`org.apache.geode.distributed.ConfigurationProperties`的API中定义。



### 配置缓存和数据区域的选项

要填充Apache Geode缓存并微调其存储和分发行为，您需要定义缓存数据区域并为缓存和区域提供自定义配置。

缓存配置属性定义：

- 缓存范围的设置，例如磁盘存储，通信超时以及将成员指定为服务器的设置
- 缓存数据区域

通过以下一种或多种方法配置缓存及其数据区域：

- 通过在发出使用gfsh命令行实用程序的命令时定义的持久性配置。 gfsh实用程序支持Apache Geode进程和应用程序的管理，调试和部署。 您可以使用gfsh配置区域，定位器，服务器，磁盘存储，事件队列和其他对象。

  在发出命令时，gfsh会保存一组适用于整个集群的配置，并保存仅适用于集群中已定义成员组的配置。 您可以重复使用这些配置来创建集群。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。

- 通过在`cache-xml-file``gemfire.properties`设置中命名的XML文件中的声明。 此文件通常称为`cache.xml`文件，但它可以具有任何名称。 请参阅[cache.xml](http://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_cache_xml.html#cache_xml)。

- 通过应用程序调用`org.apache.geode.cache.CacheFactory`，`org.apache.geode.cache.Cache`和`org.apache.geode.cache.Region` API。



### 本地和远程成员身份和缓存

对于许多Apache Geode讨论，您需要了解本地和远程成员资格和缓存之间的区别。

有关Geode成员资格和缓存活动的讨论通常会区分本地和远程。 本地缓存总是指向在讨论的中心成员，如果有一个这样明显的成员，而远程指的是其他成员。 如果没有明确的单一本地成员，则讨论会为成员分配名称以进行区分。 “成员Q本地”的操作，数据，配置等正在成员Q进程内运行或驻留。 “远离成员Q”的操作，数据，配置等正在运行或驻留在其他成员中。

本地缓存是属于本地成员的缓存。 所有其他缓存都是远程的，无论是在同一集群的其他成员中还是在不同的集群中。

## 缓存管理

Geode缓存是Geode缓存管理的入口点。 Geode提供不同的API和XML配置模型来支持不同成员的行为。

- **缓存管理简介**

  缓存为您的数据提供内存存储和管理。

- **管理对等或服务器缓存**

  您可以使用XML声明和API调用的组合来启动对等或服务器缓存。 完成后关闭缓存。

- **管理客户端缓存**

  您有多个客户端缓存配置选项。 使用XML声明和API调用的组合启动客户端缓存。 完成后关闭客户端缓存。

- **管理安全系统中的缓存**

  在安全系统中创建缓存时，您需要为连接过程提供凭据，以便已经运行的安全成员进行身份验证。 客户端连接到安全服务器。 对等方由安全定位器或对等成员进行身份验证。

- **管理多个安全用户的RegionServices**

  在安全系统中，您可以通过与每个客户端的服务器建立多个安全连接来创建客户端。 最常见的用例是嵌入在应用服务器中的Geode客户端，该服务器支持来自许多用户的数据请求。 可以授权每个用户访问服务器上的数据子集。 例如，可以允许客户用户仅查看和更新他们自己的订单和货件。

- **初始化缓存后启动应用程序**

  您可以指定在缓存初始化后启动的回调应用程序。



### 缓存管理简介

缓存为您的数据提供内存存储和管理。

您将缓存中的数据组织到*数据区域*中，每个数据区域都有自己的可配置行为。 您将数据存储在名为* data entries *的键/值对中的区域中。 缓存还提供事务，数据查询，磁盘存储管理和日志记录等功能。 有关`org.apache.geode.cache.Cache`的信息，请参阅Javadocs。

您通常使用`gfsh`命令行实用程序或XML声明和API调用的组合来配置缓存。 首次创建缓存时，Geode会加载并处理您的XML声明。

Geode有一种用于管理服务器和对等缓存的缓存类型，另一种用于管理客户端缓存。 缓存服务器进程在启动时自动创建其服务器缓存。 在应用程序进程中，缓存创建将返回服务器/对等或客户端缓存的实例。 从那时起，您可以通过应用程序中的API调用来管理缓存。

**缓存API**

Geode的缓存API为不同的系统成员类型和安全设置提供专门的行为。

- **org.apache.geode.cache.RegionService**. 通常，您通过`Cache`和`ClientCache`的实例使用`RegionService`功能。 您只在为许多用户提供服务的安全客户端应用程序中为受限访问用户专门使用`RegionService`实例。 `RegionService` API提供对现有缓存数据区域和缓存的标准查询服务的访问。 对于客户端缓存，查询将发送到服务器层。 对于服务器和对等缓存，查询在当前缓存和任何可用对等体中运行。 `RegionService`由`GemFireCache`实现。
- **org.apache.geode.cache.GemFireCache**. 你没有专门使用`GemFireCache`的实例，但你在`Cache`和`ClientCache`的实例中使用`GemFireCache`功能。 `GemFireCache`扩展了`RegionService`并添加了一般缓存功能，如区域属性，区域持久性和溢出的磁盘存储以及对底层集群的访问。 `GemFireCache`由`Cache`和`ClientCache`实现。
- **org.apache.geode.cache.Cache**. 使用`Cache`接口来管理服务器和对等缓存。 每个服务器或对等进程都有一个`Cache`。 `Cache`扩展了`GemFireCache`并添加了服务器/对等缓存功能，如集群内的通信，区域创建，事务和查询以及缓存服务器功能。
- **org.apache.geode≈setting_cache_initializer.cache.ClientCache**. 使用`ClientCache`接口管理客户端中的缓存。 每个客户端进程有一个`ClientCache`。 “ClientCache”扩展了“GemFireCache”并添加了客户端特定的缓存功能，如客户端区域创建，持久客户端的订阅保持活动管理，查询服务器和客户端层，以及RegionService创建，以便客户端内的多个用户进行安全访问。

**缓存XML**

您的`cache.xml`必须根据产品XML模式定义`cache-1.0.xsd`进行格式化。 架构定义文件位于<http://geode.apache.org/schema/cache/cache-1.0.xsd>。

您可以将一种格式用于对等和服务器缓存，将另一种格式用于客户端缓存。

对等/服务器的`cache.xml`：

```
<?xml version="1.0" encoding="UTF-8"?>
<cache xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0”>
...
</cache>
```

客户端的`cache.xml`：

```
<?xml version="1.0" encoding="UTF-8"?>
<client-cache
    xmlns="http://geode.apache.org/schema/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
    version="1.0”>
...
</client-cache>
```

有关`cache.xml`文件的更多信息，请参阅[cache.xml](http://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_cache_xml.html#cache_xml)。

**创建并关闭缓存**

启动成员进程并创建每个成员的Geode缓存时，将初始化您的系统配置和缓存配置。 如果您使用的是集群配置服务，则成员进程可以从集群或组的当前配置中获取其缓存配置。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。

本节中的步骤使用`gemfire.properties`和`cache.xml`文件示例，除非需要API。 您也可以通过API配置集群属性和缓存，并且可以使用文件配置和API配置的组合。

XML示例可能不包含完整的`cache.xml`文件列表。 所有声明性缓存配置必须符合<http://geode.apache.org/schema/cache/cache-1.0.xsd>上的缓存XSD。

对于您的所有Geode应用程序：

1. 为客户端应用程序创建用于对等/服务器应用程序的`Cache`或`ClientCache`。 这将连接到您已配置的Geode系统，并初始化任何已配置的数据区域。 使用缓存实例访问您的区域并执行应用程序工作。
2. 完成后关闭缓存。 这将释放资源并以有序的方式断开应用程序与集群的连接。

按照[缓存管理](http://geode.apache.org/docs/guide/17/basic_config/the_cache/chapter_overview.html#the_cache)下的子主题中的说明自定义缓存创建和关闭以满足您的应用程序需求。 您可能需要组合多个指令集。 例如，要在具有安全性的系统中创建客户端缓存，您可以按照创建和关闭客户端缓存以及在安全系统中创建和关闭缓存的说明进行操作。

**导出和导入缓存快照**

为了帮助管理缓存数据并加快新环境的设置，您可以导出整个缓存（所有区域）的快照，然后将快照导入新缓存。 例如，您可以获取生产环境缓存的快照，以便将缓存的数据导入测试环境。

有关导出和导入缓存快照的更多详细信息，请参阅[缓存和区域快照](http://geode.apache.org/docs/guide/17/managing/cache_snapshots/chapter_overview.html#concept_E6AC3E25404D4D7788F2D52D83EE3071)。

**使用gfsh和集群配置服务进行缓存管理**

您可以使用gfsh命令管理服务器缓存。 有gfsh命令可用于创建区域，启动服务器以及创建队列和其他对象。 在发出这些命令时，Cluster Configuration Service会在定位器上保存cache.xml和gemfire.properties文件，并将这些配置分发给任何加入集群的新成员。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。



### 管理对等或服务器缓存

您可以使用XML声明和API调用的组合来启动对等或服务器缓存。 完成后关闭缓存。

Geode对等体是Geode集群的成员，它不充当另一个Geode集群的客户端。 Geode服务器是同时监听和处理客户端请求的对等服务器。

1. 创建缓存：

   1. 启动集群和集群配置服务：

      1. 启动“--enable-cluster-configuration”设置为true的定位器。 （默认设置为true。）

         ```
         gfsh>start locator --name=locator1
         ```

      2. 启动使用集群配置服务的成员进程（默认情况下启用）：

         ```
         gfsh>start server --name=server1 --server-port=40404
         ```

      3. 创建区域：

         ```
         gfsh>create region --name=customerRegion --type=REPLICATE
         
         gfsh>create region --name=ordersRegion --type=PARTITION
         ```

   2. 或者，如果您未使用集群配置服务，请直接在集群的每个成员中配置cache.xml。 在`cache.xml`中，使用`cache` DOCTYPE并在`<cache>`元素中配置缓存。 例：

      ```
      <?xml version="1.0" encoding="UTF-8"?>
      <cache
          xmlns="http://geode.apache.org/schema/cache"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
          version="1.0”>
          // NOTE: Use this <cache-server> element only for server processes 
          <cache-server port="40404"/>
          <region name="customerRegion" refid="REPLICATE" />
          <region name="ordersRegion" refid="PARTITION" />
      </cache>
      ```

   3. 以编程方式创建`Cache`实例：

      - 在Java应用程序中，使用`CacheFactory`创建方法：

        ```
        Cache cache = new CacheFactory().create();
        ```

      - 如果您使用Geode`cacheserver`进程运行服务器，它会在启动时自动创建缓存和连接，并在退出时关闭它们。

      系统根据您的`gemfire.properties`和`cache.xml`规范创建连接并初始化缓存。

2. 使用`Cache`实例的继承`close`方法完成后关闭缓存：

   ```
   cache.close();
   ```



### 管理客户端缓存

您有多个客户端缓存配置选项。 使用XML声明和API调用的组合启动客户端缓存。 完成后关闭客户端缓存。

Geode客户端是将大部分或全部数据请求和更新发送到Geode服务器系统的进程。 客户端作为独立进程运行，没有自己的同级进程。

**注意:** Geode自动为您的`ClientCache`配置集群，这意味着客户端没有对等体。 不要尝试为客户端应用程序设置`gemfire.properties``mcast-port`或`locators`，否则系统将抛出异常。

1. 创建客户端缓存：

   1. 在`cache.xml`中，使用`client-cache` DOCTYPE并在`<client-cache>`元素中配置缓存。 根据需要配置服务器连接池和区域。 例：

      ```
      <?xml version="1.0" encoding="UTF-8"?>
      <client-cache
          xmlns="http://geode.apache.org/schema/cache"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
          version="1.0">
          <pool name="serverPool">
              <locator host="host1" port="44444"/>
          </pool>
          <region name="exampleRegion" refid="PROXY"/>
      </client-cache>
      ```

      **注意:** 使用`client-cache`的应用程序可能希望为区域设置`concurrency-checks-enabled`为false，以便查看该区域的所有事件。 Geode服务器成员可以继续使用并发检查，但是它们会将所有事件传递到客户端缓存。 此配置可确保客户端查看所有区域事件，但不会阻止客户端缓存区域与服务器缓存不同步。 请参阅[区域更新的一致性](http://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045)。

   2. 如果使用多个服务器池，请为每个客户端区域显式配置池名称。 例：

      ```
      <pool name="svrPool1">
          <locator host="host1" port="40404"/>
      </pool>
      <pool name="svrPool2">
          <locator host="host2" port="40404"/>
      </pool>
      <region name="clientR1" refid="PROXY" pool-name="svrPool1"/>  
      <region name="clientR2" refid="PROXY" pool-name="svrPool2"/>
      <region name="clientsPrivateR" refid="LOCAL"/>
      ```

   3. 在Java客户端应用程序中，使用`ClientCacheFactory``create`method创建缓存。 例：

      ```
      ClientCache clientCache = new ClientCacheFactory().create();
      ```

      这将创建服务器连接并根据您的`gemfire.properties`和`cache.xml`规范初始化客户端的缓存。

2. 使用`Cache`实例的`close`方法完成后关闭缓存：

   ```
   cache.close();
   ```

   如果您的客户端是持久的，并且您希望在关闭客户端缓存时维护持久队列，请使用：

   ```
   clientCache.close(true);
   ```



### 管理安全系统中的缓存

要在安全系统中创建缓存，连接时的身份验证将需要凭据。 授权允许按配置操作。

这些步骤演示了程序化缓存创建。

1. 要创建缓存：

   1. 向`gemfire.properties`或`gfsecurity.properties`文件添加必要的安全属性，以配置您的特定安全实现。 例子：

      ```
      security-client-auth-init=mySecurity.UserPasswordAuthInit.create
      ```

      ```
      security-peer-auth-init=myAuthPkg.myAuthInitImpl.create
      ```

   2. 创建缓存时，请使用以下方法之一将安全实现所需的任何属性传递给缓存工厂创建调用：

      - `ClientCacheFactory` or `CacheFactory` `set` methods. Example:

        ```
        ClientCache clientCache = new ClientCacheFactory()
            .set("security-username", username)
            .set("security-password", password)
            .create();
        ```

      - 传递给`ClientCacheFactory`或`CacheFactory``create`方法的Properties对象。 这些通常是敏感性质的属性，您不希望将它们放在`gfsecurity.properties`文件中。 例：

        ```
        Properties properties = new Properties();
        properties.setProperty("security-username", username);
        properties.setProperty("security-password", password);
        Cache cache = new CacheFactory(properties).create();
        ```

        **注意:** 传递给缓存创建方法的属性会覆盖`gemfire.properties`文件或`gfsecurity.properties`中的任何设置。

2. 完成后关闭缓存，使用`ClientCache`实例的`close`方法或`Cache`实例的继承`close`方法。 例：

   ```
   cache.close();
   ```


### 管理多个安全用户的RegionServices

在安全系统中，您可以通过与每个客户端的服务器建立多个安全连接来创建客户端。 最常见的用例是嵌入在应用服务器中的Geode客户端，该服务器支持来自许多用户的数据请求。 可以授权每个用户访问服务器上的数据子集。 例如，可以允许客户用户仅查看和更新他们自己的订单和货件。



在单个客户端中，多个经过身份验证的用户都可以通过`RegionService`接口的实例访问相同的`ClientCache`。 由于有多个用户具有不同的授权级别，因此对缓存数据的访问完全通过服务器完成，其中可以管理每个用户的授权。附加得步骤请参照 [管理安全系统中的缓存](http://geode.apache.org/docs/guide/17/basic_config/the_cache/managing_a_secure_cache.html#managing_a_secure_cache).

1. 创建缓存和`RegionService`实例：

   1. 配置客户端的服务器池以进行多个安全用户身份验证。 例：

      ```
      <pool name="serverPool" multiuser-authentication="true">
          <locator host="host1" port="44444"/>
          </pool>
      ```

      这样就可以通过池访问`RegionService`实例，并为`ClientCache`实例禁用它。

   2. 在您的`ClientCache`实例创建`ClientCache`之后，为每个用户调用`createAuthenticatedView`方法，提供用户的特定凭据。 这些是两个用户的create方法调用：

      ```
      Properties properties = new Properties();
      properties.setProperty("security-username", cust1Name);
      properties.setProperty("security-password", cust1Pwd);
      RegionService regionService1 = 
          clientCache.createAuthenticatedView(properties);
      
      properties = new Properties();
      properties.setProperty("security-username", cust2Name);
      properties.setProperty("security-password", cust2Pwd);
      RegionService regionService2 =  
          clientCache.createAuthenticatedView(properties);
      ```

   对于每个用户，通过指定的`RegionService`instance完成所有缓存和区域的工作。 对服务器缓存的访问将受服务器为每个用户配置的授权规则的约束。

2. 通过仅关闭“ClientCache”实例来关闭缓存。 不要先关闭`RegionService`实例。 这是持久的客户来说尤其重要。

**`RegionService`的要求和注意事项**

创建每个区域后，您可以通过`ClientCache`实例或`RegionService`实例对其执行操作，但不能同时对它们执行操作。

**注意:** 您可以使用`ClientCache`创建一个区域，该区域使用为多用户身份验证配置的池，然后使用您的`RegionService`实例访问并对该区域进行操作。

要使用`RegionService`，必须将区域配置为`EMPTY`。 根据您的数据访问要求，此配置可能会影响性能，因为客户端会在每次获取时转到服务器。



### 初始化缓存后启动应用程序

您可以指定在缓存初始化后启动的回调应用程序。

通过在cache.xml文件中指定`<initializer>`元素，可以触发回调应用程序，该应用程序在初始化缓存后运行。 使用cacheserver脚本启动服务器的应用程序也可以使用此功能挂钩到回调应用程序。 要使用此功能，需要在`<initializer>`元素中指定回调类。 应该将此元素添加到`cache.xml`文件的末尾。

您可以为服务器缓存或客户端缓存指定`<initializer>`元素。

回调类必须实现`Declarable`接口。 加载回调类时，调用其`init`方法，并将`<initializer>`元素中定义的任何参数作为属性传递。

以下是示例规范。

在cache.xml中：

```
<initializer>
   <class-name>MyInitializer</class-name>
      <parameter name="members">
         <string>2</string>
      </parameter>
</initializer>
```

这是相应的类定义：

```
import org.apache.geode.cache.Declarable;

public class MyInitializer implements Declarable {
   public void init(Properties properties) {
      System.out.println(properties.getProperty("members"));
   }
}
```

以下是一些其他实际使用场景：

1. 启动`SystemMembershipListener`

   ```
   <initializer>
      <class-name>TestSystemMembershipListener</class-name>
   </initializer>
   ```

2. 编写一个监视缓存资源的自定义工具

   ```
   <initializer>
      <class-name>ResourceMonitorCacheXmlLoader</class-name>
   </initializer>
   ```

可以使用initializer元素实例化和启动任何单例或计时器任务或线程。



## 数据区域

该区域是Apache Geode集群的核心构建块。 所有缓存的数据都组织到数据区域中，您可以针对它们执行所有数据的放置，获取和查询活动。

- **区域管理**

  Apache Geode提供gfsh命令，API和XML配置模型，以支持数据区域的配置和管理。

- **区域命名**

  为了能够对您的数据区域执行所有可用操作，请遵循这些区域命名准则。

- **区域快捷方式和自定义命名区域属性**

  Geode提供区域快捷方式设置，其中包含最常见区域类型的预设区域配置。 对于最简单的配置，请从快捷方式设置开始，并根据需要进行自定义。 您还可以将自己的自定义配置存储在缓存中以供多个区域使用。

- **存储和检索区域快捷方式和自定义命名区域属性**

  使用这些示例开始使用Geode区域快捷方式。

- **管理区域属性**

  使用区域属性可以微调区域快捷方式设置提供的区域配置。

- **为区域和条目创建自定义属性**

  使用自定义属性可以在缓存中存储与您的区域或其条目相关的信息。 这些属性仅对本地应用程序可见，并且不会分发。

- **用现有内容构建新区域**

  可能需要使用现有系统的数据加载新的区域或集群。 有两种方法可以完成此任务。 使用的方法取决于新集群和现有集群的组织。

### 区域管理

创建，销毁，无效，清除和更改区域配置的操作可以使用gfsh命令，XML描述和API调用。

将数据存储在区域条目键/值对中，键和值是应用程序所需的任何对象类型。 `org.apache.geode.cache.Region`接口实现了`java.util.Map`。

每个区域的属性定义如何存储，分发和管理区域中的数据。 数据区域可以在系统成员之间分配，分区，也可以在成员本地分配。

*区域快捷方式* 识别常用的区域类型。 有关详细信息，请参阅[区域快捷方式](http://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_regionshortcuts.html)。

**注意:** 如果更改定义区域的属性，则必须重新启动成员才能使更改生效。

**创建区域**

**使用gfsh创建区域**

在Apache Geode缓存中创建数据区域的一种简单快捷的方法是使用`gfsh`command-line工具。

区域创建受属性一致性检查的约束，这两者都在缓存内部，如果区域不是本地，则在定义区域的所有缓存之间。

`gfsh create region` [命令参考页](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html)详细说明了使用创建区域的命令行选项`gfsh`。

将`gfsh`连接到JMX服务器时，创建复制区域的示例命令是

```
gfsh>create region --name=region1 --type=REPLICATE
```

导出服务器的配置文件，以便在下次启动缓存服务器时保存区域的配置并重新创建具有相同属性的区域。 有关详细信息，请参阅[export config](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_C7C69306F93743459E65D46537F4A1EE)。

**注意:** 默认情况下启用的集群配置服务会自动将配置保存在集群中的定位器上。 使用`gfsh create region`命令后，您启动的任何连接到同一定位器的新服务器都会收到相同的配置。 您还可以通过在创建区域和启动服务器时指定组来在集群中创建备用配置。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。

**通过cache.xml文件创建区域**

在Apache Geode缓存中创建数据区域的常用方法是通过`cache.xml`声明。 使用`cache.xml`文件启动成员时，将创建该区域。

区域创建受属性一致性检查的约束，这两者都在缓存内部，如果区域不是本地，则在定义区域的所有缓存之间。

- 在`cache.xml`文件中，为新区域创建一个`<region>`元素作为`<cache>`元素或`<client-cache>`元素的子元素。
- 定义区域名称并使用区域快捷方式（如果适用）。
- 根据需要添加其他属性以自定义区域的行为。

**cache.xml文件示例**

名为Portfolios的复制区域的`region`声明：

```
<region name="Portfolios" refid="REPLICATE"/>
```

名为myRegion的分区区域的`region`声明：

```
<region name="myRegion" refid="PARTITION"/>
```

将内容备份到磁盘的分区区域的`region`声明：

```
<region name="myRegion" refid="PARTITION_PERSISTENT"/>
```

在区域中配置了高可用性和修改后的存储容量的分区区域的`region`声明：

```
<region name="myRegion" refid="PARTITION_REDUNDANT">
    <region-attributes>
        <partition-attributes local-max-memory="512" />
    </region-attributes>
</region>
```

复制区域的`region`声明，配置了一个事件监听器，其中条目到期：

```
<region name="myRegion" refid="REPLICATE">
    <region-attributes statistics-enabled="true">
        <entry-time-to-live>
            <expiration-attributes timeout="60" action="destroy"/>
        </entry-time-to-live>
        <cache-listener>
            <class-name>myPackage.MyCacheListener</class-name>
        </cache-listener>
    </region-attributes>
</region>
```

**通过API创建区域**

Geode的区域API为不同的系统成员类型提供专门的行为。

- `对等/服务器` 区域API

  . 使用这些方法，接口和类来创建`对等/服务器`区域。 这些都在

   

  ```
  org.apache.geode.cache
  ```

   

  package. 它们对应于

   

  ```
  cache.xml
  ```

  内部的声明

   

  ```
  <cache>
  ```

   

  用于创建和配置区域的元素。

  - **org.apache.geode.cache.Cache.createRegionFactory** . 这个方法采用`RegionShortcut``enum`来启动区域配置，并返回一个`RegionFactory`。 使用`createRegionFactory()`而不是`new RegionFactory`来创建RegionFactory。
  - **org.apache.geode.cache.RegionFactory**. 提供设置单个区域属性和创建区域的方法。 `create`调用返回一个`Region`。
  - **org.apache.geode.cache.RegionShortcut**. 定义公共区域配置。

- **Client Region APIs**. 使用这些方法，接口和类来创建客户端区域。 它们位于`org.apache.geode.cache.client`包中。 它们对应于`<client-cache>`元素中的`cache.xml`声明，用于创建和配置区域。

  这些是对等/服务器区域API的客户端版本。 这些客户端API提供类似的功能，但是根据客户区域的需求和行为进行定制。

  - **org.apache.geode.cache.clientCache.createRegionFactory** . 这个方法使用`ClientRegionShortcut``enum`来启动区域配置，并返回一个`ClientRegionFactory`。
  - **org.apache.geode.cache.client.ClientRegionFactory**. 提供设置单个区域属性和创建区域的方法。 `create`调用返回`Region`。
  - **org.apache.geode.cache.client.ClientRegionShortcut** . 定义公共区域配置。

- **Region APIs Used For All Member Types**. 这些接口和类通常用于区域管理。 它们位于`org.apache.geode.cache`包中。 它们对应于`<cache>`和`<client-cache>`元素中的`cache.xml`声明，用于创建和配置区域。

  - **org.apache.geode.cache.Region** . 用于管理区域及其条目的接口。
  - **org.apache.geode.cache.RegionAttributes** . 对象保持区域配置设置。

使用API在启动后在缓存中创建区域。 对于运行时区域创建，您需要使用API。

区域创建受属性一致性检查的约束，这两者都在缓存内部，如果区域不是本地，则在定义区域的所有缓存之间。

1. 使用区域快捷方式创建区域工厂。
   - 在对等体和服务器中，使用`org.apache.geode.cache.RegionFactory`。
   - 在客户端中，使用`org.apache.geode.cache.client.ClientRegionFactory`。
2. (可选的) 使用区域工厂进一步配置您的区域。
3. 从配置的区域工厂创建您的区域。

**API 例子**

创建名为Portfolios的复制区域：

```
Cache cache = CacheFactory.create();
RegionFactory rf = cache.createRegionFactory(REPLICATE);
Region pfloRegion = rf.create("Portfolios");
```

使用侦听器创建分区区域：

```
RegionFactory rf =   
    cache.createRegionFactory(RegionShortcut.PARTITION);
rf.addCacheListener(new LoggingCacheListener());
custRegion = rf.create("customer");
```

创建一个分区区域，其中包含用于共处区域的分区解析程序：

```
PartitionAttributesFactory paf = new PartitionAttributesFactory<CustomerId, String>();
paf.setPartitionResolver(new CustomerOrderResolver());

RegionFactory rf = 
    cache.createRegionFactory(RegionShortcut.PARTITION);
rf.setPartitionAttributes(paf.create());
rf.addCacheListener(new LoggingCacheListener());
custRegion = rf.create("customer");
```

使用池规范创建客户区域：

```
ClientRegionFactory<String,String> cRegionFactory = 
    cache.createClientRegionFactory(PROXY);
Region<String, String> region = 
    cRegionFactory.setPoolName("Pool3").create("DATA");
```

**创建和访问数据子区域**

单个区域可以包含多个子区域。 子区域是一项较旧的功能，在新设计和应用程序中无用。 它们用于在缓存中创建分层命名空间，提供与文件系统中的路径类似的命名。 以下是对次区域使用的限制：

- 具有LOCAL范围的区域只能具有LOCAL范围的子区域。
- 分区区域类型不能与子区域一起使用。 子区域可能没有作为分区区域的父级，子区域可能不是PARTITION类型。
- 子区域必须具有与其父区域相同的范围（GLOBAL，DISTRIBUTED_ACK，DISTRIBUTED_NO_ACK）。
- 子区域名称在缓存中必须是唯一的。

您可以使用以下方法之一创建子区域：

- `cache.xml`中的声明：

  ```
  <?xml version="1.0"?>
  <cache
      xmlns="http://geode.apache.org/schema/cache"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://geode.apache.org/schema/cache http://geode.apache.org/schema/cache/cache-1.0.xsd"
      version="1.0"
      lock-lease="120"
      lock-timeout="60"
      search-timeout="300">
  <!-- Create a region named Portfolios -->
    <region name="Portfolios" refid="REPLICATE">
        <region name="Private" refid="REPLICATE">
        ...
        </region>
    </region>
  </cache>
  ```

  在创建缓存时加载`cache.xml`时，系统会自动创建任何声明的区域和子区域。

- `RegionFactory` API调用：

  ```
  Cache cache = CacheFactory.create();
  RegionFactory rf = cache.createRegionFactory(REPLICATE);
  Region pfloRegion = rf.create("Portfolios");
  Region pvtSubregion = rf.createSubregion(pfloRegion, "Private");
  ```

带有`recursive`参数的`Region`方法调用对给定的区域进行操作，然后对所有包含的子区域进行递归操作。

**更新数据区域的配置**

通过`alter region`命令，API或`cache.xml`文件声明更新您的区域属性和内容。

- 使用[gfsh alter region](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_E74ED23CB60342538B2175C326E7D758)命令。
- 在API中，使用`Cache`和`Region`方法来更改配置参数并修改区域结构和数据。
- 使用`Cache.loadCacheXml`方法加载新的XML声明。 在可能的情况下，新的`cache.xml`文件中的声明将取代现有的定义。 例如，如果在`cache.xml`文件中声明的区域已经存在于缓存中，则根据文件声明修改其可变属性。 不可变属性不受影响。 如果某个区域尚不存在，则会创建该区域。 根据缓存状态和文件声明创建或更新条目和索引。

**使区域无效**

无效区域操作将删除区域的所有条目值，同时保持条目密钥不变。 只能通过“Region”实例上的API调用此操作。 发生事件通知。

```
// Invalidate the entire distributed region 
Region.invalidateRegion(); 
```

API还提供了一种方法，仅使本地缓存中的条目无效。 此方法可能不会在复制区域上使用，因为这样做会使复制协定无效。

```
// Invalidate the region within this member
Region.localInvalidateRegion(); 
```

**清除区域**

清除区域操作将删除区域中的所有条目。 此操作不适用于分区区域。 可以通过`Region`实例上的API调用此操作：

```
// Remove all entries for the region
Region.clear(); 
```

它可以使用`gfsh`命令调用：

```
gfsh>remove --region=Region1 --all 
```

清除区域操作发生事件通知。

**销毁区域**

销毁区域操作移除整个区域。 可以通过“Region”实例上的API调用此操作：

```
// Remove the entire region
Region.destroyRegion();
```

可以使用`gfsh`命令调用destroy区域操作：

```
gfsh>destroy region --name=Region1
```

销毁区域操作发生事件通知。

可以通过从`cache.xml`文件中删除区域的规范来销毁区域。

在所有成员联机时通过API调用或使用`gfsh destroy`命令销毁区域是删除区域的最佳方式，因为Geode处理删除的所有方面，包括删除区域的在线成员的持久磁盘存储 主办该地区。 通过从`cache.xml`文件中删除其规范来销毁该区域不会删除该区域的现有永久磁盘存储。

销毁操作只能传播给在线成员。 如果某个区域在线并且其他成员处于脱机状态时，系统将遇到重启问题。 作为脱机重启的成员，它们将无限期地阻塞，等待不再存在的持久区域数据。 要解决此问题，请关闭所有被阻止等待删除区域的成员。 一旦这些成员处于脱机状态，使用`gfsh alter disk-store`命令和每个脱机成员上的`--remove`选项来删除该区域。 然后，重新启动每个成员。

边缘情况在通过从`cache.xml`文件中删除其规范来销毁持久区域（R-removed）时导致问题，并且区域R-removed与另一个持久区域（R-remain）共存。 出现此问题是因为R-remaining中包含的持久性信息与R-removed的（缺乏）规范不一致。 重新启动R-remain后，其持久化元数据将R-remove视为共置区域，R-remaining的启动依赖于已删除的区域。 因此，R-started的启动仍然无法完成。 该问题可能表现为R-still区域上的操作，例如查询，放置或获取，从未完成。 要解决此问题，请使用引用已删除区域的持久元数据关闭所有成员。 一旦这些成员处于脱机状态，使用`gfsh alter disk-store`命令和每个脱机成员上的`--remove`选项删除该区域。 然后，重新启动每个成员。

**关闭区域**

使用此选项可以在不关闭整个缓存的情况下停止持久和分区区域的本地缓存：

```
Region.close();
```

`Region.close`操作就像`Region.localDestroyRegion`操作一样，具有以下显着差异：

- 为区域上安装的每个回调调用`close`方法。
- 没有调用任何事件。 特别值得注意的是，不调用入口事件`beforeDestroy`和`afterDestroy`，以及区域事件`beforeRegionDestroy`和`afterRegionDestroy`。 请参阅[事件和事件处理](http://geode.apache.org/docs/guide/17/developing/events/chapter_overview.html#implementing_event_handlers)。
- 如果持久，则从内存中删除该区域，但保留其磁盘文件。
- 如果已分区，则从本地缓存中删除该区域。 如果分区区域是冗余的，则本地数据缓存会故障转移到另一个缓存。 否则，本地数据将丢失。

### 区域命名

为了能够对您的数据区域执行所有可用操作，请遵循这些区域命名准则。

- 区域名称中允许的字符是字母数字字符（`ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789`），下划线字符（`_`）和连字符（`-`）。
- 不要使用斜杠字符（`/`）。
- 不要使用两个下划线字符（`__`）开始区域名称，因为这是为内部使用而保留的。



### 区域快捷方式和自定义命名区域属性

Geode提供区域快捷方式设置，其中包含最常见区域类型的预设区域配置。 对于最简单的配置，请从快捷方式设置开始，并根据需要进行自定义。 您还可以将自己的自定义配置存储在缓存中以供多个区域使用。

您可以通过区域快捷方式和区域属性配置数据区域及其条目的自动管理。 这些区域配置设置确定数据所在的位置，内存中如何管理区域，可靠性行为以及数据条目的自动加载，分发和到期等内容。

**注意:** 尽可能使用区域快捷方式配置您的区域，并使用区域属性进一步自定义行为。 快捷方式设置使用最常见的区域配置进行预设。

Geode提供了许多预定义的快捷方式区域属性设置供您使用。 您还可以定义自己的自定义区域属性，并使用标识符存储它们以供以后检索。 两种类型的存储属性都称为命名区域属性。 您可以在`cache.xml`文件中通过API创建和存储属性设置。

通过在`refid`属性设置中为区域创建提供ID来检索区域快捷方式和自定义命名属性。 此示例使用快捷方式REPLICATE属性创建区域：

```
<region name="testREP" refid="REPLICATE"/>
```

您可以根据需要通过在区域属性声明中提供`id`来创建自己的命名属性。 以下区域声明：

1. 检索持久分区区域快捷方式提供的所有属性设置

2. 通过指定用于持久性的磁盘存储名称来修改快捷方式属性设置

3. 将新属性设置分配给名为`testPR`的新区域

4. 将属性设置存储在名为`testPRPersist`的新自定义属性中：

   ```
   <disk-store name="testDiskStore" >
       <disk-dirs>
           <disk-dir>PRPersist1</disk-dir>
           <disk-dir>PRPersist2</disk-dir>
       </disk-dirs>
   </disk-store>
   <region name="testPR" >
       <region-attributes id="testPRPersist"
           refid="PARTITION_PERSISTENT" disk-store-name="testDiskStore"/>
   </region>
   ```

**快捷方式属性选项**

您可以从Geode在这些类中预定义的命名区域属性中选择最常用的区域属性设置：

- **org.apache.geode.cache.RegionShortcut**. 对于对等和服务器。
- **org.apache.geode.cache.client.ClientRegionShortcut**. 对于客户端。

快捷方式属性仅为方便起见。 它们只是Geode已经为您存储的命名属性。 您可以通过存储与预定义属性具有相同ID的新属性来覆盖其设置。

有关所有可用区域快捷方式的完整列表，请参阅[区域快捷方式快速参考](http://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_table.html#reference_ufj_5kz_4k)。

`org.apache.geode.cache.RegionShortcut` Javadocs提供了完整的选项列表。

**对等和服务器的`RegionShortcuts`**

这些是区域快捷方式设置中可用的主要选项。 列出的名称单独或组合显示在快捷方式标识符中，如“PARTITION”，“PARTITION_PROXY”和“PARTITION_REDUNDANT”中的“`PARTITION`”。

**缓存数据存储模式**

- `PARTITION`

   

  . 创建分区区域。 这是该地区的数据存储。 您也可以使用指定这些选项

   

  ```
  PARTITION
  ```

  :

  - **PROXY**. 数据不存储在本地缓存中，成员是该区域的数据访问者。 这需要其他成员创建该区域的非代理副本，因此数据存储在某处。
  - **REDUNDANT**. 该区域存储所有数据的辅助副本，以实现高可用性。

- `REPLICATE`

  . 创建复制区域。 这是该地区的数据存储。 您也可以使用指定这些选项

   

  ```
  REPLICATE
  ```

  :

  - **PROXY**. 数据不存储在本地缓存中，成员是该区域的数据访问者。 这需要其他成员创建该区域的非代理副本，因此数据存储在某处。

- **LOCAL**. 创建一个专用于定义成员的区域。

**Data Eviction**

- **HEAP_LRU**.当Geode资源管理器确定缓存已达到配置的存储限制时，导致最近最少使用的数据从内存中逐出。

**Disk Storage**

您可以单独或组合指定这些：

- **PERSISTENT**. 除了将所有数据存储在内存中之外，还将所有数据备份到磁盘。
- **OVERFLOW**. 当内存使用率过高时，将数据从内存中移出并移至磁盘上。

**客户端的`ClientRegionShortcuts`**

这些是客户端区域快捷方式设置中可用的主要选项。 列出的名称单独或组合出现在快捷方式标识符中，如“PROXY”和“CACHING_PROXY”中的“PROXY”。

**与服务器和数据存储的通信**

- **PROXY**. 不将数据存储在客户端缓存中，而是将区域连接到服务器以进行数据请求和更新，兴趣注册等。 客户端是该地区的数据访问者。
- **CACHING_PROXY**. 将数据存储在客户端缓存中，并将该区域连接到服务器以进行数据请求和更新，兴趣注册等。
- **LOCAL**. 将数据存储在客户端缓存中，并且不将该区域连接到服务器。 这是一个仅限客户端的区域。 请注意，这与将区域的`scope`属性设置为`LOCAL`不同。

**Data Eviction**

- **HEAP_LRU**. 当Geode资源管理器确定缓存已达到配置的存储限制时，导致最近最少使用的数据从内存中逐出。

**Disk Storage**

使用LOCAL和CACHING数据存储快捷方式选项，您还可以单独或组合指定这些磁盘存储选项：

- **PERSISTENT**. 除了将所有数据存储在内存中之外，还将所有数据备份到磁盘。
- **OVERFLOW**. 当内存使用率过高时，将数据从内存中移出并移至磁盘上。



### 存储和检索区域快捷方式和自定义命名区域属性

使用这些示例开始使用Geode区域快捷方式。

对于对等方和服务器的`org.apache.geode.cache.RegionShortcut`和用于客户端的`org.apache.geode.cache.client.ClientRegionShortcut`的Geode区域快捷方式，无论您在`cache.xml`中创建区域，都可以使用或通过API。 您存储的自定义命名属性从您存储它们的那一刻起就可用。

区域快捷方式是特殊的Geode命名区域属性，具有标识名称。 通过设置属性并使用区域属性`id`中的唯一标识符存储属性来创建自定义命名区域属性。 通过提供快捷枚举值或您在`id`中为区域创建指定的名称来检索命名属性：

- 在API中，使用区域工厂创建中的标识符
- 在`cache.xml`中，使用`<region>`或`<region-attribute>``refid`设置中的标识符。 为方便起见，`refid`在两个元素中都可用

**例子**

**Example #1**

此示例显示`cache.xml`中的分区区域创建：

- 第一个`region-attributes`声明以预定义的`PARTITION_REDUNDANT`属性开头，修改`local-max-memory`设置，并将结果属性存储在自定义命名的`myPartition`属性中。

- 区域声明使用新存储的属性，但每个属性都有自己的兴趣策略，该策略在单个区域创建中指定。

  ```XML
  <!-- Retrieving and storing attributes -->
  <region-attributes id="myPartition" refid="PARTITION_REDUNDANT">
      <partition-attributes local-max-memory="512"/>
  </region-attributes>
  
  <!-- Two partitioned regions, one colocated with the other -->
  
  <!-- Attributes are retrieved and applied in the first region -->
  <region name="PartitionedRegion1" refid="myPartition"/>
  
  <!-- Same stored attributes, modification for this region-->
  <region name="PartitionedRegion2" refid="myPartition">
      <region-attributes>
          <partition-attributes colocated-with="PartitionedRegion1" />
      </region-attributes>
  </region>
  ```

**Example #2**

这个例子使用`RegionFactory` API根据预定义的`PARTITION`区域快捷方式创建一个区域：

```
final Region diskPortfolios = 
    new RegionFactory("PARTITION").create("Portfolios");
```

此示例检索属性模板，并使用修改的池规范将其传递给区域创建：

```java
ClientRegionFactory<String,String> regionFactory =         
    cache.createClientRegionFactory(PROXY);
Region<String, String> region = regionFactory
    .setPoolName("publisher")
    .create("DATA");
```



### 管理区域属性

使用区域属性可以微调区域快捷方式设置提供的区域配置。

所有区域属性都具有默认设置，因此您只需使用区域属性来设置要覆盖的属性。 见[](http://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes)。

**定义区域属性**

使用以下任一方法创建区域属性：

- `cache.xml`` <region>`元素中的声明：

  ```xml
  <cache>
     <region name="exampleRegion" refid="REPLICATE">
        <region-attributes statistics-enabled="true">
          <entry-idle-time>
            <expiration-attributes timeout="10" action="destroy"/>
          </entry-idle-time>
          <cache-listener>
            <class-name>quickstart.SimpleCacheListener</class-name>
          </cache-listener>
        </region-attributes>
      </region>
  </cache>
  ```

  在启动时加载`cache.xml`时，会自动创建声明的区域属性并将其应用于该区域。

- `RegionFactory` API`set` *方法调用：

  ```java
  // Creating a partitioned region using the RegionFactory
  RegionFactory rf = cache.createRegionFactory(RegionShortcut.PARTITION);
  rf.addCacheListener(new LoggingCacheListener());
  custRegion = rf.create("customer");
  ```

  ```java
  // Creating a partitioned region using the RegionFactory, with attribute modifications
  RegionFactory rf = 
    cache.createRegionFactory(RegionShortcut.PARTITION);
  rf.setPartitionResolver(new CustomerOrderResolver());
  rf.addCacheListener(new LoggingCacheListener());
  custRegion = rf.create("customer");
  ```

  ```java
  // Creating a client with a Pool Specification Using ClientRegionFactory
  ClientRegionFactory<String,String> cRegionFactory = 
      cache.createClientRegionFactory(PROXY);
  Region<String, String> region = 
      cRegionFactory.setPoolName("Pool3").create("DATA");
  ```

- 通过发出gfsh`create region`命令。

**修改区域属性**

您可以在创建区域后修改区域的事件处理程序以及到期和逐出属性。

**注意:** 除非绝对必要，否则不要修改现有区域的属性。 在区域创建时创建所需的属性更有效。

以下列方式之一修改属性：

- 通过加载带有修改的区域属性规范的`cache.xml`：

  ```xml
  <!-- Change the listener for exampleRegion
  ...
      <region name="exampleRegion">
        <region-attributes statistics-enabled="true">
          <cache-listener>
            <class-name>quickstart.ComplicatedCacheListener</class-name>
          </cache-listener>
        </region-attributes>
      </region>
  ... 
  ```

- 使用`AttributesMutator` API：

  1. 从区域中检索`AttributesMutator`
  2. 调用mutator set方法来修改属性：

  ```java
  currRegion = cache.getRegion("root");
  AttributesMutator mutator = this.currRegion.getAttributesMutator();
  mutator.addCacheListener(new LoggingCacheListener()); 
  ```

- 通过发出gfsh`alter region`命令。 参见[alter region](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_E74ED23CB60342538B2175C326E7D758)。



### 为区域和条目创建自定义属性

使用自定义属性可以在缓存中存储与您的区域或其条目相关的信息。 这些属性仅对本地应用程序可见，并且不会分发。

您可以定义自定义用户属性，以便可以将数据与区域或条目相关联，并在以后检索它。 与其他配置设置不同，这些属性仅供您的应用程序使用。

**注意:** 用户属性不是分布式的。

1. 使用属性定义创建Java`Object`。
2. 将对象附加到区域或条目：
   - `Region.setUserAttribute(userAttributeObject)`
   - `Region.getEntry(key).setUserAttribute(userAttributeObject)`
3. 获取属性值：
   - `Region.getUserAttribute()`
   - `Region.getEntry(key).getUserAttribute()`

此示例存储属性以供稍后由缓存编写器检索。

```
// Attach a user attribute to a Region with database info for table portfolio
Object myAttribute = "portfolio"; 
final Region portfolios = 
      new RegionFactory().setCacheWriter(new PortfolioDBWriter()).create("Portfolios"); 
Portfolios.setUserAttribute(myAttribute);
//Implement a cache writer that reads the user attribute setting
public class PortfolioDBWriter extends CacheWriterAdapter {
  public void beforeCreate(RegionEvent event) {
    table = (String)event.getRegion().getUserAttribute();
    // update database table using name from attribute
        . . .
  }
}
```

**限制和替代方案**

用户属性不会分发给其他进程，因此如果需要在使用该区域或条目的每个进程中定义每个属性。 您需要单独更新该区域的每个实例。 用户属性不会存储到磁盘以进行区域持久性或溢出，因此无法恢复它们以重新初始化该区域。

如果您的应用程序需要用户属性不支持的功能，则可以选择创建一个单独的区域来保存此数据。 例如，您定义的区域AttributesRegion可以使用区域名称作为键，使用用户属性作为值。 对AttributesRegion的更改将分发给其他进程，您可以根据需要配置该区域的持久性或溢出。



### 用现有内容构建新区域

可能需要使用现有系统的数据加载新的区域或集群。 有两种方法可以完成此任务。 使用的方法取决于新集群和现有集群的组织。

如果新集群和现有集群中成员的数量和类型相同，则最简单的选项是对永久磁盘存储内容使用备份和还原。 在现有集群的磁盘存储中对持久数据进行完全联机备份。 将组成备份的文件复制到新的集群位置。 还原将数据注入新集群。有关如何进行备份以及使用备份还原磁盘存储的详细信息，请参见[创建系统恢复和操作管理备份](http://geode.apache.org/docs/guide/17/managing/disk_storage/backup_restore_disk_store.html)。

当新成员和现有集群中成员的数量或类型不相同时，采用不同的方法。 此方法使用区域数据的导出和导入。 导出现有集群的区域数据以创建快照。 将快照复制到新的集群位置。 将快照导入新集群。有关制作和使用快照的详细信息，请参阅[缓存和区域快照](http://geode.apache.org/docs/guide/17/managing/cache_snapshots/chapter_overview.html)中的相应部分。



## 数据条目

数据条目是存储数据的键/值对。 您可以单独和批量管理您的条目。 要将域对象用于条目值和键，您需要遵循Apache Geode要求进行数据存储和分发。

- **管理数据条目**

  编写应用程序以创建，修改和管理缓存的数据条目。

- **在数据缓存中使用自定义类的要求**

  请遵循以下准则，为缓存的条目键和值使用自定义域类。



### 管理数据条目

编写应用程序以创建，修改和管理缓存的数据条目。

**注意:** 如果没有将缓存的`copy-on-read`属性设置为true，请不要更改从Java条目访问方法返回的对象。 而是创建对象的副本，然后修改副本并将其传递给Java`put`方法。 修改适当的值会绕过Geode提供的整个分发框架，包括缓存侦听器和到期活动，并可能产生不希望的结果。

**基本创建和更新**

要在缓存中创建或更新条目，请使用“Region.put”。 例如：

```
String name = ... 
String value = ...  
this.currRegion.put(name,value); 
```

**注意:** 您还可以使用`gfsh put`命令向区域添加条目，使用`get`命令从区域中检索条目。 见[get](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/get.html)和[put](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/put.html)了解更多信息。

如果只想创建条目（如果条目已存在，则为空值且方法失败），请改用`Region.create`。

**批处理操作(getAll，putAll，removeAll)**

Geode提供了三个API来对多个区域条目执行批处理操作：

- `Region.getAll`
- `Region.putAll`
- `Region.removeAll`

`getAll`方法获取一组键并返回所提供键的值的“Map”。 如果区域中不存在给定键，则返回映射中该键的值将为null。

`putAll`方法接受键值对的`Map`并将它们放入缓存中并在单个操作中分发它们。

**例子:**

```java
void putAll(String command) throws CacheException 
{ 
// Get Entry keys and values into Strings key1, ... keyN and value1, ... valueN 
  Map map = new LinkedHashMap(); 
  map.put(key1, value1)); 
  ...
  map.put(keyN, valueN));
  this.currRegion.putAll(map); 
}
```

缓存的更新按照它们放置在“Map”中的顺序单独完成。 对于分区区域，多个事件作为单个消息发送到主存储桶，然后分发到辅助存储桶。

**注意:** 处理具有很多条目`和/或`非常大的数据的映射可能会影响系统性能并导致缓存更新超时，尤其是在该区域对磁盘使用溢出或持久性的情况下。

`removeAll`方法获取一组键，并从该区域中删除指定键的所有条目。 对于指定集合中的每个键，此调用对此区域执行一次调用`destroy（Object）`。 如果条目不存在，则跳过该键。 不抛出`EntryNotFoundException`。 如果区域的范围未设置为`Scope.LOCAL`，则此操作将分发给其他缓存。

**安全条目修改**

从缓存中获取条目值时，默认情况下，检索方法会返回对缓存对象的直接引用。 这样可以尽快提供值，但也会打开缓存以进行直接的就地更改。

**注意:** 不要直接修改缓存的值。 修改适当的值会绕过Geode分发框架，包括缓存编写器和侦听器，到期活动和事务管理，并可能产生不希望的结果。

始终使用检索到的对象的副本更改条目 - 永远不要直接修改返回的对象。 您可以通过以下两种方式之一完成此操作：

1. 通过将缓存属性`copy-on-read`设置为true(默认值为false)来更改缓存的条目检索行为。

   ```
   <cache copy-on-read="true">
    ...
   </cache>
   ```

   当`copy-on-read`为真时，条目访问方法返回条目的副本。 这可以防止您无意中就地修改，但在不需要复制时会对性能和内存消耗产生负面影响。

   如果`copy-on-read`为false，则这些条目访问方法返回条目引用;如果`copy-on-read`为true，则返回条目的副本：

   `Region.get` result of `Region.put` `EntryEvent.getNewValue` `Region.values` `Region.Entry.getValue` `EntryEvent.getOldValue` `Query.select`

2. 创建返回对象的副本并使用它。 对于可克隆或可序列化的对象，可以使用`org.apache.geode.CopyHelper.copy`将条目值复制到新对象。 例：

   ```java
   Object o = (StringBuffer)region.get("stringBuf");
   StringBuffer s = (StringBuffer) CopyHelper.copy(o);
   s.append("Changes to value, added using put.");
   region.put("stringBuf", s);
   ```

**从代理成员中检索区域条目**

`Region.values`方法调用仅适用于本地区域实例。 如果使用PROXY快捷方式从客户区域调用`values`方法，则方法调用将不会重定向到服务器区域。 要从客户端获取Region中所有值的集合，您应该在ALL_KEYS上使用兴趣注册，或使用查询。

如果从代理成员使用`Region.get`方法，则方法调用将重定向到服务器上的区域，如果它无法在本地找到密钥。

**使用gfsh来`get`和`put`**

您可以使用gfsh`get`和`put`命令来管理数据。 见[get](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/get.html)和[put](http://geode.apache.org/docs/引导/17/tools_modules/gfsh/命令页/put.html)。

例如：

```java
get --key=('id':'133abg124') --region=region1

// Retrieving when key type is a wrapper(primitive)/String
get --key=('133abg124') --region=/region1/region12 --value-class=data.ProfileDetails

get --key=('100L') --region=/region1/region12 --value-class=data.ProfileDetails 
--key-class=java.lang.Long
put --key=('id':'133abg125') --value=('firstname':'James','lastname':'Gosling') 
--region=/region1 --key-class=data.ProfileKey --value-class=data.ProfileDetails

put --key=('133abg124') --value=('Hello World!!') --region=/region2

put --key=('100F') --value=('2146547689879658564')  --region=/region1/region12 
--key-class=java.lang.Float --value-class=java.lang.Long
```



### 在数据缓存中使用自定义类的要求

请遵循以下准则，为缓存的条目键和值使用自定义域类。

**CLASSPATH**

每个成员的`CLASSPATH`必须包含成员访问的所有对象的类。

- 对于Java应用程序，请使用标准Java`CLASSPATH`。
- 对于缓存服务器进程，使用`CLASSPATH`环境变量或`gfsh start server`的`--classpath`参数。 请参阅[运行Geode服务器进程](http://geode.apache.org/docs/guide/17/configuring/running/running_the_cacheserver.html)。

数据以序列化形式在客户端和服务器之间发送，服务器以序列化形式存储客户端数据。 服务器不需要反序列化数据以将其发送到另一个客户端或通过`PDXInstance`访问它，但它需要反序列化它以通过其他方式访问它。 服务器`CLASSPATH`必须包含以下类：

- 所有条目键
- 服务器持久保存到磁盘的区域中的条目值
- 服务器访问的条目值，除了使用`PdxInstance`或将完整条目值传输到客户端之外的任何其他原因

有关`PdxInstance`s的信息，请参阅[数据序列化](http://geode.apache.org/docs/guide/17/developing/data_serialization/chapter_overview.html#data_serialization)。

**数据序列化**

Geode序列化数据输入键和值以进行分发，因此Geode因任何原因移出本地缓存的所有数据都必须是可序列化的。 另外，分区区域以序列化形式存储数据。 几乎每个配置都需要序列化。

有关数据序列化的要求和选项的信息，请参阅[数据序列化](http://geode.apache.org/docs/guide/17/developing/data_serialization/chapter_overview.html#data_serialization)。

**用作键的类**

该区域使用键上的散列。 如果您定义要用作键的自定义类，则对于该类，重写：

- `equals`
- `hashCode`. 从`Object`继承的默认`hashCode`使用identity，在每个系统成员中都是不同的。 在分区区域中，基于标识的散列将数据放在错误的位置。 有关详细信息，请参阅“java.lang.Object”的Java API文档。


