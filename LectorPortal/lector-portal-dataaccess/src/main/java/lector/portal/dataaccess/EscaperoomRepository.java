package lector.portal.dataaccess;

import lector.portal.dataaccess.entity.Escaperoom;
import lector.portal.dataaccess.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscaperoomRepository extends JpaRepository<Escaperoom, Long> {

    Optional<List<Escaperoom>> findEscaperoomByUser(User User);

}
