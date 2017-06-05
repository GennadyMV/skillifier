package rage.models.http;

@SuppressWarnings("nullness")
public class OauthResponse {
    
    private Long id;
    private String username;
    private String email;
    private boolean administrator;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean getAdministrator() {
        return administrator;
    }
    
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
    
}
