package rage.models.http;

import java.util.Calendar;

public class CachedOauth {
    
    private final Calendar timestamp;
    private final OauthResponse response;
    
    public CachedOauth(OauthResponse response) {
        this.timestamp = Calendar.getInstance();
        this.response = response;
    }
    
    public boolean hasExpired() {
        Calendar currentTime = Calendar.getInstance();
        timestamp.add(Calendar.HOUR_OF_DAY, 1);
        if (timestamp.before(currentTime)) {
            return true;
        }
        timestamp.add(Calendar.HOUR_OF_DAY, -1);
        return false;
    }
    
    public OauthResponse getResponse() {
        return response;
    }
    
}
