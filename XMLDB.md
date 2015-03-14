# Creation #
## Register Schema ##
```
begin
dbms_xmlschema.registeruri('http://www.info606.org/Book/Book.xsd', '/public/mines/xsd/Book.xsd', 
 local=>true,
 gentypes=>true,
 genbean=>false,
 gentables=>true,
 owner=>'greg');
 end;
 /
```

## Create Database Table ##
### Structured ###
```
 DROP TABLE registeredXml cascade constraints purge;

CREATE TABLE registeredXml (id NUMBER, xml XMLType)
  XMLTYPE COLUMN xml
 XMLSCHEMA "http://www.info606.org/Book/Book.xsd"
  ELEMENT "book";
```

### Semi-Structured ###
```
 DROP TABLE binaryXml cascade constraints purge;

CREATE TABLE binaryXml (id NUMBER, xml XMLType)
  XMLTYPE COLUMN xml
  STORE AS CLOB
  XMLSCHEMA "http://www.info606.org/Book/Book.xsd"
  ELEMENT "book";
```

# Deletion #

## Unregister Schema ##
```
BEGIN
       dbms_xmlschema.deleteSchema
               ( schemaurl =>  'http://www.info606.org/Book/Book.xsd'
               , delete_option => dbms_xmlschema.DELETE_CASCADE_FORCE
               );
    END;
/ 
```


# Performance #
```
Xpath	                        Rows	Total Time
/books/book/description/text()	5	244893588
/books/book/description/text()	1116	6410781523
//description/text()	        5	252937470
//description/text()	        1116	6651234828
```
Note: Time is in nano seconds.

What this means is that using a fully qualified XPath expression is beneficial (0.6 second difference). Adding 1000 records slowed the query by 0.0008 seconds for a fully-qualified search and 0.02 seconds for the catch-all search.