package media.fynema.api.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Aria2Client {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${aria2.rpc-url}")
    private String rpcUrl;

    @Value("${aria2.rpc-secret}")
    private String rpcSecret;

    public JsonNode call(String method, ArrayNode params) throws IOException {
        ObjectNode req = mapper.createObjectNode();
        req.put("jsonrpc", "2.0");
        req.put("id", "java-client");
        req.put("method", method);
        req.set("params", params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(req.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(rpcUrl, HttpMethod.POST, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new IOException("Appel RPC échoué: " + response);
        return mapper.readTree(response.getBody());
    }

    public ArrayNode baseParams() {
        ArrayNode params = mapper.createArrayNode();
        if (rpcSecret != null && !rpcSecret.isEmpty()) params.add("token:" + rpcSecret);
        return params;
    }
}
