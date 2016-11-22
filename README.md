# spring-rest-api-example

Following is a description of the design of Books API and reasoning behind it.

## Resources
Unsurprisingly, my CRUD interface to the collection of books provides following 5 methods:

- GET /catalog/books
- GET /catalog/books/<id>
- POST /catalog/books
- PUT /catalog/books/<id>
- DELETE /catalog/books/<id>

The first decision is obvious as it is encoded in the URI. There is no definition of owner of collection, as you can see. I decided I do not need multi-tenancy for this simple task (neither it was requested), although I was tempted to put "/my" in front of the namespace.  

Secondly, I didn't add an "/api" namespace, and the reason behind this is that I prefer to think of REST not as of HTTP + JSON, but as of distributed hyper-media. Media available in certain locations that just happen to be encoded with JSON. So if you think about it, it turns that everything in the Web is an API, this is why speaking namespaces like "catalog" make more sense than "api" to me. Arguing about REST-interface definition I usually refer to this brilliant speech Ted Neward delivered at NSBCon http://fast.wistia.net/embed/iframe/s4aqt9esc0?popover=true  

The third decision is not so obvious and is not visible in the scheme, but has rather dramatic effect on semantics of this API. It is so big, that at some point I even considered going CRD instead of CRUD. I am speaking about books' identity. In my domain model a book identity is a bundle of title, author, cover and type (I should have included a publication year as well) OR, thanks to a publishing industry that came up with a definition of a book identifier decades ago, ISBN (https://en.wikipedia.org/wiki/International_Standard_Book_Number). So books in my domain model are immutable regarding these properties and none of them allowed to be changed ever since the book was persisted. However, not all books are guaranteed to have ISBN (self-published ones, for instance), this is why model generates an identifier using aforementioned properties in the lack of (or in case of incorrect) ISBN. Model generates equal ids consistently, provided that identifying attributes of different objects are equal. 
So what does it mean for API? For instance, if you happen to POST the object with the same ISBN twice your collection would not grow up to two objects, nether would API return an error code. Instead, already existing object with the same identifier would get a number of copies increased.  

## Model
A Book model has following attributes:
- id (natural identifier either equal to isbn or calculated as described above)
- isbn
- title*
- author* - decided not to extract an entity (resource) to represent an author for the sake of simplicity
- year
- publisher
- cover (either NONE, HARD or PAPERBACK)
- coverColor - if you think about a book as of a real object, it makes a lot of sense, you can even refer to books as to 'that blue one over there' or 'I read three books in my life - ABC, the second and the blue one'
- ebook - boolean flag
- description
- notes
- numberOfCopies

Title and author are two required attributes, failing to provide them would cause API to reply with 400. An attempt to change them would result in 403. Whenever an object that could not be found in the collection would be requested, API would return 404.

##Compromises
1. I didn't want to get any external dependencies for this test task, so I implemented the simplest id generation algorithm I came up with. In real project I'd go with some sort of hash, a name-based uuid as in IETF UUID specification, perhaps. I'd pick a library that implements that, I mean.
2. Immutability is implemented and checked for three out of five(or six) properties, for the sake of simplicity;
3. I stored all attributes in a single "flat" Book model as I didn't want to complicate matters. If I had more time, I'd probably decouple attributes that are allowed to be changed from identity of the book;
4. Used a custom in memory repository for the sake of speed of development. I saved myself some time googling how to setup an in-memory storage and mock it up in tests. I guess it is supposed to be extremely easy;

##What's missing:
1. Decreasing the number of copies upon objects' deletion. It took me a while to get to the version provided. I ran out of time, so I decided to stop;
2. Pagination - should I used a JpaRepository I'd get it almost for free, decided not to waste time on it;
3. HATEOAS - well, it is nice to have this feature, but definitely not a must have, kind of a delighter;
4. Some edge cases aren't tested and probably do not work as expected;
5. More Book attributes.

##What did I do wrong:
1. If I was to write the test again, I'd write unit tests for controller, instead of integration tests. Spent a lot of time figuring how to setup integration tests properly;
2. Should have made more interruptions;

##What did I do well:
1. Test driving a model and a controller, I enjoyed TDD-ing as always;
2. I think I had a sudden breakthrough the moment I realised a collection can include multiple copies of a same book, but not copies of the object (Evans calls it 'refactoring towards deeper insight', so be it);
3. All in all I like the model I came up with, the rest is just the infrastructure one has to wire together;

Time it took: ~4:00. I started at around 5:00 PM and worked till 8:30 PM with small breaks up to 10 minutes in total. Then I continued from 10:10 PM through to 10:50 PM. 
