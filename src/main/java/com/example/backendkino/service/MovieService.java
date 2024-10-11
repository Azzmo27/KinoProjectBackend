package com.example.backendkino.service;

import com.example.backendkino.model.Actor;
import com.example.backendkino.model.Director;
import com.example.backendkino.model.Genre;
import com.example.backendkino.model.Movie;
import com.example.backendkino.repository.ActorRepository;
import com.example.backendkino.repository.DirectorRepository;
import com.example.backendkino.repository.GenreRepository;
import com.example.backendkino.repository.MovieRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

@Service
public class MovieService {

    @Value("${api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.omdbapi.com/";

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Transactional
    public List<Movie> fetchAllMovies() {
        return retrieveMoviesFromApi();
    }

    private List<Movie> retrieveMoviesFromApi() {
        List<Movie> movieList = new ArrayList<>();
        for (int page = 1; page <= 2; page++) {
            try {
                String urlString = BASE_URL + "?s=movie&type=movie&page=" + page + "&apikey=" + apiKey;
                HttpURLConnection conn = establishHttpConnection(urlString);

                if (conn.getResponseCode() == 200) {
                    String response = readHttpResponse(conn);
                    JSONObject data = new JSONObject(response);

                    if (data.has("Error")) {
                        System.out.println("API Error: " + data.getString("Error"));
                        continue;
                    }

                    if (data.has("Search")) {
                        JSONArray movies = data.getJSONArray("Search");
                        for (int i = 0; i < movies.length(); i++) {
                            JSONObject movieJson = movies.getJSONObject(i);
                            String imdbID = movieJson.getString("imdbID");
                            saveMovieDetails(imdbID, movieList);
                        }
                    }
                } else {
                    System.out.println("HTTP error code: " + conn.getResponseCode());
                }

                conn.disconnect();
            } catch (Exception e) {
                System.out.println("Error fetching movies from API on page " + page + ": " + e.getMessage());
            }
        }
        return movieList;
    }

    private void saveMovieDetails(String imdbID, List<Movie> movieList) {
        try {
            if (movieRepository.existsByImdbID(imdbID)) {
                System.out.println("Movie with IMDb ID " + imdbID + " already exists.");
                return;
            }

            String detailedUrlString = BASE_URL + "?i=" + imdbID + "&apikey=" + apiKey;
            HttpURLConnection conn = establishHttpConnection(detailedUrlString);

            if (conn.getResponseCode() == 200) {
                String response = readHttpResponse(conn);
                JSONObject detailedData = new JSONObject(response);

                Movie movie = convertJsonToMovie(detailedData);
                movieRepository.save(movie);
                movieList.add(movie);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error fetching movie details for IMDb ID " + imdbID + ": " + e.getMessage());
        }
    }

    private Movie convertJsonToMovie(JSONObject detailedData) {
        Movie movie = new Movie(
                null,
                detailedData.getString("Title"),
                detailedData.getString("Year"),
                detailedData.optString("Released", "N/A"),
                detailedData.optString("Runtime", "N/A"),
                detailedData.optString("Poster", "N/A"),
                detailedData.optString("imdbRating", "N/A"),
                detailedData.getString("imdbID")
        );

        Set<Director> directors = extractAndSaveDirectors(detailedData.optString("Director", "N/A"));
        movie.setDirectors(directors);

        Set<Actor> actors = extractAndSaveActors(detailedData.optString("Actors", "N/A"));
        movie.setActors(actors);

        Set<Genre> genres = extractAndSaveGenres(detailedData.optString("Genre", "N/A"));
        movie.setGenres(genres);

        return movie;
    }

    private Set<Director> extractAndSaveDirectors(String directorString) {
        String[] directorArray = directorString.split(",\\s*");
        Set<Director> directors = new HashSet<>();

        for (String directorName : directorArray) {
            Director director = directorRepository.findDirectorByFullName(directorName);
            if (director == null) {
                director = new Director(directorName);
                directorRepository.save(director);
            }
            directors.add(director);
        }

        return directors;
    }

    private Set<Actor> extractAndSaveActors(String actorString) {
        String[] actorArray = actorString.split(",\\s*");
        Set<Actor> actors = new HashSet<>();

        for (String actorName : actorArray) {
            Actor actor = actorRepository.findActorByFullName(actorName);
            if (actor == null) {
                actor = new Actor(actorName);
                actorRepository.save(actor);
            }
            actors.add(actor);
        }

        return actors;
    }

    private Set<Genre> extractAndSaveGenres(String genreString) {
        String[] genreArray = genreString.split(",\\s*");
        Set<Genre> genres = new HashSet<>();

        for (String genreName : genreArray) {
            Genre genre = genreRepository.findByGenreName(genreName);
            if (genre == null) {
                genre = new Genre(genreName);
                genreRepository.save(genre);
            }
            genres.add(genre);
        }

        return genres;
    }

    private HttpURLConnection establishHttpConnection(String urlString) throws Exception {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    private String readHttpResponse(HttpURLConnection conn) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
