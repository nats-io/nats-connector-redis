# NATS Redis Publish Subscribe Connector
A pluggable [Java](http://www.java.com) based service to bridge the [NATS messaging system](https://nats.io) and other technologies.

[![License Apache 2.0](https://img.shields.io/badge/License-Apache2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/nats-io/nats-connector-redis.svg?branch=master)](http://travis-ci.org/nats-io/nats-connector-redis)
[![Javadoc](http://javadoc-badge.appspot.com/io.nats/nats-connector-redis.svg?label=javadoc)](http://nats-io.github.io/nats-connector-redis)
[![Coverage Status](https://coveralls.io/repos/nats-io/nats-connector-redis/badge.svg?branch=master&service=github)](https://coveralls.io/github/nats-io/nats-connector-redis?branch=master)


## Summary

The NATS Redis publish subscribe connector is provided to facilitate the bridging of NATS and Redis Publish/Subscribe.  See [The NATS Connector Framework](https://github.com/nats-io/nats-connector-framework) for more information. 

Documentation can be found [here](http://nats-io.github.io/nats-connector-redis).

## Installation

### Maven Central

#### Releases

The NATS Redis connector is currently alpha and there are no official releases.

#### Snapshots

Snapshots are regularly uploaded to the Sonatype OSSRH (OSS Repository Hosting) using
the same Maven coordinates.
If you are embedding the NATS Redis connector, add the following dependency to your project's `pom.xml`.

```xml
  <dependencies>
    ...
    <dependency>
      <groupId>io.nats</groupId>
      <artifactId>nats-connector-redis</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
```
If you don't already have your pom.xml configured for using Maven snapshots, you'll also need to add the following repository to your pom.xml.

```xml
<repositories>
    ...
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
#### Building from source code (this repository)
First, download the source code:
```
git clone git@github.com:nats-io/nats-connector-redis.git .
```

To build the library, use [maven](https://maven.apache.org/). From the root directory of the project:

```
mvn package verify
```
The jar file will be built in the `target` directory. Then copy the jar file to your classpath and you're all set.

NOTE: Running the unit tests requires that `gnatsd` be installed on your system and available in your executable search path, and the redis server must be installed and running at the default port.


### Source code (this repository)
To download the source code:
```
git clone git@github.com:nats-io/nats-connector-redis.git .
```

To build the library, use [maven](https://maven.apache.org/). From the root directory of the project:

```
mvn verify package
```

## NATS Redis Connector source package structure

* io.nats.connector.plugins.redis - This redis plug-in, developed by the NATS team.


### Referencing the Redis plugin from the NATS connector framework

The redis plug-in referenced by the nats connector framework is:
```
com.io.nats.connector.plugins.redis.RedisPubSubPlugin
```

#### Configuration

NATS configuration is set through the jnats client library properties and can be passed into the jvm, or specified in a configuration file. The properties are described [here](http://nats-io.github.io/jnats/io/nats/client/Constants.html).

The NATS Redis connector is configured by specifying a url that returns JSON file as a system property.  In this example, 
the url specifies a local file.  It can be any location that meets the URI standard.

```
-Dnats.io.connector.plugins.redispubsub.configurl="file:///Users/colinsullivan/redis_nats_connector.json"
```
in code:
```
System.setProperty(RedisPubSubPlugin.CONFIG_URL, "file:///Users/colinsullivan/redis_nats_connector.json");
```

The Redis Pub/Sub plugin configuration file read at the URI must have the following format:

```
{
    "host" : "localhost",
    "port" : 6379,
    "timeout" : 2000,
    "nats_to_redis_map" : [
        {
            "subject" : "Export.Redis",
            "channel" : "Import_NATS"
        }
    ],
    "redis_to_nats_map" : [
        {
            "channel" : "Export_NATS",
            "subject" : "Import.Redis",
        }
    ]
}

```

* Host is the redis cluster host.
* Port is the redis port.
* Timeout is the Redis timeout.

The nats_to_redis_map array is a list of NATS subjects mapped to Redis channels.  NATS wildcarding is supported.  
So, in this case, any messages published to Export.Redis in the NATS cluster will be received, and published onto
the Redis Channel "Import_NATS".

From the other direction, any message published from redis on the Export_NATS channel, will be published into
the NATS cluster on the Import.Redis subject.  At least one map needs to be defined.

Wildcarding and pattern matching is not supported at this time.

Circular message routes generated by overlapping maps should be avoided.

Basic circular route detection is performed, but is not considered a fatal error and plugin will operate.

## Running the NATS Redis connector

There are two ways to launch the NATS Redis Pub/Sub connector - invoking the connector as an application or programatically from your own application.

To invoke the connector from the command line:

```
java -classpath <your classpath> io.nats.connector.redis.RedisPubSubConnector <args>
```

The arguments are optional:
```
    -configURL <URL of the Redis Connector Configuration>
    -debug
```

To invoke the connector from an application:
```
System.setProperty(Connector.PLUGIN_CLASS, "com.io.nats.connector.plugins.redis.RedisPubSubPlugin");
new Connector().run();
```
or finally, use the framework itself to run the redis connector:
```
java -Dio.nats.connector.plugin=com.io.nats.connector.plugins.redis.RedisPubSubPlugin io.nats.connector.Connector
```

If not using maven, ensure your classpath includes the most current nats-connector (nats-connector-0.1.5-SNAPSHOT.jar) and jnats (jnats-0.4.1.jar) archives, as well as java archives compatible with jedis-2.7.3.jar, commons-pool2-2.4.2.jar, slf4j-simple-1.7.14.jar, slf4j-api-1.7.14.jar, and json-20151123.jar.

## TODO

- [ ] Wildcard Support
- [ ] Auth (password)
- [ ] Failover/Clustering Support

## License

Unless otherwise noted, the NATS source files are distributed
under the Apache Version 2.0 license found in the LICENSE file.