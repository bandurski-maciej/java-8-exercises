package pl.klolo.workshops.logic;

import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.mock.HoldingMockGenerator;
import pl.klolo.workshops.mock.UserMockGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.summingInt;

class WorkShop {
  /**
   * Lista holdingów wczytana z mocka.
   */
  private final List<Holding> holdings;

  private final Predicate<User> isWoman = user -> user.getSex().equals(Sex.WOMAN);
  private Predicate<User> isMan = m -> m.getSex() == Sex.MAN;

  WorkShop() {
    final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
    holdings = holdingMockGenerator.generate();
  }

  /**
   * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
   */

  long findAmountOfHoldingsWithAtLeastOneCompany() {
    return holdings.stream()
      .filter(holding -> holding.getCompanies().size() >= 1)
      .count();

  }

  /**
   * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
   */

  List<String> findHoldingsNameAsList() {
    return holdings.stream()
      .map(Holding::getName)
      .collect(Collectors.toList());

  }


  /**
   * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane.
   * String ma postać: (Coca-Cola, Nestle, Pepsico)
   */

  String findHoldingsNameAsString() {
    return holdings.stream()
      .map(Holding::getName)
      .sorted()
      .collect(Collectors.joining(", ", "", ""));
  }


  /**
   * Zwraca liczbę firm we wszystkich holdingach.
   */

  long findTotalAmountOfCompanies() {
    return holdings.stream()
      .map(holding -> holding.getCompanies().size())
      .reduce(0, Integer::sum);
  }


  /**
   * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
   */

  int findTotalAmountOfEmployees() {
    return holdings.stream()
      .flatMap(holding -> holding.getCompanies().stream()
        .map(company -> company.getUsers().size()))
      .reduce(0, Integer::sum);

  }

  /**
   * Zwraca listę wszystkich nazw firm w formie listy. Tworzenie strumienia firm umieść w osobnej metodzie którą
   * później będziesz wykorzystywać.
   */

  public List<String> findCompaniesNamesAsList() {
    return findCompaniesAsStream()
      .map(Company::getName)
      .collect(Collectors.toList());

  }

  /**
   * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy
   * po zakończeniu działania strumienia.
   */

