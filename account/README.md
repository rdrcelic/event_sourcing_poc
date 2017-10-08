# Account
Project defines Account behaviour object and after recognised behaviours tries to declare events.
As account state changes, events are stored in Account own newChanges collection, which in turn is persisted in event source repository.
Every time Account object persisted, newChanges are appended to event source repository.
Account object knows how to recreate its instance from the collection of events (state changes) ever applied on that object.
  
## Utils
Interesting library [vavr][1] has been used to apply all changes on Account object from the beginning of time (used left fold).
The same library offers "scala like" pattern matching :).

 
[1]:http://www.vavr.io/vavr-docs/ 
