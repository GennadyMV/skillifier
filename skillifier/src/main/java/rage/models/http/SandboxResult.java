package rage.models.http;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class SandboxResult implements Serializable {

    private String status;

    @SerializedName("exit_code")
    private String exitCode;

    private String token;

    @SerializedName("test_output")
    private String testOutput;

    private String stdout;

    private String stderr;

    public SandboxResult(String status, String exitCode, String token, String testOutput, String stdout, String stderr) {
        this.status = status;
        this.exitCode = exitCode;
        this.token = token;
        this.testOutput = testOutput;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTestOutput() {
        return testOutput;
    }

    public void setTestOutput(String testOutput) {
        this.testOutput = testOutput;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }
    
}
