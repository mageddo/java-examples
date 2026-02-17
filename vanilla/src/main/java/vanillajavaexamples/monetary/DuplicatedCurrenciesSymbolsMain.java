package vanillajavaexamples.monetary;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class DuplicatedCurrenciesSymbolsMain {

  public static void main(String[] args) {

    final var currencies = Currency.getAvailableCurrencies();
    final var map = buildStore(currencies);

    final var symbols = map.keySet();
    final var duplicatedSymbols = symbols.stream()
        .filter(symbol -> map.get(symbol)
            .size() > 1
        )
        .toList();

    final var uniqueSymbols = symbols.stream()
        .filter(symbol -> map.get(symbol)
            .size() == 1
        )
        .toList();


    System.out.println("\n\n");
    System.out.println("DUPLICATED");
    System.out.println("========");
    System.out.println(uniqueSymbols.size());
    for (final var symbol : duplicatedSymbols) {
      System.out.printf("%s -> %s%n", symbol, map.get(symbol));
    }

    System.out.println("\n\n");
    System.out.println("UNIQUE");
    System.out.println("========");
    System.out.println(uniqueSymbols.size());
    System.out.println(uniqueSymbols);

    System.out.println("\n\n");
    System.out.println("SUMMARY");
    System.out.println("========");
    System.out.printf(
        "currencies=%d, store(size=%d, duplicated=%s, unique=%d)",
        currencies.size(),
        map.size(), duplicatedSymbols.size(), uniqueSymbols.size()
    );

    System.out.println("\n\n");

  }


  static MultiValuedMap<String, Data> buildStore(Set<Currency> currencies) {
    final var m = new ArrayListValuedHashMap<String, Data>();
    final var locales = Locale.getAvailableLocales();
    for (final var currency : currencies) {
      for (final var locale : locales) {
        final var symbol = currency.getSymbol(locale);
        if (m.containsKey(symbol)) {
          final var pairs = commons.Collections.keyBy(m.get(symbol), Data::currencyCode);
          if (!pairs.containsKey(currency.getCurrencyCode())) {
            m.put(symbol, new Data(currency, locale));
          }
        } else {
          m.put(symbol, new Data(currency, locale));
        }
      }
    }
    return m;
  }

  record Data(java.util.Currency currency, Locale locale) {
    public String currencyCode() {
      return this.currency.getCurrencyCode();
    }
  }

}
