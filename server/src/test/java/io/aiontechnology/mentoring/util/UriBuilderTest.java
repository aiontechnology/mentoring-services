package io.aiontechnology.mentoring.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UriBuilderTest {

    @Test
    void testBaseOnly() {
        // setup the fixture
        String base = "BASE";
        UriBuilder builder = new UriBuilder(base);

        // execute the SUT
        String result = builder.build();

        // validation
        assertThat(result).isEqualTo(base);
    }

    @Test
    void testMultipleQueryParams() {
        // setup the fixture
        String base = "BASE";
        UriBuilder builder = new UriBuilder(base);
        builder.withParam("PARAM1", "VALUE1")
                .withParam("PARAM2", "VALUE2");

        // execute the SUT
        String result = builder.build();

        // validation
        assertThat(result).isEqualTo("BASE?PARAM1=VALUE1&PARAM2=VALUE2");
    }

    @Test
    void testSingleQueryParam() {
        // setup the fixture
        String base = "BASE";
        UriBuilder builder = new UriBuilder(base);
        builder.withParam("PARAM1", "VALUE1");

        // execute the SUT
        String result = builder.build();

        // validation
        assertThat(result).isEqualTo("BASE?PARAM1=VALUE1");
    }

    @Test
    void testNullParamValue() {
        // setup the fixture
        String base = "BASE";
        UriBuilder builder = new UriBuilder(base);
        builder.withParam("PARAM1", null);

        // execute the SUT
        String result = builder.build();

        // validation
        assertThat(result).isEqualTo(base);
    }

}
