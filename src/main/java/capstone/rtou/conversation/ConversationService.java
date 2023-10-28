package capstone.rtou.conversation;

import capstone.rtou.domain.conversation.CharacterInfo;
import capstone.rtou.domain.conversation.Conversation;
import capstone.rtou.storage.StorageService;
import com.google.cloud.speech.v1.*;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
//    private final CharacterInfoRepository characterInfoRepository;
    private final StorageService storageService;
    private static String characterName; // AR 캐릭터 이름
    private static String voiceName; // AR 캐릭터 음성
    private static double pitch; // AR 캐릭터 음성 높낮이


    public ConversationService(ConversationRepository conversationRepository, StorageService storageService) {
        this.conversationRepository = conversationRepository;
//        this.characterInfoRepository = characterInfoRepository;
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
    public String startConversation(String userId, String characterName) throws IOException {

//        settingVoice(characterName);

        String hello = "Hi. I'm "+ characterName + ". Nice to meet you!! What's your name?";

        ByteString speech = TextToSpeech(hello);
        if (speech != null) {
            return storageService.uploadModelAudioAndSend(userId, speech);
        } else {
            return null;
        }
    }

    /**
     * 캐릭터 음성 설정
     * @param characterName
     */
    /*private void settingVoice(String characterName) {
        CharacterInfo characterInfo = characterInfoRepository.getReferenceById(characterName);
        this.characterName = characterName;
        this.voiceName = characterInfo.getVoice_name();
        this.pitch = characterInfo.getPitch();
    }*/

    /**
     * 사용자로부터 음성 파일을 저장하고 다음 대화 내용 가져오기
     * @param userId
     * @param audioFile
     * @return
     * @throws IOException
     */
    public String getNextAudio(String userId, MultipartFile audioFile) throws IOException {

        // 사용자 음성 파일 업로드
        String clientAudioLink = storageService.uploadClientAudio(userId, audioFile);

        String transcribe = SpeechToText(audioFile); // 사용자 음성 파일 텍스트로 변환
        String mlVoiceUrl = getFromModel(userId, transcribe); // 위에서 얻은 사용자의 문장으로부터 모델에게서 다음 문장 가져오기

        Conversation conversation = new Conversation(userId, clientAudioLink, transcribe);
        conversationRepository.save(conversation);

        return mlVoiceUrl;
    }

    /**
     * 모델로부터 다음 문장 가져오기
     * @param userId
     * @param sentence
     * @return
     * @throws IOException
     */
    private String getFromModel(String userId, String sentence) throws IOException {
        // 모델 API와 연결.

        ByteString speech = TextToSpeech("");

        if (speech != null) {
            return storageService.uploadModelAudioAndSend(userId, speech);
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
     * 모델로부터 받아온 text를 음성 파일로 변환
     *
     * @param sentence
     * @return
     * @throws IOException
     */
    private ByteString TextToSpeech(String sentence) throws IOException {

        if (sentence.isEmpty()) {
            throw new IOException("Required sentence is not present.");
        }

        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(sentence).build();

            CustomVoiceParams voiceParams = CustomVoiceParams.newBuilder()
                    .setReportedUsage(CustomVoiceParams.ReportedUsage.OFFLINE)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setCustomVoice(voiceParams)
                    .setSsmlGender(SsmlVoiceGender.FEMALE)
                    //.setName()
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .setSpeakingRate(0.7)  // 임의로 지정한 수 추후 변경
                    .setPitch(17) // 임의로 지정한 수 -> 추후 캐릭터 테이블에서 데이터를 가져와 설정하는 방법으로 변경
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
}
