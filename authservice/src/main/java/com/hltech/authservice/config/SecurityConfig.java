//  package com.hltech.authservice.config;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.web.SecurityFilterChain;

// // @Configuration
// // public class SecurityConfig {

// //     @Bean
// //     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

// //         return http
// //                 .csrf(csrf -> csrf.disable())
// //                 .authorizeHttpRequests(auth -> auth
// //                         .requestMatchers(
// //                                 "/api/auth/register",
// //                                 "/api/auth/login",
// //                                 "/actuator/health",
// //                                 "/actuator/info")
// //                         .permitAll()
// //                         .anyRequest().authenticated())
// //                 .httpBasic(basic -> basic.disable())
// //                 .formLogin(form -> form.disable())
// //                 .build();
// //     }

//     // @Bean
//     // PasswordEncoder passwordEncoder() {
//     //     return new BCryptPasswordEncoder();
//     // }
// // }



// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         return http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                 .httpBasic(basic -> basic.disable())
//                 .formLogin(form -> form.disable())
//                 .build();
//     }
//         @Bean
//     PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }

package com.hltech.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // TEMPORARY: allow every request. Lock this down before deploying.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}