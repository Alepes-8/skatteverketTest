package api;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.Config;

class APISkatteverketTest {
    /**
     * Positivt testfall:
     * Verifierar att anropet till API:et returnerar korrekt information när giltiga parametrar skickas in.
     * Vi testar med ett känt testpersonnummer och standardvärden för limit och offset.
     * 
     * Förväntat resultat:
     * - resultCount = 1
     * - offset = 0
     * - limit = 100
     * - results[0].testpersonnummer = det personnummer vi skickade in
     */  
    @Test
    public void testGetInfo() throws Exception {
        //Arrange
        APISkatteverket apiSkatteverket = new APISkatteverket(HttpClient.newHttpClient());
        int resultCount = 1;
        int limit = 100;
        int offset = 0;
        String personalNumber =  Config.get("test.api.Skatteverket.personnummer");

        //Act
        String response = apiSkatteverket.getSamplePersonalNumbers(personalNumber, limit, offset);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        //Assert
        assertEquals(root.get("resultCount").asInt(), resultCount);
        assertEquals(root.get("offset").asInt(), offset);
        assertEquals(root.get("limit").asInt(), limit);
        assertEquals(root.get("results").get(0).get("testpersonnummer").asText(), personalNumber);
    }

    /**
     * Negativt testfall:
     * Verifierar att felaktiga parametrar inte ger samma resultat som vid korrekta värden.
     * Vi testar med: ogiltigt personnummer, limit som är större än tillåtet maxvärde, offset som är mindre än min värdet
     * 
     * Förväntat resultat:
     * - resultCount != 1
     * - offset != det ogiltiga värdet
     * - limit != det ogiltiga värdet
     * - results[0] ska vara null (inget resultat returneras)
     */   
    @Test
    public void givenIncorrectInput_whenGetSamplePersonalNumbers_thenDefulatValuesAreSetWithPersonalNumber() throws Exception {
        //Arrange
        APISkatteverket apiSkatteverket = new APISkatteverket(HttpClient.newHttpClient());
        int resultCount = 1;
        int limit = Config.getInt("api.Skatteverket.max.limit") * 2;
        int offset = Config.getInt("api.Skatteverket.min.offset") - 1;
        String personalNumber =  Config.get("test.api.Skatteverket.incorrect.personnummer");

        //Act
        String response = apiSkatteverket.getSamplePersonalNumbers(personalNumber, limit, offset);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        
        //Assert
        assertNotEquals(resultCount, root.get("resultCount").asInt());
        assertNotEquals(offset, root.get("offset").asInt());
        assertNotEquals(limit, root.get("limit").asInt());
        assertEquals(null, root.get("results").get(0));
    }
}
