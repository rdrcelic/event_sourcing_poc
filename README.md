# Event sourcing POC
This is playground to understand event sourcing.

Project defines Account behaviour object and after recognised behaviours tries to declare events.
After every state changed, events are stored in Account newChanges collection, which in turn is persisted in event source repository.
Every time Account object persisted, newChanges are appended to event source repository.
Account object knows how to recreate its instance from the collection of events (state changes) ever applied on that object.

## Utils
Interesting library [vavr][1] has been used to apply all changes on Account object from the beginning of time (a.k.a left fold).
The same library offers "scala like" pattern matching :).

 
[1]:http://www.vavr.io/vavr-docs/ 
