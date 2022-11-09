package mynt.parcel.calculator.service;

import io.swagger.client.ApiException;
import io.swagger.client.api.VoucherApi;
import io.swagger.client.model.VoucherItem;
import mynt.parcel.calculator.config.VoucherServiceConfig;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class VoucherServiceTest {

  @Mock
  private VoucherServiceConfig config;

  @InjectMocks
  private VoucherService voucherService;

  @Test
  public void shouldSetCorrectBasePath() {
    // setup
    // exercise
    var voucherApi = voucherService.getVoucherApi();

    // verify
    Assertions.assertNotNull(voucherApi);
  }

  @Test
  public void shouldPassCorrectArguments() throws ApiException {
    // setup
    var code = "MYNTCODE";
    var apiKey = "apikey";
    var config = new VoucherServiceConfig();
    config.setApiKey(apiKey);
    var service = Mockito.spy(new VoucherService(config));
    var mockApi = Mockito.mock(VoucherApi.class);
    Mockito.doReturn(mockApi).when(service).getVoucherApi();
    var expected = new VoucherItem();
    Mockito.when(mockApi.voucher(code, apiKey)).thenReturn(expected);

    // exercise
    var actual = service.getVoucherByCode(code);

    // verify
    Assertions.assertEquals(expected, actual);
  }
}
