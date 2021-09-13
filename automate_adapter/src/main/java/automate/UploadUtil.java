package automate;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.io.IOException;

public class UploadUtil {
    static String secretId = "";
    static String secretKey = "";
    static String bucketName = "";
    static Region region;
    static COSCredentials cred;
    static ClientConfig clientConfig;
    static COSClient cosClient;

    static {
        region = new Region("ap-beijing");
        cred = new BasicCOSCredentials(secretId, secretKey);
        clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        cosClient = new COSClient(cred, clientConfig);
    }

    public static boolean upload(String movieId){
        try{
            File f = new File(  movieId + "/face.png");
            String key = "proj/" + movieId + ".png";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, f);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
