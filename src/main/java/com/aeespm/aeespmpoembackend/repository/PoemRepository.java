package com.aeespm.aeespmpoembackend.repository;

import com.aeespm.aeespmpoembackend.entity.Poem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoemRepository extends JpaRepository<Poem, Long> {
}
