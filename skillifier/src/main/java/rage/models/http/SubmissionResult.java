package rage.models.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubmissionResult implements Serializable {

    private final String status;
    private final ArrayList<TestResult> testResults;
    private final Log logs;

    public SubmissionResult() {
        this.status = "";
        this.testResults = new ArrayList<>();
        this.logs = new Log();
    }

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

    public String getStatus() {
        return status;
    }

    public Map<String, Serializable> toCoreJson() {
        Map<String, Serializable> json = new HashMap<>();
        json.put("status", toCoreStatusString());
        json.put("testResults", testResults);
        return json;
    }

    static class TestResult implements Serializable {
        public String name;
        boolean successful;
        public ArrayList<String> points;
        public String message;
        public ArrayList<String> exception;
        public boolean valgrindFailed;

        public TestResult(String name, boolean successful, ArrayList<String> points, String message, ArrayList<String> exception, boolean valgrindFailed) {
            this.name = name;
            this.successful = successful;
            this.points = points;
            this.message = message;
            this.exception = exception;
            this.valgrindFailed = valgrindFailed;
        }
    }

    static class Log implements Serializable {
        public ArrayList<String> stdout;
        public ArrayList<String> stderr;

        public Log() {
            this.stdout = new ArrayList<>();
            this.stderr = new ArrayList<>();
        }
    }
    
}
