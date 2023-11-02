//package capstone.rtou.estimation;
//
//import capstone.rtou.storage.StorageService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@Slf4j
//public class EstimationService {
//
//    private final RestTemplate restTemplate;
//    private final EstimationRepository repository;
//    private final StorageService storageService;
//    private ObjectMapper objectMapper;
//
//    private final String endpoint = "https://eastus.api.cognitive.microsoft.com/sts/v1.0/issuetoken";
//    private final String key = "673540caf6af45cb94482557d5d1a726";
//
//    public EstimationService(RestTemplateBuilder restTemplateBuilder, EstimationRepository repository, StorageService storageService) {
//        this.restTemplate = restTemplateBuilder.build();
//        this.repository = repository;
//        this.storageService = storageService;
//    }
//
//    public void estimation() {
//        final SpeechConfig speechConfig = SpeechConfig.fromSubscription(YourSubscriptionKey, YourServiceRegion);
//        final AudioConfig audioConfig = AudioConfig.fromWavFileInput(copyAssetToCacheAndGetFilePath());
//        final SpeechRecognizer reco = new SpeechRecognizer(speechConfig, "en-US", audioConfig);
//
//        String referenceText =  "Today was a beautiful day. We had a great time taking a long walk outside in the morning. The countryside was in full bloom, yet the air was crisp and cold. Towards the end of the day, clouds came in, forecasting much needed rain.";
//        // create pronunciation assessment config, set grading system, granularity and if enable miscue based on your requirement.
//        String jsonConfig = "{\"GradingSystem\":\"HundredMark\",\"Granularity\":\"Phoneme\",\"EnableMiscue\":true,\"EnableProsodyAssessment\":true}";
//        PronunciationAssessmentConfig pronConfig = PronunciationAssessmentConfig.fromJson(jsonConfig);
//        pronConfig.setReferenceText(referenceText);
//
//        pronConfig.applyTo(reco);
//
//        ArrayList<String> recognizedWords = new ArrayList<>();
//        ArrayList<Word> pronWords = new ArrayList<>();
//        long[] totalDurations = new long[2]; // duration, valid duration
//        long[] offsets = new long[2]; // start offset, end offset
//        double[] totalAccuracyScore = new double[1];
//        int totalProsodyScore = 0;
//        double sumProsodyScore = 0;
//        reco.recognized.addEventListener((o, speechRecognitionResultEventArgs) -> {
//            final String s = speechRecognitionResultEventArgs.getResult().getText();
//            Log.i(logTag, "Final result received: " + s);
//            PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(speechRecognitionResultEventArgs.getResult());
//            Log.i(logTag, "Accuracy score: " + pronResult.getAccuracyScore() +
//                    ";  pronunciation score: " +  pronResult.getPronunciationScore() +
//                    ", completeness score: " + pronResult.getCompletenessScore() +
//                    ", fluency score: " + pronResult.getFluencyScore());
//            String jString = speechRecognitionResultEventArgs.getResult().getProperties().getProperty(PropertyId.SpeechServiceResponse_JsonResult);
//
//            for (WordLevelTimingResult w: pronResult.getWords()) {
//                Word word = new Word(w.getWord(),
//                        w.getErrorType(),
//                        w.getAccuracyScore(),
//                        w.getDuration() / 10000, // The time unit is ticks (100ns); convert it to milliseconds
//                        w.getOffset() / 10000); // Same as above
//                pronWords.add(word);
//                recognizedWords.add(word.word);
//                totalProsodyScore++;
//                sumProsodyScore += word.prosodyScore;
//                totalAccuracyScore[0] += word.duration * word.accuracyScore;
//                totalDurations[0] += word.duration;
//                if (word.errorType != null && word.errorType.equals("None")) {
//                    totalDurations[1] += word.duration + 10;
//                }
//                offsets[1] = word.offset + word.duration + 10;
//            }
//        });
//
//        reco.sessionStopped.addEventListener((o, s) -> {
//            Log.i(logTag, "Session stopped.");
//            reco.stopContinuousRecognitionAsync();
//
//            // We can calculate whole accuracy score by duration weighted averaging
//            double accuracyScore = totalAccuracyScore[0] / totalDurations[0];
//            double fluencyScore = 0;
//            if (!recognizedWords.isEmpty()) {
//                offsets[0] = pronWords.get(0).offset;
//                fluencyScore = (double)totalDurations[1] / (offsets[1] - offsets[0]) * 100;
//            }
//
//            // For continuous pronunciation assessment mode, the service won't return the words with `Insertion` or `Omission`
//            // even if miscue is enabled.
//            // We need to compare with the reference text after received all recognized words to get these error words.
//            String[] referenceWords = referenceText.toLowerCase().split(" ");
//            for (int j = 0; j < referenceWords.length; j++) {
//                referenceWords[j] = referenceWords[j].replaceAll("^\\p{Punct}+|\\p{Punct}+$","");
//            }
//            Patch<String> diff = DiffUtils.diff(Arrays.asList(referenceWords), recognizedWords, true);
//
//            int currentIdx = 0;
//            ArrayList<Word> finalWords = new ArrayList<>();
//            int[] validWord = new int[1];
//            for (AbstractDelta<String> d : diff.getDeltas()) {
//                if (d.getType() == DeltaType.EQUAL) {
//                    for (int i = currentIdx; i < currentIdx + d.getSource().size(); i++) {
//                        finalWords.add(pronWords.get(i));
//                    }
//                    currentIdx += d.getTarget().size();
//                    validWord[0] += d.getTarget().size();
//                }
//                if (d.getType() == DeltaType.DELETE || d.getType() == DeltaType.CHANGE) {
//                    for (String w : d.getSource().getLines()) {
//                        finalWords.add(new Word(w, "Omission"));
//                    }
//                }
//                if (d.getType() == DeltaType.INSERT || d.getType() == DeltaType.CHANGE) {
//                    for (int i = currentIdx; i < currentIdx + d.getTarget().size(); i++) {
//                        Word w = pronWords.get(i);
//                        w.errorType = "Insertion";
//                        finalWords.add(w);
//                    }
//                    currentIdx += d.getTarget().size();
//                }
//            }
//
//            // Calculate whole completeness score
//            double completenessScore = (double)validWord[0] / referenceWords.length * 100;
//            double prosodyScore = sumProsodyScore / totalProsodyScore;
//            double pronScore = accuracyScore * 0.4 + prosodyScore * 0.2 + fluencyScore * 0.2 + completenessScore * 0.2;
//
//            AppendTextLine("Paragraph pronunciation score: " + pronScore +
//                    ", accuracy score: " + accuracyScore +
//                    ", completeness score: " + completenessScore +
//                    ", fluency score: " + fluencyScore +
//                    ", prosody score: " + prosodyScore + "\n", true);
//            for (Word w : finalWords) {
//                AppendTextLine(" word: " + w.word + "\taccuracy score: " +
//                        w.accuracyScore + "\terror type: " + w.errorType, false);
//            }
//
//            enableButtons();
//        });
//
//        reco.startContinuousRecognitionAsync();
//    }
//
//    private void getEstimation() {
//        SpeechRecognizer speechRecognizer = new SpeechRecognizer(
//                speechConfig,
//                audioConfig);
//
//        pronunciationAssessmentConfig.applyTo(speechRecognizer);
//        Future<SpeechRecognitionResult> future = speechRecognizer.recognizeOnceAsync();
//        SpeechRecognitionResult speechRecognitionResult = future.get(30, TimeUnit.SECONDS);
//
//// The pronunciation assessment result as a Speech SDK object
//        PronunciationAssessmentResult pronunciationAssessmentResult =
//                PronunciationAssessmentResult.fromResult(speechRecognitionResult);
//
//// The pronunciation assessment result as a JSON string
//        String pronunciationAssessmentResultJson = speechRecognitionResult.getProperties().getProperty(PropertyId.SpeechServiceResponse_JsonResult);
//
//        recognizer.close();
//        speechConfig.close();
//        audioConfig.close();
//        pronunciationAssessmentConfig.close();
//        speechRecognitionResult.close();
//    }
//
//    public void getEstimationById(String userId) {
//        repository.findAllById(userId);
//    }
//}
