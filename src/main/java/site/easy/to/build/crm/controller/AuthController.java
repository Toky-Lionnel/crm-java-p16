package site.easy.to.build.crm.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.dto.LoginRequest;
import site.easy.to.build.crm.dto.LoginResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final HttpSession httpSession;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, HttpSession httpSession) {
        this.authenticationManager = authenticationManager;
        this.httpSession = httpSession;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Créer un token d'authentification non authentifié
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                );

            // Authentifier via AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Récupérer les rôles/authorities
            List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

            // Stocker l'authentification dans la session
            httpSession.setAttribute("loggedInUsername", authentication.getName());

            return ResponseEntity.ok(new LoginResponse(
                true,
                "Login successful",
                authentication.getName(),
                roles
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(false, "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponse(false, "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<LoginResponse> logout() {
        httpSession.invalidate();
        return ResponseEntity.ok(new LoginResponse(true, "Logged out successfully"));
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> checkAuth() {
        String username = (String) httpSession.getAttribute("loggedInUsername");
        if (username != null) {
            return ResponseEntity.ok(new LoginResponse(true, "User is authenticated", username, null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new LoginResponse(false, "User is not authenticated"));
    }
}
