package mynt.parcel.calculator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the parcel_rule table.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parcel_rule")
public class ParcelRule {
  /**
   * The primary key.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /**
   * The priority (precedence) of the rule. Lower means high value.
   */
  @Column(name = "priority", nullable = false)
  private int priority;

  /**
   * Name of the rule or classification of the parcel.
   */
  @Column(name = "rule_name", nullable = false)
  private String ruleName;

  /**
   * Boolean condition when the rule is applicable. This should be a valid javascript expression.
   * Please use weight or volume as variable names. For weight, it is in kg. For volume, it is in
   * cubic cm. For e.g. (weight > 100 || volume < 1500).
   */
  @Column(name = "condition", nullable = false)
  private String condition;

  /**
   * Mathematical expression to calculate for the cost. This should be a valid javascript
   * expression. Please use weight or volume as variable names. For volume, it is in cubic cm. The
   * number should be in pesos. For e.g. weight * 100.
   */
  @Column(name = "cost")
  private String costExpression;
}
