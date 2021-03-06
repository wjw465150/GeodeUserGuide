# 缓存数据的内存要求

Geode解决方案架构师需要估算资源需求，以满足应用程序性能，可伸缩性和可用性目标。

这些需求包括以下资源的估计：

- 内存
- 机器数量
- 网络带宽

这里的信息仅是指南，并且假定了对Geode的基本了解。 尽管没有两个应用程序或用例完全相同，但是根据实际经验，此处的信息应该是一个坚实的起点。 与物理数据库设计非常相似，最终正确的部署配置和物理拓扑是基于操作环境的性能要求，应用程序数据访问特征以及资源限制（即内存，CPU和网络带宽）。



# Geode数据区域设计的核心准则

以下准则适用于区域设计：

- 对于32位JVM：如果您的数据集较小（小于 2GB ）且读取量很大，则应使用复制的区域。
- 对于64位JVM：如果您的数据集大于JVM堆空间的50-60％，则可以使用复制的区域。 对于读取繁重的应用程序，这可能是性能上的胜利。 对于写繁重的应用程序，应使用分区缓存。
- 如果数据量很大，并且担心可伸缩性，则应使用分区区域。
- 如果您有一个大的数据集，并且能够容忍磁盘上的数据子集，那么您应该使用复制区域或磁盘溢出的分区区域。
- 如果您有满足上述条件的不同数据集，那么您可能需要考虑混合复制和分区区域的混合解决方案。根据应用程序的写密集型程度，不要超过JVM堆大小的50 - 75%。

## 内存使用概述

以下准则应提供系统消耗的内存量的粗略估计。

关于键和条目（对象）的内存计算以及它们的区域开销可以除以仅用于放置在分区区域中的数据的集群成员数。 对于其他区域，该计算是针对托管该区域的每个成员的。 套接字，线程使用的内存以及Geode的少量应用程序开销是每个成员的。

对于添加到区域中的每个条目，Geode缓存API都会消耗一定数量的内存来存储和管理数据。 即使条目溢出或持久存储到磁盘，也需要此开销。 因此，即使将对象分页到磁盘，磁盘上的对象也会占用一些JVM内存。 区域使用32位JVM引入的Java缓存开销可以大致估算如下。

实际的内存使用量取决于多种因素，包括正在使用的JVM和正在运行的平台。 对于64位JVM，其使用情况通常会大于32位JVM。 由于对象引用和标头使用更多的内存，因此64位JVM可能需要多达80％的内存。

在计算内存需求时，还有其他一些注意事项：

