
package com.flutzpanel.persistence;

import com.flutzpanel.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {



}
