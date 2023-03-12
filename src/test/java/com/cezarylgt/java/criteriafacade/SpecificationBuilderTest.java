package com.cezarylgt.java.criteriafacade;

import com.cezarylgt.java.criteriafacade.demoapp.DemoApplication;
import com.cezarylgt.java.criteriafacade.demoapp.Person;
import com.cezarylgt.java.criteriafacade.demoapp.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = DemoApplication.class)
@ActiveProfiles("test")
public class SpecificationBuilderTest {

    private final LocalDate dt1 = LocalDate.of(2010, 5, 5);
    private final LocalDate dt2 = LocalDate.of(2011, 5, 5);
    private final LocalDate dt3 = LocalDate.of(2012, 5, 5);
    private final LocalDate dt4 = LocalDate.of(2013, 5, 5);

    @Autowired
    PersonRepository personRepository;

    GenericSpecificationBuilder<Person> genericSpecificationBuilder = new GenericSpecificationBuilder<>();

    @BeforeEach
    private void setup() {
        Person p1 = new Person(1, "User 1", 25, true, dt1, LocalDateTime.of(2020, 1, 1, 12, 0, 0));
        Person p2 = new Person(2, "User 2", 30, true, dt2, LocalDateTime.of(2020, 1, 1, 12, 0, 0));
        Person p3 = new Person(3, "User 3", 35, false, dt3, LocalDateTime.of(2020, 1, 1, 12, 0, 0));
        Person p4 = new Person(4, "User 4", 40, false, dt4, LocalDateTime.of(2020, 1, 1, 12, 0, 0));

        personRepository.saveAll(Arrays.asList(p1, p2, p3, p4));


    }

    @AfterEach
    private void teardown() {
        personRepository.deleteAll();
    }

    static Stream<Arguments> datesArguments() {
        return Stream.of(
                Arguments.arguments(LocalDate.of(2010, 5, 5), ComparisonOperator.EQUAL.label, 1),
                Arguments.arguments(LocalDate.of(2010, 5, 5), ComparisonOperator.NOT_EQUAL.label, 3),
                Arguments.arguments("2011-05-05", ComparisonOperator.CONTAINS.label, 1),
                Arguments.arguments("2011-05-05", ComparisonOperator.GREATER.label, 2),
                Arguments.arguments("2011-05-05", ComparisonOperator.GREATER_EQUAL.label, 3),
                Arguments.arguments("2011-05-01", ComparisonOperator.LESS.label, 1),
                Arguments.arguments("2011-05-05", ComparisonOperator.LESS_EQUAL.label, 2)
        );
    }


    @ParameterizedTest
    @MethodSource("datesArguments")
    public void searchingByDate(Object value, String operator, Integer expectedSize) {

        SearchCriteria criteria = new SearchCriteria("dateOfBirth", value, operator);
        Specification<Person> spec = genericSpecificationBuilder.build(Arrays.asList(criteria));
        List<Person> people = personRepository.findAll(spec);
        assertEquals(expectedSize, people.size());

    }

    static Stream<Arguments> stringArguments() {
        return Stream.of(
                Arguments.arguments("User 1", ComparisonOperator.EQUAL.label, 1),
                Arguments.arguments("User 1", ComparisonOperator.NOT_EQUAL.label, 3),
                Arguments.arguments("User", ComparisonOperator.CONTAINS.label, 4),
                Arguments.arguments("User", ComparisonOperator.GREATER.label, 4),
                Arguments.arguments("User 4", ComparisonOperator.GREATER_EQUAL.label, 1),
                Arguments.arguments("User", ComparisonOperator.LESS.label, 0),
                Arguments.arguments("User 1", ComparisonOperator.LESS_EQUAL.label, 1),
                Arguments.arguments("User 1,User 2", ComparisonOperator.IN.label, 2)
        );
    }


    @ParameterizedTest
    @MethodSource("stringArguments")
    public void searchingByString(String value, String operator, Integer expectedSize) {
        SearchCriteria criteria = new SearchCriteria("name", value, operator);
        Specification<Person> spec = genericSpecificationBuilder.build(Arrays.asList(criteria));
        List<Person> people = personRepository.findAll(spec);
        assertEquals(expectedSize, people.size());

    }

    static Stream<Arguments> numberArguments() {
        return Stream.of(
                Arguments.arguments(35, ComparisonOperator.EQUAL.label, 1),
                Arguments.arguments(35, ComparisonOperator.NOT_EQUAL.label, 3),
                Arguments.arguments(3, ComparisonOperator.CONTAINS.label, 0),
                Arguments.arguments(35, ComparisonOperator.GREATER.label, 1),
                Arguments.arguments(35, ComparisonOperator.GREATER_EQUAL.label, 2),
                Arguments.arguments(30, ComparisonOperator.LESS.label, 1),
                Arguments.arguments(30, ComparisonOperator.LESS_EQUAL.label, 2),
                Arguments.arguments("35,25", ComparisonOperator.IN.label, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("numberArguments")
    public void searchByNumbers(Object value, String operator, Integer expectedSize) {
        SearchCriteria criteria = new SearchCriteria("age", value, operator);
        Specification<Person> spec = genericSpecificationBuilder.build(Arrays.asList(criteria));
        List<Person> people = personRepository.findAll(spec);
        assertEquals(expectedSize, people.size());

    }

    static Stream<Arguments> booleanArguments() {
        return Stream.of(
                Arguments.arguments(true, ComparisonOperator.EQUAL.label, 2),
                Arguments.arguments(true, ComparisonOperator.NOT_EQUAL.label, 2),
                Arguments.arguments(false, ComparisonOperator.EQUAL.label, 2),
                Arguments.arguments(false, ComparisonOperator.NOT_EQUAL.label, 2),
                Arguments.arguments(true, ComparisonOperator.CONTAINS.label, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("booleanArguments")
    public void searchByBooleans(Object value, String operator, Integer expectedSize) {
        SearchCriteria criteria = new SearchCriteria("enabled", value, operator);
        Specification<Person> spec = genericSpecificationBuilder.build(Arrays.asList(criteria));
        List<Person> people = personRepository.findAll(spec);
        assertEquals(expectedSize, people.size());

    }


}