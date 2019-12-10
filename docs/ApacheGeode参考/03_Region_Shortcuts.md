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



## Region Shortcuts Quick Reference 

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



## LOCAL

### Description

A region configured with the LOCAL region shortcut is scoped only to its JVM and is not visible to other peer members. The region does not distribute data and operations to other caches.

### Default Attributes

Region Attributes

|              |        |
| :----------- | :----- |
| scope:       | local  |
| data-policy: | NORMAL |

### gfsh Command Example

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

### XML Definition of LOCAL

```
<region name="myLOCALregion">
   <region-attributes scope="local"/>
</region>
```

## LOCAL_HEAP_LRU

### Description

A region configured with the LOCAL_HEAP_LRU region shortcut is scoped to its JVM and is not visible to other peer members. The region does not distribute data and operations to other caches. The region destroys the least recently used entries when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |        |
| :----------- | :----- |
| scope:       | local  |
| data-policy: | NORMAL |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | local-destroy       |

### gfsh Command Example

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

### XML Definition of LOCAL_HEAP_LRU

```
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

### Description

A region configured with the LOCAL_OVERFLOW region shortcut is scoped only to its JVM and is not visible to other peer members. The region does not distribute data and operations to other caches. The region moves the values of entries to disk when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |        |
| :----------- | :----- |
| scope:       | local  |
| data-policy: | NORMAL |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of LOCAL_OVERFLOW

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

### Description

A region configured with the LOCAL_PERSISTENT region shortcut is scoped to its JVM and is not visible to other peer members. The region does not distribute data and operations to other caches. The region writes its state to disk and can recover that state when the member restarts.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| scope:       | local                |
| data-policy: | PERSISTENT_REPLICATE |

### gfsh Command Example

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

### XML Definition of LOCAL_PERSISTENT

```
<region name="myLPregion">
    <region-attributes 
      scope="local" 
      data-policy="persistent-replicate"/>
</region>
```

## LOCAL_PERSISTENT_OVERFLOW

### Description

A region configured with the LOCAL_PERSISTENT_OVERFLOW region shortcut is scoped to its JVM and is not visible to other peer members. The region does not distribute data and operations to other caches. The region writes its state to disk and can recover that state when the member restarts. The region also removes the values of entries from memory when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| scope:       | local                |
| data-policy: | PERSISTENT_REPLICATE |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of LOCAL_PERSISTENT_OVERFLOW

```
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

### Description

A region configured with the PARTITION region shortcut is partitioned across each peer member that created the region.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

### gfsh Command Example

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

### XML Definition of PARTITION

```
<region name="myPregion">
    <region-attributes 
      data-policy="partition"/>
  </region>
```

## PARTITION_HEAP_LRU

### Description

A region configured with PARTITION_HEAP_LRU is partitioned across each peer member that created the region. The region destroys entries when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | local-destroy       |

### gfsh Command Example

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

### XML Definition of PARTITION_HEAP_LRU

```
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

### Description

A region configured with the PARTITION_OVERFLOW region shortcut has state that is partitioned across each peer member that creates the region. The region moves the values of entries to disk when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of PARTITION_OVERFLOW

```
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

### Description

A region configured with the PARTITION_PERSISTENT region shortcut has state that is partitioned across each peer member that creates the region. The region writes its state to disk and can recover that state when the member restarts.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| data-policy: | PERSISTENT_PARTITION |

### gfsh Command Example

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

### XML Definition of PARTITION_PERSISTENT

```
<region name="myPPregion">
    <region-attributes 
     data-policy="persistent-partition"/>
</region>
```

## PARTITION_PERSISTENT_OVERFLOW

### Description

A region configured with the PARTITION_PERSISTENT_OVERFLOW region shortcut has state that is partitioned across each peer member that creates the region. The region writes its state to disk and can recover that state when the member restarts. The region removes the values of entries from memory when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| data-policy: | PERSISTENT_PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of PARTITION_PERSISTENT_OVERFLOW

