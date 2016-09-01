<<<<<<< HEAD
# HoldMail
=======
![HoldMail](docs/images/holdmail-header.png "HoldMail")
>>>>>>> 2eb343e... Add new logo, some minor styling changes.

### HoldMail - A fake SMTP relay server.

Instead of spamming real users (or worse - customers) while you test your applications, HoldMail offers the following: 

* An SMTP service that stores mails instead of relaying them for delivery.  
* A web interface for searching and viewing of those emails.
* A REST API to query and fetch email content, very useful for testing.
* The ability to manually release(forward) individually selected mails to the real world.

## Run It

If you just want to try it out, grab and install the [RPM](#rpm-link-coming-soon).  After installation, start the **holdmail** service:

<pre><code>#&gt; sudo /etc/init.d/holdmail start
or
#&gt; sudo service holdmail start
</code></pre>

It's now ready to use!  Without any configuration, HoldMail does the following:

* It accepts SMTP messages on localhost port **25000**. Configure your application to use this as the outgoing SMTP server.
* It makes the webapp available at [http://localhost:8080/](http://localhost:8080/). 
* Similarly, the REST API will be available at [http://localhost:8080/rest/messages](http://localhost:8080/rest/messages)
* A [H2](http://www.h2database.com/) embedded database will be created in holdmail's home directory (/opt/holdmail).

### REST API

| Endpoint&nbsp;Description | Method | URI | Request/Response |
|---|---|---|---|
| Find messages | GET | **/rest/messages** <br/>Query Params:<br/> **'recipient'** (str) - search by email address<br/> **'size'** (int) - limit response hit size<br/> **'page'** (int) - pagination support | **Response**: 200, application/json: a _'messages: [..]'_ array.
| Get message by ID | GET | **/rest/messages/{id}** | **Response**: 200, application/json: JSON object with summary attributes. |
| Get original raw message | GET | **/rest/messages/{id}/raw** | **Response**: 200, text/plain: the original MIME message. |
| Get message text body | GET | **/rest/messages/{id}/text** | **Response**: 200, text/plain: the text body if one was present, 404 otherwise. |
| Get message HTML body | GET | **/rest/messages/{id}/html** | **Response**: 200, text/html: the HTML body if one was present, HTTP 404 otherwise. Any embedded content in the HTML will be replaced with a URI to the 'embedded content' endpoint (next)  |
| Get embedded content | GET | **/rest/messages/{id}/content/{cid}** |  **Response**: 200, The embedded content with identifier 'cid' will be served with its related content type |
| Forward message | POST | **/rest/messages/{id}/forward** | **Request**: application/json: The recipient email, in the format: <code>{"recipient":"herp@derp.com"}</code>. <br/><br/>**Response**: 202 on acceptance. |

## Configure It

If using the RPM deployment, HoldMail will look for an optional file called <code>/etc/holdmail.properties</code> which it will use to override its default configuration.  

---

&#9889; **Note: Since this is the initial release of holdmail, configuration is somewhat limited and is likely to receive some necessary cleanup/namespacing in the future.  While any [Spring Boot](http://projects.spring.io/spring-boot) configuration is currently supported in this file, it may not be guaranteed to work in future releases.** &#9889;

---

### Port Numbers

	# HTTP/REST (default is 8080)
	server.port=5555
	
	# SMTP (default is 25000)
	holdmail.smtp.port=22222
	
	
### Security

HoldMail is designed to be open.  There are no accounts or mailboxes to configure.  Mail for _any_ recipient will be stored and will be queryable.  This is to facilitate the ease of email testing without requiring test authors/performers to perform mailbox setup in advance.  

If you're going to have to expose the HTTP service to a larger group than desired, you can use spring basic configuration to lock it down:

	security.basic.enabled=true
	security.user.name=homer
	security.user.password=mmmdonut


### Using a different Datasource

By default, the application will create a [H2](http://www.h2database.com/html/main.html) database file called <code>holdmail.mv.db</code> in the running user's home directory (this is <code>/opt/holdmail/holdmail.mv.db</code> if you used the RPM intaller).

This should be good enough for most users, but if you want to use a different RDBMS such as MySQL, you can configure your own datasource. Add your configuaration to <code>/etc/holdmail.properties</code> using the relevant [Spring Boot DataSource properties](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).  The following example shows how to configure to connect to a MySQL DB:

	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	spring.datasource.url=jdbc:mysql://mysql-server-host:3306/holdmail?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
	spring.datasource.username=holdmail
	spring.datasource.password=password


* The app manages its own database, so this user will need to have sufficient provileges to create and modify tables.

* HoldMail doesn't distribute drivers for external databases (for reasons of distributable size, but mostly to avoid farcical licensing complications &#128584;).  
   * If using the HoldMail RPM distribution, place your driver JAR file in <code>/opt/holdmail/lib</code> and the app will find it automatically.
   * To use the MySQL configuration above, you'll need to download the [MySQL connector driver JAR](https://dev.mysql.com/downloads/connector/j/5.0.html) yourself.


### Mail Forwarding

To release forwarded mails from HoldMail to the real world, you'll need to configure an external SMTP relay server. By default, HoldMail is configured with itself as the outgoing SMTP relay, so forwarded messages will just end up back in HoldMail!  

&#9889; This feature is in its infancy, and can be temperamental.  Most real-world relays are (and should be) pretty strict and may reject, or worse, silently discard a forwarded email.  A familiar "From" email may be specified, as many relays won't trust arbitrary sender addresses.  Your system/network administrator may be needed to help you trace outbound delivery issues.

	# the outgoing relay hostname (default: localhost)
	holdmail.outgoing.smtp.server=localhost
	
	# the outgoing relay port (default: holdmail's SMTP port)
	holdmail.outgoing.smtp.port=${holdmail.smtp.port}
	
	# the "From" header to be set on a forwarded mail (default: holdmail@localhost.localdomain)
	holdmail.outgoing.mail.from=holdmail@spartasystems.com


## Run It From Source

HoldMail's backend is a [Spring Boot](http://projects.spring.io/spring-boot) application, exposing a REST API and SMTP server.  The UI is an [Angular JS](https://angularjs.org) & [Bootstrap](http://getbootstrap.com) web application. 

### You'll Need:

 * [Java 8+ JDK](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html#javasejdk)
 * [Gradle](https://gradle.org)
 * [NPM](https://www.npmjs.com) with the following globally installed (npm install -g _modulename_) modules:
    * browserify
    * uglifyjs 
    * karma	 
 
To build from the command line, use:

	gradle build (this triggers NPM as well)

You'll find the JARs under <code>build/libs</code>, with the RPM under <code>build/rpm</code>.  

Most modern Java-aware IDEs should be able to import *build.gradle* and launch the app by running the <code>HoldMailApplication</code> class, but from the command line, the app can be launched in dev mode by using:

	gradle bootRun

	
# Meta

## License

HoldMail is licensed under the [Apache 2.0](LICENSE) license.  

## Sparta Systems

© Copyright 2016 Sparta Systems Inc. 

[Sparta Systems](http://www.spartasystems.com) helps customers bring products to market safely and efficiently by delivering quality management software solutions that provide control and transparency throughout the enterprise and their critical supplier network. 

HoldMail is a product of Sparta's R&D department, built to help us test notification systems in [our applications](http://www.spartasystems.com/solutions). Efficient and comprehensive automated testing is an integral feature of how we build software. [Come work with us!](http://www.spartasystems.com/about-us/careers). 


