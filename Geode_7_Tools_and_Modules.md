# 工具和模块 {#Tools_and_Modules}

*工具和模块*描述了与Apache Geode相关的工具和模块。

- **gfsh**

  `gfsh`（发音为"jee-fish"）提供了一个强大的命令行界面，您可以从中启动，管理和监控Geode进程，数据和应用程序。

- **Gemcached**

  Gemcached是一个Geode适配器，允许Memcached客户端与Geode服务器集群通信，就像服务器是memcached服务器一样。 Memcached是一个开源缓存解决方案，它使用分布式内存中的哈希映射来存储字符串或对象数据的键值对。

- **HTTP Session Management Modules**

  Apache Geode HTTP会话管理模块为HTTP服务器提供快速，可扩展且可靠的会话复制，而无需更改应用程序。

- **Geode Pulse**

  Geode Pulse是一个Web应用程序，它提供了一个图形仪表板，用于监视Geode集群，成员和区域的重要实时运行状况和性能。

- **Geode Redis Adapter**

  Geode Redis适配器允许Geode作为Redis数据存储的直接替代品，让Redis应用程序可以利用Geode的扩展功能而无需更改其客户端代码。 Redis客户端使用IP地址和端口号以与连接Redis服务器相同的方式连接到Geode服务器。

- **Apache Lucene® Integration**

  ApacheLucene®集成使用户能够创建Lucene索引并对存储在Geode中的数据执行Lucene搜索。



## gfsh {#gfsh}
`gfsh`（发音为“jee-fish”）提供了一个强大的命令行界面，您可以从中启动，管理和监控Geode进程，数据和应用程序。

