package com.example.shareEdu.configuration;

import com.example.shareEdu.dto.request.IntrospectRequest;
import com.example.shareEdu.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustumJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

   // @Lazy
    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    //Customize the decryption function to use the introspect function to check if the token is revoked
    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            //use introspect
            var response = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build()

            );
            //if token invalid throw exception.
            if (!response.isValid()) throw new JwtException("invalid token");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());

        }

        //if nimbusJwtDecoder not initialized then initialize with SecretKeySpec(alg 'HS512')
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();

        }
        return nimbusJwtDecoder.decode(token);

    }
}