- **您存储的数据的大小。** 要估计存储数据的大小，请首先确定是将数据存储为序列化形式还是非序列化形式。一般来说，非序列化形式是两者中较大的一个。参见[确定对象序列化开销](https://geode.apache.org/docs/guide/17/reference/topics/memory_requirements_for_cache_data.html#topic_psn_5tz_j4)

  Geode中的对象被序列化以存储到分区区域中，并进行所有分发活动，包括将数据移动到磁盘以实现溢出和持久性。 为了获得最佳性能，Geode尝试减少对象被序列化和反序列化的次数，因此您的对象可以序列化或非序列化的形式存储在缓存中。

- **数据的应用程序对象开销。** 在计算应用程序开销时，请确保对键和值进行计数，如果键和/或值是复合对象，则对每个对象进行计数。

  下一节“计算应用程序对象开销”提供有关如何估计缓存中存储的键和值的内存开销的详细信息。

## 计算应用程序对象开销

要计算Java对象的内存开销，请执行以下步骤：

1. **确定对象头的大小。** 每个Java对象都有一个对象头。对于32位JVM，它是8字节。对于堆小于或等于32GB的64位JVM，它是12字节。对于堆大于32GB的64位JVM，它是16字节。

2. **确定对象字段的内存开销。** 对于每个实例字段（包括超类的字段），添加字段的大小。 对于基本字段，大小为：

   - **8** for long 和 double
   - **4** for int 和 float
   - **2** for char 和 short
   - **1** for byte 和 boolean

   对于对象引用字段，堆大于32GB的64位JVM的大小为8字节。对于所有其他jvm，使用4个字节。

3. **把步骤1和步骤2的数字加起来，四舍五入到下一个8的倍数。** 结果是该Java对象的内存开销。

**Java 数组.** 要计算Java数组的内存开销，您将添加对象标头（因为数组是一个对象）和一个包含其大小的原始int字段。 将数组的每个元素都视为实例字段。 例如，大小为100字节的字节数组将具有一个对象标头，一个int字段和100个字节的字段。 使用上述的三步过程进行计算。

**序列化对象.** 在计算序列化值的内存开销时，请记住，序列化形式存储在字节数组中。因此，要计算序列化表单包含多少字节，请计算该大小的Java字节数组的内存开销，然后添加序列化值包装器的大小。

当一个值最初以序列化形式存储在高速缓存中时，会围绕该值引入一个包装器，即使该值后来被反序列化，该包装器也将在该值的生命期内保留在内存中。 尽管此包装器仅在内部使用，但确实会增加内存占用量。 包装器是一个具有一个int字段和一个对象引用的对象。

如果使用分区，则每个值最初都以序列化形式存储。 对于其他区域类型，最初仅将来自远程成员（对等或客户端）的值以序列化形式存储。 （这是最常见的情况。） 但是，如果本地操作将值存储在本地JVM的缓存中，那么该值将以对象形式存储。大量的操作会导致以序列化形式存储的值被反序列化。任何需要将值的对象形式设为local的操作都可能导致这种反序列化。如果执行了这些操作，那么该值将以对象形式(带有附加的序列化包装器)存储，而序列化的形式将成为垃圾。

**注意:** 一个例外是，如果序列化的表单是用PDX编码的，那么将`read-serialized`设置为true将使序列化的表单保留在缓存中。

请参阅[确定对象序列化开销](https://geode.apache.org/docs/guide/17/reference/topics/memory_requirements_for_cache_data.html#topic_psn_5tz_j4)，以获取有关如何计算存储序列化对象的内存使用要求的更多信息。

## 使用键存储优化

键以对象形式存储，但优化了键存储的某些类除外。键存储是通过将条目的对象引用替换为条目上“inline(内联)”存储密钥数据的一个或两个基本字段来优化的。以下规则适用于确定键是否以“内联”方式存储:

- 如果键的类是`java.lang.Integer`，`java.lang.Long`或`java.util.UUID`，则该键始终以内联方式存储。 内联Integer或Long键的内存开销为0（零）。 内联UUID的内存开销为8。

- 如果键的类为`java.lang.String`，则如果字符串的长度足够小，则将内联键。

  - 对于长度小于8的ASCII字符串，嵌入式内存开销为0（零）。
  - 对于长度小于16的ASCII字符串，内联内存开销为8。
  - 对于长度小于4的非ASCII字符串，内联内存开销为0（零）。
  - 对于长度小于8的非ASCII字符串，内联内存开销为8。

  所有其他字符串都没有内联。

**何时禁用嵌入式键存储.** 在某些情况下，内联存储键可能会引入额外的内存或CPU使用。如果所有的键都引用自其他对象，那么最好不要内联键。如果您经常从该区域请求键，那么您可能希望将对象表单存储在缓存中，这样就不需要不断地重新创建对象表单。注意，检查键是否在区域中的基本操作不需要对象表单，而是使用内联原始数据。

可以在成员启动时通过指定以下Geode属性来禁用键内联功能：

```properties
-Dgemfire.DISABLE_INLINE_REGION_KEYS=true
```

## 测量缓存开销

该表提供了对32位JVM中的缓存开销的估计。 即使条目溢出或持久存储到磁盘，也需要开销。 实际的内存使用量取决于多种因素，包括JVM类型和运行平台。 对于64位JVM，其使用率通常会大于32位JVM，并且使用率可能会高出80％。

| 计算缓存开销时…                             | 你应该 …                                                 |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 对于每一个区域<br/><br/>**注意:** 对于64位JVM，不同的JVM实现和不同的JDK版本，对象头和对象引用的内存消耗可能会有所不同。 | 每个条目增加64个字节                            |
| 并且并发检查已禁用（默认情况下启用）| 每个条目减去16个字节（请参阅[一致性检查的开销](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_0BDACA590B2C4974AC9C450397FE70B2).) |
| 而且                    | 每个条目增加16个字节                                       |
| 而且该区域是持久化的                                  | 每个条目增加52个字节                                       |
| 而且该区域仅是溢出的                              | 每个条目增加44个字节                                       |
| 而且该地区有一个LRU收回控制器                | 每个条目增加16个字节                                       |
| 该地区具有全局范围                              | 每个条目增加110个字节                                      |
| 而且该区域已配置条目过期               | 每个条目增加112个字节                                      |
| 对于每个可选的用户属性                             | 每个条目增加40个字节，再加上用户属性对象的内存开销 |

对于查询中使用的索引，开销会因要存储的数据类型和创建的索引类型而有很大不同。 您可以大致估算出某些类型的索引的开销，如下所示：

- 如果索引的每个区域条目的索引表达式只有一个值，则索引每个区域条目最多引入243个字节。此类索引的一个示例是：`fromClause="/portfolios", indexedExpression="id"`。 如果每个区域条目的索引表达式具有唯一值，则每个区域条目的最大值为243个字节。 如果条目没有唯一索引值，则可以减少开销。
- 如果每个区域条目的索引表达式具有多个值，但没有两个区域条目具有相同的值，则索引将为每个区域条目引入最多236 C + 75字节，其中C是每个地区的平均数量的值条目的表达式。
- Lucene索引为每个条目添加大约737字节。 此处列出的其他索引开销估算值也适用于Lucene索引。

## 估计管理和监控开销

Geode JMX管理和监控系统会增加内存开销，在确定部署的内存要求时应考虑到这一点。 具体来说，以JMX管理器运行的任何进程（例如定位器）的内存占用量都可能增加。

对于由JMX Manager管理和监控的群集中的每个资源（例如，每个MXBean，例如MemberMXBean，RegionMXBean，DiskStoreMXBean，LockServiceMXBean等），都应向JMX Manager节点添加10 KB的必需内存。

## 确定对象序列化开销

与Java Serializable相比，Geode PDX序列化除了可以提供更好的性能外，还可以节省大量空间。 在某些情况下，我们可以节省多达65％，但是节省的多少将取决于域对象。 PDX序列化最有可能在所有可用选项中节省最多的空间。 DataSerializable更为紧凑，但是它要求在访问时对对象进行反序列化，因此应将其考虑在内。 另一方面，PDX可序列化对于大多数操作都不需要反序列化，因此，它可以节省更多空间。

在任何情况下，应该在数据序列化的上下文中考虑在服务器端完成的操作的种类和数量，因为Geode必须为某些类型的操作(访问)反序列化数据。例如，如果一个函数在服务器端调用get操作，那么在大多数情况下，get操作返回的值将被反序列化(只有在使用PDX序列化并设置了read-serialized属性时，它才不会被反序列化)。查明实际开销的惟一方法是运行测试，并检查内存使用情况。

一些其他的序列化指南和技巧：

- 如果使用复合对象，请勿将标准Java序列化与Geode序列化（DataSerializable或PDX）混合使用。 当与Geode序列化混合使用时，标准Java序列化可以正常运行，但是最终会产生更多的序列化字节。

  要确定您是否正在使用标准Java序列化，请在进程执行时指定`-DDataSerializer.DUMP_SERIALIZED=true`。 然后检查您的日志中是否存在以下格式的消息：

  ```verilog
  DataSerializer Serializing an instance of <className>  
  ```

  任何类列表都将使用标准Java序列化进行序列化。 您可以通过在`PdxSerializer`或`DataSerializer`中处理这些类或将类更改为`PdxSerializable`或`DataSerializable`来优化序列化。

- 确定对象序列化大小的一种简单方法是创建该对象的实例，然后调用 `DataSerializer.writeObject(obj dataOutput)` ，其中“dataOutput”包装`ByteArrayOutputStream`。 然后，您可以询问流的大小，它将返回序列化的大小。 在调用`writeObject`之前，请确保已配置了`PdxSerializer`和/或`DataSerializer`。

如果您确实想估计PDX序列化数据的内存使用情况，下表提供了使用PDX序列化时各种类型的估计大小：

| 类型          | 内存使用情况                                                 |
| :------------ | :----------------------------------------------------------- |
| boolean       | 1 字节                                                       |
| byte          | 1 字节                                                       |
| char          | 2 字节                                                       |
| short         | 2 字节                                                       |
| int           | 4 字节                                                       |
| long          | 8 字节                                                       |
| float         | 8 字节                                                       |
| String        | String.length + 3 字节                                       |
| Domain Object | 9个字节（用于PDX头）+ 对象序列化长度（所有成员字段的总数）+ 1至4个额外的字节（取决于Domain对象的总大小） |

注意事项–如果域对象包含许多域对象作为成员字段，则PDX序列化的内存开销可能比其他类型的序列化要大得多。

## 计算套接字内存需求

服务器始终保持与每个对等方的两个传出连接。 因此，对于服务器具有的每个对等点，总共有四个连接：两个连接到对等点，两个从对等点进入。

服务客户端请求的服务器线程还与对等方通信以分发事件和转发客户端请求。如果服务器的Geode连接属性*conserve-sockets*设置为true(默认值)，这些线程将使用已经建立的对等连接进行通信。

如果*conserve-sockets*为false，那么每个服务客户端的线程都建立了两个到它的服务器端独立的连接，一个发送，一个接收。每个套接字使用一个文件描述符，因此可用套接字的数量由两个操作系统设置控制:

- 整个系统上允许打开的最大文件数
- 每个会话允许的最大打开文件数

在有许多线程为客户服务的服务器中，如果*conserve-sockets*设置为false，对连接的需求很容易超出可用套接字的数量。即使将*conserve-sockets*设置为false，您也可以通过设置服务器的*max-threads*参数来限制这些连接的数量。

由于每个客户端连接都在线程上使用一个服务器套接字来处理连接，并且由于该服务器充当分区区域上的代理以获取结果，或者代表客户端执行针对分区区域的功能服务, 如果conserve-sockets设置为false，这还会导致服务器上的新套接字向每个对等方打开。 因此，打开了N个套接字，其中N是对等体的数量。 大量客户端同时连接到具有分区区域且conserve-sockets设置为false的分区的大量对等端，可能会导致套接字占用大量内存。 在这些情况下，将conserv-sockets设置为true。

**注意:** 对于每个正在处理的客户端连接，线程堆栈也有JVM开销，对于大多数JVM而言，该开销设置为256KB或512KB。 在某些JVM上，您可以将其减少到128KB。 您可以使用Geode的`max-threads`属性或Geode的`max-connections`属性限制客户端线程的数量，从而限制线程开销和套接字开销。

下表列出了基于连接的内存要求。

| 连接数                                                       | 内存需求                                                     |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 每个 socket                                                  | 32,768 / socket（可配置）每个套接字的默认值应设置为> 100 + sizeof（区域中最大的对象）+ sizeof（最大键）的数字 |
| 如果是服务器（例如，如果有客户端连接到该服务器）             | = (服务器上的max-threads属性或 max-connections属性) * (套接字缓冲区大小+ JVM的线程开销 ) |
| 如果conserve sockets设置为true，则集群的每个成员             | 4* peers数量                                                 |
| 每个成员，如果 conserve sockets设置为false                   | 4 * 托管该区域的对等点数量 * 线程数量                        |
| 如果成员托管分区区域，则将conserve sockets设置为false且它是服务器（这是上面的累积值） | = < 最大线程数 * 2 * 对等体数<br/><br/>**注意:** <span style="color:red">= 2 * 当前连接的客户端数 * 对等体数。 每个连接都产生一个线程。</span> |
| **订阅队列**                                                 |                                                              |
| 每个服务器，取决于是否限制队列大小。 如果这样做，您可以指定兆字节数或条目数，直到队列溢出到磁盘为止。 如果可能，队列上的条目是引用，以最大程度地减少内存影响。 队列不仅占用键和条目的内存，还消耗客户端ID（或线程ID）以及操作类型的内存。 由于您可以将队列限制为1 MB，因此该数字是完全可配置的，因此没有简单的公式。 | 1 MB +                                                       |
| **Geode类和JVM开销**                                         | 大致 50MB                                                    |
| **Thread overhead**                                          |                                                              |
| 与服务器的每个并发客户端连接都会导致产生一个线程，使其达到max-threads设置。 之后，一个线程为多个客户端提供服务，直至max-clients设置。 | 每个连接都有线程堆栈开销（至少256KB至512KB，您可以在许多JVM上将其设置为更小至128KB。） |

