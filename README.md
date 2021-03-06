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

## Run

```bash
sbt runAll
```

## Reference

 * Auction system: https://github.com/lagom/online-auction-scala
 * Lagom reference guide: http://www.lagomframework.com/documentation/1.3.x/scala/ReferenceGuide.html
 * MSDN CQRS: https://msdn.microsoft.com/en-us/library/jj591573.aspx
 * MSDN Event Sourcing: https://msdn.microsoft.com/en-us/library/jj591559.aspx
