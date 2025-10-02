package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Base API class. 
 * 
 * This class can be extended to create specific API clients (e.g., Skatteverket API).
 * The idea is to reuse common HTTP functionality across multiple APIs.
 */
public class Api {
    protected final HttpClient client;
    protected String apiAddress;
    
    Api(HttpClient client){
        this.client = client;
    }

    /**
     * Adds a query parameter to a URL, inserting either "?" or "&" 
     * depending on whether the URL already contains parameters.
     * 
     * @param url The base URL
     * @param paramName The name of the query parameter
     * @param value The value of the query parameter (ignored if null)
     * @return The URL with the appended query parameter
     */
    protected String addQueryParam(String url, String paramName, Object value) {
        if (value == null) return url;
        String prefix = url.contains("?") ? "&" : "?";
        return String.format("%s%s=%s", prefix, paramName, value);
    }
    
    /**
     * Sends a GET request to the provided URI.
     * Handling of the response is left to the subclass.
     * 
     * @param uriAddress The target URI
     * @return HttpResponse<String> containing the response body
     */    
    protected HttpResponse<String> doGet(String uriAddress) throws IOException, InterruptedException {
         HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriAddress))
                .GET()
                .build();
        
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
