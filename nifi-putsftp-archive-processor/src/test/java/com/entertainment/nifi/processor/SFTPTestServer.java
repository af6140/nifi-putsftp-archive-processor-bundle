package com.entertainment.nifi.processor;


import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;

import org.apache.sshd.common.file.nativefs.NativeFileSystemFactory;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.ValidateUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.UserAuthPassword;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystem;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import javax.swing.filechooser.FileSystemView;

public class SFTPTestServer {
    SshServer sshServer = null;
    public SFTPTestServer(int port, String vfsPath) {
        System.out.println("Starging sftp server on port " + port + " with root "+ vfsPath);
        File SERVER_ROOT = new File(vfsPath);
        SERVER_ROOT.mkdirs();
        sshServer = SshServer.setUpDefaultServer();
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(SERVER_ROOT.toPath()) {
            @Override
            public Path getUserHomeDir(String userName) {
                Path userHome=SERVER_ROOT.toPath().toAbsolutePath().resolve(userName);
                userHome.toFile().mkdir();
                System.out.println("User home: "+userHome);
                return userHome;
            }

        });
        sshServer.setPort(port);
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshServer.setCommandFactory(new ScpCommandFactory());
        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>();
        userAuthFactories.add(new UserAuthPasswordFactory());
        sshServer.setUserAuthFactories(userAuthFactories);
        sshServer.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession session) {
                return true;
            }
        });

        List<NamedFactory<Command>> namedFactoryList = new ArrayList<>();
        namedFactoryList.add(new SftpSubsystemFactory());
        sshServer.setSubsystemFactories(namedFactoryList);
        try {
            sshServer.start();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    public void stop() {
        System.out.println("Stopping test sftp server");
        try {
            this.sshServer.stop();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
