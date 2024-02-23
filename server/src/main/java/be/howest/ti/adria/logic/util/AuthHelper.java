package be.howest.ti.adria.logic.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;

public class AuthHelper {
    private final JWTAuth jwtAuth;

    public AuthHelper(JWTAuth jwtAuth) {
        this.jwtAuth = jwtAuth;
    }

    public String generateJwt(String adriaId){
        JsonObject claims = new JsonObject()
                .put("sub", adriaId)
                .put("role", "user");


        JWTOptions jwtOptions = new JWTOptions()
                .setExpiresInSeconds(604800) //1 week
                .setAlgorithm("HS256");

        return jwtAuth.generateToken(claims, jwtOptions);
    }
}
