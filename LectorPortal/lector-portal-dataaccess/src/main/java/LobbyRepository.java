import entity.Escaperoom;
import entity.OpenLobbys;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static query.LobbyNativeQueries.FIND_ALL_BY_STATE_PLAYING;
import static query.LobbyNativeQueries.FIND_BY_ESCAPEROOM_AND_USER_AND_STATE_STOPPED_NOT;

@Repository
public interface LobbyRepository extends JpaRepository<OpenLobbys, Long> {

    //Optional<List<Escaperoom>> findEscaperoomByUser(User User);

    void deleteByEscaperoomAndUser(Escaperoom escaperoom, User user);

    Optional<List<OpenLobbys>> findByEscaperoomAndUser(Escaperoom escaperoom, User user);

    @Query(value = FIND_ALL_BY_STATE_PLAYING)
    Optional<List<OpenLobbys>> findAllByStatePlaying();

    @Query(value = FIND_BY_ESCAPEROOM_AND_USER_AND_STATE_STOPPED_NOT, nativeQuery = true)
    Optional<OpenLobbys> findByEscaperoomAndUserAndStateStoppedNot(Long escaperoomID, User user);
}
