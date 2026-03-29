package com.example.beleg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sicherheitskonfiguration der Arztpraxis-Anwendung.
 * Konfiguriert Spring Security mit rollenbasierter Zugriffskontrolle (RBAC).
 * Es werden zwei Rollen unterschieden: ADMIN mit vollem Zugriff
 * und USER mit eingeschraenkten Rechten (nur Lesen und Erstellen).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Erstellt und konfiguriert den Passwort-Encoder.
     * Verwendet BCrypt-Hashing fuer sichere Passwortspeicherung.
     *
     * @return BCryptPasswordEncoder-Instanz
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfiguriert die Benutzer der Anwendung im Arbeitsspeicher (In-Memory).
     * Zwei Benutzer werden angelegt:
     * - admin: vollstaendiger Zugriff (ROLE_ADMIN)
     * - user: eingeschraenkter Zugriff (ROLE_USER)
     *
     * @param encoder PasswordEncoder zum Verschluesseln der Passwoerter
     * @return InMemoryUserDetailsManager mit den konfigurierten Benutzern
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("12345"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("hochschule123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    /**
     * Konfiguriert die HTTP-Sicherheitsregeln der Anwendung.
     * Legt fest welche Endpunkte welche Rollen benoetigen:
     * - Erstellen/Bearbeiten/Loeschen von Aerzten: nur ADMIN
     * - Loeschen von Patienten und Terminen: nur ADMIN
     * - Alle anderen Endpunkte: alle authentifizierten Benutzer
     *
     * @param http HttpSecurity-Objekt fuer die Konfiguration
     * @return Konfigurierte SecurityFilterChain
     * @throws Exception bei Konfigurationsfehlern
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Nur ADMIN kann Aerzte loeschen oder bearbeiten
                        .requestMatchers("/aerzte/new", "/aerzte/edit/**", "/aerzte/delete/**").hasRole("ADMIN")
                        // Nur ADMIN kann Patienten und Aerzte loeschen
                        .requestMatchers("/patients/delete/**").hasRole("ADMIN")
                        .requestMatchers("/termine/delete/**").hasRole("ADMIN")
                        // Alle autorizierte users koennnen schauen und erstellen
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}