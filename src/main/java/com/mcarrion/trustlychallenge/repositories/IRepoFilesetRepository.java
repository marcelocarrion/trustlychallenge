package com.mcarrion.trustlychallenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mcarrion.trustlychallenge.domain.RepoFileset;

/**
 * @author Marcelo Carrion
 */
@Repository
public interface IRepoFilesetRepository extends JpaRepository<RepoFileset, Long>
{

}
