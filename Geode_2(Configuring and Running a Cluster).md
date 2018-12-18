# 配置和运行集群

使用`gfsh`命令行实用程序配置Apache Geode集群。 集群配置服务会保留集群配置并将配置分发给集群成员。 还有其他几种配置集群的方法。

使用`gfsh`配置区域，磁盘存储，成员和其他Geode对象。 您还可以使用`gfsh`来启动和停止定位器，服务器和Geode监视工具。 执行这些命令时，集群配置服务会保留配置。 当新成员加入集群时，该服务会将配置分发给新成员。

`gfsh`是配置和管理Apache Geode集群的推荐方法,但您仍然可以使用`cache.xml`和`gemfire.properties`文件的旧方法配置集群的许多方面. 请参阅[cache.xml](http://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_cache_xml.html#cache_xml)和[参考](http://geode.apache.org/docs /guide/17/reference/book_intro.html#reference)了解配置参数. 您还可以使用Java API配置集群的某些方面. 请参阅[管理Apache Geode](http://geode.apache.org/docs/guide/17/managing/book_intro.html#managing_gemfire_intro).

- **[集群配置服务概述](#集群配置服务概述)**

  Apache Geode集群配置服务将由`gfsh`commands创建的集群配置持久保存到集群中的定位器，并将配置分发给集群成员。

- **[教程 - 创建和使用集群配置](#教程 - 创建和使用集群配置)**

  A short walk-through that uses a single computer to demonstrate how to use `gfsh` to create a cluster configuration for a Geode cluster.

- **[将应用程序JAR部署到Apache Geode成员](#将应用程序JAR部署到Apache Geode成员)**

  您可以将应用程序JAR文件动态部署到特定成员或集群中的所有成员。 Geode自动跟踪JAR文件版本; 将已部署的JAR文件自动加载到CLASSPATH; 并自动注册JAR包含的任何函数。

- **[使用成员组](#使用成员组)**

  Apache Geode允许您将集群成员组织到逻辑成员组中。

- **[导出和导入集群配置](#导出和导入集群配置)**

  集群配置服务导出和导入使用`gfsh`为整个Apache Geode集群创建的配置。

- **[集群配置文件和故障排除](#集群配置文件和故障排除)**

  在Geode中使用集群配置服务时，可以检查定位器上`cluster_config`目录中生成的配置文件。 `gfsh`在集群级别和单个组级别保存配置文件。

- **[使用gfsh通过HTTP或HTTPS管理远程集群](#使用gfsh通过HTTP或HTTPS管理远程集群)**

  您可以通过HTTP或HTTPS将`gfsh`连接到远程集群，并使用`gfsh`命令管理集群。

- **[在没有集群配置服务的情况下部署配置文件](#在没有集群配置服务的情况下部署配置文件)**

  您可以在系统目录结构或jar文件中部署Apache Geode配置文件。 您可以确定部署配置文件的方式并相应地进行设置。

- **[启动和关闭系统](#启动和关闭系统)**

  确定正确的启动和关闭过程，并编写启动和关闭脚本。

- **[运行Geode定位器进程](#运行Geode定位器进程)**

  定位器是一个Geode进程，它告诉运行成员所在的新连接成员，并为服务器使用提供负载均衡。

- **[运行Geode服务器进程](#运行Geode服务器进程)**

  Geode服务器是一个作为客户端/服务器系统的长期可配置成员运行的进程。

- **[管理系统输出文件](#管理系统输出文件)**

  Geode输出文件是可选的，可以变得非常大。 与系统管理员一起确定放置它们的位置，以避免干扰其他系统活动。

- **[防火墙注意事项](#防火墙注意事项)**

  您可以为涉及防火墙的情况配置和限制端口使用，例如，在客户端 - 服务器或服务器 - 服务器连接之间。



## 集群配置服务概述

Apache Geode集群配置服务将由`gfsh`commands创建的集群配置持久保存到集群中的定位器，并将配置分发给集群成员。

### 为什么使用集群配置服务

我们强烈建议您使用`gfsh`命令行和集群配置服务作为管理集群配置的主要机制。 在`cache.xml`文件中指定配置，仅用于那些使用`gfsh`无法指定或更改的项。使用通用集群配置可减少在配置集群中的新成员时配置单个成员所花费的时间并实施一致的配置。 您不再需要重新配置添加到集群的每个新成员。 您不再需要担心验证`cache.xml`文件。 在整个集群中传播配置更改并将配置更改部署到不同的环境也变得更加容易。

您可以使用集群配置服务：

- 保存整个Apache Geode集群的配置。
- 使用以前保存的配置重新启动成员。
- 从开发环境导出配置并迁移该配置以创建测试或生产系统。
- 无需单独配置每个服务器即可启动其他服务器。
- 配置一些服务器以托管某些区域和其他服务器以托管不同的区域，并将所有服务器配置为托管一组公共区域。

### 使用集群配置服务

要在Geode中使用集群配置服务，必须在部署中使用专用的独立定位器。 您不能将集群配置服务与位于同一位置的定位器（在另一个进程（如服务器）中运行的定位器）或多播环境中一起使用。

独立定位器将配置分发到集群中的所有定位器。 将`--enable-cluster-configuration`设置为true的集群中的每个定位器都会记录所有集群级别和组级别配置设置。

**注意:** `gfsh`的默认行为是创建和保存集群配置。 启动定位器时，可以使用`--enable-cluster-configuration = false`选项禁用集群配置服务。

您可以使用[`gfsh import cluster-configuration`](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/import.html#topic_vnv_grz_ck )命令在启动定位器后将现有配置加载到集群中。

随后，任何以`-use-cluster-configuration`设置为`true`的`gfsh`开头的服务器将从定位器以及任何适当的组级配置中获取集群配置(对于成员组，它们 属于)。 要在服务器上禁用集群配置服务，必须使用`--use-cluster-configuration`参数设置为`false`来启动服务器。 默认情况下，该参数设置为true。

### 集群配置服务的工作原理

使用`gfsh`命令创建Apache Geode区域，磁盘存储和其他对象时，集群配置服务会保存集群中每个定位器的配置。 如果在发出这些命令时指定组，则会保存单独的配置，其中仅包含适用于该组的配置。

当您使用`gfsh`启动新的Apache Geode服务器时，定位器会将持久配置分发到新服务器。 如果在启动服务器时指定组，则除了集群级配置外，服务器还会接收组级配置。 集群级配置在集群范围配置后应用; 因此，您可以使用组级别来覆盖集群级别设置。

![img](assets/cluster_config_overview.svg)

### gfsh创建集群配置的命令

以下`gfsh`命令会将配置写入集群中的所有定位器（定位器将配置写入磁盘）：

- `configure pdx`*
- `create region`
- `alter region`
- `alter runtime`
- `destroy region`
- `create index`
- `destroy index`
- `create disk-store`
- `destroy disk-store`
- `create async-event-queue`
- `deploy jar`
- `undeploy jar`
- `create gateway-sender`
- `create gateway-receiver`

***** 请注意，必须在启动数据成员之前执行configure pdx命令。 此命令不会影响系统中当前正在运行的任何成员。 运行此命令后启动的数据成员（已启用集群配置）将获取新的PDX配置。

### gfsh限制

这些是您无法使用`gfsh`创建或更改的配置。 这些配置必须位于`cache.xml`文件中，或者使用API：

- 客户端缓存配置

- 您无法直接修改以下对象的属性：

  - `function`

  - `custom-load-probe`

  - `compressor`

  - `serializer`

  - `instantiator`

  - `pdx-serializer`

    **注意:** `configure pdx`命令总是指定`org.apache.geode.pdx.ReflectionBasedAutoSerializer`类。 您无法在gfsh中指定自定义PDX序列化程序。

  - `initializer`

  - `lru-heap-percentage`

  - `lru-memory-size`

  - `partition-resolver`

  - `partition-listener`

  - `transaction-listener`

  - `transaction-writer`

- Adding or removing a `TransactionListener`

- Deleting an `AsyncEventQueue`

- Configuring a `GatewayConflictResolver`

- 您无法为以下内容指定Java类的参数和值：

  - `gateway-listener`
  - `gateway-conflict-resolver`
  - `gateway-event-filter`
  - `gateway-transport-filter`
  - `gateway-event-substitution-filter`

### 禁用集群配置服务

如果您不想使用集群配置服务，请启动定位器，并将`--enable-cluster-configuration`参数设置为false，或者不要使用独立定位器。 然后，您需要在所有集群成员上单独配置缓存（通过cache.xml或API）。


## 教程 - 创建和使用集群配置

一个简短的演练，使用一台计算机演示如何使用`gfsh`为Geode集群创建集群配置。

`gfsh`命令行工具允许您配置和启动Geode集群。 集群配置服务使用Apache Geode定位器在组和集群级别存储配置，并在新成员启动时将这些配置提供给新成员。 定位器将配置存储在可供所有定位器使用的隐藏区域中，并将配置数据作为XML文件写入磁盘。 配置数据在执行`gfsh`命令时更新。

本节提供了配置简单Apache Geode集群，然后在新上下文中重用该配置的演练示例。

1. 创建一个工作目录(例如：`/home/username/my_geode`)并切换到新目录。 该目录将包含集群的配置。

2. 启动`gfsh`命令行工具。 例如：

   ```
   $ gfsh
   ```

   显示`gfsh`命令提示符。

   ```
       _________________________     __
      / _____/ ______/ ______/ /____/ /
     / /  __/ /___  /_____  / _____  /
    / /__/ / ____/  _____/ / /    / /
   /______/_/      /______/_/    /_/    1.7
   
   Monitor and Manage Apache Geode
   gfsh>
   ```

3. 使用以下示例中的命令启动定位器：

   ```
   gfsh>start locator --name=locator1
   Starting a Geode Locator in /Users/username/my_geode/locator1...
   .............................
   Locator in /Users/username/my_geode/locator1 on 192.0.2.0[10334] as locator1
   is currently online.
   Process ID: 5203
   Uptime: 15 seconds
   Geode Version: 1.7
   Java Version: 1.8.0_121
   Log File: /Users/username/my_geode/locator1/locator1.log
   JVM Arguments: -Dgemfire.enable-cluster-configuration=true
   -Dgemfire.load-cluster-configuration-from-dir=false
   -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
   -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
   Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
   
   Successfully connected to: [host=192.0.2.0, port=1099]
   
   Cluster configuration service is up and running.
   ```

   请注意，`gfsh`会响应一条消息，指示集群配置服务已启动并正在运行。 如果看到指示问题的消息，请查看定位器日志文件以查找可能的错误。 日志文件的路径显示在`gfsh`的输出中。

4. 使用以下示例中的命令启动Apache Geode服务器：

   ```
   gfsh>start server --name=server1 --groups=group1
   Starting a Geode Server in /Users/username/my_geode/server1...
   .....
   Server in /Users/username/my_geode/server1 on 192.0.2.0[40404] as server1
   is currently online.
   Process ID: 5627
   Uptime: 2 seconds
   Geode Version: 1.7
   Java Version: 1.8.0_121
   Log File: /Users/username/my_geode/server1/server1.log
   JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10334] -Dgemfire.groups=group1
   -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p
   -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
   -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
   Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
   
   gfsh>start server --name=server2 --groups=group1 --server-port=40405
   Starting a Geode Server in /Users/username/my_geode/server2...
   .....
   Server in /Users/username/my_geode/server2 on 192.0.2.0[40405] as server2
   is currently online.
   Process ID: 5634
   Uptime: 2 seconds
   Geode Version: 1.7
   Java Version: 1.8.0_121
   Log File: /Users/username/my_geode/server2/server2.log
   JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10334] -Dgemfire.groups=group1
   -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p
   -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
   -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
   Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
   
   gfsh>start server --name=server3 --server-port=40406
   Starting a Geode Server in /Users/username/my_geode/server3...
   .....
   Server in /Users/username/my_geode/server3 on 192.0.2.0[40406] as server3
   is currently online.
   Process ID: 5637
   Uptime: 2 seconds
   Geode Version: 1.7
   Java Version: 1.8.0_121
   Log File: /Users/username/my_geode/server3/server3.log
   JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10334]
   -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p
   -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
   -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
   Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
   ```

   请注意，用于启动`server1`和`server2`的`gfsh`命令指定了一个名为`group1`的组，而`server3`的命令没有指定组名。

5. 使用以下示例中的命令创建一些区域：

   ```
   gfsh>create region --name=region1 --groups=group1 --type=REPLICATE
   Member  | Status
   ------- | --------------------------------------
   server2 | Region "/region1" created on "server2"
   server1 | Region "/region1" created on "server1"
   
   gfsh>create region --name=region2 --type=REPLICATE
   Member  | Status
   ------- | --------------------------------------
   server1 | Region "/region2" created on "server1"
   server2 | Region "/region2" created on "server2"
   server3 | Region "/region2" created on "server3"
   ```

   请注意，在启动缓存服务器(在此示例中为`server1`和`server2`)时，在指定名为`group1`的组的所有缓存服务器上创建`region1`。 因为没有指定组，所以在所有成员上创建`region2`。

6. 部署jar文件。 使用`gfsh deploy`命令将应用程序jar文件部署到所有成员或指定的成员组。 以下示例从分发中部署`mx4j-3.0.1.jar`和`ra.jar`文件。 (注意：这只是一个示例，您不需要部署这些文件来使用集群配置服务。或者，您可以使用任何两个jar文件进行此演示。)

   ```
   gfsh>deploy --groups=group1 --jars=/lib/mx4j-3.0.1.jar
   Post substitution: deploy --groups=group1 --jars=/Users/username/Apache_Geode_1.0.0_Linux/lib/mx4j-3.0.1.jar
   Member  |    Deployed JAR    | Deployed JAR Location
   ------- | ------------------ | -------------------------------------------------------
   server1 | mx4j-3.0.1.jar     | /Users/username/my_geode/server1/vf.gf#mx4j-3.0.1.jar#1
   server2 | mx4j-3.0.1.jar     | /Users/username/my_geode/server2/vf.gf#mx4j-3.0.1.jar#1
   
   gfsh>deploy --jars=/lib/ra.jar
   Post substitution: deploy --jar=/Users/username/Apache_Geode_1.0.0_Linux/lib/ra.jar
   Member  | Deployed JAR | Deployed JAR Location
   ------- | ------------ | -----------------------------------------------
   server1 | ra.jar       | /Users/username/my_geode/server1/vf.gf#ra.jar#1
   server2 | ra.jar       | /Users/username/my_geode/server1/vf.gf#ra.jar#1
   server3 | ra.jar       | /Users/username/my_geode/server1/vf.gf#ra.jar#1
   ```

   请注意，`mx4j-3.0.1.jar`文件仅部署到`group1`的成员，而`ra.jar`部署到所有成员。

7. 导出集群配置。 您可以使用`gfsh export cluster-configuration`命令创建一个包含集群持久配置的zip文件。 zip文件包含`cluster_config`目录内容的副本。 例如：

   ```
   gfsh>export cluster-configuration --zip-file-name=/Users/username/myClConfig.zip
   ```

   Apache Geode将集群配置写入指定的zip文件。

   ```
   Downloading cluster configuration : /Users/username/myClConfig.zip
   ```

   其余步骤演示了如何使用刚刚创建的集群配置。

8. 使用以下命令关闭集群：

   ```
   gfsh>shutdown --include-locators=true
   As a lot of data in memory will be lost, including possibly events in queues, do you
   really want to shutdown the entire distributed system? (Y/n): Y
   Shutdown is triggered
   
   gfsh>
   No longer connected to 192.0.2.0[1099].
   gfsh>
   ```

9. 退出`gfsh`命令shell：

   ```
   gfsh>quit
   Exiting...
   ```

10. 创建一个新的工作目录(例如：`new_geode`)并切换到新目录。

11. 启动`gfsh`命令shell：

    ```
    $ gfsh
    ```

12. 启动一个新的定位器。 例如：

    ```
    gfsh>start locator --name=locator2 --port=10335
    Starting a Geode Locator in /Users/username/new_geode/locator2...
    .............................
    Locator in /Users/username/new_geode/locator2 on 192.0.2.0[10335] as locator2
    is currently online.
    Process ID: 5749
    Uptime: 15 seconds
    Geode Version: 1.7
    Java Version: 1.8.0_121
    Log File: /Users/username/new_geode/locator2/locator2.log
    JVM Arguments: -Dgemfire.enable-cluster-configuration=true
    -Dgemfire.load-cluster-configuration-from-dir=false
    -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
    -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
    Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
    
    Successfully connected to: [host=192.0.2.0, port=1099]
    
    Cluster configuration service is up and running.
    ```

13. 使用`import cluster-configuration`命令导入集群配置。 例如：

    ```
    gfsh>import cluster-configuration --zip-file-name=/Users/username/myClConfig.zip
    Cluster configuration successfully imported
    ```

    请注意，`locator2`目录现在包含`cluster_config`子目录。

14. 启动不引用组的服务器：

    ```
    gfsh>start server --name=server4 --server-port=40414
    Starting a Geode Server in /Users/username/new_geode/server4...
    ........
    Server in /Users/username/new_geode/server4 on 192.0.2.0[40414] as server4
    is currently online.
    Process ID: 5813
    Uptime: 4 seconds
    Geode Version: 1.7
    Java Version: 1.8.0_121
    Log File: /Users/username/new_geode/server4/server4.log
    JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10335]
    -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p
    -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
    -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
    Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
    ```

15. 启动另一个引用`group1`的服务器：

    ```
    gfsh>start server --name=server5 --groups=group1 --server-port=40415
    Starting a Geode Server in /Users/username/new_geode/server5...
    .....
    Server in /Users/username/new_geode/server2 on 192.0.2.0[40415] as server5
    is currently online.
    Process ID: 5954
    Uptime: 2 seconds
    Geode Version: 1.7
    Java Version: 1.8.0_121
    Log File: /Users/username/new_geode/server5/server5.log
    JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10335] -Dgemfire.groups=group1
    -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p
    -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true
    -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
    Class-Path: /Users/username/Apache_Geode_1.0.0_Linux/lib/geode-dependencies.jar
    ```

16. 使用`list regions`命令显示已配置的区域。 请注意，在以前集群级别中配置的region1和region2可用。

    ```
    gfsh>list regions
    List of regions
    ---------------
    region1
    region2
    ```

17. 使用`describe region`命令查看哪些成员托管每个区域。 请注意，region1仅由server5托管，因为server5是使用group1配置启动的。 region2托管在server4和server5上，因为在没有指定组的情况下创建了region2。

    ```
    gfsh>describe region --name=region1
    ..........................................................
    Name            : region1
    Data Policy     : replicate
    Hosting Members : server5
    
    Non-Default Attributes Shared By Hosting Members
    
     Type  |    Name     | Value
    ------ | ----------- | ---------------
    Region | data-policy | REPLICATE
           | size        | 0
           | scope       | distributed-ack
    
    gfsh>describe region --name=region2
    ..........................................................
    Name            : region2
    Data Policy     : replicate
    Hosting Members : server5
                      server4
    
    Non-Default Attributes Shared By Hosting Members
    
     Type  |    Name     | Value
    ------ | ----------- | ---------------
    Region | data-policy | REPLICATE
           | size        | 0
           | scope       | distributed-ack
    ```

    此新集群使用与原始系统相同的配置。 您可以使用此集群配置启动任意数量的服务器。 所有服务器都将收到集群级配置。 指定`group1`的服务器也接收`group1`配置。

18. 使用以下命令关闭集群：

    ```
    gfsh>shutdown --include-locators=true
    As a lot of data in memory will be lost, including possibly events in queues,
      do you really want to shutdown the entire distributed system? (Y/n): Y
    Shutdown is triggered
    
    gfsh>
    No longer connected to 192.0.2.0[1099].
    ```

## 将应用程序JAR部署到Apache Geode成员

您可以将应用程序JAR文件动态部署到特定成员或集群中的所有成员。 Geode自动跟踪JAR文件版本; 将已部署的JAR文件自动加载到CLASSPATH; 并自动注册JAR包含的任何函数。

要在Apache Geode中部署和取消部署应用程序JAR文件，请使用`gfsh` `deploy`或`undeploy`命令。 您可以部署单个JAR或多个JAR（通过指定JAR文件名或指定包含JAR文件的目录），也可以将部署目标指向成员组或多个成员组。 例如，在连接到要部署JAR文件的集群后，可以在`gfsh`提示符下键入：

```
gfsh> deploy --jars=group1_functions.jar
```

此命令将`group1_functions.jar`文件部署到集群中的所有成员。

要将JAR文件部署到成员的子集，请使用`--groups`参数。 例如：

```
gfsh> deploy --jars=group1_functions.jar --groups=MemberGroup1
```

在示例中，假设您已经定义了在启动成员时要使用的成员组。有关如何定义成员组以及如何将成员添加到组的详细信息，请参阅[配置和运行集群](http://geode.apache.org/docs/guide/17/configuring/chapter_overview.html#concept_lrh_gyq_s4)。

要将位于特定目录中的所有JAR文件部署到所有成员：

```
gfsh> deploy --dir=libs/group1-libs
```

您可以提供JAR文件名或JAR目录以进行部署，但不能同时指定两者。

要在整个集群中取消部署所有以前部署的JAR文件：

```
gfsh> undeploy
```

要取消部署特定的JAR文件：

```
gfsh> undeploy --jars=group1_functions.jar
```

在取消部署所有JAR文件时定位特定成员组：

```
gfsh> undeploy --groups=MemberGroup1
```

只有先前部署在MemberGroup1组中的成员上的JAR文件才会被取消部署。

要查看集群中所有已部署的JAR文件的列表：

```
gfsh> list deployed
```

要查看特定成员组中所有已部署JAR的列表，请执行以下操作：

```
gfsh> list deployed --groups=MemberGroup1
```

样本输出：

```
 Member   |     Deployed JAR     |                JAR Location            
--------- | -------------------- | ---------------------------------------------------
datanode1 | group1_functions.jar | /usr/local/gemfire/deploy/vf.gf#group1_functions.jar#1
datanode2 | group1_functions.jar | /usr/local/gemfire/deploy/vf.gf#group1_functions.jar#1
```

有关`gfsh`用法的更多信息，请参阅[gfsh](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html).

### JAR文件的部署位置

在每个成员上写入JAR文件的系统位置由为该成员配置的`deploy-working-dir` Geode属性确定。 例如，您可以在您的成员的`gemfire.properties`文件中配置以下内容：

```properties
#gemfire.properties
deploy-working-dir=/usr/local/gemfire/deploy
```

此部署位置可以是本地或多个成员使用的共享网络资源（例如装载位置），以减少磁盘空间使用。 如果使用共享目录，则仍需要在要访问应用程序的每个成员上部署JAR文件，因为部署会更新CLASSPATH和自动注册功能。

### 关于部署JAR文件和集群配置服务

默认情况下，集群配置服务将已部署的JAR文件分发到集群中的所有定位器。 当您使用`gfsh`启动新服务器时，定位器将配置文件和已部署的jar文件提供给成员，并将它们写入服务器的目录。

请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

### JAR文件的版本控制

将JAR文件部署到集群或成员组时，将修改JAR文件以在其名称中指示版本信息。 每个JAR文件名都以`vf.gf #`为前缀，并在文件名末尾包含一个版本号。 例如，如果您将`MyClasses.jar`部署五次，则在列出所有已部署的jar时，文件名将显示为`vf.gf＃MyClasses.jar＃5`。

部署新的JAR文件时，接收部署的成员会检查JAR文件是否重复，因为JAR文件已部署在该成员上，或者因为JAR文件已部署到共享部署工作目录， 其他成员也在使用。如果另一个成员已将此JAR文件部署到共享目录（通过与其目录中的最新版本进行逐字节比较来确定），则接收最新部署的成员不会将该文件写入磁盘。相反，该成员更新ClassPathLoader以使用已部署的JAR文件。 如果在磁盘上检测到较新版本的JAR文件并且已在使用中，则会取消部署。

当成员开始使用JAR文件时，该成员将获取该文件的共享锁。 如果成员通过部署接收到较新版本，则该成员将释放共享锁并尝试删除现有JAR文件以支持较新版本。 如果没有其他成员在现有JAR上具有共享锁，则会删除现有的旧版本JAR。

### 自动类路径加载

启动缓存时，新缓存会请求将当前工作目录中每个JAR文件的最新版本添加到ClassPathLoader。 如果已将JAR文件部署到ClassPathLoader，则ClassPathLoader会在找到更新版本时更新其加载的版本; 否则，没有变化。 如果检测到，则如果没有其他成员对其具有共享锁，则会删除旧版本的JAR文件。

取消部署JAR文件不会自动卸载部署期间加载的类。 您需要重新启动成员才能卸载这些类。

当缓存关闭时，它会请求从ClassPathLoader中删除所有当前部署的JAR文件。

如果使用共享部署工作目录，则共享该目录的所有成员应属于同一成员组。 重新启动后，共享同一部署工作目录的所有成员将使用当前工作目录中找到的任何JAR部署和自动加载其CLASSPATH。 这意味着某些成员可能会加载JAR，即使它们不是接收原始部署的成员组的一部分。

### 自动函数注册

部署包含函数的JAR文件（换句话说，包含实现Function接口的类）时，该函数将通过`FunctionService.registerFunction`方法自动注册。如果使用相同的函数部署了另一个JAR文件（具有相同的JAR文件名或另一个文件名），则会注册该函数的新实现，覆盖旧的实现。如果取消部署JAR文件，则在部署时自动注册的任何功能都将取消注册。 由于多次部署具有相同名称的JAR文件会导致JAR未部署和重新部署，因此JAR中的函数将在每次发生时取消注册并重新注册。如果从多个不同名称的JAR文件中注册了具有相同ID的函数，则在重新部署或取消部署任何这些JAR文件时，将取消注册该函数。

在`cache.xml`加载期间，保存任何声明的参数。 如果在JAR文件中找到的函数也是可声明的，并且与加载cache.xml后保存参数的声明符具有相同的类名，则使用这些参数创建函数实例并进行注册。 因此，如果在具有不同参数集的`cache.xml`中多次声明相同的函数，则在部署JAR时，将为每组参数实例化一个函数。 如果使用来自`cache.xml`加载的参数注册任何函数，则不会注册默认的无参数函数。



## 使用成员组

Apache Geode允许您将集群成员组织到逻辑成员组中。

在Apache Geode中使用成员组是可选的。 使用成员组的好处是能够根据逻辑组成员身份协调成员上的某些操作。 例如，通过定义和使用成员组，您可以：

- 更改特定成员的配置属性的子集。 请参阅`gfsh`中的[alter runtime](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_7E6B7E1B972D4F418CB45354D1089C2B)。
- 执行某些磁盘操作，例如跨成员组进行磁盘存储压缩。 有关命令列表，请参阅[磁盘存储命令](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_1ACC91B493EE446E89EC7DBFBBAE00EA)。
- 管理组中所有成员的特定索引或区域。
- 在成员组中启动和停止多站点（WAN）服务，例如网关发件人和网关接收器。
- 在组中的所有成员上部署或取消部署JAR应用程序。
- 在特定组的所有成员上执行功能。

您可以在成员的`gemfire.properties`文件的`groups`属性中或在`gfsh`中的成员启动时定义组名。

**注意:** 现在的`roles`属性中定义的任何角色现在都将被视为一个组。 如果您希望向集群添加成员资格角色，则应将它们作为成员组添加到`groups`属性中。 'roles`属性已被弃用，建议使用`groups`属性。

要将成员添加到组，请在启动之前将成员组的名称添加到成员的`gemfire.properties`文件中，或者可以在`gfsh`中在启动成员时传入`--groups`参数 。

单个成员可以属于多个组。

成员组还可以用于从客户的角度或从同行成员的角度组织成员。 请参阅[将对等方组织到逻辑成员组](http://geode.apache.org/docs/guide/17/topologies_and_comm/p2p_configuration/configuring_peer_member_groups.html) 和 [将服务器组织到逻辑成员组](http://geode.apache.org/docs/guide/17/topologies_and_comm/cs_configuration/configure_servers_into_logical_groups.html)以获取更多信息。在客户端，您可以在配置客户端的连接池时提供成员组名称。 使用客户端的`cache.xml`中的`<pool server-group>`元素。



## 导出和导入集群配置

集群配置服务导出和导入使用`gfsh`为整个Apache Geode集群创建的配置。

使用`gfsh`命令创建区域，磁盘存储和其他对象时，集群配置服务会保存集群配置。 您可以将此配置以及包含应用程序文件的任何jar文件导出到zip文件，然后导入此配置以创建新集群。

### 导出集群配置

发出`gfsh``export cluster-configuration`命令，以便在zip文件中保存集群的配置数据。 此zip文件包含集群级配置的子目录和集群中指定的每个组的目录。 [集群配置文件和故障排除](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_config_troubleshooting.html#concept_ylt_2cb_y4)中介绍了这些目录的内容。

要导出集群配置，请在连接到Geode集群时运行`gfsh``export cluster-configuration`命令。 例如：

```
export cluster-configuration --zip-file-name=/home/username/configs/myClusterConfig.zip
```

参见 [导出集群配置](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_mdv_jgz_ck).

**注意:** `gfsh`仅保存使用`gfsh`指定的配置的集群配置值。 管理API创建的配置不随集群配置一起保存。

### 导入集群配置

使用`gfsh``import cluster-configuration`命令根据从其他系统导出的配置信息来配置新的集群。 您只能将集群配置导入新集群，即：

- 没有正在运行的缓存服务器

或者

- 唯一运行的缓存服务器满足以下所有条件：
  - 是刚刚启动的
  - 没有定义区域
  - 自从它们启动以来，没有给出任何其他配置更改

导入配置后，您启动的任何服务器都将收到此集群配置。

要导入集群配置，请启动一个或多个定位器，然后运行`gfsh``import cluster-configuration`命令。 例如：

```
import cluster-configuration --zip-file-name=/home/username/configs/myClusterConfig.zip
```

请参阅[导入集群配置](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/import.html#topic_vnv_grz_ck)。

## 集群配置文件和故障排除

在Geode中使用集群配置服务时，可以检查定位器上`cluster_config`目录中生成的配置文件。 `gfsh`在集群级别和单个组级别保存配置文件。

运行集群配置服务的定位器上提供了以下目录和配置文件：

**Cluster-level configuration**
对于适用于集群的所有成员的配置，定位器在`cluster_config`目录中创建一个`cluster`subdirectory（或在使用`--cluster-config-dir=value启动定位器时在集群配置目录中参数）指定。 所有服务器在使用`gfsh`启动时都会收到此配置。 该目录包含：

- `cluster.xml` – Geode`cache.xml`文件，包含所有成员共有的配置
- `cluster.properties` – Geode`gemfire.properties`文件，包含所有成员共有的属性
- 用于部署到所有成员的Jar文件

**Group-level configuration**
当您在`gfsh`命令中指定`--groups`参数时（例如，`start server`或`create region`），定位器会将每个组的配置写入与该组同名的子目录中。 启动指定一个或多个组名的服务器时，服务器将同时接收集群级配置和指定的所有组的配置。 该子目录包含：

- `<group-name>.xml` – Geode`cache.xml`文件，包含该组所有成员共有的配置
- `<group-name>.properties` – Geode`gemfire.properties`文件，包含该组所有成员共有的属性
- 用于部署到组的所有成员的Jar文件

![img](assets/cluster-group-config.svg)

您可以导出包含集群配置的所有工件的zip文件。 zip文件包含定位器的`cluster_config`（或其他指定的）子目录中的所有文件。 您可以将此配置导入新集群。 请参见[导出和导入集群配置](http://geode.apache.org/docs/guide/17/configuring/cluster_config/export-import.html#concept_wft_dkq_34)。

### 故障排除提示

- 当您使用`gfsh`启动定位器时，您应该看到以下消息：

  ```
  Cluster configuration service is up and running.
  ```

  如果您没有看到此消息，则集群配置服务可能存在问题。 使用`status cluster-config-service`命令检查集群配置的状态。

  - 如果该命令返回RUNNING，则集群配置正常运行。
  - 如果命令返回WAITING，则运行`status locator`命令。 此命令的输出返回WAITING状态的原因。

- 使用`cache.xml`文件进行配置时，这些文件中的配置应用程序有一个特定的顺序。 Geode首先应用集群范围的配置文件, 接着是组级配置。 最后一个是成员自己的配置文件（`cache.xml`和`gemfire.properties`文件）中的配置。

- 如果服务器启动失败并出现以下异常：`ClusterConfigurationNotAvailableException`，则集群配置服务可能不处于RUNNING状态。 因为服务器从定位器请求集群配置不可用，所以`start server`命令失败。

- 您可以通过检查服务器的日志文件来确定服务器从定位器接收的配置。 请参阅[Logging](http://geode.apache.org/docs/guide/17/managing/logging/logging.html#concept_30DB86B12B454E168B80BB5A71268865)。

- 如果`start server`命令指定与现有集群配置冲突的cache.xml文件，则服务器启动可能会失败。

- 如果由于无法保存集群配置而导致`gfsh`命令失败，则会显示以下消息：

  ```
  Failed to persist the configuration changes due to this command, 
  Revert the command to maintain consistency. Please use "status cluster-config-service" 
  to determine whether Cluster configuration service is RUNNING."
  ```

- 有些类型的配置无法使用`gfsh`进行。 请参阅[gfsh限制](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl__section_bn3_23p_y4)。



## 使用gfsh通过HTTP或HTTPS管理远程集群

您可以通过HTTP或HTTPS将`gfsh`连接到远程集群，并使用`gfsh`commands管理集群。

要使用HTTP协议将`gfsh`连接到远程集群：

1. 启动`gfsh`。 请参阅[启动gfsh](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/starting_gfsh.html#concept_DB959734350B488BBFF91A120890FE61)。

2. 在远程主机上启动远程集群时，可以选择在启动JMX管理器（服务器或定位器）时将`--http-bind-address`和`--http-service-port`指定为Geode属性。 然后，可以在从本地系统连接到远程集群中的HTTP服务时使用的URL中使用这些属性。 例如：

   ```
   gfsh>start server --name=server1 --J=-Dgemfire.jmx-manager=true \
   --J=-Dgemfire.jmx-manager-start=true --http-service-port=8080 \
   --http-service-bind-address=myremotecluster.example.com
   ```

   此命令必须直接在主机上执行，该主机最终将充当承载HTTP服务以进行远程管理的远程服务器。 （您无法远程启动服务器。）

3. 在本地系统上，运行`gfsh connect`命令连接到远程系统。 包括`--use-http`和`--url`参数。 例如：

   ```
   gfsh>connect --use-http=true --url="http://myremotecluster.example.com:8080/geode/v1"
   
   Successfully connected to: Geode Manager's HTTP service @ http://myremotecluster.example.com:8080/geode/v1
   ```

   See [connect](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/connect.html).

   `gfsh`现在连接到远程系统。 大多数`gfsh`命令现在将在远程系统上执行; 但是，也有例外。 在本地集群上执行以下命令：

   - `alter disk-store`
   - `compact offline-disk-store`
   - `describe offline-disk-store`
   - `help`
   - `hint`
   - `sh` (for executing OS commands)
   - `sleep`
   - `start jconsole` (however, you can connect JConsole to a remote cluster when gfsh is connected to the cluster via JMX)
   - `start jvisualvm`
   - `start locator`
   - `start server`
   - `start vsd`
   - `status locator``*`
   - `status server``*`
   - `stop locator``*`
   - `stop server``*`
   - `run` (for executing gfsh scripts)
   - `validate disk-store`
   - `version`

   `*`当`gfsh`通过JMX或HTTP / S连接到集群时，可以通过对这些`stop` /`status`命令使用`--name`选项来停止并获取*远程定位器和服务器*的状态。 如果对这些命令使用`--pid`或`--dir`选项，则`stop` /`status`命令仅在本地执行。

要为远程连接（HTTPS）配置SSL，请在`gemfire.properties`或`gfsecurity-properties`或服务器启动时为`http`组件启用SSL。 有关配置SSL参数的详细信息，请参阅[SSL](http://geode.apache.org/docs/guide/17/managing/security/ssl_overview.html)。 这些SSL参数也适用于配置的JMX Manager上托管的所有HTTP服务，其中包括以下内容：

- Developer REST API service
- Pulse monitoring tool



## 在没有集群配置服务的情况下部署配置文件

您可以在系统目录结构或jar文件中部署Apache Geode配置文件。 您可以确定部署配置文件的方式并相应地进行设置。

**注意:** 如果使用集群配置服务来创建和管理Apache Geode集群配置，则不需要本节中描述的过程，因为Geode会自动管理配置文件和jar文件到集群成员的分发。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。

您可以使用本节中描述的过程来分发特定于成员的配置，或者您不希望使用集群配置服务的情况。

- **部署配置文件的主要步骤**

  这些是部署配置文件的基本步骤，以及后续章节中的相关详细信息。

- **默认文件规范和搜索位置**

  每个文件都有一个默认名称，一组文件搜索位置以及可用于覆盖默认值的系统属性。

- **更改文件规范**

  您可以在`gemfire.properties`文件和命令行中更改所有文件规范。

- **在JAR文件中部署配置文件**

  本节提供了在JAR文件中部署配置文件的过程和示例。

### 部署配置文件的主要步骤

这些是部署配置文件的基本步骤，以及后续章节中的相关详细信息。

1. 确定安装所需的配置文件。
2. 将文件放在目录或jar文件中。
3. 对于具有非默认名称或位置的任何文件，请在系统属性文件 和/或 成员`CLASSPATH中提供文件规范。

#### Geode配置文件

- `gemfire.properties`. 包含集群成员所需的设置。 这些设置包括许可，系统成员发现，通信参数，日志记录和统计信息。 请参阅[Geode属性参考](http://geode.apache.org/docs/guide/17/reference/topics/gemfire_properties.html)。
- **gfsecurity.properties**. 一个可选的单独文件，包含与`gemfire.properties`中另外定义的安全相关（`security- *`）设置。 将这些成员属性放入单独的文件允许您限制用户对这些特定设置的访问。 请参阅[Geode属性参考](http://geode.apache.org/docs/guide/17/reference/topics/gemfire_properties.html)。
- `cache.xml`. 声明性缓存配置文件。 此文件包含缓存，区域和区域条目配置的XML声明。 您还可以使用它来配置磁盘存储，数据库登录凭据，服务器和远程站点位置信息以及套接字信息。 请参阅[cache.xml](http://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_cache_xml.html#cache_xml)。

### 默认文件规范和搜索位置

每个文件都有一个默认名称，一组文件搜索位置以及可用于覆盖默认值的系统属性。

要使用默认规范，请将文件放在其目录或jar文件的顶层。 系统属性是标准文件规范，可以具有绝对或相对路径名和文件名。

**注意:** 如果未指定绝对文件路径和名称，搜索将检查文件的所有搜索位置。

| Default File Specification | Search Locations for Relative File Specifications | Available Property for File Specification                  |
| -------------------------- | ------------------------------------------------- | ---------------------------------------------------------- |
| `gemfire.properties`       | current directoryhome directoryCLASSPATH          | As a Java system property, use `gemfirePropertyFile`       |
| `cache.xml`                | current directoryCLASSPATH                        | In `gemfire.properties`, use the `cache-xml-file` property |

有效`gemfirePropertyFile`规范的示例：

- `/zippy/users/jpearson/gemfiretest/gemfire.properties`
- `c:\gemfiretest\gemfire.prp`
- `myGF.properties`
- `test1/gfprops`

对于`test1 / gfprops`规范，如果你在Unix文件系统中从`/ testDir`启动Geode系统成员，Geode会按此顺序查找文件，直到它找到文件或耗尽所有位置：

1. `/testDir/test1/gfprops`
2. `<yourHomeDir>/test1/gfprops`
3. under every location in your `CLASSPATH` for `test1/gfprops`



### 更改文件规范

您可以在`gemfire.properties`文件和命令行中更改所有文件规范。

**注意:** Geode应用程序可以使用API将`java.lang.System`属性传递给集群连接。 这会更改在命令行和`gemfire.properties`文件中生成的文件规范。 您可以在应用程序启动时记录的配置信息中验证应用程序的属性设置。 当`gemfire.properties``log-level`设置为`config`或更低时，会列出配置。

这个对应用程序`testApplication.TestApp1`的调用为`cache.xml`和`gemfire.properties`文件提供了非默认规范：

```
java -Dgemfire.cache-xml-file=\
/gemfireSamples/examples/dist/cacheRunner/queryPortfolios.xml \
-DgemfirePropertyFile=defaultConfigs/gemfire.properties \
testApplication.TestApp1
```

gfsh start server命令可以使用相同的规范：

```
gfsh>start server \
--J=-Dgemfire.cache-xml-file=/gemfireSamples/examples/dist/cacheRunner/queryPortfolios.xml \
--J=-DgemfirePropertyFile=defaultConfigs/gemfire.properties
```

您还可以在`gemfire.properties`文件中更改`cache.xml`文件的规范。

**注意:** `gemfire.properties`文件中的规范不能使用环境变量。

示例`gemfire.properties`文件，带有非默认的`cache.xml`规范：

```properties
#Tue May 09 17:53:54 PDT 2006
mcast-address=192.0.2.0
mcast-port=10333
locators=cache-xml-file=/gemfireSamples/examples/dist/cacheRunner/queryPortfolios.xml
```



### 在JAR文件中部署配置文件

本节提供了在JAR文件中部署配置文件的过程和示例。

**程序**

1. Jar文件。
2. 将Apache Geode系统属性设置为指向存放在jar文件中的文件。
3. 在你的`CLASSPATH`中包含jar文件
4. 验证jar文件副本是运行时应用程序唯一可见的副本。 Geode在搜索其他位置后搜索`CLASSPATH`，因此这些文件在其他搜索区域中无法使用。
5. 启动应用程序。 配置文件从jar文件加载。

**部署配置JAR的示例**

以下示例在`my.jar`中部署缓存配置文件`myCache.xml`。 以下显示了'my.jar`的内容：

```
% jar -tf my.jar 
META-INF 
META-INF/MANIFEST.MF 
myConfig/ 
myConfig/myCache.xml
```

在此示例中，您将执行以下步骤来部署配置jar文件：

1. 将系统属性`gemfire.cache-xml-file`设置为`myConfig/myCache.xml`。
2. 将`CLASSPATH`设置为包含`my.jar`。
3. 验证名为`./myConfig/myCache.xml`的文件系统中是否已有文件，因此Geode将被强制搜索jar文件以查找它。

启动应用程序时，将从jar文件加载配置文件。



## 启动和关闭系统

确定正确的启动和关闭过程，并编写启动和关闭脚本。

精心设计的启动和停止系统的程序可以加快启动速度并保护您的数据。 启动和停止所需的过程包括服务器和定位器进程以及您的其他Geode应用程序，包括客户端。 您使用的过程部分取决于系统的配置以及系统进程之间的依赖关系。

使用以下准则创建启动和关闭过程和脚本。 其中一些说明使用[`gfsh`](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html)。

### 启动你的系统

您应该在启动Geode系统时,遵循一定的顺序准则。

在启动客户端应用程序之前启动服务器。 在每个集群中，请遵循以下成员启动准则：

- 首先启动定位器。 有关定位器启动命令的示例，请参阅[运行Geode定位器进程](http://geode.apache.org/docs/guide/17/configuring/running/running_the_locator.html)。

- 在其余进程之前启动缓存服务器，除非实现要求在其之前启动其他进程。 有关服务器启动命令的示例，请参阅[运行Geode服务器进程](http://geode.apache.org/docs/guide/17/configuring/running/running_the_cacheserver.html)。

- 如果您的集群同时使用持久性复制和非持久性复制区域，则应在启动非持久性区域之前并行启动所有持久性复制成员。 这样，持久成员不会延迟其他持久成员使用以后的数据启动。

- 对于包含持久区域的系统，请参阅[使用磁盘存储启动和关闭](http://geode.apache.org/docs/guide/17/managing/disk_storage/starting_system_with_disk_stores.html)。

- 如果您正在运行`生产者进程`和`消费者`或`事件侦听器进程`，请首先启动消费者。 这可确保消费者和侦听器不会错过任何通知或更新。

- 如果您同时启动定位器和对等成员，则可以在进程启动时使用`locator-wait-time`属性（以秒为单位）。 此超时允许对等方在尝试加入集群之前等待定位器完成启动。 如果已将进程配置为等待定位器启动，则会记录信息级消息

  > `GemFire初创公司无法联系定位器。 等待一个开始。 配置的定位器是frodo [12345]，pippin [12345] .`

  然后，该进程将休眠一秒钟并重试，直到它连接或者`locator-wait-time`中指定的秒数已经过去。 默认情况下，`locator-wait-time`设置为零，这意味着在启动时无法连接到定位器的进程将引发异常。

**注意:** 您可以选择覆盖关闭单个进程的默认超时期限。 必须在成员启动期间指定此覆盖设置。 有关详细信息，请参阅[关闭系统](http://geode.apache.org/docs/guide/17/configuring/running/starting_up_shutting_down.html#starting_up_shutting_down__section_mnx_4cp_cv)。

### 在磁盘上丢失数据后启动

此信息与Geode磁盘存储文件的灾难性丢失有关。 如果丢失磁盘存储文件，则下一次启动可能会挂起，等待丢失的磁盘存储重新联机。 如果您的系统在启动时挂起，请使用`gfsh`命令`show missing-disk-store`列出缺少的磁盘存储，如果需要，还可以撤消丢失的磁盘存储，以便完成系统启动。 您必须使用磁盘存储ID来撤消磁盘存储。 这些是两个命令：

```
gfsh>show missing-disk-stores

Disk Store ID             |   Host    |               Directory                                           
------------------------------------ | --------- | -------------------------------------
60399215-532b-406f-b81f-9b5bd8d1b55a | excalibur | /usr/local/gemfire/deploy/disk_store1 

gfsh>revoke missing-disk-store --id=60399215-532b-406f-b81f-9b5bd8d1b55a
```

**注意:** 此`gfsh`命令要求您通过JMX Manager节点连接到集群。

### 关闭系统

使用`gfsh``shutdown`命令关闭Geode系统，或者一次关闭一个成员。

### 使用`shutdown`命令

如果您正在使用持久性区域（成员将数据持久保存到磁盘），则应使用`gfsh``shutdown`命令以有序的方式停止正在运行的系统。 此命令在关闭之前同步持久分区区域，这使得集群的下一次启动尽可能高效。

如果可能，所有成员应在关闭之前运行，以便进行同步。 使用以下`gfsh`命令关闭系统：

```
gfsh>shutdown
```

默认情况下，shutdown命令仅关闭数据节点。 如果要关闭包括定位器在内的所有节点，请指定`--include-locators = true`参数。 例如：

```
gfsh>shutdown --include-locators=true
```

这将逐个关闭所有定位器，最后关闭管理器。

要在宽限期后关闭所有数据成员，请指定超时选项（以秒为单位）。

```
gfsh>shutdown --time-out=60
```

要在宽限期后关闭包括定位器在内的所有成员，请指定超时选项（以秒为单位）。

```
gfsh>shutdown --include-locators=true --time-out=60
```

### 单独关闭系统成员

如果您不使用持久性区域，则可以按照与启动相反的顺序关闭每个成员来关闭集群。 （有关成员启动的建议顺序，请参阅[启动系统](http://geode.apache.org/docs/guide/17/configuring/running/starting_up_shutting_down.html#starting_up_shutting_down__section_3D111558326D4A38BE48C17D44BB66DB)。）

根据成员的类型关闭集群成员。 例如，使用以下机制关闭成员：

- 使用适当的机制关闭集群中运行的任何与Geode连接的客户端应用程序。

- 关闭所有缓存服务器。 要关闭服务器，请发出以下`gfsh`命令：

  ```
  gfsh>stop server --name=<...>
  ```

  或者

  ```
  gfsh>stop server --dir=<server_working_dir>
  ```

- 关闭任何定位器。 要关闭定位器，请发出以下`gfsh`命令：

  ```
  gfsh>stop locator --name=<...>
  ```

  或者

  ```
  gfsh>stop locator --dir=<locator_working_dir>
  ```

- 在正常情况下，不要使用命令行`kill -9`来关闭服务器。特别是在具有少量成员的系统上，使用`kill`而不是`gfsh stop`会导致分区检测机制将系统置于最终状态，该状态将永远等待重新连接到被杀死的服务器，并且 无法重启那个被杀死的服务器。 如果出现`kill`命令是摆脱服务器系统的唯一方法，那么`kill` * all *集群的进程或使用`kill -INT`，这将允许有序关闭进程。

### 系统成员关闭行为的选项

`DISCONNECT_WAIT`命令行参数设置关闭过程中每个步骤的最长时间。 如果任何步骤花费的时间超过指定的数量，则强制结束。 每个操作都给出此宽限期，因此缓存成员关闭所需的总时间长度取决于操作数和“DISCONNECT_WAIT”设置。 在关机过程中，Geode会生成以下消息：

```
Disconnect listener still running
```

`DISCONNECT_WAIT`默认值是10000毫秒。

要更改它，请在用于成员启动的Java命令行上设置此系统属性。 例如：

```
gfsh>start server --J=-DDistributionManager.DISCONNECT_WAIT=<milliseconds>
```

每个进程可以有不同的`DISCONNECT_WAIT`设置。



## 运行Geode定位器进程

定位器是一个Geode进程，它告诉运行成员所在的新连接成员，并为服务器使用提供负载均衡。

您可以将定位器作为对等定位器，服务器定位器或两者运行：

- 对等定位器将连接成员的连接信息提供给已在定位器集群中运行的成员。
- 服务器定位器为客户端的集群中运行的服务器提供连接信息。 服务器定位器还监视服务器负载并将客户端发送到负载最少的服务器。

默认情况下，定位器作为对等和服务器定位器运行。

您可以独立运行定位器或嵌入另一个Geode过程。 独立运行定位器可提供定位器服务的最高可靠性和可用性。

### 定位器配置和日志文件

定位器配置和日志文件具有以下属性：

- 使用`gfsh`启动独立定位器时，`gfsh`会自动将所需的JAR文件`lib/geode-dependencies.jar`加载到JVM进程的CLASSPATH中。 如果使用`LocatorLauncher` API启动独立定位器，则必须在用于启动定位器进程的命令中指定此JAR文件。 有关Geode中CLASSPATH设置的更多信息，请参阅[设置CLASSPATH](http://geode.apache.org/docs/guide/17/getting_started/setup_classpath.html)。 您可以通过指定`--classpath`参数来修改CLASSPATH。

- 定位器是集群的成员，就像任何其他成员一样。 在“mcast-port”和“locators”配置方面，应该以与服务器相同的方式配置定位器。 因此，如果集群中还有另外两个定位器，则每个定位器应引用其他定位器（就像服务器成员一样）。 例如：

  ```
  gfsh> start locator --name=locator1 --port=9009 --mcast-port=0 \
  --locators='host1[9001],host2[9003]'
  ```

- 您可以在`gemfire.properties`文件中配置定位器，也可以在命令行上指定启动参数。 如果要在属性文件中指定定位器的配置，则定位器需要与集群的其他成员相同的`gemfire.properties`设置，并且如果使用单独的受限访问安全设置文件，则需要相同的`gfsecurity.properties`设置。

  例如，在`gemfire.properties：`中配置定位器和多播端口

  ```
  locators=host1[9001],host2[9003]
  mcast-port=0
  ```

- 没有特定于定位器的缓存配置。

- 对于日志记录输出，定位器在其当前工作目录中创建日志文件。 日志文件输出默认为定位器工作目录中的`locator_name.log`。 如果使用以前使用的定位器名称重新启动定位器，则会自动为您重命名现有的*locator_name* .log文件（例如，`locator1-01-01.log`或`locator1-02-01.log`）。 您可以通过在启动定位器时在`--log-level`参数中指定级别来修改此文件中的日志记录详细信息的级别。

- 默认情况下，定位器将从执行`gfsh`的目录下的子目录（以定位器命名）开始。 该子目录被视为当前工作目录。 在`gfsh`中启动定位器时，您还可以指定其他工作目录。

- 默认情况下，由于网络分区事件或成员无响应而已关闭和断开连接的定位器将自行重新启动并自动尝试重新连接到现有集群。 当定位器处于重新连接状态时，它不为集群提供任何发现服务。 有关详细信息，请参阅[使用自动重新连接处理强制高速缓存断开连接](http://geode.apache.org/docs/guide/17/managing/member-reconnect.html)。

### 定位器和集群配置服务

定位器使用集群配置服务来保存适用于所有集群成员或指定组成员的配置。 配置保存在Locator的目录中，并传播到集群中的所有定位器。 使用`gfsh`启动服务器时，服务器从定位器接收组级别和集群级别配置。

请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)。

### 启动定位器

使用以下准则启动定位器：

- **独立定位器**. 以下列方式之一启动独立定位器：

  - 使用`gfsh`命令行实用程序。 有关使用`gfsh`的更多信息，请参阅[`gfsh`](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/chapter_overview.html)。 例如：

    ```
    gfsh>start locator --name=locator1
    
    gfsh> start locator --name=locator2 --bind-address=192.0.2.0 --port=13489
    ```

  - 使用`org.apache.geode.distributed.LocatorLauncher`类中的`main`方法和Java可执行文件启动定位器。 具体来说，您使用`LocatorLauncher`类API在您创建的Java应用程序进程中运行嵌入式Locator服务。 执行java命令的目录成为定位器进程的工作目录。

  - 启动多个定位器时，不要并行启动它们（换句话说，同时启动它们）。 作为最佳实践，您应该等待大约30秒，以便第一个定位器在启动任何其他定位器之前完成启动。 要检查定位器的成功启动，请检查定位器日志文件。 要查看正在运行的定位器的正常运行时间，可以使用`gfsh status locator`命令。

- **嵌入式（共置）定位器**. 在成员启动时或通过API管理共置定位器：

  - 使用`gemfire.properties``start-locator`设置在Geode成员中自动启动定位器。 请参阅[参考](http://geode.apache.org/docs/guide/17/reference/book_intro.html#reference)。 成员退出时定位器自动停止。 该属性具有以下语法：

    ```
    #gemfire.properties
    start-locator=[address]port[,server={true|false},peer={true|false}]
    ```

    Example:

    ```
    #gemfire.properties
    start-locator=13489
    ```

  - 使用`org.apache.geode.distributed.LocatorLauncher` API启动代码中的定位器。 使用`LocatorLauncher.Builder`类构造`LocatorLauncher`的实例，然后使用`start()`方法启动嵌入在Java应用程序进程中的Locator服务。 `LocatorLauncher`类中的其他方法提供有关定位器的状态信息，并允许您停止定位器。

    ```
    import org.apache.geode.distributed.LocatorLauncher;
    
     public class MyEmbeddedLocator {
    
        public static void main(String[] args){
            LocatorLauncher locatorLauncher  = new LocatorLauncher.Builder()
              .setMemberName("locator1")
              .setPort(13489)
              .build();
    
              locatorLauncher.start();
    
              System.out.println("Locator successfully started");
            }
        }
    ```

    这是另一个将定位器嵌入应用程序，启动它然后在允许其他成员访问它之前检查定位器状态的示例：

    ```
    package example;
    
    import ...
    
    class MyApplication implements Runnable {
    
      private final LocatorLauncher locatorLauncher;
    
      public MyApplication(final String... args) {
        validateArgs(args);
    
        locatorLauncher = new LocatorLauncher.Builder()
          .setMemberName(args[0])
          .setPort(Integer.parseInt(args[1])
          .setRedirectOutput(true)
          .build();
      }
    
      protected void args(final String[] args) {
        ...
      }
    
      public void run() {
        ...
    
        // start the Locator in-process
        locatorLauncher.start();
    
        // wait for Locator to start and be ready to accept member (client) connections
        locatorLauncher.waitOnStatusResponse(30, 5, TimeUnit.SECONDS);
    
        ...
      }
    
      public static void main(final String... args) {
        new MyApplication(args).run();
      }
    
    }
    ```

    然后执行应用程序，您将运行：

    ```
    /working/directory/of/MyApplication$ java \
     -server -classpath "path/to/installation/lib/geode-dependencies.jar:/path/to/application/classes.jar" \
     example.MyApplication Locator1 11235
    ```

    执行java命令的目录成为定位器进程的工作目录。

### 检查定位器状态

如果使用`gfsh`连接到集群，则可以通过提供定位器名称来检查正在运行的定位器的状态。 例如：

```
gfsh>status locator --name=locator1
```

如果未连接到集群，则可以通过提供进程ID，定位器的主机名和端口或定位器的当前工作目录来检查本地定位器的状态。 例如：

```
gfsh>status locator --pid=2986
```

或者

```
gfsh>status locator --host=host1 --port=1035
```

或者

```
$ gfsh status locator --dir=<locator_working_directory>
```

其中<* locator_working_directory *>对应于运行定位器的本地工作目录。

如果成功，该命令将返回以下信息（使用启动时提供的JVM参数）：

```
$ gfsh status locator --dir=locator1
Locator in /home/user/locator1 on ubuntu.local[10334] as locator1 is currently online.
Process ID: 2359
Uptime: 17 minutes 3 seconds
GemFire Version: 8.0.0
Java Version: 1.8.0_121
Log File: /home/user/locator1/locator1.log
JVM Arguments: -Dgemfire.enable-cluster-configuration=true -Dgemfire.load-cluster-configuration-from-dir=false
 -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
Class-Path: /Users/username/apache_geode/lib/geode-dependencies.jar

Cluster configuration service is up and running.
```

### 停止定位器

如果使用`gfsh`连接到集群，则可以通过提供定位器名称来停止正在运行的定位器。 例如：

```
gfsh>stop locator --name=locator1
```

如果未连接到集群，则可以通过指定定位器的进程ID或定位器的当前工作目录来停止本地定位器。 例如：

```
gfsh>stop locator --pid=2986
```

或者

```
gfsh>stop locator --dir=<locator_working_directory>
```

其中<* locator_working_directory *>对应于运行定位器的本地工作目录。

### 定位器和多站点（WAN）部署

如果使用多站点（WAN）配置，则可以在启动定位器时将定位器连接到远程站点。

要将新定位器进程连接到WAN配置中的远程定位器，请在启动时指定以下内容：

```
gfsh> start locator --name=locator1 --port=9009 --mcast-port=0 \
--J='-Dgemfire.remote-locators=192.0.2.0[9009],198.51.100.0[9009]'
```



## 运行Geode服务器进程

Geode服务器是一个作为客户端/服务器系统的长期可配置成员运行的进程。

Geode服务器主要用于托管长期数据区域以及运行标准Geode进程，例如客户端/服务器配置中的服务器。 您可以使用以下方法启动和停止服务器：

- `gfsh`命令行工具。
- 以编程方式，通过`org.apache.geode.distributed.ServerLauncher` API。 `ServerLauncher` API只能用于以`gfsh`或`ServerLauncher`类本身启动的Geode服务器。

### 默认服务器配置和日志文件

`gfsh`实用程序为其配置文件和日志文件使用工作目录。 这些是默认值和配置选项：

- 使用`gfsh`启动独立服务器时，`gfsh`会自动将所需的JAR文件`lib/geode-dependencies.jar`加载到JVM进程的CLASSPATH中。 如果使用ServerLauncher API启动独立服务器，则必须在命令中指定此JAR文件以启动该过程。 有关Geode中CLASSPATH设置的更多信息，请参阅[设置CLASSPATH](http://geode.apache.org/docs/guide/17/getting_started/setup_classpath.html)。

- 服务器的配置与任何其他Geode进程一样，使用`gemfire.properties`和共享集群配置文件。 除了应用程序插件外，它不可编程。 通常，您提供`gemfire.properties`文件和`gfsecurity.properties`文件。 您还可以在缓存服务器的工作目录中指定`cache.xml`文件。

- 默认情况下，以“gfsh”启动的新服务器从集群配置服务接收其初始缓存配置，假设定位器正在运行集群配置服务。 如果在启动服务器时指定组，则服务器还会接收适用于组的配置。 共享配置包括`cache.xml`文件，`gemfire.properties`文件和部署的jar文件。 使用`gfsh`启动服务器时，可以通过指定`--use-cluster-configuration = false`来禁用集群配置服务。 请参见[集群配置服务概述](http://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl)。

- 如果您使用的是Spring Framework，则可以使用`--spring-xml-location`命令行选项在`gfsh`中启动服务器时指定Spring ApplicationContext XML文件。 此选项允许您使用Spring应用程序的配置引导Geode服务器进程。 有关此文件的更多信息，请参阅[Spring文档](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/resources.html#resources-app-ctx)。

- 对于日志记录输出，日志文件输出默认为缓存服务器工作目录中的`<server-name> .log`。 如果重新启动具有相同服务器名称的服务器，则会自动重命名现有日志文件，例如`server1-01-01.log`和`server1-02-01.log`。 您可以通过在启动服务器时在`--log-level`参数中指定级别来修改此文件中的日志记录详细信息的级别。

- 默认情况下，服务器将在一个子目录中启动，该子目录以服务器指定的名称命名，位于执行`gfsh`的目录下。 该子目录被视为当前工作目录。 在`gfsh`中启动缓存服务器时，您还可以指定其他工作目录。

- 默认情况下，由于网络分区事件或成员无响应而已关闭和断开连接的服务器进程将自行重新启动并自动尝试重新连接到现有集群。 有关详细信息，请参阅[使用自动重新连接处理强制高速缓存断开连接](http://geode.apache.org/docs/guide/17/managing/member-reconnect.html#concept_22EE6DDE677F4E8CAF5786E17B4183A9)。

- 您可以在服务器启动时使用`-J = -Dproperty.name = value`将JVM参数传递到服务器的JVM。 这些参数可以是Java属性或Geode属性，例如`gemfire.jmx-manager`。 例如：

  ```
  gfsh>start server --name=server1 --J=-Dgemfire.jmx-manager=true \
  --J=-Dgemfire.jmx-manager-start=true --J=-Dgemfire.http-port=8080
  ```

- 我们建议您在启动服务器时不要使用`-XX：+ UseCompressedStrings`和`-XX：+ UseStringCache`JVM配置属性。 这些JVM选项可能会导致数据损坏和兼容性问题。

### 使用gfsh启动服务器

有关语法信息，请参阅[`gfsh start server`](http://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_3764EE2DB18B4AE4A625E0354471738A)命令参考页。

这些示例`gfsh start server` start命令为缓存配置指定`cache.xml`文件，并使用不同的传入客户端连接端口：

```
gfsh>start server --name=server1 \
--cache-xml-file=../ServerConfigs/cache.xml --server-port=40404

gfsh>start server --name=server2 \
--cache-xml-file=../ServerConfigs/cache.xml --server-port=40405
```

可以在`gemfire.properties`文件中定义`cache.xml`文件的位置和客户端连接端口的设置。 然后，启动指定`gemfire.properties`文件的服务器，如示例命令中所示：

```
gfsh>start server --name=server1 \
--properties-file=/home/username/cluster/gemfire.properties
```

要使用嵌入式JMX Manager启动服务器：

```
gfsh>start server --name=server2 \
--J=-Dgemfire.jmx-manager=true --J=-Dgemfire.jmx-manager-start=true
```

当在服务器启动期间指定`--max-heap`和`--initial-heap`时，代表您指定其他GC参数。 如果您不想设置其他默认GC属性，请使用`-Xms`和`-Xmx` JVM选项来设置这些参数。 有关详细信息，请参阅[使用资源管理器控制堆使用](http://geode.apache.org/docs/guide/17/managing/heap_use/heap_management.html#configuring_resource_manager)。 要启动服务器，请提供JVM配置设置：

```
gfsh>start server --name=server3 \
--J=-Xms80m,-Xmx80m --J=-XX:+UseConcMarkSweepGC,-XX:CMSInitiatingOccupancyFraction=65
```

### 以编程方式启动服务器

使用`org.apache.geode.distributed.ServerLauncher` API在代码中启动缓存服务器进程。 使用`ServerLauncher.Builder`类构造`ServerLauncher`的实例，然后使用`start（）`方法启动服务器服务。 `ServerLauncher`类中的其他方法提供有关服务器的状态信息，并允许您停止服务器。

```
import org.apache.geode.distributed.ServerLauncher;

 public class MyEmbeddedServer {

    public static void main(String[] args){
        ServerLauncher serverLauncher  = new ServerLauncher.Builder()
          .setMemberName("server1")
          .setServerPort(40405)
          .set("jmx-manager", "true")
          .set("jmx-manager-start", "true")
          .build();

        serverLauncher.start();  

        System.out.println("Cache server successfully started");
    }
}
```

### 检查服务器状态

在`gfsh`中连接到集群后，通过提供服务器名称来检查正在运行的缓存服务器的状态：

```
gfsh>status server --name=server1
```

如果未连接到集群，则可以通过提供进程ID或服务器的当前工作目录来检查本地缓存服务器的状态。 例如：

```
gfsh>status server --pid=2484
```

或者

```
% gfsh status server --dir=server1
```

如果成功，输出将提供此示例中的信息：

```
% gfsh status server --dir=server4
Server in /home/username/server4 on 192.0.2.0[40404] as server4 is currently online.
Process ID: 49008
Uptime: 2 minutes 4 seconds
Geode Version: 1.7
Java Version: 1.8.0_144
Log File: /home/username/server4/server4.log
JVM Arguments: 
...
```

### 停止服务器

当连接到`gfsh`中的集群时，通过提供服务器名称来停止正在运行的缓存服务器：

```
gfsh>stop server --name=server1
```

如果未连接，则可以通过指定服务器的当前工作目录或进程ID来停止本地缓存服务器。 例如：

```
gfsh>stop server --pid=2484
```

或者

```
gfsh>stop server --dir=server1
```

您还可以使用`gfsh shutdown`命令以有序的方式关闭所有缓存服务器。 对于具有持久区域的系统，执行`shutdown`是正确的方法。 有关详细信息，请参阅[启动和关闭系统](http://geode.apache.org/docs/guide/17/configuring/running/starting_up_shutting_down.html)。



## 管理系统输出文件

Geode输出文件是可选的，可以变得非常大。 与系统管理员一起确定放置它们的位置，以避免干扰其他系统活动。

Geode包括几种类型的可选输出文件，如下所述。

- **Log Files**. 全面的日志消息，可帮助您确认系统配置并调试配置和代码中的问题。 在`gemfire.properties`文件中配置日志文件行为。 请参阅[Logging](http://geode.apache.org/docs/guide/17/managing/logging/logging.html#concept_30DB86B12B454E168B80BB5A71268865)。
- **Statistics Archive Files**. 缓存和分发活动的标准统计信息，可以在磁盘上存档。 在`gemfire.properties`，`archive-disk-space-limit`和`archive-file-size-limit`中配置统计信息收集和归档。 请参阅[参考](http://geode.apache.org/docs/guide/17/reference/book_intro.html#reference)。
- **Disk Store Files**. 保持缓存中的持久性和溢出数据。 您可以配置区域以将数据持久保存到磁盘以进行备份或溢出到磁盘以控制内存使用。 服务器用于向客户端发送事件的订阅队列可以溢出到磁盘。 网关发件人自动将磁盘溢出到磁盘，并且可以持久保存以实现高可用性。 通过`cache.xml`配置它们。 请参阅[磁盘存储](http://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html)。



## 防火墙注意事项

您可以为涉及防火墙的情况配置和限制端口使用，例如，在客户端 - 服务器或服务器 - 服务器连接之间。

- **Firewalls and Connections**

  请注意在计算机上运行防火墙可能导致的连接问题。

- **Firewalls and Ports**

  确保为防火墙正确配置了端口设置。

### 防火墙和连接

请注意在计算机上运行防火墙可能导致的连接问题。

Apache Geode是一个以网络为中心的分布式系统，因此如果您的计算机上运行了防火墙，则可能会导致连接问题。 例如，如果防火墙对基于Java的套接字的入站或出站权限设置了限制，则连接可能会失败。 您可能需要修改防火墙配置以允许流量到您计算机上运行的Java应用程序。 具体配置取决于您使用的防火墙。

例如，由于超时设置，防火墙可能会关闭与Geode的连接。 如果防火墙在某个时间段内未检测到任何活动，则可能会在活动恢复时关闭连接并打开新连接，这可能会导致您对哪些连接产生混淆。

有关Geode客户端和服务器如何连接的更多信息，请参阅以下主题：

- [客户端/服务器连接如何工作](http://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/how_the_pool_manages_connections.html#how_the_pool_manages_connections)
- [套接字通信](http://geode.apache.org/docs/guide/17/managing/monitor_tune/socket_communication.html)
- [控制套接字使用](http://geode.apache.org/docs/guide/17/managing/monitor_tune/performance_controls_controlling_socket_use.html#perf)
- [设置套接字缓冲区大小](http://geode.apache.org/docs/guide/17/managing/monitor_tune/socket_communication_setting_socket_buffer_sizes.html)



### 防火墙和端口

确保为防火墙正确配置了端口设置。

使用防火墙时需要考虑几种不同的端口设置：

- 缓存服务器侦听的端口。 这可以使用cache.xml中的`cache-server`元素，Java API中的CacheServer类，以及`gfsh start server`命令的命令行选项进行配置。

  默认情况下，如果没有另外指定，Geode客户端和服务器会在localhost上的预定义端口(** 40404 **)上相互发现。

- 定位器端口。 Geode客户端可以使用定位器自动发现缓存服务器。 定位器端口可配置为`gfsh start locator`命令的命令行选项。 定位器用于对等缓存部署以发现其他进程。 客户端可以使用它们来定位服务器，作为使用一组服务器地址和端口配置客户端的替代方法。

  默认情况下，如果没有另外指定，Geode定位器使用默认端口** 10334 **。

- 由于定位器启动集群，定位器还必须通过防火墙使其临时端口范围和TCP端口可供其他成员访问。

- 对于客户端，您将客户端配置为使用客户端池配置连接到服务器。 客户端的池配置有两个选项：您可以使用服务器元素列表或定位器元素列表创建池。 对于每个元素，指定主机和端口。 必须通过防火墙访问指定的端口。

### 限制短暂的端口以实现点对点成员资格

默认情况下，Geode分配* ephemeral *端口，即从指定范围分配的临时端口，可以包含大量可能的端口。 当存在防火墙时，短暂的端口范围通常必须限制为更小的数量，例如六个。 如果要通过防火墙配置P2P通信，则还必须为每个进程设置TCP端口，并确保允许UDP流量通过防火墙。

### 防火墙和端口配置的属性

此表包含可能涉及防火墙行为的属性，并提供每个属性的简要说明。

| **Configuration area** | **Property or Setting** | **Definition**                                               |
| ---------------------- | ----------------------- | ------------------------------------------------------------ |
| peer-to-peer config    | `conserve-sockets`      | 指定套接字是否由系统成员的线程共享。                         |
| peer-to-peer config    | `locators`              | 系统成员使用的定位器列表。 必须为集群的每个成员一致地配置列表。 |
| peer-to-peer config    | `mcast-address`         | 用于发现集群的其他成员的地址。 仅在mcast-port为非零时使用。 此属性必须在整个集群中保持一致。 |
| peer-to-peer config    | `mcast-port`            | 与mcast-address一起使用的端口，用于与集群的其他成员进行多播通信。 如果为零，则禁用多播以进行数据分发。 |
| peer-to-peer config    | `membership-port-range` | 可用于单播UDP消息传递和对等集群中的TCP故障检测的临时端口范围。 |
| peer-to-peer config    | `tcp-port`              | 侦听缓存通信的TCP端口。                                      |

| Configuration Area  | **Property or Setting**                                      | **Definition**                                               |
| ------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| cache server config | `hostname-for-clients`                                       | 作为服务器正在侦听的位置传递给客户端的主机名或IP地址。       |
| cache server config | `max-connections`                                            | 服务器的最大客户端连接数。 达到最大值时，服务器拒绝其他客户端连接。 |
| cache server config | `port` (cache.xml) or `--port`parameter to the `gfsh start server` command | 服务器侦听以进行客户端通信的端口。                           |

### 默认端口配置

| **Port Name**         | Related Configuration Setting                                | **Default Port**                                             |
| --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Cache Server          | `port` (cache.xml)                                           | 40404                                                        |
| HTTP                  | `http-service-port`                                          | 7070                                                         |
| Locator               | `start-locator` (for embedded locators) or `--port` parameter to the `gfsh start locator`command. | *if not specified upon startup or in the start-locator property, uses default port 10334* |
| Membership Port Range | `membership-port-range`                                      | 1024 to 65535                                                |
| Memcached Port        | `memcached-port`                                             | *not set*                                                    |
| Multicast             | `mcast-port`                                                 | 10334                                                        |
| RMI                   | `jmx-manager-port`                                           | 1099                                                         |
| TCP                   | `tcp-port`                                                   | ephemeral port                                               |

### 多站点（WAN）配置中的防火墙和端口配置的属性

每个网关接收器使用单个端口接受来自其他系统中的网关发送器的连接。 网关接收器的配置指定要使用的一系列可能的端口值。 当网关接收器启动时，Geode从指定范围中选择一个可用端口。 配置防火墙，以便WAN上的网关发件人可以访问所有可能的端口值。

| Configuration Area                           | Property or Setting                                          | Definition                                       |
| -------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------ |
| multi-site (WAN) config for gateway sender   | [hostname-for-senders](http://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#gateway-receiver) | 网关发件人用于连接的网关接收器的主机名或IP地址。 |
| multi-site (WAN) config for locator          | [remote-locators](http://geode.apache.org/docs/guide/17/reference/topics/gemfire_properties.html#gemfire_properties) | 远程WAN站点上可用的定位器（及其端口）列表。      |
| multi-site (WAN) config for gateway receiver | [start-port](http://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#gateway-receiver) and [end-port](http://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#gateway-receiver) (cache.xml) or `--start-port` and `--end-port` parameters to the `gfsh start gateway receiver`command | 网关接收器可用于侦听网关发送方通信的端口范围。   |



