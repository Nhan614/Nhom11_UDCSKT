package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.AuthenticationRequest;
import com.example.shareEdu.dto.request.IntrospectRequest;
import com.example.shareEdu.dto.request.LogoutRequest;
import com.example.shareEdu.dto.request.RefreshTokenRequest;
import com.example.shareEdu.dto.response.AuthenticationResponse;
import com.example.shareEdu.dto.response.IntrospectResponse;
import com.example.shareEdu.entity.InvalidatedToken;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.repository.InvalidatedTokenRepository;
import com.example.shareEdu.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    UserRepository userRepository;
  //PasswordEncoder passwordEncoder; no autowired because error injection cycle (A -> B -> C -> A) -> error



    //this method check token validity
    public IntrospectResponse introspect (IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifierToken(token, false);
        }catch (AppException e){
            isValid = false; //if token invalid
        }
        return IntrospectResponse.builder()
                .valid(isValid).build();

    }
    //
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        //strength of BCrypt alg also known as cost factor. This value specifies the number
        //of iterations to increase the complexity of pass hashing.
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        //get user by userName to proceed with authentication
        var user = userRepository.findByUserName(authenticationRequest.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        //check pass
        boolean authenticated =  passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

       //if failed throw exception
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = genarateToken(user);

        //success return token and auth status is 'true'
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    //when user login or refresh token then the system generate new token for user
    String genarateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())// identify the subject of the token.
                .issuer("dinhHieu.com") // identify publisher
                .issueTime(new Date()) // release date
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                )) // set expiration time
                .jwtID(UUID.randomUUID().toString()) //set id for token -> to use black list
                .claim("scope", buildScope(user)).build();// set data for token

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());//convert claims(info token) to json object

        /**
        JWSObject is a jwt signed according to the JSW(JSON Web Signature) standard
        It consists of three main parts:header.payload.signature
        The header of the jwt contain info about thw signing alg
        The payload contain jwt data
         **/
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException(e);
        }
    }

    //this method use to add data into jwt
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));

            });

        return stringJoiner.toString();


    }
    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        try {

            var signToken = verifierToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime =signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException e){
            log.info("token already expired");
        }

    }
    private SignedJWT verifierToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        /*
            Case 1: isRefresh == true
            → Recalculate the expiration time based on the issuance time + REFRESHABLE_DURATION.
            Case 2: isRefresh == false
            → Keep the original expiration time of the token.
         */
        Date expiration = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();


        var verified =  signedJWT.verify(verifier);
        if(!(verified && expiration.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if(invalidatedTokenRepository.
                existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        try {
            var signedJWT = verifierToken(request.getToken(), true);

            var jit = signedJWT.getJWTClaimsSet().getJWTID();
            var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expirationTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);

            var username = signedJWT.getJWTClaimsSet().getSubject();

            var user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

            var token = genarateToken(user);

            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

}
