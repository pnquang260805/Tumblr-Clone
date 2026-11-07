package com.qpn.tumblrclone.common;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtil {
    private final String SECRET = "US6GThES7yPe1rofiopI9ogWqL3g8X7n2a4ogb1fLLRtusL4lBj7AiKmbzGMLt8n\n" +
            "GaySjuMkA77jZ7hAzYu2Uw==";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 giờ

    public String generateToken(Integer userId) throws JOSEException {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        // 1. Tạo signer
        JWSSigner signer = new MACSigner(SECRET);
        // 2. Chuẩn bị đối tượng JWS (Header + Payload)
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("expiry", expiry);
        JWSObject object = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(payload)
        );
        // 3. Ký dữ liệu
        object.sign(signer);
//      4. Chuyển đổi đối tượng JWS thành một chuỗi duy nhất để dễ dàng truyền đi qua HTTP/URL.
//      Chuỗi trả về có dạng: [Header].[Payload].[Signature] (ngăn cách bởi 2 dấu chấm .)
        String token = object.serialize();
        return token;
    }

    public boolean verifyToken(String token) throws ParseException, JOSEException {
        // 1. parse chuỗi String thành JWSObject
        JWSObject object = JWSObject.parse(token);
        // 2. Tạo Verifier với secret key
        JWSVerifier verifier = new MACVerifier(SECRET);
        // 3. Tiến hành kiểm tra chữ ký
        return object.verify(verifier);
    }

    public Map<String, Object> getPayload(String token) throws ParseException {
        JWSObject object = JWSObject.parse(token);
        return object.getPayload().toJSONObject();
    }

    public String parseAuthHeader(String authHeader){
        String token = authHeader;
        if(authHeader != null && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7);
        return token;
    }
}
