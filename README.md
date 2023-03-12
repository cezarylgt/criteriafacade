# Criteria API Facade

Facade interface for preparing JpaSpecification objects, 
abstracting away complicated API of Criteria Builder.

## Key features

- building Specification<T> using simplified interface: List of SearchCriteria objects.
- comparison operators


We can delegate Specification build to a GenericSpecificationBuilder with:
```

IGenericSpecificationBuilder<Person> builder = new GenericSpecificationBuilder<>();
List<SearchCriteria>  criteria = Arrays.asList(
        new SearchCriteria("dateOfBirth", "2022-05-05", ">"),
        new SearchCriteria("name", "John", "~")
   );
   
Specification<Person> spec = builder.build(criteria);

```

created Specification<Person> can now be passed to JpaRepository, 
which should return Person entities that meet following:
- dateOfBirth attribute should be younger( greater) than "2022-05-05"
- AND name attribute contains "John" string