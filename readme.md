[Dropwizard] - DDSL service
=========

A [Dropwizard] service that serves as a [DDSL] (Dynamic Distributed Service Locator).  
  
#### The Dropwizard service contains the following:
  - Runs the [DDSL] host / provider (No need to manually install / configure Zookeeper :) )
  - Allows you to configure the [DDSL] host / provider settings (Host adres, port etc) in 1 file.
  - Listens to other service that can be registered through [DDSL]
  - Configures the load balancer (<B>nginx</B>) and triggers an update when service are added.  

How does it work
=========

You can start the microservice (after installing [nginx]) with the following command:  
 - Navigate to the Dist folder 
```sh
java -jar ddsl-service-1.0.jar server ddsl.yml
```  

The DDSL service wil then listen to other services. Other services (created with i.e. Play Framework, Dropwizard or Ruby) can register to the DDSL service after providing their name, version # and location.  
The DDSL service will then build a configuration file for the HTTP server ([nginx]) which will serve as a load balancer.

What is DDSL
=========

DDSL - Dynamic Distributed Service Locator.
More information can be found here: https://github.com/mbknor/ddsl

#### Dynamic
 - No admin needed
 - You don't have to manually add your service / version to the repository
 - Your application can automatically register its location. -- It can also register it's "quality" (Clients will preferred locations with better "quality"
 - Locations on "localhost" will be preferred
 - You can mix several "environments" (prod, test) within the same DDSL-repository
 - Automatically load balancing between multiple locations with same "quality"
 - Service is automatically removed from repository, if it crashes/go down

#### Distributed
 - DDSL has no single point of failure
 - It uses ZooKeeper as its dynamic distributed storage

#### Service locator
 - A repository of services (with version) and their current locations

#### Where Does it help
In many big companies you have a lot of services (SOAP, REST, etc) spread across many servers on several different Web Containers/Application Servers/ESB (Weblogic, Glassfish, Tomcat, Jetty, Mule, etc). You also have several different environments: test, preprod, prod etc with different servers and databases etc.

One service might use several other services.

You might also have (or want) several different versions of one service to run at the same time.

All those service locations... This means a lot of configuring

This is where DDSL helps..

Differences in the configwriter
==========
The configwriter in this module works slightly different from the [configwriter] created by mbknor.  
Since this configwriter is also able to support multiple different services at the same time. And is thus able to run multiple services on a single port #, by using the DDSL name attribute to resolve the location path.

For example:  
You have 4 identical authentication services with the name "authentication".  
You also have 3 identical calculation services with the name "calculator"   
These 7 services are hosted on different ports (and maybe on different hosts aswell)  
The load balancer is hosted for example on port 7080 and on hostname: 127.0.0.1  
The result will then be as follow:  
127.0.0.1:7080/authentication will resolve to: the authentication services
127.0.0.1:7080/calculator will resolve to: the calculation services.  
  
Its important that you don't add "/" to the hostname to support subpaths since zookeeper does not support that.

Version
----

1.0 - First release.

Tech
-----------

Dropwizard DDSL service uses a number of open source projects to work properly:

* [DDSL] - the Dynamic Distributed Service Locator
* [Zookeeper] - as its dynamic distributed storage
* [Dropwizard] - a Java framework for developing services.

Installation
--------------

#### step 1:
Install [nginx] and run it.
#### step 2:
Edit the ddsl.yml to your needs.
#### step 3:
Create the jar file:
```
mvn package
```
Or
run the already packaged jar in the dist directory directly.  
Navigate to the Dist folder 
```sh
java -jar ddsl-service-1.0.jar server ddsl.yml
```  
### Step 4:
Run other services and let them connect with the [DDSL] library.
There are also DDSL plugins available for: Play Framework 1.x, Play Framework 2, Dropwizard and Ruby
License
----

MIT

[DDSL]:https://github.com/mbknor/ddsl
[Dropwizard]:https://dropwizard.codahale.com
[nginx]:http://wiki.nginx.org/Main
[Zookeeper]:http://zookeeper.apache.org/
[configwriter]:https://github.com/mbknor/ddslConfigWriter