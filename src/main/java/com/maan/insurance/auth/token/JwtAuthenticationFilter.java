package com.maan.insurance.auth.token;

import static com.maan.insurance.auth.token.Constants.HEADER_STRING;
import static com.maan.insurance.auth.token.Constants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maan.insurance.auth.bean.SessionTable;
import com.maan.insurance.auth.repository.SessionDetailsRepository;




public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Lazy
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private SessionDetailsRepository sessionRep;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		String username = null;
		String authToken = null;
		SessionTable table = new SessionTable();
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
		if (header != null && header.startsWith(TOKEN_PREFIX)) {
			authToken = header.replace(TOKEN_PREFIX, "");
			table = sessionRep.findByTemptokenid(authToken);
			authToken = table.getTokenid();
	        requestWrapper.addHeader(HEADER_STRING, authToken);
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (IllegalArgumentException e) {
				logger.error("an error occured during getting username from token", e);
			} catch (Exception e) {
				logger.warn("the token is expired and not valid anymore");
				username = table.getTemptokenid();
			} 
		} else {
			//req.getSession().invalidate();
			logger.warn("couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			boolean validtoken = jwtTokenUtil.validateToken(username, userDetails,req);
			if (validtoken) {
				
				  /* String refreshtoken = jwtTokenUtil.doGenerateToken(username);
				   logger.info("new refresh token generated ======> "+refreshtoken); int
				   deletecount =sessionRep.countByTempTokenid(table.getTempTokenid());
				   if(deletecount>0) sessionRep.deleteByTempTokenid(table.getTempTokenid());
				   logger.info("delete session table and insert new token ======> "+deletecount);
				   table.setTokenId(refreshtoken); sessionRep.save(table);*/
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else if (validtoken) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}/*else if (!validtoken) {
				String refreshtoken = jwtTokenUtil.doGenerateToken(username);
				logger.info("new refresh token generated ======> "+refreshtoken);
				int deletecount = sessionRep.deleteByTemptokenid(table.getTemptokenid());
				logger.info("delete session table and insert new token ======> "+deletecount);
				table.setTokenid(refreshtoken);  
				sessionRep.save(table);
				req.getSession().invalidate();
				validtoken = jwtTokenUtil.validateToken(username, userDetails,req);
				if (validtoken) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(requestWrapper));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}*/
		}
		chain.doFilter(requestWrapper, res);
	}
}