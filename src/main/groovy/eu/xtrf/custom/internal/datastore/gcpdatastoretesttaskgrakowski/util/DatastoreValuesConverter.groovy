package eu.xtrf.custom.internal.datastore.gcpdatastoretesttaskgrakowski.util

import org.springframework.cloud.gcp.data.datastore.core.convert.DatastoreCustomConversions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

import java.math.RoundingMode

@Configuration
class DatastoreValuesConverter {
    @Bean
    DatastoreCustomConversions datastoreCustomConversions() {
        return new DatastoreCustomConversions([
                STRING_BIGDECIMAL_CONVERTER,
                BIGDECIMAL_STRING_CONVERTER
        ])
    }

    static final Converter<String, BigDecimal> STRING_BIGDECIMAL_CONVERTER =
            new Converter<String, BigDecimal>() {
                @Override
                BigDecimal convert(String source) {
                    return source.toBigDecimal().setScale(2)
                }
            }
    static final Converter<BigDecimal, String> BIGDECIMAL_STRING_CONVERTER =
            new Converter<BigDecimal, String>() {
                @Override
                String convert(BigDecimal source) {
                    return source.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toString()
                }
            }
}
