package mynt.parcel.calculator.service;

import java.util.ArrayList;
import java.util.Collections;
import javax.script.ScriptException;
import mynt.parcel.calculator.dto.request.ParcelCostRequest;
import mynt.parcel.calculator.error.exception.NoCostExpressionException;
import mynt.parcel.calculator.error.exception.ParcelRuleNotFoundException;
import mynt.parcel.calculator.model.ParcelRule;
import mynt.parcel.calculator.repository.ParcelRuleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ParcelRuleServiceTest {

  @Mock
  private ParcelRuleRepository repository;

  @Before
  public void setup() {
    var rejectRule = new ParcelRule(1, 1, "Reject", "weight > 50", null);
    var heavy = new ParcelRule(2, 2, "Heavy Parcel", "weight > 10", "20 * weight");
    var small = new ParcelRule(3, 3, "Small Parcel", "volume < 1500", "0.03 * volume");
    var medium = new ParcelRule(4, 4, "Medium Parcel", "volume < 2500", "0.04 * volume");
    var large = new ParcelRule(5, 5, "Large Parcel", "volume >= 2500", "0.05 * volume");

    var list = new ArrayList<ParcelRule>();
    list.add(rejectRule);
    list.add(heavy);
    list.add(small);
    list.add(medium);
    list.add(large);

    Mockito.when(repository.findAllByOrderByPriority()).thenReturn(list);
  }

  @Test(expected = NoCostExpressionException.class)
  public void shouldThrowExceptionIfRejectParcel() throws ScriptException {
    // setup
    var service = new ParcelRuleService(repository, new ScriptEngineService());
    service.populateRules();
    var rejectReq = new ParcelCostRequest(51, 2500, 1, 1);

    // exercise
    // verify
    service.getCostResponse(rejectReq);
  }

  @Test(expected = ParcelRuleNotFoundException.class)
  public void shouldThrowExceptionIfRuleNotPresent() throws ScriptException {
    // setup
    var rejectRule = new ParcelRule(1, 1, "Reject", "weight > 50", null);
    Mockito.when(repository.findAllByOrderByPriority())
        .thenReturn(Collections.singletonList(rejectRule));
    var service = new ParcelRuleService(repository, new ScriptEngineService());
    service.populateRules();

    //exercise
    // verify
    service.getCostResponse(new ParcelCostRequest(5, 1, 1, 1));
  }

  @Test
  public void shouldReturnCorrectParcelCostResponse() throws ScriptException {
    // setup
    var service = new ParcelRuleService(repository, new ScriptEngineService());
    service.populateRules();
    var heavyReq = new ParcelCostRequest(50, 2500, 1, 1);
    var smallReq = new ParcelCostRequest(10, 1499, 1, 1);
    var mediumReq = new ParcelCostRequest(10, 2499, 1, 1);
    var largeReq = new ParcelCostRequest(10, 2500, 1, 1);

    // exercise
    var heavyRes = service.getCostResponse(heavyReq);
    var smallRes = service.getCostResponse(smallReq);
    var mediumRes = service.getCostResponse(mediumReq);
    var largeRes = service.getCostResponse(largeReq);

    // verify
    Assertions.assertEquals("Heavy Parcel", heavyRes.getRuleName());
    Assertions.assertEquals(20 * 50, heavyRes.getCost());
    Assertions.assertEquals("Small Parcel", smallRes.getRuleName());
    Assertions.assertEquals(0.03 * 1499, smallRes.getCost());
    Assertions.assertEquals("Medium Parcel", mediumRes.getRuleName());
    Assertions.assertEquals(0.04 * 2499, mediumRes.getCost());
    Assertions.assertEquals("Large Parcel", largeRes.getRuleName());
    Assertions.assertEquals(0.05 * 2500, largeRes.getCost());
  }
}
