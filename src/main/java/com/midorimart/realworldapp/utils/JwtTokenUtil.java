package com.midorimart.realworldapp.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.midorimart.realworldapp.entity.User;
import com.midorimart.realworldapp.model.TokenPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	private String secret = "DUY_ANH";

	public String generateToken(User user, long expiredDate) {
		Map<String, Object> claims = new HashMap<>();
		TokenPayload payload = TokenPayload.builder().userId(user.getId()).email(user.getEmail()).build();
		claims.put("payload", payload);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiredDate * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public TokenPayload getPayload(String token) {
		return getClaimsFromToken(token, (Claims claim) -> {
			Map<String, Object> mapResult = (Map<String, Object>) claim.get("payload");
			return TokenPayload.builder().userId((int) mapResult.get("userId")).email(mapResult.get("email").toString())
					.build();
		});
	}

	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimFunction) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claimFunction.apply(claims);
	}

	public boolean validate(String token, User user) {
		TokenPayload tokenPayload = getPayload(token);

		return tokenPayload.getUserId() == user.getId() && tokenPayload.getEmail().equals(user.getEmail())
				&& isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Date expiredDate = getClaimsFromToken(token, Claims::getExpiration);
		return expiredDate.before(new Date());
	}
}
