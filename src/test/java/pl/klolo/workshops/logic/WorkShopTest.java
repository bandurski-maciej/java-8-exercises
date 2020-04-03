package pl.klolo.workshops.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.domain.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  @Test
  void findCompaniesNamesAsList() {
    assertThat(List.of("Fanta", "Sprite", "Nescafe", "Gerber", "Nestea", "Lays", "Pepsi", "Mirinda"))
      .hasSameElementsAs(workShop.findCompaniesNamesAsList());

  }

  @Test
  void findCompaniesAsLinkedList() {
    assertThat(List.of("Fanta", "Sprite", "Nescafe", "Gerber", "Nestea", "Lays", "Pepsi", "Mirinda"))
      .hasSameElementsAs(workShop.findCompaniesAsLinkedList());
    assertThat(workShop.findCompaniesAsLinkedList()).isInstanceOf(LinkedList.class);

  }

  @Test
  void findCompaniesAsStringWithPlusDelimiter() {
    String companies = "Names: Fanta + Gerber + Lays + Mirinda + Nescafe + Nestea + Pepsi + Sprite.";
    assertThat(companies).contains(workShop.findCompaniesAsStringWithPlusDelimiter());

  }

  @Test
  void findCompaniesAsStringWithPlusDelimiterAsStringBuilder() {
    String companies = "Names: Fanta + Gerber + Lays + Mirinda + Nescafe + Nestea + Pepsi + Sprite";
    assertThat(companies).contains(workShop.findCompaniesAsStringWithPlusDelimiterAsStringBuilder());
  }

  @Test
  void findAccountsAmount() {
    assertThat(35).isEqualTo(workShop.findAccountsAmount());
  }

  @Test
  void findAllCurrenciesAsString() {
    assertEquals("CHF, EUR, PLN, USD",
      workShop.findAllCurrenciesAsString());
  }

  @Test
  void getAllCurrenciesUsingGenerate() {
    assertEquals("CHF, EUR, PLN, USD",
      workShop.getAllCurrenciesUsingGenerate());
  }

  @Test
  void findAmountOfWomen() {
    assertEquals(4, workShop.findAmountOfWomen());
  }

  @Test
  void calculateToPLN() {
    final Account account = Account.builder()
      .amount(new BigDecimal("1.0"))
      .currency(Currency.PLN)
      .build();

    assertEquals(new BigDecimal("1.00"), workShop.calculateToPLN(account));
  }

  @Test
  void calculateListToPLN() {
    final Account account = Account.builder()
      .amount(new BigDecimal("3.72"))
      .currency(Currency.USD)
      .build();

    final Account account1 = Account.builder()
      .amount(new BigDecimal("1.0"))
      .currency(Currency.PLN)
      .build();

    assertThat(List.of(new BigDecimal("1.00"), new BigDecimal("13.84")))
      .hasSameElementsAs(workShop.calculateListToPLN(List.of(account, account1)));
  }

  @Test
  void calculateSumToPLN() {
    final Account account = Account.builder()
      .amount(new BigDecimal("100"))
      .currency(Currency.USD)
      .build();

    final Account account1 = Account.builder()
      .amount(new BigDecimal("200"))
      .currency(Currency.PLN)
      .build();

    assertThat(new BigDecimal("572.00")).isEqualByComparingTo(workShop.calculateSumToPLN(List.of(account, account1)));
  }

  @Test
  void findNamesByPredicate() {
    assertThat(Set.of("Adam", "Alfred", "Amadeusz"))
      .containsExactlyInAnyOrderElementsOf(workShop.findNamesByPredicate(user -> user.getFirstName().startsWith("A")));
    assertThat(11).isEqualTo(workShop.findNamesByPredicate(user -> user.getSex().equals(Sex.MAN)).size());

  }

  @Test
  void findOlderThanNotManNamesAsList() {
    assertThat(List.of("Marta", "Mariusz", "Magdalena", "Zosia"))
      .hasSameElementsAs(workShop.findOlderThanNotManNamesAsList(30));

  }

  @Test
  void executeForEachCompany() {
    List<String> companyNames = new ArrayList<>();
    workShop.executeForEachCompany(company -> companyNames.add(company.getName()));
    assertThat(List.of("Fanta", "Sprite", "Nescafe", "Gerber", "Nestea", "Lays", "Pepsi", "Mirinda"))
      .hasSameElementsAs(companyNames);

  }

  @Test
  void findRichestWoman() throws Exception {
    assertThat("Zosia").isEqualTo(workShop.findRichestWoman().getFirstName());
  }

  @Test
  void findFistNCompaniesNames() {
    assertThat(2).isEqualTo(workShop.findFistNCompaniesNames(2).size());
  }

  @Test
  void findMostPopularAccountType() {
    assertThat(AccountType.ROR1).isEqualTo(workShop.findMostPopularAccountType());
  }

  @Test
  void findUserMatchedToPredicate() {
    assertThat("Bartek").isEqualTo(workShop.findUserMatchedToPredicate(user -> user.getFirstName()
      .equals("Bartek")).getFirstName());
  }

  @Test
  void getCompanyMapWithEmployeeAmount() {

    assertThat(workShop.getCompanyMapWithEmployeeAmount()).contains(Map.entry("Fanta", 3));
    assertThat(workShop.getCompanyMapWithEmployeeAmount()).contains(Map.entry("Sprite", 2));
    assertThat(workShop.getCompanyMapWithEmployeeAmount()).contains(Map.entry("Nescafe", 4));

  }

  @Test
  void getCompanyMapWithEmployeeAmountAsString() {
    assertThat(workShop.getCompanyMapWithEmployeeAmountAsString())
      .contains(Map.entry("Fanta", List.of("Adam Wojcik", "Mateusz Kowalski", "Bartek Pasibrzuch").toString()));

  }

  @Test
  void getCompanyMapWithEmployeeAmountAsList() {
    assertThat(workShop.getCompanyMapWithEmployeeAmountAsList(
      user -> user.getFirstName() + " " + user.getLastName()))
      .contains(Map.entry("Fanta", List.of("Adam Wojcik", "Mateusz Kowalski", "Bartek Pasibrzuch")));

  }

  @Test
  void getUserMapWithUserGenderAndAmount() {
    assertThat(workShop.getUserMapWithUserGenderAndAmount()).contains(Map.entry(true, 4L));
    assertThat(workShop.getUserMapWithUserGenderAndAmount()).contains(Map.entry(false, 13L));

  }

  @Test
  void getAccountsMapWithAccountNumberAndAccount() {
    assertThat(workShop.getAccountsMapWithAccountNumberAndAccount()).contains(Map.entry("8474",
      workShop.getAccountStream()
        .filter(account -> account.getNumber().equals("8474"))
        .findFirst()
        .orElseThrow()));
  }

  @Test
  void findUserNamesAsString() {
    assertThat(workShop.findUserNamesAsString()).contains("Adam Alfred");
  }

  @Test
  void getUserSetLimitedTo10() {
    assertThat(workShop.getUserSetLimitedTo10()).hasSize(10);
    assertThat(workShop.getUserSetLimitedTo10()).isSubsetOf(workShop.getUserStream().collect(Collectors.toSet()));
  }

  @Test
  public void shouldSaveAccountsListInFile() throws IOException {
    workShop.saveAccountsInFile("accounts.txt");
  }

  @Test
  void findUser() {
    assertThat("Bartek").isEqualTo(workShop.findUserMatchedToPredicate(user -> user.getFirstName()
      .equals("Bartek")).getFirstName());
  }

  @Test
  void shouldReturnInfoAboutUserAge() {
    assertThat(workShop.getInfoAboutUserAge(Optional.of(workShop.findUser(user1 -> user1.getFirstName()
      .equals("Bartek")))))
      .contains("BARTEK PASIBRZUCH ma lat 18");
    assertThat(workShop.getInfoAboutUserAge(Optional.empty()))
      .contains("Brak użytkownika");
  }

  @Test
  void shouldReturnNamesAndSurnamesSortedReversed() {
    assertThat(workShop.getNamesAndSurnamesSortedReversed()).contains("Zosia Psikuta, Zenon Kucowski, Zenek Biednapalka");
  }

  @Test
  void shouldReturnMapWithAccountTypeAndAmountDenominatedInPLN() {
    assertThat(workShop.getMapWithAccountTypeAndAmountDenominatedInPLN()).contains(Map.entry(AccountType.ROR2, new BigDecimal(427948)));
  }

  @Test
  void shouldReturnSquareSumOfAge() {
    assertThat(workShop.getSquareSumOfAge()).isEqualTo(27720);
  }

  @Test
  void shouldReturnNUniqueUsers() {
    assertThat(workShop.getNUniqueUsers(5)).hasSize(5);
    assertThrows(IllegalArgumentException.class, () -> workShop.getNUniqueUsers(100));
  }

  @Test
  void shouldReturnMapWithMapContainingUsersAndAmount() {
    Map<Stream<AccountType>, Map<User, BigDecimal>> manWithSumMoneyOnAccountsTest =
      workShop.getMapWithAccountTypeKeyAndSumMoneyForManInPLN();
    assertEquals(8, manWithSumMoneyOnAccountsTest.size());
    assertFalse(manWithSumMoneyOnAccountsTest.isEmpty());
  }

  @Test
  void getSumOfMoneyForOtherSex() {

    BigDecimal sumMoneyOnAccountsForPeopleOtherInPLN = workShop.getSumOfMoneyForOtherSex();

    assertEquals(new BigDecimal("1667.000"), sumMoneyOnAccountsForPeopleOtherInPLN.setScale(3, RoundingMode.HALF_UP));
    assertNotEquals(new BigDecimal("1666.000"), sumMoneyOnAccountsForPeopleOtherInPLN);
    assertNotNull(sumMoneyOnAccountsForPeopleOtherInPLN);
    assertNotSame(Integer.TYPE, sumMoneyOnAccountsForPeopleOtherInPLN.getClass());
  }

  @Test
  void shouldReturnPartitionedUsersByAge() {
    assertThat(19L).isEqualTo(workShop.getPartitionedUsersByAge().get(true));
  }

}


