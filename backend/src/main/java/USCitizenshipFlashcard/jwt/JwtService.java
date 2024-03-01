package USCitizenshipFlashcard.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;

@Service
public class JwtService {
    public static String ISSUER = "USCitizenshipFlashcard";
    static String SECRET_KEY = "Zmxhc2hDYXJ0";

    // 1 hour is  3600 * 1000 millisecond.
    public static final int EXPIRY_IN_MILLISECOND = 3600 * 1000;

    private static final HashSet<String> JWT_DENY_LIST = new HashSet<>();

    public static void invalidateToken(String token) {
        JWT_DENY_LIST.add(token);
    }

    public static boolean isTokenInvalidated(String token) {
        return JWT_DENY_LIST.contains(token);
    }

    public static void addToDenyList(String token)
    {
        JWT_DENY_LIST.add(token);
    }

    public static boolean isDenyListed(String token)
    {
        return JWT_DENY_LIST.contains(token);
    }

    public static String createToken(String username)
    {
        return createToken(username, ISSUER);
    }

    static String createToken (String username, String issuer) {
        Date now = new Date();

        //Set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(username)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + EXPIRY_IN_MILLISECOND))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public static String getUserNameForToken(String token) {
        if(isTokenInvalidated(token)) {
            return null;
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        if(!claims.getIssuer().equals(JwtService.ISSUER)) {
            return null;
        }

        Date expiryDate = claims.getExpiration();
        if(expiryDate.before(new Date())) {
            return null;
        }
        return claims.getSubject();
    }

    static Key getSignInKey() {return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));}
}
