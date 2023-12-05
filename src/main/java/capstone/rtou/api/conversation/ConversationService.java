package capstone.rtou.api.conversation;

import capstone.rtou.api.auth.AuthRepository;
import capstone.rtou.api.conversation.dto.ConversationRequestDto;
import capstone.rtou.api.conversation.dto.ConversationResponse;
import capstone.rtou.api.conversation.dto.ModelRequest;
import capstone.rtou.api.conversation.dto.ModelResponse;
import capstone.rtou.api.conversation.repository.CharacterInfoRepository;
import capstone.rtou.api.conversation.repository.ConversationCharacterRepository;
import capstone.rtou.api.conversation.repository.ConversationsRepository;
import capstone.rtou.api.estimation.repository.ErrorWordRepository;
import capstone.rtou.api.estimation.repository.EstimationRepository;
import capstone.rtou.api.estimation.repository.EstimationScoreRepository;
import capstone.rtou.domain.conversation.CharacterInfo;
import capstone.rtou.domain.conversation.ConversationCharacter;
import capstone.rtou.api.storage.StorageService;
import capstone.rtou.domain.conversation.Conversations;
import capstone.rtou.domain.conversation.ConversationsId;
import capstone.rtou.domain.estimation.ErrorWords;
import capstone.rtou.domain.estimation.EstimationScores;
import capstone.rtou.domain.estimation.Estimations;
import capstone.rtou.domain.estimation.EstimationsId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioInputStream;
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class ConversationService {

    private final AuthRepository authRepository;
    private final ConversationsRepository conversationsRepository;
    private final CharacterInfoRepository characterInfoRepository;
    private final ConversationCharacterRepository conversationCharacterRepository;
    private final EstimationRepository estimationRepository;
    private final EstimationScoreRepository estimationScoreRepository;
    private final ErrorWordRepository errorWordRepository;
    private final RestTemplate restTemplate;
    private final StorageService storageService;
    private final String key = "673540caf6af45cb94482557d5d1a726";

    public ConversationService(AuthRepository authRepository, ConversationsRepository conversationsRepository, CharacterInfoRepository characterInfoRepository, ConversationCharacterRepository conversationCharacterRepository, EstimationRepository estimationRepository, EstimationScoreRepository estimationScoreRepository, ErrorWordRepository errorWordRepository, RestTemplateBuilder restTemplateBuilder , StorageService storageService) {
        this.authRepository = authRepository;
        this.conversationsRepository = conversationsRepository;
        this.characterInfoRepository = characterInfoRepository;
        this.conversationCharacterRepository = conversationCharacterRepository;
        this.estimationRepository = estimationRepository;
        this.estimationScoreRepository = estimationScoreRepository;
        this.errorWordRepository = errorWordRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.storageService = storageService;
    }

    /**
     * 시작 대화 생성
     *
     * @param userId
     * @param characterName
     * @return
     * @throws IOException
     */
    @Transactional
    public ConversationResponse startConversation(String userId, String characterName) throws IOException, ExecutionException, InterruptedException {
        String hello = "Hi. I'm " + characterName + ". Nice to meet you!! What's your name?";
        String conversationId = randomString();

        if (!authRepository.existsById(userId)) {
            return new ConversationResponse(false, "등록되지 않은 사용자입니다.");
        }

        CharacterInfo characterInfo = characterInfoRepository.findByName(characterName);

        if (characterInfo == null) {
            return new ConversationResponse(false, "해당하는 캐릭터 정보가 없습니다.");
        }

        ByteString audioContent = TextToSpeech(hello, characterInfo.getVoiceName(), characterInfo.getPitch(), characterInfo.getLangCode());

        CompletableFuture<ConversationResponse> conversationResponse =  CompletableFuture.supplyAsync(() -> {
            try {
                if (audioContent != null) {
                    String audioLink = storageService.uploadModelAudioAndSend(userId, audioContent);
                    conversationsRepository.save(new Conversations(new ConversationsId(conversationId, userId, hello)));
                    return new ConversationResponse(true, "음성 생성 완료", conversationId, audioLink);
                } else {
                    return new ConversationResponse(false, "음성이 생성X");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.runAsync(() -> {
            getSentence(characterName, "<start>");

            if (conversationCharacterRepository.existsById(userId)) {
                conversationsRepository.save(new Conversations(new ConversationsId(conversationId, userId, hello)));
                conversationCharacterRepository.updateByUserId(userId, characterName);
            } else {
                conversationsRepository.save(new Conversations(new ConversationsId(conversationId, userId, hello)));
                conversationCharacterRepository.save(new ConversationCharacter(userId, characterName));
            }
        });

        return conversationResponse.get();
    }

    /**
     * 사용자로부터 받은 음성을 대화 아이디와 같이 저장하고 다음 대화를 전달.
     *
     * @param userId
     * @param conversationId
     * @param audioFile
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Transactional
    public ConversationResponse getNextAudio(String userId, String conversationId, MultipartFile audioFile) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        String character = conversationCharacterRepository.findByUserId(userId);
        CharacterInfo characterInfo = characterInfoRepository.getReferenceById(character);
        String sentence = SpeechToText(audioFile); // 사용자 음성 파일 텍스트로 변환
        AtomicReference<String> url = new AtomicReference<>();

        CompletableFuture.runAsync(() -> {
            try {
                pronunciationAssessment(userId, conversationId, sentence, audioFile);
            } catch (ExecutionException | InterruptedException | TimeoutException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<String> mlSentence = getSentence(character, sentence).thenApply(result -> {
            try {
                ByteString speech = TextToSpeech(result, characterInfo.getVoiceName(), characterInfo.getPitch(), characterInfo.getLangCode());
                if (speech != null) {
                    url.set(storageService.uploadModelAudioAndSend(userId, speech));
                    return result;
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orTimeout(1, TimeUnit.MILLISECONDS);

        if (mlSentence.get() != null) {

            conversationsRepository.save(new Conversations(new ConversationsId(conversationId, userId, mlSentence.get())));
            return new ConversationResponse(true, "사용자 음성 저장 및 음성 생성 완료", url.get());
        } else {
            return new ConversationResponse(false, "음성 생성 X");
        }
    }

    @Transactional
    public ConversationResponse endConversation(ConversationRequestDto conversationRequest) {
        EstimationsId id = new EstimationsId(conversationRequest.getConversationId(), conversationRequest.getUserId());

        String character = conversationCharacterRepository.findByUserId(conversationRequest.getUserId());
        estimationRepository.save(new Estimations(id, character));

        return new ConversationResponse(true, "대화 종료");
    }

    /**
     * 사용자 음성을 텍스트로 변환
     *
     * @param audioFile
     * @return
     * @throws IOException
     */
    private String SpeechToText(MultipartFile audioFile) throws IOException {
        long start = System.currentTimeMillis();
        if (audioFile.isEmpty()) {
            throw new IOException("Required audioFile is not present.");
        }

        ByteString audioString = ByteString.copyFrom(audioFile.getBytes());

        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-US")
                    .setSampleRateHertz(16000)
                    .build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioString)
                    .build();

            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> resultList = response.getResultsList();

            if (!resultList.isEmpty()) {
                SpeechRecognitionResult recognitionResult = resultList.get(0);
                String sentence = recognitionResult.getAlternatives(0).getTranscript();
                long end = System.currentTimeMillis();
                log.info("Convert Speech To Text={}ms", end - start);
                return sentence;
            } else {
                log.error("No transcription result found");
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 모델로부터 받아온 텍스트를 음성으로 변환.
     *
     * @param sentence
     * @param voiceName
     * @param pitch
     * @return
     * @throws IOException
     */
    private ByteString TextToSpeech(String sentence, String voiceName, double pitch, String langCode) throws IOException {
        long start = System.currentTimeMillis();
        if (sentence.isEmpty()) {
            throw new IOException("Required sentence is not present.");
        }

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(sentence).build();

            CustomVoiceParams voiceParams = CustomVoiceParams.newBuilder()
                    .setReportedUsage(CustomVoiceParams.ReportedUsage.OFFLINE)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(langCode)
                    .setCustomVoice(voiceParams)
                    .setName(voiceName)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(0.75)
                    .setPitch(pitch)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

            if (!audioContents.isEmpty()) {
                long end = System.currentTimeMillis();
                log.info("Convert Speech To Text={}ms", end - start);
                return audioContents;
            } else {
                return null;
            }
        }
    }

    /**
     * 사용자 아이디와 대화 아이디가 일치하는 문장들을 가져와 음성 평가 결과 반환.
     * 비동기식으로 처리.
     *
     * @param userId
     * @param conversationId
     * @param reference
     * @param audioFile
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    @Async(value = "AsyncExecutor")
    public void pronunciationAssessment(String userId, String conversationId, String reference, MultipartFile audioFile) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        log.info("Async Pronunciation Start");

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(key, "eastus");
        String lang = "en-US";

        PullAudioInputStream stream = AudioInputStream.createPullStream(new WavStream(audioFile.getInputStream()));

        com.microsoft.cognitiveservices.speech.audio
                .AudioConfig audioConfig = com.microsoft.cognitiveservices.speech.audio
                .AudioConfig.fromStreamInput(stream);

        PronunciationAssessmentConfig pronunciationConfig = new PronunciationAssessmentConfig(reference,
                PronunciationAssessmentGradingSystem.HundredMark, PronunciationAssessmentGranularity.Word, true);
        pronunciationConfig.enableProsodyAssessment();

        // 발음 평가 결과 가져오기
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, lang, audioConfig);

        pronunciationConfig.applyTo(speechRecognizer);
        Future<com.microsoft.cognitiveservices.speech
                .SpeechRecognitionResult> future = speechRecognizer.recognizeOnceAsync();
        com.microsoft.cognitiveservices.speech
                .SpeechRecognitionResult speechRecognitionResult = future.get(30, TimeUnit.SECONDS);

        // JSON string으로 발음 평가 결과 가져오기
        String pronunciationAssessmentResultJson = speechRecognitionResult.getProperties()
                .getProperty(PropertyId.SpeechServiceResponse_JsonResult);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(pronunciationAssessmentResultJson);
        String displayText = rootNode.path("DisplayText").asText();
        JsonNode pronunciation = rootNode.path("NBest").get(0).path("PronunciationAssessment");
        double accuracy = pronunciation.path("AccuracyScore").asDouble();
        double prosody = pronunciation.path("ProsodyScore").asDouble();
        double fluency = pronunciation.path("FluencyScore").asDouble();
        double completeness = pronunciation.path("CompletenessScore").asDouble();
        double pron = pronunciation.path("PronScore").asDouble();

        conversationsRepository.save(new Conversations(new ConversationsId(conversationId, userId, displayText)));

        EstimationScores estimationScores = new EstimationScores(
                userId, conversationId, displayText, accuracy, prosody, pron, fluency, completeness);

        estimationScoreRepository.save(estimationScores);
        JsonNode wordsNode = rootNode.path("NBest").get(0).path("Words");
        for (JsonNode wordNode : wordsNode) {
            JsonNode pronunciationAssessment = wordNode.path("PronunciationAssessment");
            String errorWord = pronunciationAssessment.path("Word").asText();
            String errorType = pronunciationAssessment.path("ErrorType").asText();
            if (errorType == "None") {
                ErrorWords errorWords = new ErrorWords(userId, displayText, errorWord, errorType);
                errorWordRepository.save(errorWords);
            }
        }

        speechRecognizer.close();
        speechConfig.close();
        pronunciationConfig.close();
        speechRecognitionResult.close();

        log.info("Async Pronunciation End");
    }

    @Async(value = "AsyncExecutor")
    public CompletableFuture<String> getSentence(String characterName, String sentence) {
//        String apiUrl = "http://localhost:8000/conversation"; // API 엔드포인트 URL
        String apiUrl = "http://13.124.7.35:8000/conversation";

        // API 호출
        ModelResponse modelResponse = restTemplate.postForObject(apiUrl, new ModelRequest(1, sentence, characterName), ModelResponse.class);
        return CompletableFuture.completedFuture(modelResponse.getText());
    }

    private String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
