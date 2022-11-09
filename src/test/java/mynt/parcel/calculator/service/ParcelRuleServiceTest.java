package mynt.parcel.calculator.service;

import io.swagger.client.ApiException;
import io.swagger.client.model.VoucherItem;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.script.ScriptException;
import mynt.parcel.calculator.dto.request.ParcelCostRequest;
import mynt.parcel.calculator.error.exception.NoCostExpressionException;
import mynt.parcel.calculator.error.exception.ParcelRuleNotFoundException;
import mynt.parcel.calculator.model.ParcelRule;
import mynt.parcel.calculator.repository.ParcelRuleRepository;
import mynt.parcel.calculator.util.DiscountUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ParcelRuleServiceTest {

  private final DiscountUtil discountUtil = new DiscountUtil();

  public static final DecimalFormat format = new DecimalFormat("0.00");

  @Mock
  private ParcelRuleRepository repository;

  @Mock
  private VoucherService voucherService;

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
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);
    service.populateRules();
    var rejectReq = new ParcelCostRequest(51, 2500, 1, 1, null);

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
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);
    service.populateRules();

    //exercise
    // verify
    service.getCostResponse(new ParcelCostRequest(5, 1, 1, 1, null));
  }

  private Double getFormattedDouble(double val) {
    return Double.parseDouble(format.format(val));
  }

  @Test
  public void shouldReturnCorrectParcelCostResponse() throws ScriptException, ApiException {
    // setup
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);
    service.populateRules();
    var code = "test-code";
    var heavyReq = new ParcelCostRequest(50, 2500, 1, 1, null);
    var smallReq = new ParcelCostRequest(10, 1499, 1, 1, null);
    var mediumReq = new ParcelCostRequest(10, 2499, 1, 1, null);
    var largeReq = new ParcelCostRequest(10, 2500, 1, 1, code);
    var discount = 25.0f;
    var voucherItem = new VoucherItem();
    voucherItem.setDiscount(discount);
    Mockito.when(voucherService.getVoucherByCode(code)).thenReturn(voucherItem);

    // exercise
    var heavyRes = service.getCostResponse(heavyReq);
    var smallRes = service.getCostResponse(smallReq);
    var mediumRes = service.getCostResponse(mediumReq);
    var largeRes = service.getCostResponse(largeReq);

    // verify
    Assertions.assertEquals("Heavy Parcel", heavyRes.getRuleName());
    Assertions.assertEquals(getFormattedDouble(20 * 50), heavyRes.getCost());
    Assertions.assertEquals("Small Parcel", smallRes.getRuleName());
    Assertions.assertEquals(getFormattedDouble(0.03 * 1499), smallRes.getCost());
    Assertions.assertEquals("Medium Parcel", mediumRes.getRuleName());
    Assertions.assertEquals(getFormattedDouble(0.04 * 2499), mediumRes.getCost());
    Assertions.assertEquals("Large Parcel", largeRes.getRuleName());
    Assertions.assertEquals(getFormattedDouble(0.05 * 2500 * ((100 - discount) / 100)),
        largeRes.getCost());
  }

  @Test
  public void shouldProceedIfNoVoucherCode() {
    // setup
    var expected = 1234;
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);

    // exercise
    var actual = service.checkDiscount(expected, null);

    // verify
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void shouldProceedIfExceptionOccurs() throws ApiException {
    // setup
    var expected = 1234;
    var code = "Code";
    Mockito.when(voucherService.getVoucherByCode(code)).thenThrow(new ApiException());
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);

    // exercise
    var actual = service.checkDiscount(expected, code);

    // verify
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void shouldApplyCorrectDiscount() throws ApiException {
    // setup
    var expected = 1234;
    var code = "Code";
    var voucherItem = new VoucherItem();
    var discount = 50.0f;
    voucherItem.setDiscount(discount);
    Mockito.when(voucherService.getVoucherByCode(code)).thenReturn(voucherItem);
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);

    // exercise
    var actual = service.checkDiscount(expected, code);

    // verify
    Assertions.assertEquals(expected * ((100 - discount) / 100), actual);
  }

  @Test
  public void shouldProceedIfNullPointerExceptionOccurs() throws ApiException {
    // setup
    var expected = 1234;
    var code = "Code";
    Mockito.when(voucherService.getVoucherByCode(code)).thenReturn(new VoucherItem());
    var service = new ParcelRuleService(repository, new ScriptEngineService(), voucherService,
        discountUtil);

    // exercise
    var actual = service.checkDiscount(expected, code);

    // verify
    Assertions.assertEquals(expected, actual);
  }
}
