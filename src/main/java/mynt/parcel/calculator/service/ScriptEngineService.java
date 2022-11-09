package mynt.parcel.calculator.service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.springframework.stereotype.Service;

/**
 * Handles script engine related functions.
 */
@Service
public class ScriptEngineService {

  /**
   * Creates new script engine instances.
   */
  private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

  /**
   * Get script engine to be used.
   *
   * @return nashorn ScriptEngine
   */
  public ScriptEngine getScriptEngine() {
    return scriptEngineManager.getEngineByName("nashorn");
  }
}
