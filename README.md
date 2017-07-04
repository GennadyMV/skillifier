# skillifier

### About setup
Skillifier is configured to be used with [TMC Sandbox](https://github.com/testmycode/tmc-sandbox) and [TMC Langs](https://github.com/testmycode/tmc-langs).

* **TMC Sandbox** handles the running of tests for the submitted project. See the Github page for installation.

* **TMC Langs** is included as a submodule, so the required files should be generated for you as soon as you build the project. However, you need to specify the correct paths to them. See ["Configuring application properties"](#configuring-application-properties).

To build the project, you will need:

* **Git** to download and manage the required submodules.

* **Maven** to download and manage the required dependencies.

Building Skillifier is easy, just run `buildSkillifier.sh` or execute commands `git submodule init`, `git submodule update` and then `mvn clean install` on project root.

### Configuring application properties
You need to specify certain properties for the application to run correctly. These settings can be found in the Main class, `Skillifier.java`. Example configuration is provided.

| Setting | Description |
| --- | --- |
| server.port | Preferred server port |
| server.url | The public base URL you want to be redirected to |
| server.local.langs | Path to local tmc-langs-cli.jar |
| server.local.download | Your local directory with the downloadable exercises in it |
| server.local.references | Your local directory with the tmc-run script and where tmc-langs.jar is copied |
| server.local.submissions | Your local directory where the temporary submission files are stored |
| server.external.sandbox | The external Sandbox server API endpoint |
| server.external.oauth | The external OAuth server API endpoint |

### Using a local database H2
Skillifier defaults to a local in-memory database called H2. This is done to make both development and tests easier. If you want to generate new tables every time you fire up the application, you can use
```
# Table autogeneration
spring.jpa.hibernate.ddl-auto = create
```
or `create-drop`. Both settings generate tables automatically, with the latter also dropping them upon exiting.
Test data is inserted from `import.sql` file found under "Other Sources".
H2 also features a nifty console found from `localhost:port/h2-console` which lets you inspect the database schema and run queries.

### Using an external database
You may want to use an external database if you want to keep inserted data around for longer. To use an existing database instead of H2, simply add a file called `application-production.properties` under "Other Sources". For example, your configuration might look something like this:
```
# Table autogeneration
spring.jpa.hibernate.ddl-auto = none

# This is the production configuration
spring.datasource.username = username
spring.datasource.password = password
spring.datasource.url = jdbc:postgresql://path/to:5432/databaseName
spring.datasource.driver-class-name = org.postgresql.Driver
```

The row `spring.jpa.hibernate.ddl-auto` is important to be specified to `none` (you may also use `update` or `validate`) when changing to the production profile unless you want to rewrite everything on startup.

To apply these changes, change your preferred profile from `application.properties`. You would need to add the following row:
```
spring.profiles.active = production
```

### Running Skillifier
Once you've set up and built Skillifier, you can run the .jar file found in /skillifier/target or use the included `skillifier.sh` script to start the server. The script takes the following parameters:

| Paremeter | Description |
| --- | --- |
| -h   --help | Display help message |
| -r   --run | Start Skillifier, write output to applog.txt |
| -s   --stop | Kill application process |
| -st   --status | Display current status (running/not) |
| -rs   --restart | Stop and restart Skillifier |

---

### Developers
* [Saku Olin](https://github.com/sakuolin) Project architecture

* [Ville Tanttu](https://github.com/Forbaya) Project architecture, TMC Sandbox

* [Antti Kotiranta](https://github.com/fogre) Project architecture, System admin

* [Marko Vainio](https://github.com/mavai) Core integration, TMC Langs

* [Qianyue Jin](https://github.com/laituli) Core integration, TMC Langs

* [Piia Hartikka](https://github.com/Piia) Plugin integration

* [Sara Kauvo](https://github.com/saraheina) Plugin integration

---

### Additional Ohtuprojekti information
**Skillifier** is a backend for downloading and submitting adaptive exercises. It's built with Spring and Hibernate, using Java and PostgreSQL.

* [Backlog](https://docs.google.com/spreadsheets/d/1n3tnFChejBiBZ1VVkDLRjteTlGQG_64NRIwSkfzarXM/)

* [Workhours](https://docs.google.com/spreadsheets/d/1e0fi_Z3VFRN9jP3Kd1zfb2XRoD62o27cjtYcM7oF4gs/)

#### Repositories committed to:
* [TMC Core](https://github.com/testmycode/tmc-core)

* [TMC Netbeans](https://github.com/testmycode/tmc-netbeans)

* [TMC Langs](https://github.com/testmycode/tmc-langs)

#### License

[MIT license](license.md)
