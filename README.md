# scbdev
1. package com.kb.challenge.cache:
has the implemetatio for thread safe cache. It support:
  a. multiple reads without write locks
  b. multiple writes which are synchronzied using write locks
  c. null agnostic (does not use Optional as of now) which will be enhanced in future
  
 2. package  com.kb.challenge.deadline :
 has implemenation for deadline scheduler. It supports:
  a. keeps the earlier deadline first in queue, ref https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling
  b. maintains a pair of deadline vs requestid for cross lookup
  c. when poll is called for requests where deadline is in past, the request is cancelled.
  d. internally uses AtomicLong & ConcurrentHashMap for threadsafe  operations
  
