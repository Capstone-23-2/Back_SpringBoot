package capstone.rtou.conversation;

import com.google.cloud.speech.v1.*;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private static String characterName; // AR 캐릭터 이름
    private static String voiceName; // AR 캐릭터 음성
    private static double pitch; // AR 캐릭터 음성 높낮이


    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public byte[] startConversation(String userId, String characterName) throws IOException {

        // 캐릭터 이름을 데이터 모델에 전달하여 대화 시작.
        this.characterName = characterName;

        // 캐릭터 정보 가져오는 코드

        connectML();
        String hello = "Hi. I'm "+ characterName + ". Nice to meet you!! What's your name?";

        byte[] resource = TextToSpeech(hello);

        return resource;
    }

    /**
     * ML과 연결
     */
    private void connectML() {

        // ML 연결 코드
    }


    public byte[] getNextAudio(MultipartFile audioFile) throws IOException {

        String transcribe = SpeechToText(audioFile);
        String mlSentence = sendToML(transcribe);
        byte[] resource = TextToSpeech(mlSentence);

        return resource;
    }

    private String sendToML(String sentence) {

        // 모델에서 문장 가져오는 코드 추가
        return null;
    }

    /**
     * 사용자의 음성을 text로 변환
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
                    .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                    .setSampleRateHertz(44100)
                    .setLanguageCode("en-US")
                    .build();

            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> resultList = response.getResultsList();

            if (!resultList.isEmpty()) {
                SpeechRecognitionResult recognitionResult = resultList.get(0);
                return recognitionResult.getAlternatives(0).getTranscript();
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
    private byte[] TextToSpeech(String sentence) throws IOException {

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

            return audioContents.toByteArray();
        }
    }

    public void endConversation() {

    }
}
