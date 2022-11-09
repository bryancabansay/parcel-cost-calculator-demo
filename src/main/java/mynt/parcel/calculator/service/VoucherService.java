package mynt.parcel.calculator.service;

import io.swagger.client.ApiException;
import io.swagger.client.api.VoucherApi;
import io.swagger.client.model.VoucherItem;
import lombok.RequiredArgsConstructor;
import mynt.parcel.calculator.config.VoucherServiceConfig;
import org.springframework.stereotype.Service;

/**
 * Service handling all the voucher related requests.
 */
@Service
@RequiredArgsConstructor
public class VoucherService {

  /**
   * Configuration object containing the base path and the api key.
   */
  private final VoucherServiceConfig config;

  /**
   * Get a voucher api object.
   *
   * @return {@link VoucherApi} object.
   */
  public VoucherApi getVoucherApi() {
    var api = new VoucherApi();
    api.getApiClient().setBasePath(config.getBasePath());
    return api;
  }

  /**
   * Get voucher item from the other service.
   *
   * @param code Voucher code to be checked.
   * @return {@link VoucherItem} object
   * @throws ApiException thrown exception.
   */
  public VoucherItem getVoucherByCode(final String code) throws ApiException {
    var api = getVoucherApi();
    return api.voucher(code, config.getApiKey());
  }
}
