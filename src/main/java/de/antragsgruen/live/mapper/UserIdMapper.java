package de.antragsgruen.live.mapper;

import org.springframework.lang.Nullable;

public class UserIdMapper {
    public static boolean isLoggedInUser(String jwtUserId, @Nullable Integer apiUserId)
    {
        if (apiUserId == null) {
            return false;
        }
        return jwtUserId.equals("login-" + apiUserId);
    }

    public static boolean isAnonymousUser(String jwtUserId, @Nullable String apiUserToken)
    {
        if (apiUserToken == null) {
            return false;
        }
        return jwtUserId.equals("anonymous-" + apiUserToken);
    }
}
