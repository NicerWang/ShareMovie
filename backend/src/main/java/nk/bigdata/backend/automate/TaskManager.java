package nk.bigdata.backend.automate;

import nk.bigdata.backend.domain.Movie;
import nk.bigdata.backend.domain.Task;
import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.service.impl.MovieServiceImpl;
import nk.bigdata.backend.utils.Config;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class TaskManager {

    private static final String host;
    private static final Integer port;

    static {
        Properties properties = new Properties();
        try {
            properties.load(TaskManager.class.getClassLoader().getResourceAsStream("automate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = properties.getProperty("host");
        port = Integer.parseInt(properties.getProperty("port"));
    }

    public static void submitTask(Task task) {
        new Thread(new SingleTask(task)).start();
    }

    static class SingleTask implements Runnable {
        private final Task task;
        private final MovieService movieService = new MovieServiceImpl();

        public SingleTask(Task task) {
            this.task = task;
        }

        public void proc() throws Exception {
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(new Config());
            oos.writeObject(task);
            if (task.getType().equals("include")) {
                Movie movie = (Movie) ois.readObject();
                if (movie.getId() != null && !movie.getId().equals("")) {
                    movieService.add(movie);
                }
            }
            oos.close();
            ois.close();
            socket.close();
        }

        @Override
        public void run() {
            try {
                proc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
