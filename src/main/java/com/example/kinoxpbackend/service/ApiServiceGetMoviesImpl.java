package com.example.kinoxpbackend.service;

import com.example.kinoxpbackend.model.Actor;
import com.example.kinoxpbackend.model.Director;
import com.example.kinoxpbackend.model.Genre;
import com.example.kinoxpbackend.model.Movie;
import com.example.kinoxpbackend.repository.ActorRepository;
import com.example.kinoxpbackend.repository.DirectorRepository;
import com.example.kinoxpbackend.repository.GenreRepository;
import com.example.kinoxpbackend.repository.MovieRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApiServiceGetMoviesImpl implements ApiServiceGetMovies {

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

    @Override
    @Transactional
    public List<Movie> getMovies() {
        return fetchMoviesFromAPI();
    }

    private List<Movie> fetchMoviesFromAPI() {
        List<Movie> movieList = new ArrayList<>();
        for (int page = 1; page <= 2; page++) {
            try {
                String urlString = BASE_URL + "?s=movie&type=movie&page=" + page + "&apikey=" + apiKey;
                HttpURLConnection conn = createHttpConnection(urlString);

                if (conn.getResponseCode() == 200) {
                    String response = readResponse(conn);
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
                            fetchAndSaveMovieDetails(imdbID, movieList);
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

    private void fetchAndSaveMovieDetails(String imdbID, List<Movie> movieList) {
        try {
            if (movieRepository.existsByImdbID(imdbID)) {
                System.out.println("Movie with IMDb ID " + imdbID + " already exists.");
                return;
            }

            String detailedUrlString = BASE_URL + "?i=" + imdbID + "&apikey=" + apiKey;
            HttpURLConnection conn = createHttpConnection(detailedUrlString);

            if (conn.getResponseCode() == 200) {
                String response = readResponse(conn);
                JSONObject detailedData = new JSONObject(response);

                Movie movie = mapMovieFromJson(detailedData);
                movieRepository.save(movie); // Save movie to the database
                movieList.add(movie); // Add to the result list
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error fetching movie details for IMDb ID " + imdbID + ": " + e.getMessage());
        }
    }

    private Movie mapMovieFromJson(JSONObject detailedData) {
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

        Set<Director> directors = createAndReturnDirectors(detailedData.optString("Director", "N/A"));
        movie.setDirectors(directors);

        Set<Actor> actors = createAndReturnActors(detailedData.optString("Actors", "N/A"));
        movie.setActors(actors);

        Set<Genre> genres = createAndReturnGenres(detailedData.optString("Genre", "N/A"));
        movie.setGenres(genres);

        return movie;
    }

    private Set<Director> createAndReturnDirectors(String directorString) {
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

    private Set<Actor> createAndReturnActors(String actorString) {
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

    private Set<Genre> createAndReturnGenres(String genreString) {
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

    private HttpURLConnection createHttpConnection(String urlString) throws Exception {
        URI uri = new URI(urlString); // Create a URI from the string
        URL url = uri.toURL(); // Convert the URI to a URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
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