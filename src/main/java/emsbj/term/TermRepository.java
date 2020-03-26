package emsbj.term;

import emsbj.schoolyear.SchoolYear;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TermRepository extends CrudRepository<Term, Long> {
    Iterable<Term> findBySchoolYear(SchoolYear schoolYear);
    Optional<Term> findBySchoolYearAndName(SchoolYear schoolYear, String name);
}