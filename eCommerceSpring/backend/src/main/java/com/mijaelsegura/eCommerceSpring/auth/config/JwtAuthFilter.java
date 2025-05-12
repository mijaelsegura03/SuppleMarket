package com.mijaelsegura.eCommerceSpring.auth.config;

import com.mijaelsegura.eCommerceSpring.auth.service.JwtService;
import com.mijaelsegura.eCommerceSpring.models.User;
import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import com.mijaelsegura.eCommerceSpring.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { //se va a ejecutar una sola vez por request
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal( //este metodo intercepta cada HTTP request y verifica que el JWT sea válido
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { //verificamos que en el header de la request esté presente el token JWT y que su formato sea correcto
            filterChain.doFilter(request, response); //con doFilter pasamos al siguiente filtro SIN realizar la autenticacion, porque algo estuvo mal.
            return;
        }
        final String jwtToken = authHeader.substring(7);
        final String userName = jwtService.extractUsername(jwtToken);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userName == null || authentication != null) { //verificamos que el JWT contenga el subject, y que el usuario no esté ya autenticado en la sesión de la petición
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName); //cargamos al usuario desde la base de datos


        if (jwtService.isTokenValid(jwtToken, userDetails)) { //verificamos que el token no esté expirado y que el subject coincida con el del usuario de la BD
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //el objeto de autenticacion representa al usuario que vamos a autenticar
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request,response); // pasa al siguiente filtro con el usuario ya autenticado, o no.
    }
}
