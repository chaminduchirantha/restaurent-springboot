package lk.ijse.gdse.restaurentspringbootbackend.service;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SentimentService {

    private final StanfordCoreNLP pipeline;

    public SentimentService() {
        Properties props = new Properties();
        // tokenize = wachana walata bedanna
        // ssplit = vaakya walata bedanna
        // pos = Part of Speech (nouns, verbs, etc.)
        // lemma = wachanaye mul roopa (e.g. went -> go)
        // parse = vaakya grammar tree hadanna
        // sentiment = vaakya sentiment analyze karanna
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public String analyzeSentiment(String text) {
        Annotation annotation = pipeline.process(text);

        int totalScore = 0;
        int count = 0;

        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentimentStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

            totalScore += convertSentimentToScore(sentimentStr);
            count++;
        }

        int avgScore = (count > 0) ? totalScore / count : 3;

        if (avgScore >= 5) return "VERY POSITIVE";
        else if (avgScore == 4) return "POSITIVE";
        else if (avgScore == 3) return "NEUTRAL";
        else if (avgScore == 2) return "NEGATIVE";
        else return "VERY NEGATIVE";
    }

    private int convertSentimentToScore(String sentiment) {
        switch (sentiment.toUpperCase()) {
            case "VERY POSITIVE": return 5;
            case "POSITIVE": return 4;
            case "NEUTRAL": return 3;
            case "NEGATIVE": return 2;
            case "VERY NEGATIVE": return 1;
            default: return 3;
        }
    }

}
