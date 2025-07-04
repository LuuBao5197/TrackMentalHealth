package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestRepository extends JpaRepository<Test, Integer> {
    @Query(
            "select t from Test t where lower(t.title) = lower(:title)"
    )
    Test findByTitleIgnoreCase(@Param("title") String title);
}
