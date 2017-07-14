package rage;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Skillifier {

    static final Path userDir = Paths.get(System.getProperty("user.dir"));

    public static void main(String[] args) {
        setLocalTestProperties();
        SpringApplication.run(Skillifier.class, args);
    }

    //For local and unit testing
    public static void setLocalTestProperties() {
        // System properties
        System.getProperties().put("server.port", 3000);
        //System.getProperties().put("server.url", "tmc-adapt.testmycode.io/");
        System.getProperties().put("server.url", "https://fb23e6b9.ngrok.io/");
        
        // Local directories
        //Project root

        System.setProperty("server.local.root", userDir.toString());
        //Path to folder containing tmc-run init script and langs.jar
        System.setProperty("server.local.references", userDir.resolve("references").toString());
        //Path to adaptive exercise zips
        System.setProperty("server.local.download", userDir.resolve("resources").toString());
        //Path to where tmp files for submission are created;
        System.setProperty("server.local.submissions", userDir.resolve("submissions").toString());
        //Path to tmc-langs-cli jar
        System.setProperty("server.local.langs",
                userDir.getParent().resolve("tmc-langs/tmc-langs-cli/target/tmc-langs-cli-0.7.7-SNAPSHOT.jar").toString());
    
        // External sources
        System.getProperties().put("server.external.sandbox", "https://tmc-adapt.testmycode.io:3001/tasks.json");
        System.getProperties().put("server.external.oauth", "https://tmc.mooc.fi/staging/api/v8/users/current.json/");
    }
    
    //production properties
    private static void setProductionProperties() {
        // System properties
        System.getProperties().put("server.port", 3000);
        System.getProperties().put("server.url", "tmc-adapt.testmycode.io/");
        
        // Local directories
        //Path to tmc-langs-cli jar
        System.getProperties().put("server.local.langs",
                userDir.resolve("skillifier/tmc-langs/tmc-langs-cli/target/tmc-langs-cli-0.7.7-SNAPSHOT.jar").toString());
        //Location to tmc-run init script and where the langs jar is copied
        System.getProperties().put("server.local.references", userDir.resolve("skillifier/skillifier/references"));
        //Path to local zipfile downlaods directory
        System.getProperties().put("server.local.download", "/home/fogre/resources2/");
        //Path to where the temporary submission zips are stored
        System.getProperties().put("server.local.submissions", userDir.resolve("skillifier/skillifier/submissions"));
        
        // External sources
        System.getProperties().put("server.external.sandbox", "http://localhost:3001/tasks.json");
        System.getProperties().put("server.external.oauth", "https://tmc.mooc.fi/staging/api/v8/users/current.json");
    }
    
}
