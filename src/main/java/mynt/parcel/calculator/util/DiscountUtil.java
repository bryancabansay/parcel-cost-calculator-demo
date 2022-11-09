package mynt.parcel.calculator.util;

import org.springframework.stereotype.Component;

/**
 * Discount util to centralize calculation of discounts.
 */
@Component
public class DiscountUtil {

  /**
   * Get the discounted price.
   *
   * @param origPrice Original value of the item.
   * @param discount  Discount in 100% format.
   * @return Discounted price.
   */
  public double getDiscountedPrice(double origPrice, double discount) {
    return origPrice * ((100 - discount) / 100);
  }
}
