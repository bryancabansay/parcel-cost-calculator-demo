package mynt.parcel.calculator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mynt.parcel.calculator.dto.request.ParcelCostRequest;
import mynt.parcel.calculator.dto.response.ParcelCostResponse;
import mynt.parcel.calculator.error.exception.NoCostExpressionException;
import mynt.parcel.calculator.error.exception.ParcelRuleNotFoundException;
import mynt.parcel.calculator.model.ParcelRule;
import mynt.parcel.calculator.repository.ParcelRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class that handles all parcel rules related function.
 */
@Service
@RequiredArgsConstructor
public class ParcelRuleService {

  /**
   * All rules present in the database.
   */
  private static final List<ParcelRule> rules = new ArrayList<>();

  /**
   * Repository class for parcel rules.
   */
  private final ParcelRuleRepository parcelRuleRepository;

  /**
   * Script engine service.
   */
  private final ScriptEngineService scriptEngineService;

  /**
   * Replace old rules with new ones.
   *
   * @param newRules New rules to be added.
   */
  private void replaceRules(final List<ParcelRule> newRules) {
    rules.clear();
    rules.addAll(newRules);
  }

  /**
   * Populate the rules list.
   */
  @PostConstruct
  public void populateRules() {
    replaceRules(parcelRuleRepository.findAllByOrderByPriority());
  }

  /**
   * Get how to cost is calculated based on weight and height.
   *
   * @param weight in kg.
   * @param volume in cubic cm.
   * @return The correct parcel rule to be applied.
   * @throws ScriptException thrown by the engine.eval function.
   */
  public Optional<ParcelRule> getParcelRule(final double weight, final double volume)
      throws ScriptException {
    // Get a script engine from the manager
    var engine = scriptEngineService.getScriptEngine();

    // Create the bindings for both weight and volume
    var bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
    bindings.put("weight", weight);
    bindings.put("volume", volume);

    // Iterate each rule and evaluate if the condition is true or not.
    // Parcel rules are fetched by priority (ascending).
    for (var rule : rules) {
      // If the condition is correct, return the rule.
      if ((boolean) engine.eval(rule.getCondition(), bindings)) {
        return Optional.of(rule);
      }
    }

    // Return an empty optional.
    return Optional.empty();
  }

  /**
   * Get cost based on the weight and volume.
   *
   * @param costExpression Cost expression to be followed.
   * @param weight         Weight in kg.
   * @param volume         Volume in cubic cm.
   * @return Cost of the parcel.
   * @throws ScriptException thrown by the engine.eval function.
   */
  public double getCost(final String costExpression, @Positive final double weight,
      @Positive double volume)
      throws ScriptException {
    if (!StringUtils.hasLength(costExpression)) {
      throw new NoCostExpressionException();
    }

    // Get a script engine from the manager
    var engine = scriptEngineService.getScriptEngine();

    // Create the bindings for both weight and volume
    var bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
    bindings.put("weight", weight);
    bindings.put("volume", volume);

    return (Double) engine.eval(costExpression, bindings);
  }

  /**
   * Get cost response based on the parameters passed.
   *
   * @param request {@link ParcelCostRequest} object.
   * @return {@link ParcelCostResponse}
   * @throws ScriptException thrown by the engine.eval function.
   */
  public ParcelCostResponse getCostResponse(final ParcelCostRequest request)
      throws ScriptException {
    // Get the weight and volume
    var weight = request.getWeight();
    var volume = request.getVolume();

    // Get the parcel rule applicable based on
    var optional = getParcelRule(weight, volume);

    // Throw an exception if
    var rule = optional.orElseThrow(() -> {
      throw new ParcelRuleNotFoundException();
    });

    // Get cost
    var cost = getCost(rule.getCostExpression(), weight, volume);

    // Populate and return the response
    return ParcelCostResponse.builder().cost(cost).ruleName(rule.getRuleName()).build();
  }

}
