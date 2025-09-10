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
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public String analyzeSentiment(String text) {
        Annotation annotation = pipeline.process(text);
        int mainSentiment = 0;
        int longest = 0;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentimentStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            int sentimentScore = convertSentimentToScore(sentimentStr);
            String sentenceText = sentence.toString();
            if (sentenceText.length() > longest) {
                mainSentiment = sentimentScore;
                longest = sentenceText.length();
            }
        }

        if (mainSentiment > 2) return "POSITIVE";
        else if (mainSentiment < 2) return "NEGATIVE";
        else return "NEUTRAL";
    }

    private int convertSentimentToScore(String sentiment) {
        switch (sentiment.toUpperCase()) {
            case "VERY POSITIVE": return 4;
            case "POSITIVE": return 3;
            case "NEUTRAL": return 2;
            case "NEGATIVE": return 1;
            case "VERY NEGATIVE": return 0;
            default: return 2;
        }
    }
}
