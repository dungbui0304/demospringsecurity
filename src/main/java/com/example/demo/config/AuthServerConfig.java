package com.example.demo.config;

import java.util.Base64;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.security.config.Customizer;

@Configuration
public class AuthServerConfig {

    private static final String PRIVATE_KEY = """
        MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQgP/JquCT7HHaVX23a9WS4uJPoALgc9kgewSXfJckLxf21n9Bvu7mcKNB7ga9tCvwBiqI/wfkSVTTUkU14E7Hr5ytFQN/ed8HazarGltYfmY7t9Hk+AjO4a9YX78//7MXn89AwN/f5hjTJpt1KykeA/7yw8pUWf/cp30lgniSypALdxGPn9shN++igLu0HyeAedZ8M7jOkhPhvWiQ+ECfYUvaRvfozurClslFHEidLbPSl//uzm68UhxXvtE/l5Uiip1Q44dx0BDGOrCt7RsI+g9aViOnJCxcc0/lLa/DBmhvYVYgHrRHG7HQRQzxxyc4SmYn4SsQbBRYLpL2AkP3AgMBAAECggEAAkO60Ee3cFCogxsomWRl1m/5m0aWoWkNgHY1lfU33KAlhEkCHgZ8qJFF0XWykKVM6nd2rZqis4HXlpzIvNp2oJT+TT1Y/Pp0exHybw6DOkVtLaFEE1bD6bm2/Abvy22NVjVZiYK1K8TFzw1KGCfjpqLOeXggHqU3mGYgf6OYiA3/VBkcJngO8PlWyvK/DyvLmpoubgiRImyGEShYS4tHal/3WGqOjXblmMjmipffxSckhARjgJl0LjWDMqwQi+ldCY/KzZEGrV82RKGqpScG3IXK3ydVGZ36iDaU+jlchwMSRBb/vSyptIR++tt6P7NDUhYc252fGBaZ2o/D0PkjcQKBgQDUmBvMGVm41xi/BqQrsob9q7UCFoGt5w4+QBvKvhFFrkrOHye4xrgbjC3Pp4LcFt02YtqABI2O4fpY9wxwEFi07vPlP2mzLId2fhbkdbNKmWijzZpxJP0OY11gSlRlsI9HujfkCTGaATbGSi/wrvg3GQsIWbT4vJadGAwv8wKZjwKBgQD7ExmU4XJwd1ZM6wG/cCG0ApgBa5hNlkjkD2SHWX5DpNkpy2nZfOtXa1DVQc+HWE91UqSRnTtXz2A/m16S+AkXHH0NduMek0vA0iAsdJ8WTF/vITIlr99G535h0/vC7LPPFKl4pQ9QFXwVHcNJlcaWJ8uUI3QWYQjqtk8iqAXrGQKBgQCOb38oRs/TROICkquJz2ocKV/0DypBXm4vjPpR4vhljDn4Ue1mIlYVtty2pQ/K79K0bpTgcqx5T7RQyOzfZERarUJhlRP3hN0Zqa8i0C0YzSHLpaRVLCgT7MUSD5ruKtZEg2KQ+Qvvl+KgBfKKul/5YmD+7kOO56OtiwAXXCSgxwKBgCmW+F3ZGoPWwMztANUdVffjlhpmIEx54Ikmo5hoEqpR3Ec4EsicEL/iXvl0dVQOJNtiXTBhwzrAd0kSC5B81cSmS3C3iNe/voSqSfoTN2cSL7IE5bGoeGveXOkHnGSloQNkgM4jSW4SoQM+cuL1lHe7D9XyKYCrgjafDGzJOHHBAoGAMxaJ7W8zUTlgzda94W6pwONHouzBxEurnj3YVokvZrxcLs07QqFbQog2f1LUxyXe7t36UdyDdmP814qPJlcAg1EpEPzu6otA4jYyIlj9rLGtF1oz0j9V53wY+ITWvDsiSyh5QaBac3hDqhGmhY4WnOZcjM7C1zfG0R+QlpD7TZ0=
        """;
    private static final String PUBLIC_KEY = """
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ID/yargk+xx2lV9t2vVkuLiT6AC4HPZIHsEl3yXJC8X9tZ/Qb7u5nCjQe4GvbQr8AYqiP8H5ElU01JFNeBOx6+crRUDf3nfB2s2qxpbWH5mO7fR5PgIzuGvWF+/P/+zF5/PQMDf3+YY0yabdSspHgP+8sPKVFn/3Kd9JYJ4ksqQC3cRj5/bITfvooC7tB8ngHnWfDO4zpIT4b1okPhAn2FL2kb36M7qwpbJRRxInS2z0pf/7s5uvFIcV77RP5eVIoqdUOOHcdAQxjqwre0bCPoPWlYjpyQsXHNP5S2vwwZob2FWIB60Rxux0EUM8ccnOEpmJ+ErEGwUWC6S9gJD9wIDAQAB
        """;

    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
            .with(authorizationServerConfigurer, (authorizationServer) -> 
                authorizationServer.oidc(Customizer.withDefaults())
            );
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client-id")
            .clientSecret("{noop}client-secret")
            .scope("read")
            .scope("write")
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8080/login/oauth2/code/client-oidc")
            .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair = generator.generateKeyPair();
        String publicKey = Base64.getEncoder()
                .encodeToString(keyPair.getPublic().getEncoded());

        String privateKey = Base64.getEncoder()
                .encodeToString(keyPair.getPrivate().getEncoded());

        System.out.println("PUBLIC:");
        System.out.println(publicKey);

        System.out.println("\nPRIVATE:");
        System.out.println(privateKey);
        RSAKey rsaKey = new RSAKey.Builder(getPublicKey())
                .privateKey(getPrivateKey())
                .keyID("my-key-id")
                .build();

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PRIVATE_KEY.replaceAll("\\s", ""));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private RSAPublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY.replaceAll("\\s", ""));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
