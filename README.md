# lagom-practice

This project is based on the lagom/lagom-scala.g8 template.
This application uses MySQL driver alternative to Cassandra driver.

If you want to introduce API gateway pattern, the web gateway depends on PlayScala.


## Requirements

 * JDK
 * Scala 2.11.8
 * SBT
 
## Setup

First of all, you need to create a database for the application and prepare for JDBC settings.

Execute SQL above SQL for read_side_offsets, journal and snapshot DDL if you get invalid callable syntax errors on Lagom with JDBC (MySQL):

```sql
create table `journal` (`ordering` BIGINT NOT NULL AUTO_INCREMENT UNIQUE,`deleted` BOOLEAN DEFAULT false NOT NULL,`persistence_id` VARCHAR(255) NOT NULL,`sequence_number` BIGINT NOT NULL,`message` BLOB NOT NULL,`tags` VARCHAR(255));
create table `snapshot` (`persistence_id` VARCHAR(255) NOT NULL,`sequence_number` BIGINT NOT NULL,`created` BIGINT NOT NULL,`snapshot` BLOB NOT NULL);
create table `read_side_offsets` (`read_side_id` VARCHAR(255) NOT NULL,`tag` VARCHAR(255) NOT NULL,`sequence_offset` BIGINT,`time_uuid_offset` CHAR(36));
```


```
[error] a.a.OneForOneStrategy - invalid callable syntax. must be like {? = call <procedure/function name>[(?,?, ...)]}                                                                                                                        
 but was : create table `journal` (`ordering` BIGINT NOT NULL AUTO_INCREMENT UNIQUE,`deleted` BOOLEAN DEFAULT false NOT NULL,`persistence_id` VARCHAR(255) NOT NULL,`sequence_number` BIGINT NOT NULL,`message` BLOB NOT NULL,`tags` VARCHAR(2
55))  
```

See also for the errors: https://github.com/lagom/lagom/issues/498

## Run

```bash
sbt runAll
```

## Reference

 * Auction system: https://github.com/lagom/online-auction-scala
 * Lagom reference guide: http://www.lagomframework.com/documentation/1.3.x/scala/ReferenceGuide.html
 * MSDN CQRS: https://msdn.microsoft.com/en-us/library/jj591573.aspx
 * MSDN Event Sourcing: https://msdn.microsoft.com/en-us/library/jj591559.aspx
