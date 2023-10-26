package capstone.rtou.conversation;

import capstone.rtou.domain.conversation.Conversation;
import com.google.cloud.speech.v1.*;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    //    private final Module module;
    private static String userId; // 사용자 아이디
    private static String characterName; // AR 캐릭터 이름
    private static String voiceName; // AR 캐릭터 음성
    private static double pitch; // AR 캐릭터 음성 높낮이


    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
//        try (InputStream is = getClass().getResourceAsStream("")){
//            this.module = Module.load();
//        } catch (IOException e) {
//            throw new RuntimeException("모델 파일을 불러올 수 없습니다.");
//        }
    }

    /**
     * 시작 대화 생성
     * @param userId
     * @param characterName
     * @return
     * @throws IOException
     */
    public byte[] startConversation(String userId, String characterName) throws IOException, InterruptedException {

        // 캐릭터 이름을 데이터 모델에 전달하여 대화 시작.
        this.characterName = characterName;
        this.userId = userId;

        // 캐릭터 정보 가져오는 코드
        String hello = "Hi. I'm "+ characterName + ". Nice to meet you!! What's your name?";
        queue.put(hello);

        byte[] resource = TextToSpeech(hello);

        return resource;
    }


    /**
     * 클라이언트에게 보낼 데이터 가져오기
     * @param sentence
     * @return
     */
//    private String sendToML(String sentence) {
//
//        float[] inputData = parseInputData(sentence);
//
//        Tensor inputTensor = Tensor.fromBlob(inputData, new long[]{1, inputData.length});
//
//        IValue output = module.forward(IValue.from(inputTensor));
//        Tensor outputTensor = output.toTensor();
//
//        byte[] result = outputTensor.getDataAsByteArray();
//
//        return parseOutput(result);
//    }

    /**
     * 클라이언트에게 문장을 보내기 위해 byte 데이터를 String으로 변환
     * @param result
     * @return
     */
    private String parseOutput(byte[] result) {
        String text = new String(result, StandardCharsets.UTF_8);

        return text;
    }

    /**
     * 문장을 모델에 전달할 수 있도록 float[]으로 변환.
     * @param input
     * @return
     */
    private float[] parseInputData(String input) {
        String[] strings = input.split(".");

        float[] data = new float[input.length()];

        for (int i = 0; i < input.length(); i++) {
            data[i] = Float.parseFloat(strings[i]);
        }

        return data;
    }


    public byte[] getNextAudio(MultipartFile audioFile) throws IOException, InterruptedException {

        String transcribe = SpeechToText(audioFile);
        /*String mlSentence = sendToML(transcribe);

        Conversation conversation = new Conversation(userId, queue.take(), transcribe);

        conversationRepository.save(conversation);
        queue.put(mlSentence);

        byte[] resource = TextToSpeech(mlSentence);

        return resource;*/
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
}
