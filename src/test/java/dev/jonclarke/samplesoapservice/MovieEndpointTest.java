package dev.jonclarke.samplesoapservice;

import dev.jonclarke.samplesoapservice.dataaccess.MovieRepository;
import dev.jonclarke.samplesoapservice.models.MovieDataModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

@WebServiceServerTest
class MovieEndpointTest {
    private static final Map<String, String> NAMESPACE_MAPPING = createMapping();

    @Autowired
    private MockWebServiceClient client;

    @MockBean
    private MovieRepository repository;

    @Test
    void getAllMovies_withValidRequest_expectValidResponse() throws IOException {
        MovieDataModel movie1 = createMovie(1);
        MovieDataModel movie2 = createMovie(2);
        when(repository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        StringSource request = new StringSource(
                "<ws:listMoviesRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "</ws:listMoviesRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:listMoviesResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ns2:movies>" +
                        "<ns2:id>" + movie1.getId() + "</ns2:id>" +
                        "<ns2:title>" + movie1.getTitle() + "</ns2:title>" +
                        "<ns2:description>" + movie1.getDescription() + "</ns2:description>" +
                        "<ns2:releaseDate>" + movie1.getReleaseDate().toString() + "</ns2:releaseDate>" +
                        "<ns2:availableOnDvd>" + movie1.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                        "</ns2:movies>" +
                        "<ns2:movies>" +
                        "<ns2:id>" + movie2.getId() + "</ns2:id>" +
                        "<ns2:title>" + movie2.getTitle() + "</ns2:title>" +
                        "<ns2:description>" + movie2.getDescription() + "</ns2:description>" +
                        "<ns2:releaseDate>" + movie2.getReleaseDate().toString() + "</ns2:releaseDate>" +
                        "<ns2:availableOnDvd>" + movie2.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                        "</ns2:movies>" +
                        "</ns2:listMoviesResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[1]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(movie1.getId()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[1]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(movie1.getTitle()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[1]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(movie1.getDescription()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[1]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(movie1.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[1]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(movie1.isAvailableOnDvd().toString()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[2]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(movie2.getId()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[2]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(movie2.getTitle()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[2]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(movie2.getDescription()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[2]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(movie2.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:listMoviesResponse/ns2:movies[2]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(movie2.isAvailableOnDvd().toString()))
                .andExpect(payload(expectedResponse));
    }

    @Test
    void getSingleMovie_withValidRequest_expectValidResponse() throws IOException {
        MovieDataModel movie = createMovie(1);
        when(repository.findById(1)).thenReturn(Optional.of(movie));

        StringSource request = new StringSource(
                "<ws:getMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                          "<ws:id>" + movie.getId() + "</ws:id>" +
                        "</ws:getMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:getMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                          "<ns2:movie>" +
                            "<ns2:id>" + movie.getId() + "</ns2:id>" +
                            "<ns2:title>" + movie.getTitle() + "</ns2:title>" +
                            "<ns2:description>" + movie.getDescription() + "</ns2:description>" +
                            "<ns2:releaseDate>" + movie.getReleaseDate().toString() + "</ns2:releaseDate>" +
                            "<ns2:availableOnDvd>" + movie.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                          "</ns2:movie>" +
                        "</ns2:getMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:getMovieResponse/ns2:movie[1]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getId()))
                .andExpect(xpath("/ns2:getMovieResponse/ns2:movie[1]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getTitle()))
                .andExpect(xpath("/ns2:getMovieResponse/ns2:movie[1]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getDescription()))
                .andExpect(xpath("/ns2:getMovieResponse/ns2:movie[1]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:getMovieResponse/ns2:movie[1]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.isAvailableOnDvd().toString()))
                .andExpect(payload(expectedResponse));
    }

    @Test
    void getSingleMovie_withInvalidRequest_expectValidResponse() throws IOException {
        when(repository.findById(1)).thenReturn(Optional.empty());

        StringSource request = new StringSource(
                "<ws:getMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ws:id>1</ws:id>" +
                        "</ws:getMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:getMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "</ns2:getMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(payload(expectedResponse));
    }

    @Test
    void newMovie_withValidRequest_expectValidResponse() throws IOException {
        String expectedMessage = "Movie added successfully";

        MovieDataModel movie = createMovie(1);
        MovieDataModel newMovie = new MovieDataModel() {
            {
                setTitle(movie.getTitle());
                setDescription(movie.getDescription());
                setReleaseDate(movie.getReleaseDate());
                setAvailableOnDvd(movie.isAvailableOnDvd());
            }
        };

        when(repository.save(newMovie)).thenReturn(movie);

        StringSource request = new StringSource(
                "<ws:addMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ws:movie>" +
                        "<ws:title>" + movie.getTitle() + "</ws:title>" +
                        "<ws:description>" + movie.getDescription() + "</ws:description>" +
                        "<ws:releaseDate>" + movie.getReleaseDate().toString() + "</ws:releaseDate>" +
                        "<ws:availableOnDvd>" + movie.isAvailableOnDvd().toString() + "</ws:availableOnDvd>" +
                        "</ws:movie>" +
                        "</ws:addMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:addMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ns2:movie>" +
                        "<ns2:id>" + movie.getId() + "</ns2:id>" +
                        "<ns2:title>" + movie.getTitle() + "</ns2:title>" +
                        "<ns2:description>" + movie.getDescription() + "</ns2:description>" +
                        "<ns2:releaseDate>" + movie.getReleaseDate().toString() + "</ns2:releaseDate>" +
                        "<ns2:availableOnDvd>" + movie.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                        "</ns2:movie>" +
                        "<ns2:message>" + expectedMessage + "</ns2:message>" +
                        "</ns2:addMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:movie[1]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getId()))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:movie[1]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getTitle()))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:movie[1]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getDescription()))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:movie[1]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:movie[1]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.isAvailableOnDvd().toString()))
                .andExpect(xpath("/ns2:addMovieResponse/ns2:message", NAMESPACE_MAPPING)
                        .evaluatesTo(expectedMessage))
                .andExpect(payload(expectedResponse));
    }

    @Test
    void updateMovie_withValidRequest_expectValidResponse() throws IOException {
        String expectedMessage = "Movie updated successfully";

        MovieDataModel movie = createMovie(1);
        MovieDataModel updatedMovie = new MovieDataModel() {
            {
                setId(movie.getId());
                setTitle(movie.getTitle() + " updated");
                setDescription(movie.getDescription() + " updated");
                setReleaseDate(movie.getReleaseDate());
                setAvailableOnDvd(!movie.isAvailableOnDvd());
            }
        };

        when(repository.findById(1)).thenReturn(Optional.of(movie));
        when(repository.save(updatedMovie)).thenReturn(updatedMovie);

        StringSource request = new StringSource(
                "<ws:updateMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ws:movie>" +
                        "<ws:id>" + updatedMovie.getId() + "</ws:id>" +
                        "<ws:title>" + updatedMovie.getTitle() + "</ws:title>" +
                        "<ws:description>" + updatedMovie.getDescription() + "</ws:description>" +
                        "<ws:releaseDate>" + updatedMovie.getReleaseDate().toString() + "</ws:releaseDate>" +
                        "<ws:availableOnDvd>" + updatedMovie.isAvailableOnDvd().toString() + "</ws:availableOnDvd>" +
                        "</ws:movie>" +
                        "</ws:updateMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:updateMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ns2:movie>" +
                        "<ns2:id>" + updatedMovie.getId() + "</ns2:id>" +
                        "<ns2:title>" + updatedMovie.getTitle() + "</ns2:title>" +
                        "<ns2:description>" + updatedMovie.getDescription() + "</ns2:description>" +
                        "<ns2:releaseDate>" + updatedMovie.getReleaseDate().toString() + "</ns2:releaseDate>" +
                        "<ns2:availableOnDvd>" + updatedMovie.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                        "</ns2:movie>" +
                        "<ns2:message>" + expectedMessage + "</ns2:message>" +
                        "</ns2:updateMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:movie[1]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(updatedMovie.getId()))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:movie[1]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(updatedMovie.getTitle()))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:movie[1]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(updatedMovie.getDescription()))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:movie[1]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(updatedMovie.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:movie[1]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(updatedMovie.isAvailableOnDvd().toString()))
                .andExpect(xpath("/ns2:updateMovieResponse/ns2:message", NAMESPACE_MAPPING)
                        .evaluatesTo(expectedMessage))
                .andExpect(payload(expectedResponse));
    }

    @Test
    void deleteMovie_withValidRequest_expectValidResponse() throws IOException {
        String expectedMessage = "Movie deleted successfully";
        MovieDataModel movie = createMovie(1);

        when(repository.findById(1)).thenReturn(Optional.of(movie));
        doNothing().when(repository).delete(movie);

        StringSource request = new StringSource(
                "<ws:deleteMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ws:id>" + movie.getId() + "</ws:id>" +
                        "</ws:deleteMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:deleteMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ns2:movie>" +
                        "<ns2:id>" + movie.getId() + "</ns2:id>" +
                        "<ns2:title>" + movie.getTitle() + "</ns2:title>" +
                        "<ns2:description>" + movie.getDescription() + "</ns2:description>" +
                        "<ns2:releaseDate>" + movie.getReleaseDate().toString() + "</ns2:releaseDate>" +
                        "<ns2:availableOnDvd>" + movie.isAvailableOnDvd().toString() + "</ns2:availableOnDvd>" +
                        "</ns2:movie>" +
                        "<ns2:message>" + expectedMessage + "</ns2:message>" +
                        "</ns2:deleteMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:movie[1]/ns2:id", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getId()))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:movie[1]/ns2:title", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getTitle()))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:movie[1]/ns2:description", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getDescription()))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:movie[1]/ns2:releaseDate", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.getReleaseDate().toString()))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:movie[1]/ns2:availableOnDvd", NAMESPACE_MAPPING)
                        .evaluatesTo(movie.isAvailableOnDvd().toString()))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:message", NAMESPACE_MAPPING)
                        .evaluatesTo(expectedMessage))
                .andExpect(payload(expectedResponse));

        verify(repository, times(1)).delete(movie);
    }

    @Test
    void deleteMovie_withInvalidRequest_expectValidResponse() throws IOException {
        String expectedMessage = "Movie not found";

        when(repository.findById(1)).thenReturn(Optional.empty());

        StringSource request = new StringSource(
                "<ws:deleteMovieRequest xmlns:ws=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ws:id>1</ws:id>" +
                        "</ws:deleteMovieRequest>"
        );

        StringSource expectedResponse = new StringSource(
                "<ns2:deleteMovieResponse xmlns:ns2=\"http://jonclarke.dev/movies/web-service\">" +
                        "<ns2:message>" + expectedMessage + "</ns2:message>" +
                        "</ns2:deleteMovieResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("movies.xsd")))
                .andExpect(xpath("/ns2:deleteMovieResponse/ns2:message", NAMESPACE_MAPPING)
                        .evaluatesTo(expectedMessage))
                .andExpect(payload(expectedResponse));

        verify(repository, times(0)).delete(any(MovieDataModel.class));
    }

    private MovieDataModel createMovie(final int id) {
        MovieDataModel movie = new MovieDataModel();
        movie.setId(id);
        movie.setTitle("Test Movie " + id);
        movie.setDescription("Test Description " + id);
        movie.setReleaseDate(LocalDateTime.parse("2023-01-01T01:23:45", DateTimeFormatter.ISO_DATE_TIME));
        movie.setAvailableOnDvd(true);
        return movie;
    }

    private static Map<String, String> createMapping() {
        Map<String, String> mapping = new HashMap<>();
        //mapping.put("gs", "http://jonclarke.dev/movies/web-service");
        mapping.put("ns2", "http://jonclarke.dev/movies/web-service");
        return mapping;
    }
}