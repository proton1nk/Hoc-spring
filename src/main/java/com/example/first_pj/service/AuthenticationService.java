package com.example.first_pj.service;

import com.example.first_pj.Entity.InvalidatedToken;
import com.example.first_pj.Entity.User;
import com.example.first_pj.dto.request.AuthenticationRequest;
import com.example.first_pj.dto.request.IntrospectRequest;
import com.example.first_pj.dto.request.LogoutRequest;
import com.example.first_pj.dto.request.RefreshRequest;
import com.example.first_pj.dto.response.AuthenticationResponse;
import com.example.first_pj.dto.response.IntrospectResponse;
import com.example.first_pj.exception.AppException;
import com.example.first_pj.exception.ErrorCode;
import com.example.first_pj.repository.InvalidatedRepository;
import com.example.first_pj.repository.UserRepository;
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
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedRepository InvalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token,false);
        }
        catch (AppException e )
        {
            isValid=false;
        }
        return  IntrospectResponse.builder()
                .valid(isValid)
                .build();


    }
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISITED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated)

            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .Authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("test.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cant create token",e);
            throw new RuntimeException(e);
        }
    }
    public AuthenticationResponse refreshToken (RefreshRequest request ) throws ParseException, JOSEException {
        var signJWT= verifyToken(request.getToken(),true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user= userRepository.findByUsername(username).orElseThrow(()->  new AppException(ErrorCode.USER_NOTEXISITED));
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .Authenticated(true)
                .build();

    }
    public void logout(LogoutRequest request ) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(),true);
        try {
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            InvalidatedTokenRepository.save(invalidatedToken);
        }
        catch(AppException e) {
            log.info("Token already expired time");
        }

    }
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier =new MACVerifier(SIGN_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh)
                ? new Date
                        (signedJWT.getJWTClaimsSet().getIssueTime().toInstant().
                                plus(REFRESH_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified=  signedJWT.verify(verifier);
        if(!( verified && expiryTime.after(new Date())))
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        if(InvalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    private String buildScope(User user)
    {
        StringJoiner stringJoiner =new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role->{

                stringJoiner.add("ROLE_" + role.getName());

                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().
                        forEach(permission -> stringJoiner.add(permission.getName()));
                });

        return stringJoiner.toString();
    }


}
