# mailhold

Mailhold is a simple app which runs an SMTP server that accepts mail, but doesn't deliver it. Instead, it stores it locally and makes it available for viewing via a web UI, or a REST API.

## running it

This is a [spring boot](http://projects.spring.io/spring-boot) application.  Most modern Java-aware IDEs should be able to import it and run it without any special config. 

For the command line folks, go to the root directory and run:

	mvn spring-boot:run

## accessing it

* SMTP port default is *25000*
* Webapp available at [http://localhost:8080/](http://localhost:8080/)
* Following REST endpoints:
	* GET /rest/messages[?recipient=herp@derp.com] _(list all messages)_
	* GET /rest/messages/3 _(list info about message 3)_
	* GET /rest/messages/3/raw _(the original SMTP message)_
	* GET /rest/messages/3/text _(the text/plain content of the message, 404 if none)_
	* GET /rest/messages/3/html _(the text/html content of the message, 404 if none)_
	* GET /rest/messages/3/content/blah _(the inline content 'blah' (if the /html resource returns HTML contianing cid:blah references, they will have been replaced with a URI referencing this resource)_

## configuring it

At the moment configuration is very minimal, comprising the standard spring options that can be specified in **src/main/resource/application.properties**, in addition to a custom property to change the SMTP port if desired.

## storage

The app uses simple JPA with SQL-based storage, so should support the usual suspects (MySQL, Postgres, H2). The default config will automatically create and initialize a H2 database located at **$HOME/mailhold-db.mv.db**, which will be persisted between runs.

