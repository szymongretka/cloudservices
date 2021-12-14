package pl.sg.managment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.managment.model.Application;
import pl.sg.managment.model.State;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @EntityGraph(attributePaths = "stateChange")
    Optional<Application> findById(Integer integer);

    Page<Application> findByNameAndState(String name, State state, Pageable pageable);

}
