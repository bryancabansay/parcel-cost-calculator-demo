package mynt.parcel.calculator.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for the parcel cost.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelCostRequest {

  /**
   * Weight in kg.
   */
  @NotNull
  @Positive
  private double weight;

  /**
   * Height in cm.
   */
  @NotNull
  @Positive
  private double height;

  /**
   * Width in cm.
   */
  @NotNull
  @Positive
  private double width;

  /**
   * Length in cm.
   */
  @NotNull
  @Positive
  private double length;

  /**
   * Voucher code as string.
   */
  private String voucherCode;

  /**
   * Get the volume.
   *
   * @return Returns the volume of the parcel.
   */
  public double getVolume() {
    return height * width * length;
  }
}
