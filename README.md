# Event sourcing POC
This is playground to understand event sourcing.

## What is event sourcing patter good for?
Object as an Entity is mutable, means it is chaning over time and these changes may be cause from various reasons (clients, events, etc.).
This fact makes it harder to model persistency of such entities in distributed and scalable environment - we will offten have to deal with 
consistency and locking. Event sourcing is one way to cope with this - instead storing mutable object itself, store immutable Events.
Old events may not be changed, only new events may be appended as entity of interest changes.
Another interesting benefit is 'recreating events of interest'. In this example it is possible to recreate events from certain point(s) in time,
so debugging application may be easier.
  
## Utils
Interesting library [vavr][1] has been used to apply all changes on objects from the beginning of time.
The same library offers "scala like" pattern matching :).

 
[1]:http://www.vavr.io/vavr-docs/ 
