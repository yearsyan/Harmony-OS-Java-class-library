package ohos.global.icu.impl.number;

import ohos.global.icu.text.DecimalFormatSymbols;
import ohos.global.icu.util.Currency;
import ohos.global.icu.util.ULocale;

public class CustomSymbolCurrency extends Currency {
    private static final long serialVersionUID = 2497493016770137670L;
    private String symbol1;
    private String symbol2;

    public static Currency resolve(Currency currency, ULocale uLocale, DecimalFormatSymbols decimalFormatSymbols) {
        if (currency == null) {
            currency = decimalFormatSymbols.getCurrency();
        }
        if (currency == null) {
            return Currency.getInstance("XXX");
        }
        if (!currency.equals(decimalFormatSymbols.getCurrency())) {
            return currency;
        }
        String currencySymbol = decimalFormatSymbols.getCurrencySymbol();
        String internationalCurrencySymbol = decimalFormatSymbols.getInternationalCurrencySymbol();
        String name = currency.getName(decimalFormatSymbols.getULocale(), 0, (boolean[]) null);
        String currencyCode = currency.getCurrencyCode();
        if (!name.equals(currencySymbol) || !currencyCode.equals(internationalCurrencySymbol)) {
            return new CustomSymbolCurrency(currencyCode, currencySymbol, internationalCurrencySymbol);
        }
        return currency;
    }

    public CustomSymbolCurrency(String str, String str2, String str3) {
        super(str);
        this.symbol1 = str2;
        this.symbol2 = str3;
    }

    @Override // ohos.global.icu.util.Currency
    public String getName(ULocale uLocale, int i, boolean[] zArr) {
        if (i != 0) {
            return super.getName(uLocale, i, zArr);
        }
        if (zArr != null) {
            zArr[0] = false;
        }
        return this.symbol1;
    }

    @Override // ohos.global.icu.util.Currency
    public String getName(ULocale uLocale, int i, String str, boolean[] zArr) {
        return super.getName(uLocale, i, str, zArr);
    }

    @Override // ohos.global.icu.util.Currency
    public String getCurrencyCode() {
        return this.symbol2;
    }

    @Override // ohos.global.icu.util.MeasureUnit
    public int hashCode() {
        return this.symbol2.hashCode() ^ (super.hashCode() ^ this.symbol1.hashCode());
    }

    @Override // ohos.global.icu.util.MeasureUnit
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            CustomSymbolCurrency customSymbolCurrency = (CustomSymbolCurrency) obj;
            return customSymbolCurrency.symbol1.equals(this.symbol1) && customSymbolCurrency.symbol2.equals(this.symbol2);
        }
    }
}
