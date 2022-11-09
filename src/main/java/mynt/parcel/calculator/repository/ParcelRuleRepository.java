package mynt.parcel.calculator.repository;

import java.util.List;
import mynt.parcel.calculator.model.ParcelRule;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the parcel_rule table.
 */
public interface ParcelRuleRepository extends CrudRepository<ParcelRule, Integer> {

  /**
   * Get all the parcel rules in the database. This is sorted by priority.
   *
   * @return List of {@link ParcelRule} objects.
   */
  List<ParcelRule> findAllByOrderByPriority();
}
