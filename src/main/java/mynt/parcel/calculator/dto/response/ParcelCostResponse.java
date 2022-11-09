package mynt.parcel.calculator.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Represents the response to the get parcel cost request.
 */
@Data
@Builder
public class ParcelCostResponse {

  /**
   * Classification of the parcel based on set rules.
   */
  @JsonProperty("classification")
  private String ruleName;

  /**
   * Cost of the parcel.
   * -1 is returned if it should be rejected.
   */
  private double cost;
}
