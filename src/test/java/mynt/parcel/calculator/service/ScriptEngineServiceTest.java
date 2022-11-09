package mynt.parcel.calculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScriptEngineServiceTest {
  @Test
  private void shouldReturnCorrectEngine() {
    // setup
    var engineService = new ScriptEngineService();

    // exercise
    var engine = engineService.getScriptEngine();

    // verify
    Assertions.assertNotNull(engine);
  }
}
