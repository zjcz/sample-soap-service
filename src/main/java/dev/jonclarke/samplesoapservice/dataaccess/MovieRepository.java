package dev.jonclarke.samplesoapservice.dataaccess;

import dev.jonclarke.samplesoapservice.models.MovieDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieDataModel, Integer> {
}