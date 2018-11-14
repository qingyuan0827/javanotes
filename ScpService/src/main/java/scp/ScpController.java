package scp;

import application.LabInfo;
import com.jcraft.jsch.JSchException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@RestController
//@Import(EmbeddedTomcatConfiguration.class)
public class ScpController {

    private String checkParameters(Map<String, String> scpParameters){
        if(null == scpParameters.get("sourceHost") || scpParameters.get("sourceHost").isEmpty()){
            return ("sourceHost is null!");
        }
        if(null == scpParameters.get("sourceFile") || scpParameters.get("sourceFile").isEmpty()){
            return ("sourceFile is null!");
        }
        if(null == scpParameters.get("sourceUserName") || scpParameters.get("sourceUserName").isEmpty()){
            return ("sourceUserName is null!");
        }
        if(null == scpParameters.get("sourceUserPassword") || scpParameters.get("sourceUserPassword").isEmpty()){
            return ("sourceUserPassword is null!");
        }
        if(null == scpParameters.get("destHost") || scpParameters.get("destHost").isEmpty()){
            return ("destHost is null!");
        }
        if(null == scpParameters.get("destFile") || scpParameters.get("destFile").isEmpty()){
            return ("destFile is null!");
        }
        if(null == scpParameters.get("destUserName") || scpParameters.get("destUserName").isEmpty()){
            return ("destUserName is null!");
        }
        if(null == scpParameters.get("destUserPassword") || scpParameters.get("destUserPassword").isEmpty()){
            return ("destUserPassword is null!");
        }
        return "ok";
    }

    @RequestMapping("/")
    public String greeting() {
        return "Welcome to upload-download service!";
    }

    @PostMapping(value = "scpservice")
    public String startDownload(@RequestParam Map<String, String> map) {
        System.out.println(map.toString());
        if(null == map || map.isEmpty()){
            return "RequestParam is null! Can't execute scp.";
        }
        String checkResult = checkParameters(map);
        if(!checkResult.equals("ok")){
            return checkResult + " Can't execute scp.";
        }

        ScpService scpInstance = new ScpService();
        if(scpInstance.scpFile(map)) {
            return "SCP successfully!";
        }
        else {
            return "SCP failed!";
        }

    }

public static void main(String[] args) {
    SpringApplication.run(ScpController.class, args);
}
}