  public LinkedList<String> findCompaniesAsLinkedList() {
    return findCompaniesAsStream()
      .map(Company::getName)
      .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Zwraca listę firm jako String gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
   */

  public String findCompaniesAsStringWithPlusDelimiter() {
    return findCompaniesAsStream()
      .map(Company::getName)
      .sorted()
      .collect(Collectors.joining(" + ", "Names: ", "."));
  }


  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+".
   * Używamy collect i StringBuilder.
   * <p>
   * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
   */

  public String findCompaniesAsStringWithPlusDelimiterAsStringBuilder() {
    AtomicBoolean first = new AtomicBoolean(false);
    return findCompaniesAsStream()
      .map(Company::getName)
      .sorted()
      .collect(Collector.of(StringBuilder::new,
        (stringBuilder, s) -> {
          if (first.getAndSet(true)) stringBuilder.append(" + ");
          stringBuilder.append(s);
        },
        StringBuilder::append,
        StringBuilder::toString));

  }

  /**
   * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
   */

  public long findAccountsAmount() {
    return findCompaniesAsStream()
      .flatMap(company -> company.getUsers().stream()
        .flatMap(user -> user.getAccounts().stream()))
      .count();

  }

  /**
   * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości
   * występują bez powtórzeń i są posortowane.
   */

  public String findAllCurrenciesAsString() {
    return findCompaniesAsStream()
      .flatMap(company -> company.getUsers().stream()
        .flatMap(user -> user.getAccounts().stream()))
      .map(Account::getCurrency)
      .map(Enum::toString)
      .distinct()
      .sorted()
      .collect(Collectors.joining(", "));
  }

  /**
   * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody
   * stream, tylko skorzystaj z  Stream.generate. Wspólny kod wynieś do osobnej metody.
   */

  String getAllCurrenciesUsingGenerate() {
    final List<String> currencies = getAllCurrenciesToListAsString();

    return Stream.generate(currencies.iterator()::next)
      .limit(currencies.size())
      .distinct()
      .sorted()
      .collect(Collectors.joining(", "));
  }

  private List<String> getAllCurrenciesToListAsString() {
    return findCompaniesAsStream()
      .flatMap(company -> company.getUsers().stream())
      .flatMap(user -> user.getAccounts().stream())
      .map(Account::getCurrency)
      .map(c -> Objects.toString(c, null))
      .collect(Collectors.toList());
  }


  /**
   * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień użytkowników umieść
   * w osobnej metodzie. Predicate określający czy mamy do czynienia z kobietą niech będzie polem statycznym w klasie.
   */


  public Stream<User> getUserStream() {
    return findCompaniesAsStream()
      .flatMap(company -> company.getUsers().stream());
  }


  /**
   * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency.
   */

  public BigDecimal calculateToPLN(Account account) {
    return account
      .getAmount()
      .multiply(BigDecimal.valueOf(account.getCurrency().rate))
      .round(new MathContext(4, RoundingMode.HALF_UP));

  }


  /**
   * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency i sumuje ją.
   */

  public List<BigDecimal> calculateListToPLN(List<Account> account) {
    return account.stream()
      .map(this::calculateToPLN)
      .collect(Collectors.toList());
  }

  public BigDecimal calculateSumToPLN(List<Account> accounts) {
    return calculateListToPLN(accounts).stream()
      .reduce(BigDecimal::add)
      .orElseThrow();
  }

  /**
   * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
   */

  public Set<String> findNamesByPredicate(Predicate<User> userPredicate) {
    return getUserStream()
      .filter(userPredicate)
      .map(User::getFirstName)
      .collect(Collectors.toSet());
  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn
   * i zwraca ich imiona w formie listy.
   */

  public List<String> findOlderThanNotManNamesAsList(int age) {
    return getUserStream()
      .filter(user -> user.getSex() != Sex.MAN)
      .filter(user -> user.getAge() >= age)
      .map(User::getFirstName)
      .collect(Collectors.toList());

  }

  /**
   * Dla każdej firmy uruchamia przekazaną metodę.
   */

  public void executeForEachCompany(Consumer<Company> companyConsumer) {
    findCompaniesAsStream().forEach(companyConsumer);
  }

  /**
   * Wyszukuje najbogatsza kobietę i zwraca ją. Metoda musi uzwględniać to że rachunki są w różnych walutach.
   */
  //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/55052733/9360524
  public BigDecimal findWomanAccountAmountInPln(User user) {
    return user.getAccounts().stream()
      .map(this::calculateToPLN)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public User findRichestWoman() throws Exception {
    return getUserStream()
      .filter(user -> user.getSex().equals(Sex.WOMAN))
      .max(Comparator.comparing(this::findWomanAccountAmountInPln))
      .orElseThrow(Exception::new);
  }

  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
   */

  public List<String> findFistNCompaniesNames(int n) {
    return holdings.stream()
      .flatMap(holding -> holding.getCompanies().stream())
      .limit(n)
      .map(Company::getName)
      .collect(Collectors.toList());
  }


  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metodę getAccountStream.
   * Jeżeli nie udało się znaleźć najpopularniejszego rachunku metoda ma wyrzucić wyjątek IllegalStateException.
   * Pierwsza instrukcja metody to return.
   */

  public AccountType findMostPopularAccountType() {
    return getAccountStream()
      .map(Account::getType)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .max(Map.Entry.comparingByValue())
      .map(Map.Entry::getKey)
      .orElseThrow(IllformedLocaleException::new);
  }

  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca
   * wyjątek IllegalArgumentException.
   */

  public User findUserMatchedToPredicate(Predicate<User> predicate) {
    return getUserStream()
      .filter(predicate)
      .findFirst()
      .orElseThrow(IllegalArgumentException::new);
  }


  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
   */

  public Map<String, Integer> getCompanyMapWithEmployeeAmount() {
    return findCompaniesAsStream()
      .collect(Collectors.groupingBy(Company::getName, summingInt(company -> company.getUsers().size())));

  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako String
   * składający się z imienia i nazwiska. Podpowiedź:  Możesz skorzystać z metody entrySet.
   */

  public Map<String, String> getCompanyMapWithEmployeeAmountAsString() {
    return findCompaniesAsStream()
      .collect(Collectors.toMap(Company::getName, company -> company.getUsers().stream()
        .map(user -> user.getFirstName() + " " + user.getLastName())
        .collect(Collectors.toList())
        .toString()));

  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty
   * typu T, tworzonych za pomocą przekazanej funkcji.
   */
  //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/54969615/9360524
  public <T> Map<String, List<T>> getCompanyMapWithEmployeeAmountAsList(final Function<User, T> converter) {
    return findCompaniesAsStream()
      .collect(Collectors.toMap(Company::getName, company -> company.getUsers().stream()
        .map(converter)
        .collect(Collectors.toList())));

  }

  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą.
   * Osoby "innej" płci mają zostać zignorowane. Wartością jest natomiast zbiór nazwisk tych osób.
   */

  public Map<Boolean, Long> getUserMapWithUserGenderAndAmount() {
    return getUserStream()
      .filter(user -> !user.getSex().equals(Sex.OTHER))
      .collect(Collectors.partitioningBy(user -> user.getSex().equals(Sex.WOMAN), Collectors.counting()));
  }


  /**
   * Zwraca mapę rachunków, gdzie kluczem jest numer rachunku, a wartością ten rachunek.
   */

  public Map<String, Account> getAccountsMapWithAccountNumberAndAccount() {
    return getAccountStream()
      .collect(Collectors.toMap(Account::getNumber, Function.identity()));
  }

  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
   */

  public String findUserNamesAsString() {
    return getUserStream()
      .map(User::getFirstName)
      .distinct()
      .sorted()
      .collect(Collectors.joining(" "));
  }


  /**
   * Zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
   */

  public Set<User> getUserSetLimitedTo10() {
    return getUserStream()
      .limit(10)
      .collect(Collectors.toSet());
  }

  /**
   * Zapisuje listę numerów rachunków w pliku na dysku, gdzie w każda linijka wygląda następująco:
   * NUMER_RACHUNKU|KWOTA|WALUTA
   * <p>
   * Skorzystaj z strumieni i try-resources.
   */

  public void saveAccountsInFile(String s) throws IOException {

    FileWriter fileWriter = new FileWriter(s);
    PrintWriter printWriter = new PrintWriter(fileWriter);

    getAccountStream()
      .forEachOrdered(account -> printWriter.println(account.getNumber() + "|" + account.getAmount().toString() + "|" + account.getCurrency().toString()));

    printWriter.close();
  }


  /**
   * Zwraca użytkownika, który spełnia podany warunek.
   */

  public User findUser(Predicate<User> predicate) {
    return getUserStream()
      .filter(predicate)
      .findFirst()
      .orElseThrow();

  }

  /**
   * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie:
   * IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak użytkownika.
   * <p>
   * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów.
   */

  public String getInfoAboutUserAge(Optional<User> user) {
    return user.flatMap(u -> getUserStream()
      .filter(user1 -> user1.equals(user.get())).findFirst())
      .map(user1 -> user1.getFirstName().toUpperCase() + " " + user1.getLastName().toUpperCase() + " ma lat " + user1.getAge())
      .orElse("Brak użytkownika");

  }

  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imię, nazwisko) posortowanych od z do a.
   * Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred Pasibrzuch, Adam Wojcik
   */

  public String getNamesAndSurnamesSortedReversed() {
    return getUserStream()
      .sorted(Comparator.comparing(User::getFirstName).reversed())
      .map(user -> user.getFirstName() + " " + user.getLastName() + ", ")
      .collect(Collectors.joining());

  }

  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu
   * przeliczona na złotówki.
   */

  public Map<AccountType, BigDecimal> getMapWithAccountTypeAndAmountDenominatedInPLN() {
    return getAccountStream()
      .collect(Collectors.toMap(Account::getType, this::calculateToPLN, BigDecimal::add));
  }

  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników.
   */

  public int getSquareSumOfAge() {
    return getUserStream()
      .mapToInt(User::getAge)
      .map(p -> (int) Math.pow(p, 2))
      .sum();
  }


  /**
   * Zwraca strumień wszystkich firm.
   */

  public Stream<Company> findCompaniesAsStream() {
    return holdings.stream()
      .flatMap(holding -> holding.getCompanies().stream());
  }

  /**
   * Zwraca zbiór walut w jakich są rachunki.
   */

  private Set<Currency> getAllCurrenciesToSet() {
    return findCompaniesAsStream()
      .flatMap(company -> company.getUsers().stream())
      .flatMap(user -> user.getAccounts().stream())
      .map(Account::getCurrency)
      .collect(Collectors.toSet());
  }


  /**
   * Tworzy strumień rachunków.
   */

  public Stream<Account> getAccountStream() {
    return getUserStream()
      .flatMap(user -> user.getAccounts().stream());
  }

  /**
   * Tworzy strumień użytkowników.
   */

  public long findAmountOfWomen() {
    return getUserStream()
      .filter(isWoman)
      .count();
  }


  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się
   * powtarzać, wszystkie zmienną muszą być final. Jeżeli podano liczbę większą niż liczba użytkowników należy
   * wyrzucić wyjątek (bez zmiany sygnatury metody).
   */

  public List<User> getNUniqueUsers(int n) {
    final UserMockGenerator userMockGenerator = new UserMockGenerator();
    final List<User> userList = userMockGenerator.generate();
    if (n > userList.size()) {
      throw new IllegalArgumentException("Requested users are exceeding available users amount.");
    }
    return userList.stream()
      .distinct()
      .limit(n)
      .collect(Collectors.toList());
  }

  /**
   * 38.
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem
   * jest obiekt User a wartością suma pieniędzy na rachunku danego typu przeliczona na złotkówki.
   */

  Map<Stream<AccountType>, Map<User, BigDecimal>> getMapWithAccountTypeKeyAndSumMoneyForManInPLN() {
    return findCompaniesAsStream()
      .collect(Collectors.toMap(
        company -> company.getUsers()
          .stream()
          .flatMap(user -> user.getAccounts()
            .stream()
            .map(Account::getType)),
        this::manWithSumMoneyOnAccounts
      ));
  }

  private Map<User, BigDecimal> manWithSumMoneyOnAccounts(final Company company) {
    return company
      .getUsers()
      .stream()
      .filter(isMan)
      .collect(Collectors.toMap(
        Function.identity(),
        this::getSumUserAmountInPLN
      ));
  }

  private BigDecimal getSumUserAmountInPLN(final User user) {
    return user.getAccounts()
      .stream()
      .map(this::calculateToPLN)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * 39. Policz ile pieniędzy w złotówkach jest na kontach osób które nie są ani kobietą ani mężczyzną.
   */

  public BigDecimal getSumOfMoneyForOtherSex() {
    return getUserStream()
      .filter(user -> user.getSex().equals(Sex.OTHER))
      .flatMap(user -> user.getAccounts().stream())
      .map(this::calculateToPLN)
      .reduce(BigDecimal::add)
      .orElseThrow();
  }

  /**
   * 40. Wymyśl treść polecenia i je zaimplementuj.
   * Policz ile osób pełnoletnich posiada rachunek oraz ile osób niepełnoletnich posiada rachunek. Zwróć mapę
   * przyjmując klucz True dla osób pełnoletnich i klucz False dla osób niepełnoletnich. Osoba pełnoletnia to osoba
   * która ma więcej lub równo 18 lat
   */

  public Map<Boolean, Long> getPartitionedUsersByAge() {
    return getUserStream()
      .collect(Collectors.partitioningBy(user -> user.getAge() >= 18, Collectors.counting()));
  }

}
