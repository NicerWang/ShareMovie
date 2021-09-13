package automate;

import nk.bigdata.backend.domain.Movie;
import nk.bigdata.backend.domain.Task;
import nk.bigdata.backend.utils.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;


public class ServiceHandler extends Thread {
    private final Socket socket;
    String dataBaseAddr;
    String username;
    String pwd;

    ServiceHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        InetAddress inetAddress = socket.getInetAddress();

        long id = Thread.currentThread().getId();

        System.out.print("[" + id + "]");
        System.out.println("[=============Task Begin=============]");
        System.out.print("[" + id + "]");
        System.out.println("[1]==> Established new connection from " + inetAddress.getHostAddress());

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Task task = null;
        Config config = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("[" + id + "]");
            System.out.println("[Fatal Error]==> Fail to open stream, exited");
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }
        System.out.print("[" + id + "]");
        System.out.println("[1]==> [OK]");
        System.out.print("[" + id + "]");
        System.out.println("[2]==> Fetch task info");

        try {
            config = (Config) ois.readObject();
            task = (Task) ois.readObject();
            System.out.print("[" + id + "]");
            System.out.println(config);
            System.out.print("[" + id + "]");
            System.out.println(task);
            dataBaseAddr = config.getDataBaseAddr();
            username = config.getUsername();
            pwd = config.getPassword();
        } catch (Exception e) {
            System.out.print("[" + id + "]");
            System.out.println("[2]==> [Fatal Error] Fail to fetch task info, exited");
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }
        System.out.print("[" + id + "]");
        System.out.println("[2]==> [OK]");

        String movieId = task.getId();
        String spiderRes = null;
        String[] spiderStatus = null;
        System.out.print("[" + id + "]");
        System.out.println("[3]==> Run spider");
        System.out.print("[" + id + "]");
        System.out.println("[3-1]====> Run spider for movie info");

        if (task.getType().equals("include")) {

            spiderRes = CommandUtil.run("python3 ./python/movieInfo.py " + movieId);

            spiderStatus = spiderRes.split("\\^");

            task.setLog(spiderRes);

            if (spiderStatus[0].equals("Fail")) {
                try {
                    System.out.print("[" + id + "]");
                    System.out.println("[3-1]====> [Fatal Error] Spider error, exited");
                    oos.writeObject(new Movie());
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    handleException(task);
                    cleanup(movieId);
                    return;
                }
            }
            System.out.print("[" + id + "]");
            System.out.println("[3-1]====> [OK]");

            System.out.print("[" + id + "]");
            System.out.println("[3-2]====> Parse movie info");
            Movie movie = MovieParser.parseMovie(movieId);
            try {
                oos.writeObject(movie);
                socket.close();
            } catch (IOException e) {
                System.out.print("[" + id + "]");
                System.out.println("[3-2]====> [Fatal Error] Movie parser, exited");
                handleException(task);
                cleanup(movieId);
                e.printStackTrace();
                return;
            }
            System.out.print("[" + id + "]");
            System.out.println("[3-2]====> [OK]");
        }

        System.out.print("[" + id + "]");
        System.out.println("[3-3]====> Run spider for comments");

        if (task.getType().equals("include")) {
            System.out.println("[include]======>");
            spiderRes = CommandUtil.run("python3 ./python/comment.py " + movieId);
        } else {
            System.out.println("[update]======>");
            spiderRes = CommandUtil.run("python3 ./python/comment.py " + movieId + " " + task.getStart().getTime()/1000);
        }

        spiderStatus = spiderRes.split("\\^");

        task.setLog(task.getLog() + spiderRes);

        if (spiderStatus[0].equals("Fail")) {
            try {
                System.out.print("[" + id + "]");
                System.out.println("[3-3]====> [Fatal Error] Spider error, exited");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                handleException(task);
                cleanup(movieId);
                return;
            }
        }
        System.out.print("[" + id + "]");
        System.out.println("[3-3]==> [OK]");
        System.out.print("[" + id + "]");
        System.out.println("[4]==> Copy data to hdfs");
        System.out.print("[" + id + "]");
        System.out.println("[4-1]====> Create hdfs floders");
        String mkdir1 = "hdfs dfs -mkdir /input/" + movieId;
        String mkdir2 = "hdfs dfs -mkdir /input/" + movieId + "_content";
        System.out.println(mkdir1);
        CommandUtil.run(mkdir1);
        System.out.println(mkdir2);
        CommandUtil.run(mkdir2);
        System.out.print("[" + id + "]");
        System.out.println("[4-1]====> [OK]");
        System.out.print("[" + id + "]");
        System.out.println("[4-2]====> Move data");
        String mv1 = "hdfs dfs -put ./" + movieId + "/comments.csv" + " /input/" + movieId;
        String mv2 = "hdfs dfs -put ./" + movieId + "/content.txt" + " /input/" + movieId + "_content/";
        System.out.println(mv2);
        CommandUtil.run(mv2);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(mv1);
        CommandUtil.run(mv1);
        System.out.print("[" + id + "]");
        System.out.println("[4-2]====> [OK]");

        System.out.print("[" + id + "]");
        System.out.println("[5]==> Strat mapreduce");

        String mapReduceCmd1 = null;
        String mapReduceCmd2 = "hadoop jar ./wordcloud.jar " + movieId + " " + dataBaseAddr + " " + username + " " + pwd;

        if (task.getType().equals("include")) {
            mapReduceCmd1 = "hadoop jar ./purifydata.jar " + movieId + " " + dataBaseAddr + " " + username + " " + pwd;
        } else {
            mapReduceCmd1 = "hadoop jar ./purifydata.jar " + movieId + " " + dataBaseAddr + " " + username + " " + pwd + " " + spiderStatus[1];
        }

        System.out.println(mapReduceCmd1);
        CommandUtil.run(mapReduceCmd1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(mapReduceCmd2);
        CommandUtil.run(mapReduceCmd2);

        System.out.print("[" + id + "]");
        System.out.println("[5]==> [OK]");

        System.out.print("[" + id + "]");
        System.out.println("[6]==> cleaning up");
        cleanup(movieId);
        task.setStatus("Success");
        UpdateTaskInfo updateTaskInfo = new UpdateTaskInfo();
        updateTaskInfo.update(task);

        System.out.print("[" + id + "]");
        System.out.println("[6]==> [OK]");

        System.out.print("[" + id + "]");
        System.out.println("[=============Task Finished for " + movieId + " =============]");

    }
    void cleanup(String movieId){
        String rm1 = "mv " + movieId + " archive/" + movieId + "_" + new Date().getTime();
        String rm2 = "hdfs dfs -rm -r /input/" + movieId;
        String rm3 = "hdfs dfs -rm -r /input/" + movieId + "_content";
        String rm4 = "hdfs dfs -rm -r /output/" + movieId;
        CommandUtil.run(rm1);
        CommandUtil.run(rm2);
        CommandUtil.run(rm3);
        CommandUtil.run(rm4);
    }
    void handleException(Task task){
        UpdateTaskInfo updateTaskInfo = new UpdateTaskInfo();
        task.setStatus("Failed");
        updateTaskInfo.update(task);
    }
}

