package mynt.parcel.calculator.controller;

import javax.script.ScriptException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import mynt.parcel.calculator.dto.request.ParcelCostRequest;
import mynt.parcel.calculator.dto.response.ParcelCostResponse;
import mynt.parcel.calculator.service.ParcelRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for getting the delivery cost.
 */
@RestController
@RequestMapping("/api/parcel/delivery-cost")
@RequiredArgsConstructor
public class ParcelCostController {

  /**
   * Handles parcel rules related functions.
   */
  private final ParcelRuleService parcelRuleService;

  /**
   * Get parcel cost depending on the passed values.
   *
   * @param request {@link ParcelCostRequest}
   * @return {@link ParcelCostResponse}
   * @throws ScriptException thrown by the script engine.
   */
  @GetMapping
  public ParcelCostResponse getParcelCost(@Valid ParcelCostRequest request)
      throws ScriptException {
    return parcelRuleService.getCostResponse(request);
  }
}
