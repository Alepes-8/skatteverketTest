package api;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import main.Config;

public class APISkatteverket extends Api {
    // I used the following Swagger UI to explore the API and gather the required information:
    // https://swagger.entryscape.com/?url=https%3A%2F%2Fskatteverket.entryscape.net%2Frowstore%2Fdataset%2Fb4de7df7-63c0-4e7e-bb59-1f156a591763%2Fswagger#/default/datasetQuery

    private static final int MIN_LIMIT  = Config.getInt("api.Skatteverket.min.limit");
    private static final int MAX_LIMIT  = Config.getInt("api.Skatteverket.max.limit");
    private static final int MIN_OFFSET  = Config.getInt("api.Skatteverket.min.offset");

    private static final String PARAM_PERSONAL_NUMBER  = Config.get("api.Skatteverket.param.personnummer");
    private static final String PARAM_LIMIT  = Config.get("api.Skatteverket.param.limit");
    private static final String PARAM_OFFSET  = Config.get("api.Skatteverket.param.offset");


    public APISkatteverket(HttpClient client) {
        super(client);
        this.apiAddress = Config.get("api.Skatteverket.baseUrl");
    }
    
    /*
     * Extra information on the API contact and possible inputs:
     * 
     * testpersonnummer (String) - A Swedish personal identity number (personnummer) to be used for testing purposes.
     * _limit (integer) - Number of rows to return. Range: 1–500. Default is 100.
     * _offset (integer) - The offset (results, not pages) to be used when paginating through query results. 
     *                     Example: page 3 of a multi-page result can be requested with _limit=50 and _offset=100.
     * _callback (string) - The name of the callback method to be used for JSONP.
     * 
     * Example:
     * To get 10 rows with an offset of 5, the URL would be:
     * https://skatteverket.entryscape.net/rowstore/dataset/b4de7df7-63c0-4e7e-bb59-1f156a591763?_limit=10&_offset=5
     */

    public String getSamplePersonalNumbers(String personnummer, Integer limit, Integer offset){
        String updateAPIAdress = createAPIAddress(personnummer, limit, offset);
        return getData(updateAPIAdress);
    }

    // Since the assignment is not about implementing full support for JSONP,
    // and because the callback parameter is not a requirement, I chose not to implement it.
    private String createAPIAddress(String personalNumber, Integer limit, Integer offset){

        String url = apiAddress;
        if(personalNumber == null && (limit == null || (limit <= 0 || limit > 500)) && (offset == null || offset < 0)){
            return url;
        }


        if (personalNumber != null && !personalNumber.isEmpty()){
            url += addQueryParam(url, PARAM_PERSONAL_NUMBER, personalNumber);
        }
        

        if (limit != null && limit > MIN_LIMIT && limit <= MAX_LIMIT){
            url += addQueryParam(url, PARAM_LIMIT, limit);
        }else{
            System.out.println("Input Limit must be between 1 and 500, otherwise the default of 100 will be used.");
        }

        if (offset != null && offset >= MIN_OFFSET){
            url += addQueryParam(url, PARAM_OFFSET, offset);

        }else{
            System.out.println("Input Offset must be 0 or greater, otherwise the default of 0 will be used.");
        }
        return url;
    }
    
    protected String getData(String uriAddress) {
        try {
             HttpResponse<String> response = doGet(uriAddress);
            if(response.statusCode() != 200){
                System.out.println("Error: " + response.statusCode());
                return null;
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to call API", e);
        }
    }
}
