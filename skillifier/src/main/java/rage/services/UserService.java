package rage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import rage.exceptions.AuthenticationFailedException;
import rage.models.User;
import rage.models.daos.UserDao;
import rage.models.http.CachedOauth;
import rage.models.http.OauthResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserService {

    private UserDao userDao;

    private final Map<String, CachedOauth> localCache;

    @Autowired
    public UserService(UserDao userDao) {
        this.localCache = new HashMap<>();
        this.userDao = userDao;
    }

    /**
     * Find User from Database. If one doesn't exist,
     * create a new User and save it.
     * @param username The name of the User
     * @return (User) Found or newly created User
     */
    public User setupUser(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            user = new User(username);
            user = userDao.save(user);
        }
        return user;
    }

    /**
     * Ask the specified server about the identity
     * of the token. Find or create User accordingly.
     * @param token The OAuth token as a String
     * @return (User) User whom the token belongs to
     */
    public User oauthFromServer(String token) throws AuthenticationFailedException {
        if (localCache.containsKey(token)) {
            if (!localCache.get(token).hasExpired()) {
                return setupUser(localCache.get(token).getResponse().getUsername());
            }
        }
        URI url = URI.create(System.getProperty("server.external.oauth") + "?access_token=" + token);
        RestTemplate template = new RestTemplate();
        OauthResponse response = template.getForEntity(url, OauthResponse.class).getBody();
        localCache.put(token, new CachedOauth(response));
        return setupUser(response.getUsername());
    }

}
