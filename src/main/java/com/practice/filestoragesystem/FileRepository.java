package com.practice.filestoragesystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<StoredFile, Long> {
}
