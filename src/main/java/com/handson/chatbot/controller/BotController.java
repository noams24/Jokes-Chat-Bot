package com.handson.chatbot.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.handson.chatbot.service.JokesService;
import com.handson.chatbot.service.MoviesService;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    JokesService jokesService;

    @RequestMapping(value = "/joke", method = RequestMethod.GET)
    public ResponseEntity<?> getJoke(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(jokesService.getJoke(keyword), HttpStatus.OK);
    }

    @Autowired
    MoviesService moviesService;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<?> getMovie(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(moviesService.searchMovies(keyword), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = { RequestMethod.POST })
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = "Not found";
        // if (params.containsKey("joke")) {
            res = jokesService.getJoke(params.get("joke"));
            // res = jokesService.searchWeather(params.get("city"));
        // } 
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }

    static class BotQuery {
        QueryResult queryResult;

        public QueryResult getQueryResult() {
            return queryResult;
        }
    }

    static class QueryResult {
        HashMap<String, String> parameters;

        public HashMap<String, String> getParameters() {
            return parameters;
        }
    }

    static class BotResponse {
        String fulfillmentText;
        String source = "BOT";

        public String getFulfillmentText() {
            return fulfillmentText;
        }

        public String getSource() {
            return source;
        }

        public static BotResponse of(String fulfillmentText) {
            BotResponse res = new BotResponse();
            res.fulfillmentText = fulfillmentText;
            return res;
        }
    }

}