```
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

### Description

A region configured with the PARTITION_PROXY region shortcut has no local state and forwards all operations to a peer region configured with the PARTITION shortcut or a peer region configured with the PARTITION_PERSISTENT shortcut.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| local-max-memory: | 0    |

### gfsh Command Example

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

### XML Definition of PARTITION_PROXY

```
<region name="myPPXYregion">
    <region-attributes 
        data-policy="partition">
        <partition-attributes 
           local-max-memory="0"/>
    </region-attributes>
</region>
```

## PARTITION_PROXY_REDUNDANT

### Description

A region configured with the PARTITION_PROXY_REDUNDANT region shortcut has no local state and forwards all operations to a peer region configured with the PARTITION_REDUNDANT shortcut or a peer region configured with the PARTITION_REDUNDANT_PERSISTENT shortcut.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |
| local-max-memory  | 0    |

### gfsh Command Example

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

### XML Definition of PARTITION_PROXY_REDUNDANT

```
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

### Description

A region configured with the PARTITION_REDUNDANT region shortcut has state that is partitioned across each peer member that creates the region. The region maintains an extra copy of the data in memory.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |

### gfsh Command Example

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

### XML Definition of PARTITION_REDUNDANT

```
<region name="myPRregion">
   <region-attributes 
       data-policy="partition">
       <partition-attributes 
          redundant-copies="1"/>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_HEAP_LRU

### Description

A region configured with the PARTITION_REDUNDANT_HEAP_LRU region shortcut has state that is partitioned across each peer member that creates the region. The region keeps an extra copy of the data in memory. The region destroys entries when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | local-destroy       |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |

### gfsh Command Example

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

### XML Definition of PARTITION_REDUNDANT_HEAP_LRU

```
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

### Description

A region configured with the PARTITION_REDUNDANT_OVERFLOW region shortcut has state that is partitioned across each peer member that creates the region. The region keeps an extra copy of the data in memory. The region moves the values of entries to disk when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |           |
| :----------- | :-------- |
| data-policy: | PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |

### gfsh Command Example

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

### XML Definition of PARTITION_REDUNDANT_OVERFLOW

```
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

### Description

A region configured with the PARTITION_REDUNDANT_PERSISTENT region shortcut has state that is partitioned across each peer member that creates the region. The region writes its state to disk and recovers its state from disk when the region is created. The region maintains an extra copy of the data in memory.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| data-policy: | PERSISTENT_PARTITION |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |

### gfsh Command Example

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

### XML Definition of PARTITION_REDUNDANT_PERSISTENT

```
<region name="myPRPregion">
   <region-attributes 
       data-policy="persistent-partition">
       <partition-attributes 
        redundant-copies="1"/>
   </region-attributes>
</region>
```

## PARTITION_REDUNDANT_PERSISTENT_OVERFLOW

### Description

A region configured with the PARTITION_REDUNDANT_PERSISTENT_OVERFLOW region shortcut has state that is partitioned across each peer member that creates the region. The region writes its state to disk and recovers its state from disk when the region is created. The region maintains an extra copy of the data in memory. The region removes the values of entries from memory when it detects that the JVM is running out of memory.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| data-policy: | PERSISTENT_PARTITION |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

Partition Attributes

|                   |      |
| :---------------- | :--- |
| redundant-copies: | 1    |

### gfsh Command Example

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

### XML Definition of PARTITION_REDUNDANT_PERSISTENT_OVERFLOW

```
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

### Description

A region configured with the REPLICATE region shortcut has local state that is kept in sync with all other peer regions configured with a REPLICATE data policy.

### Default Attributes

Region Attributes

|              |                 |
| :----------- | :-------------- |
| scope:       | distributed-ack |
| data-policy: | REPLICATE       |

### gfsh Command Example

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

### XML Definition of REPLICATE

