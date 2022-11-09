package mynt.parcel.calculator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Represents the configuration objects for the voucher service.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "voucher-service")
public class VoucherServiceConfig {

  /**
   * API key for the request.
   */
  private String apiKey;
}
