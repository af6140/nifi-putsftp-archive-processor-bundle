package com.entertainment.nifi.processor;

import com.entertainment.nifi.processor.util.ConfigurableSFTPTransfer;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//@SideEffectFree
@Tags({"SFTP", "File", "Archive"})
@CapabilityDescription("Archive file on Remote SFTP by rotate it.")
public class PutSFTPWithArchive extends PutFileTransfer<ConfigurableSFTPTransfer> {



    private List<PropertyDescriptor> properties;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> properties = init_properties();
        this.properties = Collections.unmodifiableList(properties);
    }

    protected List<PropertyDescriptor> init_properties() {
        List<PropertyDescriptor> properties = new ArrayList<>();

        properties.add(ConfigurableSFTPTransfer.PROPERTIES_FILE_SERVICE);
        properties.add(ConfigurableSFTPTransfer.CONFIG_SCOPE);
        properties.add(ConfigurableSFTPTransfer.ROTATE_COUNT);

        properties.add(ConfigurableSFTPTransfer.HOSTNAME);
        properties.add(ConfigurableSFTPTransfer.PORT);
        properties.add(ConfigurableSFTPTransfer.USERNAME);
        properties.add(ConfigurableSFTPTransfer.PASSWORD);
        properties.add(ConfigurableSFTPTransfer.PRIVATE_KEY_PATH);
        properties.add(ConfigurableSFTPTransfer.PRIVATE_KEY_PASSPHRASE);
        properties.add(ConfigurableSFTPTransfer.REMOTE_PATH);
        properties.add(ConfigurableSFTPTransfer.CREATE_DIRECTORY);
        properties.add(ConfigurableSFTPTransfer.BATCH_SIZE);
        properties.add(ConfigurableSFTPTransfer.CONNECTION_TIMEOUT);
        properties.add(ConfigurableSFTPTransfer.DATA_TIMEOUT);
        properties.add(ConfigurableSFTPTransfer.CONFLICT_RESOLUTION);
        properties.add(ConfigurableSFTPTransfer.REJECT_ZERO_BYTE);
        properties.add(ConfigurableSFTPTransfer.DOT_RENAME);
        properties.add(ConfigurableSFTPTransfer.TEMP_FILENAME);
        properties.add(ConfigurableSFTPTransfer.HOST_KEY_FILE);
        properties.add(ConfigurableSFTPTransfer.LAST_MODIFIED_TIME);
        properties.add(ConfigurableSFTPTransfer.PERMISSIONS);
        properties.add(ConfigurableSFTPTransfer.REMOTE_OWNER);
        properties.add(ConfigurableSFTPTransfer.REMOTE_GROUP);
        properties.add(ConfigurableSFTPTransfer.STRICT_HOST_KEY_CHECKING);
        properties.add(ConfigurableSFTPTransfer.USE_KEEPALIVE_ON_TIMEOUT);
        properties.add(ConfigurableSFTPTransfer.USE_COMPRESSION);

        return properties;
    }
    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }


    @Override
    protected void beforePut(final FlowFile flowFile, final ProcessContext context, final ConfigurableSFTPTransfer transfer) throws IOException {
        final ComponentLog logger = getLogger();

        //explicitly initialize sftp channel
        // initialize channel, so scope can be populate etc
        transfer.init_channel(flowFile);
        logger.info("End of beforePut");
    }

//    private  void archiveFile(final ConfigurableSFTPTransfer transfer, final String path, final String remoteFileName, int keepCount) throws IOException {
//       final ComponentLog logger = getLogger();
//       logger.info("Archive remote file {} in {}", new Object[]{remoteFileName, path});
//       if(keepCount<1) {
//           logger.debug("Rotate count is less than 1, skip archive.");
//           return;
//       }
//       String remoteFileNameDelete = remoteFileName+"."+keepCount;
//        try {
//            //final String rootPath = context.getProperty(FileTransfer.REMOTE_PATH).evaluateAttributeExpressions(flowFile).getValue();
//
//            //String fullPath = path == null?remoteFileNameDelete:(path.endsWith("/")?path + remoteFileNameDelete:path + "/" + remoteFileNameDelete);
//            transfer.deleteFile(path, remoteFileNameDelete);
//        }catch (FileNotFoundException e){
//            logger.info("Error while delete file: {}", new Object[]{e.getMessage()});
//        }
//
//        for(int i=keepCount-1; i>=0; i--) {
//            String currentFileName = i==0 ? remoteFileName : remoteFileName+"."+i;
//            String targetFileName = remoteFileName+"."+(i+1);
//            String oldFullPath = (path == null) ? currentFileName : (path.endsWith("/")) ? path + currentFileName : path + "/" + currentFileName;
//            String newFullPath = (path == null) ? targetFileName : (path.endsWith("/")) ? path + targetFileName : path + "/" + targetFileName;
//            logger.debug("Rename file from {} to {}", new Object[]{oldFullPath, newFullPath});
//            try {
//                transfer.rename(oldFullPath, newFullPath);
//            }catch (FileNotFoundException e){
//                logger.debug("Cannot found file renaming: {}.", new Object[]{e.getMessage()});
//            }
//        }
//    }

    @Override
    protected ConfigurableSFTPTransfer getFileTransfer(ProcessContext context) {
        return new ConfigurableSFTPTransfer(context, getLogger());
    }
}
