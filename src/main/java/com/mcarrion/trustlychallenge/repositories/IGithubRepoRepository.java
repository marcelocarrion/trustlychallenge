package com.mcarrion.trustlychallenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mcarrion.trustlychallenge.domain.GithubRepo;

/**
 * @author Marcelo Carrion
 */
@Repository
public interface IGithubRepoRepository extends JpaRepository<GithubRepo, Long>
{

}
