package net.lab1024.sa.admin.module.freeserver.renew.conmmon;

import com.alibaba.fastjson2.JSONObject;
import net.lab1024.sa.admin.module.freeserver.renew.constant.CloudInfo;
import net.lab1024.sa.admin.module.freeserver.renew.constant.Constans;
import net.lab1024.sa.admin.module.freeserver.renew.constant.Profile;
import net.lab1024.sa.admin.module.freeserver.renew.constant.ResourcePath;
import net.lab1024.sa.admin.util.FileUtil;
import net.lab1024.sa.admin.util.StringUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.jgit.api.Git.cloneRepository;

/**
 * @author Demo-Liu
 * @create 2020-07-30 10:18
 * @description 操作 git 发布博客
 */
public class BlogGit {

    private static final int DELETE = 1;
    private static final int ADD = 2;

    /**
     * 发布博客
     */
    public static String sendCustomBlogByType(String type) throws IOException, GitAPIException {

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-");
        String nowStr = sdf.format(new Date());

        int i = (int) (Math.random() * 9000000);

        String fileName = sb.append(nowStr)
                .append(Profile.BLOG_USERNAME)
                .append("_")
                .append(i)
                .append(".md")
                .toString();

        Git git;
        if (FileUtil.exist(Profile.BLOG_LOCAL_PATH + "/" + Constans.GIT_INFO_DIR)) {
            git = Git.open(new File(Profile.BLOG_LOCAL_PATH));
        } else {
            git = gitClone();
        }

        String newBlogFilePath = Profile.BLOG_LOCAL_PATH + "/" + Constans.BLOG_ROOT_DIR + "/" + fileName;
        File newBlogFile = new File(newBlogFilePath);
        FileUtil.copyResourceToFile(ResourcePath.MD_TEMPLATE_PATH, newBlogFile);

        Map<String, String> map = new HashMap<>();
        CloudInfo cloudInfo = CloudInfo.getCloudInfo(type);
        map.put(Constans.MD_CLOUD_NAME, cloudInfo.getCloudName());
        map.put(Constans.MD_CLOUD_OS, cloudInfo.getOs());

        FileUtil.replaceFileStr(newBlogFile, map);

        gitPush(git, Constans.BLOG_ROOT_DIR + "/" + fileName, ADD);

        sb.setLength(0);
        return sb.append(Profile.BLOG_URL)
                .append("/")
                .append(nowStr.replace("-", "/"))
                .append(Profile.BLOG_USERNAME)
                .append("_")
                .append(i)
                .append(".html")
                .toString();
    }

    /**
     * 从配置地址新 clone 一个仓库
     */
    public static Git gitClone() throws GitAPIException {
        return cloneRepository()
                .setURI(Profile.BLOG_URI)
                .setDirectory(new File(Profile.BLOG_LOCAL_PATH))
                .setCloneAllBranches(true)
                .setTransportConfigCallback(new MyTransportConfigCallback())
                .call();
    }

    /**
     * 提交文件
     */
    public static void gitPush(Git git, String rootBlogPath, int type) throws GitAPIException {
        if (type == DELETE) {
            git.rm().addFilepattern(rootBlogPath).call();
        } else {
            git.add().addFilepattern(rootBlogPath).call();
        }

        git.commit()
                .setMessage(Profile.BLOG_USERNAME)
                .call();

        git.push()
                .setRemote("origin")
                .add("master")
                .setTransportConfigCallback(new MyTransportConfigCallback())
                .call();
    }

    /**
     * 删除博客
     */
    public static void deleteBlog(String blogUrl) throws IOException, GitAPIException {
        String fileName = StringUtil.subByLastIndex(blogUrl, "/", 3)
                .substring(1)
                .replace("/", "-");
        fileName = fileName.substring(0, fileName.length() - 4) + "md";

        String filePath = Constans.BLOG_ROOT_DIR + "/" + fileName;

        Git git = Git.open(new File(Profile.BLOG_LOCAL_PATH));
        gitPush(git, filePath, DELETE);
    }

    /**
     * JGit 7.5 SSH 配置
     */
    private static class MyTransportConfigCallback implements TransportConfigCallback {

        private final SshdSessionFactory sshSessionFactory;

        private MyTransportConfigCallback() {
            this.sshSessionFactory = new SshdSessionFactoryBuilder()
                    .setHomeDirectory(new File(System.getProperty("user.home")))
                    .setSshDirectory(new File(System.getProperty("user.home"), ".ssh"))
                    .build(null);
        }

        @Override
        public void configure(Transport transport) {
            if (transport instanceof SshTransport sshTransport) {
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }
        }
    }

    public static void main(String[] args) {
        JSONObject statusJson = JSONObject.parseObject("{\"msg\":\"您尚未登录！\",\"response\":\"50140\"}");
        String msg = statusJson.getString("msg");
        System.out.println(msg);

    }
}