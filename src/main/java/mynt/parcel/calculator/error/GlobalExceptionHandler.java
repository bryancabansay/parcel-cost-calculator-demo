package mynt.parcel.calculator.error;

import lombok.extern.slf4j.Slf4j;
import mynt.parcel.calculator.dto.response.ParcelCostResponse;
import mynt.parcel.calculator.error.exception.NoCostExpressionException;
import mynt.parcel.calculator.error.exception.ParcelRuleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles all the exceptions.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle all the parcel rule related exceptions.
   *
   * @param ex      The runtime exception thrown.
   * @param request The web request.
   * @return {@link ResponseEntity<ParcelCostResponse>}
   */
  @ExceptionHandler({ParcelRuleNotFoundException.class, NoCostExpressionException.class})
  public ResponseEntity<ParcelCostResponse> handleParcelRuleExceptions(final RuntimeException ex,
      final WebRequest request) {
    if (log.isErrorEnabled()) {
      log.error(ex.getMessage());
      log.error("Parameters: {}", request.getParameterMap());
    }
    return new ResponseEntity<>(ParcelCostResponse.builder().cost(-1).build(), HttpStatus.OK);
  }
}
