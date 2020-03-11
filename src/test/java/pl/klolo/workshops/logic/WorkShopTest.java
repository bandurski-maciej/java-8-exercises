package pl.klolo.workshops.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class WorkShopTest {

  private WorkShop workShop;

  @BeforeEach
  public void setUp() {
    workShop = new WorkShop();
  }

  /**
   * 1.
   */

  @Test
  void findAmountOfHoldingsWithAtLeastOneCompany() {
    assertEquals(3, workShop.findAmountOfHoldingsWithAtLeastOneCompany());
  }

  @Test
  void findHoldingsNameAsList() {
    assertThat(List.of("Nestle", "Coca-Cola", "Pepsico")).hasSameElementsAs(workShop.findHoldingsNameAsList());

  }

  @Test
  void findHoldingsNameAsString() {
    String names = "Coca-Cola, Nestle, Pepsico";
    assertThat(names).containsSequence(workShop.findHoldingsNameAsString());
  }

  @Test
  void findTotalAmountOfCompanies() {
    assertThat(8).isEqualTo(workShop.findTotalAmountOfCompanies());

  }

  @Test
  void findTotalAmountOfEmployees() {
    assertThat(20).isEqualTo(workShop.findTotalAmountOfEmployees());
  }
}
