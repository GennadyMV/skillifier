package rage.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class JsonService {

    private final ObjectMapper jsonHandler;

    public JsonService() {
        jsonHandler = new ObjectMapper();
    }

    /**
     * Using Jackson, map an Object or a list of Objects
     * into a formatted Json String.
     * @param obj The Object or the list of Objects
     * @return (String) Json String
     */
    public String toJson(Object obj) {
        try {
            return jsonHandler.writeValueAsString(obj);
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Parse a specified Class from a Json String.
     * @param json The Json String
     * @param classType Class you want to parse the Json to
     * @return (Object) Parsed class as an Object
     */
    public Object fromJson(String json, Class classType) throws IOException {
        return jsonHandler.readValue(json, classType);
    }

}
