# 使用Apache Geode进行开发

*使用Apache Geode进行开发*解释了使用Apache Geode进行应用程序编程的主要概念。 它描述了如何规划和实现区域，数据序列化，事件处理，增量传播，事务等。

有关Geode REST应用程序开发的信息，请参阅[为Apache Geode开发REST应用程序](https://geode.apache.org/docs/guide/17/rest_apps/book_intro.html).

- **区域数据存储和分发**

  Apache Geode数据存储和分发模型可以在适当的时间将您的数据放在正确的位置。 在开始配置数据区域之前，您应该了解Geode中数据存储的所有选项。

- **分区区域**

  除基本区域管理外，分区区域还包括高可用性选项，数据位置控制以及跨集群的数据平衡。

- **分布式和复制区域**

  除基本区域管理外，分布式和复制区域还包括推送和分发模型，全局锁定和区域条目版本等选项，以确保Geode成员之间的一致性。

- **区域更新的一致性**

  Geode确保区域的所有副本最终在托管该区域的所有成员和客户端上达到一致状态，包括分发区域事件的Geode成员。

- **一般地区数据管理**

  对于所有区域，您可以选择控制内存使用，将数据备份到磁盘，并将过时数据保留在缓存之外。

- **数据序列化**

  您在Geode中管理的数据必须序列化和反序列化，以便在进程之间进行存储和传输。 您可以选择多个数据序列化选项。

- **事件和事件处理**

  Geode为缓存的数据和系统成员事件提供了通用且可靠的事件分发和处理。

- **增量传播**

  增量传播允许您通过仅包括对象而不是整个对象的更改来减少通过网络发送的数据量。

- **查询**

  Geode提供了一种类似SQL的查询语言OQL，允许您访问存储在Geode区域中的数据。

- **连续查询**

  连续查询会不断返回与您设置的查询匹配的事件。

- **事务**

  Geode提供了一个事务API，使用`begin`，`commit`和`rollback`方法。 这些方法与熟悉的关系数据库事务方法非常相似。

- **函数执行**

  函数是驻留在服务器上的代码体，应用程序可以从客户端或其他服务器调用，而无需自己发送函数代码。 调用者可以指示数据相关函数对特定数据集进行操作，或者可以指示与数据无关的函数在特定服务器，成员或成员组上操作。


## 区域数据存储和分发

Apache Geode数据存储和分发模型可以在适当的时间将您的数据放在正确的位置。 在配置数据区域之前，您应该了解Geode中数据存储的所有选项。

- **存储和分配选项**

  Geode提供了多种数据存储和分发模型，包括分区或复制区域以及分布式或非分布式区域（本地缓存存储）。

- **区域类型**

  区域类型定义单个集群中的区域行为。 您可以使用各种区域数据存储和分发选项。

- **区域数据存储和数据访问器**

  了解存储区域数据的成员与仅作为区域数据访问者的成员之间的区别。

- **动态创建区域**

  您可以在应用程序代码中动态创建区域，并自动在集群成员上实例化它们。


### 存储和分配选项

Geode提供了多种数据存储和分发模型，包括分区或复制区域以及分布式或非分布式区域（本地缓存存储）。

#### 点对点区域存储和分发

最常见的是，数据管理意味着在应用程序需要的时间和地点提供当前数据。在正确配置的Geode安装中，您将数据存储在本地成员中，Geode会根据您的缓存配置设置自动将其分发给需要它的其他成员。您可能正在存储需要特殊考虑的非常大的数据对象，或者您可能需要仔细配置大量数据以保护应用程序的性能或内存使用。您可能需要能够在特定操作期间显式锁定某些数据。大多数数据管理功能都可以作为配置选项使用，您可以使用`gfsh`集群配置服务，`cache.xml`文件或API指定。配置完成后，Geode会自动管理数据。例如，这是您管理数据分发，磁盘存储，数据过期活动和数据分区的方式。通过API在运行时管理一些功能。

在体系结构级别，数据分发在单个群集中的对等体之间以及客户端和服务器之间运行。

- 点对点提供核心分发和存储模型，这些模型被指定为数据区域上的属性。
- 对于客户端/服务器，您可以选择在客户端和服务器层之间共享哪些数据区域。 然后，在每个区域内，您可以通过订阅子集来微调服务器自动发送到客户端的数据。

任何类型的安装中的数据存储都基于每个群集的点对点配置。 数据和事件分发基于点对点和系统到系统配置的组合。

存储和分发模型通过缓存和区域属性进行配置。 主要选择是分区，复制或仅分布式。 必须对所有服务器区域进行分区或复制。 每个区域的`data-policy`和`subscription-attributes`，如果它不是分区区域，它的`scope`进行交互以更好地控制数据分发。

在本地缓存中存储数据

要将数据存储在本地缓存中，请使用带有本地状态的`RegionShortcut`或`ClientRegionShortcut`的区域`refid`。 这些会自动将区域`data-policy`设置为非空策略。 没有存储的区域可以发送和接收事件分发，而无需在应用程序堆中存储任何内容。 使用其他设置，接收的所有输入操作都存储在本地。


### 区域类型

区域类型定义单个集群中的区域行为。 您可以使用各种区域数据存储和分发选项。

在Geode集群中，您可以定义分布式区域和非分布式区域，并且可以定义其数据分布在集群中的区域，以及其数据完全包含在单个成员中的区域。

您选择的区域类型部分取决于您运行的应用程序类型。 特别是，您需要为服务器和客户端使用特定的区域类型，以便在两个层之间进行有效通信：

- 服务器区域由服务器在`缓存`内创建，并由从服务器集群外部连接到服务器的客户端访问。 服务器区域必须具有分区或复制的区域类型。 服务器区域配置使用`RegionShortcut`枚举设置。
- 客户端区域由客户端在`ClientCache`内创建，并配置为在客户端和服务器层之间分发数据和事件。 客户区域必须具有区域类型`local`。 客户端区域配置使用`ClientRegionShortcut`枚举设置。
- 对等区域在`Cache`内创建。 对等区域可以是服务器区域，或者它们可以是客户端不访问的区域。 对等区域可以具有任何区域类型。 对等区域配置使用`RegionShortcut`枚举设置。

使用`gfsh`或`cache.xml`文件配置服务器或对等区域时，可以使用*region shortcuts*来定义您所在区域的基本配置。 区域快捷方式提供了一组默认配置属性，这些属性是为各种类型的缓存体系结构设计的。 然后，您可以根据需要添加其他配置属性以自定义应用程序。 有关这些区域快捷方式的更多信息和完整参考，请参阅[区域快捷方式参考](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_lt4_54c_lk).


这些是每个数据区域的主要配置选择。

| 区域类型                   | 描述                                                         | 最适合...                                                    |
| -------------------------- | ------------------------------------------------------------ | :----------------------------------------------------------- |
| Partitioned                | 系统范围的数据集设置。 数据被划分为跨定义区域的成员的桶。 为了实现高可用性，请配置冗余副本，以便每个存储桶存储在多个成员中，其中一个成员持有主数据. | 服务器区域和对等区域: 1.非常大的数据集; 2.高可用性; 3.写性能; 4.分区事件监听器和数据加载器 |
| Replicated (distributed)   | 保存分布式区域中的所有数据。 来自分布区域的数据被复制到成员副本区域中。 可以与非复制混合，一些成员持有副本，一些成员持有非副本。 | 服务器区域和对等区域: 1.阅读繁重的小型数据集; 2.异步分配; 3.查询性能 |
| Distributed non-replicated | 数据分布在定义区域的成员之间。 每个成员只保留其表示感兴趣的数据。可以与复制混合，一些成员持有副本，一些成员持有非副本。 | 对等区域，但不是服务器区域和不是客户区域: 异步分配;查询性能  |
| Non-distributed (local)    | 该区域仅对定义成员可见。                                     | 客户区域和对等区域: 1.应用程序之间未共享的数据               |



**分区区域**

对于非常大的服务器区域，分区是一个不错的选择。 分区区域非常适用于数百GB甚至更多的数据集。

**注意:** 分区区域通常需要比其他区域类型更多的JDBC连接，因为承载数据的每个成员必须具有连接。

分区区域将数据分组到存储桶中，每个存储桶都存储在所有系统成员的子集中。 存储桶中的数据位置不会影响逻辑视图 - 所有成员都会看到相同的逻辑数据集。

使用分区：

- **大数据集**. 存储太大而无法放入单个成员的数据集，并且所有成员都将看到相同的逻辑数据集。 分区区域将数据划分为称为存储区的存储单元，这些存储区分为托管分区区域数据的成员，因此没有成员需要托管所有区域的数据。 Geode提供动态冗余恢复和分区区域的重新平衡，使其成为大规模数据容器的选择。 系统中的更多成员可以在所有主机成员之间实现更均匀的数据平衡，从而允许在添加新成员时扩展系统吞吐量（获取和放置）。
- **高可用性**. 分区区域允许您配置Geode应该创建的数据副本数。 如果成员失败，您的数据将在不中断其他成员的情况下可用。 分区区域也可以持久保存到磁盘以获得额外的高可用性。
- **可扩展性**. 分区区域可以扩展为大量数据，因为数据在可用于托管区域的成员之间划分。 只需添加新成员即可动态增加数据容量。 分区区域还允许您扩展处理能力。 由于您的条目分布在托管区域的成员中，因此对这些条目的读取和写入也会分散在这些成员中。
- **良好的写性能**. 您可以配置数据的副本数量。 每次写入传输的数据量不会随着成员数量的增加而增加。 相反，对于复制区域，每个写入必须发送到具有复制区域的每个成员，因此每次写入传输的数据量随着成员数量的增加而增加。

在分区区域中，您可以在存储桶内和多个分区区域内共存keys。 您还可以控制哪些成员存储哪些数据存储桶。

#### 复制区域

复制区域在吞吐量和延迟方面提供最高性能。 对于中小型服务器区域，复制是一个不错的选择。

使用复制区域：

- **集群的所有成员都需要少量数据**. For example, currency rate information and mortgage rates.
- **可以完全包含在单个成员中的数据集**. 每个复制区域都包含该区域的完整数据集
- **高性能数据访问**. 复制保证了堆对应用程序线程的本地访问，从而为数据访问提供尽可能低的延迟。
- **异步分发**. 所有分布式区域（复制和非复制）都提供最快的分发速度。

#### 分布式，非复制区域

分布式区域提供与复制区域相同的性能，但每个成员仅通过订阅来自其他成员的事件或通过在其缓存中定义数据条目来仅存储其表达兴趣的数据。

使用分布式，非复制区域：

- **对等区域，但不是服务器区域或客户区域**. 服务器的区域必须是复制或分区。 客户区域必须是本地的。
- **数据集，其中各个成员仅需要通知和更新数据子集的更改**. 在非复制区域中，每个成员仅接收其在本地缓存中定义的数据条目的更新事件。
- **异步分发**. 所有分布式区域（复制和非复制）都提供最快的分发速度。

#### 本地区域

**注意:** 当使用`ClientRegionShortcut`设置创建时，客户端区域自动定义为本地，因为所有客户端分发活动都来自服务器层。

本地区域没有对等分发活动。

使用本地地区:

- **客户区域**. 分发仅在客户端和服务器层之间进行。
- **定义成员的私有数据集**. 对等成员看不到本地区域。


### 区域数据存储和数据访问器

了解存储区域数据的成员与仅作为区域数据访问者的成员之间的区别。

在大多数情况下，在成员缓存中定义数据区域时，还要指定该成员是否为数据存储。 存储区域数据的成员称为数据存储或数据主机。 不存储数据的成员称为访问者成员或空成员。 定义区域的任何成员，存储或访问者都可以访问它，将数据放入其中，并从其他成员接收事件。 要配置区域以使成员是数据访问者，请使用不为该区域指定本地数据存储的配置。 否则，该成员是该区域的数据存储。

对于服务器区域，通过在名称中指定包含术语`PROXY`的区域快捷方式来抑制区域创建时的本地数据存储，例如`PARTITION_PROXY`或`REPLICATE_PROXY`。

对于客户端区域，通过指定`PROXY`区域快捷方式来抑制区域创建时的本地数据存储。 不要使用`CACHING_PROXY`快捷方式，因为它允许本地数据存储。


### 动态创建区域

您可以在应用程序代码中动态创建区域，并自动在集群成员上实例化它们。

如果您的应用程序不需要分区区域，则可以使用org.apache.geode.cache.DynamicRegionFactory类动态创建区域，也可以使用cache.xml中的`<dynamic-region-factory>`元素创建它们。 定义区域的文件。 见[](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#dynamic-region-factory).

由于涉及的选项数量众多，大多数开发人员使用函数在其应用程序中动态创建区域，如本主题中所述。 也可以从`gfsh`命令行创建动态区域。

有关使用Geode函数的完整讨论，请参阅[Function Execution](https://geode.apache.org/docs/guide/17/developing/function_exec/chapter_overview.html)。 函数使用org.apache.geode.cache.execute.FunctionService类。

例如，以下Java类定义并使用函数来创建动态区域：

CreateRegionFunction类定义客户端使用FunctionService类的onServer()方法在服务器上调用的函数。 此函数调用通过将条目放入区域属性元数据区域来启动区域创建。 条目key是区域名称，value是用于创建区域的区域属性集。

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

CreateRegionCacheListener类是一个缓存侦听器，它实现了两个方法：afterCreate()和afterRegionCreate()。 afterCreate()方法创建区域。 afterRegionCreate()方法使每个新服务器创建元数据区域中定义的所有区域。

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


## 分区区域

除基本区域管理外，分区区域还包括高可用性选项，数据位置控制以及跨群集的数据平衡。

- **了解分区**

  要使用分区区域，您应该了解它们的工作方式以及管理它们的选项。

- **配置分区区域**

  规划主机和访问者成员的分区区域的配置和持续管理，并配置启动区域。

- **配置分区区域的桶数**

  确定要分配给分区区域的桶数，并相应地设置配置。

- **Custom-Partitioning and Colocating Data**

  您可以自定义Apache Geode如何使用自定义分区和数据同地对分区区域数据进行分组。

- **配置分区区域的高可用性**

  默认情况下，Apache Geode仅在区域的数据存储中存储分区区域数据的单个副本。 您可以配置Geode以维护分区区域数据的冗余副本，以实现高可用性。

- **配置对服务器分区区域的单跳客户端访问**

  单跳数据访问使客户端池能够跟踪分区区域的数据在服务器中的托管位置。 要访问单个条目，客户端将在一个跃点中直接联系承载key的服务器。

- **重新平衡分区区域数据**

  在对成员读取或更新并发线程的争用最小的群集中，您可以使用重新平衡来动态增加或减少数据和处理容量。

- **检查分区中的冗余**

  在某些情况下，验证分区区域数据是否为冗余并且在成员重新启动时，已跨分区区域成员正确恢复冗余非常重要。

- **将分区区域数据移动到另一个成员**

  您可以使用`PartitionRegionHelper`,`moveBucketByKey`和`moveData`方法将分区区域数据从一个成员显式移动到另一个成员。


### 了解分区

要使用分区区域，您应该了解它们的工作方式以及管理它们的选项。

在操作期间，分区区域看起来像一个大的虚拟区域，具有定义区域的所有成员保持相同的逻辑视图。![img](assets/how_partitioning_works_1.svg)

对于您定义区域的每个成员，您可以选择允许区域数据存储的空间，包括根本没有本地存储。 无论本地存储多少，该成员都可以访问所有区域数据。![img](assets/how_partitioning_works_2.svg)

群集可以具有多个分区区域，并且可以将分区区域与分布区域和局部区域混合。 除了具有局部范围的区域之外，对于唯一区域名称的通常要求仍然适用。 单个成员可以托管多个分区区域。

#### 数据分区

Geode自动确定托管分区区域数据的成员中数据的物理位置。 Geode将分区区域数据分解为称为存储区的存储单元，并将每个存储区存储在区域主机成员中。 存储桶根据成员的区域属性设置进行分配。

创建条目时，会将其分配给存储桶。 key组合在一个桶中并始终保留在那里。 如果配置允许，则可以在构件之间移动桶以平衡负载。

您必须运行所需的数据存储以适应分区区域的存储区。 您可以动态启动新的数据存储。 当新数据存储创建区域时，它负责分区区域和成员配置允许的桶数。

您可以自定义Geode如何使用自定义分区和数据同地对分区区域数据进行分组。

#### 分区区域操作

分区区域的运行方式与分区范围的非分区区域非常相似。 大多数标准的`Region`方法都是可用的，尽管一些通常是本地操作的方法成为分布式操作，因为它们作为一个整体而不是本地缓存在分区区域上工作。 例如，分区区域中的`put`或`create`实际上可能不会存储到调用该操作的成员的高速缓存中。 检索任何条目只需要成员之间不超过一跳。

与其他区域一样，分区区域支持客户端/服务器模型。 如果需要将数十个客户端连接到单个分区区域，则使用服务器可以大大提高性能。

#### 有关分区区域的其他信息

请记住有关分区区域的以下内容：

- 分区区域永远不会异步运行。 分区区域中的操作始终等待来自包含原始数据条目和任何冗余副本的高速缓存的确认。
- 分区区域需要每个区域数据存储中的缓存加载器（`local-max-memory`> 0）。
- Geode在存储分区区域数据的所有成员之间尽可能均匀地分布数据桶，在您使用的任何自定义分区或数据同地的限制内。 分配区域分配的桶数决定了数据存储的粒度，从而决定了数据的均匀分布。 存储桶的数量是整个群集中整个区域的总数。
- 在重新平衡该地区的数据时，Geode移动存储桶，但不会在存储桶内移动数据。
- 您可以查询分区区域，但存在某些限制。 有关详细信息，请参阅[查询分区区域](https://geode.apache.org/docs/guide/17/developing/querying_basics/querying_partitioned_regions.html#querying_partitioned_regions) 。


### 配置分区区域

规划主机和访问者成员的分区区域的配置和持续管理，并配置启动区域。

在开始之前，请了解[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).

1. 使用`PARTITION`区域快捷键设置之一启动区域配置。 请参见[区域快捷方式和自定义命名区域属性](https://geode.apache.org/docs/guide/17/basic_config/data_regions/region_shortcuts.html).
2. 如果您需要分区区域的高可用性，请为此配置。 请参见[为分区区域配置高可用性](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
3. 估算该地区所需的空间量。 如果使用冗余，则这是存储在成员中的所有主副本和副副本的最大值。 例如，冗余为1时，每个区域数据条目需要的空间是没有冗余的两倍，因为条目存储了两次。 请参阅[缓存数据的内存要求](https://geode.apache.org/docs/guide/17/reference/topics/memory_requirements_for_cache_data.html#calculating_memory_requirements).
4. 配置该区域的桶总数。 对于同地区域，此数字必须相同。 请参见[配置分区区域的桶数](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_bucket_for_pr.html#configuring_total_buckets).
5. 为区域配置成员的数据存储和数据加载：
   1. 您可以让没有本地数据存储的成员和具有不同存储量的成员。 确定此区域的不同成员类型中可用的最大内存。 这些将在`partition-attributes` `local-max-memory`中设置。 这是`partition-attributes`中唯一可以在成员之间变化的设置。 使用这些最大值和您对区域内存要求的估计值来帮助您计算该区域开始的成员数量。
   2. 对于存储区域数据的成员(`local-max-memory`大于0)，定义数据加载器。 请参阅[实现数据加载器](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/implementing_data_loaders.html#implementing_data_loaders).
   3. 如果您的成员没有本地数据存储（`local-max-memory`设置为0），请检查系统启动/关闭过程。 当任何没有存储的成员正在运行时，请确保始终至少有一个成员具有本地数据存储。
6. 如果要自定义对区域中的数据进行分区，或者在多个区域之间同地数据，请相应地进行编码和配置。 请参阅[了解自定义分区和数据同地](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/custom_partitioning_and_data_colocation.html#custom_partitioning_and_data_colocation).
7. 规划您的分区重新平衡策略并为其配置和编程。 请参阅[重新平衡分区区域数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data).

**注意:** 要使用gfsh配置分区区域，请参阅[gfsh命令帮助](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/gfsh_command_index.html#concept_C291647179C5407A876CC7FCF91CF756).


### 配置分区区域的桶数

确定要分配给分区区域的桶数，并相应地设置配置。

分区区域的存储区总数决定了数据存储的粒度，从而决定了数据的均匀分布。 Geode在数据存储中尽可能均匀地分配存储桶。 区域创建后，桶的数量是固定的。

分区属性`total-num-buckets`设置所有参与成员的整个分区区域的编号。 使用以下之一设置它：

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

  使用`create region`命令的-total-num-buckets参数。 例如：

  ```
  gfsh>create region --name="PR1" --type=PARTITION --total-num-buckets=7
  ```

#### 计算分区区域的桶总数

请遵循以下准则来计算分区区域的存储区总数：

- 使用素数。 这提供了最均匀的分布。
- 使其至少是您期望为该地区拥有的数据存储数量的四倍。 存储桶与数据存储的比率越大，负载在成员之间的分布越均匀。 但请注意，在负载平衡和开销之间存在折衷。 管理存储桶会带来很大的开销，尤其是冗余级别更高。

您试图避免某些成员拥有的数据条目明显多于其他成员的情况。 例如，比较接下来的两个数字。 该图显示了具有三个数据存储区和七个存储区的区域。 如果以大约相同的速率访问所有条目，则此配置在成员M3中创建热点，其具有比其他数据存储多大约50％的数据。 M3可能是一个缓慢的接收器和潜在的故障点。

![img](assets/partitioned_data_buckets_1.svg)

配置更多存储桶可使存储桶中的条目更少，数据分布更均衡。 此图使用与以前相同的数据，但将桶数增加到13.现在数据条目的分布更均匀。

![img](assets/partitioned_data_buckets_2.svg)


### 自定义分区和共享数据

您可以自定义Apache Geode如何使用自定义分区和数据同地对分区区域数据进行分组。

- **了解自定义分区和数据同地**

  自定义分区和数据同地可以单独使用，也可以相互结合使用。

- **标准自定义分区**

  默认情况下，Geode使用key上的散列策略将每个数据条目分区到一个存储桶中。 此外，键值对的物理位置从应用程序中抽象出来。 您可以通过提供标准分区解析程序来更改分区区域的这些策略，该分区解析程序将条目映射到称为分区的一组存储区。

- **固定的自定义分区**

  默认情况下，Geode使用key上的散列策略将每个数据条目分区到一个存储桶中。 此外，键值对的物理位置从应用程序中抽象出来。 您可以通过提供固定分区解析程序来更改分区区域的这些策略，该解析程序不仅将条目映射到称为分区的一组存储区，还指定哪些成员承载哪些数据存储区。

- **共享来自不同分区区域的数据**

  默认情况下，Geode为独立于任何其他分区区域的数据位置的分区区域分配数据位置。 您可以为任何分区区域组更改此策略，以便跨区域相关数据全部由同一成员托管。 某些操作需要进行同地，并通过减少对其他集群成员上托管的条目的数据访问次数来提高其他操作的性能。


#### 了解自定义分区和数据同地

自定义分区和数据同地可以单独使用，也可以相互结合使用。

##### 自定义分区

使用自定义分区将类似条目分组到区域内的区域存储桶中。 默认情况下，Geode根据条目键的哈希码为桶分配新条目。 通过自定义分区，您可以以任何方式将条目分配给存储桶。

如果使用自定义分区对区域内的类似数据进行分组，通常可以获得更好的性能。 例如，如果所有1月帐户数据都由单个成员托管，则在1月创建的所有帐户上运行的查询运行得更快。 为单个客户分组所有数据可以提高处理客户数据的数据操作的性能。 数据感知功能执行还利用了自定义分区。

使用自定义分区，您有两种选择：

- **标准自定义分区**. 使用标准自定义分区，您可以将条目分组到存储区中，但不指定存储区所在的位置。 Geode始终将条目保留在您指定的存储区中，但可以移动存储区以进行负载平衡。 请参阅[标准自定义分区](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/standard_custom_partitioning.html) for implementation and configuration details.

- **固定的自定义分区**. 使用固定的自定义分区，您可以指定每个区域条目所在的确切成员。 您将条目分配给分区，然后分配给该分区中的存储桶。 您将特定成员命名为每个分区的主要和辅助主机。

  这使您可以完全控制该区域的主要和任何辅助存储桶的位置。 当您希望在特定物理机器上存储特定数据或需要将数据保持在某些硬件元素附近时，这非常有用。

  固定分区具有以下要求和注意事项：

  - Geode无法重新平衡固定分区区域数据，因为它无法在主机成员之间移动存储桶。 您必须仔细考虑您创建的分区的预期数据加载。
  - 使用固定分区，主机成员之间的区域配置不同。 每个成员都标识它承载的命名分区，以及它是托管主副本还是主副本。 然后，您可以对固定分区解析程序进行编程以返回分区ID，因此该条目位于右侧成员上。 对于特定分区名称，只有一个成员可以是主要成员，并且该成员不能是该分区的辅助成员。

  有关实现和配置细节,请参见[固定自定义分区](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/fixed_custom_partitioning.html) 。

##### 区域之间的数据同地

通过数据同地，Geode在单个成员中存储跨多个数据区域相关的条目。 Geode通过将具有相同ID的所有区域桶一起存储在同一成员中来实现此目的。 在重新平衡操作期间，Geode将这些桶组移动到一起或根本不移动。

因此，例如，如果您有一个区域包含客户联系信息，另一个区域包含客户订单，您可以使用托管将单个客户的所有联系信息和所有订单保存在一个成员中。 这样，对单个客户执行的任何操作都只使用单个成员的缓存。

此图显示了具有数据同地的两个区域，其中数据按客户类型进行分区。

![img](assets/colocated_partitioned_regions.svg)

数据同地需要对所有同地区域使用相同的数据分区机制。 您可以使用Geode提供的默认分区或任何自定义分区策略。

您必须在同地区域中使用相同的高可用性设置。

有关实施和配置详细信息，请参阅[来自不同分区区域的共存数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html)。


#### 标准自定义分区

默认情况下，Geode使用key上的散列策略将每个数据条目分区到一个存储桶中。 此外，键值对的物理位置从应用程序中抽象出来。 您可以通过提供以自定义方式映射条目的标准自定义分区解析程序来更改分区区域的这些策略。

**注意:** 如果要同时进行区域数据和自定义分区，则所有同地区域必须使用相同的自定义分区机制。 请参阅[来自不同分区区域的共存数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data).

要自定义分区您的区域数据，请执行以下两个步骤：

- 实现接口
- 配置区域

**实现标准自定义分区**

- 以下列方式之一实现`org.apache.geode.cache.PartitionResolver`接口，在Geode使用的搜索顺序中列出：

  - **使用自定义类**. 在类中实现`PartitionResolver`，然后在创建区域时将类指定为分区解析器。
  - **使用key的类**. 让条目key的类实现`PartitionResolver`接口。
  - **使用回调参数的类**. 让实现你的回调参数的类实现`PartitionResolver`接口。 使用此实现时，任何和所有`Region`操作必须是指定回调参数的操作。

- 实现解析器的`getName`，`init`和`close`方法。

  `getName`的简单实现是

  ```
  return getClass().getName();
  ```

  `init`方法在高速缓存启动时执行与分区解析器任务相关的任何初始化步骤。 仅当使用由`gfsh`或XML（在`cache.xml`文件中）配置的自定义类时才会调用此方法。

  `close`方法完成在缓存关闭完成之前必须完成的任何清理。 例如，`close`可能会关闭分区解析器打开的文件或连接。

- 实现解析器的`getRoutingObject`方法以返回每个条目的路由对象。 返回的路由对象的哈希值确定存储桶。 因此，`getRoutingObject`应该返回一个对象，该对象在通过其`hashCode`运行时，将分组对象定向到所需的存储桶。

  **注意:** 只有`getKey`返回的键应该由`getRoutingObject`使用。 不要在`getRoutingObject`的实现中使用与键相关联的值或任何其他元数据。 不要使用`getOperation`或`getValue`。

**实现字符串前缀分区解析器**

Geode在`org.apache.geode.cache.util.StringPrefixPartitionResolver`中提供了基于字符串的分区解析器的实现。 该解析器不需要任何进一步的实现。 它根据key的一部分将条目分组到存储桶中。 所有键必须是字符串，每个键必须包含一个’|’字符，用于分隔字符串。 在键中的’|’分隔符之前的子字符串将由`getRoutingObject`返回。

**配置分区解析程序区域属性**

配置区域，以便Geode找到所有区域操作的解析器。

- **自定义类**. 使用以下方法之一指定分区解析程序区域属性：

  **gfsh:**

  将选项`--partition-resolver`添加到`gfsh create region`命令，指定标准自定义分区解析程序的包和类名。

  **字符串前缀分区解析器的gfsh:**

  将`--partition-resolver = org.apache.geode.cache.util.StringPrefixPartitionResolver`选项添加到`gfsh create region`命令。

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

  **字符串前缀分区解析器的Java API:**

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

  **字符串前缀分区解析器的XML:**

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

- 如果您的同地数据位于服务器系统中，请将`PartitionResolver`实现类添加到Java客户端的`CLASSPATH`中。 对于Java单跳访问，解析器类需要具有零参数构造函数，并且解析器类不能具有任何状态; `init`方法包含在此限制中。


#### 固定的自定义分区

默认情况下，Geode使用密钥上的散列策略将每个数据条目分区到一个存储桶中。 此外，键值对的物理位置从应用程序中抽象出来。 您可以通过提供固定的自定义分区解析程序来更改分区区域的这些策略，该解析程序不仅将条目映射到称为分区的一组存储区，还指定哪些成员承载哪些数据存储区。

**注意:** 如果要同时进行区域数据和自定义分区，则所有同地区域必须使用相同的自定义分区机制。 请参阅[来自不同分区区域的同地数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data).

要自定义分区您的区域数据，请执行以下两个步骤：

- 实现接口
- 配置区域

这些步骤根据使用的分区解析程序而有所不同。

**实现固定自定义分区**

- 在以下位置之一中实现`org.apache.geode.cache.FixedPartitionResolver`接口，这些位置在Geode使用的搜索顺序中列出：

  - **自定义类**. 在区域创建期间将此类指定为分区解析程序。
  - **条目 key**. 对于作为对象实现的键，定义键类的接口。
  - **在缓存回调类中**. 在缓存回调的类中实现接口。 使用此实现时，任何和所有`Region`操作必须是将回调指定为参数的操作。

- 实现解析器的`getName`，`init`和`close`方法。

  `getName`的简单实现是

  ```
  return getClass().getName();
  ```

  `init`方法在高速缓存启动时执行与分区解析器任务相关的任何初始化步骤。

  `close`方法完成在缓存关闭完成之前必须完成的任何清理。 例如，`close`可能会关闭分区解析器打开的文件或连接。

- 实现解析器的`getRoutingObject`方法以返回每个条目的路由对象。 返回的路由对象的哈希值确定分区内的存储区。

  对于每个分区只有一个存储区的固定分区，此方法可以为空。 该实现将分区分配给服务器，以便应用程序完全控制服务器上的分组条目。

  **注意:** 创建路由对象时，只应使用key上的字段。 不要为此目的使用值或其他元数据。

- 实现`getPartitionName`方法，根据您希望条目驻留的位置，为每个条目返回分区的名称。 分区中的所有条目都将位于单个服务器上。

  此示例根据日期放置数据，每个季度使用不同的分区名称，每个月使用不同的路由对象。

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

**配置固定自定义分区**

- 为每个成员设置固定分区属性。

  这些属性定义成员为区域存储的数据，并且对于不同的成员必须是不同的。 有关属性的定义，请参阅`org.apache.geode.cache.FixedPartitionAttributes`。 在区域的数据主机成员中定义每个`partition-name`。 对于每个分区名称，在要托管主副本的成员中，使用`is-primary`设置为`true`来定义它。 在要托管辅助副本的每个成员中，使用`is-primary`设置为`false`（默认值）来定义它。 辅助数量必须与您为该区域定义的冗余副本数相匹配。 请参见[为分区区域配置高可用性](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).

  **注意:** 分区的存储桶仅由已在其`FixedPartitionAttributes`中定义分区名称的成员托管。

  这些示例将成员的分区属性设置为“Q1”分区数据的主要主机和“Q3”分区数据的辅助主机。

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

    您不能使用gfsh指定固定分区解析程序。

- 如果您的同地数据位于服务器系统中，请将实现`FixedPartitionResolver`接口的类添加到Java客户端的`CLASSPATH`中。 对于Java单跳访问，解析器类需要具有零参数构造函数，并且解析器类不能具有任何状态; `init`方法包含在此限制中。


#### 共享来自不同分区区域的数据

默认情况下，Geode为独立于任何其他分区区域的数据位置的分区区域分配数据位置。 您可以为任何分区区域组更改此策略，以便跨区域相关数据全部由同一成员托管。 某些操作需要进行同地，并通过减少对其他集群成员上托管的条目的数据访问次数来提高其他操作的性能。


分区区域之间的数据同地通常可以提高数据密集型操作的性能。 您可以减少网络跃点，以便对相关数据集进行迭代操作。 数据密集型的计算密集型应用程序可以显着提高整体吞吐量。 例如，如果所有数据都分组在一个成员中，则对患者的健康记录，保险和账单信息运行的查询会更有效。 同样，如果所有交易，风险敏感度和与单个工具相关的参考数据在一起，则金融风险分析应用程序运行得更快。

**步骤**

1. 将一个区域标识为中心区域，明确地将其他区域中的数据与其区分开来。 如果对任何区域使用持久性，则必须保留中心区域。

   1. 在创建其他区域之前创建中心区域，可以在`cache.xml`或代码中创建。 XML中的区域是在代码中的区域之前创建的，因此如果在XML中创建任何共处区域，则必须先在XML中创建中心区域，然后再创建其他区域。 Geode将在其他人创建时验证其存在，如果中心区域不存在则返回`IllegalStateException`。 不要向此中心区域添加任何托管规范。

   2. 对于所有其他区域，在区域分区属性中，在`colocated-with`属性中提供中心区域的名称。 使用以下方法之一：

      - XML:

        ```xml
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

        ```java
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

2. 对于每个同地区域，对与存储区管理相关的这些分区属性使用相同的值：

   - `recovery-delay`
   - `redundant-copies`
   - `startup-recovery-delay`
   - `total-num-buckets`

3. 如果您自定义分区您的区域数据，请为所有同地区域指定自定义解析程序。 此示例对两个区域使用相同的分区解析程序：

   - XML:

     ```xml
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

     ```java
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

     指定分区解析程序，如[自定义分区您的区域数据]的配置部分所述(https://geode.apache.org/docs/guide/17/developing/partitioned_regions/using_custom_partition_resolvers.html).

4. 如果要在同地区域中保留数据，请保留中心区域，然后根据需要保留其他区域。 对您保留的所有共存区域使用相同的磁盘存储。


### 配置分区区域的高可用性

默认情况下，Apache Geode仅在区域的数据存储中存储分区区域数据的单个副本。 您可以配置Geode以维护分区区域数据的冗余副本，以实现高可用性。

- **了解分区区域的高可用性**

  凭借高可用性，为分区区域托管数据的每个成员都会获得一些主副本和一些冗余（辅助）副本。

- **配置分区区域的高可用性**

  为分区区域配置内存中高可用性。 设置其他高可用性选项，例如冗余区域和冗余恢复策略。


#### 了解分区区域的高可用性

凭借高可用性，为分区区域托管数据的每个成员都会获得一些主副本和一些冗余（辅助）副本。


使用冗余时，如果一个成员发生故障，则操作将在分区区域继续运行，而不会中断服务：

- 如果托管主副本的成员丢失，Geode会将辅助副本作为主副本。 这可能会导致暂时的冗余丢失，但不会导致数据丢失。
- 只要没有足够的辅助副本来满足冗余，系统就会通过将另一个成员分配为辅助副本并将数据复制到其中来恢复冗余。

**注意:** 如果足够的成员在足够短的时间内发生故障，您仍然可以在使用冗余时丢失缓存数据。

您可以配置系统在不满足时如何恢复冗余。 您可以将恢复配置为立即执行，或者，如果您希望为替换成员提供启动机会，则可以配置等待期。 在任何分区数据重新平衡操作期间，还会自动尝试冗余恢复。 使用`gemfire.MAX_PARALLEL_BUCKET_RECOVERIES`系统属性配置并行恢复的最大桶数。 默认情况下，系统尝试恢复冗余时，最多可并行恢复8个存储桶。

如果没有冗余，丢失任何区域的数据存储都会导致丢失某些区域的缓存数据。 通常，当应用程序可以直接从其他数据源读取，或者写入性能比读取性能更重要时，不应使用冗余。

##### 控制你的初级和二级居住地

默认情况下，Geode会为您放置主数据副本和辅助数据副本，从而避免在同一台物理计算机上放置两个副本。 如果没有足够的机器将不同的副本分开，Geode会将副本放在同一台物理计算机上。 您可以更改此行为，因此Geode仅将副本放在不同的计算机上。

您还可以控制哪些成员存储主数据副本和辅助数据副本。 Geode提供两种选择：

- **固定自定义分区**. 为该区域设置此选项。 固定分区使您可以绝对控制托管区域数据的位置。 通过固定分区，您可以为Geode提供代码，该代码为区域中的每个数据条目指定存储桶和数据存储。 将此选项与冗余配合使用时，可以指定主数据存储和辅助数据存储。 固定分区不参与重新平衡，因为您固定了所有存储桶位置。
- **冗余区域**. 此选项在成员级别设置。 冗余区域允许您按成员组或区域分隔主副本副本。 您将每个数据主机分配给区域。 然后Geode将冗余副本放在不同的冗余区域中，就像在不同的物理机器上放置冗余副本一样。 您可以使用此选项在不同的机架或网络中拆分数据副本。此选项允许您动态添加成员并使用重新平衡来重新分配数据负载，并在单独的区域中维护冗余数据。 使用冗余区域时，Geode不会在同一区域中放置两个数据副本，因此请确保您有足够的区域。

##### 在虚拟机中运行进程

默认情况下，Geode将冗余副本存储在不同的计算机上。 在虚拟机中运行进程时，计算机的常规视图将成为VM而非物理计算机。 如果在同一台物理计算机上运行多个VM，则最终可能会将分区区域主存储区存储在单独的VM中，但与第二个存储区位于同一物理计算机上。 如果物理机出现故障，您可能会丢失数据。 在VM中运行时，可以将Geode配置为标识物理计算机并将冗余副本存储在不同的物理计算机上。

##### 在高可用分区区域中进行读写

Geode在高可用性分区区域中对读取和写入的处理方式与在其他区域中不同，因为数据在多个成员中可用：

- 写操作（如`put`和`create`）转到数据键的主要操作，然后同步分发到冗余副本。 事件被发送到配置了`subscription-attributes` `intece-policy`设置为`all`的成员。
- 读操作会转到任何持有数据副本的成员，并且本地缓存很受欢迎，因此读取密集型系统可以更好地扩展并处理更高的负载。

在此图中，M1正在读取W，Y和Z.它直接从其本地副本获取W. 由于它没有Y或Z的本地副本，因此它会进入缓存，随机选择源缓存。

![img](assets/partitioned_data_HA.svg)


#### 配置分区区域的高可用性

为分区区域配置内存中高可用性。 设置其他高可用性选项，例如冗余区域和冗余恢复策略。

以下是为分区区域配置高可用性的主要步骤。 请参阅后面的部分了解详情

1. 设置系统应保留区域数据的冗余副本数。 请参阅[设置冗余份数](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_pr_redundancy.html#set_pr_redundancy).
2. （可选）如果要将数据存储成员分组到冗余区域，请相应地进行配置。 请参见[为成员配置冗余区域](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_redundancy_zones.html#set_redundancy_zones).
3. （可选）如果希望Geode仅将冗余副本放在不同的物理计算机上，请为此配置。 请参阅[设置强制唯一主机](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_enforce_unique_host.html#set_pr_redundancy).
4. 决定如何管理冗余恢复并根据需要更改Geode的默认行为。
   - **成员崩溃后**. 如果要进行自动冗余恢复，请更改其配置。 请参见[为分区区域配置成员崩溃冗余恢复](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_crash_redundancy_recovery.html#set_crash_redundancy_recovery).
   - **成员加入后**. 如果您*不*希望立即进行自动冗余恢复，请更改配置。 请参见[为分区区域配置成员加入冗余恢复](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/set_join_redundancy_recovery.html#set_join_redundancy_recovery).
5. 确定Geode在执行冗余恢复时应尝试并行恢复的桶数。 默认情况下，系统最多可并行恢复8个存储桶。 使用`gemfire.MAX_PARALLEL_BUCKET_RECOVERIES`系统属性可以在执行冗余恢复时增加或减少并行恢复的最大桶数。
6. 对于除固定分区区域之外的所有区域，请查看启动重新平衡的点。 冗余恢复在任何重新平衡开始时自动完成。 如果在成员崩溃或加入后没有自动恢复运行，这是最重要的。 请参阅[重新平衡分区区域数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data).

在运行时，您可以通过添加区域的新成员来添加容量。 对于不使用固定分区的区域，您还可以启动重新平衡操作以在所有成员之间传播区域存储桶。

- **设置冗余副本数**

  通过指定要在区域数据存储中维护的辅助副本数，为分区区域配置内存中高可用性。

- **为成员配置冗余区域**

  将成员分组到冗余区域，以便Geode将冗余数据副本分成不同的区域。

- **设置强制唯一主机**

  将Geode配置为仅使用唯一的物理机器来分区区域数据的冗余副本。

- **为分区区域配置成员崩溃冗余恢复**

  配置成员崩溃后是否以及如何在分区区域中恢复冗余。

- **为分区区域配置成员加入冗余恢复**

  配置成员加入后是否以及如何在分区区域中恢复冗余。


##### 设置冗余副本数

通过指定要在区域数据存储中维护的辅助副本数，为分区区域配置内存中高可用性。

在分区属性`redundant-copies`设置中指定分区区域数据所需的冗余副本数。 默认设置为0。

例如:

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

##### 为成员配置冗余区域

将成员分组到冗余区域，以便Geode将冗余数据副本分成不同的区域。

了解如何设置成员的'gemfire.properties`设置。 见[参考](https://geode.apache.org/docs/guide/17/reference/book_intro.html#reference).

使用`gemfire.properties`设置`redundancy-zone`将分区区域主机分组到冗余区域。

例如，如果将冗余设置为1，那么每个数据条目都有一个主副本和一个副副本，则可以通过为每个机架定义一个冗余区域，在两个计算机机架之间拆分主数据副本和辅助数据副本。 为此，您可以在`gemfire.properties`中为在一个机架上运行的所有成员设置此区域：`pre redundancy-zone = rack1`

你可以为另一个机架上的所有成员设置这个区域`gemfire.properties`：`pre redundancy-zone = rack2`

每个辅助副本都将托管在托管其主要副本的机架对面的机架上。


##### 设置强制唯一主机

将Geode配置为仅使用唯一的物理机器来分区区域数据的冗余副本。

Understand how to set a member’s `gemfire.properties` settings. See [Reference](https://geode.apache.org/docs/guide/17/reference/book_intro.html#reference).

配置您的成员，以便Geode始终使用不同的物理机器使用`gemfire.properties`设置`enforce-unique-host`来分区区域数据的冗余副本。 此设置的默认值为false。

例子:

```
enforce-unique-host=true
```

##### 为分区区域配置成员崩溃冗余恢复

配置成员崩溃后是否以及如何在分区区域中恢复冗余。

使用partition属性`recovery-delay`指定成员崩溃冗余恢复。

| 恢复延迟分区属性 | 会员失败后的效果                             |
| ---------------- | -------------------------------------------- |
| -1               | 成员失败后无法自动恢复冗余。 这是默认值。    |
| 大于或等于0      | 在恢复冗余之前成员发生故障后要等待的毫秒数。 |

默认情况下，成员崩溃后不会恢复冗余。 如果您希望快速重新启动大多数崩溃的成员，将此默认设置与成员加入冗余恢复相结合可以帮助您在成员关闭时避免不必要的数据重排。 通过等待丢失的成员重新加入，使用新启动的成员完成冗余恢复，并且使用较少的处理来更好地平衡分区。

使用以下方法之一设置崩溃冗余恢复：

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


##### 为分区区域配置成员加入冗余恢复

本节介绍配置成员加入后是否以及如何在分区区域中恢复冗余。

使用partition属性`startup-recovery-delay`指定成员加入冗余恢复。

| `startup-recovery-delay`的值 | 成员加入后的效果                                             |
| --------------------------------- | ------------------------------------------------------------ |
| -1                                | 新成员上线后无法自动恢复冗余。 使用此值和默认的`recovery-delay`设置，冗余恢复仅通过重新平衡操作来实现。 |
| long >= 0                         | 成员加入恢复冗余之前等待的毫秒数。 默认值为0（零），只要承载分区区域的成员加入，就会立即执行冗余恢复。 |

将`startup-recovery-delay`设置为高于默认值0的值允许多个新成员在冗余恢复开始之前加入。 在恢复期间存在多个成员时，系统将在它们之间扩展冗余恢复。 如果没有延迟，如果紧密连续启动多个成员，则系统可以仅选择为大多数或所有冗余恢复启动的第一个成员。

**注意:** 满足冗余与增加容量不同。 如果满足冗余，则新成员在调用重新平衡操作之前不会获取桶。

并行恢复实施迅速恢复。 因此，在同时重新启动多个成员时，将`startup-recovery-delay`配置为适当的值更为重要。 将`startup-recovery-delay`设置为一个值，确保所有成员在冗余恢复启动*之前*都已启动并可用。

使用以下方法之一设置加入冗余恢复：

- XML:

  ```xml
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


### 配置对服务器分区区域的单跳客户端访问

单跳数据访问使客户端池能够跟踪分区区域的数据在服务器中的托管位置。 要访问单个条目，客户端将在一个跃点中直接联系承载key的服务器。

- **了解客户端对服务器分区区域的单跳访问**

  通过单跳访问，客户端连接到每个服务器，因此通常使用更多连接。 这适用于较小的安装，但是缩放的障碍。

- **配置客户端对服务器分区区域的单跳访问**

  配置客户端/服务器系统，以便直接，单跳访问服务器中的分区区域数据。


#### 了解客户端对服务器分区区域的单跳访问

通过单跳访问，客户端连接到每个服务器，因此通常使用更多连接。 这适用于较小的安装，但是缩放的障碍。

如果您具有包含许多客户端的大型安装，则可能需要通过在池声明中将池属性`pr-single-hop-enabled`设置为false来禁用单跳。

如果没有单跳，客户端将使用任何可用的服务器连接，与所有其他操作相同。 接收请求的服务器确定数据位置并与主机联系，主机可能是不同的服务器。 因此，对服务器系统进行了更多的多跳请求。

**注意:** 单跳用于以下操作：`put`，`get`，`destroy`，`putAll`，`getAll`，`removeAll`和`onRegion`函数执行。

即使启用了单跳访问，您偶尔也会看到一些多跳行为。 要执行单跳数据访问，客户端会自动从服务器获取有关托管条目所在位置的元数据。 元数据是懒惰的。 它仅在单跳操作最终需要多跳之后更新，这是客户端中陈旧元数据的指示。

##### 单跳和池最大连接设置

不要在启用单跳的情况下设置池的`max-connections`设置。 使用单跳限制池的连接可能会导致连接抖动，吞吐量丢失和服务器日志膨胀。

如果您需要限制池的连接，请禁用单跳或密切关注系统中的这些负面影响。

但是，对连接设置无限制可能会导致与服务器的连接过多，从而可能导致您遇到系统的文件句柄限制。 检查您预期的连接使用情况，并确保您的服务器能够容纳它。

##### 平衡单跳服务器连接使用

当您的服务器之间的数据访问平衡良好时，单跳可带来最大的好处。 特别是，如果您将这些负载组合在一起，客户端/服务器连接的负载可能会失去平衡：

- 作为空数据访问器或不承载客户端通过单键操作访问的数据的服务器
- 来自客户的许多单键操作

如果数据访问大大失衡，客户端可能会尝试访问数据服务器。 在这种情况下，禁用单跳并通过不托管数据的服务器可能会更快。


#### 配置客户端对服务器分区区域的单跳访问

配置客户端/服务器系统，以便直接，单跳访问服务器中的分区区域数据。

这需要在服务器上使用一个或多个分区区域的客户端/服务器安装。

1. 验证客户端的池属性，未设置`pr-single-hop-enabled`或设置为true。 默认情况下是这样。
2. 如果可能，将池的`max-connections`保留为默认的无限设置（-1）。
3. 如果可能，请使用自定义数据解析程序根据客户端的数据使用模式对服务器区域数据进行分区。 请参阅[自定义 - 分区您的区域数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/using_custom_partition_resolvers.html). 在客户端的`CLASSPATH`中包含服务器的分区解析器实现。 服务器为每个自定义分区区域传递解析程序的名称，因此客户端使用正确的分区。 如果服务器不使用分区解析器，则服务器和客户端之间的默认分区匹配，因此单跳工作。
4. 将单跳注意事项添加到整个服务器负载平衡计划中。 单跳使用数据位置而不是最少加载的服务器来选择服务器以进行单键操作。 不均衡的单跳数据访问会影响整体客户端/服务器负载平衡。 一些平衡是自动完成的，因为具有更多单键操作的服务器变得更加负载，并且不太可能被挑选用于其他操作。


#### 重新平衡分区区域数据

在对成员读取或更新并发线程的争用最小的群集中，您可以使用重新平衡来动态增加或减少数据和处理容量。

重新平衡是一项成员操作。 它会影响成员定义的所有分区区域，无论成员是否承载区域的数据。 重新平衡操作执行两项任务：

1. 如果不满足配置的分区区域冗余，则重新平衡会尽其所能来恢复冗余。 请参见[为分区区域配置高可用性](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
2. 重新平衡根据需要在主机成员之间移动分区区域数据桶，以在集群中建立最公平的数据和行为平衡。

为了提高效率，在启动多个成员时，在添加所有成员后，一次触发重新平衡。

**注意:** 如果您的系统中正在运行事务，请务必规划重新平衡操作。 重新平衡可能会在成员之间移动数据，这可能会导致正在运行的事务失败并出现`TransactionDataRebalancedException`。 修复了自定义分区，完全阻止了重新平衡。 除非您在不同时间运行事务和重新平衡操作，否则所有其他数据分区策略都允许重新平衡并导致此异常。

使用以下方法之一启动重新平衡：

- `gfsh`命令。 首先，启动`gfsh`提示并连接到集群。 然后键入以下命令：

  ```
  gfsh>rebalance
  ```

  （可选）您可以指定要在重新平衡中包含或排除的区域，为重新平衡操作指定超时或仅[模拟重新平衡操作](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/rebalancing_pr_data.html#rebalancing_pr_data__section_495FEE48ED60433BADB7D36C73279C89). 输入`help rebalance`或查看[rebalance](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/rebalance.html) 以获取更多信息。

- API 调用:

  ```java
  ResourceManager manager = cache.getResourceManager(); 
  RebalanceOperation op = manager.createRebalanceFactory().start(); 
  //Wait until the rebalance is complete and then get the results
  RebalanceResults results = op.getResults(); 
  //These are some of the details we can get about the run from the API
  System.out.println("Took " + results.getTotalTime() + " milliseconds\n"); 
  System.out.println("Transfered " + results.getTotalBucketTransferBytes()+ "bytes\n");
  ```

您还可以通过API模拟重新平衡，以查看是否值得运行：

```java
ResourceManager manager = cache.getResourceManager(); 
RebalanceOperation op = manager.createRebalanceFactory().simulate(); 
RebalanceResults results = op.getResults(); 
System.out.println("Rebalance would transfer " + results.getTotalBucketTransferBytes() +" bytes "); 
System.out.println(" and create " + results.getTotalBucketCreatesCompleted() + " buckets.\n");
```

##### 分区区域重新平衡的工作原理

重新平衡操作以异步方式运行。

默认情况下，一次在一个分区区域上执行重新平衡。 对于具有共处置数据的区域，重新平衡作为一个组在区域上工作，维护区域之间的数据共置。

您可以选择通过设置`gemfire.resource.manager.threads`系统属性来并行重新平衡多个区域。 将此属性设置为大于1的值可使Geode在使用API启动重新平衡操作时并行重新平衡多个区域。

在重新平衡正在进行时，您可以继续正常使用分区区域。 数据移动时，继续执行读操作，写操作和函数执行。 如果函数正在本地数据集上执行，则在函数执行期间，如果该数据移动到另一个主机，则可能会出现性能下降。 将来的函数调用将路由到正确的成员。

Geode尝试确保每个成员具有与每个分区区域相同的可用空间百分比。 百分比在`partition-attributes`和`local-max-memory`设置中配置。

分区重新平衡：

- 除非通过溢出到磁盘启用LRU驱逐，否则不允许超出`local-max-memory`设置。
- 尽可能将同一存储桶的多个副本放在不同的主机IP地址上。
- 在存储桶迁移期间重置实时和空闲时间统计信息的输入时间。
- 替换离线成员。

##### 何时重新平衡分区区域

您通常希望在成员启动，关闭或失败时增加或减少容量时触发重新平衡。

您可能还需要在以下时间重新平衡：

- 您使用冗余实现高可用性，并将您的区域配置为在丢失后不自动恢复冗余。 在这种情况下，Geode仅在您调用重新平衡时恢复冗余。 请参见[为分区区域配置高可用性](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/configuring_ha_for_pr.html).
- 您有不均匀的数据散列。 如果您的key没有哈希代码方法（确保均匀分布），或者如果使用`PartitionResolver`来分配分区区域数据(请参阅[来自不同分区区域的共存数据](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/colocating_partitioned_region_data.html#colocating_partitioned_region_data))，则可能会出现不均匀的哈希。在任何一种情况下，一些桶可能比其他桶接收更多数据。 通过在托管大型存储桶的成员上放置更少的存储桶，可以使用重新平衡来平衡数据存储之间的负载。

##### 如何模拟区域重新平衡

您可以通过使用以下选项执行`rebalance`命令来移动任何实际数据之前模拟重新平衡操作：

```
gfsh>rebalance --simulate
```

**注意:** 如果您使用`heap_lru`进行数据驱逐，您可能会注意到模拟结果与实际重新平衡结果之间存在差异。 这种差异可能是由于VM在您执行模拟后开始逐出条目。 然后，当您执行实际的重新平衡操作时，操作将根据较新的堆大小做出不同的决定。

##### 自动重新平衡

实验[自动重新平衡功能](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/automated_rebalance.html) 会根据时间表触发重新平衡操作。


#### 检查分区中的冗余

在某些情况下，验证分区区域数据是否为冗余并且在成员重新启动时，已跨分区区域成员正确恢复冗余非常重要。

您可以通过确保所有分区区域的`numBucketsWithoutRedundancy`统计数值为**零**来验证分区区域冗余。 要检查此统计信息，请使用以下`gfsh`命令：

```
gfsh>show metrics --categories=partition --region=region_name
```

例如:

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

如果为分区区域配置了`start-recovery-delay=-1`，则需要在重新启动群集中的任何成员后对区域执行重新平衡，以便恢复冗余。

如果将`start-recovery-delay`设置为较低的数字，则可能需要等待额外的时间，直到该区域恢复冗余。


#### 将分区区域数据移动到另一个成员

您可以使用`PartitionRegionHelper` `moveBucketByKey`和`moveData`方法将分区区域数据从一个成员显式移动到另一个成员。

`moveBucketByKey`方法将包含指定键的存储桶从源成员移动到目标成员。 例如，您可以使用该方法将流行的产品项移动到新的空成员，以减少源成员的负载。

例如:

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

有关更多详细信息，请参阅`org.apache.geode.cache.partition.PartitionRegionHelper.moveBucketByKey`的Java API文档。

`moveData`方法将数据从源成员移动到目标成员的给定百分比（以字节为单位）。 例如，您可以使用此方法将指定百分比的数据从重载成员移动到另一个成员以改进分发。

例如:

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

有关更多详细信息，请参阅`org.apache.geode.cache.partition.PartitionRegionHelper.moveData`的Java API文档。

有关分区区域和重新平衡的更多信息，请参阅[分区区域](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/chapter_overview.html).


## 分布式和复制区域

除基本区域管理外，分布式和复制区域还包括推送和分配模型，全局锁定和区域条目版本等选项，以确保Geode成员之间的一致性。

- **分布式如何运作**

  要使用分布式和复制区域，您应该了解它们的工作方式以及管理它们的选项。

- **区域分布选项**

  您可以使用包含和不包含确认的分发，或使用区域分布的全局锁定。 配置为通过确认分发的区域也可以配置为在托管该区域的所有Geode成员之间一致地解析并发更新。

- **复制和预加载的工作原理**

  要使用复制和预加载区域，您应该了解如何在缓存中初始化和维护其数据。

- **配置分布式，复制和预加载区域**

  规划分布式，复制和预加载区域的配置和持续管理，并配置区域。

- **锁定全局区域**

  在全局区域中，系统在更新期间锁定条目和区域。 您还可以根据应用程序的需要显式锁定区域及其条目。 锁定包括系统设置，可帮助您优化性能并锁定成员之间的行为。


### 分布式如何运作

要使用分布式和复制区域，您应该了解它们的工作方式以及管理它们的选项。

**注意:** 复制和分布式区域的管理补充了[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html)中提供的用于管理数据区域的一般信息。 另请参见`org.apache.geode.cache.PartitionAttributes`。

分布式区域自动将条目值更新发送到远程高速缓存并从它们接收更新。

- 分布式条目更新来自`Region` `put`和`create`操作（具有非null值的条目的创建被视为已经具有条目key的远程缓存的更新）。 条目更新是有选择地分发的 - 仅限于已定义条目key的高速缓存。 与您通过复制获得的推送模型相比，这提供了拉动模型。
- 仅分发不会导致从远程缓存复制新条目。
- 分布式区域跨群集共享缓存加载器和缓存编写器应用程序事件处理程序插件。

在分布式区域中，新的和更新的条目值会自动分发到已定义条目的远程缓存中。

**步骤 1:** 应用程序更新或创建条目。 此时，M1缓存中的条目可能尚不存在。

![img](assets/distributed_how_1.svg)

**步骤 2:** 新值自动分发给持有条目的缓存。

![img](assets/distributed_how_2.svg)

**步骤 3:** 整个群集中条目的值相同。

![img](assets/distributed_how_3.svg)


### 区域分布是选项

您可以使用包含和不包含确认的分发，或使用区域分布的全局锁定。 配置为通过确认分发的区域也可以配置为在托管该区域的所有Geode成员之间一致地解析并发更新。

每个分布式区域必须在整个群集中具有相同的范围和并发检查设置。

分布式范围分为三个级别：

- **distributed-no-ack**. 分发操作返回时无需等待其他缓存的响应。 此范围提供最佳性能并使用最少的开销，但它也最容易出现由网络问题引起的不一致。 例如，网络传输层的临时中断可能导致在将更新分发到远程机器上的缓存时失败，同时本地缓存继续更新。

- **distributed-ack**. 发布在继续之前等待来自其他缓存的确认。 这比`distributed-no-ack`慢，但涵盖了简单的通信问题，例如临时网络中断。

  在存在许多`distributed-no-ack`操作的系统中，`distributed-ack`操作可能需要很长时间才能完成。 群集具有可配置的时间，等待对任何`distributed-ack`消息的确认，然后向日志发送有关无响应成员可能出现的问题的警报。 无论等待多长时间，发送方都会等待以遵守分布式ack区域设置。 管理它的`gemfire.properties`属性是`ack-wait-threshold`。

- **global**. 在分发操作期间，条目和区域会在群集中自动锁定。 对区域及其条目的所有加载，创建，放置，无效和销毁操作都使用分布式锁执行。 全局范围在整个集群中实施严格一致性，但它是实现一致性的最慢机制。 除了分发操作执行的隐式锁定之外，还可以通过应用程序API显式锁定具有全局范围及其内容的区域。 这允许应用程序对区域和区域条目执行原子，多步操作。


### 复制和预加载的工作原理

要使用复制和预加载区域，您应该了解如何在缓存中初始化和维护其数据。

通过使用`REPLICATE`区域快捷方式设置之一，或通过将region属性`data-policy`设置为`replicate`，`persistent-replicate`或`preloaded`来配置复制和预加载区域。

#### 复制和预加载区域的初始化

在区域创建时，系统使用可以找到的最完整和最新的数据集初始化预加载或复制的区域。 系统使用这些数据源按照此优先顺序初始化新区域：

1. 另一个已在集群中定义的复制区域。
2. 仅用于持久复制。 磁盘文件，后跟分布式缓存中区域的所有副本的并集。
3. 仅适用于预加载区域。 另一个已在集群中定义的预加载区域。
4. 分布式缓存中区域的所有副本的并集。

![img](assets/distributed_replica_preload.svg)

在从复制或预加载区域初始化区域时，如果源区域崩溃，则初始化将重新开始。

如果区域联合用于初始化，如图所示，并且其中一个单独的源区域在初始化期间消失（由于缓存关闭，成员崩溃或区域破坏），新区域可能包含部分数据集 来自坠毁的源区域。 发生这种情况时，不会记录任何警告或抛出异常。 新区域仍然有一整套剩余的成员区域。

#### 初始化后复制和预加载区域的行为

初始化后，预加载区域的操作类似于具有`normal`和`data-policy`数据策略的区域，仅接收它在本地缓存中定义的条目的分布。

![img](assets/distributed_preload.svg)

如果区域配置为复制区域，则它将从其他成员接收分布式区域中的所有新创建。 这是推送分发模型。 与预加载区域不同，复制区域具有一个契约，表明它将保存分布式区域中任何位置的所有条目。

![img](assets/distributed_replica.svg)

### 配置分布式，复制和预加载区域

规划分布式，复制和预加载区域的配置和持续管理，并配置区域。

在开始之前，请了解[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).

1. 选择与您的区域配置最匹配的区域快捷方式设置。 请参阅** org.apache.geode.cache.RegionShortcut **或[Region Shortcuts](https://geode.apache.org/docs/guide/17/reference/topics/chapter_overview_regionshortcuts.html#concept_ymp_rkz_4dffhdfhk). 要创建复制区域，请使用`REPLICATE`快捷方式设置之一。 要创建预加载区域，请将您的区域`data-policy`设置为`preloaded`。 这个`cache.xml`声明创建了一个复制区域：

   ```
   <region-attributes refid="REPLICATE"> 
   </region-attributes>
   ```

   您还可以使用gfsh配置区域。 例如：

   ```
   gfsh>create region --name=regionA --type=REPLICATE
   ```

   参见[区域类型](https://geode.apache.org/docs/guide/17/developing/region_options/region_types.html#region_types).

2. 选择您所在地区的分布式级别。 `RegionShortcut`中分布式区域的区域快捷方式使用`distributed-ack`范围。 如果需要不同的范围，请将`region-attributes` `scope`设置为`distributed-no-ack`或`global`。

   例子:

   ```
   <region-attributes refid="REPLICATE" scope="distributed-no-ack"> 
   </region-attributes>
   ```

3. 如果您使用`distributed-ack`范围，则可以选择启用该区域的并发检查。

   例子:

   ```
   <region-attributes refid="REPLICATE" scope="distributed-ack" concurrency-checks-enabled="true"> 
   </region-attributes>
   ```

4. 如果您正在使用`global`范围，除了Geode提供的自动锁定外，还需要编程您需要的任何显式锁定。

#### 复制区域中的本地销毁和无效

在仅影响本地缓存的所有操作中，在复制区域中仅允许本地区域销毁。 其他操作不可配置或抛出异常。 例如，您不能将本地destroy用作复制区域上的到期操作。 这是因为诸如条目失效和破坏之类的本地操作仅从本地缓存中删除数据。 如果数据在本地删除但保持不变，则复制区域将不再完整。


### 锁定全局区域

在全局区域中，系统在更新期间锁定条目和区域。 您还可以根据应用程序的需要显式锁定区域及其条目。 锁定包括系统设置，可帮助您优化性能并锁定成员之间的行为。

在具有全局范围的区域中，锁定有助于确保缓存一致性

区域和条目的锁定有两种方式：

1. **Implicit(隐式)**. Geode在大多数操作期间自动锁定全局区域及其数据条目。 区域失效和销毁不会获取锁定。

2. **Explicit(明确)**. 您可以使用API显式锁定区域及其条目。 这样做是为了保证具有多步分布式操作的任务的原子性。 `Region`方法`org.apache.geode.cache.Region.getDistributedLock`和`org.apache.geode.cache.Region.getRegionDistributedLock`为区域和指定的键返回`java.util.concurrent.locks.Lock`的实例。

   **注意:** 您必须使用`Region` API来锁定区域和区域条目。 不要在`org.apache.geode.distributed`包中使用`DistributedLockService`。 该服务仅适用于锁定任意分布式应用程序。 它与`Region`的locking方法不兼容。

#### 锁定超时

获取区域或条目的锁定是获取实体的锁定实例然后使用实例设置锁定的两步过程。 锁定后，持有它进行操作，然后将其释放给其他人使用。 您可以设置等待获取锁定所花费的时间限制以及持有锁定所花费的时间。 隐式和显式锁定操作都受超时影响：

- 锁定超时限制等待获取锁定。 缓存属性`lock-timeout`控制隐式锁请求。 对于显式锁定，通过调用从`Region` API返回的`java.util.concurrent.locks.Lock`实例来指定等待时间。 您可以等待一段特定的时间，无论是否有锁，都可以立即返回，或者无限期地等待。

  ```
  <cache lock-timeout="60"> 
  </cache>
  ```

  gfsh:

  ```
  gfsh>alter runtime --lock-timeout=60 
  ```

- 锁定租约限制锁定在自动释放之前可以保持多长时间。 定时锁允许应用程序在成员未能在租用时间内释放获得的锁时进行恢复。 对于所有锁定，此超时使用缓存属性`lock-lease`设置。

  ```
  <cache lock-lease="120"> </cache>
  ```

  gfsh:

  ```
  gfsh>alter runtime --lock-lease=120
  ```

#### 优化锁定性能

对于每个全局区域，将为已定义区域的成员之一分配锁定授予者的作业。 锁定授予者运行锁定服务，该服务接收来自系统成员的锁定请求，根据需要对它们进行排队，并按接收的顺序授予它们。

锁定授予者比其他成员略有优势，因为它是唯一一个不必发送消息来请求锁定的成员。 出于同样的原因，设保人的要求成本最低。 因此，您可以通过将锁定授予者状态分配给获取最多锁定的成员来优化区域中的锁定。 这可能是执行最多put的成员，因此需要最隐式锁，或者这可能是执行许多显式锁的成员。

锁定授予者分配如下：

- 任何具有区域定义的成员都会为其分配请求锁定授予者状态。 因此，在任何时候，发出请求的最新成员是锁定授予者。
- 如果没有成员请求区域的锁定授予者状态，或者当前锁定授予者消失，则系统从具有在其高速缓存中定义的区域的成员分配锁定授予者。

您可以申请锁定设备状态：

1. 在区域创建时通过`is-lock-grantor`属性。 您可以通过region方法`getAttributes`检索此属性，以查看您是否要求成为该区域的锁定授予者。 **注意:** 区域创建后，`is-lock-grantor`属性不会更改。
2. 通过区域`becomeLockGrantor`方法创建区域后。 但是，应该谨慎地更改锁定授予者，因为这样做需要从其他操作开始循环。 特别是，要小心避免创建一个成员争夺锁定授予者状态的情况。

#### 例子

这两个示例显示了条目锁定和解锁。 注意如何获取条目的`Lock`对象，然后调用其锁定方法来实际设置锁。 示例程序将条目锁定信息存储在哈希表中以供将来参考。

```java
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


## 区域更新的一致性

Geode确保区域的所有副本最终在托管该区域的所有成员和客户端上达到一致状态，包括分发区域事件的Geode成员。

- **按地区类型检查一致性**

  Geode根据您配置的区域类型执行不同的一致性检查。

- **配置一致性检查**

  Geode默认启用一致性检查。 您无法禁用持久性区域的一致性检查。 对于所有其他区域，您可以通过将`cache.xml`中的`concurrency-checks-enabled` 区域属性设置为“true”或“false”来显式启用或禁用一致性检查。

- **一致性检查的开销**

  一致性检查需要额外的开销来存储和分发版本和时间戳信息，以及在一段时间内维护销毁的条目以满足一致性要求。

- **一致性检查如何适用于复制区域**

  每个区域都存储用于冲突检测的版本和时间戳信息。 在应用分布式更新之前，Geode成员使用记录的信息一致地检测和解决冲突。

- **如何解决Destroy和Clear操作**

  为区域启用一致性检查时，当应用程序销毁该条目时，Geode成员不会立即从该区域中删除条目。 相反，成员将条目保留其当前版本标记一段时间，以便检测可能与已发生的操作发生冲突。 保留的条目称为*墓碑*。 为了提供一致性，Geode保留了分区区域和非复制区域以及复制区域的逻辑删除。

- **具有一致性区域的事务**

  修改启用了一致性检查的区域的事务会在事务提交时生成区域更新的所有必要版本信息。


### 按地区类型检查一致性

Geode根据您配置的区域类型执行不同的一致性检查。

**分区区域的一致性**

对于分区区域，Geode通过将给定key上的所有更新路由到保存该key主副本的Geode成员来维护一致性。 该成员持有对key的锁定，同时将更新分发给承载key副本的其他成员。 由于分区区域的所有更新都在主要Geode成员上序列化，因此所有成员都以相同的顺序应用更新，并始终保持一致性。 请参阅[了解分区](https://geode.apache.org/docs/guide/17/developing/partitioned_regions/how_partitioning_works.html).

**复制区域一致性**

对于复制区域，托管该区域的任何成员都可以更新key并将该更新分发给其他成员，而无需锁定key。 两个成员可能同时更新相同的key（并发更新）。 由于网络等待时间，一个成员的更新也可能在稍后时间分配给其他成员，之后这些成员已经对key应用了更新的更新（无序更新）。 默认情况下，Geode成员在应用区域更新之前执行冲突检查，以便检测并一致地解决并发和无序更新。 冲突检查可确保区域数据最终在托管该区域的所有成员上保持一致。 复制区域的冲突检查行为总结如下：

- 如果两个成员同时更新同一个key，则冲突检查会确保所有成员最终应用相同的值，即两个并发更新之一的值。
- 如果成员收到无序更新（在应用一个或多个最新更新后收到的更新），则冲突检查可确保丢弃无序更新，而不应用于缓存。

[一致性检查如何适用于复制区域](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_C5B74CCDD909403C815639339AA03758) 和 [如何解决Destroy和Clear操作](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_321B05044B6641FCAEFABBF5066BD399) 提供有关Geode在应用更新时如何执行冲突检查的更多详细信息。

**非复制区域和客户端缓存一致性**

当成员收到非复制区域中的条目的更新并应用更新时，它将以与复制区域相同的方式执行冲突检查。 但是，如果成员对区域中不存在的条目启动操作，则它首先将该操作传递给承载复制的成员。 承载副本的成员生成并提供后续冲突检查所需的版本信息。 请参见[一致性检查如何为复制区域工作](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_C5B74CCDD909403C815639339AA03758).

客户端缓存在收到区域条目的更新时也以相同的方式执行一致性检查。 但是，首先将源自客户端缓存的所有区域操作传递到可用的Geode服务器，该服务器生成后续冲突检查所需的版本信息。

### 配置一致性检查

Geode默认启用一致性检查。 您无法禁用持久性区域的一致性检查。 对于所有其他区域，您可以通过将`cache.xml`中的`concurrency-checks-enabled` 区域属性设置为“true”或“false”来显式启用或禁用一致性检查。

承载区域的所有Geode成员必须对该区域使用相同的`concurrency-checks-enabled`设置。

即使服务器缓存启用了对同一区域的一致性检查，客户端缓存也可以禁用区域的一致性检查。 此配置可确保客户端查看该区域的所有事件，但不会阻止客户端缓存区域与服务器缓存不同步。

参见 [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes).

**注意:** 不启用一致性检查的区域仍然受竞争条件的影响。 并发更新可能导致一个或多个成员具有相同key的不同值。 网络延迟可能导致在发生更新后将旧更新应用于key。

### 一致性检查的开销

一致性检查需要额外的开销来存储和分发版本和时间戳信息，以及在一段时间内维护销毁的条目以满足一致性要求。

为了提供一致性检查，每个区域条目使用额外的16个字节。 删除条目时，会创建并维护大约13个字节的逻辑删除条目，直到逻辑删除过期或在成员中进行垃圾收集。 （当一个条目被销毁时，该成员临时保留该条目及其当前版本标记，以检测可能与已发生的操作的冲突。保留的条目称为墓碑。）参见[如何解决销毁和清除操作](https://geode.apache.org/docs/guide/17/developing/distributed_regions/how_region_versioning_works.html#topic_321B05044B6641FCAEFABBF5066BD399).

如果您无法支持部署中的额外开销，则可以通过为每个区域设置`concurrency-checks-enabled`为“false”来禁用一致性检查。 请参阅[区域更新的一致性](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).

### 一致性检查如何适用于复制区域

每个区域都存储用于冲突检测的版本和时间戳信息。 在应用分布式更新之前，Geode成员使用记录的信息一致地检测和解决冲突。

默认情况下，区域中的每个条目都存储上次更新条目的Geode成员的ID，以及每次更新发生时递增的条目的版本标记。 版本信息存储在每个本地条目中，并且在更新本地条目时将版本标记分发给其他Geode成员。

接收更新消息的Geode成员或客户端首先将更新版本标记与其本地高速缓存中记录的版本标记进行比较。 如果更新版本标记较大，则表示该条目的较新版本，因此接收成员在本地应用更新并更新版本信息。 较小的更新版本标记表示无序更新，将被丢弃。

相同的版本标记表示多个Geode成员同时更新了相同的条目。 要解决并发更新，Geode成员始终应用（或保留）具有最高成员身份ID的区域条目; 具有较低成员资格ID的区域条目被丢弃。

**注意:** 当Geode成员丢弃更新消息时（无论是无序更新还是解析并发更新），它都不会将丢弃的事件传递给该区域的事件侦听器。 您可以使用`conflatedEvents`统计信息跟踪每个成员的丢弃更新数。 参见[Geode统计列表](https://geode.apache.org/docs/guide/17/reference/statistics_list.html#statistics_list). 某些成员可能会在其他成员应用更新时丢弃更新，具体取决于每个成员收到更新的顺序。 因此，每个Geode成员的`conflatedEvents`统计信息都不同。 以下示例更详细地描述了此行为。

以下示例显示如何在三个Geode成员的集群中处理并发更新。 假设成员A，B和C的成员资格ID分别为1,2和3。 每个成员当前在其版本C2的缓存中存储条目X（该条目最后由成员C更新）：

**步骤 1:** 应用程序更新Geode成员A上的条目X，同时另一个应用程序更新成员C上的条目X.每个成员递增条目的版本标记，并在其本地缓存中记录带有其成员标识的版本标记。 在这种情况下，条目最初是在C2版本，因此每个成员在其本地缓存中将版本更新为3（分别为A3和C3）。

![img](assets/region_entry_versions_1.svg)

**步骤 2:** 成员A将其更新消息分发给成员B和C.

成员B将更新版本标记（3）与其记录的版本标记（2）进行比较，并将更新作为版本A3应用于其本地高速缓存。 在此成员中，更新将暂时应用，并传递给已配置的事件侦听器。

成员C将更新版本标记（3）与其记录的版本标记（3）进行比较，并标识并发更新。 为解决冲突，成员C接下来将更新的成员身份ID与存储在其本地缓存中的成员身份ID进行比较。 因为更新（A3）的分布式系统ID低于存储在高速缓存（C3）中的ID，所以成员C丢弃更新（并增加`conflatedEvents`统计信息）。

![img](assets/region_entry_versions_2.svg)

**步骤 3:** 成员C将更新消息分发给成员A和B.

成员A和B将更新版本标记（3）与其记录的版本标记（3）进行比较，并识别并发更新。 为了解决冲突，两个成员都将更新的成员身份ID与存储在其本地缓存中的成员身份ID进行比较。 由于缓存值中A的分布式系统ID小于更新中的C的ID，因此两个成员都会在其本地缓存中记录更新C3，从而覆盖先前的值。

此时，托管该区域的所有成员都已成为成员A和C上的并发更新的一致状态。

![img](assets/region_entry_versions_3.svg)

### 如何解决Destroy和Clear操作

为区域启用一致性检查时，当应用程序销毁该条目时，Geode成员不会立即从该区域中删除条目。 相反，成员将条目保留其当前版本标记一段时间，以便检测可能与已发生的操作发生冲突。 保留的条目称为*墓碑*。 为了提供一致性，Geode保留了分区区域和非复制区域以及复制区域的逻辑删除。

客户端缓存或非复制区域中的逻辑删除在8分钟后到期，此时逻辑删除立即从缓存中删除。

复制或分区区域的墓碑在10分钟后到期。 过期的墓碑有资格由Geode成员进行垃圾收集。 任何类型的100,000个墓碑在本地Geode成员中超时后，将自动触发垃圾收集。 您可以选择将`gemfire.tombstone-gc-threshold`属性设置为小于100000的值，以更频繁地执行垃圾回收。

**注意:** 为了避免内存不足错误，当可用内存量低于总内存的30％时，Geode成员还会启动逻辑删除的垃圾回收。

您可以使用`CachePerfStats`中的`tombstoneCount`统计信息来监视缓存中的逻辑删除总数。 `tombstoneGCCount`统计信息记录成员执行的逻辑删除垃圾收集周期的总数。 `replicatedTombstonesSize`和`nonReplicatedTombstonesSize`分别显示复制或分区区域和非复制区域中墓碑当前消耗的大致字节数。 参见[Geode统计列表](https://geode.apache.org/docs/guide/17/reference/statistics_list.html#statistics_list).

**关于Region.clear()操作**

区域条目版本标记和逻辑删除仅在单个条目被销毁时确保一致性。 但是，`Region.clear()`操作一次对区域中的所有条目进行操作。 为了为`Region.clear()`操作提供一致性，Geode获得该区域的分布式读/写锁，该锁阻止对该区域的所有并发更新。 在清除区域之前允许在清除操作之前启动的任何更新。

### 具有一致性区域的事务

修改启用了一致性检查的区域的事务会在事务提交时生成区域更新的所有必要版本信息。

如果事务修改了正常区域，预加载区域或空区域，则事务首先委托给保存区域复制的Geode成员。 此行为类似于分区区域的事务行为，其中分区区域事务将转发到承载分区区域更新主节点的成员。

正常，预加载或空区域上的事务限制是，当启用一致性检查时，事务不能对该区域执行`localDestroy`或`localInvalidate`操作。 在这种情况下，Geode会抛出`UnsupportedOperationInTransactionException`异常。 当启用一致性检查时，应用程序应使用`Destroy`或`Invalidate`操作代替`localDestroy`或`localInvalidate`。


## 一般地区数据管理

对于所有区域，您可以选择控制内存使用，将数据备份到磁盘，以及从缓存中丢弃过时数据。

- **Persistence and Overflow(持久性和溢出)**

  您可以将数据保留在磁盘上以进行备份，并将其溢出到磁盘以释放内存，而无需从缓存中完全删除数据。

- **Eviction(驱逐)**

  使用逐出来控制数据区域大小。 驱逐行动由基于空间的阈值触发。

- **Expiration(到期)**

  使用到期可以保持数据最新并通过删除过时条目来减小区域大小。 到期操作由基于时间的阈值触发。

- **保持缓存与外部数据源同步**

  通过编程和安装适用于您所在地区的应用程序插件，使分布式缓存与外部数据源保持同步。


### Persistence and Overflow((持久性和溢出))

您可以将数据保留在磁盘上以进行备份，并将其溢出到磁盘以释放内存，而无需从缓存中完全删除数据。

**注意:** 这补充了[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html)中提供的管理数据区域的一般步骤.

所有磁盘存储都使用Apache Geode [磁盘存储](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

- **持久性和溢出如何工作**

  要使用Geode持久性和溢出，您应该了解它们如何处理您的数据。

- **配置区域持久性和溢出**

  计划数据区域的持久性和溢出并相应地进行配置。

- **溢出配置示例**

  `cache.xml`示例显示了区域和服务器订阅队列溢出的配置。


#### 持久性和溢出如何工作

要使用Geode持久性和溢出，您应该了解它们如何处理您的数据。

Geode持续存在并溢出了几种类型的数据。 您可以保留或溢出您所在地区的应用程序数据。 此外，Geode持续存在并溢出消息队列，以管理内存消耗并提供高可用性。

持久性数据比区域所在的成员更长，并且可用于在创建时初始化区域。 溢出仅作为内存中区域的扩展。

根据Geode磁盘存储的配置将数据写入磁盘。 对于任何磁盘选项，您可以指定要使用的磁盘存储的名称或使用Geode默认磁盘存储。 参见[磁盘存储](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

**数据如何保持和溢出**

对于持久性，将条目键和值复制到磁盘。 对于溢出，仅复制条目值。 其他数据（如统计信息和用户属性）仅保留在内存中。

- 数据区域通过最近最少使用（LRU）条目溢出到磁盘，因为这些条目被认为是应用程序最不感兴趣的，因此不太可能被访问。
- 服务器订阅队列溢出最近使用的（MRU）条目。 这些是位于队列末尾的消息，因此最后排队发送到客户端。

**Persistence(持久化)**

持久性提供区域条目数据的磁盘备份。 所有条目的键和值都保存到磁盘，就像在磁盘上具有该区域的副本一样。 区域输入操作（如put和destroy）在内存和磁盘上执行。

![img](assets/developing_persistence.svg)

当成员因任何原因停止时，磁盘上的区域数据仍然存在。 在分区区域中，数据存储区在成员之间划分，这可能导致某些数据仅在磁盘上，某些数据在磁盘上和内存中。 磁盘数据可以在成员启动时使用以填充相同的区域。

**Overflow(溢出)**

溢出通过将最近最少使用（LRU）条目的值移动到磁盘来限制内存中的区域大小。 溢出基本上使用磁盘作为条目值的交换空间。 如果请求的条目的值仅在磁盘上，则该值将被复制回内存，可能导致将不同LRU条目的值移动到磁盘。 与持久化条目一样，溢出条目在磁盘上维护，就像它们在内存中一样。

在此图中，条目X的值已移至磁盘以在内存中腾出空间。 X的键仍然在内存中。 从分布式系统的角度来看，磁盘上的值与内存中的数据一样是区域的一部分。

![img](assets/developing_overflow.svg)

**持久性和溢出在一起**

一起使用，持久性和溢出将所有条目键和值保留在磁盘上，并且只保留内存中最活跃的条目值。 由于溢出而从内存中删除条目值对磁盘副本没有影响，因为所有条目都已在磁盘上。

![img](assets/developing_persistence_and_overflow.svg)

**持久性和多站点配置**

多站点网关发送方队列溢出最近使用的（MRU）条目。 这些是队列末尾的消息，因此最后排队发送到远程站点。 您还可以配置网关发件人队列以保持高可用性。


#### 配置区域持久性和溢出

计划数据区域的持久性和溢出并相应地进行配置。

使用以下步骤为持久性和溢出配置数据区域：

1. 根据需要配置磁盘存储。 请参阅[设计和配置磁盘存储](https://geode.apache.org/docs/guide/17/managing/disk_storage/using_disk_stores.html#defining_disk_stores)。 缓存磁盘存储区定义数据写入磁盘的位置和方式。

   ```
   <disk-store name="myPersistentStore" . . . >
   <disk-store name="myOverflowStore" . . . >
   ```

2. 指定区域的持久性和溢出条件。 如果未使用默认磁盘存储，请在区域属性配置中提供磁盘存储名称。 要异步写入磁盘，请指定`disk-synchronous=“false”`。

   - 对于溢出，请在区域的'eviction-attributes`中指定溢出条件，并命名要使用的磁盘存储。

     例子:

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

     你不能使用gfsh配置`lru-memory-size`。

   - 对于持久性，将`data-policy`设置为`persistent-replicate`并命名要使用的磁盘存储。

     例子:

     ```
     <region name="partitioned_region" refid="PARTITION_PERSISTENT">
       <region-attributes disk-store-name="myPersistentStore">
         . . . 
       </region-attributes>
     </region> 
     ```

启动成员时，将使用磁盘存储和磁盘写入行为自动执行溢出和持久性。

**注意:** 您还可以使用gfsh命令行界面配置区域和磁盘存储。 参见[地区命令](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD) 和 [磁盘存储命令](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_1ACC91B493EE446E89EC7DBFBBAE00EA).


| 相关话题                                               |
| ------------------------------------------------------------ |
| `org.apache.geode.cache.RegionAttributes` 用于数据区域持久性信息 |
| `org.apache.geode.cache.EvictionAttributes` 用于数据区域溢出信息 |
| `org.apache.geode.cache.server.ClientSubscriptionConfig`     |


#### 溢出配置示例

`cache.xml`示例显示了区域和服务器订阅队列溢出的配置。

根据以下因素之一配置溢出条件：

- 条目计数
- 绝对内存消耗
- 内存消耗占应用程序堆的百分比（不适用于服务器订阅队列）

区域溢出配置：

```
<!-- Overflow when the region goes over 10000 entries -->
<region-attributes>
  <eviction-attributes>
    <lru-entry-count maximum="10000" action="overflow-to-disk"/>
  </eviction-attributes>
</region-attributes>
```

服务器客户端订阅队列溢出的配置：

```
<!-- Overflow the server's subscription queues when the queues reach 1 Mb of memory -->
<cache> 
  <cache-server> 
    <client-subscription eviction-policy="mem" capacity="1"/> 
  </cache-server> 
</cache>
```


### Eviction(逐出)

使用逐出来控制数据区域大小。 驱逐行动由基于空间的阈值触发。

- **逐出如何运作**

  逐出设置会导致Apache Geode通过删除最近最少使用（LRU）条目来为新条目腾出空间，从而使区域的资源在指定级别下保持使用。

- **配置数据逐出**

  配置区域的'eviction-attributes`设置以使您的区域保持在指定的限制内。


#### 驱逐如何运作

驱逐通过删除最近最少使用（LRU）条目来为新条目让路，从而使区域的资源在指定级别下保持使用。 您可以选择过期的条目是溢出到磁盘还是已销毁。 参见[持久性和溢出](https://geode.apache.org/docs/guide/17/developing/storing_data_on_disk/chapter_overview.html).

当超过基于大小的阈值时触发驱逐。 区域的逐出阈值可以基于：

- 条目计数
- 绝对内存使用量
- 可用堆的百分比

这些驱逐算法是互斥的; 只有一个可以对给定区域有效。

当Geode确定添加或更新条目会使区域超过指定级别时，它会溢出或删除足够的旧条目以腾出空间。 对于条目计数驱逐，这意味着较新条目的一对一交易。 对于内存设置，需要删除以创建空间的旧条目数取决于较旧和较新条目的大小。

为了提高效率，移除物品的选择不是严格的LRU，而是从该地区最古老的条目中选择驱逐候选者。 因此，逐出可能会在本地数据存储中留下该区域的旧条目。

**驱逐行动**

Apache Geode提供以下驱逐操作：

- **当地销毁** - 从本地缓存中删除条目，但不将删除操作分发给远程成员。 此操作可以应用于分区区域中的条目，但如果启用了冗余（冗余副本> 0），则不建议这样做，因为它会引入冗余存储区之间的不一致。 当应用于复制区域中的条目时，Geode会将区域类型静默更改为`预加载`以适应本地修改。
- **溢出到磁盘** - 条目的值溢出到磁盘并在内存中设置为null。 条目的key保留在缓存中。 这是分区区域唯一完全支持的驱逐操作。

**分区中的驱逐**

在分区区域中，Geode会删除正在执行新条目操作的存储桶中可找到的最旧条目。 Geode在逐桶的基础上维护LRU条目信息，因为跨分区区域维护信息的成本会降低系统的性能。

- 对于存储器和入口计数驱逐，LRU驱逐在正在执行新的条目操作的桶中完成，直到成员中的组合桶的总体大小已经下降到足以执行操作而不超过限制。
- 对于堆驱逐，每个分区区域桶被视为它是一个单独的区域，每个驱逐操作仅考虑桶的LRU，而不是整个分区区域。


#### 配置数据驱逐

配置区域的'eviction-attributes`设置以使您的区域保持在指定的限制内。

配置数据驱逐如下。 您无需按所示顺序执行这些步骤。

1. 决定是否根据以下方式逐出：
   - 条目计数（如果您的条目大小相对均匀，则非常有用）。
   - 使用的总字节数。 在分区区域中，使用`local-max-memory`设置。 在非分区区域中，它在`eviction-attributes`中设置。
   - 使用的应用程序堆的百分比。 这使用Geode资源管理器。 当管理器确定需要驱逐时，管理器命令驱逐控制器开始从驱逐算法设置为`lru-heap-percentage`的所有区域驱逐。 驱逐出去，直到管理器停止。 Geode驱逐该成员为该地区托管的最近最少使用的条目。 请参阅[管理堆和堆外内存](https://geode.apache.org/docs/guide/17/managing/heap_use/heap_management.html#resource_manager).
2. 确定达到限制时要采取的操作：
   - 在本地销毁该条目。
   - 将条目数据溢出到磁盘。 参见[持久性和溢出](https://geode.apache.org/docs/guide/17/developing/storing_data_on_disk/chapter_overview.html).
3. 确定成员中允许的最大数据量，用于指示的驱逐测量。 这是成员中区域的所有存储的最大值。 对于分区区域，这是存储在区域成员中的所有存储区的总数，包括用于冗余的任何辅助存储区。
4. 决定是否为您所在的地区编制自定义sizer。 如果您能够提供这样的类，它可能比Geode完成的标准大小更快。 您的自定义类必须遵循定义自定义类的准则，另外，必须实现`org.apache.geode.cache.util.ObjectSizer`。 请参见[在数据缓存中使用自定义类的要求](https://geode.apache.org/docs/guide/17/basic_config/data_entries_custom_classes/using_custom_classes.html).

**例子:**

设置LRU内存驱逐阈值为1000 MB。 使用自定义类来测量区域中每个对象的大小：

```
gfsh>create region --name=myRegion --type=REPLICATE --eviction-max-memory=1000 \
--eviction-action=overflow-to-disk --eviction-object-sizer=com.myLib.MySizer
```

在分区区域上创建逐出阈值，最大条目数为512：

```
gfsh>create region --name=myRegion --type=PARTITION --eviction-entry-count=512 \
--eviction-action=overflow-to-disk 
```

要为堆LRU驱逐配置分区区域，首先在服务器启动时配置资源管理器，然后创建启用了驱逐的区域：

```
gfsh>start server --name=Server1 --eviction-heap-percentage=80
...
gfsh>create region --name=myRegion --type=PARTITION --eviction-action=overflow-to-disk
```


### Expiration(到期)

使用到期可以保持数据最新并通过删除过时条目来减小区域大小。 到期操作由基于时间的阈值触发。

- **过期如何运作**

  到期删除您未使用的旧条目和条目。 您可以选择是否使过期的条目失效或销毁。

- **配置数据过期**

  配置到期类型和要使用的到期操作。


#### 过期如何运作

到期通过删除您未使用的旧条目和条目来保持区域数据的新鲜。 您可以选择是否使过期的条目失效或销毁。

分布式区域中的过期活动可以是分布式的或本地的。 因此，一个高速缓存可以控制系统中的多个高速缓存的到期。

此图显示了客户端/服务器系统的两个基本过期设置。 服务器（右侧）从数据库填充区域，数据自动分布在整个系统中。 数据仅在一小时内有效，因此服务器对一小时的条目执行分布式销毁。 客户端应用程序是消费者。 客户端通过删除没有本地利益的条目的本地副本（空闲时间到期）来释放其缓存中的空间。 对客户端已过期的条目的请求将转发到服务器。

![img](assets/expiration.png)

**到期类型**

Apache Geode提供两种类型的到期，每种类型都由基于时间的阈值触发。 这些可以共存; 它们不是相互排斥的。

- **Time to live (TTL)**. 在上次创建或更新后，对象可能保留在缓存中的时间量（以秒为单位）。 对于条目，对于创建和放置操作，计数器设置为零。 创建区域时以及当条目的计数器重置时，区域计数器将复位。 TTL到期属性是`region-time-to-live`和`entry-time-to-live`。
- **Idle timeout**. 在上次访问后，对象可能保留在缓存中的时间量（以秒为单位）。 只要TTL计数器复位，对象的空闲超时计数器就会复位。 此外，只要通过get操作或netSearch访问条目，就会重置条目的空闲超时计数器。 只要为其中一个条目重置空闲超时，就会重置区域的空闲超时计数器。 空闲超时到期属性是：`region-idle-time`和`entry-idle-time`。

**到期行动**

Apache Geode提供以下过期操作：

- **invalidate (default)** - 数据项的值将被删除，但key仍保留在缓存中。 适用于复制数据项的所有分布式成员。
- **destroy** - 数据项的键和值都被删除。 适用于复制数据项的所有分布式成员。
- **local invalidate** - 删除数据项的值。 仅适用于本地成员。
- **local destroy** - 删除数据项的键和值。 仅适用于本地成员。

您不能在复制或分区区域中使用`local-destroy`或`local-invalidate`到期操作。 您只能在分布式区域上使用本地选项，其数据策略为`空`，`正常`或`预加载`。

**复制区域和分区区域中的条目到期**

在复制区域中，条目更新在最方便可用的数据副本中执行，然后复制到其他成员，同时重置其上次更新的统计信息。 在分区区域中，始终在主副本中完成条目更新，重置主副本的上次更新和最后访问的统计信息，然后更新辅助副本以匹配。

在复制区域和分区区域中，条目检索使用最方便的可用数据副本，其可以是任何分布式副本。 检索不会传播给其他成员。 当数据项被考虑到期时，将协调上次访问时间的差异。

如果自上次更新或读取访问后经过的时间超过建立的阈值，则可以在复制区域的任何副本中触发到期。 分区区域中的到期在主副本中执行，基于主要的上次访问和最后更新的统计信息。 在这两种情况下，到期机制都会检查数据项的所有副本的最后访问日期，并将所有副本的最后访问日期更新为最近的最后访问日期。 然后，如果经过的时间仍然使数据项超过到期阈值，则根据为该区域指定的到期动作删除该项。

**到期设置和netSearch之间的交互**

在`netSearch`从远程缓存中检索条目值之前，它根据*local*区域的到期设置验证*remote*条目的统计信息。 已经过期的本地缓存中的条目将被传递。 验证后，该条目将进入本地缓存，并为本地副本更新本地访问和更新统计信息。 重置最后访问的时间，并将最后修改的时间更新为远程高速缓存中的时间，并对系统时钟差异进行更正。 因此，为本地条目分配了在集群中修改条目的真实最后时间。 `netSearch`操作对远程缓存中的到期计数器没有影响。

`netSearch`方法仅在分布式区域上运行，其数据策略为空，正常和预加载。


#### 配置数据过期

配置到期类型和要使用的到期操作。

- 到期操作需要将`statistics-enabled`的region属性设置为`true`。 这可以在`cache.xml`文件的region元素，`gfsh`命令行或通过API完成。
- 使用到期类型设置到期属性，包括最大时间和到期操作。 查看区域属性列表中的`entry-time-to-live`，`entry-idle-time`，`region-time-to-live`和`region-idle-time` 在 [](https://geode.apache.org/docs/guide/17/reference/topics/cache_xml.html#region-attributes).

用于到期的统计信息可通过`Region`和`Region.Entry` `getStatistics`方法返回的`CacheStatistics`对象直接提供给应用程序。 `CacheStatistics`对象还提供了重置统计计数器的方法。

**对于分区区域:**

- 在分区区域上，仅对区域的条目支持到期，而不支持区域本身。 区域范围的到期属性，例如`region-time-to-live`和`region-idle-time`不适用于分区区域中的数据项。
- 要在使用分区区域时确保可靠的读取行为，请使用`entry-time-to-live`属性，而不是`entry-idle-time`属性。
- 您不能在分区区域中使用`local-destroy`或`local-invalidate`到期操作。

**复制区域示例:**

```
// Setting standard expiration on an entry
<region-attributes statistics-enabled="true"> 
  <entry-idle-time> 
    <expiration-attributes timeout="60" action="local-invalidate"/> 
  </entry-idle-time> 
</region-attributes> 
```

- 如果应用程序需要，请覆盖特定条目的区域范围设置。 去做这个:

  1. 编写实现`org.apache.geode.cache.CustomExpiry`的自定义过期类。 例如:

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

  2. 在区域的到期属性设置中定义类。 例如：

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

  上述XML的gfsh等价物是：

  ```
  gfsh> create region --name=region1 --type=REPLICATE --enable-statistics \
  --entry-idle-time-expiration=60 --entry-idle-time-custom-expiry=com.company.mypackage.MyClass
  ```

- 当主要过期条目时，它会从辅助节点请求最后访问的统计信息。 如果有必要，主要采用最近的访问时间并重新安排到期时间。 这仅针对分布式到期操作执行，并且适用于分区和复制区域。

您还可以使用gfsh命令行界面配置区域。 参见[区域命令](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD).

**配置到期的线程数**

您可以使用`gemfire.EXPIRY_THREADS`系统属性来增加处理到期的线程数。 默认情况下，一个线程处理到期，当条目到期的速度超过线程可以使它们到期时，线程可能会变得过载。 如果单个线程处理过多的过期，则可能导致OOME。 启动缓存服务器时，将gemfire.EXPIRY_THREADS系统属性设置为所需的数字。


### 保持缓存与外部数据源同步

通过编程和安装适用于您所在地区的应用程序插件，使分布式缓存与外部数据源保持同步。

- **外部数据源概述**

  Apache Geode具有应用程序插件，可将数据读入缓存并将其写出。

- **使用JNDI配置数据库连接**.

  使用JNDI维护包含外部数据源的连接池。

- **数据加载器的工作原理**

  默认情况下，区域没有定义数据加载器。 通过在托管区域数据的成员上设置region属性cache-loader，将应用程序定义的加载程序插入任何区域。

- **实现数据加载器**

  编程数据加载器并配置您的区域以使用它。


#### 外部数据源概述

Apache Geode具有应用程序插件，可将数据读入缓存并将其写出。

应用程序插件：

1. 使用`org.apache.geode.cache.CacheLoader`的实现加载有关缓存未命中的数据。 当`get`操作无法在缓存中找到值时，将调用`CacheLoader.load`方法。 从加载器返回的值被放入缓存并返回到`get`operation。 您可以将此与数据到期结合使用以清除旧数据和其他数据加载应用程序，这些应用程序可能由外部数据源中的事件提示。 请参阅[配置数据过期](https://geode.apache.org/docs/guide/17/developing/expiration/configuring_data_expiration.html).

2. 使用缓存事件处理程序`CacheWriter`和`CacheListener`将数据写入数据源。 有关实现的详细信息，请参阅[实现缓存事件处理程序](https://geode.apache.org/docs/guide/17/developing/events/implementing_cache_event_handlers.html).

   实现缓存事件处理程序

   - `CacheWriter` 同步运行。 在对区域条目执行任何操作之前，如果为群集中的区域定义了任何缓存编写器，则系统将调用最方便的编写器。 在分区和分布式区域中，缓存编写器通常仅在包含该区域的高速缓存的子集中定义 - 通常仅在一个高速缓存中。 缓存写入器可以中止区域输入操作。
   - `CacheListener` 在更新缓存后同步运行。 此侦听器仅适用于本地缓存事件，因此请将侦听器安装在您希望它处理事件的每个缓存中。 您可以在任何缓存中安装多个缓存侦听器。

除了使用应用程序插件外，还可以在cache.xml中配置外部JNDI数据库源，并在事务中使用这些数据源。 有关详细信息，请参阅[使用JNDI配置数据库连接](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html)。


#### 使用JNDI配置数据库连接

要连接到外部数据库，例如在使用JTA事务时，可以在`cache.xml`中配置数据库JNDI数据源。 `DataSource`对象指向JDBC连接，或者更常见的是JDBC连接池。 连接池通常是首选，因为程序可以根据需要使用和重用连接，然后释放它以供另一个线程使用。

以下列表显示了JTA事务中使用的`DataSource`连接类型：

- **XAPooledDataSource**. XA池化SQL连接。
- **ManagedDataSource**. J2EE连接器体系结构（JCA）ManagedConnectionFactory的JNDI绑定类型。
- **PooledDataSource**. 池化SQL连接。
- **SimpleDataSource**. 单个SQL连接。 没有完成SQL连接池。 连接是动态生成的，无法重复使用。

`jndi-binding`元素的`jndi-name`属性是键绑定参数。 如果`jndi-name`的值是DataSource，则它被绑定为`java:/`*myDatabase**，其中*myDatabase*是您为数据源指定的名称。 如果数据源无法在运行时绑定到JNDI，则Geode会记录警告。 有关`DataSource`接口的信息，请参阅: <http://docs.oracle.com/javase/8/docs/api/javax/sql/DataSource.html>

Geode支持JDBC 2.0和3.0。

**注意:** 在CLASSPATH中包含任何数据源JAR文件。

**cache.xml中的示例DataSource配置**

以下部分显示为每个`DataSource`连接类型配置的示例`cache.xml`文件。

**XAPooledDataSource cache.xml示例(Derby)**

该示例显示了为连接到数据资源`newDB`的`XAPooledDataSource`连接池配置的`cache.xml`文件。

登录和阻止超时设置低于默认值。 连接信息，包括`user-name`和`password`，在`cache.xml`文件中设置，而不是等到连接时间。 密码未加密。

在为支持XA事务的JCA实现的数据库驱动程序指定配置属性时（换句话说，**XAPooledDataSource**），必须使用配置属性来定义数据源连接，而不是`connection-url`元素的`<jndi-binding>`属性。 配置属性因数据库供应商而异。 通过`config-property`标记指定JNDI绑定属性，如本例所示。 您可以根据需要添加尽可能多的`config-property`标签。

```xml
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

**不同XAPooledDataSource连接的JNDI绑定配置属性**

以下是不同数据库的一些示例数据源配置。 有关其他详细信息，请参阅供应商数据库的文档。

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

**ManagedDataSource连接示例（Derby）**

JCA的`ManagedConnectionFactory`的`ManagedDataSource`连接的配置如示例所示。 这种配置类似于`XAPooledDataSource`连接，除了类型是`ManagedDataSource`，你指定`managed-conn-factory-class`而不是`xa-datasource-class`。

```xml
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

**PooledDataSource示例（Derby）**

对于在任何事务之外执行的操作，使用`PooledDataSource`和`SimpleDataSource`连接。 此示例显示了一个`cache.xml`文件，该文件是为数据资源`newDB`的`PooledDataSource`连接池配置的。 对于此非事务性连接池，登录和阻止超时设置为高于前两个示例中的事务连接池。 连接信息，包括`user-name`和`password`，在`cache.xml`文件中设置，而不是等到连接时间。 密码未加密。

```xml
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

**SimpleDataSource连接示例（Derby）**

下面的示例显示`cache.xml`文件中的一个非常基本的配置，用于与数据资源`oldDB`的`SimpleDataSource`连接。 您只需要为此连接池配置一些属性，如`jndi-name`，`oldDB1`和`databaseName`，`oldDB`。 此密码为明文。

简单的数据源连接通常不需要特定于供应商的属性设置。 如果需要，请添加`config-property`标签，如前面的示例所示。

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


#### 数据加载器的工作原理

默认情况下，区域没有定义数据加载器。 通过在托管区域数据的成员上设置region属性cache-loader，将应用程序定义的加载程序插入任何区域。

在get操作期间，加载器在高速缓存未命中时被调用，除了将值返回给调用线程之外，它还使用新的条目值填充高速缓存。

可以将加载程序配置为从外部数据存储将数据加载到Geode缓存中。 要执行反向操作，将数据从Geode缓存写入外部数据存储，请使用缓存编写器事件处理程序。 请参见[实现缓存事件处理程序](https://geode.apache.org/docs/guide/17/developing/events/implementing_cache_event_handlers.html).

如何安装缓存加载器取决于区域的类型。

**分区区域中的数据加载**

由于它们可以处理大量数据，因此分区区域支持分区加载。 每个缓存加载器仅加载定义加载器的成员中的数据条目。 如果配置了数据冗余，则仅在成员拥有主副本时才加载数据。 因此，您必须在分区属性`local-max-memory`不为零的每个成员中安装缓存加载器。

如果依赖于JDBC连接，则每个数据存储都必须与数据源建立连接，如下图所示。 这三个成员需要三个连接。 有关如何配置数据源的信息，请参阅[使用JNDI配置数据库连接](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/configuring_db_connections_using_JNDI.html)。

**注意:** 分区区域通常需要比分布式区域更多的JDBC连接。

![img](assets/cache_data_loader-1545192090096.svg)


**分布式区域中的数据加载**

在非分区的分布式区域中，一个成员中定义的缓存加载器可供所有已定义区域的成员使用。 加载器通常仅定义在包含该区域的高速缓存的子集中。 当需要加载器时，将调用该区域的所有可用加载器，从最方便的加载器开始，直到加载数据或尝试所有加载器。

在下图中，一个群集的这些成员可以在不同的计算机上运行。 从M1执行对分布区域的加载。

![img](assets/cache_data_loader_2-1545192090106.svg)

**本地区域的数据加载**

对于本地区域，缓存加载器仅在定义它的成员中可用。 如果定义了加载程序，则只要在本地缓存中找不到值，就会调用它。


#### 实现数据加载器

要使用数据加载器：

1. 实现`org.apache.geode.cache.CacheLoader`接口。
2. 配置和部署实施。

**实现CacheLoader接口**

对于get操作，如果键不在缓存中，则为get操作提供服务的线程将调用`CacheLoader.load`方法。 实现`load`以返回键的值，除了返回给调用者之外，它还将被放入该区域。

`org.apache.geode.cache.CacheLoader`继承自`Declarable`，因此如果你的`CacheLoader`实现需要用一些参数初始化，那么实现`Declarable.initialize`方法。 在`cache.xml`文件或gfsh`create region`或`alter region`命令中指定所需的参数。 不要定义`Declarable.init()`方法; 它已被弃用。

这是一个示例实现：

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

如果需要从实现中运行`Region` API调用，则为它们生成单独的线程。 不要从`load`方法直接调用`Region`方法，因为它可能导致缓存加载器阻塞，从而损害集群的性能。

**配置和部署**

使用以下三种方法之一配置和部署缓存加载器：

**选项 1:** 如果通过定义`cache.xml`文件来配置集群，则在启动服务器时通过将缓存加载器添加到类路径来进行部署。

这是`cache.xml`文件中的一个示例配置，它指定不带参数的加载器：

```
<region-attributes>
    <cache-loader>
        <class-name>myLoader</class-name>
    </cache-loader>
</region-attributes>
```

或者，这是`cache.xml`文件中的一个示例配置，它指定带有参数的加载器：

```
<cache-loader>
    <class-name>com.company.data.DatabaseLoader</class-name>
    <parameter name="URL">
        <string>jdbc:cloudscape:rmi:MyData</string>
    </parameter>
</cache-loader>
```

要部署JAR文件，请在启动服务器时将缓存加载器JAR文件添加到类路径中。 例如：

```
gfsh>start server --name=s2 --classpath=/var/data/lib/myLoader.jar
```

**选项 2:** 如果在服务器启动时部署JAR文件，请将JAR文件添加到类路径并使用gfsh将配置应用于该区域。

要部署JAR文件，请在启动服务器时将缓存加载器JAR文件添加到类路径中。 例如：

```
gfsh>start server --name=s2 --classpath=/var/data/lib/myLoader.jar
```

使用gfsh将`CacheLoader`实现的配置应用于具有`gfsh create region`或`gfsh alter region`的区域。 以下是不带参数的区域创建示例：

```
gfsh>create region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

以下是使用参数创建区域的示例：

```
gfsh>create region --name=r3 \
--cache-loader=com.example.appname.myCacheLoader{'URL':'jdbc:cloudscape:rmi:MyData'}
```

以下是更改区域的示例：

```
gfsh>alter region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

**选项 3 适用于分区区域:** 如果在启动服务器后使用gfsh deploy命令部署JAR文件，请使用gfsh将配置应用于该区域。

创建服务器后，使用gfsh将JAR文件部署到所有服务器。 例如：

```
gfsh>deploy --jars=/var/data/lib/myLoader.jar
```

当服务器托管复制区域时，我们通常不使用gfsh deploy命令，详见[数据加载器如何工作](https://geode.apache.org/docs/guide/17/developing/outside_data_sources/how_data_loaders_work.html).

使用gfsh将`CacheLoader`实现的配置应用于具有`gfsh create region`或`gfsh alter region`的区域。 以下是不带参数的区域创建示例：

```
gfsh>create region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

以下是使用参数创建区域的示例：

```
gfsh>create region --name=r3 \
--cache-loader=com.example.appname.myCacheLoader{'URL':'jdbc:cloudscape:rmi:MyData'}
```

以下是更改区域的示例：

```
gfsh>alter region --name=r3 --cache-loader=com.example.appname.myCacheLoader
```

**使用缓存加载器实现服务器或对等**

具有嵌入式缓存的服务器和对等体可以仅在有意义的成员中配置缓存加载器。 例如，设计可以将从数据库加载的作业分配给由更多成员托管的区域的一个或两个成员。 当外部源是数据库时，可以这样做以减少连接数。

实现`org.apache.geode.cache.CacheLoader`接口。 区域创建配置缓存加载器，如下例所示：

```
RegionFactory<String,Object> rf = cache.createRegionFactory(REPLICATE);
rf.setCacheLoader(new QuoteLoader());
quotes = rf.create("NASDAQ-Quotes");
```


## 数据序列化

您在Geode中管理的数据必须序列化和反序列化，以便在进程之间进行存储和传输。 您可以选择多个数据序列化选项。

- **数据序列化概述**

  Geode提供除Java序列化之外的序列化选项，为数据存储，传输和语言类型提供更高的性能和更大的灵活性。

- **Geode PDX 序列化**

  Geode的便携式数据交换（PDX）是一种跨语言数据格式，可以降低分发和序列化对象的成本。 PDX将数据存储在您可以单独访问的命名字段中，以避免反序列化整个数据对象的成本。 PDX还允许您混合已添加或删除字段的对象版本。

- **Geode 数据序列化 (DataSerializable and DataSerializer)**

  Geode的`DataSerializable`接口为您提供了对象的快速序列化。

- **标准的 Java 序列化**

  您可以对仅在Java应用程序之间分发的数据使用标准Java序列化。 如果在非Java客户端和Java服务器之间分发数据，则需要执行其他编程以获取各种类格式之间的数据。


### 数据序列化概述

Geode提供除Java序列化之外的序列化选项，为数据存储，传输和语言类型提供更高的性能和更大的灵活性。

Geode移出本地缓存的所有数据都必须是可序列化的。 但是，您不一定需要实现`java.io.Serializable`，因为Geode中提供了其他序列化选项。 必须可序列化的区域数据属于以下类别：

- 分区区域
- 分布式区域
- 持久存储或溢出到磁盘的区域
- 客户端/服务器安装中的服务器或客户端区域
- 配置了网关发件人的区域，用于在多站点安装中分发事件
- 从远程缓存接收事件的区域
- 提供函数参数和结果的区域

**注意:** 如果使用[HTTP会话管理模块](https://geode.apache.org/docs/guide/17/tools_modules/http_session_mgmt/chapter_overview.html)存储对象，则这些对象必须是可序列化的，因为它们在被序列化之前 存储在该地区。

为了最大限度地降低序列化和反序列化的成本，Geode尽可能避免更改数据格式。 这意味着您的数据可能以序列化或反序列化的形式存储在缓存中，具体取决于您使用它的方式。 例如，如果服务器仅充当客户端之间数据分发的存储位置，则将数据保留为序列化形式，准备传输给请求它的客户端是有意义的。 分区区域数据最初始终以序列化形式存储。

**数据序列化选项**

使用Geode，您可以选择自动序列化域对象或使用Geode的一个接口实现序列化。 启用自动序列化意味着域对象被序列化和反序列化，而无需对这些对象进行任何代码更改。 这种自动序列化是通过使用名为`ReflectionBasedAutoSerializer`的自定义`PdxSerializer`注册域对象来执行的，该自定义`PdxSerializer`使用Java反射来推断要序列化的字段。

如果autoserialization不能满足您的需求，您可以通过实现Geode接口之一，`PdxSerializable`或`DataSerializable`来序列化您的对象。 您可以使用这些接口替换任何标准Java数据序列化以获得更好的性能。 如果你不能或不想修改你的域类，每个接口都有一个备用的序列化器类，`PdxSerializer`和`DataSerializer`。 要使用这些，请创建自定义序列化程序类，然后将其与Geode缓存配置中的域类相关联。

Geode数据序列化比PDX序列化快约25％，但使用PDX序列化将帮助您避免执行反序列化的更高成本。



| 性能                                                   | Geode Data Serializable | Geode PDX Serializable |
| ------------------------------------------------------------ | ----------------------- | ---------------------- |
| Implements Java Serializable.                                | X                       |                        |
| 处理多个版本的应用程序域对象，通过添加或减少字段来提供不同的版本. |                         | X                      |
| 提供序列化数据的单字段访问，无需完全反序列化 - 也支持OQL查询. |                         | X                      |
| Geode自动移植到其他语言             |                         | X                      |
| Works with .NET clients.                                     | X                       | X                      |
| Works with C++ clients.                                      | X                       | X                      |
| Works with Geode delta propagation.                          | X                       | X (See note below.)    |

**表 1.** 序列化选项：功能比较

**注意:** 默认情况下，您可以将Geode delta传播与PDX序列化一起使用。 但是，如果已将Geode属性`read-serialized`设置为“true”，则delta传播将不起作用。 在反序列化方面，要应用更改增量传播，需要域类实例和`fromDelta`方法。 如果你将`read-serialized`设置为true，那么你将收到一个`PdxInstance`而不是一个域类实例，而'PdxInstance`没有delta传播所需的`fromDelta`方法。

**Geode序列化（PDX或数据可序列化）和Java序列化之间的差异**

Geode序列化（PDX序列化或数据序列化）不支持循环对象图，而Java序列化则支持循环对象图。 在Geode序列化中，如果在对象图中多次引用同一对象，则为每个引用序列化对象，并且反序列化生成对象的多个副本。 相比之下，在这种情况下，Java序列化将对象序列化一次，并且在反序列化对象时，它会生成具有多个引用的对象的一个实例。


### Geode PDX序列化

Geode的便携式数据交换（PDX）是一种跨语言数据格式，可以降低分发和序列化对象的成本。 PDX将数据存储在您可以单独访问的命名字段中，以避免反序列化整个数据对象的成本。 PDX还允许您混合已添加或删除字段的对象版本。

- **Geode PDX序列化功能**

  Geode PDX序列化在功能方面具有多项优势。

- **使用PDX序列化的高级步骤**

  要使用PDX序列化，您可以配置和使用Geode基于反射的自动化程序，也可以使用PDX接口和类对对象的序列化进行编程。

- **使用基于自动反射的PDX序列化**

  您可以将缓存配置为自动序列化和反序列化域对象，而无需向其添加任何额外代码。

- **使用PdxSerializer序列化您的域对象**

  对于您不能或不想修改的域对象，请使用`PdxSerializer`类来序列化和反序列化对象的字段。 您对整个缓存使用一个`PdxSerializer`实现，为您以这种方式处理的所有域对象编程。

- **在域对象中实现PdxSerializable**

  对于可以修改源的域对象，在对象中实现`PdxSerializable`接口，并使用其方法序列化和反序列化对象的字段。

- **编写应用程序以使用PdxInstances**

  `PdxInstance`是PDX序列化字节周围的轻量级包装器。 它为应用程序提供对PDX序列化对象字段的运行时访问。

- **将JSON文档添加到Geode缓存**

  `JSONFormatter` API允许您将JSON格式的文档放入区域，然后通过将文档作为PdxInstances存储在内部来检索它们。

- **使用PdxInstanceFactory创建PdxInstances**

  当域类在服务器上不可用时，您可以使用`PdxInstanceFactory`接口从原始数据创建`PdxInstance`。

- **将PDX元数据保留到磁盘**

  Geode允许您将PDX元数据持久保存到磁盘并指定要使用的磁盘存储。

- **使用PDX对象作为区域条目键**

  Using PDX objects as region entry keys is highly discouraged.


#### Geode PDX序列化功能

Geode PDX序列化在功能方面具有多项优势。

**PDX域对象的应用程序版本控制**

域对象随应用程序代码一起发展。 您可以创建一个具有两个地址行的地址对象，然后稍后实现某些情况下需要第三行。 或者您可能会意识到某个特定字段未被使用并且想要摆脱它。 使用PDX，如果版本因添加或删除字段而不同，则可以在群集中一起使用旧版本和新版本的域对象。 通过此兼容性，您可以逐步将已修改的代码和数据引入群集，而无需关闭群集。

Geode维护PDX域对象元数据的中央注册表。 无论字段是否已定义，Geode都使用注册表保留每个成员缓存中的字段。 当成员收到具有该成员不知道的注册字段的对象时，该成员不会访问该字段，而是保留该字段并将其与整个对象一起传递给其他成员。 当成员根据成员的版本收到缺少一个或多个字段的对象时，Geode会将字段类型的Java默认值分配给缺少的字段。

**PDX可序列化对象的可移植性**

使用PDX序列化对象时，Geode将对象的类型信息存储在中央注册表中。 信息在客户端和服务器，对等端和集群之间传递。

对象类型信息的这种集中对于客户端/服务器安装是有利的，其中客户端和服务器以不同语言编写。 客户端在存储PDX序列化对象时会自动将注册表信息传递给服务器。 客户端可以针对服务器中的数据运行查询和功能，而无需服务器和存储对象之间的兼容性。 一个客户端可以将数据存储在服务器上以供另一个客户端检索，而服务器部分没有要求。

**减少序列化对象的反序列化**

PDX序列化对象的访问方法允许您检查域对象的特定字段，而无需反序列化整个对象。 根据您的对象使用情况，您可以显着降低序列化和反序列化成本。

Java和其他客户端可以针对服务器缓存中的对象运行查询和执行函数，而无需反序列化服务器端的整个对象。 查询引擎自动识别PDX对象，检索对象的`PdxInstance`并仅使用它需要的字段。 同样，对等体只能访问序列化对象中的必要字段，从而使对象以序列化形式保存在缓存中。


#### 使用PDX序列化的高级步骤

要使用PDX序列化，您可以配置和使用Geode基于反射的自动化程序，也可以使用PDX接口和类对对象的序列化进行编程。

（可选）对应用程序代码进行编程，以便对序列化对象的PDX表示中的各个字段进行反序列化。 您可能还需要将PDX元数据保留到磁盘以便在启动时进行恢复。

**步骤**

1. 对于要使用PDX序列化序列化的每种对象类型，请使用以下序列化选项之一：

   - [使用基于自动反射的PDX序列化](https://geode.apache.org/docs/guide/17/developing/data_serialization/auto_serialization.html)
   - [使用PdxSerializer序列化您的域对象](https://geode.apache.org/docs/guide/17/developing/data_serialization/use_pdx_serializer.html)
   - [在域对象中实现PdxSerializable](https://geode.apache.org/docs/guide/17/developing/data_serialization/use_pdx_serializable.html)

2. 要确保服务器不需要加载应用程序类，请将`pdx` `read-serialized`属性设置为true。 在gfsh中，在启动服务器之前执行以下命令：

   ```
   gfsh>configure pdx --read-serialized=true
   ```

   通过使用gfsh，此配置可以通过[集群配置服务](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)在群集中传播。 或者，您需要在每个服务器的`cache.xml`文件中配置`pdx read-serialized`。

3. 如果要将任何Geode数据存储在磁盘上，则必须配置PDX序列化以使用持久性。 有关详细信息，请参阅[将PDX元数据保留到磁盘](https://geode.apache.org/docs/guide/17/developing/data_serialization/persist_pdx_metadata_to_disk.html) 。

4. （可选）无论您在何处运行显式应用程序代码来检索和管理缓存条目，您都可能希望在不使用完全反序列化的情况下管理数据对象。 要执行此操作，请参阅[编写应用程序以使用Pdx实例](https://geode.apache.org/docs/guide/17/developing/data_serialization/program_application_for_pdx.html).

**PDX和多站点（WAN）部署**

仅对于多站点（WAN）安装 - 如果要在任何启用WAN的区域中使用PDX序列化，则对于每个群集，必须选择介于0（零）和255之间的唯一整数并设置`distributed-system-id` 在每个成员的`gemfire.properties`文件中。 请参见[配置多站点（WAN）系统](https://geode.apache.org/docs/guide/17/topologies_and_comm/multi_site_configuration/setting_up_a_multisite_system.html).


#### 使用基于自动反射的PDX序列化

您可以将缓存配置为自动序列化和反序列化域对象，而无需向其添加任何额外代码。

您可以自动序列化和反序列化域对象，而无需编写`PdxSerializer`类。 您可以通过使用名为`ReflectionBasedAutoSerializer`的自定义`PdxSerializer`注册域对象来执行此操作，该自定义`PdxSerializer`使用Java反射来推断要序列化的字段。

您还可以扩展ReflectionBasedAutoSerializer以自定义其行为。 例如，您可以为BigInteger和BigDecimal类型添加优化的序列化支持。 有关详细信息，请参阅[扩展ReflectionBasedAutoSerializer](https://geode.apache.org/docs/guide/17/developing/data_serialization/extending_the_autoserializer.html#concept_9E020566EE794A81A48A90BA798EC279) 。

**注意:** 您的自定义PDX autoserializable类不能使用`org.apache.geode`包。 如果是这样，PDX自动序列化器将忽略这些类。


**先决条件**

- 一般了解如何配置Geode缓存。
- 了解PDX序列化的工作原理以及如何配置应用程序以使用`PdxSerializer`。

**步骤**

在从缓存管理数据的应用程序中，根据需要提供以下配置和代码：

1. 在您希望自动化的域类中，确保每个类都有一个零参数构造函数。 例如：

   ```
   public PortfolioPdx(){}
   ```

2. 使用以下方法之一，将PDX序列化程序设置为`ReflectionBasedAutoSerializer`。

   1. 在gfsh中，在启动托管数据的任何成员之前执行以下命令：

      ```
      gfsh>configure pdx --auto-serializable-classes=com\.company\.domain\..*
      ```

      通过使用gfsh，此配置可以通过[Cluster Configuration Service]在群集中传播(https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

   2. 或者，在`cache.xml`中：

      ```xml
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

      参数`classes`采用逗号分隔的类模式列表来定义要序列化的域类。 如果您的域对象是其他域类的聚合，则需要明确注册域对象和每个域类，以便完全序列化域对象。

   3. 使用 Java API:

      ```
      Cache c = new CacheFactory()
        .setPdxSerializer(new ReflectionBasedAutoSerializer("com.company.domain.DomainObject"))
        .create();
      ```

3. 使用以下机制之一自定义`ReflectionBasedAutoSerializer`的行为：

   - 通过使用类模式字符串来指定要自动序列化的类，并自定义类的序列化方式。 可以通过将字符串传递给`ReflectionBasedAutoSerializer`构造函数或在cache.xml中指定类模式字符串来指定类模式字符串。 有关详细信息，请参阅[使用类模式字符串自定义序列化](https://geode.apache.org/docs/guide/17/developing/data_serialization/autoserialization_with_class_pattern_strings.html#concept_9B67BBE94B414B7EA63BD7E8D61D0312)。
   - 通过创建`ReflectionBasedAutoSerializer`的子类并覆盖特定方法。 有关详细信息，请参阅[扩展ReflectionBasedAutoSerializer](https://geode.apache.org/docs/guide/17/developing/data_serialization/extending_the_autoserializer.html#concept_9E020566EE794A81A48A90BA798EC279)。

4. 如果需要，配置`ReflectionBasedAutoSerializer`以检查它在尝试自动化之前传递的对象的可移植性。 当此标志设置为true时，`ReflectionBasedAutoSerializer`将在尝试自动化非可移植对象时抛出`NonPortableClassException`错误。 要设置此项，请使用以下配置：

   - 在gfsh中，使用以下命令：

     ```
     gfsh>configure pdx --portable-auto-serializable-classes=com\.company\.domain\..*
     ```

     通过使用gfsh，此配置可以通过[Cluster Configuration Service](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html)在群集中传播。

   - 在cache.xml中:

     ```xml
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

   - 使用 Java API:

     ```
     Cache c = new CacheFactory()
       .setPdxSerializer(new ReflectionBasedAutoSerializer(true,"com.company.domain.DomainObject"))
       .create();
     ```

对于您提供的每个域类，除了那些定义为`static`或`transient`的字段以及使用类模式字符串明确排除的字段外，所有字段都被视为序列化。

**注意:** `ReflectionBasedAutoSerializer`遍历给定域对象的类层次结构，以检索要考虑进行序列化的所有字段。 因此，如果`DomainObjectB`继承自`DomainObjectA`，则只需注册`DomainObjectB`即可将所有`DomainObjectB`序列化。


##### 使用类模式字符串自定义序列化

使用类模式字符串命名要使用Geode基于反射的自动传感器序列化的类，并指定对象标识字段并指定要从序列化中排除的字段。

用于配置`ReflectionBasedAutoSerializer`的类模式字符串是标准正则表达式。 例如，此表达式将选择`com.company.domain`包及其子包中定义的所有类：

```
com\.company\.domain\..*
```

您可以使用特殊符号扩充模式字符串，以定义要从序列化中排除的字段，并定义要标记为PDX标识字段的字段。 模式字符串的完整语法是：

```
<class pattern> [# (identity|exclude) = <field pattern>]... [, <class pattern>...]
```

以下示例模式字符串设置这些PDX序列化条件：

- 名称与模式`com.company.DomainObject.*`匹配的类被序列化。 在这些类中，以`id`开头的字段标记为标识字段，名为`creationDate`的字段未标记为序列化。
- 类`com.company.special.Patient`被序列化。 在类中，字段`ssn`被标记为标识字段

```
com.company.DomainObject.*#identity=id.*#exclude=creationDate, 
com.company.special.Patient#identity=ssn
```

**注意:** `identity`和`exclude`选项之间没有关联，因此上面的模式也可以表示为：

```
com.company.DomainObject.*#identity=id.*, com.company.DomainObject.*#exclude=creationDate, 
com.company.special.Patient#identity=ssn
```

**注意:** 模式的顺序无关紧要。 在确定字段是应该被视为标识字段还是应该被排除时，使用所有定义的类模式。

例子:

- 此XML使用上面显示的示例模式：

  ```
  <parameter name="classes">
    <string>com.company.DomainObject.*#identity=id.*#exclude=creationDate, 
  com.company.special.Patient#identity=ssn</string>
  </parameter>
  ```

- 此应用程序代码设置相同的模式：

  ```
  classPatterns.add("com.company.DomainObject.*#identity=id.*#exclude=creationDate,
     com.company.special.Patient#identity=ssn");
  ```

- 此应用程序代码具有相同的效果：

  ```
   Cache c = new CacheFactory().set("cache-xml-file", cacheXmlFileName)
       .setPdxSerializer(new ReflectionBasedAutoSerializer("com.foo.DomainObject*#identity=id.*",
           "com.company.DomainObject.*#exclude=creationDate","com.company.special.Patient#identity=ssn"))
       .create();
  ```


##### 扩展ReflectionBasedAutoSerializer

您可以扩展`ReflectionBasedAutoSerializer`以自定义方式处理序列化。 本节概述了可用的基于方法的自定义选项以及扩展序列化程序以支持BigDecimal和BigInteger类型的示例。

**扩展ReflectionBasedAutoSerializer的原因**

扩展`ReflectionBasedAutoSerializer`的主要用例之一是，您希望它处理当前需要由标准Java序列化处理的对象。 必须使用标准Java序列化有几个问题，可以通过扩展PDX`ReflectionBasedAutoSerializer`来解决。

- 每次我们从Geode序列化对象转换为将被Java I/O序列化的对象时，必须序列化额外数据。 这可能会导致大量的序列化开销。 这就是为什么值得扩展`ReflectionBasedAutoSerializer`以处理通常必须是Java I/O序列化的任何类的原因。
- 当遇到对象图时，扩展可以使用`ReflectionBasedAutoSerializer`的类的数量是有益的。 在对象上使用Java I/O序列化之后，对象图中该对象下的任何对象也必须是Java I/O序列化的。 这包括通常使用PDX或`DataSerializable`序列化的对象。
- 如果在对象上完成标准Java I/O序列化并且您启用了检查可移植性，则将引发异常。 即使您不关心对象的可移植性，也可以使用此标志来查找哪些类将使用标准Java序列化（通过获取它们的异常），然后增强自动序列化程序来处理它们。

**覆盖ReflectionBasedAutoSerializer行为**

您可以通过覆盖以下方法来自定义`ReflectionBasedAutoSerializer`中的特定行为：

- **isClassAutoSerialized** 自定义要自动化的类。
- **isFieldIncluded** 指定要自动化的类的哪些字段。
- **getFieldName** 定义将在autoserialization期间生成的特定字段名称。
- **isIdentifyField** 控制哪个字段标记为标识字段。 当PdxInstance计算其哈希码以确定它是否等于另一个对象时，将使用标识字段。
- **getFieldType** 确定自动化给定字段时将使用的字段类型。
- **transformFieldValue** 控制是否可以在序列化期间转换PDX对象的特定字段值。
- **writeTransform** 控制在自动序列化期间写入的字段值。
- **readTransform** 控制在自动反序列化期间读取的字段值。

这些方法仅在`ReflectionBasedAutoSerializer`第一次看到新类时调用。 结果会被记住和使用的下一次同样的类被看见。

有关这些方法及其默认行为的详细信息，请参阅[ReflectionBasedAutoSerializer]上的JavaDocs(https://geode.apache.org/releases/latest/javadoc/org/apache/geode/pdx/ReflectionBasedAutoSerializer.html) 以获取详细信息。

**优化BigInteger和BigDecimal类型的自动化的示例**

本节提供了扩展`ReflectionBasedAutoSerializer`以优化BigInteger和BigDecimal类型的自动序列化的示例。

下面的代码示例说明了`ReflectionBasedAutoSerializer`的子类，它优化了BigInteger和BigDecimal autoserialization：

```java
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


#### 使用PdxSerializer序列化您的域对象

对于您不能或不想修改的域对象，请使用`PdxSerializer`类来序列化和反序列化对象的字段。 您对整个缓存使用一个`PdxSerializer`实现，为您以这种方式处理的所有域对象编程。

使用`PdxSerializer`，您可以按原样保留域对象，并在单独的序列化程序中处理序列化和反序列化。 您在缓存PDX配置中注册序列化程序。 对序列化程序进行编程以处理所需的所有域对象。

如果您编写自己的`PdxSerializer`并且还使用`ReflectionBasedAutoSerializer`，那么`PdxSerializer`需要拥有`ReflectionBasedAutoSerializer`并委托给它。 Cache只能有一个`PdxSerializer`实例。

**注意:** `PdxSerializer``toData`和`fromData`方法与`PdxSerializable`的方法不同。 它们具有不同的参数和结果。

**步骤**

1. 在您希望PDX序列化的域类中，确保每个类都有一个零参数构造函数。 例如：

   ```
   public PortfolioPdx(){}
   ```

2. 如果您还没有为其他域对象实现`PdxSerializer`，请执行以下步骤：

   1. 创建一个新类作为缓存范围的序列化程序，并使其实现`PdxSerializer`。 如果要在`cache.xml`文件中声明新类，请让它实现`Declarable`。

      例子:

      ```
      import org.apache.geode.cache.Declarable;
      import org.apache.geode.pdx.PdxReader;
      import org.apache.geode.pdx.PdxSerializer;
      import org.apache.geode.pdx.PdxWriter;
      
      public class ExamplePdxSerializer implements PdxSerializer, Declarable {
      ...
      ```

   2. 在高速缓存pdx配置中，在高速缓存的`<pdx>` `<pdx-serializer>` `<class-name>`属性中注册序列化器类。

      例子:

      ```xml
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

      或者使用`CacheFactory.setPdxSerializer` API。

      ```java
      Cache c = new CacheFactory
         .setPdxSerializer(new ExamplePdxSerializer())
         .create();
      ```

   **注意:** 您不能使用gfsh指定自定义`pdx-serializer`类，但`configure pdx`命令会自动配置org.apache.geode.pdx.ReflectionBasedAutoSerializer类。 见[configure pdx](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/configure.html#topic_jdkdiqbgphqh).

3. 编程`PdxSerializer.toData`来识别，强制转换和处理你的域对象：

   1. 使用`PdxWriter`写入方法编写域类的每个标准Java数据字段。
   2. 为你希望Geode用来识别你的对象的每个字段调用`PdxWriter` `markIdentityField`方法。 把它放在字段的写方法之后。 Geode使用此信息来比较不同查询等操作的对象。 如果你没有设置至少一个身份字段，那么`equals`和`hashCode`方法将使用所有PDX字段来比较对象，因此也不会执行。 重要的是，`equals`和`hashCode`实现使用的字段与您标记为标识字段的字段相同。
   3. 对于类的特定版本，您需要每次始终写入相同的命名字段。 对于相同的类版本，字段名称或字段数不得从一个实例更改为另一个实例。
   4. 为获得最佳性能，首先是固定宽度字段，然后是可变长度字段。
   5. 如果需要，可以在序列化之前检查对象的可移植性，方法是在使用`ppxWriter` `writeObject`，`writeObjectArray`和`writeField`方法时添加`checkPortability`参数。

   示例`toData`代码:

   ```java
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

   1. 编写`PdxSerializer.fromData`来创建类的实例，使用`PdxReader`读取方法将序列化表单中的数据字段读入对象的字段，并返回创建的对象。

      提供与`toData`中相同的名称，并以与调用`toData`实现中的写操作相同的顺序调用读操作。

      Geode为`fromData`方法提供了域类类型和`PdxReader`。

      示例`fromData`代码:

      ```java
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

4. 如果需要，您还可以在使用`PdxWriter`时启用额外验证。 您可以通过将系统属性`gemfire.validatePdxWriters`设置为**true**来设置此项。 请注意，如果要调试新代码，则只应设置此选项，因为此选项会降低系统性能。


#### 在域对象中实现PdxSerializable

对于可以修改源的域对象，在对象中实现`PdxSerializable`接口，并使用其方法序列化和反序列化对象的字段。

**步骤**

1. 在您的域类中，实现`PdxSerializable`，导入所需的`org.apache.geode.pdx`类。

   例如:

   ```
   import org.apache.geode.pdx.PdxReader;
   import org.apache.geode.pdx.PdxSerializable;
   import org.apache.geode.pdx.PdxWriter;
   
   public class PortfolioPdx implements PdxSerializable {
     ...
   ```

2. 如果您的域类没有零参数构造函数，请为其创建一个。

   例如:

   ```
   public PortfolioPdx(){}
   ```

3. 程序`PdxSerializable.toData`

   1. 使用`PdxWriter`写入方法编写域类的每个标准Java数据字段。 Geode自动为`PdxSerializable`对象的`toData`方法提供`PdxWriter`。

   2. 为你希望Geode用来识别你的对象的每个字段调用`PdxWriter` `markIdentifyField`方法。 把它放在字段的写方法之后。 Geode使用此信息来比较不同查询等操作的对象。 如果你没有设置至少一个身份字段，那么`equals`和`hashCode`方法将使用所有PDX字段来比较对象，因此也不会执行。 重要的是，`equals`和`hashCode`实现使用的字段与您标记为标识字段的字段相同。

   3. 对于类的特定版本，您需要每次始终写入相同的命名字段。 对于相同的类版本，字段名称或字段数不得从一个实例更改为另一个实例。

   4. 为获得最佳性能，首先是固定宽度字段，然后是可变长度字段。

      示例`toData`代码：

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

4. 编程`PdxSerializable.fromData`使用`PdxReader`读取方法将序列化形式的数据字段读入对象的字段。

   提供与`toData`中相同的名称，并以与调用`toData`实现中的写操作相同的顺序调用读操作。

   Geode自动为`PdxSerializable`objects的`fromData`方法提供`PdxReader`。

   示例`fromData`代码：

   ```java
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

**接下来做什么**

- 根据需要，配置和编程Geode应用程序以使用`PdxInstance`进行选择性对象反序列化。 请参阅[编写应用程序以使用Pdx实例](https://geode.apache.org/docs/guide/17/developing/data_serialization/program_application_for_pdx.html).


#### 编写应用程序以使用PdxInstances

`PdxInstance`是PDX序列化字节周围的轻量级包装器。 它为应用程序提供对PDX序列化对象字段的运行时访问。

您可以将缓存配置为在反序列化PDX序列化对象时返回`PdxInstance`，而不是将对象反序列化为域类。 然后，您可以编写读取条目的应用程序代码，以处理从缓存中获取的`PdxInstance`。

**注意:** 这仅适用于使用诸如`EntryEvent.getNewValue`和`Region.get`之类的方法显式编码的条目检索，就像在函数内部或缓存侦听器代码中一样。 这不适用于查询，因为查询引擎会检索条目并为您处理对象访问。

如果将高速缓存配置为允许PDX序列化读取，则从高速缓存中获取将以找到的形式返回数据。 如果对象未序列化，则fetch返回域对象。 如果对象是序列化的，则fetch返回对象的`PdxInstance`。

**注意:** 如果您使用的是`PdxInstance`，则无法使用增量传播将更改应用于PDX序列化对象。

例如，在编程和配置为处理来自客户端的所有数据活动的客户端/服务器应用程序中，在服务器端完成的PDX序列化读取将始终返回`PdxInstance`。 这是因为所有数据都被序列化以便从客户端传输，并且您没有执行任何服务器端活动来反序列化服务器缓存中的对象。

在混合情况下，例如从客户端操作填充服务器缓存以及在服务器端完成的数据加载，在服务器上完成的提取可以返回`PdxInstance`和域对象的混合。

在启用PDX序列化读取的高速缓存中获取数据时，最安全的方法是编码以处理这两种类型，从获取操作接收`对象`，检查类型并在适当时进行转换。 但是，如果您知道该类在JVM中不可用，则可以避免执行类型检查。

`PdxInstance`会覆盖您为对象的`equals`和`hashcode`方法编写的任何自定义实现。 编写PDX序列化对象时，请确保至少标记了一个标识字段。 如果您没有设置至少一个标识字段，那么PdxInstance`equals`和`hashCode`方法将使用所有PDX字段来比较对象，因此也不会执行。


**先决条件**

- 一般了解如何配置Geode缓存。 参见[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html#basic_config_management).


**步骤**

在从缓存中获取数据的应用程序中，根据需要提供以下配置和代码：

1. 在运行条目提取的成员的cache.xml文件中，将`<pdx>` `read-serialized`属性设置为true。 不必在您为其编码的成员上访问数据。 例如，如果客户端应用程序在服务器上运行某个函数，则实际的数据访问是在服务器上完成的，因此您在服务器上将`read-serialized`设置为true。

   例如:

   ```
   // Cache configuration setting PDX read behavior 
   <cache>
     <pdx read-serialized="true" />
     ...
   </cache>
   ```

2. 编写从缓存中获取数据的应用程序代码来处理`PdxInstance`。 如果您确定只从缓存中检索`PdxInstance`，则只能为此编码。 在许多情况下，可能会从缓存条目检索操作返回`PdxInstance`或域对象，因此您应检查对象类型并处理每种可能的类型。

   例如:

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

   **注意:** 由于PDX的限制，如果启用PDX的缓存包含TreeSet域对象，则应实现可以处理域对象和PdxInstance对象的Comparator。 您还需要在服务器上提供域类。


#### 将JSON文档添加到Geode缓存

`JSONFormatter` API允许您将JSON格式的文档放入区域，然后通过将文档作为PdxInstances存储在内部来检索它们。

Geode本身支持使用JSON格式的文档。 将JSON文档添加到Geode缓存时，可以调用JSONFormatter API将它们转换为PDX格式（作为`PdxInstance`），这使Geode能够在字段级别理解JSON文档。

在查询和索引方面，由于文档在内部存储为PDX，因此应用程序可以对JSON文档中包含的任何字段（包括任何嵌套字段（在JSON对象或JSON数组中）进行索引。）对这些存储文档运行的任何查询都将返回PdxInstances 作为结果。 要更新存储在Geode中的JSON文档，可以在PdxInstance上执行一个函数。

然后，您可以使用`JSONFormatter`将PdxInstance结果转换回JSON文档。

`JSONFormatter`使用流解析器（[Jackson](http://wiki.fasterxml.com/JacksonHome)，JSON处理器）将JSON文档转换为优化的PDX格式。 要使用JSONFormatter，请确保应用程序的CLASSPATH中有`lib/geode-dependencies.jar`。

`JSONFormatter`类有四个静态方法，用于将JSON文档转换为PdxInstances，然后将这些PdxInstances转换回JSON文档。

在将任何JSON文档放入Geode区域之前，需要调用以下方法：

- `fromJSON`. 从JSON字节数组创建PdxInstance。 返回PdxInstance。
- `fromJSON`. 从JSON字符串创建PdxInstance。 返回PdxInstance。

将JSON文档作为PdxInstance放入区域后，您可以执行标准Geode查询并在JSON文档上创建索引，方法与查询或索引任何其他Geode PdxInstance的方式相同。

执行Geode查询或调用`region.get`后，您可以使用以下方法将PdxInstance转换回JSON格式：

- `toJSON`. 读取PdxInstance并返回JSON字符串。
- `toJSONByteArray`. 读取PdxInstance并返回JSON字节数组。

有关使用JSONFormatter的更多信息，请参阅`org.apache.geode.pdx.JSONFormatter`的Java API文档。

**序列化JSON字段的排序行为**

默认情况下，Geode序列化为每个唯一的JSON文档创建唯一的pdx typeID，即使JSON文档之间的唯一区别是其字段的指定顺序。

如果您希望仅按指定字段顺序不同的JSON文档映射到相同的typeID，请将属性`gemfire.pdx.mapper.sort-json-field-names`设置为“true”。 这告诉系统在序列化之前对JSON字段进行排序，允许系统识别匹配的条目，并有助于减少序列化机制生成的pdx typeID的数量。


#### 使用PdxInstanceFactory创建PdxInstances

当域类在服务器上不可用时，您可以使用`PdxInstanceFactory`接口从原始数据创建`PdxInstance`。

当您需要一个域类的实例来插入代码（如函数或加载器）时，这可能特别有用。 如果您拥有域对象的原始数据（类名和每个字段的类型和数据），那么您可以显式创建`PdxInstance`。 `PdxInstanceFactory`与`PdxWriter`非常相似，只是在写完每个字段后，需要调用create方法返回创建的PdxInstance。

创建工厂调用`RegionService.createPdxInstanceFactory`。 工厂只能创建单个实例。 要创建多个实例，请创建多个工厂或使用`PdxInstance.createWriter()`来创建后续实例。 使用`PdxInstance.createWriter()`通常更快。

创建PdxInstance时，使用`markIndentityField`方法设置至少一个标识字段。 如果不标记标识字段，则PdxInstance`equals`和`hashCode`方法将使用所有PDX字段来比较对象，因此也不会执行。 重要的是，`equals`和`hashCode`实现使用的字段与您标记为标识字段的字段相同。

以下是使用`PdxInstanceFactory`的代码示例：

```java
PdxInstance pi = cache.createPdxInstanceFactory("com.company.DomainObject")
   .writeInt("id", 37)
   .markIdentityField("id")
   .writeString("name", "Mike Smith")
   .writeObject("favoriteDay", cache.createPdxEnum("com.company.Day", "FRIDAY", 5))
   .create();
```

有关更多信息，请参阅Java API文档中的`PdxInstanceFactory`。

**枚举对象为PdxInstances**

您现在可以使用枚举对象作为PdxInstances。 从缓存中获取枚举对象时，现在可以将其反序列化为`PdxInstance`。 要检查`PdxInstance`是否为枚举，请使用`PdxInstance.isEnum`方法。 枚举`PdxInstance`将有一个名为`name`的字段，其值是与枚举常量名称对应的String。

枚举`PdxInstance`不可写; 如果你调用`createWriter`它将抛出异常。

`RegionService`有一个方法，允许你创建一个表示枚举的`PdxInstance`。 请参阅Java API文档中的`RegionService.createPdxEnum`。


#### 将PDX元数据保留到磁盘

Geode允许您将PDX元数据持久保存到磁盘并指定要使用的磁盘存储。

**先决条件**

- 一般了解如何配置Geode缓存。 参见[基本配置和编程](https://geode.apache.org/docs/guide/17/basic_config/book_intro.html).
- 了解Geode磁盘存储的工作原理。 参见[磁盘存储](https://geode.apache.org/docs/guide/17/managing/disk_storage/chapter_overview.html).

**步骤**

1. 在缓存配置中将`<pdx>`属性`persistent`设置为true。 对于将PDX与持久区域一起使用的缓存以及使用网关发送方通过WAN分发事件的区域，这是必需的。否则，它是可选的。
2. （可选）如果要使用不是Geode默认磁盘存储的磁盘存储，请将`<pdx>`属性`disk-store-name`设置为非默认磁盘存储的名称。 **注意:**如果您使用PDX序列化对象作为区域条目键并且您使用的是持久区域，则必须将PDX磁盘存储配置为与持久区域使用的磁盘存储不同。
3. （可选）如果以后要重命名持久保存到磁盘的PDX类型，则可以通过执行`pdx rename`命令在脱机磁盘存储上执行此操作。 见[pdx 重命名](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/pdx.html).

**示例cache.xml：**

此示例`cache.xml`启用PDX持久性并在服务器缓存配置中设置非默认磁盘存储：

```
  <pdx read-serialized="true" 
       persistent="true" disk-store-name="SerializationDiskStore">
    <pdx-serializer>
      <class-name>pdxSerialization.defaultSerializer</class-name>
    </pdx-serializer>
  </pdx>
  <region ...
```


#### 使用PDX对象作为区域输入键

强烈建议不要将PDX对象用作区域条目键。

创建区域条目键的最佳做法是使用简单的键; 例如，使用String或Integer。 如果key必须是域类，则应使用非PDX序列化类。

如果必须使用PDX序列化对象作为区域条目键，请确保不要将`read-serialized`设置为`true`。 此配置设置将导致分区区域出现问题，因为分区区域要求key的哈希码在分布式系统中的所有JVM上都相同。 当键是`PdxInstance`对象时，其哈希码可能与域对象的哈希码不同。

如果您使用PDX序列化对象作为区域条目键并且您使用的是持久区域，则必须将PDX磁盘存储配置为与持久区域使用的磁盘存储不同。


### Geode数据序列化（DataSerializable和DataSerializer）

Geode的`DataSerializable`接口为您提供了对象的快速序列化。

**使用DataSerializable接口进行数据序列化**

Geode的`DataSerializable`接口为您提供比标准Java序列化或Geode PDX序列化更快，更紧凑的数据序列化。 然而，虽然Geode`DataSerializable`接口通常比Geode的'PdxSerializable`更高性能，但它需要在服务器上进行完全反序列化，然后再进行重新序列化以将数据发送回客户端。

您可以通过`Instantiator`注册`DataSerializable`类的实例化器来进一步加速序列化，从而无需反射来找到正确的序列化器。 您可以通过API提供自己的序列化。

注册自定义`Instantiator`的推荐方法是在cache.xml的`serialization-registration`元素中指定它。

有关更多信息，请参阅`DataSerializable`和`DataSerializer`的在线Java文档。

**例子 cache.xml:**

以下提供了如何使用cache.xml注册实例化器的示例。

```
<serialization-registration>
<instantiator id="30">
   <class-name>com.package.MyClass</class-name>
</instantiator>
</serialization-registration>
```

除了加速标准对象序列化之外，您还可以使用`DataSerializable`接口来序列化存储在缓存中的任何自定义对象。

**使用DataSerializer序列化您的域对象**

您还可以使用`DataSerializer`来序列化域对象。 它以与`DataSerializable`相同的方式序列化数据，但允许您在不修改域类代码的情况下序列化类。

请参阅[DataSerializable]上的JavaDocs(https://geode.apache.org/releases/latest/javadoc/org/apache/geode/DataSerializable.html)和[DataSerializer](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/DataSerializer.html) 了解更多信息。


### 标准Java序列化

您可以对仅在Java应用程序之间分发的数据使用标准Java序列化。 如果在非Java客户端和Java服务器之间分发数据，则需要执行其他编程以获取各种类格式之间的数据。

根据定义，标准Java类型是可序列化的。 对于您的域类，实现`java.io.Serializable`，然后确保根据对象标记瞬态和静态变量。 有关信息，请参阅Java版本的`java.io.Serializable`的联机文档。

将`DataSerializable`与`Serializable`或`PdxSerializable`混合使用在相同的数据上会导致内存使用量增加，吞吐量低于仅对整个数据使用`Serializable`，特别是如果`Serializable`条目在集合中。 数据收集越大，吞吐量越低，因为使用`DataSerializable`时不会共享集合条目的元数据。


## 事件和事件处理

Geode为缓存的数据和系统成员事件提供了通用且可靠的事件分发和处理。

- **事件是如何工作**

  集群中的成员通过缓存事件从其他成员接收缓存更新。 其他成员可以是成员，客户端或服务器或其他集群的对等成员。

- **实现Geode事件处理程序**

  您可以为区域和区域条目操作以及管理事件指定事件处理程序。

- **配置点对点事件消息**

  您可以从集群对等方接收非本地区域的任何区域的事件。 本地区域仅接收本地缓存事件。

- **配置客户端/服务器事件消息**

  您可以从服务器接收服务器端缓存事件和查询结果更改的事件。

- **配置多站点（WAN）事件队列**

  在多站点（WAN）安装中，Geode使用网关发件人队列来为使用网关发件人配置的区域分发事件。 AsyncEventListeners还使用异步事件队列来分配已配置区域的事件。 本节介绍用于配置网关发件人或AsyncEventListener实现使用的事件队列的其他选项。


### 事件是如何工作

Geode集群中的成员通过缓存事件从其他成员接收缓存更新。 其他成员可以是成员，客户端或服务器或其他集群的对等成员。

**事件特征**

这些是Geode事件的主要特征：

- 基于内容的事件
- 带有混合的异步事件通知
- 低延迟的同步事件通知
- 通过冗余消息队列实现高可用性
- 事件排序和一次且仅一次交付
- 分布式事件通知
- 持久的订阅
- 持续查询

**事件类型**

有两类事件和事件处理程序。

- 缓存API中的缓存事件由具有缓存的应用程序使用。 缓存事件为数据更改提供详细级别的通知。 连续查询事件属于此类别。
- 管理API中的管理事件由没有缓存的管理应用程序使用。

两种事件都可以由单个成员操作生成。

**注意:** 您可以在单个系统成员中处理这些类别的事件之一。 您无法在单个成员中处理缓存和管理事件。

由于Geode分别维护管理事件的顺序和缓存事件的顺序，因此在单个进程中使用缓存事件和管理事件可能会导致意外结果。

**事件周期**

以下步骤描述了事件周期:

1. 操作开始，例如数据放入或缓存关闭。
2. 操作执行生成以下对象：
   - `Operation`类型的对象，描述触发事件的方法。
   - 描述事件的事件对象，例如操作源自的成员和区域。
3. 可以处理事件的事件处理程序被调用并传递事件对象。 不同的事件类型需要不同位置的不同处理程序类型 如果没有匹配的事件处理程序，则不会改变操作的效果，这通常会发生。
4. 当处理程序收到事件时，它会触发此事件的处理程序的回调方法。 回调方法可以将事件对象作为另一个方法的输入切换。 根据事件处理程序的类型，可以在操作之前或之后触发回调。 时间取决于事件处理程序，而不是事件本身。 **注意:**对于事务，事务后监听器在事务提交后收到事件。
5. 如果操作是分布式的，那么它会导致其他成员的后续操作，那些操作会生成他们自己的事件，这些事件可以由他们的侦听器以相同的方式处理。

**事件对象**

事件对象有多种类型，具体取决于操作。 某些操作会生成多个不同类型的对象。 所有事件对象都包含描述事件的数据，每个事件类型都包含适合其匹配操作的略微不同类型的数据。 事件对象是稳定的。 例如，如果将其传递给另一个线程上的方法，则其内容不会更改。

对于缓存事件，事件对象描述在本地缓存中执行的操作。 如果事件是远程发起的，则它描述远程输入操作的本地应用程序，而不是远程操作本身。 唯一的例外是本地区域有空数据策略; 然后事件携带远程（始发）高速缓存操作的信息。

**事件分发**

成员在其本地缓存中处理事件后，会根据成员的配置和远程缓存的配置将其分发到远程缓存。 例如，如果客户端更新其缓存，则更新将转发到客户端的服务器。 服务器将更新分发给其对等方，并根据它们对数据条目的兴趣将其转发给任何其他客户端。 如果服务器系统是多站点部署的一部分并且数据区域配置为使用网关发送方，则网关发送方还将更新转发到远程站点，在该站点中进一步分发和传播更新。

**事件处理程序和区域数据存储**

您可以为没有本地数据存储的区域配置区域，并仍然发送和接收该区域的事件。 相反，如果您在区域中存储数据，则无论您是否安装了任何事件处理程序，都会使用事件中的数据更新缓存。

**多个监听器**

安装多个侦听器时，可以使用缓存侦听器，按照添加到区域或缓存的顺序依次调用侦听器。 监听器一次执行一个。 因此，除非您将侦听器编程为将处理传递给另一个线程，否则您可以在以后的侦听器中使用一个侦听器的工作。

**事件排序**

在高速缓存操作期间，在操作的各个阶段调用事件处理程序。 在区域更新之前调用一些事件处理程序，在区域更新操作之后调用一些事件处理程序。 根据被调用的事件处理程序的类型，事件处理程序可以按顺序或无序接收它们在Region上应用的事件。

- `CacheWriter`和`AsyncEventListener`总是按照它们在区域上应用的顺序接收事件。
- `CacheListener`和`CqListener`可以按照与在区域上应用它们的顺序不同的顺序接收事件。

**注意:** `EntryEvent`包含条目的旧值和新值，这有助于指示由特定键上的缓存操作替换的值。


#### 点对点事件分发

执行区域或条目操作时，Geode会根据系统和缓存配置在集群中分配关联的事件。

为每个系统成员中需要接收区域和条目更改通知的区域安装缓存侦听器。

**分区区域中的事件**

分布式操作遵循此序列在分区区域中：

1. 如果适用，将操作应用于具有主数据条目的缓存。
2. 根据其他成员的订阅属性兴趣策略进行分发。
3. 在接收分发的缓存中调用任何侦听器。
4. 使用主数据条目在缓存中调用侦听器。

在下图中：

1. 成员M1中的API调用创建一个条目。
2. 分区区域在M2中的缓存中创建新条目。 M2，主副本的持有者，驱动程序的其余部分。
3. 这两个操作同时发生：
   - 分区区域在M3中的高速缓存中创建条目的辅助副本。 创建辅助副本不会调用M3上的侦听器。
   - M2将事件分发给M4。 向其他成员的分配基于他们的利益政策。 M4拥有所有人的利益政策，因此它接收该地区任何地方的所有事件的通知。 由于M1和M3具有缓存内容的兴趣策略，并且此事件不会影响其本地缓存中的任何预先存在的条目，因此它们不会收到该事件。
4. M4上的缓存侦听器处理M2上的远程事件的通知。
5. 一旦其他成员上的所有内容都成功完成，M2上的原始创建操作就会成功并调用M2上的缓存侦听器。

![img](assets/Events-2.gif)

**分布式区域中的事件**

分布式操作遵循此序列在分布式区域中：

1. 如果适用，将操作应用于本地缓存。
2. 调用本地侦听器。
3. 做分发。
4. 接收分发的每个成员在响应中执行其自己的操作，其调用任何本地侦听器。

在下图中：

1. 通过成员M1上的直接API调用创建条目。
2. create在M1上调用缓存侦听器。
3. M1将事件分发给其他成员。
4. M2和M3通过自己的本地操作应用远程更改。
5. M3执行创建，但M2执行更新，因为该条目已存在于其缓存中。
6. M2上的缓存侦听器接收本地更新的回调。 由于M3上没有缓存侦听器，因此不会处理来自M3上的create的回调。 成员M1中的API调用创建一个条目。

![img](assets/Events-3.gif)

**管理多线程应用程序中的事件**

对于分区区域，Geode保证跨线程排序事件，但对于分布式区域，它不会。 对于创建分布式区域的多线程应用程序，您需要使用应用程序同步以确保在下一个操作开始之前完成一个操作。 如果将`conserve-sockets`属性设置为true，则通过distributed-no-ack队列进行分发可以使用多个线程。 然后线程共享一个队列，保留分布区域中事件的顺序。 不同的线程可以调用相同的侦听器，因此如果允许不同的线程发送事件，则可能导致侦听器的并发调用。 仅当线程具有某种共享状态时才会出现此问题 - 例如，如果它们正在递增序列号，或者将其事件添加到日志队列中。 然后，您需要使您的代码线程安全。


#### 客户端到服务器事件分发

客户端和服务器根据客户端活动并根据客户端在服务器端缓存更改中注册的兴趣来分发事件。

当客户端更新其缓存时，对客户端区域的更改会自动转发到服务器端。 然后，服务器端更新将传播到已连接的其他客户端并启用订阅。 服务器不会将更新返回给发送客户端。

更新将传递到服务器，然后通过该值传递给已注册对条目key感兴趣的其他每个客户端。 此图显示了如何传播客户端的条目更新。

![img](assets/client_server_event_dist.svg)

该图显示了以下过程：

1. 通过Client1上的直接API调用在区域A中更新或创建条目X.
2. 对该区域的更新将传递到该区域中指定的池。
3. 池将事件传播到缓存服务器，更新区域。
4. 服务器成员将事件分发给其对等体，并将其放入Client2的预订队列中，因为该客户端先前已注册对条目X的兴趣。
5. 条目X的事件从队列发送到Client2。 当这种情况发生时是不确定的。

客户端到服务器分发使用客户端池连接将更新发送到服务器。 具有命名池的任何区域都会自动将更新转发给服务器。 客户端缓存修改首先通过客户端`CacheWriter`（如果已定义），然后通过客户端池传递到服务器，最后传递到客户端缓存本身。 客户端或服务器端的缓存写入器可能会中止操作。

| 更改客户端缓存                              | 对服务器缓存的影响                                       |
| --------------------------------------------------- | ------------------------------------------------------------ |
| 条目创建或更新                              | 创建或更新条目。                                 |
| 分布式条目销毁                           | 条目销毁。 即使条目不在客户端缓存中，destroy调用也会传播到服务器。 |
| 分布式区域销毁/清除（仅分布式） | 区域销毁/清除                                         |

**注意:** 客户端的失效不会转发到服务器。

**服务器到客户端事件分发**

服务器仅针对客户端已注册的key自动发送条目修改事件。 在兴趣注册中，客户端指示是为服务器端条目创建和更新发送新值还是仅发送无效。 如果使用了无效，则客户端会根据需要懒惰地更新值。

此图显示了兴趣注册的完整事件订阅事件分发，请求了值收据（receiveValues = true）且没有。

![img](assets/server_client_event_dist.svg)

| 更改服务器缓存                      | 对客户端缓存的影响                                       |
| ------------------------------------------- | ------------------------------------------------------------ |
| 条目创建/更新                         | 对于`receiveValues`设置为true的订阅，条目创建或更新。对于`receiveValues`设置为false的订阅，如果条目已存在于客户端缓存中，则条目无效; 否则，没有效果。 下一个客户端获取的条目将转发到服务器。 |
| 条目无效/销毁（仅限分布式） | 条目无效/销毁                                     |
| 区域销毁/清除（仅限分布式）     | 区域破坏或局部区域清除                         |

服务器端分布式操作是在服务器或其中一个对等体中作为分布式操作发起的所有操作。 服务器中的区域失效不会转发到客户端。

**注意:** 要在服务器中维护统一的数据集，请不要在服务器区域中执行本地条目失效。

**服务器到客户端的消息跟踪**

服务器使用异步消息传递队列将事件发送到其客户端。 队列中的每个事件都源自客户端，服务器中的线程或服务器或某个其他集群中的应用程序执行的操作。 事件消息具有唯一标识符，该标识符由始发线程的ID与其成员的分布式系统成员ID以及操作的顺序ID组成。 因此，源自任何单个线程的事件消息可以按时间从最低序列ID到最高序列进行分组和排序。 服务器和客户端跟踪每个成员线程ID的最高顺序ID。

单个客户端线程接收并处理来自服务器的消息，跟踪收到的消息以确保它不处理重复发送。 它使用来自原始线程的进程ID来完成此操作。

![img](assets/client_server_message_tracking.svg)

客户端的消息跟踪列表保存为每个始发线程接收的任何消息的最高序列ID。 在有许多不同线程进出并在缓存上工作的系统中，该列表可能变得非常大。 线程死亡后，不需要跟踪条目。 为了避免维护已经死亡的线程的跟踪信息，客户端会使没有活动的条目超过`subscription-message-tracking-timeout`。

**在服务器上注册客户端兴趣**

系统按照以下步骤处理客户兴趣注册：

1. 客户区域中可能受此注册影响的条目将被静默销毁。 其他key是独自留下的。
   - 对于`registerInterest`方法，系统会销毁所有指定的key，只留下客户区域中的其他key。 因此，如果您有一个带有键A，B和C的客户区，并且您在`registerInterest`操作开始时注册了对键列表A，B的兴趣，系统会破坏客户端缓存中的键A和B，但是 不要碰键C.
   - 对于`registerInterestRegex`方法，系统以静默方式销毁客户区域中的所有key。
2. 兴趣规范被发送到服务器，并将其添加到客户端的兴趣列表中。 该列表可以指定在注册兴趣时不在服务器区域中的条目。
3. 如果在调用的`InterestResultPolicy`参数中请求批量加载，则在将控制返回到调用方法之前，服务器将发送当前满足兴趣规范的所有数据。 使用下载的数据自动更新客户端的区域。 如果服务器区域已分区，则整个分区区域将用于批量加载。 否则，仅使用服务器的本地缓存区域。 兴趣结果政策选项包括：
   - KEYS—客户端接收与兴趣注册标准匹配的所有可用key的批量加载。
   - KEYS_VALUES—客户端接收所有可用key的批量加载和与兴趣注册标准匹配的值。 这是默认的兴趣结果政策。
   - NONE—客户端没有立即收到批量加载。

一旦注册了兴趣，服务器就会持续监控区域活动并将事件发送给符合兴趣的客户。

- 注册器兴趣调用不会生成任何事件，即使它们将值加载到客户端缓存中也是如此。
- 服务器维护所有兴趣注册的联合，因此如果客户注册对密钥'A'的兴趣，然后注册对正则表达式“B*”的兴趣，服务器将发送带有key'A'或以字母'B'开头key的所有条目的更新 。
- 服务器将兴趣注册列表与区域分开。 该列表可以包含当前不在服务器区域中的条目的规范。
- `registerInterestRegex`方法使用标准的`java.util.regex`方法来解析key规范。

**服务器故障转移**

当托管订阅队列的服务器失败时，排队职责将传递给另一台服务器。 如何发生这取决于新服务器是否是辅助服务器。 在任何情况下，所有故障转移活动都由Geode系统自动执行。

- **非HA故障转移:** 如果未配置冗余，或者在初始化新辅助节点之前所有辅助节点都失败，则客户端在没有高可用性的情况下进行故障转移。只要它可以连接到服务器，客户端就会进行自动重新初始化过程。在此过程中，客户端上的故障转移代码以静默方式销毁客户端感兴趣的所有条目，并从新服务器中重新获取它们，实质上是从新服务器的缓存中重新初始化客户端缓存。对于notify all配置，这将清除并重新加载连接到服务器的客户端区域的所有条目。对于按订阅通知，它仅清除并重新加载区域兴趣列表中的条目。为了减少故障转移噪声，由本地条目销毁和重新获取引起的事件被故障转移代码阻止，并且不会到达客户端缓存侦听器。因此，您的客户端可能会在服务器故障转移期间和之后收到一些无序事件。例如，故障转移期间，故障服务器上存在而不是其替换的条目将被销毁，并且永远不会重新创建。由于销毁事件被阻止，因此客户端最终会从其缓存中删除条目而没有关联的销毁事件。

- **HA 故障转移:** 如果客户端池配置了冗余，并且主服务器出现故障时辅助服务器可用，则客户端将无法访问故障转移。 一旦检测到主要丢失，辅助服务器就会恢复排队活动。 辅助节点可能会重新发送一些事件，这些事件会被客户端邮件跟踪活动自动丢弃。

  **注意:** 在HA服务器故障转移期间，消息丢失的可能性非常小。 故障转移到已完全初始化其订阅队列数据的辅助节点的风险不存在。 在使用至少两台辅助服务器的健康系统中，风险极低。 在服务器经常出现故障的不稳定系统中，以及辅助设备在成为初选之前没有时间初始化其订阅队列数据的风险更高。 为了最小化风险，故障转移逻辑选择寿命最长的辅助节点作为新的主节点。

  **注意:** 冗余管理由客户端处理，因此当持久客户端与服务器断开连接时，不会维护客户端事件冗余。 即使服务器一次失败一个，以便运行的客户端有时间进行故障转移并选择新的辅助服务器，脱机持久客户端也无法进行故障转移。 结果，客户端丢失其排队的消息。


#### 多站点（WAN）事件分发

Geode在集群之间分配缓存事件的子集，对每个系统的性能影响最小。 仅为您配置为使用网关发件人进行分发的区域分发事件。

**为分发排队事件**

在使用一个或多个网关发件人（`gateway-sender-ids`属性）配置的区域中，事件会自动添加到网关发件人队列以分发到其他站点。 放置在网关发送方队列中的事件将异步分发到远程站点。 对于串行网关队列，可以使用`order-policy`属性保留站点之间发送的事件的顺序。

如果队列变得太满，它会溢出到磁盘以防止成员内存不足。 您可以选择将队列配置为持久保存到磁盘（使用`enable-persistence` `gateway-sender`属性）。 使用持久性，如果管理队列的成员发生故障，成员将在重新启动后从中断处继续。

**来自网关发件人的操作分配**

多站点安装旨在最大限度地降低对群集性能的影响，因此只有最远的入口操作才会在站点之间分配。

这些操作分布在：

- 条目创建
- 条目放置
- 条目分布式销毁，提供的操作不是到期操作

这些操作不分发：

- 获取
- 作废
- 当地销毁
- 任何类型的到期行为
- 区域操作

**网关发件人如何处理其队列**

每个主网关发送器都包含一个处理器线程，该线程从队列中读取消息，对其进行批处理，并将批处理分发到远程站点中的网关接收器。 要处理队列，网关发送方线程将执行以下操作：

1. 从队列中读取消息
2. 创建一批消息
3. 同步将批处理分发到其他站点并等待回复
4. 在另一个站点成功回复后，从队列中删除批处理

因为在其他站点回复之前，批处理不会从队列中删除，所以该消息不会丢失。 另一方面，在此模式下，可以多次处理消息。 如果站点在处理一批消息的过程中脱机，则一旦站点重新联机，将再次发送该批次。

您可以配置消息的批处理大小以及批处理时间间隔设置。 当达到批量大小或时间间隔时，网关发件人处理来自队列的一批消息。 在活动网络中，很可能在时间间隔之前达到批量大小。 在空闲网络中，最有可能在批量大小之前达到时间间隔。 这可能导致一些与时间间隔相对应的网络延迟。

**网关发件人如何处理批处理失败**

批处理期间的不同点可能会发生异常：

- 网关接收器可能会因确认而失败。 如果在网关接收器处理批处理时处理失败，则接收方将回复包含异常的失败确认，包括失败消息的标识以及成功处理的最后一条消息的ID。 然后，网关发件人从队列中删除成功处理的消息和失败的消息，并使用失败的消息信息记录异常。 然后，发送方继续处理队列中剩余的消息。
- 网关接收器可能无法确认而失败。 如果网关接收方未确认已发送的批次，则网关发件人不知道哪些消息已成功处理。 在这种情况下，网关发件人重新发送整批。
- 没有网关接收器可用于处理。 如果由于没有可用的远程网关接收器而发生批处理异常，则批处理仍保留在队列中。 网关发件人等待一段时间，然后尝试重新发送批次。 尝试之间的时间间隔为五秒。 现有服务器监视器不断尝试连接到网关接收器，以便可以建立连接并继续进行队列处理。 消息在队列中累积，并可能在等待连接时溢出到磁盘。


#### 事件处理程序和事件列表

Geode提供了许多类型的事件和事件处理程序，可帮助您管理不同的数据和应用程序需求。

**事件处理程序**

在任何单个应用程序中使用缓存处理程序或成员资格处理程序。 不要同时使用两者。 除非另有说明，否则此表中的事件处理程序是缓存处理程序。

| 处理程序API                          | 收到的事件                                       | 描述                                                  |
| ------------------------------------ | ----------------------------------------------------- | ------------------------------------------------------------ |
| `AsyncEventListener`                 | `AsyncEvent`                                          | 跟踪区域中的更改以进行后写处理。 扩展`CacheCallback`接口。 您将回写缓存侦听器安装到`AsyncEventQueue`实例。 然后，您可以将“AsyncEventQueue”实例添加到一个或多个区域以进行后写处理。 请参阅[实现用于后写高速缓存事件处理的AsyncEventListener](https://geode.apache.org/docs/guide/17/developing/events/implementing_write_behind_event_handler.html#implementing_write_behind_cache_event_handling)。|
| `CacheCallback`                      |                                                       | 所有缓存事件侦听器的超级接口。 仅用于清除回调分配的资源的函数。 |
| `CacheListener`                      | `RegionEvent`, `EntryEvent`                           | 跟踪区域及其数据条目的更改。 同步响应。 扩展`CacheCallback`接口。 安装在地区。 仅接收本地缓存事件。 在您希望此侦听器处理事件的每个成员中安装一个。 在分区区域中，缓存侦听器仅在主数据存储中触发。 辅助听众不会被解雇。|
| `CacheWriter`                        | `RegionEvent`, `EntryEvent`                           | 接收该区域及其成员或其中一个对等方中的区域及其数据条目的*待定*更改事件。 是否有能力中止有问题的操作。 扩展`CacheCallback`接口。 安装在地区。 从分布式区域中的任何位置接收事件，因此您可以为整个分布式区域安装一个成员。 仅在分区区域中的主数据存储中接收事件，因此在每个数据存储中安装一个事件。 |
| `ClientMembershipListener`           | `ClientMembershipEvent`                               | 替换已弃用的Admin API的接口之一。 您可以使用ClientMembershipListener仅接收有关客户端的成员资格事件。 当此进程检测到客户端的连接更改时，将调用此侦听器的回调方法。 回调方法包括`memberCrashed`，`memberJoined`，`memberLeft`（graceful exit）。 |
| `CqListener`                         | `CqEvent`                                             | 从服务器缓存接收满足客户端指定查询的事件。 扩展`CacheCallback`接口。 安装在`CqQuery`里面的客户端。 |
| `GatewayConflictResolver`            | `TimestampedEntryEvent`                               | 决定是否将可能存在冲突的事件应用于通过WAN配置分发的区域。 仅当更新事件的分布式系统ID与上次更新区域条目的ID不同时，才会调用此事件处理程序。 |
| `MembershipListener`                 | `MembershipEvent`                                     | 使用此接口仅接收有关对等方的成员资格事件。 当对等成员加入或离开集群时，将调用此侦听器的回调方法。 回调方法包括`memberCrashed`，`memberJoined`和`memberLeft`（graceful exit）。 |
| `RegionMembershipListener`           | `RegionEvent`                                         | 当在另一个成员中创建了具有相同名称的区域以及托管该区域的其他成员加入或离开集群时，提供事件后通知。 扩展`CacheCallback`和`CacheListener`。 作为`CacheListener`安装在区域中。 |
| `TransactionListener`                | `TransactionEvent` with embedded list of `EntryEvent` | 跟踪事务的结果和事务中数据条目的更改。**注意:**同一缓存上的多个事务可能导致并发调用`TransactionListener`方法，因此实现为多个线程进行适当同步以进行线程安全操作的方法。扩展`CacheCallback`接口。 使用事务管理器安装在缓存中。 如果需要，可以使用区域级侦听器。 |
| `TransactionWriter`                  | `TransactionEvent` with embedded list of `EntryEvent` | 接收* pending *事务提交的事件。 有能力中止交易。 扩展`CacheCallback`接口。 使用事务管理器安装在缓存中。 每个事务最多调用一个编写器。 在每个事务主机中安装writer。 |
| `UniversalMembershipListenerAdapter` | `MembershipEvent` and `ClientMembershipEvent`         | 替换已弃用的Admin API的接口之一。 为客户端和对等端的MembershipListener和ClientMembershipListener回调提供包装器。 |

**事件**

除非另有说明，否则此表中的事件是缓存事件。

| 事件                               | 传递给处理程序 …                                          | 描述                                                  |
| ----------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `AsyncEvent`                        | `AsyncEventListener`                                         | 提供有关异步，后写处理的高速缓存中单个事件的信息。 |
| `CacheEvent`                        |                                                              | 超级接口到`RegionEvent`和`EntryEvent`。 这定义了常见的事件方法，并包含诊断事件环境所需的数据，包括正在执行的操作的描述，有关事件源自何处的信息，以及传递给生成此事件的方法的任何回调参数。 |
| `ClientMembershipEvent`             | `ClientMembershipListener`                                   | 当此进程检测到对服务器或客户端的连接更改时，会将事件传递给`ClientMembershipListener`。 |
| `CqEvent`                           | `CqListener`                                                 | 提供有关代表客户端在服务器上运行的连续查询结果的更改的信息。 `CqEvent`s在客户端上处理。 |
| `EntryEvent`                        | `CacheListener`, `CacheWriter`, `TransactionListener`(inside the `TransactionEvent`) | 为入口事件扩展`CacheEvent`。 包含有关影响缓存中数据条目的事件的信息。 信息包括key，此事件之前的值以及此事件之后的值。 `EntryEvent.getNewValue`返回数据条目的当前值。 `EntryEvent.getOldValue`返回此事件之前的值（如果可用）。 对于分区区域，如果本地缓存包含条目的主副本，则返回旧值。 `EntryEvent`提供Geode事务ID（如果可用）。您可以使用`getSerialized` *方法从`EntryEvent`检索序列化值。 如果从一个区域的事件中获取值只是为了将它们放入单独的缓存区域，这将非常有用。 没有对应的`put`函数，因为put识别该值被序列化并绕过序列化步骤。 |
| `MembershipEvent`(membership event) | `MembershipListener`                                         | 描述发起此事件的成员的事件。 当成员加入或离开集群时，会将此实例传递给`MembershipListener`。 |
| `RegionEvent`                       | `CacheListener`, `CacheWriter`, `RegionMembershipListener`   | 为区域事件扩展`CacheEvent`。 提供有关影响整个区域的操作的信息，例如在销毁后重新初始化该区域。 |
| `TimestampedEntryEvent`             | `GatewayConflictResolver`                                    | 扩展`EntryEvent`以包括与事件关联的时间戳和分布式系统ID。 冲突解决程序可以将事件中的时间戳和ID与存储在条目中的值进行比较，以确定本地系统是否应该应用潜在冲突的事件。 |
| `TransactionEvent`                  | `TransactionListener`, `TransactionWriter`                   | 描述事务中完成的工作。 此事件可能用于挂起或已提交的事务，也可能用于显式回滚或失败提交放弃的工作。 该工作由`EntryEvent`实例的有序列表表示。 条目事件按事务中执行操作的顺序列出。在执行事务操作时，条目事件被混合，每个条目的最后一个事件仅保留在列表中。 因此，如果修改了条目A，然后是条目B，那么条目A，该列表将包含条目B的事件，后面是条目A的第二个事件。 |


### 实现Geode事件处理程序

您可以为区域和区域条目操作以及管理事件指定事件处理程序。

- **实现缓存事件处理程序**

  根据您的安装和配置，缓存事件可以来自本地操作，对等方，服务器和远程站点。 事件处理程序在一个或多个事件中注册其兴趣，并在事件发生时得到通知。

- **为Write-Behind Cache事件处理实现AsyncEventListener**

  `AsyncEventListener`”在将批量事件应用于区域后异步处理这些事件。 您可以使用`AsyncEventListener`实现作为后写缓存事件处理程序，以将区域更新与数据库同步。

- **如何从事件处理程序回调安全地修改缓存**

  事件处理程序是同步的。 如果需要更改缓存或从事件处理程序回调执行任何其他分布式操作，请小心避免可能阻止并影响整体系统性能的活动。

- **缓存事件处理程序示例**

  缓存事件处理程序的一些示例。


#### 实现缓存事件处理程序

根据您的安装和配置，缓存事件可以来自本地操作，对等方，服务器和远程站点。 事件处理程序在一个或多个事件中注册其兴趣，并在事件发生时得到通知。

对于每种类型的处理程序，Geode为接口回调方法提供了一个带有空存根的便捷类。

**注意:** 通过扩展`AsyncEventListener`接口创建后写高速缓存侦听器，并且它们配置有您分配给一个或多个区域的`AsyncEventQueue`。

**步骤**

1. 确定应用程序需要处理哪些事件。 对于每个区域，确定要处理的事件。 对于缓存，决定是否处理事务事件。

2. 对于每个事件，决定使用哪些处理程序。 `org.apache.geode.cache.util`中的`*Listener`和`*Adapter`类显示选项。

3. 编程每个事件处理程序:

   1. 扩展处理程序的适配器类。

   2. 如果要在`cache.xml`中声明处理程序，也要实现`org.apache.geode.cache.Declarable`接口。

   3. 根据应用程序的需要实现处理程序的回调方法。

      **注意:** 编程不正确的事件处理程序可能会阻止您的分布式系统。 缓存事件是同步的。 要根据事件修改缓存或执行分布式操作，请遵循[如何从事件处理程序回调安全地修改缓存](https://geode.apache.org/docs/guide/17/developing/events/writing_callbacks_that_modify_the_cache.html#writing_callbacks_that_modify_the_cache)中的准则来避免阻塞系统.

      例子:

      ```java
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

4. 通过API或`cache.xml`安装事件处理程序。

   XML Region事件处理程序安装：

   ```xml
   <region name="trades">
     <region-attributes ... >
       <!-- Cache listener -->
       <cache-listener>
         <class-name>myPackage.MyCacheListener</class-name>
       <cache-listener>
     </region-attributes>
   </region>
   ```

   Java Region事件处理程序安装：

   ```java
   tradesRegion = cache.createRegionFactory(RegionShortcut.PARTITION)
     .addCacheListener(new MyCacheListener())
     .create("trades");
   ```

   XML事务编写器和监听器安装：

   ```xml
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

启动成员时，在区域创建期间会自动初始化事件处理程序。

**在区域上安装多个侦听器**

XML:

```xml
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


#### 为Write-Behind Cache事件处理实现AsyncEventListener

`AsyncEventListener`在将批量事件应用于区域后异步处理这些事件。 您可以使用`AsyncEventListener`实现作为后写缓存事件处理程序，以将区域更新与数据库同步。

**AsyncEventListener的工作原理**

`AsyncEventListener`实例由其自己的专用线程提供服务，其中调用了一个回调方法。 更新区域的事件放在内部的`AsyncEventQueue`中，并且一个或多个线程一次将一批事件分派给侦听器实现。

您可以将`AsyncEventQueue`配置为串行或并行。 串行队列部署到一个Geode成员，它按发生顺序将所有区域事件传递给已配置的`AsyncEventListener`实现。 并行队列部署到多个Geode成员，并且队列的每个实例都可以同时将区域事件传递给本地的`AsyncEventListener`实现。

虽然并行队列为写入事件提供了最佳吞吐量，但它对订购这些事件提供的控制较少。 使用并行队列时，您无法保留整个区域的事件排序，因为多个Geode服务器会同时排队并传递区域的事件。 但是，可以保留给定分区（或分布式区域的给定队列）的事件顺序。

对于串行和并行队列，您可以控制每个队列使用的最大内存量，以及处理队列中批次的批量大小和频率。 您还可以将队列配置为持久保存到磁盘（而不是简单地溢出到磁盘），以便后续缓存可以在成员关闭并稍后重新启动时从中断处获取。

（可选）队列可以使用多个线程来分派排队事件。 为串行队列配置多个线程时，Geode成员上托管的逻辑队列将分为多个物理队列，每个队列都有一个专用的调度程序线程。 然后，您可以配置线程是按键，按线程还是按照将事件添加到队列的相同顺序来调度排队事件。 为并行队列配置多个线程时，托管在Geode成员上的每个队列都由调度程序线程处理; 创建的队列总数取决于托管该区域的成员数。

可以在`AsyncEventQueue`上放置一个`GatewayEventFilter`来控制是否将特定事件发送到选定的`AsyncEventListener`。 例如，可以检测与敏感数据相关联的事件而不排队。 有关更多详细信息，请参阅`GatewayEventFilter`的Javadoc。

`GatewayEventSubstitutionFilter`可以指定事件是完整传输还是以更改的表示形式传输。 例如，要减小要序列化的数据的大小，仅通过其键表示完整对象可能更有效。 有关更多详细信息，请参阅`GatewayEventSubstitutionFilter`的Javadoc。

**来自AsyncEventQueue的分布式操作**

`AsyncEventQueue`分发这些操作：

- Entry 创建
- Entry 放置
- Entry 分布式销毁，提供的操作不是到期操作
- 如果`forward-expiration-destroy`属性设置为'true`，则到期销毁。 默认情况下，此属性为`false`，但您可以使用`cache.xml`或`gfsh`将其设置为`true`。 要在Java API中设置此属性，请使用`AsyncEventQueueFactory.setForwardExpirationDestroy()`。 有关详细信息，请参阅javadocs。

这些操作不是分布式的：

- 获取
- 失效
- 本地销毁
- 区域操作
- 过期操作
- 如果`forward-expiration-destroy`属性设置为`false`，则到期销毁。 默认值为`false`。

**使用AsyncEventListener的准则**

在使用AsyncEventListener之前，请查看以下准则：

- 如果使用`AsyncEventListener`来实现后写缓存侦听器，则代码应检查由于先前的异常而可能已关闭现有数据库连接的可能性。 例如，在catch块中检查`Connection.isClosed()`并在执行进一步操作之前根据需要重新创建连接。
- 如果在向侦听器实现传递事件时需要保留线程中区域事件的顺序，请使用序列`AsyncEventQueue`。 当线程内的事件顺序不重要时，以及处理事件时需要最大吞吐量时，请使用并行队列。 在串行和并行两种情况下，给定键的操作顺序都保留在线程的范围内。
- 您必须在承载要处理其事件的区域的Geode成员上安装`AsyncEventListener`实现。
- 如果配置并行`AsyncEventQueue`，请在承载该区域的每个Geode成员上部署队列。
- 如果具有活动`AsyncEventListener`的成员关闭，您可以在多个成员上安装侦听器以提供高可用性并保证事件的传递。 在任何给定时间，只有一个成员具有用于调度事件的主动侦听器。 其他成员的监听器仍处于备用状态以实现冗余。 为了获得最佳性能和最有效的内存使用，请仅安装一个备用侦听器（最多一个冗余）。
- 出于性能和内存原因，安装不超过一个备用侦听器（最多一个冗余）。
- 要通过成员关闭保留挂起事件，请将Geode配置为将`AsyncEventListener`的内部队列持久保存到可用磁盘存储中。 默认情况下，如果活动侦听器的成员关闭，则驻留在`AsyncEventListener`的内部队列中的任何挂起事件都将丢失。
- 要确保事件的高可用性和可靠传递，请将事件队列配置为持久和冗余。

**实现AsyncEventListener**

要接收要处理的区域事件，可以创建一个实现`AsyncEventListener`接口的类。 侦听器中的`processEvents`方法接收每个批处理中排队的`AsyncEvent`对象的列表。

每个`AsyncEvent`对象都包含有关区域事件的信息，例如事件发生的区域的名称，区域操作的类型以及受影响的键和值。

实现后写事件处理程序的基本框架包括迭代批处理事件并将每个事件写入数据库。 例如：

```java
class MyAsyncEventListener implements AsyncEventListener {

  public boolean processEvents(List<AsyncEvent> events) {

      // Process each AsyncEvent

      for(AsyncEvent event: events) {

          // Write the event to a database

      }
    }
}
```

**处理AsyncEvents**

使用[AsyncEventListener.processEvents](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/asyncqueue/AsyncEventListener.html)方法处理AsyncEvents。 当事件排队等待处理时，将异步调用此方法。 列表的大小反映了在AsyncEventQueueFactory中定义批量大小的批处理事件的数量。 `processEvents`method返回一个布尔值; 如果正确处理了AsyncEvents，则为true;如果任何事件处理失败，则为false。 只要`processEvents`返回false，Geode就会继续重新尝试处理事件。

您可以使用`getDeserializedValue`方法获取已更新或创建的条目的缓存值。 由于`getDeserializedValue`方法将为已销毁的条目返回null值，因此应使用`getKey`方法获取对已销毁的缓存对象的引用。 这是处理AsyncEvents的示例：

```java
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

**配置AsyncEventListener**

要配置后写高速缓存侦听器，首先要配置异步队列以分派区域事件，然后使用侦听器实现创建队列。 然后，您可以将队列分配给某个区域，以便处理该区域的事件。

**步骤**

1. 使用侦听器实现的名称配置唯一的`AsyncEventQueue`。 您可以选择为并行操作，持久性，批量大小和最大内存大小配置队列。 有关详细信息，请参阅[WAN配置](https://geode.apache.org/docs/guide/17/reference/topics/elements_ref.html#topic_7B1CABCAD056499AA57AF3CFDBF8ABE3) 。

   **gfsh配置**

   ```
   gfsh>create async-event-queue --id=sampleQueue --persistent --disk-store=exampleStore --listener=com.myCompany.MyAsyncEventListener --listener-param=url#jdbc:db2:SAMPLE,username#gfeadmin,password#admin1
   ```

   The parameters for this command uses the following syntax:

   ```
   create async-event-queue --id=value --listener=value [--group=value] [--batch-size=value] 
   [--persistent(=value)?] [--disk-store=value] [--max-queue-memory=value] [--listener-param=value(,value)*]
   ```

   有关更多信息，请参阅[create async-event-queue](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/create.html#topic_ryz_pb1_dk).

   **cache.xml 配置**

   ```xml
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

   **Java 配置**

   ```java
   Cache cache = new CacheFactory().create();
   AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
   factory.setPersistent(true);
   factory.setDiskStoreName("exampleStore");
   factory.setParallel(false);
   AsyncEventListener listener = new MyAsyncEventListener();
   AsyncEventQueue asyncQueue = factory.create("sampleQueue", listener);
   ```

2. 如果您使用并行的`AsyncEventQueue`，则上面的gfsh示例不需要更改，因为gfsh适用于所有成员。 如果使用cache.xml或Java API来配置`AsyncEventQueue`，请在将托管该区域的每个Geode成员中重复上述配置。 为每个队列配置使用相同的ID和配置设置。 **注意:**您可以使用gfsh中提供的群集配置服务确保其他成员使用示例配置。 请参见[群集配置服务概述](https://geode.apache.org/docs/guide/17/configuring/cluster_config/gfsh_persist.html).

3. 在托管`AsyncEventQueue`的每个Geode成员上，将队列分配给要与`AsyncEventListener`实现一起使用的每个区域。

   **gfsh 配置**

   ```
   gfsh>create region --name=Customer --async-event-queue-id=sampleQueue 
   ```

   请注意，您可以在逗号分隔的列表中的命令行上指定多个队列。

   **cache.xml 配置**

   ```
   <cache>
   <region name="Customer">
       <region-attributes async-event-queue-ids="sampleQueue">
       </region-attributes>
     </region>
   ...
   </cache>
   ```

   **Java 配置**

   ```
   RegionFactory rf1 = cache.createRegionFactory();
   rf1.addAsyncEventQueue(sampleQueue);
   Region customer = rf1.create("Customer");
   
   // Assign the queue to multiple regions as needed
   RegionFactory rf2 = cache.createRegionFactory();
   rf2.addAsyncEventQueue(sampleQueue);
   Region order = rf2.create("Order");
   ```

   使用Java API，您还可以向已创建的区域添加和删除队列：

   ```
   AttributesMutator mutator = order.getAttributesMutator();
   mutator.addAsyncEventQueueId("sampleQueue");        
   ```

   有关详细信息，请参阅[Geode API文档](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/AttributesMutator.html)。

4. （可选）为队列配置持久性和混合。**注意:** 如果使用持久数据区域，则必须将AsyncEventQueue配置为持久性。 不支持使用具有持久区域的非持久队列。

5. （可选）使用[配置调度程序线程和事件分发的顺序策略](https://geode.apache.org/docs/guide/17/developing/events/configuring_gateway_concurrency_levels.html)中的说明配置多个调度程序线程和队列的排序策略。

`AsyncEventListener`从配置有关联的`AsyncEventQueue`的每个区域接收事件。


#### 如何从事件处理程序回调安全地修改缓存

事件处理程序是同步的。 如果需要更改缓存或从事件处理程序回调执行任何其他分布式操作，请小心避免可能阻止并影响整体系统性能的活动。

**在事件处理程序中避免的操作**

不要直接从事件处理程序执行任何类型的分布式操作。 Geode是一个高度分布式的系统，许多操作似乎在本地调用分布式操作。

这些是常见的分布式操作，可能会让您陷入麻烦：

- 在事件的区域或任何其他区域调用`Region`方法。
- 使用Geode`DetributedLockService`。
- 修改区域属性。
- 通过Geode`FunctionService`执行一个函数。

为了安全起见，请不要直接从事件处理程序中调用Geode API。 从单独的线程或执行程序中进行所有Geode API调用。

**如何基于事件执行分布式操作**

如果您需要使用处理程序中的Geode API，请使您的工作与事件处理程序异步。 您可以生成一个单独的线程或使用像`java.util.concurrent.Executor`接口这样的解决方案。

此示例显示了一个串行执行程序，其中回调创建一个`Runnable`，可以从队列中拉出并由另一个对象运行。 这保留了事件的顺序。

```java
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

有关`Executor`的其他信息，请参阅Oracle Java Web站点上的`SerialExecutor`示例。


#### 缓存事件处理程序示例

缓存事件处理程序的一些示例。

**使用参数声明和加载事件处理程序**

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

**注意:** You can also configure Regions using the gfsh command-line interface. See [Region Commands](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/quick_ref_commands_by_area.html#topic_EF03119A40EE492984F3B6248596E1DD).



### Configuring Client/Server Event Messaging

You can receive events from your servers for server-side cache events and query result changes.

For cache updates, you can configure to receive entry keys and values or just entry keys, with the data retrieved lazily when requested. The queries are run continuously against server cache events, with the server sending the deltas for your query result sets.

Before you begin, set up your client/server installation and configure and program your basic event messaging.

Servers receive updates for all entry events in their client’s client regions.

To receive entry events in the client from the server:

1. Set the client pool `subscription-enabled` to true. See [](https://geode.apache.org/docs/guide/17/reference/topics/client-cache.html#cc-pool).

2. Program the client to register interest in the entries you need.

   **注意:** This must be done through the API.

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

**注意:** For a single durable client ID, you must maintain the same durability of your registrations and queries between client runs.

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

      **注意:** Registering interest with `InterestResultPolicy.KEYS_VALUES` initializes the client cache with the *current* values of specified keys. If concurrency checking is enabled for the region, any earlier (older) region events that are replayed to the client are ignored and are not sent to configured listeners. If your client must process all replayed events for a region, register with `InterestResultPolicy.KEYS` or `InterestResultPolicy.NONE` when reconnecting. Or, disable concurrency checking for the region in the client cache. See [Consistency for Region Updates](https://geode.apache.org/docs/guide/17/developing/distributed_regions/region_entry_versions.html#topic_CF2798D3E12647F182C2CEC4A46E2045).

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

**注意:** This method of conflation is different from the one used for multi-site gateway sender queue conflation. It is the same as the method used for the conflation of peer-to-peer distribution messages within a single cluster.



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

    **注意:** With this setting, one slow client can slow the server and all of its other clients because this blocks the threads that write to the queues. All operations that add messages to the queue block until the queue size drops to an acceptable level. If the regions feeding these queues are partitioned or have `distributed-ack` or `global` scope, operations on them remain blocked until their event messages can be added to the queue. If you are using this option and see stalling on your server region operations, your queue capacity might be too low for your application behavior.



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

**注意:** If you configure a parallel queue and/or you configure multiple dispatcher threads for a queue, the values that are defined in the `maximum-queue-memory` and `disk-store-name` attributes apply to each instance of the queue.

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

**注意:** Do not use conflation if your receiving applications depend on the specific ordering of entry modifications, or if they need to be notified of every change to an entry.

Conflation is most useful when a single entry is updated frequently, but other sites only need to know the current value of the entry (rather than the value of each update). When an update is added to a queue that has conflation enabled, if there is already an update message in the queue for the entry key, then the existing message assumes the value of the new update and the new update is dropped, as shown here for key A.

![img](assets/MultiSite-4.gif)

**注意:** This method of conflation is different from the one used for server-to-client subscription queue conflation and peer-to-peer distribution within a cluster.

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



## 增量传播

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

**注意:** Only the object itself being placed in the cache can implement the `Delta` interface and propagate changes. Any sub-objects of the cache object do not propagate their changes.

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



## 查询

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

**注意:** You can also perform queries using the gfsh `query` command. See [query](https://geode.apache.org/docs/guide/17/tools_modules/gfsh/command-pages/query.html).



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

**注意:** The ’*’ wildcard is not supported in OQL LIKE predicates.

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

**注意:** If you are using DISTINCT queries, you must implement the equals and hashCode methods for the objects that you query.

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

**注意:** If you are using ORDER BY queries, you must implement the equals and hashCode methods for the objects that you query.

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

**注意:** If you do not specify the type of index in cache.xml, the type defaults to “range”.

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

**注意:** Using a key-index with an explicit type=‘range’ in the cache.xml will lead to an exception. Key indexes will not be used in 'range’ queries.

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

**注意:** If you do not specify the type of index when defining indexes using cache.xml, the type defaults to “range”.



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

**注意:** You can still query against Map or HashMap fields without querying against a map index. For example, you can always create a regular range query on a single key in any Map or HashMap field. However, note that subsequent query lookups will be limited to a single key.



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

   **注意:** Equi-join queries require regular indexes. Key indexes are not applied to equi-join queries.

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



## 连续查询

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

   **注意:** Be especially careful if you choose to update your cache from your `CqListener`. If your listener updates the region that is queried in its own CQ and that region has a `Pool` named, the update will be forwarded to the server. If the update on the server satisfies the same CQ, it may be returned to the same listener that did the update, which could put your application into an infinite loop. This same scenario could be played out with multiple regions and multiple CQs, if the listeners are programmed to update each other’s regions.

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



## 事务

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



## 函数执行

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
  2. For high availability, code `execute` to accommodate multiple identical calls to the function. Use the `RegionFunctionContext` `isPossibleDuplicate` to determine whether the call may be a high-availability re-execution. This boolean is set to true on execution failure and is false otherwise. **注意:** The `isPossibleDuplicate` boolean can be set following a failure from another member’s execution of the function, so it only indicates that the execution might be a repeat run in the current member.
  3. Use the function context to get information about the execution and the data:
     - The context holds the function ID, the `ResultSender` object for passing results back to the originator, and function arguments provided by the member where the function originated.
     - The context provided to the function is the `FunctionContext`, which is automatically extended to `RegionFunctionContext` if you get the `Execution` object through a `FunctionService` `onRegion` call.
     - For data dependent functions, the `RegionFunctionContext` holds the `Region` object, the `Set` of key filters, and a boolean indicating multiple identical calls to the function, for high availability implementations.
     - For partitioned regions, the `PartitionRegionHelper` provides access to additional information and data for the region. For single regions, use `getLocalDataForContext`. For colocated regions, use `getLocalColocatedRegions`. **注意:** When you use `PartitionRegionHelper.getLocalDataForContext`, `putIfAbsent` may not return expected results if you are working on local data set instead of the region.
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

  **注意:** Modifying a function instance after registration has no effect on the registered function. If you want to execute a new function, you must register it with a different identifier.

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
4. If the function returns results, call `getResult` from the results collector returned from `execute`and code your application to do whatever it needs to do with the results. **注意:** For high availability, you must call the `getResult` method.

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



