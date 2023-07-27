package dev.jonclarke.samplesoapservice.helpers;

import dev.jonclarke.movies.web_service.Movie;
import dev.jonclarke.samplesoapservice.models.MovieDataModel;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;

public class MovieHelper {
    /**
     * convert a Movie data model into a new web service Movie
     * @param dataModel MovieDataModel to convert
     * @return a new web service Movie object.  If dataModel is null, returns null
     */
    public static Movie convertMovie(final MovieDataModel dataModel) {
        if (dataModel == null) {
            return null;
        }

        Movie item = new Movie();
        item.setId(dataModel.getId());
        item.setTitle(dataModel.getTitle());
        item.setDescription(dataModel.getDescription());
        item.setReleaseDate(convertToXMLGregorianCalendar(dataModel.getReleaseDate()));
        item.setAvailableOnDvd(dataModel.isAvailableOnDvd());
        return item;
    }

    /**
     * Convert a LocalDateTime to an XMLGregorianCalendar object
     * @param dateTime LocalDateTime to convert
     * @return XMLGregorianCalendar object
     */
    private static XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime dateTime) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTime.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
