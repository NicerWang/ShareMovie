package nk.bigdata.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config implements Serializable {
    private static final long serialVersionUID = 1;
    public String dataBaseAddr;
    public String username;
    public String password;
    public String tableName;

    {
        Properties properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBaseAddr = properties.getProperty("dataBaseAddr");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        tableName = properties.getProperty("tableName");
    }
}
