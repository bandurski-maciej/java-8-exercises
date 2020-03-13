package pl.klolo.workshops.logic;

import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.mock.HoldingMockGenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public Stream<Company> findCompaniesAsStream() {
    return holdings.stream()
      .flatMap(holding -> holding.getCompanies().stream());
  }

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

  public long findAmountOfWomen() {
    return getUserStream()
      .filter(isWoman)
      .count();
  }

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
      .get();
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

  public List<String> findOlderThanNotManNamesAsList(int age) {
    return getUserStream()
      .filter(user -> user.getSex() != Sex.MAN)
      .filter(user -> user.getAge() >= age)
      .map(User::getFirstName)
      .collect(Collectors.toList());
  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn
   * i zwraca ich imiona w formie listy.
   */


  /**
   * Dla każdej firmy uruchamia przekazaną metodę.
   */


  /**
   * Wyszukuje najbogatsza kobietę i zwraca ją. Metoda musi uzwględniać to że rachunki są w różnych walutach.
   */
  //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/55052733/9360524


  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
   */


  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metodę getAccountStream.
   * Jeżeli nie udało się znaleźć najpopularniejszego rachunku metoda ma wyrzucić wyjątek IllegalStateException.
   * Pierwsza instrukcja metody to return.
   */


  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca
   * wyjątek IllegalArgumentException.
   */


  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
   */


  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako String
   * składający się z imienia i nazwiska. Podpowiedź:  Możesz skorzystać z metody entrySet.
   */


  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty
   * typu T, tworzonych za pomocą przekazanej funkcji.
   */
  //pomoc w rozwiązaniu problemu w zadaniu: https://stackoverflow.com/a/54969615/9360524


  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą.
   * Osoby "innej" płci mają zostać zignorowane. Wartością jest natomiast zbiór nazwisk tych osób.
   */


  /**
   * Zwraca mapę rachunków, gdzie kluczem jest numer rachunku, a wartością ten rachunek.
   */


  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
   */


  /**
   * Zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
   */


  /**
   * Zapisuje listę numerów rachunków w pliku na dysku, gdzie w każda linijka wygląda następująco:
   * NUMER_RACHUNKU|KWOTA|WALUTA
   * <p>
   * Skorzystaj z strumieni i try-resources.
   */


  /**
   * Zwraca użytkownika, który spełnia podany warunek.
   */


  /**
   * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie:
   * IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak użytkownika.
   * <p>
   * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów.
   */


  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imię, nazwisko) posortowanych od z do a.
   * Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred Pasibrzuch, Adam Wojcik
   */


  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu
   * przeliczona na złotówki.
   */
  //TODO: fix
  // java.lang.AssertionError:
  // Expected :87461.4992
  // Actual   :87461.3999


  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników.
   */


  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się
   * powtarzać, wszystkie zmienną muszą być final. Jeżeli podano liczbę większą niż liczba użytkowników należy
   * wyrzucić wyjątek (bez zmiany sygnatury metody).
   */


  /**
   * Zwraca strumień wszystkich firm.
   */


  /**
   * Zwraca zbiór walut w jakich są rachunki.
   */


  /**
   * Tworzy strumień rachunków.
   */


  /**
   * Tworzy strumień użytkowników.
   */


  /**
   * 38.
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem
   * jest obiekt User a wartością suma pieniędzy na rachunku danego typu przeliczona na złotkówki.
   */
  //TODO: zamiast Map<Stream<AccountType>, Map<User, BigDecimal>> metoda ma zwracać
  // Map<AccountType>, Map<User, BigDecimal>>, zweryfikować działania metody


  /**
   * 39. Policz ile pieniędzy w złotówkach jest na kontach osób które nie są ani kobietą ani mężczyzną.
   */


  /**
   * 40. Wymyśl treść polecenia i je zaimplementuj.
   * Policz ile osób pełnoletnich posiada rachunek oraz ile osób niepełnoletnich posiada rachunek. Zwróć mapę
   * przyjmując klucz True dla osób pełnoletnich i klucz False dla osób niepełnoletnich. Osoba pełnoletnia to osoba
   * która ma więcej lub równo 18 lat
   */

}
