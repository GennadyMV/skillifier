package rage.models.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("nullness")
public class SubmissionResult implements Serializable {

    public String status;
    public ArrayList<TestResult> testResults;
    public Log logs;

    private String toCoreStatusString() {
        switch (status.toLowerCase()) {
            case "passed":
                return "OK";
            case "tests_failed":
                return "FAIL";
            default:
                return "ERROR";
        }
    }

    public Map<String, Serializable> toCoreJson() {
        Map<String, Serializable> json = new HashMap<>();
        json.put("status", toCoreStatusString());
        json.put("testResults", testResults);
        return json;
    }

    static class TestResult implements Serializable {
        public String name;
        public boolean successful;
        public ArrayList<String> points;
        public String message;
        public ArrayList<String> exception;
        public boolean valgrindFailed;
    }

    static class Log implements Serializable {
        public ArrayList<String> stdout;
        public ArrayList<String> stderr;
    }
    
}
