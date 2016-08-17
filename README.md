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

Storing the mail in the automatically-created H2 database isn't the most durable solution, but for most mail testing purposes, it's more than enough.  If you do need to use an external DB, use spring's datasource configuration to switch to your desired provider.  For example, to use MySQL:

	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	spring.datasource.url=jdbc:mysql://mysql-server-host:3306/holdmail?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
	spring.datasource.username=holdmail
	spring.datasource.password=password
	loader.path=/path/to/mysql.jar  # TODO: verify this 


## Build It

The backend is a [Spring Boot](http://projects.spring.io/spring-boot) application, exposing a REST API and SMTP server.  The UI is an [Angular](https://angularjs.org/) application built with [Bower](https://bower.io) and [Gradle](https://gradle.org) respectively. 

For a first time run, fetch the UI deps with:

    bower install
       
To build from the command line, use:

	gradle build

And you'll find the JARs under <code>build/libs</code>, with the RPM under <code>build/rpm</code>.  

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

