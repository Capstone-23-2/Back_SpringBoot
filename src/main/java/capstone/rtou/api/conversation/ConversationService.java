package capstone.rtou.api.conversation;

import capstone.rtou.api.auth.AuthRepository;
import capstone.rtou.api.conversation.dto.ConversationResponse;
import capstone.rtou.api.conversation.model.ModelService;
import capstone.rtou.api.conversation.repository.CharacterInfoRepository;
import capstone.rtou.api.conversation.repository.ConversationCharacterRepository;
import capstone.rtou.api.conversation.repository.ConversationRepository;
import capstone.rtou.api.estimation.dto.EstimationResponse;
import capstone.rtou.api.estimation.repository.ErrorWordRepository;
import capstone.rtou.api.estimation.repository.EstimationRepository;
import capstone.rtou.domain.conversation.CharacterInfo;
import capstone.rtou.domain.conversation.Conversations;
import capstone.rtou.domain.conversation.ConversationCharacter;
import capstone.rtou.domain.estimation.ErrorWords;
import capstone.rtou.domain.estimation.EstimationResult;
import capstone.rtou.storage.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.microsoft.cognitiveservices.speech.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class ConversationService {

    private final AuthRepository authRepository;
    private final ConversationRepository conversationRepository;
    private final CharacterInfoRepository characterInfoRepository;
    private final ConversationCharacterRepository conversationCharacterRepository;
    private final EstimationRepository estimationRepository;
    private final ErrorWordRepository errorWordRepository;
    private final ModelService modelService;
    private final StorageService storageService;
    private final String key = "673540caf6af45cb94482557d5d1a726";

    public ConversationService(AuthRepository authRepository, ConversationRepository conversationRepository, CharacterInfoRepository characterInfoRepository, ConversationCharacterRepository conversationCharacterRepository, EstimationRepository estimationRepository, ErrorWordRepository errorWordRepository, StorageService storageService, ModelService modelService) {
        this.authRepository = authRepository;
        this.conversationRepository = conversationRepository;
        this.characterInfoRepository = characterInfoRepository;
        this.conversationCharacterRepository = conversationCharacterRepository;
        this.estimationRepository = estimationRepository;
        this.errorWordRepository = errorWordRepository;
        this.storageService = storageService;
        this.modelService = modelService;
    }

    /**
     * 시작 대화 생성
     * @param userId
     * @param characterName
     * @return
     * @throws IOException
     */
    @Transactional
    public ByteArrayResource startConversation(String userId, String characterName) throws IOException {

        if (!authRepository.existsById(userId)) {
            return null;
        }

        if (!characterInfoRepository.existsById(characterName)) {
            return null;
        }

        if (conversationCharacterRepository.existsById(userId)) {
            conversationCharacterRepository.updateByUserId(userId, characterName);
        } else {
            conversationCharacterRepository.save(new ConversationCharacter(userId,characterName));
        }

        String hello = "Hi. I'm "+ characterName + ". Nice to meet you!! What's your name?";

        modelService.getSentence(characterName, "<start>");

        CharacterInfo characterInfo = characterInfoRepository.findByName(characterName);
        ByteString speech = TextToSpeech(hello, characterInfo.getVoiceName(), characterInfo.getPitch(), characterInfo.getLangCode());

        if (speech != null) {
            return new ByteArrayResource(speech.toByteArray());
        } else {
            return null;
        }
    }

    /**
     * 사용자로부터 받은 음성을 대화 아이디와 같이 저장하고 다음 대화를 전달.
     * @param userId
     * @param audioFile
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Transactional
    public ByteArrayResource getNextAudio(String userId, MultipartFile audioFile) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        String transcribe = SpeechToText(audioFile); // 사용자 음성 파일 텍스트로 변환
        ByteString model = getFromModel(userId, transcribe); // 위에서 얻은 사용자의 문장으로부터 모델에게서 다음 문장 가져오기

        if (model != null) {
            // 사용자 음성 저장.
            String clientAudioLink = storageService.uploadClientAudio(userId, audioFile, transcribe);
            pronunciationAssessment(userId, transcribe, clientAudioLink);
            Conversations conversations = new Conversations(userId, clientAudioLink, transcribe);
            conversationRepository.save(conversations);

            return new ByteArrayResource(model.toByteArray());
        } else {
            return null;
        }
    }

    /**
     * 모델로부터 다음 문장 가져오기
     * @param userId
     * @param sentence
     * @return
     * @throws IOException
     */
    private ByteString getFromModel(String userId, String sentence) throws IOException {
        // 모델 API와 연결.
        String character = conversationCharacterRepository.findByUserId(userId);
        String mlSentence = modelService.getSentence(character, sentence);
        CharacterInfo characterInfo = characterInfoRepository.getReferenceById(character);
        ByteString speech = TextToSpeech(mlSentence, characterInfo.getVoiceName(), characterInfo.getPitch(), characterInfo.getLangCode());

        if (speech != null) {
            return speech;
        } else {
            return null;
        }
    }

    /**
     * 사용자로부터 받은 음성 파일을 문장으로 변환
     * @param audioFile
     * @return
     * @throws IOException
     */
    private String SpeechToText(MultipartFile audioFile) throws IOException {

        if (audioFile.isEmpty()) {
            throw new IOException("Required audioFile is not present.");
        }

        byte[] audioBytes = audioFile.getBytes();

        try (SpeechClient speechClient = SpeechClient.create()) {
            ByteString audioData = ByteString.copyFrom(audioBytes);

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(44100)
                    .setLanguageCode("en-US")
                    .build();


            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> resultList = response.getResultsList();

            if (!resultList.isEmpty()) {
                SpeechRecognitionResult recognitionResult = resultList.get(0);
                String sentence = recognitionResult.getAlternatives(0).getTranscript();
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
     * @param sentence
     * @param voiceName
     * @param pitch
     * @return
     * @throws IOException
     */
    private ByteString TextToSpeech(String sentence, String voiceName, double pitch, String langCode) throws IOException {

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
                    .setSpeakingRate(0.8)
                    .setPitch(pitch)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

            if (!audioContents.isEmpty()) {
                return audioContents;
            } else {
                return null;
            }
        }
    }

    /**
     * 사용자 아이디와 대화 아이디가 일치하는 문장들을 가져와 음성 평가 결과 반환.
     * 비동기식으로 처리.
     * @param userId
     * @param sentence
     * @param audioLink
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws JsonProcessingException
     */
    @Async(value = "PronAsyncExecutor")
    public void pronunciationAssessment(String userId, String sentence, String audioLink) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        log.info("Async Pronunciation Start");

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(key, "eastus");
        String lang = "en-US";

        com.microsoft.cognitiveservices.speech.audio.AudioConfig audioConfig = com.microsoft.cognitiveservices.speech.audio.AudioConfig.fromWavFileOutput(audioLink);

        String referenceText = sentence;
        String jsonConfig = "{\"referenceText\":\""+sentence+"\"," +
                "\"gradingSystem\":\"HundredMark\"," +
                "\"granularity\":\"Word\",\"enableMiscue\":true}";
        PronunciationAssessmentConfig pronunciationConfig = PronunciationAssessmentConfig.fromJson(jsonConfig);
        pronunciationConfig.setReferenceText(referenceText);

        // 발음 평가 결과 가져오기
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, lang, audioConfig);

        pronunciationConfig.applyTo(speechRecognizer);
        Future<com.microsoft.cognitiveservices.speech.SpeechRecognitionResult> future = speechRecognizer.recognizeOnceAsync();
        com.microsoft.cognitiveservices.speech.SpeechRecognitionResult speechRecognitionResult = future.get(30, TimeUnit.SECONDS);

        // The pronunciation assessment result as a JSON string
        String pronunciationAssessmentResultJson = speechRecognitionResult.getProperties()
                .getProperty(PropertyId.SpeechServiceResponse_JsonResult);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(pronunciationAssessmentResultJson);
        String displayText = rootNode.path("DisplayText").asText();
        JsonNode pronunciation = rootNode.path("NBest").get(0).path("PronunciationAssessment");
        double accuracy = pronunciation.path("AccuracyScore").asDouble();
        double fluency = pronunciation.path("FluencyScore").asDouble();
        double completeness = pronunciation.path("CompletenessScore").asDouble();
        double pron = pronunciation.path("PronScore").asDouble();

        EstimationResult result = new EstimationResult(
                userId, displayText,
                accuracy, fluency, completeness, pron);
        estimationRepository.save(result);

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
}
