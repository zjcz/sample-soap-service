package dev.jonclarke.samplesoapservice;

import dev.jonclarke.movies.web_service.*;
import dev.jonclarke.samplesoapservice.dataaccess.MovieRepository;
import dev.jonclarke.samplesoapservice.helpers.MovieHelper;
import dev.jonclarke.samplesoapservice.models.MovieDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * Web service endpoint handler for movies data.
 * Handle the SOAP requests and return the appropriate responses
 */
@Endpoint
public class MovieEndpoint {
    private static final String NAMESPACE_URI = "http://jonclarke.dev/movies/web-service";

    private final MovieRepository repository;

    @Autowired
    public MovieEndpoint(MovieRepository movieRepository) {
        this.repository = movieRepository;
    }

    /**
     * Handle the listMoviesRequest SOAP request.  List all available movies
     * @param request SOAP request
     * @return listMoviesResponse SOAP response
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listMoviesRequest")
    @ResponsePayload
    public ListMoviesResponse getAllMovies(@RequestPayload ListMoviesRequest request) {
        ListMoviesResponse response = new ListMoviesResponse();
        List<MovieDataModel> items = repository.findAll();
        response.getMovies().addAll(items.stream().map(MovieHelper::convertMovie).toList());
        return response;
    }

    /**
     * Handle the getMovieRequest SOAP request.  Get a single movie by ID
     * @param request SOAP request
     * @return getMovieResponse SOAP response
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMovieRequest")
    @ResponsePayload
    public GetMovieResponse getSingleMovie(@RequestPayload GetMovieRequest request) {
        GetMovieResponse response = new GetMovieResponse();
        MovieDataModel item = repository.findById(request.getId()).orElse(null);
        response.setMovie(MovieHelper.convertMovie(item));
        return response;
    }

    /**
     * Handle the addMovieRequest SOAP request.  Add a new movie
     * @param request SOAP request
     * @return addMovieResponse SOAP response, containing the movie record added
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addMovieRequest")
    @ResponsePayload
    public AddMovieResponse newMovie(@RequestPayload AddMovieRequest request) {
        AddMovieResponse response = new AddMovieResponse();

        MovieDataModel item = new MovieDataModel();
        item.setTitle(request.getMovie().getTitle());
        item.setDescription(request.getMovie().getDescription());
        item.setReleaseDate(request.getMovie().getReleaseDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime());
        item.setAvailableOnDvd(request.getMovie().isAvailableOnDvd());

        try {
            MovieDataModel savedItem = repository.save(item);
            response.setMovie(MovieHelper.convertMovie(savedItem));
            response.setMessage("Movie added successfully");
        } catch (Exception e) {
            response.setMessage("Movie not added");
        }

        return response;
    }

    /**
     * Handle the updateMovieRequest SOAP request.  Update an existing movie
     * @param request SOAP request
     * @return updateMovieResponse SOAP response, containing the movie record updated
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateMovieRequest")
    @ResponsePayload
    public UpdateMovieResponse updateMovie(@RequestPayload UpdateMovieRequest request) {
        UpdateMovieResponse response = new UpdateMovieResponse();
        MovieDataModel item = repository.findById(request.getMovie().getId()).orElse(null);

        if (item != null) {
            item.setTitle(request.getMovie().getTitle());
            item.setDescription(request.getMovie().getDescription());
            item.setReleaseDate(request.getMovie().getReleaseDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime());
            item.setAvailableOnDvd(request.getMovie().isAvailableOnDvd());
            repository.save(item);

            response.setMovie(MovieHelper.convertMovie(item));
            response.setMessage("Movie updated successfully");
        } else {
            response.setMessage("Movie not found");
        }

        return response;
    }

    /**
     * Handle the deleteMovieRequest SOAP request.  Delete an existing movie
     * @param request SOAP request
     * @return deleteMovieResponse SOAP response, containing the movie record deleted
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteMovieRequest")
    @ResponsePayload
    public DeleteMovieResponse deleteMovie(@RequestPayload DeleteMovieRequest request) {
        DeleteMovieResponse response = new DeleteMovieResponse();
        MovieDataModel item = repository.findById(request.getId()).orElse(null);
        if (item != null) {
            repository.delete(item);
            response.setMovie(MovieHelper.convertMovie(item));
            response.setMessage("Movie deleted successfully");
        } else {
            response.setMessage("Movie not found");
        }

        return response;
    }
}