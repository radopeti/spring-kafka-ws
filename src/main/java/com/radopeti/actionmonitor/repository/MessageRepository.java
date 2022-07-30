package com.radopeti.actionmonitor.repository;

import com.radopeti.actionmonitor.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
