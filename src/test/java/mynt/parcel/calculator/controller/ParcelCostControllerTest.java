package mynt.parcel.calculator.controller;

import java.util.ArrayList;
import mynt.parcel.calculator.model.ParcelRule;
import mynt.parcel.calculator.repository.ParcelRuleRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ParcelCostController.class)
public class ParcelCostControllerTest {

  @MockBean
  private ParcelRuleRepository repository;

  @Autowired
  private MockMvc mvc;

  @Before
  public void setup() {
    var rejectRule = new ParcelRule(1, 1, "Reject", "weight > 50", null);
    var heavy = new ParcelRule(2, 2, "Heavy Parcel", "weight > 10", "20 * weight");
    var small = new ParcelRule(3, 3, "Small Parcel", "volume < 1500", "0.03 * volume");
    var medium = new ParcelRule(4, 4, "Medium Parcel", "volume < 2500", "0.04 * volume");
    var large = new ParcelRule(5, 5, "Large Parcel", "volume >= 2500", "0.05 * volume");

    var list = new ArrayList<ParcelRule>();
    list.add(rejectRule);
    list.add(heavy);
    list.add(small);
    list.add(medium);
    list.add(large);

    Mockito.when(repository.findAllByOrderByPriority()).thenReturn(list);
  }

//  @Test
//  public void should

}
