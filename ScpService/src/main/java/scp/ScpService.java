package scp;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Properties;

public class ScpService
{
    public ScpService() {

    }

    public boolean scpFile(Map<String, String> scpMapInfo) {
        String localTmpDir = "/tmp/";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            localTmpDir = "D://";
        }

        File sourceFile =new File( scpMapInfo.get("sourceFile").trim());
        String localTmpFile = localTmpDir + sourceFile.getName();

        try {
            downloadFile(localTmpFile,scpMapInfo);
            uploadFile(localTmpFile,scpMapInfo);
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void uploadFile(String sourceFile, Map<String, String> scpMapInfo) throws JSchException
    {
        ChannelSftp sftp = getChannelSftp(scpMapInfo.get("destHost"), scpMapInfo.get("destUserName"),
                scpMapInfo.get("destUserPassword"));
        upload(sourceFile, scpMapInfo.get("destFile"), sftp );
        sftp.getSession().disconnect();
        System.out.println( "Session closed." );
        sftp.disconnect();
        System.out.println( "Connection closed." );
        System.out.println( sourceFile + " has been upload to remote path " + scpMapInfo.get("destFile"));
    }

    private void downloadFile( String destFile,Map<String, String> scpMapInfo) throws JSchException
    {
        ChannelSftp sftp = getChannelSftp(scpMapInfo.get("sourceHost"), scpMapInfo.get("sourceUserName"),
                scpMapInfo.get("sourceUserPassword"));
        //download( sourceDir + "oss_trace0_0.log", destDir + "oss_trace0_0.log", sftp );
        download(scpMapInfo.get("sourceFile"), destFile, sftp );
        sftp.getSession().disconnect();
        System.out.println( "Session closed." );
        sftp.disconnect();
        System.out.println( "Connection closed." );
        System.out.println( scpMapInfo.get("sourceFile") + " has been downloaded to local path " + destFile);
    }

    private ChannelSftp getChannelSftp(String hostName, String userName, String userPassword) throws JSchException {
        return connect( hostName, 22, userName, userPassword );
    }

    /**
     * 
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws JSchException
     */
    private static ChannelSftp connect( String host, int port, String username, String password ) throws JSchException
    {
        ChannelSftp sftp = null;
        JSch jsch = new JSch();
        jsch.getSession( username, host, port );
        Session sshSession = jsch.getSession( username, host, port );
        System.out.println( "Session created.");
        sshSession.setPassword( password );
        Properties sshConfig = new Properties();
        sshConfig.put( "StrictHostKeyChecking", "no" );
        sshSession.setConfig( sshConfig );
        sshSession.connect();
        System.out.println( "Session connected." );
        System.out.println( "Opening Channel." );
        Channel channel = sshSession.openChannel( "sftp" );
        channel.connect();
        sftp = (ChannelSftp) channel;
        System.out.println( "Connected to " + host + "." );
        return sftp;
    }

    /**
     * 
     * @param srcFile
     * @param destFile
     * @param sftp
     */
    private static void upload( String srcFile, String destFile, ChannelSftp sftp )
    {
        try
        {
            System.out.println( "starting upload " + srcFile );
            File file = new File( srcFile );
            sftp.put( new FileInputStream( file ), destFile );
            System.out.println( "upload " + srcFile + " successfully" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static void download( String srcFile, String dstFile, ChannelSftp sftp )
    {
        try
        {
            System.out.println( "starting download " + srcFile );
            sftp.get(srcFile, dstFile );
            System.out.println( "download " + srcFile + " successfully" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
}
