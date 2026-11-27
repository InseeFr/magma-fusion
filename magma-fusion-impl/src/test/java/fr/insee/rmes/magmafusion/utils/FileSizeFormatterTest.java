package fr.insee.rmes.magmafusion.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FileSizeFormatterTest {

    @ParameterizedTest(name = "{0} octets -> {1}")
    @CsvSource(
            delimiter = '|',
            value = {
                "0             | 0 o",
                "512           | 512 o",
                "999           | 999 o",
                "1000          | 1 ko",
                "130048        | 130 ko",
                "1500          | 1,5 ko",
                "999949        | 999,9 ko",
                "1234567       | 1,2 Mo",
                "20000000      | 20 Mo",
                "3400000000    | 3,4 Go",
                "5000000000000 | 5 To"
            })
    void should_be_expressed_in_the_largest_unit_keeping_the_value_under_1000(String bytes, String expected) {
        assertThat(FileSizeFormatter.humanReadable(bytes)).isEqualTo(expected);
    }

    @Test
    void should_switch_to_the_next_unit_when_rounding_reaches_1000() {
        assertThat(FileSizeFormatter.humanReadable("999999")).isEqualTo("1 Mo");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"127 ko", "-1", "abc"})
    void should_give_no_size_when_the_stored_value_is_not_a_number_of_bytes(String bytes) {
        assertThat(FileSizeFormatter.humanReadable(bytes)).isNull();
    }
}
