## scbdev
Standard Chartered Take Home Coding exercise
```
1. package com.kb.challenge.cache:
has the implementation for thread safe cache. Features:
  a. multiple reads on a key using Reentrant read only lock
  b. multiple writes which are synchronzied using write locks
  c. null agnostic (does not use Optional as of now) which will be enhanced in future
  d. default eviction policy is Least Recently Used (LRU)
```

```
 2. package  com.kb.challenge.deadline :
 has implementation for deadline scheduler. Features:
  a. keeps the earlier deadline first in queue, ref https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling
  b. maintains a pair of deadline vs requestid for cross lookup
  c. when poll is called for requests where deadline is in past, the request is cancelled.
  d. internally uses AtomicLong & ConcurrentHashMap for threadsafe  operations
  
```
