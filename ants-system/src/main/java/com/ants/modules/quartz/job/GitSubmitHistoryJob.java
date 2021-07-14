package com.ants.modules.quartz.job;

import com.ants.common.exception.AntsException;
import com.ants.common.utils.DateUtils;
import com.ants.modules.system.entity.GitSubmitHistory;
import com.ants.modules.system.service.GitSubmitHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.*;

/**
 * TODO   获取git代码提交记录
 * Author Chen
 * Date   2021/4/2 9:30
 */
@Slf4j
public class GitSubmitHistoryJob implements Job {

    private Git git;                        // git对象
    @Value("${ants.git.username}")
    private String userName;                // 资源库账户
    @Value("${ants.git.password}")
    private String pwd;                     // 密码
    @Value("${ants.git.giturl}")
    private String giturl;                  // git地址
    @Value("${ants.git.pjName}")
    private String pjName;                  // 项目名
    @Value("${ants.git.branch}")
    private String branch;                  // 分支名
    @Value("${ants.git.localPath}")
    private String localPath;               // 项目本地克隆地址
    @Autowired
    GitSubmitHistoryService gitSubmitHistoryService;

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            // 初始化Git
            initGit();
            // 获取版本号
            ArrayList<HashMap<String, Object>> gitVersion = getGitVersion();
            // 根据版本号获取Git提交记录
            for (HashMap<String, Object> map : gitVersion) {
                HashMap<String, Object> commitLogList = getCommitLogList(map.get("version") + "");
                log.info(commitLogList.get("commitPerson") + "   "
                        + commitLogList.get("message") + "   "
                        + commitLogList.get("commitDate"));
                GitSubmitHistory gitSubmitHistory = new GitSubmitHistory();
                QueryWrapper<GitSubmitHistory> queryWrapper = new QueryWrapper<GitSubmitHistory>();
                queryWrapper.eq("version", map.get("version") + "");
                GitSubmitHistory one = gitSubmitHistoryService.getOne(queryWrapper);
                if (one != null) {
                    gitSubmitHistory.setVersion(map.get("version") + "");
                    gitSubmitHistory.setCommitPerson(commitLogList.get("commitPerson") + "");
                    gitSubmitHistory.setCommitDate((Date) commitLogList.get("commitDate"));
                    gitSubmitHistory.setMessage(commitLogList.get("message") + "");
                    try {
                        gitSubmitHistoryService.save(gitSubmitHistory);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(String.format("welcome %s! Ants 获取git代码提交记录 !   时间:" + DateUtils.now(), this.parameter));
    }


    /**
     * @方法简介:初始化一个GIT对象
     */
    public void initGit() throws Exception {
        // 如果目录存在就先更新后open
        if (new File(localPath + File.separator + this.pjName).exists()) {
            this.git = Git.open(new File(localPath + File.separator + this.pjName));
            FetchResult resule = this.git.fetch()   // 如果已经存在该库，就使用fetch刷新一下
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(this.userName, this.pwd))   //远程登录git的凭证
                    .setCheckFetchedObjects(true)
                    .call();
            if (resule == null) {
                throw new AntsException("获取的Git对象已失效");
            }
        } else {
            // 如果不存在就clone
            this.git = Git.cloneRepository()
                    .setURI(this.giturl)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(this.userName, this.pwd))
                    .setBranch(branch)
                    .setDirectory(new File(localPath + File.separator + this.pjName))    //指定本地clone库
                    .call();
        }
    }

    /**
     * @方法简介: 获取所有的版本号与版本号对应的时间戳
     */
    public ArrayList<HashMap<String, Object>> getGitVersion()
            throws GitAPIException {
        Iterable<RevCommit> logIterable = this.git.log().call();
        Iterator<RevCommit> logIterator = logIterable.iterator(); //获取所有版本号的迭代器

        if (logIterator == null) {
            throw new AntsException("该项目暂无日志记录");
        }
        int row = 0;
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        while (logIterator.hasNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            RevCommit commit = logIterator.next();
            Date commitDate = commit.getAuthorIdent().getWhen();     //提交时间
            String commitPerson = commit.getAuthorIdent().getName();    //提交人
            String commitID = commit.getName();    //提交的版本号（之后根据这个版本号去获取对应的详情记录）
            map.put("version", commitID);
            map.put("commitDate", commitDate);
            map.put("commitPerson", commitPerson);
            list.add(row, map);
            row++;
        }
        return list;
    }

    /**
     * @方法简介 :根据指定版本号获取版本号下面的详情记录
     */
    @SuppressWarnings("resource")
    public HashMap<String, Object> getCommitLogList(String revision) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 通过git获取项目库
        Repository repository = this.git.getRepository();
        // 根据所需要查询的版本号查新ObjectId
        ObjectId objId = repository.resolve(revision);

        //根据版本号获取指定的详细记录
        Iterable<RevCommit> allCommitsLater = git.log().add(objId).call();
        if (allCommitsLater == null) {
            throw new Exception("该提交版本下无当前查询用户的提交记录");
        }
        Iterator<RevCommit> iter = allCommitsLater.iterator();
        RevCommit commit = iter.next();//提交对象
        Date commitDate = commit.getAuthorIdent().getWhen();//提交时间
        map.put("commitDate", commitDate);
        String commitPerson = commit.getAuthorIdent().getName();//提交人
        map.put("commitPerson", commitPerson);
        String message = commit.getFullMessage();//提交日志
        map.put("message", message);

        // 给存储库创建一个树的遍历器
        TreeWalk tw = new TreeWalk(repository);
        // 将当前commit的树放入树的遍历器中
        tw.addTree(commit.getTree());

        commit = iter.next();
        if (commit != null) {
            tw.addTree(commit.getTree());
        } else {
            return null;
        }

        tw.setRecursive(true);
        RenameDetector rd = new RenameDetector(repository);
        rd.addAll(DiffEntry.scan(tw));
        //获取到详细记录结果集    在这里就能获取到一个版本号对应的所有提交记录（详情！！！）
        List<DiffEntry> list = rd.compute();

        map.put("list", list);
        return map;
    }
}
