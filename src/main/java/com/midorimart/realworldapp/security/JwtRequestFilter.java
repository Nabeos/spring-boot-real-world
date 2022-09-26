package com.midorimart.realworldapp.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.midorimart.realworldapp.entity.User;
import com.midorimart.realworldapp.model.TokenPayload;
import com.midorimart.realworldapp.repository.UserRepository;
import com.midorimart.realworldapp.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String token = null;
		TokenPayload tokenPayload = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Token")) {
			token = requestTokenHeader.substring(6).trim();
			try {
				tokenPayload = jwtTokenUtil.getPayload(token);
			} catch (SignatureException e) {
				System.out.println("invalid signature jwt");
			} catch (IllegalArgumentException e) {
				System.out.println("unable to get jwt");
			} catch (ExpiredJwtException e) {
				System.out.println("expired");
			}
		} else {
			System.out.println("JWT does not start with Token");
		}
		if (tokenPayload != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Optional<User> userOptional = userRepository.findById(tokenPayload.getUserId());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				if (jwtTokenUtil.validate(token, user)) {
					List<SimpleGrantedAuthority> authorities = new ArrayList<>();
					UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
							user.getPassword(), authorities);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails,null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
