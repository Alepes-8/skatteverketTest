package main;

import java.net.http.HttpClient;

import api.APISkatteverket;

/**
 * Only a description of the work process, not used in real life.
 * 1. Understand the assignment.
 * 2. Use the Swagger UI to test the API and understand its parameters.
 *    - https://swagger.entryscape.com/?url=https%3A%2F%2Fskatteverket.entryscape.net%2Frowstore%2Fdataset%2Fb4de7df7-63c0-4e7e-bb59-1f156a591763%2Fswagger
 * 3. Set up the project using Visual Studio Code.
 * 4. Create the APISkatteverket.java class.
 *    - Focus on how to call the API in Java directly, not through tools such as Postman or Swagger-UI.
 * 5. Adjust the implementation so that input values are correctly set based on the parameters defined by Skatteverket.
 * 6. Refactor the code to follow a more single-purpose structure by introducing a parent Api class.
 *    - This makes it easier to implement additional APIs in the future using the same reusable methods.
 * 7. Create a config.properties file to store values and parameter names globally.
 *    - This allows for easier adjustments without having to update multiple parts of the code.
 *    - A challenge for me was learning how to properly use and access this configuration.
 *      - Researching and experimenting with this gave me the current setup.
 * 8. Make adjustments for issues that occurred during development.
 *    - Ensuring the code worked as intended after each change.
 * 9. Create tests focusing on how the methods should be used (not just how they currently work).
 *    - Here I discovered a bug in createAPIAddress(), so I fixed the method instead of changing the test.
 *    - I could have used test-driven development (TDD), but that requires a clearer idea of the design and requirements in advance.
 *      - Since I was unsure, I implemented the code first and then created tests to validate the intended behavior.
 * 10. Experiment with different approaches to testing.
 *    - I considered testing the actual API responses but limited the current setup to handle only valid input values.
 *      - This restricted the scope of test cases. I also debated whether to reduce statement and branch coverage to keep the code simpler.
 */

public class Main {
    public static void main(String[] args) {
        APISkatteverket apiSkatteverket = new APISkatteverket(HttpClient.newHttpClient());
        String response = apiSkatteverket.getSamplePersonalNumbers(null, null, null);
        System.out.println(response);
    }
}