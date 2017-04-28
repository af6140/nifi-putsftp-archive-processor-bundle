package com.entertainment.nifi.processor;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entertainment.nifi.processor.util.ConfigurableSFTPTransfer;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class PutSFTPWithArchiveTest {

    SFTPTestServer sftpTestServer =null;
    String vfsPath = "target/sftp";
    String userName = "test";
    @Before
    public void setUp() {
        this.sftpTestServer = new SFTPTestServer(2322, vfsPath);
    }
    @org.junit.Test
    public void testOnTrigger(){
        // Content to be mock a json file
        try {

            // Generate a test runner to mock a processor in a flow
            TestRunner runner = TestRunners.newTestRunner(new PutSFTPWithArchive());

            // Add properties
            runner.setProperty(ConfigurableSFTPTransfer.ROTATE_COUNT, "4");
            runner.setProperty("Hostname", "localhost");
            runner.setProperty("Port", "2322");
            runner.setProperty("Username", userName);
            runner.setProperty("Password", "test");
            runner.setValidateExpressionUsage(false);
            // Add the content to the runner
            MockFlowFile inputFlowFile = runner.enqueue("target/test-classes/test.xml");
            Map fileAttributes = new HashMap<String, String>();

            fileAttributes.put("filename", "test.xml");
            //fileAttributes.put(SFTPTransfer.REMOTE_PATH, "inbound");
            inputFlowFile.putAttributes(fileAttributes);
            System.out.println("flow file name: " + inputFlowFile.getAttribute("filename"));

            // Run the enqueued content, it also takes an int = number of contents queued
            runner.run(1);

            // All results were processed with out failure
            runner.assertQueueEmpty();

            // If you need to read or do additional tests on results you can access the content
            List<MockFlowFile> results = runner.getFlowFilesForRelationship(PutSFTPWithArchive.REL_SUCCESS);
            assertTrue("1 match", results.size() == 1);
            MockFlowFile result = results.get(0);
            //String resultValue = new String(runner.getContentAsByteArray(result));
            //System.out.println("Match: " + IOUtils.toString(runner.getContentAsByteArray(result)));

            // Test attributes and content
            //result.assertAttributeEquals("Rotate Count", "4");
            //result.assertContentEquals("nifi rocks");

            assertTrue(Files.exists(new File(vfsPath).toPath().resolve(userName+ FileSystems.getDefault().getSeparator()+"test.xml")));

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @After
    public void tearDown() {
        if(this.sftpTestServer!=null) {
            this.sftpTestServer.stop();
        }
    }
}