```
<region name="myRregion">
    <region-attributes 
       scope="distributed-ack" 
       data-policy="replicate"/>
</region>
```

## REPLICATE_HEAP_LRU

### Description

A region configured with the REPLICATE_HEAP_LRU region shortcut has local state that is kept in sync with all other peer regions configured with a REPLICATE data policy. The region destroys entries when it detects that the JVM is running low on memory.

**Note:** Normally, you cannot have a `local-destroy` eviction policy on replicated regions. However, the REPLICATE_HEAP_LRU region is not the same as other replicated regions. This region is preloaded and remains in synch by registering interest in the keys of its peer’s replicated regions.

### Default Attributes

Region Attributes

|              |                 |
| :----------- | :-------------- |
| scope:       | distributed-ack |
| data-policy: | PRELOADED       |

Eviction Attributes

| eviction-algorithm: | lru-heap-percentage                                          |
| ------------------- | ------------------------------------------------------------ |
| eviction-action:    | local-destroy**Note:**Normally, you cannot have a `local-destroy` eviction policy on replicated regions. However, the REPLICATE_HEAP_LRU region is not the same as other replicated regions. This region is preloaded and remains in synch by registering interest in the keys of its peer’s replicated regions. |

|                  |      |
| :--------------- | :--- |
| interest-policy: | all  |

### gfsh Command Example

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

### XML Definition of REPLICATE_HEAP_LRU

```
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

### Description

A region configured with the REPLICATE_OVERFLOW region shortcut has local state that is kept in sync with all other peer regions configured with a REPLICATE data policy.

### Default Attributes

Region Attributes

|              |                 |
| :----------- | :-------------- |
| scope:       | distributed-ack |
| data-policy: | REPLICATE       |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of REPLICATE_OVERFLOW

```
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

### Description

A region configured with the REPLICATE_PERSISTENT region shortcut has local state that is kept in sync with all other peer regions that are configured with a REPLICATE data policy. The region writes its state to disk and recovers that state when the member restarts.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| scope:       | distributed-ack      |
| data-policy: | PERSISTENT_REPLICATE |

### gfsh Command Example

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

### XML Definition of REPLICATE_PERSISTENT

```
<region name="myRPregion">
    <region-attributes 
        scope="distributed-ack" 
        data-policy="empty"/>
</region>
```

## REPLICATE_PERSISTENT_OVERFLOW

### Description

A region configured with the REPLICATE_PERSISTENT_OVERFLOW region shortcut has local state that is kept in sync with all other peer regions configured with a REPLICATE data policy. The region writes its state to disk and recovers that state when the member restarts. The region removes the values of entries from memory when it detects that the JVM is running low on memory.

### Default Attributes

Region Attributes

|              |                      |
| :----------- | :------------------- |
| scope:       | distributed-ack      |
| data-policy: | PERSISTENT_REPLICATE |

Eviction Attributes

|                     |                     |
| :------------------ | :------------------ |
| eviction-algorithm: | lru-heap-percentage |
| eviction-action:    | overflow-to-disk    |

### gfsh Command Example

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

### XML Definition of REPLICATE_PERSISTENT_OVERFLOW

```
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

### Description

A region configured with the REPLICATE_PROXY region shortcut has no local state and forwards all operations to a peer region configured with the [REPLICATE](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_wq4_jpy_lk) or [REPLICATE_PERSISTENT](https://geode.apache.org/docs/guide/17/reference/topics/region_shortcuts_reference.html#reference_emp_jpy_lk) region shortcut.

### Default Attributes

Region Attributes

|              |                 |
| :----------- | :-------------- |
| scope:       | distributed-ack |
| data-policy: | EMPTY           |

### gfsh Command Example

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

### XML Definition of REPLICATE_PROXY

```
<region name="myRPXYregion">
    <region-attributes 
        scope="distributed-ack" 
        data-policy="empty"/>
</region>
```



