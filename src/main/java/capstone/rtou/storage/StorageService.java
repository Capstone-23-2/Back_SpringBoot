package capstone.rtou.storage;

import com.google.cloud.storage.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Service
public class StorageService {

    private final String projectId = "";
    private final String bucketName = "rtou";
    private final Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    private final Acl acl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER);

    public String uploadClientAudio(String userId, MultipartFile file, String sentence) throws IOException {
        BlobId blobId = BlobId.of(bucketName, userId + "/user/" + sentence + ".wav");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("audio/wav")
                .setAcl(new ArrayList<>(Arrays.asList(acl)))
                .build();

        Blob blob = storage.createFrom(blobInfo, file.getInputStream());

        log.info("Upload Url = {}", blob.getMediaLink());

        return blob.getMediaLink();
    }

    public String uploadModelAudioAndSend(String userId, ByteString audioContents) throws IOException {
        byte[] audioBytes = audioContents.toByteArray();

        BlobId blobId = BlobId.of(bucketName, userId + "/model/ModelVoice" + randomString() + ".mp3");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("audio/mpeg")
                .setAcl(new ArrayList<>(Arrays.asList(acl)))
                .build();

        Blob blob = storage.createFrom(blobInfo, new ByteArrayInputStream(audioBytes));


        if (blob != null) {
            log.info("Send Url = {}", blob.getMediaLink());
            return blob.getMediaLink();
        } else {
            return null;
        }
    }

    private String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
