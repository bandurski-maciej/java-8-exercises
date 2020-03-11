package pl.klolo.workshops.logic;

import pl.klolo.workshops.domain.Holding;
import pl.klolo.workshops.domain.Sex;
import pl.klolo.workshops.domain.User;
import pl.klolo.workshops.mock.HoldingMockGenerator;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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


    /**
     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy
     * po zakończeniu działania strumienia.
     */


    /**
     * Zwraca listę firm jako String gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
     */


    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+".
     * Używamy collect i StringBuilder.
     * <p>
     * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
     */


    /**
     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
     */


    /**
     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości
     * występują bez powtórzeń i są posortowane.
     */


    /**
     * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody
     * stream, tylko skorzystaj z  Stream.generate. Wspólny kod wynieś do osobnej metody.
     *
     * @see #getAllCurrencies()
     */




    /**
     * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień użytkowników umieść
     * w osobnej metodzie. Predicate określający czy mamy do czynienia z kobietą niech będzie polem statycznym w klasie.
     */



    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency.
     */


    /**
     * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency i sumuje ją.
     */


    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
     */


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