- **What You Can Do with gfsh](#What You Can Do with gfsh {#What_You_Can_Do_with_gfsh})**

  `gfsh`支持Apache Geode进程和应用程序的管理，调试和部署。

- **[Startinggfsh](#Starting gfsh {#Starting_gfsh})**

  在开始`gfsh`之前，请确认您已设置JAVA_HOME，并且您的PATH变量包含`gfsh`可执行文件。

- **Configuring the gfsh Environment**

  `gfsh.bat`和`gfsh` bash脚本自动将所需的Apache Geode和JDK的`.jar`库附加到现有的CLASSPATH。 您可以为安全性，环境变量，日志记录和故障排除设置用户可配置的属性。

- **Useful gfsh Shell Variables**

  您可以在脚本中使用内置的`gfsh` shell变量。

- **Basic Shell Features and Command-Line Usage**

  `gfsh`实用程序为shell环境提供了有用的功能，包括命令自动完成，保留的命令历史记录和多行命令的分隔。 上下文相关的帮助可通过命令和主题获得。

- **Specifying JSON within Command-Line Options**

  一些`gfsh`命令允许在命令行选项中使用JSON规范。

- **Tutorial—Performing Common Tasks with gfsh**

  本主题将指导您在启动`gfsh`后执行的典型任务序列。

- **Quick Reference of gfsh Commands by Functional Area**

  此快速参考将所有命令分类到功能区域。

- **gfsh Command Help**

  本节提供按字母顺序列出的所有`gfsh`命令的帮助和用法信息。

- **Creating and Running gfsh Command Scripts**

  `gfsh`提供了几种在脚本环境中运行命令的方法。

- **Running gfsh Commands on the OS Command Line**

- **Mapping of cache.xml Elements to gfsh Configuration Commands.**

  您可以使用cache.xml文件配置Geode集群，也可以使用`gfsh`和集群配置服务来配置集群。 本节将cache.xml元素映射到配置和管理集群的`gfsh`命令。



### 你可以用gfsh做什么 {#What_You_Can_Do_with_gfsh}
`gfsh`支持Apache Geode进程和应用程序的管理，调试和部署。

使用`gfsh`，您可以:

- 启动和停止Apache Geode进程，例如定位器和缓存服务器
- 启动和停止网关发送方和网关接收方进程
- 部署应用程序
- 创建和销毁区域
- 执行函数
- 管理磁盘存储
- 导入和导出数据
- 监控Apache Geode进程
- 启动Apache Geode监控工具

`gfsh`命令行界面使开发人员可以花更少的时间配置缓存实例XML，属性，日志和统计信息。 gfsh命令生成报告; 捕获集群范围的统计数据; 并支持导出统计信息，日志和配置。 与Spring Roo一样，gfsh具有命令完成功能（因此您无需了解语法），上下文相关帮助，脚本以及使用简单API从应用程序内调用任何命令的功能。 gfsh接口使用`JMX/RMI`与Apache Geode进程通信。

您可以使用HTTP协议将gfsh连接到远程群集。 请参阅[使用gfsh通过HTTP或HTTPS管理远程群集](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html)。

默认情况下，群集配置服务会在您使用gfsh创建Apache Geode对象时保存Apache Geode群集的配置。 您可以导出此配置并将其导入另一个Apache Geode集群。 请参见[群集配置服务概述](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl)。



### 启动gfsh {#Starting_gfsh}

在启动gfsh之前，请确认您已设置JAVA_HOME并且您的PATH变量包含gfsh可执行文件。

**注意:**在Windows上，必须正确设置JAVA_HOME环境变量，才能对定位器和服务器使用start，stop和status命令。

要启动gfsh命令行界面，请在当前随Apache Geode一起安装的任何计算机上的提示符处执行以下命令:

**Start gfsh on Windows:**

```bash
<product_directory>\bin\gfsh.bat 
```

其中<*product_directory*>对应于安装Apache Geode的位置。

**Start gfsh on Unix:**

```bash
<product_directory>/bin/gfsh
```

其中<*product_directory*>对应于安装Apache Geode的位置。 执行时，`gfsh`脚本将所需的Apache Geode和JDK的Jar库附加到现有的CLASSPATH。

如果您已成功启动`gfsh`，则会出现`gfsh`启动画面和提示。

```bash
c:\Geode\Latest>gfsh.bat
    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /
/______/_/      /______/_/    /_/

Monitor and Manage Geode
gfsh>
```

您也可以直接在终端中运行一些gfsh命令，而无需输入`gfsh`提示。 例如，在`Unix/Linux`上你可以输入:

```bash
$ gfsh start server --name=server1
```

或在Windows上:

```bash
prompt> gfsh start server --name=server1
```

有关详细信息，请参阅[创建和运行gfsh命令脚本](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command_scripting.html#concept_9B2F7550F16C4717831AD40A56922259)。


### 配置gfsh环境 {#Configuring_the_gfsh_Environment}

`gfsh.bat`和`gfsh` bash脚本自动将所需的Apache Geode和`JDK jar`库附加到现有的CLASSPATH。 您可以为安全性，环境变量，日志记录和故障排除设置用户可配置的属性。

**CLASSPATH中的JAR库**

当你启动`gfsh`时，它会自动加载已经打包在`gfsh-dependencies.jar`文件中的所需JAR文件。 您无需修改CLASSPATH即可运行`gfsh`。

JAR文件打包在`lib`目录的安装目录中。

**机器主机名**

在某些操作系统上，您可能需要确保在系统主机文件中配置了计算机的主机名。 例如，在macOS上，您可能需要将机器的主机名映射到`/etc/hosts`文件中的IP地址，以便`gfsh`和Pulse正常运行。

**配置gfsh安全性**

由于`gfsh`必须连接到JMX Manager成员才能运行某些命令（即管理和监视其他成员的那些命令），因此JMX Manager配置属性可能会影响`gfsh`安全性。 在`gemfire.properties`中，以下Geode属性可以影响JMX Manager的`gfsh`connection设置:

- `jmx-manager-ssl`
- `jmx-manager-port`
- `jmx-manager-password-file`
- `jmx-manager-access-file`

您可能还需要验证端口是否可用并对客户端连接开放。 有关这些安全属性的详细信息，请参阅[配置JMX Manager](https://geode.apache.org/docs/guide/17/managing/management/jmx_manager_operations.html#topic_263072624B8D4CDBAD18B82E07AA44B6)。

**配置gfsh环境变量**

此外，您可以使用`set variable`命令设置特定于gfsh的预设SHELL变量。 例如，您可以将`gfsh`设置为以安静模式运行。 并非所有`gfsh`变量都是可修改的。 用户可配置的变量包括:

- APP_FETCH_SIZE
- APP_QUIET_EXECUTION

有关详细信息，请参阅[有用的gfsh Shell变量](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/useful_gfsh_shell_variables.html#concept_731ECA5E40E943CBA5C1198A0745D8EE)。

**配置gfsh会话日志记录**

默认情况下，禁用`gfsh`会话日志记录。 要启用gfsh日志记录，必须设置Java系统属性`-Dgfsh.log-level=desired_log_level`，其中*desired_log _level*是以下值之一:`severe，warning，info，config，fine，fine，finest`。 例如，在Linux中:

```bash
export JAVA_ARGS=-Dgfsh.log-level=info
```

然后，启动`gfsh`。

gfsh生成一个名为**gfsh-%u_%g.log**的日志文件。 此日志文件记录单个gfsh会话的事件。 它包括环境信息，例如Java和系统信息，以及详细的命令执行。 变量替换如下:

- **%u** - 解决冲突的唯一编号
- **%g** - 用于区分旋转日志的世代号

gfsh使用JDK Logger生成gfsh会话日志文件。 有关如何在命名日志文件时使用变量的说明，请参见`http://docs.oracle.com/javase/7/docs/api/java/util/logging/FileHandler.html`。 无法更改生成的gfsh日志文件的默认名称。

默认情况下，日志文件将写入您执行`gfsh`或`gfsh.bat`脚本的当前工作目录。 要修改写入日志文件的目录位置，请使用`gfsh.log-dir` Java系统属性。 例如:

```bash
export JAVA_ARGS="-Dgfsh.log-level=info -Dgfsh.log-dir=/machinename/logs"
```

然后，启动`gfsh`。

另外，`gfsh`在`${SYS_USER_HOME}/.geode/.gfsh.history`文件中记录了命令的历史记录，您可以使用它来创建脚本或查看过去的命令。

**成员日志文件**

gfsh为通过gfsh启动的任何成员写入几个日志文件。 有用的成员日志文件包括:

- **<locator_name>.log**. 详细说明定位器的配置（包括所有`gemfire.properties`）以及启动后定位器上发生的所有活动。 此日志文件将写入以定位符命名的目录。 例如，如果启动名为locator1的定位器，则该文件将在`<product_dir>/locator1`目录中写为`locator1.log`。
- **vf.gf.locator.pid**. 包含定位器的进程ID。 您可以使用PID来停止或查看此定位器的状态。 此文件将写入与定位器日志文件相同的目录位置。
- **<server_name>.log**. 详细说明服务器的配置（包括所有`gemfire.properties`）以及启动后服务器上发生的所有活动。 此日志文件将写入以服务器命名的目录。 例如，如果启动名为server1的服务器，则该文件将在`<product_dir>/server1`目录中写为`server1.log`。 如果停止并以相同的名称启动服务器，则较旧的日志文件将保留在同一目录中，但会重命名以进行版本控制。
- **vf.gf.server.pid**. 包含服务器的进程ID。 您可以使用PID来停止或查看此服务器的状态。 此文件将写入与服务器日志文件相同的位置。

**查看标准输出和标准错误**

默认情况下，Geode不会将应用程序写入的消息显示为标准输出和标准错误。 要允许将这些消息分别写入定位器和服务器日志文件，请使用gfsh`start locator`或`start server`命令指定`--redirect-output`选项。 例如，以下命令会将`stdout`和`stderr`消息写入`locator1.log`文件:

```bash
gfsh> start locator --name=locator1 --redirect-output
```

**Tab自动补全**

*本节仅适用于UNIX安装.*

从UNIX bash shell运行gfsh命令时，可以通过运行以下命令在shell中启用自动tab-completion:

```bash
source <gemfire-install-directory>/bin/gfsh-completion.bash
```

运行此命令后，您可以在从bash shell运行gfsh命令时使用自动完成。

请参阅[使用TAB自动补全](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/getting_started_gfsh.html#concept_45D28CC9710C4EAFB6EECFA0D651D439__p_sb2_ttx_n4)。

**命令历史和gfsh.history**

已成功执行的命令历史记录在运行gfsh的用户的主目录下的`.gemfire`目录中的`.gfsh.history`文件中。 您还可以使用`history --file=your_file_name`命令导出历史文件。

**JMX Manager更新率和系统监控**

当您执行数据操作（例如put）然后监视系统状态（例如使用gfsh`show metrics`命令或Geode Pulse）时，受监视的系统可能不会立即反映最近的操作。 例如，如果执行put操作然后立即执行`show metrics` gfsh命令，则可能看不到该区域中正确的条目数。 管理层每2秒更新一次。 执行操作活动后等待几秒钟以查看最准确的结果。

您可以修改`gemfire.properties`中的`jmx-manager-update-rate`属性，以增加或减少将更新推送到JMX Manager的速率（以毫秒为单位）。 此属性设置应大于或等于`statistic-sample-rate`。 如果遇到性能问题，您可能希望提高此间隔; 但是，将此值设置得太高会导致在`gfsh`和Geode Pulse中看到陈旧值。

**格式化结果**

*本节仅适用于UNIX安装.*

诸如`query`之类的gfsh命令产生具有宽列的输出，这些列可能会变得不对齐并需要手动重新格式化以查看输出。 如果输出无法适应终端的可用宽度，gfsh会自动修剪列宽以适应。 您可以通过将gfsh环境变量`GFSH.TRIMSCRWIDTH`设置为`false`来禁用此行为。

您可以将`APP_RESULT_VIEWER`变量设置为`external`，以便使用UNIX`less`命令查看输出。

请参阅[配置gfsh环境变量](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/configuring_gfsh.html#concept_3B9C6CE2F64841E98C33D9F6441DF487__section_C69A2711A7664A9091A5E634221053CB)。


### 有用的gfsh Shell变量 {#Useful_gfsh_Shell_Variables}
您可以在脚本中使用内置的`gfsh` shell变量。

您还可以使用`set variable`命令修改shell行为或定义自己的变量。

要查看所有gfsh shell变量及其当前值的列表，请使用以下命令:

```bash
gfsh>echo --string=$*
```

要获取现有变量的当前值，请使用以下命令语法（该变量必须括在大括号中）:

```bash
gfsh>echo --string=${VARIABLE}
```

例如:

```bash
gfsh>echo --string=${SYS_CLASSPATH}
```

**系统变量**

|                       |                                                              |
| :-------------------- | :----------------------------------------------------------- |
| SYS_CLASSPATH         | gfsh JVM的CLASSPATH（只读）.                       |
| SYS_GEMFIRE_DIR       | 已安装Geode的产品目录（只读）. |
| SYS_HOST_NAME         | 启动gfsh的主机（只读）.                 |
| SYS_JAVA_VERSION      | 使用的Java版本 （只读）.                               |
| SYS_OS                | 操作系统名称 （只读）.                                         |
| SYS_OS_LINE_SEPARATOR | 在编写gfsh脚本时可以使用的行分隔符（\或^）变量。 （只读）. |
| SYS_USER              | 用户名（只读）.                                       |
| SYS_USER_HOME         | 用户的主目录（只读）.                           |

**GFSH环境变量**

|                                |                                                              |
| :----------------------------- | :----------------------------------------------------------- |
| APP_FETCH_SIZE                 | 查询时要使用的提取大小。 值:0  -  2147483647.默认值为100. |
| APP_LAST_EXIT_STATUS           | 上次命令退出状态。 与$相似？ （Unix）和％errorlevel％（Windows）。 值:0（成功），1（错误），2（崩溃）（只读）. |
| APP_LOGGING_ENABLED            | Whether gfsh logging is enabled. Default: false (read only). You can enable gfsh logging by setting the `gfsh.log-level` Java system property to a [supported Java log level](http://docs.oracle.com/javase/8/docs/api/java/util/logging/Level.html). |
| APP_LOG_FILE                   | 当前gfsh日志文件的路径和名称（只读）.          |
| APP_NAME                       | 应用程序的名称 - “gfsh”（只读）.                 |
| APP_PWD                        | 启动gfsh的当前工作目录（只读）. |
| APP_QUERY_RESULTS_DISPLAY_MODE | 切换显示模式以返回查询结果。 值:`table or catalog(表或编目)`。 默认值为`table`. |
| APP_QUIET_EXECUTION            | 执行是否应该处于安静模式。 值（不区分大小写）:true，false。 默认值为false. |
| APP_RESULT_VIEWER              | 仅限Unix。 将此变量设置为`external`以使用UNIX`less`命令查看输出。 默认值为basic（gfsh）. |


### 基本Shell功能和命令行用法 {#Basic_Shell_Features_and_Command_Line_Usage}
`gfsh`实用程序为shell环境提供了有用的功能，包括命令自动完成，保留的命令历史记录和多行命令的分隔。 上下文相关的帮助可通过命令和主题获得。


**要查看可用的gfsh命令列表，请在空提示符下按Tab键.**

您看到的命令列表取决于您是否连接到Geode群集。 如果未连接，则会看到可用的本地命令列表。

**使用hint命令获取有关特定主题的信息.**

hint命令显示指定主题的单行描述和关联命令。 例如，**hint data**返回数据主题的描述以及可用于数据的所有可能操作:

```bash
gfsh>hint Data
User data as stored in regions of the Geode distributed system.

create index  : Create an index that can be used when executing queries.
destroy index : Destroy/Remove the specified index.
export data   : Export user data from a region to a file.
get           : Display an entry in a region. If using a region whose key and value classes have been set, then specifying --key-class and --value-class is unnecessary.
import data   : Import user data from a file to a region.
list indexes  : Display the list of indexes created for all members.
locate entry  : Identifies the location, including host, member and region, of entries that have the specified key.
put           : Add/Update an entry in a region. If using a region whose key and value classes have been set, then specifying --key-class and --value-class is unnecessary.
query         : Run the specified OQL query as a single quoted string and display the results in one or more pages. Limit will default to the value stored in the "APP_FETCH_SIZE" variable. Page size will default to the value stored in the "APP_COLLECTION_LIMIT" variable.
rebalance     : Rebalance partitioned regions. The default is for all partitioned regions to be rebalanced.
remove        : Remove an entry from a region. If using a region whose key class has been set, then specifying --key-class is unnecessary.
```

要查看提示主题列表，请键入`hint`。

**使用help命令获取有关特定命令的信息.**

根据命令，键入`help <command-name>`将显示该特定命令的使用信息或与该命令相关的命令列表。

例如，键入`help start`以显示带有简短描述的启动命令列表。 说明指示命令是否可用，并取决于`gfsh`的连接状态。 以下示例假定`gfsh`当前未连接（通过连接到JMX Manager节点），因此某些启动命令不可用。

```bash
gfsh>help start
start gateway-receiver (Not Available)
    Start the Gateway Receiver on a member or members.
start gateway-sender (Not Available)
    Start the Gateway Sender on a member or members.
start jconsole (Available)
    Start the JDK's JConsole tool in a separate process. JConsole will be
    launched, but connecting to GemFire must be done manually.
start jvisualvm (Available)
    Start the JDK's Java VisualVM (jvisualvm) tool in a separate process. Java
    VisualVM will be launched, but connecting to GemFire must be done manually.
start locator (Available)
    Start a Locator.
start pulse (Available)
    Open a new window in the default Web browser with the URL for the Pulse
    application.
start server (Available)
    Start a GemFire Cache Server.
```

**使用Tab键自动完成命令或触发可能的完成.**

输入部分命令以触发命令的自动完成或可能的命令完成列表后，使用Tab键。 例如，在键入`hint`后按Tab键会显示所有可用主题:

```bash
gfsh>hint

Configuration           Data                    Debug-Utility
Disk Store              Function Execution      GFSH
GemFire                 Help                    JMX
Lifecycle               Locator                 Management-Monitoring
Manager                 Region                  Server
Statistics
```

键入`hint d`后按Tab键会显示以`d`开头的可用主题:

```bash
gfsh>hint d

data            debug-Utility   disk Store
```

自动完成还为命令提供可用参数和参数; 例如，在键入`start`后点击TAB键将列出可以启动的所有服务。

```bash
gfsh>start

start data-browser   start jconsole       start jvisualvm
start locator        start pulse          start server
```

在`start locator`之后点击TAB键将在命令后填充`--name`参数。 （如果您没有指定成员名称，gfsh将自动选择一个随机名称。这对自动化非常有用。）

**使用向上箭头访问命令历史记录.**

通过使用向上箭头滚动以前的命令来访问shell历史记录中的命令。

**使用反斜杠分隔多行命令.**

输入长命令时，可以使用反斜杠字符（'\'）作为分隔符来中断命令行。 例如:

```bash
gfsh>create region --name=region1 \
--type=REPLICATE_PERSISTENT \
--cache-writer=org.apache.geode.examples.MyCacheWriter \
--group=Group1 --disk-store=DiskStore1
```

**用单引号或双引号括起包含空格或逗号的字符串**

在gfsh命令shell中执行gfsh命令时，请在单引号中包含任何包含空格或逗号的字符串。 例如:

```bash
gfsh>start locator --name='locator 1'
start locator --name=locator1 --port=9009 --mcast-port=0\
 --J='-Dgemfire.remote-locators=192.0.2.0[9009],192.0.2.1[9009]'
```

当您从操作系统shell在一行中执行多个gfsh命令时，请将gfsh命令用双引号括起来。 在双引号内，包含任何包含带单引号的空格或逗号的字符串。 例如:

```bash
$ gfsh -e "start locator --name='locator 1'" -e "start server --name=server1"
```


### 在命令行选项中指定JSON {#Specifying_JSON_within_Command_Line_Options}


一些`gfsh`命令允许在某些选项中使用JSON规范。 这些JSON字符串以键/值对的形式指定初始化属性。

对于那些实现`Declarable`接口的类，JSON字符串被附加到完全限定的类名。 接口的`init`方法的参数是一个包含初始化属性的对象。 解析JSON字符串并将其分解为一组定义对象的属性名称和值。

`gfsh create region`命令中的缓存加载器类的规范可能具有JSON规范。 例如，

```bash
gfsh>create region --name=region1 --type=PARTITION \
   --cache-loader=com.example.Setup{'k1':'v1','k2':'v2','k3':'v3'} 
```

此示例中的JSON字符串是

```
{'k1':'v1','k2':'v2','k3':'v3'} 
```

JSON字符串由大括号括起，并且用逗号分隔。
每对由冒号分隔。 定义一对的两个字符串中的每一个都用单引号或双引号字符括起来。 在使用单引号或双引号字符时，在整个JSON字符串中保持一致。

空格字符分隔`gfsh`命令行选项，并且在解析JSON字符串之前进行选项的标记化。 如果命令行选项中的值包含空格，请使用双引号将其分隔。

例如，假设有一对，并且该对的值是`value with space`。 `--cache-loader`示例选项的右侧变为

```bash
gfsh>create region --name=region2 --type=PARTITION \
   --cache-loader="com.example.Setup{'k1':'value with space'}" 
```

要在值中包含单引号字符，请使用两个反斜杠字符对其进行转义。 例如，

```bash
gfsh>create region --name=region3 --type=PARTITION \
   --cache-loader="com.example.Setup{'k1':'property\\'s value'}" 
```

value的值是 `property's value`。


### 教程 - 使用gfsh执行常见任务 {#Tutorial}
本主题将指导您在启动`gfsh`后执行的典型任务序列。

步骤1:创建临时工作目录并切换到该目录。 例如:

```bash
$ mkdir gfsh_tutorial
$ cd gfsh_tutorial
```

**Step 1: 启动gfsh提示符.**

```bash
$ gfsh
    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /
/______/_/      /______/_/    /_/

Monitor and Manage Geode
gfsh>
```

有关详细信息，请参阅[启动gfsh](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/starting_gfsh.html#concept_DB959734350B488BBFF91A120890FE61)。

**Step 2: 启动定位器.** 输入以下命令:

```bash
gfsh>start locator --name=locator1
```

出现以下输出:

```bash
gfsh>start locator --name=locator1
.....
Locator in /home/username/gfsh_tutorial/locator1 on 192.0.2.0[10334]
as locator1 is currently online.
Process ID: 67666
Uptime: 6 seconds
Geode Version: 1.7
Java Version: 1.8.0_121
Log File: /home/username/gfsh_tutorial/locator1.log
JVM Arguments: -Dgemfire.enable-cluster-configuration=true
 -Dgemfire.load-cluster-configuration-from-dir=false
 -Dgemfire.launcher.registerSignalHandlers=true
 -Djava.awt.headless=true -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
Class-Path: /home/username/geode/geode-assembly/build/install/apache-geode/lib
/geode-core-1.2.0.jar:/home/username/geode/geode-assembly/build/install/apache-geode
/lib/geode-dependencies.jar

Successfully connected to: JMX Manager [host=192.0.2.0, port=1099]

Cluster configuration service is up and running.
```

如果你从gfsh运行`start locator`而没有指定成员名，gfsh会自动选择一个随机成员名。 这对自动化很有用。

在文件系统中，检查执行`gfsh`的文件夹位置。 请注意，`start locator`命令已自动创建了一个工作目录（使用定位器的名称），并在该工作目录中创建了一个日志文件，一个状态文件和一个`.pid`（包含定位器的进程ID）对于这个定位器。

此外，由于还没有其他JMX Manager存在，请注意`gfsh`已在定位器内的端口1099上自动启动嵌入式JMX Manager，并已将您连接到该JMX Manager。

**Step 3: 检查现有的gfsh连接.**

在当前shell中，键入以下命令:

```bash
gfsh>describe connection
```

如果已连接到在步骤2中启动的定位器中启动的JMX Manager，则会显示以下输出:

```bash
gfsh>describe connection
Connection Endpoints
--------------------
ubuntu.local[1099]
```

请注意，JMX Manager位于1099，而定位器的默认端口为10334。

**Step 4: 从不同的终端连接到相同的`locator/JMX Manager`.**

此步骤说明如何连接到`locator/JMX Manager`。 打开第二个终端窗口，然后启动第二个`gfsh`提示符。 在第二个提示中键入与步骤3中相同的命令:

```bash
gfsh>describe connection
```

这一次，请注意您未连接到JMX Manager，并显示以下输出:

```bash
gfsh>describe connection
Connection Endpoints
--------------------
Not connected
```

在第二个`gfsh`终端中键入以下命令:

```bash
gfsh>connect
```

该命令将连接到您在步骤2中启动的当前运行的本地定位器。

```bash
gfsh>connect
Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=ubuntu.local, port=1099] ..
Successfully connected to: [host=ubuntu.local, port=1099]
```

请注意，如果您在启动定位器时使用了自定义`--port`，或者您从另一个成员的`gfsh`提示符连接时，您还需要在连接时指定`--locator=hostname[port]` 到群集。 例如（如果你想尝试下一个命令，首先键入`disconnect`）:

```bash
gfsh>connect --locator=localhost[10334]
Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=ubuntu.local, port=1099] ..
Successfully connected to: [host=ubuntu.local, port=1099]
```

将`gfsh`连接到集群的另一种方法是直接连接到定位器内运行的JMX Manager。 例如（如果你想尝试下一个命令，首先键入`disconnect`）:

```bash
gfsh>connect --jmx-manager=localhost[1099]
Connecting to Manager at [host=localhost, port=1099] ..
Successfully connected to: [host=localhost, port=1099]
```

此外，您还可以通过HTTP连接到远程群集。 请参阅[使用gfsh通过HTTP或HTTPS管理远程群集](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html)。

**Step 5: 断开并关闭第二个终端窗口.** 键入以下命令以断开连接并退出第二个`gfsh`提示符:

```bash
gfsh>disconnect
Disconnecting from: localhost[1099]
Disconnected from : localhost[1099]
gfsh>exit
```

关闭第二个终端窗口。

**Step 6: 启动服务器.** 返回到第一个终端窗口，然后启动使用您在步骤2中启动的定位器的缓存服务器。键入以下命令:

```bash
gfsh>start server --name=server1 --locators=localhost[10334]
```

如果服务器成功启动，则会显示以下输出:

```bash
gfsh>start server --name=server1 --locators=localhost[10334]
Starting a Geode Server in /home/username/gfsh_tutorial/server1/server1.log...
...
Server in /home/username/gfsh_tutorial/server1 on 192.0.2.0[40404] as server1
is currently online.
Process ID: 68375
Uptime: 4 seconds
Geode Version: 1.7
Java Version: 1.8.0_121
Log File: /home/username//gfsh_tutorial/server1/server1.log
JVM Arguments: -Dgemfire.locators=localhost[10334]
 -Dgemfire.use-cluster-configuration=true -Dgemfire.start-dev-rest-api=false
 -XX:OnOutOfMemoryError=kill -KILL %p
 -Dgemfire.launcher.registerSignalHandlers=true
 -Djava.awt.headless=true -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
Class-Path: /home/username/geode/geode-assembly/build/install/apache-geode/lib
/geode-core-1.2.0.jar:/home/username/geode/geode-assembly/build/install
/apache-geode/lib/geode-dependencies.jar
```

如果从gfsh运行`start server`而不指定成员名称，gfsh将自动选择一个随机成员名称。 这对自动化很有用。

在文件系统中，检查执行`gfsh`的文件夹位置。 请注意，就像`start locator`命令一样，`start server`命令已经自动创建了一个工作目录（以服务器命名），并且在该工作目录中，它创建了一个日志文件和一个`.pid`（包含服务器的 进程ID）此缓存服务器。 此外，它还编写了日志文件。

**Step 7: 列出成员.** 使用`list members`命令查看刚刚创建的集群的当前成员。

```bash
gfsh>list members
  Name       | Id
------------ | ---------------------------------------
Coordinator: | ubuntu(locator1:5610:locator)<ec><v0>:34168
locator1     | ubuntu(locator1:5610:locator)<ec><v0>:34168
server1      | ubuntu(server1:5931)<v1>:35285
```

**Step 8: 通过执行`describe member`命令查看成员详细信息.**

```bash
gfsh>describe member --name=server1
Name        : server1
Id          : ubuntu(server1:5931)<v1>:35285
Host        : ubuntu.local
Regions     :
PID         : 5931
Groups      :
Used Heap   : 12M
Max Heap    : 239M
Working Dir : /home/username/gfsh_tutorial/server1
Log file    : /home/username/gfsh_tutorial/server1/server1.log
Locators    : localhost[10334]

Cache Server Information
Server Bind              :
Server Port              : 40404
Running                  : true
Client Connections       : 0
```

请注意，尚未为此成员分配任何区域。

**Step 9: 创建您的第一个区域.** 键入以下命令，然后键入Tab键:

```bash
gfsh>create region --name=region1 --type=
```

将显示可能的区域类型列表，后跟您输入的部分命令:

```bash
gfsh>create region --name=region1 --type=

PARTITION
PARTITION_REDUNDANT
PARTITION_PERSISTENT
PARTITION_REDUNDANT_PERSISTENT
PARTITION_OVERFLOW
PARTITION_REDUNDANT_OVERFLOW
PARTITION_PERSISTENT_OVERFLOW
PARTITION_REDUNDANT_PERSISTENT_OVERFLOW
PARTITION_HEAP_LRU
PARTITION_REDUNDANT_HEAP_LRU
REPLICATE
REPLICATE_PERSISTENT
REPLICATE_OVERFLOW
REPLICATE_PERSISTENT_OVERFLOW
REPLICATE_HEAP_LRU
LOCAL
LOCAL_PERSISTENT
LOCAL_HEAP_LRU
LOCAL_OVERFLOW
LOCAL_PERSISTENT_OVERFLOW
PARTITION_PROXY
PARTITION_PROXY_REDUNDANT
REPLICATE_PROXY

gfsh>create region --name=region1 --type=
```

使用您要创建的区域类型完成命令。 例如，创建一个本地区域:

```bash
gfsh>create region --name=region1 --type=LOCAL
Member  | Status
------- | --------------------------------------
server1 | Region "/region1" created on "server1"
```

由于此时集群中只有一台服务器，因此该命令会在server1上创建本地区域。

**Step 10: 启动其他服务器.** 这次使用不同的服务器端口指定`--server-port`参数，因为您在同一主机上启动了缓存服务器进程。

```bash
gfsh>start server --name=server2 --server-port=40405
Starting a Geode Server in /home/username/gfsh_tutorial/server2...
...
Server in /home/username/gfsh_tutorial/server2 on 192.0.2.0[40405] as
server2 is currently online.
Process ID: 68423
Uptime: 4 seconds
Geode Version: 1.7
Java Version: 1.8.0_121
Log File: /home/username/gfsh_tutorial/server2/server2.log
JVM Arguments: -Dgemfire.default.locators=192.0.2.0[10334]
 -Dgemfire.use-cluster-configuration=true -Dgemfire.start-dev-rest-api=false
 -XX:OnOutOfMemoryError=kill -KILL %p -Dgemfire.launcher.registerSignalHandlers=true
 -Djava.awt.headless=true -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
Class-Path: /home/username/geode/geode-assembly/build/install/apache-geode
/lib/geode-core-1.2.0.jar:/home/username/geode/geode-assembly/build/install
/apache-geode/lib/geode-dependencies.jar
```

**Step 11: 创建复制区域.**

```bash
gfsh>create region --name=region2 --type=REPLICATE
Member  | Status
------- | --------------------------------------
server1 | Region "/region2" created on "server1"
server2 | Region "/region2" created on "server2"
```

**Step 12: 创建分区区域.**

```bash
gfsh>create region --name=region3 --type=PARTITION
Member  | Status
------- | --------------------------------------
server1 | Region "/region3" created on "server1"
server2 | Region "/region3" created on "server2"
```

**Step 13: 创建一个复制的持久区域.**

```bash
gfsh>create region --name=region4 --type=REPLICATE_PERSISTENT
Member  | Status
------- | --------------------------------------
server1 | Region "/region4" created on "server1"
server2 | Region "/region4" created on "server2"
```

**Step 14: 列出区域.** 将显示您刚刚创建的所有区域的列表.

```bash
gfsh>list regions
List of regions
---------------
region1
region2
region3
region4
```

**Step 15: 通过执行describe member命令再次查看成员详细信息.**

```bash
gfsh>describe member --name=server1
Name        : server1
Id          : ubuntu(server1:5931)<v1>:35285
Host        : ubuntu.local
Regions     : region4
              region3
              region2
              region1
PID         : 5931
Groups      :
Used Heap   : 14M
Max Heap    : 239M
Working Dir : /home/username/gfsh_tutorial/server1
Log file    : /home/username/gfsh_tutorial/server1/server1.log
Locators    : localhost[10334]

Cache Server Information
Server Bind              :
Server Port              : 40404
Running                  : true
Client Connections       : 0
```

请注意，您现在创建的所有区域都显示在成员说明的“Regions”部分中。

```bash
gfsh>describe member --name=server2
Name        : server2
Id          : ubuntu(server2:6092)<v2>:17443
Host        : ubuntu.local
Regions     : region4
              region3
              region2
              region1
PID         : 6092
Groups      :
Used Heap   : 14M
Max Heap    : 239M
Working Dir : /home/username/gfsh_tutorial/server2
Log file    : /home/username/gfsh_tutorial/server2/server2.log
Locators    : 192.0.2.0[10334]

Cache Server Information
Server Bind              :
Server Port              : 40405
Running                  : true
Client Connections       : 0
```

请注意，即使您在创建第一个区域（region1）后启动了第二个服务器，第二个服务器仍会列出region1，因为它从群集配置服务中选择了其配置。

**Step 16: 将数据放进本地区域.** 输入以下put命令:

```bash
gfsh>put --key=('123') --value=('ABC') --region=region1
Result      : true
Key Class   : java.lang.String
Key         : ('123')
Value Class : java.lang.String
Old Value   : <NULL>
```

**Step 17: 将数据放在复制区域中.** 输入以下put命令:

```bash
gfsh>put --key=('123abc') --value=('Hello World!!') --region=region2
Result      : true
Key Class   : java.lang.String
Key         : ('123abc')
Value Class : java.lang.String
Old Value   : <NULL>
```

**Step 18: 检索数据.** 您可以使用`locate entry`,`query`或`get`来返回刚刚放入该区域的数据。

例如，使用`get`命令:

```bash
gfsh>get --key=('123') --region=region1
Result      : true
Key Class   : java.lang.String
Key         : ('123')
Value Class : java.lang.String
Value       : ('ABC')
```

例如，使用`locate entry`命令:

```bash
gfsh>locate entry --key=('123abc') --region=region2
Result          : true
Key Class       : java.lang.String
Key             : ('123abc')
Locations Found : 2


MemberName | MemberId
---------- | -------------------------------
server2    | ubuntu(server2:6092)<v2>:17443
server1    | ubuntu(server1:5931)<v1>:35285
```

请注意，由于条目已放入复制区域，因此该条目位于两个集群成员上。

例如，使用`query`命令:

```bash
gfsh>query --query='SELECT * FROM /region2'

Result     : true
startCount : 0
endCount   : 20
Rows       : 1

Result
-----------------
('Hello World!!')

NEXT_STEP_NAME : END
```

**Step 19: 导出您的数据.** 要保存区域数据，可以使用`export data`命令。

例如:

```bash
gfsh>export data --region=region1 --file=region1.gfd --member=server1
```

您可以稍后使用`import data`命令将该数据导入另一个成员的同一区域。

**Step 20: 关闭群集.**

```bash
gfsh>shutdown --include-locators=true
```


### 按功能区快速参考gfsh命令 {#Quick_Reference_of_gfsh_Commands_by_Functional_Area}
此快速参考将所有命令分类到功能区域。

单击命令可查看其他信息，包括语法，选项列表和示例。

- **Basic Geode gfsh Commands**
- **Configuration Commands**
- **Data Commands**
- **Deployment Commands**
- **Disk Store Commands**
- **Durable CQ and Client Commands**
- **Function Execution Commands**
- **Gateway (WAN) Commands**
- **Geode Monitoring Commands**
- **Index Commands**
- **JMX Connection Commands**
- **Locator Commands**
- **Lucene Commands**
- **PDX Commands**
- **Region Commands**
- **Server Commands**



#### 基本Geode gfsh命令 {#Basic_Geode_gfsh_Commands}


| 命令                                                      | 描述                                                  | 可用性    |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [debug](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/debug.html) | 在`gfsh`中启用或禁用调试输出.                | online, offline |
| [echo](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/echo.html) | 回显给定文本，其中可能包括系统和用户变量. | online, offline |
| [exit](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/exit.html) | 退出gfsh shell。 您也可以使用`quit`退出shell. | online, offline |
| [help](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/help.html) | 如果参数是gfsh命令，则显示该命令的语法和用法信息。 如果没有参数，则显示所有可用命令的列表. | online, offline |
| [hint](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/hint.html) | 显示有关主题的信息以及与主题关联的命令列表. | online, offline |
| [history](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/history.html) | 显示或保存命令历史记录.                            | online, offline |
| [run](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/run.html) | 执行一组GFSH命令.                              | online, offline |
| [set variable](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/set.html) | 在GFSH环境中设置变量.                       | online, offline |
| [sh](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/sh.html) | 执行操作系统（OS）命令.                     | offline, online |
| [sleep](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/sleep.html) | 延迟`gfsh`命令执行.                              | online, offline |
| [version](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/version.html) | 显示产品版本信息.                         | online, offline |

#### 配置命令 {#Configuration_Commands}


| 命令                                                      | 描述                                                  | 可用性                                                 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| [alter runtime](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_7E6B7E1B972D4F418CB45354D1089C2B) | 在一个或多个成员运行时更改特定成员的配置属性. | online                                                       |
| [change loglevel](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/change.html#topic_E74ED23CB60342538B2175C326E7D758) | 更改指定服务器上的日志记录级别.              | online                                                       |
| [configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh) | 此命令可更改所有高速缓存的群集范围的PDX配置设置. | online**注意:** 必须在启动数据成员之前运行此命令才能强制执行配置设置. |
| [describe config](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_3C2C817D999C4E40AF788808B7B6AF99) | 显示成员的配置.                       | online                                                       |
| [export config](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_C7C69306F93743459E65D46537F4A1EE) | 导出配置，数据，日志和堆栈跟踪.          | online                                                       |
| [export cluster-configuration](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_mdv_jgz_ck) | 导出共享配置ZIP文件，其中包含配置和操作集群所需的cache.xml文件，gemfire.properties文件和JAR文件. | online                                                       |
| [import cluster-configuration](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/import.html#topic_vnv_grz_ck) | 导入导出的配置.                            | online                                                       |
| [status cluster-config-service](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_ts1_qb1_dk2) | 报告群集配置服务器的状态.   | online                                                       |

#### 数据命令 {#Data_Commands}


| 命令                                                         | 描述                                      | 可用性 |
| :----------------------------------------------------------- | :---------------------------------------- | :----- |
| [export data](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_263B70069BFC4A7185F86B3272011734) | 将用户数据从区域导出到文件. | online |
| [get](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/get.html) | 显示区域中的条目.             | online |
| [import data](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/import.html#topic_jw2_2ld_2l) | 将用户数据从文件导入区域. | online |
| [locate entry](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/locate.html#concept_73B980C1138743DDBBFACE68009BD1E3__section_04BD7EC0032147DFA9CCD1331EE3B694) | 在成员上找到区域条目.        | online |
| [put](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/put.html) | 添加或更新区域条目.             | online |
| [query](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/query.html) | 对Geode区域运行查询.        | online |
| [remove](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/remove.html) | 从区域中删除条目.            | online |

#### 部署命令 {#Deployment_Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [deploy](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/deploy.html) | 将JAR打包的应用程序部署到一个或多个成员.     | online |
| [list deployed](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_59DF60DE71AD4097B281749425254BFF) | 显示使用`deploy`命令部署到成员的JAR列表. | online |
| [undeploy](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/undeploy.html) | 取消部署部署使用`deploy`命令部署在成员或群体的JAR文件. | online |

#### 磁盘存储命令 {#Disk_Store_Commands}


| 命令                                                         | 描述                                                         | 可用性          |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [alter disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_99BCAD98BDB5470189662D2F308B68EB) | 修改现有的Geode资源.                           | online          |
| [backup disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/backup.html#topic_E74ED23CB60342538B2175C326E7D758) | 将所有成员的持久数据备份到指定目录. | online          |
| [compact disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/compact.html#topic_F113C95C076F424E9AA8AC4F1F6324CC) | 压缩在线磁盘存储.                                  | online          |
| [compact offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/compact.html#topic_9CCFCB2FA2154E16BD775439C8ABC8FB) | 压缩脱机磁盘存储.                               | online, offline |
| [create disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_bkn_zty_ck) | 定义一个或多个磁盘存储的池，可供区域和客户端订阅队列使用. | online          |
| [describe disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_C635B500BE6A4F1D9572D0BC98A224F2) | 显示有关成员磁盘存储的信息.             | online          |
| [describe offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_kys_yvk_2l) | 显示有关脱机成员磁盘存储的信息     | online, offline |
| [destroy disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_yfr_l2z_ck) | 删除磁盘存储以及磁盘存储使用的磁盘上的所有文件。 先前使用此磁盘存储的封闭区域的数据将丢失. | online          |
| [list disk-stores](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_BC14AD57EA304FB3845766898D01BD04) | 列出Geode集群中的所有可用磁盘存储.           | online          |
| [revoke missing-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/revoke.html) | 指示集群成员停止等待磁盘存储可用. | online          |
| [show missing-disk-stores](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_7B3D624D5B4F41D1A0F8A9C3C8B2E780) | 显示群集中当前缺少的磁盘存储的摘要. | online          |
| [validate offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/validate.html) | 验证脱机磁盘存储.                                | online, offline |

#### 持久的CQ和客户端命令 {#Durable_CQ_and_Client_Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [list durable-cqs](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_66016A698C334F4EBA19B99F51B0204B) | 列出与指定的持久客户端ID关联的持久客户端CQ. | online |
| [close durable-cq](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/close.html#topic_1BC15B3132BA480DB227921A9B3ABDD1) | 关闭持久客户端注册的持久CQ，并从订阅队列中为持久CQ保留排除事件. | online |
| [close durable-client](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/close.html#topic_4125AAAB9FE44CD787166E48B694C41D) | 试图关闭持久客户端。 客户端必须断开连接. | online |
| [show subscription-queue-size](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_395C96B500AD430CBF3D3C8886A4CD2E) | 显示订阅队列中的事件数。 如果提供CQ名称，则它计算指定CQ的订阅队列中的事件数. | online |

#### 函数执行命令 {#Function_Execution_Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [destroy function](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_E48C2DF809054C12A162026D8A2139BB) | Destroy or unregister a function. The default is for the function to be unregistered from all members. | online |
| [execute function](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/execute.html#concept_F2ED1987126E4338AEB14A7C8F561A2A__section_FB435A8AC76E4E1A8E72DBEA64BA2E57) | Execute the function with the specified ID. By default, executes on all members. | online |
| [list functions](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_DCC7CCBBEF5942B783A8F2A4A5B2FABF) | Display a list of registered functions. The default is to display functions for all members. | online |

#### 网关(WAN)命令 {#Gateway__WAN__Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [create async-event-queue](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_ryz_pb1_dk) | 创建异步事件队列.                         | online |
| [create gateway-receiver](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_a4x_pb1_dk) | 在一个或多个成员上创建网关接收器.           | online |
| [create gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_hg2_bjz_ck) | 在一个或多个成员上创建网关发件人.             | online |
| [destroy gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy-gs) | 销毁一个或多个成员上的网关发件人.            | online |
| [list async-event-queues](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_j22_kzk_2l) | 显示所有成员的异步事件队列列表.        | online |
| [list gateways](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_B1D89671C7B74074899C7D52F15849ED) | 显示一个或多个成员的网关发件人和收件人. | online |
| [load-balance gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/load-balance.html#concept_fn2_qls_5q) | 使指定的网关发送方关闭其当前连接并以更平衡的方式重新连接到远程网关接收方. | online |
| [pause gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/pause.html#concept_44B6EC91FD464A1A91E438D6DF65C9D6) | 暂停网关发件人.                                      | online |
| [resume gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/resume.html#concept_8B0532D13CF34E0CB04ACC3F14F1E4A2) | 恢复已暂停的所有网关发件人.             | online |
| [start gateway-receiver](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_67738A5B68E84DEE95D1C92DAB2E26E5) | 在给定成员或成员组上启动网关接收器. | online |
| [start gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_AB8BA3F42B9645A8BE9BD97CE2F839A8) | 在一个或多个成员上启动网关发件人.             | online |
| [status gateway-receiver](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_B0F45DC2D5F64FB1A2F738206BC6539E) | 显示指定网关接收器的状态.        | online |
| [status gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_6F539877F0564F05AF264A9E704EC842) | 显示指定网关发件人的状态.          | online |
| [stop gateway-receiver](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/stop.html#topic_CD1D526FD6F84A7B80B25C741129ED30) | 停止一个或多个成员的网关接收器.            | online |
| [stop gateway-sender](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/stop.html#topic_0BBDD4B3B8A44A65A610F766C9E85519) | 在指定成员或指定成员组成员上具有给定ID的网关发件人. | online |

#### Geode 异步事件队列命令 {#Geode_AsyncEventQueue_Commands}


| 命令                                                         | 描述                                                  | 可用性 |
| :----------------------------------------------------------- | :---------------------------------------------------- | :----- |
| [create async-event-queue](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_ryz_pb1_dk) | 创建异步事件队列.                  | online |
| [list async-event-queues](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_j22_kzk_2l) | 显示所有成员的异步事件队列列表. | online |

#### Geode监控命令 {#Geode_Monitoring_Commands}


| 命令                                                         | 描述                                                         | 可用性          |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [describe client](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_gyr_jgz_ck) | 显示指定客户端的详细信息.                   | online          |
| [describe member](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_D62F3D42B1D84CF68F03D54D5122806A) | 显示具有给定名称/ID的成员的详细信息.              | online          |
| [export logs](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_B80978CC659244AE91E2B8CE56EBDFE3) | 将日志导出/转储到给定目录.                       | online          |
| [export stack-traces](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_195D27B8B2B64A4E84CF2256636D54BD) | 导出一个或多个成员的堆栈跟踪.              | online          |
| [gc](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/gc.html) | 强制成员或成员的垃圾回收.             | online          |
| [list clients](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_ts1_qb1_dk) | 显示已连接客户端的列表.                        | online          |
| [list members](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_5B5BFB2E5F314210858641BE3A689637) | 显示全部或部分成员.                          | online          |
| [netstat](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/netstat.html) | 通过“netstat”操作系统命令报告网络信息和统计信息. | online          |
| [show dead-locks](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_1125347FAD6541DF995C9999650165B1) | 显示死锁，日志，指标和丢失的磁盘存储.    | online          |
| [show log](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_45AAEDAC3AFF46EC9BB68B24FC9A32B3) | 显示成员的日志.                                | online          |
| [show metrics](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_6EB786C63AEB46179EEE8FA18624295A) | 显示或导出整个群集，成员或区域的度量标准. | online          |
| [shutdown](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/shutdown.html) | 关闭所有具有缓存的成员.                     | online          |
| [start jconsole](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_D00507416F3944DFAB48D2FA2B9E4A31) | 在单独的进程中启动JDK JConsole监视应用程序。 JConsole会自动连接到正在运行的JMX Manager节点（如果有） | online, offline |
| [start jvisualvm](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_5B5BF8BEE905463D8B7762B89E2D65E7) | 在单独的进程中启动JDK的Java VisualVM监视应用程序. | online, offline |
| [start pulse](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_E906BA7D9E7F4C5890FEFA7ECD40DD77) | 在用户的默认系统浏览器中启动Geode Pulse监控仪表板工具. | online, offline |

#### 索引命令 {#Index_Commands}


| 命令                                                         | 描述                                                         | 可用性          |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [clear defined indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/clear.html) | 清除所有已定义的索引.                              | online, offline |
| [create defined indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_w2t_l3m_qq) | 创建所有已定义的索引.                             | online          |
| [create index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_960A5B6FD3D84E1881EE118E299DD12D) | 创建可在执行查询时使用的索引.     | online          |
| [define index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/define.html) | 定义可在执行查询时使用的索引。 然后，您可以一次创建多个索引. | online, offline |
| [destroy index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_D00219CCD6F64C1582A0802AC5CDF3F3) | 销毁或删除指定的索引.                       | online          |
| [list indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_B3B51B6DEA484EE086C4F657EC9831F2) | 显示为所有成员创建的索引列表.         | online          |

#### JMX连接命令 {#JMX_Connection_Commands}


| 命令                                                         | 描述                                                       | 可用性          |
| :----------------------------------------------------------- | :--------------------------------------------------------- | :-------------- |
| [connect](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/connect.html) | 直接或通过定位器连接到jmx-manager. | offline         |
| [describe connection](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_591DC6B781B641168E6173E69AC6D201) | 显示连接信息详细信息.                    | online, offline |
| [disconnect](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/disconnect.html) | 关闭所有活动连接.                            | online          |

#### 定位器命令 {#Locator_Commands}


| 命令                                                         | 描述                                                         | 可用性          |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [start locator](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_591260CF25D64562A0EDD7260D2AC6D4) | 启动定位器。 该命令创建一个以定位符命名的子目录和日志文件。 如果定位器检测到不存在其他JMX Manager，则定位器将自动启动嵌入式JMX Manager并将当前的`gfsh`会话连接到JMX Manager. | online, offline |
| [status locator](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_E96D0EFA513C4CD79B833FCCDD69C832) | 显示指定定位器的状态。                | online, offline |
| [stop locator](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/stop.html#topic_EF61C54B35BA4AB7B14E58CF911F283E) | 停止定位器.                                              | online, offline |

#### Lucene 命令 {#Lucene_Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [create lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#create_lucene_index) | 创建Lucene索引.                                       | online |
| [describe lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#describe_lucene_index) | 描述一个Lucene索引.                                     | online |
| [destroy lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy_lucene_index) | 销毁Lucene索引.                                      | online |
| [list lucene indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#list_lucene_indexes) | 列出为所有成员创建的Lucene索引。 可选的`--with-stats`限定符显示索引上的活动. | online |
| [search lucene](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/search.html#search_lucene) | 搜索Lucene索引.                                       | online |

#### PDX 命令 {#PDX_Commands}


| 命令                                                         | 描述                                                         | 可用性          |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :-------------- |
| [configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh) | 为群集中的所有缓存配置Portable Data eXchange. | online, offline |
| [pdx rename](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/pdx.html) | 在脱机磁盘存储中重命名PDX类型.                  | online, offline |

#### 区域 命令 {#Region_Commands}


| 命令                                                         | 描述                                                         | 可用性 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| [alter region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_E74ED23CB60342538B2175C326E7D758) | 改变区域的配置。                        | online |
| [create region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_54B0985FEC5241CA9D26B0CE0A5EA863) | 创建和配置区域.                               | online |
| [describe region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_DECF7D3D33F54071B6B8AD4EA7E3F90B) | 显示区域的属性和关键信息.      | online |
| [destroy region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_BEDACECF4599407794ACBC0E56B30F65) | 销毁或删除某个地区.                                  | online |
| [list regions](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_F0ECEFF26086474498598035DD83C588) | 显示成员或成员的区域。 如果未指定参数，则列出群集中的所有区域. | online |
| [rebalance](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/rebalance.html) | 重新平衡分区区域.                               | online |

#### 服务器命令 {#Server_Commands}


| 命令                                                         | 描述                                                    | 可用性          |
| :----------------------------------------------------------- | :------------------------------------------------------ | :-------------- |
| [start server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_3764EE2DB18B4AE4A625E0354471738A) | 启动Geode缓存服务器进程.                     | online, offline |
| [status server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_E5DB49044978404D9D6B1971BF5D400D) | 显示指定的Geode缓存服务器的状态. | online, offline |
| [stop server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/stop.html#topic_723EE395A63A40D6819618AFC2902115) | 停止Geode缓存服务器.                              | online, offline |



### gfsh命令帮助 {#gfsh_Command_Help}


本节提供按字母顺序列出的所有`gfsh`命令的帮助和用法信息。

- **alter**

  修改现有的Geode资源。

- **backup disk-store**

  将所有成员的持久数据备份到指定目录。

- **change loglevel**

  更改指定成员的日志记录级别。

- **clear defined indexes**

  清除所有已定义的索引。

- **close**

  关闭持久客户CQ和持久客户。

- **compact**

  压缩在线和离线磁盘存储。

- **configure**

  为群集中的所有缓存配置Portable Data eXchange。

- **connect**

  直接或通过定位器连接到jmx-manager。

- **create**

  创建异步事件队列，磁盘存储，网关接收器，网关发件人，索引和区域。

- **debug**

  在`gfsh`中启用或禁用调试输出。

- **define index**

  定义可在执行查询时使用的索引。 然后，您可以使用`create defined indexes`执行单个命令以一次创建多个索引。

- **deploy**

  将JAR打包的应用程序部署到一个或多个成员。

- **describe**

  显示成员配置，shell连接，磁盘存储，成员或区域的详细信息。

- **destroy**

  删除或取消注册功能，删除索引，磁盘存储和区域。

- **disconnect**

  关闭所有活动连接。

- **echo**

  回显给定文本，其中可能包括系统和用户变量。

- **execute function**

  在成员或区域上执行功能。

- **exit**

  退出`gfsh` shell。 您也可以使用`quit`退出shell。

- **export**

  导出配置，数据，日志和堆栈跟踪。

- **gc**

  对一个或多个成员强制GC（垃圾收集）。

- **get**

  显示区域中的条目。

- **help**

  显示所有可用命令的语法和用法信息。

- **hint**

  显示有关主题的信息以及与主题关联的命令列表。

- **history**

  显示或保存命令历史记录。

- **import**

  您可以将数据导入区域或将现有群集配置导入群集。

- **list**

  列出现有的Geode资源，例如已部署的应用程序，磁盘存储，功能，成员，服务器和区域。

- **load-balance gateway-sender**

  使指定的网关发送方关闭其当前连接并以更平衡的方式重新连接到远程网关接收方。

- **locate entry**

  在成员上找到区域条目。

- **netstat**

  通过“netstat”操作系统命令报告网络信息和统计信息。

- **pause gateway-sender**

  暂停网关发件人。

- **pdx rename**

  在脱机磁盘存储中重命名PDX类型。

- **put**

  添加或更新区域条目。

- **query**

  对Geode区域运行查询。

- **rebalance**

  重新平衡分区区域。

- **remove**

  从区域中删除条目。

- **resume gateway-sender**

  恢复已暂停的所有网关发件人。

- **revoke missing-disk-store**

  指示群集的成员停止等待磁盘存储可用。

- **run**

  执行一组GFSH命令。

- **set variable**

  在GFSH环境中设置变量。

- **sh**

  执行操作系统命令。

- **show**

  显示死锁，日志，指标和丢失的磁盘存储。

- **shutdown**

  停止所有成员。

- **sleep**

  延迟`gfsh`命令执行。

- **start**

  启动服务器，定位器，网关发送器和网关接收器以及监视工具。

- **status**

  检查群集配置服务和Geode成员进程的状态，包括定位器，网关接收器，网关发件人和服务器。

- **stop**

  停止网关接收器，网关发送器，定位器和服务器。

- **undeploy**

  取消使用`deploy`命令部署在成员或组上部署的JAR文件。

- **validate offline-disk-store**

  验证脱机磁盘存储。

- **version**

  显示产品版本信息。



#### alter {#alter}
Modify an existing Geode resource.

- **alter async-event-queue**

  修改异步事件队列的属性

- **alter disk-store**

  修改或删除脱机磁盘存储中的区域。

- **alter region**

  改变区域的配置。

- **alter runtime**

  在成员或成员运行时更改所有成员或成员子集的配置属性。

##### alter async-event-queue {#alter_async_event_queue}
更改指定的异步事件队列的属性。 必须重新启动托管指定异步事件队列的每个服务器，新属性设置才能在该服务器上生效。

**可用性:** Online. 必须在gfsh中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
alter async-event-queue --id=value [--batch-size=value] [--batch-time-interval=value]
    [--max-queue-memory=value] [--if-exists(=value)]
```

必需的选项`--id`标识要更改的异步事件队列。

**Parameters, alter async-event-queue**

| 名称                  | 描述                                                  |
| :-------------------- | :----------------------------------------------------------- |
| ‑‑id                  | *Required.* 要更改的异步事件队列的ID        |
| ‑‑batch‑size          | 批处理可以包含的最大事件数            |
| ‑‑batch‑time‑interval | 在批次交付之前可以经过的最长时间（以毫秒为单位） |
| ‑‑max‑queue‑memory    | 在溢出到磁盘之前队列可以消耗的最大内存量（以兆字节为单位） |
| ‑‑if‑exists           | 如果指定的异步事件队列不存在，gfsh将响应该消息。 如果此参数为true，则响应的前缀为"Skipping:"标签。 对脚本测试很有用。 默认值（如果未指定参数）:false。 默认值（如果指定的参数没有值）:true。 |

**示例命令:**

```bash
alter async-event-queue --id=myAsyncEventQueue --batch-size=50 --if-exists
```

##### alter disk-store {#alter_disk_store}
修改或删除脱机磁盘存储中的区域。

修改区域的配置时，通常会使该区域脱机并使用新配置重新启动。 您可以使用`alter disk-store`命令更改磁盘存储中存储的区域的配置，以匹配您在重新启动时使用的配置。

**可用性:** Offline.

**句法:**

```bash
alter disk-store --name=value --region=value --disk-dirs=value(,value)*
    [--compressor(=value)] [--concurrency-level=value]
    [--enable-statistics=value] [--initial-capacity=value] [--load-factor=value]
    [--lru-algorithm=value] [--lru-action=value] [--lru-limit=value]
    [--off-heap(=value)] [--remove(=value)]
```

三个必需的选项`--name`，`--region`和`--disk-dirs`标识要更改的磁盘存储区和区域。 如果未指定其他选项，`gfsh`将显示当前配置而不进行任何更改。

**Parameters, alter disk-store**

| 名称                | 描述                                                  |
| :------------------ | :----------------------------------------------------------- |
| --name              | *Required*. 将更改其内容的磁盘存储的名称。 |
| --region            | *Required*. 使用磁盘存储区域的名称（包括路径）。 |
| --disk-dirs         | *Required*. 先前已写入磁盘存储数据的目录。 |
| --compressor        | 压缩区域条目值时要使用的压缩程序的完全限定类名。 值为`none`会删除压缩器。 |
| --concurrency-level | 估计将同时访问区域条目的最大应用程序线程数。 与`--initial-capacity`和`--load-factor`一起，在用于存储区域条目的底层`java.util.ConcurrentHashMap`上设置参数。 此属性不适用于分区区域。 |
| --enable-statistics | 启用`--region`选项指定的区域的统计信息。 有效值为true或false。 如果指定的参数没有值，则使用true值。 |
| --initial-capacity  | 与`--concurrency-level`和`--load-factor`一起，在用于存储区域条目的底层`java.util.ConcurrentHashMap`上设置参数。 |
| --load-factor       | 与`--concurrency-level`和`--initial-capacity`一起，在用于存储区域条目的底层`java.util.ConcurrentHashMap`上设置参数。 这必须是0到1之间的浮点数，包括0和1。 |
| --lru-action        | 驱逐该地区的条目时要采取的行动。 有效值为:`none`,`overflow-to-disk`,`local-destroy` |
| --lru-algorithm     | 最近最少使用逐出算法。 有效类型是:`none`,`lru-entry-count`,`lru-heap-percentage`,`lru-memory-size` |
| --lru-limit         | 驱逐前该地区允许的条目数量。 |
| --off-heap          | 指定区域值是在堆内存还是堆外内存中。 如果为true，则区域值位于堆外内存中。 如果指定的参数没有值，则使用true值。 |
| --remove            | 指定是否从磁盘存储中删除该区域。 如果指定的参数没有值，则使用true值。 注意: `--remove`删除该区域的所有持久数据。 如果您可能希望在以后检索数据，请考虑在使用此选项之前将磁盘存储文件复制到备份。 |

**示例命令:**

```bash
alter disk-store --name=DiskStore1 --region=region1 --disk-dirs=/Disks/DiskStore1 --off-heap
alter disk-store --name=DiskStore1 --region=region1 --disk-dirs=/Disks/DiskStore1 --remove
```

##### alter region {#alter_region}
改变区域的配置。

有关语法详细信息，请参阅[在命令行选项中指定JSON](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/json_in_gfsh.html)。

**可用性:** Online. 必须在gfsh中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
alter region --name=value [--groups=value(,value)*]
    [--entry-idle-time-expiration=value]
    [--entry-idle-time-expiration-action(=value)?]
    [--entry-time-to-live-expiration=value]
    [--entry-time-to-live-expiration-action(=value)?]
    [--entry-idle-time-custom-expiry=value] [--entry-time-to-live-custom-expiry=value]
    [--region-idle-time-expiration=value]
    [--region-idle-time-expiration-action(=value)?]
    [--region-time-to-live-expiration=value]
    [--region-time-to-live-expiration-action(=value)?]
    [--cache-listener=value(,value)*] [--cache-loader=value]
    [--cache-writer=value] [--async-event-queue-id=value(,value)*]
    [--gateway-sender-id=value(,value)*] [--enable-cloning(=value)?]
    [--eviction-max(=value)?]
```

**Parameters, alter region**

| 名称                                   | 描述                                                  | 默认值 |
| :------------------------------------- | :----------------------------------------------------------- | :------------ |
| --async-event-queue-id                 | 将用于后写操作的异步事件队列的ID。 |               |
| --cache-listener                       | 要实例化的插件的完全限定类名，用于接收对区域及其条目的更改的事件后通知。 可以配置任意数量的缓存侦听器。 完全限定的类名可以附加一个JSON规范，该规范将被解析成为实现`Declarable`接口的类的`init()`方法的参数字段。 |               |
| --cache-loader                         | 要实例化的插件的完全限定类名，用于接收区域中缓存未命中的通知。 最多可以在该区域的每个成员中定义一个缓存加载器。 对于分布式区域，可以从具有定义区域的其他成员远程调用缓存加载器。 完全限定的类名可以附加一个JSON规范，该规范将被解析成为实现`Declarable`接口的类的`initialize()`方法的参数字段。 |               |
| --cache-writer                         | 要实例化的插件的完全限定类名，用于接收区域及其条目更改的事件前通知。 插件可能会取消该事件。 最多可以在该区域的每个成员中定义一个缓存写入器。 完全限定的类名可以附加一个JSON规范，该规范将被解析为实现`Declarable`接口的类的`init()`方法的参数字段。 |               |
| --enable-cloning                       | 确定`fromDelta`如何将增量应用于本地缓存以进行增量传播。 如果为true，则将更新应用于值的克隆，然后将克隆保存到缓存中。 如果为false，则在缓存中就地修改该值。 | `false`       |
| --entry-idle-time-expiration           | 区域或条目到期之前的秒数。 指定`-1`表示此类型没有到期 | -1            |
| --entry-idle-time-expiration-action    | 应在区域或条目到期时发生的操作。选择以下过期操作之一:`local-destroy`，从本地缓存中删除区域或条目，但不将删除操作分发给远程成员。 您不能对分区区域条目使用此操作。 `destroy`，从缓存中完全删除区域或条目。 根据区域的分布设置分发销毁操作。 当群集中的任何应用程序不再需要区域或条目时，请使用此选项。使默认到期操作无效。 将区域中的条目或所有条目标记为无效。 根据区域范围分配失效。 当区域或条目不再对群集中的任何应用程序有效时，这是正确的选择。 `local-invalidate`将区域中的条目或所有条目标记为无效，但不分发操作。 您不能对分区区域条目使用此操作。 仅对未配置为复制区域的区域支持本地区域失效。 | `invalidate`  |
| –entry-time-to-live-expiration         | 在区域或项过期前的秒数。指定`-1`表示该类型没有过期。 | -1            |
| –entry-time-to-live-expiration-action  | 应在区域或条目到期时执行的操作。选择以下过期操作之一: `local-destroy`从本地缓存中删除区域或条目，但不将删除操作分发给远程成员。您不能对分区区域条目使用此操作。 `destroy`从缓存中完全删除区域或条目。根据区域的分布设置分发销毁操作。当群集中的任何应用程序不再需要区域或条目时，请使用此选项。 `invalidate`默认到期操作。将区域中的条目或所有条目标记为无效。根据区域范围分配失效。当区域或条目不再对群集中的任何应用程序有效时，这是正确的选择。 `local-invalidate`将区域中的条目或所有条目标记为无效，但不分发操作。您不能对分区区域条目使用此操作。仅对未配置为复制区域的区域支持本地区域失效。 | `invalidate`  |
| –entry-idle-time-custom-expiry         | 为入口空闲时间实现CustomExpiry的类的名称。 为初始化属性附加JSON字符串。 |               |
| –entry-time-to-live-custom-expiry      | 实现CustomExpiry以进入生存时间的类的名称。 为初始化属性附加JSON字符串。 |               |
| –eviction-max                          | 驱逐算法用于确定何时执行其驱逐操作的驱逐属性的最大值。 最大值的单位由驱逐算法确定。 | 0             |
| –gateway-sender-id                     | 数据路由的网关发件人的ID。             |               |
| –groups                                | 该地区将被改变的成员组。        |               |
| –name                                  | Required. 该地区的名称（包括路径）。               |               |
| –region-idle-time-expiration           | 区域或条目到期之前的秒数。 如果未指定超时，则默认为零（这意味着没有到期）。 | -1            |
| –region-idle-time-expiration-action    | 应在区域或条目到期时执行的操作。选择以下过期操作之一:`local-destroy`从本地缓存中删除区域或条目，但不将删除操作分发给远程成员。您不能对分区区域条目使用此操作。 `destroy`从缓存中完全删除区域或条目。根据区域的分布设置分发销毁操作。当群集中的任何应用程序不再需要区域或条目时，请使用此选项。 `invalidate`默认到期操作。将区域中的条目或所有条目标记为无效。根据区域范围分配失效。当区域或条目不再对群集中的任何应用程序有效时，这是正确的选择。 `local-invalidate`将区域中的条目或所有条目标记为无效，但不分发操作。您不能对分区区域条目使用此操作。仅对未配置为复制区域的区域支持本地区域失效。 | `invalidate`  |
| –region-time-to-live-expiration        | 区域或条目到期之前的秒数。 如果未指定超时，则默认为零（这意味着没有到期）。 | -1            |
| –region-time-to-live-expiration-action | 应在区域或条目到期时执行的操作。选择以下过期操作之一:`local-destroy`从本地缓存中删除区域或条目，但不将删除操作分发给远程成员。您不能对分区区域条目使用此操作。 `destroy`从缓存中完全删除区域或条目。根据区域的分布设置分发销毁操作。当群集中的任何应用程序不再需要区域或条目时，请使用此选项。 `invalidate`默认到期操作。将区域中的条目或所有条目标记为无效。根据区域范围分配失效。当区域或条目不再对群集中的任何应用程序有效时，这是正确的选择。 `local-invalidate`将区域中的条目或所有条目标记为无效，但不分发操作。您不能对分区区域条目使用此操作。仅对未配置为复制区域的区域支持本地区域失效。 | `invalidate`  |



**示例命令:**

```bash
alter region --name=region1 --eviction-max=5000 [-group=all]
```

**样本输出:**

```bash
gfsh>alter region --name=customer --eviction-max=5000
Member  | Status
------- | ----------------------------------
server1 | Region "/customer" altered on "server1"
```

##### alter runtime {#alter_runtime}
在成员或成员运行时更改所有成员或成员子集的配置属性。

有关这些配置属性的更多信息，请参阅[cache.xml](https://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_cache_xml.html#cache_xml) 和配置参数参考。

**可用性:** Online.  您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
alter runtime [--members=value(,value)*] [--groups=value(,value)*]
    [--archive-disk-space-limit=value]
    [--archive-file-size-limit=value] [--log-disk-space-limit=value]
    [--log-file-size-limit=value] [--log-level=value]
    [--statistic-archive-file=value] [--statistic-sample-rate=value]
    [--enable-statistics=value] [--copy-on-read(=value)?] [--lock-lease=value]
    [--lock-timeout=value] [--message-sync-interval=value] [--search-timeout=value]
```

**Parameters, alter runtime**

| 名称                      | 描述                                                  | 默认值                                                |
| :------------------------ | :----------------------------------------------------------- | :----------------------------------------------------------- |
| –members                  | 要在运行时更改其配置的成员的名称或ID。 如果未指定此参数，则会使用群集配置服务为所有群集成员修改配置属性。 | 如果未指定，则使用群集配置服务的所有成员 |
| –groups                   | 要更改其成员的运行时配置的组的名称。 如果未指定此参数，则会使用群集配置服务为所有群集成员修改配置属性。 | 如果未指定，则使用群集配置服务的所有成员 |
| –archive-disk-space-limit | 归档磁盘空间限制。 组合的所有非活动统计归档文件的最大大小（以兆字节为单位）。 如果超出此限制，则会删除非活动归档文件，最先删除，直到总大小在限制范围内。 如果设置为零，则磁盘空间使用不受限制。 有效值为（以兆字节为单位）:0  -  1000000。 | 0                                                            |
| –archive-file-size-limit  | 存档文件大小限制。 单个统计存档文件的最大大小（以兆字节为单位）。 超过此限制后，将创建新的统计存档文件，并且当前存档文件将变为非活动状态。 如果设置为零，则文件大小不受限制。 有效值为（以兆字节为单位）:0  -  1000000。 | 0                                                            |
| –log-disk-space-limit     | 记录磁盘空间限制。 所有非活动日志文件的最大大小（以兆字节为单位）。 如果超出此限制，则会删除非活动日志文件，这是最早的，直到总大小在限制范围内。 如果设置为零，则磁盘空间使用不受限制。 有效值为（以兆字节为单位）:0  -  1000000。 | 0                                                            |
| –log-file-size-limit      | 日志文件大小限制。 日志文件关闭之前的最大大小（兆字节），并且日志记录将滚动到新的（子）日志文件。 如果设置为零，则禁用日志滚动。 有效值为（以兆字节为单位）:0  -  1000000。 | 0                                                            |
| –loglevel                 | 新的日志级别。 此选项是必需的，您必须指定一个值。 有效值包括:`ALL`，`TRACE`，`DEBUG`，`INFO`，`WARN`，`ERROR`，`FATAL`，`OFF`。 | INFO                                                         |
| –statistic-archive-file   | 正在运行的系统成员写入统计样本的文件。 例如:“StatisticsArchiveFile.gfs”。 必须定义为将归档存储到文件。 将`.gz`后缀添加到文件名会导致它被压缩。 请参阅[统计](https://geode.apache.org/docs/guide/17/managing/statistics/chapter_overview.html)。 | *not set*                                                    |
| –statistic-sample-rate    | 统计抽样率。 有效值为（以毫秒为单位）:100 - 60000.请参阅[统计](https://geode.apache.org/docs/guide/17/managing/statistics/chapter_overview.html)。 | 1000                                                         |
| –enable-statistics        | 是否应启用统计抽样。 指定`--statistic-archive-file`将统计信息存储到文件中。 有效值为:`true`和`false`。 请参阅[统计](https://geode.apache.org/docs/guide/17/managing/statistics/chapter_overview.html)。 | false                                                        |
| –copy-on-read             | 对或错。 设置高速缓存读取操作的“读取时复制”功能。 请参阅[安全条目修改](https://geode.apache.org/docs/guide/17/basic_config/data_entries_custom_classes/managing_data_entries.html#managing_data_entries__section_A0E0F889AC344EFA8DF304FD64418809)。 | false                                                        |
| –lock-lease               | 设置此缓存获取的分布式锁定租约的长度（以秒为单位）。 请参阅[设置缓存超时](https://geode.apache.org/docs/guide/17/managing/monitor_tune/performance_controls_setting_cache_timeouts.html#perf)。 | 120                                                          |
| –lock-timeout             | 设置高速缓存操作在超时之前可等待以获取分布式锁定租约的秒数。 请参阅[设置缓存超时](https://geode.apache.org/docs/guide/17/managing/monitor_tune/performance_controls_setting_cache_timeouts.html#perf)。 | 60                                                           |
| –message-sync-interval    | 设置主缓存服务器节点将消息发送到所有辅助缓存服务器节点的频率（以秒为单位），以删除已从队列调度的事件。 请参阅[更改服务器队列同步频率](https://geode.apache.org/docs/guide/17/developing/events/ha_event_messaging_whats_next.html#ha_event_messaging_whats_next__section_741052B413F24F47A14F5B7D7955F0AA)。 | 1                                                            |
| –search-timeout           | 设置缓存获取操作可用于搜索值的秒数。 请参阅[设置缓存超时](https://geode.apache.org/docs/guide/17/managing/monitor_tune/performance_controls_setting_cache_timeouts.html#perf)。 | 300                                                          |



**示例命令:**

```bash
alter runtime --members=server1 --loglevel=WARN --enable-statistics=true
```

**样本输出:**

```bash
gfsh>alter runtime --members=server1 --loglevel=WARN --enable-statistics=true
Runtime configuration altered successfully for the following member(s)
192.0.2.0(server1:240)<v1>:64871
```



#### backup disk-store {#backup_disk_store}
将所有成员的持久数据备份到指定目录。

指定的目录必须存在于所有成员上，但它可以是每台计算机上的本地目录。 此命令可确保备份文件不会被并发操作损坏。 建议不要使用操作系统复制命令备份正在运行的系统。

您还可以使用此命令执行增量备份。 有关增量备份的详细信息，请参阅[为系统恢复和操作管理创建备份](https://geode.apache.org/docs/guide/17/managing/disk_storage/backup_restore_disk_store.html#backup_restore_disk_store)。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
backup disk-store --dir=value [--baseline-dir=value]
```



| 名称           | 描述                                                  |
| :------------- | :----------------------------------------------------------- |
| --dir          | *Required.* 写入备份文件的目录。     |
| --baseline-dir | 包含用于在增量备份期间进行比较的基准备份的目录。增量备份操作会备份`--baseline-dir`中指定的目录中不存在的任何数据。 如果该成员找不到以前备份的数据，或者先前备份的数据已损坏，则该命令会对该成员执行完全备份。 |

Table 1. 备份磁盘存储参数

**示例命令:**

```bash
backup disk-store --dir=data/backups

backup disk-store --dir=data/backup/disk-store --baselineDir=data/backups/2012-09-24-17-08-50
```

**样本输出:**

```bash
gfsh>backup disk-store --dir=data/backups
The following disk stores were backed up successfully

Member  |                 UUID                 |              Directory               | Host
------- | ------------------------------------ | ------------------------------------ | ---------------
server2 | a6bb11f0-0baa-45c9-b23e-64876d02a586 | c:\PivotalGemFire70\Latest\server2\. | 192.0.2.0
server1 | 8dc365bd-c086-4af4-99d0-86b0b521aa04 | c:\PivotalGemFire70\Latest\server1\. | 192.0.2.0
```



#### change loglevel {#change_loglevel}
更改指定成员的日志记录级别。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
change loglevel --loglevel=value [--members=value(,value)*] [--groups=value(,value)*]
```



| Name       | Description                                                  | Default Value |
| :--------- | :----------------------------------------------------------- | :------------ |
| --members  | Name or ID of one or more member(s) whose logging level you want to change. |               |
| --groups   | One or more group names. The logging level changes for all members of these groups. |               |
| --loglevel | *Required.* Log level to change. Valid options are: `ALL`, `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`. |               |

Table 1. 更改Loglevel参数

**示例命令:**

```bash
gfsh>change loglevel --loglevel=DEBUG --members=server1
```

**样本输出:**

```bash
gfsh>change loglevel --loglevel=DEBUG --members=server1

Summary

                Member            | Changed log-level
--------------------------------- | -----------------
192.0.2.0(server1:3060)<v1>:24653 | true
```



#### clear defined indexes {#clear_defined_indexes}
清除所有已定义的索引。

索引定义本地存储在`gfsh`客户端上。 如果要创建一组新索引，或者如果一个或多个索引创建失败，则可能需要清除定义

另请参见[define index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/define.html)。

**可用性:** Online or offline.

**句法:**

```bash
clear defined indexes
```

**示例命令:**

```bash
gfsh> clear defined indexes
```

**样本输出:**

```bash
gfsh>clear defined indexes
Index definitions successfully cleared
```



#### close {#close}
关闭持久客户CQ和持久客户。

- **close durable-client**

  试图关闭一个持久的客户端。 必须断开客户端才能使此命令起作用。

- **close durable-cq**

  关闭持久客户端注册的持久连续查询（CQ），并从订阅队列中排除为持久CQ保留的事件。

##### close durable-client {#close_durable_client}
试图关闭一个持久的客户端。 必须断开客户端才能使此命令起作用。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
close durable-client --durable-client-id=value [--members=value(,value)*]
[--groups=value(,value)*]
```



| 名称                | 描述                                                  |
| :------------------ | :----------------------------------------------------------- |
| --durable-client-id | *Required.* 持久客户端的ID.                    |
| --members           | 要关闭持久客户端的成员的名称或ID. |
| --groups            | 持久客户将被关闭的成员组. |

Table 1. 关闭持久客户端参数

**示例命令:**

```bash
close durable-client --durable-client-id=client1
```

**样本输出:**

```bash
gfsh>close durable-client --durable-client-id=client1
Closed the durable client : "client1". on following members.
1.server4
2.server3
```

**错误消息:**

```bash
gfsh>close durable-cq --durable-cq-name=cq1 --durable-client-id=client1

Could not close the durable-cq : "cq1" for the durable-client-id : "client1" due to following reasons.

CacheClientProxy: Could not drain cq cq1 because client proxy id client1 is connected.
Occurred on members
1.server4
2.server3

No client found with client-id : client1
Occurred on members
1.server1
```

##### close durable-cq {#close_durable_cq}
关闭持久客户端注册的持久连续查询（CQ），并从订阅队列中排除为持久CQ保留的事件。

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```bash
close durable-cq --durable-client-id=value --durable-cq-name=value 
[--members=value(,value)*] [--groups=value(,value)*]
```



| 名称                | 描述                                                  |
| :------------------ | :----------------------------------------------------------- |
| --durable-client-id | Required. 持久客户端的ID.                      |
| --durable-cq-name   | Required. 要关闭的CQ的名称.                       |
| --members           | 注册持久客户端的成员的名称或ID以及要关闭的持久CQ. |
| --groups            | 持久客户注册的成员组和关闭的持久CQ. |

Table 2. 关闭Durable-CQ参数

**示例命令:**

```bash
close durable-cq --durable-client-id=client1 --durable-cq-name=cq1
```

**示例输出:**

```bash
gfsh>close durable-cq --durable-cq-name=cq1 --durable-client-id=client1
Closed the durable cq : "cq1" for the durable client : "client1". on following members.
1.server4
2.server3
```

**错误消息:**

```bash
gfsh>close durable-client --durable-client-id=client1
Unable to close the durable client : "client1" due to following reasons.

Cannot close a running durable client : client1
Occurred on members
1.server4
2.server3

No client found with client-id : client1
Occurred on members
1.server1
```



#### compact {#compact}
压缩在线和离线磁盘存储。

- **compact disk-store**

  使用该磁盘存储在所有成员上压缩磁盘存储。

- **compact offline-disk-store**

  压缩脱机磁盘存储。

##### compact disk-store {#compact_disk_store}
使用该磁盘存储在所有成员上压缩磁盘存储。

此命令使用每个成员为其磁盘存储配置的压缩阈值。 磁盘存储必须将`allow-force-compaction`属性设置为`true`。

有关详细信息，请参阅[在磁盘存储日志文件上运行压缩](https://geode.apache.org/docs/guide/17/managing/disk_storage/compacting_disk_stores.html#compacting_disk_stores)。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
compact disk-store --name=value [--groups=value(,value)*]
```



| 名称     | 描述                                                  |
| :------- | :----------------------------------------------------------- |
| --name   | *Required.* 要压缩的磁盘存储的名称。          |
| --groups | 执行磁盘压缩的成员组。 如果未指定组，则磁盘存储将由所有成员压缩。 |

Table 1. 压缩磁盘存储参数

**示例命令:**

```bash
compact disk-store --name=Disk1
compact disk-store --name=Disk1 --group=MemberGroup1,MemberGroup2
```

**错误消息:**

```bash
"Disk store \"{0}\" does not exist.";
 " for group(s) \"{0}\"";
"No members found in the specified group(s) \"{0}\".";
"Compaction was attempted but nothing to compact.";
"Error occurred while doing compaction. Reason: \"{0}\"";
```

##### compact offline-disk-store {#compact_offline_disk_store}
压缩脱机磁盘存储。

如果磁盘存储很大，则可能需要使用`-J=-XmxNNNm`参数为进程分配额外的内存。

有关详细信息，请参阅[在磁盘存储日志文件上运行压缩](https://geode.apache.org/docs/guide/17/managing/disk_storage/compacting_disk_stores.html#compacting_disk_stores)。

**注意:** 不要在增量备份的基线目录上执行脱机压缩。

**可用性:** Online or offline.

**句法:**

```bash
compact offline-disk-store --name=value --disk-dirs=value(,value)* 
[--max-oplog-size=value] [--J=value(,value)*]
```



| 名称             | 描述                                                  | 默认值 |
| :--------------- | :----------------------------------------------------------- | :------------ |
| --name           | *Required.* 要压缩的脱机磁盘存储的名称。  |               |
| --disk-dirs      | *Required.* 一个或多个目录，其中先前已写入磁盘存储的数据。 用逗号分隔目录。 |               |
| --max-oplog-size | 压缩创建的oplog的最大大小（以兆字节为单位）。 | -1            |
| --J              | 传递给Java虚拟机的参数在磁盘存储上执行压缩操作。 例如：`-J=-Xmx1024m`。 |               |

Table 2. 压缩离线磁盘存储参数

**示例命令:**

```bash
compact offline-disk-store --name=Disk2 --disk-dirs=/Disks/Disk2
compact offline-disk-store --name=Disk2 --disk-dirs=/Disks/Disk2 --max-oplog-size=512 -J=-Xmx1024m
```



#### configure {#configure}
为集群中的所有缓存配置Portable Data eXchange。

**configure pdx**

为集群中的所有缓存配置Geode的Portable Data eXchange。 此命令不会影响系统中正在运行的成员。 此命令将使用群集配置服务在定位器中保留pdx配置。

**注意:** 应在启动任何数据成员之前发出此命令。

**可用性:** Online.

**句法:**

```bash
configure pdx [--read-serialized=value] [--ignore-unread-fields=value]
    [--disk-store=value] [--auto-serializable-classes=value(,value)*]
    [--portable-auto-serializable-classes=value(,value)*
```



| 名称                                 | 描述                                                  | 默认值 |
| :----------------------------------- | :----------------------------------------------------------- | :------ |
| --read-serialized                    | 设置为true以使PDX反序列化生成PdxInstance而不是域类的实例。 | false   |
| --ignore-unread-fields               | 控制PDX是否忽略反序列化期间未读取的字段。 默认设置是在序列化期间保留未读字段包括其数据。 但是，如果将缓存配置为忽略未读字段，则在序列化期间它们的数据将丢失。如果您知道此成员将只读取缓存数据，则应仅将此属性设置为true。 在此用例中，您不需要支付保留未读字段的成本，因为您永远不会重新序列化PDX数据。 | false   |
| --disk-store                         | 命名磁盘存储，其中将存储PDX类型数据。     | none    |
| --auto-serializable-classes          | 将ReflectionBasedAutoSerializer配置为成员类的PDX序列化程序。 与域类名称匹配的模式，以确定它们是否应自动序列化。 不检查类是否可以移植到非java语言（相当于`check-portability=false`）。 | none    |
| --portable-auto-serializable-classes | 将ReflectionBasedAutoSerializer配置为成员类的PDX序列化程序。 与域类名匹配的模式，以确定是否应序列化它们。 如果这些类的对象不能移植到非Java语言（相当于`check-portability=true`），PDX autoserializer完成的序列化将引发异常。 | none    |

Table 1. Configure PDX Parameters

**示例命令:**

```bash
gfsh>configure pdx --read-serialized=true
```

**示例输出:**

```bash
gfsh>configure pdx --read-serialized=true
persistent = false
read-serialized = true
ignore-unread-fields = false

gfsh>configure pdx --disk-store=/home/username/server4/DEFAULT.drf
persistent = true
disk-store = /home/username/server4/DEFAULT.drf
read-serialized = false
ignore-unread-fields = false
```

**错误消息:**

```bash
"Failed to persist the configuration changes due to this command, Revert the command to maintain consistency.
Please use "status cluster-config-service" to determing whether Cluster configuration service is RUNNING."
```



#### connect {#connect}
直接或通过定位器连接到JMX管理器。

如果您通过定位器进行连接，并且JMX管理器尚不存在，则定位器将启动一个。

gfsh作为发现客户端连接到定位器服务并询问JMX Manager的位置。 定位器知道何时没有当前配置为JMX管理器的成员，只需在其自身内启动JMX管理器服务。 gfsh作为JMX客户端连接到定位器的JMX RMI端口。

您还可以使用HTTP协议连接到远程定位器，如下面的第二个示例所示。

**可用性:** Offline. 如果您已连接，您将收到“已连接到：主机[端口]”的通知。

**句法:**

```bash
connect [--locator=value] [--jmx-manager=value] [--use-http(=value)?] [--url=value]
    [--user=value][--password=value] [--key-store=value] [--key-store-password=value]
    [--trust-store=value] [--trust-store-password=value] [--ciphers=value]
    [--protocols=value] [--security-properties-file=value] [--use-ssl(=value)?]
    [--skip-ssl-validation(=value)?]
```



| 名称                       | 描述                                                  | 默认值                                                      |
| :------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| --locator                  | 定位器的网络地址格式为：`host [port]`。    | `localhost[10334]`                                           |
| --jmx-manager              | JMX管理器的网络地址格式为：`host [port]`。 |                                                              |
| --use-http                 | 使用HTTP协议连接到JMX管理器HTTP服务。 | 如果未指定参数：`false`如果指定的参数没有值：`true` |
| --url                      | 用于连接到JMX管理器的HTTP服务的URL          | `http://localhost:8080/gemfire/v1`                           |
| --user                     | 连接到JMX管理器时用于身份验证的凭据的用户名。 如果指定，如果未指定`--password`选项，`gfsh`将提示输入密码。 |                                                              |
| --password                 | 连接到JMX管理器时用于身份验证的凭据的密码部分。 |                                                              |
| --key-store                | 包含此应用程序的证书和私钥的Java密钥库文件。 如果未指定`--key-store-password`参数，gfsh会提示操作员输入密码。 |                                                              |
| --key-store-password       | 用于从`--key-store`指定的密钥库文件访问私钥的密码。 |                                                              |
| --trust-store              | 包含此应用程序信任的CA证书集合的Java密钥库文件。 如果未指定`--trust-store-password`参数，gfsh会提示操作员输入密码。 |                                                              |
| --trust-store-password     | 用于解锁`--trust-store`指定的密钥库文件的密码。 |                                                              |
| --ciphers                  | 加密连接时使用的SSL/TLS密码。 默认值为“any”。 |                                                              |
| --protocols                | 加密连接时启用的SSL/TLS协议版本。 默认值为“any”。 |                                                              |
| --security-properties-file | `gfsecurity.properties`文件，用于配置gfsh以连接到Locator/Manager。 文件路径可以是当前gfsh目录的绝对路径或相对路径。 |                                                              |
| --use-ssl                  | 是否使用SSL与Locator和/或JMX Manager进行通信。 如果设置为`true`，则connect命令也会读取`gfsecurity.properties`。 SSL选项优先于属性文件中设置的值。 如果未指定，则使用默认值。 | 如果未指定参数：`false`如果指定的参数没有值：`true` |
| --skip-ssl-validation      | 启用SSL通信并指定此选项或赋值为`true`时，此gfsh客户端接受任何SSL证书，允许此gfsh客户端验证它所连接的任何定位器或服务器。 此选项的存在是为了便于测试，而不适用于生产系统。 | `false`                                                      |

Table 1. 连接参数

**示例命令:**

如果未指定定位器或JMX管理器，则`gfsh`将连接到默认端口上localhost上的定位器。

```bash
gfsh>connect
```

**示例输出:**

```bash
gfsh>connect
Connecting to Locator at [host=localhost, port=10334] ..
Connecting to Manager at [host=GemFireStymon, port=1099] ..
Successfully connected to: [host=GemFireStymon, port=1099]
```

**通过HTTP连接到远程定位器的示例:**

```bash
gfsh>connect --use-http=true --url="http://myLocatorHost.example.com:8080/gemfire/v1"
Successfully connected to: GemFire Manager's HTTP service @ 
http://myLocatorHost.example.com:8080/gemfire/v1
```

**错误消息:**

```bash
"Locator could not find a JMX Manager";
"jmx password must be specified.";
"Could not connect to : {0}. {1}";
"Could not find a GemFire jmx-manager service running at {0}.";
"Could not connect to GemFire Locator service at {0}."
```



#### create {#create}
创建异步事件队列，磁盘存储，网关接收器，网关发件人，索引和区域。

- **create async-event-queue**

  在网关发件人传递事件之前，为批处理事件创建异步事件队列。

- **create defined indexes**

  创建所有已定义的索引。

- **create disk-store**

  定义一个或多个磁盘存储池，可供区域和客户机订阅队列使用，以及网关发送方队列用于WAN分发。

- **create gateway-receiver**

  创建网关接收器。 每个成员只能有一个网关接收器，与网关发送器不同，您不需要为网关接收器指定标识符。

- **create gateway-sender**

  在群集的一个或多个成员上创建网关发件人。

- **create index**

  创建可在执行查询时使用的索引。

- **create jndi-binding**

  创建一个JNDI绑定，指定描述JDBC连接的资源属性。

- **create lucene index**

  创建具有给定路径和配置的区域。

- **create region**

  创建具有给定路径和配置的区域。

##### create async-event-queue {#create_async_event_queue}
在网关发件人传递事件之前，为批处理事件创建异步事件队列。

请参阅[配置多站点（WAN）事件队列](https://geode.apache.org/docs/guide/17/developing/events/configure_multisite_event_messaging.html#configure_multisite_event_messaging)。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
create async-event-queue --id=value --listener=value [--groups=value(,value)*]
    [--parallel(=value)?] [--enable-batch-conflation(=value)?] [--batch-size=value]
    [--batch-time-interval=value] [--persistent(=value)?] [--disk-store=value]
    [--disk-synchronous(=value)?] [--max-queue-memory=value]
    [--dispatcher-threads=value] [--order-policy=value]
    [--gateway-event-filter=value(,value)*]
    [--gateway-event-substitution-filter=value] 
    [--listener-param=value(,value)*]
```

**Parameters, create async-event-queue:**

| 名称                               | 描述                                                  | 默认值                                        |
| :--------------------------------- | :----------------------------------------------------------- | :--------------------------------------------------- |
| –id                                | *Required*. 异步事件队列的ID               |                                                      |
| –groups                            | 队列是在组的所有成员上创建的。 如果未指定组，则会在所有成员上创建队列。 |                                                      |
| –parallel                          | 指定队列是否并行。                     | false                                                |
| –enable-batch-conflation           | 启用批处理混合。                                    | false                                                |
| –batch-size                        | 批处理可以包含的最大消息数。         | 100                                                  |
| –batch-time-interval               | 在批处理之前可以经过的最长时间（以毫秒为单位）。 | 5                                                    |
| –persistent                        | 确定Geode是否持久保留此队列的布尔值。 | false如果指定了一个值，则默认为true。 |
| –disk-store                        | 命名磁盘存储，用于存储队列溢出或持久化队列。 如果指定值，则必须存在指定的磁盘存储。 如果指定空值，Geode将使用默认磁盘存储来进行溢出和队列持久性。 |                                                      |
| –disk-synchronous                  | 指定磁盘写入是否同步。               | true                                                 |
| –max-queue-memory                  | 在溢出到磁盘之前队列可以消耗的最大内存量（兆字节）。 | 100                                                  |
| –dispatcher-threads                | 用于发送事件的线程数。                   | 5                                                    |
| –order-policy                      | 当`–dispatcher-threads is > 1`时调度事件的策略。可能的值是`THREAD`，`KEY`，`PARTITION`。 | KEY                                                  |
| –gateway-event-filter              | 此队列的GatewayEventFilters的完全限定类名列表。 这些类在分派到远程服务器之前过滤事件。 |                                                      |
| –gateway-event-substitution-filter | 此队列的`GatewayEventSubstitutionFilter`的完全限定类名。 |                                                      |
| –listener                          | *Required.* 此队列的Async Event Listener的完全限定类名 |                                                      |
| –listener-param                    | 要传递给Async Event Listener类的参数名称和值。 （可选）您可以通过使用`＃`字符和值跟随参数名称来指定值。 例如：`--listener-param=myParam#24` |                                                      |
| –forward-expiration-destroy        | 允许将到期销毁操作转发到AsyncEventListener实例。 如果指定没有值，则此参数设置为“false”。 | false                                                |



**示例命令:**

```bash
create async-event-queue --id=myAEQ --listener=myApp.myListener
```

##### create defined indexes {#create_defined_indexes}
创建所有已定义的索引。

另请参阅[define index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/define.html)和[clear defined indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/clear.html)。

**可用性:** Online. 必须在gfsh中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
create defined indexes [--members=value(,value)*] [--groups=value(,value)*]
```

**参数，创建定义的索引:**

| 名称      | 描述                                                  | 默认值 |
| :-------- | :----------------------------------------------------------- | :------ |
| --members | 将在其上创建索引的成员的名称/ID。     |         |
| --groups  | 将在成员组中的所有成员上创建索引。 |         |

**示例命令:**

```bash
create defined indexes
```

**示例输出:**

```bash
gfsh>create defined indexes
Indexes successfully created. Use list indexes to get details.
1. ubuntu(server1:17682)<v1>:27574
```

如果索引创建失败，您可能会在gfsh中收到类似于以下内容的错误消息:

```bash
gfsh>create defined indexes
Exception : org.apache.geode.cache.query.RegionNotFoundException , 
Message : Region ' /r3' not found: from  /r3Occurred on following members
1. india(s1:17866)<v1>:27809
```

##### create disk-store {#create_disk_store}
定义一个或多个磁盘存储池，可供区域和客户机订阅队列使用，以及网关发送方队列用于WAN分发。

请参阅[磁盘存储](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html)

**可用性:** Online. 必须在gfsh中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
create disk-store --name=value --dir=value(,value)* [--allow-force-compaction(=value)?] 
[--auto-compact(=value)?] [--compaction-threshold=value] [--max-oplog-size=value]
[--queue-size=value] [--time-interval=value] [--write-buffer-size=value]
[--groups=value(,value)*]
[--disk-usage-warning-percentage=value] [--disk-usage-critical-percentage=value]
```

**参数，创建磁盘存储:**

| 名称                             | 描述                                                  | 默认值 |
| :------------------------------- | :----------------------------------------------------------- | :------------ |
| --name                           | *Required.* 此磁盘存储的名称。                     |               |
| --dir                            | *Required.* 写入磁盘存储文件的一个或多个目录名。 可选地，目录名后面可以跟`#`和磁盘存储可以在目录中使用的最大兆字节数。 例如：`--dir=/data/ds1  --dir=/data/ds2#5000`。如果指定的目录不存在，该命令将为您创建目录。 |               |
| --allow-force-compaction         | 设置为true以允许在此磁盘存储上强制执行磁盘压缩。 | false         |
| --auto-compact                   | 设置为true以自动压缩磁盘文件。         | true          |
| --compaction-threshold           | 磁盘存储符合压缩条件之前允许的垃圾百分比。 | 50            |
| --max-oplog-size                 | oplog文件的最大大小（以兆字节为单位）。 当oplog文件达到此大小时，该文件将转到新文件。 | 1024          |
| --queue-size                     | 可以异步排队以写入磁盘的最大操作数。 | 0             |
| --time-interval                  | 将未写入的数据写入磁盘之前可以经过的毫秒数。 | 1000          |
| –groups                          | 磁盘存储在组的所有成员上创建。 如果未指定组，则会在所有成员上创建磁盘存储。 |               |
| --write-buffer-size              | 此磁盘存储在将数据写入磁盘时使用的写入缓冲区的大小。 较大的值可能会提高性能但会占用更多内存。 磁盘存储分配一个此大小的直接内存缓冲区。 | 32768         |
| --disk-usage-warning-percentage  | 磁盘使用率高于此阈值会生成警告消息。 例如，如果阈值设置为90%，则在100 GB可用磁盘空间下的1 TB驱动器上会生成警告。设置为“0”（零）以禁用。 | 90            |
| --disk-usage-critical-percentage | 磁盘使用率高于此阈值会生成错误消息并关闭成员的缓存。 例如，如果阈值设置为99%，则1 TB驱动器上的10 GB可用磁盘空间不足会生成错误并关闭缓存。设置为“0”（零）以禁用。 | 99            |

**示例命令:**

```bash
create disk-store --name-store1 --dir=/data/ds1
```

**示例输出:**

```bash
gfsh>create disk-store --name-store1 --dir=/data/ds1
Member  | Result
------- | -------
server1 | Success
```

##### create gateway-receiver {#create_gateway_receiver}
创建网关接收器。 每个成员只能有一个网关接收器，与网关发送器不同，您不需要为网关接收器指定标识符。

除非指定了`--groups`或`--members`选项，否则创建将在所有服务器上进行。

如果网关接收器创建成功至少一个成员，则此`gfsh`命令退出，退出代码表示成功。

输出每个成员的网关接收器的表格格式状态，与创建的成功或失败无关。

请参阅[网关接收器](https://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/multisite_overview.html#topic_4DB3D9CF01AD4F4899457D1250468D00)。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
create gateway-receiver [--groups=value(,value)*] [--members=value(,value)*] 
  [--manual-start=value] [--start-port=value] [--end-port=value] [--bind-address=value] 
  [--maximum-time-between-pings=value] [--socket-buffer-size=value]
  [--gateway-transport-filter=value(,value)*] [--hostname-for-senders=value]
  [--if-not-exists=(value)?]
```

**参数，创建网关接收器:**

| 名称                         | 描述                                                  | 默认值 |
| :--------------------------- | :----------------------------------------------------------- | :------------ |
| --groups                     | 网关接收器是在组的成员上创建的。 |               |
| --members                    | 要在其上创建网关接收器的成员的名称。 为了向后兼容，如果指定了此选项并且启用了集群配置，则不会保留网关接收器配置。 |               |
| --manual-start               | 布尔值，指定是否需要手动启动网关接收器。 如果提供空值，则网关接收器自动启动默认值为“false”。 | true          |
| --start-port                 | 在指定可能的端口号范围时使用的起始端口号，此网关接收器将用于连接到其他站点中的网关发送器。 Geode在指定的端口号范围内选择一个未使用的端口号来启动接收器。 如果该范围内没有可用的端口号，则抛出异常。`STARTPORT`值包含在内，而`ENDPORT`值是独占的。 例如，如果指定`STARTPORT ="50510"`和`ENDPOINT ="50520"`，Geode会选择50510到50519之间的端口值。 | 5000          |
| --end-port                   | 定义在指定此网关接收器将用于来自其他站点中的网关发件人的连接的可能端口号范围时要使用的上限端口号。 Geode在指定的端口号范围内选择一个未使用的端口号来启动接收器。 如果该范围内没有可用的端口号，则抛出异常.`ENDPORT`值是独占的，而`STARTPORT`值是包含的。 例如，如果指定`STARTPORT="50510"`和`ENDPOINT="50520"`，Geode会选择50510到50519之间的端口值。 | 5500          |
| --bind-address               | 来自其他站点中的网关发件人的连接的网络地址。 将地址指定为文字字符串值。 |               |
| --socket-buffer-size         | 一个整数值，用于设置此网关接收器的套接字连接的缓冲区大小（以字节为单位）。 此值应与连接到此接收器的网关发件人的`socket-buffer-size`设置相匹配。 | 32768         |
| --gateway-transport-filter   | 要添加到网关接收器的GatewayTransportFilter的完全限定类名。 |               |
| --maximum-time-between-pings | 整数值，指定ping到连接的WAN站点之间使用的时间间隔（以毫秒为单位）。 此值确定在将远程WAN站点视为脱机之前可以经过的最长时间。 | 60000         |
| --hostname-for-senders       | 主机名或IP地址告知网关发件人作为他们连接的地址。 定位器通知网关发件人此值。 |               |
| --if-not-exists              | 如果指定时未提供布尔值或指定并设置为true，则网关接收器（如果已存在）将不会创建。 命令输出报告每次创建尝试的状态。 | false         |

**示例命令:**

```bash
gfsh>create gateway-receiver --members=server1
```

**示例输出:**

```bash
gfsh>create gateway-receiver --members=server1
Member  | Status
------- | ---------------------------------------------------------------------------
server1 | GatewayReceiver created on member "server1" and will listen on the port "0"
```

##### create gateway-sender {#create_gateway_sender}
在群集的一个或多个成员上创建网关发件人。

请参阅[网关发件人](https://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/multisite_overview.html#topic_9AA37B43642D4DE19072CA3367C849BA)。

**注意:** 在承载网关发件人的每个Geode成员上，特定发件人`id`的网关发件人配置必须相同。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

```bash
create gateway-sender --id=value --remote-distributed-system-id=value 
   [--groups=value(,value)*] [--members=value(,value)*] [--parallel=value] 
   [--manual-start=value] [--socket-buffer-size=value] [--socket-read-timeout=value] 
   [--enable-batch-conflation=value] [--batch-size=value] [--batch-time-interval=value]
   [--enable-persistence=value] [--disk-store-name=value] [--disk-synchronous=value] 
   [--maximum-queue-memory=value] [--alert-threshold=value] [--dispatcher-threads=value] 
   [--order-policy=value][--gateway-event-filter=value(,value)*] 
   [--gateway-transport-filter=value(,value)*]
```

**参数，创建网关发件人:**

| 名称                           | 描述                                                  | 默认值 |
| :----------------------------- | :----------------------------------------------------------- | :------ |
| --id                           | *Required.* 网关发件人的唯一标识符，通常是与物理位置关联的标识符。 |         |
| --remote-distributed-system-id | *Required.* 此网关发件人发送事件的远程群集的ID。 |         |
| --groups                       | 网关发件人是在组成员上创建的。  |         |
| --members                      | 要在其上创建网关发件人的成员的名称。 |         |
| --parallel                     | 设置为true时，指定并行网关发件人。       | false   |
| --enable-batch-conflation      | 确定Geode是否应该混淆消息的布尔值。 | false   |
| --manual-start                 | **不推荐使用** 布尔值，指定是否需要手动启动网关发件人。 如果提供空值，则使用默认值false，网关发件人自动启动。 *手动启动可能会导致数据丢失，因此不应在生产系统中使用手动启动。* | false   |
| --socket-buffer-size           | 将消息发送到远程站点的套接字缓冲区的大小。 此大小应与处理区域事件的远程网关接收器的`socket-buffer-size`属性的大小相匹配。 | 32768   |
| --socket-read-timeout          | 网关发件人等待从远程站点接收确认的时间量（以毫秒为单位）。 默认情况下，此值设置为0，表示没有超时。 如果设置此超时，则必须将其设置为最小值30000（毫秒）。 将其设置为较小的数字将生成错误消息并将值重置为默认值0。 | 0       |
| --batch-size                   | 批处理可以包含的最大消息数。         | 100     |
| --batch-time-interval          | 发送批次之间可以经过的最大毫秒数。 | 1000    |
| --enable-persistence           | 确定Geode是否持久保存网关队列的布尔值。 | false   |
| --disk-store-name              | 命名磁盘存储，用于存储队列溢出或持久化队列。 如果指定值，则必须存在指定的磁盘存储。 如果指定空值，Geode将使用默认磁盘存储来进行溢出和队列持久性。 |         |
| --disk-synchronous             | 对于写入磁盘的区域，boolean指定是否为该区域同步完成磁盘写入。 | true    |
| --maximum-queue-memory         | 在溢出到磁盘之前队列可以消耗的最大内存量（兆字节）。 | 100 MB  |
| --alert-threshold              | 在Geode记录警报之前，区域事件可以保留在网关发件人队列中的最大毫秒数。 | 0       |
| --dispatcher-threads           | 用于处理来自网关发件人队列或异步事件队列的区域事件的调度程序线程数。 | 5       |
| --order-policy                 | 当`dispatcher-threads`属性大于1时，`order-policy`配置多个调度程序线程处理来自串行网关队列或串行异步事件队列的区域事件的方式。此属性可以具有以下值之一：**key**从本地队列分发区域事件时，多个调度程序线程保留密钥更新的顺序。**thread**从本地队列分发区域事件时，多个调度程序线程保留给定线程将区域事件添加到队列的顺序。**partition**从本地队列分发区域事件时，多个调度程序线程保留区域事件添加到本地队列的顺序。对于分区区域，这意味着传递到特定分区的所有区域事件都以相同的顺序传递到远程Geode站点。对于分布式区域，这意味着传递到本地网关发送方队列的所有密钥更新都以相同的顺序分发到远程站点。您无法为并行事件队列配置`order-policy`，因为并行队列无法保留事件订购地区。只能保留给定分区（或分布式区域的给定队列）中事件的顺序。 | key     |
| --gateway-event-filter         | 要与GatewaySender关联的GatewayEventFilters（由逗号分隔）的完全限定类名列表。 这用作用户在调度到远程集群之前过滤掉事件的回调。 例如：`gateway-event-filter=com.user.filters.MyFilter1,com.user.filters.MyFilters2` |         |
| --gateway-transport-filter     | 要添加到GatewaySender的GatewayTransportFilter的完全限定类名。 |         |

**示例命令:**

```bash
gfsh>create gateway-sender --remote-distributed-system-id="2" --id="sender2"
```

**示例输出:**

```bash
gfsh>create gateway-sender --remote-distributed-system-id="2" --id="sender2"
Member  | Status
------- | --------------------------------------------
server1 | GatewaySender "sender2" created on "server1"
```

##### create index {#create_index}
创建可在执行查询时使用的索引。

**可用性:** Online. 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

请参阅[使用索引](https://geode.apache.org/docs/guide/17/developing/query_index/query_index.html)。

**句法:**

```bash
create index --name=value --expression=value --region=value 
[--members=value(,value)*] [--type=value] [--groups=value(,value)*]
```

**参数，创建索引:**

| 名称         | 描述                                                  | 默认值 |
| :----------- | :----------------------------------------------------------- | :------ |
| --name       | *Required.* 要创建的索引的名称。                     |         |
| ‑‑expression | *Required.* 索引引用的区域值的字段。 |         |
| --region     | *Required.* 与查询中的“from”子句对应的区域的名称/路径。 |         |
| --members    | 将在其上创建索引的成员的名称/ID。     |         |
| --type       | 索引的类型。 有效值为：`range`和`key`。 （第三种类型，`hash`，仍然被识别，但不推荐使用哈希索引。） | `range` |
| --groups     | 将在组中的所有成员上创建索引。 |         |

**示例命令:**

```bash
create index --name=myKeyIndex --expression=region1.Id --region=region1 --type=key
```

**示例输出:**

```bash
gfsh>create index --name=myKeyIdex --expression=region1.Id --region=region1 --type=key
Index successfully created with following details
Name       : myKeyIdex
Expression : region1.Id
RegionPath : /region1
Members which contain the index
1. ubuntu(server1:17682)<v1>:27574

gfsh>create index --name=myIndex2 --expression=exp2 --region=/exampleRegion
Failed to create index "myIndex2" due to following reasons
Index "myIndex2" already exists.  Create failed due to duplicate name.
Occurred on following members
1. ubuntu(server1:17682)<v1>:27574
```

##### create jndi-binding {#create_jndi_binding}
创建一个JNDI绑定，指定描述JDBC连接的资源属性。

**可用性:** Online. You must be connected in gfsh to a JMX Manager member to use this command.

**句法:**

```
create jndi-binding --name=value --type=value --jdbc-driver-class=value
 --connection-url=value [--blocking-timeout-seconds=value]
 [--conn-pooled-datasource-class=value] [--idle-timeout-seconds=value]
 [--init-pool-size=value] [--login-timeout-seconds=value]
 [--managed-conn-factory-class=value] [--max-pool-size=value] [--password=value]
 [--transaction-type=value] [--username=value] [--xa-datasource-class=value]
 [--if-not-exists(=value)?] [--datasource-config-properties=value(,value)*]
```

**Parameters, create jndi-binding:**

| Name                           | Description                                                  | Default |
| :----------------------------- | :----------------------------------------------------------- | :------ |
| --name                         | *Required.* Name of the binding to create.                   |         |
| --type                         | *Required.* Type of the XA datasource. One of: `MANAGED`, `SIMPLE`, `POOLED`, or `XAPOOLED`. |         |
| --jdbc-driver-class            | *Required.* The fully qualified name of the JDBC driver class. |         |
| --connection-url               | *Required.* the JDBC driver connection URL string. For example, `jdbc:hsqldb:hsql://localhost:1701`. |         |
| --blocking-timeout-seconds     | Specifies the maximum time, in seconds, to block while waiting for a connection before throwing an exception. |         |
| --conn-pooled-datasource-class | The fully qualified name of the connection pool implementation that holds XA datasource connections. |         |
| --idle-timeout-seconds         | Specifies the time, in seconds, that a connection may be idle before being closed. |         |
| --init-pool-size               | Specifies the initial number of connections the pool should hold. |         |
| --login-timeout-seconds        | The quantity of seconds after which the client thread will be disconnected due to inactivity. |         |
| --managed-conn-factory-class   | The fully qualified name of the connection factory implementation. |         |
| --max-pool-size                | The maximum number of connections that may be created in a pool. |         |
| --password                     | The default password used when creating a new connection.    |         |
| --transaction-type             | Type of the transaction. One of `XATransaction`, `NoTransaction`, or `LocalTransaction`. |         |
| --username                     | Specifies the default user name to be used when creating a new connection. |         |
| --xa-datasource-class          | The fully qualified name of the `javax.sql.XADataSource`implementation class. |         |
| --if-not-exists                | When true, a duplicate jndi binding will not be created if one with the same name already exists. When false, an attempt to create a duplicate jndi binding results in an error. The option is set to true if the option is specified without a value. | false   |
| --datasource-config-properties | Properties for the custom `XADataSource` driver. Append a JSON string containing a (name, type, value) tuple to set any property. For example: `--datasource-config-properties={'name':'name1','type':'type1','value':'value1'},{'name':'name2','type':'type2','value':'value2'}` |         |

**示例命令:**

```
gfsh>create jndi-binding --name=jndi1 --type=SIMPLE \
  --jdbc-driver-class=org.apache.derby.jdbc.EmbeddedDriver \
  --connection-url="jdbc:derby:newDB;create=true"
```

##### create lucene index {#create_lucene_index}
Create a Lucene index. For details on Lucene index creation, see [Apache Lucene Integration](https://geode.apache.org/docs/guide/17/tools_modules/lucene_integration.html).

For additional Lucene-related gfsh commands, see [describe lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#describe_lucene_index), [destroy lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy_lucene_index), [list lucene indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#list_lucene_indexes) and [search lucene](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/search.html#search_lucene).

**可用性:** Online. You must be connected in gfsh to a JMX Manager member to use this command.

**句法:**

```
create lucene index --name=value --region=value --field=value(,value)*
  [--analyzer=value(,value)*] [--serializer=value] [--group=value(,value)*]
```

**Parameters, create lucene index:**

| Name         | Description                                                  | Default                   |
| :----------- | :----------------------------------------------------------- | :------------------------ |
| --name       | *Required.* Name of the index to create.                     |                           |
| --region     | *Required.* Name/Path of the region on which to define the index. |                           |
| --field      | *Required.* Field(s) of the region values that are referenced by the index, specified as a comma-separated list. To treat the entire value as a single field, specify `__REGION_VALUE_FIELD`. |                           |
| ‑‑analyzer   | Analyzer(s) to extract terms from text, specified as a comma-separated list. If not specified, the default analyzer is used for all fields. If specified, the number of analyzers must exactly match the number of fields specified. When listing analyzers, use the keyword `DEFAULT` for any field that will use the default analyzer. | Lucene `StandardAnalyzer` |
| ‑‑serializer | Fully qualified class name of the serializer to be used with this index. The serializer must implement the `LuceneSerializer`interface. You can use the built-in `org.apache.geode.cache.lucene.FlatFormatSerializer` to index and search collections and nested fields. If not specified, the simple default serializer is used, which indexes and searches only the top level fields of the region objects. | simple serializer         |
| --group      | The index will be created on all the members in the specified member groups. |                           |

**示例命令:**

```
gfsh>create lucene index --name=customerIndex --region=/Customer 
   --field=__REGION_VALUE_FIELD

gfsh>create lucene index --name=analyzerIndex --region=/Person 
     --field=name,email,address,revenue 
     --analyzer=DEFAULT,org.apache.lucene.analysis.core.KeywordAnalyzer,
                examples.MyCharacterAnalyzer,DEFAULT
```

**示例输出:**

```
gfsh>create lucene index --name=testIndex --region=testRegion
    --field=__REGION_VALUE_FIELD
               Member                  | Status
-------------------------------------- | ---------------------------------
192.168.1.23(server505:17200)<v1>:1025 | Successfully created lucene index
```

##### create region {#create_region}
Create a region with given path and configuration.

You must specify either a `--type` or a `--template-region` for initial configuration when creating a region. Specifying a `--key-constraint` and `--value-constraint` makes object type information available during querying and indexing.

See [Region Data Storage and Distribution](https://geode.apache.org/docs/guide/17/developing/region_options/chapter_overview.html).

See [Specifying JSON within Command-Line Options](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/json_in_gfsh.html) for syntax details.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
 create region --name=value [--type=value] [--template-region=value]
    [--groups=value(,value)*] [--if-not-exists(=value)?]
    [--key-constraint=value] [--value-constraint=value]
    [--enable-statistics=value] [--entry-idle-time-expiration=value]
    [--entry-idle-time-expiration-action=value]
    [--entry-time-to-live-expiration=value]
    [--entry-time-to-live-expiration-action=value]
    [--entry-idle-time-custom-expiry=value] [--entry-time-to-live-custom-expiry=value]
    [--region-idle-time-expiration=value]
    [--region-idle-time-expiration-action=value]
    [--region-time-to-live-expiration=value]
    [--region-time-to-live-expiration-action=value] [--disk-store=value]
    [--enable-synchronous-disk=value] [--enable-async-conflation=value]
    [--enable-subscription-conflation=value] [--cache-listener=value(,value)*]
    [--cache-loader=value] [--cache-writer=value]
    [--async-event-queue-id=value(,value)*]
    [--gateway-sender-id=value(,value)*] [--enable-concurrency-checks=value]
    [--enable-cloning=value] [--concurrency-level=value]
    [--colocated-with=value] [--local-max-memory=value]
    [--recovery-delay=value] [--redundant-copies=value]
    [--startup-recovery-delay=value] [--total-max-memory=value]
    [--total-num-buckets=value] [--compressor=value] [--off-heap(=value)]
    [--partition-resolver=value] [--eviction-entry-count=value]
    [--eviction-max-memory=value] [--eviction-action=value] [--eviction-object-sizer=value]
```

**Parameters, create region:**

| Name                                    | Description                                                  | Default                                                      |
| :-------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| --name                                  | *Required.* Name/Path of the region to be created.           |                                                              |
| --type                                  | *Required* (if template-region is not specified.) Type of region to create. Options include: PARTITION, PARTITION_REDUNDANT, REPLICATE, LOCAL, etc.To get a list of of all region type options, add the --typeparameter and then select the TAB key to display a full list. |                                                              |
| --template-region                       | *Required* (if type is not specified.) Name/Path of the region whose attributes should be duplicated when creating this region. |                                                              |
| --groups                                | Group(s) of members on which the region will be created.     |                                                              |
| --if-not-exists                         | A new region will not be created if a region with the same name already exists. By default, an attempt to create a duplicate region is reported as an error. If this option is specified without a value or is specified with a value of `true`, then gfsh displays a “Skipping…” acknowledgement, but does not throw an error. | false                                                        |
| --key-constraint                        | Fully qualified class name of the objects allowed as region keys. Ensures that keys for region entries are all of the same class. |                                                              |
| --value-constraint                      | Fully qualified class name of the objects allowed as region values. If not specified, then region values can be of any class. |                                                              |
| --enable-statistics                     | Whether to gather statistics for the region. Must be true to use expiration on the region. |                                                              |
| --entry-idle-time-expiration            | How long the region’s entries can remain in the cache without being accessed. | no expiration                                                |
| --entry-idle-time-expiration-action     | Action to be taken on an entry that has exceeded the idle expiration. Valid expiration actions include destroy, local-destroy, invalidate (default), local-invalidate. |                                                              |
| --entry-time-to-live-expiration         | How long the region’s entries can remain in the cache without being accessed or updated. The default is no expiration of this type. | no expiration                                                |
| --entry-time-to-live-expiration-action  | Action to be taken on an entry that has exceeded the TTL expiration. Valid expiration actions include destroy, local-destroy, invalidate (default), local-invalidate. |                                                              |
| --entry-idle-time-custom-expiry         | The name of a class implementing CustomExpiry for entry idle time. Append a JSON string for initialization properties. |                                                              |
| --entry-time-to-live-custom-expiry      | The name of a class implementing CustomExpiry for entry time to live. Append a JSON string for initialization properties. |                                                              |
| --region-idle-time-expiration           | How long the region can remain in the cache without being accessed. The default is no expiration of this type. |                                                              |
| --region-idle-time-expiration-action    | Action to be taken on a region that has exceeded the idle expiration. Valid expiration actions include destroy, local-destroy, invalidate (default), local-invalidate. |                                                              |
| --region-time-to-live-expiration        | How long the region can remain in the cache without being accessed or updated. The default is no expiration of this type. | no expiration                                                |
| --region-time-to-live-expiration-action | Action to be taken on a region that has exceeded the TTL expiration. Valid expiration actions include destroy, local-destroy, invalidate (default), local-invalidate. |                                                              |
| --disk-store                            | Disk Store to be used by this region. The [list disk-stores](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_BC14AD57EA304FB3845766898D01BD04)command can be used to display existing disk stores. |                                                              |
| --enable-synchronous-disk               | Whether writes are done synchronously for regions that persist data to disk. |                                                              |
| --enable-async-conflation               | Whether to allow aggregation of asynchronous TCP/IP messages sent by the producer member of the region. A false value causes all asynchronous messages to be sent individually. |                                                              |
| --enable-subscription-conflation        | Whether the server should conflate its messages to the client. A false value causes all server-client messages to be sent individually. |                                                              |
| --cache-listener                        | Fully qualified class name of a plug-in to be instantiated for receiving after-event notification of changes to the region and its entries. Any number of cache listeners can be configured. A fully qualified class name may be appended with a JSON specification that will be parsed to become the fields of the parameter to the `init()`method for a class that implements the `Declarable`interface. |                                                              |
| --cache-loader                          | Fully qualified class name of a plug-in to be instantiated for receiving notification of cache misses in the region. At most, one cache loader can be defined in each member for the region. For distributed regions, a cache loader may be invoked remotely from other members that have the region defined. A fully qualified class name may be appended with a JSON specification that will be parsed to become the fields of the parameter to the `initialize()`method for a class that implements the `Declarable`interface. |                                                              |
| --cache-writer                          | Fully qualified class name of a plug-in to be instantiated for receiving before-event notification of changes to the region and its entries. The plug-in may cancel the event. At most, one cache writer can be defined in each member for the region. A fully qualified class name may be appended with a JSON specification that will be parsed to become the fields of the parameter to the `init()`method for a class that implements the `Declarable`interface. |                                                              |
| --async-event-queue-id                  | IDs of the Async Event Queues that will be used for write-behind operations. |                                                              |
| --gateway-sender-id                     | IDs of the Gateway Senders to which data will be routed.     |                                                              |
| --enable-concurrency-checks             | Whether Region Version Vectors are implemented. Region Version Vectors are an extension to the versioning scheme that aid in synchronization of replicated regions. |                                                              |
| --enable-cloning                        | Determines how fromDelta applies deltas to the local cache for delta propagation. When true, the updates are applied to a clone of the value and then the clone is saved to the cache. When false, the value is modified in place in the cache. |                                                              |
| --concurrency-level                     | Estimate of the maximum number of application threads that will concurrently access a region entry at one time. This attribute does not apply to partitioned regions. |                                                              |
| --colocated-with                        | Central Region with which this region should be colocated.   |                                                              |
| --local-max-memory                      | Maximum amount of memory, in megabytes, to be used by the region in this process. (The default is 90% of available heap.) |                                                              |
| --recovery-delay                        | Delay in milliseconds that existing members will wait after a member crashes before restoring this region’s redundancy on the remaining members. The default value (-1) indicates that redundancy will not be recovered after a failure. |                                                              |
| --redundant-copies                      | Number of extra copies of buckets desired. Extra copies allow for both high availability in the face of VM departure (intended or unintended) and load balancing read operations. (Allowed values: 0, 1, 2 and 3) |                                                              |
| --startup-recovery-delay                | Delay in milliseconds that new members will wait before assuming their share of cluster-level redundancy. This allows time for multiple regions to start before the redundancy workload is parceled out to the new members. A value of -1 indicates that adding new members will not trigger redundancy recovery. | The default is to recover redundancy immediately when a new member is added. |
| --total-max-memory                      | Maximum amount of memory, in megabytes, to be used by the region in all processes. |                                                              |
| --total-num-buckets                     | Total number of hash buckets to be used by the region in all processes. | 113                                                          |
| --compressor                            | Java class name that implements compression for the region. You can write a custom compressor that implements `org.apache.geode.compression.Compressor` or you can specify the Snappy compressor (`org.apache.geode.compression.SnappyCompressor`), which is bundled with Geode. See [Region Compression](https://geode.apache.org/docs/guide/17/managing/region_compression.html#topic_r43_wgc_gl). | no compression                                               |
| --off-heap                              | Specifies whether the region values are stored in heap memory or off-heap memory. When true, region values are in off-heap memory. If the parameter is specified without a value, the value of true is used. | false                                                        |
| --partition-resolver                    | Specifies the full path to a custom partition resolver. Specify `org.apache.geode.cache.util.StringPrefixPartitionResolver`to use the included string prefix partition resolver. |                                                              |
| --eviction-entry-count                  | Enables eviction, where the eviction policy is based on the number of entries in the region. |                                                              |
| --eviction-max-memory                   | Enables eviction, where the eviction policy is based on the amount of memory consumed by the region, specified in megabytes. |                                                              |
| --eviction-action                       | Action to take when the eviction threshold is reached.local-destroyEntry is destroyed locally. Use with caution - may lead to inconsistencies.overflow-to-diskEntry is overflowed to disk. For partitioned regions, this provides the most reliable read behavior across the region. |                                                              |
| –eviction-object-sizer                  | Specifies your implementation of the ObjectSizer interface to measure the size of objects in the region. The sizer applies only to heap and memory based eviction. |                                                              |



**示例命令:**

```
create region --name=region1 --type=REPLICATE_PERSISTENT \
--cache-writer=org.apache.geode.examples.MyCacheWriter \
--group=Group1 --disk-store=DiskStore1

create region --name=region12 --template-region=/region1

create region --name=region2 --type=REPLICATE \
--cache-listener=org.apache.geode.examples.MyCacheListener1,\
org.apache.geode.examples.MyCacheListener2 \
--group=Group1,Group2

create region --name=region3 --type=PARTITION_PERSISTENT --redundant-copies=2 \
--total-max-memory=1000 --startup-recovery-delay=5 --total-num-buckets=100 \
--disk-store=DiskStore2 --cache-listener=org.apache.geode.examples.MyCacheListener3 \
--group=Group2 

create region --name=region4 --type=REPLICATE_PROXY \
--cache-listener=org.apache.geode.examples.MyCacheListener1 --group=Group1,Group2

create region --name=myRegion --type=REPLICATE --eviction-max-memory=100 \
--eviction-action=overflow-to-disk --eviction-object-sizer=my.company.geode.MySizer

create region --name=r1 --type=PARTITION \
--cache-loader=org.example.myLoader{'URL':'jdbc:cloudscape:rmi:MyData'}
```

**示例输出:**

```
gfsh>create region --name=myRegion --type=LOCAL
Member  | Status
------- | ---------------------------------------
server1 | Region "/myRegion" created on "server1"
```



#### debug {#debug}
Enable or disable debugging output in `gfsh`.

**可用性:** Online or offline.

**句法:**

```
debug --state=value
```



| Name    | Description                                                  | Default Value |
| :------ | :----------------------------------------------------------- | :------------ |
| ‑‑state | Whether to turn debugging ON or OFF. Valid options are: ON, OFF (Case insensitive) | OFF           |

Table 1. Debug Parameters

**示例命令:**

```
debug --state=off
debug --state=on
```

**示例输出:**

```
gfsh>debug --state=on
Debug is on
```



#### define index {#define_index}
Define an index that can be used when executing queries. Then, you can execute a single command to create multiple indexes all at once using `create defined indexes`.

**可用性:** Online or offline.

**句法:**

```
define index --name=value --expression=value --region=value [--type=value]
```



| Name         | Description                                                  | Default Value |
| :----------- | :----------------------------------------------------------- | :------------ |
| --name       | *Required.* Name of the index to define.                     |               |
| ‑‑expression | *Required.* Field of the region values that are referenced by the index. |               |
| --region     | *Required.* Name/Path of the region which corresponds to the “from” clause in a query. |               |
| --type       | Type of the index. Valid values are: range, key and hash.    | range         |

Table 1. Define Index Parameters

**示例命令:**

```
gfsh> define index --name=myIndex1 --expression=exp1 --region=/exampleRegion 

gfsh> define index --name=myIndex2 --expression=”c.exp2” --region="/exampleRegion e, e.collection1 c" 

gfsh> define index --name=myIndex3 --expression=exp3 --region=/exampleRegion --type=hash 

//then to create the indexes, execute:

gfsh> create defined indexes
```

**示例输出:**

```
gfsh>define index --name=myIndex1 --expression=exp1 --region=/exampleRegion
Index successfully defined with following details
Name       : myIndex1
Expression : exp1
RegionPath : /exampleRegion
```



#### deploy {#deploy}
Deploy JAR-packaged applications to a member or members.

Only one of either `--jars` or `--dir` may be specified.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
deploy [--groups=value(,value)*] [--jars=value(,value)*] [--dir=value]
```



| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| ‑‑groups | Group(s) to which the specified JARs will be deployed. If this option is not specified, the deployment will occur on all members. |
| --jars   | Path(s) of the JAR(s) to deploy.                             |
| --dir    | Directory from which to deploy the JARs.                     |

Table 1. Deploy Parameters

**示例命令:**

```
deploy --jars=group1_functions.jar --groups=Group1

deploy --dir=libs/group1-libs --groups=Group2
```

**示例输出:**

```
gfsh> deploy --jars=group1_functions.jar --groups=Group1

 Member   |     Deployed JAR     |                Deployed JAR Location            
--------- | -------------------- | ---------------------------------------------------
datanode1 | group1_functions.jar | /usr/local/gemfire/deploy/GF#group1_functions.jar#1
datanode2 | group1_functions.jar | /usr/local/gemfire/deploy/GF#group1_functions.jar#1


gfsh> deploy --dir=libs/group1-libs --groups=Group2

Deploying files: group2_functions.jar, group2_dependencies.jar
Total file size is: 0.64MB

Continue?  (Y/n): Y

 Member   |      Deployed JAR       |             Deployed JAR Location            
--------- | ----------------------- | ---------------------------------------------
datanode3 | group2_functions.jar    | /usr/local/gemfire/deploy/GF#group2_functions.jar#1
datanode3 | group2_dependencies.jar | /usr/local/gemfire/deploy/GF#group2_dependencies.jar#1 
datanode4 | group2_functions.jar    | /usr/local/gemfire/deploy/GF#group2_functions.jar#1
datanode4 | group2_dependencies.jar | /usr/local/gemfire/deploy/GF#group2_dependencies.jar#1
```



#### describe {#describe}
Display details of a member’s configuration, shell connection, disk-stores, members, or regions.

- **describe client**

  Displays details about a specified client.

- **describe config**

  Display the configuration of a member.

- **describe connection**

  Display connection information details.

- **describe disk-store**

  Display information about a member’s disk store.

- **describe jndi-binding**

  Display information about the configuration of a JNDI binding.

- **describe lucene index**

  Display information about a Lucene index.

- **describe member**

  Display details of a member with given name/id.

- **describe offline-disk-store**

  Display information about an offline member’s disk store.

- **describe region**

  Display the attributes and key information of a region.

##### describe client {#describe_client}
Displays details about a specified client.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe client --clientID=value
```

**Parameters, describe client:**

| Name       | Description                                                  |
| :--------- | :----------------------------------------------------------- |
| ‑‑clientID | *Required.* ID of the client. To find a client ID, you can use the `list clients`command to display a list of connected clients and their IDs. |

**示例命令:**

```
describe client --clientID=192.0.2.0(4987:loner):58922:7b3398cf
```

**示例输出:**

```
gfsh>describe client --clientID=192.0.2.0(4987:loner):58922:7b3398cf
-------------------------------------------------------------------
Primary Servers                : 192.0.2.0(server1:5764)<v1>:15189
Secondary Servers              : 192.0.2.0(server2:5891)<v2>:39082
CPU                            : 0
Number of Cache Listner Calls  : 0
Number of Gets                 : 0
Number of Misses               : 0
Number of Puts                 : 0
Number of Threads              : 0
Process CPU Time (nanoseconds) : 0
Queue size                     : 1
UP Time (seconds)              : 67
Is Durable                     : No
```

##### describe config {#describe_config}
Display the configuration of a member.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe config --member=value [--hide-defaults(=value)?]
```

**Parameters, describe config:**

| Name            | Description                                                  | Default Value |
| :-------------- | :----------------------------------------------------------- | :------------ |
| --member        | Name or ID of a member whose configuration is to be shown.   |               |
| --hide-defaults | Whether to hide configuration information for properties with the default value. | true          |

**示例命令:**

```
describe config --member=Member1;
```

**示例输出:**

```
gfsh>describe config --member=server1
Configuration of member : "server1"


JVM command line arguments
-----------------------------------
-Dgemfire.mcast-port=0
-Dgemfire.locators=localhost[10334]

GemFire properties defined using the API
................................................
log-file                                 : vf.gf.server.log
name                                     : server1

GemFire properties defined at the runtime
................................................
log-level                                 : finest
statistic-sampling-enabled                : true

Cache attributes
................................................
is-server        : true

Cache-server attributes
 . bind-address         : localhost
```

##### describe connection {#describe_connection}
Display connection information details.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe connection
```

**示例命令:**

```
describe connection
```

**示例输出:**

```
gfsh>describe connection
Connection Endpoints
--------------------
GemFireUser[1099]
```

##### describe disk-store {#describe_disk_store}
Display information about a member’s disk store.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe disk-store --member=value --name=value
```

**Parameters, describe disk-store:**

| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| --member | *Required.* Name/ID of the member with the disk store to be described. |
| --name   | *Required*. Name of the disk store to be described.          |

**示例命令:**

```
describe disk-store --member=server1 --name=DiskStore1
```

**示例输出:**

```
gfsh>describe disk-store --name=disk1 --member=server1
Disk Store ID                      : a531bc7b-5188-4510-85d7-de7de30c6671
Disk Store Name                    : disk1
Member ID                          : ubuntu(server1:7467)<v1>:35249
Member Name                        : server1
Allow Force Compaction             : No
Auto Compaction                    : Yes
Compaction Threshold               : 50
Max Oplog Size                     : 1024
Queue Size                         : 0
Time Interval                      : 1000
Write Buffer Size                  : 32768
Disk Usage Warning Percentage      : 90
Disk Usage Critical Percentage     : 99
PDX Serialization Meta-Data Stored : No


        Disk Directory          | Size
------------------------------- | ----------
/home/user/server1/DiskStore1 | 2147483647
```

##### describe jndi-binding {#describe_jndi_binding}
Print the configuration information that describes a JDBC connection.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe jndi-binding --name=value
```

**Parameters, describe jndi-binding:**

| Name   | Description                                           |
| :----- | :---------------------------------------------------- |
| --name | *Required.* Name of the JNDI binding to be described. |

**示例命令:**

```
describe jndi-binding --name=jndi1
```

**示例输出:**

```
gfsh>describe jndi-binding --name=jndi1
    Property      | Value
----------------- | ------------------------------------
type              | SimpleDataSource
jndi-name         | jndi1
jdbc-driver-class | org.apache.derby.jdbc.EmbeddedDriver
user-name         |
connection-url    | jdbc:derby:newDB
```

##### describe lucene index {#describe_lucene_index}
Describe a Lucene index.

See also [create lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#create_lucene_index), [destroy lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy_lucene_index), [list lucene indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#list_lucene_indexes) and [search lucene](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/search.html#search_lucene).

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe lucene index --name=value --region=value
```

**Parameters, describe lucene index:**

| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| --name   | *Required.* Name of the Lucene index to describe             |
| --region | *Required.* Name and path of the region in which the Lucene index exists |

**示例命令:**

```
gfsh>describe lucene index --name=personIndex --region=/Person
```

**示例输出:**

```
gfsh>describe lucene index --name=personIndex --region=/Person
Index Name  | Region Path |                 Indexed Fields                 | Field Analyzer |   Status    | Query Executions | Updates | Commits | Documents
----------- | ----------- | ---------------------------------------------- | -------------- | ----------- | ---------------- | ------- | ------- | ---------
personIndex | /Person     | [name, email, address, streetAddress, revenue] | {}             | Initialized | 339              | 1008    | 962     | 1004

gfsh>describe lucene index --name=analyzerIndex --region=/Person
 Index Name   | Region Path |     Indexed Fields     |            Field Analyzer             |   Status    | Query Executions | Updates | Commits | Documents
------------- | ----------- | ---------------------- | ------------------------------------- | ----------- | ---------------- | ------- | ------- | ---------
analyzerIndex | /Person     | [address, name, email] | {address=MyCharacterAnalyzer, email.. | Initialized | 1695             | 1008    | 962     | 1004
```

##### describe member {#describe_member}
Display details of a member with given name/id.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe member --name=value
```

**Parameters, describe member:**

| Name   | Description                                                  |
| :----- | :----------------------------------------------------------- |
| ‑‑name | *Required.* Display information about a member, including name, ID, groups, regions, etc. |

**示例命令:**

```
describe member --name=server1
```

**示例输出:**

```
gfsh>describe member --name=server1
Name        : server1
Id          : GemFireUser(server1:240)<v1>:64871
Host        : 192.0.2.0
Regions     : region4
region5
region3
region2
region1

PID         : 240
Groups      :
Used Heap   : 5M
Max Heap    : 123M
Working Dir : c:\PivotalGemFire70\Latest\server1
Log file    : C:\PivotalGemFire70\Latest\server1\vf.gf.server.log
Locators    : localhost[10334]

Server Bind        : localhost
Server Port        : 40404
Running            : true
Client Connections : 0
```

##### describe offline-disk-store {#describe_offline_disk_store}
Display information about an offline member’s disk store.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe offline-disk-store --name=value --disk-dirs=value(,value)* [--pdx=value] [--region=value]
```

**Parameters, describe offline-disk-store:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | *Required.* Name of the disk store to be described.          |
| --disk-dirs | *Required.* Directory that contains the disk store files.    |
| --pdx       | If set (or set to true), display all the pdx types stored in the disk store. |
| --region    | Name and path of the region in the disk store to be described. |

**示例命令:**

```
describe offline-disk-store --name=DiskStore1\
 --disk-dirs=/home/username/gemfire/mydiskStore1Dir

describe offline-disk-store --name=DiskStore1 --disk-dirs=/DiskDir1  --pdx=true
```

**示例输出:**

```
gfsh>describe offline-disk-store --name=DiskStore1 --disk-dirs=/DiskDir1 --pdx=true
Regions in the disk store:
  /PdxTypes: -lru=none -concurrencyLevel=16 -initialCapacity=16 -loadFactor=0.75 -compressor=none -statisticsEnabled=false
  /Region1: -lru=none -concurrencyLevel=16 -initialCapacity=16 -loadFactor=0.75 -compressor=none -statisticsEnabled=false
PDX Types:
  com.app.data.PositionPdx: id=1
    long avg20DaysVol;
    String bondRating;
    double convRatio;
    String country;
    double delta;
    long industry;
    long issuer;
    double mktValue;
    double qty;
    String secId; // identity
    String secIdIndexed;
    String secLinks;
    double sharesOutstanding;
    String underlyer;
    long volatility;
    int pid;
    int portfolioId;
  com.app.data.StockPdx: id=2
    int ID; // identity
    String pkid;
    Object position1;
    Object position2;
    Object positions;
    Object collectionHolderMap;
    String type;
    String status;
    String[] names;
    String description;
    long createTime;
    Object[] position3;
    Object aDay;
    Date date;
PDX Enums:
  com.app.data.StockPdx$Day.Monday
```

##### describe region {#describe_region}
Display the attributes and key information of a region.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
describe region --name=value
```

**Parameters, describe region:**

| Name   | Description                                          |
| :----- | :--------------------------------------------------- |
| --name | *Required.* Name/Path of the region to be described. |

**示例命令:**

```
describe region --name=region1
```

**示例输出:**

```
gfsh>describe region --name=Region1
..........................................................
Name : Region1
Data Policy : persistent replicate
Hosting Members : server-5
server-4
server-3
server-2

Non-Default Attributes Shared By Hosting Members 

Type   | Name            | Value
------ | --------------- | --------------------
Region | data-policy     | PERSISTENT_REPLICATE
       | disk-store-name | DiskStore1
       | size            | 0
       | scope           | distributed-ack

...........................................................
Name             : Region1
Data Policy      : empty
Accessor Members : server-1

Non-Default Attributes Shared By Accessor Members  

 Type  |    Name     | Value
------ | ----------- | ---------------
Region | data-policy | EMPTY
       | size        | 0
       | scope       | distributed-ack
```



#### destroy {#destroy}
Delete or unregister functions, remove indexes, disk stores and regions.

- **destroy async-event-queue**

  Destroy an asynchronous event queue.

- **destroy disk-store**

  Delete a disk store and all files on disk used by the disk store.

- **destroy function**

  Destroy or unregister a function.

- **destroy gateway-sender**

  Destroy a gateway sender.

- **destroy index**

  Destroy or remove the specified index.

- **destroy jndi-binding**

  Destroy the specified JNDI binding.

- **destroy lucene index**

  Destroy or remove the specified Lucene index.

- **destroy region**

  Destroy or remove a region.

##### destroy async-event-queue {#destroy_async_event_queue}
Destroy an asynchronous event queue.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy async-event-queue --id=value [--groups=value(,value)*] [--if-exists=value]
```

**Parameters, destroy async-event-queue:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --id        | *Required.* ID of the async event queue to be deleted.       |
| ‑‑groups    | Group(s) of members on which the async event queue will be destroyed. If no group is specified, the queue is destroyed on all members. |
| ‑‑if‑exists | If the specified async event queue does not exist, gfsh responds with a message to that effect. If this parameter is true, the response is prefixed with the label “Skipping: ”. Useful for scripted tests. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**示例命令:**

```
destroy async-event-queue --id=myAsyncEventQueue
```

##### destroy disk-store {#destroy_disk_store}
Delete a disk store and all files on disk used by the disk store. Data for closed regions that previously used this disk store are lost.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy disk-store --name=value [--groups=value(,value)*] [--if-exists=value]
```

**Parameters, destroy disk-store:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | *Required.* Name of the disk store to be deleted.            |
| ‑‑groups    | Group(s) of members on which the disk store will be destroyed. If no group is specified, the disk store is destroyed on all members. |
| ‑‑if‑exists | If the specified disk store does not exist, gfsh responds with a message to that effect. If this parameter is true, the response is prefixed with the label “Skipping: ”. Useful for scripted tests. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**示例命令:**

```
destroy disk-store --name=store1
```

**示例输出:**

```
gfsh>destroy disk-store --name=store1
Member  | Result
------- | -------
server1 | Success
```

##### destroy function {#destroy_function}
Destroy or unregister a function.

The default is for the function to be unregistered from all members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy function --id=value [--groups=value(,value)*] [--member=value]
```

**Parameters, destroy function:**

| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| --id     | *Required.* Unique function identifier. Use the `list functions` command to obtain the ID. |
| --groups | One or more groups of members from which this function will be unregistered. |
| ‑‑member | Name or ID of the member from which this function will be unregistered. |

**示例命令:**

```
(1) destroy function --id=InterestCalculations
(2) destroy function --id=InterestCalculations --member=server1
(3) destroy function --id=InterestCalculations --group=Group1
```

##### destroy gateway-sender {#destroy_gateway_sender}
Destroy a gateway sender that is no longer used by a region.

The default is for the gateway sender to be destroyed on all members.

No region may be attached to the gateway sender to be destroyed. If a region is still attached, the system issues an error message similar to:

```
ERROR: The GatewaySender ParallelGatewaySender{id=ln,remoteDsId=2,isRunning =false}
could not be destroyed as it is still used by region(s).
```

Remove the gateway sender from the region with a command similar to:

```
gfsh>alter region --name=regionA --gateway-sender-id=""
```

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy gateway-sender --id=value [--groups=value(,value)*]
  [--members=value(,value)*] [--if-exists=value]
```

**Parameters, destroy gateway-sender:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --id        | *Required.* Unique gateway sender identifier. Use the `list gateways` command to obtain the ID. |
| --groups    | One or more groups of members from which this gateway sender will be destroyed. |
| ‑‑members   | Name or ID of the member(s) from which this gateway sender will be destroyed. |
| ‑‑if‑exists | If the specified gateway sender does not exist, gfsh responds with a message to that effect. If this parameter is true, the response is prefixed with the label “Skipping: ”. Useful for scripted tests. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**示例命令:**

```
destroy gateway-sender --id=SiteASender
```

##### destroy index {#destroy_index}
Destroy or remove the specified index.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy index [--name=value] [--region=value] [--members=value(,value)*]
[--groups=value(,value)*] [--if-exists=value]
```

**注意:** You must specify at least one of the parameter options. If you enter `destroy index` without any parameters, the command will ask you to specify at least one option.

**Parameters, destroy index:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | Name of the index to be removed.                             |
| ‑‑members   | Id of the member(s) on which index is to be removed.         |
| --region    | Name of the region from which an index or all indexes are to be destroyed. |
| --groups    | The index will be removed on all the members in the group(s). |
| ‑‑if‑exists | If the specified index does not exist, gfsh responds with a message to that effect. If this parameter is true, the response is prefixed with the label “Skipping: ”. Useful for scripted tests. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**示例命令:**

```
destroy index --members=server2
destroy index --name=MyKeyIndex
```

##### destroy jndi-binding {#destroy_jndi_binding}
Destroy a specified JNDI binding that holds the configuration for an XA data source.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy jndi-binding --name=value [--if-exists=value]
```

**Parameters, destroy jndi-binding:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | *Required.* Name of the JNDI binding to be destroyed.        |
| ‑‑if‑exists | Skip the destroy operation when the specified JNDI binding does not exist. Without this option, an error results from the specification of a JNDI binding that does not exist. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**Example Command:**

```
destroy jndi-binding --name=jndi1
```

##### destroy lucene index {#destroy_lucene_index}
Destroy or remove the specified Lucene index.

See also [create lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#create_lucene_index), [describe lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#describe_lucene_index), [list lucene indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#list_lucene_indexes) and [search lucene](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/search.html#search_lucene).

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy lucene index --region=value [--name=value]
```

**Parameters, destroy lucene index:**

| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| ‑‑region | *Required.* Name of the region from which indexes are to be removed. If no `--name`option is specified, all indexes associated with the region are destroyed. |
| ‑‑name   | Name of the index to be removed.                             |

**示例命令:**

```
destroy lucene index --region=region1
destroy lucene index --region=region1 --name=MyKeyIndex
```

##### destroy region {#destroy_region}
Destroy or remove a region.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
destroy region --name=value [--if-exists=value]
```

**Parameters, destroy region:**

| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | *Required.* Name and path of the region to be removed.       |
| ‑‑if‑exists | If the specified region does not exist, gfsh responds with a message to that effect. If this parameter is true, the response is prefixed with the label “Skipping: ”. Useful for scripted tests. Default (if the parameter is not specified): false. Default (if the parameter is specified without value): true. |

**示例命令:**

```
destroy region --name=region4
destroy region --name=/region1/subregion1
```

**示例输出:**

```
gfsh>destroy region --name=region1
"region1"  destroyed successfully.
```



#### disconnect {#disconnect}
Close any active connection(s).

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
disconnect
```

**示例命令:**

```
disconnect
```

**示例输出:**

```
gfsh>disconnect
Disconnecting from: Locator1[1099]
Disconnected from : Locator1[1099]
```

**错误消息:**

```
Error occurred while disconnecting: {0}
Not connected!
```



#### echo {#echo}




Echo the given text, which may include system and user variables.

The command can also echo gfsh environment properties (using ’[set variable](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/set.html)’ command) if variable name is pre-pended with ’$’ - like UNIX.

See [Useful gfsh Shell Variables](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/useful_gfsh_shell_variables.html#concept_731ECA5E40E943CBA5C1198A0745D8EE) for a list of gfsh environment variables.

**可用性:** Online or offline.

**句法:**

```
echo [--string=value]
```



| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| --string | String to be echoed. For example, `SYS_USER variable is set to ${SYS_USER}`. |

Table 1. Echo Parameters

**示例命令:**

```
echo --string="Hello World!"
echo --string="Hello World! This is ${SYS_USER}"
echo --string=${APP_FETCH_SIZE}
```

To see all the variable set in the shell:

```
echo --string=$*
```

**示例输出:**

```
gfsh>echo --string=${SYS_JAVA_VERSION}
Post substitution: echo --string=1.8.0_60
1.8.0_60
```



#### execute function {#execute_function}
Execute functions on members or regions.

**execute function**

Execute the function with the specified ID. By default, the function executes on all members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
execute function --id=value [--groups=value(,value)*]
[--members=value(,value)*] [--region=value] 
[--arguments=value(,value)*] [--result-collector=value] [--filter=value]
```



|                    |                                                              |
| :----------------- | :----------------------------------------------------------- |
| --id               | *Required.* ID of the function to execute.                   |
| --groups           | One or more groups of members on which this function should be executed. |
| --members          | Name/ID of the member(s) on which the function will be executed. |
| --region           | Region on which the data dependent function will be executed. |
| --arguments        | Arguments to the function in comma separated string format.  |
| --result-collector | Fully qualified class name of the `ResultCollector` to instantiate for gathering results. |
| --filter           | Key list which causes the function to only be executed on members which have entries with these keys. |

Table 1. Execute Function Parameters

**示例命令:**

```
execute function --id=InterestCalculations --region=/InterestRegion
execute function --id=InterestCalculations --members=server1
execute function --id=InterestCalculations --groups=Group1
```



#### exit {#exit}
Exit the `gfsh` shell. You can also use `quit` to exit the shell.

Exits the gfsh shell and returns to the OS shell.

**可用性:** Online or offline.

**句法:**

```
exit
```

**示例命令:**

```
exit
```



#### export {#export}
Export configurations, data, logs and stack-traces.

- **export cluster-configuration**

  Export a cluster configuration ZIP file that contains the `cache.xml` files, `gemfire.properties`files, and application JAR files needed to configure and operate a cluster.

- **export config**

  Export configuration properties for a member or members.

- **export data**

  Export user data from a region to a file.

- **export logs**

  Export logs to a given directory.

- **export offline-disk-store**

  Export region data from an offline disk store into gemfire snapshot files.

- **export stack-traces**

  Export the stack trace for a member or members.

##### export cluster-configuration {#export_cluster_configuration}
Exports a single XML file or a ZIP file with cluster configuration that contains the `cache.xml` files, `gemfire.properties` files, and application JAR files needed to configure and operate a cluster.

When neither a file name nor a ZIP file name is specified, the cluster configuration is written to standard output.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

See [Overview of the Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html#concept_r22_hyw_bl).

**句法:**

```
export cluster-configuration [--group(=value)?] [--xml-file=value]
 [--zip-file-name=value]
```

**Export Cluster-Configuration Parameters:**

| Name            | Description                                                  | Default Value |
| :-------------- | :----------------------------------------------------------- | :------------ |
| ‑‑group         | Export the configuration for the specified server group. When no group is specified, use the default `cluster` group. |               |
| ‑‑xml-file      | Filename to contain the exported cluster configuration. May also include an absolute or relative path. Only specify one of `--xml-file` or `--zip-file-name`. |               |
| ‑‑zip-file-name | Filename of the ZIP file to contain the exported cluster configuration. May also include an absolute or relative path. Only specify one of `--xml-file`or `--zip-file-name`. |               |

**示例命令:**

```
gfsh>export cluster-configuration --zip-file-name=/group/shared-configs/devClusterConfig.zip
gfsh>export cluster-configuration --zip-file-name=my-configs/myClusterConfig.zip
gfsh>export cluster-configuration --zip-file-name=myClusterConfig.zip
gfsh>export cluster-configuration --xml-file=Cluster3Config.xml
```

**示例输出:**

```
gfsh>export cluster-configuration --zip-file-name=mySharedConfig.zip
Downloading cluster configuration : /home/username/gemfire/mySharedConfig.zip
```

##### export config {#export_config}
Export configuration properties for a member or members.

If you do not specify any parameters, all member configurations will be exported.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
export config [--members=value(,value)*] [--groups=value(,value)*]
[--dir=value]
```

**Export Config Parameters:**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --members | Name/Id of the member(s) whose configuration will be exported. |
| --groups  | Group(s) of members whose configuration will be exported.    |
| --dir     | Directory to which the exported configuration files will be written. |

**示例命令:**

```
export config
export config --members=member1
```

**示例输出:**

```
gfsh>export config --members=member1
Downloading Cache XML file: c:\PivotalGemFire\Latest\.\member1-cache.xml
Downloading properties file: c:\PivotalGemFire\Latest\.\member1-gf.properties
```

##### export data {#export_data}
将用户数据从一个区域导出到一个或多个文件。

**可用性:** 线上。 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

要将数据从任何区域（已复制或已分区）按顺序导出到单个文件，请使用`--file`选项:

```bash
export data --region=value --member=value --file=value
```

在此方案中，来自复制和分区区域的数据将按顺序导出到指定成员上的单个文件中。

对于分区区域，可以使用`--parallel`选项加快导出过程:

```bash
export data  --parallel --region=value --member=value --dir=value
```

在这种情况下，分区区域数据在所有托管节点上同时导出到这些相应节点的本地目录。

- 并行导出仅适用于分区区域。
- `--file`和`--dir`选项是互斥的; `--file`只能用于串行导出，`--dir`可用于串行和并行导出。

**导出数据参数:**

| 名称       | 描述                                                         |
| :--------- | :----------------------------------------------------------- |
| --region   | *Required.* 要从中导出数据的区域.                            |
| ‑‑member   | *Required.* 托管该区域的成员的`Name/ID`。 在串行导出中，所有数据都将导出到运行该成员的主机上的指定文件中。 在并行导出中，来自部分托管在此成员上的分区区域的数据将为每个分区导出到承载这些分区的节点上的文件. |
| --file     | 要写入导出数据的文件。 该文件必须具有`.gfd`的扩展名。 不能与--dir同时指定，不能与--parallel一起使用. |
| --dir      | 要写入导出数据的目录。 如果--parallel为true，则为必需。 不能同时指定--file. |
| --parallel | 将每个节点上的本地数据导出到该计算机上的目录。 仅适用于分区区域. |

**示例命令:**

```bash
export data --region=region2 --file=region2_20121001.gfd --member=server2
```

**样本输出:**

```bash
 gfsh>export data --region=region2 --file=region2_20121001.gfd --member=server1
Data succesfully exported from region : region2 to file : C:\PivotalGemFire\
Latest\server1\region2_20121001.gfd on host : 192.0.2.0
```

##### export logs {#export_logs}
Export logs to a given directory.

All files that have logs in the specified time range will be exported. If no time range is specified, all logs will be exported.

The `--dir` parameter specifies a local directory to which log files will be written. This is used only when you are exporting logs using an http connection. If executed over http, the zip archive will be saved in the specified directory on the user’s client machine. If not specified, logs are written to the location specified by the `user.dir` system property. When the command is executed over JMX, logs will be saved as `exportedlogs_xxx.zip` in the connected locator’s working directory.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
export logs [--dir=value] [--groups=value(,value)*] [--members=value(,value)*] 
[--log-level=value] [--only-log-level=value] [--merge-log=value] 
[--start-time=value] [--end-time=value] [logs-only(=value)?] 
[--stats-only(=value)?] [--file-size-limit(=value)?]
```

**Export Logs Parameters:**

| Name              | Description                                                  | Default Value                                                |
| :---------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| --dir             | Local directory to which log files will be written when logs are exported using an http connection. Ignored when the command is executed over JMX. |                                                              |
| --groups          | Group(s) of members whose log files will be exported.        |                                                              |
| ‑‑members         | Name/ID of the member(s) whose log files will be exported.   |                                                              |
| --log-level       | Minimum level of log entries to export. Valid values are: `OFF`, `FATAL`, `ERROR`, `WARN`, `INFO`, `DEBUG`, `TRACE`, and `ALL`. | `INFO`                                                       |
| --only-log-level  | Whether to only include only entries that exactly match the --log-level specified. | false                                                        |
| ‑‑merge‑log       | Whether to merge logs after exporting to the target directory (deprecated). | false                                                        |
| --start-time      | Log entries that occurred after this time will be exported. Format: yyyy/MM/dd/HH/mm/ss/SSS/z OR yyyy/MM/dd | no limit                                                     |
| --end-time        | Log entries that occurred before this time will be exported. Format: yyyy/MM/dd/HH/mm/ss/SSS/z OR yyyy/MM/dd | no limit                                                     |
| --logs-only       | Whether to export only logs (not statistics)                 | If parameter not specified: false. If parameter specified without a value: true |
| --stats-only      | Whether to export only statistics (not logs)                 | If parameter not specified: false. If parameter specified without a value: true |
| --file-size-limit | Limits total unzipped size of the exported files. Specify 0 (zero) for no limit. Value is in megabytes by default or [k,m,g,t] may be specified. | If parameter not specified: 100m. If parameter specified without a value: 0 |

**Example commands, showing output:**

```
gfsh>export logs --dir=data/logs
Logs exported to the connected member's file system: /my-locator/data/logs/exportedLogs_1489513007261.zip
gfsh>export logs --dir=data/logs --file-size-limit=1k
Estimated exported logs expanded file size = 95599, file-size-limit = 1024.
To disable exported logs file size check use option "--file-size-limit=0".

gfsh>export logs --dir=data/logs --file-size-limit=99k
Logs exported to the connected member's file system: /my-locator/data/logs/exportedLogs_1489513007261.zip
```

##### export offline-disk-store {#export_offline_disk_store}
Export region data from an offline disk store into gemfire snapshot files.

**可用性:** Online or offline.

**句法:**

```
export offline-disk-store --name=value --disk-dirs=value(,value)* --dir=value
```

**Export Offline-Disk-Store Parameters:**

| Name        | Description                                        |
| :---------- | :------------------------------------------------- |
| --name      | *Required.* Name of the disk store to be exported. |
| --disk-dirs | Directories which contain the disk store files.    |
| --dir       | Directory to export the snapshot files to.         |

**示例命令:**

```
 export offline-disk-store --name= DiskStore1 \
--disk-dirs=/home/username/gemfire/mydiskStore1Dir --dir=/home/username/gemfire/export
```

##### export stack-traces {#export_stack_traces}
Export the stack trace for a member or members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
export stack-traces --file=value [--members=value(,value)*] [--groups=value(,value)*]
```

**Export Stack-Traces Parameters:**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --file    | *Required.* Filename to which the stack-traces will be written. |
| --members | Name or ID of the member(s) whose log files will be exported. |
| --groups  | Group(s) of members whose log files will be exported.        |

**示例命令:**

```
export stack-traces --file=stack.txt
```

**示例输出:**

```
gfsh>export stack-traces --file=stack.txt
stack-trace(s) exported to file: C:\PivotalGemFire\Latest\locator1\stack.txt
On host : GemFireStymon
```



#### gc {#gc}
Force GC (Garbage Collection) on a member or members.

The default is for garbage collection to occur on all caching members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
gc [--groups=value(,value)*] [--member=value]
```



| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| --groups | One or more group(s) of members on which garbage collection will be forced. |
| --member | Name/ID of the member on which garbage collection will be forced. |

Table 1. GC Parameters

**示例命令:**

```
gc --member=server1
gc --groups=Group1
gc
```

**示例输出:**

```
gfsh>gc
Sucessfully executed GC
```



#### get {#get}
Display an entry in a region.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
get --key=value --region=value [--key-class=value] [--value-class=value]
```



| Name                 | Description                                                  | Default Value                                                |
| :------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| --key                | *Required.* String or JSON text from which to create the key. For example: “`James`”, “`100L`” and “`('id': 'l34s')`”. |                                                              |
| ‑‑region             | *Required.* Region from which to get the entry.              |                                                              |
| --key-class          | Fully qualified class name of the key’s type.                | The default is the key constraint for the current region or String. |
| --value-class        | Fully qualified class name of the value’s type.              | The default is the value constraint for the current region or String. |
| --load-on-cache-miss | Explicitly enables or disables the use of any registered CacheLoaders on the specified Region when retrieving a value for the specified Key on Cache misses. | true (enabled)                                               |

Table 1. Get Parameters

**示例命令:**

```
get --key=('id':'133abg124') --region=region1

// Retrieving when key type is a wrapper(primitive)/String
get --key=('133abg124') --region=/region1/region12 --value-class=data.ProfileDetails

get --key=('100L') --region=/region1/region12 --value-class=data.ProfileDetails 
--key-class=java.lang.Long
```

**示例输出:**

```
gfsh>get --key=('123') --region=region1
Result      : true
Key Class   : java.lang.String
Key         : ('123')
Value Class : java.lang.String
Value       : ABC
```



#### help {#help}
Display syntax and usage information for all the available commands.

Typing help without a command as an argument lists all available commands.

**可用性:** Online or offline.

**句法:**

```
help [command]
```

**Examples Commands:**

```
help
help rebalance
```

**示例输出:**

```
gfsh>help rebalance
NAME
        rebalance
IS AVAILABLE
        true
SYNOPSIS
        Rebalance partitioned regions. The default is for all partitioned region
s to be rebalanced.
SYNTAX
        rebalance [--include-region=value(,value)*] [--exclude-region=value(,val
ue)*] [--time-out=value] [--simulate(=value)?]
PARAMETERS
        include-region
                Partitioned regions to be included when rebalancing. Includes ta
ke precedence over excludes.
                Required:false
        exclude-region
                Partitioned regions to be excluded when rebalancing.
                Required:false
        time-out
                Time to wait (in seconds) before GFSH returns to a prompt while
rebalancing continues in the background. The default is to wait for rebalancing
to complete.
                Required:false
                Default if the parameter is not specified:-1
        simulate
                Whether to only simulate rebalancing. The --time-out parameter i
s not available when simulating.
                Required:false
                Default if no value for the parameter is given:true
                Default if the parameter is not specified:false
```



#### hint {#hint}
Display information on topics and a list of commands associated with a topic.

Provide hints for a topic or lists all available topics if a topic is not specified.

**可用性:** Online or offline.

**句法:**

```
hint [topic]
```

**示例命令:**

```
hint
hint Server
```

**示例输出:**

```
gfsh>hint
Hints are available for the following topics. Use "hint <topic-name>" for a specific
 hint.
Configuration
Data
Debug-Utility
Disk Store
Function Execution
GFSH
Help
JMX
Lifecycle
Locator
Management-Monitoring
Manager
Region
Server
Statistics

gfsh>hint server
A server is GemFire cluster member which holds a GemFire cache. Depending on the
 topology used it can refer to either a system that responds to client requests
or a system that is only a peer to other members.

describe member : Display information about a member, including name, id, groups
, regions, etc.
export logs     : Export the log files for a member or members.
list members    : Display all or a subset of members.
start server    : Start a GemFire Cache Server.
status server   : Display the status of a GemFire Cache Server.
stop server     : Stop a GemFire Cache Server..
```



#### history {#history}
Show or save the command history.

This history can be saved to a file which can also be used as a script later.

A history of commands that have been executed successfully is also logged in the `.geode/.gfsh.history` file within the home directory of the user running gfsh.

**可用性:** Online or offline.

**句法:**

```
history [--file=<history text file>]
```



| Name    | Description                                              | Default Value |
| :------ | :------------------------------------------------------- | :------------ |
| --file  | File to which the history is to be saved.                |               |
| --clear | When set to `true`, clears the history of gfsh commands. | false         |

Table 1. History Parameters

**示例命令:**

```
history
history --file=./mycommands.gfsh;
```

**示例输出:**

```
gfsh>history --file=./mycommands.gfsh
Wrote successfully to file ./mycommands.gfsh
```



#### 导入

您可以导入导出的群集配置以创建新群集或将数据导入区域。

- **import cluster-configuration**

  导入群集配置。

- **import data**

  将用户数据从文件导入区域。

##### import cluster-configuration {#import_cluster_configuration}
Imports a previously exported cluster configuration from a ZIP file or an XML file. This command is useful when spinning up a new cluster.

- In a given cluster, only one locator needs to perform the import. That locator shares the imported configuration with all other connected locators in the cluster.
- Locators share the imported configuration with data members (servers) when the servers start, or if the servers have been recently started, have no regions defined in them, and have been given no other configuration changes since they started.

To import a cluster configuration, start one or more locators and then run the `gfsh` `import cluster-configuration` command.

**可用性:** Online. You must be connected in `gfsh` to a locator to use this command.

**句法:**

```
import cluster-configuration [--action=value] [--group(=value)?]
 [--xml-file=value] [--zip-file-name=value]
```

**Import Cluster-Configuration Parameters:**

| Name            | Description                                                  |
| :-------------- | :----------------------------------------------------------- |
| ‑‑action        | When the value is `APPLY` (the default), the configuration is applied to the running servers that have no configuration. If any servers already have configuration, the command fails. When the value is `STAGE`, the configuration is overwritten and will be used during future server creation; the configuration of currently running servers is not changed. |
| ‑‑group         | Do the import for the specified server group. When no group is specified, the `cluster` is implied. |
| ‑‑xml-file      | Filename from which to import the cluster configuration. May also include an absolute or relative path. Only specify one of `--xml-file` or `--zip-file-name`. |
| --zip-file-name | Name of the ZIP file containing the cluster configuration artifacts to be imported. Only specify one of `--xml-file` or `--zip-file-name`. |

**示例命令:**

```
gfsh>import cluster-configuration --zip-file-name=/home/username/myClusterConfig.zip
gfsh>import cluster-configuration --xml-file=configs/Cluster3Config.xml
```

**示例输出:**

```
gfsh>import cluster-configuration --zip-file-name=/home/username/myClusterConfig.zip
Cluster configuration successfully imported
```

##### import data {#import_data}
将用户数据从一个或多个文件导入区域。

**可用性:** 线上。 您必须在`gfsh`中连接到JMX Manager成员才能使用此命令。

**句法:**

如果数据按顺序导出到单个文件，请使用`import`命令的序列形式导入数据:

```bash
import data --region=value --file=value --member=value [--invoke-callbacks=value]
```

在此方案中，从指定成员上的单个文件顺序导入来自复制和分区区域的数据。

有关串行和并行数据导出之间差异的说明，请参阅[导出数据](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/export.html#topic_263B70069BFC4A7185F86B3272011734)。

如果数据是以并行格式从分区区域导出的，请使用`--parallel`选项导入数据:

```bash
import data --parallel --region=value --member=value --dir=value
  [--invoke-callbacks=value]
```

在这种情况下，分区区域数据在所有托管节点上从这些相应节点的本地目录同时导入。

- 并行导入仅适用于以并行模式导出的分区区域。
- `--file`和`--dir`选项是互斥的; `--file`只能用于串行导入，`--dir`可用于串行和并行导入。

**导入数据参数:**

| 名称               | 描述                                                  | 缺省值 |
| :----------------- | :----------------------------------------------------------- | :------------ |
| --region           | *Required.* 将导入数据的区域.         |               |
| ‑‑member           | *Required.* 托管该区域的成员的`Name/ID`。 在串行导入中，所有数据都是从运行该成员的主机上的指定文件导入的。 在并行导入中，从托管这些分区的节点上的文件为每个分区导入部分托管在此成员上的分区区域的数据。 |               |
| --file             | 将从中读取导入数据的文件。 该文件必须具有`.gfd`的扩展名，并且必须位于该成员的文件系统上（或通过NFS可访问该成员），该目标是导入。 不能与--dir同时指定，不能与--parallel一起使用. |               |
| --dir              | 从中导入数据的目录。 如果--parallel为true，则为必需。 不能同时指定--file。 如果给定节点在指定目录中没有数据文件，则会以静默方式跳过该节点的导入操作. |               |
| ‑‑invoke‑callbacks | 布尔值，当为true时，在数据导入期间调用回调. | false         |

**示例命令:**

```bash
import data --region=region2 --file=/mnt5/region2_20121001.gfd --member=server1
```



#### list {#list}
List existing Geode resources such as deployed applications, disk-stores, functions, members, servers, and regions.

- **list async-event-queues**

  Display a list of async event queues for all members.

- **list clients**

  Displays a list of connected clients.

- **list deployed**

  Display a list of JARs that were deployed to members using the deploy command.

- **list disk-stores**

  List all available disk stores across the Geode cluster

- **list durable-cqs**

  List durable client CQs associated with the specified durable client id.

- **list functions**

  Display a list of registered functions. The default is to display functions for all members.

- **list gateways**

  Displays the gateway senders and receivers for a member or members.

- **list indexes**

  Display the list of indexes created for all members.

- **list jndi-binding**

  List all JNDI bindings, active and configured.

- **list lucene indexes**

  List Lucene indexes created for all members.

- **list members**

  Display all or a subset of members.

- **list regions**

  Display regions of a member or members. If no parameter is specified, all regions in the cluster are listed.

##### list async-event-queues {#list_async_event_queues}
Display a list of async event queues for all members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list async-event-queues
```

**示例命令:**

```
list async-event-queues
```

##### list clients {#list_clients}
Displays a list of connected clients.

Displays a list of connected clients and the servers they are connected to.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list clients
```

**示例命令:**

```
list clients
```

**示例输出:**

```
gfsh>list clients

ClientList

            Client Name / ID              | Server Name / ID
----------------------------------------- | -----------------------------------------------------
192.0.2.0(4987:loner):58922:7b3398cf | member=server2,port=53508;  member=server1,port=56806
192.0.2.0(5065:loner):39906:a6f598cf | member=server2,port=53508;  member=server1,port=56806
```

##### list deployed {#list_deployed}
Display a list of JARs that were deployed to members using the deploy command.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list deployed [--groups=value(,value)*]
```

**Parameters, list deployed:**

| Name     | Description                                                  |
| :------- | :----------------------------------------------------------- |
| ‑‑groups | Group(s) of members for which deployed JARs will be displayed. If not specified, JARs for all members are displayed. |

**示例命令:**

```
list deployed
list deployed --groups=Group2
```

**示例输出:**

```
gfsh> list deployed  --groups=Group2

 Member   |     Deployed JAR     |                JAR Location
--------- | -------------------- | ---------------------------------------------------
datanode1 | group1_functions.jar | /usr/local/gemfire/deploy/vf.gf#group1_functions.jar#1
datanode2 | group1_functions.jar | /usr/local/gemfire/deploy/vf.gf#group1_functions.jar#1
```

**错误消息:**

```
No JAR Files Found
```

##### list disk-stores {#list_disk_stores}
List all available disk stores across the Geode cluster

The command also lists the configured disk directories and any Regions, Cache Servers, Gateways, PDX Serialization and Async Event Queues using Disk Stores to either overflow and/or persist information to disk. Use the `describe disk-store` command to see the details for a particular Disk Store.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list disk-stores
```

**示例命令:**

```
list disk-stores
```

**示例输出:**

```
gfsh> list disk-stores

   Member Name   |                   Member Id                   | Disk Store Name |            Disk Store ID
  -------------- | --------------------------------------------- | --------------- | ------------------------------------
  consumerServer | 192.0.2.0(consumerServer:13825)<v5>:3545  | consumerData    | 4029af26-fd82-4997-bd6c-33382cdbb5e9
  consumerServer | 192.0.2.0(consumerServer:13825)<v5>:3545  | observerData    | 7e0316ad-963c-49b0-9b01-8f59b8d9e29e
  producerServer | 192.0.2.0(producerServer:13826)<v3>:53764 | producerData    | 4670e4eb-1c50-4465-b418-08ede3d5dbed
```

**错误消息:**

```
gfsh> list disk-stores
No Disk Stores Found
```

##### list durable-cqs {#list_durable_cqs}
List durable client CQs associated with the specified durable client id.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list durable-cqs --durable-client-id=value
 [--members=value(,value)*] [--groups=value(,value)*]
```

**Parameters, list durable-cqs:**

| Name                | Description                                                  |
| :------------------ | :----------------------------------------------------------- |
| --durable-client-id | *Required.* The ID used to identify the durable client.      |
| --members           | Name or Id of the member(s) for which the durable client is registered and durable CQs will be displayed. |
| --groups            | Group(s) of members for which the durable client is registered and durable CQs will be displayed. |

**示例命令:**

```
list durable-cqs --durable-client-id=client1
```

**样本输出:**

```
gfsh>list durable-cqs --durable-client-id=client1
member  | durable-cq-name
------- | ---------------
server3 | cq3
        | cq1
        | cq2
server4 | cq3
        | cq1
```

**错误消息:**

```
gfsh>list durable-cqs --durable-client-id=client1

Unable to list durable-cqs for durable-client-id : "client1" due to following reasons.

No client found with client-id : client1
Occurred on members
1.server4
2.server1
3.server3
```

##### list functions {#list_functions}
Display a list of registered functions. The default is to display functions for all members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list functions [--matches=value] [--groups=value(,value)*]
[--members=value(,value)*]
```

**Parameters, list functions:**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --matches | Pattern that the function ID must match in order to be included. Uses Java pattern matching rules, not UNIX. For example, to match any character any number of times use “.*” instead of “*”. |
| --groups  | Group(s) of members for which functions will be displayed. Use a comma separated list for multiple groups. |
| ‑‑members | Name or ID of the member(s) for which functions will be displayed. Use a comma separated list for multiple members. |

**示例命令:**

```
gfsh> list functions
gfsh> list functions --matches=reconcile.*
```

**示例输出:**

```
list functions

   Member   |          Function
  --------- | --------------------------
  camelot   | loadDataFromExternalSource
  camelot   | reconcileWeeklyExpenses
  excalibur | loadDataFromExternalSource
  excalibur | reconcileDailyExpenses


Example of 'list function' with a "matches" filter:

gfsh> list functions --matches=reconcile.*

   Member   |        Function
  --------- | -----------------------
  camelot   | reconcileWeeklyExpenses
  excalibur | reconcileDailyExpenses


Example of 'list functions' when no functions are found in Geode :

gfsh> list functions
No Functions Found.
```

##### list gateways {#list_gateways}
Displays the gateway senders and receivers for a member or members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list gateways [--members=value(,value)*] [--groups=value(,value)*]
```

**Parameters, list gateways:**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| ‑‑members | Member(s) whose gateways senders and receiver display.       |
| --groups  | Group(s) of members for which Gateway Senders and Receivers will be displayed. Use a comma separated list for multiple groups. |

**示例命令:**

```
list gateways
```

##### list indexes {#list_indexes}
Display the list of indexes created for all members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list indexes [--with-stats(=value)?]
```

**Parameters, list indexes:**

| Name         | Description                                            | Default Value |
| :----------- | :----------------------------------------------------- | :------------ |
| --with-stats | Specifies whether statistics should also be displayed. | false         |

**示例命令:**

```
list indexes

list indexes --with-stats
```

**示例输出:**

```
gfsh>list indexes
Member Name    |                   Member ID                   | Region Path |   Name   | Type  | Indexed Expression | From Clause
-------------- | --------------------------------------------- | ----------- | -------- | ----- | ------------------ | -----------
consumerServer | 192.0.2.0(consumerServer:13873):6317      | /consumers  | cidIdx   | KEY   | id                 | /consumers
consumerServer | 192.0.2.0(consumerServer:13873):6317      | /consumers  | cnameIdx | RANGE | name               | /consumers
producerServer | 192.0.2.0(producerServer:13874):19198     | /producers  | pidIdx   | RANGE | id                 | /producers

Example of 'list indexes' with stats printed:

gfsh>list indexes --with-stats

Member Name  | Member ID | Region Path |   Name   | Type  | Indexed Expression | From Clause | Uses | Updates | Update Time | Keys | Values
------------ | --------- | ----------- | -------- | ----- | ------------------ | ----------- | ---- | ------- | ----------- | ---- | ------
cs...        | 192...    | /consumers  | cidIdx   | KEY   | id                 | /consumers  | 2512 | 0       | 0           | 5020 | 5020  
cs...        | 192...    | /consumers  | cnameIdx | RANGE | name               | /consumers  | 0    | 5020    | 421224000   | 0    | 5020
ps...        | 192...    | /producers  | pidIdx   | RANGE | id                 | /producers  | 0    | 5031    | 497872000   | 5031 | 5031
```

**错误消息:**

Example of output when no indexes are found in Geode:

```
gfsh> list indexes
No Indexes Found
```

##### list jndi-binding {#list_jndi_binding}
List all JNDI bindings, active and configured. An active binding is one that is bound to the server’s JNDI context and is also listed in the cluster configuration. A configured binding is one that is listed in the cluster configuration, but may not be active on the servers.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list jndi-binding
```

**示例输出:**

```
gfsh>list jndi-binding

Configured JNDI bindings: 

Group Name | JNDI Name | JDBC Driver Class
---------- | --------- | ------------------------------------
cluster    | jndi1     | org.apache.derby.jdbc.EmbeddedDriver

Active JNDI bindings found on each member: 

    Member      |        JNDI Name        | JDBC Driver Class
--------------- | ----------------------- | ----------------------------------------------------
land-gifted-gun | java:UserTransaction    | org.apache.geode.internal.jta.UserTransactionImpl
land-gifted-gun | java:TransactionManager | org.apache.geode.internal.jta.TransactionManagerImpl
```

##### list lucene indexes {#list_lucene_indexes}
Display the list of Lucene indexes created for all members. The optional `--with-stats` qualifier shows activity on the indexes.

See also [create lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#create_lucene_index), [describe lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#describe_lucene_index), [destroy lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy_lucene_index) and [search lucene](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/search.html#search_lucene).

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list lucene indexes [--with-stats(=value)]
```

**Parameters, list lucene indexes:**

| Name         | Description                                            | Default Value                             |
| :----------- | :----------------------------------------------------- | :---------------------------------------- |
| --with-stats | Specifies whether statistics should also be displayed. | false if not specified, true if specified |

**示例命令:**

```
list lucene indexes
```

**示例输出:**

```
gfsh>list lucene indexes --with-stats
Index Name | Region Path |     Indexed Fields     | Field Analy.. | Status  | Query Executions | Updates | Commits | Documents
---------- | ----------- | ---------------------- | ------------- | ------- | ---------------- | ------- | ------- | ---------
testIndex  | /testRegion | [__REGION_VALUE_FIELD] | {__REGION_V.. | Defined | NA               | NA      | NA      | NA

gfsh>list lucene indexes
 Index Name   | Region Path |                           Indexed Fields                           | Field Analy.. | Status
------------- | ----------- | ------------------------------------------------------------------ | ------------- | -----------
analyzerIndex | /Person     | [revenue, address, name, email]                                    | {revenue=St.. | Initialized
customerIndex | /Customer   | [symbol, revenue, SSN, name, email, address, __REGION_VALUE_FIELD] | {}            | Initialized
pageIndex     | /Page       | [id, title, content]                                               | {}            | Initialized
personIndex   | /Person     | [name, email, address, revenue]                                    | {}            | Initialized
```

##### list members {#list_members}
Display all or a subset of members.

Within the output, the membership coordinator is listed. `<vN>` identifies which view the member currently has; `N` will be zero or a positive integer. `<ec>` indicates which members are eligible to be a membership coordinator.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
list members [--group=value]
```

**Parameters, list members:**

| Name    | Description                                     |
| :------ | :---------------------------------------------- |
| --group | Group name for which members will be displayed. |

**示例命令:**

```
list members
list members --group=Group1
```

**示例输出:**

```
gfsh>list members
  Name       | Id
------------ | -------------------------------------
Coordinator: | 192.0.2.0(locator1:216:locator)<ec><v6>:33368
locator1     | 192.0.2.0(locator1:216:locator)<ec><v6>:33368
server1      | 192.0.2.0(server1:888)<v7>:10839
server2      | 192.0.2.0(server2:3260)<v8>:16721
```

##### list regions {#list_regions}
Display regions of a member or members. If no parameter is specified, all regions in the cluster are listed.

**句法:**

```
list regions [--groups=value(,value)*] [--members=value(,value)*]
```

**Parameters, list regions:**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --groups  | Group(s) of members for which regions will be displayed.     |
| --members | Name or ID of the member(s) for which regions will be displayed. |

**示例命令:**

```
list regions
list regions --groups=G1
list regions --members=member1
```

**示例输出:**

```
gfsh>list regions
List of regions
---------------
region1
region2
```



#### load-balance gateway-sender {#load_balance_gateway_sender}
Causes the specified gateway sender to close its current connections and reconnect to remote gateway receivers in a more balanced fashion.

Use this command to load balance connections between gateway senders to receivers. For example, when you add a new gateway receiver node at a remote site, execute this command so that the new gateway receiver can pick up connections from the specified gateway sender. Invoking this command redistributes a sender’s connections more evenly among all the gateway receivers.

**注意:** This command has no effect on ping connections.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
load-balance gateway-sender --id=value
```



| Name | Description                           |
| :--- | :------------------------------------ |
| --id | *Required.* ID of the Gateway Sender. |

Table 1. Load-Balance Gateway-Sender Parameters

**示例命令:**

```
load-balance gateway-sender --id=sender1-LN
```

**示例输出:**

```
load-balance gateway-sender --id=ny

                 Member             | Result | Message
  --------------------------------- | ------ |--------------------------------------------------------------------------
boglesbymac(ln-1:88651)<v2>:48277 | OK     | GatewaySender ny is rebalanced on member boglesbymac(ln-1:88651)<v2>:48277
boglesbymac(ln-4:88681)<v5>:42784 | OK     | GatewaySender ny is rebalanced on member boglesbymac(ln-4:88681)<v5>:42784
boglesbymac(ln-3:88672)<v4>:43675 | OK     | GatewaySender ny is rebalanced on member boglesbymac(ln-3:88672)<v4>:43675
boglesbymac(ln-2:88662)<v3>:12796 | OK     | GatewaySender ny is rebalanced on member boglesbymac(ln-2:88662)<v3>:12796
```



#### locate entry {#locate_entry}
Locate a region entry on a member.

**locate entry**

Locate a given entry on members using the specified key. This command is useful when using partitioned regions.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
locate entry --key=value --region=value [--key-class=value] 
[--value-class=value] [--recursive=value]
```



| Name          | Description                                                  | Default Value      |
| :------------ | :----------------------------------------------------------- | :----------------- |
| --key         | *Required.* String or JSON text from which to create a key. Examples include: “`James`”, “`100L`” and “`('id': 'l34s')`”. |                    |
| --region      | *Required.* Region in which to locate values.                |                    |
| --key-class   | Fully qualified class name of the key’s type.                | `java.lang.String` |
| --value-class | Fully qualified class name of the value’s type.              | `java.lang.String` |
| ‑‑recursive   | Whether to traverse regions and subregions recursively.      | false              |

Table 1. Locate Entry Parameters

**示例命令:**

```
locate entry --key=('id':'133abg124') --region=/region1 
--key-class=data.ProfileKey --recursive=true;
```

**示例输出:**

```
gfsh>locate entry --key=('123abc') --region=region2
Result          : true
Key Class       : java.lang.String
Key             : ('123abc')
Locations Found : 2


MemberName | MemberId
---------- | -------------------------------------
server1    | GemFireStymon(server1:3692)<v1>:13487
server2    | GemFireStymon(server2:2340)<v2>:11613
```



#### netstat {#netstat}
Report network information and statistics via the “netstat” operating system command.

Report important network usage information/statistics for the given member.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
netstat [--members=value(,value)*] [--group=value] [--file=value] 
[--with-lsof(=value)?]
```



| Name        | Description                                                  | Default Value |
| :---------- | :----------------------------------------------------------- | :------------ |
| --members   | Name or ID of the member(s) on which to run the `netstat` command. |               |
| --group     | Group of members on which to run the `netstat` command.      |               |
| --file      | Text file to which output from the `netstat` command will be written. A “`.txt`” extension will be added if it is not already part of the specified name. |               |
| ‑‑with‑lsof | Specifies whether `lsof` (list open files) command output should also be displayed. Not applicable for Microsoft Windows hosts. | false         |

Table 1. Netstat Parameters

**示例命令:**

```
netstat
netstat --members=server1
netstat --members=server1 --file=server1_netstat.txt
```

**示例输出:**

```
gfsh>netstat
####################################
Host: GemFireTest
OS: Windows XP 5.1 x86
Member(s):
 server2, locator1, server1
####################################

Active Connections

  Proto  Local Address          Foreign Address        State
  TCP    GemFireTest:epmap    GemFireTest:0        LISTENING
  TCP    GemFireTest:microsoft-ds  GemFireTest:0        LISTENING
  TCP    GemFireTest:1034     GemFireTest:0        LISTENING
  TCP    GemFireTest:1069     GemFireTest:0        LISTENING
  TCP    GemFireTest:1099     GemFireTest:0        LISTENING
  TCP    GemFireTest:1134     GemFireTest:0        LISTENING
  TCP    GemFireTest:3389     GemFireTest:0        LISTENING
  TCP    GemFireTest:8080     GemFireTest:0        LISTENING
  TCP    GemFireTest:8081     GemFireTest:0        LISTENING
  TCP    GemFireTest:10334    GemFireTest:0        LISTENING
  TCP    GemFireTest:40404    GemFireTest:0        LISTENING
  TCP    GemFireTest:40405    GemFireTest:0        LISTENING
  TCP    GemFireTest:1025     GemFireTest:0        LISTENING
  TCP    GemFireTest:5152     GemFireTest:0        LISTENING
  TCP    GemFireTest:netbios-ssn  GemFireTest:0        LISTENING
  TCP    GemFireTest:1035     GemFireTest:0        LISTENING
  TCP    GemFireTest:1035     GemFireTest:1081     ESTABLISHED
  TCP    GemFireTest:1035     GemFireTest:1086     ESTABLISHED
  TCP    GemFireTest:1035     GemFireTest:1147     ESTABLISHED
  TCP    GemFireTest:1035     GemFireTest:1156     ESTABLISHED
  TCP    GemFireTest:1046     GemFireTest:1099     ESTABLISHED
  TCP    GemFireTest:1049     osdc-proxy-vip.vmware.com:http  CLOSE_WA
  TCP    GemFireTest:1050     osdc-proxy-vip.vmware.com:3128  CLOSE_WA
  TCP    GemFireTest:1071     GemFireTest:0        LISTENING
  TCP    GemFireTest:1071     GemFireTest:1077     ESTABLISHED
  TCP    GemFireTest:1071     GemFireTest:1150     ESTABLISHED
  TCP    GemFireTest:1071     GemFireTest:1157     ESTABLISHED
  TCP    GemFireTest:1077     GemFireTest:1071     ESTABLISHED
  TCP    GemFireTest:1078     GemFireTest:24400    ESTABLISHED
  TCP    GemFireTest:1081     GemFireTest:1035     ESTABLISHED
  TCP    GemFireTest:1086     GemFireTest:1035     ESTABLISHED
  TCP    GemFireTest:1099     GemFireTest:1046     ESTABLISHED
  TCP    GemFireTest:1136     GemFireTest:0        LISTENING
  TCP    GemFireTest:1136     GemFireTest:1143     ESTABLISHED
  TCP    GemFireTest:1136     GemFireTest:1151     ESTABLISHED
  TCP    GemFireTest:1136     GemFireTest:1201     ESTABLISHED
  TCP    GemFireTest:1141     GemFireTest:4247     ESTABLISHED
  TCP    GemFireTest:1142     GemFireTest:48640    ESTABLISHED
  TCP    GemFireTest:1143     GemFireTest:1136     ESTABLISHED
  TCP    GemFireTest:1147     GemFireTest:1035     ESTABLISHED
  TCP    GemFireTest:1150     GemFireTest:1071     ESTABLISHED
  TCP    GemFireTest:1151     GemFireTest:1136     ESTABLISHED
  TCP    GemFireTest:1156     GemFireTest:1035     ESTABLISHED
  TCP    GemFireTest:1157     GemFireTest:1071     ESTABLISHED
  TCP    GemFireTest:1201     GemFireTest:1136     ESTABLISHED
  TCP    GemFireTest:1349     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:1350     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:1351     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:1352     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:1353     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:1354     GemFireTest:10334    TIME_WAIT
  TCP    GemFireTest:4247     GemFireTest:0        LISTENING
  TCP    GemFireTest:4247     GemFireTest:1141     ESTABLISHED
  TCP    GemFireTest:24400    GemFireTest:0        LISTENING
  TCP    GemFireTest:24400    GemFireTest:1078     ESTABLISHED
  TCP    GemFireTest:48640    GemFireTest:0        LISTENING
  TCP    GemFireTest:48640    GemFireTest:1142     ESTABLISHED
  UDP    GemFireTest:microsoft-ds  *:*
  UDP    GemFireTest:isakmp   *:*
  UDP    GemFireTest:4500     *:*
  UDP    GemFireTest:ntp      *:*
  UDP    GemFireTest:1900     *:*
  UDP    GemFireTest:ntp      *:*
  UDP    GemFireTest:netbios-ns  *:*
  UDP    GemFireTest:netbios-dgm  *:*
  UDP    GemFireTest:1900     *:*
  UDP    GemFireTest:32270    *:*
  UDP    GemFireTest:42838    *:*
  UDP    GemFireTest:47727    *:*
```



#### pause gateway-sender {#pause_gateway_sender}
Pause a gateway sender.

Pause the gateway sender on a member or members. See [Pausing Gateway Senders](https://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/multisite_overview.html#topic_9AA37B43642D4DE19072CA3367C849BA__section_hdt_2js_bq) for details on pausing gateway senders.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
pause gateway-sender --id=value [--groups=value(,value)*] 
[--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --id      | *Required.* ID of the gateway sender.                        |
| --groups  | Group(s) of members on which to pause the gateway sender.    |
| --members | Name or ID of the member(s) on which to pause the gateway sender. |

Table 1. Pause Gateway-Sender Parameters

**示例命令:**

```
pause gateway-sender --id=sender1
```



#### pdx rename {#pdx_rename}
Renames PDX types in an offline disk store.

Any PDX types that are renamed will be listed in the output. If no renames are done or the disk-store is online, then this command will fail.

**可用性:** Offline.

**句法:**

```
pdx rename --old=value --new=value --disk-store=value --disk-dirs=value(,value)*
```

| Name         | Description                                                  |
| :----------- | :----------------------------------------------------------- |
| --old        | *Required.* If a PDX type’s fully qualified class name has a word that matches this text then it will be renamed. Words are delimited by ’.’ and ’$’. |
| --new        | *Required.* The text to replace the word that matched old.   |
| ‑‑disk‑store | *Required.* Name of the disk store to operate on.            |
| --disk-dirs  | *Required.* Directories which contain the disk store files.  |

**示例命令:**

Change all packages that start with “com.gemstone” to “com.pivotal”:

```
gfsh>pdx rename --old=com.gemstone --new=com.pivotal --disk-store=ds1 --disk-dirs=/diskDir1
```

Change a class named “MyClassName” to “YourClassName”:

```
gfsh>pdx rename --old=MyClassName --new=YourClassName --disk-store=ds1 --disk-dirs=/diskDir1
```

Change the FQCN “com.target.app1.OldClass” to “com.target.app2.NewClass”:

```
gfsh>pdx rename --old=com.target.app1.OldClass --new=com.target.app2.NewClass --disk-store=ds1 --disk-dirs=/diskDir1
```

**示例输出:**

```
gfsh>pdx rename --old=PortfolioPdx --new=StockPdx --disk-store=DiskStore1 --disk-dirs=/DiskDir1
Successfully renamed pdx types:
  com.app.data.StockPdx: id=2
  com.app.data.StockPdx$Day.Monday
```

**错误消息:**

If no types match, you may receive the following error message:

```
gfsh>pdx rename --old=gemstone --new=pivotal --disk-store=DiskStore1 --disk-dirs=/DiskDir1
Could not process command due to GemFire error. No Pdx types found to rename.
```

If the disk-store where the PDX types are stored is online, you will receive the following error message:

```
gfsh>pdx rename --old=StockPdx --new=PortfolioPdx --disk-store=DiskStore1 --disk-dirs=/DiskDir1
Could not process command due to GemFire error. Error renaming pdx types :
GemFireCache[id = 484629896; isClosing = false; isShutDownAll = false;
closingGatewayHubsByShutdownAll = false; created = Wed Jan 07 10:29:45 PST 2015;
server = false; copyOnRead = false; lockLease = 120; lockTimeout = 60]: An open cache
already exists.
```



#### put {#put}
Add or update a region entry.

Add or update an entry in a region.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
put --key=value --value=value --region=value [--key-class=value] 
[--value-class=value] [--if-not-exists(=value)]
```

| Name            | Description                                                  | Default Value      |
| :-------------- | :----------------------------------------------------------- | :----------------- |
| --key           | *Required.* String or JSON text from which to create the key. For example: “`James`”, “`100L`” and “`('id': 'l34s')`”. |                    |
| --value         | *Required.* String or JSON text from which to create the value. For example: “`James`”, “`100L`” and “`('id': 'l34s')`”. |                    |
| --region        | *Required.* Region into which the entry will be put.         |                    |
| --key-class     | Fully qualified class name of the key’s type.                | `java.lang.String` |
| --value-class   | Fully qualified class name of the value’s type.              | `java.lang.String` |
| --if-not-exists | Skip the put operation when an entry with the same key already exists. | false              |

**示例命令:**

```
put --key=('id':'133abg125') --value=('firstname':'James','lastname':'Gosling') 
--region=/region1 --key-class=data.ProfileKey --value-class=data.ProfileDetails

put --key=('133abg124') --value=('Hello World!!') --region=/region2

put --key=('100F') --value=('2146547689879658564')  --region=/region1/region12 
--key-class=java.lang.Float --value-class=java.lang.Long
```

**示例输出:**

```
gfsh>put --key=('123abc') --value=('Hello World!!') --region=region2
Result      : true
Key Class   : java.lang.String
Key         : ('123abc')
Value Class : java.lang.String
Old Value   : <NULL>
```

**错误消息:**

```
"Region name is either empty or Null";
"Key is either empty or Null";
"Value is either empty or Null";
"Region <{0}> not found in any of the members";
"Region <{0}> Not Found";
"Key is not present in the region";
```



#### query {#query}
Run queries against Geode regions.

If a limit restricting the result size is not set in the query, then a default limit of the gfsh environment variable `APP_FETCH_SIZE`, as defined in [Useful gfsh Shell Variables](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/useful_gfsh_shell_variables.html), will be applied.

Surround the OQL query with single quote marks.

**注意:** This command should not be executed from `gfsh` if the objects being queried contain cyclic references.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
query --query=value [--step-name=value] [--file=path/to/results/file]
```



| Name    | Description                                                  |
| :------ | :----------------------------------------------------------- |
| --query | *Required.* The OQL string.                                  |
| --file  | When specified, all query results are written to the specified file. An error is issued if the file already exists. |

Table 1. Query Parameters

**示例输出:**

```
gfsh>query --query='SELECT * FROM /region2'

Result     : true
startCount : 0
endCount   : 20
Rows       : 1

Result
-----------------
('Hello World!!')

NEXT_STEP_NAME : END
```



#### rebalance {#rebalance}
Rebalance partitioned regions.

The default is for all partitioned regions to be rebalanced.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
rebalance [--include-region=value(,value)*] 
[--exclude-region=value(,value)*] [--time-out=value] [--simulate(=value)?]
```



| Name             | Description                                                  | Default Value                        |
| :--------------- | :----------------------------------------------------------- | :----------------------------------- |
| --include-region | Partitioned Region paths to be included for rebalance operation. Includes take precedence over excludes. |                                      |
| --exclude-region | Partitioned Region paths to be excluded for rebalance operation. |                                      |
| --time-out       | Time to wait (in seconds) before GFSH returns to a prompt while rebalancing continues in the background. | -1 (wait for rebalancing to complete |
| --simulate       | 是否只模拟重新平衡。 模拟时-time-out参数不可用. | false                                |

Table 1. Rebalance Parameters

**示例命令:**

```
rebalance --include-region=/region3 --simulate=true
```

**示例输出:**

```
rebalance
1. server1  host1(3467):12435:12423
Row  Rebalanced Stats               | Value 
---  ----------------               | ----- 
1    TotalBucketCreateBytes         | 0 
2    TotalBucketCreateTime          | 0 
3    TotalBucketCreatesCompleted    | 0 
4    TotalBucketTransferBytes       | 0 
5    TotalBucketTransferTime        | 0 
6    TotalBucketTransfersCompleted  | 0 
7    TotalPrimaryTransferTime       | 0 
8    TotalPrimaryTransfersCompleted | 0 
9    TotalTime                      | 56

Rebalance complete on host1(3467):12435:12423.
```



#### remove {#remove}
Remove an entry from a region.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
remove --region=value [--key=value] [--all(=value)?] [--key-class=value]
```



| Name        | Description                                                  | Default Value                                   |
| :---------- | :----------------------------------------------------------- | :---------------------------------------------- |
| --key       | String or JSON text that will be used to create a key to retrieve a value . |                                                 |
| ‑‑key‑class | Fully qualified class name of the key’s type.                | key constraint for the current region or String |
| --region    | *Required.* Region from which to remove the entry.           |                                                 |
| --all       | A boolean value that, when true, clears the region by removing all entries. This option is not available for partitioned regions. | false                                           |

**示例命令:**

```
gfsh>remove --region=/region1 --key=('id': '133abg134')
gfsh>remove --region=/region1 --key=('id': '133abg134') --key-class=data.ProfileKey 
gfsh>remove --region=/region1 --all=true
```

**错误消息:**

```
"Region name is either empty or Null"

"Key is either empty or Null"

"Value is either empty or Null"

"Region <{0}> not found in any of the members"

"Region <{0}> Not Found"

"Key is not present in the region"

"Option --all is not supported on partitioned region"
```



#### resume gateway-sender {#resume_gateway_sender}
Resume any gateway senders that you have paused.

Resume the gateway sender on a member or members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
resume gateway-sender --id=value [--groups=value(,value)*] 
[--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --id      | *Required.* ID of the Gateway Sender.                        |
| --groups  | Group(s) of members on which to resume the Gateway Sender.   |
| --members | Name/Id of the member(s) on which to resume the Gateway Sender. |

Table 1. Resume Gateway-Sender Parameters

**示例命令:**

```
resume gateway-sender --id=sender1-LN --groups=LN-Group1
```



#### revoke missing-disk-store {#revoke_missing_disk_store}
Instruct the member(s) of a cluster to stop waiting for a disk store to be available.

Only revoke a disk store if its files are lost as it will no longer be recoverable after revoking is initiated. Use the “show missing-disk-store” command to get descriptions of missing disk stores.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
revoke missing-disk-store --id=value
```



| Name | Description                                             |
| :--- | :------------------------------------------------------ |
| --id | *Required.* ID of the missing disk store to be revoked. |

Table 1. Revoke Missing-Disk-Store Parameters

**示例命令:**

```
revoke missing-disk-store --id=60399215-532b-406f-b81f-9b5bd8d1b55a
```

**示例输出:**

```
gfsh>revoke missing-disk-store --id=60399215-532b-406f-b81f-9b5bd8d1b55a
Missing disk store successfully revoked
```

**错误消息:**

Example of `revoke missing-disk-store` when the disk store cannot be found:

```
gfsh> revoke missing-disk-store --id=60399215-532b-406f-b81f

Unable to find missing disk store to revoke
```



#### run {#run}
Execute a set of GFSH commands.

Commands that normally prompt for additional input will instead use default values.

**可用性:** Online or offline.

**注意:** Some commands specified in the file require online status.

**句法:**

```
run --file=value [--quiet(=value)?] [--continue-on-error(=value)?]
```



| Name                | Description                                                  | Default Value |
| :------------------ | :----------------------------------------------------------- | :------------ |
| --file              | *Required.* Path of the file scripted with commands that `gfsh` recognizes. Path should be relative or absolute. |               |
| --quiet             | Specifies whether to show command output.**注意:**All commands in a script are run non-interactively when the `run`command is used. This option does not change that functionality. | false         |
| --continue-on-error | Specifies whether further execution of the script should continue if there is an error while executing one of the commands fails. | false         |

Table 1. Run Parameters

**示例命令:**

```
run --file=create-regions.gfsh --quiet=true

(2) From command line:
prompt> /home/user1/gemfire70/bin/gfsh run ./create-regions.gfsh --quiet=true
prompt> /home/user1/gemfire70/bin/gfsh run ./create-regions.gfsh 
--continue-on-error=true
```

**示例输出:**

```
gfsh>run --file=create-regions.gfsh
1. Executing - create region --name=region4 --type=REPLICATE

Member  | Status
------- | --------------------------------------
server2 | Region "/region4" created on "server2"
server1 | Region "/region4" created on "server1"

2. Executing - create region --name=region1/subregion1 --type=LOCAL

Parent region for "region1/subregion1" doesn't exist. 
```



#### search lucene {#search_lucene}
Search a Lucene index

See also [create lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#create_lucene_index), [describe lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#describe_lucene_index), [destroy lucene index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#destroy_lucene_index) and [list lucene indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#list_lucene_indexes).

**可用性:** Online.

**句法:**

```
search lucene --name=value --region=value --queryString=value --defaultField=value
    [--limit=value] [--keys-only=value]
```

**Parameters, search lucene:**

| Name           | Description                                                  | Default Value                            |
| :------------- | :----------------------------------------------------------- | :--------------------------------------- |
| --name         | *Required*. Name of the Lucene index to search.              |                                          |
| --region       | *Required*. Name/Path of the region where the Lucene index exists. |                                          |
| ‑‐queryString  | *Required*. Query string to search the Lucene index. Use `__REGION_VALUE_FIELD` as the field name within the query string when the field is a primitive value. Surround a string with double quote marks to do an exact match of the string. |                                          |
| ‑‐defaultField | *Required*. Default field to search in. `__REGION_VALUE_FIELD`identifies the field as a primitive value. |                                          |
| --limit        | Number of search results needed.                             | If the parameter is not specified: -1    |
| --keys-only    | Return only keys of search results.                          | If the parameter is not specified: false |

**示例命令:**

```
gfsh> search lucene --name=testIndex --region=/testRegion --queryString=value1
   --defaultField=__REGION_VALUE_FIELD


gfsh> search lucene --name=indexOfStrings --region=/stringTestRegion 
      --queryString='__REGION_VALUE_FIELD:"my exact string"'
      --defaultField=__REGION_VALUE_FIELD
```

**示例输出:**

```
gfsh>search lucene --name=testIndex --region=/testRegion --queryString=value* 
   --defaultField=__REGION_VALUE_FIELD
key | value  | score
--- | ------ | -----
3   | value3 | 1
2   | value2 | 1
1   | value1 | 1
gfsh>search lucene --region=/Person --name=analyzerIndex 
   --defaultField=addr --queryString="97763"
 key   |                         value                                      | score
------ | ------------------------------------------------------------------ | --------
key763 | Person{name='Kris Cat', addr='7 Ash St, Portland_OR_97763', emai.. | 1.669657
```



#### set variable {#set_variable}
Set variables in the GFSH environment.

Set GFSH variables that can be used by commands.

You can use the [echo](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/echo.html) command to view the value of a variable. For example, to see a list of all environment variables and their current values, use the following command:

```
gfsh>echo --string=$*
```

See [Useful gfsh Shell Variables](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/useful_gfsh_shell_variables.html#concept_731ECA5E40E943CBA5C1198A0745D8EE) for a description of preset environment variables.

**可用性:** Online or offline.

**句法:**

```
set variable --name=value --value=value
```



| Name    | Description                                                  |
| :------ | :----------------------------------------------------------- |
| ‑‑name  | *Required.* Name for the variable. Name must only be composed of letters, numbers and the “_” character and may not start with a number. |
| ‑‑value | *Required.* Value that the variable will be set to.          |

Table 1. Set Variable Parameters

**示例命令:**

```
set variable --name=APP_COLLECTION_LIMIT --value=10
set variable --name=FOO --value="foo"
set variable --name=BAR --value="bar"
```

**示例输出:**

```
gfsh>set variable --name=APP_COLLECTION_LIMIT --value=10
Value for variable APP_COLLECTION_LIMIT is now: 10.

gfsh>set variable --name=BAR --value="bar"
Value for variable BAR is now: "bar".
```



#### sh {#sh}
Execute operating system commands.

Executes operating system (OS) commands. Use ’`&`’ to return to the `gfsh` prompt immediately.

**注意:** Commands that pass output to another shell command are not supported.

**句法:**

```
sh command [--use-console(=value)?]
```



| Name          | Description                                                  | Default Value |
| :------------ | :----------------------------------------------------------- | :------------ |
| --use-console | Set this parameter on UNIX systems for applications which need a handle to the console. Adds “`</dev/tty >/dev/tty`” to the specified command. | false         |

Table 1. Sh Parameters

**示例命令:**

```
gfsh>sh ls -al
total 80
drwxrwxr-x. 10 username username  4096 Sep  3 15:10 .
drwxrwxr-x.  4 username username  4096 Sep  3 14:58 ..
drwx------.  2 username username  4096 Sep  3 15:09 bin
drwx------.  2 username username  4096 Sep  3 15:09 defaultConfigs
drwx------.  3 username username  4096 Sep  3 15:09 docs
drwx------.  2 username username  4096 Sep  3 15:09 dtd
-rwx------.  1 username username 31830 Sep  3 15:09 EULA.txt
drwx------.  2 username username  4096 Sep  3 15:09 lib
drwx------.  6 username username  4096 Sep  3 15:09 SampleCode
drwx------.  4 username username  4096 Sep  3 15:09 templates
drwx------.  5 username username  4096 Sep  3 15:09 tools
```



#### show {#show}
Display deadlocks, logs, metrics and missing disk-stores.

- **show dead-locks**

  Display any deadlocks in the cluster.

- **show log**

  Display the log for a member.

- **show metrics**

  Display or export metrics for the entire cluster, a member, or a region.

- **show missing-disk-stores**

  Display a summary of the disk stores that are currently missing from the cluster.

- **show subscription-queue-size**

  Shows the number of events in the subscription queue.

##### show dead-locks {#show_dead_locks}
Display any deadlocks in the cluster.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
show dead-locks --file=value
```



| Name   | Description                                                  |
| :----- | :----------------------------------------------------------- |
| --file | *Required.* Name of the file to which dependencies between members will be written. |

Table 1. Show Dead-Locks Parameters

**示例命令:**

```
show dead-locks --file=deadlocks.txt
```

**示例输出:**

```
gfsh>show dead-locks --file=deadlocks.txt
No dead lock detected.

Please view the dependencies between the members in file : deadlocks.txt
```

##### show log {#show_log}
Display the log for a member.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
show log --member=value [--lines=value]
```



| Name     | Description                                                  | Default Value |
| :------- | :----------------------------------------------------------- | :------------ |
| --member | *Required.* Name/ID of the member whose log file will be displayed. |               |
| --lines  | Number of lines from the log file to display. The maximum is 100. | 0             |

**示例命令:**

```
show log --member=locator1 --lines=5
```

**示例输出:**

```
gfsh>show log --member=locator1 --lines=5
SystemLog:
[info 2012/09/25 14:04:51.340 PDT locator1 <RMI TCP Connection(14)-192.0.2.0> tid=0x57] (tid=12 msgId=4) Parent region for "region1/subregion1" doesnt exi
st.

[info 2012/09/25 14:04:51.372 PDT locator1 <RMI TCP Connection(14)-192.0.2.0> tid=0x57] (tid=12 msgId=5) Error occurred while executing "create region --n
ame=region1/subregion1 --type=LOCAL".

[info 2012/09/25 15:14:34.314 PDT locator1 <RMI TCP Connection(159)-192.0.2.0> tid=0x68] (tid=13 msgId=6) Error occurred while executing "show log --membe
r=server1 --lines=5".
```

##### show metrics {#show_metrics}
Display or export metrics for the entire cluster, a member, or a region.

When no command line arguments are given, metrics under the categories of cluster, cache, diskstore, and query are displayed.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
show metrics [--member=value] [--region=value] [--file=value]
[--port=value] [--categories=value(,value)*]
```



| Name         | Description                                                  |
| :----------- | :----------------------------------------------------------- |
| --member     | Name/ID of the member whose metrics will be displayed/exported. |
| --region     | Name/Path of the region whose metrics will be displayed/exported. |
| --file       | Name of the file to which metrics will be written.           |
| --port       | Port number of the Cache Server whose metrics are to be displayed/exported. This can only be used along with the `--member`parameter. |
| --categories | Categories available based upon the parameters specified (listed above) are:**region specified**: cluster, region, partition, diskstore, callback, eviction**member specified**: member, jvm, region, serialization, communication, function, transaction, diskstore, lock, eviction, distribution, offheap**member and region specified**: region, partition, diskstore, callback, eviction |

Table 3. Show Metrics Parameters

**示例命令:**

```
// Metrics for the entire system
show metrics
// Metrics for a region:
show metrics --region=region1
// Metrics for a given member
show metrics --member=server1
// Metrics for a region on a member
show metrics --region=region1 --member=server1
//  Metrics for a member and the cacheserver it hosts
// NOTE: port option only work when used with --member option
show metrics --member=server1 --port=10334
// Export metrics for the entire system
show metrics --file=data/stats/system-stats.csv
```

**示例输出:**

```
gfsh>show metrics

Cluster-wide Metrics

  Type    |        Metric         | Value
--------- | --------------------- | -----
cluster   | totalHeapSize         | 123
cache     | totalRegionEntryCount | 0
          | totalRegionCount      | 0
          | totalMissCount        | 0
          | totalHitCount         | 0
diskstore | totalBytesOnDisk      | 0
          | diskReadsRate         | 0
          | diskWritesRate        | 0
          | flushTimeAvgLatency   | 0
          | totalBackupInProgress | 0
query     | queryRequestRate      | 0
```

##### show missing-disk-stores {#show_missing_disk_stores}
Display a summary of the disk stores that are currently missing from the cluster.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
show missing-disk-stores
```

**示例命令:**

```
show missing-disk-stores
```

**示例输出:**

```
gfsh> show missing-disk-stores

           Disk Store ID             |   Host    |               Directory
------------------------------------ | --------- | -------------------------------------
60399215-532b-406f-b81f-9b5bd8d1b55a | excalibur | /usr/local/gemfire/deploy/disk_store1
```

##### show subscription-queue-size {#show_subscription_queue_size}
Shows the number of events in the subscription queue.

If a CQ name is provided, it counts the number of events in the subscription queue for the specified CQ.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
show subscription-queue-size --durable-client-Id=value
 [--members=value(,value)*] [--groups=value(,value)*]
```



| Name                | Description                                                  |
| :------------------ | :----------------------------------------------------------- |
| --durable-client-id | *Required.* The ID used to identify the durable client.      |
| --durable-cq-name   | The name that identifies the CQ.                             |
| --members           | Name/Id of the member(s) for which subscription queue events are to be counted. |
| --groups            | Group(s) of members for which subscription queue events are to be counted. |

Table 4. Show Subscription-Queue-Size Parameters

**示例命令:**

```
show subscription-queue-size --durable-client-id=client1
```

**示例输出:**

```
gfsh>show subscription-queue-size --durable-client-Id=client1
member  | subcription-queue-size for durable-client : "client1".
------- | ------------------------------------------------------
server3 | 1
server4 | 0
```

**错误消息:**

```
gfsh>show subscription-queue-size --durable-client-Id=client1

No client found with client-id : client1
Occurred on members
1.server4
2.server1
3.server3
```



#### shutdown {#shutdown}
Stop all members.

Asks all the members that have a cache to close the cache and disconnect from the system. If the `--include-locators` parameter is specified, the command shuts down any running locators one by one. The timeout parameter allows you to specify that the system should be shutdown forcibly after the time has exceeded.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
shutdown [--time-out=value] [--include-locators=value]
```



| Name               | Description                                                  | Default Value |
| :----------------- | :----------------------------------------------------------- | :------------ |
| --time-out         | Time to wait (in seconds) for a graceful shutdown. Should be at least 10 seconds. The default value if not specified is 10. | 10            |
| --include-locators | To shutdown locators, specify this option as true.           | false         |

Table 1. Shutdown Parameters

**示例命令:**

```
shutdown
shutdown --time-out=15
shutdown --include-locators=true
```

**示例输出:**

```
gfsh>shutdown
"As a lot of data in memory will be lost, including possibly events in
queues, do you really want to shutdown the entire distributed system? (Y/n):"
Y
Shutdown is triggered
```



#### sleep {#sleep}
Delay `gfsh` command execution.

Delay for a specified amount of time in seconds - floating point values are allowed.

**可用性:** Online of offline.

**句法:**

```
sleep [--time=value]
```



| Name   | Description                 | Default Value |
| :----- | :-------------------------- | :------------ |
| --time | Number of seconds to sleep. | 3             |

Table 1. Sleep Parameters

**示例命令:**

```
sleep
sleep --time=60
```

**示例输出:**

```
gfsh>sleep --time=60

gfsh>
```



#### start {#start}
Start servers, locators, gateway senders and gateway receivers, and monitoring tools.

- **start gateway-receiver**

  Start the gateway receiver on a given member or group of members.

- **start gateway-sender**

  Start the gateway sender on a member or members.

- **start jconsole**

  Start the JDK JConsole monitoring application in a separate process.

- **start jvisualvm**

  Start the JDK’s Java VisualVM monitoring application in a separate process.

- **start locator**

  Start a locator.

- **start pulse**

  Launch the Geode Pulse monitoring dashboard tool in the user’s default system browser and navigates the user to the landing page (login page).

- **start server**

  Start a Geode cache server process.

##### start gateway-receiver {#start_gateway_receiver}
Start the gateway receiver on a given member or group of members.

Note that you can only have one gateway receiver on each member, and unlike a gateway sender, you do not need to specify an identifier for the gateway receiver.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
start gateway-receiver [--groups=value(,value)*] [--members=value(,value)*]
```

**Parameters, start gateway-receiver**

| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --members | Name or ID of the member(s) on which to start the Gateway Receiver. |
| --groups  | Group(s) of members on which to start the Gateway Receiver.  |

**示例命令:**

```
start gateway-receiver
start gateway-receiver --members=member1
```

**示例输出:**

```
gfsh>start gateway-receiver
      Member                | Result | Message
--------------------------- | -------| -----------------------------------------------------------------------
pc13(2266)<v6>:56852        | OK     | GatewayReceiver is started on member pc13(2266)<v6>:56852
pc13(Manager:2242)<v5>:57631| Error  | GatewayReceiver is not available on member pc13(Manager:2242)<v5>:57631
pc13(2275)<v7>:47480        | OK     | GatewayReceiver is started on member pc13(2275)<v7>:47480
pc13(2293)<v8>:55472        | OK     | GatewayReceiver is started on member pc13(2293)<v8>:55472

gfsh>start gateway-receiver --members=pc13(2266)<v14>:36579
GatewayReceiver is started on member pc13(2266)<v14>:36579

gfsh>start gateway-receiver --group=RG1
         Member      | Result | Message
-------------------- | -------| ----------------------------------------------------------
pc13(2275)<v23>:27484| OK     | GatewayReceiver is started on member pc13(2275)<v23>:27484
pc13(2293)<v24>:55810| OK     | GatewayReceiver is started on member pc13(2293)<v24>:55810
pc13(2266)<v22>:4522 | OK     | GatewayReceiver is started on member pc13(2266)<v22>:4522
```

##### start gateway-sender {#start_gateway_sender}
Start the gateway sender on a member or members.

For information on how to configure a gateway sender, see [Configure Gateway Senders](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html#setting_up_a_multisite_system__section_1500299A8F9A4C2385680E337F5D3DEC).

Note: By default, gateway senders are configured to start automatically. Manual restart introduces a risk of data loss; it is not intended for production systems.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
start gateway-sender --id=value [--groups=value(,value)*] [--members=value(,value)*]
```

**Parameters, start gateway-sender**

| Name      | Description                                               |
| :-------- | :-------------------------------------------------------- |
| --id      | *Required.* ID of the GatewaySender.                      |
| --groups  | Group(s) of members on which to start the Gateway Sender. |
| --members | Member(s) on which to start the Gateway Sender            |

**示例命令:**

```
start gateway-sender --id=sender1-NY
start gateway-sender --id=sender1-NY --members=server1
start gateway-sender --id=sender1-NY --groups=MemberGroup1,MemberGroup2
```

**示例输出:**

```
gfsh>start gateway-sender --id=ln
 Member                       |  Result |                   Message
------------------------------| ------- | -------------------------------------------------------------------------
pc13(30614)<v6>:63670         | OK      | GatewaySender ln is started on member pc13(30614)<v6>:63670
pc13(30621)<v7>:36015         | OK      | GatewaySender ln is started on member pc13(30621)<v7>:36015
pc13(30633)<v8>:13633         | OK      | GatewaySender ln is started on member pc13(30633)<v8>:13633
pc13(Manager:30588)<v5>:42792 | Error   | GatewaySender ln is not available on member pc13(Manager:30588)<v5>:42792

gfsh>start gateway-sender --id=ln --members=pc13(30614)<v14>:44519
GatewaySender ln is started on member pc13(30614)<v14>:44519
gfsh>start gateway-sender --id=ln --groups=SenderGroup1
Member                 | Result| Message
---------------------- | ------| ------------------------------------------------------------
pc13(30614)<v18>:15201 | OK    | GatewaySender ln is started on member pc13(30614)<v18>:15201
pc13(30621)<v19>:61437 | OK    | GatewaySender ln is started on member pc13(30621)<v19>:61437
pc13(30633)<v20>:22567 | OK    | GatewaySender ln is started on member pc13(30633)<v20>:22567
```

##### start jconsole {#start_jconsole}
Start the JDK JConsole monitoring application in a separate process.

JConsole automatically connects to a running JMX Manager node if one is available.

Note that you must have a JDK installed (not just a JRE) and the correct PATH and JAVA_HOME environment variables set.

See [Browsing Geode MBeans through JConsole](https://geode.apache.org/docs/guide/17/managing/management/mbeans_jconsole.html) for an example of using JConsole with the Geode management and monitoring system.

**可用性:** Online or offline.

**句法:**

```
start jconsole [--interval=<seconds>] [--notile] [--version]
[--J<jconsole JVM options>]
```

**Parameters, start jconsole**

| Name         | Description                                                  | Default Value |
| :----------- | :----------------------------------------------------------- | :------------ |
| --interval   | Set the update interval to n seconds (default is 4 seconds). (Equivalent to JConsole’s `-interval=n`) | 4             |
| --notile     | Whether to initially tile windows for two or more connections. This parameter is passed as `-notile` to JConsole. | false         |
| ‑‑pluginpath | Directories or JAR files which are searched for JConsole plugins. The path should contain a provider-configuration file named:`META-INF/services/com.sun.tools.jconsole.JConsolePlugin` containing one line for each plugin specifying the fully qualified class name of the class implementing the `com.sun.tools.jconsole.JConsolePlugin` class. |               |
| --version    | Display the JConsole version information. This parameter is passed as `-version` to JConsole. | false         |
| --J          | Arguments passed to the JVM on which JConsole runs           |               |

**示例命令:**

```
gfsh>start jconsole --interval=8 --notile;
Running JDK JConsole

gfsh>start jconsole --version;
JConsole version "1.8.0_31-b01-1"
Java(TM) SE Runtime Environment (build 1.8.0_31-b01-1-11)
Java HotSpot(TM) 64-Bit Server VM (build 20.6-b01-11, mixed mode)
```

**示例输出:**

```
gfsh>start jconsole
Running JDK JConsole
```

The JConsole application appears and auto-connects to a JMX Manager node if one is available:

![img](assets/JConsole.png)

**错误消息:**

```
An error occurred while launching JConsole = %1$s

Connecting by the Geode member's name or ID is not currently supported.
Please specify the member as '<hostname|IP>[PORT].

An IO error occurred while launching JConsole.
Please ensure that JAVA_HOME is set to the JDK installation
or the JDK bin directory is in the system PATH.

JConsole could not be found.\nPlease ensure that JAVA_HOME is set to the
JDK installation or the JDK bin directory is in the system PATH.
```

##### start jvisualvm {#start_jvisualvm}
Start the JDK’s Java VisualVM monitoring application in a separate process.

**可用性:** Online or offline.

**句法:**

```
start jvisualvm [--J=value(,value)*]
```

**Parameters, start jvisualvm**

| Name | Description                                                  |
| :--- | :----------------------------------------------------------- |
| --J  | VM-option passed to the spawned CacheServer VM. For example: `--J=-Dfoo.bar=true`for setting foo.bar to ‘true’. |

**示例命令:**

```
start jvisualvm
```

**示例输出:** ![img](assets/jvisualvm.png)

##### start locator {#start_locator}
Start a locator.

The command creates a subdirectory and log file named after the locator. If the locator detects that no other JMX Manager exists, then the locator will automatically start an embedded JMX Manager and connect the current `gfsh` session to the JMX Manager.

**注意:** You must have `JAVA_HOME` set before starting gfsh to use this command.

In addition, if gfsh is not already connected to a JMX Manager, the gfsh console will automatically connect to the new embedded JMX Manager started by the new locator.

**注意:** When both `--max-heap` and `--initial-heap` are specified during locator startup, additional GC parameters are specified internally by Geode’s Resource Manager. If you do not want the additional default GC properties set by the Resource Manager, then use the`-Xms` and `-Xmx` JVM options. See [Controlling Heap Use with the Resource Manager](https://geode.apache.org/docs/guide/17/managing/heap_use/heap_management.html#configuring_resource_manager) for more information.

**可用性:** Online or offline.

**句法:**

```
start locator --name=value [--bind-address=value] [--force(=value)]
 [--groups=value(,value)*] [--hostname-for-clients=value]
 [--locators=value] [--log-level=value] [--mcast-address=value] [--mcast-port=value] [--port=value] [--dir=value]
 [--properties-file=value] [--security-properties-file=value] [--initial-heap=value] [--max-heap=value]
 [--connect(=value)] [--enable-cluster-configuration(=value)] [--load-cluster-configuration-from-dir(=value)]
 [--cluster-config-dir=value] [--redirect-output(=value)]
 [--http-service-port=value] [--http-service-bind-address=value] 
 [--J=value(,value)*]
```

**Parameters, start locator**

| Name                                  | Description                                                  | Default Value                    |
| :------------------------------------ | :----------------------------------------------------------- | :------------------------------- |
| --name                                | Name to be used for this Geode locator service. If not specified, gfsh generates a random name. |                                  |
| --bind-address                        | IP address on which the locator will be bound.               | bind to all addresses            |
| --force                               | Whether to allow the PID file from a previous locator run to be overwritten. | false                            |
| --groups                              | Group(s) the locator will be a part of.                      |                                  |
| --hostname-for-clients                | Host name or IP address that will be sent to clients so they can connect to this locator. | uses `bind-address`              |
| --locators                            | List of locators used by this locator to join the appropriate Geode cluster. |                                  |
| --log-level                           | Level of output logged to the locator log file. Possible values for log-level include: ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF. |                                  |
| --mcast-address                       | IP address or hostname used to bind the UPD socket for multi-cast networking so the locator can locate other members in the Geode cluster. If mcast-port is zero, then mcast-address is ignored. |                                  |
| --mcast-port                          | Port used for multi-cast networking so the locator can locate other members of the Geode cluster. A zero value disables mcast. |                                  |
| --port                                | Port the locator will listen on.                             | 10334                            |
| --dir                                 | Directory in which the Locator will be started and run.      | ./<locator-member-name>          |
| --properties-file                     | Specify the `gemfire.properties` file for configuring the locator’s cluster. The file’s path should be absolute or relative to gfsh’s working directory. |                                  |
| --security-properties-file            | The gfsecurity.properties file for configuring the Locator’s security configuration in the cluster. The file’s path can be absolute or relative to gfsh’s working directory. |                                  |
| --initial-heap                        | Size has the same format as the `-Xmx`/`-Xms` JVM options.**注意:** If you use the `-J-Xms` and `-J-Xmx` JVM properties instead of `-initial-heap` and `-max-heap`, then Geode does not use default JVM resource management properties. If you use the JVM properties, you must then specify all properties manually for eviction, garbage collection, heap percentage, and so forth. |                                  |
| --max-heap                            | Size has the same format as the `-Xmx`/`-Xms` JVM options**注意:** If you use the `-J-Xms` and `-J-Xmx` JVM properties instead of `-initial-heap` and `-max-heap`, then Geode does not use default JVM resource management properties. If you use the JVM properties, you must then specify all properties manually for eviction, garbage collection, heap percentage, and so forth. |                                  |
| --connect                             | When connect is set to false, gfsh does not automatically connect to the locator which is started using this command. | true                             |
| --enable-cluster-configuration        | Enables cluster configuration behavior where locators maintain configurations for all members of the cluster. See [Overview of the Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html). | true                             |
| --load-cluster-configuration-from-dir | **Deprecated. Use gfsh import cluster-configuration for this functionality.**Loads the cluster configuration from the shared-config directory. (When set to false, the configuration is loaded from the disk store of the internal, persistent region used by the locator to persist the configuration.) | false                            |
| --cluster-config-dir                  | Directory used by the cluster configuration service to store the cluster configuration on the filesystem | cluster-config                   |
| --redirect-output                     | When true, redirect standard output and standard error to the locator log file. If specified without a value, the value is set to true. | false                            |
| --http-service-port                   | Specifies the HTTP service port.                             | 7070                             |
| --http-service-bind-address           | Specifies the IP address to which the HTTP service will be bound. | the local host machine’s address |
| --J                                   | Argument passed to the JVM on which the Locator will run. For example, specifying `--J=-Dfoo.bar=true` sets property “foo.bar” to “true”.**注意:**If the argument you are passing contains spaces or commas, enclose the option in single quotes. For example:`start locator --name=locator1 --port=9009 --mcast-port=0\ --J='-Dgemfire.remote-locators=192.0.2.0[9009],192.0.2.1[9009]'` | none                             |

**示例命令:**

```
start locator --name=locator1
```

##### start pulse {#start_pulse}
Launch the Geode Pulse monitoring dashboard tool in the user’s default system browser and navigates the user to the landing page (login page).

For more information on Geode Pulse, see [Geode Pulse](https://geode.apache.org/docs/guide/17/tools_modules/pulse/pulse-overview.html).

**可用性:** Online or offline.

**句法:**

```
start pulse [--url=value]
```

**Parameters, start pulse**

| Name  | Description                      | Default                       |
| :---- | :------------------------------- | :---------------------------- |
| --url | URL of the Pulse Web application | `http://localhost:7070/pulse` |

**示例命令:**

```
start pulse
start pulse --url=http://gemfire.example.com:7070/pulse
```

**示例输出:** See [Geode Pulse](https://geode.apache.org/docs/guide/17/tools_modules/pulse/pulse-overview.html) for examples of Pulse.

##### start server {#start_server}
Start a Geode cache server process.

**注意:** When both --max-heap and --initial-heap are specified during server startup, additional GC parameters are specified on your behalf. If you do not want the additional default GC properties set, then use the `-Xms` and `-Xmx` JVM options to set just these parameters. See [Controlling Heap Use with the Resource Manager](https://geode.apache.org/docs/guide/17/managing/heap_use/heap_management.html#configuring_resource_manager) for more information.

**可用性:** Online or offline.

**句法:**

```
start server --name=value [--assign-buckets(=value)] [--bind-address=value]
    [--cache-xml-file=value] [--classpath=value] [--disable-default-server(=value)]
    [--disable-exit-when-out-of-memory(=value)] [--enable-time-statistics(=value)]
    [--force(=value)] [--include-system-classpath(=value)] [--properties-file=value]
    [--security-properties-file=value] [--groups=value(,value)*]
    [--locators=value] [--locator-wait-time=value] [--log-level=value]
    [--mcast-address=value] [--mcast-port=value] [--memcached-port=value]
    [--memcached-protocol=value] [--rebalance(=value)] [--server-bind-address=value]
    [--server-port=value] [--spring-xml-location=value]
    [--statistic-archive-file=value] [--dir=value] [--initial-heap=value]
    [--max-heap=value] [--use-cluster-configuration(=value)] [--J=value(,value)*]
    [--critical-heap-percentage=value] [--critical-off-heap-percentage=value]
    [--eviction-heap-percentage=value] [--eviction-off-heap-percentage=value]
    [--hostname-for-clients=value] [--max-connections=value]
    [--message-time-to-live=value] [--max-message-count=value] [--max-threads=value]
    [--socket-buffer-size=value] [--lock-memory=value] [--off-heap-memory-size=value]
    [--start-rest-api=value] [--redirect-output(=value)]
    [--http-service-port=value] [--http-service-bind-address=value]
    [--user=value] [--password=value]
```

**Parameters, start server**

| Name                              | Description                                                  | Default Value                                                |
| :-------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| --name                            | Member name for this server. If not specified, gfsh generates a random name. |                                                              |
| --assign-buckets                  | Whether to assign buckets to the partitioned regions of the cache on server start. | false                                                        |
| --bind-address                    | The IP address on which the server will be bound.            | binds to all local addresses                                 |
| --cache-xml-file                  | Specifies the name of the XML file or resource to initialize the cache with when it is created. |                                                              |
| --classpath                       | Application classes added to the server’s CLASSPATH after the core jar file. See [Setting Up the CLASSPATH](https://geode.apache.org/docs/guide/17/getting_started/setup_classpath.html) for details. |                                                              |
| --include-system-classpath        | When true, include the System CLASSPATH on the Server’s CLASSPATH, as the System CLASSPATH is not included by default. If specified without a value, the value is set to true. | false                                                        |
| --disable-default-server          | Whether the cache server will be started by default. If the parameter is specified without a value, the value is set to true. If set to true, the cache server acts as a peer. | false                                                        |
| --disable-exit-when-out-of-memory | Prevents the JVM from exiting when an OutOfMemoryError occurs. | false                                                        |
| --enable-time-statistics          | Causes additional time-based statistics to be gathered for Geode operations. | true                                                         |
| --properties-file                 | The gemfire.properties file for configuring the server’s cluster. The file’s path can be absolute or relative to the gfsh working directory. |                                                              |
| --security-properties-file        | The gfsecurity.propertiesfile for configuring the server’s security configuration in the cluster. The file’s path can be absolute or relative to gfsh directory. |                                                              |
| --groups                          | Group(s) the Cache Server will be a part of.                 |                                                              |
| --force                           | Whether to allow the PID file from a previous Cache Server run to be overwritten. | false                                                        |
| --locators                        | Sets the list of locators used by the Cache Server to join the appropriate Geode cluster. |                                                              |
| --locator-wait-time               | Sets the number of seconds the server will wait for a locator to become available during startup before giving up. | 0                                                            |
| --log-level                       | Sets the level of output logged to the Cache Server log file. Possible values for log-level include: ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF. |                                                              |
| --mcast-address                   | The IP address or hostname used to bind the UDP socket for multi-cast networking so the Cache Server can locate other members in the Geode cluster. If mcast-port is zero, then mcast-address is ignored. |                                                              |
| --mcast-port                      | Sets the port used for multi-cast networking so the Cache Server can locate other members of the Geode cluster. A zero value disables mcast. |                                                              |
| --memcached-port                  | If specified and is non-zero, sets the port number for an embedded Gemcached server and starts the Gemcached server. |                                                              |
| --memcached-protocol              | Sets the protocol used by an embedded Gemcached server. Valid values are `BINARY` and `ASCII.` If you omit this property, the ASCII protocol is used. |                                                              |
| --server-bind-address             | Overrides the `bind-address`on which this server will listen for client connections. Set this option in a multi-homed server environment to distinguish communications from clients. Setting a value of the empty string (“”) uses the value of `bind-address`. | value of `bind-address`                                      |
| --server-port                     | Port the Server will listen on for client connections.       | 40404                                                        |
| --spring-xml-location             | Specifies the location of a Spring XML configuration file(s) for bootstrapping and configuring a Geode Server. This configuration file can exist on the CLASSPATH (default) or any location supported by Spring’s Resource(Loader) location specifiers (for example, classpath:, file:, etc). ResourceLoader is described in the [Spring documentation](http://docs.spring.io/spring/docs/4.0.9.RELEASE/spring-framework-reference/htmlsingle/#resources-resourceloader). |                                                              |
| --rebalance                       | Whether to initiate rebalancing across the Geode cluster.    | false                                                        |
| --dir                             | Specify the directory in which the server will run in. This directory is written to the location where you started `gfsh`. | If not specified, the directory is named after the server.   |
| --statistic-archive-file          | The file that statistic samples are written to. For example: “StatisticsArchiveFile.gfs”. Must be defined to store the archiving to a file. An empty string (default) disables statistic archival. | *not set*                                                    |
| --initial-heap                    | Initial size of the heap in the same format as the JVM -Xms parameter.**注意:** If you use the `--J=-Xms` and `--J=-Xmx` JVM properties instead of `--initial-heap` and `--max-heap`, then Geode does not use default JVM resource management properties. If you use the JVM properties, you must then specify all properties manually for eviction, garbage collection, heap percentage, and so forth. |                                                              |
| --max-heap                        | Maximum size of the heap in the same format as the JVM -Xmx parameter.**注意:** If you use the `--J=-Xms` and `--J=-Xmx` JVM properties instead of `--initial-heap` and `--max-heap`, then Geode does not use default JVM resource management properties. If you use the JVM properties, you must then specify all properties manually for eviction, garbage collection, heap percentage, and so forth. |                                                              |
| --J                               | Argument passed to the JVM on which the Cache Server will run. For example, `--J=-Dfoo.bar=true` will set the property “foo.bar” to “true”.If the argument you are passing contains spaces or commas, enclose the option in single quotes. |                                                              |
| --use-cluster-configuration       | Specifies whether the server requests a cluster configuration from the locator. See [Overview of the Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html). | true                                                         |
| --critical-heap-percentage        | Set the percentage of heap at or above which the cache is considered in danger of becoming inoperable due to garbage collection pauses or out of memory exceptions. Past the threshold, operations that require heap space will throw a `LowMemoryException`. This feature requires additional VM flags to perform properly; you must set `--initial-heap`and `--max-heap` or the corresponding JVM properties to use this threshold. You must also set `--max-heap` and `--initial-heap` to the same value. | 0 (no critical heap threshold enforced)                      |
| --critical-off-heap-percentage    | The percentage of off-heap memory used at or above which the cache is considered in danger of becoming inoperable due to out of memory exceptions. Past the threshold, operations that require heap space will throw a `LowMemoryException`. | 0 (no critical off-heap threshold enforced)                  |
| --eviction-heap-percentage        | Set the percentage of heap at or above which the eviction should begin on Regions configured for HeapLRU eviction. Changing this value may cause eviction to begin immediately. Only one change to this attribute or critical heap percentage will be allowed at any given time and its effect will be fully realized before the next change is allowed. This feature requires additional VM flags to perform properly; you must set `--initial-heap`and `--max-heap` or the corresponding JVM properties to use this threshold. You must also set `--max-heap` and `--initial-heap` to the same value. | 0, if no region is configured with heap evictionIf `critical-heap-percentage`is set to a non-zero value, 5% less than that value.80%, if `critical-heap-percentage`is not configured. |
| --eviction-off-heap-percentage    | The percentage of off-heap memory used at or above which the eviction should begin on regions configured for off-heap and HeapLRU eviction. Changing this value may cause eviction to begin immediately. Only one change to this attribute or critical off-heap percentage will be allowed at any given time, and its effect will be fully realized before the next change is allowed. | 0, if no region is configured with heap evictionIf `critical-off-heap-percentage`is set to a non-zero value, 5% less than that value.80%, if `critical-off-heap-percentage`is not configured. |
| --hostname-for-clients            | Sets the IP address or host name that a locator will provide to clients. Clients use the address to connect to a server. Set this value when clients use a different address to connect with the server than the `bind-address`, as those clients might with servers in a private cloud or multi-homed environment. Not specifying this option or setting this option to the empty string (“”) causes the `bind-address` to be given to clients. |                                                              |
| --max-connections                 | Sets the maximum number of client connections allowed. When the maximum is reached the cache server will stop accepting connections |                                                              |
| --message-time-to-live            | Sets the time (in seconds ) after which a message in the client queue will expire. |                                                              |
| --max-message-count               | Sets maximum number of messages that can be enqueued in a client-queue. |                                                              |
| --max-threads                     | Sets the maximum number of threads allowed in this cache server to service client requests. The default of 0 causes the cache server to dedicate a thread for every client connection. |                                                              |
| --socket-buffer-size              | Sets the buffer size in bytes of the socket connection for this CacheServer. The default is 32768 bytes. |                                                              |
| --lock-memory                     | (Linux only) When true, the member’s heap and off-heap memory are locked in RAM, preventing them from being paged to disk. You must increase the related `ulimit`operating system resource to allow the OS to lock memory chunks of sufficient size. | false                                                        |
| --off-heap-memory-size            | The integer quantity of off-heap memory to be used for storing region values. Specified in Gigabytes with a 'g’ suffix, or Megabytes with an ’m’ suffix. For example, allocate a 2 Gigabyte off-heap space with `--off-heap-memory-size=2g`. The default value of 0 does not use any off-heap memory. | 0                                                            |
| --start-rest-api                  | When true, starts the REST API service.                      | false                                                        |
| --redirect-output                 | When true, redirect standard output and standard error to the server log file. If specified without a value, the value is set to true. | false                                                        |
| --http-service-port               | Specifies the HTTP service port.                             | 7070                                                         |
| --http-service-bind-address       | Specifies the IP address to which the HTTP service will be bound. | the local host machine’s address                             |
| --user                            | The user name of the credential to use in authenticating to the cluster. When specified, if the `--password` option is not also specified, then `gfsh` will prompt for the password. |                                                              |
| --password                        | The password portion of the credential to use in authenticating to the cluster. |                                                              |

**Examples**

```
gfsh>start server --name=server1
gfsh>start server --name=server2 --server-port=40405
```



#### status {#status}
Check the status of the cluster configuration service and Geode member processes, including locators, gateway receivers, gateway senders, and servers.

- **status cluster-config-service**

  Displays the status of the cluster configuration service.

- **status gateway-receiver**

  Display the status of the specified gateway receiver.

- **status gateway-sender**

  Display the status of the specified gateway sender.

- **status locator**

  Displays the status of the specified locator.

- **status server**

  Display the status of the specified Geode cache server.

##### status cluster-config-service {#status_cluster_config_service}
Displays the status of the cluster configuration service.

Displays the status of cluster configuration service on all the locators where enable-cluster-configuration is set to `true`.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
status cluster-config-service
```

**示例命令:**

```
status cluster-config-service
```

**示例输出:**

```
gfsh>status cluster-config-service
Status of shared configuration on locators

  Name   | Status
-------- | -------
locator8 | RUNNING
```

##### status gateway-receiver {#status_gateway_receiver}
Display the status of the specified gateway receiver.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
status gateway-receiver [--groups=value(,value)*] [--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --groups  | Group(s) of Gateway Receivers for which to display status.   |
| --members | Name or ID of the Gateway Receiver(s) for which to display status. |

Table 1. Status Gateway-Receiver Parameters

**示例命令:**

```
status gateway-receiver --groups=LN-Group1
status gateway-receiver --members=server1
```

**示例输出:**

```
gfsh>status gateway-receiver
Member               | Port  | Status
---------------------| ------| -------
pc13(8151)<v2>:26518 | 26837 | Running
pc13(8175)<v4>:53787 | 23753 | Running
pc13(8164)<v3>:24081 | 25457 | Running

Member                       | Error
-----------------------------| ---------------------------------------------------
pc13(Manager:8124)<v1>:52410 | GatewayReceiver is not available or already stopped
pc13(8130):8180              | GatewayReceiver is not available or already stopped


gfsh>status gateway-receiver --members=pc13(8151)<v2>:50130
Member               | Port  |  Status
-------------------- | ----- | --------
pc13(8151)<v2>:50130 | 28592 | Running

gfsh>status gateway-receiver --group=RG1
Member                 | Port  | Status
-----------------------| ------| -------
pc13(8151)<v2>:19450   | 27815 | Running
pc13(8175)<v4>:14139   | 27066 | Running
pc13(8164)<v3>:45150   | 29897 | Running
```

##### status gateway-sender {#status_gateway_sender}
Display the status of the specified gateway sender.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
status gateway-sender --id=value [--groups=value(,value)*]
[--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --id      | *Required.* ID of the Gateway Sender.                        |
| --groups  | Group(s) of Gateway Senders for which to display status. Comma separated list for multiple member groups. |
| ‑‑members | Name/ID of the Gateway Sender(s) for which to display status. |

Table 2. Status Gateway-Sender Parameters

**示例命令:**

```
status gateway-receiver receiver1-LN --groups=LN-Group1;
```

**示例输出:**

```
gfsh>status gateway-sender --id=ln_Serial
Member                 |  Type  | Runtime Policy | Status
-----------------------| -------| -------------- | -----------
 pc13(8175)<v4>:21449  | Serial | Secondary      | Not Running
 pc13(8151)<v2>:40761  | Serial | Secondary      | Not Running
 pc13(8164)<v3>:33476  | Serial | Secondary      | Not Running

Member                         | Error
------------------------------ | ------------------------------
 pc13(8130):2365               | GatewaySender is not available
 pc13(Manager:8124)<v1>:43821  | GatewaySender is not available

gfsh>status gateway-sender --id=ln_Serial --members=pc13(8151)<v2>:7411
Member               |  Type  | Runtime Policy | Status
-------------------  | ------ | -------------- | -----------
 pc13(8151)<v2>:7411 | Serial | Secondary      | Not Running

gfsh>status gateway-sender --id=ln_Serial --members=pc13(8151)<v2>:7411
Member               |  Type  | Runtime Policy | Status
------------------- -| ------ | -------------- | -------
 pc13(8151)<v2>:7411 | Serial | Primary        | Running

gfsh>status gateway-sender --id=ln_Serial --groups=Serial_Sender
==>
Member                 |  Type  | Runtime Policy | Status
---------------------- | -------| -------------- | -----------
 pc13(8151)<v2>:44396  | Serial | Secondary      | Not Running
 pc13(8164)<v3>:29475  | Serial | Secondary      | Not Running
Member                 | Error
---------------------- | ------------------------------
 pc13(8186)<v5>:45840  | GatewaySender is not available
```

##### status locator {#status_locator}
Displays the status of the specified locator.

The status will be one of the following:

- started
- online
- offline
- not responding

**可用性:** Online or offline. If you want to obtain the status of a locator while you are offline, use the `--dir` option.

**句法:**

```
status locator [--name=value] [--host=value] [--port=value] [--dir=value]
```



| Name   | Description                                                  | Default Value     |
| :----- | :----------------------------------------------------------- | :---------------- |
| ‑‑name | Name/ID of the locator for which to display status. You must be connected to the JMX Manager to use this option. Can be used to obtain status of remote locators. See [Using gfsh to Manage a Remote Cluster Over HTTP or HTTPS](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html). |                   |
| --host | Hostname or IP address on which the Locator is running.      |                   |
| --port | Port on which the locator is listening.                      | 10334             |
| --dir  | Directory in which the locator was started.                  | current directory |

Table 3. Status Locator Parameters

**示例命令:**

```
status locator
status locator --name=locator1
```

##### status server {#status_server}
Display the status of the specified Geode cache server.

**可用性:** Online or offline. If you want to obtain the status of a server while you are offline, use the `--dir` option.

**句法:**

```
status server [--name=value] [--dir=value]
```



| Name   | Description                                                  | Default Value     |
| :----- | :----------------------------------------------------------- | :---------------- |
| ‑‑name | Name or ID of the Cache Server for which to display status. You must be connected to the JMX Manager to use this option. Can be used to obtain status of remote servers. See [Using gfsh to Manage a Remote Cluster Over HTTP or HTTPS](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html). |                   |
| --dir  | Directory in which the Geode Cache Server was started.       | current directory |

Table 4. Status Server Parameters

**示例命令:**

```
status server
status server --name=server1
```



#### stop {#stop}
Stop gateway receivers, gateway senders, locators and servers.

- **stop gateway-receiver**

  Stop the gateway receiver on a member or members.

- **stop gateway-sender**

  Stop a gateway sender with a given id on a specified member or members of a specified member group.

- **stop locator**

  Stop a locator.

- **stop server**

  Stop a Geode cache server.

##### stop gateway-receiver {#stop_gateway_receiver}
Stop the gateway receiver on a member or members.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
stop gateway-receiver [--groups=value(,value)*] [--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --groups  | Group(s) of members on which to stop the Gateway Receiver. Use a comma-separated list for multiple member groups. |
| ‑‑members | Name/Id of the member(s) on which to stop the Gateway Receiver. |

Table 1. Stop Gateway-Receiver Parameters

**示例命令:**

```
stop gateway-receiver --members=receiver1-LN 
stop gateway-receiver --groups=LN-Group1
```

**示例输出:**

```
gfsh>stop gateway-receiver
      Member                | Result | Message
--------------------------- | -------| -----------------------------------------------------------------------
pc13(2266)<v6>:56852        | OK     | GatewayReceiver is stopped on member pc13(2266)<v6>:56852
pc13(Manager:2242)<v5>:57631| Error  | GatewayReceiver is not available on member pc13(Manager:2242)<v5>:57631
pc13(2275)<v7>:47480        | OK     | GatewayReceiver is stopped on member pc13(2275)<v7>:47480
pc13(2293)<v8>:55472        | OK     | GatewayReceiver is stopped on member pc13(2293)<v8>:55472

gfsh>stop gateway-receiver --members=pc13(2266)<v14>:36579
GatewayReceiver is stopped on member pc13(2266)<v14>:36579

gfsh>stop gateway-receiver --groups=RG1
         Member      | Result | Message
-------------------- | -------| ----------------------------------------------------------
pc13(2275)<v23>:27484| OK     | GatewayReceiver is stopped on member pc13(2275)<v23>:27484
pc13(2293)<v24>:55810| OK     | GatewayReceiver is stopped on member pc13(2293)<v24>:55810
pc13(2266)<v22>:4522 | OK     | GatewayReceiver is stopped on member pc13(2266)<v22>:4522
```

##### stop gateway-sender {#stop_gateway_sender}
Stop a gateway sender with a given id on a specified member or members of a specified member group.

**CAUTION:** Use caution with the `stop gateway-sender` command (or equivalent `GatewaySender.stop()` API) on parallel gateway senders. Instead of stopping an individual parallel gateway sender on a member, we recommend shutting down the entire member to ensure that proper failover of partition region events to other gateway sender members. Using this command on an individual parallel gateway sender can occur in event loss. See [Stopping Gateway Senders](https://geode.apache.org/docs/guide/17/topologies_and_comm/topology_concepts/multisite_overview.html#topic_9AA37B43642D4DE19072CA3367C849BA__section_aqm_2js_bq)for more details.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
stop gateway-sender --id=value [--groups=value(,value)*] [--members=value(,value)*]
```



| Name      | Description                                                  |
| :-------- | :----------------------------------------------------------- |
| --id      | *Required.* ID of the Gateway Sender.                        |
| --groups  | Group(s) of members on which to stop the Gateway Sender.     |
| --members | Name/Id of the member(s) on which to stop the Gateway Sender. |

Table 2. Stop Gateway-Sender Parameters

**示例命令:**

```
stop gateway-sender --id=ln --members=server1
```

**示例输出:**

```
gfsh>stop gateway-sender --id=ln
Member                       | Result | Message
---------------------------- | ------ | ------------------------------------------------------------------------
pc13(5184)<v7>:41914         | OK     | GatewaySender ln is stopped on member pc13(5184)<v7>:41914
pc13(5192)<v8>:25704         | OK     | GatewaySender ln is stopped on member pc13(5192)<v8>:25704
pc13(5174)<v6>:53996         | OK     | GatewaySender ln is stopped on member pc13(5174)<v6>:53996
pc13(Manager:5148)<v5>:64040 | Error  | GatewaySender ln is not available on member pc13(Manager:5148)<v5>:64040

gfsh>stop gateway-sender --id=ln --members=pc13(5174)<v14>:17819
GatewaySender ln is stopped on member pc13(5174)<v14>:17819

gfsh>stop gateway-sender --id=ln --groups=SenderGroup1

Member                | Result | Message
--------------------- | ------ | -----------------------------------------------------------
pc13(5174)<v18>:63332 | OK     | GatewaySender ln is stopped on member pc13(5174)<v18>:63332
pc13(5184)<v19>:20055 | OK     | GatewaySender ln is stopped on member pc13(5184)<v19>:20055
pc13(5192)<v20>:14622 | OK     | GatewaySender ln is stopped on member pc13(5192)<v20>:14622
```

##### stop locator {#stop_locator}
Stop a locator.

**注意:** One of the command line options `--name` or `--dir` must be specified to identify the locator to be stopped.

**可用性:** Online or offline. If you want to stop a locator while you are offline, use the `--dir`option.

**句法:**

```
stop locator --name=value | --dir=value
```



| Name   | Description                                                  | Default Value     |
| :----- | :----------------------------------------------------------- | :---------------- |
| ‑‑name | The Geode member name of the locator to stop. You must be connected to the JMX Manager to use this option. Can be used to stop remote locators. See [Using gfsh to Manage a Remote Cluster Over HTTP or HTTPS](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html). |                   |
| --dir  | Directory in which the locator was started.                  | current directory |

Table 3. Stop Locator Parameters

**示例命令:**

```
stop locator --name=locator3
```

**示例输出:**

```
gfsh>stop locator --name=locator3
Stopping Locator running in /Users/test/locator3 on 192.0.2.0[10334] as locator3...
Process ID: 71531
Log File: /Users/test/locator3/locator3.log
...
No longer connected to 192.0.2.0[1099].
gfsh>stop locator --dir=loc2
Stopping Locator running in /Users/test/loc2 on 192.0.2.0[10334] as loc2...
Process ID: 71714
Log File: /Users/test/loc2/loc2.log
...
No longer connected to 192.0.2.0[1099].
```

##### stop server {#stop_server}
Stop a Geode cache server.

**可用性:** Online or offline. If you want to stop a cache server while you are offline, use the `--dir` option.

**句法:**

```
stop server [--name=value] [--dir=value]
```



| Name   | Description                                                  | Default Value     |
| :----- | :----------------------------------------------------------- | :---------------- |
| ‑‑name | Name/Id of the Geode Cache Server to stop. You must be connected to the JMX Manager to use this option. Can be used to stop remote servers. See [Using gfsh to Manage a Remote Cluster Over HTTP or HTTPS](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_remote.html). |                   |
| --dir  | Directory in which the Geode Cache Server was started.       | current directory |

Table 4. Stop Server Parameters

**示例命令:**

```
stop server --name=server1
stop server --dir=server1
```



#### undeploy {#undeploy}
Undeploy the JAR files that were deployed on members or groups using `deploy` command.

If `--jars` is not specified, the command will undeploy all deployed JARs. If `--groups` is not specified, the command applies to the entire cluster. Note that this command can’t unload the classes that were loaded during deployment. Member(s) should be restarted for that.

**可用性:** Online. You must be connected in `gfsh` to a JMX Manager member to use this command.

**句法:**

```
undeploy [--jars=value(,value)*] [--groups=value(,value)*]
```



| Name     | Description                                                  | Default Value                      |
| :------- | :----------------------------------------------------------- | :--------------------------------- |
| --groups | Group(s) from which the specified JAR(s) will be undeployed. | undeploy will occur on all members |
| --jars   | JAR or JARs to be undeployed.                                | All JARs will be undeployed        |

Table 1. Undeploy Parameters

**示例命令:**

```
undeploy --jars=domain-objects.jar
undeploy --groups=Group1
```

**示例输出:**

```
gfsh>undeploy --jars=domain-objects.jar

  Member   |  Un-Deployed JAR   |         Un-Deployed From JAR Location        
---------- | ------------------ | ---------------------------------------------
datanode10 | domain-objects.jar | /usr/local/gemfire/deploy/GF#domain-objects#1
datanode11 | domain-objects.jar | /usr/local/gemfire/deploy/GF#domain-objects#1 


gfsh> undeploy --groups=Group1

 Member   |     Un-Deployed JAR     |             Un-Deployed From JAR Location        
--------- | ----------------------- | ------------------------------------------------------
datanode1 | group1_functions.jar    | /usr/local/gemfire/deploy/GF#group1_functions.jar#1
datanode1 | group1_dependencies.jar | /usr/local/gemfire/deploy/GF#group1_dependencies.jar#1 
datanode2 | group1_functions.jar    | /usr/local/gemfire/deploy/GF#group1_functions.jar#1
datanode2 | group1_dependencies.jar | /usr/local/gemfire/deploy/GF#group1_dependencies.jar#1
```

**错误消息:**

```
No JAR Files Found
```



#### validate offline-disk-store {#validate_offline_disk_store}




Validate offline disk stores.

**可用性:** Offline.

**句法:**

```
validate offline-disk-store --name=value --disk-dirs=value(,value)*
```



| Name        | Description                                                  |
| :---------- | :----------------------------------------------------------- |
| --name      | *Required.* Name of the disk store to be validated.          |
| --disk-dirs | *Required.* Directories where data for the disk store was previously written. |

Table 1. Validate Offline-Disk-Store Parameters

**示例命令:**

```
validate offline-disk-store --name=DiskStore2 --disk-dirs=data/dir3,data/dir4
```



#### version {#version}
Display product version information.

**可用性:** Online or offline.

**句法:**

```
version [--full]
```



| Name   | Description                        | Defalut Value |
| :----- | :--------------------------------- | :------------ |
| --full | Show the full version information. | false         |

Table 1. Version Parameters

**示例命令:**

```
version
version --full
```

**示例输出:**

```
gfsh>version
v8.0.0


gfsh>version --full
Java version:   8.0.0 build 48319 07/31/2014 17:26:09 PDT javac 1.8.0_1
Native version: native code unavailable
Source revision: 48319
Source repository: gemfire/branches/cedar_dev_Oct12
Running on: /192.0.2.0, 1 cpu(s), amd64 Linux 2.6.32-38-generic
```



#### Creating and Running gfsh Command Scripts {#Creating_and_Running_gfsh_Command_Scripts}
gfsh offers several ways to run commands in a scripting environment.

**Running gfsh Scripts**

You can create and run scripts that contain gfsh commands that you wish to execute. To execute the script, use the gfsh [run](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/run.html) command. For example:

```
gfsh run --file=mycommands.gfsh
```

**注意:** When you run a gfsh script, interactive parameters are ignored. You can also set the script to run in quiet mode to prevent output and instruct the script to skip any errors it encounters.

Your command history file can be helpful when you write a gfsh script. A history of commands that have been executed successfully is logged in the `.gfsh.history` file in the home directory of the user running gfsh. You can also export a history file with the `history --file=your_file_name`command.

When a user runs `start server` or `start locator` from gfsh without specifying the member name, gfsh will automatically pick a random member name. This is useful for automation.



#### Running gfsh Commands on the OS Command Line {#Running_gfsh_Commands_on_the_OS_Command_Line}
You can run some `gfsh` commands directly from your operating system’s prompt by preceding the command with `gfsh`. This can be useful for Unix shell or Windows batch scripting. For example:

```
$ gfsh start locator --name=locator2 --port=10335
```

To see which gfsh commands are available directly on the prompt:

```
$ gfsh help
```

**Running Multiple gfsh Commands on the OS Command Line**

To run multiple commands directly on the command line, use the `-e` option followed by the `gfsh` command within quote marks. For example:

```
prompt>gfsh -e "start locator --name=locator1" -e "start server --name=server1"

prompt>gfsh -e "start jconsole"

prompt>gfsh -e "connect --locator=remotehost[10334]" -e "rebalance" -e "gc"
```



#### Mapping cache.xml Elements to gfsh Configuration Commands {#Mapping_cache_xml_Elements_to_gfsh_Configuration_Commands}
You can configure a Geode cluster using either cache.xml files, or you can use gfsh and the cluster configuration service to configure a cluster. This table maps `cache.xml` elements to the gfsh commands that configure and manage a cluster.



| cache.xml Element       | gfsh Command                                                 |
| :---------------------- | :----------------------------------------------------------- |
| <cache>, <cache-server> | [start server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/start.html#topic_3764EE2DB18B4AE4A625E0354471738A)[status server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/status.html#topic_E5DB49044978404D9D6B1971BF5D400D)[stop server](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/stop.html#topic_723EE395A63A40D6819618AFC2902115)[alter runtime](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_7E6B7E1B972D4F418CB45354D1089C2B) |
| <async-event-queue>     | [create async-event-queue](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_ryz_pb1_dk)[list async-event-queues](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_j22_kzk_2l) |
| <pdx>                   | [configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh) |
| <region>                | [create region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_54B0985FEC5241CA9D26B0CE0A5EA863)[alter region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_E74ED23CB60342538B2175C326E7D758)[destroy region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_BEDACECF4599407794ACBC0E56B30F65)[describe region](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_DECF7D3D33F54071B6B8AD4EA7E3F90B)[list regions](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_F0ECEFF26086474498598035DD83C588)[rebalance](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/rebalance.html) |
| <index>                 | [create index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_960A5B6FD3D84E1881EE118E299DD12D)[destroy index](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_D00219CCD6F64C1582A0802AC5CDF3F3)[list indexes](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_B3B51B6DEA484EE086C4F657EC9831F2) |
| <disk-store>            | [create disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_bkn_zty_ck)[alter disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/alter.html#topic_99BCAD98BDB5470189662D2F308B68EB)[backup disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/backup.html#topic_E74ED23CB60342538B2175C326E7D758)[compact disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/compact.html#topic_F113C95C076F424E9AA8AC4F1F6324CC)[compact offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/compact.html#topic_9CCFCB2FA2154E16BD775439C8ABC8FB)[describe disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_C635B500BE6A4F1D9572D0BC98A224F2)[describe offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/describe.html#topic_kys_yvk_2l)[destroy disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/destroy.html#topic_yfr_l2z_ck)[list disk-stores](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/list.html#topic_BC14AD57EA304FB3845766898D01BD04)[revoke missing-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/revoke.html)[show missing-disk-stores](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/show.html#topic_7B3D624D5B4F41D1A0F8A9C3C8B2E780)[validate offline-disk-store](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/validate.html) |

Table 1. Migrating cache.xml elements to gfsh commands





[#What_You_Can_Do_with_gfsh]: 