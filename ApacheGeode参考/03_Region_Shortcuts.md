# Region Shortcuts(区域快捷方式)

[TOC]

本主题介绍可用于配置Geode区域的各种区域快捷方式。

区域快捷方式是定义区域特征的预配置属性的分组。 在配置区域时，可以使用区域快捷方式作为起点，还可以添加其他配置以自定义应用程序。 要在Geode的`cache.xml`文件中引用区域快捷方式，请使用`<region>`元素的`refid`属性。 例如：

```xml
<region name="myRegion" refid="PARTITION_REDUNDANT"/>
```

您可以覆盖默认值，并在`cache.xml`文件的`<region-attributes>`元素内添加其他配置。 例如，以下配置将覆盖local-max-memory设置，并添加recovery-delay属性：

```xml
<region name="myRegion" refid="PARTITION_REDUNDANT">
    <region-attributes>
        <partition-attributes 
          local-max-memory="512" 
           recovery-delay=-1/>
    </region-attributes>
</region>
```

您也可以为常见的自定义配置创建自己的命名区域快捷方式。 请参见[区域快捷方式和自定义命名区域属性](https://geode.apache.org/docs/guide/17/basic_config/data_regions/region_shortcuts.html).

要使用gfsh命令行工具配置区域，请使用`--type`参数指定快捷方式名称。 例如：

```
gfsh>create region --name=myRegion --type=PARTITION_REDUNDANT
```

**注意:** 如果更改了定义区域的cache.xml文件，则必须重新启动成员才能使更改生效。

有关配置区域的更多信息，请参见[区域管理]。(https://geode.apache.org/docs/guide/17/basic_config/data_regions/managing_data_regions.html).

有关使用各种类型的Geode区域以及何时使用它们的更多信息，请参见[区域类型]。(https://geode.apache.org/docs/guide/17/developing/region_options/region_types.html#region_types).

- **区域快捷键快速参考**

  本部分提供所有区域快捷方式的快速参考。

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



## 区域快捷键快速参考

本部分提供所有区域快捷方式的快速参考。

[区域快捷方式默认配置](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_table.html#reference_ufj_5kz_4k__table_lkl_hws_mk)总结了每个区域快捷方式的默认配置。 有关每个快捷方式的其他信息，请参见交叉参考。



| 区域快捷方式                                                 | 区域属性                                                     | 其他属性                                                     |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| [LOCAL](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_w2h_3cd_lk) | 范围(scope): local <br/>数据政策(data-policy): NORMAL                        |                                                              |
| [LOCAL_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wd5_lpy_lk) | 范围(scope): local <br/>数据政策(data-policy): NORMAL                        | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): local-destroy |
| [LOCAL_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_adk_y4y_lk) | 范围(scope): local <br/>数据政策(data-policy): NORMAL                        | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [LOCAL_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_l5r_y4y_lk) | 范围(scope): local <br/>数据政策(data-policy): PERSISTENT_REPLICATE          |                                                              |
| [LOCAL_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_a45_y4y_lk) | 范围(scope): local <br/>数据政策(data-policy): PERSISTENT_REPLICATE          | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [PARTITION](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_ow5_4qy_lk) | 数据政策(data-policy): PARTITION                                       |                                                              |
| [PARTITION_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_twx_y4y_lk) | 数据政策(data-policy): PARTITION                                       | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): local-destroy |
| [PARTITION_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_js1_z4y_lk) | 数据政策(data-policy): PARTITION                                       | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [PARTITION_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_d4k_jpy_lk) | 数据政策(data-policy): PERSISTENT_PARTITION                            |                                                              |
| [PARTITION_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_v5l_jpy_lk) | 数据政策(data-policy): PERSISTENT_PARTITION                            | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [PARTITION_PROXY](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_v4m_jpy_lk) | 数据政策(data-policy): PARTITION                                       | 分区属性<br/><br/>local-max-memory: 0            |
| [PARTITION_PROXY_REDUNDANT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_c1n_jpy_lk) | 数据政策(data-policy): PARTITION                                       | 分区属性<br/><br/>redundant-copies(冗余副本): 1<br/>local-max-memory: 0 |
| [PARTITION_REDUNDANT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_shn_jpy_lk) | 数据政策(data-policy): PARTITION                                       | 分区属性<br/><br/>redundant-copies(冗余副本): 1            |
| [PARTITION_REDUNDANT_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_m4n_jpy_lk) | 数据政策(data-policy): PARTITION                                       | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): local-destroy<br/><br/>分区属性<br/>redundant-copies(冗余副本): 1 |
| [PARTITION_REDUNDANT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_own_jpy_lk) | 数据政策(data-policy): PARTITION                                       | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk<br/><br/>分区属性<br/>redundant-copies(冗余副本): 1 |
| [PARTITION_REDUNDANT_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_bd4_jpy_lk) | 数据政策(data-policy): PERSISTENT_PARTITION                            | 分区属性<br/><br/>redundant-copies(冗余副本): 1            |
| [PARTITION_REDUNDANT_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_xqq_tvc_lk) | 数据政策(data-policy): PERSISTENT_PARTITION                            | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk<br/><br/>分区属性<br/>redundant-copies(冗余副本): 1 |
| [REPLICATE](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wq4_jpy_lk) | 数据政策(data-policy): REPLICATE <br/>范围(scope): distributed-ack           |                                                              |
| [REPLICATE_HEAP_LRU](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_xx4_jpy_lk) | 数据政策(data-policy): PRELOADED <br/><br/>范围(scope): distributed-ack      | 驱逐属性<br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/><br/>age驱逐行动(eviction-action): local-destroy<br/>**Note:**<span style="color:red">Normally, you cannot have a `local-destroy`eviction policy on replicated regions. However, the REPLICATE_HEAP_LRU region is not the same as other replicated regions. This region is preloaded and remains in synch by registering interest in the keys of its peer’s replicated regions.</span><br/><br/>Subscription Attributes<br/>interest-policy: all |
| [REPLICATE_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_t2p_jpy_lk) | 数据政策(data-policy): REPLICATE <br/><br/>范围(scope): distributed-ack      | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [REPLICATE_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_emp_jpy_lk) | 数据政策(data-policy): PERSISTENT_REPLICATE<br/><br/>范围(scope): distributed-ack |                                                              |
| [REPLICATE_PERSISTENT_OVERFLOW](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_tsp_jpy_lk) | 数据政策(data-policy): PERSISTENT_REPLICATE <br/>范围(scope): distributed-ack | 驱逐属性<br/><br/>驱逐算法(eviction-algorithm): lru-heap-percent<br/>age驱逐行动(eviction-action): overflow-to-disk |
| [REPLICATE_PROXY](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_n1q_jpy_lk) | 数据政策(data-policy): EMPTY <br/><br/>范围(scope): distributed-ack          |                                                              |

表1.区域快捷方式默认配置



## LOCAL

### 描述

使用LOCAL区域快捷方式配置的区域仅在其JVM范围内，其他对等成员不可见。 该区域不会将数据和操作分发到其他缓存。

### 默认属性

区域属性

|              |        |
| :----------- | :----- |
| 范围(scope):       | local  |
| 数据政策(data-policy): | NORMAL |

### gfsh命令示例

```
gfsh>create region --name=myLOCALregion --type=LOCAL
gfsh>describe region --name=myLOCALregion
..........................................................
Name            : myLOCALregion
Data Policy     : normal
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

 Type  |    Name     | Value
------ | ----------- | ------
Region | data-policy | NORMAL
       | size        | 0
```

### XML的定义 - LOCAL

```xml
<region name="myLOCALregion">
   <region-attributes scope="local"/>
</region>
```

## LOCAL_HEAP_LRU

### 描述

使用LOCAL_HEAP_LRU区域快捷方式配置的区域在其JVM范围内，其他对等成员不可见。 该区域不会将数据和操作分发到其他缓存。 当该区域检测到JVM的内存不足时，该区域将破坏最近最少使用的条目。

### 默认属性

区域属性

|              |        |
| :----------- | :----- |
| 范围(scope):       | local  |
| 数据政策(data-policy): | NORMAL |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | local-destroy       |

### gfsh命令示例

```
gfsh>create region --name=myLHLregion --type=LOCAL_HEAP_LRU
gfsh>describe region --name=myLHLregion
...........................................................
Name            : myLHLregion
Data Policy     : normal
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | data-policy        | NORMAL
         | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | local-destroy
```

### XML的定义 - LOCAL_HEAP_LRU

```xml
<region name="myLHLregion">
   <region-attributes 
        scope="local">
      <eviction-attributes>
        <lru-heap-percentage 
           action="local-destroy">
          <class-name>
           org.apache.geode.internal.size.SizeClassOnceObjectSizer
          </class-name>
        </lru-heap-percentage>
      </eviction-attributes>
   </region-attributes>
</region>
```

## LOCAL_OVERFLOW

### 描述

使用LOCAL_OVERFLOW区域快捷方式配置的区域仅在其JVM范围内，其他对等成员不可见。 该区域不会将数据和操作分发到其他缓存。 当区域检测到JVM的内存不足时，该区域会将条目的值移至磁盘。

### 默认属性

区域属性

|              |        |
| :----------- | :----- |
| 范围(scope):       | local  |
| 数据政策(data-policy): | NORMAL |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myLOregion --type=LOCAL_OVERFLOW
gfsh>describe region --name=myLOregion
...........................................................
Name            : myLOregion
Data Policy     : normal
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | data-policy        | NORMAL
         | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - LOCAL_OVERFLOW

```
<region name="myLOregion">
    <region-attributes 
        scope="local">
      <eviction-attributes>
        <lru-heap-percentage 
           action="overflow-to-disk"/>
      </eviction-attributes>
    </region-attributes>
</region>
```

## LOCAL_PERSISTENT

### 描述

使用LOCAL_PERSISTENT区域快捷方式配置的区域在其JVM范围内，其他对等成员不可见。 该区域不会将数据和操作分发到其他缓存。 该区域将其状态写入磁盘，并且在成员重新启动时可以恢复该状态。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 范围(scope):       | local                |
| 数据政策(data-policy): | PERSISTENT_REPLICATE |

### gfsh命令示例

```
gfsh>create region --name=myLPregion --type=LOCAL_PERSISTENT
Member  | Status
------- | -----------------------------------------
server2 | Region "/myLPregion" created on "server2"
server1 | Region "/myLPregion" created on "server1"

gfsh>describe region --name=myLPregion
..........................................................
Name            : myLPregion
Data Policy     : persistent replicate
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

 Type  | Name | Value
------ | ---- | -----
Region | size | 0
```

### XML的定义 - LOCAL_PERSISTENT

```xml
<region name="myLPregion">
    <region-attributes 
      scope="local" 
      data-policy="persistent-replicate"/>
</region>
```

## LOCAL_PERSISTENT_OVERFLOW

### 描述

使用LOCAL_PERSISTENT_OVERFLOW区域快捷方式配置的区域在其JVM范围内，其他对等成员不可见。 该区域不会将数据和操作分发到其他缓存。 该区域将其状态写入磁盘，并且在成员重新启动时可以恢复该状态。 当区域检测到JVM的内存不足时，该区域还会从内存中删除条目的值。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 范围(scope):       | local                |
| 数据政策(data-policy): | PERSISTENT_REPLICATE |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myLPOregion --type=LOCAL_PERSISTENT_OVERFLOW
Member  | Status
------- | ------------------------------------------
server1 | Region "/myLPOregion" created on "server1"
server2 | Region "/myLPOregion" created on "server2"

gfsh>describe region --name=myLPOregion
...........................................................
Name            : myLPOregion
Data Policy     : persistent replicate
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - LOCAL_PERSISTENT_OVERFLOW

```xml
<region name="myLPOregion">
   <region-attributes 
        scope="local" 
        data-policy="persistent-replicate">
      <eviction-attributes>
        <lru-heap-percentage 
          action="overflow-to-disk"/>
      </eviction-attributes>
   </region-attributes>
</region>
```

## PARTITION

### 描述

使用PARTITION区域快捷方式配置的区域在创建该区域的每个对等成员之间进行分区。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

### gfsh命令示例

```
gfsh>create region --name=myPregion --type=PARTITION
Member  | Status
------- | ----------------------------------------
server1 | Region "/myPregion" created on "server1"
server2 | Region "/myPregion" created on "server2"

gfsh>describe region --name=myPregion
..........................................................
Name            : myPregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

 Type  | Name | Value
------ | ---- | -----
Region | size | 0
```

### XML的定义 - PARTITION

```xml
<region name="myPregion">
    <region-attributes 
      data-policy="partition"/>
  </region>
```

## PARTITION_HEAP_LRU

### 描述

使用PARTITION_HEAP_LRU配置的区域在创建该区域的每个对等成员之间进行分区。 当该区域检测到JVM的内存不足时，该区域将破坏条目。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | local-destroy       |

### gfsh命令示例

```
gfsh>create region --name=myPHLregion --type=PARTITION_HEAP_LRU
Member  | Status
------- | ------------------------------------------
server1 | Region "/myPHLregion" created on "server1"
server2 | Region "/myPHLregion" created on "server2"

gfsh>describe region --name=myPHLregion
...........................................................
Name            : myPHLregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | local-destroy
```

### XML的定义 - PARTITION_HEAP_LRU

```xml
<region name="myPHLregion">
  <region-attributes 
      data-policy="partition">
      <eviction-attributes>
        <lru-heap-percentage 
            action="local-destroy">
            <class-name>org.apache.geode.internal.size.SizeClassOnceObjectSizer</class-name>
        </lru-heap-percentage>
      </eviction-attributes>
  </region-attributes>
</region>
```

## PARTITION_OVERFLOW

### 描述

使用PARTITION_OVERFLOW区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 当区域检测到JVM的内存不足时，该区域会将条目的值移至磁盘。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myPOregion --type=PARTITION_OVERFLOW
Member  | Status
------- | -----------------------------------------
server1 | Region "/myPOregion" created on "server1"
server2 | Region "/myPOregion" created on "server2"

gfsh>describe region --name=myPOregion
...........................................................
Name            : myPOregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - PARTITION_OVERFLOW

```xml
<region name="myPOregion">
    <region-attributes 
        data-policy="partition">
        <eviction-attributes>
           <lru-heap-percentage 
             action="overflow-to-disk"/>
        </eviction-attributes>
    </region-attributes>
</region>
```

## PARTITION_PERSISTENT

### 描述

使用PARTITION_PERSISTENT区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 该区域将其状态写入磁盘，并且在成员重新启动时可以恢复该状态。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 数据政策(data-policy): | PERSISTENT_PARTITION |

### gfsh命令示例

```
gfsh>create region --name=myPPregion --type=PARTITION_PERSISTENT
Member  | Status
------- | -----------------------------------------
server1 | Region "/myPPregion" created on "server1"
server2 | Region "/myPPregion" created on "server2"

gfsh>describe region --name=myPPregion
..........................................................
Name            : myPPregion
Data Policy     : persistent partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

 Type  | Name | Value
------ | ---- | -----
Region | size | 0
```

### XML的定义 - PARTITION_PERSISTENT

```xml
<region name="myPPregion">
    <region-attributes 
     data-policy="persistent-partition"/>
</region>
```

## PARTITION_PERSISTENT_OVERFLOW

### 描述

使用PARTITION_PERSISTENT_OVERFLOW区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 该区域将其状态写入磁盘，并且在成员重新启动时可以恢复该状态。 当区域检测到JVM的内存不足时，该区域将从内存中删除条目的值。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 数据政策(data-policy): | PERSISTENT_PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myPPOregion --type=PARTITION_PERSISTENT_OVERFLOW
Member  | Status
------- | ------------------------------------------
server2 | Region "/myPPOregion" created on "server2"
server1 | Region "/myPPOregion" created on "server1"

gfsh>describe region --name=myPPOregion
...........................................................
Name            : myPPOregion
Data Policy     : persistent partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - PARTITION_PERSISTENT_OVERFLOW

```xml
<region name="myPPOregion">
   <region-attributes 
        data-policy="persistent-partition">
        <eviction-attributes>
          <lru-heap-percentage 
             action="overflow-to-disk"/>
        </eviction-attributes>
   </region-attributes>
</region>
```

## PARTITION_PROXY

### 描述

使用PARTITION_PROXY区域快捷方式配置的区域没有本地状态，并将所有操作转发到使用PARTITION快捷方式配置的对等区域或使用PARTITION_PERSISTENT快捷方式配置的对等区域。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

分区属性

|                   |      |
| :---------------- | :--- |
| local-max-memory: | 0    |

### gfsh命令示例

```
gfsh>create region --name=myPPXYregion --type=PARTITION_PROXY
Member  | Status
------- | -------------------------------------------
server1 | Region "/myPPXYregion" created on "server1"
server2 | Region "/myPPXYregion" created on "server2"

gfsh>describe region --name=myPPXYregion
...........................................................
Name             : myPPXYregion
Data Policy      : partition
Accessor Members : server1
                   server2

Non-Default Attributes Shared By Accessor Members  

  Type    |       Name       | Value
--------- | ---------------- | -----
Region    | size             | 0
Partition | local-max-memory | 0
```

### XML的定义 - PARTITION_PROXY

```xml
<region name="myPPXYregion">
    <region-attributes 
        data-policy="partition">
        <partition-attributes 
           local-max-memory="0"/>
    </region-attributes>
</region>
```

## PARTITION_PROXY_REDUNDANT

### 描述

使用PARTITION_PROXY_REDUNDANT区域快捷方式配置的区域没有本地状态，并将所有操作转发到使用PARTITION_REDUNDANT快捷方式配置的对等区域或使用PARTITION_REDUNDANT_PERSISTENT快捷方式配置的对等区域。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |
| local-max-memory  | 0    |

### gfsh命令示例

```
gfsh>create region --name=myPPRregion --type=PARTITION_PROXY_REDUNDANT
Member  | Status
------- | ------------------------------------------
server1 | Region "/myPPRregion" created on "server1"
server2 | Region "/myPPRregion" created on "server2"

gfsh>describe region --name=myPPRregion
...........................................................
Name             : myPPRregion
Data Policy      : partition
Accessor Members : server1
                   server2

Non-Default Attributes Shared By Accessor Members  

  Type    |       Name       | Value
--------- | ---------------- | -----
Region    | size             | 0
Partition | redundant-copies | 1
          | local-max-memory | 0
```

### XML的定义 - PARTITION_PROXY_REDUNDANT

```xml
<region name="myPPRregion">
    <region-attributes 
       data-policy="partition">
       <partition-attributes 
        redundant-copies="1" 
        local-max-memory="0"/>
    </region-attributes>
</region>
```

## PARTITION_REDUNDANT

### 描述

使用PARTITION_REDUNDANT区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 该区域在内存中维护数据的额外副本。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |

### gfsh命令示例

```
gfsh>create region --name=myPRregion --type=PARTITION_REDUNDANT
Member  | Status
------- | -----------------------------------------
server1 | Region "/myPRregion" created on "server1"
server2 | Region "/myPRregion" created on "server2"

gfsh>describe region --name=myPRregion
..........................................................
Name            : myPRregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type    |       Name       | Value
--------- | ---------------- | -----
Region    | size             | 0
Partition | redundant-copies | 1
```

### XML的定义 - PARTITION_REDUNDANT

```xml
<region name="myPRregion">
   <region-attributes 
       data-policy="partition">
       <partition-attributes 
          redundant-copies="1"/>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_HEAP_LRU

### 描述

使用PARTITION_REDUNDANT_HEAP_LRU区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 该区域将额外的数据副本保存在内存中。 当该区域检测到JVM的内存不足时，该区域将破坏条目。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | local-destroy       |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |

### gfsh命令示例

```
gfsh>create region --name=myPRHLregion --type=PARTITION_REDUNDANT_HEAP_LRU
Member  | Status
------- | -------------------------------------------
server1 | Region "/myPRHLregion" created on "server1"
server2 | Region "/myPRHLregion" created on "server2"

gfsh>describe region --name=myPRHLregion
...........................................................
Name            : myPRHLregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type    |        Name        | Value
--------- | ------------------ | -------------------
Region    | size               | 0
Eviction  | eviction-algorithm | lru-heap-percentage
          | eviction-action    | local-destroy
Partition | redundant-copies   | 1
```

### XML的定义 - PARTITION_REDUNDANT_HEAP_LRU

```xml
<region name="myPRHLregion">
    <region-attributes 
        data-policy="partition">
        <partition-attributes redundant-copies="1"/>
        <eviction-attributes>
          <lru-heap-percentage action="local-destroy">
           <class-name>
            org.apache.geode.internal.size.SizeClassOnceObjectSizer
           </class-name>
          </lru-heap-percentage>
      </eviction-attributes>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_OVERFLOW

### 描述

使用PARTITION_REDUNDANT_OVERFLOW区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 该区域将额外的数据副本保存在内存中。 当区域检测到JVM的内存不足时，该区域会将条目的值移至磁盘。

### 默认属性

区域属性

|              |           |
| :----------- | :-------- |
| 数据政策(data-policy): | PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |

### gfsh命令示例

```
gfsh>create region --name=myPROregion --type=PARTITION_REDUNDANT_OVERFLOW
Member  | Status
------- | ------------------------------------------
server2 | Region "/myPROregion" created on "server2"
server1 | Region "/myPROregion" created on "server1"

gfsh>describe region --name=myPROregion
...........................................................
Name            : myPROregion
Data Policy     : partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type    |        Name        | Value
--------- | ------------------ | -------------------
Region    | size               | 0
Eviction  | eviction-algorithm | lru-heap-percentage
          | eviction-action    | overflow-to-disk
Partition | redundant-copies   | 1
```

### XML的定义 - PARTITION_REDUNDANT_OVERFLOW

```xml
<region name="myPROregion">
   <region-attributes 
       data-policy="partition">
       <partition-attributes 
          redundant-copies="1"/>
       <eviction-attributes>
         <lru-heap-percentage 
           action="overflow-to-disk"/>
       </eviction-attributes>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_PERSISTENT

### 描述

使用PARTITION_REDUNDANT_PERSISTENT区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 创建区域时，该区域将其状态写入磁盘，并从磁盘恢复其状态。 该区域在内存中维护数据的额外副本。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 数据政策(data-policy): | PERSISTENT_PARTITION |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |

### gfsh命令示例

```
gfsh>create region --name=myPRPregion --type=PARTITION_REDUNDANT_PERSISTENT
Member  | Status
------- | ------------------------------------------
server2 | Region "/myPRPregion" created on "server2"
server1 | Region "/myPRPregion" created on "server1"

gfsh>describe region --name=myPRPregion
..........................................................
Name            : myPRPregion
Data Policy     : persistent partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type    |       Name       | Value
--------- | ---------------- | -----
Region    | size             | 0
Partition | redundant-copies | 1
```

### XML的定义 - PARTITION_REDUNDANT_PERSISTENT

```xml
<region name="myPRPregion">
   <region-attributes 
       data-policy="persistent-partition">
       <partition-attributes 
        redundant-copies="1"/>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_PERSISTENT_OVERFLOW

### 描述

使用PARTITION_REDUNDANT_PERSISTENT_OVERFLOW区域快捷方式配置的区域具有在创建该区域的每个对等成员之间划分的状态。 创建区域时，该区域将其状态写入磁盘，并从磁盘恢复其状态。 该区域在内存中维护数据的额外副本。 当该区域检测到JVM内存不足时，将从内存中删除条目的值。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 数据政策(data-policy): | PERSISTENT_PARTITION |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

分区属性

|                   |      |
| :---------------- | :--- |
| redundant-copies(冗余副本): | 1    |

### gfsh命令示例

```
create region --name=myPRPOregion --type=PARTITION_REDUNDANT_PERSISTENT_OVERFLOW
describe region --name=myPRPOregion
...........................................................
Name            : myPRPOregion
Data Policy     : persistent partition
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type    |        Name        | Value
--------- | ------------------ | -------------------
Region    | size               | 0
Eviction  | eviction-algorithm | lru-heap-percentage
          | eviction-action    | overflow-to-disk
Partition | redundant-copies   | 1
```

### XML的定义 - PARTITION_REDUNDANT_PERSISTENT_OVERFLOW

```xml
<region name="myPRPOregion">
    <region-attributes 
       data-policy="persistent-partition">
       <partition-attributes 
           redundant-copies="1"/>
       <eviction-attributes>
         <lru-heap-percentage 
            action="overflow-to-disk"/>
      </eviction-attributes>
    </region-attributes>
</region>
```

## REPLICATE

### 描述

配置了REPLICATE区域快捷方式的区域具有本地状态，该状态与配置了REPLICATE数据策略的所有其他对等区域保持同步。

### 默认属性

区域属性

|              |                 |
| :----------- | :-------------- |
| 范围(scope):       | distributed-ack |
| 数据政策(data-policy): | REPLICATE       |

### gfsh命令示例

```
gfsh>create region --name=myRregion --type=REPLICATE

REPLICATE                       REPLICATE_PERSISTENT            REPLICATE_OVERFLOW              
REPLICATE_PERSISTENT_OVERFLOW   REPLICATE_HEAP_LRU              REPLICATE_PROXY                 

gfsh>create region --name=myRregion --type=REPLICATE
Member  | Status
------- | ----------------------------------------
server1 | Region "/myRregion" created on "server1"
server2 | Region "/myRregion" created on "server2"
```

### XML的定义 - REPLICATE

```xml
<region name="myRregion">
    <region-attributes 
       scope="distributed-ack" 
       data-policy="replicate"/>
</region>
```

## REPLICATE_HEAP_LRU

### 描述

使用REPLICATE_HEAP_LRU区域快捷方式配置的区域具有本地状态，该状态与使用REPLICATE数据策略配置的所有其他对等区域保持同步。 当该区域检测到JVM的内存不足时，该区域将破坏条目。

**注意:** 通常，在复制区域上不能有`local-destroy`驱逐策略。 但是，REPLICATE_HEAP_LRU区域与其他复制的区域不同。 该区域已预加载，并且通过在对等方复制区域的键中注册兴趣来保持同步。

### 默认属性

区域属性

|              |                 |
| :----------- | :-------------- |
| 范围(scope):       | distributed-ack |
| 数据政策(data-policy): | PRELOADED       |

驱逐属性

| 驱逐算法(eviction-algorithm): | lru-heap-percentage                                          |
| ------------------- | ------------------------------------------------------------ |
| 驱逐行动(eviction-action):    | local-destroy<br/>**注意:** 通常，在复制区域上不能有`local-destroy`驱逐策略。 但是，REPLICATE_HEAP_LRU区域与其他复制的区域不同。 该区域已预加载，并且通过在对等方复制区域的键中注册兴趣来保持同步。 |

|                  |      |
| :--------------- | :--- |
| interest-policy: | all  |

### gfsh命令示例

```
gfsh>create region --name=myRHLregion --type=REPLICATE_HEAP_LRU
Member  | Status
------- | -------------------------------------------
server1 | Region "/myRHLregion2" created on "server1"
server2 | Region "/myRHLregion2" created on "server2"

gfsh>describe region --name=myRHLregion
...........................................................
Name            : myRHLregion
Data Policy     : preloaded
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | local-destroy
```

### XML的定义 - REPLICATE_HEAP_LRU

```xml
<region name="myRHLregion">
    <region-attributes 
       scope="distributed-ack" 
       data-policy="preloaded">
       <subscription-attributes 
          interest-policy="all"/>
       <eviction-attributes>
         <lru-heap-percentage 
            action="local-destroy">
            <class-name>org.apache.geode.internal.size.SizeClassOnceObjectSizer</class-name>
         </lru-heap-percentage>
       </eviction-attributes>
    </region-attributes>
</region>
```

## REPLICATE_OVERFLOW

### 描述

使用REPLICATE_OVERFLOW区域快捷方式配置的区域具有本地状态，该状态与配置了REPLICATE数据策略的所有其他对等区域保持同步。

### 默认属性

区域属性

|              |                 |
| :----------- | :-------------- |
| 范围(scope):       | distributed-ack |
| 数据政策(data-policy): | REPLICATE       |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myROregion --type=REPLICATE_OVERFLOW
Member  | Status
------- | -----------------------------------------
server2 | Region "/myROregion" created on "server2"
server1 | Region "/myROregion" created on "server1"

gfsh>describe region --name=myROregion
...........................................................
Name            : myROregion
Data Policy     : replicate
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - REPLICATE_OVERFLOW

```xml
<region name="myROregion">
    <region-attributes 
       scope="distributed-ack" 
       data-policy="replicate">
       <eviction-attributes>
          <lru-heap-percentage 
           action="overflow-to-disk"/>
       </eviction-attributes>
    </region-attributes>
</region>
```

## REPLICATE_PERSISTENT

### 描述

使用REPLICATE_PERSISTENT区域快捷方式配置的区域具有本地状态，该状态与配置了REPLICATE数据策略的所有其他对等区域保持同步。 该区域将其状态写入磁盘，并在成员重新启动时恢复该状态。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 范围(scope):       | distributed-ack      |
| 数据政策(data-policy): | PERSISTENT_REPLICATE |

### gfsh命令示例

```
gfsh>create region --name=myRPregion --type=REPLICATE_PERSISTENT
Member  | Status
------- | -----------------------------------------
server2 | Region "/myRPregion" created on "server2"
server1 | Region "/myRPregion" created on "server1"

gfsh>describe region --name=myRPregion
..........................................................
Name            : myRPregion
Data Policy     : persistent replicate
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

 Type  | Name | Value
------ | ---- | -----
Region | size | 0
```

### XML的定义 - REPLICATE_PERSISTENT

```xml
<region name="myRPregion">
    <region-attributes 
        scope="distributed-ack" 
        data-policy="empty"/>
</region>
```

## REPLICATE_PERSISTENT_OVERFLOW

### 描述

使用REPLICATE_PERSISTENT_OVERFLOW区域快捷方式配置的区域具有本地状态，该状态与使用REPLICATE数据策略配置的所有其他对等区域保持同步。 该区域将其状态写入磁盘，并在成员重新启动时恢复该状态。 当区域检测到JVM的内存不足时，该区域将从内存中删除条目的值。

### 默认属性

区域属性

|              |                      |
| :----------- | :------------------- |
| 范围(scope):       | distributed-ack      |
| 数据政策(data-policy): | PERSISTENT_REPLICATE |

驱逐属性

|                     |                     |
| :------------------ | :------------------ |
| 驱逐算法(eviction-algorithm): | lru-heap-percentage |
| 驱逐行动(eviction-action):    | overflow-to-disk    |

### gfsh命令示例

```
gfsh>create region --name=myRPOregion --type=REPLICATE_PERSISTENT

REPLICATE_PERSISTENT            REPLICATE_PERSISTENT_OVERFLOW   

gfsh>create region --name=myRPOregion --type=REPLICATE_PERSISTENT_OVERFLOW
Member  | Status
------- | ------------------------------------------
server1 | Region "/myRPOregion" created on "server1"
server2 | Region "/myRPOregion" created on "server2"

gfsh>describe region --name=myRPOregion
...........................................................
Name            : myRPOregion
Data Policy     : persistent replicate
Hosting Members : server1
                  server2

Non-Default Attributes Shared By Hosting Members  

  Type   |        Name        | Value
-------- | ------------------ | -------------------
Region   | size               | 0
Eviction | eviction-algorithm | lru-heap-percentage
         | eviction-action    | overflow-to-disk
```

### XML的定义 - REPLICATE_PERSISTENT_OVERFLOW

```xml
 <region name="myRPOregion">
    <region-attributes 
       scope="distributed-ack" 
       data-policy="persistent-replicate">
       <eviction-attributes>
         <lru-heap-percentage 
             action="overflow-to-disk"/>
      </eviction-attributes>
    </region-attributes>
</region>
```

## REPLICATE_PROXY

### 描述

使用REPLICATE_PROXY区域快捷方式配置的区域没有本地状态，并将所有操作转发到使用[REPLICATE](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wq4_jpy_lk)或[REPLICATE_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_emp_jpy_lk)区域快捷方式。

### 默认属性

区域属性

|              |                 |
| :----------- | :-------------- |
| 范围(scope):       | distributed-ack |
| 数据政策(data-policy): | EMPTY           |

### gfsh命令示例

```
gfsh>create region --name=myRPXYregion --type=REPLICATE_PROXY
Member  | Status
------- | -------------------------------------------
server1 | Region "/myRPXYregion" created on "server1"
server2 | Region "/myRPXYregion" created on "server2"

gfsh>describe region --name=myRPXYregion
...........................................................
Name             : myRPXYregion
Data Policy      : empty
Accessor Members : server1
                   server2

Non-Default Attributes Shared By Accessor Members  

 Type  | Name | Value
------ | ---- | -----
Region | size | 0
```

### XML的定义 - REPLICATE_PROXY

```xml
<region name="myRPXYregion">
    <region-attributes 
        scope="distributed-ack" 
        data-policy="empty"/>
</region>
```



