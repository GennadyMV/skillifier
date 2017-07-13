package rage.models.http;

public class OauthResponse {
    
    private final Long id;
    private final String username;
    private final String email;
    private final boolean administrator;

    public OauthResponse(Long id, String username, String email, boolean administrator) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.administrator = administrator;
    }

    public long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean getAdministrator() {
        return administrator;
    }
}
