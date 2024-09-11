package com.escapedoom.auth.data.dataclasses.repositories;


import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscaperoomRepository extends JpaRepository<Escaperoom, Long> {

    Optional<List<Escaperoom>> findEscaperoomByUser(User User);

}
