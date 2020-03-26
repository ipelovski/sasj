package emsbj.schoolyear;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SchoolYearRepository extends CrudRepository<SchoolYear, Long> {
    Optional<SchoolYear> findByBeginYear(Integer beginYear);
    @EntityGraph("SchoolYear.all")
    @Query("select s from SchoolYear as s")
    Iterable<SchoolYear> findAllWithAll();
    @EntityGraph("SchoolYear.all")
    Iterable<SchoolYear> findByBeginYearGreaterThanEqual(Integer beginYear);
    @EntityGraph("SchoolYear.all")
    @Query("select s from SchoolYear as s where s.id = ?1")
    Optional<SchoolYear> findByIdWithAll(Long id);
}