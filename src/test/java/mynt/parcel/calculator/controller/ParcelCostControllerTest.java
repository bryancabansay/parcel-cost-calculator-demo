package mynt.parcel.calculator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mynt.parcel.calculator.dto.request.ParcelCostRequest;
import mynt.parcel.calculator.dto.response.ParcelCostResponse;
import mynt.parcel.calculator.error.exception.NoCostExpressionException;
import mynt.parcel.calculator.error.exception.ParcelRuleNotFoundException;
import mynt.parcel.calculator.service.ParcelRuleService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@WebMvcTest(ParcelCostController.class)
public class ParcelCostControllerTest {

  @Captor
  private ArgumentCaptor<ParcelCostRequest> captor;

  @MockBean
  private ParcelRuleService service;

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  private MockHttpServletRequestBuilder getRequestBuilder(String weight, String width,
      String length,
      String height) {
    return get("/api/parcel/delivery-cost").param("weight", weight).param("width", width)
        .param("length", length).param("height", height);
  }

  @Test
  public void shouldReturnOkResponseWhenValidRequest() throws Exception {
    // setup
    var weight = "50";
    var width = "49";
    var length = "48";
    var height = "47";
    var mockCost = 123;
    var mockClassification = "Test Classification";
    var expectedResponse = ParcelCostResponse.builder().cost(mockCost).ruleName(mockClassification)
        .build();
    Mockito.when(service.getCostResponse(captor.capture())).thenReturn(expectedResponse);

    // exercise
    var jsonResponse = mvc.perform(getRequestBuilder(weight, width, length, height))
        .andExpect(status().isOk()).andReturn()
        .getResponse();

    // verify
    var actualResponse = mapper.readValue(jsonResponse.getContentAsString(),
        ParcelCostResponse.class);
    Assertions.assertEquals(expectedResponse.getCost(), actualResponse.getCost());
    Assertions.assertEquals(expectedResponse.getRuleName(), actualResponse.getRuleName());
    var capturedRequest = captor.getValue();
    Assertions.assertEquals(Double.parseDouble(weight), capturedRequest.getWeight());
    Assertions.assertEquals(Double.parseDouble(width), capturedRequest.getWidth());
    Assertions.assertEquals(Double.parseDouble(length), capturedRequest.getLength());
    Assertions.assertEquals(Double.parseDouble(height), capturedRequest.getHeight());
  }

  @Test
  public void shouldReturn400IfBadRequest() throws Exception {
    // setup
    // exercise
    // verify
    // zero values for each property (weight, height, length, width)
    mvc.perform(getRequestBuilder("0", "1", "1", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "0", "1", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "1", "0", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "1", "1", "0"))
        .andExpect(status().isBadRequest());
    // negative values for each property (weight, height, length, width)
    mvc.perform(getRequestBuilder("-1", "1", "1", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "-1", "1", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "1", "-1", "1"))
        .andExpect(status().isBadRequest());
    mvc.perform(getRequestBuilder("1", "1", "1", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldReturn200AndNegativeCostWhenNoCostException() throws Exception {
    // setup
    Mockito.when(service.getCostResponse(Mockito.any())).thenThrow(new NoCostExpressionException());

    // exercise
    var jsonResponse = mvc.perform(getRequestBuilder("1", "1", "1", "1"))
        .andExpect(status().isOk()).andReturn()
        .getResponse();

    // verify
    var actualResponse = mapper.readValue(jsonResponse.getContentAsString(),
        ParcelCostResponse.class);
    Assertions.assertEquals(-1, actualResponse.getCost());
  }

  @Test
  public void shouldReturn200AndNegativeCostWhenNoRuleException() throws Exception {
    // setup
    Mockito.when(service.getCostResponse(Mockito.any()))
        .thenThrow(new ParcelRuleNotFoundException());

    // exercise
    var jsonResponse = mvc.perform(getRequestBuilder("1", "1", "1", "1"))
        .andExpect(status().isOk()).andReturn()
        .getResponse();

    // verify
    var actualResponse = mapper.readValue(jsonResponse.getContentAsString(),
        ParcelCostResponse.class);
    Assertions.assertEquals(-1, actualResponse.getCost());
  }

}
