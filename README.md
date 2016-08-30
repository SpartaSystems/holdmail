# Holdmail

*Holdmail is a mail server that doesn't deliver mail!*

Instead of spamming real users (or worse - customers) while you test your applications, holdmail offers the following: 

* An SMTP service that stores mails instead of relaying them for delivery.  
* A web interface for searching and viewing of those emails.
* A REST API to query and fetch email content, very useful for testing.

## Run It

If you just want to try it out, grab and install the [RPM](#rpm-link-coming-soon).  After installation, start the **holdmail** service:

<pre><code>#&gt; sudo /etc/init.d/holdmail start
or
#&gt; sudo service holdmail start
</code></pre>

It's now ready to use!  Without any configuration, holdmail does the following:

* It accepts SMTP messages on localhost port **25000**. Configure your application to use this as the outgoing SMTP server.
* It makes the webapp available at [http://localhost:8080/](http://localhost:8080/). 
* Similarly, the REST API will be available at [http://localhost:8080/rest/messages](http://localhost:8080/rest/messages)
* A [H2](http://www.h2database.com/) embedded database will be created in holdmail's home directory (/opt/holdmail).

## Configure It

If using the RPM deployment, holdmail will look for an optional file called <code>/etc/holdmail.properties</code> which it will use to override its default configuration.  

*Note: This is the first release of holdmail, so configuration is pretty limited, and is likely to receive some necessary attention in the next release.  Currently, any [Spring Boot](http://projects.spring.io/spring-boot) configuration can be used in this file, but may not be supported as such functionality becomes a first-class configuration option in holdmail.*

### Security

Holdmail is designed to be open.  There are no accounts or mailboxes to configure.  Mail for _any_ recipient will be stored and will be queryable.  This is to facilitate the ease of email testing without requiring test authors/performers to perform mailbox setup in advance.  

If you're going to have to expose the HTTP service to a larger group than desired, you can use spring basic configuration to lock it down:

	security.basic.enabled=true
	security.user.name=homer
	security.user.password=mmmdonut


### Using a different Datasource

By default, the application will create a [H2](http://www.h2database.com/html/main.html) database file called <code>holdmail.mv.db</code> in the running user's home directory (this is <code>/opt/holdmail/holdmail.mv.db</code> if you used the RPM intaller).

This should be good enough for most users, but if you want to use a different RDBMS such as MySQL, you can configure your own datasource.

 in <code>/etc/holdmail.properties</code> using the relevant [Spring Boot DataSource properties](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html).  The following example shows how to configure to connect to a MySQL DB:

	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	spring.datasource.url=jdbc:mysql://mysql-server-host:3306/holdmail?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
	spring.datasource.username=holdmail
	spring.datasource.password=password


* The app manages its own database, so this user will need to have sufficient provileges to create and modify tables.

* Holdmail doesn't distribute drivers for external databases (for reasons of distributable size, but mostly to avoid farcical licensing complications &#128584;).  
   * If using the Holdmail RPM distribution, place your driver JAR file in <code>/opt/holdmail/lib</code> and the app will find it automatically.
   * To use the MySQL configuration above, you'll need to download the [MySQL connector driver JAR](https://dev.mysql.com/downloads/connector/j/5.0.html) yourself.



## Build It

The backend is a [Spring Boot](http://projects.spring.io/spring-boot) application, exposing a REST API and SMTP server.

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

	
## REST API

* SMTP port default is *25000*
* Webapp available at [http://localhost:8080/](http://localhost:8080/)
* Following REST endpoints:
	* GET /rest/messages[?recipient=herp@derp.com] _(list all messages)_
	* GET /rest/messages/3 _(list info about message 3)_
	* GET /rest/messages/3/raw _(the original SMTP message)_
	* GET /rest/messages/3/text _(the text/plain content of the message, 404 if none)_
	* GET /rest/messages/3/html _(the text/html content of the message, 404 if none)_
	* GET /rest/messages/3/content/blah _(the inline content 'blah' (if the /html resource returns HTML contianing cid:blah references, they will have been replaced with a URI referencing this resource)_


## License

Holdmail is licensed under the [Apache 2.0](LICENSE) license.  

