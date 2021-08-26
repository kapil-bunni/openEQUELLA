package io.github.openequella.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tle.webtests.framework.TestConfig;
import com.tle.webtests.framework.TestInstitution;
import io.github.openequella.rest.models.BaseEntitySummary;
import java.io.IOException;
import java.util.List;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import org.testng.annotations.Test;

@TestInstitution("fiveo")
public class AdvancedSearchApiTest extends AbstractRestApiTest {
  private final String ADVANCEDSEARCH_API_ENDPOINT =
      getTestConfig().getInstitutionUrl() + "api/settings/advancedsearch";

  @Override
  protected TestConfig getTestConfig() {
    if (testConfig == null) {
      testConfig = new TestConfig(AdvancedSearchApiTest.class);
    }
    return testConfig;
  }

  @Test
  public void testRetrieveAdvancedSearches() throws Exception {
    final List<BaseEntitySummary> searches = getAdvancedSearches();
    assertEquals(
        "The number of returned advanced searches should match the institution total.",
        3,
        searches.size());
  }

  @Test(description = "list all Wizard controls of an Sdvanced search")
  public void testRetrieveWizardDefinition() throws IOException {
    final String ADVANCED_SEARCH_UUID = "c9fd1ae8-0dc1-ab6f-e923-1f195a22d537";
    final HttpMethod method =
        new GetMethod(ADVANCEDSEARCH_API_ENDPOINT + "/" + ADVANCED_SEARCH_UUID);
    assertEquals(HttpStatus.SC_OK, makeClientRequest(method));
    JsonNode result = mapper.readTree(method.getResponseBody());
    // This Advanced Search has 13 controls.
    assertEquals(13, result.size());
    result.forEach(
        control -> {
          assertNotNull(control.get("controlType"));
        });
  }

  private List<BaseEntitySummary> getAdvancedSearches() throws IOException {
    final HttpMethod method = new GetMethod(ADVANCEDSEARCH_API_ENDPOINT);
    assertEquals(HttpStatus.SC_OK, makeClientRequest(method));
    return mapper.readValue(
        method.getResponseBodyAsString(), new TypeReference<List<BaseEntitySummary>>() {});
  }
}
